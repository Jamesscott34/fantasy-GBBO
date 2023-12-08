import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NewPlayerWindow extends JFrame {
    private final DefaultListModel<String> selectedContestantsModel;
    private final JList<String> selectedContestantsList;

    public NewPlayerWindow(String enteredUsername) {
        setTitle("New Player Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        CSVHandler csvHandler = new CSVHandler();
        List<String> contestantNames = CSVHandler.readContestantNames();

        JList<String> contestantsList = new JList<>(contestantNames.toArray(new String[0]));
        contestantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton removeButton = new JButton("Remove");
        JButton saveButton = new JButton("Save Selection");
        selectedContestantsModel = new DefaultListModel<>();
        selectedContestantsList = new JList<>(selectedContestantsModel);

        JScrollPane scrollPane = new JScrollPane(contestantsList);
        scrollPane.setPreferredSize(new Dimension(300, 300));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.WEST);
        panel.add(selectedContestantsList, BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);

        contestantsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
    }
}
