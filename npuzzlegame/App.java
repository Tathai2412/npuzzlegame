package npuzzlegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JPanel implements ActionListener {

  protected Timer timer;
  protected Game game;
  private int tileSize;
  private int margin;
  private int dimension;

  protected boolean isRunnableSolutions;

  public App(Game game) {
    this.game = game;
    init();
    timer = new Timer(400, this);

    isRunnableSolutions = false;

    setPreferredSize(new Dimension(dimension, dimension + margin));
    setBackground(Colors.BACKGROUND_COLOR);
    setForeground(Colors.FOREGROUND_COLOR);
    setFont(new Font("SansSerif", Font.BOLD, 80));
  }

  public void init() {
    margin = game.MARGIN;
    dimension = game.DIMENSION;
    tileSize = game.TILE_SIZE;
  }

  public void setGame(Game game) {
    this.game = game;
    init();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!isRunnableSolutions) {
      repaint();
      return;
    }
    game.initial = game.solutions.pop();
    game.isGameOver = game.initial.isGoal();

    repaint();

    if (game.isGameOver) {
      timer.stop();
      isRunnableSolutions = false;
    }
  }


  /**
   * Draw the board
   * @param g Graphics2D
   */
  private void drawBoard(Graphics2D g) {
    for (int id = 0; id < game.initial.getTiles().length; id++) {
      int x = (id % game.SIZE_OF_BOARD) * tileSize + (dimension - tileSize * game.SIZE_OF_BOARD) / 2;
      int y = margin + (id / game.SIZE_OF_BOARD) * tileSize;

      // check special case for blank tile
      if (game.initial.getTiles()[id] == 0) {
        if (game.isGameOver) {
          g.setColor(Colors.FOREGROUND_COLOR);
          drawCenteredString(g, "\u2713", x, y);
        }
        continue;
      }

      // for other tiles
      g.setColor(getForeground());
      g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
      g.setColor(Color.BLACK);
      g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
      g.setColor(Color.WHITE);

      drawCenteredString(g, String.valueOf(game.initial.getTiles()[id]), x, y);
    }
  }

  /**
   * Draw number for a tile
   * @param g Graphics2D
   * @param s number to draw
   * @param x coordinate x
   * @param y coordinate y
   */
  private void drawCenteredString(Graphics2D g, String s, int x, int y) {
    // center string s for the given tile(x,y)
    FontMetrics fontMetrics = g.getFontMetrics();
    int asc = fontMetrics.getAscent();
    int desc = fontMetrics.getDescent();
    g.drawString(s,  x + (tileSize - fontMetrics.stringWidth(s)) / 2,
            y + (asc + (tileSize - (asc + desc)) / 2));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    drawBoard(g2D);
  }
}
