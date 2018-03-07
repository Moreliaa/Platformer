package data;

import static helpers.Graphics.tileSize;

public enum TileType {
	/*@formatter:off*/
	Background("bg", false, "000", 0, 0), Block("block", true, "001", 0, 0),
	Slope0x15("slope0x15", true, "002", 0, 15), Slope16x31("slope16x31", true, "003", 16, 31),
	Slope32x47("slope32x47", true, "004", 32, 47), Slope48x63("slope48x63", true, "005", 48, 63);
	/*@formatter:on*/
	Texture texture;
	boolean solid; // solid tiles cannot be passed through by entities
	String id;
	int yCoordSlopeL, yCoordSlopeR;

	TileType(String textureName, boolean solid, String id, int yCoordSlopeL, int yCoordSlopeR) {
		this.texture = new Texture(textureName, (int) tileSize, (int) tileSize);
		this.solid = solid;
		this.id = id;
		this.yCoordSlopeL = yCoordSlopeL;
		this.yCoordSlopeR = yCoordSlopeR;

	}

	public String getID() {
		return id;
	}

	public int getyCoordSlopeL() {
		return yCoordSlopeL;
	}

	public int getyCoordSlopeR() {
		return yCoordSlopeR;
	}

}
