# Se2230- Project: Art Gallery System

### Group Members
- Demir Demirdöğen - 23070006036
- Batuhan Salcan - 22070006040
- Beril Filibelioğlu - 22070006042
- Yağmur Pazı - 23070006066

---
### Technologies Used: Java Swing + MySQL

**This project is SQL-focused**. Most of the logic—such as artwork management, bidding, rating, and offer validation—is executed directly from the UI using **structured SQL operations**. These include:

- **Stored Procedures** for complex business rules
- **User-defined Functions** for reusable SQL logic
- **Views** to simplify query layers
- **Triggers** to automate tasks like countdown creation

---

### Our Development Environment
- **Java JDK 17+**
- **IntelliJ IDEA Ultimate**
- **MySQL 8.0+**
- **MySQL Workbench**

---

###  External JARs (Already Added in `/lib`)
Make sure to include the following libraries:

| JAR File                      | Purpose                                   |
|-------------------------------|-------------------------------------------|
| `mysql-connector-j-9.3.0.jar` | MySQL JDBC driver                         |
| `flatlaf-demo-3.6.jar`        | FlatLaf UI theme for modern styling       |

### How to Add as Library in IntelliJ:
1. Right-click the `lib` folder → **"Add as Library..."**
2. Ensure **Project Level** is selected.
3. Click OK.

---

### ️ Database Configuration

- Create a database named: `artgallerysystem`
- Import the provided SQL schema (includes tables like `Artwork`, `Customer`, `Offer`, `Rate`, `Countdown`, `Sales`, etc.)

Modify the `DBConnector.java` class with your database credentials:
```java
String url = "jdbc:mysql://localhost:3306/artgallerysystem";
String username = "root";
String password = "your_password";
```
---

## Project Structure


### `src/app/data/`

| Class              | Purpose                                                                 |
|--------------------|-------------------------------------------------------------------------|
| `DBConnector.java` | Manages MySQL connection using JDBC. Used globally for DB access.       |

---

###  `src/app/models/`

| Class               | Purpose                                                                 |
|---------------------|-------------------------------------------------------------------------|
| `Artwork.java`      | Models an artwork: title, artist ID, category, price, status, etc.      |
| `CurrentUser.java`  | Holds session info for logged-in user (either Customer or Artist).      |

---

###  `src/app/ui/pages/` – Main Application Screens

| Class                      | Purpose                                                                 |
|----------------------------|-------------------------------------------------------------------------|
| `HomePage.java`            | Welcome page + navigation to other features.                           |
| `LoginPage.java`           | Handles user login (Customer or Artist) using credentials.              |
| `RegisterPage.java`        | Creates new users (inserts into Customer/Artist tables).                |
| `ArtworksPage.java`        | Displays all available artworks with filter/search and countdowns.      |
| `FavoritesPage.java`       | Lists artworks favorited by the current customer.                       |
| `CustomerOrdersPage.java`  | Shows artworks purchased by the customer after bidding ends.            |
| `ManageArtworksPage.java`  | Allows artists to manage their own artworks (view, edit, delete).       |
| `ArtistProfilePage.java`   | Shows artist bio and profile when clicking on an artwork.               |

---

###  `src/app/ui/windows/` – Dialog/Popup Windows

| Class                     | Purpose                                                                 |
|---------------------------|-------------------------------------------------------------------------|
| `MakeOfferWindow.java`    | Opens a modal for placing a bid (INSERT into `Offer`).                 |
| `RateArtworkWindow.java`  | Popup window for submitting a rating (1–5 stars) for an artwork.        |
| `OfferHistoryWindow.java` | Displays the history of bids for a selected artwork.                   |

---

###  `resources/images/`

- Stores artwork and artist images used in the UI display.

---

###  `AppRun.java`

- Application's main entry point. Starts the Swing UI by launching the login screen.


---

## Page Responsibilities & SQL Operations

| UI Page / Window            | Description / Actions                                                                 | Example SQL Operations Used                                                       |
|----------------------------|----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| **LoginPage**              | Customer/Artist login                                                                 | `CALL LoginUser(?, ?, ?, ?)`                                                      |
| **RegisterPage**           | Register new users (Customer/Artist)                                                  | `CALL RegisterCustomer(?, ?, ?, ?, ?)` & `CALL RegisterArtist(?, ?, ?, ?, ?, ?, ?)` |
| **HomePage**               | Welcome page + navigation                                                              | –                                                                                 |
| **ArtworksPage**           | Lists all available artworks, search, filter, view countdown                          | `CALL SearchArtworksWithCategory(?, ?)`                                           |
| **FavoritesPage**          | Lists artworks added to favorites by the user                                         | `CALL GetFavoritesByCustomer(?)`                                                  |
| **ManageArtworksPage**     | Artists view & manage their own artworks                                              | `DELETE FROM Artwork WHERE ArtworkId = ?`                                         |
| **CustomerOrdersPage**     | Customers view artworks they won after bidding                                        | `SELECT * FROM CustomerOrderView`                                                 |
| **ArtistProfilePage**      | Show artist details when an artwork is clicked                                        | `CALL GetMentorInfoByArtistId(?)`                                          |
| **MakeOfferWindow**        | Allows customers to place bids during countdown                                       | `INSERT INTO Offer (...) VALUES (...)`                                            |
| **RateArtworkWindow**      | Submit artwork ratings                                                                | `CALL SaveOrUpdateRating(?, ?, ?)`                   |
| **OfferHistoryWindow**     | Show past offers for a selected artwork                                               | `CALL GetOfferHistoryForArtwork(?)`                 |

<sub>**Note:** Each row above includes only one sample SQL query for the corresponding UI component. In reality, many of these classes involve multiple SQL operations such as `UPDATE`, `DELETE`, nested `SELECT`, and complex JOIN queries depending on the actions performed by the user.</sub>

---


