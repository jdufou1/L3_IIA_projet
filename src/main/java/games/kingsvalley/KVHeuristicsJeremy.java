package games.kingsvalley;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import iialib.games.algs.IHeuristic;

public class KVHeuristicsJeremy {
	
	/*
	 * 
	 * Debut de partie : au moins 3 pions dans le camp allie
	 * 
	 * */
	private static Random rand = new Random();
	
	static int[][] plateau_value_for_king = {
			{0,0,0,0,0,0,0},
			{0,200,0,200,0,200,0},
			{0,0,500,500,500,0,0},
			{0,200,500,0,500,200,0},
			{0,0,500,500,500,0,0},
			{0,200,0,200,0,200,0},
			{0,0,0,0,0,0,0}
	};
	
	public static IHeuristic<KVBoard,KVRole>  hBlue = (board,role,prof) -> {
		int score = 0; /* score d'heuristique */
		KVRole ally = KVRole.BLUE;
		KVRole ennemy = KVRole.WHITE;
		
		Point blueKing = board.getPositionOfAKing(ally);
		Point whiteKing = board.getPositionOfAKing(ennemy);
		
		/* DETECTION COUPS FATALS */

		
		ArrayList<KVMove> movesKing = board.getPossibleMovesForOnePiece(blueKing.x, blueKing.y, true);
		int nbMoveBlueKing = movesKing.size();
		int nbMoveWhiteKing = board.getPossibleMovesForOnePiece(whiteKing.x, whiteKing.y, true).size();
		
		/**
		 * Si roi bloque
		 */
		
		if(nbMoveBlueKing == 0)
				return Integer.MIN_VALUE;
		else if(nbMoveWhiteKing == 0)
				return Integer.MAX_VALUE - prof;
		
		
		/**
		 * Si roi milieu
		 */
		char[][] plateau = board.getPlateau(); /* recuperation du plateau de jeu */

		if(blueKing.x == KVBoard.MIDDLE_PLATEAU && blueKing.y == KVBoard.MIDDLE_PLATEAU)
				return Integer.MAX_VALUE - prof;
		if(whiteKing.x == KVBoard.MIDDLE_PLATEAU && whiteKing.y == KVBoard.MIDDLE_PLATEAU)
				return Integer.MIN_VALUE;
		
		
		/* FIN DETECTION */
		
		/* DETECTION PRE-BLOCAGE */
		
		if(movesKing.size() < 2 && role == ennemy) {
			return -1000;
		}
		
		/* DETECTION DEBUT DE PARTIE */
		
		int nbPiece = 0;
		char piece;
		for(int cpt = 0; cpt < plateau.length; cpt++) {
			piece = plateau[cpt][0];
			if(piece == 'x')
				nbPiece++;
		}
		
		if(nbPiece>=3) {
			return rand.nextInt(10);
		}
		
		/* FIN DETECTION DEBUT DE PARTIE */
		
		/* STRATEGIE FIN DE PARTIE */
		
		for(KVMove move : movesKing) {
			String[] arg = move.toString().split("-");
			if(arg[1].contains("D4") && role == ally) {
					return 1000 - prof;
			}
		}
		
		movesKing = board.getPossibleMovesForOnePiece(whiteKing.x, whiteKing.y, true);
		for(KVMove move : movesKing) {
			String[] arg = move.toString().split("-");
			if(arg[1].contains("D4") && role == ennemy) {
					return -1000;
			}
		}
		
		for(int i = 0; i < plateau_value_for_king.length; i++) {
			for(int j = 0; j < plateau_value_for_king.length; j++) {
				piece = plateau[j][i];
				if(piece == 'X')
					return  plateau[j][i] - prof;
				else if (piece == 'O')
					return  plateau[j][i]* -1 ;
			}
		}
		
		return score - prof;
	};
	
	
	
	
	public static IHeuristic<KVBoard,KVRole>  hWhite = (board,role,prof) -> {
		int score = 0; /* score d'heuristique */
		KVRole ally = KVRole.WHITE;
		KVRole ennemy = KVRole.BLUE;
		
		Point whiteKing = board.getPositionOfAKing(ally);
		Point blueKing = board.getPositionOfAKing(ennemy);
		
		ArrayList<KVMove> movesKing = board.getPossibleMovesForOnePiece(whiteKing.x, whiteKing.y, true);
		
		int nbMoveBlueKing = board.getPossibleMovesForOnePiece(blueKing.x, blueKing.y, true).size();
		int nbMoveWhiteKing = movesKing.size();
		/* DETECTION COUPS FATALS */
		
		/**
		 * Si roi bloque
		 */
		if(nbMoveWhiteKing == 0)
			return Integer.MIN_VALUE;
		else if(nbMoveBlueKing == 0)
				return Integer.MAX_VALUE - prof;
		
		/**
		 * Si roi milieu
		 */
		
		char[][] plateau = board.getPlateau(); /* recuperation du plateau de jeu */
		if(blueKing.x == board.MIDDLE_PLATEAU && blueKing.y == board.MIDDLE_PLATEAU ) {
			return Integer.MIN_VALUE;
		}
				
		if(whiteKing.x == board.MIDDLE_PLATEAU && whiteKing.y == board.MIDDLE_PLATEAU ) {
			return Integer.MAX_VALUE - prof;
		}
				
		
		/* FIN DETECTION */
		
		/* DETECTION PRE-BLOCAGE */
		
		if(movesKing.size() < 2 && role == ennemy) {
			return -1000;
		}
		
		/* DETECTION DEBUT DE PARTIE */
		
		int nbPiece = 0;
		char piece;
		
		for(int cpt = 0; cpt < plateau.length; cpt++) {
			piece = plateau[cpt][plateau.length-1];
			if(piece == 'o')
				nbPiece++;
		}
		
		if(nbPiece>=3) {
			return rand.nextInt(10);
		}
		
		/* FIN DETECTION DEBUT DE PARTIE */
			
		for(KVMove move : movesKing) {
			String[] arg = move.toString().split("-");
			if(arg[1].contains("D4") && role == ally) {
					return 1000 - prof;
			}
		}
		
		movesKing = board.getPossibleMovesForOnePiece(blueKing.x, blueKing.y, true);
		for(KVMove move : movesKing) {
			String[] arg = move.toString().split("-");
			if(arg[1].contains("D4") && role == ennemy) {
					return -1000;
			}
		}
		
		for(int i = 0; i < plateau_value_for_king.length; i++) {
			for(int j = 0; j < plateau_value_for_king.length; j++) {
				piece = plateau[j][i];
				if(piece == 'O')
					return  plateau[j][i] - prof;
				else if (piece == 'X')
					return  plateau[j][i]* -1 ;
			}
		}
		
		return score - prof;
	};
	
	
	
	
	
	
}
