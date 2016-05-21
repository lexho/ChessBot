package board;

import java.util.ArrayList;
import java.util.List;

import board.pieces.Bishop;
import board.pieces.BlackPawn;
import board.pieces.King;
import board.pieces.Knight;
import board.pieces.Piece;
import board.pieces.Queen;
import board.pieces.Rook;
import board.pieces.WhitePawn;
import board.square.InvalidSquare;
import board.square.Square;
import board.square.ValidSquare;

public class Position {
	Square squares[][];
	char activeColor;
	int moveNr;
	PieceList pieces;
	

	
	public Position() {
		squares = new Square[8][8];
		
		/* Initial Position */
		activeColor = 'w';
		moveNr = 0;
		
		pieces = new PieceList();
		pieces.add(new Rook("r", new int[]{0,7}));
		pieces.add(new Knight("n", new int[]{1,7}));
		pieces.add(new Bishop("b", new int[]{2,7}));
		pieces.add(new Queen("q", new int[]{3,7}));
		pieces.add(new King("k", new int[]{4,7}));
		pieces.add(new Bishop("b", new int[]{5,7}));
		pieces.add(new Knight("n", new int[]{6,7}));
		pieces.add(new Rook("r", new int[]{7,7}));
		
		for(int x = 0; x < 8; x++) {
			pieces.add(new BlackPawn("p", new int[]{x,6}));
		}
		for(int x = 0; x < 8; x++) {
			pieces.add(new WhitePawn("P", new int[]{x,1}));
		}
		
		pieces.add(new Rook("R", new int[]{0,0}));
		pieces.add(new Knight("N", new int[]{1,0}));
		pieces.add(new Bishop("B", new int[]{2,0}));
		pieces.add(new Queen("Q", new int[]{3,0}));
		pieces.add(new King("K", new int[]{4,0}));
		pieces.add(new Bishop("B", new int[]{5,0}));
		pieces.add(new Knight("N", new int[]{6,0}));
		pieces.add(new Rook("R", new int[]{7,0}));
		
		/* Build Square Array */
		buildSquareArray();
	}
	
	public Position(Position position) {
		squares = new Square[8][8];
		
		activeColor = new Character(position.getActiveColor());
		moveNr = new Integer(position.getMoveNr());
		
		pieces = new PieceList();
		for(Piece p : position.getPieces()) {
			switch(p.getID()) {
			case Piece.WHITE_PAWN:
				pieces.add(new WhitePawn(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BLACK_PAWN:
				pieces.add(new BlackPawn(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KING:
				pieces.add(new King(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.QUEEN:
				pieces.add(new Queen(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BISHOP:
				pieces.add(new Bishop(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KNIGHT:
				pieces.add(new Knight(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.ROOK:
				pieces.add(new Rook(p.getRepresentation(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;	
			}
			//pieces.add(new Piece(p));
		}
		
		/* Build Square Array */
		initSquares();
		for(Piece p : pieces) {
			int x = p.getPosition()[0];
			int y = p.getPosition()[1];
			//System.out.println(p + " " + x + " " + y);
			squares[x][y] = new ValidSquare(p);
		}

	}
	
	/* Init Squares */
	private void initSquares() {
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				squares[x][y] = new ValidSquare();
			}
		}
	}
	
	private void buildSquareArray() {
		initSquares();
		
		int i = 0;
		for(int y = 7; y >= 6; y--) {
			for(int x = 0; x < 8; x++) {
				squares[x][y] =  new ValidSquare(pieces.get(i));
				i++;
			}
		}
		for(int y = 1; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				squares[x][y] =  new ValidSquare(pieces.get(i));
				i++;
			}
		}
	}
	
	public Square getSquareAt(int x, int y) {
		/* Square is out of border range */
		if(x > 7 || y > 7 || x < 0 || y < 0) return new InvalidSquare();
		else return squares[x][y];
	}
	
	public Square getSquareAt(int[] coord) {
		return getSquareAt(coord[0], coord[1]);
	}
	
	public char getActiveColor() {
		return activeColor;
	}
	
	public void switchActiveColor() {
		if (activeColor == 'w') activeColor = 'b';
		else if (activeColor == 'b') activeColor = 'w';
		else System.err.println("Error: no active color");
	}
	
	public char getUnactiveColor() {
		if(getActiveColor() == 'w') return 'b';
		else if(getActiveColor() == 'b') return 'w';
		else return (Character) null;
	}
	
	public PieceList getPieces() {
		return pieces;
	}
	
	public int getMoveNr() {
		return moveNr;
	}
	
	public void increaseMoveNr() {
		moveNr++;
	}
	
	public boolean isInCheck() {
		Piece king = getPieces().getByID(Piece.KING);
		List<Piece> opponentsPieces = getPieces().getPieces(getUnactiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves()) {
				if(MoveValidator.validate(this, m)) 
				if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isInCheck(char color) {
		Piece king = getPieces().getByID(Piece.KING);
		List<Piece> opponentsPieces;
		if(color == getActiveColor()) opponentsPieces = getPieces().getPieces(getUnactiveColor());
		else opponentsPieces = getPieces().getPieces(getActiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves()) {
				if(MoveValidator.validate(this, m)) 
				if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		String outstr = new String();
		String check = new String();
		if(isInCheck()) check = " ch ";
		outstr += moveNr + " " + activeColor + check + ", " + pieces.size() + " pieces on the board, " + pieces.getWhitePieces().size() + " white, " + pieces.getBlackPieces().size() + " black\n";
		for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				outstr += squares[x][y].toString() + " ";
			}
			outstr += '\n';
		}
		return outstr;
	}
}
