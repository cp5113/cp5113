package test.conflictArea;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class TArea extends JApplet implements ActionListener {
  DrawingCanvas canvas;

  JRadioButton addButton, subtractButton, intersectButton, exclusiveORButton,
      resetButton;

  public static void main(String[] a) {
    JFrame f = new JFrame();
    TArea area = new TArea();
    area.init();
    f.getContentPane().add(area);
    f.setDefaultCloseOperation(1);
    f.setSize(650, 450);
    f.setVisible(true);
  }

  public void init() {
    Container container = getContentPane();

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 2));

    resetButton = new JRadioButton("Reset", true);

    addButton = new JRadioButton("Add");
    subtractButton = new JRadioButton("Subtract");
    intersectButton = new JRadioButton("Intersect");
    exclusiveORButton = new JRadioButton("ExclusiveOR");

    ButtonGroup group = new ButtonGroup();
    group.add(resetButton);
    group.add(addButton);
    group.add(subtractButton);
    group.add(intersectButton);
    group.add(exclusiveORButton);
    group.add(resetButton);

    resetButton.addActionListener(this);
    addButton.addActionListener(this);
    subtractButton.addActionListener(this);
    intersectButton.addActionListener(this);
    exclusiveORButton.addActionListener(this);

    panel.add(addButton);
    panel.add(subtractButton);
    panel.add(intersectButton);
    panel.add(exclusiveORButton);

    container.add(panel, BorderLayout.NORTH);
    container.add(resetButton, BorderLayout.SOUTH);

    canvas = new DrawingCanvas();
    container.add(canvas);
  }

  class DrawingCanvas extends Canvas {
    GeneralPath gp;

    Ellipse2D ellipse;

    Area area1, area2;

    boolean drawFlag = true;

    boolean fillFlag = false;

    public DrawingCanvas() {
      setBackground(Color.white);
      setSize(350, 250);
      int w = getWidth();
      int h = getHeight();

      gp = new GeneralPath();
      gp.moveTo(w / 8, h / 2);
      gp.lineTo(w / 2, h / 4);
      gp.lineTo(7 * w / 8, h / 2);
      gp.lineTo(w / 2, 3 * h / 4);
      gp.closePath();
      area1 = new Area(gp);

      ellipse = new Ellipse2D.Double(w / 4, h / 4, w / 2, h / 2);
      area2 = new Area(ellipse); // Ellipse area object
    }

    public void paint(Graphics g) {
      Graphics2D g2D = (Graphics2D) g;
      g2D.setStroke(new BasicStroke(2.0f));

      if (drawFlag) {
        g2D.draw(area1);
        g2D.draw(area2);
      }
      if (fillFlag)
        g2D.fill(area1);
    }
  }

  public void actionPerformed(ActionEvent e) {
    JRadioButton temp = (JRadioButton) e.getSource();

    if (temp.equals(addButton)) {
      canvas.area1 = new Area(canvas.gp);
      canvas.area1.add(canvas.area2);
      canvas.drawFlag = false;
      canvas.fillFlag = true;
      canvas.repaint();
    } else if (temp.equals(subtractButton)) {
      canvas.area1 = new Area(canvas.gp);
      canvas.area1.subtract(canvas.area2);
      canvas.drawFlag = false;
      canvas.fillFlag = true;
      canvas.repaint();
    } else if (temp.equals(intersectButton)) {
      canvas.area1 = new Area(canvas.gp);
      canvas.area1.intersect(canvas.area2);
      canvas.drawFlag = false;
      canvas.fillFlag = true;
      canvas.repaint();
    } else if (temp.equals(exclusiveORButton)) {
      canvas.area1 = new Area(canvas.gp);
      canvas.area1.exclusiveOr(canvas.area2);
      canvas.drawFlag = false;
      canvas.fillFlag = true;
      canvas.repaint();
    } else if (temp.equals(resetButton)) {
      if (canvas.drawFlag == false) {
        canvas.area1 = new Area(canvas.gp);
        canvas.drawFlag = true;
        canvas.fillFlag = false;
        canvas.repaint();
      }
    }
  }
}

           
       