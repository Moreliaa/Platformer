package character;

import static helpers.Physics.*;
import static org.lwjgl.glfw.GLFW.*;

import data.*;
import helpers.*;

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
		super.updateCharacterTimers(c);

		if (c.dashActive) {
			EffectAfterimage e = new EffectAfterimage(c, c.state.sprite.getCurrentTexture(), 1, 1, 1);
			data.Game.addNewEffect(e);
		}

		super.handleGravity(c);

		if (stepX(c)) {
			c.xSpeed = 0;
		}
		if (!stepY(c)) {
			if (c.ySpeed > 0)
				enterNewState(c, States.Jumping);
		} else {
			c.ySpeed = 0;
		}

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
