import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReturnBooks extends JFrame {
    private JFrame parentFrame;
    private String studentId;
    private static final String BORROWING_FILE = "borrowing_records.txt";

    public ReturnBooks(JFrame parent, String studentId) {
        this.parentFrame = parent;
        this.studentId = studentId;
        setTitle("ULMS - Return Books");

        // Set up background
        ImageIcon background = new ImageIcon("bgpictttt.png");
        Image backgroundImage = background.getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setPreferredSize(new Dimension(800, 650));

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Ensure "logo.png" is the correct path
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Header
        JLabel titleLabel = new JLabel("RETURN BOOKS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        titleLabel.setForeground(new Color(0x3B3030));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titlePanel.add(logoLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Borrowed books table
        String[] columnNames = {"ISBN", "Title", "Author"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        loadBorrowedBooks(tableModel);

        JTable borrowedBooksTable = new JTable(tableModel);
        borrowedBooksTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        borrowedBooksTable.setRowHeight(25);
        borrowedBooksTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
        borrowedBooksTable.getTableHeader().setBackground(new Color(0x4B2E2E));
        borrowedBooksTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.setPreferredSize(new Dimension(750, 250));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        // Return button
        JButton returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        returnButton.setBackground(new Color(0x603F26));
        returnButton.setForeground(Color.WHITE);
        returnButton.setAlignmentX(CENTER_ALIGNMENT);
        returnButton.setToolTipText("Click to return the selected book");
        returnButton.addActionListener(e -> returnSelectedBook(borrowedBooksTable, tableModel));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(returnButton);
        bottomPanel.add(backButton);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Frame settings
        add(backgroundPanel);
        setSize(800, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadBorrowedBooks(DefaultTableModel tableModel) {
        List<Book> allBooks = Createbook.getAllBooks();
        for (Book book : allBooks) {
            if (!book.isAvailable() && isBookBorrowedByStudent(book.getIsbn())) {
                tableModel.addRow(new Object[]{book.getIsbn(), book.getTitle(), book.getAuthor()});
            }
        }
    }

    private boolean isBookBorrowedByStudent(String isbn) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn) && 
                    parts[2].equals(studentId) && 
                    parts[4].equals("Borrowed")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void returnSelectedBook(JTable borrowedBooksTable, DefaultTableModel tableModel) {
        int selectedRow = borrowedBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);  // Cast to String explicitly

        try {
            // Update borrowing record first
            if (updateBorrowingRecord(isbn)) {
                // Only update book status and remove row if borrowing record was updated successfully
                Createbook.updateBookStatus(isbn, true);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Book returned successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error returning book. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }

    private boolean updateBorrowingRecord(String isbn) {
        List<String> records = new ArrayList<>();
        boolean recordFound = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn) && parts[2].equals(studentId) && parts[4].equals("Borrowed")) {
                    // Create new record with "Returned" status
                    records.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + ",Returned");
                    recordFound = true;
                } else {
                    records.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!recordFound) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWING_FILE))) {
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 