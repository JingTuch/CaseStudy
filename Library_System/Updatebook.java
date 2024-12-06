import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Updatebook {
    private JFrame frame;
    private JTextField bookNumberField, bookTitleField, bookAuthorField;

    public Updatebook(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Update Book");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("bgpictttt.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Update Book Details");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

        JLabel bookNumberLabel = new JLabel("Enter Book Number to Update");
        bookNumberLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(bookNumberLabel);

        bookNumberField = new JTextField();
        panel.add(bookNumberField);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        searchButton.setBackground(new Color(0x603F26));
        searchButton.setForeground(Color.WHITE);
        searchButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(searchButton);

        JLabel bookTitleLabel = new JLabel("Book Title");
        bookTitleLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(bookTitleLabel);
        bookTitleField = new JTextField();
        bookTitleField.setEditable(false);
        panel.add(bookTitleField);

        JLabel bookAuthorLabel = new JLabel("Book Author");
        bookAuthorLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(bookAuthorLabel);
        bookAuthorField = new JTextField();
        bookAuthorField.setEditable(false);
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                adminFrame.setVisible(true);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(backButton);
        horizontalBox.add(Box.createRigidArea(new Dimension(20, 0)));

        panel.add(horizontalBox);

        frame.add(panel);
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String[] findBookDetails(String bookNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",", 3);
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
                String[] details = line.split(",", 3);
                if (details[0].equals(bookNumber)) {
                    writer.write(bookNumber + "," + bookTitle + "," + bookAuthor);
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
}
