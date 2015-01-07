package nl.atcomputing.refcard.recyclerview;

import java.util.List;
import java.util.Map;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    private SparseBooleanArray rowExpanded;

    public interface OnItemClickListener {
        public void onItemClicked(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View[] views;
        public View[] expansionViews;
        public View expansionView;
        public View rowView;

        public ViewHolder(View v, int[] to, OnClickListener listener) {
            super(v);
            rowView = v;

            v.setOnClickListener(listener);
            views = new View[to.length];
            for(int i = 0; i < to.length; i++) {
                views[i] = v.findViewById(to[i]);
            }
        }

        /**
         * Sets the view that should be expanded when an item is clicked
         * @param to resource identifiers in view v
         */
        public void setExpansion(int resource, int[] to) {
            expansionView = rowView.findViewById(resource);
            expansionView.setVisibility(View.GONE);
            expansionViews = new View[to.length];
            for(int i = 0; i < to.length; i++) {
                expansionViews[i] = rowView.findViewById(to[i]);
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
    public ExpandableMapAdapter(List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.data = data;
        this.rowExpanded = new SparseBooleanArray(data.size());
    }

    /**
     * Sets the data elements that should be shown when item is clicked
     * @param data
     * @param resource identifier pointing to the ViewGroup in the layout set using {@link #ExpandableMapAdapter(List, int, String[], int[])}
     * where the expansion items should be added to
     * @param from array holding the keys as used in the map. Make sure the order of keys correspond with the order of the resource identifiers in <code>int[] to</code>
     * @param to array holding the resource identifiers in the layout given by <code>layout</code>
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
        ViewHolder vh = new ViewHolder(v, to, this);

        vh.setExpansion(this.expansionResource, expansionTo);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, ?> item = this.data.get(position);
        for(int i = 0; i < from.length; i++) {
            View v = vh.views[i];
            if( v instanceof TextView ) {
                ((TextView) v).setText(item.get(from[i]).toString());
            }
        }

        fillContentView(vh, position);

        if( rowExpanded.get(position, false) ) {
            ((ViewGroup.MarginLayoutParams) vh.expansionView.getLayoutParams()).bottomMargin = 0;
            vh.expansionView.setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup.MarginLayoutParams) vh.expansionView.getLayoutParams()).bottomMargin = -300;
            vh.expansionView.setVisibility(View.GONE);
        }

        vh.itemView.setTag(vh);
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

        boolean expand = ! rowExpanded.get(pos, false);

        this.rowExpanded.put(pos, expand);

        ExpandAnimation expandAni = new ExpandAnimation(vh.expansionView, 500, expand);
        vh.expansionView.startAnimation(expandAni);
    }

    private void fillContentView(ViewHolder vh, int pos) {
        if( this.expansionData != null ) {
            Map<String, ?> expansionItems = this.expansionData.get(pos);
            for(int i = 0; i < this.expansionFrom.length; i++) {
                View ev = vh.expansionViews[i];
                if( ev instanceof TextView ) {
                    ((TextView) ev).setText((String) expansionItems.get(this.expansionFrom[i]));
                }
            }
        }
    }
}
