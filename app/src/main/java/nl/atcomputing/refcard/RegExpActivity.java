package nl.atcomputing.refcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.SimpleExpandableListAdapter;

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
  	  	
  	  	// Prevent setting background to black when scrolling
  	  	getExpandableListView().setCacheColorHint(0x00000000);
    }
    
    /**
	 * Added to fix ClassCastException bug in some android versions
	 * as reported by users
	 */
    @Override
    protected void onRestoreInstanceState(Bundle state) {
    	
    }
    
	// Create the group list (first level menu items) for the basic
    // and extended regular expressions
	private List<HashMap<String, String>> createGroupList() {
		  ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		  HashMap<String, String> map;

		  map = new HashMap<String, String>();
		  map.put("reCategory", getResources().getString(R.string.re_basic));
		  result.add(map);
		  
		  map = new HashMap<String, String>();
		  map.put("reCategory", getResources().getString(R.string.re_extended));
		  result.add(map);

		  map = new HashMap<String, String>();
		  map.put("reCategory", getResources().getString(R.string.re_perl));
		  result.add(map);
		  
		  return (List<HashMap<String, String>>) result;
	}

	// Creates the child list (second level lists) for the basic and
	// extended regular expressions
	private List<ArrayList<HashMap<String, String>>> createChildList() {
		ArrayList<ArrayList<HashMap<String, String>>> result = 
				new ArrayList<ArrayList<HashMap<String, String>>>();

	    addItemsToMap(result, regexpb);
	    addItemsToMap(result, regexpe);
	    addItemsToMap(result, regexpp);
	    
		return result;
	}
	
	private void addItemsToMap(ArrayList<ArrayList<HashMap<String, String>>> mylist, String[] strTab) {
		  ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
		  HashMap<String, String> map;

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
