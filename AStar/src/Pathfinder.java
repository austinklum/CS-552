import java.util.List;
import java.util.Optional;

/**
 *  Abstract class describing the required functionality of an A*
 *  implementation project.
 */
public abstract class Pathfinder {

  /**
   *  For your use in debugging this program.
   *
   *  A verbosity level of 0 (or negative) means that <b>nothing</b>
   *  should be written to Standard.out or Standard.err expect when
   *  encountering a fatal error in receiving input.  When I test your
   *  code, I will set the verbosity level to 0.
   *
   *  Higher verbosity levels may be useful to you when debugging your
   *  code, and you are free to make the program write output as you
   *  need at these higher levels.
   */
  public abstract void setVerbose(int level);

  /**
   *  The primary method of this project, described in the separate
   *  specification.
   */
  public abstract FoundPath getPathInfo(String startCity, String endCity);

  /**
   *  Accessor returning the cost of direct travel between two cities,
   *  or the empty {@link java.util.Optional} instance if no direct
   *  route is possible.  This method should simply repeat back the
   *  information provided in a particlar model.
   */
  public abstract Optional<Double>
      getDirectDistance(String startCity, String endCity);

  /**
   *  Accessor returning the list of known city names.  This method
   *  should simply repeat back the information provided in a
   *  particlar model.
   */
  public abstract List<String> getCities();

  /**
   *  Convenience method which returns only the path component of a
   *  call to getPathInfo.
   */
  public final List<String> getPath(String startCity, String endCity) {
    return getPathInfo(startCity, endCity).getPath();
  }

}