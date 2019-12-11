// Logs.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.AgentPosition;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Render;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.TilePosition;

/**
 *  Some standard record-keepers.
 */
public final class Logs {

  /**
   *  A log which does nothing
   */
  public static final Log NO_LOGGING = new Log() { };

  /**
   *  Construct a log which records the play-by-play details of each
   *  game via LaTeX.
   */
  public static final Log latexGameLog(final String filename) {
    return latexLog(filename, true);
  };

  /**
   *  Construct a log which records the summary details of a
   *  tournament via LaTeX.
   */
  public static final Log latexTournamentLog(final String filename) {
    return latexLog(filename, false);
  };

  /**
   *  Construct a log which records selected details via LaTeX.
   */
  public static final Log latexLog(final String filename,
                                   final boolean playByPlay) {
    return new Log() {
      private final String fname = filename;
      private PrintWriter out = null;
      private final Map<Object,String>
          agentColors = new HashMap<Object,String>();
      private int tilesDealt = 0;

      @Override public void start() {
        try {
          final FileWriter fout = new FileWriter(fname);
          final BufferedWriter buffout = new BufferedWriter(fout);
          this.out = new PrintWriter(buffout);
        } catch (IOException e) {
          throw new RuntimeException("Cannot create log", e);
        }
        out.println("\\documentclass{article}\n");
        out.println("\\usepackage[margin=15mm]{geometry}\n");
        out.println("\\usepackage{multicol}\n");
        out.println(Render.LATEX_DECLS);
        out.println("\\begin{document}\n");
      }
      @Override public void stop(Optional<Scoreboard> scoreboard) {
        if (scoreboard.isPresent()) {
          out.println("\\section*{Final scores}\n");
          out.println("\\begin{center}");
          out.println("\\begin{tabular}[t]{ll}");
          scoreboard.get().foreach
              ((id, score) -> out.printf("%s & %,.2f\\\\\n", id, score));
          out.println("\\end{tabular}");
          out.println("\\end{center}");
        }
        out.println("\\end{document}\n");
        out.flush();
        out.close();
      }

      public void printAgent(final Object id) {
        out.print(id.toString());
        final String color = agentColors.get(id);
        if (color != null) {
          out.printf(" \\textcolor{%s}{\\LARGE$\\bullet$}", color);
        }
      }

      @Override public void tileDealt(Object id, Agent player, Tile tile) {
        if (playByPlay) {
          out.printf("\\item[(%,d)] ", ++tilesDealt);
          printAgent(id);
          out.printf(" draws ");
          out.println("\\begin{tikzpicture}[x=7mm,y=7mm]\n");
          tile.toTikZ(out, 0, 0);
          out.println("\\end{tikzpicture}\n");
        }
      }

      @Override public void singleTournamentStart(String name, int repetitions,
                                                  int pointsPerGame) {
        out.printf("\\section{%s}\n", name);
        if (!playByPlay) {
          out.printf("%d points per game, %,d games.\n",
                     pointsPerGame, repetitions);
          out.println("\\begin{multicols}{3}\\raggedcolumns");
          out.println("\\begin{compactitem}");
        }
      }

      @Override
      public void singleTournamentEnd(Scoreboard localScores,
                                      Scoreboard overallScores) {
        if (!playByPlay) {
          out.println("\\end{compactitem}");
          out.println("\\end{multicols}");
          out.println("Scores from this round:");
          out.println("\\begin{tabular}[t]{ll}");
          localScores.foreach
              ((id, score) -> out.printf("%s & %,.2f\\\\\n", id, score));
          out.println("\\end{tabular}");
        }
      }

      @Override
      public void tournamentGameStart(int num, int max, String name) {
        if (playByPlay) {
          out.printf("\\subsection{%s}\n", name);
        } else {
          out.printf("\\item %s:\n", name);
        }
      }

      @Override public void
          tournamentGamePost(int num, List<Object> winners) {
        if (!playByPlay) {
          String sep = "";
          for(Object winner : winners) {
            out.printf("%s%s", sep, winner);
            sep = ", ";
          }
          out.println();
        }
      }

      // @Override
      // public void tournamentGameEnd(Scoreboard localScores,
      //                               Scoreboard overallScores) {
      // }

      @Override public void gameStart(Board startingPosition) {
        if (playByPlay) {
          out.println("\\begin{multicols}{3}\\raggedcolumns\n");

          int a=0;
          for(final Object id : startingPosition.getAgentIDs()) {
            agentColors.put(id, COLOR_BANK[a++]);
          }

          final int size = startingPosition.size();
          out.println("\\begin{enumerate}\n");
          out.println("\\item[$\\bullet$] Initial position\n");
          out.println("\\begin{center}\n");
          out.println("\\begin{tikzpicture}[x=7mm,y=7mm]\n");
          startingPosition.toTikZ(out, agentColors, 0, 0, size, size);
          out.println("\\end{tikzpicture}\n");
          out.println("\\end{center}\n");
        }
      }

      @Override public void turnStart(Object id, Agent player, Board board) {
        if (playByPlay) {
          final AgentPosition playerPos = board.getAgentPosition(id);
          final TilePosition slot = board.nextPlaySlot();
          out.print("\\item ");
          printAgent(id);
          out.printf(" plays at row %d, col %d:\n", 1+slot.row(), 1+slot.col());
          out.flush();
        }
      }

      @Override public void turnResult(Object id, Agent player, Board board,
                             Move move, Board result) {
        if (playByPlay) {
          final int size = board.size();
          out.println("\\begin{center}\n");
          out.println("\\begin{tikzpicture}[x=7mm,y=7mm]\n");
          result.toTikZ(out, agentColors, 0, 0, size, size);
          out.println("\\end{tikzpicture}\n");
          out.println("\\end{center}\n");
        }
      }

      @Override public void turnEnd() {
      }

      @Override public void forfeitPregame(Object id, AgentFactory factory,
                                           Throwable t) {
        if (playByPlay) {
          out.printf(" %s forfeits following %s\n", id,
                     t.getClass().getSimpleName());
          final String message = t.getMessage();
          if (message != null) {
            out.printf(" (%s)\n", message);
          }
        }
      }

      @Override public void forfeit(Object id, Agent player,
                                    Board currentBoard, Board newBoard,
                                    Throwable t) {
        if (playByPlay) {
          out.printf(" %s forfeits following %s\n", id,
                     t.getClass().getSimpleName());
          final String message = t.getMessage();
          if (message != null) {
            out.printf(" (%s)\n", message);
          }
        }
      }

      // @Override public void noEliminations() {
      //   out.println("No eliminations.\n");
      // }

      @Override public void eliminationsStart() {
        if (playByPlay) {
          out.println("\\begin{compactitem}\n");
        }
      }

      // @Override public void notEliminated(Object id, Agent player) {
      //   out.print("\\par ");
      //   printAgent(id);
      //   out.printf(" not eliminated.");
      // }

      @Override public void eliminated(Object id, Agent player,
                                       Set<Tile> tiles) {
        if (playByPlay) {
          out.print("\\item ");
          printAgent(id);
          out.printf(" eliminated.  Returning %d tile%s to deck", tiles.size(),
                     (tiles.size() == 1 ? "" : "s"));
          if (tiles.size()>0) {
            out.println
                (":\\raggedright\\ \\begin{tikzpicture}[x=7mm,y=7mm]\n");
            int a=0;
            for(final Tile tile: tiles) {
              tile.toTikZ(out, (a++)*1.2, 0, 1, 1);
            }
            out.println("\\end{tikzpicture}\n");
          } else {
            out.println(".");
          }
        }
      }

      @Override public void eliminationsEnd() {
        if (playByPlay) {
          out.println("\\end{compactitem}\n");
        }
      }

      @Override public void gameEnd(List<Object> winners) {
        if (playByPlay) {
          out.printf("\\end{enumerate}\n\\colorbox{black}{\\textcolor{yellow}{\\bfseries\\sffamily{%d winner%s}}}\n",
                     winners.size(), (winners.size()==1 ? "" : "s"));
          String sep = "";
          for (final Object winnerID : winners) {
            out.print(sep);
            printAgent(winnerID);
            sep = ", ";
          }
          out.println(".");
          out.println("\\end{multicols}");

          tilesDealt = 0;
        }
      }

      @Override public void alert(String s) {
        System.out.println(s);
        out.printf("\\fbox{%s}\n", s);
      }
    };
  }

  static final String[] COLOR_BANK = {
    "red", "green", "blue", "gray", "cyan",
    "purple", "lime", "brown", "orange",
    "black", "magenta",
    "yellow", "darkgray", "lightgray", "blue!40",
    "brown!40", "lime!40", "olive", "olive!40",
    "pink", "yellow!100!30", "purple!30", "teal"
  };
}
