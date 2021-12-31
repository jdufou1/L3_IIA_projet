package games.kingsvalley;

import java.awt.Point;
import java.util.Random;

import iialib.games.algs.IHeuristic;

//Tu ne peux pas gagner desole frero
public class KVHeuristicsKozlov {

	private static Random r = new Random();
	
	public static IHeuristic<KVBoard,KVRole>  hBlue = (board,role,prof) -> {
		KVRole ennemy = KVRole.WHITE;
		KVRole ally = KVRole.BLUE;
		
		Point kingAlly = board.getPositionOfAKing(ally);
		Point kingEnnemy = board.getPositionOfAKing(ennemy);
		/**
		 * Si roi bloque
		 */
		if(board.getPossibleMovesForOnePiece(kingAlly.x, kingAlly.y, true).size() == 0) { 
			return Integer.MIN_VALUE;
		} else if(board.getPossibleMovesForOnePiece(kingEnnemy.x, kingEnnemy.y, true).size() == 0) {
			return Integer.MAX_VALUE - prof;
		}
		
		/**
		 * Si roi milieu
		 */
		if(kingAlly.x == board.MIDDLE_PLATEAU && kingAlly.y == board.MIDDLE_PLATEAU) {
			return Integer.MAX_VALUE - prof;
		}
		else if(kingEnnemy.x == board.MIDDLE_PLATEAU && kingEnnemy.y == board.MIDDLE_PLATEAU) {
			return Integer.MIN_VALUE;
		}
		
		//le roi peut se déplacer vers le milieu
		int res = r.nextInt(20);
		if(kingCanGoMid(board, kingAlly)) {
			res += 20;
		} 
		if(kingIsCloseToMid(board, kingAlly)) {
			res += 300;
			res += pionOpposeKing(board, kingAlly);
		}
		if(kingCanGoMid(board, kingEnnemy)) {
			res -= 50;
		} 
		if(kingIsCloseToMid(board, kingEnnemy)) {
			res -= 10000;
		}
		if(kingCanGoMidInTwo(board, kingEnnemy)) {
			res -= 10;
		}
		return res;
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
			return Integer.MAX_VALUE - prof;
		}
		
		/**
		 * Si roi milieu
		 */
		if(kingAlly.x == board.MIDDLE_PLATEAU && kingAlly.y == board.MIDDLE_PLATEAU) {
			return Integer.MAX_VALUE - prof;
		}
		else if(kingEnnemy.x == board.MIDDLE_PLATEAU && kingEnnemy.y == board.MIDDLE_PLATEAU) {
			return Integer.MIN_VALUE;
		}
		
		//le roi peut se déplacer vers le milieu
		int res = r.nextInt(20);
		if(kingCanGoMid(board, kingAlly)) {
			res += 20;
		} 
		if(kingIsCloseToMid(board, kingAlly)) {
			res += 300;
			res += pionOpposeKing(board, kingAlly);
		}
		if(kingCanGoMid(board, kingEnnemy)) {
			res -= 50;
		} 
		if(kingIsCloseToMid(board, kingEnnemy)) {
			res -= 10000;
		}
		if(kingCanGoMidInTwo(board, kingEnnemy)) {
			res -= 10;
		}
		return res;
	};
	
	private static boolean kingCanGoMid(KVBoard board, Point king) {
		if(king.x == 3) {
			for(int y = king.y ; y != 3;) {
				if(y != king.y && !board.caseVide(king.x, y)) {
					return false;
				}
				if(y>3) y--;
				else y++;
			}
			return true;
		}
		else if(king.y == 3) {
			for(int x = king.x ; x != 3;) {
				if(x != king.x && !board.caseVide(x, king.y)) {
					return false;
				}
				if(x>3) x--;
				else x++;
			}
			return true;
		}
		else if(king.x == king.y) {
			for(int i = king.x ; i != 3;) {
				if(i != king.x && !board.caseVide(i, i))
					return false;
				if(i>3) i--;
				else i++;
			}
			return true;
		}
		else if(king.x + king.y == 6) {
			for(int i = king.x ; i != 3;) {
				if(i != king.x && !board.caseVide(i, 6-i))
					return false;
				if(i>3) i--;
				else i++;
			}
			return true;
		}
		return false;
	}
	
	private static boolean kingCanGoMidInTwo(KVBoard board, Point king) {
		if(king.x == 3 || king.y == 3) return true;
		else if(king.x == king.y) return true;
		else if(king.x + king.y == 6) return true;
		return false;
	}
	
	private static boolean kingIsCloseToMid(KVBoard board, Point king) {
		for(int i = -1 ; i <= 1 ; i++) {
			for(int j = -1 ; j <= 1 ; j++) {
				if(king.x + i == 3 && king.y + j == 3) return true;
			}
		}
		return false;
	}
	
	private static int pionOpposeKing(KVBoard board, Point king) {
		int res = 0;
		Point p;
		if(king.x == king.y || king.x + king.y == 6) {
			p = new Point((king.x + 2) % 6, (king.y + 2) % 6);
		} else {
			if(king.x == 3) {
				if(king.y == 2) {
					p = new Point(2,4);
				} else p = new Point(2,0);
			} else {
				if(king.x == 2) {
					p = new Point(4,2);
				} else {
					p = new Point(0,2);
				}
			}
		}
		for(int i = 0 ; i <= 2 ; i++) {
			for(int j = 0 ; j <= 2 ; j++) {
				if(!board.caseVide(p.x+i, p.y+j))
					res += 10;
			}
		}
		return res;
	}
}
