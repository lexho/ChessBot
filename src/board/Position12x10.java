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
import exceptions.InvalidIndexException;
import util.StringUtils;

public class Position12x10 implements PositionInterface {
	int [] board_12x10;
	char activeColor;
	int moveNr;
	/* Castling availability */
	boolean castling[];
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
		
		/* set castling KQkq */
		castling = new boolean[] {true, true, true, true};
		
		/* Create inital piece list */
		pieces = new PieceList(this);
		pieces.add(new Rook('r', coordToIndex(new int[]{0,7})));
		pieces.add(new Knight('n', coordToIndex(new int[]{1,7})));
		pieces.add(new Bishop('b', coordToIndex(new int[]{2,7})));
		pieces.add(new Queen('q', coordToIndex(new int[]{3,7})));
		pieces.add(new King('k', coordToIndex(new int[]{4,7})));
		pieces.add(new Bishop('b', coordToIndex(new int[]{5,7})));
		pieces.add(new Knight('n', coordToIndex(new int[]{6,7})));
		pieces.add(new Rook('r', coordToIndex(new int[]{7,7})));
		
		for(int x = 0; x < 8; x++) {
			pieces.add(new BlackPawn('p', coordToIndex(new int[]{x,6})));
		}
		for(int x = 0; x < 8; x++) {
			pieces.add(new WhitePawn('P', coordToIndex(new int[]{x,1})));
		}
		
		pieces.add(new Rook('R', coordToIndex(new int[]{0,0})));
		pieces.add(new Knight('N', coordToIndex(new int[]{1,0})));
		pieces.add(new Bishop('B', coordToIndex(new int[]{2,0})));
		pieces.add(new Queen('Q', coordToIndex(new int[]{3,0})));
		pieces.add(new King('K', coordToIndex(new int[]{4,0})));
		pieces.add(new Bishop('B', coordToIndex(new int[]{5,0})));
		pieces.add(new Knight('N', coordToIndex(new int[]{6,0})));
		pieces.add(new Rook('R', coordToIndex(new int[]{7,0})));
		
	}
	
	public Position12x10(Position12x10 position) {
		/* Init board and pieces */
		this.board_12x10 = new int [120];
		pieces = new PieceList(this);
		
		/* Init castling */
		castling = new boolean[4];
		
		/* copy castling availability */
		for(int i = 0; i < 4; i++) {
			castling[i] = position.getCastling()[i];
		}
		
		/* Copy 12x10 board */
		for(int i = 0; i < position.board_12x10.length; i++) {
			this.board_12x10[i] = position.board_12x10[i];
		}
		
		/* Copy piece list */
		/*for(Piece p : position.getPieces()) {
			this.pieces.add(PieceCreator.createPiece(p.getCharRep(), p.getPosIndex()));
		}*/
		/* Create the piece list */
		updatePieceList();
		
		/* set active color */
		this.activeColor = position.activeColor;
		this.moveNr = new Integer(position.getMoveNr());
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
		
		/* Init castling */
		castling = new boolean[4];
		
		/* copy castling availability */
		for(int i = 0; i < 4; i++) {
			castling[i] = position.getCastling()[i];
		}
		
		/*pieces = new PieceList(this);
		for(Piece p : position.getPieces()) {
			this.pieces.add(PieceCreator.createPiece(p.getCharRep(), p.getPosIndex()));
		}*/
		/* Create the piece list */
		updatePieceList();
		
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
		
		//TODO parse castling string
		/* Init castling */
		castling = new boolean[4];
		for(int i = 0; i < 4; i++) {
			castling[i] = false;
		}
		
		/* Parse castling part of Fen */
		String castlingstr = fen.getCastling();
		for(int i = 0; i < castlingstr.length(); i++) {
			switch(castlingstr.charAt(i)) {
			case 'K':
				castling[0] = true;
				break;
			case 'Q':
				castling[1] = true;
				break;
			case 'k':
				castling[2] = true;
				break;
			case 'q':
				castling[3] = true;
				break;
			}
		}
		
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
		/* Init 12x10 board */
		board_12x10 = new int [120];
		int [] emptyBoard = {
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	EMPTY,	INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,
				INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID,	INVALID };
		
		for (int i=0; i < 120; i++)
			board_12x10 [i] = emptyBoard [i];

		for(Piece p: pieces) {
			char piece = p.getCharRep();
			int index = p.getPosIndex();
			board_12x10[index] = piece;
		}
		
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
	
	/** Get castling availability */
	public boolean[] getCastling() {
		return castling;
	}
	
	/** Disable castling
	 * @param index 0 = K, 1 = Q, 2 = k, 3 = q
	 * */
	public void disableCastling(int index) {
		castling[index] = false;
	}
	
	public boolean isInCheck() {
		return isInCheck(getActiveColor());
		/*Piece king = getPieces().getByID(Piece.KING, getActiveColor());
		if(king == null) return true; // game over
		List<Piece> opponentsPieces = getPieces().getPieces(getUnactiveColor());
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves(this)) {
				Position12x10 temp = new Position12x10(this);
				temp.setActiveColor(p.getColor());
				if(MoveValidator.validate(temp, m)) 
				if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
					return true;
				}
			}
		}
		return false;*/
	}
	
	public boolean isInCheck(char color) {
		// TODO check if board is running (?)
		boolean white;
		if(color == 'w') white = true;
		else white = false;
		
		//System.out.println("test " + color + " for check");

		Position12x10 temp = new Position12x10(this);
		Piece king;
		List<Piece> opponentsPieces;
		if(white) {
			temp.setActiveColor('b');
			king = temp.getPieceByID('K').get(0); 
			opponentsPieces = temp.getPieces().getBlackPieces();
		}
		else { 
			temp.setActiveColor('w');
			king = temp.getPieceByID('k').get(0); 
			opponentsPieces = temp.getPieces().getWhitePieces();
		}
		
		if(king == null) return true; //TODO handle no king error
		
		//System.out.println("opponents pieces: " + opponentsPieces);
		for(Piece p : opponentsPieces) {
			for(Move m : p.getPossibleMoves(temp)) {
				
				/* Is any of the opponent's pieces threatening the king? */
				if(m.getTargetIndex() == king.getPosIndex()) {
					//System.out.println(m + " is threatening the king");
					return true;
				}
			}
		}
		return false;
	}
	
	//TODO implement castling test
	/*public boolean castlingAllowed() {
		return true;
	}*/
	
	public boolean isFree(int index) {
		if(board_12x10[index] == EMPTY) {
			return true;
		} else return false;
	}
	
	@Override
	public boolean isFree(int[] coord) {
		int index = coordToIndex(coord);
		return isFree(index);
	}

	@Override
	public boolean isValid(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		if(board_12x10[i] == INVALID) return false;
		else return true;
	}
	
	public boolean isValid(int index) {
		try {
			return board_12x10[index] != INVALID;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidIndexException(index);
		}
	}
	
	/** test piece for color */
	public boolean isColor(int index, char color) {
		char p = (char) board_12x10[index];
		boolean isWhite = Character.isUpperCase(p);
		if(isWhite && color == 'w') return true; 
		else if(!isWhite && color == 'b') return true;
		else return false;
	}
	
	public char getColor(int index) {
		if(Character.isUpperCase(board_12x10[index])) return 'w';
		else return 'b';
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
	
	/** Creates a new piece list or returns the current one */
	public PieceList getPieces() {
		return pieces;
	}
	
	/** This method should be called always after the board has been manipulated */
	public void updatePieceList() {
		pieces = new PieceList();
		for(int i = 0; i < board_12x10.length; i++) {
			
			if(board_12x10[i] != INVALID && board_12x10[i] != EMPTY)
				pieces.add(getPieceAt(i));
		}
	}
	
	public Piece getPieceAt(int x, int y) {
		return getPieceAt(new int[]{x,y});
	}
	
	public Piece getPieceAt(int index) {
		return PieceCreator.createPiece((char)board_12x10[index], index);
	}
	
	public Piece getPieceAt(int[] coord) {
		return getPieceAt(coordToIndex(coord));
		/*int x = coord[0];
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
			
		return new Piece((char)board_12x10[i], coord);*/
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
	
	public void movePiece(int src, int trg) {
		setPieceAt(board_12x10[src], trg);
		clear(src);
	}
	
	//TODO method is not safe
	public void setPieceAt(int p, int index) {
		try {
			board_12x10[index] = p;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidIndexException(index);
		}
	}
	
	@Override
	public void setPieceAt(Piece p, int[] coord) {
		int index = coordToIndex(coord);
		setPieceAt(p.getCharRep(), index);
	}
	
	//TODO method is not safe
	public void clear(int index) {
		try {
			board_12x10[index] = EMPTY;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidIndexException(index);
		}
	}
	
	@Override
	public void clear(int[] coord) {
		int index = coordToIndex(coord);
		clear(index);
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
		char rep = (char)p; // TODO don't use string as rep, use character
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
	
	
	/** Converts one dimensional 12x10 board index to Cartesian coordinates of a 8x8 board
	 * @param index one dimensional 12x10 board index 
	 * @return Cartesian coordinates */
	public static int[] indexToCoord(int index) {
		int x = (index % 10) - 1;
		int y = (11 - (index / 10) - 2) ;
		return new int[]{x,y};
	}
	
	
	/** Converts Cartesian coordinates of a 8x8 board to one dimensional 12x10 board index
	 * @param coord Cartesian coordinates
	 * @return one dimensional 12x10 board index */
	public static int coordToIndex(int[] coord) {
		int index = ((7 - coord[1]) + 2) * 10 + coord[0] + 1;
		return index;
	}
	

	/** Prints a list of all pieces and their location (for Debugging) */
	public void printPieceLocationList() {
		for(Piece p : getPieces()) {
			System.out.println(p.getRep() + " " + p.getPosition()[0] + "/" + p.getPosition()[1]);
		}
	}
	
	/** Print a board by piece list */
	public void printPieceBoard() {
		int [] board = {
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.BROOK,	Position12x10.BKNIGHT,	Position12x10.BBISHOP,	Position12x10.BQUEEN,	Position12x10.BKING, 	Position12x10.BBISHOP,	Position12x10.BKNIGHT,	Position12x10.BROOK,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.WROOK,	Position12x10.WKNIGHT,	Position12x10.WBISHOP,	Position12x10.WQUEEN,	Position12x10.WKING,	Position12x10.WBISHOP, 	Position12x10.WKNIGHT,	Position12x10.WROOK,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID };
		for(Piece p : getPieces()) {
			board[p.getPosIndex()] = p.getCharRep();
		}
		
		String outstr = "####### printPieceBoard #######\n";
		for(int i = 0; i < board_12x10.length; i++) {
			//String number = Integer.toString(board_12x10[i]);
			String number;
			if(board_12x10[i] != -1)number = Character.toString((char)board_12x10[i]);
			else number = Integer.toString(board_12x10[i]);
				outstr += number;
			/* adjust space between the numbers */
			for(int c = number.length(); c < 4; c++) {
				outstr += " ";
			}
			if((i + 1) % 10 == 0) outstr += '\n';
		}
		outstr += "##############################";
		System.out.println(outstr);
	}
	
	public String toString() {
		String outstr = new String();
		String check = new String();
		if(isInCheck()) check = " ch";
		PieceList pieces = getPieces();
		String castlingstr = new String();
		String str = "KQkq";
		for(int i = 0; i < castling.length; i++) {
			if(castling[i]) castlingstr += str.charAt(i);
		}
		
		outstr += moveNr + " " + activeColor + check + ", " + castlingstr + ", " + pieces.size() + " pieces on the board, " + pieces.getWhitePieces().size() + " white, " + pieces.getBlackPieces().size() + " black\n";
		for(int i = 0; i < board_12x10.length; i++) {
			//String number = Integer.toString(board_12x10[i]);
			String number;
			if(board_12x10[i] != -1)number = Character.toString((char)board_12x10[i]);
			else number = Integer.toString(board_12x10[i]);
				outstr += number;
			/* adjust space between the numbers */
			for(int c = number.length(); c < 4; c++) {
				outstr += " ";
			}
			if((i + 1) % 10 == 0) outstr += '\n';
		}
		return outstr;
	}
}
