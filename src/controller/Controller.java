package controller;

import java.io.IOException;
import model.Image;


/**
 * This interface represents the Controller in the image processing system. The controller is
 * responsible for handling user commands, loading, saving, and processing images, as well as
 * combining and splitting RGB channels.
 */
public interface Controller {

  /**
   * Executes a command given as a string input.
   *
   * @param commandLine the command input as a string
   * @throws IOException if an I/O error occurs during execution
   */
  void execute(String commandLine) throws IOException;

  /**
   * Loads an image from the given file path and assigns it a name.
   *
   * @param filePath the path of the image file to be loaded
   * @param name     the name to refer to the loaded image
   * @throws IOException if an error occurs while loading the image
   */
  void loadImage(String filePath, String name) throws IOException;

  /**
   * Saves the image with the given name to the specified file path.
   *
   * @param filePath the path to save the image to
   * @param name     the name of the image to save
   * @throws IOException if an error occurs while saving the image
   */
  void saveImage(String filePath, String name) throws IOException;


  /**
   * Processes an image with the given operation and value, producing a new image.
   *
   * @param inputName  the name of the input image
   * @param outputName the name for the output image
   * @param operation  the operation to apply on the image (e.g., brighten, darken)
   * @param value      the value used in the operation (e.g., brightness increment)
   * @throws IOException if an error occurs during processing
   */
  void processImage(String inputName, String outputName, String operation, int... value)
      throws IOException;

  /**
   * Processes an image with the given operation and a mask image, producing a new image.
   *
   * @param inputName  the name of the input image
   * @param outputName the name for the output image
   * @param operation  the operation to apply on the image (e.g., blur, sharpen, greyscale)
   * @param maskImage  the mask image defining the regions for the operation
   * @throws IOException if an error occurs during processing
   */
  void processImageWithMask(String inputName, String outputName, String operation, Image maskImage)
      throws IOException;

  /**
   * Splits an image into its red, green, and blue components, storing each in a new image.
   *
   * @param inputName the name of the input image
   * @param redName   the name for the red component image
   * @param greenName the name for the green component image
   * @param blueName  the name for the blue component image
   * @throws IOException if an error occurs during processing
   */
  void rgbSplit(String inputName, String redName, String greenName, String blueName)
      throws IOException;

  /**
   * Combines three images (red, green, blue components) into one image.
   *
   * @param destName  the name for the combined image
   * @param redName   the name of the red component image
   * @param greenName the name of the green component image
   * @param blueName  the name of the blue component image
   * @throws IOException if an error occurs during processing
   */
  void rgbCombine(String destName, String redName, String greenName, String blueName)
      throws IOException;

  /**
   * Handles a GUI-specific operation triggered by user interaction.
   *
   * @param command the GUI-specific command string
   * @throws IOException if an error occurs during execution
   */
  void handleGuiCommand(String command) throws IOException;

  Image getCurrentImage();

}
