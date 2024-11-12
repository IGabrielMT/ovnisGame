package co.edu.uptc.views;

import co.edu.uptc.interfaces.Interfaces;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel {
    private static GamePanel instance;
    private Point selectedUfo;
    private final Interfaces.Presenter presenter;
    @Setter
    private CopyOnWriteArrayList<Point> ufos;

    private GamePanel(Interfaces.Presenter presenter) {
        this.presenter = presenter;
        this.ufos = new CopyOnWriteArrayList<>();
        setSize(800, 600);
        setBackground(Color.BLACK);
        addMoveMouseListener();
    }

    private void addMoveMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    presenter.selectUfo(e.getPoint());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    presenter.addTrajectoryPoint(e.getPoint());
                    presenter.moveSelectedUfo();
                    paintPoint(e.getPoint());
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    presenter.addTrajectoryPoint(e.getPoint());
                    paintPoint(e.getPoint());
                }
            }
        });
    }

    private void paintPoint(Point point) {
        SwingUtilities.invokeLater(() -> {
            Graphics g = getGraphics();
            if (g != null) {
                g.setColor(new Color(0, 255, 0));
                g.fillOval(point.x - 5, point.y - 5, 10, 10);
                g.dispose();
            }
        });
    }

    public static GamePanel getInstance(Interfaces.Presenter presenter) {
        if (instance != null) {
            return instance;
        }
        synchronized (GamePanel.class) {
            if (instance == null) {
                instance = new GamePanel(presenter);
            }
        }
        return instance;
    }

    @SneakyThrows
    public void paintOvnis(Point ufo) {
        SwingUtilities.invokeLater(() -> {
            ufos.add(ufo);
            repaint();
        });
    }

    @SneakyThrows
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintUfos(MainFrame.getInstance().getOvniTypeComboBox().getSelectedItem().toString(), g2d);
    }
    private void paintUfos(String caseName, Graphics2D g2d) {
        for (Point ufo : ufos) {
            boolean isSelected = ufo.equals(selectedUfo);
            switch (caseName) {
                case "Ovni 1" -> drawUfo1(g2d, isSelected);
                case "Ovni 2" -> drawUfo2(g2d, isSelected);
                case "Ovni 3" -> drawUfo3(g2d, isSelected);
            }
        }
    }

    private void drawUfo1(Graphics2D g2d, boolean isSelected){
        for (Point point : ufos) {
            UfoModels.drawUfo1(g2d, point, isSelected);
        }
    }
    private void drawUfo2(Graphics2D g2d, boolean isSelected){
        for (Point point : ufos) {
            UfoModels.drawUfo2(g2d, point, isSelected);
        }

    }
    private void drawUfo3(Graphics2D g2d, boolean isSelected){
        for (Point point : ufos) {
            UfoModels.drawUfo3(g2d, point, isSelected);
        }
    }


    public void resetGamePanel() {
        SwingUtilities.invokeLater(() -> {
            ufos.clear();
            repaint();
        });
    }

    public void paintGoal(Rectangle goal, Point goalPoint) {
        Goal goals = new Goal(goalPoint.x, goalPoint.y, goal.width, goal.height);
        for (Component comp : this.getComponents()){
            if (comp instanceof Goal){
                remove(comp);
            }
        }
        add(goals);
    }

}