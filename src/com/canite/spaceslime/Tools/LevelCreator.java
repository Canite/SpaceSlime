package com.canite.spaceslime.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.Sprites.Ground;

/**
 * Created by Austin on 2/3/2016.
 */
public class LevelCreator {
    public LevelCreator(TiledMap map, PlayScreen screen) {
        for (MapObject object: map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            // new Ground();
        }
    }
}
