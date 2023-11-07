package io.triz.studio.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import io.triz.studio.game.objects.Feather;
import io.triz.studio.game.objects.GoldCoin;
import io.triz.studio.game.objects.Reimu;
import io.triz.studio.game.objects.Rock;
import io.triz.studio.screens.MenuScreen;
import io.triz.studio.utils.CameraHelper;
import io.triz.studio.utils.Constants;

public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();

    public CameraHelper cameraHelper;
//    public Sprite[] testSprites;
//    public int selectedSprite;

    public Level level;
    public int lives;
    public int score;

    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    private Game game;

    private void backToMenu() {
        game.setScreen(new MenuScreen(game));
    }

    public WorldController(Game game) {
        this.game = game;
        init();
    }

    private void onCollisionReimuWithRock(Rock rock) {
        Reimu reimu = level.reimu;
        float heightDifference = Math.abs(reimu.position.y - (rock.position.y + rock.bounds.height));

        if (heightDifference > 0.25f) {
            boolean hitRightEdge = reimu.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                reimu.position.x = rock.position.x + rock.bounds.width;
            } else {
                reimu.position.x = rock.position.x - reimu.bounds.width;
            }
            return;
        }

        switch (reimu.jumpState) {
            case GROUND:
                break;
            case FALLING:
            case JUMP_FALLING:
                reimu.position.y = rock.position.y + reimu.bounds.height + reimu.origin.y;
                reimu.jumpState = Reimu.JUMP_STATE.GROUND;
                break;
            case JUMP_RISING:
                reimu.position.y = rock.position.y + reimu.bounds.height + reimu.origin.y;
                break;
        }
    }

    private void onCollisionReimuWithGoldCoin(GoldCoin coin) {
        coin.collected = true;
        score += coin.getScore();
        Gdx.app.log(TAG, "Gold coin collected.4");
    }

    private void onCollisionReimuWithFeather(Feather feather) {
        feather.collected = true;
        score += feather.getScore();
        level.reimu.setFeatherPowerUp(true);
        Gdx.app.log(TAG, "Feather collected");
    }

    private void testCollisions() {
        r1.set(level.reimu.position.x, level.reimu.position.y,
                level.reimu.bounds.width, level.reimu.bounds.height);
        //碰撞检测 和岩石
        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionReimuWithRock(rock);
            // IMPORTANT : 必须测试所有岩石
        }
        //碰撞检测 和金币
        for (GoldCoin coin : level.goldCoins) {
            if (coin.collected) continue;
            r2.set(coin.position.x, coin.position.y, coin.bounds.width, coin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionReimuWithGoldCoin(coin);
            break;
        }
        //碰撞检测 和羽毛
        for (Feather feather : level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionReimuWithFeather(feather);
            break;
        }
    }

    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        initLevel();
//        initTestObjects();
    }

    private void initLevel() {
        score = 0;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.reimu);
    }

//    private void initTestObjects() {
//        testSprites = new Sprite[5];
////        int width = 32;
////        int height = 32;
////        Pixmap pixmap = createProceduralPixmap(width, height);
////        Texture texture = new Texture(pixmap);
////        for (int i = 0; i < testSprites.length; i++) {
////            Sprite sprite = new Sprite(texture);
////            sprite.setSize(1, 1);
////            sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
////            float randomX = MathUtils.random(-2.0f, 2.0f);
////            float randomY = MathUtils.random(-2.0f, 2.0f);
////            sprite.setPosition(randomX, randomY);
////            testSprites[i] = sprite;
////        }
//        Array<TextureRegion> regions = new Array<>();
//        regions.add(Assets.instance.reimu.head);
//        regions.add(Assets.instance.gold.gold);
//        for (int i = 0; i < testSprites.length; i++) {
//            Sprite sprite = new Sprite(regions.random());
//            sprite.setSize(1, 1);
//            sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
//            float randomX = MathUtils.random(-2.0f, 2.0f);
//            float randomY = MathUtils.random(-2.0f, 2.0f);
//            sprite.setPosition(randomX, randomY);
//            testSprites[i] = sprite;
//        }
//        selectedSprite = 0;
//    }

    private Pixmap createProceduralPixmap(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        handleInputGame(deltaTime);
        level.update(deltaTime);
        testCollisions();
//        updateTestObjects(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleInputGame(float deltaTime) {
        if (cameraHelper.hasTarget(level.reimu)) {
            //角色移动
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.reimu.velocity.x = -level.reimu.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.reimu.velocity.x = level.reimu.terminalVelocity.x;
            } else {
                //如果不是桌面平台 则自动向右移动
//                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
//                    level.reimu.velocity.x = level.reimu.terminalVelocity.x;
//                }
            }
            //跳跃
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.reimu.setJumping(true);
            } else {
                level.reimu.setJumping(false);
            }
        }
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;
        if (!cameraHelper.hasTarget(level.reimu)) {
            //相机移动
            float cameraMoveSpeed = 5 * deltaTime;
            float cameraMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) cameraMoveSpeed *= cameraMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-cameraMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(cameraMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, cameraMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -cameraMoveSpeed);
        }
//        float sprMoveSpeed = 5 * deltaTime;
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveSelectedSprite(-sprMoveSpeed, 0);
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveSelectedSprite(sprMoveSpeed, 0);
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveSelectedSprite(0, sprMoveSpeed);
//        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveSelectedSprite(0, -sprMoveSpeed);
        //相机缩放
        float cameraZoomSpeed = 1 * deltaTime;
        float cameraZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) cameraZoomSpeed *= cameraZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(cameraZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-cameraZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }
//
//    private void moveSelectedSprite(float x, float y) {
//        testSprites[selectedSprite].translate(x, y);
//    }
//
//    private void updateTestObjects(float deltaTime) {
//        float rotation = testSprites[selectedSprite].getRotation();
//        rotation += 90 * deltaTime;
//        rotation %= 360;
//        testSprites[selectedSprite].setRotation(rotation);
//    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "GAME WORLD RESETTED");
        }
//        else if (keycode == Input.Keys.SPACE) {
//            selectedSprite = (selectedSprite + 1) % testSprites.length;
//            if (cameraHelper.hasTarget()) {
//                cameraHelper.setTarget(testSprites[selectedSprite]);
//            }
//            Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
//        }
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.reimu);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }

}
