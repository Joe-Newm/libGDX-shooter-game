package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shooter.weapons.Bullet;
import com.shooter.weapons.Pistol;
import com.shooter.weapons.Shotgun;
import com.shooter.weapons.Weapon;

import java.util.ArrayList;


public class Player {
    public Sprite sprite;
    public Vector2 position;
    float speed = 300f;
    public Rectangle boundingBox;
    private Texture healthBarTexture;
    private Texture healthBarTexture1;
    private Sprite healthBarSprite;
    private Sprite healthBarSprite1;
    private Texture coinTexture;
    private Sprite coinSprite;
    private float healthBarWidth = 300;
    private float healthBarHeight = 25;
    public int maxHealth = 1000;
    public int currentHealth = 1000;
    public int directionChange;
    public boolean facingLeft = false;
    public Weapon weapon;
    public Texture playTex1 = new Texture(Gdx.files.internal("player/shotgun_player.png"));
    public Texture playTex;
    public BitmapFont font;
    public int currentCoins = 0;

    public Player () {
        playTex = new Texture(Gdx.files.internal("player/player-pistol.png"));
        sprite = new Sprite(playTex);
        sprite.scale(1);
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2 );
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());

        // health bar
        Pixmap pixmapHealth = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth.setColor(0, 1, 0, 1);
        pixmapHealth.fill();
        healthBarTexture = new Texture(pixmapHealth);
        healthBarSprite = new Sprite(healthBarTexture);

        // coin ui
        coinTexture = new Texture(Gdx.files.internal("objects/coin.png"));
        coinSprite = new Sprite(coinTexture);
        coinSprite.scale(3);
        font = new BitmapFont();
        font.getData().setScale(3);

        // health bar background
        Pixmap pixmapHealth1 = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth1.setColor(1, 0, 0, 1);
        pixmapHealth1.fill();
        healthBarTexture1 = new Texture(pixmapHealth1);
        healthBarSprite1 = new Sprite(healthBarTexture1);

        pixmapHealth1.dispose();
        pixmapHealth.dispose();

        // weapons
        this.weapon = new Pistol();
    }

    public void update(float delta, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {

        boundingBox.setPosition(position.x, position.y);

        // controls
        if (Gdx.input.isKeyPressed(Keys.A)) {
            position.x -= delta * speed;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            position.x += delta * speed;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            position.y -= delta * speed;
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            position.y += delta * speed;
        }

        //shoot
        if (Gdx.input.justTouched()) {
            weapon.attack(this, camera, player_bullets);
        }

        // update health bar position
        healthBarSprite.setPosition( 25, 1080 - 50);
        healthBarSprite1.setPosition(25, 1080 - 50);
        // update coin ui position
        coinSprite.setPosition(25, 1080 - 120);

        // update health bar
        if (currentHealth > 0) {
            healthBarSprite.setSize((currentHealth / (float) maxHealth) * healthBarWidth, healthBarHeight);
        } else {
            healthBarSprite.setSize(0, healthBarHeight);
        }

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
    }

    public void draw(SpriteBatch batch, float delta, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {
        update(delta, camera, player_bullets);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        healthBarSprite1.draw(batch);
        healthBarSprite.draw(batch);
        coinSprite.draw(batch);
        font.draw(batch, "x"+currentCoins, 80, 1080 - 80);
    }
}
