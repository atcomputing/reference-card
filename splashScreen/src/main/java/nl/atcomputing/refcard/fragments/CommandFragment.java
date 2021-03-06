/**
 * 
 * Copyright 2014 AT Computing BV
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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.recyclerview.DividerItemDecoration;
import nl.atcomputing.refcard.recyclerview.ExpandableMapAdapter;
import nl.atcomputing.refcard.utils.SparseBooleanArrayParcelable;

public class CommandFragment extends Fragment {
	private ExpandableMapAdapter mAdapter;

	public static String getName() {
		return "Command Reference";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycleview, container, false);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

		// obtain the commands_array
		String[] cmdall = getResources().getStringArray(R.array.commands_array);

		// prepare an array list to map the command names with their descriptions
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		for (int i=0, nel=cmdall.length; i < nel; i++) {
			String[] cmdTab = cmdall[i].split("!");

			HashMap<String, String> map = new HashMap<String, String>();

			String cmdname = cmdTab[0];
			String cmddesc = cmdTab[1];
			map.put("cmdname", cmdname);
			map.put("cmddesc", cmddesc);
			
			int nparts          = cmdTab.length;
			String synops = nparts >= 3 ? cmdTab[2] : "";
			String ldescr = nparts >= 4 ? cmdTab[3] : null;
			map.put("cmdsynops", cmdname + " " +synops);
			map.put("cmdlongdesc", ldescr);
			
			mylist.add(map);
		}

		mAdapter = new ExpandableMapAdapter(getActivity(), mylist, R.layout.cmdrow,
				new String[] {"cmdname", "cmddesc"},
				new int[]    {R.id.cmdname, R.id.cmddesc});
        mAdapter.setExpansion(mylist, R.id.expansion,
				new String[] {"cmdsynops", "cmdlongdesc"},
				new int[] {R.id.synopsis, R.id.ldescription});

        if( savedInstanceState != null ) {
            SparseBooleanArray sbArray = (SparseBooleanArray) savedInstanceState.getParcelable("key_rowsexpanded");
            if( sbArray != null ) {
                mAdapter.setRowsExpanded(sbArray);
            }
        }

       recyclerView.setAdapter(mAdapter);
		
		return recyclerView;
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("key_rowsexpanded", new SparseBooleanArrayParcelable(mAdapter.getRowsExpanded()));
    }
}
