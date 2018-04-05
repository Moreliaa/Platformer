package character;

import static helpers.Graphics.*;

import data.*;

public class Character implements Entity {

	private TileGrid grid;
	private Camera camera;
	float x, y; // physical x and y coordinates
	float xSpeed, ySpeed; // axis aligned speed vectors
	float maxSpeed; // default max xSpeed, unrelated to ySpeed

	int health;

	float airControl; // strength of air control
	float airFriction; // strength of air resistance

	int width, height; // hitbox dimensions

	float xTextureOffset, yTextureOffset;

	States state;
	boolean facingRight; // returns true while the character is facing to the right

	boolean damageInvul; // invulnerability window after being damaged
	int damageInvulDurationTotal, damageInvulDurationCurrent; // invulnerability timer after landing
	int damageInvulSpriteFlashInterval, damageInvulSpriteFlashDuration;

	boolean jumpDisabled, wallJumpDisabled, boostDisabled;

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
		this.health = 3;
		this.airControl = 2;
		this.airFriction = 0.1f;
		this.width = 60;
		this.height = 115;

		this.xTextureOffset = 0;
		this.yTextureOffset = 0;

		this.state = States.Standing;
		this.facingRight = true;

		this.damageInvul = false;
		this.damageInvulDurationTotal = 60;
		this.damageInvulDurationCurrent = 0;
		this.damageInvulSpriteFlashInterval = 6;
		this.damageInvulSpriteFlashDuration = 3;

		this.jumpDisabled = true;
		this.wallJumpDisabled = true;
		this.boostDisabled = false;

		this.boostsPerJump = 1;
		this.boostsLeft = boostsPerJump;

		this.dashActive = false;
		this.framesHeldLeft = 0;
		this.framesHeldRight = 0;
		this.framesUntilDashActivation = 60;
	}

	public void update() {
		state.s.handleInput(this);
		state.s.update(this);

	}

	public void drawDiagnostics(Camera c) {
		drawHitbox(c);
		// System.out.println(x);
		// System.out.println(y);
	}

	public void draw(Camera c) {
		Texture t = state.sprite.animate();

		setTextureOffsets();

		boolean flash = false;
		if (this.damageInvulDurationCurrent > 0 && this.damageInvulDurationCurrent != this.damageInvulDurationTotal) {
			if (this.damageInvulDurationCurrent
					% this.damageInvulSpriteFlashInterval <= this.damageInvulSpriteFlashDuration)

				flash = true;
		}

		if (flash == false) {
			if (facingRight)
				drawQuadTex(c, t, xTextureOffset, yTextureOffset, t.getWidth(), t.getHeight());
			else
				drawQuadTexFlipHorizontal(c, t, xTextureOffset, yTextureOffset, t.getWidth(), t.getHeight());
		}
	}

	public void drawHitbox(Camera c) {
		drawLineLoop(c, x, y, width, height);
	}

	public void damage(int damage) {
		if (damageInvul == false) {
			health -= damage;
			if (health <= 0) {
				// TODO ded
			}

			state.s.enterNewState(this, States.Damaged);
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getxSpeed() {
		return xSpeed;
	}

	public float getySpeed() {
		return ySpeed;
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

	public float getxTextureOffset() {
		return xTextureOffset;
	}

	public float getyTextureOffset() {
		return yTextureOffset;
	}

	private void setTextureOffsets() {
		Texture t = state.sprite.getCurrentTexture();
		xTextureOffset = x - ((t.getWidth() - width) / 2);
		yTextureOffset = y - ((t.getHeight() - height)) + 18;

	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public TileGrid getGrid() {
		return grid;
	}

	@Override
	public int getyCoord() {
		return Math.floorDiv((int) y, tileSize);
	}

	@Override
	public int getxCoord() {
		return Math.floorDiv((int) x, tileSize);
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
