package particle;

import general.CameraZ;
import general.Map;
import general.Painter;
import geometry.Cube;

import java.awt.Graphics2D;

import toon.Toon;

public class Particle {
	float x, y, z;
	float w, h, thick;
	float vx, vy, vz;
	public int time;
	int[] color;
	Cube cube;
	Toon originator;

	final int TOP = Painter.TOP, SCALE = Painter.SCALE;

	public Particle(float x, float y, float z, float w, float h, float thick,
			float theta, float vx, float vy, float vz, int time, int[] color,
			Toon originator) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.thick = thick;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		this.time = time;
		this.color = color;
		this.originator = originator;

		cube = new Cube(x, y, z, w, h, thick, theta);
	}

	boolean update(Map map, ParticleSystem particleSys) {
		x += vx;
		y += vy;
		z += vz;
		cube.updateCubeCoord(x, y, z);
		return --time == 0;
	}

	void paint(Graphics2D brush, CameraZ camera) {
		// 1
		// int[] rect = camera.genRectWorldToScreen(x, y, w, h);
		// if (rect[0] != -1) {
		// brush.setColor(color);
		// brush.fillRect(rect[1], rect[2], rect[3], rect[4]);
		// }

		// 2
		// camera.fillCube(x, y, z - thick / 2, w, h, thick, new boolean[] {
		// true,
		// true, true, true }, color, brush);

		// 3
		cube.paint(color, camera, brush);
	}

	static float randomAngle() {
		return (float) (Math.random() * Math.PI * 2);
	}

	static int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}
}
