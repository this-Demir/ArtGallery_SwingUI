package app;

import app.ui.pages.HomePage;
import javax.swing.*;

public class main {
    public static void main(String[] args) {
        // todo: write a full db seeder for first time run  
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}
