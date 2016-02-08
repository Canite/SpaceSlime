package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.World.World;

import java.util.HashMap;

/**
 * Created by Austin on 2/7/2016.
 */
public class Slime extends Sprite{
    public enum State {FALLING, JUMPING, LANDING, STANDING, RUNNING, HOOKING};
    public State currentState, previousState;
    /* Physics body used in world physics calculations and collisions */
    public SpriteBody body;
    public float x, y;
    private HashMap<State, Animation> animations;
    private World world;
    private PlayScreen screen;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;


    }
}
