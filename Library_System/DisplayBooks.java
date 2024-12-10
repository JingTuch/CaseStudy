import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DisplayBooks {
    private JFrame frame;

    public DisplayBooks(JFrame parentFrame, JFrame adminFrame) {
        frame = new JFrame("Books List");
        frame.setIconImage(new ImageIcon("White and Blue Illustrative Class Logo-modified.png").getImage());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0xFFF0D1));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Book List"));
        frame.setContentPane(contentPanel);

        JLabel titleLabel = new JLabel("All Books", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Book ID", "Book Title", "Book Authors", "Status", "Overdue Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookTable.setBackground(new Color(0xFFF0D1));
        bookTable.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookTable.setDefaultRenderer(Object.class, centerRenderer);

        bookTable.getTableHeader().setBackground(new Color(0x603F26));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setMaximumSize(new Dimension(120, 40));
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        backButton.setFocusPainted(false);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(0x7A4B3A));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(0x603F26));
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            parentFrame.setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(0xFFF0D1));
        bottomPanel.add(backButton);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadBookDetails(tableModel);

        frame.setSize(600, 500);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadBookDetails(DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 4) {
                    String isbn = details[0].trim();
                    String status = details[3].trim();
                    String overdueStatus = "N/A";
                    
                    if (status.equalsIgnoreCase("borrowed")) {
                        overdueStatus = isBookOverdue(isbn) ? "Overdue" : "Not Overdue";
                    }

                    tableModel.addRow(new Object[]{
                        isbn,
                        details[1].trim(),
                        details[2].trim(),
                        status.substring(0, 1).toUpperCase() + status.substring(1),
                        overdueStatus
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage());
        }
    }

    private boolean isBookOverdue(String isbn) {
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowing_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(isbn) && parts[4].equals("Borrowed")) {
                    LocalDate borrowDate = LocalDate.parse(parts[3]);
                    return LocalDate.now().isAfter(borrowDate.plusDays(3));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
