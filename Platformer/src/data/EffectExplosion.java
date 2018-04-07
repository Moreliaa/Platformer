package data;

import static helpers.Graphics.*;

public class EffectExplosion implements Effect {
	private float x, y;
	private Animation texture;
	private int currentFrame, totalFrames;

	public EffectExplosion(Entity e) {
		Texture[] tex = { new Texture("explosion_1x1", 64, 64), new Texture("explosion_1x2", 64, 64),
				new Texture("explosion_1x3", 64, 64), new Texture("explosion_1x4", 64, 64),
				new Texture("explosion_1x5", 64, 64), new Texture("explosion_2x1", 64, 64),
				new Texture("explosion_2x2", 64, 64), new Texture("explosion_2x3", 64, 64),
				new Texture("explosion_2x4", 64, 64), new Texture("explosion_2x5", 64, 64),
				new Texture("explosion_3x1", 64, 64), new Texture("explosion_3x2", 64, 64),
				new Texture("explosion_3x3", 64, 64), new Texture("explosion_3x4", 64, 64),
				new Texture("explosion_3x5", 64, 64), new Texture("explosion_4x1", 64, 64),
				new Texture("explosion_4x2", 64, 64), new Texture("explosion_4x3", 64, 64),
				new Texture("explosion_4x4", 64, 64), new Texture("explosion_4x5", 64, 64),
				new Texture("explosion_5x1", 64, 64), new Texture("explosion_5x2", 64, 64),
				new Texture("explosion_5x3", 64, 64), new Texture("explosion_5x4", 64, 64),
				new Texture("explosion_5x5", 64, 64) };
		totalFrames = tex.length;
		currentFrame = 0;
		this.texture = new Animation(tex, 1);
		this.x = (e.getX() + (e.getWidth()) / 2) - (texture.getCurrentTexture().getWidth() / 2);
		this.y = (e.getY() + (e.getHeight()) / 2) - (texture.getCurrentTexture().getHeight() / 2);
	}

	@Override
	public void draw(Camera c) {
		Texture t = texture.getCurrentTexture();

		drawQuadTex(c, t, x, y, t.getWidth(), t.getHeight());

		texture.animate();
		currentFrame++;

	}

	@Override
	public boolean effectFinished() {
		return (currentFrame >= totalFrames);
	}

}
