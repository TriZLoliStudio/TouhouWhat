package io.triz.studio.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import io.triz.studio.game.WorldController;
import io.triz.studio.game.WorldRender;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();

    private WorldController worldController;
    private WorldRender worldRender;

    private boolean paused;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            worldController.update(delta);
        }
        //清屏颜色 浅蓝色
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        //清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRender.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRender.resize(width, height);
    }

    @Override
    public void show() {
        worldController = new WorldController(game);
        worldRender = new WorldRender(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        worldRender.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        //只有安卓才会被调用
        paused = false;
    }
}
