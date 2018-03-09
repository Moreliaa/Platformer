package character;

import static helpers.Physics.getBoostDuration;
import static helpers.Physics.getBoostSpeed;
import static helpers.Physics.getInitialSmallJumpVelocity;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import data.EffectAfterimage;
import data.Texture;
import helpers.KeyboardHandler;

public class StateBoosting extends CharacterState {

	private float duration; // iterator for the boost duration, used to exit boost state
	private float xDir, yDir; // unit vectors for the boost direction

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

		if (xDir != 0 && yDir != 0) {
			c.xSpeed *= Math.abs(Math.cos(45 * 180 / Math.PI));
			c.ySpeed *= Math.abs(Math.cos(45 * 180 / Math.PI));
		}

		c.getCamera().shake(6, 15);
		EffectAfterimage e = new EffectAfterimage(c, new Texture("mm001", 158, 158), 3, 2, 5);
		data.Game.addNewEffect(e);

	}

	/**
	 * Call to enter a new state from the current one.
	 */
	@Override
	public void enterNewState(Character c, States s) {
		if (duration >= getBoostDuration()) { // prevent exiting boost state prematurely
			c.xSpeed = c.maxSpeed * xDir;
			c.ySpeed = (-1) * getInitialSmallJumpVelocity() * yDir;
			c.state = s;
			c.boostsLeft -= 1;
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
