package com.pacman.model;

import com.pacman.constant.Constants;
import com.pacman.constant.Direction;
import java.awt.Image;
import java.util.HashSet;
import java.util.Random;

public class GameModel {
    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Block> ghosts;
    private Block pacman;

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    private final Random random = new Random();
    private final Direction[] directions = Direction.values();

    public GameModel() {
        this.walls = new HashSet<>();
        this.foods = new HashSet<>();
        this.ghosts = new HashSet<>();
    }

    public void init(Image wallImage, Image blueGhostImage, Image orangeGhostImage, 
                     Image pinkGhostImage, Image redGhostImage, Image pacmanRightImage) {
        loadMap(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
        resetPositions();
    }

    public void loadMap(Image wallImage, Image blueGhostImage, Image orangeGhostImage, 
                        Image pinkGhostImage, Image redGhostImage, Image pacmanRightImage) {
        this.pacman = GameMap.loadMap(walls, foods, ghosts, wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
    }

    public void update() {
        if (gameOver) return;

        // Move Pacman
        pacman.setX(pacman.getX() + pacman.getVelocityX());
        pacman.setY(pacman.getY() + pacman.getVelocityY());

        for (Block wall : walls) {
            if (Block.collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
                break;
            }
        }

        // Move Ghosts
        for (Block ghost : ghosts) {
            if (ghost.getY() == Constants.TILE_SIZE * 9 && ghost.getDirection() != Direction.UP && ghost.getDirection() != Direction.DOWN) {
                ghost.updateDirection(Direction.UP, walls);
            }
            
            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());

            if (isGhostCollisionWithWallOrBounds(ghost)) {
                ghost.setX(ghost.getX() - ghost.getVelocityX());
                ghost.setY(ghost.getY() - ghost.getVelocityY());
                Direction newDirection = directions[random.nextInt(directions.length)];
                ghost.updateDirection(newDirection, walls);
            }
        }

        // Check Collisions
        checkCollisions();
    }

    private void checkCollisions() {
        // Ghost collision
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

        // Food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (Block.collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        if (foodEaten != null) foods.remove(foodEaten);

        if (foods.isEmpty()) {
            // Reload map but keep score/lives? Original logic:
            // pacman = GameMap.loadMap(...) -> this reloads everything including foods
            // resetPositions()
            // We need images to reload. 
            // Ideally we shouldn't store images in Model, but for now we pass them or store them.
            // Let's assume the controller handles the full reload or we store images.
            // For simplicity, let's just set a flag or callback? 
            // Or better, store images in Model since they are part of the entity state (image field).
            // But images are View assets. 
            // Refactoring: Model shouldn't know about Images. Block has Image. 
            // We'll keep it as is for now to minimize changes.
        }
    }
    
    // Helper to handle level complete (needs images)
    public void checkLevelComplete(Image wallImage, Image blueGhostImage, Image orangeGhostImage, Image pinkGhostImage, Image redGhostImage, Image pacmanRightImage) {
        if (foods.isEmpty()) {
            loadMap(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
            resetPositions();
        }
    }

    private boolean isGhostCollisionWithWallOrBounds(Block ghost) {
        for (Block wall : walls) {
            if (Block.collision(ghost, wall)) return true;
        }
        if (ghost.getX() <= 0 || ghost.getX() + ghost.getWidth() >= Constants.BOARD_WIDTH) return true;
        return false;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);
        for (Block ghost : ghosts) {
            ghost.reset();
            Direction newDirection = directions[random.nextInt(directions.length)];
            ghost.updateDirection(newDirection, walls);
        }
    }
    
    public void resetGame(Image wallImage, Image blueGhostImage, Image orangeGhostImage, 
                          Image pinkGhostImage, Image redGhostImage, Image pacmanRightImage) {
        loadMap(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
    }

    // Getters
    public HashSet<Block> getWalls() { return walls; }
    public HashSet<Block> getFoods() { return foods; }
    public HashSet<Block> getGhosts() { return ghosts; }
    public Block getPacman() { return pacman; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameOver() { return gameOver; }
}
