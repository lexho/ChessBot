package board.position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import board.pieces.Piece;
import board.pieces.PieceList;
import board.position.bitboard.LookUpTables;
import board.Move;
import board.position.bitboard.Movement;

public class PositionBB implements PositionInterface {
	
    // Bitboards
    //public long[] pieceTypeBB;
	//public long whiteBB, blackBB;
    public long[] pieceTypeBB;
    public long whiteBB, blackBB, allBB;
    public long occupied; // will be dynamically created
    
    int wMtrl;
    int bMtrl;
    
    public boolean whiteMove;
    
    /** Bit definitions for the castleMask bit mask. */
    public static final int A1_CASTLE = 0; /** White long castle. */
    public static final int H1_CASTLE = 1; /** White short castle. */
    public static final int A8_CASTLE = 2; /** Black long castle. */
    public static final int H8_CASTLE = 3; /** Black short castle. */
    
    private int castleMask;

    private int epSquare;
    
    /** Number of half-moves since last 50-move reset. */
    int halfMoveClock;
    
    /** Game move number, starting from 1. */
    public int fullMoveCounter;
    
    char[] board = new char[64]; // 8x8 printable board
    int[] squares = {Piece.WROOK, Piece.WKNIGHT, Piece.WBISHOP, Piece.WQUEEN, Piece.WKING, Piece.WBISHOP, Piece.WKNIGHT, Piece.WROOK, 
    				 Piece.WPAWN, Piece.WPAWN, Piece.WPAWN, Piece.WPAWN, Piece.WPAWN, Piece.WPAWN, Piece.WPAWN, Piece.WPAWN,	   
    				 Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
    			     Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
    			     Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
    			     Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
    			     Piece.BPAWN, Piece.BPAWN, Piece.BPAWN, Piece.BPAWN, Piece.BPAWN, Piece.BPAWN, Piece.BPAWN, Piece.BPAWN,
    			     Piece.BROOK, Piece.BKNIGHT, Piece.BBISHOP, Piece.BQUEEN, Piece.BKING, Piece.BBISHOP, Piece.BKNIGHT, Piece.BROOK};
    //new int[64];
    
    /** Initialize board to empty position. */
    public PositionBB() {
    	pieceTypeBB = new long[Piece.nPieceTypes];
    	
    	/* Initialize Bitboards */
    	for (int i = 0; i < Piece.nPieceTypes; i++) {
             pieceTypeBB[i] = 0L;
        }
    	whiteBB = blackBB = 0L;
    	createStartpos();
    }
    
    public PositionBB(PositionBB pos) {
    	pieceTypeBB = new long[Piece.nPieceTypes];
    	for(int i = 0; i < Piece.nPieceTypes; i++) {
    		pieceTypeBB[i] = pos.pieceTypeBB[i];
    	}
    	
    	this.whiteMove = pos.whiteMove;
    	
    	for(int i = 0; i < this.squares.length; i++) {
    		this.squares[i] = pos.squares[i];
    	}
    	
    	this.wMtrl = pos.wMtrl;
    	this.bMtrl = pos.bMtrl;
    	
    }
    
    public PositionBB(Fen fen) {
		// TODO implement PositionBB byFen-constructor
	}

	public void updateBitBoards() {
    	createWhiteBB();
    	createBlackBB();
    	createAllPiecesBB();
    }
    
    public void createStartpos() {
        
    	/* WhitePawns, WhiteKnights, WhiteBishops, WhiteRooks, WhiteQueens, and WhiteKing, BlackPawns, BlackKnights, BlackBishops, BlackRooks, BlackQueens and BlackKing. */
    	
        /* Initial position */
    	pieceTypeBB[1] = 0b0000000000000000000000000000000000000000000000001111111100000000L; // WhitePawns
    	pieceTypeBB[2] = 0b0000000000000000000000000000000000000000000000000000000001000010L; // WhiteKnights
    	pieceTypeBB[3] = 0b0000000000000000000000000000000000000000000000000000000000100100L; // WhiteBishops
    	pieceTypeBB[4] = 0b0000000000000000000000000000000000000000000000000000000010000001L; // WhiteRooks
    	pieceTypeBB[5] = 0b0000000000000000000000000000000000000000000000000000000000001000L; // WhiteQueens
    	pieceTypeBB[6] = 0b0000000000000000000000000000000000000000000000000000000000010000L; // WhiteKing
    	
    	pieceTypeBB[7] = 0b0000000011111111000000000000000000000000000000000000000000000000L; // BlackPawns
    	pieceTypeBB[8] = 0b0100001000000000000000000000000000000000000000000000000000000000L; // BlackKnights
    	pieceTypeBB[9] = 0b0010010000000000000000000000000000000000000000000000000000000000L; // BlackBishops
    	pieceTypeBB[10] = 0b1000000100000000000000000000000000000000000000000000000000000000L; // BlackRooks
    	pieceTypeBB[11] = 0b0000100000000000000000000000000000000000000000000000000000000000L; // BlackQueens
    	pieceTypeBB[12] = 0b0001000000000000000000000000000000000000000000000000000000000000L; // BlackKing
    	whiteBB = 0b0000000000000000000000000000000000000000000000001111111111111111L;
    	blackBB = 0b1111111111111111000000000000000000000000000000000000000000000000L;
    	allBB = 0b1111111111111111000000000000000000000000000000001111111111111111L;
    	whiteMove = true;
    	wMtrl = bMtrl = 8 * Piece.PAWN_V + 2 * Piece.ROOK_V + 2 * Piece.BISHOP_V + Piece.KNIGHT_V + Piece.QUEEN_V + Piece.KING_V;
    }
    
    private void createAllPiecesBB() {
    	allBB = whiteBB | blackBB;
    }
    
    private void createWhiteBB() {
    	whiteBB = 0L; // init white pieces bitboard with zeros
    	for(int i = 1; i < 7; i++) {
    		whiteBB |= pieceTypeBB[i];
    	}
    }
    
    private void createBlackBB() {
    	blackBB = 0L; // init white pieces bitboard with zeros
    	for(int i = 7; i < 13; i++) {
    		blackBB |= pieceTypeBB[i];
    	}
    }
    
    /** Apply a move to the current position. */
    public final void makeMove(Move move, UndoInfo ui) {
        ui.capturedPiece = squares[move.getTarget8x8Index()];
        ui.castleMask = castleMask;
        ui.epSquare = epSquare;
        ui.halfMoveClock = halfMoveClock;
        boolean wtm = whiteMove;
        
        final int p = squares[move.getSource8x8Index()];
        int capP = squares[move.getTarget8x8Index()];
        long fromMask = 1L << move.getSource8x8Index();

        int prevEpSquare = epSquare;
        setEpSquare(-1);

        if ((capP != Piece.EMPTY) || (((pieceTypeBB[Piece.WPAWN] | pieceTypeBB[Piece.BPAWN]) & fromMask) != 0)) {
            halfMoveClock = 0;

            // Handle en passant and epSquare
            if (p == Piece.WPAWN) {
                if (move.getTarget8x8Index() - move.getSource8x8Index() == 2 * 8) {
                    int x = getX(move.getTarget8x8Index());
                    if (    ((x > 0) && (squares[move.getTarget8x8Index() - 1] == Piece.BPAWN)) ||
                            ((x < 7) && (squares[move.getTarget8x8Index() + 1] == Piece.BPAWN))) {
                        setEpSquare(move.getSource8x8Index() + 8);
                    }
                } else if (move.getTarget8x8Index() == prevEpSquare) {
                    setPiece(move.getTarget8x8Index() - 8, Piece.EMPTY);
                }
            } else if (p == Piece.BPAWN) {
                if (move.getTarget8x8Index() - move.getSource8x8Index() == -2 * 8) {
                    int x = getX(move.getTarget8x8Index());
                    if (    ((x > 0) && (squares[move.getTarget8x8Index() - 1] == Piece.WPAWN)) ||
                            ((x < 7) && (squares[move.getTarget8x8Index() + 1] == Piece.WPAWN))) {
                        setEpSquare(move.getSource8x8Index() - 8);
                    }
                } else if (move.getTarget8x8Index() == prevEpSquare) {
                    setPiece(move.getTarget8x8Index() + 8, Piece.EMPTY);
                }
            }

            if (((pieceTypeBB[Piece.WKING] | pieceTypeBB[Piece.BKING]) & fromMask) != 0) {
                if (wtm) {
                    setCastleMask(castleMask & ~(1 << A1_CASTLE));
                    setCastleMask(castleMask & ~(1 << H1_CASTLE));
                } else {
                    setCastleMask(castleMask & ~(1 << A8_CASTLE));
                    setCastleMask(castleMask & ~(1 << H8_CASTLE));
                }
            }

            // Perform move
            setPiece(move.getSource8x8Index(), Piece.EMPTY);
            // Handle promotion
            if (move.getPromoteTo() != Piece.EMPTY) {
                setPiece(move.getTarget8x8Index(), move.getPromoteTo());
            } else {
                setPiece(move.getTarget8x8Index(), p);
            }
        } else {
            halfMoveClock++;

            // Handle castling
            if (((pieceTypeBB[Piece.WKING] | pieceTypeBB[Piece.BKING]) & fromMask) != 0) {
                int k0 = move.getSource8x8Index();
                if (move.getTarget8x8Index() == k0 + 2) { // O-O
                    movePieceNotPawn(k0 + 3, k0 + 1);
                } else if (move.getTarget8x8Index() == k0 - 2) { // O-O-O
                    movePieceNotPawn(k0 - 4, k0 - 1);
                }
                if (wtm) {
                    setCastleMask(castleMask & ~(1 << A1_CASTLE));
                    setCastleMask(castleMask & ~(1 << H1_CASTLE));
                } else {
                    setCastleMask(castleMask & ~(1 << A8_CASTLE));
                    setCastleMask(castleMask & ~(1 << H8_CASTLE));
                }
            }

            // Perform move
            movePieceNotPawn(move.getSource8x8Index(), move.getTarget8x8Index());
        }
        if (wtm) {
            // Update castling rights when rook moves
            if ((LookUpTables.maskCorners & fromMask) != 0) {
                if (p == Piece.WROOK)
                    removeCastleRights(move.getSource8x8Index());
            }
            if ((LookUpTables.maskCorners & (1L << move.getTarget8x8Index())) != 0) {
                if (capP == Piece.BROOK)
                    removeCastleRights(move.getTarget8x8Index());
            }
        } else {
            fullMoveCounter++;
            // Update castling rights when rook moves
            if ((LookUpTables.maskCorners & fromMask) != 0) {
                if (p == Piece.BROOK)
                    removeCastleRights(move.getSource8x8Index());
            }
            if ((LookUpTables.maskCorners & (1L << move.getTarget8x8Index())) != 0) {
                if (capP == Piece.WROOK)
                    removeCastleRights(move.getTarget8x8Index());
            }
        }

        //hashKey ^= whiteHashKey;
        whiteMove = !wtm;
    }
    
    public final void unMakeMove(Move move, UndoInfo ui) {
        //hashKey ^= whiteHashKey;
        whiteMove = !whiteMove;
        int p = squares[move.getTarget8x8Index()];
        setPiece(move.getSource8x8Index(), p);
        setPiece(move.getTarget8x8Index(), ui.capturedPiece);
        setCastleMask(ui.castleMask);
        setEpSquare(ui.epSquare);
        halfMoveClock = ui.halfMoveClock;
        boolean wtm = whiteMove;
        if (move.getPromoteTo() != Piece.EMPTY) {
            p = wtm ? Piece.WPAWN : Piece.BPAWN;
            setPiece(move.getSource8x8Index(), p);
        }
        if (!wtm) {
            fullMoveCounter--;
        }
        
        // Handle castling
        int king = wtm ? Piece.WKING : Piece.BKING;
        if (p == king) {
            int k0 = move.getSource8x8Index();
            if (move.getTarget8x8Index() == k0 + 2) { // O-O
                movePieceNotPawn(k0 + 1, k0 + 3);
            } else if (move.getTarget8x8Index() == k0 - 2) { // O-O-O
                movePieceNotPawn(k0 - 1, k0 - 4);
            }
        }

        // Handle en passant
        if (move.getTarget8x8Index() == epSquare) {
            if (p == Piece.WPAWN) {
                setPiece(move.getTarget8x8Index() - 8, Piece.BPAWN);
            } else if (p == Piece.BPAWN) {
                setPiece(move.getTarget8x8Index() + 8, Piece.WPAWN);
            }
        }
    }
    
    public List<board.Move> getPossibleMoves() {
    	/* Update Bitboards */
		createWhiteBB();
		createBlackBB();
		createAllPiecesBB();
    	occupied = whiteBB | blackBB;
    	
    	List<board.Move> possible = new ArrayList<board.Move>();
    	
    	if(whiteMove) {
	    	possible.addAll(Movement.whiteRooksMoves(this));
	    	possible.addAll(Movement.whiteKingMoves(this));
	    	possible.addAll(Movement.whitePawnMoves(this));
	    	possible.addAll(Movement.whiteBishopsMoves(this));
    	} else {
	    	possible.addAll(Movement.blackRooksMoves(this));
	    	possible.addAll(Movement.blackKingMoves(this));
	    	possible.addAll(Movement.blackPawnMoves(this));
	    	possible.addAll(Movement.blackBishopsMoves(this));
    	}

    	//TODO implement missing methods getKingSq(),...
    	//Movement.removeIllegal(this, possible); // remove illegal moves (check situations)
    	return possible;
    }

	public int getWhiteMaterial() {
		return wMtrl;
	}
	
	public int getBlackMaterial() {
		return bMtrl;
	}
	
	public boolean whiteMove() {
		return whiteMove;
	}
	
	public void setWhiteMove(boolean whiteMove) {
		this.whiteMove = whiteMove;
	}
	
    public final void setCastleMask(int castleMask) {
        //hashKey ^= castleHashKeys[this.castleMask];
        //hashKey ^= castleHashKeys[castleMask];
        this.castleMask = castleMask;
    }
    
    public final void setEpSquare(int epSquare) {
        if (this.epSquare != epSquare) {
            //hashKey ^= epHashKeys[(this.epSquare >= 0) ? getX(this.epSquare) + 1 : 0];
            //hashKey ^= epHashKeys[(epSquare >= 0) ? getX(epSquare) + 1 : 0];
            this.epSquare = epSquare;
        }
    }
	
    /** Return x position (file) corresponding to a square. */
    public final static int getX(int square) {
        return square & 7;
    }
    /** Return y position (rank) corresponding to a square. */
    public final static int getY(int square) {
        return square >> 3;
    }
    
    public final static int getSquare(int x, int y) {
        return y * 8 + x;
    }
    
    public int getKingSq(boolean isWhite) {
    	//TODO implement getKingSq()
    	return -1;
    }
    
    public int getEpSquare() {
    	//TODO implement getEpSquare()
    	return -1;
    }
    
    public int[] getSquares() {
    	return squares;
    }
	
	public final int getPieceAt(int src) {
		return squares[src];
	}

	/** Set a square to a piece value. */
	public final void setPiece(int square, int piece) {
    	//System.out.println("square: " + square);
    	//System.out.println("10x12: " + squareTo10x12(square));
    	/*System.out.println("piece: " + board_12x10[squareTo10x12(square)]);
    	System.out.println("BBIndex: " + charPieceToPieceType(board_12x10[squareTo10x12(square)]));
        */
    	System.out.println("piece: " + piece);
        
    	int removedPiece = squares[square];
    	if (removedPiece != Piece.EMPTY) {
            if (Piece.isWhite(removedPiece)) {
            	wMtrl -= Piece.getValue(removedPiece);
            } else {
            	bMtrl -= Piece.getValue(removedPiece); 	
            }
        }
    	squares[square] = piece;


        // Update bitboards
        final long sqMask = 1L << square;
        pieceTypeBB[removedPiece] &= ~sqMask;
        pieceTypeBB[piece] |= sqMask;

        if (removedPiece != Piece.EMPTY) {
            if (Piece.isWhite(removedPiece)) {
                whiteBB &= ~sqMask;
            } else {
                blackBB &= ~sqMask;
            }
        }

        if (piece != Piece.EMPTY) {
            if (Piece.isWhite(piece)) {
                whiteBB |= sqMask;
                System.out.println("white bitboard: ");
                System.out.println(BitBoard.toString(whiteBB));
            } else {
                blackBB |= sqMask;
                System.out.println("black bitboard: ");
                System.out.println(BitBoard.toString(blackBB));
            }
        }
    }

	
	public final void movePieceNotPawn(int from, int to) {
		final int piece = squares[from];
		squares[from] = Piece.EMPTY;
		squares[to] = piece;
		//final int piece = BitBoardUtils.charPieceToPieceType(this.getPieceAt(from).getCharRep());
		
		//System.out.println("piece to move: " + piece);
		
	    final long sqMaskF = 1L << from;
	    final long sqMaskT = 1L << to;
	    pieceTypeBB[piece] &= ~sqMaskF;
	    pieceTypeBB[piece] |= sqMaskT;
	    if (Piece.isWhite(piece)) {
	        whiteBB &= ~sqMaskF;
	        whiteBB |= sqMaskT;
	        /*if (piece == WKING)
	            wKingSq = to;*/
	    } else {
	        blackBB &= ~sqMaskF;
	        blackBB |= sqMaskT;
	        /*if (piece == BKING)
	            bKingSq = to;*/
	    }
	}
	
	@Override
	public void switchActiveColor() {
		whiteMove = !whiteMove;
	}
	
	@Override
	@Deprecated
	public char getActiveColor() {
		if(whiteMove) return 'w';
		else return 'b';
	}
	
	@Override
	@Deprecated
	public char getUnactiveColor() {
		if(!whiteMove) return 'w';
		else return 'b';
	}

	@Override
	@Deprecated
	public void setActiveColor(char color) {
		whiteMove = color == 'w' ? true : false;
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
	
	private final void removeCastleRights(int square) {
        if (square == getSquare(0, 0)) {
            setCastleMask(castleMask & ~(1 << A1_CASTLE));
        } else if (square == getSquare(7, 0)) {
            setCastleMask(castleMask & ~(1 << H1_CASTLE));
        } else if (square == getSquare(0, 7)) {
            setCastleMask(castleMask & ~(1 << A8_CASTLE));
        } else if (square == getSquare(7, 7)) {
            setCastleMask(castleMask & ~(1 << H8_CASTLE));
        }
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
	
	public void clear(int index) {
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
		// TODO implement isInCheck()
		return false;
	}

	@Override
	public boolean isInCheck(char color) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/** Creates a 8x8 Board from BitBoards */
	private void convertBitBoardTo8x8Board() {
		char[] pieceLabels = {'.', 'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};
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
				board[63 - sq] = '.';
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
					board[63 - sq] = pieceLabels[pieceIndex];
					iter.remove();
				}
			}
			pieceIndex++;
		}
	}

	@Override
	public void printPieceLocationList() {
		// TODO Auto-generated method stub
		
	}
	
	/** Prints a human-readable 8x8 board*/
	@Override
	public String toString() {
		//System.out.println("Bitboards: ");
		System.out.println(getActiveColor() + "Bitboards: ");
		updateBitBoards();
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
