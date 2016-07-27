package toon;

import general.HostWorld;
import geometry.Cube;

public class ToonStackMonster extends Toon {

	float[] bodyshiftx;
	float[] bodyshiftz;

	public ToonStackMonster(float x, float y, int team, boolean mainToon) {
		super(x, y, team, mainToon);
		w = 6;
		h = 8;
		initializeCubes();
	}

	void initializeCubes() {
		float whead = w / 3;
		float hhead = whead * 1.5f;
		float thead = w * .08f;

		cube = new Cube[10];
		bodyshiftx = new float[9];
		bodyshiftz = new float[9];
		cube[9] = new Cube(x, y + h / 2 - hhead / 2, .5f, whead, hhead, thead,
				0);
		for (int i = 0; i < cube.length - 1; i++) {
			bodyshiftx[i] = HostWorld.random(.1f) * w;
			bodyshiftz[i] = HostWorld.random(.1f) * w * .08f;
			float wbody = (0.5f + HostWorld.random(0.5f)) * w;
			float tbody = (0.5f + HostWorld.random(0.5f)) * w * .08f;
			float hbody = (h - hhead) / (cube.length - 1);
			cube[i] = new Cube(x + bodyshiftx[i],
					y - h / 2 + hbody * (i + .5f), .5f + bodyshiftz[i], wbody,
					hbody, tbody, 0);
		}
	}

	public void setCubes() {
		float whead = w / 2;
		float hhead = whead * 1.5f;

		cube[9].updateCubeCoord(x, y + h / 2 - hhead / 2, .5f);

		for (int i = 0; i < cube.length - 1; i++) {
			float hbody = (h - hhead) / (cube.length - 1);
			cube[i].updateCubeCoord(x + bodyshiftx[i], y - h / 2 + hbody
					* (i + .5f), .5f + bodyshiftz[i]);
		}
	}
}
