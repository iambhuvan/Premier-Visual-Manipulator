package view;


/**
 * The TextView class implements the ImageView interface and serves as the view in the MVC
 * (Model-View-Controller) pattern. It handles displaying messages and errors to the user via the
 * console.
 */
public class TextView implements ImageView {

  /**
   * Displays a message to the user by printing it to the standard output.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * Displays an error message to the user by printing it to the standard error output.
   *
   * @param error the error message to display
   */
  @Override
  public void displayError(String error) {
    System.err.println(error);
  }

  /**
   * Updates the histogram display. This implementation does nothing as TextView does not support
   * visual displays.
   *
   * @param red   the red channel histogram data
   * @param green the green channel histogram data
   * @param blue  the blue channel histogram data
   */
  @Override
  public void updateHistogram(int[] red, int[] green, int[] blue) {
    // TextView does not support histograms.
  }


  @Override
  public void setImage(model.Image image) {
    if (image == null) {
      System.out.println("No image to display.");
    } else {
      System.out.println("Image displayed: " + image);
    }
  }

}
