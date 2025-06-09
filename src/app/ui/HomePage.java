package app.ui;

import app.models.CurrentUser;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Sanat Galerisi");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.white);

        // Navbar (üst panel)
        JPanel navBarPanel = new JPanel(new BorderLayout());
        navBarPanel.setBackground(new Color(72, 144, 239));
        navBarPanel.setPreferredSize(new Dimension(900, 50));

        JLabel artGalleryLabel = new JLabel("  Art Gallery");
        artGalleryLabel.setFont(new Font("Serif", Font.BOLD, 20));
        artGalleryLabel.setForeground(Color.WHITE);
        navBarPanel.add(artGalleryLabel, BorderLayout.WEST);

        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navButtonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton logoutButton = new JButton("Logout");
        JButton favoritesButton = new JButton("Favorites");
        JButton manageArtworksButton = new JButton("Manage My Artworks");

        favoritesButton.setBackground(new Color(40, 167, 69));
        favoritesButton.setForeground(Color.WHITE);
        favoritesButton.setPreferredSize(new Dimension(100, 30));
        favoritesButton.setFocusPainted(false);
        favoritesButton.addActionListener(e -> openFavoritesPage());

        manageArtworksButton.setBackground(new Color(113, 186, 28));
        manageArtworksButton.setForeground(Color.WHITE);
        manageArtworksButton.setPreferredSize(new Dimension(170, 30));
        manageArtworksButton.setFocusPainted(false);
        manageArtworksButton.addActionListener(e -> openManageArtworksPage());

        if (CurrentUser.currentUser != null || CurrentUser.currentArtist != null) {
            loginButton.setVisible(false);
            registerButton.setVisible(false);

            logoutButton.setBackground(new Color(220, 53, 69));
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
            logoutButton.setPreferredSize(new Dimension(90, 30));
            logoutButton.addActionListener(e -> logoutUser());

            if (CurrentUser.currentUser != null) {
                navButtonPanel.add(favoritesButton);
            }
            if (CurrentUser.currentArtist != null) {
                navButtonPanel.add(manageArtworksButton);
            }

            navButtonPanel.add(logoutButton);

        } else {
            loginButton.setBackground(new Color(33, 150, 243));
            loginButton.setForeground(Color.WHITE);
            loginButton.setPreferredSize(new Dimension(90, 30));
            loginButton.addActionListener(e -> openLoginPage());

            registerButton.setBackground(new Color(33, 150, 243));
            registerButton.setForeground(Color.WHITE);
            registerButton.setPreferredSize(new Dimension(90, 30));
            registerButton.addActionListener(e -> openRegisterPage());

            navButtonPanel.add(loginButton);
            navButtonPanel.add(registerButton);
        }

        navBarPanel.add(navButtonPanel, BorderLayout.EAST);

        // Header Panel (orta içerik)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.white);

        JLabel artGalleryText = new JLabel("Art Gallery");
        artGalleryText.setFont(new Font("Serif", Font.BOLD, 72));
        artGalleryText.setForeground(new Color(16, 14, 14));
        artGalleryText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(50));
        headerPanel.add(artGalleryText);

        JLabel sloganLabel = new JLabel("Discover the Beauty of Art");
        sloganLabel.setFont(new Font("Serif", Font.BOLD, 36));
        sloganLabel.setForeground(Color.BLACK);
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(sloganLabel);

        JButton seeAllArtworksButton = new JButton("See All Artworks");
        seeAllArtworksButton.setFont(new Font("Arial", Font.BOLD, 16));
        seeAllArtworksButton.setBackground(new Color(72, 144, 239));
        seeAllArtworksButton.setForeground(Color.WHITE);
        seeAllArtworksButton.setFocusPainted(false);
        seeAllArtworksButton.setPreferredSize(new Dimension(400, 80));
        seeAllArtworksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        seeAllArtworksButton.addActionListener(e -> openArtworksPage());

        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(seeAllArtworksButton);

        JLabel aboutUsContent = new JLabel("<html><div style='text-align: center;'>"
                + "<br><b>About Us</b><br><br>"
                + "Art Gallery is a modern platform for discovering, rating, and bidding on fine art.<br><br>"
                + "Built by software engineering students at Yaşar University, the system provides a seamless experience for both customers and artists.<br><br>"
                + "Technologies used include Java Swing, MySQL.<br><br>"
                + "Enjoy browsing, rating, and purchasing unique artworks curated from talented artists."
                + "</div></html>");
        aboutUsContent.setFont(new Font("Arial", Font.PLAIN, 16));
        aboutUsContent.setForeground(Color.DARK_GRAY);
        aboutUsContent.setHorizontalAlignment(SwingConstants.CENTER);
        aboutUsContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalStrut(40));
        headerPanel.add(aboutUsContent);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(0, 123, 255));
        footerPanel.setPreferredSize(new Dimension(900, 40));

        JLabel footerLabel = new JLabel("© 2025 Sanat Galerisi");
        footerLabel.setForeground(Color.WHITE);

        footerPanel.add(footerLabel);

        mainPanel.add(navBarPanel, BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.setVisible(false);
    }

    private void openRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
        this.setVisible(false);
    }

    private void logoutUser() {
        CurrentUser.currentUser = null;
        CurrentUser.currentArtist = null;
        JOptionPane.showMessageDialog(this, "Logged out successfully!");
        dispose();
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }

    private void openArtworksPage() {
        ArtworksPage artworksPage = new ArtworksPage();
        artworksPage.setVisible(true);
        this.setVisible(false);
    }

    private void openFavoritesPage() {
        FavoritesPage favoritesPage = new FavoritesPage();
        favoritesPage.setVisible(true);
        this.setVisible(false);
    }

    private void openManageArtworksPage() {
        ManageArtworksPage managePage = new ManageArtworksPage();
        managePage.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}
