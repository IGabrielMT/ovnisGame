package co.edu.uptc.views;

import co.edu.uptc.interfaces.Interfaces;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainFrame extends JFrame implements Interfaces.View {
    private Timer timer;
    private static MainFrame instance;
    private Interfaces.Presenter presenter;
    private JDialog dialog;
    private JTextField appearanceSpeed;
    private JTextField ufoQuantity;
    private JTextField ufoSpeed;
    @Getter
    private JComboBox<String> ovniTypeComboBox;
    private CustomButton okButton;
    private int appearanceSpeedValue;
    private int ufoQuantityValue;
    private int ufoSpeedValue;
    private int destroyedUfos;
    private int ufosArrivedToGoal;


    private MainFrame() {
        super("Simulador de ufo");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        initComponents();
        initTimer();
    }

    private void initTimer() {
        timer = new Timer(666, e -> GamePanel.getInstance(presenter).setUfos(new CopyOnWriteArrayList<>()));
    }

    public static MainFrame getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (MainFrame.class) {
            if (instance == null) {
                instance = new MainFrame();
            }
        }
        return instance;
    }

    private void initComponents() {
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);
        createPlayPanel();
    }

    private void createPlayPanel() {
        addNorthPanel();
        addPlayPanel();
        addSouthPanel();
    }

    private void addNorthPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Global.COLOR_BACKGROUND);
        northPanel.setOpaque(false);
        northPanel.add(Box.createVerticalStrut(100));
        JLabel titleLabel = new JLabel("JUEGUITO DE UFO");
        titleLabel.setFont(Global.FONT_TITLE_GAME);
        titleLabel.setForeground(Global.COLOR_TEXT);
        northPanel.add(titleLabel);
        add(northPanel, BorderLayout.NORTH);
    }

    private void addPlayPanel() {
        JPanel playPanel = new JPanel();
        playPanel.setOpaque(false);
        playPanel.setBackground(Global.COLOR_BACKGROUND);
        addButtons(playPanel);
        add(playPanel, BorderLayout.CENTER);
    }

    private void addSouthPanel() {
        CustomButton exitButton = new CustomButton("Salir");
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setBackground(Global.COLOR_BACKGROUND);
        southPanel.add(Box.createVerticalStrut(150));
        southPanel.add(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        add(southPanel, BorderLayout.SOUTH);
    }

    private void addButtons(JPanel playPanel) {
        CustomButton startButton = new CustomButton("Start");
        startButton.addActionListener(e -> createConfigPanel());
        playPanel.add(startButton);
    }

    private void createConfigPanel() {
        okButton = new CustomButton("OK");
        okButton.setFont(Global.FONT_TEXTS_SMALL);
        okButton.setEnabled(false);
        okButton.addActionListener(e -> {
            initiateValues();
            dialog.dispose();
            presenter.startGame();
            createGameJDialog();
        });

        initiateConfigDialog();
        dialog.setVisible(true);
    }


    private void initiateValues() {
        appearanceSpeedValue = Integer.parseInt(appearanceSpeed.getText());
        ufoQuantityValue = Integer.parseInt(ufoQuantity.getText());
        ufoSpeedValue = Integer.parseInt(ufoSpeed.getText());
    }

    private void initiateConfigDialog() {
        createAndConfigureDialog();
        JPanel mainPanel = createMainPanel();
        assemblePanels(mainPanel);
        dialog.setContentPane(mainPanel);
    }

    private void createAndConfigureDialog() {
        dialog = new JDialog(this, "Configuración", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 300);
        dialog.setLocation(getLocation().x + 300, getLocation().y + 200);
        dialog.setBackground(new Color(0, 0, 0, 150));
        dialog.setOpacity(0.9f);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Global.COLOR_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return mainPanel;
    }

    private void assemblePanels(JPanel mainPanel) {
        JPanel fieldsPanel = createFieldsPanel();
        JPanel infoPanel = createInfoPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(fieldsPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        addFieldsToPanel(fieldsPanel);
        return fieldsPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        addInfoToPanel(infoPanel);
        return infoPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        return buttonPanel;
    }

    private void addFieldsToPanel(JPanel panel) {
        GridBagConstraints gbc = createFieldConstraints();
        JLabel[] labels = createLabels();
        JTextField[] textFields = createTextFields();
        JLabel ovniLabel = new JLabel("Tipo de ovni:");
        styleLabel(ovniLabel);
        ovniTypeComboBox = new JComboBox<>(new String[]{"Ovni 1", "Ovni 2", "Ovni 3"});
        ovniTypeComboBox.setBackground(Global.COLOR_BACKGROUND);
        ovniTypeComboBox.setForeground(Global.COLOR_TEXT);
        addLabelsAndFields(panel, gbc, labels, textFields, ovniLabel, ovniTypeComboBox);
    }

    private GridBagConstraints createFieldConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addLabelsAndFields(JPanel panel, GridBagConstraints gbc, JLabel[] labels, JTextField[] textFields, JLabel ovniLabel, JComponent ovniTypeComboBox) {
        for (int i = 0; i < labels.length; i++) {
            styleLabel(labels[i]);
            styleTextField(textFields[i]);
            addLabelAndField(panel, gbc, labels[i], textFields[i], i);
            addDocumentListener(textFields[i], textFields);
        }
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.weightx = 0.3;
        panel.add(ovniLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(ovniTypeComboBox, gbc);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void addDocumentListener(JTextField textField, JTextField[] allFields) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFields(allFields, okButton);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFields(allFields, okButton);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFields(allFields, okButton);
            }
        });
    }

    private void addInfoToPanel(JPanel panel) {
        JLabel[] infoLabels = createInfoLabels();
        addInfoLabels(panel, infoLabels);
    }

    private JLabel[] createInfoLabels() {
        return new JLabel[] {
                new JLabel(" - Velocidad: tiempo en ms que tarda en moverse (minimo 20)"),
                new JLabel(" - Velocidad de aparecimiento: tiempo en ms que tarda"),
                new JLabel("   en aparecer un nuevo ufo (minimo 100)"),
                new JLabel(" - Numero de los ufo: cantidad de ufos que apareceran (maximo 5)"),
                new JLabel(" - Tipo de ovni: selecciona el ovni a utilizar")
        };
    }

    private void addInfoLabels(JPanel panel, JLabel[] labels) {
        panel.add(Box.createVerticalStrut(10));
        for (int i = 0; i < labels.length; i++) {
            styleLabel(labels[i]);
            panel.add(labels[i]);
            if (i < labels.length - 1) {
                panel.add(Box.createVerticalStrut(5));
            }
        }
    }

    private void checkFields(JTextField[] textFields, CustomButton okButton) {
        try {
            int appearanceSpeedValue = Integer.parseInt(textFields[0].getText().trim());
            int ufoQuantityValue = Integer.parseInt(textFields[1].getText().trim());
            int ufoSpeedValue = Integer.parseInt(textFields[2].getText().trim());
            okButton.setEnabled(appearanceSpeedValue >= 100 && ufoQuantityValue <= 5 && ufoSpeedValue >= 20);

        } catch (NumberFormatException e) {
            okButton.setEnabled(false);
        }
    }

    private JLabel[] createLabels() {
        return new JLabel[]{
                new JLabel("Velocidad de aparecimiento: "),
                new JLabel("Numero de los ufo: "),
                new JLabel("Velocidad de los ufo: ")
        };
    }

    private JTextField[] createTextFields() {
        appearanceSpeed = new JTextField(10);
        ufoQuantity = new JTextField(10);
        ufoSpeed = new JTextField(10);

        return new JTextField[]{appearanceSpeed, ufoQuantity, ufoSpeed};
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Global.COLOR_TEXT);
    }

    private void styleTextField(JTextField textField) {
        textField.setForeground(Global.COLOR_TEXT);
        textField.setBackground(Global.COLOR_BACKGROUND);
        textField.setCaretColor(Global.COLOR_TEXT);
        textField.setBorder(BorderFactory.createLineBorder(Global.COLOR_TEXT));
    }

    private void createGameJDialog(){
        JDialog gameDialog = GameDialog.getInstance(this, presenter);
        gameDialog.setVisible(true);
    }

    @Override
    public int getAppearanceSpeed() {
        return appearanceSpeedValue;
    }

    @Override
    public int getUfosQuantity() {
        return ufoQuantityValue;
    }

    @Override
    public int getUfosSpeed() {
        return ufoSpeedValue;
    }

    @Override
    public void start() {
        setVisible(true);
    }

    @Override
    public void setPresenter(Interfaces.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void paint(Point points) {
        timer.start();
        GamePanel.getInstance(presenter).paintOvnis(points);
    }


    @Override
    public void endGame() {
        presenter.stopSim();
    }

    @Override
    public void paintGoal(Rectangle goal, Point goalPoint) {
        GamePanel.getInstance(presenter).paintGoal(goal, goalPoint);
    }

    @Override
    public void actualiceDestroyedUfos(int destroyedUfos) {
        this.destroyedUfos = destroyedUfos;
        GameDialog.getInstance(this, presenter).updateUfos(new int[]{destroyedUfos, ufosArrivedToGoal});
    }

    @Override
    public void actualiceUfosArrivedToGoal(int ufosArrivedToGoal) {
        this.ufosArrivedToGoal = ufosArrivedToGoal;
        GameDialog.getInstance(this, presenter).updateUfos(new int[]{destroyedUfos, ufosArrivedToGoal});
    }
}