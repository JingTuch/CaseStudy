import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class DeleteBook {
    private JFrame frame;
    private JTextField bookNumberField;

    public DeleteBook(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Delete Book");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Delete Book");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

        JLabel bookNumberLabel = new JLabel("Enter Book Number to Delete");
        bookNumberLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(bookNumberLabel);

        bookNumberField = new JTextField();
        panel.add(bookNumberField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        deleteButton.setBackground(new Color(0x603F26));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookNumber = bookNumberField.getText();

                if (bookNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a book number.");
                    return;
                }

                if (deleteBook(bookNumber)) {
                    JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
                    frame.dispose();
                    parentFrame.setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(frame, "Book not found.");
                }
            }
        });

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
                parentFrame.setVisible(true); 
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
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private boolean deleteBook(String bookNumber) {
        File inputFile = new File("books.txt");
        File tempFile = new File("books_temp.txt");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean bookFound = false;
    
            
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (!details[0].equals(bookNumber)) {
                    writer.write(line);  
                    writer.newLine();
                } else {
                    bookFound = true;  
                }
            }
    
    
            writer.flush();
    
           
            if (bookFound) {
                try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(inputFile))) {               
                    try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile))) {
                        String tempLine;
                        while ((tempLine = tempReader.readLine()) != null) {
                            fileWriter.write(tempLine);  
                            fileWriter.newLine();
                        }
                    }
                }
    
                tempFile.delete(); 
                return true; 
            } else {
                
                tempFile.delete();
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting book: " + e.getMessage());
            return false;
        }
    }
    
}