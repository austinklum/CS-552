// Tile.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.io.PrintWriter;

/**
 * Model of Tsuro playing tiles.
 *
 * The eight path endpoints on a tile are numbered from 0 to 7 from
 * the upper-left path, and proceeding clockwise.
 */
public class Tile {
  private final int id;
  final int[] pathDest;

  /**
   * Only the controller should create game tiles, so this constructor
   * is package-private.
   */
  public Tile(int id, int[] pathDest) {
    this.id = id;
    this.pathDest = new int[8];
    for(int i=0; i<8; i++) {
      this.pathDest[i] = pathDest[i];

      // Make sure that the pathDest array does, in fact, create four
      // paths
      if (pathDest[pathDest[i]] != i) {
        throw new IllegalArgumentException
            ("Ill-formed tile path " + i + "-" + pathDest[i]
             + "-" + pathDest[pathDest[i]]);
      }
      if (pathDest[i] == i) {
        throw new IllegalArgumentException
            ("Ill-formed tile path " + i + "-" + pathDest[i]);
      }
    }

  }

  /**
   * Accessor for the ID number of this tile.
   */
  public int getId() { return id; }

  /**
   * Accessor for the path point to which a given path point connects.
   */
  public int getDest(int path) { return pathDest[path]; }

  /**
   * Returns a duplicate of the array of path-point connections.
   */
  public int[] getDests() {
    int[] copy = new int[8];
    for(int i=0; i<8; i++) {
      copy[i] = pathDest[i];
    }
    return copy;
  }

  /**
   *  Returns a 7-character representation of the tile, showing the
   *  path points to which the eight points 0-7 are connected.
   */
  @Override public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("T");
    for(final int i : pathDest) {
      sb.append(i);
    }
    return sb.toString();
  }

  /**
   * Write TikZ instructions for drawing this tile
   */
  public void toTikZ(PrintWriter out, double left, double upper) {
    toTikZ(out, left, upper, 1, 1);
  }

  /**
   * Write TikZ instructions for drawing this tile
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

