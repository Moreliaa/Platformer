package character;

import static helpers.Physics.*;

import helpers.*;

public class StateDamaged extends CharacterState {

	@Override
	public void enter(Character c) {

		c.damageInvul = true;
		c.damageInvulDurationCurrent = c.damageInvulDurationTotal;

		c.jumpDisabled = true;

		if (c.facingRight) {
			c.xSpeed = -0.5f * c.maxSpeed;
		} else {
			c.xSpeed = 0.5f * c.maxSpeed;
		}

		c.ySpeed = Physics.getInitialSmallJumpVelocity();

	}

	@Override
	public void handleInput(Character c) {
		// no input during damage state

	}

	@Override
	public void update(Character c) {
		super.handleGravity(c);

		if (stepX(c)) {
			c.xSpeed = 0;
		}

		if (stepY(c)) {
			if (c.ySpeed >= 0)
				enterNewState(c, States.Standing);
			else
				c.jumpDisabled = true;
			c.ySpeed = 0;
		}

	}

}
