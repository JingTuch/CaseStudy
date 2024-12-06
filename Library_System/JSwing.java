import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JSwing extends JFrame {
    private JFrame mainmenuFrame; 



    JSwing() {
        this.setTitle("ULMS");

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

        JLabel label = new JLabel("UCC LIBRARY MANAGEMENT SYSTEM ");
        label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 32));
        label.setForeground(new Color(0x3B3030));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(50, 0, 10, 0));
        backgroundPanel.add(label);

        ImageIcon frontbg = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        JLabel imageLabel = new JLabel(frontbg);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel); 

        ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        this.setIconImage(logo.getImage());

        JButton startButton = new JButton("ENTER");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        startButton.setBackground(new Color(0x603F26));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setMaximumSize(new Dimension(150, 50));
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        startButton.setFocusPainted(false);
        backgroundPanel.add(startButton);

        this.add(backgroundPanel); 
        this.setResizable(false);
        this.setSize(720, 620);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu();
            }
        });
    }

    public void MainMenu() {
        this.dispose();
    
        mainmenuFrame = new JFrame("ULMS");
    
        ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        mainmenuFrame.setIconImage(logo.getImage());
    
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
    
        backgroundPanel.add(Box.createVerticalStrut(20));
        ImageIcon frontbg = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        JLabel imageLabel = new JLabel(frontbg);
        Image img = frontbg.getImage(); 
        Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        frontbg = new ImageIcon(newImg); 
        imageLabel.setIcon(frontbg);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel);
        
        backgroundPanel.add(Box.createVerticalStrut(-20));
        JLabel welcome = new JLabel("WELCOME TO UCC LIBRARY!  ");
        welcome.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        welcome.setForeground(new Color(0x3B3030));
        welcome.setAlignmentX(CENTER_ALIGNMENT);
        welcome.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));
        backgroundPanel.add(welcome);
    
        JButton admiButton = new JButton("Admin");
        admiButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        admiButton.setBackground(new Color(0x603F26));
        admiButton.setForeground(Color.WHITE);
        admiButton.setAlignmentX(CENTER_ALIGNMENT);
        admiButton.setPreferredSize(new Dimension(200, 50));
        admiButton.setMaximumSize(new Dimension(200, 50));
        admiButton.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        admiButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20)); 
        backgroundPanel.add(admiButton);

        admiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainmenuFrame.dispose(); 
                
                Adminlogin loginFrame = new Adminlogin(mainmenuFrame); 
                
                loginFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        if (loginFrame.isAuthenticated()) {
                            AdminMenu(); 
                        } else {  
                            if (mainmenuFrame == null || !mainmenuFrame.isDisplayable()) {
                                new JSwing(); 
                            }
                        }
                    }
                });
            }
        });
        

        JButton studenButton = new JButton("Student");
        studenButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        studenButton.setBackground(new Color(0x603F26));
        studenButton.setForeground(Color.WHITE);
        studenButton.setAlignmentX(CENTER_ALIGNMENT);
        studenButton.setPreferredSize(new Dimension(200, 50));
        studenButton.setMaximumSize(new Dimension(200, 50));
        studenButton.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        studenButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20)); 
        backgroundPanel.add(studenButton);

        studenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainmenuFrame.dispose(); 
        
                StudentLogin studentLoginFrame = new StudentLogin(mainmenuFrame);
                studentLoginFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        if (studentLoginFrame.isAuthenticated()) {
                            Studentmenu();
                        }
                    }
                });
            }
        });

        JButton aboutButton = new JButton("How to use?");
        aboutButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        aboutButton.setBackground(new Color(0x603F26));
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setAlignmentX(CENTER_ALIGNMENT);
        aboutButton.setPreferredSize(new Dimension(200, 50));
        aboutButton.setMaximumSize(new Dimension(200, 50));
        aboutButton.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        aboutButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(aboutButton);
    
        mainmenuFrame.add(backgroundPanel);
    
        mainmenuFrame.setSize(720, 520);
        mainmenuFrame.setResizable(false);
        mainmenuFrame.setLocationRelativeTo(null);
        mainmenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainmenuFrame.setVisible(true);
    }

    //ADMIN MENU
        public void AdminMenu() {

            JFrame adminFrame = new JFrame("ULMS");
        
            ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
            adminFrame.setIconImage(logo.getImage());
        
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
            Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            frontbg = new ImageIcon(newImg); 
            imageLabel.setIcon(frontbg);
            imageLabel.setAlignmentX(CENTER_ALIGNMENT);
            backgroundPanel.add(imageLabel);
            
            backgroundPanel.add(Box.createVerticalStrut(-20));
            JLabel adminportal = new JLabel("ADMIN PORTAL ");
            adminportal.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
            adminportal.setForeground(new Color(0x3B3030));
            adminportal.setAlignmentX(CENTER_ALIGNMENT);
            adminportal.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));
            backgroundPanel.add(adminportal);
    
            backgroundPanel.add(Box.createVerticalStrut(-10));
            JButton StudentMange = new JButton("Student Management");
            StudentMange.setFont(new Font("SansSerif", Font.BOLD, 23));
            StudentMange.setBackground(new Color(0x603F26));
            StudentMange.setForeground(Color.WHITE);
            StudentMange.setAlignmentX(CENTER_ALIGNMENT);
            StudentMange.setPreferredSize(new Dimension(300, 50));
            StudentMange.setMaximumSize(new Dimension(300, 50));
            StudentMange.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            StudentMange.setFocusPainted(false);
            backgroundPanel.add(Box.createVerticalStrut(20));
            backgroundPanel.add(StudentMange);
            
            StudentMange.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StudentManagement studmange = new StudentManagement(adminFrame);
                    studmange.show();
                    adminFrame.setVisible(false); 
                }
            });
            
            
    
            backgroundPanel.add(Box.createVerticalStrut(-5));
            JButton createbook = new JButton("Create Books");
            createbook.setFont(new Font("SansSerif", Font.BOLD, 23));
            createbook.setBackground(new Color(0x603F26));
            createbook.setForeground(Color.WHITE);
            createbook.setAlignmentX(CENTER_ALIGNMENT);
            createbook.setPreferredSize(new Dimension(250, 50));
            createbook.setMaximumSize(new Dimension(250, 50));
            createbook.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            createbook.setFocusPainted(false);
            backgroundPanel.add(Box.createVerticalStrut(20));
            backgroundPanel.add(createbook);
            createbook.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Createbook(adminFrame, adminFrame);
                    adminFrame.setVisible(false);
                }
            });
    
            backgroundPanel.add(Box.createVerticalStrut(-5));
            JButton updatebook = new JButton("Update Books");
            updatebook.setFont(new Font("SansSerif", Font.BOLD, 23));
            updatebook.setBackground(new Color(0x603F26));
            updatebook.setForeground(Color.WHITE);
            updatebook.setAlignmentX(CENTER_ALIGNMENT);
            updatebook.setPreferredSize(new Dimension(250, 50));
            updatebook.setMaximumSize(new Dimension(250, 50));
            updatebook.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            updatebook.setFocusPainted(false);
            backgroundPanel.add(Box.createVerticalStrut(20));
            backgroundPanel.add(updatebook);
            updatebook.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Updatebook(adminFrame, adminFrame);
                    adminFrame.setVisible(false);
                }
            });
            
            backgroundPanel.add(Box.createVerticalStrut(-5));
            JButton deletebook = new JButton("Delete Books");
            deletebook.setFont(new Font("SansSerif", Font.BOLD, 23));
            deletebook.setBackground(new Color(0x603F26));
            deletebook.setForeground(Color.WHITE);
            deletebook.setAlignmentX(CENTER_ALIGNMENT);
            deletebook.setPreferredSize(new Dimension(250, 50));
            deletebook.setMaximumSize(new Dimension(250, 50));
            deletebook.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            deletebook.setFocusPainted(false);
            backgroundPanel.add(Box.createVerticalStrut(20));
            backgroundPanel.add(deletebook);
            deletebook.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new DeleteBook(adminFrame, adminFrame); 
                    adminFrame.setVisible(false);          
                }
            });
            backgroundPanel.add(Box.createVerticalStrut(-5));
            JButton viewbooks = new JButton("Display all Books");
            viewbooks.setFont(new Font("SansSerif", Font.BOLD, 23));
            viewbooks.setBackground(new Color(0x603F26));
            viewbooks.setForeground(Color.WHITE);
            viewbooks.setAlignmentX(CENTER_ALIGNMENT);
            viewbooks.setPreferredSize(new Dimension(250, 50));
            viewbooks.setMaximumSize(new Dimension(250, 50));
            viewbooks.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            viewbooks.setFocusPainted(false);
            backgroundPanel.add(Box.createVerticalStrut(20));
            backgroundPanel.add(viewbooks);
            viewbooks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new DisplayBooks(adminFrame, adminFrame); 
                    adminFrame.setVisible(false);          
                }
            });
            backgroundPanel.add(Box.createVerticalStrut(-5));
    
            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
            backButton.setBackground(new Color(0x603F26));
            backButton.setForeground(Color.WHITE);
            backButton.setPreferredSize(new Dimension(100, 40));
            backButton.setMaximumSize(new Dimension(100, 40));
            backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            backButton.setFocusPainted(false);

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adminFrame.dispose();
                    MainMenu();
                }
            });


            
            Box horizontalBox = Box.createHorizontalBox();
            horizontalBox.add(Box.createHorizontalGlue());        
            horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));  
            horizontalBox.add(backButton);                    
            horizontalBox.add(Box.createRigidArea(new Dimension(20, 0))); 
            
            backgroundPanel.add(Box.createVerticalStrut(30)); 
            backgroundPanel.add(horizontalBox);
            
        
            adminFrame.add(backgroundPanel);
            adminFrame.setSize(720, 620);
            adminFrame.setResizable(false);
            adminFrame.setLocationRelativeTo(null);
            adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            adminFrame.setVisible(true);
        
    
    }
    //STUDENT MENU
    public void Studentmenu() {
        JFrame studentFrame = new JFrame("ULMS");
    
        ImageIcon logo = new ImageIcon("White and Blue Illustrative Class Logo-modified.png");
        studentFrame.setIconImage(logo.getImage());
    
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
        Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        frontbg = new ImageIcon(newImg); 
        imageLabel.setIcon(frontbg);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundPanel.add(imageLabel);
    
        backgroundPanel.add(Box.createVerticalStrut(-20));
        JLabel studentPortal = new JLabel("STUDENT PORTAL");
        studentPortal.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        studentPortal.setForeground(new Color(0x3B3030));
        studentPortal.setAlignmentX(CENTER_ALIGNMENT);
        studentPortal.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));
        backgroundPanel.add(studentPortal);
    
        
        JButton borrowBooks = new JButton("Borrow Books");
        borrowBooks.setFont(new Font("SansSerif", Font.BOLD, 23));
        borrowBooks.setBackground(new Color(0x603F26));
        borrowBooks.setForeground(Color.WHITE);
        borrowBooks.setAlignmentX(CENTER_ALIGNMENT);
        borrowBooks.setPreferredSize(new Dimension(250, 50));
        borrowBooks.setMaximumSize(new Dimension(250, 50));
        borrowBooks.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        borrowBooks.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(borrowBooks);
    
        borrowBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentStudentId = StudentLogin.getCurrentStudentId();
                new BorrowBooks(studentFrame, currentStudentId);
                studentFrame.setVisible(false);
            }
        });
    
        JButton returnBooks = new JButton("Return Books");
        returnBooks.setFont(new Font("SansSerif", Font.BOLD, 23));
        returnBooks.setBackground(new Color(0x603F26));
        returnBooks.setForeground(Color.WHITE);
        returnBooks.setAlignmentX(CENTER_ALIGNMENT);
        returnBooks.setPreferredSize(new Dimension(250, 50));
        returnBooks.setMaximumSize(new Dimension(250, 50));
        returnBooks.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        returnBooks.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(returnBooks);
    
        returnBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentStudentId = StudentLogin.getCurrentStudentId();
                new ReturnBooks(studentFrame, currentStudentId);
                studentFrame.setVisible(false);
            }
        });
    
        JButton historyButton = new JButton("History");
        historyButton.setFont(new Font("SansSerif", Font.BOLD, 23));
        historyButton.setBackground(new Color(0x603F26));
        historyButton.setForeground(Color.WHITE);
        historyButton.setAlignmentX(CENTER_ALIGNMENT);
        historyButton.setPreferredSize(new Dimension(250, 50));
        historyButton.setMaximumSize(new Dimension(250, 50));
        historyButton.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        historyButton.setFocusPainted(false);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(historyButton);
    
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentStudentId = StudentLogin.getCurrentStudentId();
                new BorrowingHistory(studentFrame, currentStudentId);
                studentFrame.setVisible(false);
            }
        });
    
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        backButton.setBackground(new Color(0x603F26));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setMaximumSize(new Dimension(100, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.setFocusPainted(false);
    
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentFrame.dispose();
                MainMenu();
            }
        });
    
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(backButton);
        horizontalBox.add(Box.createRigidArea(new Dimension(20, 0)));
    
        backgroundPanel.add(Box.createVerticalStrut(30));
        backgroundPanel.add(horizontalBox);
    
        studentFrame.add(backgroundPanel);
        studentFrame.setSize(620, 520);
        studentFrame.setResizable(false);
        studentFrame.setLocationRelativeTo(null);
        studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentFrame.setVisible(true);
    }
    
}


