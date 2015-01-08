package nl.atcomputing.refcard.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.atcomputing.refcard.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class ViFragment extends Fragment {
	private String[] visubcmdf;
	private String[] visubcmdi;
	private String[] visubcmde;
	private String[] visubcmdm;
	
	public static String getName() {
		return "Vim Reference";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.regexp_vi_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ExpandableListView lv = (ExpandableListView) getView().findViewById(R.id.listview);
		
		// obtain the arrays with the subcommands per category
		  visubcmdm = getResources().getStringArray(R.array.visubcomm_array);
		  visubcmdi = getResources().getStringArray(R.array.visubcomi_array);
		  visubcmde = getResources().getStringArray(R.array.visubcome_array);
		  visubcmdf = getResources().getStringArray(R.array.visubcomf_array);
		  
		  	// define and activate the expandable list adapter
		  SimpleExpandableListAdapter expListAdapter =
				  new SimpleExpandableListAdapter(
						getActivity(),
						createGroupList(),
						R.layout.vigroups,
						new String[] {"viCategory"},
						new int[]    {R.id.vicategory},
						createChildList(),
						R.layout.virow,
						new String[] {"viSubCom", "viSubText"},
						new int[]    {R.id.visubcom, R.id.visubtext}
					);
		  
		  lv.setAdapter(expListAdapter);
	}
	
	// Create the group list (first level menu items) for the four categories
    // of Vim subcommands
	private List<HashMap<String, String>> createGroupList() {
		  ArrayList<HashMap<String, String>> result = 
				  new ArrayList<HashMap<String, String>>();
		  HashMap<String, String> map;

		  map = new HashMap<String, String>();
		  map.put("viCategory", getResources().getString(R.string.vi_file));
		  result.add(map);
		  
		  map = new HashMap<String, String>();
		  map.put("viCategory", getResources().getString(R.string.vi_move));
		  result.add(map);
		  
		  map = new HashMap<String, String>();
		  map.put("viCategory", getResources().getString(R.string.vi_input));
		  result.add(map);
		  
		  map = new HashMap<String, String>();
		  map.put("viCategory", getResources().getString(R.string.vi_edit));
		  result.add(map);
		  
		  return (List<HashMap<String, String>>) result;
	}

	// Creates the child list (second level lists) for the categories
	// of Vim subcommands
	private List<ArrayList<HashMap<String, String>>> createChildList() {
		ArrayList<ArrayList<HashMap<String, String>>> result = 
				new ArrayList<ArrayList<HashMap<String, String>>>();

	    addItemsToMap(result, visubcmdf);
	    addItemsToMap(result, visubcmdm);
	    addItemsToMap(result, visubcmdi);
	    addItemsToMap(result, visubcmde);
	    
		return result;
	}
	
	private void addItemsToMap(ArrayList<ArrayList<HashMap<String, String>>> mylist, String[] strTab) {
		  ArrayList<HashMap<String, String>> secList = 
				  new ArrayList<HashMap<String, String>>();
		  HashMap<String, String> map;

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
