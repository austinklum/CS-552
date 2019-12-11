// Tournament.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.Optional;
import java.util.Random;
import uwlcs452552.h5.AgentFactory;

public abstract class Tournament {
  protected final String name;

  public Tournament(String name) {
    this.name = name;
  }

  public void go(AgentFactory[] agentFactories, Object[] ids,
                 Random rand, Log log) {
    final Scoreboard scoreboard = new Scoreboard(ids);
    log.start();
    play(agentFactories, ids, rand, log, scoreboard);
    log.stop(Optional.of(scoreboard));
  }

  public abstract void play(AgentFactory[] agentFactories, Object[] ids,
                            Random rand, Log log, Scoreboard scoreboard);
}
