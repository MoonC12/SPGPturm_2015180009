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

    public MeteorGenerator() {
        Random r = new Random();
        int minValue = 30;
        int maxValue = 10;
        time = INITIAL_SPAWN_INTERVAL * r.nextInt(maxValue - minValue + 1) + minValue;
        spawnInterval = INITIAL_SPAWN_INTERVAL * 10.0f;
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
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth() / 8;
        for (int i = 1; i <= 7; i += 2) {
            int x = tenth * i;
            int y = 0;
            Meteor meteor = Meteor.get(x, y, 1400);
            game.add(MainGame.Layer.meteor, meteor);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }
}
