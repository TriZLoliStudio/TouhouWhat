package io.triz.studio;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import io.triz.studio.game.Assets;
import io.triz.studio.screens.MenuScreen;

public class TouhouGdxGame extends Game {
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Assets.instance.init(new AssetManager());
        setScreen(new MenuScreen(this));
    }
//        extends ApplicationAdapter {
//    private static final String TAG = TouhouGdxGame.class.getName();
//    private WorldController worldController;
//    private WorldRender worldRender;
//
////    SpriteBatch batch;
////    Texture img;
//
//    @Override
//    public void create() {
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);
//        Assets.instance.init(new AssetManager());
//        worldController = new WorldController();
//        worldRender = new WorldRender(worldController);
////        batch = new SpriteBatch();
////        img = new Texture("badlogic.jpg");
//    }
//
//    @Override
//    public void render() {
//        //根据最后一帧的增量时间更新游戏世界
//        worldController.update(Gdx.graphics.getDeltaTime());
//        //设置清屏颜色
//        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        worldRender.render();
////        ScreenUtils.clear(1, 0, 0, 1);
////        batch.begin();
////        batch.draw(img, 0, 0);
////        batch.end();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        worldRender.resize(width, height);
//    }
//
//    @Override
//    public void dispose() {
//        worldRender.dispose();
//        Assets.instance.dispose();
////        batch.dispose();
////        img.dispose();
//    }
}
