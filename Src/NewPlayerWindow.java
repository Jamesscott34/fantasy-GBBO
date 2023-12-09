import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NewPlayerWindow extends JFrame {

    private final DefaultListModel<String> selectedContestantsModel;

    public NewPlayerWindow(String enteredUsername) {
        setTitle("New Player - "+ enteredUsername);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        List<String> contestantNames = CSVHandler.readContestantNames();

        JList<String> contestantsList = new JList<>(contestantNames.toArray(new String[0]));
        contestantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton removeButton = new JButton("Remove");
        JButton saveButton = new JButton("Save Selection");
        JButton InfoButton = new JButton("Info");
        selectedContestantsModel = new DefaultListModel<>();
        JList<String> selectedContestantsList = new JList<>(selectedContestantsModel);

        JScrollPane scrollPane = new JScrollPane(contestantsList);
        scrollPane.setPreferredSize(new Dimension(300, 300));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.WEST);
        panel.add(selectedContestantsList, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(InfoButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);

        contestantsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    if (!selectedContestantsModel.contains(list.getModel().getElementAt(index))) {
                        selectedContestantsModel.addElement(list.getModel().getElementAt(index));
                    }
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectedContestantsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    selectedContestantsModel.remove(selectedIndex);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedContestantsModel.size() == 3) {
                    String[] selectedContestants = new String[3];
                    for (int i = 0; i < 3; i++) {
                        selectedContestants[i] = selectedContestantsModel.getElementAt(i);
                    }
                    CSVHandler.createPlayerCSV(enteredUsername, selectedContestants);
                    dispose();
                    new ExistingPlayerWindow(enteredUsername);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select exactly 3 contestants.");
                }
            }
        });

        InfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InfoWindow(NewPlayerWindow.this, enteredUsername);
                dispose();
            }
        });

    }

    private static class InfoWindow {
        private final JFrame infoFrame;
        private static String enteredUsername = null;

        private InfoWindow(JFrame previousFrame, String enteredUsername) {
            InfoWindow.enteredUsername = enteredUsername;

            infoFrame = new JFrame("Game Information");
            infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            infoFrame.setSize(300, 200);

            JLabel infoLabel = new JLabel(getMessage());
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoFrame.add(infoLabel, BorderLayout.CENTER);

            JButton continueButton = new JButton("Continue");
            continueButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    infoFrame.dispose();
                    previousFrame.setVisible(true); // Show the previous frame
                }
            });
            infoFrame.add(continueButton, BorderLayout.SOUTH);

            infoFrame.setVisible(true);
        }
        public static String getMessage() {
            return "Fantasy GBBO - Game Information\n" +
                    "Hello "+ enteredUsername+ "\n"+
                    "Initial Player Setup:\n" +
                    "Choose 3 Contestants: Each player selects three contestants from the show.\n" +
                    "Existing Player Rules:\n" +
                    "Weekly Score Update: Points are awarded based on the placement of selected contestants in the cook-offs. Scores are updated weekly.\n" +
                    "Contestant Removal: When a contestant is eliminated from the show, they are removed from the player's list. Players can only have a maximum of 2 contestants at any time.\n" +
                    "Scoring System:\n" +
                    "Star Baker: Contestant wins Star Baker: +10 points\n" +
                    "Top 3 Placement: Contestant places in the top 3: +5 points\n" +
                    "Safe from Elimination: Contestant is safe from elimination: +3 points\n" +
                    "Team Challenge Win: Contestant's team wins a challenge: +5 points\n" +
                    "Technical Challenge Win: Contestant wins a technical challenge: +5 points\n" +
                    "Example Scoring Update (Weekly):\n" +
                    "Contestant 1: Star Baker (+10 points), Top 3 Placement (+5 points), Safe from Elimination (+3 points) = Total: 18 points\n" +
                    "Contestant 2: Top 3 Placement (+5 points), Team Challenge Win (+5 points) = Total: 10 points\n" +
                    "Contestant 3: Safe from Elimination (+3 points) = Total: 3 points\n" +
                    "Contestant Elimination:\n" +
                    "When a contestant is eliminated from the show, they are removed from the player's list.\n" +
                    "Players are then allowed to select a new contestant to replace the eliminated one, but they must maintain a total of 2 contestants.\n" +
                    "Winner Determination:\n" +
                    "The player with the highest total points at the end of the season wins the Fantasy GBBO game.\n" +
                    "Notes:\n" +
                    "Scores are updated after each episode.\n" +
                    "Players can strategize and change their selected contestants during the season based on performance and elimination."; // Customize your message here
        }
    }

}
