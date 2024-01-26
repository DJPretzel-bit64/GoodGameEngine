package SpreadingShadows.code;

import engine.entity.Menu;
import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.image.BufferedImage;

import static engine.Engine.*;

public class MainMenu extends Menu {

	public MainMenu(Vec2 pos, Vec2 size, BufferedImage texture, boolean shouldInitiate) {
		super(pos, size, texture, shouldInitiate);
	}

	@Override
	public void update(double delta, Input input) {
		super.update(delta, input);
		this.size = new Vec2(width, height).divide(scale);
	}

	@Override
	public void init() {
		super.init();
		buttons.add(new StartButton("SpreadingShadows/res/start.png", "SpreadingShadows/res/startHovered.png", new Vec2(0, 0), new Vec2(40, 40)));
	}
}
