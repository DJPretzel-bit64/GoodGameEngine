package engine.physics;

import engine.utility.Vec2;

import java.awt.*;

public class Hitbox {

	private Vec2 pos, size;

	public Hitbox(Vec2 pos, Vec2 size) {
		this.pos = pos;
		this.size = size;
	}

	public void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}

	public Vec2 getPos() {
		return new Vec2(this.pos);
	}

	public Vec2 getSize() {
		return new Vec2(this.size);
	}

	public boolean intersects(Hitbox collision) {
		Rectangle r1 = new Rectangle(this.pos.xi(), this.pos.yi(), this.size.xi(), this.size.yi());
		Rectangle r2 = new Rectangle(collision.pos.xi(), collision.pos.yi(), collision.size.xi(), collision.size.yi());
		return r1.intersects(r2);
	}
}
