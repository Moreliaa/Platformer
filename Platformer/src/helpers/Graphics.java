package helpers;

import static org.lwjgl.opengl.GL11.*;

import java.nio.*;

import org.lwjgl.stb.*;

import data.*;

public class Graphics {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int tileSize = 64;
	public static final String resPath = "src/res/"; // texture folder

	public static void drawText(float x, float y, String text, float r, float g, float b) {
		// TODO fix
		ByteBuffer buffer = ByteBuffer.allocateDirect(99999);
		int num_quads;

		num_quads = STBEasyFont.stb_easy_font_print(x, y, text, null, buffer);

		glColor3f(r, g, b);
		glEnableClientState(GL_VERTEX_ARRAY);
		glVertexPointer(2, GL_FLOAT, 16, buffer);
		glDrawArrays(GL_QUADS, 0, num_quads * 4);
		glDisableClientState(GL_VERTEX_ARRAY);
		glColor3f(1.0f, 1.0f, 1.0f);
	}

	public static void drawLineLoop(Camera c, float x, float y, float width, float height) {
		float xNew = x - c.getX();
		float yNew = y - c.getY();

		drawLineLoop(xNew, yNew, width, height);
	}

	public static void drawLineLoop(float x, float y, float width, float height) {
		glDisable(GL_TEXTURE_2D);

		glLineWidth(2.0f);

		glBegin(GL_LINE_LOOP);
		glColor3f(1.0f, 0.0f, 0.0f);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, height + y);
		glVertex2f(x, height + y);
		glColor3f(1.0f, 1.0f, 1.0f);
		glEnd();

		glEnable(GL_TEXTURE_2D);
	}

	public static void drawQuad(float x, float y, int width, int height) {

		glDisable(GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		glColor3f(0.2f, 0.2f, 0.2f);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(1, 0);
		glVertex2f(x + width, y);
		glTexCoord2f(1, 1);
		glVertex2f(x + width, height + y);
		glTexCoord2f(0, 1);
		glVertex2f(x, height + y);
		glColor3f(1.0f, 1.0f, 1.0f);
		glEnd();

		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Draws a texture to the screen.
	 */
	public static void drawQuadTex(Camera c, Texture tex, float x, float y, int width, int height) {
		float xNew = x - c.getX();
		float yNew = y - c.getY();

		drawQuadTex(tex, xNew, yNew, width, height);
	}

	public static void drawQuadTex(Texture tex, float x, float y, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, tex.getId());
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, tex.getBuffer());

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(1, 0);
		glVertex2f(x + width, y);
		glTexCoord2f(1, 1);
		glVertex2f(x + width, height + y);
		glTexCoord2f(0, 1);
		glVertex2f(x, height + y);
		glEnd();
	}

	/**
	 * Draws a horizontally flipped texture to the screen.
	 */
	public static void drawQuadTexFlipHorizontal(Camera c, Texture tex, float x, float y, int width, int height) {
		float xNew = x - c.getX();
		float yNew = y - c.getY();

		glBindTexture(GL_TEXTURE_2D, tex.getId());
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, tex.getBuffer());

		glBegin(GL_QUADS);
		glTexCoord2f(1, 0);
		glVertex2f(xNew, yNew);
		glTexCoord2f(0, 0);
		glVertex2f(xNew + width, yNew);
		glTexCoord2f(0, 1);
		glVertex2f(xNew + width, height + yNew);
		glTexCoord2f(1, 1);
		glVertex2f(xNew, height + yNew);
		glEnd();
	}

	/**
	 * Fills the screen with a texture, starting from the given coordinates
	 */
	public static void fill(Texture tex, float x, float y, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, tex.getId());
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, tex.getBuffer());

		int xRepeat = Math.floorDiv(WIDTH, width) + 1;
		int yRepeat = Math.floorDiv(HEIGHT, height) + 1;

		for (int i = 0; i < xRepeat; i++) {
			for (int j = 0; j < yRepeat; j++) {
				glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex2f(x + width * i, y + height * j);
				glTexCoord2f(1, 0);
				glVertex2f(x + width * i + width, y + height * j);
				glTexCoord2f(1, 1);
				glVertex2f(x + width * i + width, y + height * j + height);
				glTexCoord2f(0, 1);
				glVertex2f(x + width * i, y + height * j + height);
				glEnd();
			}
		}
	}

	/*
	 * public static void drawQuadTexRot(Texture tex, float x, float y, float width,
	 * float height, float angle) { //TODO fix this tex.bind(); glTranslatef(x +
	 * (width / 2), y + (height / 2), 0); glRotatef(angle, 0, 0, 1);
	 * glTranslatef(-width / 2, -height / 2, 0); glBegin(GL_QUADS); glTexCoord2f(0,
	 * 0); glVertex2f(0, 0); glTexCoord2f(1, 0); glVertex2f(width, 0);
	 * glTexCoord2f(1, 1); glVertex2f(width, height); glTexCoord2f(0, 1);
	 * glVertex2f(0, height); glEnd(); glLoadIdentity(); }
	 */

}
