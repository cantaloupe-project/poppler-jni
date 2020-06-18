package edu.illinois.library.poppler;

public class Dimension {

    private final double width, height;

    Dimension(double width, double height) {
        this.width  = width;
        this.height = height;
    }

    public double height() {
        return height;
    }

    public double width() {
        return width;
    }

}
