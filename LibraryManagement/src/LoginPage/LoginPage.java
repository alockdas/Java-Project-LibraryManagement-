package src.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.HomeNew.HomeNew;

public class LoginPage extends JFrame implements ActionListener {

    JTextField idField;
    JPasswordField passField;
    JButton loginBtn, cancelBtn;

    public LoginPage() {

        setTitle("Student Login");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- MAIN PANEL WITH BORDER ----------
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Font font = new Font("Arial", Font.PLAIN, 18);

        // -------- Labels and Fields --------
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(font);

        idField = new JTextField();
        idField.setFont(font);
        idField.setPreferredSize(new Dimension(250, 40));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(font);

        passField = new JPasswordField();
        passField.setFont(font);
        passField.setPreferredSize(new Dimension(250, 40));

        loginBtn = new JButton("Login");
        loginBtn.setFont(font);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(font);

        loginBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        // Add components to panel
        mainPanel.add(idLabel);      mainPanel.add(idField);
        mainPanel.add(passLabel);    mainPanel.add(passField);
        mainPanel.add(loginBtn);     mainPanel.add(cancelBtn);

        // Add panel to frame
        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginBtn) {
            checkLogin();
        } 
        else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    private void checkLogin() {

        String studentId = idField.getText();
        String password  = new String(passField.getPassword());

        if (studentId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        String query = "SELECT * FROM students WHERE student_id=? AND password=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, studentId);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                //con.close();
                dispose();
                new HomeNew(studentId, rs.getString("name"));
                //openNextPage(studentId);
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or Password!");
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
        
    }

        
    
}
