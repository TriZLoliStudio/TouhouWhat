package io.triz.studio.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.triz.studio.game.objects.*;

public class Level {
    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), //黑色
        ROCK(255, 255, 255), //白色
        PLAYER(0, 255, 0),//绿色 玩家
        ITEM_FEATHER(255, 0, 255), // 紫色羽毛
        ITEM_GOLD_COIN(255, 255, 0);//黄色 金币

        private int color;

        BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }

    public Array<Rock> rocks;
    public Clouds clouds;
    public Reimu reimu;
    public Array<GoldCoin> goldCoins;
    public Array<Feather> feathers;

    public Level(String filename) {
        init(filename);
    }

    private void init(String filename) {
        //玩家
        reimu = null;
        //岩石
        rocks = new Array<>();
        //游戏对象
        goldCoins = new Array<>();
        feathers = new Array<>();
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        //从左上角逐行扫描至右下角
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                //计算底部高度
                float baseHeight = pixmap.getHeight() - pixelY;
                //获取當前位置的rgba
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                //找到當前位置颜色匹配的代码块并创建对象
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {

                } else if (BLOCK_TYPE.PLAYER.sameColor(currentPixel)) {
                    obj = new Reimu();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    reimu = (Reimu) obj;
                } else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    feathers.add((Feather) obj);
                } else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goldCoins.add((GoldCoin) obj);
                } else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        obj = new Rock();
//                        float heightIncreaseFactor = 0.25f;
//                        offsetHeight = -2.5F;
//                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
//                    Gdx.app.debug(TAG, "x<" + pixelX + "> y<" + pixelY + "> set a rock.Rocks size:" + rocks.size);
                } else {
                    //未定义颜色
                    int r = 0xff & (currentPixel >> 24);
                    int g = 0xff & (currentPixel >> 16);
                    int b = 0xff & (currentPixel >> 8);
                    int a = 0xff & (currentPixel);
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + "> : r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }
        }
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        pixmap.dispose();
        Gdx.app.debug(TAG, "level" + filename + " load,width:" + pixmap.getWidth() + ",height:" + pixmap.getHeight());
    }

    public void render(SpriteBatch batch) {
        for (Rock rock : rocks) {
            rock.render(batch);
        }
        for (GoldCoin coin : goldCoins) {
            coin.render(batch);
        }
        for (Feather feather : feathers) {
            feather.render(batch);
        }
        reimu.render(batch);
        clouds.render(batch);
    }

    public void update(float deltaTime) {
        reimu.update(deltaTime);
        for (Rock rock : rocks) {
            rock.update(deltaTime);
        }
        for (GoldCoin coin : goldCoins) {
            coin.update(deltaTime);
        }
        for (Feather feather : feathers) {
            feather.update(deltaTime);
        }
        clouds.update(deltaTime);
    }

}
