package co.edu.uptc.interfaces;


import co.edu.uptc.models.Ufo;

import java.awt.*;
import java.util.ArrayList;

public interface Interfaces {
    interface View {
        int getAppearanceSpeed();

        int getUfosQuantity();

        int getUfosSpeed();

        void start();

        void setPresenter(Presenter presenter);

        void paint(Point points);

        void endGame();

        void paintGoal(Rectangle goal, Point goalPoint);

        void actualiceDestroyedUfos(int destroyedUfos);

        void actualiceUfosArrivedToGoal(int ufosArrivedToGoal);
    }
    interface Presenter{

        void setModel(Model model);

        void setView(View view);

        void paintOvnis(ArrayList<Ufo> ovnis);

        void startGame();

        int getQuantity();

        long getOvniSpeed();

        int getAppearanceSpeed();

        void stopSim();

        void selectUfo(Point point);

        void moveSelectedUfo();

        void addTrajectoryPoint(Point point);

        void paintGoal(Rectangle goal, Point goalPoint);

        void paintDestroyedUfos(int destroyedUfos);

        void paintUfosArrivedToGoal(int ufosArrivedToGoal);
    }
    interface Model{

        void showDestroyedUfos();

        void showUfosArrivedToGoal();

        void selectUfo(Point point);

        void moveSelectedUfo(double destination);

        void startSim();

        ArrayList<Ufo> getOvnis();

        void setPresenter(Presenter presenter);

        void endSimulation();

        void verifyCollisionForDestruction();

        Ufo getSelectedUfo();

        void verifyCollisionWithGoal();

        ArrayList<Point> getTrajectoryPoints();

        void addTrajectoryPoint(Point point);
    }

}
