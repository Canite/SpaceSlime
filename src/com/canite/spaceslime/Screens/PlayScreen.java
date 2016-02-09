package com.canite.spaceslime.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Sprites.MapSprite;
import com.canite.spaceslime.Tools.LevelCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Austin on 2/3/2016.
 */
public class PlayScreen implements Screen, InputProcessor{
    // Game and textures
    private SpaceSlime game;
    private TextureAtlas atlas;

    // Cameras
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    // Map loading
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Map Properties
    public int mapWidth, mapHeight, tileWidth, tileHeight, mapPixelWidth, mapPixelHeight;
    public Array<MapSprite> mapSprites = new Array<MapSprite>();

    // Class for each touchscreen "Touch" to allow multitouch support
    public class TouchInfo {
        public int touchX = 0;
        public int touchY = 0;
        public boolean touched = false;
        public String type = "";
    }

    // Hash table for each touch
    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    public PlayScreen(SpaceSlime game) {
        Gdx.app.log("started", "play screen");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SpaceSlime.V_WIDTH / SpaceSlime.PPM, SpaceSlime.V_HEIGHT / SpaceSlime.PPM, gameCam);
        atlas = new TextureAtlas("SSpack.atlas");

        // hud goes here

        // Start loading the map
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.textureMinFilter = Texture.TextureFilter.Nearest;

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(String.format("level%d.tmx", game.currentLevel), params);

        MapProperties mapProperties = map.getProperties();
        mapWidth = mapProperties.get("width", Integer.class);
        mapHeight = mapProperties.get("height", Integer.class);
        tileWidth = mapProperties.get("tilewidth", Integer.class);
        tileHeight = mapProperties.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tileWidth;
        mapPixelHeight = mapHeight * tileHeight;

        renderer = new OrthogonalTiledMapRenderer(map, 1 / SpaceSlime.PPM);

        //gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        gameCam.position.set(mapPixelWidth/2, mapPixelHeight/2, 0);

        new LevelCreator(map, this);

        Gdx.input.setInputProcessor(this);

        for (int i = 0; i < 3; i++) {
            touches.put(i, new TouchInfo());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {

    }

    public void update(float dt) {

        for (int i = 0; i < mapSprites.size; i++) {
            mapSprites.get(i).update(dt);
        }

        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        for (int i = 0; i < mapSprites.size; i++) {
            //mapSprites.get(i).draw(game.batch);
        }
        // guy.draw(game.batch);
        game.batch.end();

        // set again for hud
        //game.batch.setProjectionMatrix();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}