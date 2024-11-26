package model;

/**
 * This class represents a single pixel in an image. A pixel consists of three color channels: red,
 * green, and blue, each having an integer value between 0 and 255.
 */
public class Pixel {

  private int red;
  private int green;
  private int blue;

  /**
   * Constructs a Pixel with the specified red, green, and blue values. The values are clamped to
   * ensure they remain within the valid range [0, 255].
   *
   * @param red   the red value of the pixel
   * @param green the green value of the pixel
   * @param blue  the blue value of the pixel
   */
  public Pixel(int red, int green, int blue) {
    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  /**
   * Gets the red component of this pixel.
   *
   * @return the red value of the pixel
   */
  public int getRed() {
    return red;
  }

  /**
   * Gets the green component of this pixel.
   *
   * @return the green value of the pixel
   */
  public int getGreen() {
    return green;
  }

  /**
   * Gets the blue component of this pixel.
   *
   * @return the blue value of the pixel
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Returns the value of the specified color channel.
   *
   * @param channel the channel to retrieve (0 for red, 1 for green, 2 for blue)
   * @return the value of the specified color channel
   * @throws IllegalArgumentException if the channel index is invalid
   */
  public int getChannel(int channel) {
    switch (channel) {
      case 0:
        return red;
      case 1:
        return green;
      case 2:
        return blue;
      default:
        throw new IllegalArgumentException("Invalid channel");
    }
  }

  /**
   * Clamps the given value to ensure it remains within the valid range of [0, 255].
   *
   * @param value the value to clamp
   * @return the clamped value within [0, 255]
   */
  private int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }


  /**
   * Converts this pixel to an ARGB integer.
   *
   * @return the ARGB representation of this pixel, assuming alpha is fully opaque.
   */
  public int toARGB() {
    int alpha = 255; // Assume fully opaque
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

}
