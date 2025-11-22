// PlayerSetupDialog.java
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerSetupDialog extends JDialog {
    private ArrayList<String> playerNames;
    private ArrayList<Boolean> aiSelections;
    private boolean confirmed = false;

    // Components that need to be accessed across methods
    private JPanel configPanel;
    private JComboBox<Integer> playerCountCombo;
    private ArrayList<JTextField> nameFields;
    private ArrayList<JCheckBox> aiCheckboxes;

    public PlayerSetupDialog(Frame parent) {
        super(parent, "UNO Game Setup", true);
        this.playerNames = new ArrayList<>();
        this.aiSelections = new ArrayList<>();
        this.nameFields = new ArrayList<>();
        this.aiCheckboxes = new ArrayList<>();

        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(500, 400);
        setMinimumSize(new Dimension(450, 350));
        setLocationRelativeTo(getParent());
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("UNO Game Setup", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Player count selection
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        countPanel.add(new JLabel("Number of players (2-4):"));
        playerCountCombo = new JComboBox<>(new Integer[]{2, 3, 4});
        playerCountCombo.setPreferredSize(new Dimension(60, 25));
        countPanel.add(playerCountCombo);
        mainPanel.add(countPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Player configuration panel
        configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Configure Players"));

        JScrollPane scrollPane = new JScrollPane(configPanel);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Initial setup for 2 players
        updatePlayerRows(2);

        // Update player rows when count changes
        playerCountCombo.addActionListener(e -> {
            int count = (Integer) playerCountCombo.getSelectedItem();
            updatePlayerRows(count);
        });

        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Instructions
        JLabel instructionLabel = new JLabel("Check 'AI Player' to make a player computer-controlled", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(instructionLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 35));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));

        // Style buttons
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));

        startButton.addActionListener(e -> {
            int count = (Integer) playerCountCombo.getSelectedItem();

            // Validate inputs
            for (int i = 0; i < count; i++) {
                String name = nameFields.get(i).getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a name for Player " + (i + 1),
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Store selections
            playerNames.clear();
            aiSelections.clear();

            for (int i = 0; i < count; i++) {
                playerNames.add(nameFields.get(i).getText().trim());
                aiSelections.add(aiCheckboxes.get(i).isSelected());
            }

            confirmed = true;
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updatePlayerRows(int count) {
        configPanel.removeAll();

        // Clear the lists BEFORE adding new components
        nameFields.clear();
        aiCheckboxes.clear();

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        headerPanel.setMaximumSize(new Dimension(400, 30));

        JLabel playerHeader = new JLabel("Player", JLabel.CENTER);
        playerHeader.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(playerHeader);

        JLabel nameHeader = new JLabel("Name", JLabel.CENTER);
        nameHeader.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(nameHeader);

        JLabel aiHeader = new JLabel("AI Player", JLabel.CENTER);
        aiHeader.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(aiHeader);

        configPanel.add(headerPanel);
        configPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Create fresh player rows
        for (int i = 1; i <= count; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 3, 10, 5));
            rowPanel.setMaximumSize(new Dimension(400, 35));
            rowPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

            // Player label
            JLabel playerLabel = new JLabel("Player " + i, JLabel.CENTER);
            playerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            rowPanel.add(playerLabel);

            // Name field with unique default name
            JTextField nameField = new JTextField("Player " + i);
            nameField.setPreferredSize(new Dimension(150, 25));
            nameField.setFont(new Font("Arial", Font.PLAIN, 12));
            nameFields.add(nameField); // Add to list immediately
            rowPanel.add(nameField);

            // AI checkbox
            JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            JCheckBox aiCheckbox = new JCheckBox();
            aiCheckbox.setPreferredSize(new Dimension(20, 20));
            aiCheckboxes.add(aiCheckbox); // Add to list immediately
            checkboxPanel.add(aiCheckbox);
            rowPanel.add(checkboxPanel);

            configPanel.add(rowPanel);

            // Add spacing between rows
            if (i < count) {
                configPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        configPanel.revalidate();
        configPanel.repaint();

        // Adjust dialog size
        int baseHeight = 350;
        int rowHeight = 35;
        int newHeight = baseHeight + (count * rowHeight);
        setSize(500, Math.min(newHeight, 600));
        setLocationRelativeTo(getParent());
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public ArrayList<Boolean> getAISelections() {
        return aiSelections;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}