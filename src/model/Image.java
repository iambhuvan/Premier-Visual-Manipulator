package model;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

/**
 * This class represents an image consisting of a grid of pixels. Each image has a specified width
 * and height, and each pixel is represented by an instance of the Pixel class.
 */
public class Image {

  private int width;
  private int height;
  private Pixel[][] pixels;
  private int[][][] imageData;

  /**
   * Constructs an Image with the specified width and height. Initializes all pixels to black (RGB:
   * 0, 0, 0).
   *
   * @param width  the width of the image in pixels
   * @param height the height of the image in pixels
   */
  public Image(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new Pixel[height][width];
    this.imageData = new int[3][height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        pixels[y][x] = new Pixel(0, 0, 0);
        imageData[0][y][x] = 0;
        imageData[1][y][x] = 0;
        imageData[2][y][x] = 0;
      }
    }
  }

  /**
   * Constructs an Image from the provided image data.
   *
   * @param imageData the 3D array representing the image data
   * @param width     the width of the image in pixels
   * @param height    the height of the image in pixels
   */
  public Image(int[][][] imageData, int width, int height) {
    this.imageData = imageData;
    this.width = width;
    this.height = height;
    this.pixels = new Pixel[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        pixels[y][x] = new Pixel(imageData[0][y][x], imageData[1][y][x], imageData[2][y][x]);
      }
    }
  }

  /**
   * Gets the width of the image in pixels.
   *
   * @return the width of the image
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of the image in pixels.
   *
   * @return the height of the image
   */
  public int getHeight() {
    return height;
  }

  /**
   * Retrieves the pixel located at the specified (x, y) position.
   *
   * @param x the x-coordinate of the pixel
   * @param y the y-coordinate of the pixel
   * @return the Pixel at the specified position
   */
  public Pixel getPixel(int x, int y) {
    return pixels[y][x];
  }

  /**
   * Sets the pixel at the specified (x, y) position to the provided {@link Pixel} value.
   *
   * @param x     the x-coordinate of the pixel
   * @param y     the y-coordinate of the pixel
   * @param pixel the pixel to set at the specified position
   */
  public void setPixel(int x, int y, Pixel pixel) {
    pixels[y][x] = pixel;
    imageData[0][y][x] = pixel.getRed();
    imageData[1][y][x] = pixel.getGreen();
    imageData[2][y][x] = pixel.getBlue();
  }

  /**
   * Gets the image data as a 3D array.
   *
   * @return the 3D array representing the image data
   */
  public int[][][] getImageData() {
    return imageData;
  }

  /**
   * Updates the image data from the pixels array.
   */
  private void updateImageDataFromPixels() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pixel pixel = pixels[y][x];
        imageData[0][y][x] = pixel.getRed();
        imageData[1][y][x] = pixel.getGreen();
        imageData[2][y][x] = pixel.getBlue();
      }
    }
  }

  /**
   * Updates the pixels array from the image data.
   */
  private void updatePixelsFromImageData() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        pixels[y][x] = new Pixel(imageData[0][y][x], imageData[1][y][x], imageData[2][y][x]);
      }
    }
  }

  /**
   * Converts the custom Image to an ImageIcon for GUI display.
   *
   * @return the ImageIcon representation of this image.
   */
  public ImageIcon toImageIcon() {
    BufferedImage bufferedImage = this.toBufferedImage(); // Ensure this method exists
    return new ImageIcon(bufferedImage);
  }

  /**
   * Converts the custom Image to a BufferedImage.
   *
   * @return the BufferedImage representation of this image.
   */
  public BufferedImage toBufferedImage() {
    int width = getWidth();  // Get image width
    int height = getHeight(); // Get image height

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pixel pixel = getPixel(x, y); // Get the Pixel object
        if (pixel != null) {
          int argb = pixel.toARGB(); // Convert Pixel to ARGB
          bufferedImage.setRGB(x, y, argb);
        }
      }
    }

    return bufferedImage;
  }

}