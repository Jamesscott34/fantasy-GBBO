import java.util.Arrays;
import java.util.List;

public class Debug {

    // Debugging Login class
    public static void debugLogin() {
        System.out.println("Debugging Login class...");

        // Example: Debugging validatePlayerCredentials method
        List<String[]> playerData = CSVHandler.readPlayer();
        System.out.println("Player Data:");
        for (String[] player : playerData) {
            System.out.println("Username: " + player[0]); // Print each username in playerData
        }

        String adminUsername = "admin"; // Admin username
        String adminPassword = "admin"; // Admin password
        System.out.println("Admin Credentials:");
        System.out.println("Username: " + adminUsername + ", Password: " + adminPassword);

        // Add more specific debug instructions here based on what you want to investigate
    }

    // Debugging ExistingWindow class
    public static void debugExistingWindow() {
        System.out.println("Debugging ExistingPlayerWindow class...");

        // Example: Logging entered username
        System.out.println("Entered Username: " + ExistingPlayerWindow.enteredUsername);

        // Example: Logging player data read from CSV
        List<String[]> playerData = CSVHandler.readPlayerData(ExistingPlayerWindow.enteredUsername);
        System.out.println("Player Data:");
        for (String[] player : playerData) {
            System.out.println(Arrays.toString(player)); // Print each player's data
        }

        // Add more specific debug instructions or logging as needed
    }

    // Debugging NewPlayerWindow class
    public static void debugNewPlayerWindow() {
        System.out.println("Debugging NewPlayerWindow class...");

        // Logging the creation of NewPlayerWindow instance
        System.out.println("Creating NewPlayerWindow instance...");

        // Add specific debug instructions here
        // For instance, logging the initialization of the NewPlayerWindow UI components
        System.out.println("Initializing NewPlayerWindow UI components...");

        // Example: Logging the contestant names retrieved from CSVHandler
        List<String> contestantNames = CSVHandler.readContestantNames();
        System.out.println("Contestant Names: " + contestantNames);

        // Example: Logging the action listeners attached to buttons in NewPlayerWindow
        System.out.println("Adding action listeners to buttons in NewPlayerWindow...");

        // Add more specific debugging instructions or logging based on your debugging needs
    }
}

