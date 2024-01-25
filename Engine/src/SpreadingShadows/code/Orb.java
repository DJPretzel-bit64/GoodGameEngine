package SpreadingShadows.code;

import engine.entity.BasicEntity;
import engine.utility.Input;
import engine.utility.Vec2;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Orb extends BasicEntity {

	CustomWorld customWorld;

	public Orb(Vec2 velocity, Vec2 pos, CustomWorld customWorld) {
        super(pos, new Vec2(4, 4), null, new ArrayList<>(), 1, true, false);
		try {
			this.texture = ImageIO.read(new File("SpreadingShadows/res/orb.png"));
		}catch(IOException ignored) {}
		this.velocity = new Vec2(velocity);
		this.layer = 2;
		this.customWorld = customWorld;
		init();
    }

    @Override
    public void update(double delta, Input input) {
		velocity.y += 500 * delta * delta;
        if(lastCollisions.contains(Mar.class.getName()) || lastCollisions.contains(CustomWorld.class.getName())) {
			customWorld.removeMar(this);
		}
    }
}
