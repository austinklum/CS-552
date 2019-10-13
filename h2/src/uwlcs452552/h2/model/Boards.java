package uwlcs452552.h2.model;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

/**
 * Sample Rushhour boards.
 *
 * Overall code constructed by John Maraist; encoders of individual
 * boards: <blockquote>Josh Chianelli, Connor Langley, Jackson Lee,
 * Sean Martens, Danny Pierce, Laik Ruetten</blockquote>
 */
public class Boards {

  public static final BoardState
      ONLY_GOAL = BoardStateBuilder.standard(1).board();

  public static final BoardState
      D2_C1 = (BoardStateBuilder.standard(1)
               .vTruck("yellow", 0, 5)
               .vTruck("blue", 1, 3)
               .vTruck("purple", 1, 0)
               .vCar("yellow", 4, 0)
               .hCar("blue", 4, 4)
               .hTruck("cyan", 5, 2)
               .hTruck("green", 0, 0)
               .board());

  /** Deck 2, Card 2, contributed by Laik Ruetten */
  public static final BoardState
      D2_C2 = (BoardStateBuilder.standard(0)
               .vCar("green", 0, 0)
               .hTruck("yellow", 0, 3)
               .vCar("orange", 1, 3)
               .vTruck("purple", 1, 5)
               .vCar("blue", 2, 4)
               .hTruck("blue", 3, 0)
               .vCar("pink", 4, 2)
               .hCar("blue", 4, 4)
               .hCar("cyan", 5, 0)
               .hCar("black", 5, 3)
               .board());

  /** Deck 2, Card 4, contributed by Connor Langley */
  public static final BoardState
      D2_C4 = (BoardStateBuilder.standard(1)
               .vTruck("yellow", 0, 0)
               .vTruck("purple", 0, 3)
               .vCar("cyan", 3, 2)
               .vCar("yellow", 4, 5)
               .hTruck("blue", 3, 3)
               .hTruck("blue", 5, 2)
               .board());

  /** Deck 2, Card 5, contributed by Sean Martens */
  public static final BoardState
      D2_C5 = (BoardStateBuilder.standard(1)
               .hCar("cyan", 0, 0)
               .vTruck("purple", 1, 0)
               .vCar("pink", 4, 0)
               .hTruck("cyan", 3, 1)
               .vTruck("yellow", 0, 3)
               .vTruck("blue", 1, 4)
               .hCar("purple", 4, 4)
               .hCar("green", 5, 4)
               .vCar("orange", 0, 5) //Unsure if this car is yellow or orange
               .vCar("black", 2, 5)
               .board());

  /** Deck 2, Card 6, contributed by Jackson Lee */
  public static final BoardState
      D2_C6 = (BoardStateBuilder.standard(1)
                .vTruck("blue", 2, 3)
                .vTruck("yellow", 1, 4)
                .vTruck("purple", 1, 5)
                .vCar("green", 4, 0)
                .vCar("blue", 3, 2)
                .vCar("yellow", 0, 3)
                .hTruck("cyan", 5, 3)
                .hCar("pink", 3, 0)
                .hCar("blue", 1, 0)
                .hCar("cyan", 0, 0)
                .board());

  /** Deck 2, Card 7, contributed by Danny Pierce */
  public static final BoardState
      D2_C7 = (BoardStateBuilder.standard(1)
               .vCar("green", 0, 1)
               .hCar("yellow", 0, 2)
               .vCar("blue", 0, 4)
               .vCar("pink", 0, 5)
               .vCar("blue", 1, 3)
               .vCar("green", 2, 5)
               .hCar("yellow", 3, 2)
               .vCar("brown", 4, 3)).board();

  /** Deck 2, Card 8, contributed by Josh Chianelli */
  public static final BoardState
      D2_C8 = (BoardStateBuilder.standard(0)
               .hCar("cyan", 0, 3)
               .vTruck("yellow", 0, 5)
               .hCar("orange", 1, 2)
               .vCar("blue", 1, 4)
               .hCar("green", 3, 0)
               .hCar("tan", 4, 0)
               .hCar("lime", 5, 0)
               .vCar("pink", 2, 2)
               .vCar("light-yellow", 4, 2 )
               .vCar("navy", 2, 3)
               .hCar("brown", 3, 4)
               .hTruck("purple", 4, 3)
               .hTruck("navy", 5, 3)
               .board());

  /** Deck 2, Card 33, contributed by Josh Chianelli */
  public static final BoardState
      D2_C33 = (BoardStateBuilder.standard(0)
                .vCar("cyan", 0, 1)
                .vTruck("blue", 0, 2)
                .hCar("yellow", 0, 4)
                .vCar("light-yellow", 3, 0)
                .hCar("pink", 3,1)
                .hCar("navy", 3, 3)
                .hCar("green", 4, 1)
                .hTruck("navy", 5, 0)
                .vCar("brown", 4, 3)
                .vCar("tan", 4, 4)
                .vTruck("purple", 3, 5)
                .board());

  /** Deck 2, Card 34, contributed by Danny Pierce */
  public static final BoardState
      D2_C34 = (BoardStateBuilder.standard(0)
                .vCar("green", 0, 0)
                .hTruck("blue", 0, 3)
                .vCar("yellow", 1, 3)
                .vTruck("purple", 1, 5)
                .vCar("blue", 2, 4)
                .hTruck("blue", 3, 0)
                .vCar("pink", 3, 3)
                .vCar("blue", 4, 2)
                .hCar("green", 4, 4)
                .hCar("pale yellow", 5, 0)
                .hCar("light brown", 5, 3)).board();

  /** Deck 2, Card 35, contributed by Jackson Lee */
  public static final BoardState
      D2_C35 = (BoardStateBuilder.standard(0)
                    .vTruck("yellow", 0, 2)
                    .vTruck("purple", 0, 5)
                    .vCar("brown", 3, 0)
                    .vCar("blue", 4, 3)
                    .vCar("yellow", 1, 3)
                    .vCar("green", 4, 4)
                    .hTruck("blue", 3, 1)
                    .hCar("purple", 5, 0)
                    .hCar("pink", 4, 1)
                    .hCar("cyan", 0, 3)
                    .board());

  /** Deck 2, Card 36, contributed by Sean Martens */
  public static final BoardState
      D2_C36 = (BoardStateBuilder.standard(2)
                .vTruck("yellow", 0, 0)
                .hTruck("purple", 0, 1)
                .hCar("cyan", 0, 4)
                .vCar("orange", 1, 1)
                .hCar("blue", 1, 2)
                .vTruck("blue", 1, 5)
                .hTruck("cyan", 3, 0)
                .vCar("pink", 3, 3)
                .vCar("purple", 4, 2)
                .hCar("green", 4, 4)
                .hCar("black", 5, 0)
                .board());

  // Contains colliding cars in its current state.
  //
  //  /**
  //   * Deck 2, Card 37, contributed by Connor Langley.
  //   */
  //  public static final BoardState
  //      D2_C37 = (BoardStateBuilder.standard(1)
  //                .hCar("cyan", 0, 0)
  //                .hCar("pink", 1, 0)
  //                .hCar("black", 5, 0)
  //                .hCar("blue", 0, 4)
  //                .hCar("green", 4, 4)
  //                .hCar("tan", 5, 4)
  //                .hTruck("blue", 1, 1)
  //                .vTruck("blue", 2, 0)
  //                .vTruck("yellow", 1, 4)
  //                .vTruck("purple", 1, 5)
  //                .vCar("yellow", 0, 2)
  //                .vCar("blue", 4, 3)
  //                .board());

  /** Deck 2, Card 39, contributed by Laik Ruetten */
  public static final BoardState
      D2_C39 = (BoardStateBuilder.standard(0)
                 .vCar("green", 0, 2)
                 .hTruck("yellow", 0, 3)
                 .vCar("orange", 1, 3)
                 .vCar("blue", 2, 2)
                 .vTruck("blue", 2, 5)
                 .hCar("pink", 3, 0)
                 .hCar("blue", 3, 3)
                 .vCar("cyan", 4, 0)
                 .vCar("black", 4, 1)
                 .hCar("tan", 4, 2)
                 .hCar("yellow", 5, 2)
                 .board());

  /** Deck 2, Card 40 */
  public static final BoardState
      D2_C40 = (BoardStateBuilder.standard(3)
                .vTruck("yellow", 0, 0)
                .vTruck("purple", 1, 5)
                .vCar("orange", 0, 4)
                .vCar("blue", 1, 1)
                .vCar("pink", 1, 2)
                .vCar("purple", 3, 3)
                .vCar("green", 4, 2)
                .hTruck("blue", 3, 0)
                .hCar("green", 0, 1)
                .hCar("black", 4, 4)
                .hCar("brown", 5, 0)
                .hCar("yellow", 5, 3)
                .board());

  public static final Map<String, BoardState> BOARDS;
  static {
    final Map<String, BoardState> boardsMap = new TreeMap<>();
    boardsMap.put("goal car only", ONLY_GOAL);
    boardsMap.put("Deck 2, Card 1", D2_C1);
    boardsMap.put("Deck 2, Card 2", D2_C2);
    boardsMap.put("Deck 2, Card 4", D2_C4);
    boardsMap.put("Deck 2, Card 5", D2_C5);
    boardsMap.put("Deck 2, Card 6", D2_C6);
    boardsMap.put("Deck 2, Card 7", D2_C7);
    boardsMap.put("Deck 2, Card 8", D2_C8);
    boardsMap.put("Deck 2, Card 33", D2_C33);
    boardsMap.put("Deck 2, Card 34", D2_C34);
    boardsMap.put("Deck 2, Card 35", D2_C35);
    boardsMap.put("Deck 2, Card 36", D2_C36);
    // boardsMap.put("Deck 2, Card 37", D2_C37); // Contain error as given
    boardsMap.put("Deck 2, Card 39", D2_C39);
    boardsMap.put("Deck 2, Card 40", D2_C40);
    BOARDS = Collections.unmodifiableMap(boardsMap);
  }

  /** Main routine showsa little catalog of the boards. */
  public static void main(String[] args) {
    for (final String name : BOARDS.keySet()) {
      final BoardState board = BOARDS.get(name);
      System.out.printf("%s\n%s\n\n", name, board.toString(" "));
    }
  }
}

