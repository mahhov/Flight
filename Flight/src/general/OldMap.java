package general;

import geometry.Cube;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import particle.Arrow;
import particle.Explosion;
import particle.Hook;
import particle.ParticleSystem;
import particle.Smoke;
import toon.Toon;

public class OldMap {
	public int width, height;
	public int block[][];
	Cube cube[][];

	BufferedImage bg;

	public final int BLOCK_EMPTY = 0, BLOCK_FILL = 1, BLOCK_SEMI = 2;
	final float GRAVITY = .05f, FRICTION = 0.9f, AIR_FRICTION = 0.99f,
			HANG_FRICTION = 0.99f;
	public final static float EPSILON = 0.001f;

	OldMap(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		block = new int[width][height];
		cube = new Cube[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pix = image.getRGB(x, y);
				pix = pix != -1 ? 1 : 0;
				int ty = height - 1 - y;
				block[x][ty] = pix;
				cube[x][ty] = new Cube(x + .5f, ty + .5f, 0, 1, 1, 1, 0);
			}
		}
	}

	OldMap() {
		// old version
		// if you're going to use it, update this to add cubes
		this.width = 100;
		this.height = 100;
		block = new int[width][height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (x == 1 || x == 98 || y == 1 || y == 98)
					block[x][y] = BLOCK_FILL;
				else if (x == 10 && y == 11)
					block[x][y] = BLOCK_FILL;
				else if (y == 10 && x < 50)
					block[x][y] = BLOCK_SEMI;
				else
					block[x][y] = BLOCK_EMPTY;
	}

	void updateToon(Toon toon, Control control, CameraZ camera,
			ParticleSystem particleSys, Toon[] allToons) {
		attackUpdateToon(toon, control, camera, particleSys, allToons);
		hookUpdateToon(toon, control, camera, particleSys);
		accelerateUpdateToon(toon, control, particleSys);
		movementUpdateToon(toon);
		toon.setCubes();
		// control.outdate();
	}

	private void attackUpdateToon(Toon toon, Control control, CameraZ camera,
			ParticleSystem particleSys, Toon[] allToons) {
		// attacking old
		if (control.space && toon.attackCur == 0) {
			toon.attackCur = toon.attackTime;
			particleSys.add(new Explosion(toon.x, toon.y));
			for (Toon t : allToons)
				if (t.team != toon.team
						&& Util.magnitude(toon.x - t.x, toon.y - t.y) < toon.attackRange) {
					t.damage(toon.attackDamage);
				}
		}
		// attacking arrow
		if (control.mouseR && toon.attackCur == 0) {
			toon.attackCur = toon.attackTime;
			float[] mousexy = camera.genPointScreenToWorld(control.mousex,
					control.mousey);
			float arrowvx = mousexy[0] - toon.x;
			float arrowvy = mousexy[1] - toon.y;
			Arrow arrow = new Arrow(toon.x, toon.y, arrowvx, arrowvy);
			particleSys.add(arrow);
		}

		if (toon.attackCur > 0)
			toon.attackCur--;
	}

	private void hookUpdateToon(Toon toon, Control control, CameraZ camera,
			ParticleSystem particleSys) {
		// create hooking
		if (control.mouseL == 1) {
			float[] mousexy = camera.genPointScreenToWorld(control.mousex,
					control.mousey);
			float hookvx = mousexy[0] - toon.x;
			float hookvy = mousexy[1] - toon.y;
			Hook hook = new Hook(toon.x, toon.y, hookvx, hookvy, toon);
			particleSys.add(hook);
			if (toon.creatingHook != null)
				toon.releaseHook();
			toon.creatingHook = hook;
		}
		// destroy hooking
		if (control.mouseL == 3) {
			toon.hook = false;
			if (toon.creatingHook != null)
				toon.releaseHook();
		}
		// updating hooking
		if (toon.hook) {
			float hookvx = toon.hookx - toon.x;
			float hookvy = toon.hooky - toon.y;
			float mag = Util.magnitude(hookvx, hookvy);
			hookvx /= mag;
			hookvy /= mag;
			float project = (toon.vx * hookvx + toon.vy * hookvy);
			if (control.space) {
				toon.vx += hookvx * toon.hookAcc * 100;
				toon.vy += hookvy * toon.hookAcc * 100;
				toon.hook = false;
				if (toon.creatingHook != null)
					toon.releaseHook();
			}
			if (project < EPSILON) {
				toon.vx -= hookvx * (project * toon.hookSpringAcc);
				toon.vy -= hookvy * (project * toon.hookSpringAcc);
			}
			toon.vx += hookvx * toon.hookAcc * 4;
			toon.vy += hookvy * toon.hookAcc * 4;
		}
	}

	private void accelerateUpdateToon(Toon toon, Control control,
			ParticleSystem particleSys) {
		// run acceleration (depending on grounded or aired)
		float runAcc = toon.runAcc;
		if (!toon.grounded)
			runAcc = toon.airRunAcc;
		if (control.left)
			toon.vx -= runAcc;
		if (control.right)
			toon.vx += runAcc;

		// jumping
		if (control.up == 1) {
			if (toon.jumpRemain-- > 0) {
				// if (toon.grounded) {
				toon.vx *= toon.jumpMult;
				toon.vy += toon.jumpAcc;
			}
		}

		// jetting or climbing
		toon.jetting = false;
		if (control.up == 1 || control.up == 2) {
			if (toon.climbing)
				toon.vy += toon.climbAcc;
			else if (toon.jetfuelRemain > 1) {
				toon.jetting = true;
				toon.jetfuelRemain -= 2;
				toon.vy += toon.jetAcc;
				particleSys.add(new Smoke(toon.x, toon.y, toon.vx, toon.vy));
			}
		}

		if (toon.jetfuelRemain < toon.jetfuelMax)
			toon.jetfuelRemain += 1;

		// down jet
		if (control.down)
			toon.vy -= toon.jetAcc;

		// gravity + friction update
		if (toon.grounded) {
			toon.vx *= FRICTION;
			toon.vy = (toon.vy - GRAVITY) * FRICTION;
		} else if (toon.hook) {
			toon.vx *= HANG_FRICTION;
			toon.vy = (toon.vy - GRAVITY) * HANG_FRICTION;
		} else {
			toon.vx *= AIR_FRICTION;
			toon.vy = (toon.vy - GRAVITY) * AIR_FRICTION;
		}
	}

	private void movementUpdateToon(Toon toon) {
		toon.climbing = false;
		toon.grounded = false;

		float newx = toon.x + toon.vx;
		float newy = toon.y + toon.vy;

		// horizontal
		if (toon.vx < 0) {
			bubbleRight(toon, newx);
		} else if (toon.vx > 0)
			bubbleLeft(toon, newx);

		// vertical
		if (toon.vy < 0) {
			bubbleUp(toon, newy);
		} else if (toon.vy > 0)
			bubbleDown(toon, newy);
	}

	private void bubbleRight(Toon toon, float newx) {
		int top = (int) (toon.y + toon.h / 2 - EPSILON);
		int bottom = (int) (toon.y - toon.h / 2);
		int left = (int) (toon.x - toon.w / 2);
		int newLeft = (int) (newx - toon.w / 2);

		for (int xx = left; xx >= newLeft; xx--)
			for (int yy = bottom; yy <= top; yy++)
				if (block[xx][yy] == BLOCK_FILL) {
					toon.x = xx + 1 + toon.w / 2;
					toon.vx = 0;
					toon.climbing = true;
					return;
				}

		toon.x = newx;
	}

	private void bubbleLeft(Toon toon, float newx) {
		int top = (int) (toon.y + toon.h / 2 - EPSILON);
		int bottom = (int) (toon.y - toon.h / 2);
		int right = (int) (toon.x + toon.w / 2 - EPSILON);
		int newRight = (int) (newx + toon.w / 2 - EPSILON);

		for (int xx = right; xx <= newRight; xx++)
			for (int yy = bottom; yy <= top; yy++)
				if (block[xx][yy] == BLOCK_FILL) {
					toon.x = xx - toon.w / 2;
					toon.vx = 0;
					toon.climbing = true;
					return;
				}

		toon.x = newx;
	}

	private void bubbleUp(Toon toon, float newy) {
		int bottom = (int) (toon.y - toon.h / 2);
		int newBottom = (int) (newy - toon.h / 2);
		int left = (int) (toon.x - toon.w / 2);
		int right = (int) (toon.x + toon.w / 2 - EPSILON);

		for (int yy = bottom; yy >= newBottom; yy--)
			for (int xx = left; xx <= right; xx++)
				if (block[xx][yy] == BLOCK_FILL) {
					toon.y = yy + 1 + toon.h / 2;
					toon.vy = 0;
					toon.grounded = true;
					toon.jumpRemain = toon.jumpMax;
					return;
				}

		toon.y = newy;
	}

	private void bubbleDown(Toon toon, float newy) {
		int top = (int) (toon.y + toon.h / 2 - EPSILON);
		int newTop = (int) (newy + toon.h / 2 - EPSILON);
		int left = (int) (toon.x - toon.w / 2);
		int right = (int) (toon.x + toon.w / 2 - EPSILON);

		for (int yy = top; yy <= newTop; yy++)
			for (int xx = left; xx <= right; xx++)
				if (block[xx][yy] == BLOCK_FILL) {
					toon.y = yy - toon.h / 2;
					toon.vy = 0;
					return;
				}

		toon.y = newy;
	}

	// paints entire area every frame
	void paint(Graphics2D brush, CameraZ camera) {

		int centerx = (int) camera.x;
		int minx = centerx - camera.maxOffsetX;
		int maxx = centerx + camera.maxOffsetX;
		int centery = (int) camera.y;
		int miny = centery - camera.maxOffsetY;
		int maxy = centery + camera.maxOffsetY;

		if (minx < 0)
			minx = 0;
		if (maxx > width - 1)
			maxx = width - 1;
		if (miny < 0)
			miny = 0;
		if (maxy > height - 1)
			maxy = height - 1;

		for (int y = miny; y < centery; y++) {
			for (int x = minx; x < centerx; x++)
				paintBlock(x, y, brush, camera);
			for (int x = maxx; x >= centerx; x--)
				paintBlock(x, y, brush, camera);
		}

		for (int y = maxy; y >= centery; y--) {
			for (int x = minx; x < centerx; x++)
				paintBlock(x, y, brush, camera);
			for (int x = maxx; x >= centerx; x--)
				paintBlock(x, y, brush, camera);
		}
	}

	void paintBlock(int x, int y, Graphics2D brush, CameraZ camera) {
		int b = block[x][y];
		boolean[] sides = new boolean[4];

		if (x == 0 || block[x - 1][y] == BLOCK_EMPTY) // left
			sides[3] = true;
		if (x == width - 1 || block[x + 1][y] == BLOCK_EMPTY) // right
			sides[1] = true;
		if (y == height - 1 || block[x][y + 1] == BLOCK_EMPTY) // top
			sides[0] = true;
		if (y == 0 || block[x][y - 1] == BLOCK_EMPTY) // bottom
			sides[2] = true;

		switch (b) {
		case BLOCK_EMPTY:
			break;
		case BLOCK_FILL:
			cube[x][y].paint(sides, new int[] { 100, 100, 100 }, camera, brush);
			break;
		case BLOCK_SEMI:
			break;
		}
	}

	void paintFromBgPic(Graphics2D brush, CameraZ camera) {
		if (bg == null) {
			bg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D bgBrush = (Graphics2D) bg.getGraphics();
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++) {
					int b = block[x][y];
					switch (b) {
					case BLOCK_EMPTY:
						bgBrush.setColor(Color.WHITE);
						bgBrush.drawRect(x, height - 1 - y, 0, 0);
						break;
					case BLOCK_FILL:
						bgBrush.setColor(Color.GRAY);
						bgBrush.drawRect(x, height - 1 - y, 0, 0);
						break;
					case BLOCK_SEMI:
						bgBrush.setColor(Color.LIGHT_GRAY);
						bgBrush.drawRect(x, height - 1 - y, 0, 0);
						break;
					}
				}
		}

		// int[] rect = camera.genRectWorldToScreen(width / 2f, height / 2f,
		// width, height);
		// brush.drawImage(bg, rect[1], rect[2], rect[1] + rect[3], rect[2]
		// + rect[4], 0, 0, width, height, null);
	}
}
