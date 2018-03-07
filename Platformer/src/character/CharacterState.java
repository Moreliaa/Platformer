package character;

import static helpers.Clock.delta;
import static helpers.Graphics.tileSize;
import static helpers.Physics.checkCollision;
import static helpers.Physics.getGravity;
import static helpers.Physics.getMaxFallSpeed;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

import java.util.ArrayList;

import data.Tile;
import data.TileGrid;
import helpers.KeyboardHandler;

public abstract class CharacterState {

	/**
	 * Called whenever a state is entered.
	 */
	abstract public void enter(Character c);

	/**
	 * Must be called to enter a new state.
	 */
	public void enterNewState(Character c, States s) {
		if (c.state != s) {
			c.state = s;
			c.state.s.enter(c);
		}
	}

	abstract public void handleInput(Character c);

	abstract public void update(Character c);

	public void setFlags(Character c) {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			c.facingRight = true;
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			c.facingRight = false;

		if (KeyboardHandler.isKeyDown(GLFW_KEY_A))
			c.boostDisabled = true;
		else if (c.boostsLeft > 0)
			c.boostDisabled = false;
	}

	public void handleGravity(Character c) {
		c.ySpeed += getGravity() * delta() * 60;

		if (c.ySpeed > getMaxFallSpeed())
			c.ySpeed = getMaxFallSpeed();
	}

	/**
	 * Checks for collision in horizontal direction.
	 */
	void handleCollisionX(Character c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesX(c, grid);

		if (tiles.size() > 0) {
			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {
					if (c.xSpeed > 0) {
						c.x = t.getX() - c.width;
						if (c.state == States.Jumping && KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && c.ySpeed > 0)
							c.state.s.enterNewState(c, States.WallCling);
					}
					if (c.xSpeed < 0) {
						c.x = t.getX() + tileSize;
						if (c.state == States.Jumping && KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && c.ySpeed > 0)
							c.state.s.enterNewState(c, States.WallCling);
					}
					c.xSpeed = 0;
					break;
				}
			}
		}

	}

	/**
	 * Looks for the nearest solid tiles in horizontal direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	protected ArrayList<Tile> getClosestSolidTilesX(Character c, TileGrid grid) {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xCoord = (int) Math.floor(c.x / tileSize);
		int yCoord = (int) Math.floor(c.y / tileSize);

		for (int i = yCoord; i <= yCoord + Math.floor(c.height / tileSize) + 1 && i < grid.getMapHeight(); i++) {

			for (int j = xCoord; j <= xCoord + Math.floor((c.width / tileSize)) + 1 && j < grid.getMapWidth(); j++) {
				if (grid.getTile(j, i).isSolid()) {
					tiles.add(grid.getTile(j, i));
					break; // step to the next y coordinate
				}
			}

		}

		return tiles;
	}

	/**
	 * Checks for collision in vertical direction.
	 */
	void handleCollisionY(Character c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesY(c, grid);
		boolean collision = false;

		if (tiles.size() == 0) {
			c.state.s.enterNewState(c, States.Jumping);
		} else {

			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {

					if (c.ySpeed > 0) { // character landed on a platform
						c.y = t.getY() - c.height;
						if (c.state == States.Jumping || c.state == States.WallCling)
							c.state.s.enterNewState(c, States.Standing);
					}
					if (c.ySpeed < 0) { // character hit a ceiling
						c.y = t.getY() + tileSize;
						c.jumpDisabled = true;
					}
					c.ySpeed = 0;
					collision = true;
					break;
				}

			}

			if (!collision) { // character didn't collide with anything
				c.state.s.enterNewState(c, States.Jumping);
			}
		}
	}

	/**
	 * Looks for the nearest solid tiles in vertical direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	protected ArrayList<Tile> getClosestSolidTilesY(Character c, TileGrid grid) {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xCoord = (int) Math.floor(c.x / tileSize);
		int yCoord = (int) Math.floor(c.y / tileSize);

		for (int i = xCoord; i <= xCoord + Math.floor((c.width / tileSize)) + 1 && i < grid.getMapWidth(); i++) {

			for (int j = yCoord; j <= yCoord + Math.floor((c.height / tileSize)) + 1 && j < grid.getMapHeight(); j++) {
				if (grid.getTile(i, j).isSolid()) {
					tiles.add(grid.getTile(i, j));
					break; // step to the next x coordinate
				}
			}

		}

		return tiles;
	}

}
