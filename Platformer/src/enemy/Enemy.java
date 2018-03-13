package enemy;

import static helpers.Clock.delta;
import static helpers.Graphics.drawLineLoop;
import static helpers.Graphics.drawQuadTex;
import static helpers.Graphics.drawQuadTexFlipHorizontal;
import static helpers.Graphics.tileSize;
import static helpers.Physics.checkCollision;
import static helpers.Physics.getGravity;
import static helpers.Physics.getMaxFallSpeed;

import java.util.ArrayList;

import data.Animation;
import data.Camera;
import data.Entity;
import data.Texture;
import data.Tile;
import data.TileGrid;

public abstract class Enemy implements Entity {
	private TileGrid grid;
	private Camera camera;
	Animation sprite;
	float x, y; // physical x and y coordinates
	float xSpeed, ySpeed; // axis aligned speed vectors

	float xTextureOffset, yTextureOffset, yOffset;

	boolean facingRight, grounded; // returns true while the character is facing to the right

	/* Specific properties */
	float maxSpeed; // default max xSpeed, unrelated to ySpeed
	int width, height; // hitbox dimensions

	public Enemy(TileGrid grid, Camera c, float x, float y) {
		this.grid = grid;
		this.camera = c;
		this.x = x;
		this.y = y;
		this.xSpeed = 0;
		this.ySpeed = 0;

		this.xTextureOffset = 0;
		this.yTextureOffset = 0;
		this.yOffset = 0;

		this.facingRight = true;
		this.grounded = true;
	}

	public void update() {
		handleGravity();

		x += xSpeed * delta() * 60;
		if (grounded) {
			handleCollisionX_grounded(this, grid);
		} else {
			handleCollisionX_falling(this, grid);
		}

		y += ySpeed * delta() * 60;
		if (grounded) {
			handleCollisionY_grounded(this, grid);
		} else {
			handleCollisionY_falling(this, grid);
		}
	}

	void handleGravity() {
		ySpeed += getGravity() * delta() * 60;

		if (ySpeed > getMaxFallSpeed())
			ySpeed = getMaxFallSpeed();
	}

	private void setTextureOffsets(float yOffset) {
		Texture t = sprite.getCurrentTexture();
		xTextureOffset = x - ((t.getWidth() - width) / 2);
		yTextureOffset = y - ((t.getHeight() - height)) + yOffset;

	}

	public void draw(Camera c) {
		Texture t = sprite.animate();

		setTextureOffsets(this.yOffset);

		if (facingRight)
			drawQuadTex(c, t, xTextureOffset, yTextureOffset, t.getWidth(), t.getHeight());
		else
			drawQuadTexFlipHorizontal(c, t, xTextureOffset, yTextureOffset, t.getWidth(), t.getHeight());
	}

	public void drawHitbox(Camera c) {
		drawLineLoop(c, x, y, width, height);
	}

	void handleCollisionX_falling(Enemy c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesX(c, grid);

		if (tiles.size() > 0) {

			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {

					float xCoordLocal; // normalized x coordinate of the character's intersecting corner with the
					// current tile. A value of 0 represents the left edge of the tile, a value of 1
					// the right edge.
					float ySlopeL = t.getType().getyFloorL();
					float ySlopeR = t.getType().getyFloorR();

					if (ySlopeL == 0 && ySlopeR == 0) { // fully solid tile
						if (c.xSpeed > 0) {
							// entering tile from left
							c.x = t.getX() - c.width;
							c.xSpeed = 0;
							break;
						}
						if (c.xSpeed < 0) {
							// entering tile from right
							c.x = t.getX() + tileSize;
							c.xSpeed = 0;
							break;
						}

					} else { // slope tile

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

						float yThreshold = c.y; // minimum height difference
												// before collision
						// is
						// considered

						if (c.xSpeed > 0 && yLocal < yThreshold) {
							// entering tile from left
							c.x = t.getX() - c.width;
							c.xSpeed = 0;
							break;
						}
						if (c.xSpeed < 0 && yLocal < yThreshold) {
							// entering tile from right
							c.x = t.getX() + tileSize;
							c.xSpeed = 0;
							break;
						}
					}
				}
			}
		}

	}

	void handleCollisionX_grounded(Enemy c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesX(c, grid);

		if (tiles.size() > 0) {
			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {
					float yThreshold = c.y + c.height - 32; // minimum height difference before collision is considered
					if (c.xSpeed > 0 && t.getY() + t.getType().getyFloorL() < yThreshold) {
						// entering tile from left
						c.x = t.getX() - c.width;
						c.xSpeed = 0;
						break;
					}
					if (c.xSpeed < 0 && t.getY() + t.getType().getyFloorR() < yThreshold) {
						// entering tile from right
						c.x = t.getX() + tileSize;
						c.xSpeed = 0;
						break;
					}

				}
			}
		}

	}

	/**
	 * Looks for the nearest solid tiles in horizontal direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	protected ArrayList<Tile> getClosestSolidTilesX(Enemy c, TileGrid grid) {

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
	void handleCollisionY_falling(Enemy c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesY(c, grid);
		boolean collision = false;

		if (tiles.size() == 0) {
			// c.state.s.enterNewState(c, States.Jumping);
		} else {

			float yNew = c.y;

			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {
					collision = true;

					if (c.ySpeed > 0) { // character landed on a platform

						float xCoordLocal; // normalized x coordinate of the character's intersecting corner with the
						// current tile. A value of 0 represents the left edge of the tile, a value of 1
						// the right edge.
						float ySlopeL = t.getType().getyFloorL();
						float ySlopeR = t.getType().getyFloorR();

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

						// calculate the y position of the slope at the intersecting point
						float yLocal = t.getY() - c.height + ySlopeL + (ySlopeR - ySlopeL) * xCoordLocal;

						if (yLocal < yNew) {
							yNew = yLocal;
							c.ySpeed = 0;
							grounded = true;
						}
					}

					if (c.ySpeed < 0 && c.y > t.getY()) { // character hit a ceiling
						yNew = t.getY() + tileSize;
						c.ySpeed = 0;
						break;
					}

				}

			}

			if (collision) {
				c.y = yNew;
			} else { // character didn't collide with anything
				// c.state.s.enterNewState(c, States.Jumping);
			}
		}
	}

	/**
	 * Checks for collision in vertical direction.
	 */
	void handleCollisionY_grounded(Enemy c, TileGrid grid) {
		ArrayList<Tile> tiles = getClosestSolidTilesY(c, grid);
		boolean collision = false;

		if (tiles.size() == 0) {
			// c.state.s.enterNewState(c, States.Jumping);
		} else {

			float yNew = c.y;

			for (Tile t : tiles) {

				if (checkCollision(c.x, c.y, c.width, c.height, t)) {

					float xCoordLocal; // normalized x coordinate of the character's intersecting corner with the
										// current tile. A value of 0 represents the left edge of the tile, a value of 1
										// the right edge.
					float ySlopeL = t.getType().getyFloorL();
					float ySlopeR = t.getType().getyFloorR();

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
				// c.state.s.enterNewState(c, States.Jumping);
				grounded = false;
			}
		}
	}

	/**
	 * Looks for the nearest solid tiles in vertical direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	protected ArrayList<Tile> getClosestSolidTilesY(Enemy c, TileGrid grid) {

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
