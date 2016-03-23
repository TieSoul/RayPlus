package raytracer.math;

/**
 * Created by Thijs on 23/11/2015.
 */
public class Color {
    public double red;
    public double green;
    public double blue;
    public Color(int rgb) {
        red = Math.pow(((rgb >> 16) & 0xFF)/255.0, 2.2);
        green = Math.pow(((rgb >> 8) & 0xFF)/255.0, 2.2);
        blue = Math.pow((rgb & 0xFF)/255.0, 2.2);
    }
    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public Color(int red, int green, int blue) {
        this.red = Math.pow(red/255.0, 2.2);
        this.green = Math.pow(green/255.0, 2.2);
        this.blue = Math.pow(blue/255.0, 2.2);
    }
    public Color add(Color other) {
        return new Color(red + other.red, green + other.green, blue + other.blue);
    }
    public Color mult(Color other) {
        return new Color(red * other.red, green * other.green, blue * other.blue);
    }
    public Color mult(double c) {
        return new Color(red * c, green * c, blue * c);
    }
    public Color divide(double c) {
        return new Color(red / c, green / c, blue / c);
    }
    public Color clamp() {
        Color c = new Color(red, green, blue);
        double maxv = Math.max(red, Math.max(blue, green));
        if (maxv > 1) return c.divide(maxv);
        return c;
    }
    public int getRGB() {
        return (((int)(Math.pow(red, 1/2.2)*255) << 16) + ((int)(Math.pow(green, 1/2.2)*255) << 8) + (int)(Math.pow(blue, 1/2.2)*255));
    }

    public static final Color WHITE = new Color(1.0, 1.0, 1.0);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color BLUE = new Color(0, 0, 1.0);
    public static final Color RED = new Color(1.0, 0, 0);
    public static final Color GREEN = new Color(0, 1.0, 0);
    public static final Color MAGENTA = new Color(1.0, 0, 1.0);
    public static final Color YELLOW = new Color(1.0, 1.0, 0);
    public static final Color CYAN = new Color(0, 1.0, 1.0);
}
