package engine.physics;

import engine.entity.Entity;

import java.util.ArrayList;

public interface PhysicsEngine {
	void checkCollisions(ArrayList<Entity> entities);
}
