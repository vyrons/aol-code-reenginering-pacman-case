// App.java
// Simple bootstrap: create frame and add GamePanel
// SMELL: Previous main mixed startup & game logic.
// TECHNIQUE: Keep bootstrap separate (single responsibility).
// RESULT: same startup sequence but cleaner structure.

package com.pacman;

import com.pacman.view.GamePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pac Man - Refactored (Strict Copy Mode)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GamePanel panel = new GamePanel();
            frame.add(panel);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            panel.requestFocusInWindow();
            frame.setVisible(true);
        });
    }
}
