package character;

import static helpers.Clock.delta;
import static helpers.Graphics.drawLineLoop;
import static helpers.Graphics.drawQuadTex;
import static helpers.Graphics.drawQuadTexFlipHorizontal;
import static helpers.Graphics.tileSize;
import static helpers.Physics.checkCollision;

import java.util.ArrayList;

import data.Camera;
import data.Texture;
import data.Tile;
import data.TileGrid;

public class Character {

	private TileGrid grid;
	private Camera camera;
	float x, y; // physical x and y coordinates
	float xSpeed, ySpeed; // axis aligned speed vectors
	float maxSpeed; // default max xSpeed, unrelated to ySpeed

	float airControl; // strength of air control
	float airFriction; // strength of air resistance

	int width, height; // hitbox dimensions

	States state;
	boolean facingRight; // returns true while the character is facing to the right

	boolean jumpDisabled, boostDisabled;

	int boostsPerJump, boostsLeft;

	boolean dashActive;
	float framesHeldLeft, framesHeldRight, framesUntilDashActivation;

	public Character(TileGrid grid, Camera c, float x, float y) {
		this.grid = grid;
		this.camera = c;
		this.x = x;
		this.y = y;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.maxSpeed = 10;
		this.airControl = 2;
		this.airFriction = 0.1f;
		this.width = 60;
		this.height = 115;

		this.state = States.Standing;
		this.facingRight = true;

		this.jumpDisabled = true;
		this.boostDisabled = false;

		this.boostsPerJump = 1;
		this.boostsLeft = 1;

		this.dashActive = false;
		this.framesHeldLeft = 0;
		this.framesHeldRight = 0;
		this.framesUntilDashActivation = 60;
	}

	public void update() {
		state.s.handleInput(this);
		state.s.update(this);

		x += xSpeed * delta() * 60;
		handleCollisionX();

		y += ySpeed * delta() * 60;
		handleCollisionY();
	}

	public void drawDiagnostics(Camera c) {
		drawHitbox(c);
	}

	/**
	 * Checks for collision in horizontal direction.
	 */
	private void handleCollisionX() {
		ArrayList<Tile> tiles = getClosestSolidTilesX();

		if (tiles.size() > 0) {
			for (Tile t : tiles) {

				if (checkCollision(x, y, width, height, t)) {
					if (xSpeed > 0)
						x = t.getX() - width;
					if (xSpeed < 0)
						x = t.getX() + tileSize;
					xSpeed = 0;
					break;
				}
			}
		}

	}

	/**
	 * Looks for the nearest solid tiles in horizontal direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	private ArrayList<Tile> getClosestSolidTilesX() {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xCoord = (int) Math.floor(x / tileSize);
		int yCoord = (int) Math.floor(y / tileSize);

		for (int i = yCoord; i <= yCoord + Math.floor(height / tileSize) + 1 && i < grid.getMapHeight(); i++) {

			for (int j = xCoord; j <= xCoord + Math.floor((width / tileSize)) + 1 && j < grid.getMapWidth(); j++) {
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
	private void handleCollisionY() {
		ArrayList<Tile> tiles = getClosestSolidTilesY();
		boolean collision = false;

		if (tiles.size() == 0) {
			state.s.enterNewState(this, States.Jumping);
		} else {

			for (Tile t : tiles) {

				if (checkCollision(x, y, width, height, t)) {

					if (ySpeed > 0) { // character landed on a platform
						y = t.getY() - height;
						if (state == States.Jumping)
							state.s.enterNewState(this, States.Standing);
					}
					if (ySpeed < 0) { // character hit a ceiling
						y = t.getY() + tileSize;
						jumpDisabled = true;
					}
					ySpeed = 0;
					collision = true;
					break;
				}

			}

			if (!collision) { // character didn't collide with anything
				state.s.enterNewState(this, States.Jumping);
			}
		}
	}

	/**
	 * Looks for the nearest solid tiles in vertical direction relative to the
	 * character's x and y position that might overlap with the character.
	 */
	private ArrayList<Tile> getClosestSolidTilesY() {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xCoord = (int) Math.floor(x / tileSize);
		int yCoord = (int) Math.floor(y / tileSize);

		for (int i = xCoord; i <= xCoord + Math.floor((width / tileSize)) + 1 && i < grid.getMapWidth(); i++) {

			for (int j = yCoord; j <= yCoord + Math.floor((height / tileSize)) + 1 && j < grid.getMapHeight(); j++) {
				if (grid.getTile(i, j).isSolid()) {
					tiles.add(grid.getTile(i, j));
					break; // step to the next x coordinate
				}
			}

		}

		return tiles;
	}

	public void draw(Camera c) {
		Texture t = state.sprite.animate();
		float xTexture = x - ((t.getWidth() - width) / 2);
		float yTexture = y - ((t.getHeight() - height)) + 18;

		if (facingRight)
			drawQuadTex(c, t, xTexture, yTexture, t.getWidth(), t.getHeight());
		else
			drawQuadTexFlipHorizontal(c, t, xTexture, yTexture, t.getWidth(), t.getHeight());
	}

	public void drawHitbox(Camera c) {
		drawLineLoop(c, x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}
