package co.edu.uptc.models;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Random;

@Getter
@Setter
public class Ufo implements Runnable{
    private Random random;
    private Point ovni;
    private Point previousPosition;
    private double direction;
    private long speed;
    private boolean isStop;
    private boolean isSelected;

    public Ufo(long speed, boolean isStop) {
        ovni = new Point();
        random = new Random();
        previousPosition = new Point();
        this.speed = speed;
        this.isStop = isStop;
        this.isSelected = false;
        setLocation();
    }

    private void setLocation() {
        ovni.setLocation(random.nextInt((700 - 100) + 1) + 100, random.nextInt((500 - 100) + 1) + 100);
        direction = Math.random() * 2 * Math.PI;
    }

    public void move() {
        double distance = 5;
        previousPosition.setLocation(ovni);
        ovni.setLocation(ovni.getX() + distance * Math.cos(direction), ovni.getY() + distance * Math.sin(direction));
    }

    @Override
    public void run() {
        while (!isStop) {
            move();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}