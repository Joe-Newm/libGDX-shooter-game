package com.shooter;

import com.badlogic.gdx.ApplicationAdapter;
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
public class Shooter extends ApplicationAdapter {
    private Logic logic;

    @Override
    public void create() {
        logic = new Logic();
        logic.create();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // update camera
        logic.camera.update();
        logic.batch.setProjectionMatrix(logic.camera.combined);

        logic.batch.begin();
            logic.player.draw(logic.batch,delta, logic.camera, logic.player_bullets);
            logic.update(delta);
        logic.batch.end();
    }

    @Override
    public void dispose() {
        logic.dispose();
    }

    @Override
    public void resize(int width, int height) {
        logic.viewport.update(width, height, true);
    }
}
