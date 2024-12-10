import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class BorrowBooks extends JFrame {
    private JFrame parentFrame;
    private final DefaultTableModel tableModel;
    private final JTable bookTable;
    private final String studentId;
    private static final String BORROWING_FILE = "borrowing_records.txt";

    public BorrowBooks(JFrame parent, String studentId) {
        this.parentFrame = parent;
        this.studentId = studentId;
        setTitle("ULMS - Borrow Books");

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

        JLabel titleLabel = new JLabel("BORROW BOOKS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        titleLabel.setForeground(new Color(0x3B3030));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Logo label
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Ensure "logo.png" is the correct path
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Title label with improved alignment
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Add padding to the top
        titlePanel.add(logoLabel); // Add logo first
        titlePanel.add(Box.createVerticalStrut(10)); // Add some space between logo and title
        titlePanel.add(titleLabel); // Add title next
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Table Model and JTable
        String[] columnNames = {"ISBN", "Title", "Author"};
        tableModel = new DefaultTableModel(columnNames, 0);
        loadAvailableBooks();

        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
        bookTable.getTableHeader().setBackground(new Color(0x4B2E2E));
        bookTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(750, 250)); // Adjusted size for more top space

        // Add margin around the scroll pane
        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margin
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        // Customize Table (column sizes and alignment)
        customizeTable();

        // Borrow Button
        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        borrowButton.setBackground(new Color(0x603F26));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setAlignmentX(CENTER_ALIGNMENT);
        borrowButton.addActionListener(e -> borrowSelectedBook());

        // Borrow Button with tooltip
        borrowButton.setToolTipText("Click to borrow the selected book");

        // Back Button
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
        bottomPanel.add(borrowButton);
        bottomPanel.add(backButton);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setSize(800, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadAvailableBooks() {
        List<Book> books = Createbook.getAllBooks();
        for (Book book : books) {
            if (book.isAvailable()) {
                tableModel.addRow(new Object[]{book.getIsbn(), book.getTitle(), book.getAuthor()});
            }
        }
    }

    private void borrowSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow.");
            return;
        }

        // Check if student has any overdue books
        if (hasOverdueBooks()) {
            JOptionPane.showMessageDialog(this, 
                "You have overdue books. Please return them before borrowing new ones.", 
                "Overdue Books", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if student has reached the borrow limit
        if (!canBorrowMore()) {
            JOptionPane.showMessageDialog(this, 
                "You have reached the maximum limit of 3 borrowed books.\nPlease return some books before borrowing more.",
                "Borrow Limit Reached",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String isbn = tableModel.getValueAt(selectedRow, 0).toString();
        String title = tableModel.getValueAt(selectedRow, 1).toString();
        String author = tableModel.getValueAt(selectedRow, 2).toString();
        LocalDate borrowDate = LocalDate.now();

        // Save record to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWING_FILE, true))) {
            // Format: ISBN,Title,StudentID,BorrowDate,Status,Author
            String record = String.format("%s,%s,%s,%s,%s,%s",
                isbn,
                title,
                studentId,
                borrowDate.format(DateTimeFormatter.ISO_DATE),
                "Borrowed",
                author
            );
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error recording the borrowing.");
            return;
        }

        // Update book status
        Createbook.updateBookStatus(isbn, false);
        tableModel.removeRow(selectedRow);

        // Show success message with return date
        LocalDate dueDate = borrowDate.plusDays(3);
        JOptionPane.showMessageDialog(this, 
            String.format("Book borrowed successfully!\nDue Date: %s\nNote: Penalty will be applied if returned after due date.", 
            dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    }

    private boolean hasOverdueBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[2].equals(studentId) && parts[4].equals("Borrowed")) {
                    try {
                        LocalDate borrowDate = LocalDate.parse(parts[3]);
                        if (LocalDate.now().isAfter(borrowDate.plusDays(3))) {
                            return true;
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid date format found for record: " + line);
                        continue; // Skip this record and continue checking others
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking overdue books.");
            return false;
        }
        return false;
    }

    private boolean canBorrowMore() {
        int currentBorrowCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[2].equals(studentId) && parts[4].equals("Borrowed")) {
                    currentBorrowCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return currentBorrowCount < 3;
    }

    private void customizeTable() {
        // Modify column sizes
        TableColumnModel columnModel = bookTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150); // ISBN
        columnModel.getColumn(1).setPreferredWidth(300); // Title
        columnModel.getColumn(2).setPreferredWidth(150); // Author

        // Center align text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}

class BorrowingRecord {
    private String isbn;
    private String title;
    private String studentId;
    private LocalDate borrowDate;
    private String status;
    private String author;

    public BorrowingRecord(String isbn, String title, String studentId, LocalDate borrowDate, String author) {
        this.isbn = isbn;
        this.title = title;
        this.studentId = studentId;
        this.borrowDate = borrowDate;
        this.status = "Borrowed";
        this.author = author;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(borrowDate.plusDays(3));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s,%s,%s,%s,%s,%s",
            isbn,
            title,
            studentId,
            borrowDate.format(formatter),
            status,
            author
        );
    }
}
