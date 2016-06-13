package board.position.bitboard;

import java.util.ArrayList;
import java.util.List;

import board.position.BitBoard;
import board.position.PositionBB;

public class Movement {
	static final short FILE_A = 0;
	static final short FILE_B = 1;
	static final short FILE_C = 2;
	static final short FILE_D = 3;
	static final short FILE_E = 4;
	static final short FILE_F = 5;
	static final short FILE_G = 6;
	static final short FILE_H = 7;
	
	static final short RANK_1 = 0;
	static final short RANK_2 = 1;
	static final short RANK_3 = 2;
	static final short RANK_4 = 3;
	static final short RANK_5 = 4;
	static final short RANK_6 = 5;
	static final short RANK_7 = 6;
	static final short RANK_8 = 7;
	
    public static final long maskRow1Row8  = 0xFF000000000000FFL;
	
    private final static long[][] bTables;
    private final static long[] bMasks;
	private final static int[] bBits = { 5, 4, 5, 5, 5, 5, 4, 5,
            4, 4, 5, 5, 5, 5, 4, 4,
            4, 4, 7, 7, 7, 7, 4, 4,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            4, 4, 7, 7, 7, 7, 4, 4,
            4, 4, 5, 5, 5, 5, 4, 4,
            5, 4, 5, 5, 5, 5, 4, 5 };
	
    private final static long[][] rTables;
    private final static long[] rMasks;
    private final static int[] rBits = { 12, 11, 11, 11, 11, 11, 11, 12,
                                         11, 10, 10, 11, 10, 10, 10, 11,
                                         11, 10, 10, 10, 10, 10, 10, 11,
                                         11, 10, 10, 10, 10, 10, 10, 11,
                                         11, 10, 10, 10, 10, 10, 10, 11,
                                         11, 10, 10, 11, 10, 10, 10, 11,
                                         10,  9,  9,  9,  9,  9, 10, 10,
                                         11, 10, 10, 10, 10, 11, 10, 11 };
    
    static { // Bishop magics
        bTables = new long[64][];
        bMasks = new long[64];
        for (int sq = 0; sq < 64; sq++) {
            int x = PositionBB.getX(sq);
            int y = PositionBB.getY(sq);
            bMasks[sq] = addBishopRays(x, y, 0L, true);
            int tableSize = 1 << bBits[sq];
            long[] table = new long[tableSize];
            for (int i = 0; i < tableSize; i++) table[i] = -1;
            int nPatterns = 1 << Long.bitCount(bMasks[sq]);
            for (int i = 0; i < nPatterns; i++) {
                long p = createPattern(i, bMasks[sq]);
                int entry = (int)((p * LookUpTables.bMagics[sq]) >>> (64 - bBits[sq]));
                long atks = addBishopRays(x, y, p, false);
                if (table[entry] == -1) {
                    table[entry] = atks;
                } else if (table[entry] != atks) {
                    throw new RuntimeException();
                }
            }
            bTables[sq] = table;
        }
    }
    
    static { // Rook magics
        rTables = new long[64][];
        rMasks = new long[64];
        for (int sq = 0; sq < 64; sq++) {
            int x = PositionBB.getX(sq);
            int y = PositionBB.getY(sq);
            rMasks[sq] = addRookRays(x, y, 0L, true);
            int tableSize = 1 << rBits[sq];
            long[] table = new long[tableSize];
            for (int i = 0; i < tableSize; i++) table[i] = -1;
            int nPatterns = 1 << Long.bitCount(rMasks[sq]);
            for (int i = 0; i < nPatterns; i++) {
                long p = createPattern(i, rMasks[sq]);
                int entry = (int)((p * LookUpTables.rMagics[sq]) >>> (64 - rBits[sq]));
                long atks = addRookRays(x, y, p, false);
                if (table[entry] == -1) {
                    table[entry] = atks;
                } else if (table[entry] != atks) {
                    throw new RuntimeException();
                }
            }
            rTables[sq] = table;
        }
    }
    
    private static final long createPattern(int i, long mask) {
        long ret = 0L;
        for (int j = 0; ; j++) {
            long nextMask = mask & (mask - 1);
            long bit = mask ^ nextMask;
            if ((i & (1L << j)) != 0)
                ret |= bit;
            mask = nextMask;
            if (mask == 0)
                break;
        }
        return ret;
    }
    
    private static final long addBishopRays(int x, int y, long occupied, boolean inner) {
        long mask = 0;
        mask = addRay(mask, x, y,  1,  1, occupied, inner);
        mask = addRay(mask, x, y, -1, -1, occupied, inner);
        mask = addRay(mask, x, y,  1, -1, occupied, inner);
        mask = addRay(mask, x, y, -1,  1, occupied, inner);
        return mask;
    }
    
    private static final long addRookRays(int x, int y, long occupied, boolean inner) {
        long mask = 0;
        mask = addRay(mask, x, y,  1,  0, occupied, inner);
        mask = addRay(mask, x, y, -1,  0, occupied, inner);
        mask = addRay(mask, x, y,  0,  1, occupied, inner);
        mask = addRay(mask, x, y,  0, -1, occupied, inner);
        return mask;
    }
    
    private static final long addRay(long mask, int x, int y, int dx, int dy, 
            long occupied, boolean inner) {
		int lo = inner ? 1 : 0;
		int hi = inner ? 6 : 7;
		while (true) {
		if (dx != 0) {
		x += dx; if ((x < lo) || (x > hi)) break;
		}
		if (dy != 0) {
		y += dy; if ((y < lo) || (y > hi)) break;
		}
		int sq = PositionBB.getSquare(x, y); //Position12x10.coordToIndex(new int[] {x, y}); // was getSquare(x, y) before
		mask |= 1L << sq;
		if ((occupied & (1L << sq)) != 0)
		break;
		}
		return mask;
	}
    
    public static long whitePiecesValid(PositionBB pos) {
    	return whiteKingValid(pos) | whiteKnightsValid(pos) |
    			whiteBishopsValid(pos) | whitePawnsValid(pos);
    }
    
    public static long blackPiecesValid(PositionBB pos) {
    	return blackKingValid(pos) | blackKnightsValid(pos) |
    			blackBishopsValid(pos) | blackPawnsValid(pos);
    }
	
	public static long whiteKingValid(PositionBB pos) {
		return Movement.compute_king_incomplete(pos.pieceTypeBB[PositionBB.WKING], pos.whiteBB);
	}
	
	public static long blackKingValid(PositionBB pos) {
		return Movement.compute_king_incomplete(pos.pieceTypeBB[PositionBB.BKING], pos.blackBB);
	}
	
	public static long whiteKnightsValid(PositionBB pos) {
		return compute_knight(pos.pieceTypeBB[PositionBB.WKNIGHTS], pos.whiteBB);
	}
	
	public static long blackKnightsValid(PositionBB pos) {
		return compute_knight(pos.pieceTypeBB[PositionBB.BKNIGHTS], pos.blackBB);
	}
	
	public static long whitePawnsValid(PositionBB pos) {
		return compute_white_pawns(pos.pieceTypeBB[PositionBB.WPAWNS], pos.allBB, pos.blackBB);
	}
	
	public static long blackPawnsValid(PositionBB pos) {
		return compute_black_pawns(pos.pieceTypeBB[PositionBB.BPAWNS], pos.allBB, pos.whiteBB);
	}
	
	public static long whiteBishopsValid(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.WBISHOPS];
        long m = 0x0L;
		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= bishopAttacks(sq, pos.allBB) & ~pos.whiteBB;

            /*while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                m &= (m - 1);
            }*/
            
            squares &= squares-1;
        }
		return m;
	}
	
	public static long blackBishopsValid(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.BBISHOPS];
        long m = 0x0L;
		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= bishopAttacks(sq, pos.allBB) & ~pos.blackBB;

            /*while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                m &= (m - 1);
            }*/
            
            squares &= squares-1;
        }
		return m;
	}
	
	public static List<Move> whitePawnMoves(PositionBB pos) {
		List<Move> possible = new ArrayList<Move>();
		
		// Pawn moves
        long pawns = pos.pieceTypeBB[PositionBB.WPAWNS];
        long m = (pawns << 8) & ~pos.occupied;
        if (addPawnMovesByMask(possible, pos, m, -8, true)) return possible;
        /*m = ((m & BitBoard.maskRow3) << 8) & ~occupied;
        addPawnDoubleMovesByMask(possible, pos, m, -16);*/
		
		/*long pawnAttacks = Movement.compute_white_pawns(pos.pieceTypeBB[PositionBB.WPAWNS], pos.allBB, pos.blackBB);
        long m = pawnAttacks & ~pos.whiteBB;

        
        int sq = BitBoard.numberOfTrailingZeros(pos.pieceTypeBB[PositionBB.WPAWNS]);
        addMovesByMask(possible, pos, sq, m);*/
        return possible;
	}
	
	public static List<Move> blackPawnMoves(PositionBB pos) {
		List<Move> possible = new ArrayList<Move>();
		
		// Pawn moves
        long pawns = pos.pieceTypeBB[PositionBB.BPAWNS];
        long m = (pawns >> 8) & ~pos.occupied;
        if (addPawnMovesByMask(possible, pos, m, 8, true)) return possible;
        return possible;
	}
	
	public static List<Move> whiteKingMoves(PositionBB pos) {
		long kingAttacks = Movement.compute_king_incomplete(pos.pieceTypeBB[PositionBB.WKING], pos.whiteBB);
        long m = kingAttacks & ~pos.whiteBB;
        List<Move> possible = new ArrayList<Move>();
        
        int sq = BitBoard.numberOfTrailingZeros(pos.pieceTypeBB[PositionBB.WKING]);
        
        addMovesByMask(possible, pos, sq, m);
        /*long mask = m;
        while (mask != 0) {
            int sq1 = BitBoard.numberOfTrailingZeros(mask);
            possible.add(new Move(sq, sq1));
            mask &= (mask - 1);
        }*/
        
        return possible;
	}
	
	public static List<Move> blackKingMoves(PositionBB pos) {
		long kingAttacks = Movement.compute_king_incomplete(pos.pieceTypeBB[PositionBB.BKING], pos.blackBB);
        long m = kingAttacks & ~pos.blackBB;
        List<Move> possible = new ArrayList<Move>();
        
        int sq = BitBoard.numberOfTrailingZeros(pos.pieceTypeBB[PositionBB.BKING]);
        
        addMovesByMask(possible, pos, sq, m);
        /*long mask = m;
        while (mask != 0) {
            int sq1 = BitBoard.numberOfTrailingZeros(mask);
            possible.add(new Move(sq, sq1));
            mask &= (mask - 1);
        }*/
        
        return possible;
	}
	
	public static List<Move> whiteBishopsMoves(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.WBISHOPS];
        long m = 0x0L;
        List<Move> possible = new ArrayList<Move>();
        
		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= bishopAttacks(sq, pos.allBB) & ~pos.whiteBB;

            /* add move to movelist */
            while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                //System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                possible.add(new Move(sq, sq1));
                m &= (m - 1);
            }
            
            squares &= squares-1;
        }
		return possible;
	}

	public static List<Move> blackBishopsMoves(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.BBISHOPS];
        long m = 0x0L;
        List<Move> possible = new ArrayList<Move>();
        
		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= bishopAttacks(sq, pos.allBB) & ~pos.blackBB;

            /* add move to movelist */
            while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                //System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                possible.add(new Move(sq, sq1));
                m &= (m - 1);
            }
            
            squares &= squares-1;
        }
		return possible;
	}
	
	/** Get the white rooks possible moves */
	public static List<Move> whiteRooksMoves(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.WROOKS];
        long m = 0x0L;
        List<Move> possible = new ArrayList<Move>();

		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= rookAttacks(sq, pos.allBB) & ~pos.whiteBB;
            
            /* add move to movelist */
            while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                //System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                possible.add(new Move(sq, sq1));
                m &= (m - 1);
            }
            
            squares &= squares-1;
        }
		return possible;
	}
	
	/** Get the black rooks possible moves */
	public static List<Move> blackRooksMoves(PositionBB pos) {
        long squares = pos.pieceTypeBB[PositionBB.BROOKS];
        long m = 0x0L;
        List<Move> possible = new ArrayList<Move>();

		while (squares != 0) {
            int sq = BitBoard.numberOfTrailingZeros(squares);
            m |= rookAttacks(sq, pos.allBB) & ~pos.blackBB;
            
            /* add move to movelist */
            while (m != 0) {
                int sq1 = BitBoard.numberOfTrailingZeros(m);
                //System.out.println(sq + " " + sq1);
                //setMove(moveList, sq, sq1, Piece.EMPTY);
                possible.add(new Move(sq, sq1));
                m &= (m - 1);
            }
            
            squares &= squares-1;
        }
		return possible;
	}
	
	/** LookupTables is a structure which contains all precomputed lookup tables 
	 * @author Peter Keller
	 * */
	private static long compute_king_incomplete(long king_loc, long own_side)
	{
		/* we can ignore the rank clipping since the overflow/underflow with
			respect to rank simply vanishes. We only care about the file
			overflow/underflow. */ 

		long king_clip_file_h = king_loc & LookUpTables.clearFile[FILE_H]; 
		long king_clip_file_a = king_loc & LookUpTables.clearFile[FILE_A]; 

		/* remember the representation of the board in relation to the bitindex 
			when looking at these shifts.... */
		long spot_1 = king_clip_file_h << 7; 
		long spot_2 = king_loc << 8; 
		long spot_3 = king_clip_file_h << 9; 
		long spot_4 = king_clip_file_h << 1; 

		long spot_5 = king_clip_file_a >> 7; 
		long spot_6 = king_loc >> 8; 
		long spot_7 = king_clip_file_a >> 9; 
		long spot_8 = king_clip_file_a >> 1; 

		long king_moves = spot_1 | spot_2 | spot_3 | spot_4 | spot_5 | spot_6 |
	                    	spot_7 | spot_8; 

		long KingValid = king_moves & ~own_side; 

		/* compute only the places where the king can move and attack. The caller
			will interpret this as a white or black king. */
		return KingValid;
	}
	
	/** LookupTables is a structure which contains all precomputed lookup tables 
	 * @author Peter Keller
	 * */
	private static long compute_knight(long knight_loc, long own_side)
	{
		/* we can ignore the rank clipping since the overflow/underflow with
			respect to rank simply vanishes. We only care about the file
			overflow/underflow which is much more work for a knight. */ 
		
		long spot_1_clip = LookUpTables.clearFile[FILE_A] & LookUpTables.clearFile[FILE_B];
		long spot_2_clip = LookUpTables.clearFile[FILE_A];
		long spot_3_clip = LookUpTables.clearFile[FILE_H];
		long spot_4_clip = LookUpTables.clearFile[FILE_H] & LookUpTables.clearFile[FILE_G];

		long spot_5_clip = LookUpTables.clearFile[FILE_H] & LookUpTables.clearFile[FILE_G];
		long spot_6_clip = LookUpTables.clearFile[FILE_H];
		long spot_7_clip = LookUpTables.clearFile[FILE_A];
		long spot_8_clip = LookUpTables.clearFile[FILE_A] & LookUpTables.clearFile[FILE_B];

		/* The clipping masks we just created will be used to ensure that no
			under or overflow positions are computed when calculating the
			possible moves of the knight in certain files. */

		long spot_1 = (knight_loc & spot_1_clip) << 6;
		long spot_2 = (knight_loc & spot_2_clip) << 15;
		long spot_3 = (knight_loc & spot_3_clip) << 17;
		long spot_4 = (knight_loc & spot_4_clip) << 10;

		long spot_5 = (knight_loc & spot_5_clip) >> 6;
		long spot_6 = (knight_loc & spot_6_clip) >> 15;
		long spot_7 = (knight_loc & spot_7_clip) >> 17;
		long spot_8 = (knight_loc & spot_8_clip) >> 10;

		long KnightValid = spot_1 | spot_2 | spot_3 | spot_4 | spot_5 | spot_6 |
	                    spot_7 | spot_8;

		/* compute only the places where the knight can move and attack. The
			caller will determine if this is a white or black night. */
		return KnightValid & ~own_side;
	}
	
	/** unlike the king and knight algorithms, pawns move in fundamentally
	* different ways for each color, so we need to seperate functions to
	* deal with the change in shifting and the opponents color. This is
	* the one for computing a white pawn movement. 
	* @author Peter Keller
	*/
	private static long compute_white_pawns(long white_pawn_loc, long all_pieces, 
			long all_black_pieces) {
	/* check the single space infront of the white pawn */
	long white_pawn_one_step = (white_pawn_loc << 8) & ~all_pieces; 

	/* for all moves that came from rank 2 (home row) and passed the above 
		filter, thereby being on rank 3, check and see if I can move forward 
		one more */
	long white_pawn_two_steps = 
		((white_pawn_one_step &  LookUpTables.maskRank[RANK_3]) << 8) & ~all_pieces; 

	/* the union of the movements dictate the possible moves forward 
		available */
	long white_pawn_valid_moves = white_pawn_one_step | white_pawn_two_steps;

	/* next we calculate the pawn attacks */

	/* check the left side of the pawn, minding the underflow File A */
	long white_pawn_left_attack = (white_pawn_loc & LookUpTables.clearFile[FILE_A]) << 7;

	/* then check the right side of the pawn, minding the overflow File H */
	long white_pawn_right_attack = (white_pawn_loc & LookUpTables.clearFile[FILE_H]) << 9;

	/* the union of the left and right attacks together make up all the 
		possible attacks */
	long white_pawn_attacks = white_pawn_left_attack | white_pawn_right_attack;

	/* Calculate where I can _actually_ attack something */
	long white_pawn_valid_attacks = white_pawn_attacks & all_black_pieces;

	/* then we combine the two situations in which a white pawn can legally 
		attack/move. */
	long WhitePawnValid = white_pawn_valid_moves | white_pawn_valid_attacks;

	return WhitePawnValid;
}

	private static long compute_black_pawns(long black_pawn_loc, long all_pieces, 
			long all_white_pieces) {
	/* check the single space infront of the black pawn */
	long black_pawn_one_step = (black_pawn_loc >> 8) & ~all_pieces; 

	/* for all moves that came from rank 2 (home row) and passed the above 
		filter, thereby being on rank 3, check and see if I can move forward 
		one more */
	long black_pawn_two_steps = 
		((black_pawn_one_step &  LookUpTables.maskRank[RANK_6]) >> 8) & ~all_pieces; 

	/* the union of the movements dictate the possible moves forward 
		available */
	long black_pawn_valid_moves = black_pawn_one_step | black_pawn_two_steps;

	/* next we calculate the pawn attacks */

	/* check the left side of the pawn, minding the underflow File A */
	long black_pawn_left_attack = (black_pawn_loc & LookUpTables.clearFile[FILE_A]) >> 7;

	/* then check the right side of the pawn, minding the overflow File H */
	long black_pawn_right_attack = (black_pawn_loc & LookUpTables.clearFile[FILE_H]) >> 9;

	/* the union of the left and right attacks together make up all the 
		possible attacks */
	long black_pawn_attacks = black_pawn_left_attack | black_pawn_right_attack;

	/* Calculate where I can _actually_ attack something */
	long black_pawn_valid_attacks = black_pawn_attacks & all_white_pieces;

	/* then we combine the two situations in which a white pawn can legally 
		attack/move. */
	long WhitePawnValid = black_pawn_valid_moves | black_pawn_valid_attacks;

	return WhitePawnValid;
	}

    public static final long bishopAttacks(int sq, long occupied) {
        return bTables[sq][(int)(((occupied & bMasks[sq]) * LookUpTables.bMagics[sq]) >>> (64 - bBits[sq]))];
    }

    public static final long rookAttacks(int sq, long occupied) {
        return rTables[sq][(int)(((occupied & rMasks[sq]) * LookUpTables.rMagics[sq]) >>> (64 - rBits[sq]))];
    }
    
    private final static boolean addMovesByMask(List<Move> moveList, PositionBB pos, int sq0, long mask) {
        long oKingMask = pos.pieceTypeBB[pos.whiteMove() ? PositionBB.BKING : PositionBB.WKING];
        if ((mask & oKingMask) != 0) {
            int sq = BitBoard.numberOfTrailingZeros(mask & oKingMask);
            //moveList.size = 0;
            //setMove(moveList, sq0, sq, Piece.EMPTY);
            moveList.add(new Move(sq0, sq));
            return true;
        }
        while (mask != 0) {
            int sq = BitBoard.numberOfTrailingZeros(mask);
            //setMove(moveList, sq0, sq, Piece.EMPTY);
            moveList.add(new Move(sq0, sq));
            mask &= (mask - 1);
        }
        return false;
    }
    
    private final static boolean addPawnMovesByMask(List<Move> moveList, PositionBB pos, long mask,
            int delta, boolean allPromotions) {
		if (mask == 0)
			return false;
		long oKingMask = pos.pieceTypeBB[pos.whiteMove() ? PositionBB.BKING : PositionBB.WKING];
        if ((mask & oKingMask) != 0) {
            int sq = BitBoard.numberOfTrailingZeros(mask & oKingMask);
            moveList.add(new Move(sq + delta, sq, PositionBB.EMPTY));
            return true;
        }
        long promMask = mask & maskRow1Row8;
        mask &= ~promMask;
        while (promMask != 0) {
            int sq = BitBoard.numberOfTrailingZeros(promMask);
            int sq0 = sq + delta;
            if (sq >= 56) { // White promotion
                moveList.add(new Move(sq0, sq, PositionBB.WQUEENS));
                moveList.add(new Move(sq0, sq, PositionBB.WKNIGHTS));
                if (allPromotions) {
                    moveList.add(new Move(sq0, sq, PositionBB.WROOKS));
                    moveList.add(new Move(sq0, sq, PositionBB.WBISHOPS));
                }
            } else { // Black promotion
                moveList.add(new Move(sq0, sq, PositionBB.BQUEENS));
                moveList.add(new Move(sq0, sq, PositionBB.BKNIGHTS));
                if (allPromotions) {
                    moveList.add(new Move(sq0, sq, PositionBB.BROOKS));
                    moveList.add(new Move(sq0, sq, PositionBB.BBISHOPS));
                }
            }
            promMask &= (promMask - 1);
        }
        while (mask != 0) {
            int sq = BitBoard.numberOfTrailingZeros(mask);
            moveList.add(new Move(sq + delta, sq, PositionBB.EMPTY));
            mask &= (mask - 1);
        }
        return false;
    }
}
