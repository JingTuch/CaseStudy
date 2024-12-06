import java.awt.*;
import javax.swing.*;

public class Adminlogin extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private boolean isAuthenticated = false;

    public Adminlogin(JFrame parentFrame) {
        setTitle("ULMS Admin Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(350, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0xFFF0D1));

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
        panel.add(new JLabel("Username:"), constraints);

        usernameField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Password:"), constraints);

        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        contentPanel.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xFFF0D1));

        JButton loginButton = createStyledButton("Login");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            authenticate();
            if (isAuthenticated) {
                dispose(); 
            }
        });

        cancelButton.addActionListener(e -> {
            dispose(); 
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (!isAuthenticated) {
                    parentFrame.setVisible(true); 
                }
            }
        });

        setVisible(true);
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
        button.setMargin(new Insets(10, 20, 10, 20));
        return button;
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("adminangel") && password.equals("1234") ||
            username.equals("adminkenji") && password.equals("1234") ||
            username.equals("1") && password.equals("1")) {
            isAuthenticated = true;
            JOptionPane.showMessageDialog(this, "Login Successful");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
