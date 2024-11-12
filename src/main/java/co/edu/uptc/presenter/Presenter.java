package co.edu.uptc.presenter;

import co.edu.uptc.interfaces.Interfaces;
import co.edu.uptc.models.Ufo;
import javax.swing.Timer;
import java.awt.*;
import java.util.ArrayList;

public class Presenter implements Interfaces.Presenter {
    private Interfaces.Model model;
    private Interfaces.View view;

    @Override
    public void setModel(Interfaces.Model model) {
        this.model = model;
    }

    @Override
    public void setView(Interfaces.View view) {
        this.view = view;
    }

    @Override
    public void paintOvnis(ArrayList<Ufo> ufos) {
        for (Ufo ufo : ufos) {
            view.paint(ufo.getOvni());
        }
    }

    @Override
    public void startGame() {
        model.startSim();
    }

    @Override
    public int getQuantity() {
        return view.getUfosQuantity();
    }

    @Override
    public long getOvniSpeed() {
        return view.getUfosSpeed();
    }

    @Override
    public int getAppearanceSpeed() {
        return view.getAppearanceSpeed();
    }

    @Override
    public void stopSim() {
        model.endSimulation();
    }

    @Override
    public void selectUfo(Point point) {
        model.selectUfo(point);
    }


    @Override
    public void moveSelectedUfo() {
        if (model.getSelectedUfo() != null && !model.getTrajectoryPoints().isEmpty()) {
            new Timer(50, e -> {
                if (!model.getTrajectoryPoints().isEmpty()) {
                    Point ufoLocation = model.getSelectedUfo().getOvni();
                    Point nextLocation = model.getTrajectoryPoints().removeFirst();
                    double angle = Math.atan2(nextLocation.y - ufoLocation.y, nextLocation.x - ufoLocation.x);
                    model.moveSelectedUfo(angle);
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }).start();
        }
    }

    @Override
    public void addTrajectoryPoint(Point point) {
        model.addTrajectoryPoint(point);
    }

    @Override
    public void paintGoal(Rectangle goal, Point goalPoint) {
        view.paintGoal(goal, goalPoint);
    }

    @Override
    public void paintDestroyedUfos(int destroyedUfos) {
        view.actualiceDestroyedUfos(destroyedUfos);
    }

    @Override
    public void paintUfosArrivedToGoal(int ufosArrivedToGoal) {
        view.actualiceUfosArrivedToGoal(ufosArrivedToGoal);
    }

}
