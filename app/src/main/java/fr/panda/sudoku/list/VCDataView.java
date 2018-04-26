package fr.panda.sudoku.list;

/**
 * Created by vincentchann on 07/08/16.
 */
public class VCDataView {

    public int idx;
    public int resourceViewIdx;
    public String title;
    public String icon;

    public VCDataView(int idx, int resourceViewIdx, String title, String icon) {
        this.idx = idx;
        this.resourceViewIdx = resourceViewIdx;
        this.title = title;
        this.icon = icon;
    }
}
