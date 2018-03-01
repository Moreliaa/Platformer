package data;

import static helpers.Graphics.*;

public enum TileType {

	Block("block", true), Background("bg", false), NULL("bg", false);

	Texture texture;
	boolean solid; // solid tiles cannot be passed through by entities

	TileType(String textureName, boolean solid) {
		this.texture = new Texture(textureName, (int) tileSize, (int) tileSize);
		this.solid = solid;
	}

}
