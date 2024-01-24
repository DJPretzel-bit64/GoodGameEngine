package engine.physics;

import engine.utility.Vec2;

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
		Vec2 thatOffset = that.pos.minus(that.size.divide(2));
		double tw = this.size.x;
		double th = this.size.y;
		double rw = that.size.x;
		double rh = that.size.y;
		if (rw > 0 && rh > 0 && tw > 0 && th > 0) {
			double tx = thisOffset.x;
			double ty = thisOffset.y;
			double rx = thatOffset.x;
			double ry = thatOffset.y;
			rw += rx;
			rh += ry;
			tw += tx;
			th += ty;
			return (rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry);
		} else {
			return false;
		}
	}
}
