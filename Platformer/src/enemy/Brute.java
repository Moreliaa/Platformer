package enemy;

import data.*;

public class Brute extends Enemy {

	public Brute(Level l, Camera c, float x, float y) {
		super(l, c, x, y);
		sprite = new Animation(new Texture[] { new Texture("wheel", 100, 100) }, 1);
		this.width = 60;
		this.height = 60;
		this.maxSpeed = 3;
		this.yOffset = (sprite.getCurrentTexture().getHeight() - height) / 2;
	}

}
