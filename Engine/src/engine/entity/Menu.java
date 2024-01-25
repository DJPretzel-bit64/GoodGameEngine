package engine.entity;

import engine.utility.Button;
import engine.utility.Input;
import engine.utility.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static engine.Engine.numLayers;
import static engine.Engine.scale;

public class Menu extends BasicEntity {

	protected ArrayList<Button> buttons = new ArrayList<>();

	public Menu(Vec2 pos, Vec2 size, BufferedImage texture, boolean shouldInitiate) {
		super(pos, size, texture, new ArrayList<>(), numLayers, false, shouldInitiate);
	}

	@Override
	public void update(double delta, Input input) {
		for(Button button : buttons) {
			button.update(input);
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(texture, pos.xi(), pos.yi(), size.xi() * scale, size.yi() * scale, null);
		for(Button button : buttons) {
			button.render(g);
		}
	}
}
