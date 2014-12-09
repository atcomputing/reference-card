package nl.atcomputing.refcard.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.recyclerview.DividerItemDecoration;
import nl.atcomputing.refcard.recyclerview.ExpandableMapAdapter;
import nl.atcomputing.refcard.recyclerview.ExpandableMapAdapter.OnItemClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommandFragment extends Fragment implements OnItemClickListener {
	private String[] cmdall;
	private int mCurCheckPosition = 0;
	private ExpandableMapAdapter<String> adapter;
	
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
		cmdall = getResources().getStringArray(R.array.commands_array);

		// prepare an array list to map the command names with their descriptions
		ArrayList<HashMap<String, Spanned>> mylist = new ArrayList<HashMap<String, Spanned>>();
		ArrayList<HashMap<String, String>> mySynopsislist = new ArrayList<HashMap<String, String>>();
		
		for (int i=0, nel=cmdall.length; i < nel; i++) {
			String[] cmdTab = cmdall[i].split("!");

			HashMap<String, Spanned> map = new HashMap<String, Spanned>();

			map.put("cmdname", Html.fromHtml(cmdTab[0]));
			map.put("cmddesc", Html.fromHtml(cmdTab[1]));
			mylist.add(map);
			
			int nparts          = cmdTab.length;

			HashMap<String, String> synopsisMap = new HashMap<String, String>();
			String synops = nparts >= 3 ? cmdTab[2] : "";
			String ldescr = nparts >= 4 ? cmdTab[3] : null;
			
			synopsisMap.put("cmdsynops", synops);
			synopsisMap.put("cmdlongdesc", ldescr);
			mySynopsislist.add(synopsisMap);
		}

		this.adapter = new ExpandableMapAdapter<String>(mylist, R.layout.cmdrow,
				new String[] {"cmdname", "cmddesc"},
				new int[]    {R.id.cmdname, R.id.cmddesc}, this);
		this.adapter.setExpansion(mySynopsislist, R.id.expansion, 
				new String[] {"cmdsynops", "cmdlongdesc"}, 
				new int[] {R.id.synopsis, R.id.ldescription});
		recyclerView.setAdapter(adapter);
		
		return recyclerView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onItemClicked(View v, int position) {

	}
}
