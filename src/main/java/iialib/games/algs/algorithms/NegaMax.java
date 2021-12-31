package iialib.games.algs.algorithms;

import java.util.ArrayList;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class NegaMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	/** Defaut value for depth limit 
     */
	private final static int DEPTH_MAX_DEFAUT = 4;

	// Attributes
	/** Role of the max player 
     */
	private final Role Ami;

	/** Role of the min player 
     */
	private final Role Adv;

	/** Algorithm max depth
     */
	private int depthMax = DEPTH_MAX_DEFAUT;

	
	/** Heuristic used by the max player 
     */
	private IHeuristic<Board, Role> h;

	//
	/** number of internal visited (developed) nodes (for stats)
     */
	private int nbNodes;
	
	/** number of leaves nodes nodes (for stats)
     */
	private int nbLeaves;

	// --------- Constructors ---------

	public NegaMax(Role Ami, Role Adv, IHeuristic<Board, Role> h) {
		this.Ami = Ami;
		this.Adv = Adv;
		this.h = h;
	}

	//
	public NegaMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		Move bestMove = board.possibleMoves(playerRole).get(0);
		int value = IHeuristic.MIN_VALUE;
		long start = System.currentTimeMillis();
		ArrayList<Move> allMoves = board.possibleMoves(Ami);
		for(Move move : allMoves) {
			int tmp = max_successeur(value , -negamax(board.play(move, playerRole) , Adv ,  1, -1));
			if(tmp > value) {
				value= tmp;
				bestMove = move;
			}
		}
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("[NegaMax] Temps de calcul : " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("[NegaMax] Nombre de noeuds : " + (nbNodes * 1.0 /1000000.0) + " millions");
		System.out.println("[NegaMax] H = " + value);
		System.out.println("-----------------------------------------------------------------------------");
		return bestMove;
	}
	
	
	

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "NegaMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	
	private int negamax(Board board, Role playerRole , int prof , int position) {
		ArrayList<Move> moves = board.possibleMoves(playerRole);
		nbNodes += moves.size();
		if(prof == depthMax || estFeuille(moves)) {
			return  h.eval(board, playerRole, prof) * position;
		}
		else {
			int value = IHeuristic.MIN_VALUE;
			for(Move move : moves) {
				if(position == -1) {
					value = max_successeur(value , -negamax(board.play(move, playerRole) , Ami , prof + 1, -position));
				}else {
					value = max_successeur(value , -negamax(board.play(move, playerRole) , Adv , prof + 1, -position));
				}
			}
			return value;
		}
	}
	
	private int max_successeur(int a, int b) {
		return a > b ? a : b;
	}
	
	private boolean estFeuille(ArrayList<Move> moves) {
		return moves.size() == 0;
	}
	
	
}