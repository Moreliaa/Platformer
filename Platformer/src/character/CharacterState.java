package character;

import static helpers.Clock.delta;
import static helpers.Physics.getGravity;
import static helpers.Physics.getMaxFallSpeed;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

import helpers.KeyboardHandler;

public abstract class CharacterState {

	/**
	 * Called whenever this state is entered.
	 */
	public void enter(Character c) {

	}

	/**
	 * Must be called to enter a new state.
	 */
	public void enterNewState(Character c, States s) {
		if (c.state != s) {
			c.state = s;
			c.state.s.enter(c);
		}
	}

	public void handleInput(Character c) {
		// input should be handled for each state individually
	}

	public void update(Character c) {
		// implement for each state individually
	}

	public void setFlags(Character c) {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			c.facingRight = true;
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			c.facingRight = false;

		if (KeyboardHandler.isKeyDown(GLFW_KEY_A))
			c.boostDisabled = true;
		else if (c.boostsLeft > 0)
			c.boostDisabled = false;
	}

	public void handleGravity(Character c) {
		c.ySpeed += getGravity() * delta() * 60;

		if (c.ySpeed > getMaxFallSpeed())
			c.ySpeed = getMaxFallSpeed();
	}

}
