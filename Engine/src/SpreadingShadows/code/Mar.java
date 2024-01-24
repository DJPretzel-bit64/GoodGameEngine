package SpreadingShadows.code;

import engine.entity.BasicEntity;
import engine.utility.Vec2;

import java.util.ArrayList;

public class Mar extends BasicEntity {

	public int x, y;

	public Mar(int x, int y, int tileSize) {
		super(new Vec2(x, y - 9), new Vec2(16, 1), null, new ArrayList<>(), 1, false);
		this.x = x / tileSize;
		this.y = y / tileSize;
	}
}
