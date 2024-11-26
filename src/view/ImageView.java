package view;

import model.Image;

/**
 * This interface represents the view component in the image processing system. It provides methods
 * to display messages and errors to the user.
 */
public interface ImageView {

  /**
   * Displays a message to the user.
   *
   * @param message the message to display
   */
  void displayMessage(String message);

  /**
   * Displays an error message to the user.
   *
   * @param error the error message to display
   */
  void displayError(String error);

  /**
   * Updates the histogram display in the view with the given RGB channel data.
   *
   * @param red   the red channel histogram data
   * @param green the green channel histogram data
   * @param blue  the blue channel histogram data
   */
  void updateHistogram(int[] red, int[] green, int[] blue);

  /**
   * Displays an image in the view.
   *
   * @param image the image to display
   */
  void setImage(Image image);

}
