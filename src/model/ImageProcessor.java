package model;

/**
 * This interface defines various operations for processing images. The operations include channel
 * manipulation, flipping, brightening, darkening, filtering, and applying color transformations
 * such as greyscale or sepia conversion.
 */
public interface ImageProcessor {

  /**
   * Combines the red, green, and blue channels from three separate images into one color image.
   *
   * @param red   the image representing the red channel
   * @param green the image representing the green channel
   * @param blue  the image representing the blue channel
   * @return a new Image with combined RGB channels
   */
  Image combineChannels(Image red, Image green, Image blue);

  /**
   * Flips the given image horizontally.
   *
   * @param image the image to flip
   * @return the horizontally flipped Image
   */
  Image flipHorizontal(Image image);

  /**
   * Flips the given image vertically.
   *
   * @param image the image to flip
   * @return the vertically flipped Image
   */
  Image flipVertical(Image image);

  /**
   * Adjusts the brightness of the given image by the specified value.
   *
   * @param image      the image to adjust
   * @param adjustment the amount to adjust the brightness by (positive for brightening, negative
   *                   for darkening)
   * @return a new Image with the brightness adjusted
   */
  Image brightness(Image image, int adjustment);

  /**
   * Visualizes the red component of the given image by setting the red value in each pixel while
   * setting green and blue to zero.
   *
   * @param image the image to visualize the red component of
   * @return a new Image visualizing the red component
   */
  Image visualizeRedComponent(Image image);

  /**
   * Visualizes the green component of the given image by setting the green value in each pixel
   * while setting red and blue to zero.
   *
   * @param image the image to visualize the green component of
   * @return a new Image visualizing the green component
   */
  Image visualizeGreenComponent(Image image);

  /**
   * Visualizes the blue component of the given image by setting the blue value in each pixel while
   * setting red and green to zero.
   *
   * @param image the image to visualize the blue component of
   * @return a new Image visualizing the blue component
   */
  Image visualizeBlueComponent(Image image);

  /**
   * Darkens the given image by adjusting the brightness by a negative value.
   *
   * @param image      the image to darken
   * @param adjustment the amount to decrease the brightness by
   * @return a new Image with the brightness decreased
   */
  Image darkness(Image image, int adjustment);

  /**
   * Applies a specified filter kernel to the given image.
   *
   * @param image  the image to apply the filter to
   * @param kernel a 2D array representing the filter kernel
   * @return a new Image with the filter applied
   */
  Image applyFilter(Image image, double[][] kernel);

  /**
   * Blurs the given image using a Gaussian blur filter.
   *
   * @param image the image to blur
   * @return a blurred Image
   */
  Image blur(Image image);

  /**
   * Sharpens the given image using a sharpening filter.
   *
   * @param image the image to sharpen
   * @return a sharpened Image
   */
  Image sharpen(Image image);

  /**
   * Applies a color transformation matrix to the given image.
   *
   * @param image  the image to transform
   * @param matrix a 2D array representing the color transformation matrix
   * @return a new Image with the color transformation applied
   */
  Image applyColorTransformation(Image image, double[][] matrix);

  /**
   * Converts the given image to greyscale.
   *
   * @param image the image to convert
   * @return a greyscale Image
   */
  Image toGreyscale(Image image);

  /**
   * Converts the given image to sepia tone.
   *
   * @param image the image to convert
   * @return a sepia-toned Image
   */
  Image toSepia(Image image);

  /**
   * Visualizes the value of the image, where the value is the maximum of the red, green, and blue
   * components for each pixel.
   *
   * @param image the image to visualize the value of
   * @return a new Image visualizing the value
   */
  Image visualizeValue(Image image);

  /**
   * Visualizes the intensity of the image, where the intensity is the average of the red, green,
   * and blue components for each pixel.
   *
   * @param image the image to visualize the intensity of
   * @return a new Image visualizing the intensity
   */
  Image visualizeIntensity(Image image);

  /**
   * Visualizes the luma of the image, where the luma is the weighted sum of the red, green, and
   * blue components for each pixel.
   *
   * @param image the image to visualize the luma of
   * @return a new Image visualizing the luma
   */
  Image visualizeLuma(Image image);

  /**
   * Splits the given image into three separate images representing the red, green, and blue
   * channels.
   *
   * @param image the image to split
   * @return an array of three Image objects representing the red, green, and blue channels
   */
  Image[] splitChannels(Image image);

  /**
   * Compresses the given image by reducing the data size, applying either lossy or lossless
   * compression. The specified percentage determines the extent of compression, with higher values
   * indicating greater data reduction. A higher percentage implies a more noticeable decrease in
   * quality due to discarded data in lossy compression.
   *
   * @param image      the image to compress
   * @param percentage the percentage of data to reduce in the compression process (0-100)
   * @return a new Image representing the compressed version of the original
   */
  Image compress(Image image, int percentage);

  /**
   * Generates a histogram image for the given image, displaying the frequency distribution of pixel
   * intensity values. The histogram includes separate frequency graphs for each color channel (red,
   * green, and blue).
   *
   * @param image the image for which the histogram will be generated
   * @return a 256x256 Image visualizing the histogram of the red, green, and blue channels
   */
  Image generateHistogram(Image image);

  /**
   * Adjusts the colors of the given image to align the peaks of each color channel's histogram,
   * helping to balance color representation by offsetting channel values to align around a common
   * point. Useful for correcting color imbalances in images, such as red or blue tints.
   *
   * @param image the image to color correct
   * @return a new Image with corrected color values
   */
  Image colorCorrect(Image image);

  /**
   * Adjusts the levels of the given image based on specified shadow, midtone, and highlight values.
   * These values define points on the histogram that represent shadows (dark regions), midtones,
   * and highlights (bright regions). Each point modifies pixel intensity, allowing fine control
   * over image contrast and brightness.
   *
   * @param image           the image to adjust
   * @param shadowsValue    the intensity value for shadows (0-255)
   * @param midtonesValue   the intensity value for midtones (0-255)
   * @param highlightsValue the intensity value for highlights (0-255)
   * @return a new Image with adjusted levels to enhance contrast and brightness
   */
  Image levelsAdjust(Image image, int shadowsValue, int midtonesValue, int highlightsValue);

  /**
   * Combines the original and processed images into a single image with a split view.
   *
   * @param original        the original image to be displayed.
   * @param processed       the processed image to be displayed.
   * @param splitPercentage the percentage of the width allocated to the original image (0-100).
   * @return an Image combining the original and processed images based on the split percentage.
   * @throws IllegalArgumentException if splitPercentage is not between 0 and 100, or if original or
   *                                  processed is null.
   */
  Image applySplitView(Image original, Image processed, int splitPercentage);

  /**
   * Calculates the histogram data for an image.
   *
   * @param image the image for which the histogram is calculated
   * @return a 2D array containing histogram data for red, green, and blue channels
   */
  int[][] calculateHistogram(Image image);

  /**
   * Downscales the given image to the specified dimensions while maintaining proportional mapping
   * between the source and target pixels.
   *
   * @param sourceImage  the original image to be downscaled
   * @param targetWidth  the width of the downscaled image
   * @param targetHeight the height of the downscaled image
   * @return a new Image with the specified dimensions
   */
  Image downscaleImage(Image sourceImage, int targetWidth, int targetHeight);

  /**
   * Applies the specified image operation to only the regions of the image where the corresponding
   * pixels in the mask image are black. White regions in the mask retain the original image's pixel
   * values.
   *
   * @param sourceImage the image to process
   * @param maskImage   the black-and-white mask image defining regions to apply the operation
   * @param operation   the name of the operation to apply (e.g., blur, sharpen, greyscale, sepia)
   * @return a new Image with the operation applied to the specified regions
   */
  Image applyWithMask(Image sourceImage, Image maskImage, String operation);

}