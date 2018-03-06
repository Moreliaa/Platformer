package character;

import data.Animation;
import data.Texture;

public enum States {
	Standing(new StateStanding(), new Texture[] { new Texture("mm_idle", 158, 158) }),

	// this state is identical to the standing state except for the texture
	Walking(new StateStanding(),
			new Texture[] { new Texture("mm001", 158, 158), new Texture("mm002", 158, 158),
					new Texture("mm003", 158, 158), new Texture("mm004", 158, 158), new Texture("mm005", 158, 158),
					new Texture("mm006", 158, 158), new Texture("mm007", 158, 158), new Texture("mm008", 158, 158),
					new Texture("mm009", 158, 158), new Texture("mm010", 158, 158) }),

	Jumping(new StateJumping(), new Texture[] { new Texture("mm001", 158, 158) }),

	Boosting(new StateBoosting(), new Texture[] { new Texture("mm001", 158, 158) }),

	WallCling(new StateWallCling(), new Texture[] { new Texture("mm007", 158, 158) }),

	WallJump(new StateWallJump(), new Texture[] { new Texture("mm001", 158, 158) });

	CharacterState s;
	Animation sprite;

	States(CharacterState s, Texture[] t) {
		this.s = s;
		this.sprite = new Animation(t, 3);
	}
}
