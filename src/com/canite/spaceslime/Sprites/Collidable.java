package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Types.ColBody;

/**
 * Created by austin on 2/10/16.
 */
public abstract class Collidable extends GameObject {
    public SpriteBody horiLeftBody;
    public SpriteBody horiRightBody;
    public SpriteBody vertTopBody;
    public SpriteBody vertBotBody;
    public boolean checkCollisions = true;
    /* int body is 0 for horizontal collision, 1 for vertical collision */
    public abstract void collide(ColBody type, Collidable colObj);
    public void updateColBox() {
        horiLeftBody.colBox.setPosition(getX() + horiLeftBody.xOffset, getY() + horiLeftBody.yOffset);
        horiRightBody.colBox.setPosition(getX() + horiRightBody.xOffset, getY() + horiRightBody.yOffset);
        vertBotBody.colBox.setPosition(getX() + vertBotBody.xOffset, getY() + vertBotBody.yOffset);
        vertTopBody.colBox.setPosition(getX() + vertTopBody.xOffset, getY() + vertTopBody.yOffset);
    }
}
