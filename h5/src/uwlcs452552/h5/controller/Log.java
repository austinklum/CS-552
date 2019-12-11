// Log.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Tile;

/**
 *  How we keep a record of a game play
 */
public interface Log {
  default public void start() { }
  default public void stop(Optional<Scoreboard> scores) { }

  default public void singleTournamentStart(String name, int repetitions,
                                            int pointsPerGame) { }
  default public void singleTournamentEnd(Scoreboard localScores,
                                          Scoreboard overallScores) { }
  default public void tournamentGameStart(int num, int max, String name) { }
  default public void tournamentGamePost(int num, List<Object> winners) { }
  default public void tournamentGameEnd(Scoreboard localScores,
                                        Scoreboard overallScores) { }

  default public void gameStart(Board startingPosition) { }
  default public void gameEnd(List<Object> winners) { }
  default public void tileDealt(Object id, Agent player, Tile tile) { }
  default public void turnStart(Object id, Agent player, Board board) { }
  default public void turnMove(Object id, Agent player, Board board,
                               Move move) { }
  default public void turnResult(Object id, Agent player, Board board,
                                 Move move, Board result) { }
  default public void turnEnd() { }
  default public void forfeitPregame(Object id, AgentFactory factory,
                                     Throwable t) { }
  default public void
      forfeit(Object id, Agent player,
              Board currentBoard, Board newBoard, Throwable t) { }
  default public void noEliminations() { }
  default public void eliminationsStart() { }
  default public void notEliminated(Object id, Agent player) { }
  default public void eliminated(Object id, Agent player, Set<Tile> tiles) { }
  default public void eliminationsEnd() { }
  default public void alert(String s) { }

  // -----------------------------------------------------------------
  // Shorthand methods

  default public void tournamentGameStart(int num, int max) {
    tournamentGameStart(num, max, "Game "+num);
  }
  default public void stop() { stop(Optional.empty()); }
}
