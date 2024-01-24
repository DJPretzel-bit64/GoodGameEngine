package engine.entity;

import engine.physics.Hitbox;
import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface Entity {
	Vec2 getPos();
	void setPos(Vec2 pos);
	void setSize(Vec2 size);
	Vec2 getVelocity();
	void setVelocity(Vec2 velocity);
	ArrayList<Hitbox> getHitboxes();
	ArrayList<String> getCollidesWith();
	void setTexture(BufferedImage texture);
	int getLayer();
	ArrayList<String> getLastCollisions();
	void addToLastCollisions(String collision);
	ArrayList<Entity> getLastCollisionEntities();
	void addToLastCollisionEntities(Entity entity);
	boolean checkCollisions();
	void init();
	void render(Graphics g);
	void update(double delta, Input input);
}
