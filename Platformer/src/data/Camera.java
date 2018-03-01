package data;

import static helpers.Graphics.*;

import character.Character;

public class Camera {
	private float x, y, xMax, yMax;

	public Camera(TileGrid grid) {
		x = 0;
		y = 0;

		xMax = grid.getMapWidth() * tileSize - WIDTH;
		yMax = grid.getMapHeight() * tileSize - HEIGHT;

		if (xMax < 0)
			xMax = 0;
		if (yMax < 0)
			yMax = 0;
	}

	public void updatePosition(float x, float y) {
		this.x = x;
		this.y = y;

		checkBounds();
	}
	
	public void move(float xSpeed, float ySpeed) {
		this.x += xSpeed;
		this.y += ySpeed;

		checkBounds();
	}

	public void centerOn(Character c) {
		centerOn(c.getX(), c.getY(), c.getWidth(), c.getHeight());
	}

	public void centerOn(float x, float y, int width, int height) {

		this.x = x + (width / 2) - (WIDTH / 2);
		this.y = y + (height / 2) - (HEIGHT / 2);

		checkBounds();

	}

	private void checkBounds() {
		if (this.x < 0)
			this.x = 0;
		if (this.y < 0)
			this.y = 0;

		if (this.x > xMax)
			this.x = xMax;
		if (this.y > yMax)
			this.y = yMax;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

}
