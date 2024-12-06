import javax.swing.*;
import java.awt.*;
import java.io.*;

public class DisplayBooks {
    private JFrame frame;

    public DisplayBooks(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("All Books");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false); // make the text area non-editable
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setMaximumSize(new Dimension(100, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            frame.dispose();
            parentFrame.setVisible(true); // Show the parent frame when back is clicked
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadBookDetails(textArea);

        frame.setSize(600, 500);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadBookDetails(JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String bookInfo = String.format("Book Number: %s | Title: %s | Author: %s\n", details[0], details[1], details[2]);
                textArea.append(bookInfo);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage());
        }
    }
}
