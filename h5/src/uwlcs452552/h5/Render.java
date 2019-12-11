// Render.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.function.Consumer;

/**
 *  Utilities for drawing the LaTeX representation of boards and games.
 * @see uwlcs452552.h5.Move#toTikZ
 */
public class Render {
  public static final String LATEX_DECLS =
      "\\usepackage{tikz,paralist}\n" +
      "\\usetikzlibrary{\n" +
      "  calc,shapes.multipart,chains,positioning,shadows,\n" +
      "  arrows,arrows.meta,matrix,\n" +
      "  decorations.pathreplacing\n" +
      "}\n";

  public static void writeSimpleLaTeX(String file, Consumer<PrintWriter> c)
      throws IOException {
    final FileWriter fout = new FileWriter(file);
    final BufferedWriter buffout = new BufferedWriter(fout);
    final PrintWriter out = new PrintWriter(buffout);
    out.println("\\documentclass{article}\n");
    out.println("\\usepackage[margin=1in]{geometry}\n");
    out.println(LATEX_DECLS);
    out.println("\\begin{document}\n");
    c.accept(out);
    out.println("\\end{document}\n");
    out.flush();
    out.close();
  }

  public static final double[]
      SLOT_X_OFFSET = { 0.33, 0.66, 1.0,   1.0,   0.66, 0.33, 0.0,   0.0 },
      SLOT_Y_OFFSET = { 0.0,  0.0, -0.33, -0.66, -1.0, -1.0, -0.66, -0.33 };

  public static final int[] SLOT_ANGLE = { -90, -90, 180, 180, 90, 90, 0, 0 };
}
