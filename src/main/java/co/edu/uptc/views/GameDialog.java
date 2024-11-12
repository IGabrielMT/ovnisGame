package co.edu.uptc.views;

import co.edu.uptc.interfaces.Interfaces;

import javax.swing.*;
import java.awt.*;

public class GameDialog extends JDialog {
    private static volatile GameDialog instance;
    private final Interfaces.Presenter presenter;
    private JLabel ufosDestroyed;
    private JLabel ufosArrivedToGoal;

    private GameDialog(JFrame parent, Interfaces.Presenter presenter) {
        super(parent, "Game Dialog", true);
        this.presenter = presenter;
        configureDialog(parent);
        initComponents();
    }

    private void configureDialog(JFrame parent) {
        setSize(1000, 600);
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    public static GameDialog getInstance(JFrame parent, Interfaces.Presenter presenter) {
        if (instance == null) {
            synchronized (GameDialog.class) {
                if (instance == null) {
                    instance = new GameDialog(parent, presenter);
                }
            }
        }
        return instance;
    }

    private void initComponents() {
        add(createWestPanel(), BorderLayout.WEST);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createWestPanel() {
        JPanel westPanel = createBaseWestPanel();
        addWestPanelComponents(westPanel);
        return westPanel;
    }

    private JPanel createBaseWestPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setSize(200, 600);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panel;
    }

    private void addWestPanelComponents(JPanel westPanel) {
        addTitleLabel(westPanel, "Parametros Globales");
        addGlobalParameters(westPanel);
        addUfoCounters(westPanel);
        addInstructions(westPanel);
        addExitButton(westPanel);
    }

    private void addTitleLabel(JPanel panel, String text) {
        JLabel label = createStyledLabel(text);
        panel.add(label);
    }

    private void addGlobalParameters(JPanel panel) {
        MainFrame mainFrame = MainFrame.getInstance();
        String[] parameters = {
                "Cantidad de ufo " + mainFrame.getUfosQuantity(),
                "Velocidad de ufo " + mainFrame.getUfosSpeed(),
                "Velocidad de aparición de ufo " + mainFrame.getAppearanceSpeed()
        };

        for (String text : parameters) {
            panel.add(createStyledLabel(text));
        }
    }

    private void addUfoCounters(JPanel panel) {
        ufosDestroyed = createStyledLabel("Ovnis destruidos: 0");
        ufosArrivedToGoal = createStyledLabel("Ovnis llegados a la meta: 0");
        panel.add(ufosDestroyed);
        panel.add(ufosArrivedToGoal);
    }

    private void addInstructions(JPanel panel) {
        addTitleLabel(panel, "Instrucciones");
        String[] instructions = {
                "Click izquierdo: Seleccionar ovni",
                "Click derecho: Agregar punto de trayectoria",
                "Click derecho de nuevo: Mover"
        };
        for (String instruction : instructions) {
            panel.add(createStyledLabel(instruction));
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(Global.COLOR_BACKGROUND);
        label.setForeground(Global.COLOR_TEXT);
        label.setFont(Global.FONT_TEXTS_SMALL);
        return label;
    }

    private void addExitButton(JPanel panel) {
        CustomButton exitButton = createExitButton();
        panel.add(Box.createVerticalGlue());
        panel.add(exitButton, BorderLayout.SOUTH);
    }

    private CustomButton createExitButton() {
        CustomButton button = new CustomButton("     Salir     ");
        button.addActionListener(e -> handleExit());
        return button;
    }

    private void handleExit() {
        GamePanel.getInstance(presenter).resetGamePanel();
        MainFrame.getInstance().endGame();
        dispose();
        instance = null;
    }

    private JPanel createCenterPanel() {
        return GamePanel.getInstance(presenter);
    }

    public void updateUfos(int[] ufos) {
        ufosDestroyed.setText("Ovnis destruidos: " + ufos[0]);
        ufosArrivedToGoal.setText("Ovnis llegados a la meta: " + ufos[1]);
    }
}