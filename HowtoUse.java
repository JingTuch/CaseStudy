import javax.swing.*;
import java.awt.*;

public class HowtoUse extends JFrame {

    HowtoUse() {
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

        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));



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
