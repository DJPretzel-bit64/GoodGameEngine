package engine.entity;

import engine.physics.Hitbox;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class World extends BasicEntity {

	protected	final	File			worldCSV;
	protected	final	int				tileSize;
	protected 			BufferedImage[]	tiles;

	public World(BufferedImage tilemap, File worldCSV, int tileSize) {
		super(new Vec2(), new Vec2(), tilemap, new ArrayList<>());

		this.tileSize = tileSize;
		this.texture = tilemap;
		this.worldCSV = worldCSV;
		try {
			createWorld();
		} catch(IOException e) {
			System.out.println("Error loading world file: " + e);
		}
	}

	private void createWorld() throws IOException {
		Scanner scanner = new Scanner(worldCSV);

		tiles = new BufferedImage[(texture.getWidth() / tileSize) * (texture.getHeight() / tileSize)];
		int index = 0;
		for(int i = 0; i < texture.getHeight(); i += tileSize) {
			for(int j = 0; j < texture.getWidth(); j += tileSize) {
				tiles[index] = texture.getSubimage(j, i, tileSize, tileSize);
				index ++;
			}
		}

		ArrayList<int[]> worldData = new ArrayList<>();
		int width = 0, height = 0;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] dataString = line.split(",");
			int length = dataString.length;
			int[] dataInt = new int[length];
			width = Math.max(width, length);
			for(int i = 0; i < length; i++) {
				dataInt[i] = Integer.parseInt(dataString[i]);
			}
			worldData.add(dataInt);
			height++;
		}

		texture = new BufferedImage(width * tileSize, height * tileSize, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = texture.getGraphics();

		for(int i = 0; i < worldData.size(); i++) {
			for(int j = 0; j < worldData.get(i).length; j++) {
				try {
					doStuffWithData(worldData.get(i)[j], j * tileSize, i * tileSize, g);
				} catch(Exception ignored) {}
			}
		}

		g.dispose();

		size = new Vec2(width * tileSize, height * tileSize);
		pos = new Vec2((width - 1) * tileSize * 0.5, (height - 1) * tileSize * 0.5);
	}

	protected void doStuffWithData(int data, int x, int y, Graphics g) {
		if(data != -1)
			hitboxes.add(new Hitbox(new Vec2(x, y), new Vec2(tileSize)));
		g.drawImage(tiles[data], x, y, null);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}
}
