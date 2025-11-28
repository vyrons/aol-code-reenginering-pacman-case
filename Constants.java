// Constants.java
// SMELL: Magic Numbers / Primitive Obsession (tileSize, board dimensions, speeds embedded in code)
// TECHNIQUE: Introduce Constant (centralize configuration)
// REASON: Make tuning safe and ensure any derived values remain consistent across refactor
// RESULT: Same runtime behavior, easier to maintain and reason about.

public class Constants {
    public static final int ROW_COUNT = 21;
    public static final int COLUMN_COUNT = 19;
    public static final int TILE_SIZE = 32;

    public static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE;
    public static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;

    // Game loop delay kept exactly as original
    public static final int GAME_LOOP_DELAY_MS = 50;

    // Speeds derived from original formula
    public static final int PACMAN_SPEED = TILE_SIZE / 4;
    public static final int GHOST_SPEED = TILE_SIZE / 4; // original used tileSize/4 for updateVelocity
}
