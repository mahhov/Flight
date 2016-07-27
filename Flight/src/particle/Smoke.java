package particle;

import general.Map;
import general.Painter;
import general.Util;

public class Smoke extends Particle {

	final float FRICTION = 0.9f;

	final int TOP = Painter.TOP, SCALE = Painter.SCALE;

	public Smoke(float x, float y, float vx, float vy) {
		super(x + Util.random(1), y + Util.random(1), Util.random(.08f), 1, 1,
				.08f, randomAngle(), vx / 10 + Util.random(.1f), vy / 10
						+ Util.random(.1f), Util.random(.008f), 50, new int[] {
						200, 200, 200 }, null);
	}

	boolean update(Map map, ParticleSystem particleSys) {
		float FRICTION = 1;
		x += vx;
		y += vy;
		z += vz;
		cube.updateCubeCoord(x, y, z);
		vx *= FRICTION;
		vy *= FRICTION;
		vz *= FRICTION;
		return --time == 0;
	}

}
