package utility;

import model.Image;
import model.Pixel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles reading and writing of PPM (Portable Pixmap) format images.
 */
public class PPMHandler {

  /**
   * Reads a PPM image file and converts it to the Image model.
   *
   * @param filePath the path to the PPM file
   * @return the Image object, or null if reading fails
   */
  public static Image readPPM(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String magicNumber = br.readLine();
      if (!magicNumber.equals("P3")) {
        throw new IOException("Invalid PPM file format.");
      }

      br.readLine(); // Skip the comment line
      String[] dimensions = br.readLine().split(" ");
      int width = Integer.parseInt(dimensions[0]);
      int height = Integer.parseInt(dimensions[1]);
      int maxColorValue = Integer.parseInt(br.readLine()); // Max color value (e.g., 255)

      Image image = new Image(width, height);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int red = Integer.parseInt(br.readLine());
          int green = Integer.parseInt(br.readLine());
          int blue = Integer.parseInt(br.readLine());
          image.setPixel(x, y, new Pixel(red, green, blue));
        }
      }

      return image;
    } catch (IOException e) {
      System.out.println("Error reading PPM file: " + e.getMessage());
      return null;
    }
  }

  /**
   * Writes an Image object to a PPM file.
   *
   * @param image    the Image object to write
   * @param filePath the path to save the PPM file
   * @throws IOException if writing fails
   */
  public static void writePPM(Image image, String filePath) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
      bw.write("P3\n");
      bw.write("# Created by Image Processor\n");
      bw.write(image.getWidth() + " " + image.getHeight() + "\n");
      bw.write("255\n");

      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          Pixel pixel = image.getPixel(x, y);
          bw.write(pixel.getRed() + "\n");
          bw.write(pixel.getGreen() + "\n");
          bw.write(pixel.getBlue() + "\n");
        }
      }
    }
  }
}
