/**
 *
 * @authur James Scott
 * a rough programme for a fantasy GBBO
 * this will take new players info
 * give admin access and new and existing player
 *
 */


import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(login::new);

        SwingUtilities.invokeLater(() -> {

           // Debug.debugLogin();
           // Debug.debugExistingWindow();
            // Debug.debugNewPlayerWindow();
            // Add other debug methods as needed
        });

    }
}