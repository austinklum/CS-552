
package uwlcs452552.h2.model;

public class GameException extends RuntimeException {

  private final Move.Direction dir;
  private final PlacedCar car;

  public GameException(Move.Direction dir, PlacedCar car) {
    this.dir = dir;
    this.car = car;
  }

  public Move.Direction getDir() { return dir; }
  public PlacedCar getPlacedCar() { return car; }
}

