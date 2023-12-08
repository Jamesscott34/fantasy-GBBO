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
                // Logic for updating scores
                // Implement logic here
            }
        });

        contestantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for displaying contestants and scores from CSV file
                // Implement logic here using CSVHandler.java
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




    // Add methods to handle the logic for the action listeners
}
