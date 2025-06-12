package app;
import app.ui.pages.HomePage;
import javax.swing.*;

public class AppRun {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}
