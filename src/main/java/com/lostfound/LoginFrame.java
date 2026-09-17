package com.lostfound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

    // =========================
    // COLORS
    // =========================
    private final Color BLUE = new Color(37, 99, 235);
    private final Color DARK_BLUE = new Color(30, 64, 175);
    private final Color LIGHT_BLUE = new Color(239, 246, 255);

    private final Color TEXT = new Color(31, 41, 55);
    private final Color GRAY = new Color(107, 114, 128);
    private final Color BORDER = new Color(220, 226, 235);

    private JTextField emailField;
    private JPasswordField passwordField;

    // =========================
    // CONSTRUCTOR
    // =========================
    public LoginFrame() {

        setTitle("Lost & Found Management System");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {
        }

        buildUI();

        setVisible(true);
    }

    // =========================
    // BUILD UI
    // =========================
    private void buildUI() {

        JPanel main = new JPanel(new GridLayout(1, 2));
        main.setBackground(Color.WHITE);

        // LEFT SIDE
        main.add(createLeftPanel());

        // RIGHT SIDE
        main.add(createLoginPanel());

        setContentPane(main);
    }

    // =========================
    // LEFT PANEL
    // =========================
    private JPanel createLeftPanel() {

        GradientPanel panel = new GradientPanel();

        panel.setLayout(new BorderLayout());
        panel.setBorder(
                new EmptyBorder(45, 45, 45, 45)
        );

        JPanel content = new JPanel();
        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // Logo circle
        JPanel logo = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                40
                        )
                );

                g2.fillOval(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                30
                        )
                );

                String text = "L&F";

                FontMetrics fm =
                        g2.getFontMetrics();

                int x =
                        (getWidth()
                                - fm.stringWidth(text))
                                / 2;

                int y =
                        (getHeight()
                                + fm.getAscent())
                                / 2 - 5;

                g2.drawString(
                        text,
                        x,
                        y
                );

                g2.dispose();
            }
        };

        logo.setOpaque(false);
        logo.setPreferredSize(
                new Dimension(85, 85)
        );
        logo.setMaximumSize(
                new Dimension(85, 85)
        );

        content.add(logo);

        content.add(
                Box.createVerticalStrut(35)
        );

        // Main title
        JLabel title =
                new JLabel(
                        "<html>Lost &amp; Found<br>Management System</html>"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        title.setForeground(Color.WHITE);

        content.add(title);

        content.add(
                Box.createVerticalStrut(18)
        );

        JLabel subtitle =
                new JLabel(
                        "<html>Find it. Report it.<br>Return it.</html>"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18
                )
        );

        subtitle.setForeground(
                new Color(
                        219,
                        234,
                        254
                )
        );

        content.add(subtitle);

        content.add(
                Box.createVerticalStrut(35)
        );

        // Features
        addFeature(
                content,
                "✓",
                "Report lost items"
        );

        addFeature(
                content,
                "✓",
                "Report found items"
        );

        addFeature(
                content,
                "✓",
                "Search and claim items"
        );

        addFeature(
                content,
                "✓",
                "Admin verification"
        );

        panel.add(
                content,
                BorderLayout.CENTER
        );

        // Bottom
        JLabel footer =
                new JLabel(
                        "A smart solution for campus lost & found"
                );

        footer.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        footer.setForeground(
                new Color(
                        219,
                        234,
                        254
                )
        );

        panel.add(
                footer,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================
    // FEATURE
    // =========================
    private void addFeature(
            JPanel panel,
            String icon,
            String text
    ) {

        JPanel row =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                8
                        )
                );

        row.setOpaque(false);

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        iconLabel.setForeground(
                Color.WHITE
        );

        JLabel textLabel =
                new JLabel(text);

        textLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        textLabel.setForeground(
                Color.WHITE
        );

        row.add(iconLabel);

        row.add(
                Box.createHorizontalStrut(12)
        );

        row.add(textLabel);

        panel.add(row);
    }

    // =========================
    // LOGIN PANEL
    // =========================
    private JPanel createLoginPanel() {

        JPanel background =
                new JPanel(
                        new GridBagLayout()
                );

        background.setBackground(
                new Color(
                        248,
                        250,
                        252
                )
        );

        RoundedPanel card =
                new RoundedPanel(
                        Color.WHITE
                );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                new EmptyBorder(
                        38,
                        45,
                        38,
                        45
                )
        );

        card.setPreferredSize(
                new Dimension(
                        390,
                        460
                )
        );

        // Login title
        JLabel title =
                new JLabel(
                        "Welcome Back!"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(TEXT);
        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(8)
        );

        JLabel subtitle =
                new JLabel(
                        "Login to continue to your account"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(GRAY);
        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(subtitle);

        card.add(
                Box.createVerticalStrut(30)
        );

        // EMAIL
        JLabel emailLabel =
                createLabel("Email Address");

        card.add(emailLabel);

        card.add(
                Box.createVerticalStrut(7)
        );

        emailField =
                createTextField();

        emailField.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(emailField);

        card.add(
                Box.createVerticalStrut(18)
        );

        // PASSWORD
        JLabel passwordLabel =
                createLabel("Password");

        card.add(passwordLabel);

        card.add(
                Box.createVerticalStrut(7)
        );

        passwordField =
                createPasswordField();

        passwordField.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(passwordField);

        card.add(
                Box.createVerticalStrut(10)
        );

        // Show password
        JCheckBox showPassword =
                new JCheckBox(
                        "Show password"
                );

        showPassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        showPassword.setForeground(GRAY);
        showPassword.setBackground(Color.WHITE);
        showPassword.setFocusPainted(false);
        showPassword.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        showPassword.addActionListener(e -> {

            if (showPassword.isSelected()) {

                passwordField.setEchoChar(
                        (char) 0
                );

            } else {

                passwordField.setEchoChar('•');
            }
        });

        card.add(showPassword);

        card.add(
                Box.createVerticalStrut(22)
        );

        // LOGIN BUTTON
        JButton loginButton =
                createButton(
                        "LOGIN",
                        BLUE
                );

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(loginButton);

        loginButton.addActionListener(
                e -> login()
        );

        card.add(
                Box.createVerticalStrut(18)
        );

        // Divider
        JPanel divider =
                new JPanel(
                        new BorderLayout()
                );

        divider.setBackground(
                        Color.WHITE
                );

        divider.setMaximumSize(
                new Dimension(
                        300,
                        1
                )
        );

        JSeparator separator =
                new JSeparator();

        separator.setForeground(BORDER);

        divider.add(
                separator,
                BorderLayout.CENTER
        );

        divider.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(divider);

        card.add(
                Box.createVerticalStrut(18)
        );

        // Register text
        JPanel registerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                5,
                                0
                        )
                );

        registerPanel.setBackground(
                Color.WHITE
        );

        registerPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel noAccount =
                new JLabel(
                        "Don't have an account?"
                );

        noAccount.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        noAccount.setForeground(GRAY);

        JButton register =
                new JButton(
                        "Register"
                );

        register.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        register.setForeground(BLUE);
        register.setBackground(Color.WHITE);
        register.setBorderPainted(false);
        register.setFocusPainted(false);
        register.setContentAreaFilled(false);
        register.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        register.addActionListener(
                e -> showRegisterDialog()
        );

        registerPanel.add(noAccount);
        registerPanel.add(register);

        card.add(registerPanel);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        background.add(
                card,
                gbc
        );

        return background;
    }

    // =========================
    // LABEL
    // =========================
    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(TEXT);

        return label;
    }

    // =========================
    // TEXT FIELD
    // =========================
    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setBorder(
                new javax.swing.border.CompoundBorder(
                        new javax.swing.border.LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                5,
                                12,
                                5,
                                12
                        )
                )
        );

        field.setBackground(
                new Color(
                        249,
                        250,
                        251
                )
        );

        return field;
    }

    // =========================
    // PASSWORD FIELD
    // =========================
    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setEchoChar('•');

        field.setBorder(
                new javax.swing.border.CompoundBorder(
                        new javax.swing.border.LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                5,
                                12,
                                5,
                                12
                        )
                )
        );

        field.setBackground(
                new Color(
                        249,
                        250,
                        251
                )
        );

        return field;
    }

    // =========================
    // BUTTON
    // =========================
    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        if (getModel().isPressed()) {

                            g2.setColor(
                                    DARK_BLUE
                            );

                        } else if (
                                getModel().isRollover()
                        ) {

                            g2.setColor(
                                    DARK_BLUE
                            );

                        } else {

                            g2.setColor(color);
                        }

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                12,
                                12
                        );

                        g2.setColor(
                                Color.WHITE
                        );

                        g2.setFont(
                                getFont()
                        );

                        FontMetrics fm =
                                g2.getFontMetrics();

                        int x =
                                (getWidth()
                                        - fm.stringWidth(
                                        getText()
                                )) / 2;

                        int y =
                                (getHeight()
                                        + fm.getAscent()
                                        - fm.getDescent()
                                ) / 2;

                        g2.drawString(
                                getText(),
                                x,
                                y
                        );

                        g2.dispose();
                    }
                };

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setPreferredSize(
                new Dimension(
                        300,
                        45
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================
    // LOGIN FUNCTION
    // =========================
    private void login() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your email.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();
            return;
        }

        String sql = """
                SELECT user_id, name, role
                FROM users
                WHERE email = ?
                AND password = ?
                """;

        try (
                Connection con =
                        DB.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                int userId =
                        rs.getInt("user_id");

                String name =
                        rs.getString("name");

                String role =
                        rs.getString("role");

                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!\nWelcome, "
                                + name + ".",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new DashboardFrame(
                        userId,
                        name,
                        role
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                passwordField.setText("");
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database connection error:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // REGISTER DIALOG
    // =========================
    private void showRegisterDialog() {

        JDialog dialog =
                new JDialog(
                        this,
                        "Create Account",
                        true
                );

        dialog.setSize(
                430,
                550
        );

        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                new EmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Create Account"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(TEXT);
        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(title);

        JLabel subtitle =
                new JLabel(
                        "Register to use the Lost & Found system"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(GRAY);
        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        panel.add(subtitle);

        panel.add(
                Box.createVerticalStrut(22)
        );

        // Name
        panel.add(
                createLabel("Full Name")
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        JTextField nameField =
                createTextField();

        panel.add(nameField);

        panel.add(
                Box.createVerticalStrut(13)
        );

        // Email
        panel.add(
                createLabel("Email Address")
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        JTextField regEmail =
                createTextField();

        panel.add(regEmail);

        panel.add(
                Box.createVerticalStrut(13)
        );

        // Phone
        panel.add(
                createLabel("Phone Number")
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        JTextField phoneField =
                createTextField();

        panel.add(phoneField);

        panel.add(
                Box.createVerticalStrut(13)
        );

        // Password
        panel.add(
                createLabel("Password")
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        JPasswordField regPassword =
                createPasswordField();

        panel.add(regPassword);

        panel.add(
                Box.createVerticalStrut(20)
        );

        JButton registerButton =
                createButton(
                        "CREATE ACCOUNT",
                        BLUE
                );

        panel.add(registerButton);

        registerButton.addActionListener(e -> {

            String name =
                    nameField.getText().trim();

            String email =
                    regEmail.getText().trim();

            String phone =
                    phoneField.getText().trim();

            String password =
                    new String(
                            regPassword.getPassword()
                    );

            if (name.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please fill all fields.",
                        "Registration",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (!email.contains("@")) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a valid email.",
                        "Registration",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (phone.length() < 10) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a valid phone number.",
                        "Registration",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String sql = """
                    INSERT INTO users
                    (name, email, phone, password, role)
                    VALUES (?, ?, ?, ?, 'USER')
                    """;

            try (
                    Connection con =
                            DB.getConnection();

                    PreparedStatement ps =
                            con.prepareStatement(sql)
            ) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, password);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        dialog,
                        "Registration successful!\n"
                                + "You can now login.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialog.dispose();

                emailField.setText(email);
                passwordField.setText("");

            } catch (SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "This email is already registered.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Registration error:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dialog.setContentPane(panel);

        dialog.setVisible(true);
    }

    // =========================
    // ROUNDED PANEL
    // =========================
    private static class RoundedPanel
            extends JPanel {

        private final Color background;

        RoundedPanel(Color background) {

            this.background = background;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(background);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    25,
                    25
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================
    // GRADIENT PANEL
    // =========================
    private static class GradientPanel
            extends JPanel {

        GradientPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    37,
                                    99,
                                    235
                            ),
                            getWidth(),
                            getHeight(),
                            new Color(
                                    30,
                                    64,
                                    175
                            )
                    );

            g2.setPaint(gradient);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            // Decorative circles
            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            20
                    )
            );

            g2.fillOval(
                    getWidth() - 150,
                    -80,
                    220,
                    220
            );

            g2.fillOval(
                    -100,
                    getHeight() - 120,
                    230,
                    230
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}