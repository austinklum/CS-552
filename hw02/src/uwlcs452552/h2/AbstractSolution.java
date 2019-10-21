package uwlcs452552.h2;
import java.util.ArrayList;
import uwlcs452552.search.SearchFailureException;
import uwlcs452552.h2.model.BoardState;
import uwlcs452552.h2.model.Boards;
import uwlcs452552.h2.model.Move;
import uwlcs452552.h2.BreadthFirstFinder;

/**
 *  Wrapper for the heuristic homework solution.
 */
public abstract class AbstractSolution {
  private final MovesFinder[] moves;

  /**
   *  The constructor takes the implementations of A* with various
   *  heuristics as arguments.
   */
  public AbstractSolution(MovesFinder... moves) {
    this.moves = moves;
  }

  /**
   *  A sample runner for the solution: runs all of the solvers (plus
   *  {@linkplain BreadthFirstFinder BFS}) on all of the sample
   *  boards, and shows the results as a table.  You might find it
   *  useful to extend your version of this method to help you with
   *  the effective branching factor calculations.
   */
  public void run() {
    final int
        samples = Boards.BOARDS.size(),
        solvers = moves.length;

    // Pull the solvers names
    final String[] solverName = new String[solvers];
    for(int i=0; i<solvers; i++) {
      solverName[i] = moves[i].toString();
    }

    // Where we'll capture the data from running the samples
    final BreadthFirstFinder bfs = new BreadthFirstFinder();
    final long[][]
        addedToFrontier = new long[samples][solvers],
        expandedFromFrontier = new long[samples][solvers],
        solverDepth = new long[samples][solvers];
    final long[]
        bfsAdded = new long[samples],
        bfsExpanded = new long[samples],
        bfsDepth = new long[samples];
    final String[] sampleNames = new String[samples];
    long maxAdded=0, maxExpanded=0, maxDepth=0;
    int maxSampleNameLen=3, nextSample=0;

    // Run all the samples
    SAMPLES:
    for(final String sampleKey : Boards.BOARDS.keySet()) {
      final BoardState state = Boards.BOARDS.get(sampleKey);
      sampleNames[nextSample] = sampleKey;
      if (maxSampleNameLen < sampleKey.length()) {
        maxSampleNameLen = sampleKey.length();
      }

      // Run under BFS, set the baselines
      final ArrayList<Move> bfsResult;
      try {
        bfsResult = bfs.search(state).fillPath(new ArrayList<Move>());
      } catch (SearchFailureException sfe) {
        System.err.println
            ("Warning: BFS failed for " + sampleKey + ", skipping board");
        bfsAdded[nextSample] = -1;
        bfsExpanded[nextSample] = -1;
        bfsDepth[nextSample] = -1;
        for(int i=0; i<solvers; i++) {
          addedToFrontier[nextSample][i] = -1;
          expandedFromFrontier[nextSample][i] = -1;
          solverDepth[nextSample][i] = -1;
        }
        continue SAMPLES;
      }
      final long
          addedBfs = bfs.getLastAddedToFrontier(),
          expandedBfs = bfs.getLastExpandedFromFrontier(),
          depthBfs = bfsResult.size();
      bfsAdded[nextSample] = addedBfs;
      bfsExpanded[nextSample] = expandedBfs;
      bfsDepth[nextSample] = depthBfs;
      if (maxAdded < addedBfs) { maxAdded = addedBfs; }
      if (maxExpanded < expandedBfs) { maxExpanded = expandedBfs; }
      if (maxDepth < depthBfs) { maxDepth = depthBfs; }

      // Run under each of the solvers in the moves array, capture
      // those results.
      SOLVERS:
      for(int i=0; i<solvers; i++) {
        final MovesFinder solver = moves[i];
        final ArrayList<Move> solverResult;
        try {
          solverResult = solver.search(state).fillPath(new ArrayList<Move>());
        } catch (SearchFailureException sfe) {
          System.err.println("Warning: " + solver + " failed for "
                             + sampleKey + ", skipping");
          addedToFrontier[nextSample][i] = -1;
          expandedFromFrontier[nextSample][i] = -1;
          solverDepth[nextSample][i] = -1;
          continue SOLVERS;
        }

        final long
            addedSolver = solver.getLastAddedToFrontier(),
            expandedSolver = solver.getLastExpandedFromFrontier(),
            depthSolver = solverResult.size();
        addedToFrontier[nextSample][i] = addedSolver;
        expandedFromFrontier[nextSample][i] = expandedSolver;
        solverDepth[nextSample][i] = depthSolver;
        if (maxAdded < addedSolver) { maxAdded = addedSolver; }
        if (maxExpanded < expandedSolver) { maxExpanded = expandedSolver; }
        if (maxDepth < depthSolver) { maxDepth = depthSolver; }
      }

      // Next sample number
      nextSample += 1;
    }

    // Prep table elements
    final long
        addedWidth = Math.max(5, // "added"
                              1+Math.round(Math.floor(Math.log10(maxAdded)))),
        expandedWidth = Math.max(3, // "exp"
                                 1+Math.round(Math.floor(Math.log10(maxExpanded)))),
        depthWidth = Math.max(3, // "len"
                              1+Math.round(Math.floor(Math.log10(maxDepth))));
    final String
        sampleTitle = String.format("%%%ds", maxSampleNameLen),
        solverHeaderFmt = String.format(" | %%%ds %%%ds %%%ds",
                                        depthWidth, addedWidth, expandedWidth),
        solverHeader = String.format(solverHeaderFmt, "len", "added", "exp"),
        solverTitle = String.format(" | %%%ds",
                                    2+depthWidth+addedWidth+expandedWidth),
        dataFormat = String.format(" | %%%dd %%%dd %%%dd",
                                   depthWidth, addedWidth, expandedWidth);

    // And the table. Title rows first
    System.out.printf(sampleTitle, "");
    System.out.printf(solverTitle, "BFS");
    for(int col=0; col<solvers; col++) {
      System.out.printf(solverTitle, "[" + (1+col) + "]");
    }
    System.out.println(" |");

    System.out.printf(sampleTitle, "Sample");
    System.out.print(solverHeader);
    for(int col=0; col<solvers; col++) {
      System.out.print(solverHeader);
    }
    System.out.println(" |");

    // Table rows
    for(int row=0; row<samples; row++) {
      System.out.printf(sampleTitle, sampleNames[row]);
      System.out.printf(dataFormat,
                        bfsDepth[row], bfsAdded[row], bfsExpanded[row]);
      for(int col=0; col<solvers; col++) {
        System.out.printf(dataFormat, solverDepth[row][col],
                          addedToFrontier[row][col],
                          expandedFromFrontier[row][col]);
      }
      System.out.println(" |");
    }

    // Solver titles as footnotes
    for(int col=0; col<solvers; col++) {
      System.out.println("[" + (1+col) + "] " + solverName[col]);
    }
  }
}


