package com.canite.spaceslime.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.Sprites.Ground;
import com.canite.spaceslime.Sprites.Slime;
import com.canite.spaceslime.World.World;

/**
 * Created by Austin on 2/3/2016.
 */
public class LevelCreator {
    public LevelCreator(TiledMap map, PlayScreen screen, World world) {
        for (MapObject object: map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //Gdx.app.log("creator", Float.toString(rect.getX()) + " " + Float.toString(rect.getY()));
            Slime player = new Slime(world, screen, (int)rect.getX(), (int)rect.getY());
            world.insertObject(player);
            world.player = player;
        }

        for (MapObject object: map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //Gdx.app.log("creator", Float.toString(rect.getX()) + " " + Float.toString(rect.getY()));
            world.insertObject(new Ground(rect));
        }
    }
}
