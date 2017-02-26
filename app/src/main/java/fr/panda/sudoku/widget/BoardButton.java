package fr.panda.sudoku.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.Button;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.panda.sudoku.R;

/**
 * Created by vincentchann on 02/08/16.
 */
public class BoardButton extends Button {

    /*
    Attributes
     */
    protected Context context;
    protected int currentState;
    protected int value;
    public static final int GAP_LOCKED_VALUE = 10;

    public static final int STATE_DEFAULT = 0; // value is set or not : alteration are allowed
    public static final int STATE_SELECTED = 1;
    public static final int STATE_HIGHLIGHT = 2;
    public static final int STATE_ERROR = 3;

    public static final int STATE_LOCKED = 10; // number is set from start and not alterable
    public static final int STATE_LOCKED_SELECTED = 11;
    public static final int STATE_LOCKED_HIGHLIGHT = 12;
    public static final int STATE_LOCKED_ERROR = 13;

    /*
    Interfaces
     */
    @IntDef({STATE_DEFAULT, STATE_SELECTED, STATE_HIGHLIGHT, STATE_ERROR, STATE_LOCKED, STATE_LOCKED_SELECTED, STATE_LOCKED_HIGHLIGHT, STATE_LOCKED_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateMode {}


    /*
    Constructor
     */
    public BoardButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    /*
    Hidden methods
     */
    protected void init() {
        setCurrentState(STATE_DEFAULT);
    }

    /*
    Opened Methods
     */
    public void setCurrentState(@StateMode int state) {
        this.currentState = state;
        switch (state) {
            case STATE_DEFAULT:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_default));
                setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;

            case STATE_SELECTED:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_selected));
                setTextColor(context.getResources().getColor(R.color.colorTextWhite));
                break;

            case STATE_HIGHLIGHT:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_highlight));
                setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;

            case STATE_ERROR:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_error));
                setTextColor(context.getResources().getColor(R.color.colorTextBlack));
                break;

            case STATE_LOCKED:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_locked));
                setTextColor(context.getResources().getColor(R.color.colorTextBlack));
                break;

            case STATE_LOCKED_SELECTED:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_locked_selected));
                setTextColor(context.getResources().getColor(R.color.colorTextWhite));
                break;

            case STATE_LOCKED_HIGHLIGHT:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_locked_highlight));
                setTextColor(context.getResources().getColor(R.color.colorTextBlack));
                break;

            case STATE_LOCKED_ERROR:
                setBackground(context.getResources().getDrawable(R.drawable.board_state_locked_error));
                setTextColor(context.getResources().getColor(R.color.colorTextBlack));
                break;
        }
    }

    public void resetCurrentState() {
        setValueAndState(this.value);
    }

    public boolean isLocked() {
        if (currentState >= STATE_LOCKED) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setValue(int value) {
        this.value = value;
        if (this.value == 0) {
            setText("");
        }
        else if (this.value >= 1 && this.value <= 9) {
            setText("" + this.value);

        }
        else if (this.value >= 11 && this.value <= 19) {
            setText("" + (this.value - GAP_LOCKED_VALUE));
        }
    }

    public void setValueAndState(int value) {
        this.value = value;
        if (this.value == 0) {
            setText("");
            setCurrentState(STATE_DEFAULT);
        }
        else if (this.value >= 1 && this.value <= 9) {
            setText("" + this.value);
            setCurrentState(STATE_LOCKED);
        }
        else if (this.value >= 11 && this.value <= 19) {
            setText("" + (this.value - GAP_LOCKED_VALUE));
            setCurrentState(STATE_DEFAULT);
        }
    }

    public int getValue() {
        return value;
    }

    public int getValueDisplayed() {
        if (value > GAP_LOCKED_VALUE) {
            return (value - GAP_LOCKED_VALUE);
        }
        else {
            return value;
        }

    }

    public void select() {
        if (value < GAP_LOCKED_VALUE && value != 0) {
            setCurrentState(STATE_LOCKED_SELECTED);
        }
        else {
            setCurrentState(STATE_SELECTED);
        }
    }

    public void highlight() {
        if (value < GAP_LOCKED_VALUE && value != 0) {
            setCurrentState(STATE_LOCKED_HIGHLIGHT);
        }
        else {
            setCurrentState(STATE_HIGHLIGHT);
        }
    }

    public void error() {
        if (value < GAP_LOCKED_VALUE && value != 0) {
            setCurrentState(STATE_LOCKED_ERROR);
        }
        else {
            setCurrentState(STATE_ERROR);
        }

    }
}
