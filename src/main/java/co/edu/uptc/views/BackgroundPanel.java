package co.edu.uptc.views;

import javazoom.jl.player.Player;
import lombok.Setter;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class BackgroundPanel extends JPanel {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int STAR_COUNT = 100;
    private static final int TIMER_DELAY = 33;

    private final ArrayList<Star> stars;
    private final Random random;
    private Timer animationTimer;
    private float ufoHoverOffset = 0;
    private double time = 0;

    @Setter
    private Clip backgroundMusic;
    private Player player;

    public BackgroundPanel() {
        random = new Random();
        stars = new ArrayList<>();
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        initializeStars();
        setupAnimation();
        setupMusic();
    }

    private void initializeStars() {
        for (int i = 0; i < STAR_COUNT; i++) {
            stars.add(new Star());
        }
    }

    private void setupMusic() {
        try (InputStream musicStream = getClass().getResourceAsStream("/music/Chad Crouch - Space.mp3")) {
            if (musicStream != null) {
                player = new Player(musicStream);
                new Thread(() -> {
                    try {
                        player.play();
                    } catch (Exception e) {
                        System.out.println("No se pudo reproducir la música: " + e.getMessage());
                    }
                }).start();
            } else {
                System.out.println("El archivo de música no se encontró en el classpath.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la música: " + e.getMessage());
        }
    }

    private void setupAnimation() {
        animationTimer = new Timer(TIMER_DELAY, e -> {
            time += 0.05;
            ufoHoverOffset = (float) (Math.sin(time) * 5);
            stars.forEach(Star::update);
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 0, 40), 0, getHeight(), new Color(0, 0, 20));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        stars.forEach(star -> star.draw(g2d));

        Point currentUfoPos = new Point(getWidth() / 2, getHeight() / 3 + (int) ufoHoverOffset);
        createGlow(g2d, currentUfoPos);
        drawUfo(g2d, currentUfoPos);
        drawBlinkingLights(g2d, currentUfoPos);
    }

    private void createGlow(Graphics2D g2d, Point center) {
        int glowRadius = 60;
        float alpha = 0.1f;

        for (int i = glowRadius; i > 0; i -= 2) {
            g2d.setColor(new Color(0.4f, 0.8f, 1.0f, alpha));
            g2d.fillOval(center.x - i / 2, center.y - i / 4, i, i / 2);
            alpha += 0.001f;
        }
    }

    private void drawUfo(Graphics2D g2d, Point center) {
        g2d.setColor(new Color(100, 200, 255));
        g2d.fillOval(center.x - 40, center.y + 10, 80, 25);

        g2d.setColor(new Color(150, 220, 255));
        g2d.fillOval(center.x - 25, center.y - 15, 50, 35);

        g2d.setColor(new Color(200, 240, 255, 180));
        g2d.fillOval(center.x - 15, center.y - 10, 30, 30);
    }

    private void drawBlinkingLights(Graphics2D g2d, Point center) {
        float intensity = (float) (0.5 + 0.5 * Math.sin(time * 2));
        Color lightColor = new Color(1f, 1f, 0f, intensity);
        g2d.setColor(lightColor);

        g2d.fillOval(center.x - 30, center.y + 25, 6, 6);
        g2d.fillOval(center.x, center.y + 25, 6, 6);
        g2d.fillOval(center.x + 30, center.y + 25, 6, 6);
    }

    public void startAnimation() {
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    public void stopAnimation() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    public void startMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startAnimation();
        startMusic();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAnimation();
        stopMusic();
    }

    private class Star {
        int x, y;
        float brightness;
        float twinkleSpeed;
        float size;

        Star() {
            this.x = random.nextInt(DEFAULT_WIDTH);
            this.y = random.nextInt(DEFAULT_HEIGHT);
            this.brightness = random.nextFloat();
            this.twinkleSpeed = 0.02f + random.nextFloat() * 0.03f;
            this.size = 1 + random.nextFloat() * 2;
        }

        void update() {
            x -= 1;
            brightness += twinkleSpeed;
            if (brightness > 1.0f) {
                brightness = 1.0f;
                twinkleSpeed = -twinkleSpeed;
            } else if (brightness < 0.2f) {
                brightness = 0.2f;
                twinkleSpeed = -twinkleSpeed;
            }

            if (x < 0) {
                x = getWidth() > 0 ? getWidth() : DEFAULT_WIDTH;
                y = random.nextInt(getHeight() > 0 ? getHeight() : DEFAULT_HEIGHT);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(1f, 1f, 1f, brightness));
            g2d.fillOval(x, y, (int) size, (int) size);
        }
    }
}