// SingleGame.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.AgentPosition;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.Order;
import uwlcs452552.h5.Tile;
import uwlcs452552.h5.Tiles;
import static uwlcs452552.h5.Util.randomizeArray;

/**
 * Record class which an agent uses to specify its move.
 */
public class SingleGame {
  private final AgentFactory[] factories;
  private final Object[] givenIds;
  private final Tile[] tiles;
  private final int boardSize, maxInitSeconds, maxDrawSeconds, maxTurnSeconds;
  private final Random rand;
  private final Log log;

  private boolean separateInitialTiles=false;
  private static final ExecutorService
      executor = Executors.newCachedThreadPool();

  /**
   * Start a standard-sized game with agents instantiated from the
   * given factories.
   */
  public SingleGame(AgentFactory[] agentFactories, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         6, Tiles.standardTileSet(), new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, Random rand, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         6, Tiles.standardTileSet(), rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids, Log log) {
    this(agentFactories, ids, 6, Tiles.standardTileSet(), new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids,
                    Random rand, Log log) {
    this(agentFactories, ids, 6, Tiles.standardTileSet(), rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         boardSize, Tiles.randomTiles(boardSize*boardSize-1),
         new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize,
                    Random rand, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         boardSize, Tiles.randomTiles(boardSize*boardSize-1), rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids,
                    int boardSize, Log log) {
    this(agentFactories, ids, boardSize,
         Tiles.randomTiles(boardSize*boardSize-1),
         new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids,
                    int boardSize, Random rand, Log log) {
    this(agentFactories, ids, boardSize,
         Tiles.randomTiles(boardSize*boardSize-1),
         rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, int maxInitSeconds,
                    int maxDrawSeconds, int maxTurnSeconds, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         maxInitSeconds, maxDrawSeconds, maxTurnSeconds, new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, int maxInitSeconds,
                    int maxDrawSeconds, int maxTurnSeconds, Random rand,
                    Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         maxInitSeconds, maxDrawSeconds, maxTurnSeconds, rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids,
                    int maxInitSeconds, int maxDrawSeconds,
                    int maxTurnSeconds, Log log) {
    this(agentFactories, ids, 6, Tiles.standardTileSet(),
         maxInitSeconds, maxDrawSeconds, maxTurnSeconds, new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids,
                    int maxInitSeconds, int maxDrawSeconds,
                    int maxTurnSeconds, Random rand, Log log) {
    this(agentFactories, ids, 6, Tiles.standardTileSet(),
         maxInitSeconds, maxDrawSeconds, maxTurnSeconds, rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize,
                    Tile[] tiles, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         boardSize, tiles, 5, 1, 3,
         new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize, Tile[] tiles,
                    Random rand, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length),
         boardSize, tiles, 5, 1, 3, rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids, int boardSize,
                    Tile[] tiles, Log log) {
    this(agentFactories, ids, boardSize, tiles, 5, 1, 3, new Random(), log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids, int boardSize,
                    Tile[] tiles, Random rand, Log log) {
    this(agentFactories, ids, boardSize, tiles, 5, 1, 3, rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize, Tile[] tiles,
                    int maxInitSeconds, int maxDrawSeconds,
                    int maxTurnSeconds, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length), boardSize,
         tiles, maxInitSeconds, maxDrawSeconds, maxTurnSeconds, new Random(),
         log);
  }

  public SingleGame(AgentFactory[] agentFactories, int boardSize, Tile[] tiles,
                    int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds,
                    Random rand, Log log) {
    this(agentFactories, basicPlayerNames(agentFactories.length), boardSize,
         tiles, maxInitSeconds, maxDrawSeconds, maxTurnSeconds, rand, log);
  }

  public SingleGame(AgentFactory[] agentFactories, Object[] ids, int boardSize,
                    Tile[] tiles, int maxInitSeconds, int maxDrawSeconds,
                    int maxTurnSeconds, Log log) {
    this(agentFactories, ids, boardSize, tiles, maxInitSeconds, maxDrawSeconds,
         maxTurnSeconds, new Random(), log);
  }

  public SingleGame(AgentFactory[] factories, Object[] givenIds,
                    int boardSize, Tile[] tiles,
                    int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds,
                    Random rand, Log log) {

    // Preconditions checking on constructor arguments
    if (factories.length != givenIds.length) {
      throw new IllegalArgumentException
          ("Received " + factories.length + " agent factories but "
           + givenIds.length + " IDs, need the same number of each");
    }
    if (boardSize*boardSize != tiles.length+1) {
      throw new IllegalArgumentException
          ("For a " + boardSize + "-size board, need "
           + (boardSize*boardSize-1) + " tiles, got " + tiles.length);
    }

    this.factories = factories;
    this.givenIds = givenIds;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.boardSize = boardSize;
    this.maxInitSeconds = maxInitSeconds;
    this.maxDrawSeconds = maxDrawSeconds;
    this.maxTurnSeconds = maxTurnSeconds;
    this.rand = rand;
    this.log = log;

    randomizeArray(rand, this.tiles);
  }

  public boolean getSeparateInitialTiles() { return separateInitialTiles; }
  public void getSeparateInitialTiles(boolean separateInitialTiles) {
    this.separateInitialTiles = separateInitialTiles;
  }

  public List<Object> go(Log log) {
    log.start();
    final List<Object> result = play(log);
    log.stop();
    return result;
  }

  /**
   * @deprecated This method now drops its argument and calls
   * <tt>play()</tt>, which uses the log provided to the constructor.
   */
  @Deprecated public List<Object> play(Log log) {
    return play();
  }

  public List<Object> play() {

    // Extract agents from the agent factories into a preliminary
    // array.  We count the null agents (whether arising from timeouts
    // or from factories which give up), to filter them below.
    final int count = factories.length;
    final Agent[] givenAgents = new Agent[count];
    int nonnullAgents = 0;
    for(int i=0; i<count; i++) {
      final Future<Agent>
          agentTask = AgentMaker.get(factories[i], givenIds[i], boardSize,
                                     tiles, maxInitSeconds, maxDrawSeconds,
                                     maxTurnSeconds);
      Agent agent = null;
      try {
        agent = agentTask.get(maxInitSeconds, TimeUnit.SECONDS);
      } catch (Exception reason) {
        log.forfeitPregame(givenIds[i], factories[i], reason);
      }
      givenAgents[i] = agent;
      if (agent != null) { nonnullAgents += 1; }
    }

    // Create arrays to hold non-null agents, and their corresponding
    // IDs.
    final Agent[] agents = new Agent[nonnullAgents];
    final Object[] ids = new Object[nonnullAgents];
    nonnullAgents = 0;
    for(int i=0; i<givenAgents.length; i++) {
      final Agent agent = givenAgents[i];
      if (agent != null) {
        agents[nonnullAgents] = agent;
        ids[nonnullAgents] = givenIds[i];
        nonnullAgents++;
      }
    }

    // Assign initial positions
    final Map<Object,AgentPosition>
        initPositions = getInitialPositions(ids, agents);
    final Map<Object,Agent> agentsByID = new HashMap<>();
    for(int i=0; i<agents.length; i++) {
      agentsByID.put(ids[i], agents[i]);
    }

    // Decide on the agents' order
    final Object[] idOrder = Arrays.copyOf(ids, ids.length);
    randomizeArray(rand, idOrder);
    final Order order = new Order(idOrder);
    final Object firstToPlay = idOrder[0];

    // Build and log initial board
    final Board startingBoard = new Board(new Move[boardSize][boardSize],
                                          initPositions, firstToPlay, order);
    log.gameStart(startingBoard);

    // Set up the deck of tiles
    final LinkedList<Tile> deck = new LinkedList<>();
    for(final Tile tile : tiles) { deck.add(tile); }

    // Queue up the agents to receive their initial hands
    final Map<Agent,Set<Tile>> hands = new HashMap<>();
    final LinkedList<Object> waitingForTile = new LinkedList<>();
    for(final Agent agent : agents) {
      hands.put(agent, new HashSet<Tile>());
    }
    Object nextToDraw = firstToPlay;
    for(int i=0; i<3*agents.length; i++) {
      waitingForTile.add(nextToDraw);
      nextToDraw = order.getNext(nextToDraw, agentsByID);
    }

    // Records across game play
    Board currentBoard = startingBoard;
    final GameHistory history = new GameHistory(agents.length, boardSize);
    List<Object> lastActive = currentBoard.getAgentIDs();

    // While more than one player active, run a turn
    int played=0;
    while (currentBoard.getActiveAgentCount() > 1 && played < tiles.length) {

      // While there are players queued for a card, /and/ while there
      // are cards in the deck, hand them out.
      while (!deck.isEmpty() && !waitingForTile.isEmpty()) {
        final Object whoId = waitingForTile.remove();
        final Agent who = agentsByID.get(whoId);
        final Tile dealt = deck.remove();

        // Update the game runner's record of who has what card ---
        // might be reversed if receipt hits the timeout, but meshes
        // better with agent cleanup methods to have the tile in our
        // record of the hands for now.
        hands.get(who).add(dealt);

        // We log the event f dealing a card early, so that any
        // consequent error message is in context.
        log.tileDealt(whoId, who, dealt);

        // Enforce the timeout on an agent receiving a card.
        final Future<Void> receiveTask = Receiver.get(who, dealt);
        try {
          receiveTask.get(maxDrawSeconds, TimeUnit.SECONDS);
        } catch (Exception reason) {
          final Board newBoard = currentBoard.forfeit(whoId, who);
          log.forfeit(whoId, who, currentBoard, newBoard, reason);
          logAndCleanupEliminations(Collections.singleton(whoId),
                                    agentsByID, waitingForTile, deck,
                                    hands, log);
          currentBoard = newBoard;
        }
      }

      // Get the next agent to move
      final Object id = currentBoard.getActive();
      final Agent player = agentsByID.get(id);
      log.turnStart(id, player, currentBoard);

      // Ask the agent for their move
      Move move = null;
      Throwable exiting = null;
      final Future<Move>
          moveTask = Mover.getFuture(currentBoard, player, history);
      try {
        move = moveTask.get(maxTurnSeconds, TimeUnit.SECONDS);
      } catch (Throwable t) {
        moveTask.cancel(true);
        exiting = t;
      }

      // Make sure the move uses a tile actually in the player's hand;
      // nullify move if not.
      if (move != null) {
        if (!hands.get(player).contains(move.getTile())) {
          move = null;
          exiting = new IllegalStateException
              ("Move uses tile not in player's hand");
        }
      }

      // If the agent crashes or times out, there will be no move and
      // the agent forfeits the game.  Otherwise, apply the move and
      // get the revised board.
      final Board newBoard;
      if (move == null) {
        newBoard = currentBoard.forfeit(id, player);
        log.forfeit(id, player, currentBoard, newBoard, exiting);
      } else {

        log.turnMove(id, player, currentBoard, move);

        // Remove the played tile from our record of the agent's hand.
        hands.get(player).remove(move.getTile());

        // Play the move
        newBoard = currentBoard.apply(move);
        played++;
        log.turnResult(id, player, currentBoard, move, newBoard);
      }

      // When a player is eliminated from the game, return the cards
      // in their hands to the deck.
      final Set<Object>
          elimIds = findEliminatedPlayers(currentBoard, newBoard);
      logAndCleanupEliminations(elimIds, agentsByID,
                                waitingForTile, deck, hands, log);

      // Record the outcome of this move to the history.
      if (move == null) {
        history.recordForfeitEvent(id, currentBoard, newBoard, elimIds);
      } else {
        history.recordTileEvent(id, move, currentBoard, newBoard, elimIds);
      }

      // If the current player still in the game, they'll need to draw
      // a new tile.
      if (newBoard.isInGame(id)) {
        waitingForTile.add(id);
      }

      // Update loop variable for the end of the turn
      log.turnEnd();
      lastActive = currentBoard.getAgentIDs();
      currentBoard = newBoard;
    }

    final List<Object> winnerIDs;
    if (currentBoard.getActiveAgentCount() == 0) {
      winnerIDs = lastActive;
    } else {
      winnerIDs = currentBoard.getAgentIDs();
    }

    log.gameEnd(winnerIDs);
    return winnerIDs;
  }

  /**
   * Helper method for finding players eliminated by a move.
   */
  public Set<Object> findEliminatedPlayers(Board oldBoard, Board newBoard) {
    final Set<Object> removedIds = new HashSet<Object>();
    for(final Object elimId : oldBoard.getAgentIDs()) {
      if (!newBoard.isInGame(elimId)) {
        removedIds.add(elimId);
      }
    }
    return removedIds;
  }

  /**
   * Helper method for processing the elimination of players from the
   * game.
   */
  public void
      logAndCleanupEliminations(Set<Object> removedIds,
                                Map<Object,Agent> agentsByID,
                                LinkedList<Object> waitingForTile,
                                LinkedList<Tile> deck,
                                Map<Agent,Set<Tile>> hands, Log log) {
    if (removedIds.size() > 0) {
      log.eliminationsStart();
      for(final Object elimId : removedIds) {
        final Agent elim = agentsByID.get(elimId);
        log.eliminated(elimId, elim, hands.get(elim));

        // First redistribute their tiles to the deck.
        for(final Tile tile : hands.get(elim)) {
          deck.add(rand.nextInt(1+deck.size()), tile);
        }
        hands.get(elim).clear();

        // Remove occurrences of the eliminated player from the
        // queue waiting for a new tile.
        while (waitingForTile.remove(elimId)) { }
      }
      log.eliminationsEnd();
    } else {
      log.noEliminations();
    }
  }

  /**
   * Helper method for cleaning up when players are eliminated from
   * the game.
   */
  public Set<Object>
      removeEliminatedPlayers(Board oldBoard, Board newBoard,
                              Map<Object,Agent> agentsByID,
                              LinkedList<Object> waitingForTile,
                              LinkedList<Tile> deck,
                              Map<Agent,Set<Tile>> hands, Log log) {
    boolean anyEliminated = false;
    final List<Object> startedActive = oldBoard.getAgentIDs();
    final Set<Object> removedIds = new HashSet<Object>();

    // log.alert(startedActive + " active before move");
    for(final Object elimId : startedActive) {
      final Agent elim = agentsByID.get(elimId);

      if (!newBoard.isInGame(elimId)) {
        if (!anyEliminated) {
          anyEliminated = true;
          log.eliminationsStart();
        }
        removedIds.add(elimId);

        // The loop has found a player eliminated by this move.
        log.eliminated(elimId, elim, hands.get(elim));

        // First redistribute their tiles to the deck.
        for(final Tile tile : hands.get(elim)) {
          deck.add(rand.nextInt(1+deck.size()), tile);
        }
        hands.get(elim).clear();

        // Remove occurrences of the eliminated player from the
        // queue waiting for a new tile.
        while (waitingForTile.remove(elimId)) { }
      } else {
        log.notEliminated(elimId, elim);
      }
    }

    if (anyEliminated) {
      log.eliminationsEnd();
    } else {
      log.noEliminations();
    }

    return removedIds;
  }

  /**
   * Assign initial positions.  We assign randomly to slots on the
   * non-corner squares of each side.  If separateInitialTiles is set,
   * then we make sure that each player has their own tile.
   */
  public Map<Object,AgentPosition>
      getInitialPositions(Object[] ids, Agent[] agents) {
    final int initSlotCount
        = 4 * (boardSize-2) * (separateInitialTiles ? 1 : 2);
    if (initSlotCount < agents.length) {
      throw new IllegalStateException
          ("Too few tiles for each agent to start on a non-corner tile.");
    }

    int x=0;
    final AgentPosition[] allInitPositions = new AgentPosition[initSlotCount];
    for(int i=1; i<boardSize-1; i++) {
      if (separateInitialTiles) {
        allInitPositions[x++]
            = new AgentPosition(-1, i, 2+rand.nextInt(2));
        allInitPositions[x++]
            = new AgentPosition(boardSize, i, 6+rand.nextInt(2));
        allInitPositions[x++]
            = new AgentPosition(i, boardSize, 0+rand.nextInt(2));
        allInitPositions[x++]
            = new AgentPosition(i, -1, 4+rand.nextInt(2));
      } else {
        allInitPositions[x++] = new AgentPosition(-1, i, 2);
        allInitPositions[x++] = new AgentPosition(-1, i, 3);
        allInitPositions[x++] = new AgentPosition(boardSize, i, 6);
        allInitPositions[x++] = new AgentPosition(boardSize, i, 7);
        allInitPositions[x++] = new AgentPosition(i, boardSize, 0);
        allInitPositions[x++] = new AgentPosition(i, boardSize, 1);
        allInitPositions[x++] = new AgentPosition(i, -1, 4);
        allInitPositions[x++] = new AgentPosition(i, -1, 5);
      }
    }
    randomizeArray(rand, allInitPositions);

    final Map<Object,AgentPosition>
        initPositions = new HashMap<Object,AgentPosition>();
    for(int i=0; i<agents.length; i++) {
      initPositions.put(ids[i], allInitPositions[i]);
    }

    return initPositions;
  }

  // =================================================================
  // Static methods and classes

  public static Object[] basicPlayerNames(int count) {
    final Object[] ids = new Object[count];
    for(int i=0; i<count; i++) {
      ids[i] = "Player " + (1+i);
    }
    return ids;
  }

  static final class Mover implements Callable<Move> {
    private final Board basis;
    private final Agent player;
    private final History history;
    Mover(Board basis, Agent player, History history) {
      this.basis = basis;
      this.player = player;
      this.history = history;
    }
    @Override public Move call() {
      final Move result = player.play(basis, history);
      return result;
    }
    static Future<Move> getFuture(Board basis, Agent player, History history) {
      final Mover mover = new Mover(basis, player, history);
      final Future<Move> result = executor.submit(mover);
      return result;
    }
  }

  static final class Receiver implements Callable<Void> {
    private final Agent player;
    private final Tile tile;
    Receiver(Agent player, Tile tile) {
      this.player = player;
      this.tile = tile;
    }
    @Override public Void call() {
      player.receiveTile(tile);
      return null;
    }
    static Future<Void> get(Agent player, Tile tile) {
      final Receiver receiver = new Receiver(player, tile);
      final Future<Void> result = executor.submit(receiver);
      return result;
    }
  }

  static final class AgentMaker implements Callable<Agent> {
    private final AgentFactory factory;
    private final Object id;
    private final int boardSize;
    private final Tile[] tiles;
    private final int maxInitSeconds;
    private final int maxDrawSeconds;
    private final int maxTurnSeconds;
    AgentMaker(AgentFactory factory, Object id, int boardSize, Tile[] tiles,
               int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds) {
      this.factory = factory;
      this.id = id;
      this.boardSize = boardSize;
      this.tiles = tiles;
      this.maxInitSeconds = maxInitSeconds;
      this.maxDrawSeconds = maxDrawSeconds;
      this.maxTurnSeconds = maxTurnSeconds;
    }
    @Override public Agent call() {
      return factory.getAgent(boardSize, tiles, maxInitSeconds,
                              maxDrawSeconds, maxTurnSeconds, id);
    }
    static Future<Agent> get(AgentFactory factory, Object id, int boardSize,
                             Tile[] tiles, int maxInitSeconds,
                             int maxDrawSeconds, int maxTurnSeconds) {
      final AgentMaker receiver
          = new AgentMaker(factory, id, boardSize, tiles,
                           maxInitSeconds, maxDrawSeconds, maxTurnSeconds);
      final Future<Agent> result = executor.submit(receiver);
      return result;
    }
  }

}
