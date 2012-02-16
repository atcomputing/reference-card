package nl.atcomputing.refcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ViActivity extends ExpandableListActivity {
	private String[] visubcmdf;
	private String[] visubcmdi;
	private String[] visubcmde;
	private String[] visubcmdm;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  
	  // obtain the arrays with the subcommands per category
	  visubcmdm = getResources().getStringArray(R.array.visubcomm_array);
	  visubcmdi = getResources().getStringArray(R.array.visubcomi_array);
	  visubcmde = getResources().getStringArray(R.array.visubcome_array);
	  visubcmdf = getResources().getStringArray(R.array.visubcomf_array);
	  
	  	// define and activate the expandable list adapter
	  SimpleExpandableListAdapter expListAdapter =
			  new SimpleExpandableListAdapter(
					this,
					createGroupList(),
					R.layout.vigroups,
					new String[] {"viCategory"},
					new int[]    {R.id.vicategory},
					createChildList(),
					R.layout.virow,
					new String[] {"viSubCom", "viSubText"},
					new int[]    {R.id.visubcom, R.id.visubtext}
				);
	  
	  setListAdapter(expListAdapter);
    }
 
	// Create the group list (first level menu items) for the four categories
    // of Vim subcommands
	private List createGroupList() {
		  ArrayList result = new ArrayList();
		  HashMap map;

		  map = new HashMap();
		  map.put("viCategory", getResources().getString(R.string.vi_file));
		  result.add(map);
		  
		  map = new HashMap();
		  map.put("viCategory", getResources().getString(R.string.vi_move));
		  result.add(map);
		  
		  map = new HashMap();
		  map.put("viCategory", getResources().getString(R.string.vi_input));
		  result.add(map);
		  
		  map = new HashMap();
		  map.put("viCategory", getResources().getString(R.string.vi_edit));
		  result.add(map);
		  
		  return (List)result;
	}

	// Creates the child list (second level lists) for the categories
	// of Vim subcommands
	private List createChildList() {
		ArrayList result = new ArrayList();

	    addItemsToMap(result, visubcmdf);
	    addItemsToMap(result, visubcmdm);
	    addItemsToMap(result, visubcmdi);
	    addItemsToMap(result, visubcmde);
	    
		return result;
	}
	
	private void addItemsToMap(ArrayList mylist, String[] strTab) {
		  ArrayList secList = new ArrayList();
		  HashMap map;

		  for (int i=0, nel=strTab.length; i < nel; i++) {
			  String[] viTab = strTab[i].split("@");
			  
			  map = new HashMap<String, String>();	        	
		  	  map.put("viSubCom",  viTab[0]);
		  	  map.put("viSubText", viTab[1]);
		  	  secList.add(map);
		   }
		   mylist.add(secList);
	}
}