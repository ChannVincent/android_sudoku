package fr.panda.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.panda.sudoku.list.VCDataView;
import fr.panda.sudoku.list.VCListListener;
import fr.panda.sudoku.list.VCListView;

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
        for (int position = 0; position < 10000; position++) {
            dataViews.add(new VCDataView(position, R.layout.list_title));
        }
        return dataViews;
    }

    @Override
    public void onBindViewHolder(View itemView, VCDataView dataView) {
        if (dataView.resourceViewIdx == R.layout.list_title) {
            TextView titleView = (TextView) itemView.findViewById(R.id.list_title);
            titleView.setText("TITLE " + dataView.idx);
        }
    }
}
