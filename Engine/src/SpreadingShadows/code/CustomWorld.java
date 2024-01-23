package SpreadingShadows.code;

import engine.Engine;
import engine.entity.Entity;
import engine.entity.World;
import engine.utility.Input;
import engine.utility.Timer;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.clamp;

public class CustomWorld extends World {

	final int PLAYER_NUM = 10;
	final int FREQUENCY = 3;
	final int[] MAR_NUMS = {5, 6, 7, 8, 9, 16, 17, 18, 26, 27, 28, 36, 37, 38, 47, 57};
	Entity player;
	Mar mar;
	Timer corruptionTimer = new Timer(500, true);
	boolean playerCreated = false;
	Random random = new Random(System.nanoTime());

	public CustomWorld(BufferedImage tilemap, File worldCSV, int tileSize, int layer) {
		super(tilemap, worldCSV, tileSize, layer);
	}

	@Override
	public void init() {
		mar = (Mar) Engine.getEntity("SpreadingShadows.code.Mar");
		corruptionTimer.start();
		super.init();
	}

	@Override
	protected void doStuffWithData(int data, int x, int y, Graphics g) {
		if(data == PLAYER_NUM) {
			if(!playerCreated) {
				player = Engine.getEntity("SpreadingShadows.code.CustomPlatformer");
				assert player != null;
				player.setPos(new Vec2(x, y));
				player.setSize(new Vec2(tileSize));
				player.setTexture(tiles[data]);
				playerCreated = true;
			}
		}
		else if(contains(MAR_NUMS, data)) {
			super.doStuffWithData(data, x, y, g);
			mar.addMar(x, y);
		}
		else
			super.doStuffWithData(data, x, y, g);
	}

	@Override
	public void update(double delta, Input input) {
		if(corruptionTimer.expired())
			spread();
	}

	private void spread() {
		ArrayList<int[]> newWorldData = new ArrayList<>();
        for (int[] data : worldData) {
            int[] line = new int[data.length];
            System.arraycopy(data, 0, line, 0, line.length);
			newWorldData.add(line);
        }

		for(int i = 0; i < newWorldData.size(); i++) {
			int[] line = newWorldData.get(i);
			for(int j = 0; j < line.length; j++) {
				int data = line[j];
				if(!contains(MAR_NUMS, data) && data != -1 && data != PLAYER_NUM) {
					if(checkSurroundings(i, j) && random.nextInt(FREQUENCY) == 0)
						line[j] = data + 5;
				}
			}
		}
		worldData = new ArrayList<>();
		for (int[] data : newWorldData) {
			int[] line = new int[data.length];
			System.arraycopy(data, 0, line, 0, line.length);
			worldData.add(line);
		}
		mar.clearMar();
		hitboxes.clear();
		renderWorld();
	}

	boolean checkSurroundings(int i, int j) {
		boolean mar;
		int maxI = worldData.size() - 1;
		int maxJ = worldData.getFirst().length;

		mar = contains(MAR_NUMS, worldData.get(clamp(i + 1, 0, maxI))[clamp(j - 1, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i + 1, 0, maxI))[clamp(j, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i + 1, 0, maxI))[clamp(j + 1, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i, 0, maxI))[clamp(j - 1, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i, 0, maxI))[clamp(j + 1, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i - 1, 0, maxI))[clamp(j - 1, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i - 1, 0, maxI))[clamp(j, 0, maxJ)]);
		mar = mar || contains(MAR_NUMS, worldData.get(clamp(i - 1, 0, maxI))[clamp(j + 1, 0, maxJ)]);

		return mar;
	}

	public boolean contains(int[] nums, int data) {
		for(int num : nums)
			if(num == data)
				return true;
		return false;
	}
}
