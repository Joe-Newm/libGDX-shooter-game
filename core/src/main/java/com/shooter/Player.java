package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Player {
    public Sprite sprite;
    public Vector2 position;
    float speed = 500f;

    public Player () {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);

        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2 );
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            position.x -= delta * speed;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            position.x += delta * speed;
        }
    }

    public void draw(SpriteBatch batch, float delta) {
        update(delta);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
