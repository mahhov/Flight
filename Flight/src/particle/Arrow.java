package particle;

import general.CameraZ;
import general.Map;

import java.awt.Graphics2D;

public class Arrow extends Projectile {

	final int MAX_TIME = 100;

	public Arrow(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, null);
		time = MAX_TIME;
	}

	boolean update(Map map, ParticleSystem particleSys) {
		time--;

		if (time <= 0)
			return true;

		if (hooked)
			return false;

		particleSys.add(new Smoke(x, y, vx * 0, vy * 0));
		move(map);

		return false;
	}

	void paint(Graphics2D brush, CameraZ camera) {
		super.paint(brush, camera);
	}

}
