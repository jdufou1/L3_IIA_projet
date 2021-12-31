package games.kingsvalley;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class KVBoard implements IBoard<KVMove, KVRole, KVBoard> {

	private char plateau[][];
	private final Direction allDir[]; 
	int tour;

	public final static int TAILLE = 7;
	public final static int MIDDLE_PLATEAU = 3;

	public KVBoard() {
		initBoard();
		tour = 1;
		allDir = Direction.values();
	}

	public KVBoard(char plateau[][], int tour) {
		this.plateau = plateau;
		this.tour = tour;
		allDir = Direction.values();
	}
	
	public static void main(String[] args) {
		KVBoard kv = new KVBoard();
		Random r = new Random();
		ArrayList<KVMove> moves;
		KVMove move;
		while(!kv.isGameOver()) {
			System.out.println(kv.toString());
			moves = kv.possibleMoves(KVRole.WHITE);
			move = moves.get(r.nextInt(moves.size()));
			kv = kv.play(move, KVRole.WHITE);
			
			//System.out.println(kv.toString());
			System.out.println(kv.getPlateau());
			moves = kv.possibleMoves(KVRole.BLUE);
			move = moves.get(r.nextInt(moves.size()));
			kv = kv.play(move, KVRole.BLUE);
			
		}
		System.out.println(kv.toString());
		System.out.println("Termin� !");
	}
	
	/**
	 * Pion blanc : 'o'
	 * Roi blanc : 'O'
	 * 
	 * Pion bleu : 'x'
	 * Roi bleu : 'X'
	 */
	@Override
	public ArrayList<KVMove> possibleMoves(KVRole playerRole) {
		if (playerRole == KVRole.BLUE) {
			return blueMoves();
		} else {
			return whiteMoves();
		}
	}

	@Override
	public KVBoard play(KVMove move, KVRole playerRole) {
		char p[][] = copyPlateau();
		char tmp = p[move.startX][move.startY];
		p[move.startX][move.startY] = '-';
		p[move.endX][move.endY] = tmp;
		return new KVBoard(p, tour+1);
	}

	@Override
	public boolean isValidMove(KVMove move, KVRole playerRole) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGameOver() {
		/* FIN DE PARTIE DE BASE */
		boolean kingsInTheMiddle = (plateau[MIDDLE_PLATEAU][MIDDLE_PLATEAU] == 'X' || plateau[MIDDLE_PLATEAU][MIDDLE_PLATEAU] == 'O');
		/* FIN DE PARTIE AVEC UN DES ROIS BLOQUE */

		/* RECUPERATION DU NOMBRE DE COUP DU ROI BLEU */
		Point positionBlueKing = getPositionOfAKing(KVRole.BLUE);
		int nbMovesBlueking = getPossibleMovesForOnePiece(positionBlueKing.x , positionBlueKing.y, true).size();
		//System.out.println("position roi bleu : " + positionBlueKing + "nbcoups : " + nbMovesBlueking);

		/* RECUPERATION DU NOMBRE DE COUP DU ROI BLANC */
		Point positionWhiteKing = getPositionOfAKing(KVRole.WHITE);
		int nbMovesWhiteking = getPossibleMovesForOnePiece(positionWhiteKing.x , positionWhiteKing.y , true).size();
		//System.out.println("position roi blanc : " + positionWhiteKing + "nbcoups : " + nbMovesWhiteking);
		return kingsInTheMiddle || nbMovesBlueking == 0 || nbMovesWhiteking == 0;
	}

	@Override
	public ArrayList<Score<KVRole>> getScores() {
		ArrayList<Score<KVRole>> scores = new ArrayList<>();
		if(plateau[MIDDLE_PLATEAU][MIDDLE_PLATEAU] == 'X') {
			scores.add(new Score<KVRole>(KVRole.BLUE, Score.Status.WIN,1));
			scores.add(new Score<KVRole>(KVRole.WHITE, Score.Status.LOOSE,0));
		} else if(plateau[MIDDLE_PLATEAU][MIDDLE_PLATEAU] == 'O') {
			scores.add(new Score<KVRole>(KVRole.BLUE, Score.Status.LOOSE,0));
			scores.add(new Score<KVRole>(KVRole.WHITE, Score.Status.WIN,1));
		} else {
			Point k = getPositionOfAKing(KVRole.BLUE);
			if(getPossibleMovesForOnePiece(k.x, k.y, true).size() == 0) {
				scores.add(new Score<KVRole>(KVRole.BLUE, Score.Status.LOOSE,0));
				scores.add(new Score<KVRole>(KVRole.WHITE, Score.Status.WIN,1));
			} else {
				scores.add(new Score<KVRole>(KVRole.BLUE, Score.Status.WIN,1));
				scores.add(new Score<KVRole>(KVRole.WHITE, Score.Status.LOOSE,0));
			}
		}
		return scores;
	}

	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("  ABCDEFG  \n");
		build.append("...........\n");
		for(int i = TAILLE - 1 ; i >= 0 ; i--) {
			build.append((i+1) + ":");
			for(int j = 0 ; j < TAILLE ; j++) {
				build.append(plateau[j][i]);
			}
			build.append(":" + (i+1) + "\n");
		}
		build.append("...........\n");
		build.append("  ABCDEFG  \n");
		return build.toString();
	}

	private void initBoard() {
		plateau = new char[TAILLE][TAILLE];
		for(int i = 0 ; i < TAILLE ; i++) {
			for(int j = 0 ; j < TAILLE ; j++) {
				plateau[i][j] = '-';
			}
		}
		plateau[3][3] = '+';
		for(int i = 0 ; i < TAILLE ; i++) {
			plateau[i][0] = 'x';
			plateau[i][TAILLE - 1] = 'o';
		}
		plateau[3][0] = 'O';
		plateau[3][TAILLE - 1] = 'X';
	}

	/**
	 * Permet de transformer une position du tableau plateau[x][y] en une position 'A5' par ex
	 * @param x -> Lettre x=0 => 'A'
	 * @param y -> Num y=0 => 1
	 * @return Position like 'B2'
	 */
	public String getPositionPiece(int x, int y) {
		char c = (char) ('A' + x);
		return "" + c + (y+1);
	}



	public ArrayList<KVMove> blueMoves(){
		ArrayList<KVMove> result = new ArrayList<>();
		for(int i = 0 ; i < TAILLE ; i++) {
			for(int j = 0 ; j < TAILLE ; j++) {
				if(getRoleOfPiece(i,j) == KVRole.BLUE) {
					boolean isKing = (plateau[i][j] == 'X');
					if(tour != 1 || !isKing) {
						ArrayList<KVMove> movesForOnePiece = getPossibleMovesForOnePiece(i,j,isKing);
						for(KVMove move : movesForOnePiece)
							result.add(move);
					}
				}
			}
		}
		return result;
	}

	public ArrayList<KVMove> whiteMoves(){
		ArrayList<KVMove> result = new ArrayList<>();
		for(int i = 0 ; i < TAILLE ; i++) {
			for(int j = 0 ; j < TAILLE ; j++) {
				if(getRoleOfPiece(i,j) == KVRole.WHITE) {
					boolean isKing = (plateau[i][j] == 'O');
					if(tour > 1 || !isKing) {
						ArrayList<KVMove> movesForOnePiece = getPossibleMovesForOnePiece(i,j,isKing);
						for(KVMove move : movesForOnePiece)
							result.add(move);
					}
				}
			}
		}
		return result;
	}



	public ArrayList<KVMove> getPossibleMovesForOnePiece(int x, int y, boolean isKing) {
		ArrayList<KVMove> moves = new ArrayList<>();
		ArrayList<Direction> directions = new ArrayList<>();
		directions.addAll(Arrays.asList(allDir));
		int tour = 0; int deltaX = 0; int deltaY = 0;
		Direction dirTmp = directions.get(0);
		String defaultPosition = getPositionPiece(x, y);
		boolean stop = false;

		while(!directions.isEmpty()) {
			deltaX += dirTmp.x;
			deltaY += dirTmp.y;
			//System.out.println(deltaX + " ; " + deltaY);

			if(x + deltaX < 0 || x + deltaX >= TAILLE || y + deltaY < 0 || y + deltaY >= TAILLE) {
				//System.out.println("Arrete car x = " + (x+deltaX) + " ou y=" + (y+deltaY));
				stop = true;
				deltaX -= dirTmp.x;
				deltaY -= dirTmp.y;
			}
			else {
				char c = plateau[x + deltaX][y + deltaY]; //on recupere la piece sur laquelle on veut aller
				if(c == 'o' || c == 'O' || c == 'x' || c == 'X') {
					//System.out.println("Arrete sur :" + c);
					stop = true;
					deltaX -= dirTmp.x;
					deltaY -= dirTmp.y;
				}
				//else System.out.println("On passe sur :" + c);
			}
			//System.out.println("----------------------");
			if(!stop) {
				tour++;
			}
			else {
				// Nouvelle direction
				//System.out.println("Nouvelle direction");
				directions.remove(0);
				
				if(tour > 0) {// Si le pion ne bouge pas alors on ajoute pas le déplacement
					if((!isKing && plateau[x+deltaX][y+deltaY] != '+') || isKing) //A TESTER CAR UN PION PEUT PAS SARRETER SUR +
						moves.add(new KVMove(defaultPosition, getPositionPiece(x + deltaX, y + deltaY)));
				}
				if(!directions.isEmpty()) {
					dirTmp = directions.get(0);
					deltaX = 0;
					deltaY = 0;
					tour = 0;
					stop = false;
				}
			}
		}
		return moves;
	}


	/* private functions */

	/*
	 * @return KVRole or null if (x,y) empty position 
	 * */
	private KVRole getRoleOfPiece(int x, int y) {
		char piece = plateau[x][y];
		if(piece == 'O'  || piece == 'o')
			return KVRole.WHITE;
		else if(piece == 'X'  || piece == 'x')
			return KVRole.BLUE;
		else
			return null;
	}


	/*
	 * 
	 * @return Array : [ x ; y]
	 * */
	public Point getPositionOfAKing(KVRole role){
		Point position = new Point();
		for(int i = 0 ; i < TAILLE ; i++) {
			for(int j = 0 ; j < TAILLE ; j++) {
				if(plateau[i][j] == 'O' && role == KVRole.WHITE) {
					position.x = i;
					position.y = j;
				}
				else if(plateau[i][j] == 'X' && role == KVRole.BLUE) {
					position.x = i;
					position.y = j;
				}

			}
		}
		return position;
	}


	private char[][] copyPlateau() {
		char[][] newPlateau = new char[TAILLE][TAILLE];
		for(int i = 0 ; i < TAILLE ; i++) 
			for(int j = 0 ; j < TAILLE ; j++) 
				newPlateau[i][j] = plateau[i][j];

		return newPlateau;
	}


	/* ENUM */


	private enum Direction {
		/*
		 * H : Haut
		 * B : Bas
		 * G : Gauche
		 * D : Droite
		 */
		H(0,-1), B(0,+1), G(-1,0), D(+1,0), HD(+1,-1), HG(-1,-1), BD(+1,+1), BG(-1,+1);

		final int x;
		final int y;

		private Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	/// nom du fichier : ./ressources/board_state.txt
	public void setBoardFromFile(String fileName) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					fileName)); 
			reader.readLine();
			String line = reader.readLine();
			int i = TAILLE - 1;
			while (i >= 0 && line != null) {
				line = reader.readLine();
				char[] ligneCharBrute = line.toCharArray();
				int indexPlateau = 0;
				for(int j=4; j<=16;j+=2) {
					this.plateau[indexPlateau][i] = ligneCharBrute[j];
					indexPlateau++;
				}
				i--;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* getters et setters */
	
	public char[][] getPlateau(){
		return copyPlateau();
	}
	
	public void setPlateau(char[][] plateau) {
		this.plateau = plateau;
	}
	
	public boolean caseVide(int x, int y) {
		return plateau[x][y] == '-';
	}
}
