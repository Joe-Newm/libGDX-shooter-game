package com.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.game.HudRenderer;
import com.shooter.weapons.Assault;
import com.shooter.weapons.Bullet;
import com.shooter.objects.GameObject;
import com.shooter.weapons.Pistol;
import com.shooter.weapons.Shotgun;

import java.util.ArrayList;
import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class ArcadeLogic {

    public SpriteBatch batch;
    public Texture image;
    public Texture bulletTexture;
    public Texture enemyBulletTexture;
    public Viewport viewport;
    public OrthographicCamera camera;
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Bullet> player_bullets;
    public ArrayList<Bullet> enemy_bullets;
    public ArrayList<GameObject> coins;
    public ArrayList<GameObject> weapons;
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public float spawnTime;
    public float spawnDuration;
    public GameObject shotgunObject;
    public GameObject pistolObject;
    public GameObject assaultObject;
    public GameObject coinObject;
    public GameObject bloodObject;
    public Texture background;
    public Sprite backgroundSprite;
    public Texture shotgun;
    public Texture pistol;
    public Texture coin;
    public Texture assaultRifle;
    public int difficultyTest;
    public Texture[] bloodSplatter = new Texture[3];
    public ArrayList<GameObject> bloodArrayList;
    public int bloodChoice;
    public HudRenderer hudRenderer;
    public boolean quitSpawn;
    public int counter;
    public boolean hasSpawned = false;
    public int round;
    public int waveAmount = 2;
    public float enemySpeed = 150f;
    public int numEnemies = 10;
    public float healthLossSpeed = 500f;
    public float weaponDamageModifier = 0f;
    public float bulletDamage = 100;

    public void create() {
        batch = new SpriteBatch();
        bulletTexture = new Texture("player/bullet.png");
        enemyBulletTexture = new Texture("player/enemy-bullet.png");
        bloodSplatter[0] = new Texture("enemy/blood.png");
        bloodSplatter[1] = new Texture("enemy/blood1.png");
        bloodSplatter[2] = new Texture("enemy/blood2.png");
        background = new Texture("map/background1-big-wall.png");
        shotgun = new Texture("objects/shotgun.png");
        pistol = new Texture("objects/pistol.png");
        assaultRifle = new Texture("objects/assault-rifle.png");
        coin = new Texture("objects/coin.png");

        player = new Player();
        player_bullets = new ArrayList<>();
        enemy_bullets = new ArrayList<>();
        coins = new ArrayList<>();

        enemies = new ArrayList<>();
        bloodArrayList = new ArrayList<>();
        difficultyTest = 0;
        quitSpawn = false;
        counter = 0;


        createViewport();

        // HUD
        hudRenderer = new HudRenderer(player);

        // enemies spawn time
        spawnTime = 8;
        spawnDuration = 10f;

        // objects
//        shotgunObject = new GameObject(100,100, shotgun);
//        pistolObject = new GameObject(100, 200, pistol );
//        assaultObject = new GameObject(100,300, assaultRifle);
//        coinObject = new GameObject(100, 300, coin);
        weapons = new ArrayList<>();

        //background
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(1704 * 4, 960 * 4);

        // round
        round = 1;
    }

    // init camera and viewport
    public void createViewport() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
    }

    public void update(float delta) {
        backgroundSprite.draw(batch);



        // bloodobject
        if (bloodArrayList != null) {
            for (GameObject blood : bloodArrayList) {
                blood.draw(batch);
            }
        }

        if (enemies.isEmpty()) {
            System.out.println("enemies is empty");
        }

        collision_enemy();
        spawnEnemies(delta);
        collission_player_hit(delta);
        weaponsObjectCollision();
//        shotgunObject.draw(batch);
//        pistolObject.draw(batch);
//        assaultObject.draw(batch);
        collision_enemy_to_enemy();
        collectCoin(delta);
        zombieBulletsCollision();
        bulletDeletionCollision();


        // game objects
//        if (player.boundingBox.overlaps(shotgunObject.boundingBox)) {
//            player.weapon = new Shotgun();
//        }
//        if (player.boundingBox.overlaps(pistolObject.boundingBox)) {
//            player.weapon = new Pistol();
//        }
//        if (player.boundingBox.overlaps(assaultObject.boundingBox)) {
//            player.weapon = new Assault();
//        }

        System.out.println(player.position.x);
        // collision around map
        if (player.position.x < 10) {
            player.position.x = 10;
        }
        if (player.position.x > 1704 * 4 - 60) {
            player.position.x = 1704 * 4 - 60;
        }
        if (player.position.y < 10) {
            player.position.y = 10;
        }
        if (player.position.y > 960 * 4 - 60) {
            player.position.y = 960 * 4 - 60;
        }

        // update weapons list
        for (GameObject weapon : weapons) {
            weapon.draw(batch);
        }

        //update bullets list
        for (Bullet bullet : player_bullets) {
            bullet.player_update(delta);
            bullet.draw(batch);
        }

        //update enemy bullets list
        for (Bullet bullet : enemy_bullets) {
            bullet.player_update(delta);
            bullet.draw(batch);
        }

        zombieShoot(delta);

        //update coins list
        for (GameObject coin : coins) {
            coin.draw(batch);
        }

        //update enemies list
        for (Enemy enemy : enemies) {
            enemy.draw(batch, delta, player.position);
        }

//        //change difficulty
//        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
//            difficultyTest = 0;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
//            difficultyTest = 1;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
//            difficultyTest = 2;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
//            difficultyTest = 3;
//        }

        // Make the camera follow the player
        camera.position.set(player.position.x, player.position.y, 0);
        camera.update();

        // Apply the camera's view matrix to the batch
        batch.setProjectionMatrix(camera.combined);


    }

    //update weapons list
    public void weaponsObjectCollision(){
        ArrayList<GameObject> weaponsToRemove = new ArrayList<>();
        for (GameObject weapon : weapons) {
            if (player.boundingBox.overlaps(weapon.boundingBox)) {
                if (weapon.name == "shotgun") {
                    player.weapon = new Shotgun(2 + weaponDamageModifier);
                    player.shotgunAmmo = 30;
                    weaponsToRemove.add(weapon);
                }
                if (weapon.name == "assault") {
                    player.weapon = new Assault(4 + weaponDamageModifier);
                    player.assaultAmmo = 119;
                    weaponsToRemove.add(weapon);
                }
            }
        }
        weapons.removeAll(weaponsToRemove);
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
    public void collectCoin(float delta) {
        ArrayList<GameObject> coinsToRemove = new ArrayList<>();
        for (GameObject coin : coins) {
            if (Utils.overlaps(player.coinBoundingBox,coin.boundingBox)) {
                Vector2 direction = new Vector2(player.position).sub(coin.position).nor();
                coin.position.add(direction.scl(300 * delta));

                if (player.boundingBox.overlaps(coin.boundingBox)) {
                    player.currentCoins += 1;
                    coinsToRemove.add(coin);
                }
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
                    enemy.hit(player.position);
                    enemy.hp -= player.weapon.damage;

                    int bloodchance = random(3);
                    if (bloodchance == 0) {
                        bloodChoice = random(bloodSplatter.length - 1);
                        bloodArrayList.add(new GameObject((int) enemy.position.x, (int) enemy.position.y, bloodSplatter[bloodChoice], "blood"));
                    }

                }
            }
        }
        player_bullets.removeAll(bulletsToRemove);

        int weaponchance = random(6);
        // enemy death
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.hp <= 0) {
                enemiesToRemove.add(enemy);
                int coinChance = random(1);
                if (coinChance == 0) {
                    coins.add(new GameObject((int)enemy.position.x, (int)enemy.position.y, coin, "coin"));
                }

                if (weaponchance == 0) {
                    int weaponChoice = random(2);
                    if (weaponChoice == 0) {
                        weapons.add(new GameObject((int)enemy.position.x, (int)enemy.position.y, shotgun, "shotgun"));
                    } else if (weaponChoice == 1) {
                        weapons.add(new GameObject((int) enemy.position.x, (int) enemy.position.y, assaultRifle, "assault"));
                    }

                }
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    //when player touches weapon


    // when enemy touches other enemies
    public void collision_enemy_to_enemy() {
        final float MAX_DISPLACEMENT = 10f;
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

                    if (overlap > MAX_DISPLACEMENT) {
                        overlap = MAX_DISPLACEMENT;
                    }

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
    public void collission_player_hit (float delta) {
        boolean isTouching = false;
        for (Enemy enemy : enemies) {
            if (enemy.boundingBox.overlaps(player.boundingBox)) {
                isTouching = true;
                break;
            }
        }
        if (isTouching) {
            player.speed = 150f + player.speedModifier;
            player.currentHealth -= healthLossSpeed * delta;
        } else {
            player.speed = 300f + player.speedModifier;
        }
    }

    public void spawnEnemies(float delta) {
        spawnTime += delta;
//        numEnemies = 1;
//        difficultyTest = 1;
//
//        if (difficultyTest == 0) {numEnemies = 1;}
//        if (difficultyTest == 1) {numEnemies = 10;}
//        if (difficultyTest == 2) {numEnemies = 50;}
//        if (difficultyTest == 3) {numEnemies = 300;}

        if (quitSpawn == false && spawnTime > spawnDuration) {
            if (!hasSpawned) {
                spawnTime = 0;
                counter += 1;
                if (counter >= waveAmount) {
                    quitSpawn = true;
                }
                for (int i = 0; i < numEnemies; i++) {
                    float x = 0, y = 0;
                    int edge = random.nextInt(4);

                    switch (edge) {
                        case 0: // Top
                            x = random.nextInt((int) 1704 * 4);
                            y = 960 * 4 + 50;
                            break;

                        case 1: // bottom
                            x = random.nextInt((int) 1704 * 4);
                            y = -50;
                            break;

                        case 2: // left
                            x = -50;
                            y = random.nextInt((int) 960 * 4);
                            break;

                        case 3: // right
                            x = 1704 * 4 + 50;
                            y = random.nextInt((int) 960 * 4);
                            break;
                    }
                    enemies.add(new Enemy(x, y, enemySpeed, random(20)));
                }
                hasSpawned = true;
            }

        } else {
            hasSpawned = false;
        }
    }

    // shooting for type 3 zombies
    public void zombieShoot(float delta) {
        // shoot bullets if enemy type 3
        for (Enemy enemy : enemies) {
            enemy.shootDelay -= delta;
            if (enemy.thisType == 3) {
                if (enemy.shootDelay <= 0) {
                    // Get sprite rotation in radians
                    float rotationRad = (float) Math.toRadians(enemy.sprite.getRotation());

                    // Calculate the offset to the top center of the sprite
                    float offsetX = 0; // Top center has no X offset relative to the sprite's width
                    float offsetY = enemy.sprite.getHeight() * (2 / 3f) + 38; // From the custom origin to the top

                    // Rotate the offset around the sprite's rotation
                    float rotatedOffsetX = (float) (offsetX * Math.cos(rotationRad) - offsetY * Math.sin(rotationRad));
                    float rotatedOffsetY = (float) (offsetX * Math.sin(rotationRad) + offsetY * Math.cos(rotationRad));

                    // Calculate the tip position in world coordinates
                    float tipX = enemy.position.x + enemy.sprite.getOriginX() + rotatedOffsetX;
                    float tipY = enemy.position.y + enemy.sprite.getOriginY() + rotatedOffsetY;


                    Vector2 direction = new Vector2(player.position).sub(enemy.position).nor();
                    enemy_bullets.add(new Bullet(enemyBulletTexture,tipX, tipY, direction, 700f, "enemy"));
                    enemy.shootDelay = random(2f,6f);
                }
            }
        }
    }

    public void zombieBulletsCollision() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : enemy_bullets) {
            if (bullet.boundingBox.overlaps(player.boundingBox)) {
                player.currentHealth -= bulletDamage;
                bulletsToRemove.add(bullet);
            }
        }
        enemy_bullets.removeAll(bulletsToRemove);
    }

    public void bulletDeletionCollision() {
        // Remove player bullets
        Iterator<Bullet> playerBulletIterator = player_bullets.iterator();
        while (playerBulletIterator.hasNext()) {
            Bullet bullet = playerBulletIterator.next();
            if (bullet.position.x < 10 || bullet.position.x > 1704 * 4 - 60 ||
                bullet.position.y < 10 || bullet.position.y > 960 * 4 - 60) {
                playerBulletIterator.remove();
            }
        }

        // Remove enemy bullets
        Iterator<Bullet> enemyBulletIterator = enemy_bullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            Bullet bullet = enemyBulletIterator.next();
            if (bullet.position.x < 10 || bullet.position.x > 1704 * 4 - 60 ||
                bullet.position.y < 10 || bullet.position.y > 960 * 4 - 60) {
                enemyBulletIterator.remove();
            }
        }
    }


    public void dispose() {
        batch.dispose();
        image.dispose();
        bulletTexture.dispose();
        shotgun.dispose();
        pistol.dispose();
        coin.dispose();
    }
}
