package engine.physics;

import engine.entity.Entity;
import engine.utility.Vec2;

import java.util.ArrayList;

import static engine.Engine.getEntities;

public class DefaultPhysicsEngine implements PhysicsEngine {

	@Override
	public void checkCollisions(ArrayList<Entity> entities) {
		for(Entity entity : entities) {
			Vec2 lastPos = new Vec2(entity.getPos());

			try {
				entity.setPos(entity.getPos().plus(new Vec2(entity.getVelocity().x, 0)));
				for (Entity collisionEntity : getEntities())
					if (entity.getCollidesWith().contains(collisionEntity.getClass().getName()))
						for (Hitbox hitbox : entity.getHitboxes())
							for (Hitbox hitbox1 : collisionEntity.getHitboxes())
								if (hitbox.intersects(hitbox1))
									throw new Exception("Break");
			} catch(Exception ignored) {
				entity.setPos(new Vec2(lastPos.x, entity.getPos().y));
			}

			try {
				entity.setPos(entity.getPos().plus(new Vec2(0, entity.getVelocity().y)));
				for (Entity collisionEntity : getEntities())
					if (entity.getCollidesWith().contains(collisionEntity.getClass().getName()))
						for (Hitbox hitbox : entity.getHitboxes())
							for (Hitbox hitbox1 : collisionEntity.getHitboxes())
								if (hitbox.intersects(hitbox1))
									throw new Exception("Break");

			} catch(Exception ignored) {
				entity.setPos(new Vec2(entity.getPos().x, lastPos.y));
			}
		}
	}
}
