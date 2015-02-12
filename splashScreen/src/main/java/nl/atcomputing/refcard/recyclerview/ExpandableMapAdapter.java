package nl.atcomputing.refcard.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import nl.atcomputing.refcard.R;

public class ExpandableMapAdapter<T> extends RecyclerView.Adapter<ExpandableMapAdapter.ViewHolder> implements OnClickListener {
    private int resource;
    private String[] from;
    private int[] to;
    private List<? extends Map<String, ?>> data;
    private float showElevation;

    private List<? extends Map<String, ?>> expansionData;
    private int expansionResource;
    private String[] expansionFrom;
    private int[] expansionTo;

    private int colorathighlight;
    private int coloratwhite;

    private SparseBooleanArray rowsExpanded;

    public interface OnItemClickListener {
        public void onItemClicked(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View[] views;
        public View[] expansionViews;
        public View expansionView;
        public View rowView;
        public CardView cardView;

        public ViewHolder(View v, int[] to, OnClickListener listener) {
            super(v);
            rowView = v;
            rowView.setTag(this);
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
            cardView = (CardView) rowView.findViewById(R.id.card_view);
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
    public ExpandableMapAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.data = data;
        this.rowsExpanded = new SparseBooleanArray(data.size());

        this.showElevation = context.getResources().getDimension(R.dimen.cardselectedElevation);

        this.colorathighlight = context.getResources().getColor(R.color.athighlight);
        this.coloratwhite = context.getResources().getColor(R.color.atwhite);
    }

    /**
     * Sets the data elements that should be shown when item is clicked
     * @param data
     * @param resource identifier pointing to the ViewGroup in the layout set using {@link #ExpandableMapAdapter(Context, List, int, String[], int[])}
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

    public void setRowsExpanded(SparseBooleanArray rowsExpanded) {
        this.rowsExpanded = rowsExpanded;
    }

    public SparseBooleanArray getRowsExpanded() {
        return rowsExpanded;
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

        if( rowsExpanded.get(position, false) ) {
            vh.expansionView.setVisibility(View.VISIBLE);
            vh.cardView.setCardElevation(this.showElevation);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //we highlight as elevation on older devices does not seem to work very well
                Drawable d = vh.cardView.getBackground();
                d.setColorFilter(this.colorathighlight, PorterDuff.Mode.MULTIPLY);
            }

        } else {
            vh.expansionView.setVisibility(View.GONE);
            vh.cardView.setCardElevation(0);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // Remove highlight
                Drawable d = vh.cardView.getBackground();
                d.setColorFilter(this.coloratwhite, PorterDuff.Mode.MULTIPLY);
            }
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

        boolean expand = ! rowsExpanded.get(pos, false);

        this.rowsExpanded.put(pos, expand);

        if( expand ) {
            vh.expansionView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewAnimationUtils.createCircularReveal(vh.expansionView,
                        vh.rowView.getWidth() / 2,
                        0,
                        0,
                        vh.rowView.getWidth()).start();
            } else {
                //we highlight as elevation on older devices does not seem to work very well
                Drawable d = vh.cardView.getBackground();
                d.setColorFilter(this.colorathighlight, PorterDuff.Mode.MULTIPLY);
            }
            vh.cardView.setCardElevation(this.showElevation);
        } else {
            vh.expansionView.setVisibility(View.GONE);
            vh.cardView.setCardElevation(0);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // Remove highlight
                Drawable d = vh.cardView.getBackground();
                d.setColorFilter(this.coloratwhite, PorterDuff.Mode.MULTIPLY);
            }
        }
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
