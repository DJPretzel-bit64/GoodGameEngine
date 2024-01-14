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

	public void setSize(Vec2 size) {
		this.size = new Vec2(size);
	}

	public Vec2 getSize() {
		return new Vec2(this.size);
	}

	public boolean intersects(Hitbox that) {
		Vec2 thisOffset = this.pos.minus(this.size.divide(2));
		Rectangle r1 = new Rectangle(thisOffset.xi(), thisOffset.yi(), this.size.xi(), this.size.yi());
		Vec2 thatOffset = that.pos.minus(that.size.divide(2));
		Rectangle r2 = new Rectangle(thatOffset.xi(), thatOffset.yi(), that.size.xi(), that.size.yi());
		return r1.intersects(r2);
	}
}
