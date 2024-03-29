package controller;


import model.Board;
import model.Game;
import model.Location;
import model.Player;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	public DumbAI(Player me) {
		super(me);
	}

	protected @Override Location nextMove(Game g) {
		// Note: Calling delay here will make the CLUI work a little more
		// nicely when competing different AIs against each other.
		
		// Asserting g is not null
		assert g != null;
		// Initialize the Dumb AI's move
		Location dumbMove = new Location(0, 0);
		// boolean object to ensure that there is a remaining space
		boolean remaining_space = false;
		// Find the blank space with the smallest available column number with the smallest available row number
		for (Location loc : Board.LOCATIONS) {
			if (g.getBoard().get(loc) == null) {
				remaining_space = true;
				dumbMove = loc;
				return dumbMove;
			}
		}
		
		// If there are no remaining spaces, return null
		if (!remaining_space) {
			return null;
		}
		
		//Return Location dumbMove if there is a remaining space.
		return dumbMove;
	}

}
