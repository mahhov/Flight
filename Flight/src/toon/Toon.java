package toon;

import general.CameraZ;
import general.Painter;
import geometry.Cube;

import java.awt.Graphics2D;

import particle.Hook;

public class Toon {
	// motion
	public float x, y;
	public float w = 3, h = 2 * w;
	public float vx, vy;
	public int team;

	// constants
	public float jumpAcc = 1.3f / 2, runAcc = 0.1f, airRunAcc = 0.015f;
	public float jetAcc = 0.045f, climbAcc = 0.05f, hookAcc = 0.0125f;
	public float hookSpringAcc = .5f;
	public float jumpMult = 1.5f;
	public float attackRange = 3f;
	public int attackDamage = 10;

	// condition
	public int jumpRemain, jumpMax = 1;
	public int jetfuelRemain, jetfuelMax = 500;
	public int attackCur, attackTime = 50;
	int lifeRemain, lifeMax = 50;
	public boolean grounded, climbing, jetting, dead;
	boolean mainToon; // paint jet fuel and other interface

	// hook
	public float hookx, hooky;
	public boolean hook;
	public Hook creatingHook;

	// painting
	Cube[] cube; // 0 = body, 1 = head
	float headTheta;

	final int TOP = Painter.TOP, SCALE = Painter.SCALE,
			HEIGHT = Painter.HEIGHT;

	public Toon(float x, float y, int team, boolean mainToon) {
		this.x = x;
		this.y = y;
		this.team = team;
		jetfuelRemain = jetfuelMax;
		lifeRemain = lifeMax;
		this.mainToon = mainToon;
		initializeCubes();
	}

	Toon(float x, float y, float w, float h, int team, float jumpAcc,
			float runAcc, float airRunAcc, float jetAcc, float climbAcc,
			float hookAcc, float hookSpringAcc, float jumpMult, int jumpMax,
			int jetfuelMax, float attackRange, int attackDamage,
			int attackTime, int lifeMax, boolean mainToon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.team = team;

		this.jumpAcc = jumpAcc;
		this.runAcc = runAcc;
		this.airRunAcc = airRunAcc;
		this.jetAcc = jetAcc;
		this.climbAcc = climbAcc;
		this.hookAcc = hookAcc;
		this.hookSpringAcc = hookSpringAcc;

		this.jumpMult = jumpMult;
		this.jumpMax = jumpMax;
		this.jetfuelMax = jetfuelMax;
		this.jetfuelRemain = jetfuelMax;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.attackTime = attackTime;
		this.lifeMax = lifeMax;
		this.lifeRemain = lifeMax;

		this.mainToon = mainToon;

		initializeCubes();
	}

	public void paint(Graphics2D brush, CameraZ camera) {
		if (dead) {
			camera.fillCube(x, y, 0.5f - w * .08f / 2, w, h, w * .08f,
					new boolean[] { true, true, true, true }, new int[] { 200,
							200, 200 }, brush);
			return;
		}

		// toon
		int[] color = new int[] { 0, 0, 200 };
		color = new int[] { 100, 50, 200 };
		if (mainToon && attackCur != 0)
			color = new int[] { 100, 100, 200 };
		for (Cube c : cube)
			c.paint(color, camera, brush);

		// bars
		final float BAR_WIDTH = 4;
		final float BAR_HEIGHT = 1;
		final float BAR_THICK = 1 * .08f;

		// life
		camera.fillCube(x, y + h / 2 + 1, .5f - BAR_THICK / 2, BAR_WIDTH,
				BAR_HEIGHT, BAR_THICK,
				new boolean[] { true, true, true, true }, new int[] { 200, 200,
						200 }, brush);

		camera.fillCube(x, y + h / 2 + 1, .5f - BAR_THICK / 2, BAR_WIDTH
				* lifeRemain / lifeMax, BAR_HEIGHT, BAR_THICK, new boolean[] {
				true, true, true, true }, new int[] { 200, 0, 0 }, brush);

		// jet fuel
		camera.fillCube(x, y + h / 2 + 1 + BAR_HEIGHT, .5f - BAR_THICK / 2,
				BAR_WIDTH, BAR_HEIGHT, BAR_THICK, new boolean[] { true, true,
						true, true }, new int[] { 200, 200, 200 }, brush);

		camera.fillCube(x, y + h / 2 + 1 + BAR_HEIGHT, .5f - BAR_THICK / 2,
				BAR_WIDTH * jetfuelRemain / jetfuelMax, BAR_HEIGHT, BAR_THICK,
				new boolean[] { true, true, true, true }, new int[] { 75, 75,
						75 }, brush);
	}

	public void setHook(float x, float y) {
		hookx = x;
		hooky = y;
		hook = true;
	}

	public void releaseHook() {
		creatingHook.time = 0;
	}

	public void damage(int amount) {
		lifeRemain -= amount;
		if (lifeRemain <= 0) {
			lifeRemain = 0;
			dead = true;
		}
	}

	void initializeCubes() {
		float whead = .75f * w;
		float thead = whead * .08f;
		float tbody = w * .08f;
		float hbody = h * .6f;

		cube = new Cube[2];
		cube[0] = new Cube(x, y - h / 2 + hbody / 2, .5f, w, hbody, tbody, 0);
		cube[1] = new Cube(x, y + h / 2 - whead / 2, .5f, whead, whead, thead,
				0);
	}

	public void setCubes() {
		float whead = .75f * w;
		float hbody = h * .6f;

		headTheta += .02f;
		// headTheta = 0;
		cube[1].updateCubeTheta(headTheta);

		cube[0].updateCubeCoord(x, y - h / 2 + hbody / 2, .5f);
		cube[1].updateCubeCoord(x, y + h / 2 - whead / 2, .5f);
	}

}
