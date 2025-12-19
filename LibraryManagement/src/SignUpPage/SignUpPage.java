package src.SignUpPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUpPage extends JFrame implements ActionListener {

    JTextField idField, nameField;
    JPasswordField passField;
    JButton signupBtn, cancelBtn;

    public SignUpPage() {

        setTitle("Student Sign-Up");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- MAIN PANEL WITH BORDER ----------
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Font font = new Font("Arial", Font.PLAIN, 18);

        // -------- Form Fields --------
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(font);

        idField = new JTextField();
        idField.setFont(font);
        idField.setPreferredSize(new Dimension(250, 40));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(font);

        nameField = new JTextField();
        nameField.setFont(font);
        nameField.setPreferredSize(new Dimension(250, 40));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(font);

        passField = new JPasswordField();
        passField.setFont(font);
        passField.setPreferredSize(new Dimension(250, 40));

        signupBtn = new JButton("Sign Up");
        signupBtn.setFont(font);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(font);

        signupBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        // Add components to panel
        mainPanel.add(idLabel);        mainPanel.add(idField);
        mainPanel.add(nameLabel);      mainPanel.add(nameField);
        mainPanel.add(passLabel);      mainPanel.add(passField);
        mainPanel.add(signupBtn);      mainPanel.add(cancelBtn);

        // Add panel to frame
        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == signupBtn) {
            saveStudentToDatabase();
        } 
        else if (e.getSource() == cancelBtn) {
            dispose(); // close window
        }
    }

    // ===========================================================
    // SAVE SIGNUP DATA TO DATABASE
    // ===========================================================
    private void saveStudentToDatabase() {

        String studentId = idField.getText();
        String name = nameField.getText();
        String password = new String(passField.getPassword());

        if (studentId.isEmpty() || name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return;
        }

        String url = "db url";
        String user = "db userName";
        String pass = "db password";
        
        String query = "INSERT INTO students (student_id, name, password) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, studentId);
            pst.setString(2, name);
            pst.setString(3, password);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sign-Up Successful!");

            con.close();
            dispose(); // close signup page

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Student ID already exists!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data!");
        }
    }
}
