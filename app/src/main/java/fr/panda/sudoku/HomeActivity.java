package fr.panda.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.panda.sudoku.data.Board;
import fr.panda.sudoku.data.SudokuBoards;
import fr.panda.sudoku.list.VCDataView;
import fr.panda.sudoku.list.VCListListener;
import fr.panda.sudoku.list.VCListView;
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
            dataViews.add(new VCDataView(position, R.layout.cell_title, board.title, board.icon));
            position++;
        }
        return dataViews;
    }

    @Override
    public void onBindViewHolder(View itemView, VCDataView dataView) {
        if (dataView.resourceViewIdx == R.layout.cell_title) {
            TextView titleView = (TextView) itemView.findViewById(R.id.cell_title);
            titleView.setText(dataView.title);
            ImageView iconView = (ImageView) itemView.findViewById(R.id.cell_image);
            Glide.with(HomeActivity.this).load("file://android_asset/" + dataView.icon).into(iconView);
        }
    }
}
