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
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class Player {
    public Sprite sprite;
    public Vector2 position;
    float speed = 300f;
    public Rectangle boundingBox;
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
    public int assaultDelay = 10;
    private Sound gunshot;
    private Sound gunshot1;
    private long soundID;

    public Player () {
        playTex = new Texture(Gdx.files.internal("player/player-pistol.png"));
        sprite = new Sprite(playTex);
        sprite.scale(1);
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2 );
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());



        // weapons
        this.weapon = new Pistol();

        // sound
        gunshot = Gdx.audio.newSound(Gdx.files.internal("sounds/369528__johandeecke__short-gunshot.wav"));
        gunshot1 = Gdx.audio.newSound(Gdx.files.internal("sounds/369528__johandeecke__short-gunshot.wav"));

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
        if (this.weapon.name == "Assault"){
            if (Gdx.input.isTouched()) {
                assaultDelay -= 1;
                if (assaultDelay == 0) {
                    weapon.attack(this, camera, player_bullets);
                    assaultDelay = 10;
                    gunshot.play(0.3f);
                }
            }
        } else {
            if (Gdx.input.justTouched()) {
                weapon.attack(this, camera, player_bullets);
                gunshot.play(0.3f);
            }
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
        if (this.weapon.name == "Assault") {
            sprite.setTexture(playTex2);
        }
    }

    public void draw(SpriteBatch batch, float delta, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {
        update(delta, camera, player_bullets);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        //font.draw(batch, "x"+currentCoins, sprite.getX() - 895, sprite.getY() + 540 - 80);
    }
}
