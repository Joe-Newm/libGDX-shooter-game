package com.shooter.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shooter.Player;

import java.util.ArrayList;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun", 2, 2);
    }

    @Override
    public void attack(Player player, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {

        // Get sprite rotation in radians
        float rotationRad = (float) Math.toRadians(player.sprite.getRotation());
        Vector2 bulletDirection = new Vector2((float) Math.cos(rotationRad + 1.56), (float) Math.sin(rotationRad + 1.56));

        // Calculate the offset to the top center of the sprite
        float offsetX = 0;
        float offsetY = player.sprite.getHeight() * (2 / 3f) + 38;

        // Rotate the offset around the sprite's rotation
        float rotatedOffsetX = (float) (offsetX * Math.cos(rotationRad) - offsetY * Math.sin(rotationRad));
        float rotatedOffsetY = (float) (offsetX * Math.sin(rotationRad) + offsetY * Math.cos(rotationRad));

        // Calculate the tip position in world coordinates
        float tipX = player.position.x + player.sprite.getOriginX() + rotatedOffsetX;
        float tipY = player.position.y + player.sprite.getOriginY() + rotatedOffsetY;

        // Define the spread angle and number of bullets
        int numBullets = 5; // Number of bullets in the spread
        float spreadAngle = 30f; // Total spread angle in degrees

        for (int i = 0; i < numBullets; i++) {
            // Calculate the angle offset for this bullet
            float angleOffset = (i - (numBullets - 1) / 2f) * (spreadAngle / (numBullets - 1));

            // Create a direction vector for this bullet
            Vector2 spreadDirection = new Vector2(
                bulletDirection.x,
                bulletDirection.y
            ).nor().rotateDeg(angleOffset);

            // Create the bullet at the tip position
            Bullet bullet = new Bullet(bulletTexture, tipX, tipY, spreadDirection, 1000, "shotgun");
            player_bullets.add(bullet);
        }
    }}
