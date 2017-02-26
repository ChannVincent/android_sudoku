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
public class KeypadButton extends Button {

    /*
    Attributes
     */
    protected Context context;
    protected int currentState;
    protected int value;

    public static final int STATE_DEFAULT = 1;
    public static final int STATE_DISABLED = 2;
    public static final int STATE_COMPLETED = 3;
    public static final int STATE_SELECTED = 4;
    public static final int STATE_DISPLAY_PROGRESS = 10;
    public static final int STATE_DISPLAY_LEVEL = 20;

    /*
    Interfaces
     */
    @IntDef({STATE_DEFAULT, STATE_DISABLED, STATE_COMPLETED, STATE_SELECTED, STATE_DISPLAY_PROGRESS, STATE_DISPLAY_LEVEL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateMode {}


    /*
    Constructor
     */
    public KeypadButton(Context context) {
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
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_default));
                setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;

            case STATE_DISABLED:
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_disabled));
                setTextColor(context.getResources().getColor(R.color.colorPrimaryTranslucent));
                break;

            case STATE_COMPLETED:
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_completed));
                setTextColor(context.getResources().getColor(R.color.colorTextWhite));
                break;

            case STATE_SELECTED:
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_selected));
                setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;

            case STATE_DISPLAY_PROGRESS:
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_display_progress));
                setTextColor(context.getResources().getColor(R.color.colorTextWhite));
                break;

            case STATE_DISPLAY_LEVEL:
                setBackground(context.getResources().getDrawable(R.drawable.keypad_state_display_level));
                setTextColor(context.getResources().getColor(R.color.colorTextWhite));
                break;
        }
    }

    public void resetCurrentState() {
        setCurrentState(STATE_DEFAULT);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (value == 0) {
            setText("");
        }
        else {
            setText("" + value);
        }
    }

    public void select() {
        setCurrentState(STATE_SELECTED);
    }

    public void disabled() {
        setCurrentState(STATE_DISABLED);
    }

    public void completed() {
        setCurrentState(STATE_COMPLETED);
    }

    public boolean isDisabled() {
        if (currentState == STATE_DISABLED) {
            return true;
        }
        else {
            return false;
        }
    }
}
