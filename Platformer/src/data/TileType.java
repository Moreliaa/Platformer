package data;

import static helpers.Graphics.tileSize;

public enum TileType {

	Block("block", true, "001"), Background("bg", false, "000");

	Texture texture;
	boolean solid; // solid tiles cannot be passed through by entities
	String id;

	TileType(String textureName, boolean solid, String id) {
		this.texture = new Texture(textureName, (int) tileSize, (int) tileSize);
		this.solid = solid;
		this.id = id;
	}

	public String getID() {
		return id;
	}

}
