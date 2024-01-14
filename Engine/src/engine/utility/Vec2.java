package engine.utility;

public class Vec2 {

	public double x, y;

	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec2() {
		this.x = 0;
		this.y = 0;
	}

	public Vec2(Vec2 vec2) {
		this.x = vec2.x;
		this.y = vec2.y;
	}

	public Vec2(double d) {
		this.x = d;
		this.y = d;
	}

	public int xi() {
		return (int) Math.round(x);
	}

	public int yi() {
		return (int) Math.round(y);
	}

	public Vec2 plus(Vec2 v) {
		return new Vec2(x + v.x, y + v.y);
	}

	public Vec2 minus(Vec2 v) {
		return new Vec2(x - v.x, y - v.y);
	}

	public Vec2 times(double s) {
		return new Vec2(x * s, y * s);
	}

	public Vec2 divide(double s) {
		return this.times(1 / s);
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	public double length() {
		return Math.sqrt(this.lengthSquared());
	}

	public Vec2 normal() {
		double length = this.length();
		return length != 0 ? this.divide(this.length()) : new Vec2();
	}

	@Override
	public String toString() {
		return "x: " + x + " | y : " + y;
	}
}
