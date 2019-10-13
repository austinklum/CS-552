
package uwlcs452552.h2.model;

public class IllegalMoveException extends MoveException {

  public IllegalMoveException(Move.Direction dir, PlacedCar car) {
    super(dir, car);
  }
}

