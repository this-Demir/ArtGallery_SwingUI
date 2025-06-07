package app.ui;

import app.Data.DBConnector;
import app.models.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerRedirectButton; // Register'a yönlendiren buton
    private JButton goToHomePageButton; // HomePage'e gitmek için buton

    public LoginPage() {
        setTitle("Login");
        setSize(900, 900); // Resize window for better space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow resizing

        // Main panel for form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better control over alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Space between components

        panel.setBackground(new Color(255, 255, 255)); // White background

        // Add form fields
        addFormField(panel, gbc, "Email:", emailField = new JTextField());
        addFormField(panel, gbc, "Password:", passwordField = new JPasswordField());

        // Login button with modern design
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));  // Blue background
        loginButton.setForeground(Color.WHITE);  // White text
        loginButton.setFocusPainted(false);  // Remove focus border
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(200, 50));  // Button size

        // Register redirect button
        registerRedirectButton = new JButton("Don't have an account? Register");
        registerRedirectButton.setBackground(new Color(255, 255, 255));  // White background
        registerRedirectButton.setForeground(new Color(0, 123, 255));  // Blue text
        registerRedirectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerRedirectButton.setFocusPainted(false);
        registerRedirectButton.setBorderPainted(false);  // No border
        registerRedirectButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button

        // Go to HomePage button
        goToHomePageButton = new JButton("Go to HomePage");
        goToHomePageButton.setBackground(new Color(255, 255, 255));  // White background
        goToHomePageButton.setForeground(new Color(0, 123, 255));  // Blue text
        goToHomePageButton.setFont(new Font("Arial", Font.PLAIN, 14));
        goToHomePageButton.setFocusPainted(false);
        goToHomePageButton.setBorderPainted(false);  // No border
        goToHomePageButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginAction();
            }
        });

        // Register redirect button action listener
        registerRedirectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterPage();  // Redirect to RegisterPage
            }
        });

        // Go to HomePage button action listener
        goToHomePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openHomePage();  // Redirect to HomePage
            }
        });

        // Add the buttons to the panel
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        gbc.gridy = 5;
        panel.add(registerRedirectButton, gbc);

        gbc.gridy = 6;
        panel.add(goToHomePageButton, gbc);

        // Add the panel to the frame
        add(panel);
    }

    // Method to add form fields with GridBagLayout constraints
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel.setForeground(new Color(80, 80, 80)); // Gray label color

        field.setPreferredSize(new Dimension(350, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(new Color(240, 240, 240));  // Light gray background
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));  // Border

        // Set constraints for the label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Handle login action
    private void handleLoginAction() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (validateLogin(email, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            // Update the global currentUser or currentArtist variable
            String sql = "SELECT * FROM Customer WHERE Email = ? AND Password = ?";
            try (Connection conn = DBConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, email);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // If the user is a customer
                    CurrentUser.currentUser = rs.getString("CustomerId");
                    System.out.println("Customer Id Log: "+ rs.getString("CustomerId"));
                    JOptionPane.showMessageDialog(this, "Welcome, Customer!");
                    openHomePage();  // Redirect to HomePage after login
                } else {
                    // If the user is an artist
                    sql = "SELECT * FROM Artist WHERE Email = ? AND Password = ?";
                    try (PreparedStatement artistStmt = conn.prepareStatement(sql)) {
                        artistStmt.setString(1, email);
                        artistStmt.setString(2, password);

                        ResultSet artistRs = artistStmt.executeQuery();

                        if (artistRs.next()) {
                            // If the user is an artist
                            CurrentUser.currentArtist = artistRs.getString("ArtistId");
                            System.out.println("ArtistId Log: " + artistRs.getString("ArtistId"));
                            JOptionPane.showMessageDialog(this, "Welcome, Artist!");
                            openHomePage();  // Redirect to HomePage after login
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid credentials.");
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Redirect to another page after successful login
            // You can change this to the main application page
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }

    // Validate login credentials
    private boolean validateLogin(String email, String password) {
        return email != null && password != null && !email.isEmpty() && !password.isEmpty();
    }

    // Open the RegisterPage when the user clicks on the "Register" button
    private void openRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
        this.setVisible(false);  // Hide the login page when the register page opens
    }

    // Open the HomePage after login
    private void openHomePage() {
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        this.setVisible(false);  // Hide the login page and show the home page
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage frame = new LoginPage();
            frame.setVisible(true);
        });
    }
}
