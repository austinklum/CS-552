package uwlcs452552.h2.model;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

/**
 * Sample Rushhour boards.
 *
 * Overall code constructed by John Maraist; encoders of individual
 * boards: <blockquote>Josh Chianelli, Augustus Demay, Nathan
 * Diedrick, Joey Fedor, Wai Kit Khong, Austin Klum, Benjamin Krueger,
 * Tyler Landowski, Connor Langley, Jackson Lee, Sean Martens, Alec
 * Phelps, Danny Pierce, Tristan Rooney, Laik Ruetten, Adam Yakes,
 * Zhenhao Zhou</blockquote>
 */
public class Boards {

  /** A "sanity check" example with only the goal car on the board.  */
  public static final BoardState
      ONLY_GOAL = BoardStateBuilder.standard(1).board();

  /**
   *  A degenerate case which starts at the goal, suggested by Laik
   *  Ruetten.  Uninteresting in terms of search behavior, but should
   *  not cause crashes.
   */
  public static final BoardState
      AT_GOAL = BoardStateBuilder.standard(4).board();

  // /** Deck 1, Card 10 contributed by Austin Klum*/
  // public static final BoardState
  //         D1_C10 = (BoardStateBuilder.standard(3)
  //                               .hCar("green", 0, 1)
  //                               .vCar("orange", 0, 3)
  //                               .hCar("blue", 0, 4)
  //                               .hCar("pink", 1, 1)
  //                               .hCar("purple", 1, 5)
  //                               .vTruck("yellow", 2, 2)
  //                               .hCar("green", 3, 0)
  //                               .vTruck("purple", 3, 5)
  //                               .vCar("grey", 4, 0)
  //                               .hCar("beige", 5, 1)
  //                               .hCar("yellow", 5, 3)
  //                               .board());

  /** Deck 1, Card 31 contributed by Austin Klum*/
  public static final BoardState
          D1_C31 = (BoardStateBuilder.standard(0)
                                .vCar("green", 0, 0)
                                .vCar("orange", 0, 1)
                                .hCar("blue", 0, 2)
                                .hCar("pink", 0, 4)
                                .vTruck("yellow", 1, 2)
                                .hCar("purple", 1, 4)
                                .hCar("red", 2, 3)
                                .vTruck("purple", 2, 5)
                                .hCar("green", 3, 0)
                                .vCar("grey", 3, 3)
                                .vCar("beige", 4, 0)
                                .hCar("yellow", 4, 1)
                                .hTruck("blue", 5, 3)
                                .board());

  /** Deck 2, Card 1 */
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

  /** Deck 2, Card 10, contributed by Nathan Diedrick */
  public static final BoardState
      D2_C10 = (BoardStateBuilder.standard(1)
                .vTruck("yellow", 1, 5)
                .vTruck("purple", 2, 0)
                .hTruck("blue", 3, 1)
                .vCar("orange", 0, 2)
                .vCar("blue", 4, 3)
                .hCar("green", 0, 0)
                .hCar("pink", 1, 0)
                .hCar("blue", 0, 4)
                .hCar("grey", 5, 0)
                .hCar("green", 4, 4)
                .hCar("tan", 5,4)
                .board());

  /** Deck 2, Card 11, contributed by Alec Phelps */
  public static final BoardState
      D2_C11 = (BoardStateBuilder.standard(1)
                .vTruck("yellow", 0, 0)
                .vTruck("purple", 0, 3)
                .vCar("orange", 3, 2)
                .vCar("blue", 4, 5)
                .hCar("green", 0, 1)
                .hTruck("blue", 3, 3)
                .hTruck("cyan", 5, 2)
                .board());

  /** Deck 2, Card 12, contributed by Wai Kit Khong */
  public static final BoardState
      D2_C12 = (BoardStateBuilder.standard(0)
                .vCar("green",0, 0)
                .hCar("yellow", 0, 1)
                .vTruck("yellow", 0, 5)
                .vTruck("white",1,2)
                .hTruck("blue", 3, 3)
                .hTruck("blue",5,0)
                .vCar("blue", 4,4)
                .board());

  /** Deck 2, Card 13, contributed by Tristan Rooney */
  public static final BoardState
      D2_C13 = (BoardStateBuilder.standard(3)
                .vTruck("purple", 3, 0)
                .vCar("blue", 2, 1)
                .vCar("pink", 1, 2)
                .vCar("black", 4, 3)
                .vTruck("blue", 1, 5)
                .hCar("lime", 0, 0)
                .hCar("yellow", 0, 2)
                .hCar("green", 3, 3)
                .hCar("gray", 4, 4)
                .hCar("olive", 5, 1)
                .hCar("brown", 5, 4)
                .board());

  /** Deck 2, Card 14, contributed by Joey Fedor */
  public static final BoardState
      D2_C14 = (BoardStateBuilder.standard(2)
                .hCar("teal", 0, 0) //0
                .vCar("yellow", 0, 2) //1
                .hCar("blue", 1, 4) //2
                .vCar("pink", 2, 0) //3
                .vCar("blue", 2, 1) //4
                .hCar("white", 3, 2) //6
                .vCar("green", 2, 4) //7
                .vCar("purple", 2, 5) //8
                .hCar("green", 5, 0) //9
                .vCar("white", 4, 2) //10
                .hCar("purple", 4, 4) //11
                .board());

  /** Deck 2, Card 16, contributed by Benjamin Krueger */
  public static final BoardState
          D2_C16 = (BoardStateBuilder.standard(3)
                            .vTruck("yellow", 0, 5)
                            .vTruck("blue", 2, 2)
                            .vCar("blue", 0, 4)
                            .vCar("pink", 1, 0)
                            .vCar("green", 2, 1)
                            .hTruck("blue", 4, 3)
                            .hCar("green", 0, 0)
                            .hCar("orange", 0, 2)
                            .hCar("blue", 1, 2)
                            .hCar("black", 5, 0)
                            .board());

  /** Deck 2, Card 18, contributed by Tyler Landowski */
  public static final BoardState
      D2_C18 = (BoardStateBuilder.standard(1)
               .hCar("light-green", 0, 0)
               .hCar("blue", 1, 0)
               .hCar("pink", 4, 1)
               .vCar("orange", 0, 2)
               .hTruck("cyan", 5, 0)
               .hTruck("blue", 3, 1)
               .vTruck("purple", 2, 0)
               .vTruck("yellow", 0, 3)
               .board());

  /** Deck 2, Card 19, contributed by Adam Yakes */
  public static final BoardState
      D2_C19 = (BoardStateBuilder.standard(2)
                .vCar("cyan", 0, 2)
                .hCar("orange", 0, 3)
                .vCar("brown", 1, 4)
                .vCar("pink", 2, 1)
                .hCar("blue", 3, 2)
                .vCar("green", 3, 4)
                .hTruck("yellow", 4, 1)
                .board());

  /** Deck 2, Card 20, contributed by Zhenhao Zhou */
  public static final BoardState
      D2_C20 = (BoardStateBuilder.standard(0)
                .vTruck("pink",2,5)
                .hTruck("yellow",0,3)
                .hTruck("blue",5,3)
                .vCar("green",0,0)
                .vCar("pink",2,2)
                .vCar("blue",1,3)
                .vCar("purple",4,2)
                .hCar("yellow",1,1)
                .hCar("cyan",4,3)
                .board());

  /** Deck 2, Card 21, contributed by Zhenhao Zhou */
  public static final BoardState
      D2_C21 = (BoardStateBuilder.standard(1)
                .vTruck("yellow",0,3)
                .vTruck("pink",1,0)
                .hTruck("blue",3,1)
                .hTruck("cyan",5,3)
                .vCar("orange",0,2)
                .hCar("green",0,0)
                .board());

  /** Deck 2, Card 22, contributed by Adam Yakes */
  public static final BoardState
      D2_C22 = (BoardStateBuilder.standard(1)
                .vCar("cyan", 0, 2)
                .hTruck("yellow", 0, 3)
                .vCar("orange", 1, 0)
                .vTruck("purple", 1, 3)
                .hCar("light blue", 1, 4)
                .vCar("pink", 3, 1)
                .hCar("blue", 3, 4)
                .vCar("green", 4, 0)
                .hCar("black", 4, 2)
                .hTruck("blue", 5, 1)
                .vCar("tan", 4, 5)
                .board());

  /** Deck 2, Card 23, contributed by Tyler Landowski */
  public static final BoardState
      D2_C23 = (BoardStateBuilder.standard(3)
               .hCar("orange", 1, 3)
               .hCar("blue", 3, 4)
               .hCar("green", 4, 4)
               .vCar("light-green", 1, 2)
               .vCar("blue", 3, 2)
               .vCar("pink", 3, 3)
               .hTruck("yellow", 0, 2)
               .hTruck("blue", 5, 2)
               .vTruck("purple", 0, 5)
               .board());

  /** Deck 2, Card 25, contributed by Benjamin Krueger */
  public static final BoardState
      D2_C25 = (BoardStateBuilder.standard(1)
                .vTruck("blue",2, 0)
                .vTruck("yellow", 1, 5)
                .vCar("yellow", 0, 2)
                .vCar("blue", 2, 4)
                .vCar("green", 4, 1)
                .vCar("grey", 4, 3)
                .hTruck("blue", 3, 1)
                .hCar("green", 0, 0)
                .hCar("blue", 0, 4)
                .hCar("pink", 1, 0)
                .hCar("brown", 4, 4)
                .hCar("yellow", 5, 4)
                .board());

  /** Deck 2, Card 27, contributed by Joey Fedor */
  public static final BoardState
      D2_C27 = (BoardStateBuilder.standard(0)
                .vCar("teal", 0, 0)
                .hCar("yellow", 0, 1)
                .hCar("blue", 1, 1)
                .vCar("pink", 2, 2)
                .vTruck("yellow", 0, 3)
                .hCar("blue", 3, 3)
                .vCar("green", 4, 2)
                .hTruck("blue", 5, 3)
                .vTruck("purple", 2, 5)
                .board());

  /** Deck 2, Card 28, contributed by Tristan Rooney */
  public static final BoardState
      D2_C28 = (BoardStateBuilder.standard(0)
                .vCar("blue", 3, 0)
                .vCar("pink", 3, 1)
                .vTruck("gray", 1, 2)
                .vCar("lime", 0, 3)
                .vTruck("blue", 3, 5)
                .hTruck("yellow", 0, 0)
                .hCar("brown", 1, 4)
                .hCar("blue", 3, 3)
                .hTruck("cyan", 4, 2)
                .hCar("green", 5, 0)
                .hCar("black", 5, 2)
                .board());

  /** Deck 2, Card 29, contributed by Wai Kit Khong */
  public static final BoardState
      D2_C29 = (BoardStateBuilder.standard(0)
                .hTruck("yellow", 0, 0)
                .vTruck("white",0,4)
                .vCar("green",1,2)
                .vCar("yellow",2,5)
                .vCar("blue",3,0)
                .hCar("pink",3,1)
                .hCar("blue",3,3)
                .hCar("green",4,1)
                .vCar("black",4,3)
                .vCar("brown",4,5)
                .hTruck("blue",5,0)
                .board());

  /** Deck 2, Card 30, contributed by Alec Phelps */
  public static final BoardState
      D2_C30 = (BoardStateBuilder.standard(1)
                .vTruck("yellow", 0, 0)
                .vTruck("blue", 3, 5)
                .vCar("orange", 1, 3)
                .vCar("olive", 0, 2)
                .hTruck("purple", 0, 3)
                .hCar("blue", 5, 0)
                .hCar("green", 5, 2)
                .hCar("cyan", 3, 0)
                .hCar("pink", 3, 2)
                .board());

  /** Deck 2, Card 31, contributed by Nathan Diedrick */
  public static final BoardState
      D2_C31 = (BoardStateBuilder.standard(1)
                .vTruck("blue", 3, 2)
                .vTruck("purple", 2, 5)
                .hTruck("yellow", 0, 3)
                .hTruck("blue", 5, 3)
                .vCar("pink", 2, 0)
                .vCar("orange", 1, 3)
                .hCar("green", 0, 0)
                .hCar("green", 4, 0)
                .hCar("aqua", 1, 4)
                .hCar("blue", 3, 3)
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

  /**
   * Deck 2, Card 37, contributed by Connor Langley.
   */
  public static final BoardState
      D2_C37 = (BoardStateBuilder.standard(1)
                .hCar("cyan", 0, 0)
                .hCar("pink", 1, 0)
                .hCar("black", 5, 0)
                .hCar("blue", 0, 4)
                .hCar("green", 4, 4)
                .hCar("tan", 5, 4)
                .hTruck("blue", 3, 1)
                .vTruck("blue", 2, 0)
                .vTruck("yellow", 1, 4)
                .vTruck("purple", 1, 5)
                .vCar("yellow", 0, 2)
                .vCar("blue", 4, 3)
                .board());

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

  /** An immutable map from board names to initial states. */
  public static final Map<String, BoardState> BOARDS;
  static {
    final Map<String, BoardState> boardsMap = new TreeMap<>();
    boardsMap.put("Goal car only", ONLY_GOAL);
    boardsMap.put("Starting at goal", AT_GOAL);
    // boardsMap.put("Deck 1, Card 10", D1_C10);
    boardsMap.put("Deck 1, Card 31", D1_C31);
    boardsMap.put("Deck 2, Card 01", D2_C1);
    boardsMap.put("Deck 2, Card 02", D2_C2);
    boardsMap.put("Deck 2, Card 04", D2_C4);
    boardsMap.put("Deck 2, Card 05", D2_C5);
    boardsMap.put("Deck 2, Card 06", D2_C6);
    boardsMap.put("Deck 2, Card 07", D2_C7);
    boardsMap.put("Deck 2, Card 08", D2_C8);
    boardsMap.put("Deck 2, Card 10", D2_C10);
    boardsMap.put("Deck 2, Card 11", D2_C11);
    boardsMap.put("Deck 2, Card 12", D2_C12);
    boardsMap.put("Deck 2, Card 13", D2_C13);
    boardsMap.put("Deck 2, Card 14", D2_C14);
    boardsMap.put("Deck 2, Card 16", D2_C16);
    boardsMap.put("Deck 2, Card 18", D2_C18);
    boardsMap.put("Deck 2, Card 19", D2_C19);
    boardsMap.put("Deck 2, Card 20", D2_C20);
    boardsMap.put("Deck 2, Card 21", D2_C21);
    boardsMap.put("Deck 2, Card 22", D2_C22);
    boardsMap.put("Deck 2, Card 23", D2_C23);
    boardsMap.put("Deck 2, Card 25", D2_C25);
    boardsMap.put("Deck 2, Card 27", D2_C27);
    boardsMap.put("Deck 2, Card 28", D2_C28);
    boardsMap.put("Deck 2, Card 29", D2_C29);
    boardsMap.put("Deck 2, Card 30", D2_C30);
    boardsMap.put("Deck 2, Card 31", D2_C31);
    boardsMap.put("Deck 2, Card 33", D2_C33);
    boardsMap.put("Deck 2, Card 34", D2_C34);
    boardsMap.put("Deck 2, Card 35", D2_C35);
    boardsMap.put("Deck 2, Card 36", D2_C36);
    boardsMap.put("Deck 2, Card 37", D2_C37);
    boardsMap.put("Deck 2, Card 39", D2_C39);
    boardsMap.put("Deck 2, Card 40", D2_C40);
    BOARDS = Collections.unmodifiableMap(boardsMap);
  }

  /** Main routine shows a little catalog of the boards. */
  public static void main(String[] args) {
    for (final String name : BOARDS.keySet()) {
      final BoardState board = BOARDS.get(name);
      System.out.printf("%s\n%s\n\n", name, board.toString(" "));
    }
  }
}

