package nl.atcomputing.refcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.text.Html;

public class RegExpActivity extends ExpandableListActivity {
	private String[] regexpb;
	private String[] regexpe;
	private String[] regexpp;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

  	  	// obtain the basic and extended regexp_array
  	  	regexpb = getResources().getStringArray(R.array.regexpb_array);
  	  	regexpe = getResources().getStringArray(R.array.regexpe_array);
  	  	regexpp = getResources().getStringArray(R.array.regexpp_array);

  	  	// define and activate the expandable list adapter
  	  	SimpleExpandableListAdapter expListAdapter =
			  new SimpleExpandableListAdapter(
					this,
					createGroupList(),
					R.layout.regroups,
					new String[] {"reCategory"},
					new int[]    {R.id.recategory},
					createChildList(),
					R.layout.rerow,
					new String[] {"resym", "redesc"},
					new int[]    {R.id.resymbol, R.id.retext}
				);
  	  	
  	  	setListAdapter(expListAdapter);
    }
    
	// Create the group list (first level menu items) for the basic
    // and extended regular expressions
	private List createGroupList() {
		  ArrayList result = new ArrayList();
		  HashMap map;

		  map = new HashMap();
		  map.put("reCategory", getResources().getString(R.string.re_basic));
		  result.add(map);
		  
		  map = new HashMap();
		  map.put("reCategory", getResources().getString(R.string.re_extended));
		  result.add(map);

		  map = new HashMap();
		  map.put("reCategory", getResources().getString(R.string.re_perl));
		  result.add(map);
		  
		  return (List)result;
	}

	// Creates the child list (second level lists) for the basic and
	// extended regular expressions
	private List createChildList() {
		ArrayList result = new ArrayList();

	    addItemsToMap(result, regexpb);
	    addItemsToMap(result, regexpe);
	    addItemsToMap(result, regexpp);
	    
		return result;
	}
	
	private void addItemsToMap(ArrayList mylist, String[] strTab) {
		  ArrayList secList = new ArrayList();
		  HashMap map;

		  for (int i=0, nel=strTab.length; i < nel; i++) {
			  String[] reTab = strTab[i].split("@");
			  
			  map = new HashMap<String, String>();	        	
		  	  map.put("resym",  reTab[0]);
		  	  map.put("redesc", reTab[1]);
		  	  secList.add(map);
		   }
		   mylist.add(secList);
	}
}
