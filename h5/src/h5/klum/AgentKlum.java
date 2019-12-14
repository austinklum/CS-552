package h5.klum;

import static uwlcs452552.h5.Util.randomizeArray;

import java.awt.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.TilePosition;
import uwlcs452552.h5.Tiles;

public class AgentKlum implements Agent {
	
	private static final int MAX_DEPTH = 2;
	private final int boardSize;
	private final Tile[] initDeck;
	private final int maxInitSeconds;
	private final int maxDrawSeconds;
	private final int maxTurnSeconds;
	private final Object id;
	
	private LinkedList<Tile> hand;
	private Tile[] deck;
	private Move bestMove;
	private HashMap<Integer,LinkedList<Move>> tileMoves;
	
	/*      boardSize       boardSize-1     4 */
	/** Position(row,col) =>  TileID  =>  Move[]*/
	private HashMap<String, HashMap<Integer, LinkedList<Move>>> possibleMoves;
	
	private int processTilesCount;
	/* Future Variables?
		private int whatRound = 1;
		private LinkedList<Move> plan;
		private boolean failedInit;
	*/

	public AgentKlum(int boardSize, Tile[] deck, int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds, Object id) {
		
		this.boardSize = boardSize;
		this.deck = deck;
		this.initDeck = deck;
		this.maxInitSeconds = maxInitSeconds;
		this.maxDrawSeconds = maxDrawSeconds;
		this.maxTurnSeconds = maxTurnSeconds;
		this.id = id;
		
		hand = new LinkedList<>();
		
        try {
			init();
		} catch (InterruptedException e) {
			// Unable to completely init everything
			// failedInit = true;
		} 
	}



	@Override
	public Move play(Board board, History history) {
		Move defaultedMove = null;
		bestMove = null;
		try {
			decideTurn(board,history);
		} catch (InterruptedException e) {
			
		} 
		
		if(bestMove == null) {
			System.out.println("defaulted bestMove to first thing I could find!");
			final TilePosition spot = board.nextPlaySlot();
			final int col=spot.col(), row=spot.row();
			System.out.println("@ (" + row + "," + col + ")");
			defaultedMove = new Move(row, col, hand.getFirst(), 0);
			hand.remove(defaultedMove.getTile());
			return defaultedMove;
		} else {
			System.out.println("Best Move ever!");
			hand.remove(bestMove.getTile());
			return bestMove;
		}

	}

	@Override
	public void receiveTile(Tile tile) {
		try {
			drawTile(tile);
		} catch (InterruptedException e) {
			// Ran out of time. Nothing to do but add to hand
		} finally {
			hand.add(tile);
		}
	}
	
	
	/**
	 * Adds pertinent information to Agent about tile given with maxDrawSeconds - 1 as time limit
	 * @param tile Tile being received
	 * @throws InterruptedException 
	 */
	private void drawTile(Tile tile) throws InterruptedException {
		CompletionService<Boolean> service = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(1));
		Future<Boolean> future = service.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return true;
			}
		});
		if(service.poll(timeLimit(maxDrawSeconds), TimeUnit.MILLISECONDS) == null) {
			future.cancel(true);
		}
	}
	
	
	
	/**
	 * Creates a timed task to process the deck with maxInitSeconds - 1 as time limit
	 */
	private void init() throws InterruptedException {
		CompletionService<Boolean> service = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(1));
		Future<Boolean> future = service.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				processDeck();
				return true;
			}
		});
		if(service.poll(timeLimit(maxInitSeconds), TimeUnit.MILLISECONDS) == null) {
			future.cancel(true);
		}
	}

	private int timeLimit(int timeInSeconds) {
		return (timeInSeconds-1)*1000;
	}
	

	/**
	 * Decides best move with maxTurnSeconds - 1 as time limit
	 * @param board Current Board State
	 * @param history History of past moves
	 * @throws InterruptedException 
	 */
	private void decideTurn(Board board, History history) throws InterruptedException {
		CompletionService<Boolean> service = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(1));
		Future<Boolean> future = service.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				minimax(board, history, hand);
				//randomAvoidLoss(board,history);
				return true;
			}
		});
		if(service.poll(timeLimit(maxTurnSeconds), TimeUnit.MILLISECONDS) == null) {
			//System.out.println("decideTurns: Time's up!");
			future.cancel(true);
		}
	}
	
	private void minimax(Board board, History history, LinkedList<Tile> myHand) {
		final TilePosition spot = board.nextPlaySlot();
	    final int col = spot.col(),
	    		  row = spot.row();
	    rec(board,history,null,myHand,2);
		/*
		HashMap<Move, Board> moves = getFilteredMoves(board, history, myHand);
	    HashMap<Double, Move> utilityToMove = getUtilityToMove(board, moves, 0);
	    double bestUtil = getBestUtility(utilityToMove);
	    
	    // Get the position => get tile by ID => get move in linked list by rotation
	    bestMove = possibleMoves.get(getPositionHash(row,col)).get(utilityToMove.get(bestUtil).getTile().getId()).get(utilityToMove.get(bestUtil).getRotation());
	    
	    

	    Tile bestTile = myHand.get(myHand.indexOf(bestMove.getTile()));
		myHand.remove(bestMove.getTile());
		if(myHand.isEmpty()) { return; }
		moves = getFilteredMoves(board, history, myHand);
	    utilityToMove = getUtilityToMove(board, moves, bestUtil);
		
	    bestUtil = getBestUtility(utilityToMove);
		
	    //bestMove = possibleMoves.get(getPositionHash(row,col)).get(utilityToMove.get(bestUtil).getTile().getId()).get(utilityToMove.get(bestUtil).getRotation());

	    
		//minimax(moves.get(bestMove), history, myHand);
		// minimax recursive
		// run this same process multiple times
		// Look at best move for each level
		// Keep going forward until out of tiles
		// remvove what we think is the bestMove from our hand
	    int iterCount = 2;
	    if(hand.size() < 2) {
	    	iterCount = hand.size();
	    }
	    LinkedList<Tile> removedTiles = new LinkedList<>();
	    // List of size hand.size();
	    // follow path to depth 3
	    // add total utility to the parent tile
	    //Generate all board states for next 3 tiles
	    */
	    /*
	    for(int i = MAX_DEPTH; i > 0; i--) {
	    	// Generate all move paths
	    	for(Move move : getMovesInHand(row,col,myHand)) {
	    		Board nextBoard = board.apply(move);
	    		if(filterOut(board, nextBoard, history, move)) {
	    			break;
	    		}
	    		evaluateUtility(move, board, nextBoard);
	    	}
	    }
	    
		
		HashMap<Move, Board> moves = getFilteredMoves(board, history, myHand);
//	    HashMap<Double, Move> utilityToMove = getUtilityToMove(board, moves, 0);

		for(Move move : moves.keySet()) {
			Board nextBoard = board.apply(move);
			
		}
		
	    for(final Move move : getMovesInHand(row, col, myHand)) {
	    	Board nextBoard = board.apply(move);
	    	if(!filterOut(board, nextBoard, history, move)) {
	    		moves.put(move, nextBoard);
	    	}
	    }
	    if(moves.isEmpty()) {
	    	Move defaultMove = new Move(row, col, hand.getFirst(), 0);
	    	moves.put(defaultMove, board.apply(defaultMove));
	    }
	    
	   double bestUtility = -1;
	   //double utility = -1;
	   Move move = null;
	    for(final Move myMove : getMovesInHand(row,col,myHand)) {
			double utility = rec(board, history, myMove, myHand, MAX_DEPTH);
			
			if(bestUtility < utility) {
				bestUtility = utility;
				bestMove = move;
			}
			
	    } 
	
	    */
	}

	
	private double rec(Board board, History history, Move myMove, LinkedList<Tile> myHand, int depth) {
		
		if (depth == 0) {
			return 1;
		}
		
		final TilePosition spot = board.nextPlaySlot();
	    final int col = spot.col(),
	    		  row = spot.row();
		
	    double bestUtility = Double.MIN_VALUE;
	    Move bestChoice = null;
		for(final Move move : getMovesInHand(row, col, myHand)) {
	    	Board nextBoard = board.apply(move);
	    	if(!filterOut(board, nextBoard, history, move)) { 
	    		LinkedList<Tile> temp = new LinkedList<>();
	    		for(Tile tile : myHand) {
	    			if (tile.getId() != move.getTile().getId()) {
	    				temp.add(tile);
	    			}
	    		}
	    		double utility = rec(nextBoard,history,move, temp, depth-1) + evaluateUtility(move, board, nextBoard);
	    		if(bestUtility < utility) {
	    			bestUtility = utility;
	    			bestChoice = move;
	    		}
	    	}
	    }
		bestMove = bestChoice;
		return bestUtility;
	}


	private double getBestUtility(HashMap<Double, Move> utilityToMove) {
		double bestUtil = -1;
	    for(double util : utilityToMove.keySet()) {
	    	if(bestUtil < util) {
	    		bestUtil = util;
	    	}
	    }
		return bestUtil;
	}
	
//	private LinkedList<Move> minimaxRec(Board board, History history, LinkedList<Tile> myHand, double utilitySoFar, LinkedList<Move> moveSequence) {
//		if(myHand.isEmpty()) { return null; }
//		LinkedList<Move> list = new LinkedList<>();
//		//list.addAll(minimaxRec)
//		list.add(bestMove);
//		return (minimaxRec(board, history, myHand, utilitySoFar, moveSequence));
//		
//		HashMap<Move, Board> moves = getFilteredMoves(board, history, myHand);
//	    HashMap<Double, Move> utilityToMove = getUtilityToMove(board, moves, utilitySoFar);
//	    
//	    double bestUtil = getBestUtility(utilityToMove);
//		
//		return 0;
//	}


	/** Gets a HashMap of with key utility and value move */
	private HashMap<Double, Move> getUtilityToMove(Board board, HashMap<Move, Board> moves, double utilitySoFar) {
		HashMap<Double, Move> utilityToMove = new HashMap<>();
	    for (final Move move : moves.keySet()) {
	    	Board nextBoard = moves.get(move);
	    	utilityToMove.put(evaluateUtility(move, board, nextBoard) + utilitySoFar, move);
	    }
		return utilityToMove;
	}



	private HashMap<Move, Board> getFilteredMoves(Board board, History history, LinkedList<Tile> myHand) {
		final TilePosition spot = board.nextPlaySlot();
	    final int col = spot.col(),
	    		  row = spot.row();
	    HashMap<Move, Board> moves = new HashMap<>();
	   
	    for(final Move move : getMovesInHand(row, col, myHand)) {
	    	Board nextBoard = board.apply(move);
	    	if(!filterOut(board, nextBoard, history, move)) {
	    		moves.put(move, nextBoard);
	    	}
	    }
	    if(moves.isEmpty()) {
	    	Move defaultMove = new Move(row, col, hand.getFirst(), 0);
	    	moves.put(defaultMove, board.apply(defaultMove));
	    }
		return moves;
	}

	private double evaluateUtility( final Move move, Board board, Board nextBoard) {
		double value = 0;
		//value += weightEdges(move, nextBoard);
		//value += killOthers(move, board, nextBoard);
		//value += weightOptions(move, nextBoard);
		value += avoidAdjecentPlayers(move,nextBoard);
		return value;
	}

	private double avoidAdjecentPlayers(Move move, Board nextBoard) {
		java.util.List<Object> agents = nextBoard.getAgentIDs();
		double value = 0;
		TilePosition spot = nextBoard.getAgentPosition(id).getOppositeTilePosition();
	    final int col = spot.col(),
	    		  row = spot.row();
	    agents.remove(id);
		for(Object agent : agents) {
			TilePosition agentsSpot = nextBoard.getAgentPosition(agent).getOppositeTilePosition();
			if(agentsSpot.col() == col && agentsSpot.row() == row) {
				value -= 10;
				//System.out.println("AHH, " + agent + " is at (" + row  + ":" + agentsSpot.row() + "," + col + ":" + agentsSpot.col());
			}
		}
		return value;
	}
	
	private double weightOptions(Move move, Board nextBoard) {
		double options = 0;
		Tile[] tiles = Tiles.standardTileSet();
		final TilePosition spot = nextBoard.nextPlaySlot();
	    final int col = spot.col(),
	    		  row = spot.row();
		for(Tile tile : tiles) {
			for(int i = 0; i < 4; i++) {
				if(nextBoard.apply(new Move(row,col,tile,i)).isInGame(id)) {
					options++;
				}
			}
		}
		return options * 1.25;
	}
	
	private double weightEdges(Move move, Board nextBoard) {
		double colWeight = Math.abs(nextBoard.getAgentPosition(id).col() - boardSize/2);
		double rowWeight = Math.abs(nextBoard.getAgentPosition(id).row() - boardSize/2);
		double diff = Math.abs(colWeight - rowWeight);
		if (diff == 0) {
			return 0;
		} else if (diff == boardSize) {
			return 0;
		}
		
		return colWeight + rowWeight;
		
	}
	
	private double killOthers(Move move, Board board, Board nextBoard) {
		if(nextBoard.getActiveAgentCount() < board.getActiveAgentCount()) {
			return 10;
		}
		return 0;
	}
	
	private boolean filterOut(Board board, Board nextBoard, History history, Move move) {
		if (!nextBoard.isInGame(id)) return true;
		// if(isSymmetricToOtherMoves) return false;
		return false;
	}
	
	/**
	 * Adds pertinent information to Agent about tile given
	 * 
	 * Creates all possible moves O(4*boardSize^2)
	 * */
	private void processTile(Tile tile) {
		// Create a HashMap by position to all possible moves at that position filtered by tile.
		// Position => Tile => Moves[] 
		 for (int row = 0; row < boardSize; row++) {
			 for (int col = 0; col < boardSize; col++) {
				 final LinkedList<Move> moves = new LinkedList<>();
			      for(int rotation = 0; rotation < 4; rotation++) {
				       moves.add(new Move(row, col, tile, rotation)); 
				  }
			   moves.sort(new Comparator<Move>() {
					@Override
					public int compare(Move move1, Move move2) {
						return move1.getRotation() - move2.getRotation();
					}
			   });
			      possibleMoves.get(getPositionHash(row, col)).put(tile.getId(), moves);
			 }
			 
		 }
	}
	
	private String getPositionHash(int row, int col) {
		return row + "," + col;
	}
	
	private LinkedList<Move> getMovesInHand(int row, int col, LinkedList<Tile> myHand){
		LinkedList<Move> moves = new LinkedList<>();
		for (Tile tile : myHand) {
			moves.addAll(possibleMoves.get(getPositionHash(row,col)).get(tile.getId()));
		}
		return moves;
	}
	
	private void randomAvoidLoss(Board board, History history) {
	    final int tileCount = hand.size();
	    if (tileCount == 0) {
	      throw new IllegalStateException("Agent asked to play without tiles");
	    }

	    final TilePosition spot = board.nextPlaySlot();
	    final int col=spot.col(), row=spot.row();

	    // Look through the moves, and return the first one where we
	    // survive the move.
	    LinkedList<Move> moves = getMovesInHand(row,col,hand);
	    for(final Move move : moves) {
	      if (board.apply(move).isInGame(id)) {
	        hand.remove(move.getTile());
	        bestMove = move;
	        return;
	      }
	    }

	    // Nothing keeps us alive, just pick one of the moves.
	    hand.remove(moves.getFirst().getTile());
	    bestMove = moves.getFirst();
	    return;
	}
	
	/**
	 * Adds pertinent information to Agent about entire deck 
	 * */
	private void processDeck() {
		possibleMoves = new HashMap<>(deck.length);
		 for (int row = 0; row < boardSize; row++) {
			 for (int col = 0; col < boardSize; col++) {
				 possibleMoves.put(getPositionHash(row, col), new HashMap<>(boardSize));
			 }
		 }
		for(Tile tile : deck) {
			processTile(tile);
			processTilesCount++;
		}
	}



	
	  public static final AgentFactory FACTORY = new AgentFactory() {
	      public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
	                            int maxDrawSeconds, int maxTurnSeconds,
	                            Object id) {
	        return new AgentKlum(boardSize, deck, maxInitSeconds, maxDrawSeconds, maxTurnSeconds, id);
	      }
	    };
	
}
