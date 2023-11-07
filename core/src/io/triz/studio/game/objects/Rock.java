package io.triz.studio.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.triz.studio.game.Assets;

public class Rock extends AbstractGameObject {
    private TextureRegion rockRegion;

    private int length;

    public Rock() {
        init();
    }

    private void init() {
        dimension.set(1, 1.5f);
        rockRegion = Assets.instance.rock.rock;
        setLength(1);
    }

    public void setLength(int length) {
        this.length = length;
        //更新边界碰撞矩形的尺寸
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
    }

    @Override
    public void render(SpriteBatch batch) {
        float relX = 0;
        for (int i = 0; i < length; i++) {
            batch.draw(rockRegion.getTexture(), position.x + relX,
                    position.y + origin.y,
                    origin.x, origin.y,
                    dimension.x, dimension.y,
                    scale.x, scale.y, rotation,
                    rockRegion.getRegionX(), rockRegion.getRegionY(),
                    rockRegion.getRegionWidth(), rockRegion.getRegionHeight(), false, false);
            relX += dimension.x;
        }
    }
}
