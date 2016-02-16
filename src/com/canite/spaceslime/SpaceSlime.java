package com.canite.spaceslime;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.canite.spaceslime.Screens.PlayScreen;

public class SpaceSlime extends Game {
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;
	public static final float PPM = 1;

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
