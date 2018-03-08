package data;

import static helpers.Graphics.tileSize;
import static helpers.LevelManager.loadMap;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.util.ArrayList;

import character.Character;
import helpers.KeyboardHandler;
import helpers.StateManager;
import helpers.StateManager.GameState;

public class Game {

	private TileGrid grid;
	private Camera camera;
	private Character character;
	private static ArrayList<Effect> effects;

	public Game(int[][] map) {
		this.grid = new TileGrid(map);
		this.camera = new Camera(grid);
		this.character = new Character(grid, camera, 9 * tileSize, 4 * tileSize);
		effects = new ArrayList<Effect>();

	}

	public Game(String map) {
		this.grid = loadMap(map);
		this.camera = new Camera(grid);
		this.character = new Character(grid, camera, 9 * tileSize, 4 * tileSize);
		effects = new ArrayList<Effect>();
	}

	public void update() {
		handleInput();
		character.update();
		camera.centerOn(character);
		grid.draw(camera);
		drawEffects();
		character.draw(camera);
		character.drawDiagnostics(camera);
	}

	private void handleInput() {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE))
			StateManager.setGameState(GameState.MAINMENU);
	}

	public static void addNewEffect(Effect e) {
		effects.add(e);
	}

	private void drawEffects() {
		ArrayList<Effect> toBeRemoved = new ArrayList<Effect>();

		for (Effect e : effects) {
			if (e.effectFinished())
				toBeRemoved.add(e);
			else
				e.draw(camera);
		}

		effects.removeAll(toBeRemoved);
	}
}
