import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ReturnBooks extends JFrame {
    private JFrame parentFrame;
    private DefaultListModel<Book> borrowedBooksModel;
    private JList<Book> borrowedBooksList;
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
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        // Logo
        backgroundPanel.add(Box.createVerticalStrut(10));
        ImageIcon frontbg = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        JLabel imageLabel = new JLabel(frontbg);
        Image img = frontbg.getImage();
        Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        frontbg = new ImageIcon(newImg);
        imageLabel.setIcon(frontbg);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel);

        // Header
        backgroundPanel.add(Box.createVerticalStrut(20));
        JLabel titleLabel = new JLabel("RETURN BOOKS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        titleLabel.setForeground(new Color(0x3B3030));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        backgroundPanel.add(titleLabel);

        // Add more space after the title
        backgroundPanel.add(Box.createVerticalStrut(0));

        // Borrowed books list
        borrowedBooksModel = new DefaultListModel<>();
        loadBorrowedBooks();
        borrowedBooksList = new JList<>(borrowedBooksModel);
        borrowedBooksList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(borrowedBooksList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setMaximumSize(new Dimension(400, 300));
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(scrollPane);

        // Return button
        JButton returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        returnButton.setBackground(new Color(0x603F26));
        returnButton.setForeground(Color.WHITE);
        returnButton.setAlignmentX(CENTER_ALIGNMENT);
        returnButton.setPreferredSize(new Dimension(250, 50));
        returnButton.setMaximumSize(new Dimension(250, 50));
        returnButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        returnButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(returnButton);
        returnButton.addActionListener(e -> returnSelectedBook());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setMaximumSize(new Dimension(100, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });

        // Create horizontal box for back button
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(backButton);
        horizontalBox.add(Box.createRigidArea(new Dimension(20, 0)));

        backgroundPanel.add(Box.createVerticalStrut(-50));
        backgroundPanel.add(horizontalBox);
        backgroundPanel.add(Box.createVerticalStrut(20));

      

        // Frame settings
        add(backgroundPanel);
        setSize(720, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadBorrowedBooks() {
        List<Book> allBooks = Createbook.getAllBooks();
        for (Book book : allBooks) {
            if (!book.isAvailable()) {
                // Check if this book is borrowed by this student
                if (isBookBorrowedByStudent(book.getIsbn())) {
                    borrowedBooksModel.addElement(book);
                }
            }
        }
    }

    private boolean isBookBorrowedByStudent(String isbn) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn) && parts[2].equals(studentId) && parts[5].equals("Borrowed")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void returnSelectedBook() {
        Book selectedBook = borrowedBooksList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }

        // Update borrowing record
        updateBorrowingRecord(selectedBook.getIsbn());

        // Update book status
        Createbook.updateBookStatus(selectedBook.getIsbn(), true);
        borrowedBooksModel.removeElement(selectedBook);

        JOptionPane.showMessageDialog(this, "Book returned successfully!");
    }

    private void updateBorrowingRecord(String isbn) {
        List<String> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn) && parts[2].equals(studentId) && parts[5].equals("Borrowed")) {
                    BorrowingRecord record = new BorrowingRecord(parts[0], parts[1], parts[2]);
                    record.returnBook();
                    records.add(record.toString());
                } else {
                    records.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWING_FILE))) {
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 