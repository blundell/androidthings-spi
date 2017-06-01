package com.blundell.tut;

final class Color {
    private static final Color RED = new Color(50, 0, 0);
    private static final Color YELLOW = new Color(50, 50, 0);
    private static final Color PINK = new Color(50, 10, 12);
    private static final Color GREEN = new Color(0, 50, 0);
    private static final Color PURPLE = new Color(50, 0, 50);
    private static final Color ORANGE = new Color(50, 22, 0);
    private static final Color BLUE = new Color(0, 0, 50);

    static final Color[] RAINBOW = {
            Color.RED, Color.YELLOW, Color.PINK,
            Color.GREEN,
            Color.PURPLE, Color.ORANGE, Color.BLUE
    };

    int r;
    int g;
    int b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
