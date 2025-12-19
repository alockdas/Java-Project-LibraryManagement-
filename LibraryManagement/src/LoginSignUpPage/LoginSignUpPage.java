package src.LoginSignUpPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.SignUpPage.SignUpPage;
import src.LoginPage.LoginPage;

public class LoginSignUpPage extends JFrame implements ActionListener {

    JButton loginBtn, signupBtn;

    public LoginSignUpPage() {

        setTitle("Login / Sign Up");
        setSize(700, 300);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP MESSAGE
        JLabel msg = new JLabel(
            "If you already have an account, click Login. Otherwise, click Sign Up.",
            SwingConstants.CENTER
        );
        msg.setFont(new Font("Segoe UI", Font.BOLD, 18));
        msg.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        add(msg, BorderLayout.NORTH);

        // BUTTON AREA
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));

        loginBtn = new JButton("Login");
        signupBtn = new JButton("Sign Up");

        loginBtn.setPreferredSize(new Dimension(180, 70));
        signupBtn.setPreferredSize(new Dimension(180, 70));

        loginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        signupBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        // Add ActionListeners (NOT full work — only added)
        loginBtn.addActionListener(this);
        signupBtn.addActionListener(this);

        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginBtn) {
            dispose();
            new LoginPage();
        }
        else if (e.getSource() == signupBtn) {
            dispose();
            new SignUpPage();
        }
    }
}
