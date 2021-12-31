package iialib.games.algs.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class IDAlphaBeta <Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board>{

	
	private final Role playerMaxRole;

	/** Role of the min player 
     */
	private final Role playerMinRole;

	/** Algorithm max depth
     */
	private int depthMax = 1;

	/* seconde */
	private double time_remaining = 60;
	
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
	
	private long start;
	
	
	/* Constructor */
	public IDAlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}
	
	public IDAlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, double time) {
		this(playerMaxRole, playerMinRole, h);
		this.time_remaining = time;
	}
	
	
	
	
	@Override
	public Move bestMove(Board board, Role playerRole) {
		ArrayList<Move> moves = board.possibleMoves(playerRole);
		Move bestMove = moves.get(0);
		int alpha = IHeuristic.MIN_VALUE;
		int beta = IHeuristic.MAX_VALUE;
		
		int key = 0;
				
		start = System.currentTimeMillis()/1000;
		
		HashMap<Integer, Move> boards = new HashMap<Integer,Move>();
		ArrayList<Integer> valuesH = new ArrayList<Integer>();
		
		while( (System.currentTimeMillis()/1000) - start <= time_remaining*0.95) {
			long start_inside = System.currentTimeMillis()/1000;
			for(Move move : moves) {
				int tmp = minMax(board.play(move, playerMaxRole), playerMinRole, 0, alpha, beta);
				
				if(tmp  > alpha)
				{
					alpha = tmp;
					bestMove = move;
				}
				boards.put(key,move);
				valuesH.add(tmp);
			}
			/* Si la recherche met moins d'une seconde, on est sur d'avoir la meilleur valeur de l'arbre */
			if((System.currentTimeMillis()/1000) - start_inside < 1 && depthMax > 2)
				break;
			
			depthMax++;
		}
		
		
		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("[IDAlphaBeta] Temps de calcul : " + ((System.currentTimeMillis()) - (start*1000)) + " ms");
		System.out.println("[IDAlphaBeta] Nombre de noeuds : " + (nbNodes * 1.0 /1000000.0) + " millions");
		System.out.println("[IDAlphaBeta] Nombre de coupe : " + nbLeaves);
		System.out.println("[IDAlphaBeta] ALPHA = " + alpha);
		System.out.println("[IDAlphaBeta] Profondeur atteinte = " + depthMax);
		System.out.println("-----------------------------------------------------------------------------");
		return bestMove;
	}
	
	
	
	
	private int maxMin(Board board , Role playerRole, int prof, int alpha, int beta) {
		long end = System.currentTimeMillis()/1000;
		if(end - start >=  time_remaining*0.95) {
			int eval = h.eval(board,(Role) playerMinRole, prof);
			return eval;
		}
		if(prof == depthMax || board.isGameOver()) {
			int eval = h.eval(board,(Role) playerMaxRole, prof);
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
		long end = System.currentTimeMillis()/1000;
		if(end - start >=  time_remaining*0.95) {
			int eval = h.eval(board,(Role) playerMinRole, prof);
			return eval;
		}
		if(prof == depthMax || board.isGameOver()) {
			int eval = h.eval(board,(Role) playerMinRole, prof);
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
	
	private 

}
