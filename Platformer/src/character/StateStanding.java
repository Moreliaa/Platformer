package character;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import helpers.Clock;
import helpers.KeyboardHandler;
import helpers.Physics;

public class StateStanding extends CharacterState {

	/**
	 * Called whenever this state is entered.
	 */
	@Override
	public void enter(Character c) {
		c.boostsLeft = c.boostsPerJump; // restore boost ability
	}

	@Override
	public void handleInput(Character c) {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_A) && !c.boostDisabled)
			enterNewState(c, States.Boosting);

		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP)) {
			c.jumpDisabled = false;
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			moveRight(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			moveLeft(c);
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			stop(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !c.jumpDisabled)
			jump(c);

		super.setFlags(c);

	}

	@Override
	public void update(Character c) {
		super.handleGravity(c);
	}

	private void moveRight(Character c) {
		enterNewState(c, States.Walking);

		c.framesHeldLeft = 0;
		c.framesHeldRight += (1 * Clock.delta() * 60);

		if (c.framesHeldRight >= c.framesUntilDashActivation) {
			c.dashActive = true;
		} else {
			c.dashActive = false;
		}

		if (c.dashActive) {
			c.xSpeed = c.maxSpeed * 2;
		} else {
			c.xSpeed = c.maxSpeed;
		}
	}

	private void moveLeft(Character c) {
		enterNewState(c, States.Walking);

		c.framesHeldLeft += (1 * Clock.delta() * 60);
		c.framesHeldRight = 0;

		if (c.framesHeldLeft >= c.framesUntilDashActivation) {
			c.dashActive = true;
		} else {
			c.dashActive = false;
		}

		if (c.dashActive) {
			c.xSpeed = -c.maxSpeed * 2;
		} else {
			c.xSpeed = -c.maxSpeed;
		}

	}

	private void jump(Character c) {
		enterNewState(c, States.Jumping);
		c.ySpeed = Physics.getInitialJumpVelocity();
	}

	private void stop(Character c) {
		enterNewState(c, States.Standing); // only relevant for the overlapping Walking state

		c.framesHeldLeft = 0;
		c.framesHeldRight = 0;
		c.dashActive = false;

		c.xSpeed = 0;
	}

}
