package geometry;

public class Point2DInt {
	int x, y;

	Point2DInt() {
	}

	Point2DInt(float[] c) {
		x = (int) (c[1] + .5f);
		y = (int) (c[2] + .5f);
	}

	Point2DInt(float[] c, Point2D add1) {
		x = (int) (c[1] + add1.x * c[3] + .5f);
		y = (int) (c[2] - add1.y * c[3] + .5f);
	}

	Point2DInt(float[] c, Point2D add1, Point2D add2) {
		x = (int) (c[1] + (add1.x + add2.x) * c[3] + .5f);
		// negating y for screen coordinates since add1, and add2 are passed in
		// as world coordinates, this method is used to scale to screen
		// coordinates
		y = (int) (c[2] - (add1.y + add2.y) * c[3] + .5f);
	}

}
