package nl.atcomputing.refcard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CommandFragment extends ListFragment implements OnItemClickListener {
	private String[] cmdall;
	int mCurCheckPosition = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


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

		// Prevent setting background to black when scrolling
		lv.setCacheColorHint(0x00000000);

		SimpleAdapter cmdlist = new SimpleAdapter(getActivity(), mylist, R.layout.cmdrow,
				new String[] {"cmdname", "cmddesc"},
				new int[]    {R.id.cmdname, R.id.cmddesc});

		lv.setAdapter(cmdlist);

		// define a filter to select the proper list items when the user starts typing
		lv.setTextFilterEnabled(false);

		// define a ClickListener to react on a list selection
		lv.setOnItemClickListener(this);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Split the command specification data of the selected item
		// consisting of:
		//		command name
		//		command description
		//		synopsis (optional)
		//		flag description (optional)
		
		mCurCheckPosition = position;
		getListView().setItemChecked(position, true);
		
		
		String[] cmdpart    = cmdall[position].split("!");
		int nparts          = cmdpart.length;

		CharSequence sdescr = cmdpart[1];
		CharSequence synops = nparts >= 3 ? cmdpart[2] : "";
		CharSequence ldescr = nparts >= 4 ? cmdpart[3] : null;

		showSynopsis(R.string.flag_title, cmdpart[0], sdescr, synops, ldescr);
	}

	private void showSynopsis(int title, CharSequence cmd, CharSequence sdesc, CharSequence synop, CharSequence ldesc) {
		LayoutInflater li = LayoutInflater.from(getActivity());
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
		AlertDialog.Builder box = new AlertDialog.Builder(getActivity());

		box.setIcon(R.drawable.at);		
		box.setTitle(cmd);
		box.setView(view);

		box.setCancelable(false);
		box.setNeutralButton(R.string.confirm_ok, null);

		box.show();
	}
}
