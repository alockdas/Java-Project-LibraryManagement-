package src.AllBooksPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AllBooksPage extends JFrame implements ActionListener {

    JTextField searchField;
    JButton searchBtn, refreshBtn;
    JTable bookTable;
    DefaultTableModel model;

    public AllBooksPage() {

        setTitle("All Books");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---------------- TOP PANEL ----------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JLabel searchLabel = new JLabel("Search Book:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        searchBtn = new JButton("Search");
        refreshBtn = new JButton("Refresh");

        searchBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        searchBtn.addActionListener(this);
        refreshBtn.addActionListener(this);

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- TABLE ----------------
        model = new DefaultTableModel();
        model.addColumn("Book ID");
        model.addColumn("Book Name");
        model.addColumn("Quantity");

        bookTable = new JTable(model);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        bookTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load all books
        loadAllBooks();

        setVisible(true);
    }

    // ---------------- LOAD ALL BOOKS ----------------
    private void loadAllBooks() {
        model.setRowCount(0);

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        String query = "SELECT * FROM allBooks";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    // ---------------- SEARCH BOOK ----------------
    private void searchBook() {
        model.setRowCount(0);

        String keyword = searchField.getText();

        String url = "db url";
        String user = "db userName";
        String pass = "db password";

        String query = "SELECT * FROM allBooks WHERE book_id LIKE ? OR book_name LIKE ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    // ---------------- BUTTON EVENTS ----------------
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == searchBtn) {
            searchBook();
        }
        else if (e.getSource() == refreshBtn) {
            searchField.setText("");
            loadAllBooks();
        }
    }
}
