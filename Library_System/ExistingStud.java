import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExistingStud extends JFrame {
    private JFrame parentFrame;
    private List<Student> students;
    private DefaultTableModel tableModel;

     ExistingStud(JFrame parentFrame, List<Student> students) {
        this.parentFrame = parentFrame;
        this.students = students;

        if (students.isEmpty()) {
            students.addAll(loadStudentsFromFile());
        }


        JFrame existingFrame = new JFrame("Existing Students ");
        existingFrame.setSize(700, 500);
        existingFrame.setLocationRelativeTo(parentFrame);
        existingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


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
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JLabel header = new JLabel("EXISTING STUDENTS ");
        header.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        header.setForeground(new Color(0x3B3030));  // Dark color for the header
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(header);

        // Table 
        String[] columnNames = {"Name", "Student Number", "Password"};
        tableModel = new DefaultTableModel(columnNames, 0);
        refreshTable();

        JTable studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        studentTable.getTableHeader().setBackground(new Color(0x603F26));  // Dark header background
        studentTable.getTableHeader().setForeground(Color.WHITE);  // White text on header
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26)); 
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                existingFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        backgroundPanel.add(scrollPane);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(backButton);
        existingFrame.add(backgroundPanel);
        existingFrame.setVisible(true);
    }

    private List<Student> loadStudentsFromFile() {
        List<Student> studentsFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    String studentNumber = parts[1].trim();
                    String password = parts[2].trim();
                    studentsFromFile.add(new Student(name, studentNumber, password));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading students from file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return studentsFromFile;
    }

    // Refresh table 
    public void refreshTable() {
        tableModel.setRowCount(0); 
        for (Student student : students) {
            String[] row = {student.getName(), student.getStudentNumber(), student.getPassword()};
            tableModel.addRow(row);
        }
    }

    // student and refresh the table
    public void addStudent(Student student) {
        students.add(student);
        refreshTable();
        saveStudentToFile(student);  // Save file 
    }

    private void saveStudentToFile(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt", true))) {
            writer.write(student.getName() + "," + student.getStudentNumber() + "," + student.getPassword());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving student to file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
