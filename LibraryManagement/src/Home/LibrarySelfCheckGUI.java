package src.Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.LoginSignUpPage.LoginSignUpPage;

public class LibrarySelfCheckGUI extends JFrame implements ActionListener {

    // Buttons
    JButton proceedBtn, refreshBtn, resetBtn;
    JButton borrowHistory, borrowBooks, returnBooks;

    Dimension buttonSize = new Dimension(220, 80);

    public LibrarySelfCheckGUI() {

        JFrame frame = new JFrame("LIBRARY MANAGEMENT SYSTEM");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== BACKGROUND PANEL (ONLY ADDITION) =====
        BackgroundPanel bgPanel = new BackgroundPanel("src/assets/bg.png");
        bgPanel.setLayout(new BorderLayout());
        frame.setContentPane(bgPanel);

        // ================= TOP AREA =================
        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setOpaque(false);

        JLabel title = new JLabel("Self Check-In & Check-Out", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        title.setForeground(Color.WHITE);
        topArea.add(title, BorderLayout.NORTH);

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 25));
        topButtonPanel.setOpaque(false);

        proceedBtn = createIconButton("Proceed", "src/assets/proceed.png");
        refreshBtn = createIconButton("Refresh", "src/assets/refresh.png");
        resetBtn   = createIconButton("Reset",   "src/assets/reset.png");

        proceedBtn.addActionListener(this);
        refreshBtn.addActionListener(this);
        resetBtn.addActionListener(this);

        topButtonPanel.add(proceedBtn);
        topButtonPanel.add(refreshBtn);
        topButtonPanel.add(resetBtn);

        topArea.add(topButtonPanel, BorderLayout.CENTER);
        frame.add(topArea, BorderLayout.NORTH);

        // ================= CENTER AREA =================
        JPanel centerOptions = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 60));
        centerOptions.setOpaque(false);

        borrowHistory = createIconButton("Borrow History", "src/assets/history.png");
        borrowBooks   = createIconButton("Borrow Books",   "src/assets/borrow.png");
        returnBooks   = createIconButton("Return Books",   "src/assets/return.png");

        borrowHistory.addActionListener(this);
        borrowBooks.addActionListener(this);
        returnBooks.addActionListener(this);

        centerOptions.add(borrowHistory);
        centerOptions.add(borrowBooks);
        centerOptions.add(returnBooks);

        frame.add(centerOptions, BorderLayout.CENTER);

        // ================= INITIAL STATES =================
        borrowHistory.setEnabled(false);
        borrowBooks.setEnabled(false);
        returnBooks.setEnabled(false);
        refreshBtn.setEnabled(false);

        frame.setVisible(true);
    }

    // ================= BUTTON CREATOR =================
    private JButton createIconButton(String text, String iconPath) {

        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton button = new JButton(text, icon);
        button.setPreferredSize(buttonSize);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.setFocusPainted(false);
        button.setBackground(new Color(245, 245, 245, 230));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return button;
    }

    // ================= ACTION EVENTS =================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == proceedBtn) {
            new LoginSignUpPage();
            borrowHistory.setEnabled(true);
            borrowBooks.setEnabled(true);
            returnBooks.setEnabled(true);
            refreshBtn.setEnabled(true);
        }
        else if (e.getSource() == refreshBtn) {
            // refresh logic
        }
        else if (e.getSource() == resetBtn) {
            dispose();
            new LibrarySelfCheckGUI();
        }
        else if (e.getSource() == borrowHistory) {
            JOptionPane.showMessageDialog(this, "Borrow History Clicked");
        }
        else if (e.getSource() == borrowBooks) {
            JOptionPane.showMessageDialog(this, "Borrow Books Clicked");
        }
        else if (e.getSource() == returnBooks) {
            JOptionPane.showMessageDialog(this, "Return Books Clicked");
        }
    }

    // ================= BACKGROUND PANEL =================
    class BackgroundPanel extends JPanel {

        private Image bgImage;

        public BackgroundPanel(String path) {
            bgImage = new ImageIcon(path).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
