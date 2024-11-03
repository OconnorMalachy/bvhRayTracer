public class Color {
    private int red;
    private int green;
    private int blue;

    // Constructor
    public Color(int red, int green, int blue) {
        this.red = clamp(red);
        this.green = clamp(green);
        this.blue = clamp(blue);
    }

    // Getters for RGB values
    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    // Method to clamp RGB values between 0 and 255
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    // Static method to create a random color
    public static Color random() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
}

