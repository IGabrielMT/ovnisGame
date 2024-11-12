package co.edu.uptc.views;
import javax.swing.*;
import java.awt.*;

public class UfoModels extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Global.COLOR_BACKGROUND);
    }

    private static void drawBaseUfo(Graphics2D g2d, Point point, Color baseColor) {
        g2d.setColor(baseColor.darker());
        g2d.fillOval(point.x - 10, point.y + 2, 20, 7);
        g2d.setColor(baseColor);
        g2d.fillOval(point.x - 7, point.y - 5, 14, 8);
    }

    private static void drawDome(Graphics2D g2d, Point point, Color domeColor) {
        g2d.setColor(domeColor);
        g2d.fillOval(point.x - 3, point.y - 3, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.drawOval(point.x - 3, point.y - 3, 6, 6);
    }

    public static void drawUfo1(Graphics2D g2d, Point point, boolean isSelected) {
        Color baseColor = isSelected ? Color.RED : new Color(150, 150, 255);
        drawBaseUfo(g2d, point, baseColor);
        drawDome(g2d, point, Color.WHITE);
    }

    public static void drawUfo2(Graphics2D g2d, Point point, boolean isSelected) {
        Color baseColor = isSelected ? Color.RED : new Color(100, 255, 100);
        g2d.setColor(baseColor.darker());
        g2d.fillOval(point.x - 10, point.y + 2, 20, 7);

        int[] xPoints = {
                point.x - 7, point.x - 4, point.x + 4,
                point.x + 7, point.x + 4, point.x - 4
        };
        int[] yPoints = {
                point.y, point.y - 5, point.y - 5,
                point.y, point.y + 2, point.y + 2
        };
        g2d.setColor(baseColor);
        g2d.fillPolygon(xPoints, yPoints, 6);

        g2d.setColor(Color.WHITE);
        g2d.drawLine(point.x - 5, point.y - 2, point.x + 5, point.y - 2);
        g2d.drawLine(point.x - 5, point.y, point.x + 5, point.y);
        drawDome(g2d, point, Color.CYAN);
    }

    public static void drawUfo3(Graphics2D g2d, Point point, boolean isSelected) {
        Color baseColor = isSelected ? Color.RED : new Color(255, 100, 255);
        drawBaseUfo(g2d, point, baseColor);

        g2d.setColor(baseColor.brighter());
        g2d.drawOval(point.x - 8, point.y - 4, 16, 8);
        g2d.drawOval(point.x - 7, point.y - 3, 14, 7);

        drawDome(g2d, point, Color.WHITE);
        g2d.setColor(Color.CYAN);
        g2d.drawLine(point.x, point.y - 3, point.x, point.y + 3);
        g2d.drawLine(point.x - 3, point.y, point.x + 3, point.y);
    }
}