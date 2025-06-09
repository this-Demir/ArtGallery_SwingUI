package Tests;

import app.Data.DBConnector;
import app.Data.DBSeeder;

import java.sql.Connection;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        // Run once
        DBSeeder dbSeeder = new DBSeeder();

        // Seed customers, artists, and artworks
        dbSeeder.seedCustomers(); // Done
        dbSeeder.seedArtists();  // Done
        dbSeeder.seedArtworks(); // Done
    }
}

