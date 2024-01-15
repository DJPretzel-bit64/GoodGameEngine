package engine.entity;

import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TopDown extends BasicEntity {

	private final double speed;

	public TopDown(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, double speed) {
		super(pos, size, texture, collidesWith, layer);
		this.speed = speed;
	}

	@Override
	public void update(double delta, Input input) {
		double speed = this.speed * delta;

		if(input.up)
			velocity.y = -1;
		else if(input.down)
			velocity.y = 1;
		else
			velocity.y = 0;
		if(input.left)
			velocity.x = -1;
		else if(input.right)
			velocity.x = 1;
		else
			velocity.x = 0;

		velocity = velocity.normal().times(speed);
	}
}
