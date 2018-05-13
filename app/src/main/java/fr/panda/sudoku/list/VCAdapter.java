package fr.panda.sudoku.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vincentchann on 07/08/16.
 */
public class VCAdapter extends RecyclerView.Adapter<VCViewHolder> {

    protected VCListListener vcListListener;
    protected List<VCDataView> dataViews;

    /*
    Constructor
    */
    public VCAdapter(List<VCDataView> dataViews, VCListListener vcListListener) {
        this.dataViews = dataViews;
        this.vcListListener = vcListListener;
    }

    public void reload(List<VCDataView> dataViews) {
        this.dataViews = dataViews;
        notifyDataSetChanged();
    }

    /*
    Callbacks
     */
    @Override
    public int getItemCount() {
        return dataViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public VCViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VCViewHolder(inflater.inflate(dataViews.get(position).resourceViewIdx, parent, false));
    }

    @Override
    public void onBindViewHolder(VCViewHolder holder, int position) {
        if (vcListListener != null) {
            vcListListener.onBindViewHolder(holder.itemView, dataViews.get(position));
        }
    }

}
