package search.evalfunctions;

import java.util.function.Function;

import board.Board;
import board.Position12x10;
import board.pieces.Piece;
import board.pieces.PieceList;
import search.endconditions.EndCondition;

/**
 * This is how a scoring function could look like. It's not a very sophisticated
 * scoring function, but it shows a few basic principles. Remember that this
 * function will only get called for leaf nodes:
 * 
 * - if the search encounters a node in which the game is over, this function
 * gets called.
 * 
 * - if the search reaches its depth-limit, this function gets
 * called!
 * @param <V>
 */
public class ScoreBoard<V> implements Function<Board, Double>
{
	private char player_id;
	private char opponent_id;
	private Board start;
	public int scoreCounter;
	private Position12x10 position;

	/**
	 * This particular scoring function will get constructed each time a search is run.
	 * It gets passed the starting state of the board, so it can judge wether a sequence
	 * of moves during the search has led to a better state!
	 * @param start
	 * @param player_id
	 * @param opponent_id
	 */
	public ScoreBoard(Board start)
	{
		this.start = start;
		this.player_id = start.getColor();
		this.opponent_id = start.getOpponentsColor();
		this.scoreCounter = 0;
	}

	@Override
	public Double apply(Board board)
	{
		scoreCounter++;
		this.position = (Position12x10) board.getPosition();
		// nobody won or lost so far
		if (board.isRunning())
		  {
			double score = 0d;

			/*score += scoreFountains(board);
			score += getFountainBalance(board);
			score += scoreClouds(board);
			score -= getDistanceToFountain(board);
			score += scoreSeeds(board);
			score -= distanceToOpponent(board); */
			
			//score += getPieceSize(board);
			score += scoreMaterial(board);
			//score += getPieceDifference(board);
			score += possibleQueenMoves(board);
			score += possibleRookMoves(board);
			score += possibleBishopMoves(board);
			score += possibleKnightMoves(board);
			//System.out.println(score);
			
			return score;
		  } else
		{
			// Who is checkmate?
			if(board.getPosition().isInCheck(opponent_id)) {
				return 10000d;
			} else if (board.getPosition().isInCheck(player_id)) {
				return -10000d;
			} else {
				return 0d;
			}
			/*EndCondition ec = board.getEndCondition();
			if (ec.getWinner() == unicorn_id)
			{
				return 10000d; // our bot won! (yay!)
			}
			else if (ec.getWinner() == opponent_id)
			{
				return -10000d; // our bot lost! (we don't want that ...)
			}
			else
			{
				return 0d; // it's a draw (boooooring!)
			}*/
		}
	}
	
	private double scoreMaterial(Board board) {
		double score = 0d;
		PieceList pieces = board.getPosition().getPieces();
		for(Piece p : pieces) {
			switch(p.getID()) {
			case Piece.QUEEN:
				if(p.getColor() == player_id)
					score += 900;
				else 
					score -= 900;
				break;
			case Piece.ROOK:
				if(p.getColor() == player_id)
					score += 465;
				else 
					score -= 465;
				break;
			case Piece.BISHOP:
				if(p.getColor() == player_id)
					score += 325;
				else 
					score -= 325;
				break;
			case Piece.KNIGHT:
				if(p.getColor() == player_id)
					score += 275;
				else 
					score -= 275;
				break;
			case Piece.WHITE_PAWN:
				if(p.getColor() == player_id)
					score += 100;
				else 
					score -= 100;
				break;
			case Piece.BLACK_PAWN:
				if(p.getColor() == player_id)
					score += 100;
				else 
					score -= 100;
				break;
			}
		}
		return score;
	}
	private double getPieceSize(Board board) {
		int NrMyPieces = board.getPosition().getPieces().getPieces(player_id).size();
		return NrMyPieces;
	}
	
	private double getPieceDifference(Board board) {
		int NrMyPieces = board.getPosition().getPieces().getPieces(player_id).size();
		int NrOpponentPieces = board.getPosition().getPieces().getPieces(opponent_id).size();
		return (NrMyPieces - NrOpponentPieces) * 312; // return points in the range of [-10000, 10000]
	}
	
	/*private double possibleMoves(Board board) {
		if(player_id == board.getActiveColor())
			return board.getPossibleMoves().size();
		else
			return board.getPossibleMoves().size() * -1;
	}*/
	
	private double possibleQueenMoves(Board board) {
		return possibleMoves(board, Position12x10.WQUEEN);
		
		/*if(board.getPosition().getPieces().getByID(Piece.QUEEN, player_id) == null) return 0d; // no queen left
		//TODO get possible moves of all queens
		if(player_id == board.getActiveColor())
			return board.getPosition().getPieces().getByID(Piece.QUEEN, player_id).getPossibleMoves().size();
		else
			return board.getPosition().getPieces().getByID(Piece.QUEEN, player_id).getPossibleMoves().size() * -1;
			*/
	}
	
	private double possibleRookMoves(Board board) {
		return possibleMoves(board, Position12x10.WROOK);
	}
	
	private double possibleBishopMoves(Board board) {
		return possibleMoves(board, Position12x10.WBISHOP);
	}
	
	private double possibleKnightMoves(Board board) {
		return possibleMoves(board, Position12x10.WKNIGHT);
	}
	
	private double possibleMoves(Board board, int piece) {
		double score = 0d;
		for(Piece queen : position.getPieceByID(piece)) {
			score += queen.getPossibleMoves((Position12x10) board.getPosition()).size();
		}
		// TODO create method to convert white to black piece
		for(Piece queen : position.getPieceByID(piece + 65)) {
			score += queen.getPossibleMoves((Position12x10) board.getPosition()).size() * -1;
		}
		return score;
	}

}
