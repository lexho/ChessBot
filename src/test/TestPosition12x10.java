package test;

import board.Position12x10;

class TestPosition12x10 {
	public static void main(String[] args) {
		Position12x10 pos = new Position12x10();

		for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				System.out.print(pos.getPieceAt(x, y) + " ");
			}
			System.out.println();
		}
	}
}
