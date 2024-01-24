package SpreadingShadows.code;

import engine.entity.BasicEntity;
import engine.physics.Hitbox;
import engine.utility.Vec2;

import java.util.ArrayList;

public class Mar extends BasicEntity {

	public Mar() {
		super(new Vec2(), new Vec2(), null, new ArrayList<>(), 1);
	}

	public void addMar(int x, int y) {
		hitboxes.add(new Hitbox(new Vec2(x, y - 8), new Vec2(16, 1)));
	}

	public void removeMar(Vec2 pos) {
		hitboxes.removeIf(hitbox -> hitbox.getPos().equals(pos));
	}

	public void clearMar() {
		hitboxes.clear();
	}
}
