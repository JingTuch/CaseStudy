import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentManagement extends JFrame{
    private List<Student> students; 
    private JFrame studmanageFrame;
    
    public StudentManagement(JFrame adminFrame) {

        
       students = new ArrayList<>();

        studmanageFrame = new JFrame("ULMS");

     
        ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        studmanageFrame.setIconImage(logo.getImage());

      
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
        backgroundPanel.add(Box.createVerticalStrut(10));
      

        ImageIcon frontbg = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        JLabel imageLabel = new JLabel(frontbg);
        Image img = frontbg.getImage();
        Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        frontbg = new ImageIcon(newImg);
        imageLabel.setIcon(frontbg);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel);

        backgroundPanel.add(Box.createVerticalStrut(-20));
        backgroundPanel.add(Box.createVerticalStrut(10));
        JLabel studmanage = new JLabel("Student Management");
        studmanage.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        studmanage.setForeground(new Color(0x3B3030));
        studmanage.setAlignmentX(Component.CENTER_ALIGNMENT);
        studmanage.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));
        backgroundPanel.add(studmanage);

        //GAWA
        JButton createstud = new JButton("Create Student");
        createstud.setFont(new Font("SansSerif", Font.BOLD, 23));
        createstud.setBackground(new Color(0x603F26));
        createstud.setForeground(Color.WHITE);
        createstud.setAlignmentX(Component.CENTER_ALIGNMENT);
        createstud.setPreferredSize(new Dimension(250, 50));
        createstud.setMaximumSize(new Dimension(250, 50));
        createstud.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createstud.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(createstud);

        createstud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studmanageFrame.setVisible(false); 
                
                new CreateStudent(studmanageFrame, students);
            }
        });
        
        
        //BABAGUHIN
        JButton updatestud = new JButton("Update Student");
        updatestud.setFont(new Font("SansSerif", Font.BOLD, 23));
        updatestud.setBackground(new Color(0x603F26));
        updatestud.setForeground(Color.WHITE);
        updatestud.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatestud.setPreferredSize(new Dimension(250, 50));
        updatestud.setMaximumSize(new Dimension(250, 50));
        updatestud.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updatestud.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(updatestud);

        updatestud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateStudent upd = new UpdateStudent(studmanageFrame, students);  
                upd.setVisible(true);
            }
        });

        JButton deletestud = new JButton("Delete Student");
        deletestud.setFont(new Font("SansSerif", Font.BOLD, 23));
        deletestud.setBackground(new Color(0x603F26));
        deletestud.setForeground(Color.WHITE);
        deletestud.setAlignmentX(Component.CENTER_ALIGNMENT);
        deletestud.setPreferredSize(new Dimension(250, 50));
        deletestud.setMaximumSize(new Dimension(250, 50));
        deletestud.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        deletestud.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(deletestud);
        deletestud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteStudent del = new DeleteStudent(studmanageFrame, students);
                del.setVisible(true);
            }
        });


        //MERON NA
        JButton existingstud = new JButton("Existing Students");
        existingstud.setFont(new Font("SansSerif", Font.BOLD, 23));
        existingstud.setBackground(new Color(0x603F26));
        existingstud.setForeground(Color.WHITE);
        existingstud.setAlignmentX(Component.CENTER_ALIGNMENT);
        existingstud.setPreferredSize(new Dimension(250, 50));
        existingstud.setMaximumSize(new Dimension(250, 50));
        existingstud.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        existingstud.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(existingstud);

        existingstud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExistingStud(studmanageFrame, students);
                studmanageFrame.setVisible(false);
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE); 
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studmanageFrame.setVisible(false);
                adminFrame.setVisible(true);
            }
        });

        

        studmanageFrame.add(backgroundPanel);
        studmanageFrame.setSize(600, 600);
        studmanageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studmanageFrame.setLocationRelativeTo(null);
    }

    public void show() {
        studmanageFrame.setVisible(true);
    }
}
