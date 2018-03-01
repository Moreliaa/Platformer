package data;

import static helpers.Graphics.*;
import static org.lwjgl.glfw.GLFW.*;

import helpers.KeyboardHandler;
import static helpers.LevelManager.*;
import helpers.MouseHandler;

public class Editor {

	private TileGrid grid;
	private Camera camera;
	private int index, cameraSpeed;
	private TileType[] types;

	public Editor(String fileName) {
		grid = loadMap(fileName);
		camera = new Camera(grid);
		cameraSpeed = 100;
		index = 1;
		this.types = new TileType[2];
		this.types[0] = TileType.Background;
		this.types[1] = TileType.Block;
	}

	public Editor(int[][] map) {
		grid = new TileGrid(map);
		camera = new Camera(grid);
		cameraSpeed = 100;
		index = 1;
		this.types = new TileType[2];
		this.types[0] = TileType.Background;
		this.types[1] = TileType.Block;
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

	@SuppressWarnings("unused")
	private void moveIndex(int change) {
		index += change;

		if (index < 0)
			index = types.length - 1;
		if (index >= types.length)
			index = 0;
	}

}
