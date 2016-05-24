package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.pieces.Piece;
import board.square.Square;

public class PieceList extends ArrayList<Piece> {

	private static final long serialVersionUID = 1L;
	
	Square squares[][];
	Map<Integer, Piece> whitePieces;
	Map<Integer, Piece> blackPieces;
	
	public PieceList() {
		/*whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();*/
		whitePieces = new HashMap<Integer, Piece>();
		blackPieces = new HashMap<Integer, Piece>();
	}
	
	@Override
	public boolean add(Piece p) {
		
		// TODO pieceID is not a unique id, it's more a piece class
		// HashMap approach for faster check and mate testing
		super.add(p);
		if(p.getColor() == 'w') whitePieces.put(p.getID(), p);
		else if(p.getColor() == 'b') blackPieces.put(p.getID(), p);
		return true;
	}
	
	@Override
	public Piece get(int index) {
		return super.get(index);
	}
	
	public void setSquareArray(Square squares[][]) {
		this.squares = squares;
	}
	
	public List<Piece> getPieces(char color) {
		if(color == 'w') return getWhitePieces();
		else if(color == 'b') return getBlackPieces();
		else return null;
	}
	
	public List<Piece> getWhitePieces() {
		List<Piece> whitePieces = new ArrayList<Piece>();
		for(Piece p : this) {
			if(p.getColor() == 'w') whitePieces.add(p);
		}
		return whitePieces;
		//return new ArrayList<Piece>(whitePieces.values());
	}
	
	public List<Piece> getBlackPieces() {
		List<Piece> blackPieces = new ArrayList<Piece>();
		for(Piece p : this) {
			if(p.getColor() == 'b') blackPieces.add(p);
		}
		return blackPieces;
		//return new ArrayList<Piece>(blackPieces.values());
	}
	
	public Piece getPieceAt(int[] coord) {
		return squares[coord[0]][coord[1]].getPiece();
		//TODO remove (inefficient?)
		/*for(Piece p : this) {
			if(p.getPosition()[0] == coord[0] && p.getPosition()[1] == coord[1]) return p;
		}
		return null;*/
	}
	
	public void removePieceAt(int[] coord) {
		squares[coord[0]][coord[1]].clear();
		for(int i = 0; i < size(); i++) {
			Piece p = get(i);

			if(p.getPosition()[0] == coord[0] && p.getPosition()[1] == coord[1]) {
				remove(p);
				whitePieces.remove(p);
				blackPieces.remove(p);
				break;
			}
		}
	}
	
	/*public boolean contains(int pieceID) {
		for(Piece p : this) {
			if(p.getID() == pieceID) return true;
		}
		return false;
	}*/
	
	//TODO only usable for kings (not for pawns, queens, bishops and rooks)
	public Piece getByID(int pieceID, char color) {
		if(color == 'w')
			return whitePieces.get(pieceID);
		else
			return blackPieces.get(pieceID);
		/*for(Piece p : this) {
			if(p.getID() == pieceID && p.getColor() == color) return p;
		}
		return null;*/
	}
}
