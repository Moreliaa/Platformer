package helpers;

import static helpers.Clock.delta;
import static helpers.Graphics.tileSize;

import java.util.ArrayList;

import data.Entity;
import data.Tile;

public class Physics {

	private static float maxJumpHeight = 4.5f * tileSize;
	private static int timeToJumpApex = 25;
	private static float gravity = 2 * maxJumpHeight / (float) (Math.pow(timeToJumpApex, 2));
	private static float initialJumpVelocity = (-1) * (float) Math.sqrt(2 * gravity * maxJumpHeight);
	private static float minJumpHeight = 1.5f * tileSize;
	private static float initialSmallJumpVelocity = setInitialSmallJumpVelocity();
	private static float maxFallSpeed = 100;

	private static float boostDistance = 5f * tileSize;
	private static int boostDuration = 10;
	private static float boostSpeed = boostDistance / boostDuration;

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
