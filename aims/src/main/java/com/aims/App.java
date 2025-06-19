package com.aims;

import javax.swing.SwingUtilities;
import com.aims.view.MainFrame;
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}