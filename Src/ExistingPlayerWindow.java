import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class ExistingPlayerWindow extends JFrame {



    public ExistingPlayerWindow(String enteredUsername) {
        setTitle("Existing Player Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JButton updateButton = new JButton("Update");
        JButton contestantsButton = new JButton("Contestants");
        JButton emailButton = new JButton("Email");

        CSVHandler csvHandler = new CSVHandler();
        List<String[]> playerData = CSVHandler.readPlayerData("username"); // Replace "username" with the actual username
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String[] player : playerData) {
            listModel.addElement(Arrays.toString(player));
        }

        JList<String> contestantsList = new JList<>(listModel);





        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(contestantsList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(updateButton);
        bottomPanel.add(contestantsButton);
        bottomPanel.add(emailButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);

        // Action listeners for the buttons
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected contestants for the user
                List<String> selectedContestants = null /* Logic to retrieve selected contestants */;

                // Read the contestant data from contestants.csv
                List<String[]> contestantData = CSVHandler.readContestantData("Contestants name");

                // Iterate through the contestant data to update scores or handle removed contestants
                for (String[] contestant : contestantData) {
                    String contestantName = contestant[0];
                    boolean isUserContestant = selectedContestants.contains(contestantName);

                    if (isUserContestant) {
                        // Update the score for the contestant (logic to update score)
                        // Assuming the score is at index 3
                        int currentScore = Integer.parseInt(contestant[3]);
                        // Implement your score update logic here
                        // Example: Increment the score by 10
                        int updatedScore = currentScore + 10;
                        contestant[3] = String.valueOf(updatedScore);
                    } else {
                        // Handle removed contestants (logic to reset score or remove contestant)
                        // Remove contestant or reset score to zero
                        contestant[3] = "0"; // Reset score to zero
                    }
                }

                // Update contestant scores in the CSV file
                CSVHandler.updateContestantScores(contestantData);
                dispose(); // Close the window after updating
            }
        });

        contestantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String[]> playerData = CSVHandler.readPlayerData("username"); // Replace "username" with the actual username

                StringBuilder playersInfo = new StringBuilder("Players Information:\n");
                for (String[] player : playerData) {
                    playersInfo.append("Player: ").append(player[0]).append("\n"); // Assuming the player's name is in index 0
                    playersInfo.append("Contestants: ").append(player[1]).append("\n"); // Assuming contestant names are in index 1
                    playersInfo.append("Score: ").append(player[2]).append("\n\n"); // Assuming the score is in index 2
                }

                JOptionPane.showMessageDialog(null, playersInfo.toString(), "Player Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for cycling through username CSV files and putting them in a dropdown
                // Then implementing logic to send emails
                // Implement logic here
            }
        });
    }
}
