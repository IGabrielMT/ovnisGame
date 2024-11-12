package co.edu.uptc.presenter;

import co.edu.uptc.interfaces.Interfaces;
import co.edu.uptc.models.SimOvniPlayArea;
import co.edu.uptc.views.MainFrame;

public class RunProgram {
    public void run() {
        Interfaces.View view = MainFrame.getInstance();
        Interfaces.Presenter presenter = new Presenter();
        Interfaces.Model model = new SimOvniPlayArea();
        presenter.setModel(model);
        presenter.setView(view);
        model.setPresenter(presenter);
        view.setPresenter(presenter);
        view.start();
    }
}
