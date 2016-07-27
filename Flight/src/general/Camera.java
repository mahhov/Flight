package general;

import toon.Toon;

public class Camera {

	float x, y, scale;
	Toon follow;
	int mapWidth, mapHeight;

	Camera(Toon follow, int mapWidth, int mapHeight) {
		this.follow = follow;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}

	void update() {
		scale = 8;

		float adapt = .1f;
		x = x * (1 - adapt) + follow.x * adapt;
		y = y * (1 - adapt) + follow.y * adapt;

		float xborder = Painter.WIDTH / scale / 2;
		float yborder = Painter.HEIGHT / scale / 2;
		x = Util.bound(x, xborder, mapWidth - xborder);
		y = Util.bound(y, yborder, mapHeight - yborder);
	}

	public int[] genRectWorldToScreen(float x, float y, float w, float h) {
		int left = (int) ((x - w / 2 - this.x) * scale + Painter.WIDTH / 2 + 0.5f);
		int top = (int) ((this.y - y - h / 2) * scale + Painter.HEIGHT / 2 + 0.5f);
		int width = (int) (w * scale + 0.5f);
		int height = (int) (h * scale + 0.5f);

		boolean outOfView = (left > Painter.WIDTH || left + width < 0
				|| top > Painter.HEIGHT || top + height < 0);

		return new int[] { outOfView ? -1 : 0, left, top, width, height };

		// float scale2 = 7f / 8 * scale;
		// int left2 = (int) ((x - w / 2 - this.x) * scale2 + Painter.WIDTH / 2
		// + 0.5f);
		// int top2 = (int) ((this.y - y - h / 2) * scale2 + Painter.HEIGHT / 2
		// + 0.5f);
		// int width2 = (int) (w * scale2 + 0.5f);
		// int height2 = (int) (h * scale2 + 0.5f);

		// boolean outOfView = (left2 > Painter.WIDTH || left2 + width2 < 0
		// || top2 > Painter.HEIGHT || top2 + height2 < 0);

		// return new int[] { outOfView ? -1 : 0, left, top, width,
		// height, left2, top2, width2, height2 };
	}

	public int[] genPointWorldToScreen(float x, float y) {
		int left = (int) ((x - this.x) * scale + Painter.WIDTH / 2 + 0.5f);
		int top = (int) ((this.y - y) * scale + Painter.HEIGHT / 2 + 0.5f);

		boolean outOfView = (left > Painter.WIDTH || left < 0
				|| top > Painter.HEIGHT || top < 0);

		return new int[] { outOfView ? -1 : 0, left, top };
	}

	public float[] genPointScreenToWorld(int x, int y) {
		return new float[] { this.x + (x - Painter.WIDTH / 2f) / scale,
				this.y - (y - Painter.HEIGHT / 2f) / scale };
	}
}
