package data;

import static helpers.Clock.*;
import static helpers.Graphics.*;
import static helpers.LevelManager.*;
import static org.lwjgl.glfw.GLFW.*;

import helpers.*;
import helpers.StateManager.*;

public class Editor {

	private TileGrid grid;
	private Camera camera;
	private EditorTileSelector tileSelector;
	private int cameraSpeed;
	private TileType[] types;
	private int index;

	public Editor(String fileName) {
		grid = loadMap(fileName);
		camera = new Camera(grid);
		cameraSpeed = 100;
		types = TileType.values();
		tileSelector = new EditorTileSelector(types);
		camera.adjustBounds(tileSelector.getWidth(), 0);
		index = 0;
	}

	public Editor(int[][] map) {
		grid = new TileGrid(map);
		camera = new Camera(grid);
		cameraSpeed = 100;
		types = TileType.values();
		tileSelector = new EditorTileSelector(types);
		index = 0;
	}

	public Editor(int width, int height) {
		grid = new TileGrid(width, height);
		camera = new Camera(grid);
		cameraSpeed = 100;
		types = TileType.values();
		tileSelector = new EditorTileSelector(types);
		index = 0;
	}

	public void update() {
		handleInput();
		grid.draw(camera);
		tileSelector.draw();
	}

	private void handleInput() {
		if (MouseHandler.isButtonDown(0) && tileSelector.mouseOver(MouseHandler.getxPos(), MouseHandler.getyPos())) {
			index = tileSelector.setIndex(index);
		} else {
			if (MouseHandler.isButtonDown(0))
				setTile(types[index]);
			if (MouseHandler.isButtonDown(1))
				setTile(TileType.Background);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			camera.move(cameraSpeed * delta() * 60, 0);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			camera.move(-cameraSpeed * delta() * 60, 0);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
			camera.move(0, -cameraSpeed * delta() * 60);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_DOWN) && !KeyboardHandler.isKeyDown(GLFW_KEY_UP))
			camera.move(0, cameraSpeed * delta() * 60);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE))
			StateManager.setGameState(GameState.MAINMENU);

		if (KeyboardHandler.wasKeyReleased(GLFW_KEY_A)) {
			moveIndex(-1);
		}
		if (KeyboardHandler.wasKeyReleased(GLFW_KEY_D))
			moveIndex(1);
		if (KeyboardHandler.wasKeyReleased(GLFW_KEY_S))
			saveMap("map", grid);

	}

	private void moveIndex(int i) {
		index += i;

		if (index >= types.length)
			index = 0;
		if (index < 0)
			index = types.length - 1;

	}

	private void setTile(TileType type) {
		grid.setTile((int) Math.floor((MouseHandler.getxPos() + camera.getX()) / tileSize),
				(int) Math.floor((MouseHandler.getyPos() + camera.getY() - 1) / tileSize), type);
	}

}
