// Move.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.io.PrintWriter;

/**
 * Record class which an agent uses to specify its move.
 */
public class Move {
  private final int row, col;
  private final Tile tile;
  private final int rotation;
  private final int[] pathDest;

  /**
   * Sole constructor, assembling the move record.
   * @param tile The tile to be placed.
   * @param rotation The number of clockwise turns made to the tile
   * when placing it.  This value should be between 0 and 3, inclusively
   */
  public Move(int row, int col, Tile tile, int rotation) {
    this.row = row;
    this.col = col;
    this.tile = tile;
    this.rotation = rotation;

    this.pathDest = new int[8];
    for(int i=0; i<8; i++) {
      this.pathDest[i] =
          Math.floorMod(tile.pathDest[Math.floorMod(i-2*rotation, 8)
                                      ]+2*rotation, 8);
    }
  }

  /**
   * Accessor for the tile to be placed.
   */
  public Tile getTile() { return tile; }

  /**
   * Accessor for the number of clockwise turns made to the tile when
   * placing it.
   */
  public int getRotation() { return rotation; }

  /**
   * Return the slot number connected to the argument slot.
   */
  public int getConnectedSlot(int slot) { return pathDest[slot]; }

  /**
   * Write TikZ instructions for drawing this oriented tile
   */
  public void toTikZ(PrintWriter out, double left, double upper) {
    toTikZ(out, left, upper, 1.0, 1.0);
  }

  /**
   * Write TikZ instructions for drawing this oriented tile
   */
  public void toTikZ(PrintWriter out,
                     double left, double upper, double w, double h) {
    out.printf("\\draw[black,fill=yellow] (%f,%f) rectangle (%f,%f);\n",
               left, upper, left+w, upper-w);
    for(int from=0; from<8; from++) {
      final int to=pathDest[from];
      if (from<to) {
        out.printf("\\draw[out=%d,in=%d,looseness=1,very thick] (%f,%f) to (%f,%f);\n",
                   Render.SLOT_ANGLE[from], Render.SLOT_ANGLE[to],
                   left+Render.SLOT_X_OFFSET[from]*w,
                   upper+Render.SLOT_Y_OFFSET[from]*h,
                   left+Render.SLOT_X_OFFSET[to]*w,
                   upper+Render.SLOT_Y_OFFSET[to]*h);
      }
    }
  }
}
