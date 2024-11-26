package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Enemy {

    public Sprite sprite;
    public Vector2 position;
    float speed = 500f;
    int health;
    public Rectangle boundingBox;
    public Pixmap pixmap;
    private boolean isHit;
    private float hitTime;
    private static final float FLASH_DURATION = 0.1f;
    private static final float SPEED = 100f;

    public Enemy () {
        pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);
        sprite.setColor(1,0,0,1);
        this.health = 100;
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 3, (Gdx.graphics.getHeight() - sprite.getHeight()) / 3 );
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        isHit = false;
        hitTime = 0;
    }

    public void hit() {
        isHit = true;
        sprite.setColor(1, 1, 1, 1);
    }

    public void update(float delta, Vector2 playerPosition) {
        // Move towards the player
        Vector2 direction = new Vector2(playerPosition).sub(position).nor();
        position.add(direction.scl(SPEED * delta));

        // ends the flash after duration is up from being hit
        if (isHit) {
            hitTime += delta;
            if (hitTime > FLASH_DURATION) {
                isHit = false;
                hitTime = 0;
                sprite.setColor(1, 0, 0, 1f);
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
