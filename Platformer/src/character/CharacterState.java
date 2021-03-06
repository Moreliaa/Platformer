package character;

import static helpers.Clock.*;
import static helpers.Physics.*;
import static org.lwjgl.glfw.GLFW.*;

import helpers.*;

public abstract class CharacterState {

	/**
	 * Called whenever a state is entered.
	 */
	abstract public void enter(Character c);

	/**
	 * Must be called to enter a new state.
	 */
	public void enterNewState(Character c, States s) {
		if (c.state != s) {
			c.state = s;
			c.state.s.enter(c);
		}
	}

	abstract public void handleInput(Character c);

	abstract public void update(Character c);

	public void updateCharacterTimers(Character c) {
		if (c.damageInvulDurationCurrent > 0)
			c.damageInvulDurationCurrent--;

		if (c.damageInvulDurationCurrent == 0)
			c.damageInvul = false;
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
