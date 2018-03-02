package helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import data.TileGrid;
import data.TileType;

public class LevelManager {

	private static String mapDirectory = "maps/";
	private final static int TILE_ID_SIZE = 3; // Number of digits in the tile id

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
				mapData += grid.getTile(i, j).getType().getID(); // write the tile type ID into the file
			}
		}

		File file = new File(mapDirectory + mapName);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(mapData);
			bw.close();
		} catch (IOException e) {
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
					// read the tile type ID
					grid.setTile(i, j,
							getTileType(data.substring(i * grid.getMapHeight() * TILE_ID_SIZE + j * TILE_ID_SIZE,
									i * grid.getMapHeight() * TILE_ID_SIZE + j * TILE_ID_SIZE + TILE_ID_SIZE)));
				}
			}

			br.close();

			return grid;

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Failed to load map");
		return new TileGrid(20, 20);
	}

	private static TileType getTileType(String ID) {
		for (TileType t : TileType.values()) {
			if (ID.compareTo(t.getID()) == 0)
				return t;
		}

		System.out.println("Invalid tile type.");

		return TileType.Background;
	}
}
