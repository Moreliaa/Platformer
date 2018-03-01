package data;

import static helpers.Graphics.HEIGHT;
import static helpers.StateManager.setGameState;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import helpers.MouseHandler;
import helpers.StateManager.GameState;

public class MainMenu {

	MenuButton game, editor, quit;

	public MainMenu() {
		game = new MenuButton(new Texture("butGame", 250, 100), (float) (HEIGHT / 4));
		editor = new MenuButton(new Texture("butEditor", 250, 100), game.getY() + game.getHeight() + 15);
		quit = new MenuButton(new Texture("butQuit", 250, 100), editor.getY() + editor.getHeight() + 15);
	}

	public void update() {
		draw();
		handleInput();

	}

	private void draw() {
		game.draw();
		editor.draw();
		quit.draw();

	}

	private void handleInput() {
		if (MouseHandler.isButtonDown(0)) {

			float xPos = MouseHandler.getxPos();
			float yPos = MouseHandler.getyPos();

			if (game.isButtonClicked(xPos, yPos))
				setGameState(GameState.GAME);
			if (editor.isButtonClicked(xPos, yPos))
				setGameState(GameState.EDITOR);
			if (quit.isButtonClicked(xPos, yPos))
				glfwSetWindowShouldClose(_Main.window, true);
		}
	}

}
