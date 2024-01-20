package SpreadingShadows.code;

import engine.entity.Platformer;
import engine.utility.Input;
import engine.utility.Timer;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CustomPlatformer extends Platformer {

	int corruptionLevel = 0;
	Timer corruptionTimer = new Timer(500, false);
	BufferedImage[] corruption = new BufferedImage[5];

	public CustomPlatformer(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, double speed, double jumpSpeed, double gravity) {
		super(pos, size, texture, collidesWith, layer, speed, jumpSpeed, gravity);
		corruption[0] = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i = 0; i < 4; i++)
			corruption[i + 1] = texture.getSubimage(0, i * 16 + 32, 16, 16);
	}

	@Override
	public void update(double delta, Input input) {
		if(corruptionTimer.expired()) {
			corruptionTimer.start();
			if (lastCollisions.contains(Mar.class.getName())) {
				corruptionLevel = Math.min(corruptionLevel + 1, 5);
			} else
				corruptionLevel = Math.max(corruptionLevel - 1, 0);
		}
		super.update(delta, input);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		renderImage(g, corruption[Math.min(corruptionLevel, 4)], pos, size);
	}
}
