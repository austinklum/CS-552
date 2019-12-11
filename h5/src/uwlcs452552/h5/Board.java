// Board.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The state of the game at some point in play.
 */
public class Board {
  /**
   *  The AgentPosition of a player is on a played tile (or initially,
   *  off the edge of the board).  Methods to advance the player on a
   *  path should /first/ find the agent position on the tile opposite
   *  the current spot, and /then/ move it to the other end of the
   *  tile.  The check for finding the end of the path should be after
   *  finding the position opposite --- if it is to an empty grid
   *  spot, then there is no further advancing; if it is off the
   *  board, then the path leads over the edge.
   */
  private final Map<Object,AgentPosition> players;
  private final Move[][] grid;
  private final Object toPlay;
  private final Order order;

  public Board(Move[][] grid, Map<Object,AgentPosition> players,
               Object toPlay, Order order) {
    this.grid = grid;
    this.players = players;
    this.toPlay = toPlay;
    this.order = order;
  }

  /** Return the tile at a particlar slot in the grid. */
  public Move getPlacedTile(int x, int y) { return grid[x][y]; }

  /** Return the tile at a particlar slot in the grid. */
  public Tile getTile(int x, int y) { return getPlacedTile(x,y).getTile(); }

  /** Return the ID of the next agent to play. */
  public Object getActive() { return toPlay; }

  /** Return the ID of the next agent to play. */
  public boolean isInGame(Object id) { return getAgentPosition(id) != null; }

  /**
   * Return the number of clockwise turns of the tile at a particlar
   * slot in the grid.
   */
  public int getRotation(int x, int y) {
    return getPlacedTile(x,y).getRotation();
  }

  /** Return the current position of a player on the board. */
  public AgentPosition getAgentPosition(Object id) { return players.get(id); }

  /** Return the IDs of the agents currently in the game. */
  public List<Object> getAgentIDs() {
    final List<Object> ids = new ArrayList<Object>();
    for(final Object id : players.keySet()) {
      ids.add(id);
    }
    return ids;
  }

  /** Return the number of agents currently in the game. */
  public int getActiveAgentCount() {
    return players.size();
  }

  /** Return the current position of a player on the board. */
  public int size() { return grid.length; }

  /** Return the current position of a player on the board. */
  public boolean isFinal() { return toPlay == null; }

  /** Return the location on the grid of the next place to play. */
  public TilePosition nextPlaySlot() {
    return getAgentPosition(toPlay).getOppositeTilePosition();
  }

  /** Derive a new board for when a player forfeits. */
  public Board forfeit(Object id, Agent player) {
    // Construct new players map, and populate it with anyone except
    // the forfeiter
    final Map<Object,AgentPosition> newPlayers = new HashMap<>();
    for (final Map.Entry<Object,AgentPosition> entry : players.entrySet()) {
      final Object key = entry.getKey();
      final AgentPosition val = entry.getValue();
      if (key != id) {
        newPlayers.put(key, val);
      }
    }

    // Build/return new Board
    return new Board(grid, newPlayers, order.getNext(id,newPlayers), order);
  }

  /**
   * Construct and return the board arising from a particular move.
   */
  public Board apply(Move move) {

    // If one or fewer players, bomb
    if (isFinal()) {
      throw new IllegalStateException
          ("Cannot move with zero or one players left");
    }

    // Construct new grid from old grid
    final Move[][] newGrid = new Move[grid.length][grid.length];
    for(int i=0; i<grid.length; i++) {
      for(int j=0; j<grid.length; j++) {
        newGrid[i][j] = grid[i][j];
      }
    }

    // Add move to new grid
    final TilePosition where = nextPlaySlot();
    final int playX = where.col(), playY = where.row();
    newGrid[playX][playY] = move;

    // Construct new players map
    final Map<Object,AgentPosition> newPlayers = new HashMap<>();

    // Iterate through players
    for (final Map.Entry<Object,AgentPosition> entry : players.entrySet()) {
      final Object id = entry.getKey();
      final AgentPosition oldSpot = entry.getValue();
      // System.out.printf("Advancing %s at %s\n", id, oldSpot);
      // System.out.printf("- Play was at %s\n", where);

      // If opposite new tile, advance
      if (oldSpot.isOpposite(where)) {
        // System.out.printf("- Is opposite player\n");
        final Optional<AgentPosition>
            newSpot = oldSpot.advanceInGrid(newGrid);
        // System.out.printf("- New spot is %s\n", newSpot);

        // If still on the board (meaning that the Optional wrapper
        // answers the isPresent() method), update our map the new
        // location
        if (newSpot.isPresent()) {
          newPlayers.put(id, newSpot.get());
        }
        // Else player has advanced off board, so delete from game by
        // not adding an entry to the newPlayers map

      } else {
        // If not opposite the new tile, keep the old spot in the new
        // grid
        // System.out.printf("- Is not opposite player\n");
        newPlayers.put(id, oldSpot);
      }
    }

    // Build/return new Board
    return new Board(newGrid, newPlayers,
                     order.getNext(toPlay,newPlayers), order);
  }

  public void toTikZ(PrintWriter out, Map<Object,String> colors,
                     double left, double upper, double w, double h) {
    final int len = grid.length;

    // First draw the whole board
    out.printf("\\draw[very thick] (%f,%f) rectangle (%f,%f);\n",
               left, upper, left+w, upper-h);
    out.printf("\\draw (%f,%f) grid[step=%d] (%f,%f);\n",
               left, upper, len, left+w, upper-h);

    // Next draw the little ticks for the initial positions
    final double tileWidth = w/len, tileHeight = h/len;
    for(int i=0; i<len; i++) {
      for(int j=1; j<3; j++) {
        out.printf("\\draw[very thick] (%f,%f) to (%f,%f);\n",
                   left-0.1*tileWidth, upper-i*tileHeight-j*0.33,
                   left+0.1*tileWidth, upper-i*tileHeight-j*0.33);
        out.printf("\\draw[very thick] (%f,%f) to (%f,%f);\n",
                   left+w-0.1*tileWidth, upper-i*tileHeight-j*0.33,
                   left+w+0.1*tileWidth, upper-i*tileHeight-j*0.33);
        out.printf("\\draw[very thick] (%f,%f) to (%f,%f);\n",
                   left+i*tileWidth+j*0.33, upper-0.1*tileHeight,
                   left+i*tileWidth+j*0.33, upper+0.1*tileHeight);
        out.printf("\\draw[very thick] (%f,%f) to (%f,%f);\n",
                   left+i*tileWidth+j*0.33, upper-h-0.1*tileHeight,
                   left+i*tileWidth+j*0.33, upper-h+0.1*tileHeight);
      }
    }

    // Then draw the placed tiles
    for(int i=0; i<len; i++) {
      final double useLeft = left + i*tileWidth;
      for(int j=0; j<len; j++) {
        final double useUpper = upper - j*tileHeight;
        final Move played = grid[i][j];
        if (played != null) {
          played.toTikZ(out, useLeft, useUpper, tileWidth, tileHeight);
        }
      }
    }

    // Draw agent positions
    final double wRadius=tileWidth*0.125, hRadius=tileWidth*0.125;
    for(final Object id : getAgentIDs()) {
      String color = colors.get(id);
      if (color == null) { color = "black"; }
      final AgentPosition pos = getAgentPosition(id);
      final int row = pos.col(), col = pos.row(), slot = pos.slot();
      final double
          tileLeft  = left + tileWidth*row,
          tileUpper = upper - tileHeight*col;
      final double
          x = tileLeft + Render.SLOT_X_OFFSET[slot],
          y = tileUpper + Render.SLOT_Y_OFFSET[slot];
      // System.out.printf("%d/%d/%d  %s %s\n", row,col,slot, pos, color);
      // out.printf
      //     ("\\draw[draw=%s] (%f,%f) circle [x radius=%f,y radius=%f];\n",
      //      color, tileLeft, tileUpper, wRadius, hRadius);
      out.printf("\\draw[draw=%s,fill=%s] (%f,%f) circle [x radius=%f,y radius=%f];\n",
                 color, color, x, y, wRadius, hRadius);
    }
  }
}
