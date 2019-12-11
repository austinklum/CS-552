package h5.klum;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import uwlcs452552.h5.Agent;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Tile;

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
		
        init(); 
	}



	@Override
	public Move play(Board board, History history) {
		decideTurn(board, history);
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
				
				timer.cancel();
			}
        };

        timer.schedule(task, (maxTurnSeconds-1)*1000);
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
	 * Adds pertinent information to Agent about tile given
	 * */
	private void processTile(Tile tile) {
		
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
	
}
