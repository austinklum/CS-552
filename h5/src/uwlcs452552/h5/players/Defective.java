// Defective.java
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
import static uwlcs452552.h5.Util.randomizeArray;

/**
 *  Definitions of defective agents, for debugging various failure
 *  cases.
 */
public class Defective {

  /**
   *  Factory for agents whose instantiation incurs a ten-second
   *  delay.
   */
  public static final AgentFactory DELAYED_INIT = new AgentFactory() {
      @Override public Agent
          getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                   int maxDrawSeconds, int maxTurnSeconds, Object id) {
        return new DelayedCreation(id);
      }

      class DelayedCreation extends RandomAvoidLoss {
        public DelayedCreation(Object id) {
          super(id);
          try {
            Thread.sleep(10000);
          } catch (InterruptedException e) { }
        }
      }
    };

  /**
   *  Factory for agents which incur a ten-second delay when receiving
   *  a card.
   */
  public static final AgentFactory DELAYED_DRAW = new AgentFactory() {
      @Override public Agent
          getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                   int maxDrawSeconds, int maxTurnSeconds, Object id) {
        return new RandomAvoidLoss(id) {
          @Override public void receiveTile(Tile tile) {
            try {
              Thread.sleep(10000);
            } catch (InterruptedException e) { }
            super.receiveTile(tile);
          }
        };
      }
    };

  /**
   *  Factory for agents which incur a ten-second delay when playing a
   *  card.
   */
  public static final AgentFactory DELAYED_PLAY = new AgentFactory() {
      @Override public Agent
          getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                   int maxDrawSeconds, int maxTurnSeconds, Object id) {
        return new RandomAvoidLoss(id) {
          @Override public Move play(Board board, History history) {
            try {
              Thread.sleep(10000);
            } catch (InterruptedException e) { }
            return super.play(board, history);
          }
        };
      }
    };
}
