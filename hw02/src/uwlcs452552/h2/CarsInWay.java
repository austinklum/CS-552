package uwlcs452552.h2;

import java.lang.Thread.State;
import java.util.function.Function;

import uwlcs452552.h2.model.BoardState;

public class CarsInWay extends MovesFinder {

	private static Function<BoardState, Double> heu = bs -> {
		int row = -1;
		int currCol = -1;
		double carsInWay = 0;

		// Find the current placement of the target car
		for(int i = 0; i < bs.placed(); i++) {
			if(bs.placement(i).isTargetCar()) {
				currCol = bs.placement(i).getCol() + bs.placement(i).getLength();;
				row = bs.placement(i).getRow();
				break;
			}
		}
		
		// Check for cars in our direct path to goal
		for(int i = currCol; i < bs.getBoardSize(); i++ ) {
			if(bs.filledAt(row, i)) {
				carsInWay++;
			}
		}

		return carsInWay;
	};
	
	public CarsInWay() {
		super(heu);
	}
	
}
