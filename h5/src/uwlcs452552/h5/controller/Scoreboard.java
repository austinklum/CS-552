// Scoreboard.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.Map;
import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 *  Keep score among agents with multiple games.
 */
public class Scoreboard {
  private final Map<Object,Double> points = new HashMap<>();
  private final Object[] ids;

  public Scoreboard(Object[] ids) {
    this.ids = ids;
    for(final Object id : ids) {
      points.put(id, 0.0);
    }
  }

  public void award(Object id, int score) {
    points.put(id, score+points.get(id));
  }

  public double score(Object id) { return points.get(id); }

  public void foreach(BiConsumer<Object,Double> f) {
    for(final Object id : ids) {
      f.accept(id, points.get(id));
    }
  }
}

