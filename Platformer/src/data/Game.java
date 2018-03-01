package data;

import static helpers.Graphics.*;
import static helpers.LevelManager.*;

import character.Character;

public class Game {

	private TileGrid grid;
	private Character character;
	private Camera camera;

	public Game(int[][] map) {
		this.grid = new TileGrid(map);
		this.character = new Character(grid, 9 * tileSize, 4 * tileSize);
		this.camera = new Camera(grid);

	}
	
	public Game(String map) {
		this.grid = loadMap(map);
		this.character = new Character(grid, 9 * tileSize, 4 * tileSize);
		this.camera = new Camera(grid);
	}

	public void update() {
		character.update();
		camera.centerOn(character);
		grid.draw(camera);
		character.draw(camera);
		character.drawDiagnostics(camera);
	}
}
