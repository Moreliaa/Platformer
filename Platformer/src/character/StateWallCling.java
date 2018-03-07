package character;

import static helpers.Graphics.tileSize;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import java.util.ArrayList;

import data.Tile;
import data.TileGrid;
import helpers.KeyboardHandler;
import helpers.Physics;

public class StateWallCling extends CharacterState {

	private boolean allowExit;
	private float maxSlideSpeed = 6;

	@Override
	public void enter(Character c) {
		allowExit = false;
		c.wallJumpDisabled = true;
		if (c.ySpeed > maxSlideSpeed)
			c.ySpeed = maxSlideSpeed;
	}

	/**
	 * Must be called to enter a new state.
	 */
	@Override
	public void enterNewState(Character c, States s) {
		if ((s == States.Jumping && allowExit == true) || s == States.Standing || s == States.WallJump) {
			c.state = s;
			c.state.s.enter(c);
		}
		// TODO fix bug failing to enter jump state when sliding off wall
	}

	@Override
	public void handleInput(Character c) {

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT)
				&& c.facingRight == false)
			release(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT)
				&& c.facingRight == true)
			release(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !c.wallJumpDisabled)
			jump(c);

		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP))
			c.wallJumpDisabled = false;

	}

	private void jump(Character c) {
		allowExit = true;
		c.ySpeed = Physics.getInitialSmallJumpVelocity();
		if (c.isFacingRight()) {
			c.xSpeed = -c.maxSpeed;

		} else {
			c.xSpeed = c.maxSpeed;

		}
		enterNewState(c, States.Jumping);

	}

	private void release(Character c) {
		allowExit = true;
		enterNewState(c, States.Jumping);

	}

	@Override
	public void update(Character c) {
		c.ySpeed += Physics.getGravity();

		if (c.ySpeed > maxSlideSpeed)
			c.ySpeed = maxSlideSpeed;
	}

	/**
	 * Checks for collision in horizontal direction.
	 */
	void handleCollisionX(Character c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesX(c, grid);

		if (tiles.size() == 0) {
			allowExit = true;
			enterNewState(c, States.Jumping);
		}

	}

	/**
	 * Looks for the nearest solid tiles in horizontal direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	protected ArrayList<Tile> getClosestSolidTilesX(Character c, TileGrid grid) {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xCoord = (int) Math.floor(c.x / tileSize);
		if (!c.isFacingRight() && xCoord > 0)
			xCoord -= 1;
		int yCoord = (int) Math.floor(c.y / tileSize);

		for (int i = yCoord; i <= yCoord + Math.floor(c.height / tileSize) + 1 && i < grid.getMapHeight(); i++) {

			for (int j = xCoord; j <= xCoord + Math.floor((c.width / tileSize)) + 2 && j < grid.getMapWidth(); j++) {
				if (grid.getTile(j, i).isSolid()) {
					tiles.add(grid.getTile(j, i));
					break; // step to the next y coordinate
				}
			}

		}

		return tiles;
	}

}
