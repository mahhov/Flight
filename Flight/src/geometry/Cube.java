package geometry;

import general.CameraZ;
import general.Util;

import java.awt.Graphics2D;
import java.awt.Polygon;

public class Cube {

	static final boolean[] allSides = new boolean[] { true, true, true, true };

	float x, y, z; // center coordinates
	float w, h, t, maxwh;
	float theta, cos, sin;
	CubeData data;

	public Cube(float x, float y, float z, float w, float h, float t,
			float theta) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.t = t;
		this.theta = theta;
		this.maxwh = Util.max(w, h);
		data = new CubeData();
		updateCubeData();
	}

	public void updateCubeCoord(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		data.topLeftFront = new Point(x - (data.horiz.x + data.vert.x) / 2, y
				- (data.horiz.y + data.vert.y) / 2, z - t / 2);
	}

	public void updateCubeSize(float w, float h, float t) {
		this.w = w;
		this.h = h;
		this.t = t;
		this.maxwh = Util.max(w, h);
		data.w = w;
		data.h = h;
		data.t = t;
		data.horiz = new Point2D(w * cos, w * sin);
		data.vert = new Point2D(h * sin, -h * cos);
		data.topLeftFront = new Point(x - (data.horiz.x + data.vert.x) / 2, y
				- (data.horiz.y + data.vert.y) / 2, z - t / 2);
		data.rightNormal = data.horiz.genNormalized();
		data.bottomNormal = data.vert.genNormalized();
	}

	public void updateCubeTheta(float theta) {
		this.theta = theta;
		cos = (float) Math.cos(theta);
		sin = (float) Math.sin(theta);
		data.horiz = new Point2D(w * cos, w * sin);
		data.vert = new Point2D(h * sin, -h * cos);
		data.topLeftFront = new Point(x - (data.horiz.x + data.vert.x) / 2, y
				- (data.horiz.y + data.vert.y) / 2, z - t / 2);
		data.rightNormal = data.horiz.genNormalized();
		data.bottomNormal = data.vert.genNormalized();
	}

	void updateCubeData() {
		cos = (float) Math.cos(theta);
		sin = (float) Math.sin(theta);
		data.horiz = new Point2D(w * cos, w * sin);
		data.vert = new Point2D(h * sin, -h * cos);
		data.w = w;
		data.h = h;
		data.t = t;
		data.topLeftFront = new Point(x - (data.horiz.x + data.vert.x) / 2, y
				- (data.horiz.y + data.vert.y) / 2, z - t / 2);
		data.rightNormal = data.horiz.genNormalized();
		data.bottomNormal = data.vert.genNormalized();
	}

	public void paint(int[] color, CameraZ camera, Graphics2D brush) {
		paint(allSides, color, camera, brush);
	}

	public void paint(boolean[] sides, int[] color, CameraZ camera,
			Graphics2D brush) {
		Point p = data.topLeftFront;
		float[] back = camera.genPointWorldToScreen(p.x, p.y, p.z + t, maxwh);
		if (back[0] == -1)
			return;

		Point2DInt tlb = new Point2DInt(back);
		Point2DInt trb = new Point2DInt(back, data.horiz);
		Point2DInt brb = new Point2DInt(back, data.horiz, data.vert);
		Point2DInt blb = new Point2DInt(back, data.vert);
		float[] front = camera.genPointWorldToScreen(p.x, p.y, p.z, maxwh);
		Point2DInt tlf = new Point2DInt(front);
		Point2DInt trf = new Point2DInt(front, data.horiz);
		Point2DInt brf = new Point2DInt(front, data.horiz, data.vert);
		Point2DInt blf = new Point2DInt(front, data.vert);

		// sides[] ordering: top, right, bottom, left, ^ > V <
		float dx = camera.x - p.x;
		float dy = camera.y - p.y;

		// draw right
		if (sides[1] && data.rightNormal.dot(dx, dy) > 0) {
			camera.setColor(color, data.rightNormal.x, data.rightNormal.y,
					brush);
			brush.fillPolygon(new Polygon(new int[] { trf.x, brf.x, brb.x,
					trb.x }, new int[] { trf.y, brf.y, brb.y, trb.y }, 4));
		}
		// draw left
		else if (sides[3] && -data.rightNormal.dot(dx, dy) > 0) {
			camera.setColor(color, -data.rightNormal.x, -data.rightNormal.y,
					brush);
			brush.fillPolygon(new Polygon(new int[] { tlf.x, blf.x, blb.x,
					tlb.x }, new int[] { tlf.y, blf.y, blb.y, tlb.y }, 4));
		}
		// draw top
		if (sides[0] && -data.bottomNormal.dot(dx, dy) > 0) {
			camera.setColor(color, -data.bottomNormal.x, -data.bottomNormal.y,
					brush);
			brush.fillPolygon(new Polygon(new int[] { tlf.x, trf.x, trb.x,
					tlb.x }, new int[] { tlf.y, trf.y, trb.y, tlb.y }, 4));
		}
		// draw bottom
		else if (sides[2] && data.bottomNormal.dot(dx, dy) > 0) {
			camera.setColor(color, data.bottomNormal.x, data.bottomNormal.y,
					brush);
			brush.fillPolygon(new Polygon(new int[] { blf.x, brf.x, brb.x,
					blb.x }, new int[] { blf.y, brf.y, brb.y, blb.y }, 4));
		}
		// draw front
		camera.setColor(color, brush);
		// brush.fillRect(tlf.x, tlf.y, trf.x-tlf.x, blf.y-tlf.y);
		brush.fillPolygon(new Polygon(new int[] { tlf.x, trf.x, brf.x, blf.x },
				new int[] { tlf.y, trf.y, brf.y, blf.y }, 4));

	}

	class CubeData {
		Point topLeftFront;
		Point2D horiz, vert;
		float w, h, t;
		Point2D rightNormal, bottomNormal;
	}

}

// you dont need to keep track of any variables in Cube (unless you want to add
// incremental updates)
