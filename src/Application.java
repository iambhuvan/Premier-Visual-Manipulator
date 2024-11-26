import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import controller.ImageController;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Image;
import model.ImageProcessor;
import model.ImageProcessorImpl;
import view.TextView;
import view.SwingImageView;

/**
 * The Application class serves as the entry point for the Image Manipulation and Enhancement (IME)
 * application. It initializes the ImageProcessor implementation and allows users to interact with
 * the application through various modes such as Command-Line Interface (CLI), Batch Scripting, or
 * Graphical User Interface (GUI).
 */
public class Application {

  /**
   * The main method is the entry point of the application. It initializes the ImageProcessorImpl
   * class to handle image processing operations. Users can choose between CLI, Batch Scripting, or
   * GUI mode for interacting with the application. args is the command-line arguments array (not
   * used in this implementation).
   */
  public static void main(String[] args) {
    ImageProcessor processor = new ImageProcessorImpl();

    if (args.length > 0) {
      TextView view = new TextView();
      ImageController controller = new ImageController(view, processor);

      if (args[0].equals("-file") && args.length > 1) {
        runScript(args[1], controller);
        return;
      } else if (args[0].equals("-text")) {
        System.out.println(
            "Enter commands to load, process, and save images. Type 'exit' to quit.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
          System.out.print("> ");
          String command = scanner.nextLine().trim();
          if (command.equalsIgnoreCase("exit")) {
            break;
          }
          try {
            if (command.startsWith("run ")) {
              String scriptFile = command.substring(4).trim();
              runScript(scriptFile, controller);
            } else {
              controller.execute(command);
            }
          } catch (IOException e) {
            System.out.println("Error executing command: " + command);
            System.out.println("Details: " + e.getMessage());
          }
        }
        scanner.close();
        return;
      }
    }

    System.out.println("Welcome to the Image Processing Application");
    System.out.println("Select mode: (1) CLI (2) GUI");
    Scanner scanner = new Scanner(System.in);
    int mode = 0;

    while (mode != 1 && mode != 2) {
      try {
        System.out.print("> ");
        mode = Integer.parseInt(scanner.nextLine().trim());
        if (mode != 1 && mode != 2) {
          System.out.println("Invalid input. Please select (1) CLI or (2) GUI.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please select (1) CLI or (2) GUI.");
      }
    }

    if (mode == 2) {
      System.out.println("Launching GUI mode...");
      SwingImageView guiView = new SwingImageView();
      ImageController guiController = new ImageController(guiView, processor);

      guiView.setButtonListener(e -> {
        try {
          String command = e.getActionCommand();
          if (command.equals("load")) {
            JFileChooser loadChooser = new JFileChooser();
            if (loadChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              String loadPath = loadChooser.getSelectedFile().getAbsolutePath();
              guiController.loadImage(loadPath, "currentImage");
            }
          } else if (command.equals("save")) {
            JFileChooser saveChooser = new JFileChooser();
            if (saveChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
              String savePath = saveChooser.getSelectedFile().getAbsolutePath();
              guiController.saveImage(savePath, "currentImage");
            }
          } else if (command.equals("split-view")) {
            String percentageInput = JOptionPane.showInputDialog(null,
                "Enter split percentage (0-100):");
            if (percentageInput != null) {
              try {
                int splitPercentage = Integer.parseInt(percentageInput.trim());
                if (splitPercentage < 0 || splitPercentage > 100) {
                  throw new NumberFormatException(
                      "Split percentage must be between 0 and 100.");
                }
                guiController.handleGuiCommand("split-view-" + splitPercentage);
              } catch (NumberFormatException ex) {
                guiView.displayError(
                    "Invalid split percentage. Please enter a valid integer between 0 and 100.");
              }
            }
          } else {
            guiController.handleGuiCommand(command);
          }

          Image currentImage = guiController.getCurrentImage();
          if (currentImage != null) {
            guiView.setImage(currentImage);
            int[][] histogram = processor.calculateHistogram(currentImage);
            guiView.updateHistogram(histogram[0], histogram[1], histogram[2]);
          }
        } catch (IOException ex) {
          guiView.displayError("Command execution failed: " + ex.getMessage());
        }
      });

      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("GUI mode exited.");
      }));
    } else {
      System.out.println("CLI mode selected.");
      TextView view = new TextView();
      ImageController controller = new ImageController(view, processor);
      System.out.println("Enter commands to load, process, and save images. Type 'exit' to quit.");

      while (true) {
        System.out.print("> ");
        String command = scanner.nextLine().trim();
        if (command.equalsIgnoreCase("exit")) {
          break;
        }
        try {
          if (command.startsWith("run ")) {
            String scriptFile = command.substring(4).trim();
            runScript(scriptFile, controller);
          } else {
            controller.execute(command);
          }
        } catch (IOException e) {
          System.out.println("Error executing command: " + command);
          System.out.println("Details: " + e.getMessage());
        }
      }

      System.out.println("Thank you for using the Image Processing Application");
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        if (scanner != null) {
          scanner.close();
        }
        System.out.println("Application closed.");
      }));
    }
  }

  private static void runScript(String scriptFile, ImageController controller) {
    try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty() && !line.startsWith("#")) {
          System.out.println("> " + line);
          try {
            controller.execute(line);
          } catch (IOException e) {
            System.out.println("Error executing command from script: " + line);
            System.out.println("Details: " + e.getMessage());
          }
        }
      }
    } catch (IOException e) {
      System.out.println("Error: Unable to read script file: " + scriptFile);
      System.out.println("Details: " + e.getMessage());
    }
  }
}