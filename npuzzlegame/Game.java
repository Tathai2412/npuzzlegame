package npuzzlegame;

import java.util.*;

public class Game extends Thread {
  protected final int SIZE_OF_BOARD;
  protected final int MARGIN;
  protected final int TILE_SIZE;
  protected final int DIMENSION;

  private final Random RANDOM = new Random();

  protected Stack<Board> solutions;
  protected Board initial;
  protected boolean isGameOver = true;
  protected boolean isDone = false;

  public Game(int size, int dimension, int margin) {
    SIZE_OF_BOARD = size;
    MARGIN = margin;
    DIMENSION = dimension;
    TILE_SIZE = (dimension - 2 * margin) / SIZE_OF_BOARD;

    initial = new Board(new int[SIZE_OF_BOARD * SIZE_OF_BOARD]);
    newGame();
  }

  /**
   * Create new board util the game is solvable
   */
  public void newGame() {
    reset();
  }

  private void reset() {
    isGameOver = true;
    initial.prevBoard = null;
    int[] tiles = initial.getTiles();
    for (int id = 0; id < tiles.length; id++) {
      tiles[id] = id + 1;
    }

    // set the blank tile at the end of board
    tiles[tiles.length - 1] = 0;
    initial.setTiles(tiles);
  }

  protected void shuffle() {
    int count = 0;
    while (count < 20 * SIZE_OF_BOARD) {
      LinkedList<Board> nextSteps = (LinkedList<Board>) initial.nextSteps();
      int randId = RANDOM.nextInt(nextSteps.size());
      count++;
      initial = nextSteps.get(randId);
    }
    isGameOver = false;
  }

  public void solve() {
    Solver solver = new Solver(initial);
    solutions = solver.AStar();
  }

  @Override
  public void run(){
    solve();
    isDone = true;
  }
}