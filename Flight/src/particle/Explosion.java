package particle;

import general.CameraZ;
import general.Util;

import java.awt.Graphics2D;

public class Explosion extends Particle {

	int maxSize;

	public Explosion(float x, float y) {
		super(x + Util.random(5), y + Util.random(5), .5f, 0, 0, 0,
				randomAngle(), 0, 0, 0, 50, chooseColor(), null);
		maxSize = 3;// randomInt(3, 5);
	}

	static int[] chooseColor() {
		return new int[] { randomInt(150, 250), randomInt(0, 250), 0 };
	}

	void paint(Graphics2D brush, CameraZ camera) {
		float size = 1f * maxSize * time / 50;
		float thick = size * .08f;

		cube.updateCubeSize(size, size, thick);
		cube.paint(color, camera, brush);

		// camera.fillCube(x, y, z - thick / 2, size, size, thick, new boolean[]
		// {
		// true, true, true, true }, color, brush);
	}

}
