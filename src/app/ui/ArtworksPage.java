package app.ui;

import app.models.CurrentUser;
import app.Data.DBConnector;
import app.models.Artwork;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.sql.Timestamp;

public class ArtworksPage extends JFrame {

    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JPanel artworkGridPanel;
    private Timer countdownTimer;

    public ArtworksPage() {
        setTitle("Artworks");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.white);

        // NAVBAR (updated with "Return to Home Page" button)
        JPanel navBarPanel = new JPanel(new BorderLayout());
        navBarPanel.setBackground(new Color(72, 144, 239)); // Blue background
        navBarPanel.setPreferredSize(new Dimension(1000, 50));  // Set navbar height


        JLabel artGalleryLabel = new JLabel("  Art Gallery");
        artGalleryLabel.setFont(new Font("Serif", Font.BOLD, 20));
        artGalleryLabel.setForeground(Color.WHITE);
        navBarPanel.add(artGalleryLabel, BorderLayout.WEST);

        // "Return to Home Page" button in navbar
        JButton returnHomeButton = new JButton("Return to Home Page");
        returnHomeButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnHomeButton.setBackground(new Color(33, 150, 243)); // Blue background
        returnHomeButton.setForeground(Color.WHITE);
        returnHomeButton.setFocusPainted(false);
        returnHomeButton.setPreferredSize(new Dimension(200, 50));
        returnHomeButton.addActionListener(e -> returnToHomePage());
        navBarPanel.add(returnHomeButton, BorderLayout.EAST);

        // HEADER (Search and Category filter)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Use FlowLayout to align items horizontally
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        headerPanel.setBackground(Color.white);

        JLabel searchLabel = new JLabel("Search Artworks:");
        headerPanel.add(searchLabel);

        searchField = new JTextField(20);
        headerPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> loadArtworks());
        headerPanel.add(searchButton);

        JLabel categoryLabel = new JLabel("Category:");
        headerPanel.add(categoryLabel);

        String[] categories = {"All", "Landscape", "Seascape", "Abstract", "Portrait"};
        categoryComboBox = new JComboBox<>(categories);
        headerPanel.add(categoryComboBox);

        // CONTENT PANEL (using GridLayout for even grid-like arrangement)
        artworkGridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columns, dynamic rows
        artworkGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        artworkGridPanel.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(artworkGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // FOOTER
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(0, 123, 255));  // Blue background
        footerPanel.setPreferredSize(new Dimension(1000, 40));  // Set footer height

        JLabel footerLabel = new JLabel("© 2025 Art Gallery | About Us");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        // Add components to main panel
        mainPanel.add(navBarPanel,BorderLayout.PAGE_END); // NAVBAR
        mainPanel.add(headerPanel, BorderLayout.PAGE_START); // HEADER (Search + Category filter)
        mainPanel.add(scrollPane, BorderLayout.CENTER); // CONTENT (Artworks)
        mainPanel.add(footerPanel, BorderLayout.SOUTH); // FOOTER

        add(mainPanel);

        loadArtworks();
    }

    private void loadArtworks() {
        artworkGridPanel.removeAll();
        String searchQuery = searchField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        List<Artwork> artworks = getAllArtworks(searchQuery, category);

        for (Artwork artwork : artworks) {
            artworkGridPanel.add(createArtworkCard(artwork));
        }

        repaint();
        revalidate();
    }

    private List<Artwork> getAllArtworks(String searchQuery, String category) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM Artwork WHERE Title LIKE ? OR ArtistId IN (SELECT ArtistId FROM Artist WHERE FullName LIKE ?)";
        if (!category.equals("All")) {
            sql += " AND Category = ?";
        }

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            if (!category.equals("All")) {
                stmt.setString(3, category);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String artworkId = rs.getString("ArtworkId");
                String title = rs.getString("Title");
                String artistName = getArtistNameById(rs.getString("ArtistId"));
                double basePrice = rs.getDouble("BasePrice");
                double rating = getRatingForArtwork(artworkId);
                List<String> imageUrls = getImageUrlsForArtwork(artworkId);
                String artworkCategory = rs.getString("Category");

                Timestamp endTime = getEndTimeForArtwork(artworkId);  // Get endTime from Countdown table
                artworks.add(new Artwork(artworkId, title, artistName, basePrice, rating, basePrice, imageUrls, artworkCategory, endTime));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }

    private Timestamp getEndTimeForArtwork(String artworkId) {
        String sql = "SELECT EndTime FROM Countdown WHERE ArtworkId = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artworkId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("EndTime");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getImageUrlsForArtwork(String artworkId) {
        List<String> imageUrls = new ArrayList<>();
        String sql = "SELECT ImageUrl FROM ArtworkImages WHERE ArtworkId = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artworkId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imageUrls.add(rs.getString("ImageUrl"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imageUrls;
    }

    private String getArtistNameById(String artistId) {
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT FullName FROM Artist WHERE ArtistId = ?")) {
            stmt.setString(1, artistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("FullName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private double getRatingForArtwork(String artworkId) {
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT AVG(RatingValue) AS avgRating FROM Rate WHERE ArtworkId = ?")) {
            stmt.setString(1, artworkId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("avgRating");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getTimeLeft(Timestamp endTime) {
        long diff = endTime.getTime() - System.currentTimeMillis();

        long days = diff / (24 * 60 * 60 * 1000);
        long hours = (diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (diff % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (diff % (60 * 1000)) / 1000;

        return days + "d " + hours + "h " + minutes + "m " + seconds + "s left";
    }

    private JPanel createImageSlider(List<String> imagePaths) {
        JPanel sliderPanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        if (imagePaths.isEmpty()) return sliderPanel;

        int[] currentIndex = {0};

        // Resim yolunun doğru olduğundan emin olun ve tek bir yol ekleyin
        String imagePath = imagePaths.get(currentIndex[0]);

        // getClass().getClassLoader().getResource ile doğru yolu alın
        URL imgURL = getClass().getClassLoader().getResource(imagePath);

        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image image = icon.getImage().getScaledInstance(260, 180, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } else {
            // Eğer resim bulunamazsa, hata mesajı yazdırın
            System.out.println("Image not found: " + imagePath);
        }

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        prevButton.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] - 1 + imagePaths.size()) % imagePaths.size();
            updateImage(imageLabel, imagePaths.get(currentIndex[0]));
        });

        nextButton.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] + 1) % imagePaths.size();
            updateImage(imageLabel, imagePaths.get(currentIndex[0]));
        });

        sliderPanel.add(prevButton, BorderLayout.WEST);
        sliderPanel.add(imageLabel, BorderLayout.CENTER);
        sliderPanel.add(nextButton, BorderLayout.EAST);
        sliderPanel.setPreferredSize(new Dimension(260, 180));
        return sliderPanel;
    }

    private void updateImage(JLabel imageLabel, String imagePath) {
        String fullImagePath = imagePath;
        URL imgURL = getClass().getClassLoader().getResource(fullImagePath);

        if (imgURL != null) {
            ImageIcon newIcon = new ImageIcon(imgURL);
            Image newImage = newIcon.getImage().getScaledInstance(260, 180, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(newImage));
        } else {
            System.out.println("Image not found: " + fullImagePath);
        }
    }

    private JPanel createArtworkCard(Artwork artwork) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(300, 450)); // Kart boyutunu büyüttük

        // Image slider
        card.add(createImageSlider(artwork.getImageURL()));
        card.add(Box.createVerticalStrut(10));

        // Title
        JLabel titleLabel = new JLabel("<html><b>" + artwork.getTitle() + "</b></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Artist
        JLabel artistLabel = new JLabel("Artist: " + artwork.getArtistName(), SwingConstants.CENTER);
        artistLabel.setForeground(new Color(33, 150, 243));
        artistLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Price
        JLabel priceLabel = new JLabel("Current Price: $" + artwork.getCurrentPrice());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rating
        JLabel ratingLabel = new JLabel("Rating: " + String.format("%.1f", artwork.getRating()) + "/5");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category
        JLabel categoryLabel = new JLabel("Category: " + artwork.getCategory()); // Category added
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Countdown Timer
        JLabel countdownLabel = new JLabel();
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 14));
        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Calculate and update countdown, check if endTime is null
        if (artwork.getEndTime() != null) {
            countdownTimer = new Timer(1000, e -> {
                long remainingTime = artwork.getEndTime().getTime() - System.currentTimeMillis();

                long days = remainingTime / (1000 * 60 * 60 * 24);
                long hours = (remainingTime / (1000 * 60 * 60)) % 24;
                long minutes = (remainingTime / (1000 * 60)) % 60;
                long seconds = (remainingTime / 1000) % 60;

                if (remainingTime > 0) {
                    countdownLabel.setText("Time left: " + days + "d " + hours + "h " + minutes + "m " + seconds + "s");
                } else {
                    countdownLabel.setText("Auction Ended");
                    countdownTimer.stop();
                }
            });
            countdownTimer.start();
        } else {
            countdownLabel.setText("Auction Ended");
        }

        // Buttons
        JButton favButton = new JButton("Add to Favorites");
        JButton offerButton = new JButton("Make an Offer");
        JButton rateButton = new JButton("Rate this Artwork");
        JButton historyButton = new JButton("View Bid History");

        for (JButton btn : new JButton[]{favButton, offerButton, rateButton, historyButton}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 30));
        }

        // Add to card
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(artistLabel);
        card.add(priceLabel);
        card.add(ratingLabel);
        card.add(categoryLabel); // Category added
        card.add(countdownLabel); // Countdown added
        card.add(Box.createVerticalStrut(10));
        card.add(favButton);
        card.add(offerButton);
        card.add(rateButton);
        card.add(historyButton);

        return card;
    }

    // Navigate back to HomePage
    private void returnToHomePage() {
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        this.setVisible(false); // Close current window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArtworksPage frame = new ArtworksPage();
            frame.setVisible(true);
        });
    }
}
