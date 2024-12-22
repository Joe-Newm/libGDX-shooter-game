package com.shooter.weapons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shooter.Player;

import java.util.ArrayList;

public abstract class Weapon {
    public String name;
    public int damage;
    public Texture bulletTexture;

    public Weapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
        bulletTexture = new Texture("player/bullet.png");
    }
    public abstract void attack (Player player, OrthographicCamera camera, ArrayList<Bullet> player_bullets);
}

