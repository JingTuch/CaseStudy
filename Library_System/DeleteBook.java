import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class DeleteBook {
    private JFrame frame;
    private JTextField bookNumberField;
    private JTable bookTable;

    public DeleteBook(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Delete Book");
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
        JLabel headerLabel = new JLabel("Delete Book");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        headerLabel.setForeground(new Color(0x3B3030));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(headerLabel);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(400, 40));

        JLabel bookNumberLabel = new JLabel("Book ID:");
        bookNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookNumberField = new JTextField();
        
        inputPanel.add(bookNumberLabel);
        inputPanel.add(bookNumberField);
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

        // Delete Button
        JButton deleteButton = createStyledButton("Delete", 200, 40);
        deleteButton.addActionListener(e -> {
            String bookNumber = bookNumberField.getText().trim();
            if (bookNumber.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a book number.");
                return;
            }

            if (deleteBook(bookNumber)) {
                JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
                bookNumberField.setText("");
                updateBookTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Book not found.");
            }
        });
        panel.add(deleteButton);
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

    private void setupBookTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Book ID", "Title", "Author", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(model);
        
       
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        bookTable.getTableHeader().setBackground(new Color(0x603F26));
        bookTable.getTableHeader().setForeground(Color.WHITE);

        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        updateBookTable();
    }

    
// -----------------[ BACK END ]-----------------


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