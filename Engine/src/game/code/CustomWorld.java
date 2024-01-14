package game.code;

import engine.Engine;
import engine.entity.Entity;
import engine.entity.World;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class CustomWorld extends World {

	final int PLAYER_DATA_NUM = 0;

	public CustomWorld(BufferedImage tilemap, File worldCSV, int tileSize) {
		super(tilemap, worldCSV, tileSize);
	}

	@Override
	protected void doStuffWithData(int data, int x, int y, Graphics g) {
		if(data != PLAYER_DATA_NUM)
			super.doStuffWithData(data, x, y, g);
		else {
			Entity player = Objects.requireNonNull(Engine.getEntity("engine.entity.Platformer"));
			player.setPos(new Vec2(x, y));
			player.setSize(new Vec2(tileSize));
			player.setTexture(tiles[data]);
		}
	}
}
