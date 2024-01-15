package SpreadingShadows.code;

import engine.Engine;
import engine.entity.Entity;
import engine.entity.World;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CustomWorld extends World {

	final int PLAYER_NUM = 0;
	Entity player;
	Mar mar;

	public CustomWorld(BufferedImage tilemap, File worldCSV, int tileSize, int layer) {
		super(tilemap, worldCSV, tileSize, layer);
	}

	@Override
	public void init() {
		mar = (Mar) Engine.getEntity("SpreadingShadows.code.Mar");
	}

	@Override
	protected void doStuffWithData(int data, int x, int y, Graphics g) {
		if(data != PLAYER_NUM)
			super.doStuffWithData(data, x, y, g);
		else {
			player = Engine.getEntity("engine.entity.Platformer");
			assert player != null;
			player.setPos(new Vec2(x, y));
			player.setSize(new Vec2(tileSize * 2));
			player.setTexture(tiles[data]);
		}
	}
}
