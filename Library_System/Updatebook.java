import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

        // Main panel with background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("bgpictttt.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel headerLabel = new JLabel("Update Book Details");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(400, 120));

        // Input fields
        bookNumberField = new JTextField();
        bookTitleField = new JTextField();
        bookAuthorField = new JTextField();

        // Labels with consistent font
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        JLabel numberLabel = new JLabel("Book ID:");
        JLabel titleLabel = new JLabel("Book Title:");
        JLabel authorLabel = new JLabel("Book Author:");
        numberLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);
        authorLabel.setFont(labelFont);

        inputPanel.add(numberLabel);
        inputPanel.add(bookNumberField);
        inputPanel.add(titleLabel);
        inputPanel.add(bookTitleField);
        inputPanel.add(authorLabel);
        inputPanel.add(bookAuthorField);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

        // Update Button
        JButton updateButton = createStyledButton("Update", 200, 40);
        updateButton.addActionListener(e -> {
            String bookNumber = bookNumberField.getText().trim();
            String bookTitle = bookTitleField.getText().trim();
            String bookAuthor = bookAuthorField.getText().trim();

            if (bookNumber.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!");
                return;
            }

            if (updateBookDetails(bookNumber, bookTitle, bookAuthor)) {
                JOptionPane.showMessageDialog(frame, "Book updated successfully!");
                updateBookTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating book details.");
            }
        });
        panel.add(updateButton);
        panel.add(Box.createVerticalStrut(20));

        // Existing Books Label
        JLabel existingBooksLabel = new JLabel("Existing Books:");
        existingBooksLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        existingBooksLabel.setForeground(new Color(0x3B3030));
        existingBooksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(existingBooksLabel);
        panel.add(Box.createVerticalStrut(10));

        // Book Table
        bookTable = new JTable();
        setupBookTable();
        
        // Add table selection listener
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    bookNumberField.setText(bookTable.getValueAt(selectedRow, 0).toString());
                    bookTitleField.setText(bookTable.getValueAt(selectedRow, 1).toString());
                    bookAuthorField.setText(bookTable.getValueAt(selectedRow, 2).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(580, 200));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(20));

        // Back Button
        JButton backButton = createStyledButton("Back", 100, 40);
        backButton.addActionListener(e -> {
            frame.dispose();
            adminFrame.setVisible(true);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(0x603F26));
        button.setForeground(Color.WHITE);
        button.setMaximumSize(new Dimension(width, height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        return button;
    }

    private void clearFields() {
        bookNumberField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
    }

    private boolean updateBookDetails(String bookNumber, String bookTitle, String bookAuthor) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookNumber)) {
                    // Preserve the book's availability status
                    lines.add(bookNumber + "," + bookTitle + "," + bookAuthor + "," + parts[3]);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!found) {
            JOptionPane.showMessageDialog(frame, "Book ID not found!");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setupBookTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Book ID", "Title", "Author", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(model);
        
        // Style the table
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        bookTable.getTableHeader().setBackground(new Color(0x603F26));
        bookTable.getTableHeader().setForeground(Color.WHITE);

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        updateBookTable();
    }

    private void updateBookTable() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    model.addRow(new Object[]{
                        parts[0],  // ISBN
                        parts[1],  // Title
                        parts[2],  // Author
                        parts[3]   // Status
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
