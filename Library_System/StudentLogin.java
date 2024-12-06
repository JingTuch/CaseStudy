import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class StudentLogin extends JFrame {
    private final JTextField numberField;
    private final JPasswordField passwordField;
    private boolean isAuthenticated = false;
    private static final String STUDENT_FILE = "students.txt";
    private List<Student> students;
    private static String currentStudentId;

    StudentLogin(JFrame parentFrame) {
        setTitle("ULMS Student Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 300);
        setResizable(false);
        setLocationRelativeTo(parentFrame);

        ImageIcon logo = new ImageIcon(getClass().getResource("/White and Blue Illustrative Class Logo-modified.png"));
        setIconImage(logo.getImage());

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

        numberField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(numberField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Password:"), constraints);

        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(new Color(0xFFF0D1));
        showPassword.addActionListener(e -> passwordField.setEchoChar(showPassword.isSelected() ? '\u0000' : '*'));
        panel.add(showPassword, constraints);

        contentPanel.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xFFF0D1));

        JButton loginButton = createStyledButton("Login");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        students = loadStudentsFromFile();

        loginButton.addActionListener(e -> handleLogin(parentFrame));
        cancelButton.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                if (!isAuthenticated) {
                    parentFrame.setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    private void handleLogin(JFrame parentFrame) {
        String studentNumber = numberField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
    
        if (studentNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Optional<Student> loggedInStudent = students.stream()
                .filter(student -> student.getStudentNumber().equals(studentNumber) && student.getPassword().equals(password))
                .findFirst();
    
        if (loggedInStudent.isPresent()) {
            String studentName = loggedInStudent.get().getName();
            JOptionPane.showMessageDialog(this, "Welcome, " + studentName + "!");
            currentStudentId = studentNumber;
            isAuthenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Student> loadStudentsFromFile() {
        List<Student> studentList = new ArrayList<>();
        File file = new File(STUDENT_FILE);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Students file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return studentList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    String studentNumber = parts[1].trim();
                    String password = parts[2].trim();
                    studentList.add(new Student(name, studentNumber, password));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading students file!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return studentList;
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

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public static String getCurrentStudentId() {
        return currentStudentId;
    }
}
