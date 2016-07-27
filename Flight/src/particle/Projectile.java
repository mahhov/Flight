package particle;

import general.CameraZ;
import general.Map;
import general.Util;

import java.awt.Graphics2D;

import toon.Toon;

public class Projectile extends Particle {

	final float GRAVITY = .05f, FRICTION = .95f, initialSpeed = 7;

	boolean hooked;

	public Projectile(float x, float y, float vx, float vy, Toon originator) {
		super(x, y, .5f, 1.75f, 1.75f, 1.75f * .08f,
				(float) Math.atan2(vy, vx), vx, vy, 0, 1,
				new int[] { 0, 0, 200 }, originator);
		float mag = Util.magnitude(vx, vy);
		this.vx = vx * initialSpeed / mag;// + originator.vx;
		this.vy = vy * initialSpeed / mag;// + originator.vy;
	}

	boolean update(Map map, ParticleSystem particleSys) {
		return false;
	}

	// return true when collide with map
	boolean move(Map map) {
		boolean collide = false;

		vx *= FRICTION;
		vy = (vy - GRAVITY) * FRICTION;

		// incremental movement

		if (vx == 0 && vy == 0)
			vy = Map.EPSILON;

		float remain = 1;
		float tempx = x, tempy = y;

		while (remain > 0) {
			float leftDist = x - (int) x;
			float bottomDist = y - (int) y;
			float rightDist = 1 - leftDist;
			float topDist = 1 - bottomDist;
			if (leftDist == 0)
				leftDist = 1;
			if (bottomDist == 0)
				bottomDist = 1;

			float deltaX = 0, deltaY = 0;

			if (vx != 0)
				deltaX = Util.max(rightDist / vx, -leftDist / vx);
			if (vy != 0)
				deltaY = Util.max(topDist / vy, -bottomDist / vy);
			float delta = deltaX < deltaY && deltaX != 0 ? deltaX : deltaY;
			if (delta == 0)
				delta = 1 / Util.maxabs(vx, vy);

			delta *= (1 + Map.EPSILON);

			remain -= delta;
			if (remain > 0) {
				tempx += vx * delta;
				tempy += vy * delta;

				if ((map.block[(int) tempx][(int) tempy] == map.BLOCK_FILL)) {
					collide = true;
					hooked = true;
					remain = 0;
				}
			} else {
				tempx += vx * (remain + delta);
				tempy += vy * (remain + delta);
			}

			x = tempx;
			y = tempy;
		}
		cube.updateCubeCoord(x, y, z);
		return collide;
	}

	void paint(Graphics2D brush, CameraZ camera) {
		super.paint(brush, camera);
	}
}
