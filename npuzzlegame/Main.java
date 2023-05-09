package npuzzlegame;

import javax.swing.*;
import java.awt.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame();
      JPanel container = new JPanel();
      container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("N Puzzle");
      frame.setResizable(false);

      Game game = new Game(3, 550, 50);
      System.out.println("Tạo game thành công");
      App app = new App(game);
      app.requestFocusInWindow();

      Controller controller = new Controller(app);

      container.setPreferredSize(new Dimension(850, 600));
      container.add(controller);
      container.add(app);

      frame.add(container);
      frame.pack();
      // center on the screen
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}
