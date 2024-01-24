package engine.utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter {

	public boolean up, down, left, right, escape;
	public boolean[] chars = new boolean[65536];

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;
			case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = true;
		}
		chars[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = false;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = false;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = false;
			case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = false;
		}
		chars[e.getKeyCode()] = false;
	}
}
