package io.triz.studio.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.triz.studio.game.Assets;
import io.triz.studio.utils.Constants;

public class Reimu extends AbstractGameObject {
    public static final String TAG = Reimu.class.getName();
    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    public enum VIEW_DIRECTION {
        LEFT, RIGHT
    }

    public enum JUMP_STATE {
        GROUND, FALLING, JUMP_RISING, JUMP_FALLING
    }

    private TextureRegion regReimu;

    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeatherPowerUp;
    public float timeLeftFeatherPowerUp;

    public Reimu() {
        init();
    }

    public void init() {
        dimension.set(1, 1);
        regReimu = Assets.instance.reimu.main;
        //将原点设置为对象中心
        origin.set(dimension.x / 2, dimension.y / 2);
        //设置边界矩形的尺寸
        bounds.set(0, 0, dimension.x, dimension.y);
        //设置物理属性
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        //初始化观察方向
        viewDirection = VIEW_DIRECTION.RIGHT;
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        //初始化飞行状态
        hasFeatherPowerUp = false;
        timeLeftFeatherPowerUp = 0;
    }

    public void setJumping(boolean jumpKeyPressed) {
        switch (jumpState) {
            //站在rock平台上
            case GROUND:
                if (jumpKeyPressed) {
                    //从0开始计算跳跃经过的时间
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            //上升状态
            case JUMP_RISING:
                if (!jumpKeyPressed) jumpState = JUMP_STATE.JUMP_FALLING;
                break;
            //掉落状态
            case FALLING:
                //完成一个跳跃后的下降状态
            case JUMP_FALLING:
                if (jumpKeyPressed && hasFeatherPowerUp) {
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerUp(boolean pickedUp) {
        hasFeatherPowerUp = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerUp() {
        return hasFeatherPowerUp && timeLeftFeatherPowerUp > 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (velocity.x != 0) {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerUp > 0) {
            timeLeftFeatherPowerUp -= deltaTime;
            if (timeLeftFeatherPowerUp < 0) {
                //关闭特效
                timeLeftFeatherPowerUp = 0;
                setFeatherPowerUp(false);
            }
        }
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        switch (jumpState) {
            case GROUND:
                jumpState = JUMP_STATE.FALLING;
                break;
            case JUMP_RISING:
                //跳跃计时
                timeJumping += deltaTime;
                //如果跳跃还没达到最大高度
                if (timeJumping <= JUMP_TIME_MAX) {
                    //继续上升
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
                timeJumping += deltaTime;
                //如果跳跃按键被释放过快,则应该保证一个最低跳跃高度
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
                    velocity.y = terminalVelocity.y;
                }
        }
        if (jumpState != JUMP_STATE.GROUND)
            super.updateMotionY(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;
        //如果激活了飞行效果,则设置一个特殊着色
        if (hasFeatherPowerUp) {
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
        }
        //渲染图片
        reg = regReimu;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
        batch.setColor(1, 1, 1, 1);
    }
}
