package character;

import static helpers.Physics.getInitialSmallJumpVelocity;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import data.EffectAfterimage;
import helpers.KeyboardHandler;

public class StateJumping extends CharacterState {

	/**
	 * Called whenever this state is entered.
	 */
	@Override
	public void enter(Character c) {
		if (!c.dashActive) {
			c.framesHeldLeft = 0;
			c.framesHeldRight = 0;
		}

	}

	@Override
	public void handleInput(Character c) {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_A) && !c.boostDisabled)
			enterNewState(c, States.Boosting);

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			moveRight(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			moveLeft(c);
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			stop(c);

		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !c.jumpDisabled) {
			smallJump(c);
			c.jumpDisabled = true;
		}

		super.setFlags(c);
	}

	@Override
	public void update(Character c) {
		if (c.dashActive) {
			EffectAfterimage e = new EffectAfterimage(c, c.state.sprite.getCurrentTexture(), 1, 1, 1);
			data.Game.addNewEffect(e);
		}

		if (c.xSpeed < c.maxSpeed * (-1))
			c.xSpeed += c.airFriction;
		if (c.xSpeed < 0)
			c.xSpeed += c.airFriction;

		if (c.xSpeed > c.maxSpeed)
			c.xSpeed -= c.airFriction;
		if (c.xSpeed > 0)
			c.xSpeed -= c.airFriction;

		if (Math.abs(c.xSpeed) < c.airFriction)
			c.xSpeed = 0;

		super.handleGravity(c);

		if (c.ySpeed > 0) {
			c.jumpDisabled = true;
		}
	}

	private void smallJump(Character c) {
		if (c.ySpeed < 0) {
			if (getInitialSmallJumpVelocity() > c.ySpeed) {
				c.ySpeed = getInitialSmallJumpVelocity();
			}
		}

	}

	private void moveRight(Character c) {

		if (c.dashActive && c.xSpeed > 0) {
			c.xSpeed = c.maxSpeed * 2;
		} else {
			c.dashActive = false;

			if (c.xSpeed < c.maxSpeed)
				c.xSpeed += c.airControl;
		}
	}

	private void moveLeft(Character c) {

		if (c.dashActive && c.xSpeed < 0) {
			c.xSpeed = (-1) * c.maxSpeed * 2;
		} else {
			c.dashActive = false;

			if (c.xSpeed > (-1) * c.maxSpeed)
				c.xSpeed -= c.airControl;
		}
	}

	private void stop(Character c) {
		c.framesHeldLeft = 0;
		c.framesHeldRight = 0;
	}
}
