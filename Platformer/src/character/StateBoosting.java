package character;

import static org.lwjgl.glfw.GLFW.*;
import static helpers.Physics.*;

import helpers.KeyboardHandler;

public class StateBoosting extends CharacterState {

	private float duration; // iterator for the boost duration, used to exit boost state
	private int xDir, yDir; // unit vectors for the boost direction

	@Override
	public void enter(Character c) {
		duration = 0;
		xDir = 0;
		yDir = 0;

		// no direction held
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)
				&& !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT)) {
			if (c.facingRight)
				xDir = 1;
			else
				xDir = -1;
		}

		// right
		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			xDir = 1;

		// left
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			xDir = -1;

		// up
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
			yDir = -1;

		// down
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP) && KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
			yDir = 1;

		c.xSpeed = xDir * getBoostSpeed();
		c.ySpeed = yDir * getBoostSpeed();
	}

	/**
	 * Call to enter a new state from the current one.
	 */
	@Override
	public void enterNewState(Character c, States s) {
		if (duration >= getBoostDuration()) { // prevent exiting boost state prematurely
			c.xSpeed = c.maxSpeed * xDir;
			c.ySpeed = c.maxSpeed * yDir;
			c.state = s;
			c.state.s.enter(c);
		}
	}

	@Override
	public void handleInput(Character c) {
		// cannot act during boost state
	}

	@Override
	public void update(Character c) {
		// unaffected by gravity or air friction during boost state
		duration++;
		enterNewState(c, States.Jumping);
	}

}
