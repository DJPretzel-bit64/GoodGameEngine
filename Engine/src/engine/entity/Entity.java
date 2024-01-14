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
	Vec2 getVelocity();
	ArrayList<Hitbox> getHitboxes();
	ArrayList<String> getCollidesWith();
	void setTexture(BufferedImage texture);
	void render(Graphics g);
	void update(double delta, Input input);
}
