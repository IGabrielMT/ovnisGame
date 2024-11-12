package co.edu.uptc.views;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton{
    private final Color pressedColor = Global.BUTTON_BORDER_COLOR;
    private final Color rolloverColor = Global.COLOR_BACKGROUND;
    private final Color normalColor = Global.COLOR_BACKGROUND;

    public CustomButton(String text) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Global.COLOR_TEXT);
        setFont(Global.FONT_TITLE_BIG);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(10, 50, 10, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(rolloverColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
        g2.dispose();
        super.paintComponent(g);
        drawText(g);
    }
    private void drawText(Graphics g) {
        FontMetrics metrics = g.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(getForeground());
        g.drawString(getText(), x, y);
    }
}
