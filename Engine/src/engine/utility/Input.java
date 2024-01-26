package engine.utility;

import engine.Engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

	public boolean up, down, left, right, escape, leftClick, rightClick;
	public boolean[] chars = new boolean[65536];
	public Vec2 mousePos = new Vec2();

	@Override
	public void keyPressed(KeyEvent e) {
		if(Engine.layout.equals("colemak")) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = true;
				case KeyEvent.VK_R, KeyEvent.VK_DOWN -> down = true;
				case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
				case KeyEvent.VK_S, KeyEvent.VK_RIGHT -> right = true;
				case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = true;
			}
		}
		else {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = true;
				case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;
				case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
				case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;
				case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = true;
			}
		}
		chars[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(Engine.layout.equals("colemak")) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = false;
				case KeyEvent.VK_R, KeyEvent.VK_DOWN -> down = false;
				case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
				case KeyEvent.VK_S, KeyEvent.VK_RIGHT -> right = false;
				case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = false;
			}
		}
		else {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> up = false;
				case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = false;
				case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
				case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = false;
				case KeyEvent.VK_ESCAPE, KeyEvent.VK_E -> escape = false;
			}
		}
		chars[e.getKeyCode()] = false;
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if(mouseEvent.getButton() == 1)
			leftClick = true;
		if(mouseEvent.getButton() == 3)
			rightClick = true;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		if(mouseEvent.getButton() == 1)
			leftClick = false;
		if(mouseEvent.getButton() == 3)
			rightClick = false;
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		this.mousePos = new Vec2(mouseEvent.getX(), mouseEvent.getY());
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseEntered(MouseEvent mouseEvent) {}
	@Override
	public void mouseExited(MouseEvent mouseEvent) {}
	@Override
	public void mouseDragged(MouseEvent mouseEvent) {}
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {}
}
