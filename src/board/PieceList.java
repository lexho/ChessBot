package board;

import java.util.ArrayList;
import java.util.List;

import board.pieces.Piece;

public class PieceList extends ArrayList<Piece> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	}
	
	public List<Piece> getBlackPieces() {
		List<Piece> blackPieces = new ArrayList<Piece>();
		for(Piece p : this) {
			if(p.getColor() == 'b') blackPieces.add(p);
		}
		return blackPieces;
	}
	
	public Piece getPieceAt(int[] coord) {
		for(Piece p : this) {
			if(p.getPosition()[0] == coord[0] && p.getPosition()[1] == coord[1]) return p;
		}
		return null;
	}
	
	public void removePieceAt(int[] coord) {
		for(int i = 0; i < size(); i++) {
			Piece p = get(i);

			if(p.getPosition()[0] == coord[0] && p.getPosition()[1] == coord[1]) {
				remove(i);
			}
		}
	}
	
	public boolean contains(int pieceID) {
		for(Piece p : this) {
			if(p.getID() == pieceID) return true;
		}
		return false;
	}
	
	public Piece getByID(int pieceID) {
		for(Piece p : this) {
			if(p.getID() == pieceID) return p;
		}
		return null;
	}
	
	/*public int size() {
		return super.size();
	}*/

}
