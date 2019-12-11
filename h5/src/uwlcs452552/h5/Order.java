// Order.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for the order of play in a game
 */
public class Order {
  private Map<Object,Object> nextPlayer = new HashMap<>();

  public Order(Object[] ids) {
    for (int i=1; i<ids.length; i++) {
      nextPlayer.put(ids[i-1], ids[i]);
    }
    nextPlayer.put(ids[ids.length-1], ids[0]);
  }

  public Object getNext(Object current, Map<Object,?> playerSet) {
    if (playerSet.size() <= 1) { return null; }

    do {
      current = nextPlayer.get(current);
    } while (!playerSet.containsKey(current));

    return current;
  }

}
