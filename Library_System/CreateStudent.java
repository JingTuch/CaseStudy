import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class CreateStudent extends JFrame {
    private JTextField nameField;
    private JTextField numberField;
    private JPasswordField passwordField;

    CreateStudent(JFrame studmanageFrame, List<Student> students) {
        setTitle("ULMS ADMIN: Create Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 250);
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
        panel.add(new JLabel("Name:"), constraints);

        nameField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Student Number:"), constraints);

        numberField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(numberField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Password:"), constraints);

        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        contentPanel.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xFFF0D1));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String studentNumber = numberField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (validateInputs(name, studentNumber, password)) {
                    if (!isStudentNumberUnique(studentNumber, students)) {
                        JOptionPane.showMessageDialog(CreateStudent.this,
                                "Student number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Student newStudent = new Student(name, studentNumber, password);
                    students.add(newStudent);
                    saveStudentToFile(newStudent);

                    JOptionPane.showMessageDialog(CreateStudent.this, "Student created successfully!");
                    dispose();
                    studmanageFrame.setVisible(true); 
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

    private boolean validateInputs(String name, String studentNumber, String password) {
        if (name.isEmpty() || studentNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.length() < 6 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long and contain both letters and numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isStudentNumberUnique(String studentNumber, List<Student> students) {
        for (Student student : students) {
            if (student.getStudentNumber().equals(studentNumber)) {
                return false;
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].equals(studentNumber)) {
                    return false;
                }
            }
        } catch (IOException ignored) {
        }
        return true;
    }

    private void saveStudentToFile(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt", true))) {
            writer.write(student.getName() + "," + student.getStudentNumber() + "," + student.getPassword());
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving student to file!", "Error", JOptionPane.ERROR_MESSAGE);
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
