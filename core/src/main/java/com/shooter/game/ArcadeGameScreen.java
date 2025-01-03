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
import com.badlogic.gdx.math.MathUtils;
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
    public Music currentMusic;
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
    private Table table;
    private Label coinsLabel;
    private Label roundLabel;
    private Label defenseLabel;
    private Label damageLabel;
    private Label reloadLabel;
    private Label speedLabel;
    private Skin skin;
    private TextButton reloadButton;
    private TextButton defenseButton;
    private TextButton speedButton;
    private TextButton damageButton;

    private int reloadButtonCounter = 0;
    private int defenseButtonCounter = 0;
    private int speedButtonCounter = 0;
    private int damageButtonCounter = 0;

    public Music[] musics;


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
        backgroundShopTex = new Texture(Gdx.files.internal("map/shopmenu.png"));
        backgroundBlackSprite = new Sprite(backgroundShopTex);
        backgroundBlackSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // blue background
        Texture background = new Texture(Gdx.files.internal("map/mainmenu.png"));
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // background music
        musics = new Music[5];
        musics[0] = Gdx.audio.newMusic(Gdx.files.internal("music/air-combat.mp3"));
        musics[1] = Gdx.audio.newMusic(Gdx.files.internal("music/thrash-metal-gabber-nu-metal-instrumental-not-a-penny-to-brag-about-274641.mp3"));
        musics[2] = Gdx.audio.newMusic(Gdx.files.internal("music/generic-prednisone-epic-metal-instrumental-273972.mp3"));
        musics[3] = Gdx.audio.newMusic(Gdx.files.internal("music/epic-thrash-metal-instrumental-panek-part-1-amp-2-252508.mp3"));
        musics[4] = Gdx.audio.newMusic(Gdx.files.internal("music/archangel-tactical-team-thrash-metal-power-262794.mp3"));

        playRandomSong();
    }

    private void playRandomSong() {
        // Select a random song
        int chosenSong = MathUtils.random(4); // Random number between 0 and 4

        // Get the chosen song
        currentMusic = musics[chosenSong];



        // Play the song
        currentMusic.setLooping(false); // Set to false so it doesn't loop
        if (chosenSong == 0) {
            currentMusic.setVolume(0.2f);
        } else {
            currentMusic.setVolume(0.5f);
        }
        currentMusic.play();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //check if song finished to play next song
        if (currentMusic != null && !currentMusic.isPlaying()) {
            playRandomSong();
        }


        if (!isDead && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || !isDead && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            togglePause();
        }
        if (isPaused) {
            Gdx.input.setInputProcessor(stage);
            showPauseMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            if (!isDead) {
                stage.clear();
            }
        }

        if (logic.player.currentHealth <= 0) {
            isDead = true;
            Gdx.input.setInputProcessor(stage);
            showDeathMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);

        }

        // check if won round. screen menu for next round
        if (logic.quitSpawn == true && logic.enemies.isEmpty()) {
            isDead = true;
            Gdx.input.setInputProcessor(stage);
            showNextRoundMenu(delta);
            return;
        } else {
            Gdx.input.setInputProcessor(null);
            isDead = false;
            table = null;
        }

        // test shop screen
//        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
//            isDead = true;
//            Gdx.input.setInputProcessor(stage);
//            showNextRoundMenu(delta);
//            return;
//        } else {
//            Gdx.input.setInputProcessor(null);
//            isDead = false;
//            table = null;
//        }

        // Godmode settings
//        if (godMode) {
//            logic.player.currentHealth = 1000;
//        }


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
        logic.player.draw(logic.batch, delta, logic.camera, logic.player_bullets, logic.weaponDamageModifier);
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
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    @Override
    public void dispose() {
        logic.dispose();
        stage.dispose();
        if (currentMusic != null) {
            currentMusic.dispose();
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
        table.add(label).pad(0, 15, 0, 0).center();

        table.row().pad(10, 20, 10, 0);
        table.add(resumeButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
        table.add(quitButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
//        table.add(godButton).width(300).height(50).fillX().uniformX();
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
        BitmapFont font1 = new BitmapFont(); // Default font
        font.getData().setScale(5f); // Scale up the font size
        font1.getData().setScale(2f);

        // Create a LabelStyle with the custom font
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle labelStyle1 = new Label.LabelStyle(font1, Color.WHITE);

        Label label = new Label("YOU ARE DEAD", labelStyle);
        Label label1 = new Label("You Made it to Round " + logic.round, labelStyle1);
        table.add(label).pad(0, 15, 0, 0).center();
        table.row();
        table.add(label1).pad(0, 15, 0, 0).center();

        table.row().pad(10, 20, 10, 0);
        table.add(restartButton).width(200).height(50).fillX().uniformX();
        table.row().pad(10, 20, 50, 0);
        table.add(quitButton).width(200).height(50).fillX().uniformX();

        stage.addActor(table); // Add the pause menu to the stage
    }

    private void initializeMenu() {
        // Initialize only once
        if (table != null) return;

        batch.begin();
        backgroundBlackSprite.draw(batch);
        batch.end();

        table = new Table();
        table.setFillParent(true);
        table.center();

        skin = new Skin(Gdx.files.internal("skins/skin/commodore/uiskin.json"));

        // Create buttons and labels
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

        defenseButton = new TextButton("Buy (10 coins)", skin);
        defenseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (defenseButton.isDisabled()) return;

                logic.healthLossSpeed -= 20f;
                logic.bulletDamage -= 5;
                logic.player.currentCoins -= 10;
                defenseButtonCounter += 1;
            }
        });

        damageButton = new TextButton("Buy (10 coins)", skin);
        damageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (damageButton.isDisabled()) return;

                logic.weaponDamageModifier += .2f;
                logic.player.currentCoins -= 10;
                damageButtonCounter += 1;
            }
        });

        speedButton = new TextButton("Buy (10 coins)", skin);
        speedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedButton.isDisabled()) return;

                logic.player.speedModifier += 10f;
                logic.player.currentCoins -= 10;
                speedButtonCounter += 1;
            }
        });

        reloadButton = new TextButton("Buy (10 coins)", skin);
        reloadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (reloadButton.isDisabled()) return;

                logic.player.reloadSpeed -= .10f;
                logic.player.reloadDelay = logic.player.reloadSpeed;
                logic.player.currentCoins -= 10;
                reloadButtonCounter += 1;
            }
        });

        TextButton nextRoundButton = new TextButton("Next Round", skin);
        nextRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isDead = false;
                logic.round += 1;
                logic.hasSpawned = false;
                logic.counter = 0;
                logic.quitSpawn = false;
                logic.player.position = new Vector2((1704 * 4 / 2), 960 * 4 / 2);
                logic.player_bullets.clear();

                // Update difficulty for the next round
                logic.waveAmount += 1;
                logic.enemySpeed += 5f;
                logic.spawnDuration -= 0.5f;
                logic.numEnemies = 10 + random.nextInt(20 - 10); //
            }
        });

        // Font setup
        BitmapFont font = new BitmapFont();
        font.getData().setScale(4f);

        BitmapFont font1 = new BitmapFont();
        font1.getData().setScale(2f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle labelStyle1 = new Label.LabelStyle(font1, Color.WHITE);

        roundLabel = new Label("YOU SURVIVED ROUND " + logic.round, labelStyle);
        coinsLabel = new Label("Coins: " + logic.player.currentCoins, labelStyle1);

        defenseLabel = new Label("Upgrade Defense + " + defenseButtonCounter , labelStyle1);
        damageLabel = new Label("Upgrade Damage + " + damageButtonCounter, labelStyle1);
        speedLabel = new Label("Upgrade Speed + " + speedButtonCounter, labelStyle1);
        reloadLabel = new Label("Upgrade Reload + " + reloadButtonCounter, labelStyle1);

        // Add components to the table
        table.add(roundLabel).pad(0, 100, 300, -400);
        table.row();
        table.add(coinsLabel).pad(0, 100, 0, -400);
        table.row();

        table.add(defenseLabel).pad(10, 150, 0, 0).left();
        table.add(defenseButton).width(250).height(50).pad(10);
        table.row();

        table.add(damageLabel).pad(10, 150, 0, 0).left();
        table.add(damageButton).width(250).height(50).pad(10);
        table.row();

        table.add(speedLabel).pad(10, 150, 0, 0).left();
        table.add(speedButton).width(250).height(50).pad(10);
        table.row();

        table.add(reloadLabel).pad(10, 150, 200, 0).left();
        table.add(reloadButton).width(250).height(50).pad(10,0,200,0);
        table.row();

        table.add(quitButton).width(250).height(50).pad(0, 10, 0, 0).fillX().uniformX().left();
        table.add(nextRoundButton).width(200).height(50).pad(0, 0, 0, 10).fillX().uniformX().right();

        stage.addActor(table);
    }


    private void showNextRoundMenu(float delta) {
        initializeMenu();

        // Update labels dynamically
        roundLabel.setText("YOU SURVIVED ROUND " + logic.round);
        coinsLabel.setText("Coins: " + logic.player.currentCoins);

        damageLabel.setText("Upgrade Damage + " + damageButtonCounter);
        speedLabel.setText("Upgrade Speed + " + speedButtonCounter);
        reloadLabel.setText("Upgrade Reload + " + reloadButtonCounter);
        defenseLabel.setText("Upgrade Defense + " + defenseButtonCounter);


        // Update reloadButton state
        if (logic.player.reloadSpeed <= 0.5f || logic.player.currentCoins < 10) {
            reloadButton.setDisabled(true);
            reloadButton.setColor(Color.GRAY);
        } else {
            reloadButton.setDisabled(false);
            reloadButton.setColor(Color.WHITE);
        }

        // Update defenseButton state
        if (logic.healthLossSpeed <= 100f || logic.player.currentCoins < 10) {
            defenseButton.setDisabled(true);
            defenseButton.setColor(Color.GRAY);
        } else {
            defenseButton.setDisabled(false);
            defenseButton.setColor(Color.WHITE);
        }

        // Update defenseButton state
        if (logic.player.speed >= 1000f || logic.player.currentCoins < 10) {
            speedButton.setDisabled(true);
            speedButton.setColor(Color.GRAY);
        } else {
            speedButton.setDisabled(false);
            speedButton.setColor(Color.WHITE);
        }


        // Update defenseButton state
        if (logic.weaponDamageModifier >= 20f || logic.player.currentCoins < 10) {
            damageButton.setDisabled(true);
            damageButton.setColor(Color.GRAY);
        } else {
            damageButton.setDisabled(false);
            damageButton.setColor(Color.WHITE);
        }

        stage.act(delta);
        stage.draw();
    }
}
