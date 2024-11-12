package co.edu.uptc.views;

import javax.swing.*;
import java.awt.*;


public class Goal extends JPanel {
    public Goal(int x, int y, int width, int height) {
        setBackground(Color.GREEN);
        setSize(width, height);
        setLocation(x, y);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
    }
}
