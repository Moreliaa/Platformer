package enemy;

import static helpers.Physics.stepX;
import static helpers.Physics.stepY;

import data.Animation;
import data.Camera;
import data.Level;
import data.Texture;

public class Wheel extends Enemy {

	public Wheel(Level l, Camera c, float x, float y, float speed) {
		super(l, c, x, y);
		sprite = new Animation(new Texture[] { new Texture("wheel", 100, 100) }, 1);
		this.width = 60;
		this.height = 60;
		this.maxSpeed = Math.abs(speed);
		this.xSpeed = speed;
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
