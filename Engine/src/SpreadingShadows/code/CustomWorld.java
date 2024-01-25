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
	ArrayList<Entity> marList = new ArrayList<>();
	Timer corruptionTimer = new Timer(1000, true);
	boolean playerCreated = false;
	Random random = new Random(System.nanoTime());

	public CustomWorld(BufferedImage tilemap, boolean checkCollisions, boolean shouldInitiate, File worldCSV, int tileSize, int layer) {
		super(tilemap, checkCollisions, shouldInitiate, worldCSV, tileSize, layer);
	}

	@Override
	public void init() {
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
			Mar mar = new Mar(x, y, tileSize);
			marList.add(mar);
			Engine.addEntity(mar);
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
				if(!contains(MAR_NUMS, data) && data != -1 && data != PLAYER_NUM && checkSurroundings(i, j)) {
					if(random.nextInt(FREQUENCY) == 0)
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
		Engine.removeEntities(marList);
		marList.clear();
		hitboxes.clear();
		renderWorld();
	}

	boolean checkSurroundings(int i, int j) {
		int maxI = worldData.size() - 1;
		int maxJ = worldData.getFirst().length - 1;

		for(int k = -1; k <= 1; k++) {
			for(int l = -1; l <= 1; l++) {
				if(contains(MAR_NUMS, worldData.get(clamp(i + k, 0, maxI))[clamp(j + l, 0, maxJ)]) && (k != 0 && j != 0))
					return true;
			}
		}

		return false;
	}

	public boolean contains(int[] nums, int data) {
		for(int num : nums)
			if(num == data)
				return true;
		return false;
	}

	public void removeMar(Orb orb) {
		Engine.removeEntity(orb);
		for(Entity entity : orb.getLastCollisionEntities()) {
			if(entity instanceof Mar mar) {
				Engine.removeEntity(mar);
				removeMarFromWorld(mar.x, mar.y);
				Engine.removeEntities(marList);
				marList.clear();
				hitboxes.clear();
				renderWorld();
				break;
			}
		}
	}

	private void removeMarFromWorld(int x, int y) {
		int maxY = worldData.size() - 1;
		int maxX = worldData.getFirst().length - 1;

		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				int relY = clamp(y + j, 0, maxY);
				int relX = clamp(x + i, 0, maxX);
				if(contains(MAR_NUMS, worldData.get(relY)[relX]))
					worldData.get(relY)[relX] -= 5;
			}
		}
	}
}
