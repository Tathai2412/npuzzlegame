package npuzzlegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller extends JPanel implements ActionListener {
  public static final Font FONT = new Font("SansSerif", Font.PLAIN, 18);

  private final JLabel loadingAnimation;
  private final JLabel startAnimation;
  private final JLabel doneAnimation;

  private MyButton findSolutionsBtn;
  private MyButton showSolutionsBtn;
  private MyButton shuffleBtn;
  private MyButton levelsBtn;

  private JPopupMenu levels;
  private JMenuItem easy, medium, hard;

  CheckDone checkDone;

  String status = "";
  String totalStep = "";
  String countStep = "";

  App app;

  public Controller(App app) {
    this.app = app;
    this.setLayout(null);
    this.setPreferredSize(new Dimension(310, 600));
    this.setBackground(Colors.BACKGROUND_COLOR);

    ImageIcon loadingImage = new ImageIcon("src/image/loading.gif");
    ImageIcon startImage = new ImageIcon("src/image/start.gif");
    ImageIcon doneImage = new ImageIcon("src/image/done.gif");
    loadingAnimation = new JLabel(loadingImage);
    startAnimation = new JLabel(startImage);
    doneAnimation = new JLabel(doneImage);
    loadingAnimation.setBounds(53, 100, 200, 200);
    startAnimation.setBounds(53, 100, 200, 200);
    doneAnimation.setBounds(53, 220, 200, 200);
    addAnimation(startAnimation);

    createBtn();

    this.add(findSolutionsBtn);
    this.add(showSolutionsBtn);
    this.add(shuffleBtn);
    this.add(levels);
    this.add(levelsBtn);
  }

  public void setStatus(String status) {
    this.status = status;
    repaint();
  }

  public void setTotalStep(String totalStep) {
    this.totalStep = totalStep;
    repaint();
  }

  public void setCountStep(String countStep) {
    this.countStep = countStep;
    repaint();
  }

  private void createBtn() {
    findSolutionsBtn = new MyButton("Tìm giải");
    findSolutionsBtn.setBounds(15, 500, 120, 60);
    findSolutionsBtn.addActionListener(this);

    showSolutionsBtn = new MyButton("Chạy");
    showSolutionsBtn.setBounds(165, 500, 120, 60);
    showSolutionsBtn.addActionListener(this);

    shuffleBtn = new MyButton("Trộn");
    shuffleBtn.setBounds(15, 425, 120, 60);
    shuffleBtn.addActionListener(this);

    levelsBtn = new MyButton("Dễ \u25BC");
    levelsBtn.setBounds(165, 425, 120, 60);
    levelsBtn.setFont(FONT);

    createPopupMenu();

    levelsBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        levels.show(levelsBtn, levels.getX(), levels.getY() + levelsBtn.getHeight());
      }
    });
    levelsBtn.add(levels);
  }

  private void createPopupMenu() {
    levels = new JPopupMenu("Levels");

    easy = new JMenuItem("Dễ");
    easy.setBounds(165, 425, 120, 60);
    easy.addActionListener(this);
    setStyleComp(easy);

    medium = new JMenuItem("T.Bình");
    medium.setBounds(165, 425, 120, 60);
    medium.addActionListener(this);
    setStyleComp(medium);

    hard = new JMenuItem("Khó");
    hard.setBounds(165, 425, 120, 60);
    hard.addActionListener(this);
    setStyleComp(hard);

    levels.add(easy);
    levels.add(medium);
    levels.add(hard);
  }

  private void setStyleComp(JComponent e) {
    e.setBackground(Colors.BUTTON_COLOR);
    e.setForeground(Color.white);
    e.setFont(FONT);
    e.setFocusable(false);
  }

  protected void addAnimation(JLabel animation) {
    this.add(animation);
  }

  protected void removeAnimation(JLabel animation) {
    this.remove(animation);
  }

  private void drawStatus(Graphics2D g) {
    g.setFont(getFont().deriveFont(Font.BOLD, 28));
    g.setColor(Colors.TEXT_COLOR);
    g.drawString(status,
            (getWidth() - getFontMetrics(g.getFont()).stringWidth(status)) / 2,
            80
    );
  }

  private void drawSteps(Graphics2D g) {
    g.setFont(getFont().deriveFont(Font.BOLD, 28));
    g.setColor(Colors.TEXT_COLOR);
    g.drawString(totalStep,
            (getWidth() - getFontMetrics(g.getFont()).stringWidth(totalStep)) / 2,
            150
    );

    g.drawString(countStep,
            (getWidth() - getFontMetrics(g.getFont()).stringWidth(countStep)) / 2,
            200
    );
  }

  private void paintSlide(Graphics2D g) {
    g.setColor(Color.BLACK);
    g.drawRoundRect(this.getWidth() - 1, 0, 1, this.getHeight(), 0, 0);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics2D = (Graphics2D) g;
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paintSlide(graphics2D);
    drawStatus(graphics2D);
    drawSteps(graphics2D);
  }

  public void clearStatus() {
    setStatus("");
    setTotalStep("");
    setCountStep("");
  }

  public void clearAnimation() {
    removeAnimation(startAnimation);
    removeAnimation(loadingAnimation);
    removeAnimation(doneAnimation);
  }

  public void handleLevelBtn(Object source) {

    boolean check = false;
    if (easy.equals(source)) {
      levelsBtn.setText("Dễ \u25BC");
      app.setGame(new Game(3, 550, 50));
      check = true;
    } else if (medium.equals(source)) {
      levelsBtn.setText("T.Bình \u25BC");
      app.setGame(new Game(4, 550, 50));
      check = true;
    } else if (hard.equals(source)) {
      levelsBtn.setText("Khó \u25BC");
      app.setGame(new Game(5, 550, 50));
      check = true;
    }
    if (check) {
      addAnimation(startAnimation);
      if (checkDone != null) {
        checkDone.timer.stop();
        checkDone = null;
      }
    }
    clearStatus();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();

    if (app.isRunnableSolutions) {
      return;
    }

    Object source = e.getSource();
    if (app.game.isAlive() && !e.getSource().equals(findSolutionsBtn)) {
      return;
    }
    clearAnimation();

    handleLevelBtn(source);

    if (findSolutionsBtn.equals(source)) {
      if (app.game.solutions != null) {
        return;
      }

      if (checkDone != null) {
        checkDone.timer.stop();
      }

      if (app.game.isAlive()) {
        Board temp = app.game.initial;
        app.game = new Game(app.game.SIZE_OF_BOARD, 550, 50);
        app.game.initial = temp;
        findSolutionsBtn.setText("Tìm giải");
        setStatus("");
        addAnimation(startAnimation);
        return;
      }
      addAnimation(loadingAnimation);
      findSolutionsBtn.setText("Dừng");
      setStatus("Đang tìm lời giải!");
      repaint();

      checkDone = new CheckDone(app , this);
      app.game.start();
    }

    else if (showSolutionsBtn.equals(source)) {
      if (app.game.isGameOver) {
        setStatus("Giải xong rồi, chạy cc!");
        return;
      }
      if (app.game.solutions == null) {
        setStatus("Chưa có lời giải!");
        return;
      }
      setStatus("Đang chạy lời giải!");
      app.timer.start();
      app.isRunnableSolutions = true;
      return;
    }

    else if (shuffleBtn.equals(source)) {
      app.setGame(new Game(app.game.SIZE_OF_BOARD, 550, 50));
      app.game.shuffle();
      addAnimation(startAnimation);
      clearStatus();
    }
    app.repaint();
  }

  static class CheckDone implements ActionListener {
    Controller controller;
    Timer timer;
    App app;
    int totalStep = 0;
    int numStep;

    public CheckDone(App app, Controller controller) {
      this.controller = controller;
      this.timer = new Timer(0, this);
      this.app = app;
      timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (app.game.isDone) {
        controller.removeAnimation(controller.loadingAnimation);
        controller.repaint();
        controller.findSolutionsBtn.setText("Tìm giải");
        controller.setStatus("Đã tìm thấy lời giải!");
        if (totalStep == 0) {
          totalStep = app.game.solutions.size();
        }
        controller.setTotalStep("Tổng số bước là " + totalStep);
        numStep = totalStep;
      }

      if (app.isRunnableSolutions) {
        if (numStep != app.game.solutions.size()) {
          app.timer.setDelay(Integer.MAX_VALUE);
          numStep = app.game.solutions.size();
        }
        controller.setCountStep("Số bước còn lại: " + app.game.solutions.size() + "/" + totalStep);
        app.timer.setDelay(400);
      }

      if (app.game.isGameOver) {
        controller.setCountStep("Số bước còn lại: " + 0 + "/" + totalStep);
        controller.addAnimation(controller.doneAnimation);
        timer.stop();
      }
    }
  }
}
