package uwlcs452552.search.graph;

/**
 *  Thrown when a frontier structure is empty.  Generally this
 *  exception indicates something wrong in the implementation of the
 *  frontier, or else something very wrong in the {@link
 *  GraphSearcher#search search} method: this method arises from the
 *  {@link FrontierStructure#pop pop} method of the frontier
 *  representation, but {@link FrontierStructure#pop pop} is only
 *  called after verifying that {@link FrontierStructure#isEmpty
 *  isEmpty} is false.
 */
public class FrontierEmptyException extends IllegalStateException {
  private static String MESSAGE = "Frontier (unexpectedly) empty";

  public FrontierEmptyException() { super(MESSAGE); }

  /**
   *  Used when the underlying data structure of the frontier throws
   *  an exception.
   *
   * @param cause Exeception caught from a method call on the
   * underlying structure on behalf of the {@link
   * FrontierStructure#pop pop} method.
   */
  public FrontierEmptyException(final Throwable cause) {
    super(MESSAGE, cause);
  }
}
