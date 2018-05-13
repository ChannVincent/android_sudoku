package fr.panda.sudoku.list;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by vincentchann on 07/08/16.
 */
public class VCListView extends RecyclerView {

    /*
    Attributes
     */
    protected Context context;
    protected int columnNumber = 1;
    protected StaggeredGridLayoutManager staggeredGridLayoutManager;
    protected VCAdapter vcAdapter;

    /*
    Default constructors
     */
    public VCListView(Context context) {
        super(context);
        this.context = context;
    }

    public VCListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public VCListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /*
    Callback
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAdapter(vcAdapter);
        setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(columnNumber, VERTICAL);
        setLayoutManager(staggeredGridLayoutManager);
    }

    public void initData(int columnNumber, List<VCDataView> dataViews, VCListListener vcListListener) {
        this.vcAdapter = new VCAdapter(dataViews, vcListListener);
        this.columnNumber = columnNumber;
    }

    public void reload(List<VCDataView> dataViews) {
        vcAdapter.reload(dataViews);
    }
}
