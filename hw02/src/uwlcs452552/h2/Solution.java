package uwlcs452552.h2;

public class Solution extends AbstractSolution {
	public Solution() {
		super(new DistanceAway(), new CarsInWay(), new CarsInWayInWay(), new CarsInWayInWayAway());
	}
	
	public static void main(String[] args) { new Solution().run(); }

}
