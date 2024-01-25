package engine.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

import static engine.Engine.scale;

public class Button {

	private final BufferedImage texture;
	private final Vec2 pos, size;

	public Button(BufferedImage texture, Vec2 pos, Vec2 size) {
		this.texture = texture;
		this.pos = pos;
		this.size = size;
	}

	public void update(Input input) {

	}

	public void render(Graphics g) {
		Vec2 pos = this.pos.times(scale);
		Vec2 size = this.size.times(scale);
		g.drawImage(this.texture, pos.xi(), pos.yi(), size.xi(), size.yi(), null);
	}
}
