package com.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.weapons.Bullet;
import com.shooter.objects.GameObject;
import com.shooter.weapons.Pistol;
import com.shooter.weapons.Shotgun;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Logic {

    public SpriteBatch batch;
    public Texture image;
    public Texture bulletTexture;
    public Viewport viewport;
    public OrthographicCamera camera;
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Bullet> player_bullets;
    public ArrayList<GameObject> coins;
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public float spawnTime;
    public float spawnDuration;
    public GameObject shotgunObject;
    public GameObject pistolObject;
    public GameObject coinObject;
    public Texture background;
    public Sprite backgroundSprite;
    public Texture shotgun;
    public Texture pistol;
    public Texture coin;
    public int difficultyTest;

    public void create() {
        batch = new SpriteBatch();
        bulletTexture = new Texture("player/bullet.png");
        background = new Texture("map/background1.png");
        shotgun = new Texture("objects/shotgun.png");
        pistol = new Texture("objects/pistol.png");
        coin = new Texture("objects/coin.png");
        player = new Player();
        player_bullets = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        difficultyTest = 0;

        // init camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // enemies for testing
        enemies.add(new Enemy(50,50, 10));

        // enemies spawn time
        spawnTime = 0;
        spawnDuration = 5;

        // objects
        shotgunObject = new GameObject(100,100, shotgun);
        pistolObject = new GameObject(100, 200, pistol );
        coinObject = new GameObject(100, 300, coin);

        //background
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    }

    public void update(float delta) {
        backgroundSprite.draw(batch);
        collision_enemy();
        spawnEnemies(delta);
        collission_player_hit();
        shotgunObject.draw(batch);
        pistolObject.draw(batch);
        collision_enemy_to_enemy();
        collectCoin();

        // game objects
        if (player.boundingBox.overlaps(shotgunObject.boundingBox)) {
            player.weapon = new Shotgun();
        }
        if (player.boundingBox.overlaps(pistolObject.boundingBox)) {
            player.weapon = new Pistol();
        }

        //update bullets list
        for (Bullet bullet : player_bullets) {
            bullet.player_update(delta);
            bullet.draw(batch);
        }
        //update coins list
        for (GameObject coin : coins) {
            coin.draw(batch);
        }
        //update enemies list
        for (Enemy enemy : enemies) {
            enemy.draw(batch, delta, player.position);
        }


        //change difficulty
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            difficultyTest = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            difficultyTest = 1;
        }

    }

    public void full() {
        // toggle fullscreen
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (Gdx.graphics.isFullscreen()) {
                // Switch to windowed mode
                Gdx.graphics.setWindowedMode(1280, 720); // Set your desired window size
            } else {
                // Switch to fullscreen mode
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(displayMode);
            }}
    }

    //coin collision
    public void collectCoin() {
        ArrayList<GameObject> coinsToRemove = new ArrayList<>();
        for (GameObject coin : coins) {
            if (player.boundingBox.overlaps(coin.boundingBox)) {
                player.currentCoins += 1;
                coinsToRemove.add(coin);
            }
        }
        coins.removeAll(coinsToRemove);
    }


    // when enemy gets hit
    public void collision_enemy() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : player_bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.boundingBox.overlaps(enemy.boundingBox)) {
                    bulletsToRemove.add(bullet);
                    enemy.hit();
                    enemy.hp -= 1;
                }
            }
        }
        player_bullets.removeAll(bulletsToRemove);
        // enemy death
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.hp <= 0) {
                enemiesToRemove.add(enemy);
                coins.add(new GameObject((int)enemy.position.x, (int)enemy.position.y, coin));
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    public void collision_enemy_to_enemy() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy1 = enemies.get(i);

            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy enemy2 = enemies.get(j);

                if (enemy1.boundingBox.overlaps(enemy2.boundingBox)) {
                    // Calculate the direction to push them apart
                    Vector2 displacement = new Vector2(
                        enemy1.position.x - enemy2.position.x,
                        enemy1.position.y - enemy2.position.y
                    ).nor();

                    // Push each enemy away by half the overlap distance
                    float overlapX = (enemy1.boundingBox.width + enemy2.boundingBox.width) / 2 -
                        Math.abs(enemy1.position.x - enemy2.position.x);
                    float overlapY = (enemy1.boundingBox.height + enemy2.boundingBox.height) / 2 -
                        Math.abs(enemy1.position.y - enemy2.position.y);

                    float overlap = Math.min(overlapX, overlapY);

                    // Apply displacement
                    enemy1.position.add(displacement.scl(overlap / 2));
                    enemy2.position.sub(displacement.scl(overlap / 2));

                    // Update bounding boxes
                    enemy1.boundingBox.setPosition(enemy1.position.x, enemy1.position.y);
                    enemy2.boundingBox.setPosition(enemy2.position.x, enemy2.position.y);
                }
            }
        }
    }

    // when player gets touched by enemies
    public void collission_player_hit () {
        boolean isTouching = false;
        for (Enemy enemy : enemies) {
            if (enemy.boundingBox.overlaps(player.boundingBox)) {
                isTouching = true;
                break;
            }
        }
        if (isTouching) {
            player.speed = 150f;
            player.currentHealth -= 1;
        } else {
            player.speed = 300f;
        }
    }

    public void spawnEnemies(float delta) {
        spawnTime += delta;
        if (difficultyTest == 0) {
            if (spawnTime > spawnDuration) {
                spawnTime = 0;

                float x = 0, y = 0;
                int edge = random.nextInt(4);

                switch (edge) {
                    case 0: // Top
                        x = random.nextInt((int) viewport.getWorldWidth());
                        y = viewport.getWorldHeight() + 50;
                        break;

                    case 1: // bottom
                        x = random.nextInt((int) viewport.getWorldWidth());
                        y = -50;
                        break;

                    case 2: // left
                        x = -50;
                        y = random.nextInt((int) viewport.getWorldHeight());
                        break;

                    case 3: // right
                        x = viewport.getWorldWidth() + 50;
                        y = random.nextInt((int) viewport.getWorldHeight());
                        break;
                }
                enemies.add(new Enemy(x, y, 10));
            }
        }
        if (difficultyTest == 1) {
            if (spawnTime > spawnDuration) {
                spawnTime = 0;
                for (int i= 0; i < 10; i++) {
                float x = 0, y = 0;
                int edge = random.nextInt(4);

                switch (edge) {
                    case 0: // Top
                        x = random.nextInt((int) viewport.getWorldWidth());
                        y = viewport.getWorldHeight() + 50;
                        break;

                    case 1: // bottom
                        x = random.nextInt((int) viewport.getWorldWidth());
                        y = -50;
                        break;

                    case 2: // left
                        x = -50;
                        y = random.nextInt((int) viewport.getWorldHeight());
                        break;

                    case 3: // right
                        x = viewport.getWorldWidth() + 50;
                        y = random.nextInt((int) viewport.getWorldHeight());
                        break;
                }
                enemies.add(new Enemy(x, y, 10));
            }

        }
    }}

    public void dispose() {
        batch.dispose();
        image.dispose();
        bulletTexture.dispose();
    }
}
