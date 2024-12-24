package com.shooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shooter.Player;

import static com.shooter.Logic.VIRTUAL_HEIGHT;
import static com.shooter.Logic.VIRTUAL_WIDTH;

public class HudRenderer {
    private BitmapFont font;
    private OrthographicCamera hudCamera;
    private Texture healthBarTexture;
    private Texture healthBarTexture1;
    private Sprite healthBarSprite;
    private Sprite healthBarSprite1;
    private Texture coinTexture;
    private Sprite coinSprite;
    private float healthBarWidth = 300;
    private float healthBarHeight = 25;
    public float currentDisplayedHealth;

    public HudRenderer(Player player) {
        font = new BitmapFont();
        font.getData().setScale(3);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // health bar
        Pixmap pixmapHealth = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth.setColor(0, 1, 0, 1);
        pixmapHealth.fill();
        healthBarTexture = new Texture(pixmapHealth);
        healthBarSprite = new Sprite(healthBarTexture);

        // coin ui
        coinTexture = new Texture(Gdx.files.internal("objects/coin.png"));
        coinSprite = new Sprite(coinTexture);
        coinSprite.scale(3);

        // health bar background
        Pixmap pixmapHealth1 = new Pixmap((int) healthBarWidth, (int) healthBarHeight, Pixmap.Format.RGBA8888);
        pixmapHealth1.setColor(1, 0, 0, 1);
        pixmapHealth1.fill();
        healthBarTexture1 = new Texture(pixmapHealth1);
        healthBarSprite1 = new Sprite(healthBarTexture1);
        currentDisplayedHealth = player.currentHealth;

        pixmapHealth1.dispose();
        pixmapHealth.dispose();

    }

    public void draw(SpriteBatch batch, Player player, float delta) {
        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);

        // update health bar position
        healthBarSprite.setPosition( 50, 1080 - 50);
        healthBarSprite1.setPosition(50, 1080 - 50);
        // update coin ui position
        coinSprite.setPosition(50, 1080 - 125);

        // update health bar
        if (player.currentHealth > 0) {
            float healthChangeSpeed = 5.0f;
            currentDisplayedHealth += (player.currentHealth - currentDisplayedHealth) * healthChangeSpeed * delta;

            healthBarSprite.setSize((currentDisplayedHealth / (float) player.maxHealth) * healthBarWidth , healthBarHeight);
        } else {
            healthBarSprite.setSize(0, healthBarHeight);
        }



        batch.begin();

        // Draw coins
        font.draw(batch, "x" + player.currentCoins, 100,  1080 - 85);
        healthBarSprite1.draw(batch);
        healthBarSprite.draw(batch);
        coinSprite.draw(batch);
        batch.end();
    }
}


