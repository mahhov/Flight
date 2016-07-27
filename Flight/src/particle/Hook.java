package particle;

import general.CameraZ;
import general.Map;
import general.Util;

import java.awt.Color;
import java.awt.Graphics2D;

import toon.Toon;

public class Hook extends Projectile {

	public Hook(float x, float y, float vx, float vy, Toon originator) {
		super(x, y, vx, vy, originator);
	}

	boolean update(Map map, ParticleSystem particleSys) {
		if (Util.magnitude(x - originator.x, y - originator.y) > 150)
			time = 0;

		if (time == 0)
			return true;

		if (hooked)
			return false;

		if (move(map))
			originator.setHook(x, y);

		return false;
	}

	void paint(Graphics2D brush, CameraZ camera) {
		super.paint(brush, camera);
		// brush.setStroke(new BasicStroke(2));

		if (originator == null)
			return;
		int[] oxy = camera.genPointWorldToScreen(originator.x, originator.y,
				.5f);
		int[] xy = camera.genPointWorldToScreen(x, y, .5f);

		if (oxy[0] != -1 || xy[0] != -1) {
			brush.setColor(new Color(color[0], color[1], color[2]));
			brush.drawLine(oxy[1], oxy[2], xy[1], xy[2]);
		}
	}
}
