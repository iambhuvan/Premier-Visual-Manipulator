package utility;

import model.Image;

/**
 * Utility class for handling image reading and writing operations. Supports multiple formats
 * including PPM, PNG, and JPG.
 */
public class ImageUtil {

  /**
   * Reads an image from the specified file path.
   *
   * @param filePath the path of the image file to read
   * @return the Image object, or null if an error occurs or the format is unsupported
   */
  public static Image readImage(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      System.out.println("Error: File path cannot be null or empty.");
      return null;
    }

    String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
    try {
      switch (extension) {
        case "ppm":
          return PPMHandler.readPPM(filePath);
        case "png":
        case "jpg":
        case "jpeg":
          return OtherFormatHandler.readImage(filePath);
        default:
          System.out.println("Unsupported file format: " + extension);
          return null;
      }
    } catch (Exception e) {
      System.out.println("Error reading image: " + e.getMessage());
      return null;
    }
  }

  /**
   * Writes an image to the specified file path.
   *
   * @param image    the Image object to write
   * @param filePath the path to save the image
   * @return true if the image is written successfully, false otherwise
   */
  public static boolean writeImage(Image image, String filePath) {
    if (image == null) {
      System.out.println("Error: Image object cannot be null.");
      return false;
    }
    if (filePath == null || filePath.trim().isEmpty()) {
      System.out.println("Error: File path cannot be null or empty.");
      return false;
    }

    String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
    try {
      switch (extension) {
        case "ppm":
          PPMHandler.writePPM(image, filePath);
          return true;
        case "png":
        case "jpg":
        case "jpeg":
          return OtherFormatHandler.writeImage(image, filePath);
        default:
          System.out.println("Unsupported file format: " + extension);
          return false;
      }
    } catch (Exception e) {
      System.out.println("Error writing image: " + e.getMessage());
      return false;
    }
  }
}
