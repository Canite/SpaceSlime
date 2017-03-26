package com.canite.spaceslime.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Tools.LevelCreator;
import com.canite.spaceslime.World.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Austin on 2/3/2016.
 */
public class PlayScreen implements Screen, InputProcessor{
    // Game and textures
    private SpaceSlime game;
    private TextureAtlas atlas;
    private World world;

    // Cameras
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    // Map loading
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Map Properties
    public int mapWidth, mapHeight, tileWidth, tileHeight, mapPixelWidth, mapPixelHeight;

    // Class for each touchscreen "Touch" to allow multitouch support
    public class TouchInfo {
        public int touchX = 0;
        public int touchY = 0;
        public boolean touched = false;
        public String type = "";
    }

    // Hash table for each touch
    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    // Debug
    ShapeRenderer debug;
    FPSLogger fps_logger = new FPSLogger();

    public PlayScreen(SpaceSlime game) {
        Gdx.app.log("started", "play screen");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SpaceSlime.V_WIDTH / SpaceSlime.PPM, SpaceSlime.V_HEIGHT / SpaceSlime.PPM, gameCam);
        atlas = new TextureAtlas("SSpack.atlas");

        debug = new ShapeRenderer();

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

        // Create the world and fill it with all the map objects
        world = new World(mapPixelWidth, mapPixelHeight);
        new LevelCreator(map, this, world);

        renderer = new OrthogonalTiledMapRenderer(map, 1 / SpaceSlime.PPM);

        //gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        gameCam.position.set(mapPixelWidth/2, mapPixelHeight/2, 0);

        Gdx.input.setInputProcessor(this);

        for (int i = 0; i < 5; i++) {
            touches.put(i, new TouchInfo());
        }
    }

    public TextureAtlas getAtlas() { return atlas; }

    public static void fixBleeding(TextureRegion region) {
        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + 1.0f) * invTexWidth, (y + 1.0f) * invTexHeight, (x + width - 1.0f) * invTexWidth, (y + height - 1.0f) * invTexHeight);
    }

    @Override
    public boolean keyDown(int keycode) {
        /*switch(keycode) {
            case Input.Keys.A:
                world.player.move(-30.0f);
                break;
            case Input.Keys.D:
                world.player.move(30.0f);
                break;
            case Input.Keys.W:
                world.player.jump();
            default:
                break;
        }*/
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        /*switch(keycode) {
            case Input.Keys.A:
                world.player.stop();
                break;
            case Input.Keys.D:
                world.player.stop();
                break;
            default:
                break;
        }*/
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer < 5 && touches.get(pointer).type == "") {
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer < 5) {
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer < 5  && touches.get(pointer).type.equals("move") && screenX < SpaceSlime.V_WIDTH / (3*SpaceSlime.PPM)) {
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
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
        for (int i = 0; i < 5; i++) {
            world.player.handleInput(touches.get(i));
        }
    }

    public void update(float dt) {
        world.update(dt);
        handleInput(dt);

        /* Center camera on player */
        gameCam.position.x = Math.round(world.player.getX());
        gameCam.position.y = Math.round(world.player.getY());
        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the tiles
        renderer.render();

        // Draw the objects
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        world.draw(game.batch);
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // debug
        debug.setProjectionMatrix(gameCam.combined);
        debug.begin(ShapeRenderer.ShapeType.Filled);
        world.objectTree.drawDebugObjects(debug, 0.0f);
        debug.setColor(0, 1, 0, 1);
        //Rectangle debugBox = world.player.horiRightBody.colBox;
        //debug.rect(debugBox.x, debugBox.y, debugBox.width, debugBox.height);
        //debugBox = world.player.horiLeftBody.colBox;
        //debug.rect(debugBox.x, debugBox.y, debugBox.width, debugBox.height);
        //debugBox = world.player.vertBotBody.colBox;
        //debug.rect(debugBox.x, debugBox.y, debugBox.width, debugBox.height);
        //debugBox = world.player.vertTopBody.colBox;
        //debug.rect(debugBox.x, debugBox.y, debugBox.width, debugBox.height);
        //debugBox = new Rectangle(world.player.vertBotBody.colBox.x, world.player.vertBotBody.colBox.y - 2, world.player.vertBotBody.colBox.width, 2);
        //debug.rect(debugBox.x, debugBox.y, debugBox.width, debugBox.height);
        debug.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        debug.setProjectionMatrix(gameCam.combined);
        debug.begin(ShapeRenderer.ShapeType.Line);
        world.objectTree.drawDebugQuads(debug);
        debug.end();

        fps_logger.log();

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
        world.dispose();
        map.dispose();
        renderer.dispose();
    }
}
