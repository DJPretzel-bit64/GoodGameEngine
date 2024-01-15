package engine.physics;

import engine.entity.Entity;
import engine.utility.Vec2;

import java.util.ArrayList;

import static engine.Engine.getEntities;

public class DefaultPhysicsEngine implements PhysicsEngine {

	private final int MAX_DEPTH = 10;

	@Override
	public void checkCollisions(ArrayList<Entity> entities) {
		for(Entity entity : entities) {
			checkX(entity, 0);
			checkY(entity, 0);
		}
	}

	private void checkX(Entity entity, int iteration) {
		double lastPosX = entity.getPos().x;

		try {
			entity.setPos(entity.getPos().plus(new Vec2(entity.getVelocity().x, 0)));
			for (Entity collisionEntity : getEntities()) {
				for (Hitbox hitbox : entity.getHitboxes()) {
					for (Hitbox collisionHitbox : collisionEntity.getHitboxes()) {
						if (hitbox.intersects(collisionHitbox)) {
							if(!entity.getLastCollisions().contains(collisionEntity.getClass().getName()))
								entity.addToLastCollisions(collisionEntity.getClass().getName());
							if (entity.getCollidesWith().contains(collisionEntity.getClass().getName())) {
								throw new Exception("Break");
							}
						}
					}
				}
			}
		} catch(Exception ignored) {
			entity.setPos(new Vec2(lastPosX, entity.getPos().y));
			if(iteration >= MAX_DEPTH)
				entity.setVelocity(new Vec2(0, entity.getVelocity().y));
			else {
				entity.setVelocity(new Vec2(entity.getVelocity().x / 2., entity.getVelocity().y));
				checkX(entity, iteration + 1);
			}
		}
	}

	private void checkY(Entity entity, int iteration) {
		double lastPosY = entity.getPos().y;
		try {
			entity.setPos(entity.getPos().plus(new Vec2(0, entity.getVelocity().y)));
			for (Entity collisionEntity : getEntities()) {
				for (Hitbox hitbox : entity.getHitboxes()) {
					for (Hitbox collisionHitbox : collisionEntity.getHitboxes()) {
						if (hitbox.intersects(collisionHitbox)) {
							if(!entity.getLastCollisions().contains(collisionEntity.getClass().getName()))
								entity.addToLastCollisions(collisionEntity.getClass().getName());
							if (entity.getCollidesWith().contains(collisionEntity.getClass().getName())) {
								throw new Exception("Break");
							}
						}
					}
				}
			}
		} catch(Exception ignored) {
			entity.setPos(new Vec2(entity.getPos().x, lastPosY));
			if(iteration >= MAX_DEPTH)
				entity.setVelocity(new Vec2(entity.getVelocity().x, 0));
			else {
				entity.setVelocity(new Vec2(entity.getVelocity().x, entity.getVelocity().y / 2));
				checkY(entity, iteration + 1);
			}
		}
	}
}
