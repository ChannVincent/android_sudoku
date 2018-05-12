package fr.panda.sudoku.list;

import fr.panda.sudoku.data.Board;

/**
 * Created by vincentchann on 07/08/16.
 */
public class VCDataView {

    public int idx;
    public int resourceViewIdx;
    public Board board;

    public VCDataView(int idx, int resourceViewIdx, Board board) {
        this.idx = idx;
        this.resourceViewIdx = resourceViewIdx;
        this.board = board;
    }
}
