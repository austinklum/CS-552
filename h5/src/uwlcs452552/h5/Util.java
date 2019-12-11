// Util.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.Random;

/** Miscellaneous utilities. */
public final class Util {
  /** Randomly shuffle the elements of an array in place. */
  public static <A> void randomizeArray(Random rand, A[] arr) {
    final int len = arr.length;
    final Integer[] sorter = new Integer[len];
    for(int i=0; i<len; i++) {
      sorter[i] = rand.nextInt();
    }

    // Selection sort both arrays according to the sorter
    for(int i=0; i<len-1; i++) {
      int leastVal=sorter[i], leastIndex=i;

      for(int j=1; j<len; j++) {
        int thisVal=sorter[j];
        if (thisVal<leastVal) {
          leastVal=thisVal;
          leastIndex=j;
        }
      }

      if (leastIndex>i) {
        swap(sorter, i, leastIndex);
        swap(arr, i, leastIndex);
      }
    }
  }

  private static <A> void swap(A[] arr, int i, int j) {
    final A tmpA = arr[i];
    arr[i] = arr[j];
    arr[j] = tmpA;
  }
}

