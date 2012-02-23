package edu.umn.ecology.populus.eegg;

import javax.swing.*;
import java.awt.*;

/** Winning piece is inner yellow, then outer yellow, inner, outer... */
public class LPuzzleModel2 extends LPuzzleModel {
	private boolean isInner = true;
	
	public static LPuzzleModel createNewModel() {
		return new LPuzzleModel2();
	}
	protected int calculateWinningPiece(Color[] colors) {
		int innerYellow=-1, outerYellow=-1;
		for (int i=0; i<4; i++) {
			if (colors[i] == kColorList[2])
				innerYellow = i;
		}
		for (int i=4; i<8; i++) {
			if (colors[i] == kColorList[2])
				outerYellow = i;
		}
		int winningPiece = isInner?innerYellow:outerYellow;
		isInner = !isInner;
		return winningPiece;
	}
}
