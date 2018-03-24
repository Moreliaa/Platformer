package data;

import static helpers.Graphics.*;
import static helpers.Physics.*;

import java.util.*;

import character.Character;
import enemy.*;

public class Level {
	private TileGrid grid;
	private Character character;
	private ArrayList<Enemy> enemies;
	private Texture background;
	float backgroundScrollSpeed;

	public Level(TileGrid grid, Character character) {
		this.grid = grid;
		this.character = character;
		this.enemies = new ArrayList<Enemy>();
		background = new Texture("bg", 64, 341);
		backgroundScrollSpeed = 0.1f;
	}

	public Level(TileGrid grid, Character character, ArrayList<Enemy> enemies) {
		this.grid = grid;
		this.character = character;
		this.enemies = enemies;
		background = new Texture("bg", 64, 341);
		backgroundScrollSpeed = 0.1f;
	}

	public void update() {
		updateEnemies();
		character.update();

	}

	private void updateEnemies() {
		for (Enemy e : enemies) {
			if (getDistance(character.getX(), character.getY(), e.getX(), e.getY()) < e.getActivationRange())
				e.update();
		}

	}

	public TileGrid getGrid() {
		return grid;
	}

	public Character getCharacter() {
		return character;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void addEnemy(Enemy e) {
		this.enemies.add(e);
	}

	public void drawBackground(Camera camera) {
		float x = -camera.getX() * backgroundScrollSpeed;
		float y = -camera.getY() * backgroundScrollSpeed;
		while (x < -background.getWidth()) {
			x += background.getWidth();
		}

		while (y < -background.getHeight()) {
			x += background.getHeight();
		}
		fill(background, x, y, background.getWidth(), background.getHeight());
	}

	public void drawGrid(Camera camera) {
		grid.draw(camera);

	}

	public void drawEnemies(Camera camera) {
		for (Enemy e : enemies) {
			if (getDistance(character.getX(), character.getY(), e.getX(), e.getY()) < e.getActivationRange())
				e.draw(camera);
		}

	}

	public void drawCharacter(Camera camera) {
		character.draw(camera);

	}
}
