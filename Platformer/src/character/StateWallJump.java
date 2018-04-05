package character;

//TODO not in use atm
public class StateWallJump extends CharacterState {

	private int iterator, duration = 10;

	@Override
	public void enter(Character c) {
		iterator = 0;
	}

	@Override
	public void enterNewState(Character c, States s) {
		if (iterator >= duration) {
			c.state = s;
			c.state.s.enter(c);
		}
	}

	@Override
	public void handleInput(Character c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Character c) {
		iterator++;
		super.updateCharacterTimers(c);
		enterNewState(c, States.Jumping);

	}

}
