package board;

import java.util.ArrayList;
import java.util.List;

import board.pieces.Bishop;
import board.pieces.BlackPawn;
import board.pieces.King;
import board.pieces.Knight;
import board.pieces.Piece;
import board.pieces.PieceList;
import board.pieces.Queen;
import board.pieces.Rook;
import board.pieces.WhitePawn;
import board.position.Fen;
import board.square.InvalidSquare;
import board.square.Square;
import board.square.ValidSquare;
import util.StringUtils;

public class Position implements PositionInterface {
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
		pieces.add(new Rook('r', new int[]{0,7}));
		pieces.add(new Knight('n', new int[]{1,7}));
		pieces.add(new Bishop('b', new int[]{2,7}));
		pieces.add(new Queen('q', new int[]{3,7}));
		pieces.add(new King('k', new int[]{4,7}));
		pieces.add(new Bishop('b', new int[]{5,7}));
		pieces.add(new Knight('n', new int[]{6,7}));
		pieces.add(new Rook('r', new int[]{7,7}));
		
		for(int x = 0; x < 8; x++) {
			pieces.add(new BlackPawn('p', new int[]{x,6}));
		}
		for(int x = 0; x < 8; x++) {
			pieces.add(new WhitePawn('P', new int[]{x,1}));
		}
		
		pieces.add(new Rook('R', new int[]{0,0}));
		pieces.add(new Knight('N', new int[]{1,0}));
		pieces.add(new Bishop('B', new int[]{2,0}));
		pieces.add(new Queen('Q', new int[]{3,0}));
		pieces.add(new King('K', new int[]{4,0}));
		pieces.add(new Bishop('B', new int[]{5,0}));
		pieces.add(new Knight('N', new int[]{6,0}));
		pieces.add(new Rook('R', new int[]{7,0}));
		
		/* Build Square Array */
		buildSquareArray();
		pieces.setSquareArray(squares);
	}
	
	public Position(PositionInterface positionInterface) {
		squares = new Square[8][8];
		
		activeColor = new Character(positionInterface.getActiveColor());
		moveNr = new Integer(positionInterface.getMoveNr());
		
		pieces = new PieceList();
		for(Piece p : positionInterface.getPieces()) {
			switch(p.getID()) {
			case Piece.WHITE_PAWN:
				pieces.add(new WhitePawn(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BLACK_PAWN:
				pieces.add(new BlackPawn(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KING:
				pieces.add(new King(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.QUEEN:
				pieces.add(new Queen(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BISHOP:
				pieces.add(new Bishop(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KNIGHT:
				pieces.add(new Knight(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.ROOK:
				pieces.add(new Rook(p.getCharRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
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
		pieces.setSquareArray(squares);

	}
	
	public Position(Fen fen) {
		squares = new Square[8][8];
		
		activeColor = new Character(fen.getActiveColor());
		moveNr = 0; //new Integer(fen.getFullMove()); //TODO no compliant!?
		
		// rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR
		String piecestr = fen.getPiecePlacement();
		List<String> rows = StringUtils.splitString(piecestr, '/');

		pieces = new PieceList();
		for(int y = 7; y >= 0; y--) {
			String row = rows.get(7 - y);
			for(int x = 0, i = 0; x < 8; x++, i++) {
				char p = row.charAt(i);
				if(Character.isDigit(p)) {
					x += Character.getNumericValue(p); 
					continue;
				}
				pieces.add(PieceCreator.createPiece(Character.toString(p), new int[]{x,y}));
			}
		}
		/* Build Square Array */
		initSquares();
		for(Piece p : pieces) {
			int x = p.getPosition()[0];
			int y = p.getPosition()[1];
			//System.out.println(p + " " + x + " " + y);
			squares[x][y] = new ValidSquare(p);
		}
		pieces.setSquareArray(squares);
	}
	
	/** Init Squares */
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
	
	public void setActiveColor(char color) {
		activeColor = color;
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
		// TODO check if board is running (?)
		Piece king = getPieces().getByID(Piece.KING, getActiveColor());
		if(king == null) return true; // game over
		List<Piece> opponentsPieces = getPieces().getPieces(getUnactiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves()) {
				Position temp = new Position(this);
				temp.setActiveColor(p.getColor());
				if(MoveValidator.validate(temp, m)) 
				if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isInCheck(char color) {
		// TODO check if board is running (?)
		Piece king = getPieces().getByID(Piece.KING, color);
		//System.out.println(color + " king is at " + king.getPosition()[0] + "/" + king.getPosition()[1]);
		if(king == null) return true; //TODO handle no king error
		List<Piece> opponentsPieces;
		if(color == getActiveColor()) opponentsPieces = getPieces().getPieces(getUnactiveColor());
		else opponentsPieces = getPieces().getPieces(getActiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves()) {
				Position temp = new Position(this);
				temp.setActiveColor(p.getColor());
				if(MoveValidator.validate(temp, m)) {
					if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/** Prints a list of all pieces and their location (for Debugging) */
	public void printPieceLocationList() {
		for(Piece p : getPieces()) {
			System.out.println(p.getRep() + " " + p.getPosition()[0] + "/" + p.getPosition()[1]);
		}
	}
	
	public String toString() {
		String outstr = new String();
		String check = new String();
		if(isInCheck()) check = " ch";
		outstr += moveNr + " " + activeColor + check + ", " + pieces.size() + " pieces on the board, " + pieces.getWhitePieces().size() + " white, " + pieces.getBlackPieces().size() + " black\n";
		for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				outstr += squares[x][y].toString() + " ";
			}
			outstr += '\n';
		}
		return outstr;
	}

	@Override
	public Piece getPieceAt(int x, int y) {
		return getPieceAt(new int[] {x, y});
	}

	@Override
	public Piece getPieceAt(int[] coord) {
		return getSquareAt(coord).getPiece();
	}

	@Override
	public void setPieceAt(Piece p, int[] coord) {
		getSquareAt(coord).setPiece(p);
	}

	@Override
	public void clear(int[] coord) {
		getSquareAt(coord).clear();
	}

	@Override
	public boolean isFree(int[] coord) {
		return getSquareAt(coord).isFree();
	}

	@Override
	public boolean isValid(int[] coord) {
		return getSquareAt(coord).isValid();
	}

	@Override
	public boolean[] getCastling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePieceList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableCastling(int index) {
		// TODO Auto-generated method stub
		
	}
}
