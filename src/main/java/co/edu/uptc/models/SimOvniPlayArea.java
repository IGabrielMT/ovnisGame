package co.edu.uptc.models;

import co.edu.uptc.interfaces.Interfaces;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SimOvniPlayArea implements Interfaces.Model {
    private final int X_POSITION_GOAL = 400;
    private final int Y_POSITION_GOAL = 20;
    private final int WIDTH_GOAL = 100;
    private final int HEIGHT_GOAL = 37;
    private int ufosArrivedToGoal;
    private int destroyedUfos;
    private ArrayList<Point> trajectoryPoints;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private Rectangle goal;
    private Point goalPoint;
    private Ufo selectedUfo;
    private Interfaces.Presenter presenter;
    private final List<Ufo> ufos;
    private boolean stop;
    private static final int MARGIN = 30;


    public SimOvniPlayArea() {
        goal = new Rectangle(X_POSITION_GOAL, Y_POSITION_GOAL, WIDTH_GOAL, HEIGHT_GOAL);
        goalPoint = new Point(X_POSITION_GOAL, Y_POSITION_GOAL);
        ufos = new ArrayList<>();
        stop = false;
        trajectoryPoints = new ArrayList<>();
    }

    @Override
    public void showDestroyedUfos() {
        presenter.paintDestroyedUfos(destroyedUfos);
    }

    @Override
    public void showUfosArrivedToGoal(){
        presenter.paintUfosArrivedToGoal(ufosArrivedToGoal);
    }

    public void callShows(){
        showDestroyedUfos();
        showUfosArrivedToGoal();
    }

    @SneakyThrows
    private void createOvnis(int quantity, long speed, int appearanceSpeed) {
        new Thread(() -> {
            int i = 0;
            while (!stop && i < quantity) {
                Ufo ufo = new Ufo(speed, false);
                new Thread(ufo).start();
                ufos.add(ufo);
                i++;
                try {
                    Thread.sleep(appearanceSpeed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    @Override
    public void selectUfo(Point point) {
        for (Ufo ufo : ufos) {
            if (isWithinMargin(ufo.getOvni(), point)) {
                if (selectedUfo != null) {
                    selectedUfo.setSelected(false);
                }
                selectedUfo = ufo;
                selectedUfo.setSelected(true);
                break;

            }
        }
    }

    private boolean isWithinMargin(Point ufoPoint, Point clickPoint) {
        return Math.abs(ufoPoint.x - clickPoint.x) <= MARGIN && Math.abs(ufoPoint.y - clickPoint.y) <= MARGIN;
    }

    @Override
    public void moveSelectedUfo(double angle) {
        if (selectedUfo != null) {
            selectedUfo.setDirection(angle);
        }
    }

    @Override
    public void startSim() {
        stop = false;
        int millis = (int) presenter.getOvniSpeed();
        createOvnis(presenter.getQuantity(), millis, presenter.getAppearanceSpeed());
        verifyCollisionForDestruction();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::callShows, 0, 750, TimeUnit.MILLISECONDS);
        verifyCollisionWithGoal();
        paintOvnis(millis);
        presenter.paintGoal(goal, goalPoint);
    }

    private void paintOvnis(int millis) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> presenter.paintOvnis(getOvnis()), 0, millis / 2, TimeUnit.MILLISECONDS);
    }


    @Override
    public ArrayList<Ufo> getOvnis() {
        return new ArrayList<>(ufos);
    }

    @Override
    public void setPresenter(Interfaces.Presenter presenter) {
        this.presenter = presenter;
    }

    private void stop() {
        stop = !stop;
    }

    @Override
    public void endSimulation() {
        for (Ufo ufo : ufos) {
            ufo.setStop(true);
        }
        ufosArrivedToGoal = 0;
        destroyedUfos = 0;
        stop();
        ufos.clear();
    }

    @Override
    public void verifyCollisionForDestruction() {
        scheduler.scheduleAtFixedRate(this::verifyCollisionWithOvnis, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::verifyCollisionWithBorders, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void verifyCollisionWithGoal() {
        new Timer(100, e -> {
            synchronized (ufos) {
                Iterator<Ufo> iterator = ufos.iterator();
                while (iterator.hasNext()) {
                    Ufo ufo = iterator.next();
                    if (conditionGoal(ufo)) {
                        ufo.setStop(stop);
                        iterator.remove();
                        ufosArrivedToGoal++;
                    }
                }
            }
        }).start();
    }
    private boolean conditionGoal(Ufo ufo){
        return ufo.getOvni().getX() > X_POSITION_GOAL - WIDTH_GOAL  - 20 && ufo.getOvni().getX() < X_POSITION_GOAL + WIDTH_GOAL + 20
            && ufo.getOvni().getY() > 20 && ufo.getOvni().getY() < HEIGHT_GOAL + 10;
    }

    @Override
    public ArrayList<Point> getTrajectoryPoints() {
        return trajectoryPoints;
    }

    @Override
    public void addTrajectoryPoint(Point point) {
        trajectoryPoints.add(point);
    }

    private void verifyCollisionWithOvnis() {
        synchronized (ufos) {
            for (int i = 0; i < ufos.size(); i++) {
                Ufo ufo1 = ufos.get(i);
                for (int j = i + 1; j < ufos.size(); j++) {
                    Ufo ufo2 = ufos.get(j);
                    if (checkCollision(ufo1, ufo2)) {
                        ufos.remove(ufo1);
                        ufos.remove(ufo2);
                        destroyedUfos += 2;
                        break;
                    }
                }
            }
        }
    }

    private boolean checkCollision(Ufo ufo1, Ufo ufo2) {
        double distance = Math.sqrt(Math.pow(ufo1.getOvni().x - ufo2.getOvni().x, 2) + Math.pow(ufo1.getOvni().y - ufo2.getOvni().y, 2));
        return distance < (20);
    }

    private void verifyCollisionWithBorders() {
        synchronized (ufos) {
            Iterator<Ufo> iterator = ufos.iterator();
            while (iterator.hasNext()) {
                Ufo ufo = iterator.next();
                if (ufo.getOvni().x <= 0 || ufo.getOvni().x >= 690 || ufo.getOvni().y <= 0 || ufo.getOvni().y >= 580) {
                    ufo.setStop(true);
                    iterator.remove();
                    destroyedUfos++;
                }
            }
        }
    }

}