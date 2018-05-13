package fr.panda.sudoku;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.vistrav.pop.Pop;

import java.util.ArrayList;
import java.util.List;

import fr.panda.sudoku.data.Board;
import fr.panda.sudoku.data.SaveBoard;
import fr.panda.sudoku.data.SudokuBoards;
import fr.panda.sudoku.utils.BoardIdxManager;
import fr.panda.sudoku.utils.SharedPref;
import fr.panda.sudoku.utils.StarsManager;
import fr.panda.sudoku.utils.Utils;
import fr.panda.sudoku.widget.BoardButton;
import fr.panda.sudoku.widget.KeypadButton;

public class SudokuActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    Attributes
     */
    private String TAG = "SudokuActivity";
    protected Board currentBoard;
    protected BoardIdxManager boardIdxManager;

    protected List<BoardButton> boardButtonList = new ArrayList<>();
    protected int boardButtonPerRow = 9;
    protected FlexboxLayout flexBoardLayout;
    protected int currentSelectedIdx = 0;

    protected List<KeypadButton> keypadButtonList = new ArrayList<>();
    protected int keypadButtonPerRow = 6;
    protected FlexboxLayout flexKeypadLayout;

    protected final int KEYPAD_PROGRESS_IDX = 1100;
    protected final int KEYPAD_LEVEL_IDX = 1101;
    protected final int GAP_KEYPAD_IDENTIFIER = 1000;
    protected final int GAP_LOCKED_VALUE = 10;
    protected final int MIN_BOARD_IDX = 1;
    protected final int MAX_BOARD_IDX = 81;

    /*
    Callbacks
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        setCurrentBoard(getIntent().getExtras());

        populateBoard();
        populateKeypad();
        disableKeyboardValues();

        setTitleActionBar();
        setBackActionbar();

        showErrors();
        showProgress();
    }

    /*
    Action Bar
     */
    protected void setTitleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(currentBoard.title);
        switch (currentBoard.difficulty) {
            case 1:
                actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Niveau débutant (1 étoile)" + "</small>"));
                break;

            case 2:
                actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Niveau confirmé (2 étoiles)" + "</small>"));
                break;

            case 3:
                actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Niveau maître (3 étoiles)" + "</small>"));
                break;

            case 4:
                actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Niveau légendaire (4 étoiles)" + "</small>"));
                break;

            case 5:
                actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Niveau dieu (5 étoiles)" + "</small>"));
                break;
        }
    }

    protected void setBackActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sudoku, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.help:
                // TODO create activity help tutorial
                Toast.makeText(this, getString(R.string.in_construction), Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*
    Board
     */
    protected void setCurrentBoard(Bundle bundle) {
        // get fresh board based on extra sent from list
        if (currentBoard == null) {
            try {
                currentBoard = (Board) bundle.getSerializable(Board.BUNDLE_KEY_BOARD);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // get saved board from preferences
            String saveBoardJson = SharedPref.getInstance(this).getStringValue(SaveBoard.PREF_KEY_PREFIX + currentBoard.idx, null);
            if (saveBoardJson != null) {
                SaveBoard saveBoard = SaveBoard.fromJson(saveBoardJson);
                if (saveBoard != null) {
                    currentBoard.board = saveBoard.board;
                }
            }
        }

        // last solution : get 1st board available
        if (currentBoard == null) {
            SudokuBoards sudokuBoards = (SudokuBoards) Utils.parseJson("sudoku_boards.json", this, SudokuBoards.class);
            if (sudokuBoards != null && sudokuBoards.boards == null) {
                return;
            }
            currentBoard = sudokuBoards.boards.get(0);
        }
    }

    protected int boardWidthPx = 0;

    protected int getBoardWidthPx() {
        if (boardWidthPx == 0) {
            boardWidthPx = Utils.getWindowsWidth(this) - 2 * Math.round(getResources().getDimension(R.dimen.board_margin));
        }
        return boardWidthPx;
    }

    protected BoardButton newBoardButton(int idx, int value) {
        BoardButton boardButton = new BoardButton(this);
        int width = getBoardWidthPx() / boardButtonPerRow;
        int height = getBoardWidthPx() / boardButtonPerRow;
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(width, height);
        boardButton.setLayoutParams(params);
        boardButton.setValueAndState(value);
        boardButton.setTextSize(getResources().getDimensionPixelSize(R.dimen.board_text_size) / Utils.getDp(this));

        boardButton.setId(idx);
        boardButton.setOnClickListener(this);
        return boardButton;
    }

    protected void populateBoard() {
        // populate with 1st board
        int position = 1;
        for (Integer value : currentBoard.board) {
            boardButtonList.add(newBoardButton(position, value));
            position++;
        }

        // add views to layout
        flexBoardLayout = (FlexboxLayout) findViewById(R.id.flex_board);
        for (BoardButton boardButton : boardButtonList) {
            flexBoardLayout.addView(boardButton);
        }

        // set background
        flexBoardLayout.setBackground(Utils.getDrawable("sudoku_empty_grid.jpg", this));

        // set board manager
        boardIdxManager = new BoardIdxManager(currentBoard.board, boardButtonPerRow);
    }

    protected BoardButton getBoardButton(int idx) {
        for (BoardButton boardButton : boardButtonList) {
            if (boardButton.getId() == idx) {
                return boardButton;
            }
        }
        return null;
    }

    /*
    Keyboard
     */
    protected int keypadWidthPx = 0;

    protected int getKeypadWidthPx() {
        if (keypadWidthPx == 0) {
            keypadWidthPx = Utils.getWindowsWidth(this) - 2 * Math.round(getResources().getDimension(R.dimen.keypad_margin));
        }
        return keypadWidthPx;
    }

    protected KeypadButton newKeypadButton(int idx, int value) {
        // set view
        int KEYPAD_MARGIN_DP = 3;
        KeypadButton keypadButton = new KeypadButton(this);
        keypadButton.setCurrentState(KeypadButton.STATE_DEFAULT);
        int width = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        int height = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(width, height);
        params.setMargins(Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this) / 2), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)));
        keypadButton.setLayoutParams(params);
        keypadButton.setTextSize(getResources().getDimension(R.dimen.keypad_text_size) / Utils.getDp(this));

        // set functions
        keypadButton.setValue(value);
        keypadButton.setId(idx);
        keypadButton.setOnClickListener(this);

        // return view
        return keypadButton;
    }

    protected KeypadButton newKeypadProgressButton(int progress) {
        // set view
        int KEYPAD_MARGIN_DP = 3;
        KeypadButton keypadButton = new KeypadButton(this);
        keypadButton.setCurrentState(KeypadButton.STATE_DISPLAY_PROGRESS);
        int width = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        int height = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(width, height);
        params.setMargins(Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this) / 2), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)));
        keypadButton.setLayoutParams(params);
        keypadButton.setTextSize(getResources().getDimension(R.dimen.keypad_text_size) / Utils.getDp(this));

        // set functions
        keypadButton.setValue(0);
        keypadButton.setText(progress + "%");
        keypadButton.setId(KEYPAD_PROGRESS_IDX);
        keypadButton.setOnClickListener(this);

        // return view
        return keypadButton;
    }

    protected KeypadButton newKeypadLevelButton(int level) {
        // set view
        int KEYPAD_MARGIN_DP = 3;
        KeypadButton keypadButton = new KeypadButton(this);
        keypadButton.setCurrentState(KeypadButton.STATE_DISPLAY_LEVEL);
        int width = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        int height = getKeypadWidthPx() / keypadButtonPerRow - Math.round(2 * KEYPAD_MARGIN_DP * Utils.getDp(this));
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(width, height);
        params.setMargins(Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this) / 2), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)), Math.round(KEYPAD_MARGIN_DP * Utils.getDp(this)));
        keypadButton.setLayoutParams(params);
        keypadButton.setTextSize(getResources().getDimension(R.dimen.keypad_level_text_size) / Utils.getDp(this));
        keypadButton.setAllCaps(false);

        // set functions
        keypadButton.setValue(0);
        switch (level) {
            case 1:
                keypadButton.setText("Logo Apprent.");
                break;
            case 2:
                keypadButton.setText("Logo Comfirm.");
                break;
            case 3:
                keypadButton.setText("Logo Master");
                break;
            case 4:
                keypadButton.setText("Logo Legend.");
                break;
            case 5:
                keypadButton.setText("Logo God");
                break;
        }
        keypadButton.setId(KEYPAD_LEVEL_IDX);
        keypadButton.setOnClickListener(this);

        // return view
        return keypadButton;
    }

    protected void populateKeypad() {
        // populate views
        keypadButtonList.add(newKeypadLevelButton(currentBoard.difficulty));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 1, 1));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 2, 2));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 3, 3));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 4, 4));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 5, 5));

        keypadButtonList.add(newKeypadProgressButton(0));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 6, 6));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 7, 7));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 8, 8));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 9, 9));
        keypadButtonList.add(newKeypadButton(GAP_KEYPAD_IDENTIFIER + 0, 0));

        // add views to layout
        flexKeypadLayout = (FlexboxLayout) findViewById(R.id.flex_keypad);
        for (KeypadButton keypadButton : keypadButtonList) {
            flexKeypadLayout.addView(keypadButton);
        }
        keypadButtonList.remove(0);
        keypadButtonList.remove(5);
    }

    protected KeypadButton getKeypadButton(int idx) {
        for (KeypadButton keypadButton : keypadButtonList) {
            if (keypadButton.getId() == idx) {
                return keypadButton;
            }
        }
        return null;
    }

    protected KeypadButton getKeypadButtonByValue(int value) {
        for (KeypadButton keypadButton : keypadButtonList) {
            if (keypadButton.getValue() == value) {
                return keypadButton;
            }
        }
        return null;
    }

    /*
    Callback OnClick for each buttons
     */
    @Override
    public void onClick(View view) {
        if (boardIdxManager == null) {
            return;
        }

        // board button
        if (view.getId() < GAP_KEYPAD_IDENTIFIER) {
            BoardButton boardButton = getBoardButton(view.getId());
            cleanBoardColor();
            showRowColor(boardButton.getId());
            showColumnColor(boardButton.getId());
            showSameValueColor(boardButton.getId());
            setCurrentSelectedButton(boardButton.getId());
            showErrors();
            showKeyboardCurrentValue(boardButton.getValue());
            showKeyboardCompletedValues();
        }

        // keypad button
        if (view.getId() >= GAP_KEYPAD_IDENTIFIER) {
            KeypadButton keypadButton = getKeypadButton(view.getId());

            // only if keypad is enabled
            if (keypadButton != null && !keypadButton.isDisabled()) {
                setCurrentButtonValue(keypadButton.getValue());
                cleanBoardColor();
                showRowColor(currentSelectedIdx);
                showColumnColor(currentSelectedIdx);
                showSameValueColor(currentSelectedIdx);
                showErrors();
                BoardButton boardButton = getBoardButton(currentSelectedIdx);
                showKeyboardCurrentValue(boardButton.getValue());
                showKeyboardCompletedValues();
                showProgress();
            }
        }
    }

    /*
    Update board view methods
     */
    protected void cleanBoardColor() {
        for (BoardButton boardButton : boardButtonList) {
            boardButton.resetCurrentState();
        }
    }

    protected void showRowColor(int buttonIdx) {
        // highlight row
        List<Integer> buttonIdxList = boardIdxManager.getRowByButtonIdx(buttonIdx);
        for (Integer currentButtonIdx : buttonIdxList) {
            BoardButton boardButton = getBoardButton(currentButtonIdx);
            boardButton.highlight();
        }

        // select button
        BoardButton boardButton = getBoardButton(buttonIdx);
        boardButton.select();
    }

    protected void showColumnColor(int buttonIdx) {
        // highlight row
        List<Integer> buttonIdxList = boardIdxManager.getColumnByButtonIdx(buttonIdx);
        for (Integer currentButtonIdx : buttonIdxList) {
            BoardButton boardButton = getBoardButton(currentButtonIdx);
            boardButton.highlight();
        }

        // select button
        BoardButton boardButton = getBoardButton(buttonIdx);
        boardButton.select();
    }

    protected void showSameValueColor(int buttonIdx) {
        // get all buttons that have the same value as buttonIdx
        List<Integer> boardButtonIdxList = boardIdxManager.getSameNumberByButtonIdx(buttonIdx, boardButtonList);
        for (Integer boardButtonIdx : boardButtonIdxList) {
            getBoardButton(boardButtonIdx).select();
        }
    }

    protected void showErrors() {
        List<Integer> errorButtonList = boardIdxManager.getAllErrors(boardButtonList);
        for (int buttonIdx : errorButtonList) {
            getBoardButton(buttonIdx).error();
        }
    }

    /*
    Update keyboard view methods
     */
    protected void disableKeyboardValues() {
        for (KeypadButton keypadButton : keypadButtonList) {
            keypadButton.disabled();
        }
    }

    protected void showKeyboardCurrentValue(int value) {
        if (value > BoardButton.GAP_LOCKED_VALUE || value == 0) {
            for (KeypadButton keypadButton : keypadButtonList) {
                if (keypadButton.getValue() == (value - BoardButton.GAP_LOCKED_VALUE)) {
                    keypadButton.select();
                } else {
                    keypadButton.resetCurrentState();
                }
            }
        } else {
            disableKeyboardValues();
        }
    }

    protected void showKeyboardCompletedValues() {
        List<Integer> values = boardIdxManager.getCompletedValues(boardButtonList);
        for (int value : values) {
            getKeypadButtonByValue(value).completed();
        }
    }

    /*
    Select & set grid value
     */
    protected void setCurrentSelectedButton(int buttonIdx) {
        currentSelectedIdx = buttonIdx;
        // enable each keyboard button if (currentSelectedIdx >= MIN_BOARD_IDX && currentSelectedIdx <= MAX_BOARD_IDX)
        // else disable them
        // also disable keyboard if currentBoardButton is locked
    }

    protected void setCurrentButtonValue(int value) {
        if (currentSelectedIdx >= MIN_BOARD_IDX && currentSelectedIdx <= MAX_BOARD_IDX) {
            BoardButton boardButton = getBoardButton(currentSelectedIdx);
            if (!boardButton.isLocked()) {

                // if you click on the value already set, the value is set to 0
                if (!boardButton.isLocked() && boardButton.getValue() != 0 && boardButton.getValueDisplayed() == value) {
                    boardButton.setValue(0);
                }

                // else change the value
                else {
                    if (value == 0) {
                        boardButton.setValue(value);
                    } else {
                        boardButton.setValue(GAP_LOCKED_VALUE + value);
                    }
                }
            }
        }
    }

    /*
    Progress bar & Success
     */
    protected void showProgress() {
        KeypadButton keypadButtonProgress = (KeypadButton) findViewById(KEYPAD_PROGRESS_IDX);
        int maxProgress = 100;
        int progress = boardIdxManager.getProgressOn100(boardButtonList);

        // save progress
        SharedPref.getInstance(this).setValue(SaveBoard.PREF_KEY_PREFIX + currentBoard.idx,
                new SaveBoard(boardIdxManager.getBoard(boardButtonList), currentBoard.idx, progress).toJson());

        keypadButtonProgress.setText(progress + "%");
        if (progress >= maxProgress) {
            showSuccess();
        }
    }

    protected void showSuccess() {
        Pop.on(this)
                .with()
                .icon(R.drawable.icon_help)
                .cancelable(false)
                .layout(R.layout.dialog_victory)
                .when(new Pop.Yah() {
                    @Override
                    public void clicked(DialogInterface dialog, View view) {
                        onBackPressed();
                    }
                })
                .show(new Pop.View() {
                    @Override
                    public void prepare(View view) {
                        TextView titleView = (TextView) view.findViewById(R.id.title);
                        titleView.setText(Html.fromHtml("<strong>Victory !</strong>"));

                        TextView infoView1 = (TextView) view.findViewById(R.id.info1);
                        infoView1.setText(Html.fromHtml(String.format(getString(R.string.you_have_won_x_stars), currentBoard.difficulty + "")));

                        TextView infoView2 = (TextView) view.findViewById(R.id.info2);
                        infoView2.setText(String.format(getString(R.string.you_have_x_stars), StarsManager.getStarsCount(SudokuActivity.this) + ""));
                    }
                });
    }
}
