package fr.panda.sudoku.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vincentchann on 07/08/16.
 */
public class VCViewHolder extends RecyclerView.ViewHolder {

    /*
    Attributes
     */
    public View itemView;

    /*
    Default constructor
     */
    public VCViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }
}
