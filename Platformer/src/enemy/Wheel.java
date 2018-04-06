package enemy;

import static helpers.Physics.*;

import data.*;

public class Wheel extends Enemy {

	public Wheel(Level l, Camera c, float x, float y) {
		super(l, c, x, y);
		this.damage = 1;
		sprite = new Animation(new Texture[] { new Texture("wheel", 100, 100) }, 1);
		this.width = 60;
		this.height = 60;
		this.xSpeed = -5;
		this.maxSpeed = Math.abs(xSpeed);
		this.yOffset = (sprite.getCurrentTexture().getHeight() - height) / 2;
	}

	@Override
	public void update() {
		handleGravity();

		if (stepX(this)) {
			this.xSpeed = this.xSpeed * -1;
		}

		if (stepY(this)) {
			this.ySpeed = 0;
		}
	}

}
