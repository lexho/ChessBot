package board.position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import board.pieces.Piece;
import board.pieces.PieceList;

public class PositionBB implements PositionInterface {
	
    // Bitboards
    //public long[] pieceTypeBB;
	//public long whiteBB, blackBB;
    public long[] pieceTypeBB;
    public long whiteBB, blackBB, allBB;
    
    char[] board = new char[64]; // 8x8 board
    
    /** Initialize board to empty position. */
    public PositionBB() {
    	pieceTypeBB = new long[Piece.nPieceTypes];
    	
    	/* Initialize Bitboards */
    	for (int i = 0; i < Piece.nPieceTypes; i++) {
             pieceTypeBB[i] = 0L;
        }
    	whiteBB = blackBB = 0L;
    }
    
    public void createStartpos() {
        
    	/* WhitePawns, WhiteKnights, WhiteBishops, WhiteRooks, WhiteQueens, and WhiteKing, BlackPawns, BlackKnights, BlackBishops, BlackRooks, BlackQueens and BlackKing. */

    	
        /* Initial position */
    	/*pieceTypeBB[0] = 0b0000000000000000000000000000000000000000000000001111111100000000L; // WhitePawns
    	pieceTypeBB[1] = 0b0000000000000000000000000000000000000000000000000000000001000010L; // WhiteKnights
    	pieceTypeBB[2] = 0b0000000000000000000000000000000000000000000000000000000000100100L; // WhiteBishops
    	pieceTypeBB[3] = 0b0000000000000000000000000000000000000000000000000000000010000001L; // WhiteRooks
    	pieceTypeBB[4] = 0b0000000000000000000000000000000000000000000000000000000000001000L; // WhiteQueens
    	pieceTypeBB[5] = 0b0000000000000000000000000000000000000000000000000000000000010000L; // WhiteKing
    	
    	pieceTypeBB[6] = 0b0000000000000000000000000000000000000000000000000000000000000000L; // BlackPawns
    	pieceTypeBB[7] = 0b0100001000000000000000000000000000000000000000000000000000000000L; // BlackKnights
    	pieceTypeBB[8] = 0b0010010000000000000000000000000000000000000000000000000000000000L; // BlackBishops
    	pieceTypeBB[9] = 0b1000000100000000000000000000000000000000000000000000000000000000L; // BlackRooks
    	pieceTypeBB[10] = 0b0000100000000000000000000000000000000000000000000000000000000000L; // BlackQueens
    	pieceTypeBB[11] = 0b0001000000000000000000000000000000000000000000000000000000000000L; // BlackKing
    	
    	pieceTypeBB[12] = 0b0000000000000000000000000000000000000000000000001111111111111111L; // AllWhitePieces
    	pieceTypeBB[13] = 0b1111111111111111000000000000000000000000000000000000000000000000L; // AllBlackPieces
    	pieceTypeBB[13] = 0b1111111111111111000000000000000000000000000000001111111111111111L; // AllPieces */
    	
    	/*pieceTypeBB[0] = new BitBoard(0b0000000000000000000000000000000000000000000000001111111100000000L); // WhitePawns
    	pieceTypeBB[1] = new BitBoard(0b0000000000000000000000000000000000000000000000000000000001000010L); // WhiteKnights
    	pieceTypeBB[2] = new BitBoard(0b0000000000000000000000000000000000000000000000000000000000100100L); // WhiteBishops
    	pieceTypeBB[3] = new BitBoard(0b0000000000000000000000000000000000000000000000000000000010000001L); // WhiteRooks
    	pieceTypeBB[4] = new BitBoard(0b0000000000000000000000000000000000000000000000000000000000001000L); // WhiteQueens
    	pieceTypeBB[5] = new BitBoard(0b0000000000000000000000000000000000000000000000000000000000010000L); // WhiteKing
    	
    	pieceTypeBB[6] = new BitBoard(0b0000000011111111000000000000000000000000000000000000000000000000L); // BlackPawns
    	pieceTypeBB[7] = new BitBoard(0b0100001000000000000000000000000000000000000000000000000000000000L); // BlackKnights
    	pieceTypeBB[8] = new BitBoard(0b0010010000000000000000000000000000000000000000000000000000000000L); // BlackBishops
    	pieceTypeBB[9] = new BitBoard(0b1000000100000000000000000000000000000000000000000000000000000000L); // BlackRooks
    	pieceTypeBB[10] = new BitBoard(0b0000100000000000000000000000000000000000000000000000000000000000L); // BlackQueens
    	pieceTypeBB[11] = new BitBoard(0b0001000000000000000000000000000000000000000000000000000000000000L); // BlackKing
    	whiteBB = new BitBoard(0b0000000000000000000000000000000000000000000000001111111111111111L);
    	blackBB = new BitBoard(0b1111111111111111000000000000000000000000000000000000000000000000L);
    	allBB = new BitBoard(0b1111111111111111000000000000000000000000000000001111111111111111L);*/
    	
    	pieceTypeBB[0] = 0b0000000000000000000000000000000000000000000000001111111100000000L; // WhitePawns
    	pieceTypeBB[1] = 0b0000000000000000000000000000000000000000000000000000000001000010L; // WhiteKnights
    	pieceTypeBB[2] = 0b0000000000000000000000000000000000000000000000000000000000100100L; // WhiteBishops
    	pieceTypeBB[3] = 0b0000000000000000000000000000000000000000000000000000000010000001L; // WhiteRooks
    	pieceTypeBB[4] = 0b0000000000000000000000000000000000000000000000000000000000001000L; // WhiteQueens
    	pieceTypeBB[5] = 0b0000000000000000000000000000000000000000000000000000000000010000L; // WhiteKing
    	
    	pieceTypeBB[6] = 0b0000000011111111000000000000000000000000000000000000000000000000L; // BlackPawns
    	pieceTypeBB[7] = 0b0100001000000000000000000000000000000000000000000000000000000000L; // BlackKnights
    	pieceTypeBB[8] = 0b0010010000000000000000000000000000000000000000000000000000000000L; // BlackBishops
    	pieceTypeBB[9] = 0b1000000100000000000000000000000000000000000000000000000000000000L; // BlackRooks
    	pieceTypeBB[10] = 0b0000100000000000000000000000000000000000000000000000000000000000L; // BlackQueens
    	pieceTypeBB[11] = 0b0001000000000000000000000000000000000000000000000000000000000000L; // BlackKing
    	whiteBB = 0b0000000000000000000000000000000000000000000000001111111111111111L;
    	blackBB = 0b1111111111111111000000000000000000000000000000000000000000000000L;
    	allBB = 0b1111111111111111000000000000000000000000000000001111111111111111L;
    }

	@Override
	public char getActiveColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void switchActiveColor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiveColor(char color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public char getUnactiveColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean[] getCastling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableCastling(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PieceList getPieces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece getPieceAt(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece getPieceAt(int[] coord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePieceList() {
		// TODO Auto-generated method stub
		
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

	@Override
	public int getMoveNr() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void increaseMoveNr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInCheck(char color) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printPieceLocationList() {
		// TODO Auto-generated method stub
		
	}
	
	/** Creates a 8x8 Board from BitBoards */
	private void convertBitBoardTo8x8Board() {
		char[] pieceLabels = {'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};
		board = new char[64];
		
		/*for(int j = 0; j < 64; j++) {
			board[j] = '.';
		}*/
		
		/*Queue<Integer> squares = new LinkedList<Integer>();
		for(int j = 0; j < 64; j++) {
			squares.add(j);
			squares.remove(); // retrieve and remove
			squares.element(); // Retrieves, but does not remove, the head of this queue.
		}*/
		
		/* Squares where no piece is on */
		List<Integer> squares = new ArrayList<Integer>();
		for(int j = 0; j < 64; j++) {
			squares.add(j);
		}
		
		/* Bit-Masks for detecting pieces on squares */
		long mask = 0b0000000000000000000000000000000000000000000000000000000000000001L;
		long[] masks = new long[64];
		for(int i = 0; i < 64; i++) {
			masks[i] = mask;
			mask = mask << 1;
		}
		
		/* Detect empty squares */
		Iterator<Integer> iter = squares.iterator();
		while (iter.hasNext()) {
		    int sq = iter.next();

			if(!((allBB & masks[sq]) == masks[sq])) {
				board[sq] = '.';
				iter.remove();
			}
		}
		
		/* Detect pieces */
		int pieceIndex = 0;
		for(long bitboard : pieceTypeBB) {

			iter = squares.iterator();
			while (iter.hasNext()) {
			    int sq = iter.next();

				if((bitboard & masks[sq]) == masks[sq]) {
					board[sq] = pieceLabels[pieceIndex];
					iter.remove();
				}
			}
			pieceIndex++;
		}
	}
	
	/** Prints a human-readable 8x8 board*/
	@Override
	public String toString() {
		convertBitBoardTo8x8Board(); // create 8x8 board
		
		StringBuilder result = new StringBuilder();
		String boardstr = new String(board);
		
		/* Add newlines after each rank */
		for(int j = 0; j < 64; j += 8) {
		result.append(boardstr.substring(j, j+8));
		result.append(System.lineSeparator());
		}
		
		/*for(BitBoard bitboard : pieceTypeBB) {
			result.append(bitboard.toString() + System.lineSeparator());
		}*/
		return result.toString();
	}
}
