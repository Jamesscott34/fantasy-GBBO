import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdminWindow extends JFrame {


    public AdminWindow() {
        setTitle("Admin Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Implement admin window components
        JComboBox<String> NewplayerFoldersDropDown = new JComboBox<>();
        JButton newPlayerFolderButton = new JButton("Players");
        JButton updateButton = new JButton("Update");
        JButton resetButton = new JButton("Reset");
        JButton EmailButton = new JButton("Email");
        JButton RemoveButton = new JButton("Remove");

        // Add components to the layout
        JPanel panel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        bottomPanel.add(NewplayerFoldersDropDown);
        bottomPanel.add(newPlayerFolderButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(EmailButton);
        bottomPanel.add(RemoveButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

        setVisible(true);

        newPlayerFolderButton.addActionListener(e -> {
            List<String[]> playerData = CSVHandler.readPlayer();

            // Display the player data in a dialog
            StringBuilder playersInfo = new StringBuilder("Players:\n");
            for (String[] player : playerData) {
                playersInfo.append(Arrays.toString(player)).append("\n");
            }
            JOptionPane.showMessageDialog(null, playersInfo.toString(), "Players List", JOptionPane.INFORMATION_MESSAGE);
        });

        updateButton.addActionListener(e -> new UpdateWindow());

        resetButton.addActionListener(e -> {
            CSVHandler.resetPlayerFiles();
            dispose();
        });

        EmailButton.addActionListener(e -> {
            new EmailWindow();
            dispose();
        });

        RemoveButton.addActionListener(e -> {
            new RemoveWindow();
            dispose();
        });

        setButtonKeyBindings();
    }

    private void setButtonKeyBindings() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", evt -> {
            if (evt.getNewValue() instanceof JButton) {
                JButton focusedButton = (JButton) evt.getNewValue();
                focusedButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "clickButton");
                focusedButton.getActionMap().put("clickButton", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        focusedButton.doClick();
                    }
                });
            }
        });
    }


    public static class UpdateWindow extends JFrame {
        public UpdateWindow() {
            setTitle("Update Contestant Scores");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(0, 2));

            List<String> contestantNames = CSVHandler.readContestantNames(); // Read contestant names from contestants.csv

            for (String contestant : contestantNames) {
                panel.add(new JLabel(contestant));
                JTextField scoreField = new JTextField();
                panel.add(scoreField);
            }

            JButton updateButton = new JButton("Update Scores");
            JButton cancelButton = new JButton("Cancel");



            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to save the updated scores?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        List<String[]> updatedContestantScores = new ArrayList<>();
                        Component[] components = panel.getComponents();

                        for (int i = 0; i < components.length; i += 2) {
                            if (components[i] instanceof JLabel && components[i + 1] instanceof JTextField) {
                                JLabel nameLabel = (JLabel) components[i];
                                JTextField scoreField = (JTextField) components[i + 1];
                                String contestantName = nameLabel.getText();
                                String newScore = scoreField.getText();
                                // You might want to perform validation on the newScore here

                                // Add the updated contestant name and score to the list
                                updatedContestantScores.add(new String[]{contestantName, newScore});
                            }
                        }

                        // Update contestant scores in the main CSV file
                        CSVHandler.updateContestantScores(updatedContestantScores);
                        CSVHandler.updatePlayerScores(updatedContestantScores);
                        new AdminWindow();
                        dispose(); // Close the window after updating
                    }
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdminWindow();
                    dispose();
                }
            });

            panel.add(updateButton);
            panel.add(cancelButton);



            add(panel);
            setVisible(true);
        }

    }

    public static class RemoveWindow extends JFrame {

        public RemoveWindow() {
            setTitle("Remove Contestant");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(0, 2));

            List<String> contestantNames = CSVHandler.readContestantNames();

            // Create a list to hold checkboxes for each contestant
            List<JCheckBox> checkBoxes = new ArrayList<>();

            for (String contestant : contestantNames) {
                JCheckBox checkBox = new JCheckBox(contestant);
                checkBoxes.add(checkBox);
                panel.add(checkBox);
            }

            JButton cancelButton = new JButton("Cancel");
            JButton removeButton = new JButton("Remove");

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Iterate through checkboxes to find selected contestants
                    for (JCheckBox checkBox : checkBoxes) {
                        if (checkBox.isSelected()) {
                            String contestantToRemove = checkBox.getText();

                            // Remove contestant from contestants.csv
                            CSVHandler.removeContestantFromContestantsCSV(contestantToRemove);

                            // Update individual user CSV files by removing the contestant
                            CSVHandler.removeContestantFromUsers(contestantToRemove);
                        }
                    }
                    new AdminWindow();
                    dispose();
                }
            });


            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdminWindow();
                    dispose(); // Close the window without updating
                }
            });

            panel.add(cancelButton);
            panel.add(removeButton);

            add(panel);
            setVisible(true);
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
                    new AdminWindow();
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

                    new AdminWindow();
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




