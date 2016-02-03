package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Austin on 2/3/2016.
 *
 * MapSprites are entities which move around the world but are placed in the Tiled map
 */
public abstract class MapSprite extends Sprite {
    public abstract void update(float dt);
}
