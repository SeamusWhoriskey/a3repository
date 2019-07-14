package controller;

//import org.eclipse.jdt.annotation.NonNull;

import model.Board;
import model.Board.State;
import model.Game;
import model.Location;
import model.Player;


/**
 * A MinMaxAI is a controller that uses the minimax algorithm to select the next
 * move.  The minimax algorithm searches for the best possible next move, under
 * the assumption that your opponent will also always select the best possible
 * move.
 *
 * <p>The minimax algorithm assigns a score to each possible game configuration
 * g.  The score is assigned recursively as follows: if the game g is over and
 * the player has won, then the score is infinity.  If the game g is over and
 * the player has lost, then the score is negative infinity.  If the game is a
 * draw, then the score is 0.
 * 
 * <p>If the game is not over, then there are many possible moves that could be
 * made; each of these leads to a new game configuration g'.  We can
 * recursively find the score for each of these configurations.
 * 
 * <p>If it is the player's turn, then they will choose the action that
 * maximizes their score, so the score for g is the maximum of all the scores
 * of the g's.  However, if it is the opponent's turn, then the opponent will
 * try to minimize the score for the player, so the score for g is the
 * <em>minimum</em> of all of the scores of the g'.
 * 
 * <p>You can think of the game as defining a tree, where each node in the tree
 * represents a game configuration, and the children of g are all of the g'
 * reachable from g by taking a turn.  The minimax algorithm is then a
 * particular traversal of this tree.
 * 
 * <p>In practice, game trees can become very large, so we apply a few
 * strategies to narrow the set of paths that we search.  First, we can decide
 * to only consider certain kinds of moves.  For five-in-a-row, there are
 * typically at least 70 moves available at each step; but it's (usually) not
 * sensible to go on the opposite side of the board from where all of the other
 * pieces are; by restricting our search to only part of the board, we can
 * reduce the space considerably.
 * 
 * <p>A second strategy is that we can look only a few moves ahead instead of
 * planning all the way to the end of the game.  This requires us to be able to
 * estimate how "good" a given board looks for a player.
 * 
 * <p>This class implements the minimax algorithm with support for these two
 * strategies for reducing the search space.  The abstract method {@link
 * #moves(Board)} is used to list all of the moves that the AI is willing to
 * consider, while the abstract method {@link #estimate(Board)} returns
 * the estimation of how good the board is for the given player.
 */
public abstract class MinMaxAI extends Controller {

	/**
	 * Return an estimate of how good the given board is for me.
	 * A result of infinity means I have won.  A result of negative infinity
	 * means that I have lost.
	 */
	protected abstract int estimate(Board b);
	
	/**
	 * Return the set of moves that the AI will consider when planning ahead.
	 * Must contain at least one move if there are any valid moves to make.
	 */
	protected abstract Iterable<Location> moves(Board b);
	
	protected int d;
	
	/**
	 * Create an AI that will recursively search for the next move using the
	 * minimax algorithm.  When searching for a move, the algorithm will look
	 * depth moves into the future.
	 *
	 * <p>choosing a higher value for depth makes the AI smarter, but requires
	 * more time to select moves.
	 */
	protected MinMaxAI(Player me, int depth) {
		super(me);
		// Set the int d to hold the desired depth 
		this.d = depth;
		
	}

	/**
	 * Return the move that maximizes the score according to the minimax
	 * algorithm described above.
	 */
	protected @Override Location nextMove(Game g) {
		// TODO Auto-generated method stub
		String loc_string = nextMoveHelper(g.getBoard(), me, d)[0];
		int x_loc = Integer.parseInt(Character.toString(loc_string.charAt(1)));
		int y_loc = Integer.parseInt(Character.toString(loc_string.charAt(3)));
		Location loc_out = new Location(x_loc, y_loc);
		return loc_out;
	}
	
	

	/** Helper function for nextMove 
	 *  Returns the optimal move 
	 */
	protected String[] nextMoveHelper(Board b, Player p, int depth) {
		
		// best_score will keep track of the best possible score for each player
		// Initialize at worst possible value for Player p, being inf for opponent
		double best_score = Double.POSITIVE_INFINITY;
		// and -inf for me
		if (p == me) {
			best_score *= -1;
		}
		
		// The String[] optimal contains 
		// [0]: Location of best move
		// [1]: Score of best move
		// Initialized as N/A and worst possible score for Player
		String[] optimal = {"N/A", String.valueOf(best_score)};
		
		// If the depth has been reached, or if the game is over,
		if (depth == 0 || b.getState() != State.NOT_OVER) {
			// Set the score of optimal to be the current score of the board.
			double score = estimate(b);
			optimal[1] = String.valueOf(score);
			return optimal;
		}
		
		// Get the spaces that the AI will evaluate
		Iterable <Location> spaces = moves(b);
		for (Location move : spaces) {
			// Create an updated board with the current move
			Board curr_board = b.update(p,  move);
			// Create a String[] under the same constraints as optimal, detailed above
			String[] curr_score = nextMoveHelper(curr_board, p.opponent(), depth -1);
			// Set the move of the current score to be the current move
			curr_score[0] = move.toString();
			// Create a double est that holds the value of the current score.
			double est = Double.valueOf(curr_score[1]);
			if (p == me) {	// If Player is me, 
				// try to find the highest possible value of est, and
				if (best_score < est) {
					// Set the optimal move to be the move maximizing est
					best_score = est;
					curr_score[1] = String.valueOf(est);
					optimal = curr_score;
				}
			} else {	// Otherwise, 
				// Try to find the lowest possible value of est, and
				if (best_score > est) {
					// Set the optimal move for the opponent to be the move minimizing est
					best_score = est;
					curr_score[1] = String.valueOf(est);
					optimal = curr_score;
				}
			}
				
			
		}
		Location l = new Location(4,4);
		if (optimal[0] == l.toString()) {
			for (Location move2 : spaces) {
				System.out.println(move2.toString());
			}	
		}
		return optimal;
		
	}

	
	/** Scores the board */
	protected double scorer(Board b, Player p) {
		return estimate(b);
	}
}
