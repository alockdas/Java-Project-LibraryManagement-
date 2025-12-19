package src.BorrowBooksHistory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BorrowBooksHistory extends JFrame {

    JTable historyTable;
    DefaultTableModel model;
    String studentId;

    public BorrowBooksHistory(String studentId) {

        this.studentId = studentId; // ✅ store safely

        setTitle("Borrow Books History");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("My Borrow History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // -------- TABLE MODEL --------
        model = new DefaultTableModel();
        model.addColumn("Student ID");
        model.addColumn("Book ID");
        model.addColumn("Book Name");
        model.addColumn("Borrow Date");
        model.addColumn("Status"); // ✅ Added

        historyTable = new JTable(model);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.setRowHeight(26);

        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        loadBorrowHistory(); // load data

        setVisible(true);
    }

    // 📜 LOAD BORROW HISTORY
    private void loadBorrowHistory() {

        // ✅ SAFETY CHECK
        if (studentId == null || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student not logged in!");
            return;
        }

        model.setRowCount(0);

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        String query = """
            SELECT h.student_id,
                   h.book_id,
                   b.book_name,
                   h.borrow_date,
                   h.status
            FROM borrowBooksHistory h
            LEFT JOIN allBooks b ON h.book_id = b.book_id
            WHERE h.student_id = ?
            ORDER BY h.borrow_date DESC
        """;

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("student_id"),
                        rs.getString("book_id"),
                        rs.getString("book_name"),
                        rs.getTimestamp("borrow_date"),
                        rs.getString("status") // ✅ THIS WAS MISSING
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading history: " + e.getMessage());
        }
    }
}
