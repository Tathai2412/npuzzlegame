package npuzzlegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MyButton extends JButton {

  private boolean over;
  private Color color;
  private Color colorOver;
  private Color colorClick;
  private Color borderColor;
  private int radius = 0;

  public MyButton(String s) {
    //  Init Color
    colorOver = new Color(179, 250, 160);
    colorClick = new Color(152, 184, 144);
    borderColor = new Color(30, 136, 56);

    setText(s);
    setFocusable(false);
    setFocusPainted(false);
    setContentAreaFilled(false);
    setForeground(Color.white);
    setFont(Controller.FONT);
    setColor(Colors.BUTTON_COLOR);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent me) {
        setBackground(colorOver);
        over = true;
      }

      @Override
      public void mouseExited(MouseEvent me) {
        setBackground(color);
        over = false;

      }

      @Override
      public void mousePressed(MouseEvent me) {
        setBackground(colorClick);
      }

      @Override
      public void mouseReleased(MouseEvent me) {
        if (over) {
          setBackground(colorOver);
        } else {
          setBackground(color);
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    Graphics2D g2 = (Graphics2D) graphics;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //  Paint Border
    g2.setColor(borderColor);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    g2.setColor(getBackground());
    //  Border set 2 Pix
    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
    super.paintComponent(graphics);
  }

  public boolean isOver() {
    return over;
  }

  public void setOver(boolean over) {
    this.over = over;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
    setBackground(color);
  }

  public Color getColorOver() {
    return colorOver;
  }

  public void setColorOver(Color colorOver) {
    this.colorOver = colorOver;
  }

  public Color getColorClick() {
    return colorClick;
  }

  public void setColorClick(Color colorClick) {
    this.colorClick = colorClick;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }
}

