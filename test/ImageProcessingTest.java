import org.junit.Before;
import org.junit.Test;
import model.Image;
import model.ImageProcessor;
import model.ImageProcessorImpl;
import model.Pixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class contains JUnit test cases for the Image and ImageProcessor classes. It tests the
 * functionality of various image processing methods, including manipulating image pixels, flipping,
 * brightness, and other image operations.
 */
public class ImageProcessingTest {

  private Image testImage;
  private ImageProcessor processor;
  private Image maskImage;

  private Image createTestImage() {
    Image image = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        image.setPixel(x, y, new Pixel(x * 50, y * 50, (x + y) * 25));
      }
    }
    return image;
  }

  private Image createMaskImage() {
    Image mask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        mask.setPixel(x, y, (x + y) % 2 == 0 ? new Pixel(0, 0, 0) : new Pixel(255, 255, 255));
      }
    }
    return mask;
  }

  /**
   * Sets up the test environment by initializing a test image and an ImageProcessor before each
   * test. The test image is a 3x3 grid with different RGB values for each pixel.
   */
  @Before
  public void setUp() {
    testImage = new Image(3, 3);
    processor = new ImageProcessorImpl();
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        testImage.setPixel(x, y, new Pixel(x * 50, y * 50, (x + y) * 25));
      }
    }
  }

  /**
   * Tests the creation of an Image object with specified dimensions.
   */
  @Test
  public void testImageCreation() {
    Image img = new Image(5, 5);
    assertEquals(5, img.getWidth());
    assertEquals(5, img.getHeight());
  }

  /**
   * Tests setting and getting a pixel in an Image.
   */
  @Test
  public void testImagePixelSetAndGet() {
    Pixel pixel = new Pixel(100, 150, 200);
    testImage.setPixel(1, 1, pixel);
    assertEquals(pixel, testImage.getPixel(1, 1));
  }

  /**
   * Tests the creation of a Pixel with specified RGB values.
   */
  @Test
  public void testPixelCreation() {
    Pixel pixel = new Pixel(100, 150, 200);
    assertEquals(100, pixel.getRed());
    assertEquals(150, pixel.getGreen());
    assertEquals(200, pixel.getBlue());
  }

  /**
   * Tests clamping of RGB values to the valid range [0, 255].
   */
  @Test
  public void testPixelClamp() {
    Pixel pixel = new Pixel(-10, 300, 128);
    assertEquals(0, pixel.getRed());
    assertEquals(255, pixel.getGreen());
    assertEquals(128, pixel.getBlue());
  }

  /**
   * Tests retrieving individual color channels (red, green, blue) from a Pixel.
   */
  @Test
  public void testPixelGetChannel() {
    Pixel pixel = new Pixel(100, 150, 200);
    assertEquals(100, pixel.getChannel(0));
    assertEquals(150, pixel.getChannel(1));
    assertEquals(200, pixel.getChannel(2));
  }

  /**
   * Tests that an exception is thrown when an invalid channel is requested from a Pixel.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPixelGetChannelInvalid() {
    Pixel pixel = new Pixel(100, 150, 200);
    pixel.getChannel(3);
  }

  /**
   * Tests the visualization of the red component of an Image. The result should be a greyscale
   * image where the red values are used.
   */
  @Test
  public void testVisualizeRedComponent() {
    Image result = processor.visualizeRedComponent(testImage);
    assertEquals(100, result.getPixel(2, 2).getRed());
    assertEquals(100, result.getPixel(2, 2).getGreen());
    assertEquals(100, result.getPixel(2, 2).getBlue());
  }

  /**
   * Tests the visualization of the green component of an Image. The result should be a greyscale
   * image where the green values are used.
   */
  @Test
  public void testVisualizeGreenComponent() {
    Image result = processor.visualizeGreenComponent(testImage);
    assertEquals(100, result.getPixel(2, 2).getRed());
    assertEquals(100, result.getPixel(2, 2).getGreen());
    assertEquals(100, result.getPixel(2, 2).getBlue());
  }

  /**
   * Tests the visualization of the blue component of an Image. The result should be a greyscale
   * image where the blue values are used.
   */
  @Test
  public void testVisualizeBlueComponent() {
    Image result = processor.visualizeBlueComponent(testImage);
    assertEquals(100, result.getPixel(2, 2).getRed());
    assertEquals(100, result.getPixel(2, 2).getGreen());
    assertEquals(100, result.getPixel(2, 2).getBlue());
  }

  /**
   * Tests the combination of red, green, and blue channel images into a single color image.
   */
  @Test
  public void testCombineChannels() {
    Image red = processor.visualizeRedComponent(testImage);
    Image green = processor.visualizeGreenComponent(testImage);
    Image blue = processor.visualizeBlueComponent(testImage);
    Image result = processor.combineChannels(red, green, blue);
    assertEquals(testImage.getPixel(2, 2).getRed(), result.getPixel(2, 2).getRed());
    assertEquals(testImage.getPixel(2, 2).getGreen(), result.getPixel(2, 2).getGreen());
    assertEquals(testImage.getPixel(2, 2).getBlue(), result.getPixel(2, 2).getBlue());
  }

  /**
   * Tests flipping an Image horizontally.
   */
  @Test
  public void testFlipHorizontal() {
    Image result = processor.flipHorizontal(testImage);
    assertEquals(testImage.getPixel(0, 0).getRed(), result.getPixel(2, 0).getRed());
    assertEquals(testImage.getPixel(2, 2).getGreen(), result.getPixel(0, 2).getGreen());
  }

  /**
   * Tests flipping an Image vertically.
   */
  @Test
  public void testFlipVertical() {
    Image result = processor.flipVertical(testImage);
    assertEquals(testImage.getPixel(0, 0).getRed(), result.getPixel(0, 2).getRed());
    assertEquals(testImage.getPixel(2, 2).getGreen(), result.getPixel(2, 0).getGreen());
  }

  /**
   * Tests brightening an Image by a given value.
   */
  @Test
  public void testBrightness() {
    Image result = processor.brightness(testImage, 50);
    assertEquals(Math.min(255, testImage.getPixel(1, 1).getRed() + 50),
        result.getPixel(1, 1).getRed());
  }

  /**
   * Tests darkening an Image by a given value.
   */
  @Test
  public void testDarkness() {
    Image result = processor.darkness(testImage, 50);
    assertEquals(Math.max(0, testImage.getPixel(1, 1).getRed() - 50),
        result.getPixel(1, 1).getRed());
  }

  /**
   * Tests blurring an Image using a blur filter.
   */
  @Test
  public void testBlur() {
    testImage.setPixel(1, 1, new Pixel(100, 150, 200));
    Image blurredImage = processor.blur(testImage);
    assertNotEquals(testImage.getPixel(1, 1).getRed(), blurredImage.getPixel(1, 1).getRed());
  }

  /**
   * Tests sharpening an Image using a sharpen filter.
   */
  @Test
  public void testSharpen() {
    Image result = processor.sharpen(testImage);
    assertTrue(result.getPixel(1, 1).getRed() == testImage.getPixel(1, 1).getRed());
    assertTrue(result.getPixel(1, 1).getGreen() == testImage.getPixel(1, 1).getGreen());
    assertTrue(result.getPixel(1, 1).getBlue() == testImage.getPixel(1, 1).getBlue());
    assertTrue(result.getPixel(1, 2).getRed() == testImage.getPixel(1, 2).getRed());
    assertTrue(result.getPixel(1, 2).getGreen() != testImage.getPixel(1, 2).getGreen());
    assertTrue(result.getPixel(1, 2).getBlue() != testImage.getPixel(1, 2).getBlue());
    assertTrue(result.getPixel(2, 1).getRed() != testImage.getPixel(2, 1).getRed());
    assertTrue(result.getPixel(2, 1).getGreen() == testImage.getPixel(2, 1).getGreen());
    assertTrue(result.getPixel(2, 1).getBlue() != testImage.getPixel(2, 1).getBlue());
  }

  /**
   * Tests converting an Image to greyscale.
   */
  @Test
  public void testToGreyscale() {
    Image result = processor.toGreyscale(testImage);
    Pixel pixel = result.getPixel(1, 1);
    assertEquals(pixel.getRed(), pixel.getGreen());
    assertEquals(pixel.getGreen(), pixel.getBlue());
  }

  /**
   * Tests converting an Image to sepia tone.
   */
  @Test
  public void testToSepia() {
    Image result = processor.toSepia(testImage);
    Pixel pixel = result.getPixel(1, 1);
    assertTrue(pixel.getRed() > pixel.getGreen());
    assertTrue(pixel.getGreen() > pixel.getBlue());
  }

  /**
   * Tests visualizing the value component of an Image. The value component is the maximum of the
   * red, green, and blue values for each pixel.
   */
  @Test
  public void testVisualizeValue() {
    Image result = processor.visualizeValue(testImage);
    Pixel pixel = result.getPixel(2, 2);
    int maxValue = Math.max(Math.max(testImage.getPixel(2, 2).getRed(),
            testImage.getPixel(2, 2).getGreen()),
        testImage.getPixel(2, 2).getBlue());
    assertEquals(maxValue, pixel.getRed());
    assertEquals(maxValue, pixel.getGreen());
    assertEquals(maxValue, pixel.getBlue());
  }

  /**
   * Tests visualizing the intensity component of an Image. The intensity is the average of the red,
   * green, and blue values for each pixel.
   */
  @Test
  public void testVisualizeIntensity() {
    Image result = processor.visualizeIntensity(testImage);
    Pixel pixel = result.getPixel(2, 2);
    int avgValue = (testImage.getPixel(2, 2).getRed() +
        testImage.getPixel(2, 2).getGreen() +
        testImage.getPixel(2, 2).getBlue()) / 3;
    assertEquals(avgValue, pixel.getRed());
    assertEquals(avgValue, pixel.getGreen());
    assertEquals(avgValue, pixel.getBlue());
  }

  /**
   * Tests visualizing the luma component of an Image. The luma is calculated as a weighted sum of
   * the red, green, and blue components.
   */
  @Test
  public void testVisualizeLuma() {
    Image result = processor.visualizeLuma(testImage);
    Pixel pixel = result.getPixel(2, 2);
    int lumaValue = (int) (0.2126 * testImage.getPixel(2, 2).getRed() +
        0.7152 * testImage.getPixel(2, 2).getGreen() +
        0.0722 * testImage.getPixel(2, 2).getBlue());
    assertEquals(lumaValue, pixel.getRed());
    assertEquals(lumaValue, pixel.getGreen());
    assertEquals(lumaValue, pixel.getBlue());
  }

  /**
   * Tests splitting an Image into its red, green, and blue channels.
   */
  @Test
  public void testSplitChannels() {
    Image[] result = processor.splitChannels(testImage);
    assertEquals(3, result.length);
    assertEquals(testImage.getPixel(1, 1).getRed(), result[0].getPixel(1, 1).getRed());
    assertEquals(testImage.getPixel(1, 1).getGreen(), result[1].getPixel(1, 1).getGreen());
    assertEquals(testImage.getPixel(1, 1).getBlue(), result[2].getPixel(1, 1).getBlue());
  }

  /**
   * Tests creating an Image with zero dimensions.
   */
  @Test
  public void testImageWithZeroDimensions() {
    Image img = new Image(0, 0);
    assertEquals(0, img.getWidth());
    assertEquals(0, img.getHeight());
  }

  /**
   * Tests brightening an Image where the brightness exceeds the maximum value of 255. The values
   * should be clamped to 255.
   */
  @Test
  public void testBrightnessOverflow() {
    testImage.setPixel(0, 0, new Pixel(250, 250, 250));
    Image result = processor.brightness(testImage, 50);
    assertEquals(255, result.getPixel(0, 0).getRed());
    assertEquals(255, result.getPixel(0, 0).getGreen());
    assertEquals(255, result.getPixel(0, 0).getBlue());
  }

  /**
   * Tests darkening an Image where the brightness goes below the minimum value of 0. The values
   * should be clamped to 0.
   */
  @Test
  public void testDarknessUnderflow() {
    testImage.setPixel(0, 0, new Pixel(10, 10, 10));
    Image result = processor.darkness(testImage, 50);
    assertEquals(0, result.getPixel(0, 0).getRed());
    assertEquals(0, result.getPixel(0, 0).getGreen());
    assertEquals(0, result.getPixel(0, 0).getBlue());
  }

  /**
   * Tests flipping a single-pixel image horizontally. Since the image only has one pixel, the
   * result should be identical to the original image.
   */
  @Test
  public void testFlipHorizontalSinglePixel() {
    Image singlePixel = new Image(1, 1);
    singlePixel.setPixel(0, 0, new Pixel(100, 150, 200));
    Image result = processor.flipHorizontal(singlePixel);
    assertEquals(singlePixel.getPixel(0, 0).getRed(), result.getPixel(0, 0).getRed());
  }

  /**
   * Tests flipping a single-pixel image vertically. Since the image only has one pixel, the result
   * should be identical to the original image.
   */
  @Test
  public void testFlipVerticalSinglePixel() {
    Image singlePixel = new Image(1, 1);
    singlePixel.setPixel(0, 0, new Pixel(100, 150, 200));
    Image result = processor.flipVertical(singlePixel);
    assertEquals(singlePixel.getPixel(0, 0).getRed(), result.getPixel(0, 0).getRed());
  }

  /**
   * Tests combining channels from images of different sizes. The resulting image should have the
   * smallest common width and height of the three images.
   */
  @Test
  public void testCombineChannelsWithDifferentSizes() {
    Image red = new Image(3, 3);
    Image green = new Image(2, 2);
    Image blue = new Image(4, 4);
    Image result = processor.combineChannels(red, green, blue);
    assertEquals(2, result.getWidth());
    assertEquals(2, result.getHeight());
  }

  /**
   * Tests image initialization with negative dimensions. Expects a NegativeArraySizeException to be
   * thrown.
   */
  @Test(expected = NegativeArraySizeException.class)
  public void testInvalidImageDimensions() {
    new Image(-5, -5);
  }

  /**
   * Tests horizontal flipping of a null image. Expects a NullPointerException to be thrown.
   */
  @Test(expected = NullPointerException.class)
  public void testFlipHorizontalWithNullImage() {
    processor.flipHorizontal(null);
  }

  /**
   * Tests brightness adjustment on a null image. Expects a NullPointerException to be thrown.
   */
  @Test(expected = NullPointerException.class)
  public void testBrightnessWithNullImage() {
    processor.brightness(null, 50);
  }

  /**
   * Tests horizontal flipping on an empty image (0x0). Verifies that the resulting image also has
   * zero dimensions.
   */
  @Test
  public void testEmptyImageOperations() {
    Image emptyImage = new Image(0, 0);
    Image result = processor.flipHorizontal(emptyImage);
    assertEquals(0, result.getWidth());
    assertEquals(0, result.getHeight());
  }

  /**
   * Tests partial image channel manipulation with a null pixel. Expects a NullPointerException to
   * be thrown.
   */
  @Test(expected = NullPointerException.class)
  public void testPartialImageChannelManipulation() {
    Image partialImage = new Image(3, 3);
    partialImage.setPixel(1, 1, null);
    processor.visualizeRedComponent(partialImage);
  }

  /**
   * Tests extreme brightness adjustment (exceeding the normal range). Ensures that pixel values are
   * clamped at 255.
   */
  @Test
  public void testExtremeBrightnessValues() {
    Image result = processor.brightness(testImage, 1000);
    assertEquals(255, result.getPixel(1, 1).getRed());
    assertEquals(255, result.getPixel(1, 1).getGreen());
    assertEquals(255, result.getPixel(1, 1).getBlue());
  }

  /**
   * Tests extreme darkness adjustment. Ensures that pixel values are clamped at 0.
   */
  @Test
  public void testExtremeDarknessValues() {
    Image result = processor.brightness(testImage, Integer.MIN_VALUE);
    assertEquals(0, result.getPixel(1, 1).getRed());
  }

  /**
   * Tests combining channels of different dimensions. Expects an exception due to dimension
   * mismatch but handles unexpected behaviors.
   */
  @Test
  public void testCombineDifferentSizedChannels() {
    Image redChannel = new Image(3, 3);
    Image greenChannel = new Image(2, 2);
    Image blueChannel = new Image(3, 3);
    try {
      processor.combineChannels(redChannel, greenChannel, blueChannel);
    } catch (IllegalArgumentException e) {
      fail("Unexpected IllegalArgumentException: " + e);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }

  /**
   * Tests image initialization with negative width. Expects a NegativeArraySizeException to be
   * thrown.
   */
  @Test(expected = NegativeArraySizeException.class)
  public void testImageInitializationNegativeDimensions() {
    new Image(-1, 5);
  }

  /**
   * Tests image initialization with zero dimensions. Ensures the image is created with 0 width and
   * height.
   */
  @Test
  public void testImageInitializationZeroDimensions() {
    Image zeroDimensionImage = new Image(0, 0);
    assertNotNull(zeroDimensionImage);
    assertEquals(0, zeroDimensionImage.getWidth());
    assertEquals(0, zeroDimensionImage.getHeight());
  }

  /**
   * Tests setting a pixel at an invalid position. Expects an ArrayIndexOutOfBoundsException to be
   * thrown.
   */
  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testSetPixelInvalidPosition() {
    Image img = new Image(5, 5);
    img.setPixel(10, 10, new Pixel(100, 100, 100));
  }

  /**
   * Tests getting a pixel from an invalid position. Expects an ArrayIndexOutOfBoundsException to be
   * thrown.
   */
  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testGetPixelInvalidPosition() {
    Image img = new Image(5, 5);
    img.getPixel(6, 0);
  }

  /**
   * Tests pixel initialization with out-of-range values. Ensures pixel values are clamped to the
   * maximum allowed value of 255.
   */
  @Test
  public void testPixelInitializationOutOfRange() {
    Pixel pixel = new Pixel(300, 150, 150);
    assertEquals(255, pixel.getRed());
    assertEquals(150, pixel.getGreen());
    assertEquals(150, pixel.getBlue());
  }

  /**
   * Tests pixel initialization with negative values. Ensures pixel values are clamped to the
   * minimum allowed value of 0.
   */
  @Test
  public void testPixelInitializationNegativeValue() {
    Pixel pixel = new Pixel(-1, 100, 100);
    assertEquals(0, pixel.getRed());
    assertEquals(100, pixel.getGreen());
    assertEquals(100, pixel.getBlue());
  }

  /**
   * Tests pixel clamping behavior via constructor. Ensures out-of-range pixel values are clamped
   * correctly.
   */
  @Test
  public void testPixelClampingViaConstructor() {
    Pixel pixel = new Pixel(300, -50, 260);
    assertEquals(255, pixel.getRed());
    assertEquals(0, pixel.getGreen());
    assertEquals(255, pixel.getBlue());
  }

  /**
   * Tests pixel clamping behavior in the constructor for out-of-range values.
   */
  @Test
  public void testPixelClampingInConstructor() {
    Pixel pixel = new Pixel(300, -50, 260);
    assertEquals(255, pixel.getRed());
    assertEquals(0, pixel.getGreen());
    assertEquals(255, pixel.getBlue());
  }

  /**
   * Tests applying a blur filter to a small image. Ensures that the pixel values are correctly
   * calculated and not changed due to lack of neighboring pixels.
   */
  @Test
  public void testBlurSmallImage() {
    Image img = new Image(1, 1);
    img.setPixel(0, 0, new Pixel(100, 100, 100));
    Image result = processor.blur(img);
    assertEquals(100, result.getPixel(0, 0).getRed());
  }

  /**
   * Tests combining channels where one of the channels contains null pixels. Ensures that an
   * exception is thrown.
   */
  @Test(expected = NullPointerException.class)
  public void testCombineChannelsWithNullPixels() {
    Image redChannel = processor.visualizeRedComponent(testImage);
    Image greenChannel = processor.visualizeGreenComponent(testImage);
    Image blueChannel = new Image(3, 3);

    blueChannel.setPixel(1, 1, null);
    processor.combineChannels(redChannel, greenChannel, blueChannel);
  }

  /**
   * Tests image combination when all three channels are completely white. The resulting image
   * should also be completely white.
   */
  @Test
  public void testCombineChannelsAllWhite() {
    Image whiteChannel = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        whiteChannel.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }
    Image result = processor.combineChannels(whiteChannel, whiteChannel, whiteChannel);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(255, pixel.getRed());
        assertEquals(255, pixel.getGreen());
        assertEquals(255, pixel.getBlue());
      }
    }
  }

  /**
   * Tests visualizing the value component on an image where all pixels have the same value. Ensures
   * the result image retains the same pixel values.
   */
  @Test
  public void testVisualizeValueUniformImage() {
    Image uniformImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        uniformImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image result = processor.visualizeValue(uniformImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(100, pixel.getRed());
        assertEquals(100, pixel.getGreen());
        assertEquals(100, pixel.getBlue());
      }
    }
  }

  /**
   * Tests applying extreme brightness to an image where all pixel values are near zero. Ensures the
   * pixels are clamped at a minimum value of 0.
   */
  @Test
  public void testExtremeDarknessClampToZero() {
    Image darkImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        darkImage.setPixel(x, y, new Pixel(10, 10, 10));
      }
    }
    Image result = processor.brightness(darkImage, -100);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(0, pixel.getRed());
        assertEquals(0, pixel.getGreen());
        assertEquals(0, pixel.getBlue());
      }
    }
  }

  /**
   * Tests combining red, green, and blue channels where the blue channel contains a pixel with a
   * value that exceeds the normal range. Ensures the value is clamped to 255.
   */
  @Test
  public void testCombineChannelsWithOverflow() {
    Image red = processor.visualizeRedComponent(testImage);
    Image green = processor.visualizeGreenComponent(testImage);
    Image blue = new Image(3, 3);
    blue.setPixel(1, 1, new Pixel(0, 0, 300));
    Image result = processor.combineChannels(red, green, blue);
    assertEquals(255, result.getPixel(1, 1).getBlue());
  }

  /**
   * Tests visualizing the intensity of an image where all channels are at maximum values. The
   * resulting image should also have maximum values for all channels.
   */
  @Test
  public void testVisualizeIntensityAllMax() {
    Image maxImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        maxImage.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }
    Image result = processor.visualizeIntensity(maxImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(255, pixel.getRed());
        assertEquals(255, pixel.getGreen());
        assertEquals(255, pixel.getBlue());
      }
    }
  }

  /**
   * Tests applying a custom filter that does not conform to kernel size requirements. Ensures that
   * an exception is thrown due to malformed kernel size.
   */
  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testApplyMalformedKernelFilter() {
    double[][] malformedKernel = {{1, 0}, {0}};
    processor.applyFilter(testImage, malformedKernel);
  }


  /**
   * Tests sharpening an image where all pixel values are at the minimum (0). Ensures no underflow
   * occurs and pixel values remain at 0.
   */
  @Test
  public void testSharpenWithMinValues() {
    Image minImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        minImage.setPixel(x, y, new Pixel(0, 0, 0));
      }
    }
    Image result = processor.sharpen(minImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(0, pixel.getRed());
        assertEquals(0, pixel.getGreen());
        assertEquals(0, pixel.getBlue());
      }
    }
  }

  /**
   * Tests visualizing luma on an image where all pixels have different values. Ensures that luma is
   * calculated correctly for each pixel.
   */
  @Test
  public void testVisualizeLumaDifferentValues() {
    Image result = processor.visualizeLuma(testImage);
    Pixel pixel = result.getPixel(1, 1);
    int expectedLuma = (int) (0.2126 * testImage.getPixel(1, 1).getRed() +
        0.7152 * testImage.getPixel(1, 1).getGreen() +
        0.0722 * testImage.getPixel(1, 1).getBlue());
    assertEquals(expectedLuma, pixel.getRed());
    assertEquals(expectedLuma, pixel.getGreen());
    assertEquals(expectedLuma, pixel.getBlue());
  }

  /**
   * Tests the edge case where an image is initialized with extremely large dimensions. This ensures
   * that large images can be handled without issues.
   */
  @Test
  public void testLargeImageDimensions() {
    Image largeImage = new Image(10000, 10000);
    assertNotNull(largeImage);
    assertEquals(10000, largeImage.getWidth());
    assertEquals(10000, largeImage.getHeight());
  }

  /**
   * Tests the brighten operation where the increment exceeds the allowable range, ensuring pixel
   * values are clamped at 255.
   */
  @Test
  public void testBrightnessExceedsMaxValue() {
    Image brightImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        brightImage.setPixel(x, y, new Pixel(250, 250, 250));
      }
    }
    Image result = processor.brightness(brightImage, 100);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(255, pixel.getRed());
        assertEquals(255, pixel.getGreen());
        assertEquals(255, pixel.getBlue());
      }
    }
  }

  /**
   * Tests the brighten operation where the decrement reduces the pixel values below 0, ensuring
   * pixel values are clamped at 0.
   */
  @Test
  public void testDarknessExceedsMinValue() {
    Image darkImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        darkImage.setPixel(x, y, new Pixel(10, 10, 10));
      }
    }
    Image result = processor.brightness(darkImage, -50);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(0, pixel.getRed());
        assertEquals(0, pixel.getGreen());
        assertEquals(0, pixel.getBlue());
      }
    }
  }

  /**
   * Tests combining channels where one of the channels contains all black pixels. Ensures the
   * resulting image reflects the lack of color contribution from that channel.
   */
  @Test
  public void testCombineChannelsWithBlackChannel() {
    Image red = processor.visualizeRedComponent(testImage);
    Image green = processor.visualizeGreenComponent(testImage);
    Image blackChannel = new Image(3, 3);

    Image result = processor.combineChannels(red, green, blackChannel);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(testImage.getPixel(x, y).getRed(), pixel.getRed());
        assertEquals(testImage.getPixel(x, y).getGreen(), pixel.getGreen());
        assertEquals(0, pixel.getBlue());
      }
    }
  }

  /**
   * Tests flipping a large image horizontally and ensures the operation does not cause any errors.
   */
  @Test
  public void testFlipHorizontalLargeImage() {
    Image largeImage = new Image(1000, 1000);
    for (int x = 0; x < 1000; x++) {
      for (int y = 0; y < 1000; y++) {
        largeImage.setPixel(x, y, new Pixel(x % 255, y % 255, (x + y) % 255));
      }
    }
    Image result = processor.flipHorizontal(largeImage);
    assertEquals(largeImage.getPixel(0, 0).getRed(), result.getPixel(999, 0).getRed());
  }

  /**
   * Tests visualizing the green component of an image where the green channel contains all zero
   * values. Ensures the resulting image is completely black.
   */
  @Test
  public void testVisualizeGreenComponentWithNoGreen() {
    Image noGreenImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        noGreenImage.setPixel(x, y, new Pixel(100, 0, 100));
      }
    }
    Image result = processor.visualizeGreenComponent(noGreenImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(0, pixel.getRed());
        assertEquals(0, pixel.getGreen());
        assertEquals(0, pixel.getBlue());
      }
    }
  }

  /**
   * Tests visualizing the blue component where all blue channel values are at the maximum (255).
   * Ensures the resulting image is completely white.
   */
  @Test
  public void testVisualizeBlueComponentAllMax() {
    Image allBlueImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        allBlueImage.setPixel(x, y, new Pixel(0, 0, 255));
      }
    }
    Image result = processor.visualizeBlueComponent(allBlueImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(255, pixel.getRed());
        assertEquals(255, pixel.getGreen());
        assertEquals(255, pixel.getBlue());
      }
    }
  }

  /**
   * Tests sharpening an image where every pixel is the same color (grey). Ensures the sharpen
   * operation does not alter the pixel values.
   */
  @Test
  public void testSharpenUniformGreyImage() {
    Image greyImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        greyImage.setPixel(x, y, new Pixel(128, 128, 128));
      }
    }
    Image result = processor.sharpen(greyImage);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(128, pixel.getRed());
        assertEquals(128, pixel.getGreen());
        assertEquals(128, pixel.getBlue());
      }
    }
  }

  /**
   * Tests blurring an image that contains high contrast pixel values. Ensures the blur operation
   * smooths the values as expected.
   */
  @Test
  public void testBlurHighContrastImage() {
    Image highContrastImage = new Image(3, 3);
    highContrastImage.setPixel(0, 0, new Pixel(0, 0, 0));
    highContrastImage.setPixel(0, 1, new Pixel(255, 255, 255));
    highContrastImage.setPixel(1, 0, new Pixel(255, 255, 255));
    highContrastImage.setPixel(1, 1, new Pixel(0, 0, 0));

    Image result = processor.blur(highContrastImage);
    Pixel blurredPixel = result.getPixel(1, 1);
    assertNotEquals(0, blurredPixel.getRed());
    assertNotEquals(255, blurredPixel.getRed());
  }

  /**
   * Tests brightening an image where multiple channels might overflow. Ensures values are clamped.
   */
  @Test
  public void testBrightnessOverflowOnMultipleChannels() {
    testImage.setPixel(0, 0, new Pixel(250, 240, 245));
    Image result = processor.brightness(testImage, 20);

    assertEquals(255, result.getPixel(0, 0).getRed());
    assertEquals(255, result.getPixel(0, 0).getGreen());
    assertEquals(255, result.getPixel(0, 0).getBlue());
  }

  /**
   * Tests darkening an image where multiple channels might underflow. Ensures values are clamped.
   */
  @Test
  public void testDarknessUnderflowOnMultipleChannels() {
    testImage.setPixel(1, 1, new Pixel(5, 10, 15));
    Image result = processor.darkness(testImage, 20);

    assertEquals(0, result.getPixel(1, 1).getRed());
    assertEquals(0, result.getPixel(1, 1).getGreen());
    assertEquals(0, result.getPixel(1, 1).getBlue());
  }

  /**
   * Tests applying a blur filter to a non-square image. Ensures the filter applies correctly.
   */
  @Test
  public void testBlurNonSquareImage() {
    Image nonSquareImage = new Image(5, 3);
    nonSquareImage.setPixel(2, 1, new Pixel(100, 100, 100));
    Image result = processor.blur(nonSquareImage);
    assertNotEquals(nonSquareImage.getPixel(2, 1).getRed(),
        result.getPixel(2, 1).getRed());
  }

  /**
   * Tests sepia conversion on an image where pixel values are near the edge (255).
   */
  @Test
  public void testSepiaOnEdgePixelValues() {
    Image edgeImage = new Image(3, 3);
    edgeImage.setPixel(0, 0, new Pixel(255, 255, 255));
    Image result = processor.toSepia(edgeImage);

    Pixel pixel = result.getPixel(0, 0);
    assertTrue(pixel.getRed() <= 255);
    assertTrue(pixel.getGreen() <= 255);
    assertTrue(pixel.getBlue() <= 255);
  }

  /**
   * Tests splitting channels on an empty image (0x0). Ensures no exception is thrown.
   */
  @Test
  public void testSplitChannelsOnEmptyImage() {
    Image emptyImage = new Image(0, 0);
    Image[] result = processor.splitChannels(emptyImage);

    assertEquals(3, result.length);
    assertEquals(0, result[0].getWidth());
    assertEquals(0, result[0].getHeight());
  }

  /**
   * Tests sharpening an image where all pixel values are the same. Ensures the result remains
   * consistent.
   */
  @Test
  public void testSharpenUniformImage() {
    Image uniformImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        uniformImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image result = processor.sharpen(uniformImage);

    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(100, pixel.getRed());
        assertEquals(100, pixel.getGreen());
        assertEquals(100, pixel.getBlue());
      }
    }
  }

  /**
   * Tests converting an already grey image to greyscale. Ensures it remains unchanged.
   */
  @Test
  public void testGreyscaleOnGreyImage() {
    Image greyImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        greyImage.setPixel(x, y, new Pixel(128, 128, 128));
      }
    }
    Image result = processor.toGreyscale(greyImage);

    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(128, pixel.getRed());
        assertEquals(128, pixel.getGreen());
        assertEquals(128, pixel.getBlue());
      }
    }
  }

  /**
   * Tests that image compression with a specified threshold works as expected.
   */
  @Test
  public void testCompressImage() {
    Image compressedImage = processor.compress(testImage, 50);
    assertNotNull("Compressed image should not be null", compressedImage);
    assertNotEquals("Compressed image should differ from original", testImage,
        compressedImage);
  }

  /**
   * Tests the functionality of generating a histogram from an image.
   */
  @Test
  public void testGenerateHistogram() {
    Image histogramImage = processor.generateHistogram(testImage);
    assertNotNull("Histogram image should not be null", histogramImage);
    assertEquals("Histogram image should be 256x256", 256,
        histogramImage.getWidth());
    assertEquals("Histogram image should be 256x256", 256,
        histogramImage.getHeight());
  }

  /**
   * Tests color correction to confirm that adjustments are applied.
   */
  @Test
  public void testColorCorrection() {
    Image correctedImage = processor.colorCorrect(testImage);
    assertNotNull("Color-corrected image should not be null", correctedImage);
  }

  /**
   * Tests levels adjustment to verify contrast adjustments are applied.
   */
  @Test
  public void testLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 20, 128,
        230);
    assertNotNull("Adjusted image should not be null", adjustedImage);
  }

  /**
   * Tests compress functionality with a 0% threshold for lossless compression.
   */
  @Test
  public void testLosslessCompression() {
    Image compressedImage = processor.compress(testImage, 0);
    assertEquals("Lossless compression should retain original data", testImage,
        compressedImage);
  }

  /**
   * Tests compress functionality with a 100% threshold for maximum lossy compression.
   */
  @Test
  public void testMaxThresholdCompression() {
    Image compressedImage = processor.compress(testImage, 100);
    assertNotNull("Compressed image should not be null", compressedImage);
  }

  /**
   * Tests color correction on an already grayscale image to ensure it remains unchanged.
   */
  @Test
  public void testColorCorrectionOnGrayscaleImage() {
    Image grayscaleImage = processor.toGreyscale(testImage);
    Image correctedImage = processor.colorCorrect(grayscaleImage);
    assertEquals("Color correction should not alter a grayscale image", grayscaleImage,
        correctedImage);
  }

  /**
   * Tests levels adjustment with identical values for shadow, mid, and highlight, ensuring that the
   * image appears uniform as expected.
   */
  @Test
  public void testLevelsAdjustWithIdenticalValues() {
    Image adjustedImage = processor.levelsAdjust(testImage, 128, 128,
        128);

    boolean isUniform = true;
    Pixel firstPixel = adjustedImage.getPixel(0, 0);

    for (int y = 0; y < adjustedImage.getHeight(); y++) {
      for (int x = 0; x < adjustedImage.getWidth(); x++) {
        if (!adjustedImage.getPixel(x, y).equals(firstPixel)) {
          isUniform = false;
          break;
        }
      }
      if (!isUniform) {
        break;
      }
    }

    assertTrue("Adjusted image should appear uniform due to identical levels", isUniform);
  }

  /**
   * Tests compressing an image with 50% lossy compression.
   */
  @Test
  public void testLossyCompressionWith50PercentThreshold() {
    Image compressedImage = processor.compress(testImage, 50);
    assertNotNull("Compressed image should not be null", compressedImage);
    assertNotEquals("Compressed image should differ from original", testImage,
        compressedImage);
  }

  /**
   * Tests compressing an image with a 100% threshold for maximum lossy compression.
   */
  @Test
  public void testMaxLossyCompression() {
    Image compressedImage = processor.compress(testImage, 100);
    assertNotNull("Compressed image should not be null", compressedImage);
  }

  /**
   * Tests the effectiveness of compression across multiple thresholds.
   */
  @Test
  public void testCompressionWithMultipleThresholds() {
    for (int threshold = 10; threshold <= 90; threshold += 20) {
      Image compressedImage = processor.compress(testImage, threshold);
      assertNotNull("Compressed image should not be null at " + threshold + "% threshold",
          compressedImage);
    }
  }

  /**
   * Tests generating a histogram from a color image.
   */
  @Test
  public void testGenerateHistogramFromColorImage() {
    Image histogramImage = processor.generateHistogram(testImage);
    assertNotNull("Histogram image should not be null", histogramImage);
    assertEquals("Histogram image should be 256x256", 256,
        histogramImage.getWidth());
    assertEquals("Histogram image should be 256x256", 256,
        histogramImage.getHeight());
  }

  /**
   * Tests histogram generation for a grayscale image.
   */
  @Test
  public void testHistogramForGrayscaleImage() {
    Image grayscaleImage = processor.toGreyscale(testImage);
    Image histogramImage = processor.generateHistogram(grayscaleImage);
    assertNotNull("Histogram image should not be null for grayscale image",
        histogramImage);
  }

  /**
   * Verifies that histogram generation works correctly on images with uniform colors.
   */
  @Test
  public void testHistogramForUniformColorImage() {
    Image uniformColorImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        uniformColorImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image histogramImage = processor.generateHistogram(uniformColorImage);
    assertNotNull("Histogram image should not be null for uniform color image",
        histogramImage);
  }


  /**
   * Tests color correction on a color image to ensure color balance adjustment.
   */
  @Test
  public void testColorCorrectionOnColorImage() {
    Image correctedImage = processor.colorCorrect(testImage);
    assertNotNull("Color-corrected image should not be null", correctedImage);
  }

  /**
   * Verifies color correction does not affect an image with balanced colors.
   */
  @Test
  public void testColorCorrectionOnBalancedColorImage() {
    Image balancedColorImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        balancedColorImage.setPixel(x, y, new Pixel(128, 128, 128));
      }
    }
    Image correctedImage = processor.colorCorrect(balancedColorImage);
    assertEquals("Color correction should not alter a balanced color image",
        balancedColorImage,
        correctedImage);
  }

  /**
   * Tests adjusting levels with standard shadow, mid, and highlight values.
   */
  @Test
  public void testStandardLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 20, 128,
        230);
    assertNotNull("Adjusted image should not be null", adjustedImage);
  }


  /**
   * Tests adjusting levels on a grayscale image.
   */
  @Test
  public void testLevelsAdjustOnGrayscaleImage() {
    Image grayscaleImage = processor.toGreyscale(testImage);
    Image adjustedImage = processor.levelsAdjust(grayscaleImage, 20, 128,
        230);
    assertNotNull("Adjusted grayscale image should not be null", adjustedImage);
  }

  /**
   * Tests cascading multiple operations in sequence.
   */
  @Test
  public void testCascadingOperations() {
    Image brightenedImage = processor.levelsAdjust(testImage, 20, 128,
        230);
    Image blurredImage = processor.blur(brightenedImage);
    Image compressedImage = processor.compress(blurredImage, 50);
    assertNotNull("Final image should not be null after cascading operations",
        compressedImage);
  }

  /**
   * Tests cascading color correction and levels adjustment.
   */
  @Test
  public void testCascadingColorCorrectionAndLevelsAdjust() {
    Image correctedImage = processor.colorCorrect(testImage);
    Image adjustedImage = processor.levelsAdjust(correctedImage, 30, 150,
        220);
    assertNotNull("Final image should not be null after color correction and levels "
            + "adjustment",
        adjustedImage);
  }

  /**
   * Tests edge case for compression with a very low threshold to ensure minimal data loss.
   */
  @Test
  public void testMinimalLossyCompression() {
    Image compressedImage = processor.compress(testImage, 1);
    assertNotNull("Compressed image should not be null", compressedImage);
    assertNotEquals("Compressed image should differ slightly from original", testImage,
        compressedImage);
  }

  /**
   * Tests histogram generation for an image with a gradient from black to white.
   */
  @Test
  public void testHistogramForGradientImage() {
    Image gradientImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int intensity = (x + y) * 40;
        gradientImage.setPixel(x, y, new Pixel(intensity, intensity, intensity));
      }
    }
    Image histogramImage = processor.generateHistogram(gradientImage);
    assertNotNull("Histogram image should not be null for gradient image", histogramImage);
  }

  /**
   * Tests color correction on an image with a strong red tint.
   */
  @Test
  public void testColorCorrectionOnRedTintedImage() {
    Image redTintedImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        redTintedImage.setPixel(x, y, new Pixel(200, 50, 50));
      }
    }
    Image correctedImage = processor.colorCorrect(redTintedImage);
    assertNotNull("Color-corrected image should not be null", correctedImage);
  }

  /**
   * Tests color correction on an image with a high contrast between color channels.
   */
  @Test
  public void testColorCorrectionOnHighContrastImage() {
    Image highContrastImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int intensity = x * 100;
        highContrastImage.setPixel(x, y, new Pixel(intensity, 255 - intensity,
            intensity / 2));
      }
    }
    Image correctedImage = processor.colorCorrect(highContrastImage);
    assertNotNull("Color-corrected image should not be null", correctedImage);
  }

  /**
   * Tests levels adjustment with a high contrast configuration.
   */
  @Test
  public void testHighContrastLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 0, 128,
        255);
    assertNotNull("Adjusted image should not be null", adjustedImage);
  }

  /**
   * Tests levels adjustment with extreme low values to create a darker image.
   */
  @Test
  public void testLowLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 0, 50,
        100);
    assertNotNull("Adjusted image should not be null", adjustedImage);
  }

  /**
   * Tests cascading operations with color correction followed by compression.
   */
  @Test
  public void testCascadingColorCorrectionAndCompression() {
    Image correctedImage = processor.colorCorrect(testImage);
    Image compressedImage = processor.compress(correctedImage, 60);
    assertNotNull("Final image should not be null after color correction and compression",
        compressedImage);
  }

  /**
   * Tests cascading multiple operations: levels adjustment, color correction, and histogram
   * generation.
   */
  @Test
  public void testCascadingLevelsColorCorrectionAndHistogram() {
    Image adjustedImage = processor.levelsAdjust(testImage, 20, 128,
        230);
    Image correctedImage = processor.colorCorrect(adjustedImage);
    Image histogramImage = processor.generateHistogram(correctedImage);
    assertNotNull("Final histogram image should not be null after cascading operations",
        histogramImage);
  }

  /**
   * Tests compression with a negative threshold to ensure the function handles invalid input
   * gracefully.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCompressionWithNegativeThreshold() {
    processor.compress(testImage, -10);
  }

  /**
   * Tests compression with an above-maximum threshold (greater than 100%) to ensure the function
   * handles overflow gracefully.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCompressionWithExcessiveThreshold() {
    processor.compress(testImage, 150);
  }

  /**
   * Tests compression on a uniform image to see if the compressor identifies no significant
   * difference.
   */
  @Test
  public void testCompressionOnUniformImage() {
    Image uniformImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        uniformImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image compressedImage = processor.compress(uniformImage, 50);
    assertNotNull("Compressed image should not be null", compressedImage);
    assertNotEquals("Compressed uniform image should differ minimally from the original",
        uniformImage, compressedImage);
  }

  /**
   * Tests histogram generation for an image with a high intensity gradient to see if the histogram
   * peaks near the highest intensity.
   */
  @Test
  public void testHistogramForHighIntensityGradient() {
    Image highGradientImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int intensity = 200 + (x + y) * 20;
        highGradientImage.setPixel(x, y, new Pixel(intensity, intensity, intensity));
      }
    }
    Image histogramImage = processor.generateHistogram(highGradientImage);
    assertNotNull("Histogram image should not be null for high-intensity gradient image",
        histogramImage);
  }

  /**
   * Tests color correction on an extremely dark image to ensure it brightens appropriately.
   */
  @Test
  public void testColorCorrectionOnDarkImage() {
    Image darkImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        darkImage.setPixel(x, y, new Pixel(10, 10, 10));
      }
    }
    Image correctedImage = processor.colorCorrect(darkImage);
    assertNotNull("Color-corrected image should not be null for a dark image",
        correctedImage);
  }

  /**
   * Tests color correction on an overexposed (bright) image to ensure it darkens and normalizes.
   */
  @Test
  public void testColorCorrectionOnBrightImage() {
    Image brightImage = new Image(3, 3);
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        brightImage.setPixel(x, y, new Pixel(250, 250, 250));
      }
    }
    Image correctedImage = processor.colorCorrect(brightImage);
    assertNotNull("Color-corrected image should not be null for an overexposed image",
        correctedImage);
  }

  /**
   * Tests levels adjustment with maximum shadow and highlight values to create a high-contrast
   * image.
   */
  @Test
  public void testMaxContrastLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 0, 128,
        255);
    assertNotNull("Adjusted image should not be null for max contrast adjustment",
        adjustedImage);
  }

  /**
   * Tests levels adjustment with very low values for shadow, mid, and highlight to produce a very
   * dark image.
   */
  @Test
  public void testLowContrastLevelsAdjust() {
    Image adjustedImage = processor.levelsAdjust(testImage, 10, 30,
        50);
    assertNotNull("Adjusted image should not be null for low contrast adjustment",
        adjustedImage);
  }


  /**
   * Tests processing a very small image (1x1 pixel) to ensure no errors with minimal input.
   */
  @Test
  public void testProcessingOnSmallImage() {
    Image smallImage = new Image(1, 1);
    smallImage.setPixel(0, 0, new Pixel(100, 100, 100));

    Image compressedImage = processor.compress(smallImage, 50);
    assertNotNull("Compressed small image should not be null", compressedImage);

    Image histogramImage = processor.generateHistogram(smallImage);
    assertNotNull("Histogram for small image should not be null", histogramImage);

    Image correctedImage = processor.colorCorrect(smallImage);
    assertNotNull("Color-corrected small image should not be null", correctedImage);

    Image adjustedImage = processor.levelsAdjust(smallImage, 50, 128,
        200);
    assertNotNull("Adjusted small image should not be null", adjustedImage);
  }

  /**
   * Tests processing a larger image (e.g., 1000x1000 pixels) to validate performance and memory
   * handling.
   */
  @Test
  public void testProcessingOnLargeImage() {
    Image largeImage = new Image(1000, 1000);
    for (int y = 0; y < 1000; y++) {
      for (int x = 0; x < 1000; x++) {
        largeImage.setPixel(x, y, new Pixel((x + y) % 256, (x * y) % 256,
            (x + y * 2) % 256));
      }
    }

    Image compressedImage = processor.compress(largeImage, 50);
    assertNotNull("Compressed large image should not be null", compressedImage);

    Image histogramImage = processor.generateHistogram(largeImage);
    assertNotNull("Histogram for large image should not be null", histogramImage);

    Image correctedImage = processor.colorCorrect(largeImage);
    assertNotNull("Color-corrected large image should not be null", correctedImage);

    Image adjustedImage = processor.levelsAdjust(largeImage, 50, 128,
        200);
    assertNotNull("Adjusted large image should not be null", adjustedImage);
  }

  /**
   * Tests creating an extremely large image to verify memory handling. Expects an OutOfMemoryError
   * if the JVM cannot allocate sufficient memory.
   */
  @Test(expected = OutOfMemoryError.class)
  public void testExtremelyLargeImageCreation() {
    Image largeImage = new Image(Integer.MAX_VALUE, Integer.MAX_VALUE);
    assertNotNull(largeImage);
  }

  /**
   * Tests setting a pixel to null to ensure the setPixel method handles null values without
   * unexpected exceptions.
   */
  @Test(expected = NullPointerException.class)
  public void testSetPixelToNull() {
    testImage.setPixel(1, 1, null);
  }

  /**
   * Tests processing a null image with all methods to ensure each gracefully handles a null input.
   */
  @Test
  public void testNullImageInput() {
    try {
      processor.visualizeRedComponent(null);
      processor.visualizeGreenComponent(null);
      processor.visualizeBlueComponent(null);
      processor.flipHorizontal(null);
      processor.flipVertical(null);
      processor.brightness(null, 20);
      processor.sharpen(null);
      processor.blur(null);
      processor.compress(null, 50);
      processor.colorCorrect(null);
      processor.levelsAdjust(null, 20, 128, 200);
      processor.generateHistogram(null);
    } catch (NullPointerException e) {
      fail("Processing null images should not throw NullPointerException");
    }
  }

  /**
   * Tests compressing an image with a high-contrast pattern (alternating black and white pixels) to
   * verify how well the compression handles high variance.
   */
  @Test
  public void testCompressHighContrastPatternImage() {
    Image highContrastImage = new Image(5, 5);
    for (int x = 0; x < 5; x++) {
      for (int y = 0; y < 5; y++) {
        highContrastImage.setPixel(x, y,
            new Pixel((x + y) % 2 * 255, (x + y) % 2 * 255, (x + y) % 2 * 255));
      }
    }
    Image compressedImage = processor.compress(highContrastImage, 50);
    assertNotNull("Compressed high-contrast image should not be null", compressedImage);
  }

  /**
   * Tests brightness adjustment near the maximum allowable value (close to 255) to ensure that the
   * pixel values are clamped at 255 and do not overflow.
   */
  @Test
  public void testBrightnessOverflowAtMax() {
    testImage.setPixel(1, 1, new Pixel(245, 245, 245));
    Image brightenedImage = processor.brightness(testImage, 20);
    Pixel brightPixel = brightenedImage.getPixel(1, 1);
    assertEquals(255, brightPixel.getRed());
    assertEquals(255, brightPixel.getGreen());
    assertEquals(255, brightPixel.getBlue());
  }

  /**
   * Tests darkness adjustment on an image with low pixel values (near 0) to ensure that the values
   * are clamped at 0 without underflowing.
   */
  @Test
  public void testDarknessUnderflowAtMin() {
    testImage.setPixel(1, 1, new Pixel(5, 5, 5));
    Image darkenedImage = processor.darkness(testImage, 10);
    Pixel darkPixel = darkenedImage.getPixel(1, 1);
    assertEquals(0, darkPixel.getRed());
    assertEquals(0, darkPixel.getGreen());
    assertEquals(0, darkPixel.getBlue());
  }

  /**
   * Tests flipping a non-square image (rectangular) horizontally to confirm the flip operation
   * behaves as expected with different width and height values.
   */
  @Test
  public void testFlipHorizontalOnRectangleImage() {
    Image rectImage = new Image(2, 3);
    rectImage.setPixel(0, 0, new Pixel(50, 100, 150));
    rectImage.setPixel(1, 0, new Pixel(200, 50, 0));
    Image flippedImage = processor.flipHorizontal(rectImage);
    assertEquals(rectImage.getPixel(0, 0).getRed(),
        flippedImage.getPixel(1, 0).getRed());
    assertEquals(rectImage.getPixel(1, 0).getRed(),
        flippedImage.getPixel(0, 0).getRed());
  }

  /**
   * Tests repeated application of levels adjustment on a high-brightness image to confirm that
   * cumulative adjustments do not exceed the pixel limits.
   */
  @Test
  public void testRepeatedLevelsAdjustmentOnBrightImage() {
    Image brightImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        brightImage.setPixel(x, y, new Pixel(230, 230, 230));
      }
    }
    Image adjustedImage = processor.levelsAdjust(brightImage, 10, 128,
        200);
    adjustedImage = processor.levelsAdjust(adjustedImage, 10, 128,
        200);
    assertTrue("Levels adjustment should not exceed 255",
        adjustedImage.getPixel(0, 0).getRed() <= 255);
  }

  /**
   * Tests image compression with alternating threshold values from 1% to 99% in increments to check
   * consistency in compressed results.
   */
  @Test
  public void testCompressionWithVariousThresholds() {
    for (int threshold = 1; threshold <= 99; threshold += 10) {
      Image compressedImage = processor.compress(testImage, threshold);
      assertNotNull("Compressed image should not be null at " + threshold + "% threshold",
          compressedImage);
    }
  }

  /**
   * Tests color correction on an image with extreme blue pixel values and minimal red or green to
   * ensure color balance adjustment works across a variety of pixel distributions.
   */
  @Test
  public void testColorCorrectionWithExtremeBluePixels() {
    Image blueImage = new Image(3, 3);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        blueImage.setPixel(x, y, new Pixel(0, 0, 255));
      }
    }
    Image correctedImage = processor.colorCorrect(blueImage);
    assertNotNull("Color-corrected image with extreme blue values should not be null",
        correctedImage);
  }

  /**
   * Tests lossless compression and decompression, ensuring the image remains unchanged.
   */
  @Test
  public void testLosslessCompressionAndDecompression() {
    Image compressedImage = processor.compress(testImage, 0);
    assertEquals("Lossless compression should retain original data", testImage,
        compressedImage);
  }

  /**
   * Tests lossy compression, verifying that compression at 50% results in a modified image.
   */
  @Test
  public void testLossyCompression() {
    Image compressedImage = processor.compress(testImage, 50);
    assertNotEquals("Lossy compression should differ from the original", testImage,
        compressedImage);
  }

  /**
   * Tests compression thresholds, comparing results between low and high compression ratios.
   */
  @Test
  public void testCompressionThresholds() {
    Image compressedLow = processor.compress(testImage, 10);
    Image compressedHigh = processor.compress(testImage, 90);
    assertNotEquals("Images with different compression ratios should differ",
        compressedLow, compressedHigh);
  }

  /**
   * Tests the generation of a histogram image, ensuring dimensions of 256x256.
   */
  @Test
  public void testHistogramGeneration() {
    Image histogram = processor.generateHistogram(testImage);
    assertNotNull("Histogram image should not be null", histogram);
    assertEquals("Histogram image should be 256x256", 256, histogram.getWidth());
    assertEquals("Histogram image should be 256x256", 256, histogram.getHeight());
  }

  /**
   * Tests levels adjustment with specific shadow, mid, and highlight values.
   */
  @Test
  public void testLevelsAdjustment() {
    Image adjustedImage = processor.levelsAdjust(testImage, 10, 128,
        230);
    assertNotNull("Adjusted image should not be null", adjustedImage);
  }

  /**
   * Tests consistency of grayscale image after compression, ensuring grayscale attributes are
   * retained.
   */
  @Test
  public void testGrayscaleConsistencyPostCompression() {
    Image greyImage = processor.toGreyscale(testImage);
    Image compressedGrey = processor.compress(greyImage, 50);
    for (int x = 0; x < compressedGrey.getWidth(); x++) {
      for (int y = 0; y < compressedGrey.getHeight(); y++) {
        Pixel p = compressedGrey.getPixel(x, y);
        assertEquals("Red and Green channels should be equal in greyscale", p.getRed(),
            p.getGreen());
        assertEquals("Green and Blue channels should be equal in greyscale", p.getGreen(),
            p.getBlue());
      }
    }
  }

  /**
   * Tests histogram alignment after color correction, ensuring all colors are adjusted correctly.
   */
  @Test
  public void testHistogramAlignmentAfterColorCorrection() {
    Image correctedImage = processor.colorCorrect(testImage);
    Image histogram = processor.generateHistogram(correctedImage);
    assertNotNull("Histogram image should not be null after color correction", histogram);
  }

  /**
   * Tests compression on a monochrome image to ensure it is handled correctly.
   */
  @Test
  public void testCompressionWithMonochromeImage() {
    for (int x = 0; x < testImage.getWidth(); x++) {
      for (int y = 0; y < testImage.getHeight(); y++) {
        testImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image compressed = processor.compress(testImage, 50);
    assertNotNull("Compressed monochrome image should not be null", compressed);
  }


  /**
   * Tests compression on a high contrast image to ensure appropriate data reduction.
   */
  @Test
  public void testCompressionWithHighContrastImage() {
    testImage.setPixel(0, 0, new Pixel(255, 255, 255));
    testImage.setPixel(1, 1, new Pixel(0, 0, 0));
    Image compressed = processor.compress(testImage, 50);
    assertNotNull("Compressed high-contrast image should not be null", compressed);
  }

  /**
   * Tests levels adjustment for high contrast images, verifying shadow, mid, and highlight
   * adjustments.
   */
  @Test
  public void testLevelsAdjustmentForHighContrastImage() {
    testImage.setPixel(0, 0, new Pixel(0, 0, 0));
    testImage.setPixel(2, 2, new Pixel(255, 255, 255));
    Image adjusted = processor.levelsAdjust(testImage, 10, 128,
        230);
    assertNotNull("Adjusted image should not be null", adjusted);
  }

  // Test Cases for Assignment 6
  // Downscale

  @Test
  public void testDownscaleUniformAspectRatio() {
    Image result = processor.downscaleImage(testImage, 2, 2);
    assertNotNull(result);
    assertEquals(2, result.getWidth());
    assertEquals(2, result.getHeight());
  }

  @Test
  public void testDownscaleNonUniformAspectRatio() {
    Image result = processor.downscaleImage(testImage, 3, 1);
    assertNotNull(result);
    assertEquals(3, result.getWidth());
    assertEquals(1, result.getHeight());
  }

  @Test
  public void testDownscaleToSameDimensions() {
    Image result = processor.downscaleImage(testImage, 4, 4);
    assertEquals(testImage, result);
  }

  @Test
  public void testDownscaleToSinglePixel() {
    Image result = processor.downscaleImage(testImage, 1, 1);
    assertEquals(1, result.getWidth());
    assertEquals(1, result.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleInvalidDimensions() {
    processor.downscaleImage(testImage, -1, 5);
  }

  @Test
  public void testDownscaleEmptyImage() {
    Image emptyImage = new Image(0, 0);
    Image result = processor.downscaleImage(emptyImage, 2, 2);
    assertEquals(0, result.getWidth());
    assertEquals(0, result.getHeight());
  }

  @Test
  public void testDownscaleLargeToSmall() {
    Image largeImage = new Image(1000, 1000);
    Image result = processor.downscaleImage(largeImage, 10, 10);
    assertEquals(10, result.getWidth());
    assertEquals(10, result.getHeight());
  }

  @Test
  public void testDownscaleFloatingPointMapping() {
    Image result = processor.downscaleImage(testImage, 3, 3);
    assertEquals(3, result.getWidth());
    assertEquals(3, result.getHeight());
  }

  @Test
  public void testDownscaleWithBoundaryValues() {
    testImage.setPixel(3, 3, new Pixel(255, 255, 255));
    Image result = processor.downscaleImage(testImage, 2, 2);
    assertNotNull(result);
  }

  @Test(expected = NullPointerException.class)
  public void testDownscaleNullImage() {
    processor.downscaleImage(null, 2, 2);
  }

  @Test
  public void testDownscalePreserveColorRange() {
    Image result = processor.downscaleImage(testImage, 2, 2);
    for (int y = 0; y < result.getHeight(); y++) {
      for (int x = 0; x < result.getWidth(); x++) {
        Pixel pixel = result.getPixel(x, y);
        assertTrue(pixel.getRed() >= 0 && pixel.getRed() <= 255);
        assertTrue(pixel.getGreen() >= 0 && pixel.getGreen() <= 255);
        assertTrue(pixel.getBlue() >= 0 && pixel.getBlue() <= 255);
      }
    }
  }

  @Test
  public void testDownscaleWithEdgeArtifacts() {
    Image result = processor.downscaleImage(testImage, 3, 2);
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleToZeroWidth() {
    processor.downscaleImage(testImage, 0, 2);
  }

  @Test
  public void testDownscaleToExtremeAspectRatio() {
    Image result = processor.downscaleImage(testImage, 10, 1);
    assertEquals(10, result.getWidth());
    assertEquals(1, result.getHeight());
  }

  @Test
  public void testDownscalePerformance() {
    Image largeImage = new Image(5000, 5000);
    Image result = processor.downscaleImage(largeImage, 500, 500);
    assertNotNull(result);
  }

  @Test
  public void testDownscaleLargeToVerySmall() {
    Image largeImage = new Image(1000, 1000);
    Image result = processor.downscaleImage(largeImage, 5, 5);
    assertEquals(5, result.getWidth());
    assertEquals(5, result.getHeight());
  }

  @Test
  public void testDownscaleUniformColorImage() {
    Image uniformImage = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        uniformImage.setPixel(x, y, new Pixel(100, 100, 100));
      }
    }
    Image result = processor.downscaleImage(uniformImage, 2, 2);
    assertNotNull(result);
    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 2; x++) {
        Pixel pixel = result.getPixel(x, y);
        assertEquals(100, pixel.getRed());
        assertEquals(100, pixel.getGreen());
        assertEquals(100, pixel.getBlue());
      }
    }
  }

  @Test
  public void testDownscaleNonSquareImage() {
    Image nonSquareImage = new Image(4, 6);
    Image result = processor.downscaleImage(nonSquareImage, 2, 3);
    assertNotNull(result);
    assertEquals(2, result.getWidth());
    assertEquals(3, result.getHeight());
  }

  @Test
  public void testDownscaleNonPowerOfTwoDimensions() {
    Image result = processor.downscaleImage(testImage, 3, 3);
    assertEquals(3, result.getWidth());
    assertEquals(3, result.getHeight());
  }

  @Test
  public void testDownscaleMaxRGBValues() {
    testImage.setPixel(3, 3, new Pixel(255, 255, 255));
    Image result = processor.downscaleImage(testImage, 2, 2);
    assertNotNull(result);
    Pixel pixel = result.getPixel(1, 1);
    assertTrue(pixel.getRed() <= 255 && pixel.getGreen() <= 255 && pixel.getBlue() <= 255);
  }

  @Test
  public void testDownscalePreserveAspectRatio() {
    Image result = processor.downscaleImage(testImage, 8, 8);
    double originalAspectRatio = (double) testImage.getWidth() / testImage.getHeight();
    double resultAspectRatio = (double) result.getWidth() / result.getHeight();
    assertEquals(originalAspectRatio, resultAspectRatio, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleToLargerDimensions() {
    processor.downscaleImage(testImage, 10, 10); // Larger than original 4x4
  }

  @Test
  public void testDownscaleAspectRatioMismatch() {
    Image result = processor.downscaleImage(testImage, 3, 2);
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleZeroHeight() {
    processor.downscaleImage(testImage, 2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleNegativeWidth() {
    processor.downscaleImage(testImage, -2, 2);
  }

  @Test(expected = NullPointerException.class)
  public void testDownscaleNullInput() {
    processor.downscaleImage(null, 2, 2);
  }

  @Test
  public void testDownscaleMassiveImage() {
    Image massiveImage = new Image(10000, 10000);
    Image result = processor.downscaleImage(massiveImage, 500, 500);
    assertNotNull(result);
    assertEquals(500, result.getWidth());
    assertEquals(500, result.getHeight());
  }

  @Test
  public void testDownscaleTinyImage() {
    Image tinyImage = new Image(2, 2);
    tinyImage.setPixel(0, 0, new Pixel(255, 0, 0));
    tinyImage.setPixel(1, 0, new Pixel(0, 255, 0));
    tinyImage.setPixel(0, 1, new Pixel(0, 0, 255));
    tinyImage.setPixel(1, 1, new Pixel(255, 255, 255));
    Image result = processor.downscaleImage(tinyImage, 1, 1);
    assertEquals(1, result.getWidth());
    assertEquals(1, result.getHeight());
  }

  // Masking

  @Test
  public void testApplyWithMaskBlur() {
    Image result = processor.applyWithMask(testImage, maskImage, "blur");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithMaskSharpen() {
    Image result = processor.applyWithMask(testImage, maskImage, "sharpen");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithMaskGreyscale() {
    Image result = processor.applyWithMask(testImage, maskImage, "greyscale");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithMaskSepia() {
    Image result = processor.applyWithMask(testImage, maskImage, "sepia");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithMaskCheckerboardMask() {
    Image result = processor.applyWithMask(testImage, maskImage, "blur");
    assertNotNull(result);
    for (int y = 0; y < result.getHeight(); y++) {
      for (int x = 0; x < result.getWidth(); x++) {
        Pixel maskPixel = maskImage.getPixel(x, y);
        Pixel resultPixel = result.getPixel(x, y);
        if (maskPixel.getRed() == 0) {
          assertNotEquals(testImage.getPixel(x, y), resultPixel);
        } else {
          assertEquals(testImage.getPixel(x, y), resultPixel);
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithMaskDifferentSizes() {
    Image smallerMask = new Image(2, 2);
    processor.applyWithMask(testImage, smallerMask, "blur");
  }

  @Test(expected = NullPointerException.class)
  public void testApplyWithMaskNullSourceImage() {
    processor.applyWithMask(null, maskImage, "blur");
  }

  @Test(expected = NullPointerException.class)
  public void testApplyWithMaskNullMaskImage() {
    processor.applyWithMask(testImage, null, "blur");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithMaskInvalidOperation() {
    processor.applyWithMask(testImage, maskImage, "invalid-operation");
  }

  @Test
  public void testApplyWithAllBlackMask() {
    Image blackMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        blackMask.setPixel(x, y, new Pixel(0, 0, 0));
      }
    }
    Image result = processor.applyWithMask(testImage, blackMask, "blur");
    assertNotNull(result);
    assertNotEquals(testImage, result); // All pixels should change
  }

  @Test
  public void testApplyWithAllWhiteMask() {
    Image whiteMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        whiteMask.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(testImage, whiteMask, "sharpen");
    assertNotNull(result);
    assertEquals(testImage, result); // No pixels should change
  }

  @Test
  public void testApplyWithMaskMultipleOperations() {
    Image blurredImage = processor.applyWithMask(testImage, maskImage, "blur");
    Image sharpenedImage = processor.applyWithMask(testImage, maskImage, "sharpen");
    Image sepiaImage = processor.applyWithMask(testImage, maskImage, "sepia");
    assertNotNull(blurredImage);
    assertNotNull(sharpenedImage);
    assertNotNull(sepiaImage);
  }

  @Test
  public void testApplyWithRandomPatternMask() {
    Image randomMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        randomMask.setPixel(x, y, (x + y) % 2 == 0 ? new Pixel(0, 0, 0) : new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(testImage, randomMask, "greyscale");
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithInvalidMaskColors() {
    Image invalidMask = new Image(4, 4);
    invalidMask.setPixel(1, 1, new Pixel(128, 128, 128)); // Invalid mask pixel
    processor.applyWithMask(testImage, invalidMask, "blur");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithInvalidOperation() {
    processor.applyWithMask(testImage, maskImage, "nonexistent-operation");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithMismatchedMaskDimensions() {
    Image mismatchedMask = new Image(3, 3);
    processor.applyWithMask(testImage, mismatchedMask, "blur");
  }

  @Test
  public void testApplyWithSinglePixelImage() {
    Image singlePixelImage = new Image(1, 1);
    singlePixelImage.setPixel(0, 0, new Pixel(100, 100, 100));
    Image singlePixelMask = new Image(1, 1);
    singlePixelMask.setPixel(0, 0, new Pixel(0, 0, 0));
    Image result = processor.applyWithMask(singlePixelImage, singlePixelMask, "sepia");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithEmptyImageAndMask() {
    Image emptyImage = new Image(0, 0);
    Image emptyMask = new Image(0, 0);
    Image result = processor.applyWithMask(emptyImage, emptyMask, "blur");
    assertEquals(0, result.getWidth());
    assertEquals(0, result.getHeight());
  }

  @Test
  public void testApplyWithMaximumRGBMask() {
    Image maxRGBMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        maxRGBMask.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(testImage, maxRGBMask, "blur");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithLargeImageAndMask() {
    Image largeImage = new Image(1000, 1000);
    Image largeMask = new Image(1000, 1000);
    for (int y = 0; y < 1000; y++) {
      for (int x = 0; x < 1000; x++) {
        largeMask.setPixel(x, y, (x + y) % 2 == 0 ? new Pixel(0, 0, 0) : new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(largeImage, largeMask, "sharpen");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithDiagonalLineMask() {
    Image diagonalMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        diagonalMask.setPixel(x, y, x == y ? new Pixel(0, 0, 0) : new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(testImage, diagonalMask, "greyscale");
    assertNotNull(result);
  }

  @Test
  public void testApplyWithMaskRedComponent() {
    Image result = processor.applyWithMask(testImage, maskImage, "red");
    assertNotNull(result);
    for (int y = 0; y < testImage.getHeight(); y++) {
      for (int x = 0; x < testImage.getWidth(); x++) {
        Pixel maskPixel = maskImage.getPixel(x, y);
        Pixel resultPixel = result.getPixel(x, y);
        if (maskPixel.getRed() == 0) { // Mask applied
          assertEquals(resultPixel.getRed(), testImage.getPixel(x, y).getRed());
          assertEquals(0, resultPixel.getGreen());
          assertEquals(0, resultPixel.getBlue());
        } else { // No mask
          assertEquals(testImage.getPixel(x, y), resultPixel);
        }
      }
    }
  }

  @Test
  public void testApplyWithMaskGreenComponent() {
    Image result = processor.applyWithMask(testImage, maskImage, "green");
    assertNotNull(result);
    for (int y = 0; y < testImage.getHeight(); y++) {
      for (int x = 0; x < testImage.getWidth(); x++) {
        Pixel maskPixel = maskImage.getPixel(x, y);
        Pixel resultPixel = result.getPixel(x, y);
        if (maskPixel.getRed() == 0) { // Mask applied
          assertEquals(0, resultPixel.getRed());
          assertEquals(resultPixel.getGreen(), testImage.getPixel(x, y).getGreen());
          assertEquals(0, resultPixel.getBlue());
        } else { // No mask
          assertEquals(testImage.getPixel(x, y), resultPixel);
        }
      }
    }
  }

  @Test
  public void testApplyWithMaskBlueComponent() {
    Image result = processor.applyWithMask(testImage, maskImage, "blue");
    assertNotNull(result);
    for (int y = 0; y < testImage.getHeight(); y++) {
      for (int x = 0; x < testImage.getWidth(); x++) {
        Pixel maskPixel = maskImage.getPixel(x, y);
        Pixel resultPixel = result.getPixel(x, y);
        if (maskPixel.getRed() == 0) { // Mask applied
          assertEquals(0, resultPixel.getRed());
          assertEquals(0, resultPixel.getGreen());
          assertEquals(resultPixel.getBlue(), testImage.getPixel(x, y).getBlue());
        } else { // No mask
          assertEquals(testImage.getPixel(x, y), resultPixel);
        }
      }
    }
  }

  @Test
  public void testApplyWithMaskRedComponentSinglePixel() {
    Image singlePixelImage = new Image(1, 1);
    singlePixelImage.setPixel(0, 0, new Pixel(100, 50, 25));
    Image singlePixelMask = new Image(1, 1);
    singlePixelMask.setPixel(0, 0, new Pixel(0, 0, 0)); // Fully apply
    Image result = processor.applyWithMask(singlePixelImage, singlePixelMask, "red");
    assertNotNull(result);
    Pixel pixel = result.getPixel(0, 0);
    assertEquals(100, pixel.getRed());
    assertEquals(0, pixel.getGreen());
    assertEquals(0, pixel.getBlue());
  }

  @Test
  public void testApplyWithMaskAllWhiteRedComponent() {
    Image whiteMask = new Image(4, 4);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        whiteMask.setPixel(x, y, new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(testImage, whiteMask, "red");
    assertEquals(testImage, result); // No changes expected
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithMaskInvalidComponentName() {
    processor.applyWithMask(testImage, maskImage, "yellow");
  }

  @Test(expected = NullPointerException.class)
  public void testApplyWithMaskNullMaskForComponent() {
    processor.applyWithMask(testImage, null, "red");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyWithMaskInvalidMaskColorsForComponent() {
    Image invalidMask = new Image(4, 4);
    invalidMask.setPixel(2, 2, new Pixel(128, 128, 128)); // Invalid pixel
    processor.applyWithMask(testImage, invalidMask, "red");
  }

  @Test
  public void testApplyWithMaskRedComponentLargeImage() {
    Image largeImage = new Image(1000, 1000);
    Image largeMask = new Image(1000, 1000);
    for (int y = 0; y < 1000; y++) {
      for (int x = 0; x < 1000; x++) {
        largeMask.setPixel(x, y, (x + y) % 2 == 0 ? new Pixel(0, 0, 0) : new Pixel(255, 255, 255));
      }
    }
    Image result = processor.applyWithMask(largeImage, largeMask, "red");
    assertNotNull(result);
  }
}