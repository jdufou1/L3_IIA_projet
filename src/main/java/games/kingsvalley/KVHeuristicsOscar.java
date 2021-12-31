package games.kingsvalley;

import java.awt.Point;

import iialib.games.algs.IHeuristic;

public class KVHeuristicsOscar {
	public static IHeuristic<KVBoard,KVRole>  hBlue = (board,role,prof) -> {
		KVRole ennemy = KVRole.WHITE;
		KVRole ally = KVRole.BLUE;
		
		Point kingAlly = board.getPositionOfAKing(ally);
		Point kingEnnemy = board.getPositionOfAKing(ennemy);
		
		

		if(board.getPossibleMovesForOnePiece(kingAlly.x, kingAlly.y, true).size() == 0) {
			return Integer.MIN_VALUE;
		} else if(board.getPossibleMovesForOnePiece(kingEnnemy.x, kingEnnemy.y, true).size() == 0) {
			return Integer.MAX_VALUE - prof;
		}
		

		if(kingAlly.x == KVBoard.MIDDLE_PLATEAU && kingAlly.y == KVBoard.MIDDLE_PLATEAU)
			return Integer.MAX_VALUE  - prof;
		else if(kingEnnemy.x == KVBoard.MIDDLE_PLATEAU && kingEnnemy.y == KVBoard.MIDDLE_PLATEAU)
			return Integer.MIN_VALUE;
		
		int score = 0;
		if(isSoldierNearCenter(role, board)) {
			score += 20;
		}
		if(isKingAlignedToCenter(kingAlly)) {
			if(canKingBeBlockedOnCenter(role, kingAlly, board)) {
				score += 200;
			}
			else {
				score += 30;
			}
			
		}
		if(isKingNearCenter(kingAlly)) {
			score += 40;
		}
		
		return score;
	};
	
	public static IHeuristic<KVBoard,KVRole>  hWhite = (board,role,prof) -> {
		KVRole ennemy = KVRole.BLUE;
		KVRole ally = KVRole.WHITE;
		
		Point kingAlly = board.getPositionOfAKing(ally);
		Point kingEnnemy = board.getPositionOfAKing(ennemy);
		
		
		/**
		 * Si roi bloque
		 */
		if(board.getPossibleMovesForOnePiece(kingAlly.x, kingAlly.y, true).size() == 0) {
			return Integer.MIN_VALUE;
		} else if(board.getPossibleMovesForOnePiece(kingEnnemy.x, kingEnnemy.y, true).size() == 0) {
			return Integer.MAX_VALUE  - prof;
		}
		
		/**
		 * Si roi milieu
		 */
		if(kingAlly.x == KVBoard.MIDDLE_PLATEAU && kingAlly.y == KVBoard.MIDDLE_PLATEAU)
			return Integer.MAX_VALUE  - prof;
		else if(kingEnnemy.x == KVBoard.MIDDLE_PLATEAU && kingEnnemy.y == KVBoard.MIDDLE_PLATEAU)
			return Integer.MIN_VALUE;
		
		int score = 0;
		if(isSoldierNearCenter(role, board)) {
			score += 20;
		}
		if(isKingAlignedToCenter(kingAlly)) {
			if(canKingBeBlockedOnCenter(role, kingAlly, board)) {
				score += 200;
			}
			else {
				score += 30;
			}
			
		}
		if(isKingNearCenter(kingAlly)) {
			score += 40;
		}
		
		return score;
	};
	
	//rend vrai si le roi passe en parametre est dans l'alignement du centre
	public static boolean isKingAlignedToCenter(Point king) {
		return king.x == king.y || king.x == KVBoard.MIDDLE_PLATEAU || king.y == KVBoard.MIDDLE_PLATEAU || king.x == KVBoard.TAILLE-1 - king.y;
	}
	
	public static boolean isKingNearCenter(Point king) {
		return king.x >=2 && king.x <=4 && king.y>=2 && king.y <= 4;
	}
	
	public static boolean isSoldierNearCenter(KVRole role, KVBoard board) {
		char symbole = role == KVRole.BLUE ? 'x' : 'o';
		char[][] plateau = board.getPlateau();
		for (int i = 2; i<=4; i++) {
			for(int j= 2; j<=4; j++) {
				if(plateau[j][i] == symbole) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean canKingBeBlockedOnCenter(KVRole role, Point king, KVBoard board) {
		char[][] plateau = board.getPlateau();
		
		if(king.x == king.y) {
			if(king.x > 2) {
				return plateau[2][2] != '-';
			}
			return plateau[4][4] != '-';
		}
		else if (king.x == KVBoard.MIDDLE_PLATEAU) {
			if(king.y > 2) {
				return plateau[3][2] != '-';
			}
			return plateau[3][4] != '-';
		}
		else if(king.y == KVBoard.MIDDLE_PLATEAU) {
			if(king.x > 2) {
				return plateau[2][3] != '-';
			}
			return plateau[4][3] != '-';
		}
		else {
			if(king.x > 2) {
				return plateau[2][4] != '-';
			}
			return plateau[4][2] != '-';
		}
		
	}
	
}
