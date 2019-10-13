package uwlcs452552.h2;

public abstract class AbstractSolution {
  private final MovesFinder[] moves;

  public AbstractSolution(MovesFinder... moves) {
    this.moves = moves;
  }

  public final void run() {
    // To be extended
  }
}


