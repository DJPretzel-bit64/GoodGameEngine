package SpreadingShadows.code;

import engine.entity.Platformer;
import engine.utility.Input;
import engine.utility.Timer;
import engine.utility.Vec2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CustomPlatformer extends Platformer {

	int corruptionLevel = 0;
	Timer corruptionTimer = new Timer(500, false);

	public CustomPlatformer(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, double speed, double jumpSpeed, double gravity) {
		super(pos, size, texture, collidesWith, layer, speed, jumpSpeed, gravity);
	}

	@Override
	public void update(double delta, Input input) {
		if(lastCollisions.contains(Mar.class.getName()) && corruptionTimer.expired()) {
			corruptionTimer.start();
			corruptionLevel++;
			System.out.println(corruptionLevel);
		}
		super.update(delta, input);
	}
}
