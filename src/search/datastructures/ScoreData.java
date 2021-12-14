package search.datastructures;

import java.util.ArrayList;
import java.util.List;

import sun.awt.SunHints.Value;

public class ScoreData extends ArrayList<Double> {

	private static final long serialVersionUID = 1L;

	List<String> labels;
	double score = 0;
	
	public ScoreData() {
		labels = new ArrayList<String>();
	}
	
	public void addLabel(String label) {
		labels.add(label);
	}
	
	public String getLabelRowString() {
		String output = "";
		for(int i = 0; i < this.size(); i++) {
			output += labels.get(i) + " ";
		}
		
		output += "Score";
		
		return output;
	}

	public String toString() {
		String output = "";
		/*for(Integer value : this) {
			output += " " + value;
		}*/
		/*for(int i = 0; i < this.size(); i++) {
			output += labels.get(i) + ": " + this.get(i) + ", ";
		}*/
		
		/* fixed length formatted output */
		for(int i = 0; i < this.size(); i++) {
			output += String.format("%1$" + (labels.get(i).length() + 1) + "s", this.get(i));
			//output += labels.get(i) + ": " + this.get(i) + ", ";
		}
		
		/* Compute final score */
		score = 0;
		for(double value : this) {
			score += value;
		}
		
		output += String.format("%1$" + 6 + "s", score);
		return output;
	}
}
