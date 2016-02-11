/**
 * 
 * Copyright 2012 AT Computing BV
 *
 * This file is part of Linux Reference Card.
 *
 * Linux Reference Card is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Linux Reference Card is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Linux Reference Card.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package nl.atcomputing.refcard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.atcomputing.refcard.R;

/**
 * TODO fix highlighting http://stackoverflow.com/questions/10318642/highlight-for-selected-item-in-expandable-list
 */
public class RegExpFragment extends Fragment {
	private String[] regexpb;
	private String[] regexpe;
	private String[] regexpp;
	
	public static String getName() {
		return "Regular Expressions";
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

		// obtain the basic and extended regexp_array
		regexpb = getResources().getStringArray(R.array.regexpb_array);
		regexpe = getResources().getStringArray(R.array.regexpe_array);
		regexpp = getResources().getStringArray(R.array.regexpp_array);

		// define and activate the expandable list adapter
		SimpleExpandableListAdapter expListAdapter =
				new SimpleExpandableListAdapter(
						getActivity(),
						createGroupList(),
						R.layout.regroups,
						new String[] {"reCategory"},
						new int[]    {R.id.recategory},
						createChildList(),
						R.layout.rerow,
						new String[] {"resym", "redesc"},
						new int[]    {R.id.resymbol, R.id.retext}
						);

		lv.setAdapter(expListAdapter);
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
