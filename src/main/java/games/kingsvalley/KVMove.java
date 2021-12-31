package games.kingsvalley;

import iialib.games.model.IMove;

public class KVMove implements IMove {
	
	int startX, startY, endX, endY;
	String start, end;
	
	public KVMove(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		intToString();
	}
	
	public KVMove(String start, String end) {
		this.start = start.toUpperCase();
		this.end = end.toUpperCase();
		this.startX = start.charAt(0) - 'A';
		this.startY = Character.getNumericValue(start.charAt(1)) - 1;
		this.endX = end.charAt(0) - 'A';
		this.endY = Character.getNumericValue(end.charAt(1)) - 1;
	}
	
	private void intToString() {
		char c1 = (char) ('A' + startX);
		start = "" + c1 + (startY+1);
		
		char c2 = (char) ('A' + endX);
		start = "" + c2 + (endY+1);
	}
	
	@Override
	public String toString(){
		return start + "-" + end;
	}
}
