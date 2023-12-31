import java.io.*;
import java.util.*;


public class CSVHandler {
    private static List<String[]> contestantData;
    public static void resetPlayerFiles() {
        File folder = new File("./Files/");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".csv") && !file.getName().equals("Players.csv") && !file.getName().equals("contestants.csv")) {
                    List<String> updatedContent = new ArrayList<>();

                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        String csvSplitBy = ",";
                        while ((line = br.readLine()) != null) {
                            String[] rowData = line.split(csvSplitBy);
                            if (!rowData[0].equalsIgnoreCase("Name") && !rowData[0].equalsIgnoreCase("Username") && !rowData[0].equalsIgnoreCase("Email")) {
                                // Clear player picks (contestants) by not adding them to the updated content
                            } else {
                                updatedContent.add(line);
                            }
                        }
                    } catch (IOException e) {
                        ExceptionHandler.handleIOException((IOException) e);
                    }


                    // Rewrite the file with updated content (without player picks)
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                        for (String contentLine : updatedContent) {
                            bw.write(contentLine);
                            bw.newLine();
                        }
                    } catch (IOException e) {
                        ExceptionHandler.handleIOException((IOException) e);
                    }

                    // Delete the file after clearing its content
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        }
    }


    public static void removeContestantFromUsers(String contestantToRemove) {
        List<String[]> contestantData = readContestantData("Contestants name"); // Read contestants from CSV

        File folder = new File("./Files/");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".csv") && !file.getName().equals("Players.csv")) {
                    if (!file.getName().equals("contestants.csv")) {
                        List<String> updatedContent = new ArrayList<>();

                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                            String csvSplitBy = ",";
                            while ((line = br.readLine()) != null) {
                                String[] rowData = line.split(csvSplitBy);
                                boolean isPlayerPickInContestants = false;


                                // Check if the player pick exists in the contestants list
                                for (String[] contestant : contestantData) {
                                    if (rowData.length > 0 && rowData[0].equals(contestant[0])) {
                                        updatedContent.add(line); // Add contestant if found in contestants.csv
                                        isPlayerPickInContestants = true;
                                        break;
                                    }
                                }

                                // Remove player pick if not found in contestants.csv
                                if (!isPlayerPickInContestants) {
                                    continue; // Skip adding this line to updatedContent
                                }
                            }
                        } catch (IOException e) {
                            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
                            ExceptionHandler.handleIOException((IOException) e);
                        }

                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                            for (String contentLine : updatedContent) {
                                bw.write(contentLine);
                                bw.newLine();
                            }
                        } catch (IOException e) {
                            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
                            ExceptionHandler.handleIOException((IOException) e);
                        }
                    }
                }
            }
        }
    }





    public static List<String> getSelectedContestantsFromCSV() {
        List<String> selectedContestants = new ArrayList<>();
        String csvFile = "./Files/selected_contestants.csv"; // Replace with your CSV file path/name

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming the file contains one selected contestant per line
                selectedContestants.add(line);
            }
        } catch (IOException e) {
            ExceptionHandler.handleIOException((IOException) e);
        }

        return selectedContestants;
    }


    public void writePlayer(String name, String email, String newUsername) {
        String filePath = "./Files/Players.csv";
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.append(name).append(",").append(email).append(",").append(newUsername).append("\n");
            writer.close();
        } catch (IOException e) {
            ExceptionHandler.handleIOException((IOException) e);
        }
    }

    public static void removeContestantFromContestantsCSV(String contestantName) {
        String csvFile = "./Files/contestants.csv";
        String line;
        String csvSplitBy = ",";
        List<String> updatedContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(csvSplitBy);
                if (!rowData[0].equals(contestantName)) {
                    updatedContent.add(line);
                }
            }
        } catch (IOException e) {
            ExceptionHandler.handleIOException((IOException) e);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String contentLine : updatedContent) {
                bw.write(contentLine);
                bw.newLine();
            }
        } catch (IOException e) {
            ExceptionHandler.handleIOException((IOException) e);
        }
    }


    public static List<String[]> readPlayer() {
        List<String[]> playerData = new ArrayList<>();
        String fileName = "./Files/Players.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                playerData.add(values);
            }
        } catch (IOException e) {
            ExceptionHandler.handleIOException((IOException) e);
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
        }
        return playerData;
    }


    public static void createPlayerCSV(String username, String[] selectedContestants) {
        String playerFileName = "./Files/" + username + ".csv";
        List<String[]> playerData = readPlayer(); // Assuming readPlayer() reads Players.csv
        List<String[]> contestantData = readContestantData("Contestants name"); // Use readContestantData to get contestant info

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerFileName))) {
            for (String[] player : playerData) {
                if (player[2].equals(username)) { // Check if the username matches in Players.csv
                    writer.write("Name: " + player[0] + "\n");
                    writer.write("Username: " + player[2] + "\n");
                    writer.write("Email: " + player[1] + "\n");
                    writer.write("Player Picks:\n");

                    // Write selected contestants into the CSV file
                    for (String selectedContestant : selectedContestants) {
                        for (String[] contestant : contestantData) {
                            if (selectedContestant.equals(contestant[0])) {
                                String fullLine = String.join(",", contestant);
                                writer.write(fullLine + "\n");
                                break;
                            }
                        }
                    }

                    int totalScore = 0;
                    for (String selectedContestant : selectedContestants) {
                        for (String[] contestant : contestantData) {
                            if (selectedContestant.equals(contestant[0])) {
                                totalScore += Integer.parseInt(contestant[4]); // Assuming score is in index 4
                                break;
                            }
                        }
                    }
                    writer.write("Total Score: " + totalScore + "\n");

                    break; // Exit loop after finding the player's details
                }
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
            ExceptionHandler.handleIOException((IOException) e);
        }
    }
    public static List<String[]> readPlayerData(String username) {
        List<String[]> playerData = new ArrayList<>();

        String playerFile = "./Files/" + username + ".csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(playerFile))) {
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(csvSplitBy);
                playerData.add(rowData);
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
        }

        return playerData;
    }

    public static List<String> readContestantNames() {
        List<String> contestantNames = new ArrayList<>();
        String csvFile = "./Files/contestants.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(csvSplitBy);
                if (rowData.length > 0) {
                    contestantNames.add(rowData[0]); // Assuming the contestant name is in the first column (index 0)
                }
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
            ExceptionHandler.handleIOException((IOException) e);
        }

        return contestantNames;
    }

    public static List<String[]> readContestantData(String contestantsName) {
        List<String[]> contestantData = new ArrayList<>();

        String csvFile = "./Files/contestants.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(csvSplitBy);
                contestantData.add(rowData);
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
            ExceptionHandler.handleIOException((IOException) e);
        }

        return contestantData;
    }

    public static void updatePlayerScores(List<String[]> updatedScores) {
        for (String[] updatedScore : updatedScores) {
            String contestantName = updatedScore[0];
            String score = updatedScore[1];
            String userFileName = "./Files/" + contestantName + ".csv";

            File userFile = new File(userFileName);
            if (userFile.exists()) {
                try {
                    List<String> lines = new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new FileReader(userFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length > 1 && data[0].equals(contestantName)) {
                            // Assuming the score is in index 4, update the score
                            data[4] = score;
                        }
                        lines.add(String.join(",", data));
                    }
                    reader.close();

                    // Rewrite the updated data to the user's file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
                    for (String updatedLine : lines) {
                        writer.write(updatedLine + "\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
                    ExceptionHandler.handleIOException((IOException) e);
                }
            }
        }
    }


    public static void updateContestantScores(List<String[]> updatedScores) {
        String csvFile = "./Files/contestants.csv";
        String line;
        String csvSplitBy = ",";

        List<String> updatedFileContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(csvSplitBy);
                String contestantName = rowData[0]; // Assuming contestant name is in index 0

                for (String[] updatedScore : updatedScores) {
                    if (updatedScore[0].equals(contestantName)) {
                        rowData[4] = updatedScore[1]; // Update the score assuming it's in index 4 of updatedScore
                        break;
                    }
                }

                StringBuilder updatedLine = new StringBuilder();
                for (int i = 0; i < rowData.length - 1; i++) {
                    updatedLine.append(rowData[i]).append(",");
                }
                updatedLine.append(rowData[rowData.length - 1]);

                updatedFileContent.add(updatedLine.toString());
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
            ExceptionHandler.handleIOException((IOException) e);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String updatedLine : updatedFileContent) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
            ExceptionHandler.handleIOException((IOException) e);
        }
    }

    public static Map<String, String> readNameAndEmailFromUserFiles() {
        Map<String, String> userNamesAndEmails = new HashMap<>();
        File folder = new File("./Files");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".csv") && !file.getName().equals("Players.csv")) {
                    String username = file.getName().replace(".csv", "");
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.startsWith("Name:") || line.startsWith("Username:") || line.startsWith("Email:")) {
                                String[] rowData = line.split(": ");
                                if (rowData.length == 2) {
                                    String field = rowData[0];
                                    String value = rowData[1];
                                    if (field.equals("Name")) {
                                        userNamesAndEmails.put(username, value);
                                    } else if (field.equals("Email")) {
                                        String existingName = userNamesAndEmails.getOrDefault(username, "");
                                        userNamesAndEmails.put(username, existingName + "," + value);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        ExceptionHandler.handleFileNotFoundException((FileNotFoundException) e);
                        ExceptionHandler.handleIOException((IOException) e);
                    }
                }
            }
        }
        return userNamesAndEmails;
    }
}


