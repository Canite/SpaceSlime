package com.canite.spaceslime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.canite.spaceslime.Screens.PlayScreen;

public class SpaceSlime extends Game {
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public static final float PPM = 1f;

	public SpriteBatch batch;
	public int currentLevel, maxLevel;
	
	@Override
	public void create () {
		currentLevel = 1;
		maxLevel = 1;
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
