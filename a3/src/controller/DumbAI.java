package controller;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Game;
import model.Location;
import model.NotImplementedException;
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
		
		// Find the blank space with the smallest available column number with the smallest available row number
		for (Location loc : Board.LOCATIONS) {
			if (g.getBoard().get(loc) == null) {
				dumbMove = loc;
				return dumbMove;
			}
		}
		//Just to make sure the function always returns type Location
		return dumbMove;
	}

}
