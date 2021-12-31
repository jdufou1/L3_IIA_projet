package games.kingsvalley;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import iialib.games.algs.AIPlayer;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.AlphaBeta;
import iialib.games.algs.algorithms.MiniMax;
import iialib.games.model.IChallenger;

public class MyChallenger implements IChallenger {
	
	KVRole allie, ennemi;
	KVBoard board;
	GameAlgorithm<KVMove, KVRole, KVBoard> algo;
	AIPlayer<KVMove, KVRole, KVBoard> player;
	
	public MyChallenger() {
		this.board = new KVBoard();
	}
	
	@Override
	public String teamName() {
		return "test";/*BRUNEAU-DUFOURMANTELLE-KOZLOV*/
	}

	@Override
	public void setRole(String role) {
		System.out.println(role);
		if(role.equals("BLUE")) {
			this.allie = KVRole.BLUE;
			this.ennemi = KVRole.WHITE;
			algo = new AlphaBeta<KVMove, KVRole, KVBoard>(allie, ennemi, KVHeuristicsJeremy.hBlue, 5);
		}
		else{
			this.allie = KVRole.WHITE;
			this.ennemi = KVRole.BLUE;
			algo = new AlphaBeta<KVMove, KVRole, KVBoard>(allie, ennemi, KVHeuristicsJeremy.hWhite, 5);
		}
		player = new AIPlayer<KVMove, KVRole, KVBoard>(allie, algo);
	}

	
	@Override
	public void iPlay(String move) {
		System.out.println("My Move : " + move.toString());
		board = board.play(new KVMove(move.split("-")[0],move.split("-")[1]), allie);
	}

	/* valider un coup de l'adversaire*/
	@Override
	public void otherPlay(String move) {
		System.out.println("Ennemie Move : " + move.toString());
		board = board.play(new KVMove(move.split("-")[0],move.split("-")[1]), ennemi);
	}

	@Override
	public String bestMove() {
		return player.bestMove(board).toString();
	}

	@Override
	public String victory() {
		return "Hehe on a gagn�";
	}

	@Override
	public String defeat() {
		return "La lose quoi";
	}

	@Override
	public String tie() {
		return "Egalit� ? Bon ok";
	}

	@Override
	public String getBoard() {
		return board.toString();
	}

	@Override
	public void setBoardFromFile(String fileName) {
		System.out.println("Setboard from file : " + fileName);
		this.board.setBoardFromFile(fileName);
	}

	@Override
	public Set<String> possibleMoves() {
		ArrayList<KVMove> moves  = board.possibleMoves(allie);
		Set<String> set = new HashSet<String>();
		for(KVMove move : moves)
			set.add(move.toString());
		return set;
	}
	/*public static void main(String[] args) {
		MyChallenger j = new MyChallenger();
		j.setRole("WHITE");
		System.out.println(j.possibleMoves());
		j.setBoardFromFile("./ressources/board_state.txt");
		System.out.println(j.board.toString());
	}*/

}
