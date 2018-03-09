package character;

import static helpers.Graphics.tileSize;
import static helpers.Physics.checkCollision;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import java.util.ArrayList;

import data.EffectAfterimage;
import data.Tile;
import data.TileGrid;
import helpers.Clock;
import helpers.KeyboardHandler;
import helpers.Physics;

public class StateStanding extends CharacterState {

	/**
	 * Called whenever this state is entered.
	 */
	@Override
	public void enter(Character c) {
		c.boostsLeft = c.boostsPerJump; // restore boost ability
	}

	@Override
	public void handleInput(Character c) {
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_UP)) {
			c.jumpDisabled = false;
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT) && !KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
			moveRight(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			moveLeft(c);
		if (!KeyboardHandler.isKeyDown(GLFW_KEY_LEFT) && !KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
			stop(c);
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !c.jumpDisabled)
			jump(c);

		super.setFlags(c);

	}

	@Override
	public void update(Character c) {
		if (c.dashActive) {
			EffectAfterimage e = new EffectAfterimage(c, c.state.sprite.getCurrentTexture(), 1, 1, 1);
			data.Game.addNewEffect(e);
		}

		super.handleGravity(c);
	}

	private void moveRight(Character c) {
		enterNewState(c, States.Walking);

		c.framesHeldLeft = 0;
		c.framesHeldRight += (1 * Clock.delta() * 60);

		if (c.framesHeldRight >= c.framesUntilDashActivation) {
			c.dashActive = true;
		} else {
			c.dashActive = false;
		}

		if (c.dashActive) {
			c.xSpeed = c.maxSpeed * 2;
		} else {
			c.xSpeed = c.maxSpeed;
		}
	}

	private void moveLeft(Character c) {
		enterNewState(c, States.Walking);

		c.framesHeldLeft += (1 * Clock.delta() * 60);
		c.framesHeldRight = 0;

		if (c.framesHeldLeft >= c.framesUntilDashActivation) {
			c.dashActive = true;
		} else {
			c.dashActive = false;
		}

		if (c.dashActive) {
			c.xSpeed = -c.maxSpeed * 2;
		} else {
			c.xSpeed = -c.maxSpeed;
		}

	}

	private void jump(Character c) {
		enterNewState(c, States.Jumping);
		c.ySpeed = Physics.getInitialJumpVelocity();
	}

	private void stop(Character c) {
		enterNewState(c, States.Standing); // only relevant for the overlapping Walking state

		c.framesHeldLeft = 0;
		c.framesHeldRight = 0;
		c.dashActive = false;

		c.xSpeed = 0;
	}

	/**
	 * Checks for collision in horizontal direction.
	 */
	@Override
	void handleCollisionX(Character c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesX(c, grid);

		if (tiles.size() > 0) {
			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {
					float yThreshold = c.y + c.height - 32; // minimum height difference before collision is considered
					if (c.xSpeed > 0 && t.getY() + t.getType().getyCoordSlopeL() < yThreshold) {
						// entering tile from left
						c.x = t.getX() - c.width;
						c.xSpeed = 0;
						c.framesHeldLeft = 0;
						c.framesHeldRight = 0;
						c.dashActive = false;
						break;
					}
					if (c.xSpeed < 0 && t.getY() + t.getType().getyCoordSlopeR() < yThreshold) {
						// entering tile from right
						c.x = t.getX() + tileSize;
						c.xSpeed = 0;
						c.framesHeldLeft = 0;
						c.framesHeldRight = 0;
						c.dashActive = false;
						break;
					}

				}
			}
		}

	}

	/**
	 * Checks for collision in vertical direction.
	 */
	@Override
	void handleCollisionY(Character c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesY(c, grid);
		boolean collision = false;

		if (tiles.size() == 0) {
			c.state.s.enterNewState(c, States.Jumping);
		} else {

			float yNew = c.getY();

			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {

					float xCoordLocal; // normalized x coordinate of the character's intersecting corner with the
										// current tile. A value of 0 represents the left edge of the tile, a value of 1
										// the right edge.
					float ySlopeL = t.getType().getyCoordSlopeL();
					float ySlopeR = t.getType().getyCoordSlopeR();

					// set the local x coordinate
					if (c.x >= t.getX()) { // bottom-left corner of char intersects
						if (ySlopeL < ySlopeR)
							xCoordLocal = (float) Math.floorMod((int) c.x, tileSize) / tileSize;
						else
							xCoordLocal = 1;
					} else { // bottom-right corner of char intersects
						if (ySlopeL < ySlopeR)
							xCoordLocal = 0;
						else if (c.x + c.width <= t.getX() + tileSize)
							xCoordLocal = (float) Math.floorMod((int) c.x + c.width, tileSize) / tileSize;
						else
							xCoordLocal = 1;
					}

					// calculate the y position of the character at the intersecting point
					float yLocal = t.getY() - c.height + ySlopeL + (ySlopeR - ySlopeL) * xCoordLocal;

					if (yLocal < yNew) {
						yNew = yLocal;
						c.ySpeed = 0;
					}

					collision = true;
				}

			}

			if (collision) {
				c.y = yNew;
			} else { // character didn't collide with anything
				c.state.s.enterNewState(c, States.Jumping);
			}
		}
	}

}
