package npuzzlegame;

import java.util.LinkedList;
import java.util.List;

public class Board {

  private int[] tiles;
  private final int LENGTH; // độ dài của mảng
  protected Board prevBoard = null;
  protected int h = 0;
  protected int g = 1;
  protected int f = 0;

  public Board(int[] tiles) {
    this.LENGTH = tiles.length;
    this.tiles = new int[LENGTH];
    System.arraycopy(tiles, 0, this.tiles, 0, LENGTH);
  }

  public int[] getTiles() {
    return this.tiles;
  }

  public void setTiles(int[] tiles) {
    this.tiles = tiles;
  }

  // Lấy vị trí của ô trống
  public int getBlankTileId() {
    for (int i = 0; i < LENGTH; i++) {
      if (tiles[i] == 0) {
        return i;
      }
    }
    return -1;
  }

  // Lấy kích thước của bảng
  public int dimension() {
    return (int) Math.sqrt(LENGTH);
  }

  // Tính số xung đột tuyến tính
  public int numLinearConflict() {
    int numLinearConflict = 0;

    // Tính số xung đột tuyến tính trên hàng
    for (int i = 0; i < LENGTH; i += dimension()) {
      int tile = tiles[i];
      if (tile == 0 || (tile - 1) / dimension() != i) {
        continue;
      }
      for (int j = 0; j < dimension(); j++) {
        if ((tiles[j] - 1) / dimension() == i && tile > tiles[j]) {
          numLinearConflict++;
        }
      }
    }

    // Tính số xung đột tuyến tính trên cột
    for (int i = 0; i < dimension(); i++) {
      int tile = tiles[i];
      if (tile == 0 || (tile - 1) % dimension() != i) {
        continue;
      }
      for (int j = 0; j < LENGTH; j += dimension()) {
        if ((tiles[j] - 1) % dimension() == i && tile > tiles[j]) {
          numLinearConflict++;
        }
      }
    }
    return numLinearConflict;
  }

  // Tính khoảng cách manhattan
  public int manhattan() {
    int manhattan = 0;
    for (int i = 0; i < LENGTH; i++) {
      if (tiles[i] == 0) {
        continue;
      }
      int tile = tiles[i] - 1;
      manhattan += Math.abs(i / dimension() - tile / dimension());
      manhattan += Math.abs(i % dimension() - tile % dimension());
    }
    return manhattan;
  }

  public boolean isGoal() {
    return manhattan() == 0;
  }

  // Trả lại bảng sau khi di chuyển ô trống đến 1 vị trị gần kề
  public Board twin(char direction) {
    int id = getBlankTileId();
    int newId = id;

    switch (direction) {
      case 'u' -> newId -= dimension();
      case 'r' -> newId++;
      case 'd' -> newId += dimension();
      case 'l' -> newId--;
    }

    int[] newTiles = new int[LENGTH];

    System.arraycopy(tiles, 0, newTiles, 0, LENGTH);

    int temp = newTiles[id];
    newTiles[id] = newTiles[newId];
    newTiles[newId] = temp;

    return new Board(newTiles);
  }

  public boolean equals(Board y) {
    if (this.dimension() != y.dimension()) {
      return false;
    }

    for (int i = 0; i < LENGTH; i++) {
      if (this.tiles[i] != y.tiles[i]) {
        return false;
      }
    }

    return true;
  }

  public Iterable<Board> nextSteps() {
    List<Board> neighbors = new LinkedList<>();

    int blankId = getBlankTileId();

    if (blankId / dimension() > 0){
      neighbors.add(this.twin('u'));
    }
    if (blankId % dimension() < dimension() - 1) {
      neighbors.add(this.twin('r'));
    }
    if (blankId / dimension() < dimension() - 1){
      neighbors.add(this.twin('d'));
    }
    if (blankId % dimension() > 0){
      neighbors.add(this.twin('l'));
    }

    return neighbors;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < LENGTH; i++) {
      stringBuilder.append(this.tiles[i]).append(" ");
      if (i % dimension() == dimension() - 1) {
        stringBuilder.append("\n");
      }
    }
    return stringBuilder.toString();
  }
}
