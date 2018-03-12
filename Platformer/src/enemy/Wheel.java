package enemy;

import data.Animation;
import data.Camera;
import data.Texture;
import data.TileGrid;

public class Wheel extends Enemy {

	public Wheel(TileGrid grid, Camera c, float x, float y) {
		super(grid, c, x, y);
		sprite = new Animation(new Texture[] { new Texture("wheel", 100, 100) }, 1);
		this.width = 60;
		this.height = 60;
		this.maxSpeed = 8;
		this.xSpeed = this.maxSpeed;
		this.yOffset = (sprite.getCurrentTexture().getHeight() - height) / 2;
	}

}
