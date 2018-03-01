package data;

import static helpers.Graphics.WIDTH;
import static helpers.Graphics.drawQuadTex;

public class MenuButton {

	private Texture texture;
	private float x, y;
	private int width, height;

	public MenuButton(Texture texture, float x, float y) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a horizontally centered button.
	 */
	public MenuButton(Texture texture, float y) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.x = (WIDTH / 2) - (width / 2);
		this.y = y;
	}

	public void draw() {
		drawQuadTex(texture, x, y, width, height);
	}

	public boolean isButtonClicked(float mouseX, float mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
			return true;
		else
			return false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
