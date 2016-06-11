package search.evalfunctions;

import java.util.Comparator;

import board.Move;
import board.pieces.PieceCreator;
import board.position.Position12x10;

/** sorts moves by Most Valuable Victim - Least Valuable Aggressor */
public class MoveComparator implements Comparator<Move> {
	
	private int[] board_raw;
	
	public MoveComparator(int[] board_raw) {
		this.board_raw = board_raw;
	}
	
    @Override
    public int compare(Move a, Move b) {
    	int a_vval = getVictimValue(a);
    	int b_vval = getVictimValue(b);
    	if(a_vval > b_vval)
    		return -1;
    	else if(a_vval == b_vval) {
    		int a_agrval = getAggressorValue(a);
        	int b_agrval = getAggressorValue(b);
    		if(a_agrval < b_agrval)
    			return -1;
    		else if(a_agrval == b_agrval)
    			return 0;
    		else 
    			return 1;
    	} else {
    		return 1;
    	}
    	//return getVictimValue(a) > getVictimValue(b) ? -1 : getVictimValue(a) == getVictimValue(b) ? 0 : 1;
    }
    
    private int getVictimValue(Move m) {
    	int trg = m.getTargetIndex();
    	char rep = (char) board_raw[trg];
		
		if(rep != Position12x10.EMPTY)
			return PieceCreator.createPiece(rep,trg).getValue();
		else return 0;
		
    }
    
    private int getAggressorValue(Move m) {
    	return 0;
    }
}
