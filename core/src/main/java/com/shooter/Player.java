package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;


public class Player {
    public Sprite sprite;
    public Vector2 position;
    float speed = 300f;

    public Player () {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2 );
    }

    public void update(float delta) {
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
    }

    public void draw(SpriteBatch batch, float delta) {
        update(delta);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
