package helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import data.Tile;
import data.TileGrid;
import data.TileType;

public class LevelManager {

	private static String mapDirectory = "maps/";

	/**
	 * Saves a map into the designated map directory. The format is
	 * WIDTH-HEIGHT-MAPDATA where the map data is comprised of single-digit integers
	 * identifying each tile.
	 */
	public static void saveMap(String mapName, TileGrid grid) {
		String mapData = "";

		mapData += grid.getMapWidth() + "-";
		mapData += grid.getMapHeight() + "-";

		for (int i = 0; i < grid.getMapWidth(); i++) {
			for (int j = 0; j < grid.getMapHeight(); j++) {
				mapData += getTileID(grid.getTile(i, j));
			}
		}

		File file = new File(mapDirectory + mapName);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(mapData);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static TileGrid loadMap(String mapName) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(mapDirectory + mapName));
			String data = br.readLine();
			TileGrid grid;

			// Parse the map dimensions
			int mapWidth, mapHeight, iteratorWidth = 0, iteratorHeight = 0;

			for (int i = 0; data.substring(i, i + 1).compareTo("-") != 0; i++) {
				iteratorWidth++;
			}

			mapWidth = Integer.parseInt(data.substring(0, iteratorWidth));
			iteratorWidth++;
			iteratorHeight = iteratorWidth;

			for (int i = iteratorWidth; data.substring(i, i + 1).compareTo("-") != 0; i++) {
				iteratorHeight++;
			}

			mapHeight = Integer.parseInt(data.substring(iteratorWidth, iteratorHeight));
			iteratorHeight++;

			// Remove the map dimensions from the tile data
			data = data.substring(iteratorHeight, data.length());

			grid = new TileGrid(mapWidth, mapHeight);

			// Read the tile data
			for (int i = 0; i < grid.getMapWidth(); i++) {
				for (int j = 0; j < grid.getMapHeight(); j++) {
					grid.setTile(i, j,
							getTileType(data.substring(i * grid.getMapHeight() + j, i * grid.getMapHeight() + j + 1)));
				}
			}

			br.close();

			return grid;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Failed to load map");
		return new TileGrid(20, 20);
	}

	private static TileType getTileType(String ID) {
		switch (ID) {
		case "0":
			return TileType.Background;
		case "1":
			return TileType.Block;
		default:
			return TileType.Background;
		}
	}

	private static String getTileID(Tile t) {
		String ID = "E";

		switch (t.getType()) {
		case Background:
			ID = "0";
			break;
		case Block:
			ID = "1";
			break;
		default:
		}

		return ID;
	}
}
