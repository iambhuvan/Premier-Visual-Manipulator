package view;

import controller.ImageController;
import java.io.IOException;
import model.Image;
import model.ImageProcessor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.MenuElement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import model.ImageProcessorImpl;

/**
 * SwingImageView provides a graphical interface for the Image Processing Application.
 */
public class SwingImageView extends JFrame implements ImageView {

  private final JLabel imageLabel; // Label to display images
  private final JTextArea messageArea; // Text area for messages and errors
  private final JPanel histogramPanel; // Panel to display histograms
  private final JMenuBar menuBar; // Menu bar for application options
  // private final JSlider splitViewSlider; // Slider for split view


  /**
   * Constructs the GUI components for the Image Processing Application.
   */
  public SwingImageView() {
    super("Image Processing Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Menu bar
    menuBar = createMenuBar();
    setJMenuBar(menuBar);

    // Image display area
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    add(imageScrollPane, BorderLayout.CENTER);

    // Histogram panel
    histogramPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHistogram((Graphics2D) g);
      }
    };
    histogramPanel.setPreferredSize(new Dimension(256, 256));
    histogramPanel.setBackground(Color.LIGHT_GRAY);
    histogramPanel.setBorder(BorderFactory.createTitledBorder("Histogram"));
    add(histogramPanel, BorderLayout.EAST);

    // Control Panel
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BorderLayout());

    // Buttons Panel
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(4, 4, 5, 5)); // Adjusted for 16 buttons
    buttonsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
    // Define all GUI operations
    String[] operations = {
        "Load", "Save", "Red Component", "Green Component", "Blue Component",
        "Flip Horizontal", "Flip Vertical", "Blur", "Sharpen", "Greyscale",
        "Sepia", "Compression", "Color-Correct", "Levels-Adjust", "Split-Preview", "Downscale"
    };

    for (String operation : operations) {
      JButton button = new JButton(operation);
      if (operation.contains("Component")) {
        button.setActionCommand(operation.toLowerCase().replace(" component", "-component"));
      } else {
        button.setActionCommand(operation.toLowerCase().replace(" ", "-"));
      }

      buttonsPanel.add(button);
    }

    controlPanel.add(buttonsPanel, BorderLayout.CENTER);

    // Message Area
    messageArea = new JTextArea(5, 20);
    messageArea.setEditable(false);
    messageArea.setBorder(BorderFactory.createTitledBorder("Messages"));
    JScrollPane messageScrollPane = new JScrollPane(messageArea);
    controlPanel.add(messageScrollPane, BorderLayout.SOUTH);

    add(controlPanel, BorderLayout.SOUTH);

    // Finalize frame setup
    setPreferredSize(new Dimension(800, 600));
    pack();
    setVisible(true);
  }

  /**
   * Creates the menu bar for the application, including all 16 operations.
   *
   * @return the JMenuBar instance
   */
  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    // File Menu
    JMenu fileMenu = new JMenu("File");
    JMenuItem loadMenuItem = new JMenuItem("Load Image");
    loadMenuItem.setActionCommand("load");
    JMenuItem saveMenuItem = new JMenuItem("Save Image");
    saveMenuItem.setActionCommand("save");
    fileMenu.add(loadMenuItem);
    fileMenu.add(saveMenuItem);

    // Operations Menu
    JMenu operationsMenu = new JMenu("Operations");
    String[] operationItems = {
        "Load", "Save", "Red Component", "Green Component", "Blue Component",
        "Flip Horizontal", "Flip Vertical", "Blur", "Sharpen", "Greyscale",
        "Sepia", "Compression", "Color-Correct", "Levels-Adjust", "Split-Preview", "Downscale"
    };

    for (String item : operationItems) {
      JMenuItem menuItem = new JMenuItem(item);
      if (item.contains("Component")) {
        menuItem.setActionCommand(item.toLowerCase().replace(" component", "-component"));
      } else {
        menuItem.setActionCommand(item.toLowerCase().replace(" ", "-"));
      }
      operationsMenu.add(menuItem);
    }

    // Help Menu
    JMenu helpMenu = new JMenu("Help");
    JMenuItem aboutMenuItem = new JMenuItem("About");
    aboutMenuItem.setActionCommand("about");
    helpMenu.add(aboutMenuItem);

    menuBar.add(fileMenu);
    menuBar.add(operationsMenu);
    menuBar.add(helpMenu);

    return menuBar;
  }

  /**
   * Displays an image in the GUI.
   *
   * @param image the image to display
   */
  @Override
  public void setImage(model.Image image) {
    if (image == null) {
      imageLabel.setIcon(null);
      displayError("No image to display.");
      return;
    }

    try {
      // Convert model.Image to BufferedImage
      BufferedImage bufferedImage = image.toBufferedImage();

      // Set the BufferedImage as an ImageIcon
      if (bufferedImage != null) {
        imageLabel.setIcon(new ImageIcon(bufferedImage));
      } else {
        imageLabel.setIcon(null);
        displayError("Unable to display the image.");
      }
    } catch (Exception e) {
      imageLabel.setIcon(null);
      displayError("Error displaying the image: " + e.getMessage());
    }
  }


  /**
   * Updates the histogram display with the provided data.
   *
   * @param red   the red channel histogram data
   * @param green the green channel histogram data
   * @param blue  the blue channel histogram data
   */
  @Override
  public void updateHistogram(int[] red, int[] green, int[] blue) {
    histogramPanel.putClientProperty("red", red);
    histogramPanel.putClientProperty("green", green);
    histogramPanel.putClientProperty("blue", blue);
    histogramPanel.repaint();
  }

  /**
   * Draws the histogram on the histogram panel.
   *
   * @param g2 the graphics context
   */
  private void drawHistogram(Graphics2D g2) {
    int[] red = (int[]) histogramPanel.getClientProperty("red");
    int[] green = (int[]) histogramPanel.getClientProperty("green");
    int[] blue = (int[]) histogramPanel.getClientProperty("blue");

    if (red == null || green == null || blue == null) {
      return;
    }

    int width = histogramPanel.getWidth();
    int height = histogramPanel.getHeight();
    int maxCount = Math.max(
        Math.max(Arrays.stream(red).max().orElse(1),
            Arrays.stream(green).max().orElse(1)),
        Arrays.stream(blue).max().orElse(1)
    );

    int barWidth = width / 256;

    for (int i = 0; i < 256; i++) {
      int redHeight = (int) ((double) red[i] / maxCount * height);
      int greenHeight = (int) ((double) green[i] / maxCount * height);
      int blueHeight = (int) ((double) blue[i] / maxCount * height);

      g2.setColor(Color.RED);
      g2.drawLine(i * barWidth, height, i * barWidth, height - redHeight);

      g2.setColor(Color.GREEN);
      g2.drawLine(i * barWidth + 1, height, i * barWidth + 1, height - greenHeight);

      g2.setColor(Color.BLUE);
      g2.drawLine(i * barWidth + 2, height, i * barWidth + 2, height - blueHeight);
    }
  }

  /**
   * Adds action listeners to all buttons and menu items.
   *
   * @param buttonListener the ActionListener for buttons and menu items
   */
  public void setButtonListener(ActionListener buttonListener) {
    // Attach listeners to buttons in the control panel
    for (Component component : ((JPanel) ((BorderLayout) getContentPane().getLayout())
        .getLayoutComponent(BorderLayout.SOUTH)).getComponents()) {
      if (component instanceof JPanel) {
        for (Component buttonComponent : ((JPanel) component).getComponents()) {
          if (buttonComponent instanceof JButton) {
            JButton button = (JButton) buttonComponent;

            // Remove all existing action listeners first
            for (ActionListener al : button.getActionListeners()) {
              button.removeActionListener(al);
            }

            // Add the new action listener
            button.addActionListener(buttonListener);
          }
        }
      }
    }

    // Attach listeners to menu items in the menu bar
    for (MenuElement menuElement : menuBar.getSubElements()) {
      if (menuElement.getComponent() instanceof JMenu) {
        for (MenuElement item : ((JMenu) menuElement.getComponent()).getSubElements()) {
          if (item.getComponent() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) item.getComponent();

            // Remove all existing action listeners first
            for (ActionListener al : menuItem.getActionListeners()) {
              menuItem.removeActionListener(al);
            }

            // Add the new action listener
            menuItem.addActionListener(buttonListener);
          }
        }
      }
    }
  }

  /**
   * Displays a message in the message area.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    messageArea.append(message + "\n");
  }

  /**
   * Displays an error message in the message area.
   *
   * @param error the error message to display
   */
  @Override
  public void displayError(String error) {
    messageArea.append("ERROR: " + error + "\n");
  }

  /**
   * Main method to test the GUI interface independently.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    ImageProcessor processor = new ImageProcessorImpl();
    SwingImageView guiView = new SwingImageView();
    ImageController guiController = new ImageController(guiView, processor);

    // ActionListener for buttons and menu items
    ActionListener buttonListener = e -> {
      try {
        String command = e.getActionCommand();
        if (command.equals("load")) {
          JFileChooser fileChooser = new JFileChooser();
          if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String loadPath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
              guiController.loadImage(loadPath, "currentImage"); // Directly call loadImage
              Image currentImage = guiController.getCurrentImage(); // Refresh GUI
              if (currentImage != null) {
                guiView.setImage(currentImage);
                int[][] histogram = processor.calculateHistogram(currentImage);
                guiView.updateHistogram(histogram[0], histogram[1], histogram[2]);
              }
            } catch (IOException ex) {
              guiView.displayError("Error loading image: " + ex.getMessage());
            }
          }
        } else if (command.equals("save")) {
          JFileChooser saveChooser = new JFileChooser();
          if (saveChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String savePath = saveChooser.getSelectedFile().getAbsolutePath();
            try {
              guiController.saveImage(savePath, "currentImage"); // Directly call saveImage
              guiView.displayMessage("Image saved successfully to: " + savePath);
            } catch (IOException ex) {
              guiView.displayError("Error saving image: " + ex.getMessage());
            }
          }
        } else {
          // Handle operations like Sepia, Flip, etc.
          guiController.handleGuiCommand(command);

          // Update the GUI after operation
          Image currentImage = guiController.getCurrentImage();
          if (currentImage != null) {
            guiView.setImage(currentImage);
            int[][] histogram = processor.calculateHistogram(currentImage);
            guiView.updateHistogram(histogram[0], histogram[1], histogram[2]);
          }
        }
      } catch (IOException ex) {
        guiView.displayError("Command execution failed: " + ex.getMessage());
      }
    };

    // Attach only the button listener
    guiView.setButtonListener(buttonListener);

    System.out.println("GUI launched. Close the GUI window to exit the program.");
  }
}
