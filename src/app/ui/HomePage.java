package app.ui;

import app.models.CurrentUser;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        navBarPanel.setPreferredSize(new Dimension(900, 50));  // Küçültülmüş yükseklik

        JLabel artGalleryLabel = new JLabel("  Art Gallery");
        artGalleryLabel.setFont(new Font("Serif", Font.BOLD, 20));
        artGalleryLabel.setForeground(Color.WHITE);
        navBarPanel.add(artGalleryLabel, BorderLayout.WEST);

        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navButtonPanel.setOpaque(false); // Buton paneli navbarla aynı renk kalsın

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton logoutButton = new JButton("Logout");

        if (CurrentUser.currentUser != null || CurrentUser.currentArtist != null) {
            loginButton.setVisible(false);
            registerButton.setVisible(false);
            logoutButton.setBackground(new Color(220, 53, 69));
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
            logoutButton.setPreferredSize(new Dimension(90, 30));

            logoutButton.addActionListener(e -> logoutUser());
            navButtonPanel.add(logoutButton);

        } else {
            loginButton.setBackground(new Color(33, 150, 243));
            loginButton.setForeground(Color.WHITE);
            loginButton.setPreferredSize(new Dimension(90, 30));

            registerButton.setBackground(new Color(33, 150, 243));
            registerButton.setForeground(Color.WHITE);
            registerButton.setPreferredSize(new Dimension(90, 30));

            loginButton.addActionListener(e -> openLoginPage());
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
        seeAllArtworksButton.setPreferredSize(new Dimension(200, 40));
        seeAllArtworksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        seeAllArtworksButton.addActionListener(e -> openArtworksPage());

        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(seeAllArtworksButton);

        // Eğer artist giriş yaptıysa, özel buton ekle
        if (CurrentUser.currentArtist != null) {
            JButton manageArtworksButton = new JButton("Manage My Artworks");
            manageArtworksButton.setFont(new Font("Arial", Font.BOLD, 16));
            manageArtworksButton.setBackground(new Color(33, 150, 243));
            manageArtworksButton.setForeground(Color.WHITE);
            manageArtworksButton.setFocusPainted(false);
            manageArtworksButton.setPreferredSize(new Dimension(250, 50));
            manageArtworksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            manageArtworksButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Manage My Artworks sayfasına yönlendiriliyorsunuz."));
            headerPanel.add(Box.createVerticalStrut(20));
            headerPanel.add(manageArtworksButton);
        }

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(0, 123, 255));
        footerPanel.setPreferredSize(new Dimension(900, 40));  // Küçültülmüş yükseklik

        JLabel footerLabel = new JLabel("© 2025 Sanat Galerisi | About Us");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showAboutUs();
            }
        });

        footerPanel.add(footerLabel);

        // Panel bileşenlerini ana panele ekle
        mainPanel.add(navBarPanel, BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showAboutUs() {
        JOptionPane.showMessageDialog(this, "Sanat Galerisi hakkında bilgi...\nBizi takip edin!");
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
        JOptionPane.showMessageDialog(this, "Başarıyla çıkış yaptınız!");
        dispose();
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }

    private void openArtworksPage() {
        ArtworksPage artworksPage = new ArtworksPage();
        artworksPage.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}
