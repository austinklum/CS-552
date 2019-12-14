// Run.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.test;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import h5.klum.Factory;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Render;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.Tiles;
import uwlcs452552.h5.controller.CompoundTournament;
import uwlcs452552.h5.controller.Log;
import uwlcs452552.h5.controller.Logs;
import uwlcs452552.h5.controller.SingleGame;
import uwlcs452552.h5.controller.SingleTournament;
import uwlcs452552.h5.controller.Tournament;
import uwlcs452552.h5.players.Defective;
import uwlcs452552.h5.players.RandomAvoidLoss;

/**
 * Static method for generating individual grid tiles.
 */
public final class Run {

  public static void main(String[] argv) throws IOException {
    // tileTest();
     //randosTest();
     //randosTourney();
    //randosTourney2();
    // testSlowPlay();
    // testSlowDraw();
    // testSlowInit();
	myTourney(false,1000);
    System.exit(0);
  }

  /**
   *  Random-acting agents playing a tournament with repetitions of a
   *  single game
   */
  public static void myTourney(boolean full, int games) throws IOException {
	String fileName = "testKlum.tex";
    final SingleTournament tourney = new SingleTournament("Test tournament", games);
    final Object[] ids = new Object[] { "Klum", "Beta", "Gamma", "Delta"};
    Log log = null;
    if(full) {
    	log = Logs.latexGameLog(fileName);
    } else {
    	log = Logs.latexTournamentLog(fileName);
    }
    tourney.go(new AgentFactory[] {
    	new Factory(), RandomAvoidLoss.FACTORY,
    	RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
      }, 
      ids,
      new Random(),
      log
    );
   // autoCompileLog();
  }

  private static void autoCompileLog() throws IOException {
	  Runtime.getRuntime().exec("pdfLatex");
	  Runtime.getRuntime().exec("testKlum.pdf");
  }
  
  // TODO Add a tournament type which samples the n-choose-m subsets of
  // the whole population

  /**
   *  Random-acting agents playing a two-phase tournament
   */
  public static void randosTourney2() throws IOException {
    final Tournament tourney
        = new CompoundTournament("Two phases",
                                 new SingleTournament("Test tournament 1", 5000),
                                 new SingleTournament("Test tournament 2", 6000));
    final Object[] ids = new Object[] { "Alpha", "Beta", "Gamma", "Delta" };
    tourney.go(new AgentFactory[] {
        RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY,
        RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
      },
      ids, new Random(), Logs.latexTournamentLog("test.tex"));
  }

  /**
   *  Random-acting agents playing a tournament with repetitions of a
   *  single game
   */
  public static void randosTourney() throws IOException {
    final SingleTournament tourney = new SingleTournament("Test tournament", 1);
    final Object[] ids = new Object[] { "Klum", "Beta", "Gamma", "Delta" };
    tourney.go(new AgentFactory[] {
    	new Factory(), RandomAvoidLoss.FACTORY,
        RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
      },
      ids, new Random(), Logs.latexGameLog("testKlum.tex"));
  }

  /**
   *  Have some random-acting agents play a game
   */
  public static void randosTest() throws IOException {
    final Log log = Logs.latexGameLog("testKlum.tex");
    final SingleGame
        game = new SingleGame(new AgentFactory[] {
            new Factory(), RandomAvoidLoss.FACTORY,
            RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
          }, log);
    game.go(log);
  }

  /**
   *  Expect a slow-initializing agent to be disqualified.
   */
  public static void testSlowInit() throws IOException {
    final Log log = Logs.latexGameLog("test.tex");
    final SingleGame
        game = new SingleGame(new AgentFactory[] {
            RandomAvoidLoss.FACTORY, Defective.DELAYED_INIT,
            RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
          }, log);
    game.go(log);
  }

  /**
   *  Expect a slow-playing agent to be disqualified.
   */
  public static void testSlowDraw() throws IOException {
    final Log log = Logs.latexGameLog("test.tex");
    final SingleGame
        game = new SingleGame(new AgentFactory[] {
            RandomAvoidLoss.FACTORY, Defective.DELAYED_DRAW,
            RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
          }, log);
    game.go(log);
  }

  /**
   *  Expect a slow-playing agent to be disqualified.
   */
  public static void testSlowPlay() throws IOException {
    final Log log = Logs.latexGameLog("test.tex");
    final SingleGame
        game = new SingleGame(new AgentFactory[] {
            RandomAvoidLoss.FACTORY, Defective.DELAYED_PLAY,
            RandomAvoidLoss.FACTORY, RandomAvoidLoss.FACTORY
          }, log);
    game.go(log);
  }

  /**
   *  Test method for generating all of the tiles decribed in this
   *  file, to see if we raise an exception when some data entry or
   *  tile generation error leads to a malformed tile.
   */
  public static void tileTest() throws IOException {
    System.out.println(Math.floorMod(-2,8));
    System.out.println("Standard tiles");
    final Tile[] tiles = Tiles.standardTileSet();
    for(final Tile t : tiles) {
      System.out.println("- " + t);
    }

    // Generate 10,000,000 tiles to help us believe that the random
    // tile generator produces only valid ones (there are
    // 7!x5!x3!=3,628,800 possible tiles, so 10,000,000 should
    // actually try most of them).
    for(int i=0; i<10000000; i++) {
      Tiles.randomTile(i);
    }

    Render.writeSimpleLaTeX
        ("test.tex",
         (PrintWriter out) -> {
          out.println("\\begin{tikzpicture}[x=1cm,y=1cm]\n");
          for(int i=0; i<tiles.length; i++) {
            for(int j=0; j<4; j++) {
              final Move m = new Move(i, j, tiles[i], j);
              m.toTikZ(out, j, -i);
            }
          }
          out.println("\\end{tikzpicture}\n");
        });
  }
}

