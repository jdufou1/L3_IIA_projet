package games.kingsvalley;


import java.util.ArrayList;

import iialib.games.algs.AIPlayer;
import iialib.games.algs.AbstractGame;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.AlphaBeta;
import iialib.games.algs.algorithms.IDAlphaBeta;
import iialib.games.algs.algorithms.MiniMax;

public class KVGame extends AbstractGame<KVMove, KVRole, KVBoard> {

	KVGame(ArrayList<AIPlayer<KVMove, KVRole, KVBoard>> players, KVBoard board) {
		super(players, board);
	}

	public static void main(String[] args) {
		

		KVRole roleB = KVRole.BLUE;
		KVRole roleW = KVRole.WHITE;

		GameAlgorithm<KVMove, KVRole, KVBoard> algB = new IDAlphaBeta<KVMove, KVRole, KVBoard>(
				roleB, roleW, KVHeuristicsKozlov.hBlue, 10);

		GameAlgorithm<KVMove, KVRole, KVBoard> algW = new AlphaBeta<KVMove, KVRole, KVBoard>(
				roleW, roleB, KVHeuristicsKozlov.hWhite, 4);

		AIPlayer<KVMove, KVRole, KVBoard> playerB = new AIPlayer<KVMove, KVRole, KVBoard>(
				roleB, algB);

		AIPlayer<KVMove, KVRole, KVBoard> playerW = new AIPlayer<KVMove, KVRole, KVBoard>(
				roleW, algW);

		ArrayList<AIPlayer<KVMove, KVRole, KVBoard>> players = new ArrayList<AIPlayer<KVMove, KVRole, KVBoard>>();

		players.add(playerB); // First Player
		players.add(playerW); // Second Player

		// char debug => si yoyo alors BUG
		// sinon non
		/*char debug[][] = {
				{'x','-','-','-','-','o','-'},
				{'-','X','-','-','-','-','-'},
				{'-','-','-','-','-','-','x'},
				{'o','-','-','+','-','o','o'},
				{'o','-','-','-','x','x','x'},
				{'-','-','-','-','-','-','o'},
				{'-','O','-','-','o','x','-'},
		};
		KVBoard initialBoard = new KVBoard(c,20);*/

		KVBoard initialBoard = new KVBoard();

		KVGame game = new KVGame(players, initialBoard);
		game.runGame();
	}

}