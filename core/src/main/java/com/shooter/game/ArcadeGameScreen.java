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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.ArcadeLogic;
import com.shooter.Enemy;
import com.shooter.Logic;

import static com.badlogic.gdx.math.MathUtils.random;

public class ArcadeGameScreen implements Screen {
    private Shooter game;
    private ArcadeLogic logic;
    private Music music;
    private boolean isPaused;
    private Stage stage;
    public Viewport viewport;
    public SpriteBatch batch;
    public Texture backgroundPauseTex;
    public Texture backgroundShopTex;
    public Sprite backgroundRedSprite;
    public Sprite backgroundSprite;
    public Sprite backgroundBlackSprite;
    public boolean godMode = false;
    public boolean isDead = false;

    public ArcadeGameScreen(Shooter game, Viewport viewport) {
        this.game = game;
        this.viewport = viewport;
        logic = new ArcadeLogic();
        logic.create();
        stage = new Stage(this.viewport);
        isPaused = false;
        batch = new SpriteBatch();

        // red background
        backgroundPauseTex = new Texture(Gdx.files.internal("map/pausemenu.png"));
        backgroundRedSprite = new Sprite(backgroundPauseTex);
        backgroundRedSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // black background
        backgroundShopTex= new Texture(Gdx.files.internal("map/shopmenu.png"));
        backgroundBlackSprite = new Sprite(backgroundShopTex);
        backgroundBlackSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
            if (!isDead){stage.clear();}
        }

        // check if won round. screen menu for next round
        if (logic.quitSpawn == true && logic.enemies.isEmpty()) {
            isDead = true;
            Gdx.input.setInputProcessor(stage);
            showNextRoundMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            if (!isDead){stage.clear();}
        }

        // test shop screen
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            System.out.println("hello");
            isDead = true;
            Gdx.input.setInputProcessor(stage);
            showNextRoundMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            if (!isDead){stage.clear();}
            isDead = false;
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
        logic.player.draw(logic.batch,delta, logic.camera, logic.player_bullets);
        logic.batch.end();

        logic.hudRenderer.draw(logic.batch, logic.player, delta, logic.round);


    }

    @Override
    public void resize(int width, int height) {
        logic.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        //isPaused = true;
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
                game.setScreen(new ArcadeGameScreen(game, viewport));
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

    private void showNextRoundMenu(float delta) {

        stage.act(delta);  // Update stage animations or logic
        batch.begin();
        backgroundBlackSprite.draw(batch);
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
                game.setScreen(new ArcadeGameScreen(game, viewport));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, viewport));
            }
        });

        // shop

        TextButton defenseButton = new TextButton("Buy", skin);
        defenseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logic.healthLossSpeed -= 20f;
            }
        });

        TextButton damageButton = new TextButton("Buy", skin);
        damageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logic.player.weapon.damage += 1;
            }
        });

        TextButton speedButton = new TextButton("Buy", skin);
        speedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logic.player.currentSpeed += 10f;
            }
        });

        TextButton nextRoundButton = new TextButton("Next Round", skin);
        nextRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // start next round
                isDead = false;
                logic.round += 1;
                logic.hasSpawned = false;
                logic.counter = 0;
                logic.quitSpawn = false;
                logic.player.position = new Vector2( (1704 * 4 / 2)  , 960 * 4 / 2 );
                logic.player_bullets.clear();

                // difficulty for next round
                logic.waveAmount += 1;
                logic.enemySpeed += 5f;
                logic.spawnDuration -= 0.5f;
                logic.numEnemies = random.nextInt(10,20);
            }
        });

        // Custom font setup
        BitmapFont font = new BitmapFont(); // Default font
        font.getData().setScale(4f); // Scale up font size for the round label
        BitmapFont font1 = new BitmapFont(); // Default font
        font1.getData().setScale(2f); // Scale up font size for the round label
// Create a LabelStyle with the custom font
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle labelStyle1 = new Label.LabelStyle(font1, Color.WHITE);

// Top label for round information
        Label roundLabel = new Label("YOU SURVIVED ROUND " + logic.round, labelStyle);
        table.add(roundLabel).pad(0, 200, 500, 0).center();
        table.row(); // Move to the next row

// Reduce font scale for other labels
        Label defenseLabel = new Label("Upgrade Defense", labelStyle1);
        Label damageLabel = new Label("Upgrade Damage", labelStyle1);
        Label speedLabel = new Label("upgrade Speed", labelStyle1);

// Add the Upgrade Defense label and defenseButton in the same row
        table.add(defenseLabel).pad(10);
        table.add(defenseButton).width(200).height(50).pad(10);
        table.row();
        table.add(damageLabel).pad(10);
        table.add(damageButton).width(200).height(50).pad(10);
        table.row();
        table.add(speedLabel).pad(10);
        table.add(speedButton).width(200).height(50).pad(10);
        table.row();
// Add quitButton and nextRoundButton in the next row
        table.add(quitButton).width(200).height(50).pad(100).fillX().uniformX();
        table.add(nextRoundButton).width(200).height(50).pad(100).fillX().uniformX();

        stage.addActor(table); // Add the table to the stage
    }
}
