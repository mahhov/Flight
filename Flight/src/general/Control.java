package general;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Control implements MouseListener, MouseMotionListener, KeyListener {

	public boolean escPressd;
	public boolean left, right, down;
	// 0 = release, 1 = just pressed, 2 = down, 3 = just released
	public int up, mouseL;
	public boolean mouseR;
	public boolean space;
	public int mousex, mousey;

	public void mouseDragged(MouseEvent arg0) {
		mousex = arg0.getX();
		mousey = arg0.getY();
	}

	public void mouseMoved(MouseEvent arg0) {
		mousex = arg0.getX();
		mousey = arg0.getY();
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1)
			mouseL = 1;
		else if (arg0.getButton() == MouseEvent.BUTTON3)
			mouseR = true;
	}

	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1)
			mouseL = 3;
		else if (arg0.getButton() == MouseEvent.BUTTON3)
			mouseR = false;
	}

	public void keyPressed(KeyEvent e) {
		set(e.getKeyCode(), true);

	}

	public void keyReleased(KeyEvent e) {
		set(e.getKeyCode(), false);
	}

	void set(int keyCode, boolean value) {
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			escPressd = true;
			break;
		case KeyEvent.VK_A:
			left = value;
			break;
		case KeyEvent.VK_D:
			right = value;
			break;
		case KeyEvent.VK_S:
			down = value;
			break;
		case KeyEvent.VK_W:
			if (!value)
				up = 3;
			else if (value && up != 2)
				up = 1;
			break;
		case KeyEvent.VK_SPACE:
			space = value;
			break;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	private void outdate() {
		if (mouseL == 1)
			mouseL = 2;
		else if (mouseL == 3)
			mouseL = 0;

		if (up == 1)
			up = 2;
		else if (up == 3)
			up = 0;
	}

	ControlView getView(CameraZ camera) {
		ControlView r = new ControlView(this, camera);
		outdate();
		return r;
	}

	public class ControlView {
		boolean escPressd;
		boolean left, right, down;
		// 0 = release, 1 = just pressed, 2 = down, 3 = just released
		int up, mouseL;
		boolean mouseR;
		boolean spaceAttack;
		float worldMouse[];

		private ControlView(Control c, CameraZ camera) {
			this.escPressd = c.escPressd;
			this.left = c.left;
			this.right = c.right;
			this.down = c.down;
			this.up = c.up;
			this.mouseL = c.mouseL;
			this.mouseR = c.mouseR;
			this.spaceAttack = false; // c.spaceAttack;
			this.worldMouse = camera.genPointScreenToWorld(mousex, mousey);
		}
	}

}
