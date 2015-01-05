package nl.atcomputing.refcard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.recyclerview.DividerItemDecoration;
import nl.atcomputing.refcard.recyclerview.ExpandableMapAdapter;
import nl.atcomputing.refcard.recyclerview.ExpandableMapAdapter.OnItemClickListener;

public class CommandFragment extends Fragment {

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

		ExpandableMapAdapter adapter = new ExpandableMapAdapter(mylist, R.layout.cmdrow,
				new String[] {"cmdname", "cmddesc"},
				new int[]    {R.id.cmdname, R.id.cmddesc});
		adapter.setExpansion(mylist, R.id.expansion,
				new String[] {"cmdsynops", "cmdlongdesc"},
				new int[] {R.id.synopsis, R.id.ldescription});

       recyclerView.setAdapter(adapter);
		
		return recyclerView;
	}
}
