package data;

import static helpers.Graphics.tileSize;
import static helpers.LevelManager.loadMap;
import static helpers.LevelManager.saveMap;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import helpers.KeyboardHandler;
import helpers.MouseHandler;

public class Editor {

	private TileGrid grid;
	private Camera camera;
	private int cameraSpeed;

	public Editor(String fileName) {
		grid = loadMap(fileName);
		camera = new Camera(grid);
		cameraSpeed = 100;
	}

	public Editor(int[][] map) {
		grid = new TileGrid(map);
		camera = new Camera(grid);
		cameraSpeed = 100;
	}

	public void update() {
		// TODO Auto-generated method stub
		handleInput();
		grid.draw(camera);
	}

	private void handleInput() {
		if (MouseHandler.isButtonDown(0))
			setTile(TileType.Block);
		if (MouseHandler.isButtonDown(1))
			setTile(TileType.Background);

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			camera.move(cameraSpeed, 0);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			camera.move(-cameraSpeed, 0);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
			camera.move(0, -cameraSpeed);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_DOWN) && !KeyboardHandler.isKeyDown(GLFW_KEY_UP))
			camera.move(0, cameraSpeed);

		if (KeyboardHandler.isKeyDown(GLFW_KEY_S))
			saveMap("map", grid);
	}

	private void setTile(TileType type) {
		grid.setTile((int) Math.floor((MouseHandler.getxPos() + camera.getX()) / tileSize),
				(int) Math.floor((MouseHandler.getyPos() + camera.getY() - 1) / tileSize), type);
	}

}
