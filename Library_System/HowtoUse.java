import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HowtoUse extends JFrame {

    HowtoUse(JFrame mainmenuFrame) {
        this.setTitle("How To Use");


        ImageIcon instructionsIcon = new ImageIcon("instructions.png");
        Image instructionsImage = instructionsIcon.getImage();

    
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(instructionsImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

      
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);

     
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HowtoUse.this.dispose(); 
                mainmenuFrame.setVisible(true); 
            }
        });

        buttonPanel.add(backButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        ImageIcon logoIcon = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        this.setIconImage(logoIcon.getImage());
        this.add(backgroundPanel);
        this.setSize(800, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
