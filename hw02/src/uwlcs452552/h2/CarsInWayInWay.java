package uwlcs452552.h2;

import java.util.function.Function;

import uwlcs452552.h2.model.BoardState;

public class CarsInWayInWay extends MovesFinder {

	private static Function<BoardState, Double> heu = bs -> {
		int row = -1;
		int currCol = -1;
		
		// Find the current placement of the target car
		for(int i = 0; i < bs.placed(); i++) {
			if(bs.placement(i).isTargetCar()) {
				currCol = bs.placement(i).getCol() + bs.placement(i).getLength();
				row = bs.placement(i).getRow();
				break;
			}
		}
		
		double carsInWay = 0;
		
		for(int i = currCol; i < bs.getBoardSize(); i++) {
			// Check for cars in our direct path to goal
			if(bs.filledAt(row, i)) {
				carsInWay++;
			}
			
			// Check cars in direct paths neighbors while avoiding IndexOutOfBoundsErros
			int rowMinus = row - 1 < 0 ? 0: row - 1;
			int rowPlus = row + 1 > bs.getBoardSize() ? row : row + 1 ;
		
			// Check if blocking car has blocking neighbors
			if(bs.filledAt(row, i) && bs.filledAt(rowMinus, i) && bs.filledAt(rowPlus, i)) {
				carsInWay++;
			}
		}
		
		return carsInWay;
	};
	
	public CarsInWayInWay() {
		super(heu);
	}

}
