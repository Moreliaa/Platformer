package helpers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosHandler extends GLFWCursorPosCallback {

	public static GLFWCursorPosCallback cursorPosCallback;

	private static double xPos, yPos;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		xPos = xpos;
		yPos = ypos;

	}

	public static float getxPos() {
		return (float) xPos;
	}

	public static float getyPos() {
		return (float) yPos;
	}

}
