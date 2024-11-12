package co.edu.uptc.views;

import java.awt.*;
import java.io.InputStream;

public class Global {
    public static Font FONT_TITLE_BIG = createCustomFont("/fonts/NewsreaderText-Regular.ttf", 32);
    public static Font FONT_TEXTS_SMALL = createCustomFont("/fonts/Archivo-SemiBold.ttf", 16);
    public static Font FONT_TITLE_GAME = createCustomFont("/fonts/OXYGENE1.TTF", 52);
    public static Color COLOR_BACKGROUND = Color.BLACK;
    public static Color COLOR_TEXT = Color.WHITE;
    public static Color BUTTON_BORDER_COLOR = new Color(0,255,255);
    private static Font createCustomFont(String path, float size) {
        try {
            InputStream is = Global.class.getResourceAsStream(path);
            assert is != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(path + " not loaded. Using serif font.");
            return new Font("serif", Font.PLAIN, 24);
        }
    }
}
