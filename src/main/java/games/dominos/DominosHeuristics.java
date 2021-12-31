package games.dominos;

import iialib.games.algs.IHeuristic;

public class DominosHeuristics {
	
	/**
	 * Heuristique du point de vue du joueur vertical.
	 * @param board le plateau à évaluer
	 * @param role le joueur qui a le trait
	 * @return l'évaluation heuristique de (board,role)
	 */
	public static IHeuristic<DominosBoard,DominosRole>  hVertical = (board,role,prof) -> {
        final int nbV = board.nbVerticalMoves();
        final int nbH = board.nbHorizontalMoves();
        
        // S'il n'y plus aucun placement possible,
        // le joueur qui s'apprêtait à jouer a perdu
        if (nbV == 0 && nbH == 0) {
        	if (role == DominosRole.VERTICAL)
        		return IHeuristic.MIN_VALUE;
        	else
        		return IHeuristic.MAX_VALUE;
        }
        // Si seul le joueur vertical n'a plus de placement,
        // alors il a perdu
        else if (nbV == 0) {
        	return IHeuristic.MIN_VALUE;
        }
        // Si seul le joueur horizontal n'a plus de placement,
        // alors il a perdu
        else if (nbH == 0) {
        	return IHeuristic.MAX_VALUE;
        }
        else {
        	return  nbV - nbH;
        }
    };
    
    /**
	 * Heuristique du point de vue du joueur horizontal.
	 * @param board le plateau à évaluer
	 * @param role le joueur qui a le trait
	 * @return l'évaluation heuristique de (board,role)
	 */
	public static IHeuristic<DominosBoard,DominosRole> hHorizontal = (board,role,prof) -> {
        final int nbV = board.nbVerticalMoves();
        final int nbH = board.nbHorizontalMoves();
        // S'il n'y plus aucun placement possible,
        // le joueur qui s'apprêtait à jouer a perdu
        if (nbV == 0 && nbH == 0) {
        	if (role == DominosRole.VERTICAL)
        		return IHeuristic.MAX_VALUE;
        	else
        		return IHeuristic.MIN_VALUE;
        }
        // Si seul le joueur vertical n'a plus de placement,
        // alors il a perdu
        else if (nbV == 0) {
        	return IHeuristic.MAX_VALUE;
        }
        // Si seul le joueur horizontal n'a plus de placement,
        // alors il a perdu
        else if (nbH == 0) {
        	return IHeuristic.MIN_VALUE;
        }
        else {
        	return  nbH - nbV;
        }
    };
   
}
	