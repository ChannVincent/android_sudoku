package fr.panda.sudoku.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vincentchann on 04/08/16.
 */
public class Board implements Serializable {

    static public String BUNDLE_KEY_BOARD = "bundle key board";
    public int idx;
    public String title;
    public String icon;
    public int difficulty;
    public List<Integer> board;

    public Board() {}

    public String toString() {
        String result = "";
        result += "idx : " + idx + "\n"
                + "title : " + title + "\n"
                + "difficulty : " + difficulty + "\n";
        if (board != null) {
            result += "[";
            for (Integer value : board) {
                result += value + ",";
            }

            result += "]";
        }
        return result;
    }
}
