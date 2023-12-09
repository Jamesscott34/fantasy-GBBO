import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public login() {
        initializeLogin();
    }

    private void initializeLogin() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);
        panel.add(signupButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword()); // Get entered password as a String

                try {
                    if (validatePlayerCredentials(enteredUsername)) {
                        new NewPlayerWindow(enteredUsername);
                        dispose();
                    } else if (validateAdminCredentials(enteredUsername, enteredPassword)) {
                        new AdminWindow();
                        dispose();
                    } else {
                        new NewPlayerWindow(enteredUsername);
                        dispose();
                    }
                } catch (Exception ex) {
                    ExceptionHandler.handleGeneralException((ex));
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupWindow();
                dispose();
            }
        });

        setVisible(true);
    }

    private boolean validatePlayerCredentials(String enteredUsername) {

        List<String[]> playerData = CSVHandler.readPlayer();

        for (String[] player : playerData) {
            if (enteredUsername.equals(player[0])) {
                return true; // Return true if the entered username matches a player's username
            }
        }

        return false; // Return false if the username is not found among players
    }


    private boolean validateAdminCredentials(String enteredUsername, String enteredPassword) {
        String adminUsername = "admin"; // Admin username
        String adminPassword = "admin"; // Admin password

        return enteredUsername.equals(adminUsername) && enteredPassword.equals(adminPassword);
    }

    private void openSignupWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame signupFrame = new JFrame("Sign Up");
            signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            signupFrame.setSize(300, 150);
            signupFrame.setLocationRelativeTo(null);

            JPanel signupPanel = new JPanel(new GridLayout(4, 2));

            JLabel nameLabel = new JLabel("Name:");
            JTextField nameField = new JTextField();
            JLabel emailLabel = new JLabel("Email:");
            JTextField emailField = new JTextField();
            JLabel usernameLabel = new JLabel("Username:");
            JTextField newUsernameField = new JTextField();
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            signupPanel.add(nameLabel);
            signupPanel.add(nameField);
            signupPanel.add(emailLabel);
            signupPanel.add(emailField);
            signupPanel.add(usernameLabel);
            signupPanel.add(newUsernameField);
            signupPanel.add(saveButton);
            signupPanel.add(cancelButton);

            signupFrame.add(signupPanel);

            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String newUsername = newUsernameField.getText();

                    CSVHandler csvHandler = new CSVHandler();
                    csvHandler.writePlayer(name, email, newUsername);

                    signupFrame.dispose();
                    login loginWindow = new login();
                    loginWindow.setVisible(true);
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    signupFrame.dispose();
                    login loginWindow = new login();
                    loginWindow.setVisible(true);
                }
            });

            signupFrame.setVisible(true);
        });
    }

}
