package Tests;

import app.data.DBSeeder;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        // Run once  - Demir
        DBSeeder dbSeeder = new DBSeeder();

        // Seed customers, artists, and artworks
        dbSeeder.seedCustomers(); // Done
        dbSeeder.seedArtists();  // Done
        dbSeeder.seedArtworks(); // Done
    }
}

