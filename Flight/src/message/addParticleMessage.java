package message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import toon.Toon;

public class addParticleMessage extends Message {

	private final static byte CODE = 1;

	public static final String EXPLOSION = null;
	public static final String ARROW = null;
	public static final String SMOKE = null;
	public static final String HOOK = null;

	public addParticleMessage(String explosion2, float x, float y) {
		// TODO Auto-generated constructor stub
	}

	public addParticleMessage(String arrow2, float x, float y, float arrowvx,
			float arrowvy) {
		// TODO Auto-generated constructor stub
	}

	public addParticleMessage(String hook2, float x, float y, float hookvx,
			float hookvy, Toon toon) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte getCode() {
		return CODE;
	}

	@Override
	void read(ObjectInputStream in) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(ObjectOutputStream out) {
		// TODO Auto-generated method stub

	}

}
