package message;

import general.CameraZ;
import general.Control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerControl extends Message {

	private final static byte CODE = 0;

	public static final byte LEFT = 1, RIGHT = 2, DOWN = 4, MOUSER = 8,
			SPACE = 16, ESC = 32;

	int playerId;
	boolean escPressd;
	boolean left, right, down;
	// 0 = release, 1 = just pressed, 2 = down, 3 = just released
	int up, mouseL;
	boolean mouseR;
	boolean space;
	float worldMouse[];

	public PlayerControl(int playerId, Control c, CameraZ camera) {
		this.playerId = playerId;
		this.escPressd = c.escPressd;
		this.left = c.left;
		this.right = c.right;
		this.down = c.down;
		this.up = c.up;
		this.mouseL = c.mouseL;
		this.mouseR = c.mouseR;
		this.space = c.space;
		this.worldMouse = camera.genPointScreenToWorld(c.mousex, c.mousey);
	}

	@Override
	public byte getCode() {
		return CODE;
	}

	@Override
	void read(ObjectInputStream in) {
		try {
			this.playerId = in.readByte();
			this.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(playerId);
			out.writeByte(val)
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
