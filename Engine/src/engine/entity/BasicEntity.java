package engine.entity;

import engine.physics.Hitbox;
import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static engine.Engine.*;

public class BasicEntity implements Entity {

	protected Vec2 pos, size, velocity = new Vec2();
	protected BufferedImage texture;
	protected int id;
	protected ArrayList<Hitbox> hitboxes = new ArrayList<>();
	protected ArrayList<String> collidesWith;
	protected ArrayList<String> lastCollisions = new ArrayList<>();
	protected ArrayList<Entity> lastCollisionEntities = new ArrayList<>();
	protected int layer;
	protected boolean checkCollisions;

	public BasicEntity(Vec2 pos, Vec2 size, BufferedImage texture, ArrayList<String> collidesWith, int layer, boolean checkCollisions) {
		this.pos = pos;
		this.size = size;
		this.texture = texture;
		this.collidesWith = new ArrayList<>(collidesWith);
		Random random = new Random(System.nanoTime());
		this.id = random.nextInt();
		hitboxes.add(new Hitbox(pos, size));
		this.layer = layer;
		this.checkCollisions = checkCollisions;
	}

	@Override
	public Vec2 getPos() {
		return new Vec2(this.pos);
	}

	@Override
	public void setPos(Vec2 pos) {
		for(Hitbox hitbox : hitboxes)
			hitbox.setPos(hitbox.getPos().minus(this.pos).plus(pos));
		this.pos = pos;
	}

	@Override
	public void	setSize(Vec2 size) {
		this.size = new Vec2(size);
		this.hitboxes.getFirst().setSize(new Vec2(size));
	}

	@Override
	public Vec2 getVelocity() {
		return new Vec2(this.velocity);
	}

	@Override
	public void setVelocity(Vec2 velocity) {
		this.velocity = velocity;
	}

	@Override
	public ArrayList<String> getCollidesWith() {
		return new ArrayList<>(collidesWith);
	}

	@Override
	public ArrayList<Hitbox> getHitboxes() {
		return new ArrayList<>(this.hitboxes);
	}

	@Override
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public ArrayList<String> getLastCollisions() {
		return new ArrayList<>(lastCollisions);
	}

	@Override
	public void addToLastCollisions(String collision) {
		lastCollisions.add(collision);
	}

	@Override
	public ArrayList<Entity> getLastCollisionEntities() {
		return new ArrayList<>(lastCollisionEntities);
	}

	@Override
	public void addToLastCollisionEntities(Entity entity) {
		lastCollisionEntities.add(entity);
	}

	@Override
	public boolean checkCollisions() {
		return this.checkCollisions;
	}

	@Override
	public void init() {
	}

	@Override
	public void render(Graphics g) {
		renderImage(g, texture, pos, size);
		if(debug) {
			g.setColor(Color.ORANGE);
			for (Hitbox hitbox : hitboxes) {
				if(overview) {
					Vec2 offset = hitbox.getPos().minus(hitbox.getSize().divide(2));
					g.drawRect(offset.xi(), offset.yi(), hitbox.getSize().xi(), hitbox.getSize().yi());
				}
				else {
					Vec2 offset = hitbox.getPos().minus(cameraPos).minus(hitbox.getSize().divide(2));
					g.drawRect(offset.xi() * scale + width / 2, offset.yi() * scale + height / 2, hitbox.getSize().xi() * scale, hitbox.getSize().yi() * scale);
				}
			}
		}
	}

	protected void renderImage(Graphics g, BufferedImage texture, Vec2 pos, Vec2 size) {
		Vec2 offset = pos.minus(cameraPos).minus(size.divide(2));
		g.drawImage(texture, offset.xi() * scale + width / 2, offset.yi() * scale + height / 2, size.xi() * scale, size.yi() * scale, null);
	}

	@Override
	public void update(double delta, Input input) {}

	@Override
	public String toString() {
		return getClass().getName() + ":" + id;
	}
}
