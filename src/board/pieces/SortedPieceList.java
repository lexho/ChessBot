package board.pieces;

import java.util.ArrayList;
import java.util.List;

public class SortedPieceList extends PieceList {

	private static final long serialVersionUID = -6247611486550558528L;

	@Override
	public List<Piece> getBlackPieces() {
		List<Piece> blackPieces = new ArrayList<Piece>();
		
		for(Piece p : this) {
			if(p.getColor() == 'b') {
				int i = 0;
				while (blackPieces.get(0).getValue() > p.getValue()) i++;
				blackPieces.add(i, p);
			}
		}

		return blackPieces;
		//return new ArrayList<Piece>(blackPieces.values());
	}
}
