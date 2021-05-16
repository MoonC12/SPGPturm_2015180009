package kr.ac.kpu.game.s2015180009.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015180009.dragonflight.R;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;

public class Meteor implements GameObject, BoxCollidable, Recyclable {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final String TAG = Meteor.class.getSimpleName();
    private float x;
    private GameBitmap bitmap;
    private float y;
    private int speed;

    private Meteor() { }

    public static Meteor get(int x, int y, int speed) {
        MainGame game = MainGame.get();
        Meteor meteor = (Meteor) game.get(Meteor.class);
        if (meteor == null) {
            meteor = new Meteor();
        }

        meteor.init(x, y, speed);
        return meteor;
    }

    private void init(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.bitmap = new AnimationGameBitmap(R.mipmap.plane_240, FRAMES_PER_SECOND, 0);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        y += speed * game.frameTime;

        if (y > GameView.view.getHeight()) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void recycle() {
        // 재활용통에 들어가는 시점에 불리는 함수. 현재는 할일없음.
    }
}
