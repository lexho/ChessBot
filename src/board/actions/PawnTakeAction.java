package board.actions;

import java.util.ArrayList;
import java.util.List;

import board.position.Position12x10;

public class PawnTakeAction extends Action {

	public PawnTakeAction(int trans) {
		super(trans, true);
		// TODO Auto-generated constructor stub
	}

	/** apply action 
	 * @param source current index
	 * @return new index after performed action */
	public List<Integer> apply(Position12x10 pos, int source) {
		List<Integer> targets = new ArrayList<Integer>();
		
		int i = source + trans;
		//TODO fix this loop
		if(pos.isValid(i) && !pos.isFree(i)) targets.add(i);
		
		/*while(pos.isValid(i) && (takes && !pos.isFree(i)) ) {
			boolean takesPiece = takes && !pos.isFree(i);
			if(takesPiece && pos.isColor(i, pos.getColor(source))) break; // do not take your own pieces
			targets.add(i);
			if(!repeatable) break;
			if(takesPiece) break; // take and action is done
			i += trans;
		}*/
		
		return targets;
	}
}
