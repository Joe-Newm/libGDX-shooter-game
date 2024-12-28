package com.shooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.Logic;


import static com.shooter.Logic.VIRTUAL_HEIGHT;
import static com.shooter.Logic.VIRTUAL_WIDTH;

public class TestGameScreen implements Screen {
    private Shooter game;
    private Logic logic;
    private Music music;
    private boolean isPaused;
    private Stage stage;
    public Viewport viewport;
    public SpriteBatch batch;
    public Texture backgroundPauseTex;
    public Sprite backgroundRedSprite;
    public Sprite backgroundSprite;
    public boolean godMode = false;
    public boolean isDead = false;

    public TestGameScreen(Shooter game, Viewport viewport) {
        this.game = game;
        this.viewport = viewport;
        logic = new Logic();
        logic.create();
        stage = new Stage(this.viewport);
        isPaused = false;
        batch = new SpriteBatch();

        // red background
        backgroundPauseTex = new Texture(Gdx.files.internal("map/pausemenu.png"));
        backgroundRedSprite = new Sprite(backgroundPauseTex);
        backgroundRedSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // blue background
        Texture background = new Texture(Gdx.files.internal("map/mainmenu.png"));
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // background music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/air-combat.mp3"));

        music.setLooping(true);
        music.setVolume(0.2f);

        music.play();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (!isDead && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || !isDead && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            togglePause();
        }
        if (isPaused) {
            Gdx.input.setInputProcessor(stage);
            showPauseMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            if (!isDead){stage.clear();}
        }

        if (logic.player.currentHealth <= 0) {
            isDead = true;
            Gdx.input.setInputProcessor(stage);
            showDeathMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            stage.clear();
        }

        // Godmode settings
        if (godMode) {
            logic.player.currentHealth = 1000;
        }


        // clear the screen when not paused
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
        // ScreenUtils.clear(0f, 0f, 0f, 1f);

        // update camera
        logic.camera.update();
        logic.batch.setProjectionMatrix(logic.camera.combined);


        logic.batch.begin();
        logic.update(delta);
        logic.player.draw(logic.batch,delta, logic.camera, logic.player_bullets,2);
        logic.batch.end();

        logic.hudRenderer.draw(logic.batch, logic.player, delta, 1);


    }

    @Override
    public void resize(int width, int height) {
        logic.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.clear();
        if (music != null) {
            music.stop();
        }
    }

    @Override
    public void dispose() {
        logic.dispose();
        stage.dispose();
        if (music != null) {
            music.dispose();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    private void showPauseMenu(float delta) {
        BitmapFont pauseFont = new BitmapFont();
        pauseFont.getData().setScale(5f);

        stage.act(delta);  // Update stage animations or logic
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        stage.draw();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        Skin skin = new Skin(Gdx.files.internal("skins/skin/commodore/uiskin.json"));

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause(); // Resume the game when clicked
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, viewport));
            }
        });

        TextButton godButton = new TextButton("GodMode - " + godMode, skin);
        godButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                godMode = !godMode;
            }
        });

        //text
        // Create a custom font
        BitmapFont font = new BitmapFont(); // Default font
        font.getData().setScale(5f); // Scale up the font size

        // Create a LabelStyle with the custom font
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Label label = new Label("PAUSE", labelStyle);
        table.add(label).pad(0,15,0,0).center();

        table.row().pad(10, 20, 10, 0);
        table.add(resumeButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
        table.add(quitButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
        table.add(godButton).width(300).height(50).fillX().uniformX();
        stage.addActor(table); // Add the pause menu to the stage
    }

    private void showDeathMenu(float delta) {

        stage.act(delta);  // Update stage animations or logic
        batch.begin();
        backgroundRedSprite.draw(batch);
        batch.end();

        stage.draw();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        Skin skin = new Skin(Gdx.files.internal("skins/skin/commodore/uiskin.json"));

        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TestGameScreen(game, viewport));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, viewport));
            }
        });

        //text
        // Create a custom font
        BitmapFont font = new BitmapFont(); // Default font
        font.getData().setScale(5f); // Scale up the font size

        // Create a LabelStyle with the custom font
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Label label = new Label("YOU ARE DEAD", labelStyle);
        table.add(label).pad(0,15,0,0).center();

        table.row().pad(10, 20, 10, 0);
        table.add(restartButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
        table.add(quitButton).width(200).height(50).fillX().uniformX();

        stage.addActor(table); // Add the pause menu to the stage
    }
}


