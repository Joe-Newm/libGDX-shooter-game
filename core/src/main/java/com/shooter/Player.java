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


public class Player {
    public Sprite sprite;
    public Vector2 position;
    float speed = 300f;
    public Rectangle boundingBox;
    private Texture healthBarTexture;
    private Texture healthBarTexture1;
    private Sprite healthBarSprite;
    private Sprite healthBarSprite1;
    private float healthBarWidth = 50;
    private float healthBarHeight = 5;
    public int maxHealth = 1000;
    public int currentHealth = 1000;


    public Player () {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);
        position = new Vector2( (Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2 );
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());

        // health bar
        Pixmap pixmapHealth = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth.setColor(0, 1, 0, 1);
        pixmapHealth.fill();
        healthBarTexture = new Texture(pixmapHealth);
        healthBarSprite = new Sprite(healthBarTexture);

        // healthbar background
        Pixmap pixmapHealth1 = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth1.setColor(1, 0, 0, 1);
        pixmapHealth1.fill();
        healthBarTexture1 = new Texture(pixmapHealth1);
        healthBarSprite1 = new Sprite(healthBarTexture1);

        pixmapHealth1.dispose();
        pixmapHealth.dispose();
        pixmap.dispose();
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

        //update health bar position
        healthBarSprite.setPosition(position.x, position.y + sprite.getHeight() + 20);
        healthBarSprite1.setPosition(position.x, position.y + sprite.getHeight() + 20);
        // update health bar
        if (currentHealth > 0) {
            healthBarSprite.setSize((currentHealth / (float) maxHealth) * healthBarWidth, healthBarHeight);
        } else {
            healthBarSprite.setSize(0, healthBarHeight);
        }

        boundingBox.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch, float delta) {
        update(delta);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        healthBarSprite1.draw(batch);
        healthBarSprite.draw(batch);
    }
}
