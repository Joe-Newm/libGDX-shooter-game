package com.shooter.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shooter.Bullet;
import com.shooter.Player;

import java.util.ArrayList;

public class Pistol extends Weapon {
    public Pistol() {
        super("Pistol", 1);
    }

    @Override
    public void attack(Player player, OrthographicCamera camera, ArrayList<Bullet> player_bullets) {
        // Get mouse position in world coordinates
        Vector3 clickPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 worldClickPos3d = camera.unproject(clickPosition);
        Vector2 worldClickPosition = new Vector2(worldClickPos3d.x, worldClickPos3d.y);

        // Get sprite rotation in radians
        float rotationRad = (float) Math.toRadians(player.sprite.getRotation());

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
        Bullet bullet = new Bullet(bulletTexture, tipX, tipY, worldClickPosition, 1000);
        player_bullets.add(bullet);
    }
}
