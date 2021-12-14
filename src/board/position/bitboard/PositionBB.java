package board.position.bitboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import board.pieces.Piece;
import board.pieces.PieceCreator;
import board.pieces.PieceList;
import board.position.Fen;
import board.position.Position12x10;
import board.position.Position;
import engine.ChessBot;
import board.Board;
import board.Move;
import util.BitBoardUtils;
import util.StringUtils;

public class PositionBB implements Position {
	
    // Bitboards
    //public long[] pieceTypeBB;
	//public long whiteBB, blackBB;
    public long[] pieceTypeBB;
    public long whiteBB, blackBB, allBB;
    public long occupied; // will be dynamically created
    
    int wMtrl = 0;
    int bMtrl = 0;
    
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
    
    /** Initialize board to empty position. */
    public PositionBB(boolean empty) {
    	pieceTypeBB = new long[Piece.nPieceTypes];
    	
    	/* Initialize Bitboards */
    	for (int i = 0; i < Piece.nPieceTypes; i++) {
             pieceTypeBB[i] = 0L;
        }
    	whiteBB = blackBB = 0L;
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
    
    public PositionBB(Position position) {
    	PositionBB pos = position.getPositionBB();
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
    
    public PositionBB(String fen) {
    	this(new Fen(fen));
    }
    
    public PositionBB(Fen fen) {
    	System.out.println("active color: " + fen.getActiveColor());
    	whiteMove = fen.getActiveColor() == 'w' ? true : false;
    	
    	pieceTypeBB = new long[Piece.nPieceTypes];
    	
    	/* Initialize Bitboards */
    	for (int i = 0; i < Piece.nPieceTypes; i++) {
             pieceTypeBB[i] = 0L;
        }
    	whiteBB = blackBB = 0L;
    	
    	squares = new int[64];
    	
		// rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR
		String piecestr = fen.getPiecePlacement();
		List<String> rows = StringUtils.splitString(piecestr, '/');

		//pieces = new PieceList(this);
    	wMtrl = 0; bMtrl = 0; // reset material
		for(int y = 7; y >= 0; y--) {
			String row = rows.get(7 - y);
			for(int x = 0, i = 0; x < 8; x++, i++) {
				if(i == row.length()) break;
				char p = row.charAt(i);
				if(Character.isDigit(p)) {
					x += Character.getNumericValue(p - 1); 
					continue;
				}
				int square = PositionBB.getSquare(x, y);
				int piece = BitBoardUtils.charPieceToPieceType(p);
				//System.out.println("set " + piece + " to " + x + " / " + y);
				//System.out.println("set " + p + " to " + x + " / " + y);
				setPiece(square, piece);
			}
		}
	}

    public void updateMaterial() {
    	wMtrl = 0; bMtrl = 0; // reset material values
    	for (int piece : squares ) {
			if(piece < 7) {
				//System.out.println(piece + " " + Piece.getValue(piece) + "--> w");
				wMtrl += Piece.getValue(piece);
			} else {
				//System.out.println(piece + " " + Piece.getValue(piece) + "--> b");
				bMtrl += Piece.getValue(piece);
			}
		}
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
    	wMtrl = bMtrl = 8 * Piece.PAWN_V + 2 * Piece.ROOK_V + 2 * Piece.BISHOP_V + 2 * Piece.KNIGHT_V + Piece.QUEEN_V + Piece.KING_V;
    }
    
    /**
     * Flip a bitboard vertically about the centre ranks.
     * Rank 1 is mapped to rank 8 and vice versa.
     * @param x any bitboard
     * @return bitboard x flipped vertically
     */
    private long flipVertical(long x) {
        return  ( (x << 56)                           ) |
                ( (x << 40) & 0x00ff000000000000L ) |
                ( (x << 24) & 0x0000ff0000000000L ) |
                ( (x <<  8) & 0x000000ff00000000L ) |
                ( (x >>>  8) & 0x00000000ff000000L ) |
                ( (x >>> 24) & 0x0000000000ff0000L ) |
                ( (x >>> 40) & 0x000000000000ff00L ) |
                ( (x >>> 56) );
    }
    
    public void flip() {
    	for(int i = 0; i < pieceTypeBB.length; i++) {
    		pieceTypeBB[i] = flipVertical(pieceTypeBB[i]);
    	}
    	//updateBitBoards();
    }
    
    public void swapPieceColors() {
    	for(int i = 1; i < 7; i++) {
    		long swap = pieceTypeBB[i];
    		pieceTypeBB[i] = pieceTypeBB[i + 6];
    		pieceTypeBB[i + 6] = swap;
    	}
    	switchActiveColor();
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
    
    private void createAllPiecesBB() {
    	allBB = whiteBB | blackBB;
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
		/*createWhiteBB();
		createBlackBB();
		createAllPiecesBB();
    	occupied = whiteBB | blackBB;
    	
    	List<board.Move> possible = new ArrayList<board.Move>();
    	
    	if(whiteMove) {
	    	possible.addAll(Movement.whiteRooksMoves(this));
	    	possible.addAll(Movement.whiteKingMoves(this));
	    	possible.addAll(Movement.whitePawnMoves(this));
	    	possible.addAll(Movement.whiteBishopsMoves(this));
	    	//TODO add missing moves
    	} else {
	    	possible.addAll(Movement.blackRooksMoves(this));
	    	possible.addAll(Movement.blackKingMoves(this));
	    	possible.addAll(Movement.blackPawnMoves(this));
	    	possible.addAll(Movement.blackBishopsMoves(this));
    	}*/

    	List<board.Move> possible = getPseudoLegalMoves();
    	possible = Movement.removeIllegal(this, possible); // remove illegal moves (check situations)
    	return possible;
    }
    
	/** ExecutorService for multi-threading move generator in getPossibleMoves() */
	//private ExecutorService service;
    
    public List<board.Move> getPseudoLegalMoves() {
    	/* Update Bitboards */
		createWhiteBB();
		createBlackBB();
		createAllPiecesBB();
    	occupied = whiteBB | blackBB;
    	
		//printBitBoards();
    	
    	List<board.Move> possible = new ArrayList<board.Move>();
    	
    	List<Future<List<Move>>> futures = new ArrayList<Future<List<Move>>>();
    	//service = Executors.newFixedThreadPool(ChessBot.NR_OF_THREADS);
    	//ExecutorService service = ChessBot.service;
    	ExecutorService executor = Executors.newFixedThreadPool(4);
    	final PositionBB pos = this;
    	
    	if(whiteMove) {
    		
	        /*Callable<List<Move>> whiteRooksMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whiteRooksMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> whiteKingsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whiteKingMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> whitePawnMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whitePawnMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> whiteBishopsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whiteBishopsMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> whiteQueenMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whiteQueenMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> whiteKnightsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.whiteKnightsMoves(pos);
	            }
	        };
	        
	        futures.add(executor.submit(whiteRooksMoves));
	        futures.add(executor.submit(whiteKingsMoves));
	        futures.add(executor.submit(whitePawnMoves));
	        futures.add(executor.submit(whiteBishopsMoves));
	        futures.add(executor.submit(whiteQueenMoves));
	        futures.add(executor.submit(whiteKnightsMoves));
	        
	        executor.shutdown();

	        try {
				executor.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	        
	    	possible.addAll(Movement.whiteRooksMoves(pos.pieceTypeBB[Piece.WROOK], pos.allBB, pos.whiteBB));
	    	possible.addAll(Movement.whiteKingMoves(this));
	    	possible.addAll(Movement.whitePawnMoves(this, pos.pieceTypeBB[Piece.WPAWN], pos.occupied, pos.blackBB));
	    	possible.addAll(Movement.whiteBishopsMoves(pos.pieceTypeBB[Piece.WBISHOP], pos.allBB, pos.whiteBB));
	    	possible.addAll(Movement.whiteQueenMoves(pos.pieceTypeBB[Piece.WQUEEN], pos.allBB, pos.whiteBB));
	    	possible.addAll(Movement.whiteKnightsMoves(pos.pieceTypeBB[Piece.WKNIGHT], pos.whiteBB));

    	} else {
	        /*Callable<List<Move>> blackRooksMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackRooksMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> blackKingsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackKingMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> blackPawnMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackPawnMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> blackBishopsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackBishopsMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> blackQueenMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackQueenMoves(pos);
	            }
	        };
	        
	        Callable<List<Move>> blackKnightsMoves = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	 return Movement.blackKnightsMoves(pos);
	            }
	        };
	        
	        futures.add(executor.submit(blackRooksMoves));
	        futures.add(executor.submit(blackKingsMoves));
	        futures.add(executor.submit(blackPawnMoves));
	        futures.add(executor.submit(blackBishopsMoves));
	        futures.add(executor.submit(blackQueenMoves));
	        futures.add(executor.submit(blackKnightsMoves));
	        
	        executor.shutdown();*/
	        
	    	possible.addAll(Movement.blackRooksMoves(pos.pieceTypeBB[Piece.BROOK], pos.allBB, pos.blackBB));
	    	possible.addAll(Movement.blackKingMoves(this));
	    	possible.addAll(Movement.blackPawnMoves(this, pos.pieceTypeBB[Piece.BPAWN], pos.occupied, pos.whiteBB));
	    	possible.addAll(Movement.blackBishopsMoves(pos.pieceTypeBB[Piece.BBISHOP], pos.allBB, pos.blackBB));
	    	possible.addAll(Movement.blackQueenMoves(pos.pieceTypeBB[Piece.BQUEEN], pos.allBB, pos.blackBB));
	    	possible.addAll(Movement.blackKnightsMoves(pos.pieceTypeBB[Piece.BKNIGHT], pos.blackBB));
    	}
    	
	    /*for (Future<List<Move>> future : futures) {
	        try {
				possible.addAll(future.get());
			} catch (InterruptedException e) {
				//e.printStackTrace();
				// Interrupt search and return the moves found yet 
				executor.shutdown();
				return possible;
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }*/
	    
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
	
    /** Return x position (file) corresponding to a square. */
    public final static int getX(int square) {
        return square & 7;
    }
    /** Return y position (rank) corresponding to a square. */
    public final static int getY(int square) {
        return square >>> 3;
    }
    
    public final static int getSquare(int x, int y) {
        return y * 8 + x;
    }
    
    /** Get the square index with the king on */
    public int getKingSq(boolean isWhite) {
    	if(isWhite)
	    	for(int i = 0; i < squares.length; i++) {
	    		if(squares[i] == Piece.WKING) return i;
	    	}
    	else
	    	for(int i = 0; i < squares.length; i++) {
	    		if(squares[i] == Piece.BKING) return i;
	    	}
    	return -1;
    }
    
    /** En passant square, or -1 if no ep possible. */
    public final int getEpSquare() {
        return epSquare;
    }
    
    public final void setEpSquare(int epSquare) {
        if (this.epSquare != epSquare) {
            //hashKey ^= epHashKeys[(this.epSquare >= 0) ? getX(this.epSquare) + 1 : 0];
            //hashKey ^= epHashKeys[(epSquare >= 0) ? getX(epSquare) + 1 : 0];
            this.epSquare = epSquare;
        }
    }
    
    public int[] getSquares() {
    	return squares;
    }
    
    public void printSquares() {
		for(int sq : getSquares()) {
			System.out.print(sq + " ");
		}
		System.out.println();
    }
    
	public int getPieceAt(int src) {
		return squares[src];
	}

	/** Set a square to a piece value. */
	public final void setPiece(int square, int piece) {
    	//System.out.println("square: " + square);
    	//System.out.println("10x12: " + squareTo10x12(square));
    	/*System.out.println("piece: " + board_12x10[squareTo10x12(square)]);
    	System.out.println("BBIndex: " + charPieceToPieceType(board_12x10[squareTo10x12(square)]));
        */
        
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
            	wMtrl += Piece.getValue(piece);
            }
            else {
            	blackBB |= sqMask;
            	bMtrl += Piece.getValue(piece);
            }
        }
    }

	
	private final void movePieceNotPawn(int from, int to) {
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
	
	public void switchActiveColor() {
		whiteMove = !whiteMove;
	}
	
	@Override
	@Deprecated
	public char getActiveColor() {
		if(whiteMove) return 'w';
		else return 'b';
	}
	
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

	public boolean isInCheck() {
		return isInCheck(whiteMove);
	}
	
	@Deprecated
	public boolean isInCheck(char color) {
		return color == 'w' ? isInCheck(true) : isInCheck(false);
	}

	public boolean isInCheck(boolean isWhite) {
		PositionBB temp = new PositionBB(this);
		int king;

		temp.setWhiteMove(!isWhite);
		try {
		king = temp.getKingSq(isWhite);
		} catch (IndexOutOfBoundsException e) {
			return true; //TODO handle no king error
		}

		//System.out.println("possible moves: " + temp.getPossibleMoves());
		
		/* Is any of the opponent's pieces threatening the king? */
		for(Move m : temp.getPseudoLegalMoves()) { 
			//System.out.println(m + " " + m.getTarget8x8Index() + " =? " + king);
			if(m.getTarget8x8Index() == king) {
				//System.out.println(m + " is threatening the king");
				return true;
			}
		}

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
	
	public void printSingleBB(int p) {
		long bitboard = pieceTypeBB[p];
		char[] pieceLabels = {'.', 'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};
		
		System.out.println(BitBoardUtils.bitboardToString(bitboard, pieceLabels[p]));
		
	}
	
	public void printBitBoards() {
		System.out.println("all");
		System.out.println(BitBoardUtils.bitboardToString(allBB, 'x'));
		System.out.println("white");
		System.out.println(BitBoardUtils.bitboardToString(whiteBB, 'w'));
		System.out.println("black");
		System.out.println(BitBoardUtils.bitboardToString(blackBB, 'b'));
		for(int p = 0; p < 13; p++) {
			printSingleBB(p);
		}
	}
	
	/** Prints a human-readable 8x8 board*/
	@Override
	public String toString() {
		//System.out.println("Bitboards: ");
		String inCheck = isInCheck() ? "ch" : "";
		System.out.println(getActiveColor() + " " + inCheck);
		updateBitBoards();
		pieceTypeBB[0] = 0L; //TODO empty square bitboard is not up to date
		convertBitBoardTo8x8Board(); // create 8x8 board
		
		StringBuilder result = new StringBuilder();
		String boardstr = new String(board);
		
		/* Add newlines after each rank */
		for(int j = 64 - 8; j >= 0; j -= 8) {
			result.append(boardstr.substring(j, j + 8));
			result.append(System.lineSeparator());
		}
		//result.append("ABCDEFGH");
		
		/* Squares */
		/*for(int j = 63; j >= 0; j -= 8) {
			for(int i = j; i > j - 8; i--) {
				result.append(squares[i] + " ");
			}
		result.append(System.lineSeparator());
		}*/
		
		//System.out.println(BitBoard.toString(pieceTypeBB[9]));
		
		/*for(BitBoard bitboard : pieceTypeBB) {
			result.append(bitboard.toString() + System.lineSeparator());
		}*/
		return result.toString();
	}

	@Override
	public Position copy() {
		return new PositionBB(this);
	}

	@Override
	public boolean makeMove(Move m) {
		UndoInfo ui = new UndoInfo();
		this.makeMove(m, ui);
		return false;
	}

	@Override
	public PositionBB getPositionBB() {
		return this;
	}

	@Override
	public boolean isRunning() {
		long validMoves;
		if(whiteMove()) 
			validMoves = Movement.whitePiecesValid(this);
		else 
			validMoves = Movement.blackPiecesValid(this);
		
		if(validMoves == 0L) return false;
		else return true;
	}

	@Override
	public int getMoveNr() {
		return halfMoveClock;
	}
}
