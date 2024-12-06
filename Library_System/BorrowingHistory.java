import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BorrowingHistory extends JFrame {
    private JFrame parentFrame;
    private String studentId;
    private static final String BORROWING_FILE = "borrowing_records.txt";

    public BorrowingHistory(JFrame parent, String studentId) {
        this.parentFrame = parent;
        this.studentId = studentId;
        setTitle("ULMS - Borrowing History");

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
        backgroundPanel.add(Box.createVerticalStrut(-20));
        JLabel titleLabel = new JLabel("BORROWING HISTORY");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        titleLabel.setForeground(new Color(0x3B3030));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 10));
        backgroundPanel.add(titleLabel);

        // History table
        String[] columns = {"Book Title", "Borrow Date", "Return Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable historyTable = new JTable(model);
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setMaximumSize(new Dimension(500, 300));
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(scrollPane);

        // Load history data
        loadHistoryData(model);

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

        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(horizontalBox);

        // Frame settings
        add(backgroundPanel);
        setSize(720, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadHistoryData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[2].equals(studentId)) {
                    model.addRow(new Object[]{
                        parts[1],  // Book Title
                        parts[3],  // Borrow Date
                        parts[4],  // Return Date
                        parts[5]   // Status
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 