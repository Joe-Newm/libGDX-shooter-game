package com.shooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public Vector2 position;
    public Vector2 direction;
    public Sprite sprite;
    public float speed_bullet = 3;
    public Rectangle boundingBox;

    public Bullet(Texture img_bullet, float startX, float startY, Vector2 targetPosition, float speed) {
        sprite = new Sprite(img_bullet);
        sprite.setScale(2);
        position = new Vector2(startX, startY);
        speed_bullet = speed;

        direction = new Vector2(targetPosition).sub(position).nor();

        // calculate the angle in degrees
        float angle = direction.angleDeg() - 90;
        sprite.setRotation(angle);

        // this allows overlaps function
        boundingBox = new Rectangle(startX, startY, sprite.getWidth(), sprite.getHeight());
    }

    public void player_update(float deltaTime) {
        position.add(direction.x * speed_bullet * deltaTime, direction.y * speed_bullet * deltaTime);
        sprite.setPosition(position.x, position.y);

        boundingBox.setPosition(position.x, position.y);
    }


    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
