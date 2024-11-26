package model;

import java.util.Arrays;

/**
 * This class provides concrete implementations for various image processing operations defined in
 * the ImageProcessor interface. The operations include visualizing channels, flipping, brightening,
 * darkening, applying filters, and color transformations such as greyscale and sepia.
 */
public class ImageProcessorImpl implements ImageProcessor {

  /**
   * Visualizes the red component of the given image by setting the red value in each pixel while
   * setting green and blue to zero.
   *
   * @param image the image to visualize the red component of
   * @return a new {@link Image} visualizing the red component
   */
  @Override
  public Image visualizeRedComponent(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int redValue = pixel.getChannel(0);
        result.setPixel(x, y, new Pixel(redValue, redValue, redValue));
      }
    }
    return result;
  }

  /**
   * Visualizes the green component of the given image by setting the green value in each pixel
   * while setting red and blue to zero.
   *
   * @param image the image to visualize the green component of
   * @return a new Image visualizing the green component
   */
  @Override
  public Image visualizeGreenComponent(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int greenValue = pixel.getChannel(1);
        result.setPixel(x, y, new Pixel(greenValue, greenValue, greenValue));
      }
    }
    return result;
  }

  /**
   * Visualizes the blue component of the given image by setting the blue value in each pixel while
   * setting red and green to zero.
   *
   * @param image the image to visualize the blue component of
   * @return a new Image visualizing the blue component
   */
  @Override
  public Image visualizeBlueComponent(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int blueValue = pixel.getChannel(2);
        result.setPixel(x, y, new Pixel(blueValue, blueValue, blueValue));
      }
    }
    return result;
  }

  /**
   * Combines the red, green, and blue channels from three separate images into one color image.
   *
   * @param red   the image representing the red channel
   * @param green the image representing the green channel
   * @param blue  the image representing the blue channel
   * @return a new Image with combined RGB channels
   */
  @Override
  public Image combineChannels(Image red, Image green, Image blue) {
    int width = Math.min(red.getWidth(), Math.min(green.getWidth(), blue.getWidth()));
    int height = Math.min(red.getHeight(), Math.min(green.getHeight(), blue.getHeight()));

    Image result = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = red.getPixel(x, y).getRed();
        int g = green.getPixel(x, y).getGreen();
        int b = blue.getPixel(x, y).getBlue();
        result.setPixel(x, y, new Pixel(r, g, b));
      }
    }

    return result;
  }

  /**
   * Flips the given image horizontally.
   *
   * @param image the image to flip
   * @return the horizontally flipped Image
   */
  @Override
  public Image flipHorizontal(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Image result = new Image(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        result.setPixel(width - 1 - x, y, image.getPixel(x, y));
      }
    }
    return result;
  }

  /**
   * Flips the given image vertically.
   *
   * @param image the image to flip
   * @return the vertically flipped Image
   */
  @Override
  public Image flipVertical(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Image result = new Image(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        result.setPixel(x, height - 1 - y, image.getPixel(x, y));
      }
    }
    return result;
  }

  /**
   * Brightens the given image by the specified adjustment value.
   *
   * @param image      the image to brighten
   * @param adjustment the amount to increase the brightness by
   * @return a new Image with increased brightness
   */
  @Override
  public Image brightness(Image image, int adjustment) {
    return adjustBrightness(image, adjustment);
  }

  /**
   * Darkens the given image by the specified adjustment value.
   *
   * @param image      the image to darken
   * @param adjustment the amount to decrease the brightness by
   * @return a new Image with decreased brightness
   */
  @Override
  public Image darkness(Image image, int adjustment) {
    return adjustBrightness(image, -Math.abs(adjustment));
  }

  /**
   * Adjusts the brightness of the image.
   *
   * @param image      the image to adjust
   * @param adjustment the brightness adjustment value
   * @return a new Image with the brightness adjusted
   */
  private Image adjustBrightness(Image image, int adjustment) {
    int width = image.getWidth();
    int height = image.getHeight();
    Image result = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pixel pixel = image.getPixel(x, y);
        int r = clamp(pixel.getRed() + adjustment);
        int g = clamp(pixel.getGreen() + adjustment);
        int b = clamp(pixel.getBlue() + adjustment);
        result.setPixel(x, y, new Pixel(r, g, b));
      }
    }
    return result;
  }

  /**
   * Applies a specified filter kernel to the given image.
   *
   * @param image  the image to apply the filter to
   * @param kernel a 2D array representing the filter kernel
   * @return a new Image with the filter applied
   */
  @Override
  public Image applyFilter(Image image, double[][] kernel) {
    int width = image.getWidth();
    int height = image.getHeight();
    int kernelSize = kernel.length;
    int kernelRadius = kernelSize / 2;
    Image result = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
          for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
            int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
            int pixelY = Math.min(Math.max(y + ky, 0), height - 1);
            Pixel pixel = image.getPixel(pixelX, pixelY);
            double kernelValue = kernel[ky + kernelRadius][kx + kernelRadius];
            sumR += pixel.getRed() * kernelValue;
            sumG += pixel.getGreen() * kernelValue;
            sumB += pixel.getBlue() * kernelValue;
          }
        }
        result.setPixel(x, y, new Pixel(clamp((int) sumR), clamp((int) sumG), clamp((int) sumB)));
      }
    }
    return result;
  }

  /**
   * Blurs the given image using a Gaussian blur filter.
   *
   * @param image the image to blur
   * @return a blurred Image
   */
  @Override
  public Image blur(Image image) {
    double[][] blurKernel = {
        {1 / 16.0, 1 / 8.0, 1 / 16.0},
        {1 / 8.0, 1 / 4.0, 1 / 8.0},
        {1 / 16.0, 1 / 8.0, 1 / 16.0}
    };
    return applyFilter(image, blurKernel);
  }

  /**
   * Sharpens the given image using a sharpening filter.
   *
   * @param image the image to sharpen
   * @return a sharpened Image
   */
  @Override
  public Image sharpen(Image image) {
    double[][] sharpenKernel = {
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0}
    };
    return applyFilter(image, sharpenKernel);
  }

  /**
   * Applies a color transformation matrix to the given image.
   *
   * @param image  the image to transform
   * @param matrix a 2D array representing the color transformation matrix
   * @return a new Image with the color transformation applied
   */
  @Override
  public Image applyColorTransformation(Image image, double[][] matrix) {
    int width = image.getWidth();
    int height = image.getHeight();
    Image result = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pixel pixel = image.getPixel(x, y);
        int[] rgb = {pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
        int[] newRgb = new int[3];

        for (int i = 0; i < 3; i++) {
          double newColor = 0;
          for (int j = 0; j < 3; j++) {
            newColor += matrix[i][j] * rgb[j];
          }
          newRgb[i] = clamp((int) newColor);
        }

        result.setPixel(x, y, new Pixel(newRgb[0], newRgb[1], newRgb[2]));
      }
    }
    return result;
  }

  /**
   * Converts the given image to greyscale.
   *
   * @param image the image to convert
   * @return a greyscale Image
   */
  @Override
  public Image toGreyscale(Image image) {
    double[][] greyscaleMatrix = {
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722}
    };
    return applyColorTransformation(image, greyscaleMatrix);
  }

  /**
   * Converts the given image to sepia tone.
   *
   * @param image the image to convert
   * @return a sepia-toned Image
   */
  @Override
  public Image toSepia(Image image) {
    double[][] sepiaMatrix = {
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    };
    return applyColorTransformation(image, sepiaMatrix);
  }

  /**
   * Visualizes the value of the image, where the value is the maximum of the red, green, and blue
   * components for each pixel.
   *
   * @param image the image to visualize the value of
   * @return a new Image visualizing the value
   */
  @Override
  public Image visualizeValue(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int maxRGB = Math.max(pixel.getChannel(0),
            Math.max(pixel.getChannel(1), pixel.getChannel(2)));
        result.setPixel(x, y, new Pixel(maxRGB, maxRGB, maxRGB));
      }
    }
    return result;
  }

  /**
   * Visualizes the intensity of the image, where the intensity is the average of the red, green,
   * and blue components for each pixel.
   *
   * @param image the image to visualize the intensity of
   * @return a new Image visualizing the intensity
   */
  @Override
  public Image visualizeIntensity(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int avg = (pixel.getChannel(0) + pixel.getChannel(1) + pixel.getChannel(2)) / 3;
        result.setPixel(x, y, new Pixel(avg, avg, avg));
      }
    }
    return result;
  }

  /**
   * Visualizes the luma of the image, where the luma is the weighted sum of the red, green, and
   * blue components for each pixel.
   *
   * @param image the image to visualize the luma of
   * @return a new Image visualizing the luma
   */
  @Override
  public Image visualizeLuma(Image image) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int luma = (int) (0.2126 * pixel.getChannel(0) + 0.7152 * pixel.getChannel(1)
            + 0.0722 * pixel.getChannel(2));
        result.setPixel(x, y, new Pixel(luma, luma, luma));
      }
    }
    return result;
  }

  /**
   * Splits the given image into three separate images representing the red, green, and blue
   * channels.
   *
   * @param image the image to split
   * @return an array of three Image objects representing the red, green, and blue channels
   */
  @Override
  public Image[] splitChannels(Image image) {
    Image red = visualizeRedComponent(image);
    Image green = visualizeGreenComponent(image);
    Image blue = visualizeBlueComponent(image);
    return new Image[]{red, green, blue};
  }

  /**
   * Compresses the given image using a 2D Haar Wavelet Transform followed by thresholding to reduce
   * data. This process results in a lossy compression if small values are zeroed based on the
   * specified percentage. After compression, an inverse Haar transform is applied to reconstruct
   * the image.
   *
   * @param image      the original image to be compressed
   * @param percentage the percentage of data to retain (higher values imply less compression)
   * @return a new Image with the specified compression applied
   */
  @Override
  public Image compress(Image image, int percentage) {
    int width = image.getWidth();
    int height = image.getHeight();
    int size = nextPowerOfTwo(Math.max(width, height));
    double[][][] paddedImage = padImage(image.getImageData(), size);
    for (int c = 0; c < 3; c++) {
      paddedImage[c] = haar2D(paddedImage[c], size);
    }
    applyThreshold(paddedImage, percentage);
    for (int c = 0; c < 3; c++) {
      paddedImage[c] = invHaar2D(paddedImage[c], size);
    }
    return createCompressedImage(paddedImage, width, height);
  }

  /**
   * Applies a 2D Haar Wavelet Transform to compress the specified 2D data. The transform is applied
   * to each row and column, reducing spatial data redundancies.
   *
   * @param data the 2D data array to be transformed
   * @param size the size of the data (must be a power of 2)
   * @return the transformed data after Haar Wavelet compression
   */
  private double[][] haar2D(double[][] data, int size) {
    for (int step = size; step > 1; step /= 2) {
      for (int i = 0; i < size; i++) {
        data[i] = haar1D(data[i], step);
      }
      for (int j = 0; j < step; j++) {
        double[] col = new double[step];
        for (int i = 0; i < step; i++) {
          col[i] = data[i][j];
        }
        col = haar1D(col, step);
        for (int i = 0; i < step; i++) {
          data[i][j] = col[i];
        }
      }
    }
    return data;
  }

  /**
   * Applies a 1D Haar Wavelet Transform on the data array to compress it by averaging pairs of
   * values.
   *
   * @param data   the array of data to transform
   * @param length the length of the data to transform
   * @return the transformed data after 1D Haar compression
   */
  private double[] haar1D(double[] data, int length) {
    double[] temp = new double[length];
    for (int i = 0; i < length; i += 2) {
      double avg = (data[i] + data[i + 1]) / Math.sqrt(2);
      double diff = (data[i] - data[i + 1]) / Math.sqrt(2);
      temp[i / 2] = avg;
      temp[length / 2 + i / 2] = diff;
    }
    System.arraycopy(temp, 0, data, 0, length);
    return data;
  }

  /**
   * Reverses a 2D Haar Wavelet Transform on the data to reconstruct the original spatial
   * information.
   *
   * @param data the 2D data array to invert
   * @param size the size of the data (must be a power of 2)
   * @return the reconstructed data after inverse Haar transform
   */
  private double[][] invHaar2D(double[][] data, int size) {
    for (int step = 2; step <= size; step *= 2) {
      for (int j = 0; j < step; j++) {
        double[] col = new double[step];
        for (int i = 0; i < step; i++) {
          col[i] = data[i][j];
        }
        col = invHaar1D(col, step);
        for (int i = 0; i < step; i++) {
          data[i][j] = col[i];
        }
      }
      for (int i = 0; i < size; i++) {
        data[i] = invHaar1D(data[i], step);
      }
    }
    return data;
  }

  /**
   * Reverses a 1D Haar Wavelet Transform to reconstruct original data values.
   *
   * @param data   the array of data to invert
   * @param length the length of the data to invert
   * @return the reconstructed data after 1D inverse Haar transform
   */
  private double[] invHaar1D(double[] data, int length) {
    double[] temp = new double[length];
    int halfLength = length / 2;
    for (int i = 0; i < halfLength; i++) {
      double avg = data[i];
      double diff = data[halfLength + i];
      temp[2 * i] = (avg + diff) / Math.sqrt(2);
      temp[2 * i + 1] = (avg - diff) / Math.sqrt(2);
    }
    System.arraycopy(temp, 0, data, 0, length);
    return data;
  }

  /**
   * Applies a threshold to the transformed data to achieve lossy compression. Values below the
   * calculated threshold (based on the specified percentage) are set to zero.
   *
   * @param data       the 3D data array representing image channels
   * @param percentage the percentage of data to retain
   */
  private void applyThreshold(double[][][] data, int percentage) {
    double[] allValues = getAllValues(data);
    Arrays.sort(allValues);
    int numToZero = (int) (allValues.length * percentage / 100.0);
    double threshold = allValues[numToZero];

    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < data[c].length; i++) {
        for (int j = 0; j < data[c][i].length; j++) {
          if (Math.abs(data[c][i][j]) < threshold) {
            data[c][i][j] = 0;
          }
        }
      }
    }
  }

  /**
   * Pads the image data to the next power of two size, adding zeroes as necessary. Padding is
   * needed for the Haar transform if the image dimensions are not a power of 2.
   *
   * @param imageData the original image data as a 3D array of pixel values
   * @param size      the padded size (next power of two)
   * @return a padded 3D array with each channel extended to the specified size
   */
  private double[][][] padImage(int[][][] imageData, int size) {
    double[][][] paddedImage = new double[3][size][size];
    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < imageData[c].length; i++) {
        for (int j = 0; j < imageData[c][i].length; j++) {
          paddedImage[c][i][j] = imageData[c][i][j];
        }
      }
    }
    return paddedImage;
  }

  /**
   * Creates a new Image object from the compressed data, resizing it to the original dimensions.
   *
   * @param data   the compressed data as a 3D array
   * @param width  the original width of the image
   * @param height the original height of the image
   * @return a new Image constructed from the compressed data
   */
  private Image createCompressedImage(double[][][] data, int width, int height) {
    int[][][] compressedData = new int[3][height][width];
    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          compressedData[c][i][j] = clamp((int) Math.round(data[c][i][j]));
        }
      }
    }
    return new Image(compressedData, width, height);
  }

  /**
   * Calculates the next power of two greater than or equal to the given integer. This is useful for
   * preparing data dimensions for Haar transforms.
   *
   * @param n the integer to find the next power of two for
   * @return the next power of two
   */
  private int nextPowerOfTwo(int n) {
    return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
  }

  /**
   * Retrieves all absolute values from the 3D data array and stores them in a single array. This is
   * useful for threshold calculations during compression.
   *
   * @param data the 3D data array representing image channels
   * @return an array of all absolute values from the data
   */
  private double[] getAllValues(double[][][] data) {
    int totalSize = data[0].length * data[0][0].length * 3;
    double[] allValues = new double[totalSize];
    int index = 0;
    for (int c = 0; c < 3; c++) {
      for (double[] row : data[c]) {
        for (double val : row) {
          allValues[index++] = Math.abs(val);
        }
      }
    }
    return allValues;
  }


  /**
   * Generates a histogram image that visualizes the distribution of pixel intensity values for each
   * color channel (red, green, and blue) in the given image. The histogram is a 256x256 image where
   * each color channel's distribution is drawn as a line graph.
   *
   * @param image the image for which to generate the histogram
   * @return a new Image visualizing the histogram of the red, green, and blue channels
   */
  @Override
  public Image generateHistogram(Image image) {
    int[] redHistogram = new int[256];
    int[] greenHistogram = new int[256];
    int[] blueHistogram = new int[256];

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        redHistogram[pixel.getRed()]++;
        greenHistogram[pixel.getGreen()]++;
        blueHistogram[pixel.getBlue()]++;
      }
    }

    Image histogramImage = new Image(256, 256);

    for (int y = 0; y < 256; y++) {
      for (int x = 0; x < 256; x++) {
        histogramImage.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }

    drawGrid(histogramImage);

    int maxFreq = Math.max(
        Arrays.stream(redHistogram).max().orElse(0),
        Math.max(
            Arrays.stream(greenHistogram).max().orElse(0),
            Arrays.stream(blueHistogram).max().orElse(0)
        )
    );

    drawHistogramLine(histogramImage, redHistogram, new Pixel(255, 0, 0),
        maxFreq);
    drawHistogramLine(histogramImage, greenHistogram, new Pixel(0, 255, 0),
        maxFreq);
    drawHistogramLine(histogramImage, blueHistogram, new Pixel(0, 0, 255),
        maxFreq);

    return histogramImage;
  }

  /**
   * Draws a grid on the given image to enhance the readability of the histogram. The grid lines are
   * spaced at intervals of 32 pixels in both horizontal and vertical directions. This light gray
   * grid provides reference points for interpreting the histogram frequencies.
   *
   * @param image the Image on which to draw the grid
   */
  private void drawGrid(Image image) {
    Pixel gridColor = new Pixel(220, 220, 220);
    for (int i = 0; i < 256; i += 32) {
      for (int j = 0; j < 256; j++) {
        image.setPixel(i, j, gridColor);
        image.setPixel(j, i, gridColor);
      }
    }
  }

  /**
   * Draws a line graph representing the histogram data for a specific color channel on the
   * histogram image. The line graph is scaled to fit within the 256-pixel height based on the
   * maximum frequency of any color value.
   *
   * @param image     the histogram Image on which to draw the line graph
   * @param histogram the array of frequency values for the color channel
   * @param color     the color of the line graph representing this channel
   * @param maxFreq   the maximum frequency across all channels for scaling purposes
   */
  private void drawHistogramLine(Image image, int[] histogram, Pixel color, int maxFreq) {
    int prevX = 0;
    int prevY = 255 - (histogram[0] * 255 / maxFreq);

    for (int x = 1; x < 256; x++) {
      int y = 255 - (histogram[x] * 255 / maxFreq);
      drawLine(image, prevX, prevY, x, y, color);
      prevX = x;
      prevY = y;
    }
  }

  /**
   * Draws a line between two points on the image using the specified color. This method uses
   * Bresenham's line algorithm to ensure a smooth and accurate line.
   *
   * @param image the Image on which to draw the line
   * @param x1    the starting x-coordinate of the line
   * @param y1    the starting y-coordinate of the line
   * @param x2    the ending x-coordinate of the line
   * @param y2    the ending y-coordinate of the line
   * @param color the color of the line
   */
  private void drawLine(Image image, int x1, int y1, int x2, int y2, Pixel color) {
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);
    int sx = x1 < x2 ? 1 : -1;
    int sy = y1 < y2 ? 1 : -1;
    int err = dx - dy;

    while (true) {
      image.setPixel(x1, y1, color);
      if (x1 == x2 && y1 == y2) {
        break;
      }
      int e2 = 2 * err;
      if (e2 > -dy) {
        err -= dy;
        x1 += sx;
      }
      if (e2 < dx) {
        err += dx;
        y1 += sy;
      }
    }
  }

  /**
   * Computes the histogram for an image's red, green, and blue channels.
   *
   * @param image the image to analyze
   * @return a 2D array with frequency data for RGB channels
   * @throws NullPointerException if the image is null
   */
  @Override
  public int[][] calculateHistogram(Image image) {
    int[][] histogram = new int[3][256]; // Red, Green, Blue channels

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y); // Get the Pixel object
        if (pixel != null) {
          histogram[0][pixel.getRed()]++;
          histogram[1][pixel.getGreen()]++;
          histogram[2][pixel.getBlue()]++;
        }
      }
    }

    return histogram;
  }


  /**
   * Adjusts the colors of the given image by aligning the peaks of each color channel's histogram.
   * This process helps to balance color representation by correcting each channel based on its peak
   * frequency, targeting a common average peak across channels.
   *
   * @param image the image to color correct
   * @return a new Image with corrected color values
   */
  @Override
  public Image colorCorrect(Image image) {
    int[][][] histograms = calculateHistograms(image);
    int[] peaks = findMeaningfulPeaks(histograms);
    int averagePeak = (peaks[0] + peaks[1] + peaks[2]) / 3;

    int[] redCorrection = createCorrectionMap(peaks[0], averagePeak);
    int[] greenCorrection = createCorrectionMap(peaks[1], averagePeak);
    int[] blueCorrection = createCorrectionMap(peaks[2], averagePeak);

    return applyColorCorrection(image, redCorrection, greenCorrection, blueCorrection);
  }

  /**
   * Calculates the histograms for each color channel (red, green, and blue) of the provided image.
   * Each histogram represents the frequency distribution of intensity values for that channel.
   *
   * @param image the image for which to calculate histograms
   * @return a 3D array where each channel's histogram is stored in a 256-length array
   */
  private int[][][] calculateHistograms(Image image) {
    int[][][] histograms = new int[3][256][1];
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        histograms[0][pixel.getRed()][0]++;
        histograms[1][pixel.getGreen()][0]++;
        histograms[2][pixel.getBlue()][0]++;
      }
    }
    return histograms;
  }

  /**
   * Finds the most meaningful peak values for each color channel's histogram. Peaks are determined
   * by identifying the highest frequency of intensity values, ignoring extremes at both ends of the
   * histogram as specified (ignoring values below 10 and above 245).
   *
   * @param histograms a 3D array containing histograms for red, green, and blue channels
   * @return an array of peak values for each color channel
   */
  private int[] findMeaningfulPeaks(int[][][] histograms) {
    int[] peaks = new int[3];
    for (int channel = 0; channel < 3; channel++) {
      int maxFreq = 0;
      for (int i = 10; i < 245; i++) {
        if (histograms[channel][i][0] > maxFreq) {
          maxFreq = histograms[channel][i][0];
          peaks[channel] = i;
        }
      }
    }
    return peaks;
  }

  /**
   * Creates a correction map for a color channel based on its current peak and the target peak.
   * This map adjusts intensity values so that the histogram peak of the channel aligns with the
   * average peak, correcting color imbalances.
   *
   * @param currentPeak the current peak intensity for the color channel
   * @param targetPeak  the target intensity to align the color channel to
   * @return an array of corrected intensity values for the color channel
   */
  private int[] createCorrectionMap(int currentPeak, int targetPeak) {
    int[] correctionMap = new int[256];
    for (int i = 0; i < 256; i++) {
      correctionMap[i] = Math.min(255, Math.max(0, i - currentPeak + targetPeak));
    }
    return correctionMap;
  }

  /**
   * Applies the color correction to the image using the provided correction maps for each color
   * channel. Each pixel's color is adjusted based on the correction maps, producing a
   * color-balanced image.
   *
   * @param image           the original image to correct
   * @param redCorrection   the correction map for the red channel
   * @param greenCorrection the correction map for the green channel
   * @param blueCorrection  the correction map for the blue channel
   * @return a new Image with color-corrected values
   */
  private Image applyColorCorrection(Image image, int[] redCorrection, int[] greenCorrection,
      int[] blueCorrection) {
    Image result = new Image(image.getWidth(), image.getHeight());
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int newRed = redCorrection[pixel.getRed()];
        int newGreen = greenCorrection[pixel.getGreen()];
        int newBlue = blueCorrection[pixel.getBlue()];
        result.setPixel(x, y, new Pixel(newRed, newGreen, newBlue));
      }
    }
    return result;
  }

  /**
   * Adjusts the levels of the given image using specified shadow, midtone, and highlight values.
   * This function modifies each color channel to enhance contrast and brightness according to the
   * provided intensity points, producing a non-linear adjustment curve across the histogram. The
   * method calculates quadratic coefficients to fit the shadow, midtone, and highlight points. The
   * resulting quadratic function is applied to each pixel's color channel, producing the adjusted
   * image.
   *
   * @param image the original image to adjust
   * @param b     the shadow (dark) intensity value, clamped to a range of 0-255
   * @param m     the midtone (middle) intensity value, constrained between b and w
   * @param w     the highlight (bright) intensity value, clamped to a range of 0-255
   * @return a new Image with adjusted levels based on the shadow, midtone, and highlight values
   */
  @Override
  public Image levelsAdjust(Image image, int b, int m, int w) {
    b = Math.max(0, Math.min(b, 255));
    m = Math.max(b + 1, Math.min(m, w - 1));
    w = Math.max(m + 1, Math.min(w, 255));

    double coefficientA =
        Math.pow(b, 2) * (m - w) - b * (Math.pow(m, 2) - Math.pow(w, 2)) + w * Math.pow(m, 2)
            - m * Math.pow(w, 2);
    double coefficientAa = -b * (128 - 255) + 128 * w - 255 * m;
    double coefficientAb =
        Math.pow(b, 2) * (128 - 255) + 255 * Math.pow(m, 2) - 128 * Math.pow(w, 2);
    double coefficientAc =
        Math.pow(b, 2) * (255 * m - 128 * w) - b * (255 * Math.pow(m, 2) - 128 * Math.pow(w, 2));

    double a = coefficientAa / coefficientA;
    double bb = coefficientAb / coefficientA;
    double c = coefficientAc / coefficientA;

    Image result = new Image(image.getWidth(), image.getHeight());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel pixel = image.getPixel(x, y);
        int newRed = adjustChannel(pixel.getRed(), a, bb, c);
        int newGreen = adjustChannel(pixel.getGreen(), a, bb, c);
        int newBlue = adjustChannel(pixel.getBlue(), a, bb, c);
        result.setPixel(x, y, new Pixel(newRed, newGreen, newBlue));
      }
    }

    return result;
  }

  /**
   * Combines the original and processed images into a single image with a split view. The split
   * position is determined by the given split percentage.
   *
   * @param original        the original image to be displayed on one side of the split.
   * @param processed       the processed image to be displayed on the other side of the split.
   * @param splitPercentage the percentage of the width allocated to the processed image (0-100).
   * @return a new Image combining the processed and original images based on the split percentage.
   * @throws IllegalArgumentException if splitPercentage is not between 0 and 100, or if original or
   *                                  processed is null.
   */
  @Override
  public Image applySplitView(Image original, Image processed, int splitPercentage) {
    int width = original.getWidth();
    int height = original.getHeight();
    int splitPosition = (width * splitPercentage) / 100;

    Image result = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x < splitPosition) {
          result.setPixel(x, y, processed.getPixel(x, y));
        } else {
          result.setPixel(x, y, original.getPixel(x, y));
        }
      }
    }
    return result;
  }

  /**
   * Adjusts a single color channel value using the quadratic function defined by coefficients a, b,
   * and c. This function calculates the new intensity based on the original value and the levels
   * adjustment curve.
   *
   * @param value the original intensity value of the color channel (0-255)
   * @param a     the coefficient for the squared term in the adjustment function
   * @param b     the coefficient for the linear term in the adjustment function
   * @param c     the constant term in the adjustment function
   * @return the adjusted intensity value, clamped between 0 and 255
   */
  private int adjustChannel(int value, double a, double b, double c) {
    double adjustedValue = a * Math.pow(value, 2) + b * value + c;
    return (int) Math.max(0, Math.min(255, Math.round(adjustedValue)));
  }

  /**
   * Downscales the source image to the given width and height using bilinear interpolation.
   *
   * @param sourceImage  the original image to be downscaled
   * @param targetWidth  the width of the downscaled image
   * @param targetHeight the height of the downscaled image
   * @return the downscaled image
   */
  @Override
  public Image downscaleImage(Image sourceImage, int targetWidth, int targetHeight) {
    int srcWidth = sourceImage.getWidth();
    int srcHeight = sourceImage.getHeight();

    Image downscaledImage = new Image(targetWidth, targetHeight);

    for (int y = 0; y < targetHeight; y++) {
      for (int x = 0; x < targetWidth; x++) {
        // Calculate the source coordinates
        double srcX = (x + 0.5) * srcWidth / targetWidth - 0.5;
        double srcY = (y + 0.5) * srcHeight / targetHeight - 0.5;

        // Find the four nearest neighbors
        int floorX = (int) Math.floor(srcX);
        int ceilX = (int) Math.ceil(srcX);
        int floorY = (int) Math.floor(srcY);
        int ceilY = (int) Math.ceil(srcY);

        // Clamp values to valid ranges
        floorX = Math.max(0, Math.min(floorX, srcWidth - 1));
        ceilX = Math.max(0, Math.min(ceilX, srcWidth - 1));
        floorY = Math.max(0, Math.min(floorY, srcHeight - 1));
        ceilY = Math.max(0, Math.min(ceilY, srcHeight - 1));

        // Interpolate colors
        double r = bilinearInterpolate(srcX, srcY,
            sourceImage.getPixel(floorX, floorY).getRed(),
            sourceImage.getPixel(ceilX, floorY).getRed(),
            sourceImage.getPixel(floorX, ceilY).getRed(),
            sourceImage.getPixel(ceilX, ceilY).getRed());
        double g = bilinearInterpolate(srcX, srcY,
            sourceImage.getPixel(floorX, floorY).getGreen(),
            sourceImage.getPixel(ceilX, floorY).getGreen(),
            sourceImage.getPixel(floorX, ceilY).getGreen(),
            sourceImage.getPixel(ceilX, ceilY).getGreen());
        double b = bilinearInterpolate(srcX, srcY,
            sourceImage.getPixel(floorX, floorY).getBlue(),
            sourceImage.getPixel(ceilX, floorY).getBlue(),
            sourceImage.getPixel(floorX, ceilY).getBlue(),
            sourceImage.getPixel(ceilX, ceilY).getBlue());

        // Set the pixel color
        downscaledImage.setPixel(x, y, new Pixel((int) r, (int) g, (int) b));
      }
    }

    return downscaledImage;
  }

  /**
   * Bilinear interpolation for color values.
   */
  private double bilinearInterpolate(double x, double y, double q11, double q21, double q12,
      double q22) {
    double xDiff = x - Math.floor(x);
    double yDiff = y - Math.floor(y);

    return q11 * (1 - xDiff) * (1 - yDiff)
        + q21 * xDiff * (1 - yDiff)
        + q12 * (1 - xDiff) * yDiff
        + q22 * xDiff * yDiff;
  }

  /**
   * Applies the specified operation to the source image using a mask image. Pixels in the mask
   * image determine whether the operation is applied to the corresponding pixels in the source
   * image.
   *
   * @param sourceImage the original image to process
   * @param maskImage   the mask image determining which pixels are processed
   * @param operation   the operation to apply (e.g., "blur", "sharpen", "sepia")
   * @return the resulting image after applying the operation with the mask
   * @throws IllegalArgumentException if dimensions of the source and mask images do not match or if
   *                                  the operation is null or unsupported
   */
  @Override
  public Image applyWithMask(Image sourceImage, Image maskImage, String operation) {
    // Validate dimensions
    if (sourceImage.getWidth() != maskImage.getWidth()
        || sourceImage.getHeight() != maskImage.getHeight()) {
      throw new IllegalArgumentException("Source image and mask image dimensions must match.");
    }

    // Validate operation
    if (operation == null || operation.isEmpty()) {
      throw new IllegalArgumentException("Operation cannot be null or empty.");
    }

    Image resultImage = new Image(sourceImage.getWidth(), sourceImage.getHeight());
    Image processedImage;

    // Perform the specified operation
    switch (operation) {
      case "blur":
        processedImage = blur(sourceImage);
        break;
      case "sharpen":
        processedImage = sharpen(sourceImage);
        break;
      case "sepia":
        processedImage = toSepia(sourceImage);
        break;
      case "greyscale":
        processedImage = toGreyscale(sourceImage);
        break;
      case "red-component":
        processedImage = visualizeRedComponent(sourceImage);
        break;
      case "green-component":
        processedImage = visualizeGreenComponent(sourceImage);
        break;
      case "blue-component":
        processedImage = visualizeBlueComponent(sourceImage);
        break;
      case "value-component":
        processedImage = visualizeValue(sourceImage);
        break;
      case "intensity-component":
        processedImage = visualizeIntensity(sourceImage);
        break;
      case "luma-component":
        processedImage = visualizeLuma(sourceImage);
        break;
      default:
        throw new UnsupportedOperationException("Unsupported operation: " + operation);
    }

    // Apply the mask to combine the source and processed images
    for (int y = 0; y < sourceImage.getHeight(); y++) {
      for (int x = 0; x < sourceImage.getWidth(); x++) {
        try {
          Pixel maskPixel = maskImage.getPixel(x, y);

          // Allow a tolerance for "black" in the mask
          if (maskPixel.getRed() < 10 && maskPixel.getGreen() < 10 && maskPixel.getBlue() < 10) {
            resultImage.setPixel(x, y, processedImage.getPixel(x, y));
          } else {
            resultImage.setPixel(x, y, sourceImage.getPixel(x, y));
          }
        } catch (Exception e) {
          throw new IllegalStateException(
              "Error processing pixel at (" + x + ", " + y + "): " + e.getMessage());
        }
      }
    }

    return resultImage;
  }

  /**
   * Clamps the pixel value to ensure it is within the range [0, 255].
   *
   * @param value the value to clamp
   * @return the clamped value within the range [0, 255]
   */
  private int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }
}
