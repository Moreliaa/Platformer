package enemy;

import static helpers.Physics.*;

import data.*;

public class Brute extends Enemy {

	private WaveSine movepattern;
	private float baseY;

	public Brute(Level l, Camera c, float x, float y) {
		super(l, c, x, y);
		sprite = new Animation(new Texture[] { new Texture("brute", 185, 86) }, 1);
		this.width = 60;
		this.height = 60;
		this.maxSpeed = 5;
		this.yOffset = (sprite.getCurrentTexture().getHeight() - height) / 2;
		this.movepattern = new WaveSine(5f, 120);
		this.baseY = this.y;
	}

	@Override
	public void update() {

		if (stepX(this)) {
			this.xSpeed = 0;
		}

		// ignores y collision

		this.y = this.baseY + this.movepattern.getPhase();

	}

}
