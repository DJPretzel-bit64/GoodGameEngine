package engine.utility;

import java.awt.event.*;

public class Input implements KeyListener, MouseMotionListener, MouseListener {

	public boolean up, down, left, right, escape;

	@Override public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;
			case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = true;
		}
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

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
