package general;

public class Util {

	public static float magnitude(float dx, float dy) {
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public static float magnitude(float dx, float dy, float dz) {
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static float max(float a, float b) {
		if (a > b)
			return a;
		return b;
	}

	public static float maxabs(float a, float b) {
		if (a < 0)
			a = -a;
		if (b < 0)
			b = -b;
		if (a > b)
			return a;
		return b;
	}

	public static float bound(float x, float min, float max) {
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}

	public static float random(float d) {
		return (float) (Math.random() * d - d / 2);
	}

}
