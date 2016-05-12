package board;

import board.square.InvalidSquare;
import board.square.Piece;
import board.square.Square;
import board.square.ValidSquare;

public class Position {
	Square squares[][];
	
	public Position() {
		squares = new Square[8][8];
		
		/* Init Squares */
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				squares[x][y] = new ValidSquare();
			}
		}
		
		/* Initial Position */
		squares[0][0] = new ValidSquare(new Piece("R"));
		squares[1][0] = new ValidSquare(new Piece("N"));
		squares[2][0] = new ValidSquare(new Piece("B"));
		squares[3][0] = new ValidSquare(new Piece("Q"));
		squares[4][0] = new ValidSquare(new Piece("K"));
		squares[5][0] = new ValidSquare(new Piece("B"));
		squares[6][0] = new ValidSquare(new Piece("K"));
		squares[7][0] = new ValidSquare(new Piece("R"));
	}
	
	public Square getSquareAt(int x, int y) {
		/* Square is out of border range */
		if(x > 7 || y > 7 || x < 0 || y < 0) return new InvalidSquare();
		else return squares[x][y];
	}
	
	public String toString() {
		String outstr = new String();
		for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				outstr += squares[x][y].toString() + " ";
			}
			outstr += '\n';
		}
		return outstr;
	}
}
