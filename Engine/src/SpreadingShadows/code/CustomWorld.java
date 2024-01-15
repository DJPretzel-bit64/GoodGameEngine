package SpreadingShadows.code;

import engine.Engine;
import engine.entity.Entity;
import engine.entity.World;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CustomWorld extends World {

	final int PLAYER_NUM = 10;
	final int[] MAR_NUMS = {5, 6, 7, 8, 9, 16, 17, 18, 26, 27, 28, 36, 37, 38, 47, 57};
	Entity player;
	Mar mar;

	public CustomWorld(BufferedImage tilemap, File worldCSV, int tileSize, int layer) {
		super(tilemap, worldCSV, tileSize, layer);
	}

	@Override
	public void init() {
		mar = (Mar) Engine.getEntity("SpreadingShadows.code.Mar");
		super.init();
	}

	@Override
	protected void doStuffWithData(int data, int x, int y, Graphics g) {
		if(data == PLAYER_NUM) {
			player = Engine.getEntity("SpreadingShadows.code.CustomPlatformer");
			assert player != null;
			player.setPos(new Vec2(x, y));
			player.setSize(new Vec2(tileSize));
			player.setTexture(tiles[data]);
		}
		else if(contains(MAR_NUMS, data)) {
			super.doStuffWithData(data, x, y, g);
			mar.addMar(x, y);
		}
		else
			super.doStuffWithData(data, x, y, g);
	}

	public boolean contains(int[] nums, int data) {
		for(int num : nums)
			if(num == data)
				return true;
		return false;
	}
}
