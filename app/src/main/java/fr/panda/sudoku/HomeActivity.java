package fr.panda.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.panda.sudoku.data.Board;
import fr.panda.sudoku.data.SaveBoard;
import fr.panda.sudoku.data.SudokuBoards;
import fr.panda.sudoku.list.VCDataView;
import fr.panda.sudoku.list.VCListListener;
import fr.panda.sudoku.list.VCListView;
import fr.panda.sudoku.utils.SharedPref;
import fr.panda.sudoku.utils.Utils;

/**
 * Created by vincentchann on 07/08/16.
 */
public class HomeActivity extends AppCompatActivity implements VCListListener {

    /*
    Attributes
     */
    private String TAG = "HomeActivity";

    /*
   Callbacks
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setListView();
        setActionBar();
    }

    protected void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.play_now));
        // TODO compter le nombre de stars
        actionBar.setSubtitle(Html.fromHtml("<small>&nbsp;" + "Vous avez : " + "<strong>4 étoiles</strong>" + "</small>"));
    }

    protected void setListView() {
        VCListView listView = (VCListView) findViewById(R.id.list_view);
        listView.initData(1, getDataViews(), this);
    }

    protected List<VCDataView> getDataViews() {
        List<VCDataView> dataViews = new ArrayList<>();
        SudokuBoards sudokuBoards = (SudokuBoards) Utils.parseJson("sudoku_boards.json", this, SudokuBoards.class);
        int position = 0;
        for (Board board : sudokuBoards.boards) {
            dataViews.add(new VCDataView(position, R.layout.cell_home, board));
            position++;
        }
        return dataViews;
    }

    @Override
    public void onBindViewHolder(View itemView, final VCDataView dataView) {
        if (dataView.resourceViewIdx == R.layout.cell_home && dataView.board != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, SudokuActivity.class);
                    intent.putExtra(Board.BUNDLE_KEY_BOARD, dataView.board);
                    startActivity(intent);
                }
            });

            TextView titleView = (TextView) itemView.findViewById(R.id.cell_title);
            titleView.setText(dataView.board.title);

            int progress = 0;
            String jsonSaveBoard = SharedPref.getInstance(this).getStringValue(SaveBoard.PREF_KEY_PREFIX + dataView.board.idx, null);
            if (jsonSaveBoard != null) {
                SaveBoard saveBoard = SaveBoard.fromJson(jsonSaveBoard);
                if (saveBoard != null) {
                    progress = saveBoard.progression;
                }
            }
            TextView subtitleView = (TextView) itemView.findViewById(R.id.cell_subtitle);
            subtitleView.setText(getString(R.string.progress) + " " + progress + "% \n"
                    + getString(R.string.value) + " " + dataView.board.difficulty + " " + getString(R.string.stars));

            RatingBar ratingView = (RatingBar) itemView.findViewById(R.id.cell_rating);
            ratingView.setRating(dataView.board.difficulty);

            ImageView iconView = (ImageView) itemView.findViewById(R.id.cell_image);
            Glide.with(HomeActivity.this).load("file:///android_asset/" + dataView.board.icon).into(iconView);

        }
    }
}
