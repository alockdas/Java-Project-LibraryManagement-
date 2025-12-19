package src.BorrowBookPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BorrowBookPage extends JFrame implements ActionListener {

    JTextField bookIdField;
    JLabel bookNameLabel, qtyLabel;
    JButton searchBtn, borrowBtn;

    String studentId;
    int availableQty = 0;

    public BorrowBookPage(String studentId) {

        this.studentId = studentId; // ✅ studentId received safely

        setTitle("Borrow Book");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 15, 15));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        bookIdField.setFont(font);
        add(bookIdField);

        searchBtn = new JButton("Search Book");
        searchBtn.setFont(font);
        searchBtn.addActionListener(this);
        add(searchBtn);
        add(new JLabel());

        add(new JLabel("Book Name:"));
        bookNameLabel = new JLabel("-");
        bookNameLabel.setFont(font);
        add(bookNameLabel);

        add(new JLabel("Available Quantity:"));
        qtyLabel = new JLabel("-");
        qtyLabel.setFont(font);
        add(qtyLabel);

        borrowBtn = new JButton("Borrow Book");
        borrowBtn.setFont(font);
        borrowBtn.setEnabled(false);
        borrowBtn.addActionListener(this);
        add(borrowBtn);

        add(new JLabel());

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == searchBtn) {
            searchBook();
        }
        else if (e.getSource() == borrowBtn) {
            borrowBook();
        }
    }

    // 🔍 SEARCH BOOK
    private void searchBook() {

        String bookId = bookIdField.getText().trim();

        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Book ID!");
            return;
        }

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        String query = "SELECT book_name, quantity FROM allBooks WHERE book_id=?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, bookId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                bookNameLabel.setText(rs.getString("book_name"));
                availableQty = rs.getInt("quantity");
                qtyLabel.setText(String.valueOf(availableQty));
                borrowBtn.setEnabled(availableQty > 0);
            } else {
                JOptionPane.showMessageDialog(this, "Book not found!");
                clear();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // 📚 BORROW BOOK (FINAL FIX)
    private void borrowBook() {

        if (studentId == null || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student not logged in!");
            return;
        }

        if (availableQty <= 0) {
            JOptionPane.showMessageDialog(this, "Book not available!");
            return;
        }

        String bookId = bookIdField.getText().trim();

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            con.setAutoCommit(false);

            // 1️⃣ Reduce quantity
            String updateQty =
                    "UPDATE allBooks SET quantity = quantity - 1 WHERE book_id=?";
            PreparedStatement pst1 = con.prepareStatement(updateQty);
            pst1.setString(1, bookId);
            pst1.executeUpdate();

            // 2️⃣ Insert history
            String insertBorrow =
                    "INSERT INTO borrowBooksHistory (student_id, book_id, borrow_date) VALUES (?, ?, NOW())";
            PreparedStatement pst2 = con.prepareStatement(insertBorrow);
            pst2.setString(1, studentId);   // ✅ NO trim()
            pst2.setString(2, bookId);
            pst2.executeUpdate();

            con.commit();

            JOptionPane.showMessageDialog(this, "Book Borrowed Successfully!");
            clear();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Borrow failed: " + e.getMessage());
        }
    }

    private void clear() {
        bookNameLabel.setText("-");
        qtyLabel.setText("-");
        borrowBtn.setEnabled(false);
        availableQty = 0;
        bookIdField.setText("");
    }
}
