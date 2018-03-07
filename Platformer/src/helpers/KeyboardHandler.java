package helpers;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardHandler extends GLFWKeyCallback {

	public static GLFWKeyCallback keyCallback;

	private static boolean[] keys = new boolean[65536];
	private static boolean[] keyRelease = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_RELEASE && keys[key] == true)
			keyRelease[key] = true;
		else
			keyRelease[key] = false;

		keys[key] = action != GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean wasKeyReleased(int keycode) {
		if (keyRelease[keycode]) {
			keyRelease[keycode] = false;
			return true;
		}
		return keyRelease[keycode];
	}
}
