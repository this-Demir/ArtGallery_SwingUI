package app.models;

public class CurrentUser {

    public static String currentUser = null;
    public static String currentArtist = null;

    public void displayInfo() {
        System.out.println("UserId:" + currentUser);
        System.out.println("ArtistId" + currentArtist);
    }
}
