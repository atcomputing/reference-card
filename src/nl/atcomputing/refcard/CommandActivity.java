package nl.atcomputing.refcard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CommandActivity extends ListActivity implements OnItemClickListener {
	private String[] cmdall;
	
    /* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  // obtain the commands_array
	  cmdall = getResources().getStringArray(R.array.commands_array);

	  // prepare an array list to map the command names with their descriptions
      ArrayList<HashMap<String, Spanned>> mylist = new ArrayList<HashMap<String, Spanned>>();
      HashMap<String, Spanned> map;

      for (int i=0, nel=cmdall.length; i < nel; i++) {
    	  	String[] cmdTab = cmdall[i].split("!");

	  		map = new HashMap<String, Spanned>();

	  		map.put("cmdname", Html.fromHtml(cmdTab[0]));
	  		map.put("cmddesc", Html.fromHtml(cmdTab[1]));
	  		mylist.add(map);
	  	}
      
	  // Create a list view and attach an adapter to bind the mapped values
	  ListView lv = getListView();

      SimpleAdapter cmdlist = new SimpleAdapter(this, mylist, R.layout.cmdrow,
                  new String[] {"cmdname", "cmddesc"},
                  new int[]    {R.id.cmdname, R.id.cmddesc});

      lv.setAdapter(cmdlist);

      // define a filter to select the proper list items when the user starts typing
	  lv.setTextFilterEnabled(false);
	  
	  // define a ClickListener to react on a list selection
	  lv.setOnItemClickListener(this);
	}

	// React on a list selection by the user
	// The index of the original selection list is given
  	public void onItemClick(AdapterView<?> parent, View v, int index, long id) {
  		// Split the command specification data of the selected item
  		// consisting of:
  		//		command name
  		//		command description
  		//		synopsis (optional)
  		//		flag description (optional)
	    String[] cmdpart    = cmdall[index].split("!");
	    int nparts          = cmdpart.length;
	    
	    CharSequence sdescr = cmdpart[1];
	    CharSequence synops = nparts >= 3 ? cmdpart[2] : "";
	    CharSequence ldescr = nparts >= 4 ? cmdpart[3] : null;
	    
	    showSynopsis(R.string.flag_title, cmdpart[0], sdescr, synops, ldescr);
	}
	
	// Show a dialogue box to display the details of the command
	private void showSynopsis(int title, CharSequence cmd, CharSequence sdesc, CharSequence synop, CharSequence ldesc) {
		LayoutInflater li = LayoutInflater.from(this);
		View view         = li.inflate(R.layout.cmddescr, null);
		TextView sd, sy, ld;

		// prepare the TextView for the short description
		sd = (TextView)view.findViewById(R.id.sdescription);
		sd.setText(sdesc);
		
		// prepare the TextView for the synopsis (optional)
		sy = (TextView)view.findViewById(R.id.synopsis);
		sy.setText(cmd + " " + synop);
		
		// prepare the TextView for the flag description (optional)
		if (ldesc != null) {
		    ld = (TextView)view.findViewById(R.id.ldescription);
		    ld.setText(ldesc);
		}

		// setup the dialogue box
		AlertDialog.Builder box = new AlertDialog.Builder(this);
		
		box.setIcon(R.drawable.at);		
		box.setTitle(getResources().getString(title) + " " + cmd);
		box.setView(view);
		
		box.setCancelable(false);
		box.setNeutralButton(R.string.confirm_ok, null);
		
		box.show();
	}
}