package kr.ac.kpu.game.s2015180009.dragonflight.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import kr.ac.kpu.game.s2015180009.dragonflight.R;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "Density: " + metrics.density + " DPI:" + metrics.densityDpi);
        GameView.MULTIPLIER = metrics.density * 0.8f;
    }

    @Override
    protected void onPause() {
        GameView.view.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView.view.resumeGame();
    }
}