package kr.ac.kpu.game.s2015180009.dragonflight.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;

public class MeteorGenerator implements GameObject {

    private static final float INITIAL_SPAWN_INTERVAL = 1.0f;
    private static final String TAG = MeteorGenerator.class.getSimpleName();
    private float time;
    private float spawnInterval;
    private int wave;

    public MeteorGenerator() {
        Random r = new Random();
        int minValue = 30;
        int maxValue = 10;
        time = INITIAL_SPAWN_INTERVAL * r.nextInt(maxValue - minValue + 1) + minValue;
        spawnInterval = INITIAL_SPAWN_INTERVAL * 10.0f;
        wave = 0;
    }
    @Override
    public void update() {
        MainGame game = MainGame.get();
        time += game.frameTime;
        if (time >= spawnInterval) {
            generate();
            time -= spawnInterval;
        }
    }

    private void generate() {
        wave++;
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth() / 8;
        Random r = new Random();
        for (int i = 1; i <= 7; i += 2) {
            int x = tenth * i;
            int y = 0;
            int level = wave / 10 - r.nextInt(3);
            if (level < 1) level = 1;
            if (level > 20) level = 20;
            Meteor meteor = Meteor.get(x, y, 1400);
            game.add(MainGame.Layer.meteor, meteor);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }
}
