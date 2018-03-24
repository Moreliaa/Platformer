package data;

import static helpers.Graphics.*;
import static helpers.LevelManager.*;
import static org.lwjgl.glfw.GLFW.*;

import java.util.*;

import character.Character;
import enemy.*;
import helpers.*;
import helpers.StateManager.*;

public class Game {

	private Level level;
	private Camera camera;
	private static ArrayList<Effect> effects;

	public Game(int[][] map) {

		TileGrid grid = new TileGrid(map);
		this.camera = new Camera(grid);
		Character c = new Character(grid, camera, 9 * tileSize, 4 * tileSize);
		level = new Level(grid, c);
		effects = new ArrayList<Effect>();
		level.addEnemy(new Wheel(level, camera, 40 * tileSize, 1 * tileSize, -5));

	}

	public Game(String map) {
		TileGrid grid = loadMap(map);
		this.camera = new Camera(grid);
		Character c = new Character(grid, camera, 9 * tileSize, 4 * tileSize);
		level = new Level(grid, c);
		effects = new ArrayList<Effect>();
		level.addEnemy(new Wheel(level, camera, 40 * tileSize, 1 * tileSize, -5));
	}

	public void update() {
		handleInput();
		level.update();
		camera.centerOn(level.getCharacter());
		level.drawBackground(camera);
		level.drawGrid(camera);
		drawEffects();
		level.drawEnemies(camera);
		level.drawCharacter(camera);
		drawDiagnostics();

	}

	private void drawDiagnostics() {
		level.getCharacter().drawDiagnostics(camera);

		for (Entity e : level.getEnemies()) {
			e.drawHitbox(camera);
		}

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
