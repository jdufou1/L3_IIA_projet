package iialib.games.algs.algorithms;

import java.util.ArrayList;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board>{

	// Constants
	/** Defaut value for depth limit 
     */
	private final static int DEPTH_MAX_DEFAUT = 4;

	// Attributes
	/** Role of the max player 
     */
	private final Role playerMaxRole;

	/** Role of the min player 
     */
	private final Role playerMinRole;

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

	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	//
	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		ArrayList<Move> moves = board.possibleMoves(playerRole);
		Move bestMove = moves.get(0);
		int alpha = IHeuristic.MIN_VALUE;
		int beta = IHeuristic.MAX_VALUE;
		long start = System.currentTimeMillis();
		for(Move move : moves) {
			int tmp = minMax(board.play(move, playerMaxRole), playerMinRole, 1, alpha, beta);
			if(tmp  > alpha)
			{
				alpha = tmp;
				bestMove = move;
			}
		}
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("[AlphaBeta] Temps de calcul : " + (System.currentTimeMillis()-start) + " ms");
		System.out.println("[AlphaBeta] Nombre de noeuds : " + (nbNodes * 1.0 /1000000.0) + " millions");
		System.out.println("[AlphaBeta] Nombre de coupe : " + nbLeaves);
		System.out.println("[AlphaBeta] ALPHA = " + alpha);
		System.out.println("-----------------------------------------------------------------------------");
		return bestMove;
	}
	
	private int maxMin(Board board , Role playerRole, int prof, int alpha, int beta) {
		
		
		if(prof == depthMax || board.isGameOver()) {
			int eval = h.eval(board,(Role) playerMaxRole, prof);
			//System.out.println("MaxMin = " + eval);
			return eval;
		}
		else {
			ArrayList<Move> moves = board.possibleMoves(playerRole);
			nbNodes += moves.size();
			for(Move move : moves) {
				alpha = max_successeur(alpha , minMax(board.play(move, playerRole),
						playerMinRole, prof + 1, alpha, beta));
				if(alpha >= beta) {
					nbLeaves++;
					return beta;
				}
			}
			return alpha;
		}
	}
	
	private int minMax(Board board , Role playerRole, int prof, int alpha, int beta) {
		if(prof == depthMax || board.isGameOver()) {
			int eval = h.eval(board,(Role) playerMinRole, prof);
			//System.out.println("MinMax = " + eval);
			return eval;
		}
		else {
			ArrayList<Move> moves = board.possibleMoves(playerRole);
			nbNodes += moves.size();
			for(Move move : moves) {
				beta = min_successeur(beta , maxMin(board.play(move, playerRole) ,
						playerMaxRole , prof + 1, alpha, beta));
				if(alpha >= beta) {
					nbLeaves++;
					return alpha;
				}
			}
			return beta;
		}
	}
	
	private int max_successeur(int a, int b) {
		return a > b ? a : b;
	}
	
	private int min_successeur(int a, int b) {
		return a < b ? a : b;
	}
	
	private boolean estFeuille(ArrayList<Move> moves) {
		return moves.size() == 0;
	}

}