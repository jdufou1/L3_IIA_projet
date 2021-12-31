package games.kingsvalley;

import java.awt.Point;
import java.util.Random;

import iialib.games.algs.IHeuristic;

public class KVHeuristics {

	private static Random r = new Random();
	
	public static IHeuristic<KVBoard,KVRole>  hBlue = (board,role,prof) -> {
		/*System.out.println("Heuristic du joueur BLEU");
		if(role == KVRole.WHITE)
			System.out.println("Appelle par BLANC");
		else
			System.out.println("Appelle par BLEU");
		System.out.println(board.toString());*/
		
		KVRole ennemy = KVRole.WHITE;
		KVRole ally = KVRole.BLUE;
		
		Point kingAlly = board.getPositionOfAKing(ally);
		Point kingEnnemy = board.getPositionOfAKing(ennemy);
		/**
		 * Si roi bloque
		 */
		if(board.getPossibleMovesForOnePiece(kingAlly.x, kingAlly.y, true).size() == 0) { //Plus de move pour nous
			System.out.println("PLUS DE MOVE KING ALLIE => Integer.MIN_VALUE"+ "-" + prof);
			return Integer.MIN_VALUE;
		} else if(board.getPossibleMovesForOnePiece(kingEnnemy.x, kingEnnemy.y, true).size() == 0) {
			System.out.println("PLUS DE MOVE KING ENNEMIE => Integer.MAX_VALUE"+ "-" + prof);
			return Integer.MAX_VALUE - prof;
		}
		
		/**
		 * Si roi milieu
		 */
		if(kingAlly.x == board.MIDDLE_PLATEAU && kingAlly.y == board.MIDDLE_PLATEAU) {
			System.out.println("MILIEU KING ALLIE => Integer.MAX_VALUE"+ "-" + prof);
			return Integer.MAX_VALUE - prof;
		}
		else if(kingEnnemy.x == board.MIDDLE_PLATEAU && kingEnnemy.y == board.MIDDLE_PLATEAU) {
			System.out.println("MILIEU KING ENNEMIE => Integer.MIN_VALUE" + "-" + prof);
			return Integer.MIN_VALUE;
		}
		
		return r.nextInt(100);
	};
	
	public static IHeuristic<KVBoard,KVRole>  hWhite = (board,role,prof) -> {
		/*System.out.println("Heuristic du joueur BLANC");
		if(role == KVRole.WHITE)
			System.out.println("Appelle par BLANC");
		else
			System.out.println("Appelle par BLEU");
		System.out.println(board.toString());
		*/
		KVRole ennemy = KVRole.BLUE;
		KVRole ally = KVRole.WHITE;
		
		Point kingAlly = board.getPositionOfAKing(ally);
		Point kingEnnemy = board.getPositionOfAKing(ennemy);
		/**
		 * Si roi bloque
		 */
		if(board.getPossibleMovesForOnePiece(kingAlly.x, kingAlly.y, true).size() == 0) { //Plus de move pour nous
			//System.out.println("PLUS DE MOVE KING ALLIE => Integer.MIN_VALUE");
			return Integer.MIN_VALUE;
		} else if(board.getPossibleMovesForOnePiece(kingEnnemy.x, kingEnnemy.y, true).size() == 0) {
			//System.out.println("PLUS DE MOVE KING ENNEMIE => Integer.MAX_VALUE");
			return Integer.MAX_VALUE - prof;
		}
		
		/**
		 * Si roi milieu
		 */
		if(kingAlly.x == board.MIDDLE_PLATEAU && kingAlly.y == board.MIDDLE_PLATEAU) {
			//System.out.println("MILIEU KING ALLIE => Integer.MAX_VALUE");
			return Integer.MAX_VALUE - prof;
		}
		else if(kingEnnemy.x == board.MIDDLE_PLATEAU && kingEnnemy.y == board.MIDDLE_PLATEAU) {
			//System.out.println("MILIEU KING ENNEMIE => Integer.MIN_VALUE");
			return Integer.MIN_VALUE;
		}
		
		return r.nextInt(100);
	};
}
