package utility;

import model.Image;
import model.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Handles reading and writing of images in non-PPM formats (e.g., PNG, JPG).
 */
public class OtherFormatHandler {

  /**
   * Reads an image from a file and converts it to the Image model.
   *
   * @param filename the path to the image file
   * @return the Image object, or null if reading fails
   */
  public static Image readImage(String filename) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File(filename));
      if (bufferedImage == null) {
        throw new IOException("Invalid image format or corrupted file.");
      }
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      Image image = new Image(width, height);

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int rgb = bufferedImage.getRGB(x, y);
          int red = (rgb >> 16) & 0xFF;
          int green = (rgb >> 8) & 0xFF;
          int blue = rgb & 0xFF;
          image.setPixel(x, y, new Pixel(red, green, blue));
        }
      }

      return image;
    } catch (IOException e) {
      System.out.println("Error reading image: " + e.getMessage());
      return null;
    }
  }

  /**
   * Writes an Image object to a file in the specified format.
   *
   * @param image    the Image object to write
   * @param filename the path to save the image
   * @return true if the image is written successfully, false otherwise
   */
  public static boolean writeImage(Image image, String filename) {
    try {
      BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(),
          BufferedImage.TYPE_INT_RGB);
      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          Pixel pixel = image.getPixel(x, y);
          int rgb = (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();
          bufferedImage.setRGB(x, y, rgb);
        }
      }
      String format = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
      return ImageIO.write(bufferedImage, format, new File(filename));
    } catch (IOException e) {
      System.out.println("Error writing image: " + e.getMessage());
      return false;
    }
  }
}
