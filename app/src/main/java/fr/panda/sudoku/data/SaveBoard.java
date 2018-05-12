package fr.panda.sudoku.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import fr.panda.sudoku.utils.Utils;

/**
 * Created by CHANN on 12/05/2018.
 */

public class SaveBoard {

    public static String PREF_KEY_PREFIX = "board_";
    public List<Integer> board;
    public int idx;
    public int progression;

    public SaveBoard() {}

    public SaveBoard(List<Integer> board, int idx, int progression) {
        this.board = board;
        this.idx = idx;
        this.progression = progression;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SaveBoard fromJson(String json) {
        return (SaveBoard) Utils.parseJsonString(json, SaveBoard.class);
    }
}
