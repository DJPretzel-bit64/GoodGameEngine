package SpreadingShadows.code;

import engine.Engine;
import engine.utility.Button;
import engine.utility.Input;
import engine.utility.Vec2;

import static engine.Engine.*;

public class StartButton extends Button {

	public StartButton(String texture, String  hoveredTexture, Vec2 pos, Vec2 size) {
		super(texture, hoveredTexture, pos, size);
	}

	@Override
	public void update(Input input) {
		super.update(input);
		pos = new Vec2(width, height).divide(2 * scale).minus(new Vec2(size).divide(2));
	}

	@Override
	public void click() {
		CustomPlatformer player = (CustomPlatformer) Engine.getEntity(CustomPlatformer.class.getName());
		CustomWorld world = (CustomWorld) Engine.getEntity(CustomWorld.class.getName());
		Background background = (Background) Engine.getEntity(Background.class.getName());
		MainMenu mainMenu = (MainMenu) Engine.getEntity(MainMenu.class.getName());
		assert player != null;
		assert world != null;
		assert background != null;
		player.init();
		world.init();
		background.init();
		Engine.removeEntity(mainMenu);
	}
}
