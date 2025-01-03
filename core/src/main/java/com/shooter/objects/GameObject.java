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
    public String image;
    public String name;

    public GameObject(int x, int y,Texture img, String objectName) {
        name = objectName;
        sprite = new Sprite(img);
        sprite.scale(1);
        position = new Vector2(x, y);
        boundingBox = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    public void update() {
        boundingBox.setPosition(position.x, position.y);
        sprite.setPosition(position.x, position.y);
    }

    public void draw(Batch batch) {
        update();
        sprite.draw(batch);
    }
}
