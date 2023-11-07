package io.triz.studio.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import io.triz.studio.utils.Constants;


public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();
    private AssetManager assetManager;


    private Assets() {

    }

    public AssetReimu reimu;
    public AssetRock rock;
    public AssetGold gold;
    public AssetFeather feather;
    public AssetLevelDecoration levelDecoration;

    public AssetFonts assetFonts;

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        assetManager.finishLoading();
        Gdx.app.log(TAG, "# of assets loaded:" + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.log(TAG, "asset: " + a);
        }
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        //激活平滑纹理过滤
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        assetFonts = new AssetFonts();
        reimu = new AssetReimu(atlas);
        rock = new AssetRock(atlas);
        feather = new AssetFeather(atlas);
        gold = new AssetGold(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.debug(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        assetFonts.defaultSmall.dispose();
        assetFonts.defaultNormal.dispose();
        assetFonts.defaultBig.dispose();
    }

    public class AssetReimu {
        public final TextureAtlas.AtlasRegion head;
        public final TextureAtlas.AtlasRegion main;

        public AssetReimu(TextureAtlas atlas) {
            head = atlas.findRegion("head");
            main = atlas.findRegion("reimu");
        }
    }

    public class AssetFeather {
        public final TextureAtlas.AtlasRegion feather;

        public AssetFeather(TextureAtlas atlas) {
            feather = atlas.findRegion("items/feather");
        }
    }

    public class AssetRock {
        public final TextureAtlas.AtlasRegion rock;

        public AssetRock(TextureAtlas atlas) {
            this.rock = atlas.findRegion("rock");
        }
    }

    public class AssetGold {
        public final TextureAtlas.AtlasRegion gold;

        public AssetGold(TextureAtlas atlas) {
            this.gold = atlas.findRegion("items/gold");
        }
    }

    public class AssetLevelDecoration {
        public final TextureAtlas.AtlasRegion cloud1;
        public final TextureAtlas.AtlasRegion cloud2;
        public final TextureAtlas.AtlasRegion tree;
        public final TextureAtlas.AtlasRegion flower;

        public AssetLevelDecoration(TextureAtlas atlas) {
            this.cloud1 = atlas.findRegion("bg/cloud1");
            this.cloud2 = atlas.findRegion("bg/cloud2");
            this.tree = atlas.findRegion("bg/tree");
            this.flower = atlas.findRegion("bg/flower");
        }

    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("raw/fonts/test.fnt"),
                    true
            );
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("raw/fonts/test.fnt"),
                    true
            );
            defaultBig = new BitmapFont(
                    Gdx.files.internal("raw/fonts/test.fnt"),
                    true
            );
            //尺寸？
            //
            defaultSmall.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear
            );
            defaultNormal.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear
            );
            defaultBig.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear
            );
        }
    }
}
