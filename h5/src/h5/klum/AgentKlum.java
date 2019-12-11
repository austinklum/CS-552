package h5.klum;

import static uwlcs452552.h5.Util.randomizeArray;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.TilePosition;
import uwlcs452552.h5.players.RandomAvoidLoss;

public class AgentKlum implements Agent {
	
	private final int boardSize;
	private final Tile[] initDeck;
	private final int maxInitSeconds;
	private final int maxDrawSeconds;
	private final int maxTurnSeconds;
	private final Object id;
	
	private HashMap<Integer,Tile> hand;
	private Tile[] deck;
	private Move bestMove;
	private LinkedList<Move> plan;
	
	
	public AgentKlum(int boardSize, Tile[] deck, int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds, Object id) {
		
		this.boardSize = boardSize;
		this.deck = deck;
		this.initDeck = deck;
		this.maxInitSeconds = maxInitSeconds;
		this.maxDrawSeconds = maxDrawSeconds;
		this.maxTurnSeconds = maxTurnSeconds;
		this.id = id;
		
		hand = new HashMap<>();
		
        init(); 
	}



	@Override
	public Move play(Board board, History history) {
//		final TilePosition spot = board.nextPlaySlot();
//		final int col=spot.col(), row=spot.row();
//		Tile[] myTiles = null;
//		hand.values().toArray(myTiles);
//		bestMove = new Move(row,col, myTiles[0], 0);
//		decideTurn(board, history);
		randomAvoidLoss(board,history);
		hand.remove(bestMove.getTile().getId(), bestMove.getTile());
		return bestMove;
	}

	@Override
	public void receiveTile(Tile tile) {
		drawTile(tile);
		hand.put(tile.getId(), tile);
	}


	/**
	 * Decides best move with maxTurnSeconds - 1 as time limit
	 * @param board Current Board State
	 * @param history History of past moves
	 */
	private void decideTurn(Board board, History history) {
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				randomAvoidLoss(board, history);
				timer.cancel();
			}
        };

        timer.schedule(task, (maxTurnSeconds-1)*1000);
	}
	

	/**
	 * Adds pertinent information to Agent about tile given
	 * */
	private void processTile(Tile tile) {
		
	}
	
	private void randomAvoidLoss(Board board, History history) {
	    final int tileCount = hand.size();
	    if (tileCount == 0) {
	      throw new IllegalStateException("Agent asked to play without tiles");
	    }

	    final TilePosition spot = board.nextPlaySlot();
	    final int col=spot.col(), row=spot.row();

	    // What moves could we make?
	    final Move[] moves = new Move[4*tileCount];
		int i=0;
	    for(final Tile tile : hand.values()) {
	      for(int rotation=0; rotation<4; rotation++) {
	        moves[i++] = new Move(col, row, tile, rotation);
	      }
	    }

	    // Randomly shuffle the array of moves.
	    randomizeArray(new Random(), moves);

	    // Look through the moves, and return the first one where we
	    // survive the move.
	    for(final Move move : moves) {
	      if (board.apply(move).isInGame(id)) {
	        hand.remove(move.getTile().getId());
	        bestMove = move;
	        return;
	      }
	    }

	    // Nothing keeps us alive, just pick one of the moves.
	    hand.remove(moves[0].getTile().getId());
	    bestMove = moves[0];
	    return;
	}
	
	/**
	 * Adds pertinent information to Agent about entire deck 
	 * */
	private void processDeck() {
		for(Tile tile : deck) {
			processTile(tile);
		}
	}
	
	/**
	 * Adds pertinent information to Agent about tile given with maxDrawSeconds - 1 as time limit
	 * @param tile Tile being received
	 */
	private void drawTile(Tile tile) {
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				processTile(tile);
				timer.cancel();
			}
        };

        timer.schedule(task, (maxDrawSeconds-1)*1000);
	}
	
	
	/**
	 * Creates a timed task to process the deck with maxInitSeconds - 1 as time limit
	 */
	private void init() {
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				processDeck();
				timer.cancel();
			}
        };
        
        timer.schedule(task, (maxInitSeconds-1)*1000);
	}
	
	  public static final AgentFactory FACTORY = new AgentFactory() {
	      public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
	                            int maxDrawSeconds, int maxTurnSeconds,
	                            Object id) {
	        return new AgentKlum(boardSize, deck, maxInitSeconds, maxDrawSeconds, maxTurnSeconds, id);
	      }
	    };
	
}
