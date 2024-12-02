package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class TestGameScreen implements Screen {
    private Shooter game;
    private Logic logic;

    public TestGameScreen(Shooter game) {
        this.game = game;
        logic = new Logic();
        logic.create();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        System.out.println("TestGameScreen render() called");

        delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // update camera
        logic.camera.update();
        logic.batch.setProjectionMatrix(logic.camera.combined);

        // toggle fullscreen
        logic.full();

        logic.batch.begin();
        logic.update(delta);
        logic.player.draw(logic.batch,delta, logic.camera, logic.player_bullets);
        logic.batch.end();


    }

    @Override
    public void resize(int width, int height) {
        logic.viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        logic.dispose();
    }
}


