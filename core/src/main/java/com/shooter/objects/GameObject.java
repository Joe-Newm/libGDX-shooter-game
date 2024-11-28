package com.shooter.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    public Vector2 position;
    public Sprite sprite;
    public Rectangle boundingBox;

    public GameObject(int x, int y,String color) {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        if (color == "blue"){

            pixmap.setColor(Color.BLUE);
        } else {

            pixmap.setColor(Color.WHITE);
        }
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        sprite = new Sprite(tex);
        position = new Vector2(x, y);
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    public void update() {

    }

    public void draw(Batch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
