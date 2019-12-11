// CompoundTournament.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.Random;
import uwlcs452552.h5.AgentFactory;

/**
 *  A tournament with distinct phases, each also represented by a
 *  {@link Tournament} instance.
 */
public class CompoundTournament extends Tournament {
  private final Tournament[] tourneys;

  public CompoundTournament(String name, Tournament... tourneys) {
    super(name);
    this.tourneys = tourneys;
  }

  @Override public void play(AgentFactory[] agentFactories, Object[] ids,
                             Random rand, Log log, Scoreboard scoreboard) {
    for(final Tournament tournament : tourneys) {
      tournament.play(agentFactories, ids, rand, log, scoreboard);
    }
  }
}
