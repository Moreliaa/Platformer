package enemy;

import static helpers.Clock.delta;
import static helpers.Graphics.drawLineLoop;
import static helpers.Graphics.drawQuadTex;
import static helpers.Graphics.drawQuadTexFlipHorizontal;
import static helpers.Graphics.tileSize;
import static helpers.Physics.getGravity;
import static helpers.Physics.getMaxFallSpeed;
import static helpers.Physics.stepX;
import static helpers.Physics.stepY;

import data.Animation;
import data.Camera;
import data.Entity;
import data.Texture;
import data.TileGrid;

public abstract class Enemy implements Entity {
	private TileGrid grid;
	private Camera camera;
	Animation sprite;
	float x, y; // physical x and y coordinates
	float xSpeed, ySpeed; // axis aligned speed vectors

	float xTextureOffset, yTextureOffset, yOffset;

	boolean facingRight; // returns true while the character is facing to the right

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
	}

	public void update() {
		handleGravity();

		if (stepX(this)) {
			this.xSpeed = 0;
		}

		if (stepY(this)) {
			this.ySpeed = 0;
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

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getxSpeed() {
		return this.xSpeed;
	}

	@Override
	public float getySpeed() {
		return this.ySpeed;
	}

	@Override
	public void setX(float xNew) {
		this.x = xNew;
	}

	@Override
	public void setY(float yNew) {
		this.y = yNew;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public TileGrid getGrid() {
		return grid;
	}

	@Override
	public int getxCoord() {
		return Math.floorDiv((int) x, tileSize);
	}

	@Override
	public int getyCoord() {
		return Math.floorDiv((int) y, tileSize);
	}

	@Override
	public int getxCoordR() {
		if (Math.floorMod((int) (x + width), tileSize) == 0)
			return Math.floorDiv((int) (x + width), tileSize) - 1;
		else
			return Math.floorDiv((int) (x + width), tileSize);
	}

	@Override
	public int getyCoordB() {
		if (Math.floorMod((int) (y + height), tileSize) == 0)
			return Math.floorDiv((int) (y + height), tileSize) - 1;
		else
			return Math.floorDiv((int) (y + height), tileSize);
	}

}
