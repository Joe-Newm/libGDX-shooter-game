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
    private static final float FLASH_DURATION = 0.05f;
    private static final float SPEED = 100f;
    public int hp;
    public Texture tex;

    public Enemy (float posX, float posY, int health) {
        tex = new Texture("enemy/zombie.png");
        sprite = new Sprite(tex);
        sprite.scale(1);
        this.health = 100;
        position = new Vector2( posX, posY);
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        isHit = false;
        hitTime = 0;
        hp = health;
    }

    public void hit() {
        isHit = true;
        Texture flashtex = new Texture("enemy/zombie-flash.png");
        sprite.setTexture(flashtex);
    }

    public void update(float delta, Vector2 playerPosition) {
        // Move towards the player
        Vector2 direction = new Vector2(playerPosition).sub(position).nor();
        sprite.setRotation(direction.angleDeg() - 90);
        position.add(direction.scl(SPEED * delta));

        // ends the flash after duration is up from being hit
        if (isHit) {
            hitTime += delta;
            if (hitTime > FLASH_DURATION) {
                isHit = false;
                hitTime = 0;
                sprite.setTexture(tex);
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
