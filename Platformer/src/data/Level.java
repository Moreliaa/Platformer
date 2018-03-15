package data;

import static helpers.Physics.getDistance;

import java.util.ArrayList;

import character.Character;
import enemy.Enemy;

public class Level {
	private TileGrid grid;
	private Character character;
	private ArrayList<Enemy> enemies;

	public Level(TileGrid grid, Character character) {
		this.grid = grid;
		this.character = character;
		this.enemies = new ArrayList<Enemy>();
	}

	public Level(TileGrid grid, Character character, ArrayList<Enemy> enemies) {
		this.grid = grid;
		this.character = character;
		this.enemies = enemies;
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
