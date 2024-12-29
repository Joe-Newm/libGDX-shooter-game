package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shooter.weapons.Bullet;
import com.shooter.weapons.Pistol;
import com.shooter.weapons.Shotgun;
import com.shooter.weapons.Weapon;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class Player {
    public Sprite sprite;
    public Vector2 position;
    public float speed;
    public float currentSpeed;
    public Rectangle boundingBox;
    public Circle coinBoundingBox;
    public int maxHealth = 1000;
    public int currentHealth = 1000;
    public int directionChange;
    public boolean facingLeft = false;
    public Weapon weapon;
    public Texture playTex1 = new Texture(Gdx.files.internal("player/shotgun_player.png"));
    public Texture playTex2 = new Texture(Gdx.files.internal("player/assault_player.png"));
    public Texture playTex;
    public BitmapFont font;
    public int currentCoins = 0;
    public float assaultDelay = 0f;
    private Sound gunshot;
    private Sound gunshot1;
    private long soundID;
    public ShapeRenderer shapeRenderer;
    public int coinRadius;
    public float reloadSpeed = 2f;
    public float reloadDelay;
    public BitmapFont reloadFont;
    public boolean reloadTextFlag = false;
    public boolean isReloading = false;
    public int assaultAmmo = 119;
    public int shotgunAmmo = 29;
    public float speedModifier;


    public Player () {
        playTex = new Texture(Gdx.files.internal("player/player-pistol.png"));
        sprite = new Sprite(playTex);
        sprite.scale(1);
        position = new Vector2( (1704 * 4 / 2)  , 960 * 4 / 2 );
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        coinRadius = 75;
        coinBoundingBox = new Circle(getCenter(), coinRadius);
        shapeRenderer = new ShapeRenderer();
        reloadDelay = reloadSpeed;
        currentSpeed = 300f;
        speed = currentSpeed;
        speedModifier = 0f;

        // reload text
        reloadFont = new BitmapFont();
        reloadFont.getData().scale(.5f);

        // sound
        gunshot = Gdx.audio.newSound(Gdx.files.internal("sounds/369528__johandeecke__short-gunshot.wav"));
        gunshot1 = Gdx.audio.newSound(Gdx.files.internal("sounds/369528__johandeecke__short-gunshot.wav"));

    }

    public void update(float delta, OrthographicCamera camera, ArrayList<Bullet> player_bullets, float weaponDamage) {
        boundingBox.setPosition(position.x, position.y);
        coinBoundingBox.setPosition(getCenter());
        System.out.println(reloadDelay);

        // controls
        if (Gdx.input.isKeyPressed(Keys.A)) {
            position.x -= delta * (speed + speedModifier);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            position.x += delta * (speed + speedModifier);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            position.y -= delta * (speed + speedModifier);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            position.y += delta * (speed + speedModifier);
        }
        //reload button
        if (Gdx.input.isKeyPressed(Keys.R)) {
            isReloading = true;
        }

        // reload logic
        if (isReloading) {
            reloadDelay -= delta;
            System.out.println(reloadDelay);
            reloadTextFlag = true;
            if (reloadDelay <= 0f) {
                this.weapon.currentCapacity = this.weapon.capacity;
                reloadDelay = reloadSpeed;
                reloadTextFlag = false;
                isReloading = false;
            }

        }
        if (this.weapon == null) {
            this.weapon = new Pistol(2 + weaponDamage);
        }

        if (this.weapon.name == "Shotgun") {
            if (Gdx.input.justTouched() && !isReloading) {
                weapon.currentCapacity -= 1;
                shotgunAmmo -= 1;
                if(shotgunAmmo <= 0) {
                    this.weapon = new Pistol(2 + weaponDamage);
                }
                if(this.weapon.currentCapacity > 0) {
                    System.out.println("current cap: " + this.weapon.currentCapacity + " max cap: " + this.weapon.capacity);
                    weapon.attack(this, camera, player_bullets);
                    gunshot.play(0.3f);
                } else if(this.weapon.currentCapacity <= 0 && !isReloading) {
                    weapon.attack(this, camera, player_bullets);
                    gunshot.play(0.3f);
                    isReloading = true;
                }
            }
        }

        //shoot
        else if (this.weapon.name == "Assault") {
            if (Gdx.input.isTouched() && !isReloading) {
                assaultDelay -= delta;
                if (assaultAmmo <= 0) {
                    this.weapon = new Pistol(2 + weaponDamage);
                }
                if (assaultDelay <= 0) {
                    weapon.currentCapacity -= 1;
                    assaultAmmo -= 1;
                    if(weapon.currentCapacity <= 0 && !isReloading) {
                        weapon.attack(this,camera,player_bullets);
                        gunshot.play(0.3f);
                        isReloading = true;
                    }else {
                        weapon.attack(this, camera, player_bullets);
                        assaultDelay = 0.1f;
                        gunshot.play(0.3f);
                    }
                }
            }
        } else {
            if (Gdx.input.justTouched() && !isReloading) {
                weapon.currentCapacity -= 1;
                if(this.weapon.currentCapacity > 0) {
                    System.out.println("current cap: " + this.weapon.currentCapacity + " max cap: " + this.weapon.capacity);
                    weapon.attack(this, camera, player_bullets);
                    gunshot.play(0.3f);
                } else if(this.weapon.currentCapacity <= 0 && !isReloading) {
                    weapon.attack(this, camera, player_bullets);
                    gunshot.play(0.3f);
                    isReloading = true;
                }
            }
        }

        // debug
//        // Set the ShapeRenderer's projection matrix to the camera's combined matrix
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        // Begin the shape renderer in line mode
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        // Set the color for the outline
//        shapeRenderer.setColor(0, 1, 0, 1); // Green color (RGBA)
//        // Draw the rectangle outline
//        shapeRenderer.circle(coinBoundingBox.x, coinBoundingBox.y, coinRadius);
//        // End the shape renderer
//        shapeRenderer.end();


        // player rotation
        Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition3D);
        Vector2 mousePosition = new Vector2(mousePosition3D.x, mousePosition3D.y); // Extract 2D position

        // Calculate angle to the mouse
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 3);
        Vector2 spritePosition = new Vector2(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY());
        float angle = mousePosition.sub(spritePosition).angleDeg();

        // Rotate sprite to face the mouse
        sprite.setRotation(angle - 90);

        // update player sprite
        if (this.weapon.name == "Shotgun") {
            sprite.setTexture(playTex1);
        }
        if (this.weapon.name == "Pistol") {
            sprite.setTexture(playTex);
        }
        if (this.weapon.name == "Assault") {
            sprite.setTexture(playTex2);
        }
    }

    public void reload(float delta) {
        reloadDelay -= delta;
    }

    private Vector2 getCenter() {
        return new Vector2(boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height / 2);
    }

    public void draw(SpriteBatch batch, float delta, OrthographicCamera camera, ArrayList<Bullet> player_bullets, float weaponDamage) {
        update(delta, camera, player_bullets, weaponDamage);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        //font.draw(batch, "x"+currentCoins, sprite.getX() - 895, sprite.getY() + 540 - 80);
        if (reloadTextFlag) {
            reloadFont.draw(batch, "reloading...", position.x - 30,position.y + 120);
        }
    }
}
