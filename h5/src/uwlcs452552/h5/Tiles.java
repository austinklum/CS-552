// Tiles.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;

/**
 * Static method for generating individual grid tiles.
 */
public final class Tiles {

  /**
   *  Return a collection of randomly-generated tiles.  The IDs of the
   *  tiles will run from 0 up to one less than the size of the
   *  collection.
   *
   *  @param count The number of tiles to be generated.
   */
  public static final Tile[] randomTiles(int count) {
    final Tile[] result = new Tile[count];
    for(int i=0; i<count; i++) {
      result[i] = randomTile(i);
    }
    return result;
  }

  private static final java.util.Random gen = new java.util.Random();

  /**
   *  Return a randomly-generated tile.
   *
   *  @param id The ID to be used for the tile.
   */
  public static final Tile randomTile(int id) {
    int[] resultMap = new int[8];

    // First pick where port 0 goes, and set up that path in the
    // resultMap.
    final int dest1 = 1+gen.nextInt(7);
    resultMap[0] = dest1;
    resultMap[dest1] = 0;

    // Now build a six-element array of the remaining ports
    final int[] ports6 = new int[6];
    int nextPorts6 = 0;
    for(int i=1; i<8; i++) {
      if (i != dest1) {
        ports6[nextPorts6++] = i;
      }
    }

    // The next path will run from ports6[0] to a randomly picked
    // value from ports6[1..5]
    final int
        src2 = ports6[0],
        dest2idx = 1+gen.nextInt(5),
        dest2 = ports6[dest2idx];
    resultMap[src2] = dest2;
    resultMap[dest2] = src2;

    // Now build a four-element array of the remaining ports
    final int[] ports4 = new int[4];
    int nextPorts4 = 0;
    for(int i=1; i<6; i++) {
      final int port = ports6[i];
      if (port != dest2) {
        ports4[nextPorts4++] = port;
      }
    }

    // The next path will run from ports4[0] to a randomly picked
    // value from ports4[1..3]
    final int
        src3 = ports4[0],
        dest3idx = 1+gen.nextInt(3),
        dest3 = ports4[dest3idx];
    resultMap[src3] = dest3;
    resultMap[dest3] = src3;

    // Now build a two-element array of the remaining ports
    final int[] ports2 = new int[2];
    int nextPorts2 = 0;
    for(int i=1; i<4; i++) {
      final int port = ports4[i];
      if (port != dest3) {
        ports2[nextPorts2++] = port;
      }
    }

    // The last path runs between these two ports
    final int
        src4 = ports2[0],
        dest4 = ports2[1];
    resultMap[src4] = dest4;
    resultMap[dest4] = src4;

    // Return a tile on the random
    return new Tile(id, resultMap);
  }

  public static final Tile[] standardTileSet() {
    Tile[] result = new Tile[STANDARD_TILE_PATHS.length];
    for(int i=0; i<STANDARD_TILE_PATHS.length; i++) {
      result[i] = new Tile(i, STANDARD_TILE_PATHS[i]);
    }
    return result;
  }

  private static final int[][] STANDARD_TILE_PATHS = {
    { 7, 2, 1, 4, 3, 6, 5, 0 },
    { 4, 6, 3, 2, 0, 7, 1, 5 },
    { 1, 0, 7, 6, 5, 4, 3, 2 },

    { 6, 2, 1, 4, 3, 7, 0, 5 },
    { 4, 5, 3, 2, 0, 1, 7, 6 },
    { 5, 4, 7, 6, 1, 0, 3, 2 },

    { 3, 4, 5, 0, 1, 2, 7, 6 },
    { 4, 2, 1, 7, 0, 6, 5, 3 },
    { 1, 0, 3, 2, 5, 4, 7, 6 },

    { 7, 4, 6, 5, 1, 3, 2, 0 },
    { 5, 7, 3, 2, 6, 0, 4, 1 },
    { 5, 4, 6, 7, 1, 0, 2, 3 },

    { 2, 3, 0, 1, 7, 6, 5, 4 },
    { 2, 3, 0, 1, 6, 7, 4, 5 },
    { 6, 2, 1, 7, 5, 4, 0, 3 },

    { 4, 5, 6, 7, 0, 1, 2, 3 },
    { 2, 7, 0, 5, 6, 3, 4, 1 },
    { 2, 7, 0, 6, 5, 4, 3, 1 },

    { 7, 6, 5, 4, 3, 2, 1, 0 },
    { 5, 2, 1, 4, 3, 0, 7, 6 },
    { 3, 4, 6, 0, 1, 7, 2, 5 },

    { 1, 0, 4, 5, 2, 3, 7, 6 },
    { 4, 7, 6, 5, 0, 3, 2, 1 },
    { 5, 7, 4, 6, 2, 0, 3, 1 },

    { 4, 6, 5, 7, 0, 2, 1, 3 },
    { 6, 5, 4, 7, 2, 1, 0, 3 },
    { 4, 7, 3, 2, 0, 6, 5, 1 },

    { 6, 2, 1, 5, 7, 3, 0, 4 },
    { 7, 5, 4, 6, 2, 1, 3, 0 },
    { 1, 0, 3, 2, 7, 6, 5, 4 },

    { 3, 7, 5, 0, 6, 2, 4, 1 },
    { 3, 6, 5, 0, 7, 2, 1, 4 },
    { 6, 5, 3, 2, 7, 1, 0, 4 },

    { 3, 5, 7, 0, 6, 1, 4, 2 },
    { 7, 4, 5, 6, 1, 2, 3, 0 }
  };
}

