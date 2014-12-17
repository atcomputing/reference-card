package nl.atcomputing.refcard.recyclerview;

import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
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
	private int expansionLayout;
	private String[] expansionFrom;
	private int[] expansionTo;

	private SparseBooleanArray expanded;

	private OnItemClickListener listener;

	public interface OnItemClickListener {
		public void onItemClicked(View v, int position);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View[] views;
		public View[] expansionViews;
		public View expansionView;
		public ViewGroup expansionContainer;

		public ViewHolder(View v, int[] to, OnClickListener listener) {
			super(v);
			v.setOnClickListener(listener);
			views = new View[to.length];
			for(int i = 0; i < to.length; i++) {
				views[i] = v.findViewById(to[i]);
			}
		}

		/**
		 * Sets the view that should be expanded when an item is clicked
		 * @param v view that will hold the expanded info
		 * @param container in which the view v should be added
		 * @param to resource identifiers in view v
		 */
		public void setExpansion(View v, ViewGroup container, int[] to) {
			expansionView = v;
			expansionViews = new View[to.length];
			for(int i = 0; i < to.length; i++) {
				expansionViews[i] = v.findViewById(to[i]);
			}

			this.expansionContainer = container;
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
	 * @param resource identifier pointing to the ViewGroup in the layout set using {@link #ExpandableMapAdapter(List, int, String[], int[], OnItemClickListener)} 
	 * where the expansion layout should be added to.
	 * @param layout the expansion layout identifier that must be used to show the expansion information. This layout should hold views with identifiers as given in <code>int[] to</code>
	 * @param from array holding the keys as used in the map. Make sure the order of keys correspond with the order of the resource identifiers in <code>int[] to</code>
	 * @param to array holding the resource identifiers in the layout given by <code>layout</code>
	 */
	public void setExpansion(List<? extends Map<String, ?>> data, int resource, int layout, String[] from, int[] to) {
		this.expansionData = data;
		this.expansionResource = resource;
		this.expansionLayout = layout;
		this.expansionFrom = from;
		this.expansionTo = to;
	}

	@Override
	public ExpandableMapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(this.resource, parent, false);
		ViewHolder vh = new ViewHolder(v, to, this);

		if( this.expansionData != null ) {
			View vExpansion = v.findViewById(this.expansionResource);
			if( vExpansion instanceof ViewGroup ) {
				View ve = LayoutInflater.from(parent.getContext())
						.inflate(this.expansionLayout, (ViewGroup) vExpansion, false);
				vh.setExpansion(ve, (ViewGroup) vExpansion, expansionTo);
				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
					setLayoutTransition((ViewGroup) vExpansion);
				}
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
				((TextView) v).setText(item.get(from[i]).toString());
			}
		}

		if( this.expanded.get(position, false) ) {
			showSynopsis(holder, position);
		} else {
			hideSynopsis(holder);
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

		ViewHolder vh = (ViewHolder) v.getTag();
		int pos = vh.getPosition();

		if( this.expanded.get(pos, false) ) {
			this.expanded.put(pos, false);
			hideSynopsis(vh);
		} else {
			this.expanded.put(pos, true);
			showSynopsis(vh, pos);
		}

		if( this.listener != null ) {
			this.listener.onItemClicked(v, pos);
		}
	}

	private void showSynopsis(ViewHolder vh, int pos) {
		if( this.expansionData != null ) {
			Map<String, ?> expansionItems = this.expansionData.get(pos);
			for(int i = 0; i < this.expansionFrom.length; i++) {
				View ev = vh.expansionViews[i];
				if( ev instanceof TextView ) {
					((TextView) ev).setText((String) expansionItems.get(this.expansionFrom[i]));
				}
			}
		}

		vh.expansionContainer.addView(vh.expansionView);
	}

	private void hideSynopsis(ViewHolder vh) {
		vh.expansionContainer.removeView(vh.expansionView);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	private void setLayoutTransition(ViewGroup v) {
		LayoutTransition transition = new LayoutTransition();
		v.setLayoutTransition(transition);

		Animator scaleinAnim = ObjectAnimator.ofFloat(null, "scaleY", 0f, 1f);
		scaleinAnim.setDuration(100);
		transition.setAnimator(LayoutTransition.APPEARING, scaleinAnim);

		Animator scaleoutAnim = ObjectAnimator.ofFloat(null, "scaleY", 1f, 0f);
		scaleoutAnim.setDuration(100);
		transition.setAnimator(LayoutTransition.DISAPPEARING, scaleoutAnim);
	}
}
