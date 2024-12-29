package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;


public class Enemy {

    public Sprite sprite;
    public Vector2 position;
    public int health;
    public int health1;
    public Rectangle boundingBox;
    public Pixmap pixmap;
    private boolean isHit;
    private float hitTime;
    private static final float FLASH_DURATION = 0.05f;
    public float enemySpeed;
    public int hp;
    public Texture tex;
    public Vector2 direction;
    float knockBackForce = -1000f;
    Vector2 knockbackVelocity;
    public int thisType;

    public Enemy (float posX, float posY, float speed, int type) {
        // fat zombie
        if (type == 1) {
            tex = new Texture("enemy/zombie-2.png");
            sprite = new Sprite(tex);
            sprite.scale(1);
            health = 100;
            position = new Vector2( posX, posY);
            boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
            isHit = false;
            hitTime = 0;
            hp = health;
            enemySpeed = speed;
        }
        // fast zombie
        else if (type == 2) {
            tex = new Texture("enemy/zombie-3.png");
            sprite = new Sprite(tex);
            sprite.scale(1);
            health = 10;
            position = new Vector2(posX, posY);
            boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
            isHit = false;
            hitTime = 0;
            hp = health;
            enemySpeed = speed + 200;
        }
        // normal zombie
        else {
            tex = new Texture("enemy/zombie.png");
            sprite = new Sprite(tex);
            sprite.scale(1);
            health1 = 10;
            position = new Vector2( posX, posY);
            boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
            isHit = false;
            hitTime = 0;
            hp = health1;
            enemySpeed = speed;

        }
    }

    public void hit(Vector2 playerPosition) {
        isHit = true;
        Texture flashtex = new Texture("enemy/zombie-flash.png");
        sprite.setTexture(flashtex);

        // kockback
        knockbackVelocity = new Vector2(playerPosition).sub(position).nor().scl(knockBackForce);
    }


    public void update(float delta, Vector2 playerPosition) {
        // Move towards the player
        direction = new Vector2(playerPosition).sub(position).nor();
        sprite.setRotation(direction.angleDeg() - 90);
        position.add(direction.scl(enemySpeed * delta));
        // ends the flash after duration is up from being hit
        if (isHit) {
            hitTime += delta;
            position.add(knockbackVelocity.cpy().scl(delta));
            if (hitTime > FLASH_DURATION) {
                isHit = false;
                hitTime = 0;
                sprite.setTexture(tex);
                knockbackVelocity.setZero();
            }
        }

        // lets bounding box update to always stay on enemy
        boundingBox.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch, float delta, Vector2 playerPosition) {
        update(delta, playerPosition);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
