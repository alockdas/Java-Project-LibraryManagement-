package src.HomeNew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import src.AllBooksPage.AllBooksPage;
import src.BorrowBooksHistory.BorrowBooksHistory;
import src.ReturnBookPage.ReturnBookPage;
import src.BorrowBookPage.BorrowBookPage;

public class HomeNew extends JFrame implements ActionListener {

    JButton books, borrowHistory, borrowBooks, returnBooks, refreshBtn;
    JLabel nameLabel, idLabel;

    private String studentId;
    private Dimension buttonSize = new Dimension(220, 90);

    public HomeNew(String studentId, String studentName) {
        this.studentId = studentId;

        setTitle("LIBRARY MANAGEMENT SYSTEM");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= TOP AREA =================
        JPanel topArea = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Self Check-In & Check-Out", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        topArea.add(title, BorderLayout.NORTH);
        add(topArea, BorderLayout.NORTH);

        // ================= CENTER AREA =================
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // ---------- LEFT : STUDENT INFO ----------
        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBorder(BorderFactory.createEmptyBorder(140, 110, 80, 20));

        JLabel studentTitle = new JLabel("Student Information");
        studentTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        studentTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = new JLabel("Name: " + studentName);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        idLabel = new JLabel("ID: " + studentId);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftSide.add(studentTitle);
        leftSide.add(Box.createVerticalStrut(30));
        leftSide.add(nameLabel);
        leftSide.add(Box.createVerticalStrut(20));
        leftSide.add(idLabel);

        loadStudentInfo(studentId);

        // ---------- RIGHT : ACTION BUTTONS ----------
        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 60));

        books = createIconButton("Books", "src/assets/books.png");
        borrowBooks = createIconButton("Borrow Books", "src/assets/borrow.png");
        returnBooks = createIconButton("Return Books", "src/assets/return.png");
        borrowHistory = createIconButton("Borrow History", "src/assets/history.png");
        refreshBtn = createIconButton("Refresh", "src/assets/refresh.png");

        books.addActionListener(this);
        borrowBooks.addActionListener(this);
        returnBooks.addActionListener(this);
        borrowHistory.addActionListener(this);
        refreshBtn.addActionListener(this);

        rightSide.add(books);
        rightSide.add(borrowBooks);
        rightSide.add(returnBooks);
        rightSide.add(borrowHistory);
        rightSide.add(refreshBtn);

        centerPanel.add(leftSide);
        centerPanel.add(rightSide);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= ICON BUTTON CREATOR =================
    private JButton createIconButton(String text, String iconPath) {

        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton button = new JButton(text, icon);
        button.setPreferredSize(buttonSize);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Text below icon
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.setFocusPainted(false);
        button.setBackground(new Color(245, 245, 245));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return button;
    }

    // ================= LOAD STUDENT INFO =================
    private void loadStudentInfo(String studentId) {
        try {
            Connection conn = DriverManager.getConnection(
                    "db url",
                    "db userName",
                    "db password"
            );

            String query = "SELECT name FROM students WHERE student_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, studentId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
            } else {
                nameLabel.setText("Name: Not Found");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            nameLabel.setText("Name: Error loading");
        }
    }

    // ================= BUTTON EVENTS =================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == refreshBtn) {
            repaint();
        }
        else if (e.getSource() == borrowHistory) {
            new BorrowBooksHistory(studentId);
        }
        else if (e.getSource() == borrowBooks) {
            new BorrowBookPage(studentId);
        }
        else if (e.getSource() == returnBooks) {
            new ReturnBookPage(studentId);
        }
        else if (e.getSource() == books) {
            new AllBooksPage();
        }
    }
}
