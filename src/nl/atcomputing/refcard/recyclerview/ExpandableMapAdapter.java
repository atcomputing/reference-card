package nl.atcomputing.refcard.recyclerview;

import java.util.List;
import java.util.Map;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExpandableMapAdapter<T> extends RecyclerView.Adapter<ExpandableMapAdapter.ViewHolder> implements OnClickListener {
	private int resource;
	private String[] from;
	private int[] to;
	private List<? extends Map<String, ?>> data;

	private List<? extends Map<String, ?>> expansionData;
	private int expansionResource;
	private String[] expansionFrom;
	private int[] expansionTo;

	private SparseBooleanArray expanded;
	
	private OnItemClickListener listener;

	public interface OnItemClickListener {
		public void onitemClicked(View v, int position);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View[] views;
		public View[] expansionViews;
		public View expansionView;
		public ViewHolder(View v, String[] from, int[] to, OnClickListener listener) {
			super(v);
			v.setOnClickListener(listener);
			views = new View[from.length];
			for(int i = 0; i < from.length; i++) {
				views[i] = v.findViewById(to[i]);
			}
		}

		public void setExpansion(View v, String[] from, int[] to) {
			expansionView = v;
			expansionViews = new View[from.length];
			for(int i = 0; i < from.length; i++) {
				expansionViews[i] = v.findViewById(to[i]);
			}
		}
	}

	/**
	 * Maps the data elements to the view elements. Currently only works for TextView and Spanned types.
	 * @param data list of map where each entry in the map should hold the keys given by <code>String[] from</code>
	 * @param resource layout resource identifier that holds View elements for each resource identifier as given by <code>int[] to</code>
	 * @param from array holding the keys as used in the map. Make sure the order of keys correspond with the order of the resource identifiers in <code>int[] to</code>
	 * @param to array holding the resource identifiers in the layout given by <code>int resource</code>
	 */
	public ExpandableMapAdapter(List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, OnItemClickListener listener) {
		this.resource = resource;
		this.from = from;
		this.to = to;
		this.data = data;
		this.listener = listener;
		this.expanded = new SparseBooleanArray();
	}

	/**
	 * Sets the data elements that should be shown when item is clicked
	 * @param data
	 * @param resource
	 */
	public void setExpansion(List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		this.expansionData = data;
		this.expansionResource = resource;
		this.expansionFrom = from;
		this.expansionTo = to;
	}

	@Override
	public ExpandableMapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(this.resource, parent, false);
		ViewHolder vh = new ViewHolder(v, from, to, this);

		if( this.expansionData != null ) {
			View vExpansion = LayoutInflater.from(parent.getContext())
					.inflate(this.expansionResource, parent, false);
			if( v instanceof ViewGroup ) {
				vh.setExpansion(vExpansion, expansionFrom, expansionTo);
				((ViewGroup) v).addView(vExpansion);
			}
		}
		
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Map<String, ?> item = this.data.get(position); 
		for(int i = 0; i < from.length; i++) {
			View v = holder.views[i];
			if( v instanceof TextView ) {
				((TextView) v).setText((Spanned) item.get(from[i]));
			}
		}
		
		if( this.expanded.get(position, false) ) {
			showSynopsis(holder, position);
			holder.expansionView.setVisibility(View.VISIBLE);
		} else {
			holder.expansionView.setVisibility(View.GONE);
		}
		
		holder.itemView.setTag(holder);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return data.size();
	}

	@Override
	public void onClick(View v) {
		ViewCompat.setElevation(v, 20);
		
		ViewHolder vh = (ViewHolder) v.getTag();
		int pos = vh.getPosition();
		
		if( this.expanded.get(pos, false) ) {
			this.expanded.put(pos, false);
			vh.expansionView.setVisibility(View.GONE);
		} else {
			this.expanded.put(pos, true);
			showSynopsis(vh, pos);
			vh.expansionView.setVisibility(View.VISIBLE);
		}
		
		if( this.listener != null ) {
			this.listener.onitemClicked(v, pos);
		}
	}
	
	private void showSynopsis(ViewHolder vh, int pos) {
		if( this.expansionData != null ) {
			Map<String, ?> expansionItems = this.expansionData.get(pos);
			for(int i = 0; i < from.length; i++) {
				View ev = vh.expansionViews[i];
				if( ev instanceof TextView ) {
					((TextView) ev).setText((String) expansionItems.get(this.expansionFrom[i]));
				}
			}
		}
	}
}
