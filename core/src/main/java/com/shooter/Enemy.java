package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Enemy {

    public Sprite sprite;
    public Vector2 position;
    float speed = 500f;
    int health;
    public Rectangle boundingBox;

    public Enemy () {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 0.5f, 0.5f, 1);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);
        this.health = 100;
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 3, (Gdx.graphics.getHeight() - sprite.getHeight()) / 3 );

        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta) {
        boundingBox.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch, float delta) {
        update(delta);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
