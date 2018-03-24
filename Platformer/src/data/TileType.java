package data;

import static helpers.Graphics.*;

public enum TileType {
	/*@formatter:off*/
	Background("blank", false, "000", 0, 0), Block("block", true, "001", 0, 0),
	Slope0x15("slope0x15", true, "002", 0, 15), Slope16x31("slope16x31", true, "003", 16, 31),
	Slope32x47("slope32x47", true, "004", 32, 47), Slope48x63("slope48x63", true, "005", 48, 63),
	Slope15x0("slope15x0", true, "006", 15, 0), Slope31x16("slope31x16", true, "007", 31, 16),
	Slope47x32("slope47x32", true, "008", 47, 32), Slope63x48("slope63x48", true, "009", 63, 48);
	/*@formatter:on*/
	Texture texture;
	boolean solid; // solid tiles cannot be passed through by entities
	String id;
	int yFloorL, yFloorR; // distance (in pixels) of the first solid pixel counted from the top of the
							// tile on the left and right edge respectively

	TileType(String textureName, boolean solid, String id, int yFloorL, int yFloorR) {
		this.texture = new Texture(textureName, (int) tileSize, (int) tileSize);
		this.solid = solid;
		this.id = id;
		this.yFloorL = yFloorL;
		this.yFloorR = yFloorR;

	}

	public String getID() {
		return id;
	}

	public int getyFloorL() {
		return yFloorL;
	}

	public int getyFloorR() {
		return yFloorR;
	}

}
