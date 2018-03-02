package data;

import static helpers.Graphics.HEIGHT;
import static helpers.Graphics.WIDTH;
import static helpers.Graphics.tileSize;

public class TileGrid {
	private Tile[][] map;
	private int mapWidth;
	private int mapHeight;

	public TileGrid(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		map = new Tile[mapWidth][mapHeight];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new Tile(i * tileSize, j * tileSize, TileType.Background);
			}
		}
	}

	public TileGrid(int[][] newMap) {
		this.mapWidth = newMap[0].length;
		this.mapHeight = newMap.length;
		map = new Tile[mapWidth][mapHeight];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				switch (newMap[j][i]) {
				case 0:
					this.setTile(i, j, TileType.Background);
					break;
				case 1:
					this.setTile(i, j, TileType.Block);
					break;
				default:
					this.setTile(i, j, TileType.Background);

				}
			}
		}
	}

	public void setTile(int xCoord, int yCoord, TileType type) {
		if (xCoord >= 0 && xCoord < mapWidth && yCoord >= 0 && yCoord < mapHeight)
			map[xCoord][yCoord] = new Tile(xCoord * tileSize, yCoord * tileSize, type);
		else
			System.out.println("Failed setting tile.");
	}

	public Tile getTile(int xCoord, int yCoord) {
		if (xCoord >= 0 && xCoord < mapWidth && yCoord >= 0 && yCoord < mapHeight) {
			return map[xCoord][yCoord];
		} else {
			return new Tile(0, 0, TileType.Background);
		}

	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public Tile[][] getMap() {
		return map;
	}

	public void draw(Camera c) {
		// determine the screen boundaries in tile coordinates
		int xCoord1 = (int) Math.floor(c.getX() / tileSize);
		int yCoord1 = (int) Math.floor(c.getY() / tileSize);
		int xCoord2 = xCoord1 + (int) Math.floor(WIDTH / tileSize) + 1;
		int yCoord2 = yCoord1 + (int) Math.floor(HEIGHT / tileSize) + 1;

		for (int i = 0; i < map.length; i++) {
			if (i >= xCoord1 && i <= xCoord2) {
				for (int j = 0; j < map[i].length; j++) {
					if (j >= yCoord1 && j <= yCoord2)
						map[i][j].draw(c);
				}
			}
		}
	}
}
