package com.shooter.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shooter.Player;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Pistol extends Weapon {
    public Pistol() {
        super("Pistol", 1);
    }

    @Override
    public void attack(Player player, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {

        // Get sprite rotation in radians
        float rotationRad = (float) Math.toRadians(player.sprite.getRotation());
        Vector2 bulletDirection = new Vector2((float) Math.cos(rotationRad + 1.56), (float) Math.sin(rotationRad + 1.56));

        // Calculate the offset to the top center of the sprite
        float offsetX = 0; // Top center has no X offset relative to the sprite's width
        float offsetY = player.sprite.getHeight() * (2 / 3f) + 38; // From the custom origin to the top

        // Rotate the offset around the sprite's rotation
        float rotatedOffsetX = (float) (offsetX * Math.cos(rotationRad) - offsetY * Math.sin(rotationRad));
        float rotatedOffsetY = (float) (offsetX * Math.sin(rotationRad) + offsetY * Math.cos(rotationRad));

        // Calculate the tip position in world coordinates
        float tipX = player.position.x + player.sprite.getOriginX() + rotatedOffsetX;
        float tipY = player.position.y + player.sprite.getOriginY() + rotatedOffsetY;

        // Create the bullet at the tip position
        Bullet bullet = new Bullet(bulletTexture, tipX, tipY, bulletDirection, 1000, "pistol");
        player_bullets.add(bullet);
    }
}
