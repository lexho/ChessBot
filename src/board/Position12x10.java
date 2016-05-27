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

public class Position12x10 implements PositionInterface {
	int [] board_12x10;
	char activeColor;
	int moveNr;
	PieceList pieces;
	
	public static final int WROOK = 'R';
	public static final int WKNIGHT = 'N';
	public static final int WBISHOP = 'B';
	public static final int WQUEEN = 'Q';
	public static final int WKING = 'K';
	public static final int WPAWN = 'P';
	public static final int BROOK = 'r';
	public static final int BKNIGHT = 'n';
	public static final int BBISHOP = 'b';
	public static final int BQUEEN = 'q';
	public static final int BKING = 'k';
	public static final int BPAWN = 'p';
	public static final int EMPTY = '.';
	public static final int INVALID = -1;
	
	public Position12x10() {
		
		/* Setup initial Position */
		activeColor = 'w';
		moveNr = 0;
		init12x10();
		
		/* Create inital piece list */
		pieces = new PieceList(this);
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
	}
	
	public Position12x10(PositionInterface position) {
		
		activeColor = new Character(position.getActiveColor());
		moveNr = new Integer(position.getMoveNr());
		
		board_12x10 = new int [120];
		/* copy 12x10 board array */
		int[] board_to_copy = ((Position12x10) position).get12x10Board();
		for(int i = 0; i < board_to_copy.length; i++) {
			board_12x10[i] = board_to_copy[i];
		}
		
		pieces = new PieceList(this);
		for(Piece p : position.getPieces()) {
			switch(p.getID()) {
			case Piece.WHITE_PAWN:
				pieces.add(new WhitePawn(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BLACK_PAWN:
				pieces.add(new BlackPawn(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KING:
				pieces.add(new King(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.QUEEN:
				pieces.add(new Queen(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.BISHOP:
				pieces.add(new Bishop(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.KNIGHT:
				pieces.add(new Knight(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;
			case Piece.ROOK:
				pieces.add(new Rook(p.getRep(), new int[]{p.getPosition()[0],p.getPosition()[1]}));
				break;	
			}
		}
		
		//TODO build 12x10 array
		/*initSquares();
		for(Piece p : pieces) {
			int x = p.getPosition()[0];
			int y = p.getPosition()[1];
			//System.out.println(p + " " + x + " " + y);
			squares[x][y] = new ValidSquare(p);
		}
		pieces.setSquareArray(squares);*/

	}
	
	public Position12x10(Fen fen) {
		
		activeColor = new Character(fen.getActiveColor());
		moveNr = 0; //new Integer(fen.getFullMove()); //TODO no compliant!?
		
		// rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR
		String piecestr = fen.getPiecePlacement();
		List<String> rows = StringUtils.splitString(piecestr, '/');

		pieces = new PieceList(this);
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
		//TODO build 12x10 array
		/*initSquares();
		for(Piece p : pieces) {
			int x = p.getPosition()[0];
			int y = p.getPosition()[1];
			//System.out.println(p + " " + x + " " + y);
			squares[x][y] = new ValidSquare(p);
		}
		pieces.setSquareArray(squares);*/
	}
	
	public void increaseMoveNr() {
		moveNr++;
	}
	
	public void switchActiveColor() {
		if (activeColor == 'w') activeColor = 'b';
		else if (activeColor == 'b') activeColor = 'w';
		else System.err.println("Error: no active color");
	}
	
	public boolean isInCheck() {
		// TODO check if board is running (?)
		Piece king = getPieces().getByID(Piece.KING, getActiveColor());
		if(king == null) return true; // game over
		List<Piece> opponentsPieces = getPieces().getPieces(getUnactiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves()) {
				Position12x10 temp = new Position12x10(this);
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
				Position12x10 temp = new Position12x10(this);
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
	
	@Override
	public boolean isFree(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		//System.out.println(coord[0] + "/" + coord[1] + " --> " + i + " " + board_12x10[i]);
		if(board_12x10[i] == EMPTY) return true;
		else return false;
	}

	@Override
	public boolean isValid(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		if(board_12x10[i] == INVALID) return false;
		else return true;
	}
	
	public int[] get12x10Board() {
		return board_12x10;
	}
	
	public char getActiveColor() {
		return activeColor;
	}
	
	public char getUnactiveColor() {
		if(getActiveColor() == 'w') return 'b';
		else if(getActiveColor() == 'b') return 'w';
		else return (Character) null;
	}
	
	public int getMoveNr() {
		return moveNr;
	}
	
	public PieceList getPieces() {
		return pieces;
	}
	
	public Piece getPieceAt(int x, int y) {
		return getPieceAt(new int[]{x,y});
	}
	
	public Piece getPieceAt(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		//System.out.println(coord[0] + "/" + coord[1] + " --> " + i + " " + board_12x10[i]);
		
		String rep = Character.toString((char)board_12x10[i]); // TODO don't use string as rep, use character
		switch((char)board_12x10[i]) {
		case WPAWN:
			return new WhitePawn(rep, new int[]{x,y});
		case BPAWN:
			return new BlackPawn(rep, new int[]{x,y});
			
		case WKING:
		case BKING:
			return new King(rep, new int[]{x,y});
			
		case WQUEEN:
		case BQUEEN:
			return new Queen(rep, new int[]{x,y});
			
		case WBISHOP:
		case BBISHOP:
			return new Bishop(rep, new int[]{x,y});
			
		case WKNIGHT:
		case BKNIGHT:
			return new Knight(rep, new int[]{x,y});
			
		case WROOK:
		case BROOK:
			return new Rook(rep, new int[]{x,y});
				
		}
		//pieces.add(new Piece(p));
			
		return new Piece((char)board_12x10[i], coord);
	}
	
	public List<Piece> getPieceByID(int pieceID) {
		
		List<Piece> pieces = new ArrayList<Piece>();
		for(int i = 0; i < board_12x10.length; i++) {
			if(board_12x10[i] == pieceID) {
				pieces.add(intToPiece(board_12x10[i], indexToCoord(i)));
			}
		}
		return pieces;
	}
	
	public void setActiveColor(char color) {
		activeColor = color;
	}
	
	@Override
	public void setPieceAt(Piece p, int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		//System.out.println(coord[0] + "/" + coord[1] + " --> " + i + " " + board_12x10[i]);
		board_12x10[i] = p.getCharRep();	
	}
	
	@Override
	public void clear(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		//System.out.println(coord[0] + "/" + coord[1] + " --> " + i + " " + board_12x10[i]);
		board_12x10[i] = EMPTY;
	}
	
	/** Init 12x10 board */
	private boolean init12x10() {
		board_12x10 = new int [120];
		int [] org = {
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	BROOK,BKNIGHT,	BBISHOP,	BQUEEN,	BKING, BBISHOP,	BKNIGHT,	BROOK,INVALID,
				INVALID,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	INVALID,
				INVALID,	WROOK,WKNIGHT,	WBISHOP,	WQUEEN,	WKING,	WBISHOP, WKNIGHT,	WROOK,INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID };
		
		for (int i=0; i < 120; i++)
			board_12x10 [i] = org [i];
		
		//color = 1;
		
		//beatenFigures.clear();
		return true;
	}
	
	private Piece intToPiece(int p, int[] coord) {
		String rep = Character.toString((char)p); // TODO don't use string as rep, use character
		switch(p) {
		case WPAWN:
			return new WhitePawn(rep, coord);
		case BPAWN:
			return new BlackPawn(rep, coord);
			
		case WKING:
		case BKING:
			return new King(rep, coord);
			
		case WQUEEN:
		case BQUEEN:
			return new Queen(rep, coord);
			
		case WBISHOP:
		case BBISHOP:
			return new Bishop(rep, coord);
			
		case WKNIGHT:
		case BKNIGHT:
			return new Knight(rep, coord);
			
		case WROOK:
		case BROOK:
			return new Rook(rep, coord);
				
		}
		return new Piece('x', coord);
	}
	
	
	private int[] indexToCoord(int index) {
		int x = (index / 12) % 12;
		int y = index / 12;
		return new int[]{x,y};
	}
	
	
	
	private int coordToIndex(int[] coord) {
		int index = (7 - coord[1]) * 10 + 21 + coord[0];
		return index;
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
		for(int i = 0; i < board_12x10.length; i++) {
			String number = Integer.toString(board_12x10[i]);
			outstr += number;
			/* adjust space between the numbers */
			for(int c = number.length(); c < 4; c++) {
				outstr += " ";
			}
			if((i + 1) % 12 == 0) outstr += '\n';
		}
		return outstr;
	}
}
