package helpers;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler extends GLFWMouseButtonCallback {
	
	public static GLFWMouseButtonCallback mouseButtonCallback;
	
	private static boolean[] buttons = new boolean[8];

	@Override
	public void invoke(long window, int button, int action, int mods) {
		buttons[button] = action != GLFW_RELEASE;
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public static float getxPos() {
		return MousePosHandler.getxPos();
	}
	
	public static float getyPos() {
		return MousePosHandler.getyPos();
	}

}
