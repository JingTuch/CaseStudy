import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class DeleteStudent extends JFrame {
    private JTextField studentNumberField;

    public DeleteStudent(JFrame studmanageFrame, List<Student> students) {
        setTitle("ULMS ADMIN: Delete Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(studmanageFrame);
        setResizable(false);

        ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        studmanageFrame.setIconImage(logo.getImage());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(0xFFF0D1));
        setContentPane(contentPanel);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xFFF0D1));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Student Number:"), constraints);

        studentNumberField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(studentNumberField, constraints);

        contentPanel.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xFFF0D1));

        JButton deleteButton = createStyledButton("Delete");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentNumber = studentNumberField.getText().trim();

                if (studentNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(DeleteStudent.this, "Student number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student studentToDelete = findStudentByNumber(studentNumber, students);
                

                if (studentToDelete != null) {
                    deleteStudentFromList(studentToDelete, students);
                    deleteStudentFromFile(studentNumber);

                    String studentName = studentToDelete.getName();

                    JOptionPane.showMessageDialog(DeleteStudent.this, "Student " + studentName + " deleted successfully!");
                    dispose();
                    studmanageFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(DeleteStudent.this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                studmanageFrame.setVisible(true);
            }
        });

        setVisible(true);
    }

    private Student findStudentByNumber(String studentNumber, List<Student> students) {
        for (Student student : students) {
            if (student.getStudentNumber().equals(studentNumber)) {
                return student;
            }
        }
        return null;
    }

    private void deleteStudentFromList(Student studentToDelete, List<Student> students) {
        students.remove(studentToDelete);
    }

    private void deleteStudentFromFile(String studentNumber) {
        try {
            File file = new File("students.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder fileContents = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && !parts[1].equals(studentNumber)) {
                    fileContents.append(line).append("\n");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContents.toString());
            writer.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student from file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(new Color(0x603F26));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
