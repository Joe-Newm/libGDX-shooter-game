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
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public float spawnTime;
    public float spawnDuration;

    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        bulletTexture = new Texture("bullet.png");
        player = new Player();
        player_bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        // init camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // enemies for testing
        enemies.add(new Enemy(50,50, 10));

        // enemies spawn time
        spawnTime = 0;
        spawnDuration = 5;
    }

    public void create_bullet() {
    //check for click and create bullet
        if (Gdx.input.justTouched()) {
            Vector3 clickPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 worldClickPos3d = camera.unproject(clickPosition);
            Vector2 worldClickPosition = new Vector2(worldClickPos3d.x, worldClickPos3d.y);
            Bullet bullet = new Bullet(bulletTexture, player.position.x +25, player.position.y + 25, worldClickPosition, 1000);
            player_bullets.add(bullet);
    }}

    public void update(float delta) {
        //collision for bullets hitting enemies
        collision_enemy();
        spawnEnemies(delta);

        for (Bullet bullet : player_bullets) {
            bullet.player_update(delta);
            bullet.draw(batch);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(batch, delta, player.position);
        }
    }

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
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    public void spawnEnemies(float delta) {
        spawnTime += delta;
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

    public void dispose() {
        batch.dispose();
        image.dispose();
        bulletTexture.dispose();
    }
}
