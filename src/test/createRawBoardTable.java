package test;

public class createRawBoardTable {
	public static void main(String[] args) {
		for(int j = 21; j < 109; j += 10) {
			for(int i = j; i < j + 8; i++) {
				System.out.print(i + ", ");
			}
			System.out.println();
		}
	}
}
