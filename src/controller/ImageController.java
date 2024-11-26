package controller;

import javax.swing.JOptionPane;
import model.Image;
import model.ImageProcessor;
import view.ImageView;
import utility.ImageUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import java.io.File;
import view.SwingImageView;


/**
 * This class implements the Controller interface and handles the execution of commands for an image
 * processing system. It manages images, processes them using various operations, and interacts with
 * the user through a view.
 */
public class ImageController implements Controller {

  private Map<String, Image> imageMap;
  private ImageView view;
  private ImageProcessor processor;

  /**
   * Constructs an ImageController with the specified view and processor.
   *
   * @param view      the view to interact with the user
   * @param processor the image processor to handle image operations
   */
  public ImageController(ImageView view, ImageProcessor processor) {
    this.imageMap = new HashMap<>();
    this.view = view;
    this.processor = processor;
  }

  /**
   * Retrieves the current image being worked on.
   *
   * @return the current image, or null if no image is loaded
   */
  @Override
  public Image getCurrentImage() {
    if (imageMap.containsKey("processedImage")) {
      return imageMap.get("processedImage");
    } else if (imageMap.containsKey("loadedImage")) {
      return imageMap.get("loadedImage");
    }
    return null;
  }

  /**
   * Executes a command specified by the command line input. The command may include operations like
   * loading, saving, or processing images.
   *
   * @param commandLine the command input as a string
   * @throws IOException if an I/O error occurs while executing the command
   */
  @Override
  public void execute(String commandLine) throws IOException {
    boolean isGuiCommand = commandLine.contains("-gui"); // Check if the command comes from the GUI
    String[] tokens = commandLine.replace("-gui", "").trim().split(" ");
    try {
      switch (tokens[0]) {
        case "load":
          if (isGuiCommand) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              String filePath = fileChooser.getSelectedFile().getAbsolutePath();
              loadImage(filePath, tokens.length > 2 ? tokens[2] : "loadedImage");
            }
          } else {
            loadImage(tokens[1], tokens[2]);
          }
          break;

        case "save":
          if (isGuiCommand) {
            JFileChooser saveChooser = new JFileChooser();
            if (saveChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
              String savePath = saveChooser.getSelectedFile().getAbsolutePath();
              saveImage(savePath, tokens.length > 2 ? tokens[2] : "processedImage");
            }
          } else {
            saveImage(tokens[1], tokens[2]);
          }
          break;

        case "brighten":
          int increment = Integer.parseInt(tokens[1]);
          processImage(tokens[2], tokens[3], "brighten", increment);
          break;

        case "darken":
          int decrement = Integer.parseInt(tokens[1]);
          processImage(tokens[2], tokens[3], "darken", decrement);
          break;

        case "horizontal-flip":
          processImage(tokens[1], tokens[2], "horizontal-flip", -1);
          break;

        case "vertical-flip":
          processImage(tokens[1], tokens[2], "vertical-flip", -1);
          break;

        case "red-component":
          processImage(tokens[1], tokens[2], "red-component", -1);
          break;

        case "green-component":
          processImage(tokens[1], tokens[2], "green-component", -1);
          break;

        case "blue-component":
          processImage(tokens[1], tokens[2], "blue-component", -1);
          break;

        case "value-component":
          processImage(tokens[1], tokens[2], "value-component", -1);
          break;

        case "intensity-component":
          processImage(tokens[1], tokens[2], "intensity-component", -1);
          break;

        case "luma-component":
          processImage(tokens[1], tokens[2], "luma-component", -1);
          break;

        case "rgb-split":
          rgbSplit(tokens[1], tokens[2], tokens[3], tokens[4]);
          break;

        case "rgb-combine":
          rgbCombine(tokens[1], tokens[2], tokens[3], tokens[4]);
          break;

        case "compress":
          if (tokens.length != 4) {
            view.displayError("Invalid number of arguments for compress command.");
            return;
          }
          try {
            int percentage = Integer.parseInt(tokens[1]);
            if (percentage < 0 || percentage > 100) {
              view.displayError("Compression percentage must be between 0 and 100.");
              return;
            }
            processImage(tokens[2], tokens[3], "compress", percentage);
          } catch (NumberFormatException e) {
            view.displayError("Invalid compression percentage: " + tokens[1]);
          }
          break;

        case "histogram":
          if (tokens.length != 3) {
            view.displayError("Invalid number of arguments for histogram command.");
            return;
          }
          processImage(tokens[1], tokens[2], "histogram");
          break;

        case "blur":
        case "sharpen":
        case "sepia":
        case "greyscale":
        case "color-correct":
          if (tokens.length == 3) {
            processImage(tokens[1], tokens[2], tokens[0]);
          } else if (tokens.length == 5 && tokens[3].equals("split")) {
            int splitPercentage = Integer.parseInt(tokens[4]);
            processImage(tokens[1], tokens[2], tokens[0], splitPercentage);
          } else {
            view.displayError("Invalid number of arguments for " + tokens[0] + " command.");
          }
          break;

        case "levels-adjust":
          if (tokens.length == 6) {
            int b = Integer.parseInt(tokens[1]);
            int m = Integer.parseInt(tokens[2]);
            int w = Integer.parseInt(tokens[3]);
            if (b < 0 || b > 255 || m < 0 || m > 255 || w < 0 || w > 255 || b >= m || m >= w) {
              view.displayError(
                  "Invalid levels values. "
                      + "They should be in ascending order and between 0 and 255.");
              return;
            }
            processImage(tokens[4], tokens[5], "levels-adjust", new int[]{b, m, w});
          } else if (tokens.length == 8 && tokens[6].equals("split")) {
            int b = Integer.parseInt(tokens[1]);
            int m = Integer.parseInt(tokens[2]);
            int w = Integer.parseInt(tokens[3]);
            int splitPercentage = Integer.parseInt(tokens[7]);
            if (b < 0 || b > 255 || m < 0 || m > 255 || w < 0 || w > 255 || b >= m || m >= w) {
              view.displayError(
                  "Invalid levels values. "
                      + "They should be in ascending order and between 0 and 255.");
              return;
            }
            processImage(tokens[4], tokens[5], "levels-adjust", b, m, w, splitPercentage);
          } else {
            view.displayError("Invalid number of arguments for levels-adjust command.");
          }
          break;

        case "downscale":
          if (tokens.length != 5) {
            view.displayError(
                "Usage: downscale <target-width> <target-height> <source-image> <dest-image>");
            return;
          }

          try {
            int targetWidth = Integer.parseInt(tokens[1]);
            int targetHeight = Integer.parseInt(tokens[2]);

            if (targetWidth <= 0 || targetHeight <= 0) {
              view.displayError("Width and height must be positive integers.");
              return;
            }

            processImage(tokens[3], tokens[4], "downscale", targetWidth, targetHeight);
          } catch (NumberFormatException e) {
            view.displayError("Invalid width or height: " + e.getMessage());
          }
          break;

        case "partial":
          if (tokens.length != 5) {
            view.displayError(
                "Usage: partial <operation> <source-image> <mask-image> <dest-image>");
            return;
          }

          String operation = tokens[1];
          String sourceName = tokens[2];
          String maskName = tokens[3];
          String destName = tokens[4];

          Image maskImage = imageMap.get(maskName);
          if (maskImage == null) {
            view.displayError("Mask image '" + maskName + "' not found.");
            return;
          }

          processImageWithMask(sourceName, destName, operation, maskImage);
          break;

        default:
          view.displayError("Unknown command: " + tokens[0]);
          System.out.println("Please follow the following syntax for operations. \n"
              + "load <image-path> <image-name>\n"
              + "red-component <image-name> <dest-image-name>\n"
              + "green-component <image-name> <dest-image-name>\n"
              + "blue-component <image-name> <dest-image-name>\n"
              + "value-component <image-name> <dest-image-name>\n"
              + "intensity-component <image-name> <dest-image-name> \n"
              + "luma-component <image-name> <dest-image-name> \n"
              + "horizontal-flip <image-name> <dest-image-name>\n"
              + "vertical-flip <image-name> <dest-image-name>\n"
              + "brighten <increment> <image-name> <dest-image-name>\n"
              + "blur <image-name> <dest-image-name> \n"
              + "sharpen <image-name> <dest-image-name>\n"
              + "sepia <image-name> <dest-image-name>\n"
              + "greyscale <image-name> <dest-image-name> \n"
              + "rgb-split <image-name> <red-image-name> <green-image-name> <blue-image-name> \n"
              + "rgb-combine <dest-image-name> <red-image-name> <green-image-name> "
              + "<blue-image-name> \n"
              + "compress <compression-ratio> <image-name> <dest-image-name> \n"
              + "histogram <image-name> <dest-image-name> \n"
              + "color-correct <image-name> <dest-image-name> \n"
              + "levels-adjust <black> <mid> <white> <image-name> <dest-image-name> \n"
              + "save <image-path> <image-name>");
          break;
      }
    } catch (NumberFormatException e) {
      view.displayError("Invalid numeric input: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      view.displayError("Invalid number of arguments. Error: " + e.getMessage());
    } catch (Exception e) {
      view.displayError("An error occurred: " + e.getMessage());
    }
  }


  /**
   * Loads an image from the specified file path and stores it with the given name.
   *
   * @param filePath the path of the image file to load
   * @param name     the name to assign to the loaded image
   * @throws IOException if an error occurs during the loading process
   */
  @Override
  public void loadImage(String filePath, String name) throws IOException {
    if (filePath == null || filePath.trim().isEmpty()) {
      view.displayError("File path cannot be empty.");
      return;
    }

    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
      view.displayError("File not found: " + filePath);
      return;
    }

    Image image = ImageUtil.readImage(filePath);
    if (image != null) {
      // Use a consistent key for the current image
      imageMap.put(name, image);
      view.displayMessage("Image loaded from " + filePath);

      // Display image in the GUI
      if (view instanceof SwingImageView) {
        SwingImageView guiView = (SwingImageView) view;
        guiView.setImage(image);
      }

      // Update histogram
      if (processor != null) {
        int[][] histogram = processor.calculateHistogram(image);
        view.updateHistogram(histogram[0], histogram[1], histogram[2]);
      }
    } else {
      view.displayError("Unsupported file format or corrupted image: " + filePath);
    }
  }


  /**
   * Saves the image with the given name to the specified file path.
   *
   * @param filePath the path where the image should be saved
   * @param name     the name of the image to save
   * @throws IOException if an error occurs during the saving process
   */
  @Override
  public void saveImage(String filePath, String name) throws IOException {
    if (filePath == null || filePath.trim().isEmpty()) {
      view.displayError("File path cannot be empty.");
      return;
    }

    // Retrieve the image using the consistent key
    Image image = imageMap.get(name);
    if (image == null) {
      view.displayError("No image loaded to save.");
      return;
    }

    boolean success = ImageUtil.writeImage(image, filePath);
    if (success) {
      view.displayMessage("Image saved to " + filePath);
    } else {
      view.displayError("Failed to save image: " + filePath);
    }
  }


  /**
   * Processes an image using the specified operation and value, creating a new image with the given
   * output name.
   *
   * @param inputName  the name of the input image
   * @param outputName the name of the output image
   * @param operation  the operation to perform (e.g., brighten, darken)
   * @param values     the value used in the operation (e.g., brightness increment)
   * @throws IOException if an error occurs during processing
   */
  @Override
  public void processImage(String inputName, String outputName, String operation, int... values)
      throws IOException {
    // Fetch the input image from the map
    Image inputImage = imageMap.get(inputName);

    if (inputImage == null) {
      view.displayError("Image '" + inputName + "' not found.");
      return;
    }

    // Variables for processing
    Image outputImage;
    int splitPercentage = -1;

    // Handle operations with additional values
    if (operation.equals("levels-adjust")) {
      if (values.length < 3) {
        view.displayError(
            "Levels adjustment requires at least 3 values: black point, mid-point, "
                + "and white point.");
        return;
      }
      if (values.length == 4) {
        splitPercentage = values[3];
      }
    } else if (values.length > 0 && (operation.equals("blur") || operation.equals("sharpen")
        || operation.equals("sepia") || operation.equals("greyscale")
        || operation.equals("color-correct"))) {
      splitPercentage = values[values.length - 1];
      values = Arrays.copyOf(values, values.length - 1);
    }

    // Perform the specified operation
    switch (operation) {
      case "horizontal-flip":
        outputImage = processor.flipHorizontal(inputImage);
        break;
      case "vertical-flip":
        outputImage = processor.flipVertical(inputImage);
        break;
      case "brighten":
        outputImage = processor.brightness(inputImage, values[0]);
        break;
      case "darken":
        outputImage = processor.darkness(inputImage, values[0]);
        break;
      case "red-component":
        outputImage = processor.visualizeRedComponent(inputImage);
        break;
      case "green-component":
        outputImage = processor.visualizeGreenComponent(inputImage);
        break;
      case "blue-component":
        outputImage = processor.visualizeBlueComponent(inputImage);
        break;
      case "value-component":
        outputImage = processor.visualizeValue(inputImage);
        break;
      case "intensity-component":
        outputImage = processor.visualizeIntensity(inputImage);
        break;
      case "luma-component":
        outputImage = processor.visualizeLuma(inputImage);
        break;
      case "compress":
        outputImage = processor.compress(inputImage, values[0]);
        break;
      case "histogram":
        outputImage = processor.generateHistogram(inputImage);
        break;
      case "blur":
        outputImage = processor.blur(inputImage);
        break;
      case "sharpen":
        outputImage = processor.sharpen(inputImage);
        break;
      case "sepia":
        outputImage = processor.toSepia(inputImage);
        break;
      case "greyscale":
        outputImage = processor.toGreyscale(inputImage);
        break;
      case "color-correct":
        outputImage = processor.colorCorrect(inputImage);
        break;
      case "levels-adjust":
        outputImage = processor.levelsAdjust(inputImage, values[0], values[1], values[2]);
        break;
      case "downscale":
        if (values.length == 2) {
          outputImage = processor.downscaleImage(inputImage, values[0], values[1]);
        } else {
          view.displayError("Invalid arguments for downscale operation.");
          return;
        }
        break;
      default:
        view.displayError("Invalid operation: " + operation);
        return;
    }

    // Apply split view if applicable
    if (splitPercentage >= 0 && splitPercentage <= 100) {
      outputImage = processor.applySplitView(inputImage, outputImage, splitPercentage);
    }

    // Update imageMap for CLI
    imageMap.put(outputName, outputImage);

    // Update GUI-specific data (if applicable)
    if (view instanceof SwingImageView) {
      imageMap.put("currentImage", outputImage); // Update for GUI operations
      ((SwingImageView) view).setImage(outputImage);
      int[][] histogram = processor.calculateHistogram(outputImage);
      ((SwingImageView) view).updateHistogram(histogram[0], histogram[1], histogram[2]);
    }

    // Display success message
    view.displayMessage("Operation '" + operation + "' completed. New image: " + outputName);
  }


  /**
   * Splits the RGB channels of the specified input image into separate images for red, green, and
   * blue.
   *
   * @param inputName the name of the input image to split
   * @param redName   the name for the output red channel image
   * @param greenName the name for the output green channel image
   * @param blueName  the name for the output blue channel image
   * @throws IOException if an error occurs during the process
   */
  @Override
  public void rgbSplit(String inputName, String redName, String greenName, String blueName)
      throws IOException {
    Image inputImage = imageMap.get(inputName);
    if (inputImage == null) {
      view.displayError("Image '" + inputName + "' not found.");
      return;
    }

    Image[] rgbImages = processor.splitChannels(inputImage);
    imageMap.put(redName, rgbImages[0]);
    imageMap.put(greenName, rgbImages[1]);
    imageMap.put(blueName, rgbImages[2]);

    view.displayMessage(
        "RGB split completed. New images: " + redName + " (red), " + greenName + " (green), "
            + blueName + " (blue)");
  }

  /**
   * Combines the RGB channels from three separate images into a single image.
   *
   * @param destName  the name for the combined output image
   * @param redName   the name of the red channel image
   * @param greenName the name of the green channel image
   * @param blueName  the name of the blue channel image
   * @throws IOException if an error occurs during the process
   */
  @Override
  public void rgbCombine(String destName, String redName, String greenName, String blueName)
      throws IOException {
    Image redImage = imageMap.get(redName);
    Image greenImage = imageMap.get(greenName);
    Image blueImage = imageMap.get(blueName);

    if (redImage == null || greenImage == null || blueImage == null) {
      view.displayError("One or more source images not found.");
      return;
    }

    Image combinedImage = processor.combineChannels(redImage, greenImage, blueImage);
    imageMap.put(destName, combinedImage);

    view.displayMessage("RGB combine completed. New image: " + destName);

    // Update histogram
    int[][] histogram = processor.calculateHistogram(combinedImage);
    view.updateHistogram(histogram[0], histogram[1], histogram[2]);
  }

  /**
   * Handles GUI commands by parsing the input and executing the appropriate image processing
   * operations. The method ensures user-friendly interaction with features such as loading, saving,
   * flipping, compressing, and adjusting images, along with error handling.
   *
   * @param command the GUI command string, which specifies the operation and parameters
   * @throws IOException if an error occurs during the execution of a command
   */
  @Override
  public void handleGuiCommand(String command) throws IOException {
    String[] tokens = command.split("-");
    switch (tokens[0]) {
      case "load":
        if (!command.startsWith("gui-")) {
          loadImage(tokens[1], "currentImage");
        }
        break;

      case "save":
        if (!command.startsWith("gui-")) {
          saveImage(tokens[1], "currentImage");
        }
        break;

      case "flip":
        if (tokens.length > 1) {
          processImage("currentImage", "currentImage", tokens[1] + "-flip");
        } else {
          view.displayError("Missing direction for flip operation (horizontal/vertical).");
        }
        break;

      case "red":
      case "green":
      case "blue":
        processImage("currentImage", "currentImage", tokens[0] + "-component");
        break;

      case "color":
        processImage("currentImage", "currentImage", "color-correct");
        break;

      case "greyscale":
      case "sepia":
      case "blur":
      case "sharpen":
        processImage("currentImage", "currentImage", tokens[0]);
        break;

      case "compression":  // Changed from compress to compression
        try {
          String input = JOptionPane.showInputDialog("Enter compression percentage (0-100):");
          if (input != null) {
            if (input.contains(".") || !input.trim().matches("\\d+")) {
              view.displayError("Compression percentage must be a whole number");
              return;
            }
            int percentage = Integer.parseInt(input.trim());
            if (percentage < 0 || percentage > 100) {
              view.displayError("Compression percentage must be between 0 and 100");
              return;
            }
            processImage("currentImage", "currentImage", "compress", percentage);
          }
        } catch (NumberFormatException e) {
          view.displayError(
              "Invalid compression percentage. Please enter a whole number between 0 and 100");
        }
        break;

      case "levels":
        try {
          String blackInput = JOptionPane.showInputDialog("Enter black point (0-255):");
          if (blackInput == null) {
            return;
          }

          String midInput = JOptionPane.showInputDialog("Enter mid point (0-255):");
          if (midInput == null) {
            return;
          }

          String whiteInput = JOptionPane.showInputDialog("Enter white point (0-255):");
          if (whiteInput == null) {
            return;
          }

          // Check for non-integer inputs
          if (!blackInput.trim().matches("\\d+") ||
              !midInput.trim().matches("\\d+") ||
              !whiteInput.trim().matches("\\d+")) {
            view.displayError("All levels values must be whole numbers");
            return;
          }

          int black = Integer.parseInt(blackInput.trim());
          int mid = Integer.parseInt(midInput.trim());
          int white = Integer.parseInt(whiteInput.trim());

          // Check range
          if (black < 0 || black > 255 || mid < 0 || mid > 255 || white < 0 || white > 255) {
            view.displayError("All levels values must be between 0 and 255");
            return;
          }

          // Check ascending order
          if (!(black < mid && mid < white)) {
            view.displayError("Levels must be in strictly increasing order: black < mid < white");
            return;
          }

          processImage("currentImage", "currentImage", "levels-adjust", black, mid, white);

        } catch (NumberFormatException e) {
          view.displayError("Invalid levels input. Please enter valid integers between 0 and 255");
        } catch (IOException ex) {
          view.displayError("Error during levels adjustment: " + ex.getMessage());
        }
        break;

      case "split":
        try {
          String percentageInput = JOptionPane.showInputDialog(null,
              "Enter split percentage (0-100):");
          if (percentageInput != null) {
            // Validate split percentage is an integer
            if (!percentageInput.trim().matches("\\d+")) {
              view.displayError("Split percentage must be a whole number");
              return;
            }

            int splitPercentage = Integer.parseInt(percentageInput.trim());
            if (splitPercentage < 0 || splitPercentage > 100) {
              view.displayError("Split percentage must be between 0 and 100");
              return;
            }

            Image originalImage = imageMap.get("currentImage");
            if (originalImage == null) {
              view.displayError("No image loaded to apply split view");
              return;
            }

            String[] operations = {
                "blur", "sharpen", "sepia", "greyscale",
                "color-correct", "levels-adjust"
            };

            String operation = (String) JOptionPane.showInputDialog(null,
                "Select operation for split view:",
                "Operation Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                operations,
                operations[0]);

            if (operation == null || operation.trim().isEmpty()) {
              view.displayError("No operation specified for split view");
              return;
            }

            // Special handling for levels-adjust
            if (operation.equals("levels-adjust")) {
              String blackInput = JOptionPane.showInputDialog(null,
                  "Enter black point (0-255):");
              String midInput = JOptionPane.showInputDialog(null,
                  "Enter mid point (0-255):");
              String whiteInput = JOptionPane.showInputDialog(null,
                  "Enter white point (0-255):");

              if (blackInput == null || midInput == null || whiteInput == null) {
                return;
              }

              // Validate integer input
              if (!blackInput.trim().matches("\\d+") ||
                  !midInput.trim().matches("\\d+") ||
                  !whiteInput.trim().matches("\\d+")) {
                view.displayError("All levels values must be whole numbers");
                return;
              }

              int black = Integer.parseInt(blackInput.trim());
              int mid = Integer.parseInt(midInput.trim());
              int white = Integer.parseInt(whiteInput.trim());

              // Validate ranges
              if (black < 0 || black > 255 ||
                  mid < 0 || mid > 255 ||
                  white < 0 || white > 255) {
                view.displayError("All levels values must be between 0 and 255");
                return;
              }

              // Validate ascending order
              if (!(black < mid && mid < white)) {
                view.displayError(
                    "Levels must be in strictly increasing order: black < mid < white");
                return;
              }

              processImage("currentImage", "currentImage", "levels-adjust",
                  black, mid, white, splitPercentage);
            } else {
              processImage("currentImage", "currentImage", operation, splitPercentage);
            }
          }
        } catch (NumberFormatException e) {
          view.displayError("Invalid input. Please enter valid numbers");
        } catch (IOException ex) {
          view.displayError("Error during split view operation: " + ex.getMessage());
        }
        break;

      case "downscale":
        try {
          // Get current image dimensions
          Image currentImage = imageMap.get("currentImage");
          if (currentImage == null) {
            view.displayError("No image loaded to downscale");
            return;
          }

          int originalWidth = currentImage.getWidth();
          int originalHeight = currentImage.getHeight();

          // Prompt user for new dimensions
          String widthInput = JOptionPane.showInputDialog(
              null,
              String.format("Enter target width (max %d):", originalWidth)
          );
          if (widthInput == null) {
            return;  // User cancelled
          }

          String heightInput = JOptionPane.showInputDialog(
              null,
              String.format("Enter target height (max %d):", originalHeight)
          );
          if (heightInput == null) {
            return;  // User cancelled
          }

          // Validate integer input
          if (!widthInput.trim().matches("\\d+") || !heightInput.trim().matches("\\d+")) {
            view.displayError("Width and height must be whole numbers");
            return;
          }

          int targetWidth = Integer.parseInt(widthInput.trim());
          int targetHeight = Integer.parseInt(heightInput.trim());

          // Validate dimensions
          if (targetWidth <= 0 || targetHeight <= 0) {
            view.displayError("Width and height must be positive numbers");
            return;
          }

          if (targetWidth > originalWidth || targetHeight > originalHeight) {
            view.displayError(String.format(
                "New dimensions cannot exceed original size (%dx%d)",
                originalWidth,
                originalHeight
            ));
            return;
          }

          processImage("currentImage", "currentImage", "downscale", targetWidth, targetHeight);

        } catch (NumberFormatException ex) {
          view.displayError("Invalid input. Please enter valid whole numbers for width and height");
        } catch (IOException ex) {
          view.displayError("Error during downscaling: " + ex.getMessage());
        }
        break;

      default:
        view.displayError("Unsupported operation: " + tokens[0]);
    }
  }

  @Override
  public void processImageWithMask(String inputName, String outputName, String operation,
      Image maskImage)
      throws IOException {
    // Validate the input image
    Image inputImage = imageMap.get(inputName);
    if (inputImage == null) {
      view.displayError("Input image '" + inputName + "' not found.");
      return;
    }

    // Validate the mask image
    if (maskImage == null) {
      view.displayError(
          "Mask image is missing or invalid. Please load the mask image before processing.");
      return;
    }

    // Process the image with the mask
    try {
      Image outputImage = processor.applyWithMask(inputImage, maskImage, operation);
      imageMap.put(outputName, outputImage);
      view.displayMessage(
          "Operation '" + operation + "' with mask applied. New image: " + outputName);
    } catch (UnsupportedOperationException e) {
      view.displayError("Unsupported operation: " + operation);
    } catch (IllegalArgumentException e) {
      view.displayError("Error during mask application: " + e.getMessage());
    } catch (Exception e) {
      view.displayError("An unexpected error occurred: " + e.getMessage());
    }
  }

  private void setCurrentImage(Image image) {
    imageMap.clear(); // Remove old entries
    imageMap.put("currentImage", image); // Add the new image
  }

}