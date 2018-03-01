package data;

public class Animation {
	private Texture[] textures;
	private int framesPerTexture;
	private int counter;

	private int index;

	public Animation(Texture[] textures, int framesPerTexture) {
		this.textures = textures;
		this.framesPerTexture = framesPerTexture;
		this.index = 0;
	}

	public Texture animate() {
		Texture t = textures[index];

		counter++;
		if (counter >= framesPerTexture) {
			counter = 0;

			index++;
			if (index >= textures.length)
				index = 0;
		}

		return t;
	}

	public Texture getCurrentTexture() {
		return textures[index];
	}

}
