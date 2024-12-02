package com.shooter.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.Logic;

import static com.shooter.Logic.VIRTUAL_HEIGHT;
import static com.shooter.Logic.VIRTUAL_WIDTH;

public class MainMenuScreen implements Screen {
    private Shooter game;
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton playButton;
    private TextButton testButton;
    public SpriteBatch batch;
    Sprite backgroundSprite;
    private Logic logic;
    private BitmapFont font;

    public MainMenuScreen(Shooter game, Viewport viewport) {
        this.game = game;
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        batch = new SpriteBatch();
        Texture background = new Texture(Gdx.files.internal("map/mainmenu.png"));
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());

        playButton = new TextButton("Coming Soon", skin);
        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new MainGameScreen(game));
            }
        });

        testButton = new TextButton("Testing Ground", skin);
        testButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TestGameScreen(game));
            }
        });

        // text
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);


        // buttons
        playButton.setPosition(VIRTUAL_WIDTH / 2 - 100, VIRTUAL_HEIGHT / 2);
        testButton.setPosition(VIRTUAL_WIDTH / 2 - 100, VIRTUAL_HEIGHT / 2 - 70);
        playButton.setSize(200,50);
        testButton.setSize(200, 50);
        stage.addActor(playButton);
        stage.addActor(testButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        font.draw(batch, "ZOMBIE SURVIVAL", VIRTUAL_WIDTH /2 - 250 , VIRTUAL_HEIGHT / 2 + 200);
        stage.draw();
        batch.end();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

