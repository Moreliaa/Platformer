package helpers;

import data.*;

public class StateManager {

	public static enum GameState {
		GAME, EDITOR, MAINMENU
	}

	private static GameState gameState = GameState.MAINMENU;

	public static Game game;
	public static Editor editor;
	public static MainMenu mainmenu;

	public static void update() {
		switch (gameState) {
		case GAME:
			if (game == null)
				game = new Game("map");
			game.update();
			break;
		case EDITOR:
			if (editor == null)
				editor = new Editor("map");
			editor.update();
			break;
		case MAINMENU:
			if (mainmenu == null)
				mainmenu = new MainMenu();
			mainmenu.update();
			break;
		}
	}

	public static GameState getGameState() {
		return gameState;
	}

	public static void setGameState(GameState gameState) {
		StateManager.gameState = gameState;
	}

}
