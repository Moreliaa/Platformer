package helpers;

import static helpers.Clock.*;
import static helpers.Graphics.*;

import java.util.*;

import data.*;

public class Physics {

	private static float maxJumpHeight = 4.5f * tileSize;
	private static int timeToJumpApex = 25;
	private static float gravity = 2 * maxJumpHeight / (float) (Math.pow(timeToJumpApex, 2));
	private static float initialJumpVelocity = (-1) * (float) Math.sqrt(2 * gravity * maxJumpHeight);
	private static float minJumpHeight = 1.5f * tileSize;
	private static float initialSmallJumpVelocity = setInitialSmallJumpVelocity();
	private static float maxFallSpeed = 20;

	private static float boostDistance = 5f * tileSize;
	private static int boostDuration = 10;
	private static float boostSpeed = boostDistance / boostDuration;

	public static float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * Step the x coordinate for movement. Returns true if there was a collision,
	 * otherwise false.
	 */
	public static boolean stepX(Entity e) {
		float xNew = e.getX() + e.getxSpeed() * delta() * 60;
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		// Get the x coordinate of the forward-facing edge in the direction of movement
		int xCoordEdge;
		if (e.getxSpeed() >= 0) {
			xCoordEdge = e.getxCoordR() + 1;

			if (xCoordEdge > e.getGrid().getMapWidth()) {// OOB
				e.setX(xNew);
				return false;
			}
		} else {
			xCoordEdge = e.getxCoord() - 1;
			if (xCoordEdge < 0) {// OOB
				e.setX(xNew);
				return false;
			}
		}

		// Get the tiles the entity may intersect with
		for (int yCoord = e.getyCoord(); yCoord <= e.getyCoordB(); yCoord++) {
			tiles.add(e.getGrid().getTile(xCoordEdge, yCoord));
		}

		boolean collision = false;
		float yThresholdBlock = 0;
		float yThresholdSlope = 32; // Max height difference that can be stepped over
		// Look for the closest obstacle
		for (Tile t : tiles) {

			if (e.getxSpeed() >= 0 && t.getX() - e.getWidth() < xNew && t.isSolid()) {

				if (t.getType().getyFloorL() == 0 && t.getType().getyFloorR() == 0
						&& e.getY() + e.getHeight() > t.getY() + yThresholdBlock) {// block
					xNew = t.getX() - e.getWidth();
					collision = true;
				} else if (e.getyCoordB() > t.getYCoord()
						|| e.getY() + e.getHeight() > t.getY() + t.getType().getyFloorL() + yThresholdSlope) {// slope
					xNew = t.getX() - e.getWidth();
					collision = true;
				}
			} else if (e.getxSpeed() < 0 && t.getX() + tileSize > xNew && t.isSolid())
				if (t.getType().getyFloorL() == 0 && t.getType().getyFloorR() == 0
						&& e.getY() + e.getHeight() > t.getY() + yThresholdBlock) {// block
					xNew = t.getX() + tileSize;
					collision = true;
				} else if (e.getyCoordB() > t.getYCoord()
						|| e.getY() + e.getHeight() > t.getY() + t.getType().getyFloorR() + yThresholdSlope) {// slope
					xNew = t.getX() + tileSize;
					collision = true;
				}

		}

		e.setX(xNew);
		return collision;
	}

	public static boolean stepY(Entity e) {
		float yNew = e.getY() + e.getySpeed() * delta() * 60;
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		// Get the y coordinate of the forward-facing edge in the direction of movement
		int yCoordEdge;
		if (e.getySpeed() >= 0) {
			yCoordEdge = e.getyCoordB() + 1;

			if (yCoordEdge > e.getGrid().getMapHeight()) {// OOB
				e.setY(yNew);
				return false;
			}
		} else {
			yCoordEdge = e.getyCoord() - 1;
			if (yCoordEdge < 0) {// OOB
				e.setY(yNew);
				return false;
			}
		}

		// Check if character is standing on a slope tile
		if (e.getySpeed() >= 0) {
			for (int xCoord = e.getxCoord(); xCoord <= e.getxCoordR(); xCoord++) {
				if (e.getGrid().getTile(xCoord, e.getyCoordB()).getType().getyFloorL() != 0
						|| e.getGrid().getTile(xCoord, e.getyCoordB()).getType().getyFloorL() != 0)
					tiles.add(e.getGrid().getTile(xCoord, e.getyCoordB()));
			}
		}

		// Get the tiles the entity may intersect with
		if (tiles.isEmpty()) {
			for (int xCoord = e.getxCoord(); xCoord <= e.getxCoordR(); xCoord++) {
				tiles.add(e.getGrid().getTile(xCoord, yCoordEdge));
			}
		}

		boolean collision = false;

		// Look for the closest obstacle
		for (Tile t : tiles) {
			if (e.getySpeed() >= 0) { // falling
				if (t.getType().getyFloorL() == 0 && t.getType().getyFloorR() == 0 && yNew + e.getHeight() > t.getY()
						&& t.isSolid()) {// block
					yNew = t.getY() - e.getHeight();
					collision = true;
				} else if (t.getType().getyFloorL() != 0 || t.getType().getyFloorR() != 0) { // slope
					float xFloor;
					// normalized x position of the entity's intersecting corner with the
					// current tile. A value of 0 represents the left edge of the tile, a value of 1
					// the right edge.
					float yFloorL = t.getType().getyFloorL();
					float yFloorR = t.getType().getyFloorR();

					// set the local x coordinate
					if (e.getX() >= t.getX()) { // bottom-left corner of char intersects
						if (yFloorL < yFloorR)
							xFloor = (float) Math.floorMod((int) e.getX(), tileSize) / tileSize;
						else
							xFloor = 1;
					} else { // bottom-right corner of char intersects
						if (yFloorL < yFloorR)
							xFloor = 0;
						else if (e.getX() + e.getWidth() <= t.getX() + tileSize)
							xFloor = (float) Math.floorMod((int) e.getX() + e.getWidth(), tileSize) / tileSize;
						else
							xFloor = 1;
					}

					// calculate the y position of the character at the intersecting point
					float yNewFloor = t.getY() - e.getHeight() + yFloorL + (yFloorR - yFloorL) * xFloor;
					float yThreshold = 3;
					if (yNew > yNewFloor - yThreshold) {
						yNew = yNewFloor;
						collision = true;
					}
				}
			} else if (yNew < t.getY() + tileSize && t.isSolid()) { // rising
				yNew = t.getY() + tileSize;
				collision = true;

			}

		}

		e.setY(yNew);
		return collision;

	}

	/**
	 * Returns true if the AABB of the given coordinates intersects with the given
	 * tile, otherwise false.
	 */
	public static boolean checkCollision(float x1, float y1, int width1, int height1, Tile t) {
		if (x1 + width1 > t.getX() && x1 < t.getX() + tileSize && y1 < t.getY() + tileSize && y1 + height1 > t.getY())
			return true;
		return false;
	}

	/**
	 * Returns true if the two AABBs of the given coordinates intersect with each
	 * other, otherwise false.
	 */
	public static boolean checkCollision(float x1, float y1, int width1, int height1, float x2, float y2, int width2,
			int height2) {
		if (x1 + width1 > x2 && x1 < x2 + width2 && y1 + height1 > y2 && y1 < y2 + height2)
			return true;
		return false;
	}

	public static float getGravity() {
		return gravity;
	}

	public static float getMaxFallSpeed() {
		return maxFallSpeed;
	}

	public static float getInitialJumpVelocity() {
		return initialJumpVelocity;
	}

	public static float getInitialSmallJumpVelocity() {
		return initialSmallJumpVelocity;
	}

	private static float setInitialSmallJumpVelocity() {
		return (-1) * (float) Math
				.sqrt(Math.pow(initialJumpVelocity, 2) + (-1) * 2 * gravity * (maxJumpHeight - minJumpHeight));
	}

	public static float getBoostDistance() {
		return boostDistance;
	}

	public static float getBoostSpeed() {
		return boostSpeed;
	}

	public static int getBoostDuration() {
		return boostDuration;
	}

}
