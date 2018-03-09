package data;

import static helpers.Graphics.HEIGHT;
import static helpers.Graphics.WIDTH;
import static helpers.Graphics.drawQuad;
import static helpers.Graphics.tileSize;

import helpers.MouseHandler;

public class EditorTileSelector {
	private int width, spacing;
	private TileType[] types;
	private MenuButton[] buttons;

	public EditorTileSelector(TileType[] types) {
		this.spacing = 5;
		this.width = (tileSize * 2 + spacing * 3);
		this.types = types;
		this.buttons = new MenuButton[types.length];
		createButtons();
	}

	private void createButtons() {
		float xEdge = WIDTH - width;
		float y = 0;

		for (int i = 0; i < buttons.length; i++) {
			if (Math.floorMod(i, 2) == 0)
				buttons[i] = new MenuButton(types[i].texture, xEdge + spacing, spacing * (i + 1) + y);
			else
				buttons[i] = new MenuButton(types[i].texture, xEdge + spacing * 2 + tileSize, spacing * i + y);

			y += tileSize * Math.floorMod(i, 2);
		}

	}

	public void draw() {
		drawQuad(WIDTH - width, 0f, width, HEIGHT);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].draw();
		}
	}

	public float getWidth() {
		return width;
	}

	public boolean mouseOver(float mouseX, float mouseY) {
		if (mouseX > WIDTH - width && mouseX < WIDTH)
			return true;
		else
			return false;
	}

	public int setIndex(int index) {
		int newIndex = index;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isButtonClicked(MouseHandler.getxPos(), MouseHandler.getyPos())) {
				newIndex = i;
			}
		}
		return newIndex;

	}
}
