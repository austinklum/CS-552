// PureRandomAgent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.players;
import java.util.LinkedList;
import java.util.Random;
import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.TilePosition;

/**
 *  A simple class modeling an agent who plays randomly, and who tends
 *  to lose quickly.
 */
public class PureRandomAgent extends RandomAgent implements Agent {

  private final LinkedList<Tile> hand = new LinkedList<>();

  @Override public Move play(Board board, History history) {
    final int tileIndex = getRandom().nextInt(hand.size());
    final TilePosition spot = board.nextPlaySlot();
    final Move result = new Move(spot.col(), spot.row(), hand.get(tileIndex),
                                 getRandom().nextInt(4));
    hand.remove(tileIndex);
    return result;
  }

  @Override public void receiveTile(Tile tile) {
    hand.add(tile);
  }

  public static final AgentFactory FACTORY = new AgentFactory() {
      public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                            int maxDrawSeconds, int maxTurnSeconds,
                            Object id) {
        return new PureRandomAgent();
      }
    };
}
