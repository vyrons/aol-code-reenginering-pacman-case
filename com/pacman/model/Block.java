package com.pacman.model;

import com.pacman.constant.Constants;
import com.pacman.constant.Direction;
import java.awt.Image;
import java.util.HashSet;

public class Block {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;

    private int startX;
    private int startY;
    private Direction direction = Direction.UP; 
    private int velocityX = 0;
    private int velocityY = 0;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    public void updateDirection(Direction newDirection, HashSet<Block> walls) {
        Direction prevDirection = this.direction;
        this.direction = newDirection;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall : walls) {
            if (collision(this, wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity();
                break;
            }
        }
    }

    public void updateVelocity() {
        if (this.direction == Direction.UP) {
            this.velocityX = 0;
            this.velocityY = -Constants.TILE_SIZE / 4;
        } else if (this.direction == Direction.DOWN) {
            this.velocityX = 0;
            this.velocityY = Constants.TILE_SIZE / 4;
        } else if (this.direction == Direction.LEFT) {
            this.velocityX = -Constants.TILE_SIZE / 4;
            this.velocityY = 0;
        } else if (this.direction == Direction.RIGHT) {
            this.velocityX = Constants.TILE_SIZE / 4;
            this.velocityY = 0;
        }
    }

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    public static boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    // Getters and Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
    public int getVelocityX() { return velocityX; }
    public void setVelocityX(int velocityX) { this.velocityX = velocityX; }
    public int getVelocityY() { return velocityY; }
    public void setVelocityY(int velocityY) { this.velocityY = velocityY; }
}
