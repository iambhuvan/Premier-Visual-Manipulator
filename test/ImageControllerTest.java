import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import controller.ImageController;
import model.Image;
import model.ImageProcessor;
import view.ImageView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the ImageController class. This class tests various image processing operations
 * using different commands.
 */
public class ImageControllerTest {

  private ImageController controller;
  private TestView testView;
  private TestImageProcessor testProcessor;

  /**
   * Mock implementation of the ImageView interface for testing purposes.
   */
  private class TestView implements ImageView {

    public String lastMessage = "";
    public String lastError = "";

    @Override
    public void displayMessage(String message) {
      lastMessage = message;
    }

    @Override
    public void displayError(String error) {
      lastError = error;
    }

    @Override
    public void updateHistogram(int[] red, int[] green, int[] blue) {
      // Mock implementation
    }

    @Override
    public void setImage(Image image) {
      // Mock implementation, can leave it empty or log the call
    }

  }

  /**
   * Mock implementation of the ImageProcessor interface for testing purposes.
   */
  private class TestImageProcessor implements ImageProcessor {

    public Image lastProcessedImage;
    public int lastBrightnessAdjustment;
    public String lastOperation = "";
    public double[][] lastKernel;
    public double[][] lastColorTransformation;
    public int lastSplitPercentage;


    @Override
    public Image visualizeRedComponent(Image image) {
      lastOperation = "VisualizeRed";
      return new Image(1, 1);
    }

    @Override
    public Image visualizeGreenComponent(Image image) {
      lastOperation = "VisualizeGreen";
      return new Image(1, 1);
    }

    @Override
    public Image visualizeBlueComponent(Image image) {
      lastOperation = "VisualizeBlue";
      return new Image(1, 1);
    }

    @Override
    public Image combineChannels(Image red, Image green, Image blue) {
      lastOperation = "CombineChannels";
      return new Image(1, 1);
    }

    @Override
    public Image flipHorizontal(Image image) {
      lastOperation = "FlipHorizontal";
      return new Image(1, 1);
    }

    @Override
    public Image flipVertical(Image image) {
      lastOperation = "FlipVertical";
      return new Image(1, 1);
    }

    @Override
    public Image brightness(Image image, int adjustment) {
      lastOperation = "Brightness";
      lastBrightnessAdjustment = adjustment;
      return new Image(1, 1);
    }

    @Override
    public Image darkness(Image image, int adjustment) {
      lastOperation = "Darkness";
      lastBrightnessAdjustment = -adjustment;
      return new Image(1, 1);
    }

    @Override
    public Image applyFilter(Image image, double[][] kernel) {
      lastOperation = "ApplyFilter";
      lastKernel = kernel;
      return new Image(1, 1);
    }

    @Override
    public Image blur(Image image) {
      lastOperation = "Blur";
      return new Image(1, 1);
    }

    @Override
    public Image sharpen(Image image) {
      lastOperation = "Sharpen";
      return new Image(1, 1);
    }

    @Override
    public Image applyColorTransformation(Image image, double[][] matrix) {
      lastOperation = "ApplyColorTransformation";
      lastColorTransformation = matrix;
      return new Image(1, 1);
    }

    @Override
    public Image toGreyscale(Image image) {
      lastOperation = "Greyscale";
      return new Image(1, 1);
    }

    @Override
    public Image toSepia(Image image) {
      lastOperation = "Sepia";
      return new Image(1, 1);
    }

    @Override
    public Image visualizeValue(Image image) {
      lastOperation = "VisualizeValue";
      return new Image(1, 1);
    }

    @Override
    public Image visualizeIntensity(Image image) {
      lastOperation = "VisualizeIntensity";
      return new Image(1, 1);
    }

    @Override
    public Image visualizeLuma(Image image) {
      lastOperation = "VisualizeLuma";
      return new Image(1, 1);
    }

    @Override
    public Image[] splitChannels(Image image) {
      lastOperation = "SplitChannels";
      return new Image[]{new Image(1, 1), new Image(1, 1), new Image(1, 1)};
    }

    @Override
    public Image compress(Image image, int percentage) {
      lastOperation = "Compress";
      return new Image(1, 1);
    }

    @Override
    public Image generateHistogram(Image image) {
      lastOperation = "GenerateHistogram";
      return new Image(1, 1);
    }

    @Override
    public Image colorCorrect(Image image) {
      lastOperation = "ColorCorrect";
      return new Image(1, 1);
    }

    @Override
    public Image levelsAdjust(Image image, int b, int m, int w) {
      lastOperation = "LevelsAdjust";
      return new Image(1, 1);
    }

    @Override
    public Image applySplitView(Image original, Image processed, int splitPercentage) {
      lastOperation = "SplitView";
      lastSplitPercentage = splitPercentage;
      return processed; // Mock processed image
    }

    @Override
    public int[][] calculateHistogram(Image image) {
      // Mock implementation: Return a default histogram with all zero values
      return new int[3][256]; // Red, Green, Blue channels
    }


    @Override
    public Image downscaleImage(Image sourceImage, int targetWidth, int targetHeight) {
      // Mock implementation: Return an empty image with the target dimensions
      return new Image(targetWidth, targetHeight);
    }

    @Override
    public Image applyWithMask(Image sourceImage, Image maskImage, String operation) {
      // Mock implementation: Return the source image as-is
      return sourceImage;
    }


  }

  /**
   * Sets up the test environment by initializing the controller, view, and processor mocks.
   */
  @Before
  public void setUp() {
    testView = new TestView();
    testProcessor = new TestImageProcessor();
    controller = new ImageController(testView, testProcessor);
  }

  /**
   * Tests the loading of an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLoadImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1";
    execute(script);
    assertTrue(testView.lastMessage.contains("loaded"));
  }

  /**
   * Tests saving a nonexistent image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSaveNonExistentImage() throws IOException {
    String script = "save output.jpg nonexistentImage";
    execute(script);
    assertTrue(testView.lastError.contains("not found"));
  }

  /**
   * Tests brightening an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageBrighten() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n"
        + "brighten 20 TestImage1 TestImage1-bright";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
    assertEquals(20, testProcessor.lastBrightnessAdjustment);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests darkening an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageDarken() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n"
        + "darken -20 TestImage1 TestImage1-dark";
    execute(script);
    assertEquals("Darkness", testProcessor.lastOperation);
    assertEquals(20, testProcessor.lastBrightnessAdjustment);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests applying blur to an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageBlur() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "blur TestImage1 TestImage1-blurred";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests sharpening an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageSharpen() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "sharpen TestImage1 TestImage1-sharpened";
    execute(script);
    assertEquals("Sharpen", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests chaining multiple operations on an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testChainedOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "blur img blur-img\n" +
        "sharpen blur-img sharp-img\n" +
        "sepia sharp-img sepia-img\n" +
        "greyscale sepia-img grey-img\n" +
        "brighten 20 grey-img bright-img\n" +
        "save E:/RevanthTJ/Assignment4/ResultImages/result.jpg bright-img";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests RGB operations including splitting and combining channels.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testComplexRGBOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg original\n" +
        "rgb-split original Test-image-split-red Test-image-split-green Test-image-split-blue\n" +
        "brighten 20 Test-image-split-red bright-red\n" +
        "blur Test-image-split-green blur-green\n" +
        "sharpen Test-image-split-blue sharp-blue\n" +
        "rgb-combine result bright-red blur-green sharp-blue";
    execute(script);
    assertEquals("CombineChannels", testProcessor.lastOperation);
  }

  /**
   * Tests processing an empty command string.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testEmptyCommandString() throws IOException {
    String script = "";
    execute(script);
    assertEquals("", testView.lastError);
  }

  /**
   * Tests converting an image to greyscale.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageGreyscale() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "greyscale TestImage1 TestImage1-greyscale";
    execute(script);
    assertEquals("Greyscale", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests applying a sepia filter to an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageSepia() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "sepia TestImage1 TestImage1-sepia";
    execute(script);
    assertEquals("Sepia", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests horizontally flipping an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageHorizontalFlip() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "horizontal-flip TestImage1 flippedImage";
    execute(script);
    assertEquals("FlipHorizontal", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests vertically flipping an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageVerticalFlip() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "vertical-flip TestImage1 flippedImage";
    execute(script);
    assertEquals("FlipVertical", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests splitting an image into its RGB components.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testRgbSplit() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "rgb-split TestImage1 Test-image-split-red Test-image-split-green Test-image-split-blue\n" +
        "save E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-red.jpg Test-image-split-red\n"
        + "save E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-green.jpg "
        + "Test-image-split-green\n"
        + "save E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-blue.jpg "
        + "Test-image-split-blue";
    execute(script);
    assertEquals("SplitChannels", testProcessor.lastOperation);
  }

  /**
   * Tests combining RGB channels into a single image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testRgbCombine() throws IOException {
    String script =
        "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-red.jpg redImage\n" +
            "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-green.jpg greenImage\n" +
            "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-blue.jpg blueImage\n" +
            "rgb-combine combinedImage redImage greenImage blueImage";
    execute(script);
    System.out.println("Processor last operation: " + testProcessor.lastOperation);
    System.out.println("View last message: " + testView.lastMessage);
    assertEquals("CombineChannels", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("RGB combine completed"));
  }


  /**
   * Tests processing an invalid operation on an image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testProcessImageWithInvalidOperation() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "invalidOperation testImage outputImage";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests brightening an image with a negative value.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testBrightenWithNegativeValue() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "brighten -10 TestImage1 brightImage";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
    assertEquals(-10, testProcessor.lastBrightnessAdjustment);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests loading multiple images and processing them.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLoadMultipleImagesAndProcess() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg image1\n" +
        "load E:/RevanthTJ/Assignment4/ResultImages/result.jpg image2\n" +
        "blur image1 blurImage1\n" +
        "sharpen image2 sharpenImage2";
    execute(script);
    assertEquals("Sharpen", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests overwriting an existing image after processing it.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testOverwriteExistingImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "blur TestImage1 TestImage1";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests the execution of an unknown command.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testExecuteUnknownCommand() throws IOException {
    String script = "unknown command";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests the case sensitivity of commands.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testCaseSensitivity() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "BLUR testImage blurImage";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests special characters in file names.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSpecialCharactersInFilenames() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg test@image\n" +
        "blur test@image blur#image\n" +
        "save E:/RevanthTJ/Assignment4/ResultImages/blur-image.jpg blur#image";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests the maximum brightness adjustment value.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMaxBrightnessValue() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "brighten 2147483647 TestImage1 brightImage";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
    assertEquals(2147483647, testProcessor.lastBrightnessAdjustment);
  }

  /**
   * Tests the minimum brightness adjustment value.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMinBrightnessValue() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "brighten -2147483648 TestImage1 darkImage";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
    assertEquals(-2147483648, testProcessor.lastBrightnessAdjustment);
  }

  /**
   * Tests zero brightness adjustment.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testZeroBrightnessAdjustment() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "brighten 0 TestImage1 brightImage\n" +
        "darken 0 brightImage darkImage";
    execute(script);
    assertEquals("Darkness", testProcessor.lastOperation);
    assertEquals(0, testProcessor.lastBrightnessAdjustment);
  }

  /**
   * Tests applying multiple operations on the same image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMultipleOperationsOnSameImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "blur TestImage1 TestImage1\n" +
        "sharpen TestImage1 TestImage1\n" +
        "sepia TestImage1 TestImage1\n" +
        "greyscale TestImage1 TestImage1";
    execute(script);
    assertEquals("Greyscale", testProcessor.lastOperation);
  }

  /**
   * Tests invalid brightness adjustment value.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testInvalidBrightnessValue() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n" +
        "brighten abc TestImage1 brightImage";
    execute(script);
    assertTrue(testView.lastError.contains("Invalid input"));
  }

  /**
   * Tests mixed case commands.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMixedCaseCommands() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "bLuR testImage blurImage";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests file names with non-alphanumeric characters.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testNonAlphanumericImageNames() throws IOException {
    String script =
        "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg test-image_123!@#$\n" +
            "blur test-image_123!@#$ blur-result\n" +
            "save E:/RevanthTJ/Assignment4/ResultImages/blur-result.jpg blur-result";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests using the same image as both source and destination for multiple operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSameSourceAndDestination() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg same\n" +
        "blur same same\n" +
        "sharpen same same\n" +
        "brighten 10 same same";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
  }

  /**
   * Tests multiple component extraction operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMultipleComponentExtraction() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg test\n" +
        "red-component test red\n" +
        "green-component red green\n" +
        "blue-component green blue";
    execute(script);
    assertEquals("VisualizeBlue", testProcessor.lastOperation);
  }

  /**
   * Tests for circular dependencies in image operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testCircularDependency() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "blur img1 img2\n" +
        "blur img2 img3\n" +
        "blur img3 img1";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests command sequence with errors.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testCommandSequenceWithErrors() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "invalidcommand img1 img2\n" +
        "blur img1 img3";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests loading the same image multiple times.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLoadSameImageMultipleTimes() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "blur img1 blur-img";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests boundary values for brightness and darkness operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testBoundaryValueOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "brighten 2147483647 img max-bright\n" +
        "brighten -2147483648 img min-bright\n" +
        "darken 2147483647 img max-dark\n" +
        "darken -2147483648 img min-dark";
    execute(script);
    assertEquals("Darkness", testProcessor.lastOperation);
  }

  /**
   * Tests sequential identical operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSequentialIdenticalOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "blur img blur1\n" +
        "blur blur1 blur2\n" +
        "blur blur2 blur3";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests alternating brightness and darkness operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testAlternatingOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "brighten 10 img bright1\n" +
        "darken 10 bright1 dark1\n" +
        "brighten 10 dark1 bright2\n" +
        "darken 10 bright2 dark2";
    execute(script);
    assertEquals("Darkness", testProcessor.lastOperation);
  }

  /**
   * Tests commands with trailing spaces.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testCommandWithTrailingSpaces() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "blur img blurred";
    execute(script);
    assertEquals("Blur", testProcessor.lastOperation);
  }

  /**
   * Tests loading and saving the same file.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLoadAndSaveSameFilename() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg image\n" +
        "blur image blurred\n" +
        "save E:/RevanthTJ/Assignment4/ResultImages/test.jpg blurred";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests RGB split and recombine operations to ensure identity.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testRGBSplitAndRecombineIdentity() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg original\n" +
        "rgb-split original r g b\n" +
        "rgb-combine result r g b";
    execute(script);
    assertEquals("CombineChannels", testProcessor.lastOperation);
  }

  /**
   * Tests sequential component visualizations (red, value, intensity, luma).
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testComponentVisualizationChain() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "red-component img red\n" +
        "value-component red value\n" +
        "intensity-component value intensity\n" +
        "luma-component intensity luma";
    execute(script);
    assertEquals("VisualizeLuma", testProcessor.lastOperation);
  }

  /**
   * Tests commands with missing spaces in between.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMissingSpacesInCommand() throws IOException {
    String script = "loadtest.jpg img\n" +
        "blurimg blurred";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests extreme sequential transformations (brightness, sharpen, blur, sepia, greyscale, flip).
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testExtremeSequentialTransformations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n" +
        "brighten 100 img b1\n" +
        "sharpen b1 s1\n" +
        "blur s1 bl1\n" +
        "sepia bl1 se1\n" +
        "greyscale se1 g1\n" +
        "vertical-flip g1 v1\n" +
        "horizontal-flip v1 h1";
    execute(script);
    assertEquals("FlipHorizontal", testProcessor.lastOperation);
  }

  /**
   * Tests invalid image references.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testInvalidImageReferences() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "blur nonexistent output\n" +
        "brighten 10 alsononexistent bright\n" +
        "rgb-split nothere r g b";
    execute(script);
    assertTrue(testView.lastError.contains("not found"));
  }

  /**
   * Tests overwriting the source image with the result of each operation.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testOverwriteSourceImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg source\n" +
        "blur source source\n" +
        "sharpen source source\n" +
        "brighten 10 source source";
    execute(script);
    assertEquals("Brightness", testProcessor.lastOperation);
  }

  /**
   * Executes a given script by sending commands to the controller.
   *
   * @param script the script containing commands to be executed.
   * @throws IOException if an I/O error occurs.
   */
  private void execute(String script) throws IOException {
    String[] commands = script.trim().split("\n");
    for (String command : commands) {
      if (!command.trim().isEmpty()) {
        controller.execute(command.trim());
      }
    }
  }

  /**
   * Tests saving an image that was previously loaded and processed.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSaveProcessedImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n"
        + "blur TestImage1 blurredImage\n"
        + "save E:/RevanthTJ/Assignment4/ResultImages/blurredImage.jpg blurredImage";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests chaining multiple image operations including brightening, blurring, and flipping.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testChainedImageOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg TestImage1\n"
        + "brighten 30 TestImage1 brightImage\n"
        + "blur brightImage blurredImage\n"
        + "horizontal-flip blurredImage flippedImage\n"
        + "save E:/RevanthTJ/Assignment4/ResultImages/flippedImage.jpg flippedImage";
    execute(script);
    assertEquals("FlipHorizontal", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests attempting to save an image without first loading it.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSaveWithoutLoad() throws IOException {
    String script = "save E:/RevanthTJ/Assignment4/ResultImages/testImage.jpg TestImage1";
    execute(script);
    assertTrue(testView.lastError.contains("not found"));
  }

  /**
   * Tests loading the same image multiple times and processing each instance.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMultipleImageLoad() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg image1\n"
        + "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg image2\n"
        + "blur image1 blurredImage\n"
        + "sharpen image2 sharpenedImage";
    execute(script);
    assertEquals("Sharpen", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests invalid image operation using an unsupported operation command.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testInvalidImageOperation() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n"
        + "unsupportedOperation testImage outputImage";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests handling of mixed-case operation commands, ensuring case sensitivity.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testMixedCaseOperationCommand() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n"
        + "BlUr testImage outputImage";
    execute(script);
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests saving an image with special characters in the filename.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSaveWithSpecialCharacterFilename() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n"
        + "blur testImage blurredImage\n"
        + "save E:/RevanthTJ/Assignment4/ResultImages/blurred-image#1!.jpg blurredImage";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Test case for combining RGB channels into a single image. Verifies if the RGB combine operation
   * is applied correctly.
   */
  @Test
  public void testRgbCombineOperation() throws IOException {
    String script =
        "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-red.jpg redImage\n" +
            "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-green.jpg greenImage\n" +
            "load E:/RevanthTJ/Assignment4/ResultImages/Test-image-split-blue.jpg blueImage\n" +
            "rgb-combine combinedImage redImage greenImage blueImage";
    execute(script);
    assertEquals("CombineChannels", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("RGB combine completed"));
  }

  /**
   * Tests invalid command to ensure unknown commands are handled.
   */
  @Test
  public void testUnknownCommand() throws IOException {
    controller.execute("invalid-command");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests split view with blur on left half.
   */
  @Test
  public void testSplitViewBlurLeftHalf() throws IOException {
    String command = "split-blur testImage blurredImage 50";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(50, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests split view for full image levels adjustment (100% split).
   */
  @Test
  public void testSplitViewLevelsAdjustFullImage() throws IOException {
    String command = "split-levels-adjust testImage adjustedImage 100";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(100, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests split view with minimal split (1%).
   */
  @Test
  public void testSplitViewMinimalSplit() throws IOException {
    String command = "split-blur testImage minimallyBlurredImage 1";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(1, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests invalid split percentage (over 100%) handling.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSplitViewInvalidPercentage() throws IOException {
    String command = "split-blur testImage invalidImage 150";
    controller.execute(command);
  }

  /**
   * Tests color correction on an image.
   */
  @Test
  public void testProcessImageColorCorrect() throws IOException {
    String command = "color-correct testImage correctedImage";
    controller.execute(command);
    assertEquals("ColorCorrect", testProcessor.lastOperation);
  }

  /**
   * Tests levels adjustment with specific shadow, mid, and highlight values.
   */
  @Test
  public void testLevelsAdjustWithValues() throws IOException {
    String command = "levels-adjust 10 128 255 testImage adjustedImage";
    controller.execute(command);
    assertEquals("LevelsAdjust", testProcessor.lastOperation);
  }

  /**
   * Tests histogram generation command.
   */
  @Test
  public void testGenerateHistogram() throws IOException {
    String command = "histogram testImage histogramImage";
    controller.execute(command);
    assertEquals("Histogram", testProcessor.lastOperation);
  }

  /**
   * Tests split view functionality with color correction followed by levels adjustment.
   */
  @Test
  public void testCascadingSplitViewOperations() throws IOException {
    controller.execute("split-color-correct testImage colorCorrectedImage 50");
    assertEquals("SplitView", testProcessor.lastOperation);
    controller.execute("split-levels-adjust colorCorrectedImage finalImage 100");
    assertEquals("SplitView", testProcessor.lastOperation);
  }

  /**
   * Tests split view for 50% split applied twice to validate the cumulative effect on the same
   * image.
   */
  @Test
  public void testRepeatedSplitViewEffect() throws IOException {
    String command = "split-blur testImage blurredImage 50";
    controller.execute(command);
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(50, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests split view with a split percentage of 0% to verify that no changes are applied to the
   * image.
   */
  @Test
  public void testSplitViewWithZeroPercent() throws IOException {
    String command = "split-blur testImage unchangedImage 0";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(0, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests split view with color correction and split percentage of 100% for full-image correction.
   */
  @Test
  public void testFullImageSplitColorCorrection() throws IOException {
    String command = "split-color-correct testImage correctedImage 100";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(100, testProcessor.lastSplitPercentage);
  }


  /**
   * Tests a scenario where an invalid command sequence is executed to ensure errors are reported.
   */
  @Test
  public void testInvalidCommandSequence() throws IOException {
    String command = "load path/to/image.jpg testImage\nsplit-blur testImage\nsave output.jpg";
    controller.execute(command);
    assertTrue("Expected error for incomplete command sequence",
        testView.lastError.contains("Invalid command"));
  }

  /**
   * Tests split view with unsupported split percentage values such as negative and above 100.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSplitPercentage() throws IOException {
    String command = "split-blur testImage invalidImage -10";
    controller.execute(command);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAboveMaxSplitPercentage() throws IOException {
    String command = "split-blur testImage invalidImage 110";
    controller.execute(command);
  }

  /**
   * Tests executing an unsupported split view operation to ensure error handling.
   */
  @Test
  public void testUnsupportedSplitViewOperation() throws IOException {
    String command = "split-unsupportedOperation testImage outputImage 50";
    controller.execute(command);
    assertTrue("Error should indicate unsupported operation",
        testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests applying a compression operation on an image and verifies result is generated.
   */
  @Test
  public void testImageCompression() throws IOException {
    String command = "compress testImage compressedImage 50";
    controller.execute(command);
    assertEquals("Compress", testProcessor.lastOperation);
  }

  /**
   * Tests applying histogram generation on a processed image to check histogram accuracy.
   */
  @Test
  public void testHistogramGenerationPostProcessing() throws IOException {
    String command = "load path/to/image.jpg testImage\nblur testImage blurredImage\nhistogram "
        + "blurredImage histogramImage";
    controller.execute(command);
    assertEquals("GenerateHistogram", testProcessor.lastOperation);
  }

  /**
   * Tests adjusting levels with specific edge values and validates the image is processed.
   */
  @Test
  public void testLevelsAdjustmentEdgeValues() throws IOException {
    String command = "levels-adjust testImage adjustedImage 0 50 100";
    controller.execute(command);
    assertEquals("LevelsAdjust", testProcessor.lastOperation);
  }

  /**
   * Tests cascading split view operations with blur and color correction sequentially.
   */
  @Test
  public void testCascadingSplitViewBlurAndColorCorrect() throws IOException {
    String command = "split-blur testImage blurredImage 50";
    controller.execute(command);
    String command2 = "split-color-correct blurredImage correctedImage 50";
    controller.execute(command2);
    assertEquals("SplitView", testProcessor.lastOperation);
  }

  /**
   * Tests brightness and darkness adjustments in sequence to validate adjustments are cumulative.
   */
  @Test
  public void testSequentialBrightnessDarknessAdjustments() throws IOException {
    String command = "brighten 20 testImage brightenedImage";
    controller.execute(command);
    String command2 = "darken 10 brightenedImage adjustedImage";
    controller.execute(command2);
    assertEquals("Darkness", testProcessor.lastOperation);
    assertEquals(-10, testProcessor.lastBrightnessAdjustment);
  }


  /**
   * Tests executing a chain of commands including loading, compressing, splitting channels, and
   * recombining.
   */
  @Test
  public void testFullProcessingChain() throws IOException {
    String script = "load path/to/image.jpg testImage\n" +
        "compress testImage compressedImage 50\n" +
        "rgb-split compressedImage red green blue\n" +
        "rgb-combine recombinedImage red green blue\n" +
        "save recombinedImage path/to/save.jpg";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests applying multiple split view operations with a minimal split on each operation.
   */
  @Test
  public void testMultipleMinimalSplitViewOperations() throws IOException {
    String command = "split-blur testImage blurredImage 1";
    controller.execute(command);
    String command2 = "split-color-correct blurredImage colorCorrectedImage 1";
    controller.execute(command2);
    assertEquals("SplitView", testProcessor.lastOperation);
  }


  /**
   * Tests handling of special characters in split view operation file names.
   */
  @Test
  public void testSplitViewWithSpecialCharacterFileNames() throws IOException {
    String command = "split-blur special@image blurred#image 50";
    controller.execute(command);
    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(50, testProcessor.lastSplitPercentage);
  }

  /**
   * Tests executing commands with mixed case to ensure case-sensitivity in command handling.
   */
  @Test
  public void testMixedCaseSplitViewCommand() throws IOException {
    String command = "SPLIT-BLUR testImage mixedCaseOutput 50";
    controller.execute(command);
    assertTrue("Error should indicate unknown command due to case sensitivity",
        testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests processing image with various levels adjustment and histogram combination.
   */
  @Test
  public void testLevelsAndHistogramCombination() throws IOException {
    String script = "load path/to/image.jpg testImage\n" +
        "levels-adjust testImage adjustedImage 20 100 200\n" +
        "histogram adjustedImage histogramImage";
    execute(script);
    assertEquals("GenerateHistogram", testProcessor.lastOperation);
  }

  /**
   * Tests saving an image after applying sequential split view transformations.
   */
  @Test
  public void testSaveAfterSplitViewTransformations() throws IOException {
    String script = "load path/to/image.jpg testImage\n" +
        "split-blur testImage blurredImage 50\n" +
        "split-color-correct blurredImage finalImage 50\n" +
        "save path/to/save.jpg finalImage";
    execute(script);
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests a valid script to ensure it is parsed and processed without errors.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testValidScriptParsing() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "blur testImage blurredImage\n" +
        "save E:/RevanthTJ/Assignment4/ResultImages/blurredImage.jpg blurredImage";
    controller.execute(script);
    assertTrue("Valid script should be processed without errors",
        testView.lastMessage.contains("completed"));
  }

  /**
   * Tests an invalid script containing an unknown command to verify error handling.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testInvalidScriptParsing() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "unknownCommand testImage outputImage";
    controller.execute(script);
    assertTrue("Invalid script should result in error message",
        testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests a script with missing parameters to check error handling for incomplete commands.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testScriptWithMissingParameters() throws IOException {
    String script = "blur";
    controller.execute(script);
    assertTrue("Script with missing parameters should show error",
        testView.lastError.contains("Invalid input"));
  }

  /**
   * Tests a script with commands that have extra whitespace to verify it is parsed correctly.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testScriptWithExtraWhitespace() throws IOException {
    String script =
        "  load  E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg   testImage  \n" +
            "blur  testImage  blurredImage\n";
    controller.execute(script);
    assertTrue("Script with extra whitespace should still parse correctly",
        testView.lastMessage.contains("completed"));
  }

  /**
   * Tests the case where an empty script is provided to verify handling of no input.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testEmptyScript() throws IOException {
    String script = "";
    controller.execute(script);
    assertTrue("Empty script should result in no operation or specific error message",
        testView.lastError.isEmpty());
  }

  /**
   * Tests loading an unsupported image format.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testHandleUnsupportedImageFormat() throws IOException {
    controller.loadImage("unsupported-file.xyz", "unsupportedImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  /**
   * Tests a large script with multiple operations to verify controller handles it without issues.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLargeScriptParsing() throws IOException {
    StringBuilder script = new StringBuilder();
    for (int i = 0; i < 50; i++) {
      script.append("blur testImage blurImage").append(i).append("\n");
    }
    controller.execute(script.toString());
    assertTrue("Controller should handle large scripts without issues",
        testView.lastMessage.contains("completed"));
  }

  /**
   * Tests loading an image file that is locked by another process.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testLoadFileInUse() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/LockedImage.jpg testImage";
    controller.execute(script);
    assertTrue("File in use should trigger an error",
        testView.lastError.contains("File in use"));
  }

  /**
   * Tests using the same output name across multiple operations.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testReuseOutputNameAcrossOperations() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "blur testImage processedImage\n" +
        "sharpen processedImage processedImage\n" +
        "greyscale processedImage processedImage";
    controller.execute(script);
    assertEquals("Greyscale", testProcessor.lastOperation);
  }

  /**
   * Tests executing RGB split and combine commands in sequence on the same image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSplitAndMergeSameImage() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "rgb-split testImage red green blue\n" +
        "rgb-combine mergedImage red green blue";
    controller.execute(script);
    assertEquals("CombineChannels", testProcessor.lastOperation);
  }

  /**
   * Tests overlapping image references in operations to ensure correct handling.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testOverlappingImageReferences() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img1\n" +
        "blur img1 img2\n" +
        "sharpen img2 img1\n" +
        "greyscale img1 img3";
    controller.execute(script);
    assertEquals("Greyscale", testProcessor.lastOperation);
  }

  /**
   * Tests exceeding a hypothetical operation limit to ensure stability.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testExceedOperationLimit() throws IOException {
    StringBuilder script = new StringBuilder(
        "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg img\n");
    for (int i = 0; i < 1000; i++) {
      script.append("blur img img").append(i).append("\n");
    }
    controller.execute(script.toString());
    assertTrue("Controller should handle large number of operations without crash",
        testView.lastMessage.contains("completed"));
  }

  /**
   * Tests that splitting and then combining RGB channels results in the original image.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testIdentityAfterSplitAndCombine() throws IOException {
    String script = "load E:/RevanthTJ/Assignment4/TestImages/TestImage1JPG.jpg testImage\n" +
        "rgb-split testImage red green blue\n" +
        "rgb-combine result red green blue";
    controller.execute(script);
    assertEquals("CombineChannels", testProcessor.lastOperation);
    assertTrue("Image after split and combine should be identical to original",
        testView.lastMessage.contains("completed"));
  }

  /**
   * Tests executing a script with invalid commands to ensure proper error handling.
   */
  @Test
  public void testInvalidCommandError() throws IOException {
    String script = "unknown command";
    controller.execute(script);
    assertTrue("Expected error message for invalid command",
        testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests error handling for missing parameters in commands.
   */
  @Test
  public void testMissingParameterError() throws IOException {
    String script = "brighten";
    controller.execute(script);
    assertTrue("Expected error message for missing parameters",
        testView.lastError.contains("Invalid input"));
  }

  /**
   * Tests parsing and executing commands from a script file.
   */
  @Test
  public void testScriptFileExecution() throws IOException {
    String script = "load path/to/image.jpg testImage\nblur testImage blurredImage\n"
        + "save path/to/output.jpg blurredImage";
    controller.execute(script);
    assertTrue("Expected success message after processing", testView.lastMessage.contains("saved"));
  }

  /**
   * Tests sequential operations to verify the order of operations.
   */
  @Test
  public void testSequentialOperationsOrder() throws IOException {
    String script = "load path/to/image.jpg testImage\nblur testImage blurredImage\n"
        + "brighten 20 blurredImage finalImage";
    controller.execute(script);
    assertEquals("Expected final operation to be Brightness", "Brightness",
        testProcessor.lastOperation);
  }

  /**
   * Tests an invalid levels adjustment with out-of-range values, expecting an error.
   */
  @Test
  public void testLevelsAdjustmentWithInvalidPoints() throws IOException {
    String command = "levels-adjust -10 300 testImage adjustedImage";
    controller.execute(command);
    assertTrue("Expected error for invalid levels adjustment points",
        testView.lastError.contains("Invalid levels"));
  }

  /**
   * Tests a command for split view blur with a 50% split.
   */
  @Test
  public void testSplitViewBlurOnHalfImage() throws IOException {
    String command = "split-blur testImage outputImage 50";
    controller.execute(command);
    assertEquals("Expected SplitView operation", "SplitView",
        testProcessor.lastOperation);
  }

  /**
   * Tests unsupported commands in a script, expecting an error.
   */
  @Test
  public void testUnsupportedCommandInScript() throws IOException {
    String command = "unsupported-command";
    controller.execute(command);
    assertTrue("Expected error for unsupported command",
        testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests a chain of commands to ensure brightness and darkening balance each other.
   */
  @Test
  public void testBrightenThenDarkenImage() throws IOException {
    String script = "load path/to/image.jpg testImage\nbrighten 20 testImage brightImage\n"
        + "darken 20 brightImage finalImage";
    controller.execute(script);
    assertEquals("Expected final operation to be Darkness", "Darkness",
        testProcessor.lastOperation);
    assertEquals("Expected cumulative brightness adjustment to be zero", 0,
        testProcessor.lastBrightnessAdjustment);
  }

  /**
   * Tests split view blur command on 50% of the image through the controller, ensuring the correct
   * command is processed and result is produced.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testSplitViewBlur() throws IOException {
    String command = "split-blur testImage outputImage 50";
    controller.execute(command);

    assertEquals("SplitView", testProcessor.lastOperation);
    assertEquals(50, testProcessor.lastSplitPercentage);
    assertTrue("Controller should display success message",
        testView.lastMessage.contains("completed"));
  }

  // New Test Cases

  /**
   * Tests loading an image successfully.
   */
  @Test
  public void testLoadImageSuccess() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    assertTrue(testView.lastMessage.contains("loaded"));
  }

  /**
   * Tests saving an image successfully.
   */
  @Test
  public void testSaveImage() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("output-path.jpg", "testImage");
    assertTrue(testView.lastMessage.contains("saved"));
  }

  /**
   * Tests brightening an image with a valid adjustment.
   */
  @Test
  public void testBrightenImage() throws IOException {
    controller.execute("brighten 50 testImage brightenedImage");
    assertEquals("Brightness", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests flipping an image vertically.
   */
  @Test
  public void testVerticalFlip() throws IOException {
    controller.execute("vertical-flip testImage flippedImage");
    assertEquals("FlipVertical", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests splitting and combining RGB channels.
   */
  @Test
  public void testSplitAndCombineRGBChannels() throws IOException {
    controller.execute("rgb-split testImage red green blue");
    assertEquals("SplitChannels", testProcessor.lastOperation);

    controller.execute("rgb-combine combinedImage red green blue");
    assertEquals("CombineChannels", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests applying a split view operation.
   */
  @Test
  public void testSplitView() throws IOException {
    controller.execute("split-view-50 testImage splitViewImage");
    assertEquals("SplitView", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests applying partial operations with a mask.
   */
  @Test
  public void testPartialOperationWithMask() throws IOException {
    controller.execute("partial blur testImage maskImage partialBlurredImage");
    assertEquals("Partial Operation - blur", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests downscaling an image.
   */
  @Test
  public void testDownscaleImage() throws IOException {
    controller.execute("downscale 100 100 testImage downscaledImage");
    assertEquals("Downscale", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests loading an unsupported file format.
   */
  @Test
  public void testLoadUnsupportedFormat() throws IOException {
    controller.loadImage("unsupported-file.xyz", "unsupportedImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  /**
   * Tests handling invalid commands gracefully.
   */
  @Test
  public void testInvalidCommand() throws IOException {
    controller.execute("invalid-command testImage outputImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests chaining multiple operations on an image.
   */
  @Test
  public void testPerformChainedImageOperations() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen blurredImage sharpenedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests histogram calculation and updating.
   */
  @Test
  public void testHistogramUpdate() throws IOException {
    controller.execute("histogram testImage histogramImage");
    assertEquals("GenerateHistogram", testProcessor.lastOperation);
  }

  /**
   * Tests error handling when mask is missing for a partial operation.
   */
  @Test
  public void testPartialOperationMissingMask() throws IOException {
    controller.execute("partial blur testImage nonexistentMask partialBlurredImage");
    assertTrue(testView.lastError.contains("Mask image"));
  }

  /**
   * Tests handling of compression operations with valid inputs.
   */
  @Test
  public void testCompressionOperation() throws IOException {
    controller.execute("compress 80 testImage compressedImage");
    assertEquals("Compress", testProcessor.lastOperation);
  }

  /**
   * Tests visualization of the red component.
   */
  @Test
  public void testVisualizeRedComponent() throws IOException {
    controller.execute("visualize-red testImage redComponentImage");
    assertEquals("Red Component", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests visualization of the green component.
   */
  @Test
  public void testVisualizeGreenComponent() throws IOException {
    controller.execute("visualize-green testImage greenComponentImage");
    assertEquals("Green Component", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests visualization of the blue component.
   */
  @Test
  public void testVisualizeBlueComponent() throws IOException {
    controller.execute("visualize-blue testImage blueComponentImage");
    assertEquals("Blue Component", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests conversion of an image to greyscale using luma.
   */
  @Test
  public void testConvertToGreyscale() throws IOException {
    controller.execute("greyscale testImage greyscaleImage");
    assertEquals("Greyscale", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests conversion of an image to sepia.
   */
  @Test
  public void testConvertToSepia() throws IOException {
    controller.execute("sepia testImage sepiaImage");
    assertEquals("Sepia", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testBrightenBeyondLimits() throws IOException {
    controller.execute("brighten 300 testImage brightenedImage");
    assertEquals("Brightness", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testVerticalFlipNonexistentImage() throws IOException {
    controller.execute("vertical-flip nonexistentImage flippedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  @Test
  public void testDownscaleToZeroDimensions() throws IOException {
    controller.execute("downscale 0 0 testImage downscaledImage");
    assertTrue(testView.lastError.contains("Invalid dimensions"));
  }

  @Test
  public void testInvalidSplitViewPercentage() throws IOException {
    controller.execute("split-view-150 testImage splitViewImage");
    assertTrue(testView.lastError.contains("Invalid percentage"));
  }


  @Test
  public void testUnsupportedCommand() throws IOException {
    controller.execute("unsupported-command testImage outputImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCommand() throws IOException {
    controller.execute(null);
  }

  @Test
  public void testCompressionFactorOutOfBounds() throws IOException {
    controller.execute("compress 200 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  @Test
  public void testInvalidFileFormat() throws IOException {
    controller.loadImage("invalid-file.format", "testImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  @Test
  public void testMissingMaskForPartialOperation() throws IOException {
    controller.execute("partial blur testImage nonexistentMask partialImage");
    assertTrue(testView.lastError.contains("Mask image not found"));
  }

  /**
   * Tests blurring an image successfully.
   */
  @Test
  public void testBlurImage() throws IOException {
    controller.execute("blur testImage blurredImage");
    assertEquals("Blur", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests blurring a nonexistent image.
   */
  @Test
  public void testBlurNonexistentImage() throws IOException {
    controller.execute("blur nonexistentImage blurredImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests sharpening an image successfully.
   */
  @Test
  public void testSharpenImage() throws IOException {
    controller.execute("sharpen testImage sharpenedImage");
    assertEquals("Sharpen", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests sharpening a nonexistent image.
   */
  @Test
  public void testSharpenNonexistentImage() throws IOException {
    controller.execute("sharpen nonexistentImage sharpenedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests compression with a factor of zero.
   */
  @Test
  public void testCompressionFactorZero() throws IOException {
    controller.execute("compress 0 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  /**
   * Tests flipping an image horizontally.
   */
  @Test
  public void testHorizontalFlip() throws IOException {
    controller.execute("horizontal-flip testImage flippedImage");
    assertEquals("FlipHorizontal", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests flipping a nonexistent image horizontally.
   */
  @Test
  public void testHorizontalFlipNonexistentImage() throws IOException {
    controller.execute("horizontal-flip nonexistentImage flippedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests chaining valid and invalid operations.
   */
  @Test
  public void testChainedOperationsWithInvalidCommand() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("invalid-command blurredImage sharpenedImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  /**
   * Tests loading an image without an extension.
   */
  @Test
  public void testLoadImageWithoutExtension() throws IOException {
    controller.loadImage("image-without-extension", "testImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  /**
   * Tests saving an image without loading any image first.
   */
  @Test
  public void testSaveWithoutLoadingImage() throws IOException {
    controller.saveImage("output-path.jpg", "testImage");
    assertTrue(testView.lastError.contains("No image loaded"));
  }

  /**
   * Tests splitting RGB channels with null parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSplitRGBNullParameters() throws IOException {
    controller.execute("rgb-split testImage null null null");
  }

  /**
   * Tests loading an image with a null file path.
   */
  @Test(expected = NullPointerException.class)
  public void testLoadImageNullPath() throws IOException {
    controller.loadImage(null, "testImage");
  }

  /**
   * Tests brightening an image with an invalid adjustment value.
   */
  @Test
  public void testInvalidBrightnessAdjustment() throws IOException {
    controller.execute("brighten -300 testImage brightenedImage");
    assertTrue(testView.lastError.contains("Invalid adjustment value"));
  }

  /**
   * Tests updating a histogram without loading an image.
   */
  @Test
  public void testHistogramUpdateWithoutImage() throws IOException {
    controller.execute("histogram nonexistentImage histogramImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests chaining multiple valid commands successfully.
   */
  @Test
  public void testSuccessfulChainedCommands() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen blurredImage sharpenedImage");
    controller.execute("greyscale sharpenedImage greyscaleImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests compression operation with a valid factor.
   */
  @Test
  public void testSuccessfulCompressionOperation() throws IOException {
    controller.execute("compress 50 testImage compressedImage");
    assertEquals("Compress", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests loading a corrupted image file.
   */
  @Test
  public void testLoadCorruptedImageFile() throws IOException {
    controller.loadImage("corrupted-file.jpg", "testImage");
    assertTrue(testView.lastError.contains("Failed to load image"));
  }

  /**
   * Tests saving an image to a nonexistent directory.
   */
  @Test
  public void testSaveImageToNonexistentDirectory() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("/nonexistent-directory/output.jpg", "testImage");
    assertTrue(testView.lastError.contains("Failed to save image"));
  }

  /**
   * Tests brightening an image with zero adjustment value.
   */
  @Test
  public void testBrightenImageZeroAdjustment() throws IOException {
    controller.execute("brighten 0 testImage brightenedImage");
    assertEquals("Brightness", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests brightening an image with a negative adjustment value (darkening).
   */
  @Test
  public void testDarkenImageNegativeAdjustment() throws IOException {
    controller.execute("brighten -50 testImage darkenedImage");
    assertEquals("Brightness", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests converting to greyscale without loading an image.
   */
  @Test
  public void testGreyscaleWithoutImage() throws IOException {
    controller.execute("greyscale nonexistentImage greyscaleImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests flipping an empty input horizontally.
   */
  @Test
  public void testHorizontalFlipEmptyInput() throws IOException {
    controller.execute("horizontal-flip  testImage flippedImage");
    assertTrue(testView.lastError.contains("Invalid input"));
  }

  /**
   * Tests applying a split view with zero percentage.
   */
  @Test
  public void testSplitViewZeroPercentage() throws IOException {
    controller.execute("split-view-0 testImage splitViewImage");
    assertTrue(testView.lastError.contains("Invalid percentage"));
  }

  /**
   * Tests applying a partial operation with an invalid mask file.
   */
  @Test
  public void testPartialOperationInvalidMaskFile() throws IOException {
    controller.execute("partial blur testImage invalidMaskImage partialImage");
    assertTrue(testView.lastError.contains("Mask image not found"));
  }

  /**
   * Tests compression operation with a negative factor.
   */
  @Test
  public void testCompressionFactorNegative() throws IOException {
    controller.execute("compress -20 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  /**
   * Tests compression operation with a factor above 100.
   */
  @Test
  public void testCompressionFactorAbove100() throws IOException {
    controller.execute("compress 120 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  /**
   * Tests downscaling an image to invalid dimensions (negative width/height).
   */
  @Test
  public void testDownscaleInvalidDimensionsNegative() throws IOException {
    controller.execute("downscale -50 100 testImage downscaledImage");
    assertTrue(testView.lastError.contains("Invalid dimensions"));
  }

  /**
   * Tests applying a partial mask operation without loading an image.
   */
  @Test
  public void testPartialMaskOperationWithoutImage() throws IOException {
    controller.execute("partial blur nonexistentImage maskImage partialImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests chaining operations with a missing intermediate file.
   */
  @Test
  public void testChainedOperationsMissingIntermediateFile() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen nonexistentImage sharpenedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests updating the histogram for a null image.
   */
  @Test
  public void testHistogramUpdateNullImage() throws IOException {
    controller.execute("histogram null histogramImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  /**
   * Tests greyscale and sepia conversion with nonexistent output files.
   */
  @Test
  public void testGreyscaleWithNonexistentOutput() throws IOException {
    controller.execute("greyscale testImage nonexistentOutput");
    assertTrue(testView.lastError.contains("Failed to write output"));
  }

  @Test
  public void testSepiaWithNonexistentOutput() throws IOException {
    controller.execute("sepia testImage nonexistentOutput");
    assertTrue(testView.lastError.contains("Failed to write output"));
  }

  @Test
  public void testCompressionValidFactor() throws IOException {
    controller.execute("compress 50 testImage compressedImage");
    assertEquals("Compress", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testCompressionZeroFactor() throws IOException {
    controller.execute("compress 0 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  /**
   * Tests applying compression with a factor greater than 100.
   */
  @Test
  public void testCompressionExceedsMaxFactor() throws IOException {
    controller.execute("compress 120 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  @Test
  public void testCompressionNegativeFactor() throws IOException {
    controller.execute("compress -10 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  @Test
  public void testCompressionNullImage() throws IOException {
    controller.execute("compress 50 nonexistentImage compressedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  @Test
  public void testHistogramValidImage() throws IOException {
    controller.execute("histogram testImage histogramImage");
    assertEquals("GenerateHistogram", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testHistogramNonexistentImage() throws IOException {
    controller.execute("histogram nonexistentImage histogramImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  @Test
  public void testHistogramGreyscaleImage() throws IOException {
    controller.execute("greyscale testImage greyscaleImage");
    controller.execute("histogram greyscaleImage histogramImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testLevelsAdjustmentValid() throws IOException {
    controller.execute("adjust-levels testImage 20 128 230 adjustedImage");
    assertEquals("AdjustLevels", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testLevelsAdjustmentInvalidValues() throws IOException {
    controller.execute("adjust-levels testImage -10 128 260 adjustedImage");
    assertTrue(testView.lastError.contains("Invalid adjustment values"));
  }

  @Test
  public void testLevelsAdjustmentNullImage() throws IOException {
    controller.execute("adjust-levels nonexistentImage 20 128 230 adjustedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  @Test
  public void testSplitViewValidPercentage() throws IOException {
    controller.execute("split-view-40 testImage splitViewImage");
    assertEquals("SplitView", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  /**
   * Tests applying a split view operation with a percentage of zero.
   */
  @Test
  public void testSplitViewWithZeroPercentage() throws IOException {
    controller.execute("split-view-0 testImage splitViewImage");
    assertTrue(testView.lastError.contains("Invalid percentage"));
  }

  @Test
  public void testSplitViewNegativePercentage() throws IOException {
    controller.execute("split-view--10 testImage splitViewImage");
    assertTrue(testView.lastError.contains("Invalid percentage"));
  }

  @Test
  public void testPartialBlurValidMask() throws IOException {
    controller.execute("partial blur testImage maskImage partialBlurredImage");
    assertEquals("PartialBlur", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testPartialOperationMaskLargerThanImage() throws IOException {
    controller.execute("partial blur testImage largerMask partialBlurredImage");
    assertTrue(testView.lastError.contains("Mask dimensions mismatch"));
  }

  @Test
  public void testPartialOperationNullMask() throws IOException {
    controller.execute("partial blur testImage nullMask partialBlurredImage");
    assertTrue(testView.lastError.contains("Invalid mask"));
  }

  @Test
  public void testDownscaleValidDimensions() throws IOException {
    controller.execute("downscale 50 50 testImage downscaledImage");
    assertEquals("Downscale", testProcessor.lastOperation);
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testDownscaleInvalidDimensions() throws IOException {
    controller.execute("downscale -50 50 testImage downscaledImage");
    assertTrue(testView.lastError.contains("Invalid dimensions"));
  }

  @Test
  public void testDownscaleToSameDimensions() throws IOException {
    controller.execute("downscale 256 256 testImage sameSizeImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testChainedValidCommands() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen blurredImage sharpenedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testChainedInvalidCommands() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("invalid-command blurredImage outputImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  @Test
  public void testChainedCommandsWithNull() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute(null);
    assertTrue(testView.lastError.contains("Invalid command"));
  }

  @Test
  public void testLoadNonReadableFile() throws IOException {
    controller.loadImage("readonly-file.jpg", "testImage");
    assertTrue(testView.lastError.contains("Cannot read file"));
  }

  @Test
  public void testLoadFileWithIncorrectExtension() throws IOException {
    controller.loadImage("test-image.docx", "testImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  @Test
  public void testLoadImageWithUnsavedImage() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.loadImage("new-path.jpg", "newTestImage");
    assertTrue(testView.lastMessage.contains("Unsaved changes"));
  }

  @Test
  public void testSaveImageWithoutWritePermissions() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("/restricted/output.jpg", "testImage");
    assertTrue(testView.lastError.contains("Cannot write file"));
  }

  @Test
  public void testSaveImageToInvalidFormat() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("output.txt", "testImage");
    assertTrue(testView.lastError.contains("Unsupported file format"));
  }

  @Test
  public void testGreyscaleAlreadyGreyscaleImage() throws IOException {
    controller.execute("greyscale testImage greyscaleImage");
    controller.execute("greyscale greyscaleImage doubleGreyscaleImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testPartialOperationWithZeroMask() throws IOException {
    controller.execute("partial blur testImage zeroMaskImage partialImage");
    assertTrue(testView.lastMessage.contains("No changes applied"));
  }

  @Test
  public void testPartialOperationWithFullMask() throws IOException {
    controller.execute("partial blur testImage fullMaskImage partialImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testChainOperationsInvalidIntermediate() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen nonexistentImage sharpenedImage");
    assertTrue(testView.lastError.contains("Image not found"));
  }

  @Test
  public void testChainMixedValidCommands() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("invalid-command blurredImage sharpenedImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  @Test
  public void testCompressionFloatingPointFactor() throws IOException {
    controller.execute("compress 50.5 testImage compressedImage");
    assertTrue(testView.lastError.contains("Invalid compression factor"));
  }

  @Test
  public void testCommandWithExcessArguments() throws IOException {
    controller.execute("blur testImage blurredImage extraArg");
    assertTrue(testView.lastError.contains("Invalid command format"));
  }

  @Test
  public void testCommandWithMissingArguments() throws IOException {
    controller.execute("blur testImage");
    assertTrue(testView.lastError.contains("Invalid command format"));
  }

  @Test
  public void testCommandWithNonexistentOperation() throws IOException {
    controller.execute("nonexistentOperation testImage outputImage");
    assertTrue(testView.lastError.contains("Unknown command"));
  }

  @Test
  public void testCommandWithInvalidPathCharacters() throws IOException {
    controller.execute("load *invalidpath.jpg testImage");
    assertTrue(testView.lastError.contains("Invalid file path"));
  }

  @Test
  public void testDownscaleNonIntegralDimensions() throws IOException {
    controller.execute("downscale 100.5 100 testImage downscaledImage");
    assertTrue(testView.lastError.contains("Invalid dimensions"));
  }

  @Test
  public void testLoadImageInPngFormat() throws IOException {
    controller.loadImage("test-image.png", "testImage");
    assertTrue(testView.lastMessage.contains("loaded"));
  }

  @Test
  public void testLoadImageInJpgFormat() throws IOException {
    controller.loadImage("test-image.jpg", "testImage");
    assertTrue(testView.lastMessage.contains("loaded"));
  }

  @Test
  public void testSaveImageWithSpacesInPath() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("output path/output image.jpg", "testImage");
    assertTrue(testView.lastMessage.contains("saved"));
  }

  @Test
  public void testSaveImageOverwrite() throws IOException {
    controller.loadImage("test-path.jpg", "testImage");
    controller.saveImage("existing-file.jpg", "testImage");
    assertTrue(testView.lastMessage.contains("saved"));
  }

  @Test
  public void testVerticalFlipNonRectangularImage() throws IOException {
    controller.loadImage("non-rectangular-image.jpg", "testImage");
    controller.execute("vertical-flip testImage flippedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testBrightenToMaxBrightness() throws IOException {
    controller.execute("brighten 255 testImage brightenedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testDarkenToCompleteBlack() throws IOException {
    controller.execute("brighten -255 testImage darkenedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testVisualizeInvalidChannel() throws IOException {
    controller.execute("visualize-orange testImage outputImage");
    assertTrue(testView.lastError.contains("Invalid channel"));
  }

  @Test
  public void testAdjustLevelsIdentity() throws IOException {
    controller.execute("adjust-levels testImage 0 128 255 adjustedImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testAdjustLevelsZeroRange() throws IOException {
    controller.execute("adjust-levels testImage 100 100 100 adjustedImage");
    assertTrue(testView.lastError.contains("Invalid adjustment values"));
  }

  @Test
  public void testPartialOperationMaskOutOfBounds() throws IOException {
    controller.execute("partial blur testImage outOfBoundsMaskImage partialImage");
    assertTrue(testView.lastError.contains("Mask dimensions mismatch"));
  }

  @Test
  public void testDownscaleToOnePixel() throws IOException {
    controller.execute("downscale 1 1 testImage downscaledImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testChainOperationsWithOverlappingOutputs() throws IOException {
    controller.execute("blur testImage blurredImage");
    controller.execute("sharpen blurredImage blurredImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testCommandWithIncorrectArgumentOrder() throws IOException {
    controller.execute("brighten testImage brightenedImage 50");
    assertTrue(testView.lastError.contains("Invalid command format"));
  }

  @Test
  public void testCommandWithMultipleSpaces() throws IOException {
    controller.execute("blur     testImage    blurredImage");
    assertTrue(testView.lastMessage.contains("completed"));
  }

  @Test
  public void testEmptyCommand() throws IOException {
    controller.execute("");
    assertTrue(testView.lastError.contains("Invalid command format"));
  }

  @Test
  public void testCommandWithSpecialCharactersInFileName() throws IOException {
    controller.execute("load test-image@#$%.jpg testImage");
    assertTrue(testView.lastError.contains("Invalid file path"));
  }


}
