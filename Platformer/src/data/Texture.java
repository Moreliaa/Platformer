package data;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static helpers.Graphics.*;

public class Texture {
	private int id, width, height;
	private ByteBuffer buffer;

	public Texture(String name, int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = loadTexture(name);
		id = glGenTextures();
	}

	private ByteBuffer loadTexture(String name) {
		ByteBuffer tex = null;
		try {
			InputStream in = new FileInputStream(resPath + name + ".png");
			PNGDecoder decoder = new PNGDecoder(in);

			tex = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(tex, decoder.getWidth() * 4, Format.RGBA);
			tex.flip();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tex;
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

}
