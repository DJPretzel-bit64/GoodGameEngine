package engine.entity;

import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Platformer extends BasicEntity {

	private final double speed, jumpSpeed, gravity;
	private Vec2 lastVelocity = new Vec2();

	public Platformer(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, boolean checkCollisions, double speed, double jumpSpeed, double gravity) {
		super(pos, size, texture, collidesWith, layer, checkCollisions);
		this.speed = speed;
		this.jumpSpeed = jumpSpeed;
		this.gravity = gravity;
	}

	@Override
	public void update(double delta, Input input) {
		lastCollisions.clear();

		double speed = this.speed * delta;
		double jumpSpeed = -this.jumpSpeed * delta;
		double gravity = this.gravity * delta * delta;

		boolean canJump = velocity.y == 0 && lastVelocity.y == 0;
		lastVelocity = new Vec2(velocity);

		if(input.left)
			velocity.x = -speed;
		else if(input.right)
			velocity.x = speed;
		else
			velocity.x = 0;

		if(input.up && canJump)
			velocity.y = jumpSpeed;

		velocity.y += gravity;
	}
}
