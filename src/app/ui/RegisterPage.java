package app.ui;

import app.Data.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.UUID;

public class RegisterPage extends JFrame {

    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField addressField;
    private JTextField bioField;
    private JTextField profileImageField;
    private JRadioButton customerRadioButton;
    private JRadioButton artistRadioButton;
    private JButton registerButton;
    private JButton loginRedirectButton; // Login'a yönlendiren buton

    public RegisterPage() {
        setTitle("Register");
        setSize(1200, 800); // Resize window for better space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow resizing

        // Main panel for form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better control over alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Space between components

        panel.setBackground(new Color(255, 255, 255)); // White background

        // Aligning radio buttons (Toggle buttons) at the top
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Center the radio buttons
        customerRadioButton = new JRadioButton("Register as Customer");
        artistRadioButton = new JRadioButton("Register as Artist");
        ButtonGroup group = new ButtonGroup();
        group.add(customerRadioButton);
        group.add(artistRadioButton);
        radioPanel.add(customerRadioButton);
        radioPanel.add(artistRadioButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(radioPanel, gbc);

        // Add form fields
        addFormField(panel, gbc, "Full Name:", fullNameField = new JTextField());
        addFormField(panel, gbc, "Email:", emailField = new JTextField());
        addFormField(panel, gbc, "Password:", passwordField = new JPasswordField());
        addFormField(panel, gbc, "Address:", addressField = new JTextField());

        // Artist-specific fields
        JLabel artistNote = new JLabel("Biography & Profile Image URL are required for artists only!");
        artistNote.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));
        artistNote.setForeground(new Color(255, 0, 0));  // Red color for emphasis
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(artistNote, gbc);

        addFormField(panel, gbc, "Biography:", bioField = new JTextField());
        addFormField(panel, gbc, "Profile Image URL:", profileImageField = new JTextField());

        // Register button with modern design
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 123, 255));  // Blue background
        registerButton.setForeground(Color.WHITE);  // White text
        registerButton.setFocusPainted(false);  // Remove focus border
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(200, 50));  // Button size

        // Login redirect button
        loginRedirectButton = new JButton("Already have an account? Login");
        loginRedirectButton.setBackground(new Color(255, 255, 255));  // White background
        loginRedirectButton.setForeground(new Color(0, 123, 255));  // Blue text
        loginRedirectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginRedirectButton.setFocusPainted(false);
        loginRedirectButton.setBorderPainted(false);  // No border
        loginRedirectButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegisterAction();
            }
        });

        // Login redirect button action listener
        loginRedirectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();  // Redirect to LoginPage
            }
        });

        // Add the buttons to the panel (Move them one step down)
        gbc.gridx = 0;
        gbc.gridy = 8;  // Increased the value to move the buttons down one row
        gbc.gridwidth = 2;  // Make buttons span the entire width
        panel.add(registerButton, gbc);

        gbc.gridy = 9;  // Move the login redirect button down as well
        panel.add(loginRedirectButton, gbc);

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

    // Handle register action
    private void handleRegisterAction() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String address = addressField.getText();
        String bio = bioField.getText();
        String profileImage = profileImageField.getText();

        // Check if any required field is empty
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;  // Stop further processing if fields are empty
        }

        // Proceed with registration based on the selected type
        if (customerRadioButton.isSelected()) {
            // Register as Customer
            registerCustomer(fullName, email, password, address);
        } else if (artistRadioButton.isSelected()) {
            // Register as Artist
            if (bio.isEmpty() || profileImage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields for Artist.");
                return;
            }
            registerArtist(fullName, email, password, bio, profileImage);
        } else {
            JOptionPane.showMessageDialog(this, "Please select register type!");
        }
    }

    // Register Customer in the database
    private void registerCustomer(String fullName, String email, String password, String address) {
        String sql = "INSERT INTO Customer (CustomerId, FullName, Email, Password, Address, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String customerId = UUID.randomUUID().toString(); // Generate CustomerId

            stmt.setString(1, customerId);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, address);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer registered successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Could not register customer.");
        }
    }

    // Register Artist in the database
    private void registerArtist(String fullName, String email, String password, String bio, String profileImage) {
        String sql = "INSERT INTO Artist (ArtistId, FullName, Email, Password, Bio, ProfileImgUrl, ArtistRate, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String artistId = UUID.randomUUID().toString(); // Generate ArtistId

            stmt.setString(1, artistId);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, bio);
            stmt.setString(6, profileImage);
            stmt.setBigDecimal(7, new java.math.BigDecimal(0));  // ArtistRate set to 0
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Artist registered successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Could not register artist.");
        }
    }

    // Open the LoginPage when the user clicks on the "Login" button
    private void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.setVisible(false);  // Hide the register page when the login page opens
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterPage frame = new RegisterPage();
            frame.setVisible(true);
        });
    }
}
