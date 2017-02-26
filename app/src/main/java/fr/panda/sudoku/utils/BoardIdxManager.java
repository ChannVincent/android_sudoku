package fr.panda.sudoku.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.panda.sudoku.widget.BoardButton;

/**
 * Created by vincentchann on 04/08/16.
 */
public class BoardIdxManager {

    /*
    Attributes
     */
    protected int NUMBER_OF_COLUMN = 9;
    protected int NUMBER_OF_ROW = 9;
    protected int NUMBER_OF_BLOCK = 9;
    protected List<Integer> buttonIdxList = new ArrayList<>();
    protected List<Integer> block1 = new ArrayList<>();
    protected List<Integer> block2 = new ArrayList<>();
    protected List<Integer> block3 = new ArrayList<>();
    protected List<Integer> block4 = new ArrayList<>();
    protected List<Integer> block5 = new ArrayList<>();
    protected List<Integer> block6 = new ArrayList<>();
    protected List<Integer> block7 = new ArrayList<>();
    protected List<Integer> block8 = new ArrayList<>();
    protected List<Integer> block9 = new ArrayList<>();

    /*
    Constructor
     */
    public BoardIdxManager(List<Integer> board, int buttonsPerRow) {
        // button idx list
        for (int position = 1; position <= board.size(); position++) {
            buttonIdxList.add(position);
        }

        // button per row
        NUMBER_OF_COLUMN = buttonsPerRow;

        // number of row
        NUMBER_OF_ROW = buttonIdxList.size() / NUMBER_OF_ROW;
        if (buttonIdxList.size() % NUMBER_OF_ROW > 0) {
            NUMBER_OF_ROW++;
        }

        // block list
        Collections.addAll(block1, 1,2,3, 10,11,12, 19,20,21);
        Collections.addAll(block2, 4,5,6, 13,14,15, 22,23,24);
        Collections.addAll(block3, 7,8,9, 16,17,18, 25,26,27);

        Collections.addAll(block4, 28,29,30, 37,38,39, 46,47,48);
        Collections.addAll(block5, 31,32,33, 40,41,42, 49,50,51);
        Collections.addAll(block6, 34,35,36, 43,44,45, 52,53,54);

        Collections.addAll(block7, 55,56,57, 64,65,66, 73,74,75);
        Collections.addAll(block8, 58,59,60, 67,68,69, 76,77,78);
        Collections.addAll(block9, 61,62,63, 70,71,72, 79,80,81);
    }

    /*
    Hidden methods
     */
    protected BoardButton getBoardButton(int idx, List<BoardButton> boardButtonList) {
        for (BoardButton boardButton : boardButtonList) {
            if (boardButton.getId() == idx) {
                return boardButton;
            }
        }
        return null;
    }

    protected List<Integer> sameValueOnRow(int value, List<BoardButton> boardButtonList) {
        // get displayed value
        if (value > BoardButton.GAP_LOCKED_VALUE) {
            value -= BoardButton.GAP_LOCKED_VALUE;
        }

        List<Integer> result = new ArrayList<>();
        for (int position = 1; position <= NUMBER_OF_ROW; position++) {
            List<Integer> buttonIdxList = getRow(position);
            List<Integer> buttonIdxListOnRow = new ArrayList<>();
            for (Integer buttonIdx : buttonIdxList) {
                BoardButton boardButton = getBoardButton(buttonIdx, boardButtonList);
                if (boardButton != null && value == boardButton.getValueDisplayed()) {
                    buttonIdxListOnRow.add(buttonIdx);
                }
            }

            // if you found more than one occurrence of value => add it to error result
            if (buttonIdxListOnRow.size() > 1) {
                result.addAll(buttonIdxListOnRow);
            }
        }

        return result;
    }

    protected List<Integer> sameValueOnColumn(int value, List<BoardButton> boardButtonList) {
        // get displayed value
        if (value > BoardButton.GAP_LOCKED_VALUE) {
            value -= BoardButton.GAP_LOCKED_VALUE;
        }

        List<Integer> result = new ArrayList<>();
        for (int position = 1; position <= NUMBER_OF_COLUMN; position++) {
            List<Integer> buttonIdxList = getColumn(position);
            List<Integer> buttonIdxListOnRow = new ArrayList<>();
            for (Integer buttonIdx : buttonIdxList) {
                BoardButton boardButton = getBoardButton(buttonIdx, boardButtonList);
                if (boardButton != null && value == boardButton.getValueDisplayed()) {
                    buttonIdxListOnRow.add(buttonIdx);
                }
            }

            // if you found more than one occurrence of value => add it to error result
            if (buttonIdxListOnRow.size() > 1) {
                result.addAll(buttonIdxListOnRow);
            }
        }

        return result;
    }

    protected List<Integer> sameValueOnBlock(int value, List<BoardButton> boardButtonList) {
        // get displayed value
        if (value > BoardButton.GAP_LOCKED_VALUE) {
            value -= BoardButton.GAP_LOCKED_VALUE;
        }

        List<Integer> result = new ArrayList<>();
        for (int position = 1; position <= NUMBER_OF_BLOCK; position++) {
            List<Integer> buttonIdxList = getBlock(position);
            List<Integer> buttonIdxListOnRow = new ArrayList<>();
            for (Integer buttonIdx : buttonIdxList) {
                BoardButton boardButton = getBoardButton(buttonIdx, boardButtonList);
                if (boardButton != null && value == boardButton.getValueDisplayed()) {
                    buttonIdxListOnRow.add(buttonIdx);
                }
            }

            // if you found more than one occurrence of value => add it to error result
            if (buttonIdxListOnRow.size() > 1) {
                result.addAll(buttonIdxListOnRow);
            }
        }

        return result;
    }

    /*
    Open methods : Manage identifiers
     */
    public List<Integer> getRowByButtonIdx(int buttonIdx) {
        int rowNumber = buttonIdx / NUMBER_OF_COLUMN;
        if (buttonIdx % NUMBER_OF_COLUMN == 0) {
            rowNumber -= 1;
        }

        List<Integer> result = new ArrayList<>();
        for (int position = 1; position <= NUMBER_OF_COLUMN; position++) {
            result.add(rowNumber * NUMBER_OF_COLUMN + position);
        }
        return result;
    }

    public List<Integer> getRow(int rowNumber) {
        List<Integer> result = new ArrayList<>();
        for (int position = 1; position <= NUMBER_OF_COLUMN; position++) {
            result.add(rowNumber * NUMBER_OF_COLUMN + position);
        }
        return result;
    }

    public List<Integer> getColumnByButtonIdx(int buttonIdx) {
        int columnNumber = buttonIdx % NUMBER_OF_COLUMN;
        if (columnNumber == 0)
            columnNumber = NUMBER_OF_COLUMN;

        List<Integer> result = new ArrayList<>();
        for (int position = 0; position < NUMBER_OF_ROW; position++) {
            result.add(position * 9 + columnNumber);
        }
        return result;
    }

    public List<Integer> getColumn(int columnNumber) {
        List<Integer> result = new ArrayList<>();
        for (int position = 0; position < NUMBER_OF_ROW; position++) {
            result.add(position * 9 + columnNumber);
        }
        return result;
    }

    public List<Integer> getBlockByButtonIdx(int buttonIdx) {
        if (block1.contains(buttonIdx)) return block1;
        if (block2.contains(buttonIdx)) return block2;
        if (block3.contains(buttonIdx)) return block3;
        if (block4.contains(buttonIdx)) return block4;
        if (block5.contains(buttonIdx)) return block5;
        if (block6.contains(buttonIdx)) return block6;
        if (block7.contains(buttonIdx)) return block7;
        if (block8.contains(buttonIdx)) return block8;
        if (block9.contains(buttonIdx)) return block9;
        return new ArrayList<>();
    }

    public List<Integer> getBlock(int blockNumber) {
        switch (blockNumber) {
            case 1:
                return block1;
            case 2:
                return block2;
            case 3:
                return block3;
            case 4:
                return block4;
            case 5:
                return block5;
            case 6:
                return block6;
            case 7:
                return block7;
            case 8:
                return block8;
            case 9:
                return block9;
            default:
                return new ArrayList<>();
        }
    }

    public List<Integer> getSameNumberByButtonIdx(int buttonIdx, List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        BoardButton boardButton = getBoardButton(buttonIdx, boardButtonList);
        if (boardButton.getValue() != 0) {
            for (BoardButton currentButton : boardButtonList) {
                if (currentButton.getValueDisplayed() == boardButton.getValueDisplayed()) {
                    result.add(currentButton.getId());
                }
            }
            return result;
        }
        else {
            return result;
        }
    }

    public List<Integer> getSameNumberByValue(int value, List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        for (BoardButton currentButton : boardButtonList) {
            if (currentButton.getValueDisplayed() == value) {
                result.add(currentButton.getId());
            }
        }
        return result;
    }

    /*
    Completed : 9 boardButton has been set
     */
    public List<Integer> getNumberOfValueCompleted(int value, List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        for (BoardButton boardButton : boardButtonList) {
            if (boardButton.getValueDisplayed() == value) {
                result.add(boardButton.getId());
            }
        }
        return result;
    }

    public List<Integer> getCompletedValues(List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            if (getNumberOfValueCompleted(value, boardButtonList).size() >= 9) {
                result.add(value);
            }
        }
        return result;
    }

    /*
    Open methods : bonus
     */
    public List<Integer> getErrorsForValue(int value, List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        // check errors on block
        if (sameValueOnBlock(value, boardButtonList).size() > 1) {
            result.addAll(sameValueOnBlock(value, boardButtonList));
        }

        // check errors on column
        if (sameValueOnColumn(value, boardButtonList).size() > 1) {
            result.addAll(sameValueOnColumn(value, boardButtonList));
        }

        // check errors on row
        if (sameValueOnRow(value, boardButtonList).size() > 1) {
            result.addAll(sameValueOnRow(value, boardButtonList));
        }

        return result;
    }

    public List<Integer> getAllErrors(List<BoardButton> boardButtonList) {
        List<Integer> result = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            result.addAll(getErrorsForValue(value, boardButtonList));
        }
        return result;
    }

    public List<Integer> getPossibilitiesForValue(int value, List<BoardButton> boardButtonList) {
        return null;
    }

    /*
    Progression
     */
    public int getProgressOn100(List<BoardButton> boardButtonList) {
        List<Integer> errorButtonIdxList = getAllErrors(boardButtonList);
        List<Integer> progressButtonIdxList = new ArrayList<>();
        int unlockedButtonCount = 0;

        // add to progression only if it is not an error and if it is not locked
        for(BoardButton boardButton : boardButtonList) {
            if (!boardButton.isLocked()) {
                unlockedButtonCount++;
                if (!errorButtonIdxList.contains(boardButton.getId()) && boardButton.getValue() != 0) {
                    progressButtonIdxList.add(boardButton.getId());
                }
            }
        }

        // return progress on 100
        return Math.round((float)progressButtonIdxList.size() / (float)unlockedButtonCount * 100);
    }
}
