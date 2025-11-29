public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public char toChar() {
        switch (this) {
            case UP: return 'U';
            case DOWN: return 'D';
            case LEFT: return 'L';
            case RIGHT: return 'R';
            default: return ' ';
        }
    }
}
