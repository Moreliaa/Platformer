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
	private boolean drawDiag;
	private static ArrayList<Effect> effects;

	public Game(int[][] map) {
		drawDiag = false;

		TileGrid grid = new TileGrid(map);
		this.camera = new Camera(grid);
		Character c = new Character(grid, camera, 5 * tileSize, 15 * tileSize);
		level = new Level(grid, c);
		effects = new ArrayList<Effect>();
		level.addEnemy(new Wheel(level, camera, 15 * tileSize, 10 * tileSize, -5));

	}

	public Game(String map) {
		drawDiag = false;
		TileGrid grid = loadMap(map);
		this.camera = new Camera(grid);
		Character c = new Character(grid, camera, 5 * tileSize, 15 * tileSize);
		level = new Level(grid, c);
		effects = new ArrayList<Effect>();
		level.addEnemy(new Wheel(level, camera, 15 * tileSize, 10 * tileSize, -5));
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
		if (drawDiag)
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
		if (KeyboardHandler.wasKeyReleased(GLFW_KEY_F1))
			drawDiag = !drawDiag;
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
