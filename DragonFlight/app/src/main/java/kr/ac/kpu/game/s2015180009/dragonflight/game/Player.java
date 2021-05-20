package kr.ac.kpu.game.s2015180009.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015180009.dragonflight.R;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    protected int hit;
    private float fireTime;
    private float x, y;
    private float tx, ty;
    private float speed = 1400;
    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;
    private float delta_x;

    public Player(float x, float y, int hit) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 1400;
        this.planeBitmap = new GameBitmap(R.mipmap.fighter);
        this.fireBitmap = new GameBitmap(R.mipmap.laser_0);
        this.fireTime = 0.0f;
        this.hit = hit;
    }

    public void moveTo(float x, float y) {
        this.tx = x;
        delta_x = x - this.x;

    }

    public void update() {
        MainGame game = MainGame.get();

        float dx = speed * game.frameTime;

        if (tx < x) { // move left
            dx = -dx;
        }
        x += dx;
        if ((dx > 0 && x > tx) || (dx < 0 && x < tx)) {
            x = tx;
        }

        fireTime += game.frameTime;
        if (fireTime >= FIRE_INTERVAL) {
            fireBullet();
            fireTime -= FIRE_INTERVAL;
        }

//        if (tx < x) { // move left
//            delta_x = -delta_x;
//        }
//        x += delta_x;
//        if ((delta_x > 0 && x > tx) || (delta_x < 0 && x < tx)) {
//            x = tx;
//        }

//        fireTime += game.frameTime;
//        if (fireTime >= FIRE_INTERVAL) {
//            fireBullet();
//            fireTime -= FIRE_INTERVAL;
//        }
    }

    private void fireBullet() {
        Bullet bullet = Bullet.get(this.x, this.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.bullet, bullet);
    }

    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas, x, y);
        if (fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, x, y - 50);
        }
    }

    @Override
    public void getBoundingRect(RectF rect) {
        planeBitmap.getBoundingRect(x, y, rect);
    }
}
