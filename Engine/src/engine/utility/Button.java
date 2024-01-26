package engine.utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static engine.Engine.scale;

public class Button {

	protected BufferedImage texture;
	protected BufferedImage hoveredTexture;
	protected Vec2 pos, size;
	protected boolean hovered;

	public Button(String texture, String hoveredTexture, Vec2 pos, Vec2 size) {
		try {
			this.texture = ImageIO.read(new File(texture));
			this.hoveredTexture = ImageIO.read(new File(hoveredTexture));
		}catch(IOException e) {
			System.out.println("Could not find button textures");
		}
		this.pos = pos;
		this.size = size;
	}

	public void update(Input input) {
		hovered = input.mousePos.x > this.pos.x * scale && input.mousePos.x < (this.pos.x + this.size.x) * scale &&
				input.mousePos.y > this.pos.y * scale && input.mousePos.y < (this.pos.y + this.size.y) * scale;
		if(hovered && input.leftClick) {
			click();
			input.leftClick = false;
		}
	}

	public void render(Graphics g) {
		Vec2 pos = this.pos.times(scale);
		Vec2 size = this.size.times(scale);
		if(hovered)
			g.drawImage(this.hoveredTexture, pos.xi(), pos.yi(), size.xi(), size.yi(), null);
		else
			g.drawImage(this.texture, pos.xi(), pos.yi(), size.xi(), size.yi(), null);
	}

	public void click() {}
}
