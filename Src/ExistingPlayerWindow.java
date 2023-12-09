import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExistingPlayerWindow extends JFrame {
    static String enteredUsername;
    private final JList<String> contestantsList;
    private boolean playerInformationDisplayed = false;

    public ExistingPlayerWindow(String enteredUsername) {
        ExistingPlayerWindow.enteredUsername = enteredUsername;

        setTitle("Player - "+ enteredUsername);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JButton updateButton = new JButton("Update");
        JButton contestantsButton = new JButton("Contestants");
        JButton emailButton = new JButton("Email");
        JButton InfoButton = new JButton("Info");

        CSVHandler csvHandler = new CSVHandler();
        List<String[]> playerData = CSVHandler.readPlayerData(enteredUsername);
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String[] player : playerData) {
            listModel.addElement(Arrays.toString(player));
        }

        contestantsList = new JList<>(listModel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(contestantsList), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(updateButton);
        bottomPanel.add(contestantsButton);
        bottomPanel.add(emailButton);
        bottomPanel.add(InfoButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUsernameFromApplication();
                List<String> selectedContestants = getSelectedContestantsFromUI(contestantsList);
                List<String[]> contestantData = CSVHandler.readContestantData("Contestants name");

                List<String[]> updatedScores = new ArrayList<>();
                List<String> playerPicksToRemove = new ArrayList<>();

                // Update scores for contestants and identify player picks to remove
                for (String[] contestant : contestantData) {
                    String contestantName = contestant[0];
                    boolean isUserContestant = selectedContestants.contains(contestantName);
                    int currentScore = isUserContestant ? Integer.parseInt(contestant[4]) : 0;

                    if (isUserContestant) {
                        int updatedScore = currentScore + 10;
                        contestant[4] = String.valueOf(updatedScore);
                    } else {
                        playerPicksToRemove.add(contestantName);
                    }

                    updatedScores.add(new String[]{contestantName, contestant[4]});
                }

                // Remove player picks not present in the updated selected contestants list
                List<String> currentPicks = CSVHandler.getSelectedContestantsFromCSV();
                for (String pick : currentPicks) {
                    if (!selectedContestants.contains(pick)) {
                        playerPicksToRemove.add(pick);
                    }
                }

                // Update contestant scores
                CSVHandler.updateContestantScores(updatedScores);

                // Remove player picks that are not in the updated selected contestants list
                for (String playerPick : playerPicksToRemove) {
                    CSVHandler.removeContestantFromUsers(playerPick);
                }

                // Create/update the player CSV with selected contestants
                CSVHandler.createPlayerCSV(username, selectedContestants.toArray(new String[0]));

                // Refresh the existing player window with the updated information
                new ExistingPlayerWindow(username);
            }
        });




        contestantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!playerInformationDisplayed) {
                    displayPlayerInformation();
                    playerInformationDisplayed = true;
                } else {
                    removePlayerInformation();
                    playerInformationDisplayed = false;
                }
            }
        });

        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmailWindow();
                dispose();
            }
        });
        InfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InfoWindow(ExistingPlayerWindow.this); // Pass the correct window instance
                dispose();
            }
        });
    }
    private void displayPlayerInformation() {
        List<String[]> playerData = CSVHandler.readPlayer(); // Assuming this method reads Players.csv
        for (String[] player : playerData) {
            // Display player information as needed
            System.out.println(Arrays.toString(player)); // Replace this with your display logic
        }
    }

    private void removePlayerInformation() {
        if (contestantsList != null) {
            DefaultListModel<String> listModel = (DefaultListModel<String>) contestantsList.getModel();
            listModel.clear();
            playerInformationDisplayed = false;
        }
    }



    private List<String> getSelectedContestantsFromUI(JList<String> list) {
        List<String> selectedContestants = new ArrayList<>();

        int[] selectedIndices = list.getSelectedIndices();
        List<String> contestantNames = CSVHandler.readContestantNames(); // Assuming CSVHandler method exists

        for (int index : selectedIndices) {
            if (index >= 0 && index < contestantNames.size()) {
                selectedContestants.add(contestantNames.get(index));
            }
        }

        return selectedContestants;
    }

    private String getUsernameFromApplication() {
        // Assuming the entered username is the name of the CSV file
        return enteredUsername; // Assuming 'enteredUsername' is accessible in this scope
    }

    private static class InfoWindow extends JFrame {
        InfoWindow(JFrame previousFrame) {

            setTitle("Game Information");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 200);

            JLabel infoLabel = new JLabel(getMessage());
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            add(infoLabel, BorderLayout.CENTER);

            JButton continueButton = new JButton("Continue");
            continueButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    previousFrame.setVisible(true); // Show the previous frame
                }
            });
            add(continueButton, BorderLayout.SOUTH);

            setVisible(true);
        }
        public static String getMessage() {
            return "Fantasy GBBO - Game Information\n" +
                    "Hello "+enteredUsername+"\n"+
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
    public static class EmailWindow extends JFrame {
        private final JTextField fromField;
        private final JPasswordField passwordField; // Add this field for the password
        private final JTextField toField;
        private final JTextArea messageArea;
        private final JComboBox<String> userList;

        private EmailWindow() {
            setTitle("Send Email");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(600, 400);
            setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());

            JPanel topPanel = new JPanel(new BorderLayout());

            JPanel fromPanel = new JPanel(new FlowLayout());
            JLabel fromLabel = new JLabel("From:");
            fromField = new JTextField(20);
            fromPanel.add(fromLabel);
            fromPanel.add(fromField);

            JPanel toPanel = new JPanel(new FlowLayout());
            JLabel toLabel = new JLabel("To:");
            toField = new JTextField(20);
            toPanel.add(toLabel);
            toPanel.add(toField);

            JPanel messagePanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("Message:");
            messageArea = new JTextArea(15, 40);
            JScrollPane scrollPane = new JScrollPane(messageArea);
            messagePanel.add(messageLabel, BorderLayout.NORTH);
            messagePanel.add(scrollPane, BorderLayout.CENTER);

            topPanel.add(fromPanel, BorderLayout.NORTH);
            topPanel.add(toPanel, BorderLayout.CENTER);

            mainPanel.add(topPanel, BorderLayout.NORTH);
            mainPanel.add(messagePanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JPanel passwordPanel = new JPanel(new FlowLayout());
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField(20);
            passwordPanel.add(passwordLabel);
            passwordPanel.add(passwordField);

            topPanel.add(passwordPanel, BorderLayout.SOUTH);

            JButton returnButton = new JButton("Return");
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ExistingPlayerWindow(enteredUsername);
                    dispose();
                }
            });

            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String from = fromField.getText();
                    // Assuming you have a JPasswordField for the password input
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String

                    String to = toField.getText();
                    String subject = "Your Subject Here"; // Provide a subject for the email
                    String message = messageArea.getText();

                    // Call the sendEmail method with the necessary parameters
                    Email.sendEmail(from, password, to, subject, message);

                    new ExistingPlayerWindow(enteredUsername);
                    dispose();
                }
            });

            buttonPanel.add(returnButton);
            buttonPanel.add(sendButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            JPanel fileListPanel = new JPanel(new BorderLayout());

            Map<String, String> userData = CSVHandler.readNameAndEmailFromUserFiles();
            userList = new JComboBox<>(userData.keySet().toArray(new String[0]));

            userList.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedUser = (String) userList.getSelectedItem();
                    String userInfo = userData.get(selectedUser);
                    toField.setText(userInfo);
                }
            });

            JScrollPane scrollPaneUsers = new JScrollPane(userList);
            scrollPaneUsers.setPreferredSize(new Dimension(150, 300));
            fileListPanel.add(scrollPaneUsers, BorderLayout.CENTER);

            mainPanel.add(fileListPanel, BorderLayout.EAST);

            add(mainPanel);
            setVisible(true);
        }
    }
}

