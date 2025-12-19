// GameMap.java
// SMELL: Long Method + Data Clump (originally loadMap was large and inline in constructor).
// TECHNIQUE: Extract Method / Extract Class (Map loader).
// REASON: Keep map data and loading logic intact but separate responsibility from UI.
// RESULT: loadMap behavior identical; map kept inline (tileMap) per requirement.

package com.pacman.model;

import com.pacman.constant.Constants;
import java.awt.Image;
import java.util.HashSet;

public class GameMap {

    // tileMap exactly as original - keep inline here and unchanged
    // X = wall, O = skip, P = pac man, ' ' = food
    // Ghosts: b = blue, o = orange, p = pink, r = red
    public static final String[] TILE_MAP = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    // This method fills the provided collections and returns the pacman Block.
    // It mirrors original loadMap() semantics exactly.
    public static Block loadMap(HashSet<Block> walls, HashSet<Block> foods, HashSet<Block> ghosts,
                                Image wallImage, Image blueGhostImage, Image orangeGhostImage,
                                Image pinkGhostImage, Image redGhostImage,
                                Image pacmanRightImage) {

        walls.clear();
        foods.clear();
        ghosts.clear();
        Block pacman = null;

        int rowCount = Constants.ROW_COUNT;
        int columnCount = Constants.COLUMN_COUNT;
        int tileSize = Constants.TILE_SIZE;

        for (int r = 0; r < rowCount; r++) {
            String row = TILE_MAP[r];
            for (int c = 0; c < columnCount; c++) {
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { // wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { // blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { // orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { // pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { // red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { // pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { // food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }

        return pacman;
    }
}
