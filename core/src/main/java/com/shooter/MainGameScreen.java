package com.shooter;

import com.badlogic.gdx.Screen;

public class MainGameScreen implements Screen {
    private Shooter game;

    public MainGameScreen(Shooter game) {
        this.game = game;
        // Initialize your main game components here
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Render your main game logic here
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}



