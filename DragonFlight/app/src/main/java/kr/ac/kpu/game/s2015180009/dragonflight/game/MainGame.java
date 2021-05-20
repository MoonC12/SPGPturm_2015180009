package kr.ac.kpu.game.s2015180009.dragonflight.game;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import kr.ac.kpu.game.s2015180009.dragonflight.R;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015180009.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2015180009.dragonflight.ui.view.GameView;
import kr.ac.kpu.game.s2015180009.dragonflight.utils.CollisionHelper;

public class MainGame extends AppCompatActivity {
    private static final String TAG = MainGame.class.getSimpleName();
    // singleton
    private static MainGame instance;
    private Player player;
    private Score score;
    private float oldXvalue;

    public static MainGame get() {
        if (instance == null) {
            instance = new MainGame();
        }
        return instance;
    }
    public float frameTime;
    private boolean initialized;

//    Player player;
    ArrayList<ArrayList<GameObject>> layers;
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();

    public void recycle(GameObject object) {
        Class clazz = object.getClass();
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null) {
            array = new ArrayList<>();
            recycleBin.put(clazz, array);
        }
        array.add(object);
    }
    public GameObject get(Class clazz) {
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null || array.isEmpty()) return null;
        return array.remove(0);
    }

    public enum Layer {
        bg1, bg2, enemy, bullet, player, coin, meteor, ui, controller, ENEMY_COUNT
    }
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300, 3);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        add(Layer.controller, new EnemyGenerator());
//        add(Layer.controller, new MeteorGenerator());

        int margin = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_city, 10);
        add(Layer.bg1, bg);;

        HorizontalScrollBackground clouds = new HorizontalScrollBackground(R.mipmap.clouds, 20);
        add(Layer.bg2, clouds);

        initialized = true;
        return true;
    }

    private void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        //if (!initialized) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        ArrayList<GameObject> meteors = layers.get(Layer.meteor.ordinal());
        ArrayList<GameObject> coins = layers.get(Layer.coin.ordinal());

        for (GameObject o1: enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for (GameObject o2: bullets) {
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(enemy, bullet)) {
                    if(enemy.hit > 1){
                        remove(bullet, false);
                        enemy.hit = enemy.hit - 1;
                        break;
                    }
                    if(enemy.hit == 1){
                        remove(bullet, false);
                        remove(enemy, false);
                        dropCoin(enemy.x, enemy.y);
                        score.addScore(10);
                    }
                    collided = true;
                    break;
                }
            }

            if(CollisionHelper.collides(enemy, player))
            {
                if(player.hit > 1){
                    player.hit -= 1;
                    remove(enemy, false);
                    break;
                }
                if(player.hit == 1)
                {
                    remove(player);
//                    askRestart();
                }
            }
            for(GameObject o3: meteors){
                Meteor meteor = (Meteor) o3;
                if(CollisionHelper.collides(meteor, player))
                    {
                        remove(player);
                        remove(meteor);
                        break;
                    }
            }

            for(GameObject o4: coins){
                Coin coin = (Coin) o4;
                if(CollisionHelper.collides(coin, player))
                {
                    remove(coin);
                    score.addScore(100);
                    collided = true;
                    break;
                }
            }
            if (collided) {
                break;
            }
        }
//        for (GameObject o1 : objects) {
//            if (!(o1 instanceof Enemy)) {
//                continue;
//            }
//            Enemy enemy = (Enemy) o1;
//            boolean removed = false;
//            for (GameObject o2 : objects) {
//                if (!(o2 instanceof Bullet)) {
//                    continue;
//                }
//                Bullet bullet = (Bullet) o2;
//
//                if (CollisionHelper.collides(enemy, bullet)) {
//                    //Log.d(TAG, "Collision!" + o1 + " - " + o2);
//                    remove(enemy);
//                    remove(bullet);
//                    //bullet.recycle();
//                    //recycle(bullet);
//                    removed = true;
//                    break;
//                }
//            }
//            if (removed) {
//                continue;
//            }
//            if (CollisionHelper.collides(enemy, player)) {
//                Log.d(TAG, "Collision: Enemy - Player");
//            }
//        }
    }


//    private void askRestart() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        //String title = getResources().getString(R.string.restart_dialog_title);
//        builder.setTitle(R.string.restart_dialog_title);
//        builder.setMessage(R.string.restart_dialog_message);
//        builder.setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                startGame();
//            }
//        });
//        builder.setNegativeButton(R.string.common_no, null);
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    public void draw(Canvas canvas) {
        //if (!initialized) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent( MotionEvent event) {

        int action = event.getAction();

//        if (action == MotionEvent.ACTION_DOWN) {

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            player.moveTo(event.getX(), event.getY());
//            int li = 0;
//            for (ArrayList<GameObject> objects: layers) {
//                for (GameObject o : objects) {
//                    Log.d(TAG, "L:" + li + " " + o);
//                }
//                li++;
//            }
            return true;
        }

//        if (action == MotionEvent.ACTION_DOWN) {
//            oldXvalue = event.getX();
//        }
//        else  if(action == MotionEvent.ACTION_MOVE){
//            player.moveTo(view.getX() + (event.getX() - (view.getWidth()/2)));
//        }
//        else if(action == MotionEvent.ACTION_UP){
//            if(view.getX() < 0){
//                view.setX(0);
//            }
//        }
        return false;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
//        Log.d(TAG, "<A> object count = " + objects.size());
    }

    private  void dropCoin(float x, float y){
        Coin coin = Coin.get(x, y);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.coin, coin);
    }

    public void remove(GameObject gameObject) {
        remove(gameObject, true);
    }

    public void remove(GameObject gameObject, boolean delayed) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (ArrayList<GameObject> objects: layers) {
                    boolean removed = objects.remove(gameObject);
                    if (removed) {
                        if (gameObject instanceof Recyclable) {
                            ((Recyclable) gameObject).recycle();
                            recycle(gameObject);
                        }
                        //Log.d(TAG, "Removed: " + gameObject);
                        break;
                    }
                }
            }
        };
        if (delayed) {
            GameView.view.post(runnable);
        } else {
            runnable.run();
        }
    }


}
