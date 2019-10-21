
package uwlcs452552.h2.model;

public class MoveException extends RuntimeException {

  private final Move.Direction dir;
  private final PlacedCar car;

  public MoveException(Move.Direction dir, PlacedCar car) {
    this.dir = dir;
    this.car = car;
  }

  public Move.Direction getDirection() { return dir; }
  public PlacedCar getPlacedCar() { return car; }
}

