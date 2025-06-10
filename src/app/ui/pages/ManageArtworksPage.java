package app.ui.pages;

import app.data.DBConnector;
import app.models.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.UUID;

public class ManageArtworksPage extends JFrame {

    private JPanel artworksPanel;

    public ManageArtworksPage() {
        setTitle("Manage My Artworks");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Manage My Artworks", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        container.add(title, BorderLayout.NORTH);

        artworksPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        artworksPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(artworksPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        container.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomNav.setPreferredSize(new Dimension(1200, 60));
        bottomNav.setBackground(Color.WHITE);

        JButton addArtworkButton = new JButton("Add New Artwork");
        addArtworkButton.setFont(new Font("Arial", Font.BOLD, 14));
        addArtworkButton.setBackground(new Color(40, 167, 69));
        addArtworkButton.setForeground(Color.WHITE);
        addArtworkButton.setPreferredSize(new Dimension(200, 40));
        addArtworkButton.setFocusPainted(false);
        addArtworkButton.addActionListener(e -> openAddArtworkDialog());
        bottomNav.add(addArtworkButton);

        JButton returnButton = new JButton("Return to Home Page");
        returnButton.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton.setBackground(new Color(113, 186, 28));
        returnButton.setForeground(Color.WHITE);
        returnButton.setPreferredSize(new Dimension(200, 40));
        returnButton.addActionListener(e -> {
            HomePage home = new HomePage();
            home.setVisible(true);
            this.dispose();
        });
        bottomNav.add(returnButton);

        container.add(bottomNav, BorderLayout.SOUTH);

        add(container);
        loadArtworks();
    }

    private void loadArtworks() {
        artworksPanel.removeAll();
        String sql = """
            SELECT A.ArtworkId, A.Title, A.BasePrice, A.Category, A.Status
            FROM Artwork A
            WHERE A.ArtistId = ?
            ORDER BY A.CreatedAt DESC
        """;

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, CurrentUser.currentArtist);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JPanel card = createArtworkCard(
                        rs.getString("ArtworkId"),
                        rs.getString("Title"),
                        rs.getDouble("BasePrice"),
                        rs.getString("Category"),
                        rs.getString("Status")
                );
                artworksPanel.add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        artworksPanel.revalidate();
        artworksPanel.repaint();
    }

    private JPanel createArtworkCard(String artworkId, String title,
                                     double price, String category, String status) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 280));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("₺" + price + " | " + category + " | " + status);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        editBtn.setBackground(new Color(0, 123, 255));
        editBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);

        editBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        editBtn.addActionListener(e -> openEditDialog(artworkId));

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this artwork?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteArtwork(artworkId);
            }
        });

        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(infoLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(editBtn);
        card.add(deleteBtn);

        return card;
    }

    private void openEditDialog(String artworkId) {
        JTextField titleField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField statusField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Artwork", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Artwork SET Title=?, BasePrice=?, Category=?, Status=? WHERE ArtworkId=?")) {
                stmt.setString(1, titleField.getText());
                stmt.setDouble(2, Double.parseDouble(priceField.getText()));
                stmt.setString(3, categoryField.getText());
                stmt.setString(4, statusField.getText());
                stmt.setString(5, artworkId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Artwork updated successfully.");
                this.dispose();
                new ManageArtworksPage().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            }
        }
    }

    private void openAddArtworkDialog() {
        JTextField titleField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField statusField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Artwork", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Artwork (ArtworkId, ArtistId, Title, BasePrice, Category, Status, IsOpenToSale, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, 1, NOW())")) {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, CurrentUser.currentArtist);
                stmt.setString(3, titleField.getText());
                stmt.setDouble(4, Double.parseDouble(priceField.getText()));
                stmt.setString(5, categoryField.getText());
                stmt.setString(6, statusField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Artwork added successfully.");
                this.dispose();
                new ManageArtworksPage().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to add artwork: " + ex.getMessage());
            }
        }
    }

    private void deleteArtwork(String artworkId) {
        String sql = "DELETE FROM Artwork WHERE ArtworkId = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artworkId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Artwork deleted successfully.");
            this.dispose();
            new ManageArtworksPage().setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete artwork: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CurrentUser.currentArtist = "44c55577-4105-4fcd-9b16-af895c583e62";
        SwingUtilities.invokeLater(() -> {
            ManageArtworksPage page = new ManageArtworksPage();
            page.setVisible(true);
        });
    }
}
