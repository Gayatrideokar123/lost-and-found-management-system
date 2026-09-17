package com.lostfound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DashboardFrame extends JFrame {

    // =========================
    // PROFESSIONAL COLOR THEME
    // =========================
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color PRIMARY_DARK = new Color(29, 78, 216);
    private final Color SECONDARY = new Color(14, 165, 233);
    private final Color SUCCESS = new Color(22, 163, 74);
    private final Color DANGER = new Color(220, 38, 38);
    private final Color WARNING = new Color(234, 179, 8);

    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color TEXT_LIGHT = new Color(107, 114, 128);
    private final Color BORDER = new Color(229, 231, 235);

    private int uid;
    private String uname;
    private String role;

    private JTabbedPane tabs;

    // =========================
    // CONSTRUCTOR
    // =========================
    public DashboardFrame(int uid, String uname, String role) {

        this.uid = uid;
        this.uname = uname;
        this.role = role;

        setTitle("Lost & Found Management System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {
        }

        getContentPane().setBackground(BACKGROUND);

        buildUI();

        setVisible(true);
    }

    // =========================
    // MAIN UI
    // =========================
    private void buildUI() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        // Top header
        main.add(createHeader(), BorderLayout.NORTH);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(TEXT);

        tabs.addTab("  Dashboard  ", dashboard());
        tabs.addTab("  Report Lost  ", itemForm(false));
        tabs.addTab("  Report Found  ", itemForm(true));
        tabs.addTab("  Search  ", search());
        tabs.addTab("  My Claims  ", myClaims());

        if ("ADMIN".equalsIgnoreCase(role)) {
            tabs.addTab("  Admin  ", admin());
        }

        tabs.addTab("  Reports  ", reports());

        main.add(tabs, BorderLayout.CENTER);

        setContentPane(main);
    }

    // =========================
    // HEADER
    // =========================
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(16, 25, 16, 25));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("LOST & FOUND");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Management System");
        subtitle.setForeground(new Color(219, 234, 254));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(title);
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.setOpaque(false);

        JLabel user = new JLabel("Welcome, " + uname);
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel roleLabel = new JLabel(role);
        roleLabel.setForeground(PRIMARY);
        roleLabel.setBackground(Color.WHITE);
        roleLabel.setOpaque(true);
        roleLabel.setBorder(new EmptyBorder(6, 12, 6, 12));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        right.add(user);
        right.add(roleLabel);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // =========================
    // DASHBOARD
    // =========================
    private JPanel dashboard() {

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Welcome card
        JPanel welcome = createCard();

        welcome.setLayout(new BorderLayout());

        JPanel welcomeText = new JPanel();
        welcomeText.setOpaque(false);
        welcomeText.setLayout(new BoxLayout(welcomeText, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome back, " + uname + "!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel desc = new JLabel(
                "Manage lost and found items easily from one place."
        );
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setForeground(TEXT_LIGHT);

        welcomeText.add(title);
        welcomeText.add(Box.createVerticalStrut(8));
        welcomeText.add(desc);

        welcome.add(welcomeText, BorderLayout.CENTER);

        // Statistics
        JPanel stats = new JPanel(new GridLayout(1, 4, 18, 0));
        stats.setOpaque(false);

        stats.add(statCard(
                "Lost Items",
                String.valueOf(count("SELECT COUNT(*) FROM lost_items")),
                PRIMARY
        ));

        stats.add(statCard(
                "Found Items",
                String.valueOf(count("SELECT COUNT(*) FROM found_items")),
                SECONDARY
        ));

        stats.add(statCard(
                "My Claims",
                String.valueOf(count(
                        "SELECT COUNT(*) FROM claims WHERE user_id=" + uid
                )),
                WARNING
        ));

        stats.add(statCard(
                "Approved",
                String.valueOf(count(
                        "SELECT COUNT(*) FROM claims WHERE status='Approved'"
                )),
                SUCCESS
        ));

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);

        center.add(welcome, BorderLayout.NORTH);
        center.add(stats, BorderLayout.CENTER);

        // Quick actions
        JPanel actions = createCard();
        actions.setLayout(new BorderLayout(10, 10));

        JLabel actionTitle = new JLabel("Quick Actions");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        actionTitle.setForeground(TEXT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttons.setOpaque(false);

        JButton lost = modernButton("＋  Report Lost Item", PRIMARY);
        JButton found = modernButton("＋  Report Found Item", SUCCESS);
        JButton searchBtn = modernButton("⌕  Search Items", SECONDARY);

        lost.addActionListener(e -> tabs.setSelectedIndex(1));
        found.addActionListener(e -> tabs.setSelectedIndex(2));
        searchBtn.addActionListener(e -> tabs.setSelectedIndex(3));

        buttons.add(lost);
        buttons.add(found);
        buttons.add(searchBtn);

        actions.add(actionTitle, BorderLayout.NORTH);
        actions.add(buttons, BorderLayout.CENTER);

        center.add(actions, BorderLayout.SOUTH);

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    // =========================
    // STAT CARD
    // =========================
    private JPanel statCard(String title, String value, Color color) {

        JPanel card = createCard();
        card.setLayout(new BorderLayout());

        JPanel stripe = new JPanel();
        stripe.setBackground(color);
        stripe.setPreferredSize(new Dimension(6, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(10, 15, 10, 10));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valueLabel.setForeground(TEXT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_LIGHT);

        content.add(valueLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(titleLabel);

        card.add(stripe, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // =========================
    // REPORT LOST / FOUND
    // =========================
    private JPanel itemForm(boolean found) {

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BACKGROUND);
        outer.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel card = createCard();
        card.setLayout(new BorderLayout(15, 15));

        JLabel heading = new JLabel(
                found ? "Report a Found Item" : "Report a Lost Item"
        );

        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(TEXT);

        JLabel sub = new JLabel(
                found
                        ? "Enter the details of the item you found."
                        : "Enter the details of the item you lost."
        );

        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(TEXT_LIGHT);

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(
                headingPanel,
                BoxLayout.Y_AXIS
        ));

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(sub);

        card.add(headingPanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField itemName = textField();
        JComboBox<ItemCombo> category = combo("categories");
        JComboBox<ItemCombo> location = combo("locations");
        JTextField date = textField();

        date.setText(new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date()));

        JTextArea description = new JTextArea(5, 30);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane descScroll = new JScrollPane(description);
        descScroll.setBorder(new LineBorder(BORDER, 1, true));

        addFormRow(form, gbc, 0, "Item Name", itemName);
        addFormRow(form, gbc, 1, "Category", category);
        addFormRow(form, gbc, 2, "Location", location);
        addFormRow(form, gbc, 3, "Date", date);
        addFormRow(form, gbc, 4, "Description", descScroll);

        JButton submit = modernButton(
                found ? "✓  Submit Found Item" : "✓  Submit Lost Item",
                found ? SUCCESS : PRIMARY
        );

        submit.addActionListener(e -> {

            if (itemName.getText().trim().isEmpty()) {
                err("Please enter item name.");
                return;
            }

            if (category.getSelectedItem() == null) {
                err("Please select category.");
                return;
            }

            if (location.getSelectedItem() == null) {
                err("Please select location.");
                return;
            }

            if (date.getText().trim().isEmpty()) {
                err("Please enter date.");
                return;
            }

            try {

                ItemCombo cat =
                        (ItemCombo) category.getSelectedItem();

                ItemCombo loc =
                        (ItemCombo) location.getSelectedItem();

                String sql;

                if (found) {

                    sql = """
                            INSERT INTO found_items
                            (user_id, item_name, category_id, location_id,
                             description, found_date, status)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            """;

                } else {

                    sql = """
                            INSERT INTO lost_items
                            (user_id, item_name, category_id, location_id,
                             description, lost_date, status)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            """;
                }

                try (Connection con = DB.getConnection();
                     PreparedStatement ps =
                             con.prepareStatement(sql)) {

                    ps.setInt(1, uid);
                    ps.setString(2, itemName.getText().trim());
                    ps.setInt(3, cat.id);
                    ps.setInt(4, loc.id);
                    ps.setString(5, description.getText().trim());
                    ps.setDate(6, java.sql.Date.valueOf(date.getText().trim()));

                    ps.setString(
                            7,
                            found ? "Available" : "Lost"
                    );

                    ps.executeUpdate();

                    msg(
                            found
                                    ? "Found item reported successfully!"
                                    : "Lost item reported successfully!"
                    );

                    itemName.setText("");
                    description.setText("");

                }

            } catch (Exception ex) {
                err(ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(
                FlowLayout.RIGHT
        ));
        bottom.setOpaque(false);

        bottom.add(submit);

        card.add(form, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        outer.add(card, BorderLayout.CENTER);

        return outer;
    }

    // =========================
    // SEARCH
    // =========================
    private JPanel search() {

        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BACKGROUND);
        outer.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchCard = createCard();
        searchCard.setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("Search Lost & Found Items");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(TEXT);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);

        JTextField query = textField();
        query.setPreferredSize(new Dimension(400, 42));

        JButton searchBtn = modernButton("⌕  Search", PRIMARY);

        top.add(query, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        header.add(heading, BorderLayout.NORTH);
        header.add(top, BorderLayout.CENTER);

        searchCard.add(header, BorderLayout.NORTH);

        String[] columns = {
                "Type",
                "ID",
                "Item",
                "Category",
                "Location",
                "Date",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable table = styledTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER, 1));

        searchCard.add(scroll, BorderLayout.CENTER);

        JButton claim = modernButton(
                "✓  Claim Selected Found Item",
                SUCCESS
        );

        JPanel bottom = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        bottom.setOpaque(false);
        bottom.add(claim);

        searchCard.add(bottom, BorderLayout.SOUTH);

        searchBtn.addActionListener(e ->
                performSearch(query.getText(), model)
        );

        claim.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                err("Please select a found item.");
                return;
            }

            String type = model.getValueAt(row, 0).toString();
            String id = model.getValueAt(row, 1).toString();
            String status = model.getValueAt(row, 6).toString();

            if (!type.equals("FOUND")) {
                err("Only found items can be claimed.");
                return;
            }

            if (!status.equalsIgnoreCase("Available")) {
                err("This item is not available for claiming.");
                return;
            }

            try {

                String sql = """
                        INSERT INTO claims
                        (user_id, found_id, status)
                        VALUES (?, ?, 'Pending')
                        """;

                try (Connection con = DB.getConnection();
                     PreparedStatement ps =
                             con.prepareStatement(sql)) {

                    ps.setInt(1, uid);
                    ps.setInt(2, Integer.parseInt(id));

                    ps.executeUpdate();

                    msg("Claim submitted successfully!");

                    loadClaims();

                }

            } catch (Exception ex) {
                err(ex.getMessage());
            }
        });

        outer.add(searchCard, BorderLayout.CENTER);

        return outer;
    }

    // =========================
    // SEARCH FUNCTION
    // =========================
    private void performSearch(
            String keyword,
            DefaultTableModel model
    ) {

        model.setRowCount(0);

        if (keyword == null) {
            keyword = "";
        }

        keyword = keyword.trim();

        String sql = """
                SELECT
                    'FOUND' AS type,
                    f.found_id AS id,
                    f.item_name,
                    c.category_name,
                    l.location_name,
                    f.found_date,
                    f.status
                FROM found_items f
                JOIN categories c
                    ON f.category_id = c.category_id
                JOIN locations l
                    ON f.location_id = l.location_id
                WHERE f.item_name LIKE ?
                   OR c.category_name LIKE ?
                   OR l.location_name LIKE ?

                UNION ALL

                SELECT
                    'LOST' AS type,
                    li.lost_id AS id,
                    li.item_name,
                    c.category_name,
                    l.location_name,
                    li.lost_date,
                    li.status
                FROM lost_items li
                JOIN categories c
                    ON li.category_id = c.category_id
                JOIN locations l
                    ON li.location_id = l.location_id
                WHERE li.item_name LIKE ?
                   OR c.category_name LIKE ?
                   OR l.location_name LIKE ?
                """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            String q = "%" + keyword + "%";

            for (int i = 1; i <= 6; i++) {
                ps.setString(i, q);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getString("type"),
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("category_name"),
                        rs.getString("location_name"),
                        rs.getDate(6),
                        rs.getString("status")
                });
            }

        } catch (Exception ex) {
            err(ex.getMessage());
        }
    }

    // =========================
    // MY CLAIMS
    // =========================
    private JPanel myClaims() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel card = createCard();
        card.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("My Claims");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        card.add(title, BorderLayout.NORTH);

        String[] columns = {
                "Claim ID",
                "Item",
                "Status",
                "Claim Date"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    public boolean isCellEditable(
                            int r,
                            int c
                    ) {
                        return false;
                    }
                };

        JTable table = styledTable(model);

        card.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        panel.add(card);

        loadClaimsInto(model);

        return panel;
    }

    private void loadClaims() {
        // Refresh currently selected claims tab
    }

    private void loadClaimsInto(
            DefaultTableModel model
    ) {

        String sql = """
                SELECT
                    c.claim_id,
                    f.item_name,
                    c.status,
                    c.claim_date
                FROM claims c
                JOIN found_items f
                    ON c.found_id = f.found_id
                WHERE c.user_id = ?
                ORDER BY c.claim_id DESC
                """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, uid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("claim_id"),
                        rs.getString("item_name"),
                        rs.getString("status"),
                        rs.getTimestamp("claim_date")
                });
            }

        } catch (Exception ex) {
            err(ex.getMessage());
        }
    }

    // =========================
    // ADMIN
    // =========================
    private JPanel admin() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel card = createCard();
        card.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("Admin - Claim Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        card.add(title, BorderLayout.NORTH);

        String[] columns = {
                "Claim ID",
                "User",
                "Item",
                "Status",
                "Claim Date"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    public boolean isCellEditable(
                            int r,
                            int c
                    ) {
                        return false;
                    }
                };

        JTable table = styledTable(model);

        loadAdminClaims(model);

        card.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );

        buttons.setOpaque(false);

        JButton approve =
                modernButton("✓  Approve", SUCCESS);

        JButton reject =
                modernButton("✕  Reject", DANGER);

        buttons.add(approve);
        buttons.add(reject);

        approve.addActionListener(e ->
                changeClaimStatus(
                        table,
                        model,
                        "Approved"
                )
        );

        reject.addActionListener(e ->
                changeClaimStatus(
                        table,
                        model,
                        "Rejected"
                )
        );

        card.add(buttons, BorderLayout.SOUTH);

        panel.add(card);

        return panel;
    }

    private void loadAdminClaims(
            DefaultTableModel model
    ) {

        String sql = """
                SELECT
                    c.claim_id,
                    u.name,
                    f.item_name,
                    c.status,
                    c.claim_date
                FROM claims c
                JOIN users u
                    ON c.user_id = u.user_id
                JOIN found_items f
                    ON c.found_id = f.found_id
                ORDER BY c.claim_id DESC
                """;

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("claim_id"),
                        rs.getString("name"),
                        rs.getString("item_name"),
                        rs.getString("status"),
                        rs.getTimestamp("claim_date")
                });
            }

        } catch (Exception ex) {
            err(ex.getMessage());
        }
    }

    private void changeClaimStatus(
            JTable table,
            DefaultTableModel model,
            String status
    ) {

        int row = table.getSelectedRow();

        if (row == -1) {
            err("Please select a claim.");
            return;
        }

        int claimId =
                Integer.parseInt(
                        model.getValueAt(row, 0).toString()
                );

        String sql = """
                UPDATE claims
                SET status = ?
                WHERE claim_id = ?
                """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, claimId);

            ps.executeUpdate();

            model.setValueAt(status, row, 3);

            msg("Claim status changed to " + status);

        } catch (Exception ex) {
            err(ex.getMessage());
        }
    }

    // =========================
    // REPORTS
    // =========================
    private JPanel reports() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel card = createCard();
        card.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("System Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        card.add(title, BorderLayout.NORTH);

        JPanel stats = new JPanel(
                new GridLayout(2, 3, 15, 15)
        );

        stats.setOpaque(false);

        stats.add(reportBox(
                "Total Users",
                count("SELECT COUNT(*) FROM users")
        ));

        stats.add(reportBox(
                "Lost Items",
                count("SELECT COUNT(*) FROM lost_items")
        ));

        stats.add(reportBox(
                "Found Items",
                count("SELECT COUNT(*) FROM found_items")
        ));

        stats.add(reportBox(
                "Total Claims",
                count("SELECT COUNT(*) FROM claims")
        ));

        stats.add(reportBox(
                "Approved Claims",
                count("""
                        SELECT COUNT(*)
                        FROM claims
                        WHERE status='Approved'
                        """)
        ));

        stats.add(reportBox(
                "Pending Claims",
                count("""
                        SELECT COUNT(*)
                        FROM claims
                        WHERE status='Pending'
                        """)
        ));

        card.add(stats, BorderLayout.CENTER);

        panel.add(card);

        return panel;
    }

    private JPanel reportBox(
            String title,
            int value
    ) {

        JPanel panel = createCard();
        panel.setLayout(new BorderLayout());

        JLabel number = new JLabel(
                String.valueOf(value),
                SwingConstants.CENTER
        );

        number.setFont(
                new Font("Segoe UI", Font.BOLD, 32)
        );

        number.setForeground(PRIMARY);

        JLabel label = new JLabel(
                title,
                SwingConstants.CENTER
        );

        label.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        label.setForeground(TEXT_LIGHT);

        panel.add(number, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);

        return panel;
    }

    // =========================
    // DATABASE COUNT
    // =========================
    private int count(String sql) {

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception ignored) {
        }

        return 0;
    }

    // =========================
    // CATEGORY / LOCATION COMBO
    // =========================
    private JComboBox<ItemCombo> combo(
            String table
    ) {

        JComboBox<ItemCombo> box =
                new JComboBox<>();

        String idColumn;
        String nameColumn;

        if (table.equals("categories")) {

            idColumn = "category_id";
            nameColumn = "category_name";

        } else {

            idColumn = "location_id";
            nameColumn = "location_name";
        }

        String sql =
                "SELECT " + idColumn + ", " +
                        nameColumn +
                        " FROM " + table +
                        " ORDER BY " + nameColumn;

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                box.addItem(
                        new ItemCombo(
                                rs.getInt(1),
                                rs.getString(2)
                        )
                );
            }

        } catch (Exception ex) {
            err(ex.getMessage());
        }

        box.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        box.setBackground(Color.WHITE);

        return box;
    }

    // =========================
    // FORM ROW
    // =========================
    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            Component component
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);

        lbl.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        lbl.setForeground(TEXT);

        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(component, gbc);
    }

    // =========================
    // TEXT FIELD
    // =========================
    private JTextField textField() {

        JTextField field = new JTextField();

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        field.setPreferredSize(
                new Dimension(300, 40)
        );

        field.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                5, 10, 5, 10
                        )
                )
        );

        field.setBackground(Color.WHITE);

        return field;
    }

    // =========================
    // MODERN BUTTON
    // =========================
    private JButton modernButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                new EmptyBorder(
                        11, 20, 11, 20
                )
        );

        return button;
    }

    // =========================
    // CARD
    // =========================
    private JPanel createCard() {

        JPanel panel = new JPanel();

        panel.setBackground(CARD);

        panel.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        return panel;
    }

    // =========================
    // STYLED TABLE
    // =========================
    private JTable styledTable(
            DefaultTableModel model
    ) {

        JTable table = new JTable(model);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        table.setRowHeight(32);

        table.setShowGrid(false);
        table.setIntercellSpacing(
                new Dimension(0, 0)
        );

        table.setSelectionBackground(
                new Color(219, 234, 254)
        );

        table.setSelectionForeground(TEXT);

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        table.getTableHeader().setBackground(
                new Color(239, 246, 255)
        );

        table.getTableHeader().setForeground(
                TEXT
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();

        renderer.setBorder(
                new EmptyBorder(0, 8, 0, 8)
        );

        table.setDefaultRenderer(
                Object.class,
                renderer
        );

        return table;
    }

    // =========================
    // ITEM COMBO CLASS
    // =========================
    private static class ItemCombo {

        int id;
        String name;

        ItemCombo(
                int id,
                String name
        ) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // =========================
    // SUCCESS MESSAGE
    // =========================
    private void msg(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================
    // ERROR MESSAGE
    // =========================
    private void err(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}