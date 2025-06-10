package app;

import app.ui.pages.HomePage;
import javax.swing.*;

public class main {
    public static void main(String[] args) {
        // IN FIRST TIME RUN
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}
