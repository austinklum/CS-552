// RandomAvoidLoss.java
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
 *  An agent which attempts to selects a non-fatal move (randomly
 *  choosing one if there are several), but which attempts no further
 *  lookahead or planning.
 */
public class RandomAvoidLoss extends RandomAgent implements Agent {
  private final LinkedList<Tile> hand = new LinkedList<>();
  private final Object id;

  public RandomAvoidLoss(Object id) {
    this.id = id;
  }

  @Override public Move play(Board board, History history) {
    final int tileCount = hand.size();
    if (tileCount == 0) {
      throw new IllegalStateException("Agent asked to play without tiles");
    }

    final TilePosition spot = board.nextPlaySlot();
    final int col=spot.col(), row=spot.row();

    // What moves could we make?
    final Move[] moves = new Move[4*tileCount];
    int i=0;
    for(final Tile tile : hand) {
      for(int rotation=0; rotation<4; rotation++) {
        moves[i++] = new Move(col, row, tile, rotation);
      }
    }

    // Randomly shuffle the array of moves.
    randomizeArray(getRandom(), moves);

    // Look through the moves, and return the first one where we
    // survive the move.
    for(final Move move : moves) {
      if (board.apply(move).isInGame(id)) {
        hand.remove(move.getTile());
        return move;
      }
    }

    // Nothing keeps us alive, just pick one of the moves.
    hand.remove(moves[0].getTile());
    return moves[0];
  }

  @Override public void receiveTile(Tile tile) {
    hand.add(tile);
  }

  public static final AgentFactory FACTORY = new AgentFactory() {
      public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                            int maxDrawSeconds, int maxTurnSeconds,
                            Object id) {
        return new RandomAvoidLoss(id);
      }
    };
}
