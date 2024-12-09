import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Updatebook {
    private JFrame frame;
    private JTextField bookNumberField, bookTitleField, bookAuthorField;
    private JTable bookTable;

    public Updatebook(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Update Book");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0xFFF0D1)); // Set background color

        JLabel headerLabel = new JLabel("Update Book Details");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

        // Book ID input
        JLabel bookNumberLabel = new JLabel("Enter Book ID to Update");
        bookNumberLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        bookNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Bold font
        panel.add(bookNumberLabel);

        bookNumberField = new JTextField();
        bookNumberField.setMaximumSize(new Dimension(300, 30)); // Set max size for text field
        panel.add(bookNumberField);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        searchButton.setBackground(new Color(0x603F26));
        searchButton.setForeground(Color.WHITE);
        searchButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(searchButton);

        // Book Title and Author fields
        JLabel bookTitleLabel = new JLabel("Book Title");
        bookTitleLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        bookTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Bold font
        panel.add(bookTitleLabel);
        bookTitleField = new JTextField();
        bookTitleField.setEditable(false);
        bookTitleField.setMaximumSize(new Dimension(300, 30)); // Set max size for text field
        panel.add(bookTitleField);

        JLabel bookAuthorLabel = new JLabel("Book Author");
        bookAuthorLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        bookAuthorLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Bold font
        panel.add(bookAuthorLabel);
        bookAuthorField = new JTextField();
        bookAuthorField.setEditable(false);
        bookAuthorField.setMaximumSize(new Dimension(300, 30)); // Set max size for text field
        panel.add(bookAuthorField);

        JButton updateButton = new JButton("Update");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        updateButton.setBackground(new Color(0x603F26));
        updateButton.setForeground(Color.WHITE);
        updateButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        updateButton.setEnabled(false);
        panel.add(updateButton);

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
            adminFrame.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            String bookNumber = bookNumberField.getText();
            if (bookNumber.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a book number.");
                return;
            }

            String[] bookDetails = findBookDetails(bookNumber);
            if (bookDetails == null) {
                JOptionPane.showMessageDialog(frame, "Book not found!");
            } else {
                bookTitleField.setText(bookDetails[1]);
                bookAuthorField.setText(bookDetails[2]);
                bookTitleField.setEditable(true);
                bookAuthorField.setEditable(true);
                updateButton.setEnabled(true);
            }
        });

        updateButton.addActionListener(e -> {
            String bookNumber = bookNumberField.getText();
            String bookTitle = bookTitleField.getText();
            String bookAuthor = bookAuthorField.getText();

            if (bookTitle.isEmpty() || bookAuthor.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!");
                return;
            }

            if (updateBookDetails(bookNumber, bookTitle, bookAuthor)) {
                JOptionPane.showMessageDialog(frame, "Book updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating book details.");
            }

            frame.dispose();
            adminFrame.setVisible(true);
        });

        // Create a table to display all books
        String[] columnNames = {"Book ID", "Book Title", "Book Authors", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookTable.setBackground(new Color(0xFFF0D1)); // Match background color
        bookTable.setRowHeight(30); // Set row height for better visibility

        // Center align the content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookTable.setDefaultRenderer(Object.class, centerRenderer);

        // Highlight header
        bookTable.getTableHeader().setBackground(new Color(0x603F26));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(500, 200)); // Set preferred size for the table
        panel.add(scrollPane);

        // Load all books into the table
        loadAllBooks(tableModel);

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(backButton);
        horizontalBox.add(Box.createRigidArea(new Dimension(20, 0)));

        panel.add(horizontalBox);

        frame.add(panel);
        frame.setSize(600, 600); // Increased height for better visibility
        frame.setResizable(false);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String[] findBookDetails(String bookNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",", 4); // Adjusted to split into 4 parts
                if (details[0].equals(bookNumber)) {
                    return details;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage());
        }
        return null;
    }

    private boolean updateBookDetails(String bookNumber, String bookTitle, String bookAuthor) {
        File file = new File("books.txt");
        File tempFile = new File("books_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",", 4); // Adjusted to split into 4 parts
                if (details[0].equals(bookNumber)) {
                    writer.write(bookNumber + "," + bookTitle + "," + bookAuthor + "," + details[3]); // Include status
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
            writer.flush(); 

            if (updated) {
                try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
                     BufferedWriter originalWriter = new BufferedWriter(new FileWriter(file))) {
                     
                    while ((line = tempReader.readLine()) != null) {
                        originalWriter.write(line);
                        originalWriter.newLine();
                    }
                }
            }

            return updated;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error updating file: " + e.getMessage());
            return false;
        }
    }

    private void loadAllBooks(DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",", 4); // Adjusted to split into 4 parts
                if (details.length >= 4) { // Ensure there are enough details
                    tableModel.addRow(new Object[]{details[0], details[1], details[2], details[3]}); // Include status
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage());
        }
    }
}
