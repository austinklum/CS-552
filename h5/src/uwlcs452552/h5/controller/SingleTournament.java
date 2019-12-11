// SingleTournament.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.List;
import java.util.Random;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.Tiles;

public class SingleTournament extends Tournament {

  private final int repetitions;
  private final int boardSize;
  private final Tile[] tiles;
  private final int maxInitSeconds;
  private final int maxDrawSeconds;
  private final int maxTurnSeconds;

  public SingleTournament(String name, int repetitions) {
    this(name, repetitions, 6, Tiles.standardTileSet(), 5, 1, 2);
  }

  public SingleTournament(String name, int repetitions,
                          int boardSize, Tile[] tiles, int maxInitSeconds,
                          int maxDrawSeconds, int maxTurnSeconds) {
    super(name);
    this.repetitions = repetitions;
    this.boardSize = boardSize;
    this.tiles = tiles;
    this.maxInitSeconds = maxInitSeconds;
    this.maxDrawSeconds = maxDrawSeconds;
    this.maxTurnSeconds = maxTurnSeconds;
  }

  @Override public void play(AgentFactory[] agentFactories, Object[] ids,
                             Random rand, Log log, Scoreboard overallScores) {
    final Scoreboard roundScores = new Scoreboard(ids);
    log.singleTournamentStart(name, repetitions, agentFactories.length);

    for(int i=0; i<repetitions; i++) {
      log.tournamentGameStart(1+i, repetitions);
      final SingleGame
          game = new SingleGame(agentFactories, ids, boardSize, tiles,
                                maxInitSeconds, maxDrawSeconds, maxTurnSeconds,
                                rand, log);
      final List<Object> winners = game.play();

      log.tournamentGamePost(1+i, winners);
      for(final Object winner : winners) {
        roundScores.award(winner, agentFactories.length/winners.size());
        overallScores.award(winner, agentFactories.length/winners.size());
      }

      log.tournamentGameEnd(roundScores, overallScores);
    }

    log.singleTournamentEnd(roundScores, overallScores);
  }
}
