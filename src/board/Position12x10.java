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
	
	static final int WROOK = 'R';
	static final int WKNIGHT = 'N';
	static final int WBISHOP = 'B';
	static final int WQUEEN = 'Q';
	static final int WKING = 'K';
	static final int WPAWN = 'P';
	static final int BROOK = 'r';
	static final int BKNIGHT = 'n';
	static final int BBISHOP = 'b';
	static final int BQUEEN = 'q';
	static final int BKING = 'k';
	static final int BPAWN = 'p';

	
	public Position12x10() {
		
		/* Setup initial Position */
		activeColor = 'w';
		moveNr = 0;
		init12x10();
	}
	
	public Position12x10(Position12x10 position) {
		
		activeColor = new Character(position.getActiveColor());
		moveNr = new Integer(position.getMoveNr());
		
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
	
	/** Init 12x10 board */
	private boolean init12x10() {
		board_12x10 = new int [120];
		int [] org = {
				-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,
				-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,
				-1,	BROOK,BKNIGHT,	BBISHOP,	BQUEEN,	BKING, BBISHOP,	BKNIGHT,	BROOK,-1,
				-1,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	BPAWN,	-1,
				-1,	46,	46,	46,	46,	46,	46,	46,	46,	-1,
				-1,	46,	46,	46,	46,	46,	46,	46,	46,	-1,
				-1,	46,	46,	46,	46,	46,	46,	46,	46,	-1,
				-1,	46,	46,	46,	46,	46,	46,	46,	46,	-1,
				-1,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	WPAWN,	-1,
				-1,	WROOK,WKNIGHT,	WBISHOP,	WQUEEN,	WKING,	WBISHOP, WKNIGHT,	WROOK,-1,
				-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,
				-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1 };
		
		for (int i=0; i < 120; i++)
			board_12x10 [i] = org [i];
		
		//color = 1;
		
		//beatenFigures.clear();
		return true;
	}
	
	public Piece getPieceAt(int x, int y) {
		return getPieceAt(new int[]{x,y});
	}
	
	public Piece getPieceAt(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int i = (7 - y) * 10 + 20 + 1 + x;
		//System.out.println(coord[0] + "/" + coord[1] + " --> " + i + " " + board_12x10[i]);
		return new Piece((char)board_12x10[i], coord);
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
			outstr += board_12x10[i] + " ";
			if(i % 12 == 0) outstr += '\n';
		}
		/*for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				outstr += squares[x][y].toString() + " ";
			}
			outstr += '\n';
		}*/
		return outstr;
	}

	@Override
	public void setPieceAt(Piece p, int[] coord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(int[] coord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFree(int[] coord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(int[] coord) {
		// TODO Auto-generated method stub
		return false;
	}
}
