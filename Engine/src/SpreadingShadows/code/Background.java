package SpreadingShadows.code;

import engine.Engine;
import engine.entity.BasicEntity;
import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static engine.Engine.*;

public class Background extends BasicEntity {

	CustomPlatformer player;

	public Background(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, boolean checkCollisions, boolean shouldInitiate) {
		super(pos, size, texture, collidesWith, layer, checkCollisions, shouldInitiate);
	}

	@Override
	public void init() {
		super.init();
		player = (CustomPlatformer) Engine.getEntity("SpreadingShadows.code.CustomPlatformer");
	}

	@Override
	public void update(double delta, Input input) {
		this.pos = player.getPos().divide(-4).mod(16);
	}

	@Override
	public void render(Graphics g) {
		int posX = pos.xi() * scale;
		int posY = pos.yi() * scale;
		for(int i = 0; i < width + scale * 16; i += scale * 16) {
			for(int j = 0; j < height + scale * 16; j += scale * 16) {
				g.drawImage(texture, posX + i, posY + j, 16 * scale, 16 * scale, null);
			}
		}
	}
}
