package com.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Shooter extends Game {
    Logic logic;

    @Override
    public void create() {
        logic = new Logic();
        logic.createViewport();
        setScreen(new MainMenuScreen(this, logic.viewport));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.0f, 0.0f, 0f, 1f);


        super.render();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        logic.viewport.update(width,height,true);
    }
}
