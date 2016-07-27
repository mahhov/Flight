package geometry;

import general.Util;

public class Point2D {
	public float x, y;

	Point2D() {
	}

	Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	Point2D genNormalized() {
		float mag = Util.magnitude(x, y);
		return new Point2D(x / mag, y / mag);
	}

	float dot(float dx, float dy) {
		return (dx * x + dy * y);
	}

}
