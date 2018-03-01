package data;

import static helpers.Graphics.*;

public class Tile {

	private float x, y;
	private TileType type;

	public Tile(float x, float y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void draw(Camera c) {
		drawQuadTex(c, type.texture, x, y, tileSize, tileSize);
	}

	public boolean isSolid() {
		return type.solid;
	}

	public float getX() {
		return x;
	}

	public int getXCoord() {
		return (int) (x / tileSize);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public int getYCoord() {
		return (int) (y / tileSize);
	}

	public void setY(float y) {
		this.y = y;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}
}
