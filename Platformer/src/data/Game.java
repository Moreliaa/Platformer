package data;

import static helpers.Graphics.tileSize;
import static helpers.LevelManager.loadMap;

import character.Character;

public class Game {

	private TileGrid grid;
	private Camera camera;
	private Character character;

	public Game(int[][] map) {
		this.grid = new TileGrid(map);
		this.camera = new Camera(grid);
		this.character = new Character(grid, camera, 9 * tileSize, 4 * tileSize);

	}

	public Game(String map) {
		this.grid = loadMap(map);
		this.camera = new Camera(grid);
		this.character = new Character(grid, camera, 9 * tileSize, 4 * tileSize);
	}

	public void update() {
		character.update();
		camera.centerOn(character);
		grid.draw(camera);
		character.draw(camera);
		character.drawDiagnostics(camera);
	}
}
