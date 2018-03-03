package data;

import static helpers.Graphics.drawQuadTex;
import static helpers.Graphics.drawQuadTexFlipHorizontal;

import character.Character;

public class EffectAfterimage implements Effect {
	private Character character;
	private Texture texture;
	private int numberOfImages, spawnInterval, fadeTime, currentFrame, totalFrames;
	private float[] xPos, yPos;

	/**
	 * 
	 * @param character
	 * @param texture
	 * @param numberOfImages
	 *            The number of afterimages to be created.
	 * @param spawnInterval
	 *            Frames between spawning each afterimage.
	 * @param fadeTime
	 *            Delay until each afterimage disappears.
	 */
	public EffectAfterimage(Character character, Texture texture, int numberOfImages, int spawnInterval, int fadeTime) {
		this.character = character;
		this.texture = texture;
		this.numberOfImages = numberOfImages;
		this.spawnInterval = spawnInterval;
		this.fadeTime = fadeTime;
		this.currentFrame = 0;
		this.totalFrames = (numberOfImages - 1) * spawnInterval + fadeTime + 1;
		this.xPos = new float[numberOfImages];
		this.yPos = new float[numberOfImages];
		this.xPos[0] = character.getxTextureOffset();
		this.yPos[0] = character.getyTextureOffset();
	}

	public void draw(Camera c) {
		// update draw coordinates
		for (int i = 0; i < numberOfImages; i++) {
			if (currentFrame == spawnInterval * i) {
				xPos[i] = character.getxTextureOffset();
				yPos[i] = character.getyTextureOffset();
				break;
			}
		}

		// draw effects
		for (int i = 0; i < numberOfImages; i++) {
			if (currentFrame >= spawnInterval * i && currentFrame <= spawnInterval * i + fadeTime) {
				if (character.isFacingRight())
					drawQuadTex(c, texture, xPos[i], yPos[i], texture.getWidth(), texture.getHeight());
				else
					drawQuadTexFlipHorizontal(c, texture, xPos[i], yPos[i], texture.getWidth(), texture.getHeight());
			}
		}
		// advance to the next frame
		currentFrame++;
	}

	public boolean effectFinished() {
		if (currentFrame == totalFrames)
			return true;
		else
			return false;
	}

}
