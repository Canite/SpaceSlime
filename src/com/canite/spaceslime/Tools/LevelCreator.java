package com.canite.spaceslime.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.Sprites.Ground;
import com.canite.spaceslime.World.World;

/**
 * Created by Austin on 2/3/2016.
 */
public class LevelCreator {
    public LevelCreator(TiledMap map, PlayScreen screen, World world) {
        for (MapObject object: map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            world.insertStatic(new Ground(rect));
        }
    }
}
