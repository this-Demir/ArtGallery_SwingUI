package app.ui;

import app.models.CurrentUser;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    public HomePage() {
        // FlatLaf temasını uygulama
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // FlatLaf tema uygulandı
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Ana pencere ayarları
        setTitle("Sanat Galerisi");
        setSize(900, 900);  // Boyutu 900x900 yapıyoruz
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Ekranın ortasında açılır
        setResizable(true);  // Boyut değiştirilebilir

        // Ana panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());  // BorderLayout kullanıyoruz
        mainPanel.setBackground(Color.white);

        // Navbar
        JPanel navBarPanel = new JPanel();
        navBarPanel.setBackground(new Color(72, 144, 239));  // Mavi arka plan
        navBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel artGalleryLabel = new JLabel("Art Gallery");
        artGalleryLabel.setFont(new Font("Serif", Font.BOLD, 24));
        artGalleryLabel.setForeground(Color.WHITE);
        navBarPanel.add(artGalleryLabel);

        // Header Paneli (Art Gallery, slogan, ve sergiler butonu)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.white);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));  // Yatay hizalama

        JLabel artGalleryText = new JLabel("Art Gallery");
        artGalleryText.setFont(new Font("Serif", Font.BOLD, 72));
        artGalleryText.setForeground(new Color(16, 14, 14));  // Mavi renk
        artGalleryText.setAlignmentX(Component.CENTER_ALIGNMENT);  // Ortala
        headerPanel.add(artGalleryText);

        // Slogan
        JLabel sloganLabel = new JLabel("Discover the Beauty of Art");
        sloganLabel.setFont(new Font("Serif", Font.BOLD, 36));
        sloganLabel.setForeground(new Color(0, 0, 0));  // Mavi renk
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Ortala
        headerPanel.add(sloganLabel);

        // "See Current Exhibitions" butonu
        JButton exhibitionsButton = new JButton("See Current Exhibitions");
        exhibitionsButton.setFont(new Font("Arial", Font.BOLD, 16));
        exhibitionsButton.setBackground(new Color(72, 144, 239));  // Mavi buton
        exhibitionsButton.setForeground(Color.WHITE);
        exhibitionsButton.setFocusPainted(false);
        exhibitionsButton.setPreferredSize(new Dimension(250, 50));  // Buton boyutu
        exhibitionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Ortala
        exhibitionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Sergiler sayfasına yönlendiriliyorsunuz.");
            }
        });

        // Header'da Login, Register ve Logout butonları
        JPanel headerButtonPanel = new JPanel();
        headerButtonPanel.setBackground(Color.white);
        headerButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton logoutButton = new JButton("Logout");

        // Eğer kullanıcı giriş yaptıysa logout butonu görünsün
        if (CurrentUser.currentUser != null) {
            loginButton.setVisible(false);
            registerButton.setVisible(false);
            logoutButton.setBackground(new Color(220, 53, 69));  // Kırmızı logout butonu
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
            headerButtonPanel.add(logoutButton);

            // Logout butonu tıklandığında
            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logoutUser();
                }
            });
        } else {
            loginButton.setBackground(new Color(33, 150, 243));  // Mavi Login butonu
            loginButton.setForeground(Color.WHITE);
            registerButton.setBackground(new Color(33, 150, 243));  // Mavi Register butonu
            registerButton.setForeground(Color.WHITE);

            headerButtonPanel.add(loginButton);
            headerButtonPanel.add(registerButton);

            // Login butonuna tıklanırsa
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openLoginPage();
                }
            });

            // Register butonuna tıklanırsa
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openRegisterPage();
                }
            });
        }

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 123, 255));  // Mavi arka plan
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel footerLabel = new JLabel("© 2025 Sanat Galerisi | About Us");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        // "About Us" metnini eklemek için tıklanabilir bir buton ekleyebiliriz
        footerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showAboutUs();
            }
        });

        // İçerik Paneli (Center Panel)
        mainPanel.add(navBarPanel, BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(exhibitionsButton, BorderLayout.SOUTH);
        mainPanel.add(headerButtonPanel, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Pencereyi ekranda gösterme
        add(mainPanel);
    }

    // About Us sayfası için basit bir gösterim
    private void showAboutUs() {
        JOptionPane.showMessageDialog(this, "Sanat Galerisi hakkında bilgi...\nBizi takip edin!");
    }

    // Login sayfasına yönlendirme
    private void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.setVisible(false);
    }

    // Register sayfasına yönlendirme
    private void openRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
        this.setVisible(false);
    }

    // Logout işlemi
    private void logoutUser() {
        CurrentUser.currentUser = null;
        CurrentUser.currentArtist = null;
        JOptionPane.showMessageDialog(this, "Başarıyla çıkış yaptınız!");
        this.setVisible(true);  // Logout sonrası ana sayfayı tekrar göster
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomePage frame = new HomePage();
                frame.setVisible(true);
            }
        });
    }
}
