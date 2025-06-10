package app.data;

import java.sql.*;
import java.util.UUID;

public class DBSeeder {

    public void seedCustomers() {
        String[][] customers = {
                {"Alice Brown", "alicebrown@example.com", "password789", "789 Pine St, Anytown, USA"},
                {"Bob White", "bobwhite@example.com", "password101", "101 Birch St, Anytown, USA"},
                {"Charlie Green", "charliegreen@example.com", "password112", "112 Maple St, Anytown, USA"},
                {"Debbie Black", "debbieblack@example.com", "password131", "131 Cedar St, Anytown, USA"},
                {"Eve Blue", "eveblue@example.com", "password415", "415 Oakwood St, Anytown, USA"},
                {"Frank Red", "frankred@example.com", "password161", "161 Elm St, Anytown, USA"},
                {"Grace Yellow", "graceyellow@example.com", "password718", "718 Fir St, Anytown, USA"},
                {"Harry Purple", "harrypurple@example.com", "password192", "192 Willow St, Anytown, USA"}
        };
        String sql = "INSERT INTO Customer (CustomerId, FullName, Email, Password, Address, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String[] customer : customers) {
                // Generate a new UUID for CustomerId
                String customerId = UUID.randomUUID().toString();

                stmt.setString(1, customerId);      // CustomerId
                stmt.setString(2, customer[0]);     // FullName
                stmt.setString(3, customer[1]);     // Email
                stmt.setString(4, customer[2]);     // Password
                stmt.setString(5, customer[3]);     // Address
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // CreatedAt

                stmt.executeUpdate(); // Execute the insert statement
            }

            System.out.println("Customers seeded successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to seed artists
    public void seedArtists() {
        // Sample artist data
        String[][] artists = {
                {"Clara Painter", "clara@example.com", "artistpass3", "A master of realism."},
                {"David Sculptor", "david@example.com", "artistpass4", "Innovative sculptor."},
                {"Eva Designer", "eva@example.com", "artistpass5", "Conceptual art enthusiast."},
                {"Frank Illustrator", "frank@example.com", "artistpass6", "Creates surreal landscapes."},
                {"Grace Photographer", "grace@example.com", "artistpass7", "Capturing moments in time."}
        };


        String sql = "INSERT INTO Artist (ArtistId, FullName, Email, Password, Bio, ProfileImgUrl, ArtistRate, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String[] artist : artists) {
                // Generate a new UUID for ArtistId
                String artistId = UUID.randomUUID().toString();

                stmt.setString(1, artistId);        // ArtistId
                stmt.setString(2, artist[0]);       // FullName
                stmt.setString(3, artist[1]);       // Email
                stmt.setString(4, artist[2]);       // Password
                stmt.setString(5, artist[3]);       // Bio
                stmt.setString(6, "https://example.com/profile.jpg");  // ProfileImgUrl
                stmt.setBigDecimal(7, new java.math.BigDecimal(4.5));  // ArtistRate
                stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // CreatedAt

                stmt.executeUpdate(); // Execute the insert statement
            }

            System.out.println("Artists seeded successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to seed artworks (Ensure that ArtistId is taken from the Artist table)
    public void seedArtworks() {
        // Sample artwork data

        String[][] artworks = {
                {"Clara Painter", "Mountain Majesty", "A grand painting of a mountain range.", "1500.00", "Landscape"},
                {"David Sculptor", "Iron Will", "A sculpture depicting strength and resilience.", "5000.00", "Sculpture"},
                {"Eva Designer", "Abstract Vision", "A design reflecting the chaos of the modern world.", "1200.00", "Abstract"},
                {"Frank Illustrator", "Dreams in the Sky", "A surreal illustration of a flying city.", "1800.00", "Surrealism"},
                {"Grace Photographer", "Timeless Beauty", "A photograph capturing the serenity of nature.", "2000.00", "Photography"},
                {"Charlie Green", "Waterfall Dreams", "A painting of a serene waterfall in a lush forest.", "1100.00", "Landscape"},
                {"Debbie Black", "Night of the Wild", "A photograph of a wolf under the moonlight.", "950.00", "Photography"}
        };


        String sql = "INSERT INTO Artwork (ArtworkId, ArtistId, Title, Descp, BasePrice, Category, Status, IsOpenToSale, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String[] artwork : artworks) {
                // Generate a new UUID for ArtworkId
                String artworkId = UUID.randomUUID().toString();
                // Fetch artist ID from the artist's email (you could also join the Artist table to get the ID)
                String artistId = getArtistIdByEmail(conn, artwork[0]);

                // Ensure the artistId is valid before inserting
                if (artistId != null) {
                    stmt.setString(1, artworkId);     // ArtworkId
                    stmt.setString(2, artistId);       // ArtistId (retrieved from Artist table)
                    stmt.setString(3, artwork[1]);     // Title
                    stmt.setString(4, artwork[2]);     // Description
                    stmt.setBigDecimal(5, new java.math.BigDecimal(artwork[3]));  // BasePrice
                    stmt.setString(6, artwork[4]);     // Category
                    stmt.setString(7, "open_to_sale"); // Status
                    stmt.setInt(8, 1);                 // IsOpenToSale (1 for true)
                    stmt.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // CreatedAt

                    stmt.executeUpdate(); // Execute the insert statement
                } else {
                    System.out.println("Error: Artist not found for artwork: " + artwork[1]);
                }
            }

            System.out.println("Artworks seeded successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to fetch ArtistId based on the Artist's name
    private String getArtistIdByEmail(Connection conn, String artistName) throws SQLException {
        String artistId = null;
        String sql = "SELECT ArtistId FROM Artist WHERE FullName = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artistName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                artistId = rs.getString("ArtistId");
            }
        }

        return artistId;
    }
}
