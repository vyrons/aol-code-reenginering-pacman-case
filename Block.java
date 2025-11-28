// Block.java
// This file contains the Block class which was an inner class in original PacMan.java.
// SMELL: God Class / Bloater (original PacMan had a large inner Block used for many responsibilities).
// TECHNIQUE: Extract Class (promote inner class to top-level class).
// REASON: Keep the Block behavior identical but move it to its own file so code is modular.
// RESULT: Behavior unchanged; now Block can be reused consistently.

import java.awt.Image;

public class Block {
    public int x;
    public int y;
    public int width;
    public int height;
    public Image image;

    public int startX;
    public int startY;
    public char direction = 'U'; // U D L R
    public int velocityX = 0;
    public int velocityY = 0;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    // NOTE: This method preserves the exact logic from the original:
    // - set direction
    // - update velocity
    // - apply one step movement (x += velocityX, y += velocityY)
    // - check collisions against walls; if collides, undo the step and restore previous direction/velocity
    // We accept walls collection as parameter to avoid tight coupling to GamePanel internals.
    public void updateDirection(char newDirection, java.util.HashSet<Block> walls) {
        char prevDirection = this.direction;
        this.direction = newDirection;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall : walls) {
            if (collision(this, wall)) {
                // undo and restore previous direction & velocity (exact original behavior)
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity();
                break;
            }
        }
    }

    public void updateVelocity() {
        // keeps same formula as original: tileSize/4
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -Constants.TILE_SIZE / 4;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = Constants.TILE_SIZE / 4;
        } else if (this.direction == 'L') {
            this.velocityX = -Constants.TILE_SIZE / 4;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = Constants.TILE_SIZE / 4;
            this.velocityY = 0;
        }
    }

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    // Axis-aligned bounding-box collision check (kept identical to original)
    public static boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
}
