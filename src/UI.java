import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UI extends JFrame {

    private JLabel lblFile1, lblFile2;
    private File file1, file2;

    public UI() {
        setTitle("Darksiders II Save File Fixer");
        setSize(300, 290);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setResizable(false);
        getContentPane().setBackground(new Color(31, 31, 31));

        lblFile1 = createLabel("Your save");
        lblFile2 = createLabel("Other save");

        JTextArea statusTextArea = new JTextArea();
        statusTextArea.setText("Load the save file you've created as File 1 and load the save file you've downloaded as File 2. Note: The save files exist in two folders on your PC, so you have to fix both instances.");
        statusTextArea.setEditable(false);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setLineWrap(true);
        statusTextArea.setFont(new Font("Arial", Font.ITALIC, 12));
        statusTextArea.setPreferredSize(new Dimension(450, 40));
        statusTextArea.setBackground(new Color(21, 21, 21));
        statusTextArea.setForeground(new Color(207, 207, 207));
        statusTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusTextArea.setAlignmentX(CENTER_ALIGNMENT);
        statusTextArea.setAlignmentY(CENTER_ALIGNMENT);
        statusTextArea.setCaretPosition(0);
        statusTextArea.setCaretColor(new Color(207, 207, 207));

        JButton btnImportFile1 = createImportButton("Save File 1", lblFile1, 1);
        JButton btnImportFile2 = createImportButton("Save File 2", lblFile2, 2);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(31, 31, 31));

        panel.add(Box.createVerticalStrut(20));

        panel.add(createCenteredPanel(lblFile1));
        panel.add(createButtonPanel(btnImportFile1));

        panel.add(createCenteredPanel(lblFile2));
        panel.add(createButtonPanel(btnImportFile2));

        panel.add(Box.createVerticalStrut(20));

        panel.add(statusTextArea);

        add(panel, BorderLayout.CENTER);
    }

    private JPanel createCenteredPanel(JLabel label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(true);
        panel.setBackground(new Color(31, 31, 31));
        panel.add(label);
        return panel;
    }

    private JPanel createButtonPanel(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(true);
        panel.setBackground(new Color(31, 31, 31));
        button.setPreferredSize(new Dimension(200, 30));
        panel.add(button);
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(207, 207, 207));
        return label;
    }

    private JButton createImportButton(String text, JLabel lblFile, int fileNumber) {
        JButton button = new JButton(text);
        button.setBackground(new Color(116, 66, 150));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 30));
        button.setBorder(BorderFactory.createLineBorder(new Color(116, 66, 150), 2, true));
        button.addActionListener(e -> fileChooser(fileNumber));
        return button;
    }

    private void fileChooser(int fileNumber) {
        JFileChooser fileChooser = new JFileChooser();
        String defaultPath = System.getProperty("user.home") + "/Documents/My Games/Darksiders2";
        fileChooser.setCurrentDirectory(new File(defaultPath));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (fileNumber == 1) {
                file1 = selectedFile;  // Store the file path for File 1
                lblFile1.setText(selectedFile.getName() + " loaded");  // Update the label with the file name
            } else if (fileNumber == 2) {
                file2 = selectedFile;  // Store the file path for File 2
                lblFile2.setText(selectedFile.getName() + " loaded");  // Update the label with the file name
            }

            // Check if both files are loaded
            if (file1 != null && file2 != null) {
                System.out.println("Both files loaded. Now showing save dialog.");
                showSaveFileDialog();
            }
        }
    }

    private void showSaveFileDialog() {
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setDialogTitle("Select where to save the modified file");

        String defaultPath = System.getProperty("user.home") + "/Documents/My Games/Darksiders2";
        saveFileChooser.setCurrentDirectory(new File(defaultPath));

        int returnValue = saveFileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File outputFile = saveFileChooser.getSelectedFile();

            if (!outputFile.getName().endsWith(".dsav")) {
                outputFile = new File(outputFile.getParent(), outputFile.getName() + ".dsav");
            }

            System.out.println("Selected output file: " + outputFile.getAbsolutePath());

            if (!file1.exists()) {
                JOptionPane.showMessageDialog(this, "File 1 does not exist: " + file1.getAbsolutePath());
                return;
            }
            if (!file2.exists()) {
                JOptionPane.showMessageDialog(this, "File 2 does not exist: " + file2.getAbsolutePath());
                return;
            }

            FileProcessing fileProcessing = new FileProcessing();
            try {
                fileProcessing.processFiles(file1, file2, outputFile);
                JOptionPane.showMessageDialog(this, "File processed and saved successfully!");
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error processing files: " + ex.getMessage());
            }
        }
    }
}
