package data;

import static helpers.Graphics.HEIGHT;
import static helpers.Graphics.WIDTH;
import static helpers.Graphics.tileSize;

import character.Character;

public class Camera {
	private float x, y, xMax, yMax;
	private WaveSine screenShake;

	public Camera(TileGrid grid) {
		x = 0;
		y = 0;

		adjustBounds(grid.getMapWidth() * tileSize - WIDTH, grid.getMapHeight() * tileSize - HEIGHT);

		this.screenShake = null;
	}

	public void updatePosition(float x, float y) {
		this.x = x;
		this.y = y;

		checkBounds();
		applyScreenShake();
	}

	public void move(float xSpeed, float ySpeed) {
		this.x += xSpeed;
		this.y += ySpeed;

		checkBounds();
		applyScreenShake();
	}

	public void centerOn(Character c) {
		centerOn(c.getX(), c.getY(), c.getWidth(), c.getHeight());
	}

	public void centerOn(float x, float y, int width, int height) {

		this.x = x + (width / 2) - (WIDTH / 2);
		this.y = y + (height / 2) - (HEIGHT / 2);

		checkBounds();
		applyScreenShake();

	}

	public void shake(float amplitudeInPixels, int lengthInFrames) {
		screenShake = new WaveSine(amplitudeInPixels, lengthInFrames);
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

	private void applyScreenShake() {
		if (screenShake != null) {
			float phase = screenShake.getPhase();

			if (phase == 0) // effect finished
				screenShake = null;
			else
				this.y += phase;
		}
	}

	/**
	 * Add the specified values to the camera bounds.
	 */
	public void adjustBounds(float x, float y) {
		xMax += x;
		yMax += y;

		if (xMax < 0)
			xMax = 0;
		if (yMax < 0)
			yMax = 0;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

}
