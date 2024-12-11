import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Createbook {
    private JFrame frame;
    private JTextField bookNumberField, bookTitleField, bookAuthorField;
    private static List<Book> books = new ArrayList<>();
    private static final String BOOKS_FILE = "books.txt";
    private DefaultListModel<Book> bookListModel;
    private JTable bookTable;

    public Createbook(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Create Book");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        loadBooksFromFile();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("bgpictttt.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set an empty border on the main panel to create margins
        
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Create New Book");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

   
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(400, 120));

        bookNumberField = new JTextField();
        bookTitleField = new JTextField();
        bookAuthorField = new JTextField();

        inputPanel.add(new JLabel("Book Number:"));
        inputPanel.add(bookNumberField);
        inputPanel.add(new JLabel("Book Title:"));
        inputPanel.add(bookTitleField);
        inputPanel.add(new JLabel("Book Author:"));
        inputPanel.add(bookAuthorField);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

   
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        submitButton.setBackground(new Color(0x603F26));
        submitButton.setForeground(Color.WHITE);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setMaximumSize(new Dimension(200, 40));
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookNumber = bookNumberField.getText().trim();
                String bookTitle = bookTitleField.getText().trim();
                String bookAuthor = bookAuthorField.getText().trim();

                if (bookNumber.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!");
                    return;
                }

          
                if (isIsbnExists(bookNumber)) {
                    JOptionPane.showMessageDialog(frame, 
                        "A book with this number already exists!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveBookDetails(bookNumber, bookTitle, bookAuthor);
                clearFields();
                
            }
        });

        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(20));

     
        bookListModel = new DefaultListModel<>();
        updateBookList();
        bookTable = new JTable();
        setupBookTable(bookTable);

        // Create a panel for the label to center it
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the label
        labelPanel.setOpaque(false); // Make the panel transparent to show the background
        JLabel existingBooksLabel = new JLabel("Existing Books:");
        existingBooksLabel.setFont(new Font("Tahoma", Font.BOLD, 16)); // Set a bold font for the label
        existingBooksLabel.setForeground(new Color(0x3B3030)); // Set the text color
        labelPanel.add(existingBooksLabel); // Add the label to the panel

        // Update the JScrollPane size to match the gray background area without margins
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(580, 400)); // Set preferred size to match the panel size
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)); // Allow it to expand
        scrollPane.setBorder(null); // Remove margins around the scroll pane

        // Add the label panel and scroll pane to the main panel
        panel.add(labelPanel); // Add the centered label panel
        panel.add(scrollPane); // Add the scroll pane
        panel.add(Box.createVerticalStrut(20));


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setMaximumSize(new Dimension(100, 40));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> { 
            frame.dispose();
            adminFrame.setVisible(true);
           
        });

        panel.add(backButton);
        panel.add(Box.createVerticalStrut(20));

        frame.add(panel);
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void saveBookDetails(String bookNumber, String bookTitle, String bookAuthor) {
        if (!bookNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "Book number must contain only numbers.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE, true))) {
            writer.write(bookNumber + "," + bookTitle + "," + bookAuthor + ",available");
            writer.newLine();

            Book newBook = new Book(bookTitle, bookAuthor, bookNumber);
            books.add(newBook);
            JOptionPane.showMessageDialog(frame, "Book created successfully!");

            updateBookTable();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving book details: " + e.getMessage());
        }
    }
    

    public static List<Book> getAllBooks() {
        if (books.isEmpty()) {
            loadBooksFromFile();
        }
        return new ArrayList<>(books);
    }

    private static void loadBooksFromFile() {
        books.clear(); 
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Book book = new Book(parts[1], parts[2], parts[0]);
                    if (parts[3].equals("borrowed")) {
                        book.setAvailable(false);
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateBookStatus(String isbn, boolean isAvailable) {
    
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                book.setAvailable(isAvailable);
                break;
            }
        }


        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn)) {
                    lines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + 
                             (isAvailable ? "available" : "borrowed"));
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        bookNumberField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
    }

    private boolean isIsbnExists(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    private void updateBookList() {
        bookListModel.clear();
        for (Book book : books) {
            bookListModel.addElement(book);
        }
    }

    private void setupBookTable(JTable bookTable) {
        bookTable.setModel(new DefaultTableModel(new Object[]{"Title", "Author", "Book ID", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Borrowed";
            model.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getIsbn(), status});
        }
    }

    private void updateBookTable() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Borrowed";
            model.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getIsbn(), status});
        }
    }
}
