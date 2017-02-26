package fr.panda.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vincentchann on 06/08/16.
 */
public class SplashActivity  extends AppCompatActivity {

    /*
    Attributes
     */
    private String TAG = "SplashActivity";

    /*
   Callbacks
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


}
