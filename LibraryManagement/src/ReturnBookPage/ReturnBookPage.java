package src.ReturnBookPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReturnBookPage extends JFrame implements ActionListener {

    JTextField bookIdField;
    JButton returnBtn;

    String studentId;

    public ReturnBookPage(String studentId) {

        this.studentId = studentId;

        setTitle("Return Book");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 15, 15));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        bookIdField.setFont(font);
        add(bookIdField);

        returnBtn = new JButton("Return Book");
        returnBtn.setFont(font);
        returnBtn.addActionListener(this);
        add(returnBtn);

        add(new JLabel());

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnBtn) {
            returnBook();
        }
    }

    // 🔁 RETURN BOOK LOGIC
    private void returnBook() {

        String bookId = bookIdField.getText().trim();

        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Book ID!");
            return;
        }

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            con.setAutoCommit(false);

            // 1️⃣ Check borrowed & not returned
            String checkQuery = """
                SELECT id FROM borrowBooksHistory
                WHERE student_id=? AND book_id=? AND status='Borrowed'
            """;

            PreparedStatement checkPst = con.prepareStatement(checkQuery);
            checkPst.setString(1, studentId);
            checkPst.setString(2, bookId);

            ResultSet rs = checkPst.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "This book is not borrowed or already returned!");
                return;
            }

            int historyId = rs.getInt("id");

            // 2️⃣ Update history → Returned
            String updateHistory = """
                UPDATE borrowBooksHistory
                SET status='Returned'
                WHERE id=?
            """;
            PreparedStatement pst1 = con.prepareStatement(updateHistory);
            pst1.setInt(1, historyId);
            pst1.executeUpdate();

            // 3️⃣ Increase book quantity
            String updateQty =
                    "UPDATE allBooks SET quantity = quantity + 1 WHERE book_id=?";
            PreparedStatement pst2 = con.prepareStatement(updateQty);
            pst2.setString(1, bookId);
            pst2.executeUpdate();

            con.commit();

            JOptionPane.showMessageDialog(this, "Book Returned Successfully!");
            bookIdField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Return failed: " + e.getMessage());
        }
    }
}

