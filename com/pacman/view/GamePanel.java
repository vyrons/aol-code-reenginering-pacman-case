package com.pacman.view;

import com.pacman.constant.Constants;
import com.pacman.constant.Direction;
import com.pacman.model.Block;
import com.pacman.model.GameModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private GameModel model;

    // Images
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Game loop
    private Timer gameLoop;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        loadImages();

        model = new GameModel();
        model.init(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);

        gameLoop = new Timer(Constants.GAME_LOOP_DELAY_MS, this);
        gameLoop.start();
    }

    private void loadImages() {
        wallImage = new ImageIcon(getClass().getResource("../assets/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("../assets/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("../assets/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("../assets/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("../assets/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("../assets/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("../assets/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("../assets/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("../assets/pacmanRight.png")).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        Block pacman = model.getPacman();
        if (pacman != null && pacman.getImage() != null) {
            g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight(), null);
        }

        for (Block ghost : model.getGhosts()) {
            if (ghost.getImage() != null) g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null);
        }

        for (Block wall : model.getWalls()) {
            if (wall.getImage() != null) g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null);
        }

        g.setColor(Color.WHITE);
        for (Block food : model.getFoods()) {
            g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (model.isGameOver()) {
            g.drawString("Game Over: " + String.valueOf(model.getScore()), Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
        } else {
            g.drawString("x" + String.valueOf(model.getLives()) + " Score: " + String.valueOf(model.getScore()),
                         Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.update();
        model.checkLevelComplete(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
        repaint();
        if (model.isGameOver()) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (model.isGameOver()) {
            model.resetGame(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
            gameLoop.start();
            return;
        }

        int code = e.getKeyCode();
        Block pacman = model.getPacman();
        if (code == KeyEvent.VK_UP) {
            pacman.updateDirection(Direction.UP, model.getWalls());
        } else if (code == KeyEvent.VK_DOWN) {
            pacman.updateDirection(Direction.DOWN, model.getWalls());
        } else if (code == KeyEvent.VK_LEFT) {
            pacman.updateDirection(Direction.LEFT, model.getWalls());
        } else if (code == KeyEvent.VK_RIGHT) {
            pacman.updateDirection(Direction.RIGHT, model.getWalls());
        }

        if (pacman.getDirection() == Direction.UP) {
            pacman.setImage(pacmanUpImage);
        } else if (pacman.getDirection() == Direction.DOWN) {
            pacman.setImage(pacmanDownImage);
        } else if (pacman.getDirection() == Direction.LEFT) {
            pacman.setImage(pacmanLeftImage);
        } else if (pacman.getDirection() == Direction.RIGHT) {
            pacman.setImage(pacmanRightImage);
        }
    }
}
