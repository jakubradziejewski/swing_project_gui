import javax.swing.*;
import java.awt.*;

public class GameConfigDialog extends JDialog {
    private final JSpinner fieldSizeSpinner;
    private final JSpinner farmerCountSpinner;
    private final JSpinner spawnRateSpinner;
    private final JSpinner growthTimeSpinner;
    private final JSpinner detectionRangeSpinner;
    private final JSpinner repairTimeSpinner;
    private final JSpinner plantTimeSpinner;
    private boolean cancelled = true;
    private final boolean isInitialSetup;

    public GameConfigDialog(Frame owner, boolean isInitialSetup) {
        super(owner, isInitialSetup ? "Game Setup" : "Game Configuration", true);
        this.isInitialSetup = isInitialSetup;

        GameConfig config = GameConfig.getInstance();

        // Create spinners with current/default values
        fieldSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 20, 1));
        farmerCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 5, 1));
        spawnRateSpinner = new JSpinner(new SpinnerNumberModel(
                config.getRabbitSpawnRate(), 0.0, 1.0, 0.05));
        growthTimeSpinner = new JSpinner(new SpinnerNumberModel(
                config.getCarrotGrowthTime(), 100, 5000, 100));
        detectionRangeSpinner = new JSpinner(new SpinnerNumberModel(
                config.getDogDetectionRange(), 1, 10, 1));
        repairTimeSpinner = new JSpinner(new SpinnerNumberModel(
                config.getFarmerRepairTime(), 500, 10000, 500));
        plantTimeSpinner = new JSpinner(new SpinnerNumberModel(
                config.getFarmerPlantTime(), 500, 10000, 500));

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Game Setup section (only shown during initial setup)
        if (isInitialSetup) {
            addSectionLabel("Game Setup", 0, gbc);
            addConfigField("Field Size:", fieldSizeSpinner, 1, gbc);
            addConfigField("Number of Farmers:", farmerCountSpinner, 2, gbc);
        }

        // Game Parameters section
        addSectionLabel("Game Parameters", isInitialSetup ? 3 : 0, gbc);
        addConfigField("Rabbit Spawn Rate:", spawnRateSpinner, isInitialSetup ? 4 : 1, gbc);
        addConfigField("Carrot Growth Time (ms):", growthTimeSpinner, isInitialSetup ? 5 : 2, gbc);
        addConfigField("Dog Detection Range:", detectionRangeSpinner, isInitialSetup ? 6 : 3, gbc);
        addConfigField("Farmer Repair Time (ms):", repairTimeSpinner, isInitialSetup ? 7 : 4, gbc);
        addConfigField("Farmer Plant Time (ms):", plantTimeSpinner, isInitialSetup ? 8 : 5, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton(isInitialSetup ? "Start Game" : "Save");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(e -> {
            saveConfig();
            cancelled = false;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = isInitialSetup ? 9 : 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Dialog properties
        setMinimumSize(new Dimension(400, isInitialSetup ? 500 : 400));
        pack();
        setLocationRelativeTo(owner);
    }

    private void addSectionLabel(String text, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(label, gbc);
        gbc.gridwidth = 1;
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
        config.setRabbitSpawnRate((Double) spawnRateSpinner.getValue());
        config.setCarrotGrowthTime((Integer) growthTimeSpinner.getValue());
        config.setDogDetectionRange((Integer) detectionRangeSpinner.getValue());
        config.setFarmerRepairTime((Integer) repairTimeSpinner.getValue());
        config.setFarmerPlantTime((Integer) plantTimeSpinner.getValue());
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public int getFieldSize() {
        return isInitialSetup ? (Integer) fieldSizeSpinner.getValue() : -1;
    }

    public int getFarmerCount() {
        return isInitialSetup ? (Integer) farmerCountSpinner.getValue() : -1;
    }
}