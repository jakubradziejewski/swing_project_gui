import javax.swing.*;
import java.awt.*;

public class SetupDialog extends JDialog {
    private final JSpinner fieldSizeSpinner;
    private final JSpinner farmerCountSpinner;
    private final JSpinner rabbitSpawnSpinner;
    private final JSpinner growthTimeSpinner;
    private final JSpinner detectionRangeSpinner;
    private final JSpinner repairTimeSpinner;
    private final JSpinner plantTimeSpinner;
    private boolean cancelled = true;

    public SetupDialog(Frame owner) {
        super(owner, "Game Setup", true);

        // Create spinners with default values
        fieldSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 20, 1));
        farmerCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 5, 1));
        rabbitSpawnSpinner = new JSpinner(new SpinnerNumberModel(0.3, 0.0, 1.0, 0.05));
        growthTimeSpinner = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 100));
        detectionRangeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        repairTimeSpinner = new JSpinner(new SpinnerNumberModel(3000, 500, 10000, 500));
        plantTimeSpinner = new JSpinner(new SpinnerNumberModel(2000, 500, 10000, 500));

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Make components fill horizontal space

        // Game Setup section
        addSectionLabel("Game Setup", 0, gbc);
        addConfigField("Field Size:", fieldSizeSpinner, 1, gbc);
        addConfigField("Number of Farmers:", farmerCountSpinner, 2, gbc);

        // Game Parameters section
        addSectionLabel("Game Parameters", 3, gbc);
        addConfigField("Rabbit Spawn Rate:", rabbitSpawnSpinner, 4, gbc);
        addConfigField("Carrot Growth Time (ms):", growthTimeSpinner, 5, gbc);
        addConfigField("Dog Detection Range:", detectionRangeSpinner, 6, gbc);
        addConfigField("Farmer Repair Time (ms):", repairTimeSpinner, 7, gbc);
        addConfigField("Farmer Plant Time (ms):", plantTimeSpinner, 8, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");

        startButton.addActionListener(e -> {
            saveConfig();
            cancelled = false;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            cancelled = true;
            dispose();
        });

        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Dialog properties
        setMinimumSize(new Dimension(400, 500));  // Set minimum size for the dialog
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void addSectionLabel(String text, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(label, gbc);
        gbc.gridwidth = 1;  // Reset gridwidth
    }

    private void addConfigField(String label, JComponent component, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }

    private void saveConfig() {
        GameConfig config = GameConfig.getInstance();
        config.setRabbitSpawnRate((Double) rabbitSpawnSpinner.getValue());
        config.setCarrotGrowthTime((Integer) growthTimeSpinner.getValue());
        config.setDogDetectionRange((Integer) detectionRangeSpinner.getValue());
        config.setFarmerRepairTime((Integer) repairTimeSpinner.getValue());
        config.setFarmerPlantTime((Integer) plantTimeSpinner.getValue());
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public int getFieldSize() {
        return (Integer) fieldSizeSpinner.getValue();
    }

    public int getFarmerCount() {
        return (Integer) farmerCountSpinner.getValue();
    }
    private JSpinner createWideSpinner(SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            // Make the text field wider
            Dimension prefSize = textField.getPreferredSize();
            prefSize.width = 150;  // Set preferred width
            textField.setPreferredSize(prefSize);
        }
        return spinner;
    }
}