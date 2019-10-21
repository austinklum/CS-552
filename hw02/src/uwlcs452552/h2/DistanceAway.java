package uwlcs452552.h2;

import java.util.function.Function;

import uwlcs452552.h2.model.BoardState;

public class DistanceAway extends MovesFinder {

	private static Function<BoardState, Double> heu = bs -> {
		int currCol = -1;
		
		// Find the current placement of the target car
		for(int i = 0; i < bs.placed(); i++) {
			if(bs.placement(i).isTargetCar()) {
				currCol = bs.placement(i).getCol() + bs.placement(i).getLength();
				break;
			}
		}
		
		// Where target car is minus how far away to goal
		return (double) (bs.getBoardSize() - currCol);
		
	};
	
	public DistanceAway() {
		super(heu);
	}

}
