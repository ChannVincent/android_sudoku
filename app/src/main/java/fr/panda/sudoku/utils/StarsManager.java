package fr.panda.sudoku.utils;

import android.content.Context;

import fr.panda.sudoku.data.Board;
import fr.panda.sudoku.data.SaveBoard;
import fr.panda.sudoku.data.SudokuBoards;

/**
 * Created by CHANN on 13/05/2018.
 */

public class StarsManager {

    static public int getStarsCount(Context context) {
        int result = 0;
        SudokuBoards sudokuBoards = (SudokuBoards) Utils.parseJson("sudoku_boards.json", context, SudokuBoards.class);
        for (Board board : sudokuBoards.boards) {
            String saveBoardJson = SharedPref.getInstance(context).getStringValue(SaveBoard.PREF_KEY_PREFIX + board.idx, null);
            if (saveBoardJson != null) {
                SaveBoard saveBoard = SaveBoard.fromJson(saveBoardJson);
                if (saveBoard != null) {
                    if (saveBoard.progression >= 100) {
                        result += board.difficulty;
                    }
                }
            }
        }
        return result;
    }
}
