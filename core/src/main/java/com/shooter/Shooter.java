package com.shooter;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Shooter extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Texture bulletTexture;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Player player;
    private ArrayList<Bullet> player_bullets;
    private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;


    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        bulletTexture = new Texture("bullet.png");
        player = new Player();
        player_bullets = new ArrayList<>();

        // init camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // update camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //check for click and create bullet
        if (Gdx.input.justTouched()) {
            Vector3 clickPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 worldClickPos3d = camera.unproject(clickPosition);
            Vector2 worldClickPosition = new Vector2(worldClickPos3d.x, worldClickPos3d.y);

            Bullet bullet = new Bullet(bulletTexture, player.position.x, player.position.y, worldClickPosition, 1000);
            player_bullets.add(bullet);
        }

        batch.begin();
        player.draw(batch,delta);
        for (Bullet bullet : player_bullets) {
            bullet.player_update(delta);
            bullet.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
