package helpers;

public class Clock {
	private static boolean paused = false;
	private static long lastFrame, totalTime;
	private static float d = 0, multiplier = 1;
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
	
	public static float getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		
		if ((delta * 0.001f) > 0.05f || delta <= 0) {
			return 0.05f;
		}
		return delta * 0.001f;
	}
	
	public static float delta() {
		if (paused)
			return 0;
		else 
			return d * multiplier;
	}
	
	public static float totalTime() {
		return totalTime;
	}
	
	public static float multiplier() {
		return multiplier;
	}
	
	public static void update() {
		d = getDelta();
		totalTime += d;
	}
	
	public static void changeMultiplier(float change) {
		if (multiplier + change < -1 && multiplier + change > 7) {
			//do nothing
		} else {
			multiplier += change;
		}
	}
	
	public static void pause() {
		if (paused) {
			paused = false;
		} else {
			paused = true;
		}
	}
}
