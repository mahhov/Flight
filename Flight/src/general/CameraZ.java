package general;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import toon.Toon;

public class CameraZ {

	public float x, y;
	float scale, scaleZ;
	Toon follow;
	int mapWidth, mapHeight;

	int maxOffsetX, maxOffsetY; // maximum number of blocks in each direction
								// that can be viewed at current scale, at z=1

	float colorRight;
	float colorLeft;
	float colorTop;
	float colorBottom;
	float colorFront;

	float lightx, lighty, lightz;

	CameraZ(Toon follow, int mapWidth, int mapHeight) {
		this.follow = follow;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		lightx = 1;
		lighty = 2;
		lightz = 3;
		float mag = Util.magnitude(lightx, lighty, lightz);
		lightx /= mag;
		lighty /= mag;
		lightz /= mag;
		colorRight = .5f + .5f * lightx;
		colorLeft = .5f - .5f * lightx;
		colorTop = .5f + .5f * lighty;
		colorBottom = .5f - .5f * lighty;
		colorFront = .5f + .5f * lightz;
	}

	void update() {
		scale = 8f;
		scaleZ = -scale / 8 / 2;

		maxOffsetX = (int) (Painter.WIDTH / 2f / (scale + scaleZ)) + 1;
		maxOffsetY = (int) (Painter.HEIGHT / 2f / (scale + scaleZ)) + 1;

		float adapt = .1f;
		x = x * (1 - adapt) + follow.x * adapt;
		y = y * (1 - adapt) + follow.y * adapt;

		float xborder = Painter.WIDTH / scale / 2;
		float yborder = Painter.HEIGHT / scale / 2;
		x = Util.bound(x, xborder, mapWidth - xborder);
		y = Util.bound(y, yborder, mapHeight - yborder);
	}

	public int[] genRectWorldToScreen(float x, float y, float z, float w,
			float h, float thick) {
		float s = scale + scaleZ * z;
		int left = (int) ((x - w / 2 - this.x) * s + Painter.WIDTH / 2 + 0.5f);
		int top = (int) ((this.y - y - h / 2) * s + Painter.HEIGHT / 2 + 0.5f);
		int width = (int) (w * s + 0.5f);
		int height = (int) (h * s + 0.5f);

		s += scaleZ * thick;
		int left2 = (int) ((x - w / 2 - this.x) * s + Painter.WIDTH / 2 + 0.5f);
		int top2 = (int) ((this.y - y - h / 2) * s + Painter.HEIGHT / 2 + 0.5f);
		int width2 = (int) (w * s + 0.5f);
		int height2 = (int) (h * s + 0.5f);

		boolean outOfView = (left2 > Painter.WIDTH || left2 + width2 < 0
				|| top2 > Painter.HEIGHT || top2 + height2 < 0);

		return new int[] { outOfView ? -1 : 0, left, top, left + width,
				top + height, left2, top2, left2 + width2, top2 + height2 };
	}

	public int[] genPointWorldToScreen(float x, float y, float z) {
		float s = scale + scaleZ * z;
		int left = (int) ((x - this.x) * s + Painter.WIDTH / 2 + 0.5f);
		int top = (int) ((this.y - y) * s + Painter.HEIGHT / 2 + 0.5f);

		boolean outOfView = (left > Painter.WIDTH || left < 0
				|| top > Painter.HEIGHT || top < 0);

		return new int[] { outOfView ? -1 : 0, left, top };
	}

	public float[] genPointWorldToScreen(float x, float y, float z,
			float maxMovement) {
		float s = scale + scaleZ * z;
		float left = (x - this.x) * s + Painter.WIDTH / 2;
		float top = (this.y - y) * s + Painter.HEIGHT / 2;

		maxMovement *= s;
		boolean outOfView = (left - maxMovement > Painter.WIDTH
				|| left + maxMovement < 0 || top - maxMovement > Painter.HEIGHT || top
				+ maxMovement < 0);

		return new float[] { outOfView ? -1 : 0, left, top, s };
	}

	public void fillCube(float x, float y, float z, float w, float h,
			float thick, boolean sides[], int[] color, Graphics2D brush) {
		int[] c = genRectWorldToScreen(x, y, z, w, h, thick);

		if (c[0] == -1)
			return;

		// sides: top, right, bottom, left, ^ > V <

		// draw right
		if (x < this.x && sides[1]) {
			brush.setColor(new Color((int) (color[0] * colorRight),
					(int) (color[1] * colorRight),
					(int) (color[2] * colorRight)));
			brush.fillPolygon(new Polygon(new int[] { c[3], c[7], c[7], c[3] },
					new int[] { c[2], c[6], c[8], c[4] }, 4));
		}
		// draw left
		if (x > this.x && sides[3]) {
			brush.setColor(new Color((int) (color[0] * colorLeft),
					(int) (color[1] * colorLeft), (int) (color[2] * colorLeft)));
			brush.fillPolygon(new Polygon(new int[] { c[1], c[5], c[5], c[1] },
					new int[] { c[2], c[6], c[8], c[4] }, 4));
		}
		// draw top
		if (y < this.y && sides[0]) {
			brush.setColor(new Color((int) (color[0] * colorTop),
					(int) (color[1] * colorTop), (int) (color[2] * colorTop)));
			brush.fillPolygon(new Polygon(new int[] { c[1], c[3], c[7], c[5] },
					new int[] { c[2], c[2], c[6], c[6] }, 4));
		}
		// draw bottom
		if (y > this.y && sides[2]) {
			brush.setColor(new Color((int) (color[0] * colorBottom),
					(int) (color[1] * colorBottom),
					(int) (color[2] * colorBottom)));
			brush.fillPolygon(new Polygon(new int[] { c[1], c[3], c[7], c[5] },
					new int[] { c[4], c[4], c[8], c[8] }, 4));
		}
		// draw front
		brush.setColor(new Color((int) (color[0] * colorFront),
				(int) (color[1] * colorFront), (int) (color[2] * colorFront)));
		brush.fillPolygon(new Polygon(new int[] { c[1], c[3], c[3], c[1] },
				new int[] { c[4], c[4], c[2], c[2] }, 4));
	}

	public float[] genPointScreenToWorld(int x, int y) {
		return new float[] { this.x + (x - Painter.WIDTH / 2f) / scale,
				this.y - (y - Painter.HEIGHT / 2f) / scale };
	}

	// for faces with normal_z = 0
	public void setColor(int[] color, float normx, float normy, Graphics2D brush) {
		float mult = .5f + .5f * (lightx * normx + lighty * normy);
		brush.setColor(new Color((int) (color[0] * mult),
				(int) (color[1] * mult), (int) (color[2] * mult)));
	}

	// for faces with norm <0,0,1>, i.e., facing the user
	public void setColor(int[] color, Graphics2D brush) {
		float mult = .5f + .5f * (lightz);
		brush.setColor(new Color((int) (color[0] * mult),
				(int) (color[1] * mult), (int) (color[2] * mult)));
	}
}
