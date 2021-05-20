package kr.ac.kpu.game.s2015180009.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.s2015180009.dragonflight.R;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;

public class Coin implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Coin.class.getSimpleName();
    protected float x;
    private final GameBitmap bitmap;
    protected float y;
    private int speed;

    private Coin(float x, float y) {
        this.x = x;
        this.y = y;
        speed = 1400;

        Log.d(TAG, "loading bitmap for bullet");
        this.bitmap = new GameBitmap(R.mipmap.laser_0);
    }

//    private static ArrayList<Bullet> recycleBin = new ArrayList<>();
    public static Coin get(float x, float y) {
        MainGame game = MainGame.get();
        Coin bullet = (Coin) game.get(Coin.class);
        if (bullet == null) {
            return new Coin(x, y);
        }
        bullet.init(x, y);
        return bullet;
    }

    private void init(float x, float y) {
        this.x = x;
        this.y = y;
        speed = 1400;
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
