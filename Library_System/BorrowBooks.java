import java.awt.*;
import java.io.*;
import java.util.List;
import javax.swing.*;

public class BorrowBooks extends JFrame {
    private JFrame parentFrame;
    private DefaultListModel<Book> bookListModel;
    private JList<Book> bookList;
    private String studentId;
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
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        
        backgroundPanel.add(Box.createVerticalStrut(10));
        ImageIcon frontbg = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        JLabel imageLabel = new JLabel(frontbg);
        Image img = frontbg.getImage();
        Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        frontbg = new ImageIcon(newImg);
        imageLabel.setIcon(frontbg);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel);

    
        backgroundPanel.add(Box.createVerticalStrut(-20));
        JLabel titleLabel = new JLabel("BORROW BOOKS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        titleLabel.setForeground(new Color(0x3B3030));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 10));
        backgroundPanel.add(titleLabel);

        
        bookListModel = new DefaultListModel<>();
        loadAvailableBooks();
        bookList = new JList<>(bookListModel);
        bookList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setMaximumSize(new Dimension(400, 300));
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(scrollPane);

        
        JButton borrowButton = new JButton("Borrow book");
        borrowButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        borrowButton.setBackground(new Color(0x603F26));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setAlignmentX(CENTER_ALIGNMENT);
        borrowButton.setPreferredSize(new Dimension(250, 50));
        borrowButton.setMaximumSize(new Dimension(250, 50));
        borrowButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        borrowButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(borrowButton);
        borrowButton.addActionListener(e -> borrowSelectedBook());

        
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

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(backButton);
        horizontalBox.add(Box.createRigidArea(new Dimension(20, 0)));

        backgroundPanel.add(Box.createVerticalStrut(-50));
        backgroundPanel.add(horizontalBox);
        backgroundPanel.add(Box.createVerticalStrut(20));

     
        add(backgroundPanel);
        setSize(720, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadAvailableBooks() {
        List<Book> books = Createbook.getAllBooks();
        for (Book book : books) {
            if (book.isAvailable()) {
                bookListModel.addElement(book);
            }
        }
    }

    private void borrowSelectedBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow");
            return;
        }

        // Create borrowing record
        BorrowingRecord record = new BorrowingRecord(
            selectedBook.getIsbn(),
            selectedBook.getTitle(),
            studentId
        );

        // Save record to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWING_FILE, true))) {
            writer.write(record.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error recording the borrowing");
            return;
        }

        // Update book status
        Createbook.updateBookStatus(selectedBook.getIsbn(), false);
        bookListModel.removeElement(selectedBook);

        JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
    }
} 