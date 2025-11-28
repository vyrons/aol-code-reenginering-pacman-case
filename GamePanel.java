// GamePanel.java
// SMELL: God Class / Bloater (original PacMan combined UI, game loop, map loading and entity logic).
// TECHNIQUE: Extract Class (Block, GameMap), Extract Method (loadImages, loadMap, move etc.), Introduce Constant.
// REASON: Separate responsibilities while keeping the original behavior exactly.
// RESULT: Same gameplay and mechanics; code is modular and ready for report.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Collections and entities (kept HashSet as original)
    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Block> ghosts;
    private Block pacman;

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

    // Game loop & state (kept identical)
    private Timer gameLoop;
    private char[] directions = {'U', 'D', 'L', 'R'}; // up down left right
    private Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    // Constructor (keeps original sequence and behavior)
    public GamePanel() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT));
        setBackground(Color.BLACK);

        // keep key listener behavior same as original
        addKeyListener(this);
        setFocusable(true);

        // Load assets (extracted)
        loadImages();

        // Initialize collections
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        // Load map (exact original logic)
        pacman = GameMap.loadMap(walls, foods, ghosts,
                wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage,
                pacmanRightImage);

        // For each ghost pick initial random direction (original behavior)
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, walls);
        }

        // Timer same as original (50 ms)
        gameLoop = new Timer(Constants.GAME_LOOP_DELAY_MS, this);
        gameLoop.start();
    }

    // Load images extracted from original constructor
    private void loadImages() {
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
    }

    // Paint & draw logic preserved exactly (order same as original)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // draw pacman first as original did
        if (pacman != null && pacman.image != null) {
            g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        }

        // ghosts
        for (Block ghost : ghosts) {
            if (ghost.image != null) g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        // walls
        for (Block wall : walls) {
            if (wall.image != null) g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        // foods (draw small rectangles)
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        // HUD & score (same formatting)
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
        } else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score),
                         Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
        }
    }

    // Core move() logic preserved (kept structure and order identical)
    private void move() {

        // pacman move using velocity set previously in updateDirection
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // check wall collisions for pacman (undo)
        for (Block wall : walls) {
            if (Block.collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // ghost movement logic (kept identical)
        for (Block ghost : new HashSet<>(ghosts)) {
            // original forced upward behavior at row == 9*tileSize
            if (ghost.y == Constants.TILE_SIZE * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U', walls);
            }
            // apply movement
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            // collision with walls or world bounds
            if (isGhostCollisionWithWallOrBounds(ghost)) {
                // undo
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                // choose new random direction and apply updateDirection which does the one-step test like original
                char newDirection = directions[random.nextInt(directions.length)];
                ghost.updateDirection(newDirection, walls);
            }
        }

        // check ghost collisions with pacman
        for (Block ghost : ghosts) {
            if (Block.collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
        }

        // check food collision (replicate exact loop from original)
        Block foodEaten = null;
        for (Block food : foods) {
            if (Block.collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        if (foodEaten != null) foods.remove(foodEaten);

        // if all foods eaten -> reload map and reset positions (same as original)
        if (foods.isEmpty()) {
            pacman = GameMap.loadMap(walls, foods, ghosts, wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
            resetPositions();
        }
    }

    private boolean isGhostCollisionWithWallOrBounds(Block ghost) {
        // check wall collisions
        for (Block wall : walls) {
            if (Block.collision(ghost, wall)) return true;
        }
        // check horizontal bounds as in original
        if (ghost.x <= 0 || ghost.x + ghost.width >= Constants.BOARD_WIDTH) return true;
        return false;
    }

    private void resetPositions() {
        // original reset behavior: reset pacman and ghosts start positions; then set velocities etc.
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(directions.length)];
            ghost.updateDirection(newDirection, walls);
        }
    }

    // ActionListener (game loop) - same as original
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    // Key handling: original had empty keyPressed, main logic in keyReleased
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        // original restart behavior when game over
        if (gameOver) {
            pacman = GameMap.loadMap(walls, foods, ghosts, wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
            return;
        }

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            pacman.updateDirection('U', walls);
        } else if (code == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D', walls);
        } else if (code == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L', walls);
        } else if (code == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R', walls);
        }

        // update pacman sprite exactly as original
        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}
