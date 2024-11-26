# IME: Image Manipulation and Enhancement (Assignment 6)

# Overview

This project is an advanced Image Manipulation and Enhancement (IME) application that supports various image processing operations. The application follows the Model-View-Controller (MVC) architecture and provides three execution modes:

Command-Line Interface (CLI): For interactive command execution.
Batch Script Execution: For automating image operations using scripts.
Graphical User Interface (GUI): For real-time, user-friendly image manipulation.

It supports JPEG, PNG, and PPM file formats and provides functionalities like flipping, brightness adjustments, compression, histogram generation, color correction, and levels adjustment.

# Project Structure

1. Model

Image.java
Pixel.java
ImageProcessor.java (Interface)
ImageProcessorImpl.java (Implementation)

Description:
The Model handles all the core logic for image manipulation.
It provides operations to manipulate individual pixels, perform advanced transformations, and handle compression, histogram, and levels adjustment.

Methods in ImageProcessorImpl

a)Basic Operations:

flipHorizontal(Image image): Flips the image horizontally.
flipVertical(Image image): Flips the image vertically.
brightness(Image image, int adjustment): Adjusts brightness by adding a positive/negative value to all pixel RGB channels.
darkness(Image image, int adjustment): Reduces brightness by subtracting a value from all pixel RGB channels.

b)Color Transformations:

visualizeRedComponent(Image image): Visualizes the red channel by setting R as the RGB value for all pixels.
visualizeGreenComponent(Image image): Visualizes the green channel by setting G as the RGB value for all pixels.
visualizeBlueComponent(Image image): Visualizes the blue channel by setting B as the RGB value for all pixels.
toGreyscale(Image image): Converts the image to greyscale using luma values.
toSepia(Image image): Applies a sepia tone to the image using a color transformation matrix.

c)Advanced Operations:

compress(Image image, int percentage): Compresses the image by removing smaller details based on the given compression percentage.
generateHistogram(Image image): Generates a histogram representing the red, green, and blue distributions.
levelsAdjust(Image image, int shadows, int midtones, int highlights): Adjusts the shadows, midtones, and highlights of the image.
splitViewPreview(Image image, int percentage): Creates a split-view image where part of the image is processed, and part is original.
downscale(Image image, int width, int height): Reduces the resolution of the image to the specified dimensions.

d)Helper Methods:

applyFilter(Image image, double[][] kernel): Applies a given filter kernel (e.g., blur or sharpen).
applyColorTransformation(Image image, double[][] matrix): Applies a color transformation matrix to modify RGB channels.
clamp(int value): Ensures RGB values are within the range of 0–255.

2. Controller

File: ImageController.java

Description:
The Controller is responsible for handling user inputs and coordinating operations between the Model and the View.
It parses commands, executes operations in the ImageProcessorImpl, and updates the View with the results or error messages.
Methods in ImageController
Image I/O:

loadImage(String filePath, String name): Loads an image from the given file path and stores it with the specified name.
saveImage(String filePath, String name): Saves the processed image to the specified file path.
Command Execution:

execute(String commandLine): Parses and executes user commands.
Example commands: flip-horizontal, compress, levels-adjust, brighten.
Image Processing:

processImage(String inputName, String outputName, String operation): Executes basic operations like flip, blur, or sepia.
processImage(String inputName, String outputName, String operation, int value): Executes operations requiring numeric inputs, such as brightness or compression.
processImageWithMask(String inputName, String outputName, String operation, Image maskImage): Applies an operation with a mask image for partial processing.
Utility Methods:

getCurrentImage(): Retrieves the current image being processed.
displayError(String message): Displays an error in the View.

3. View

Files:
TextView.java
SwingImageView.java

Description:
The View provides the user interface for interacting with the application.
It supports two modes:
TextView: Handles command-line interactions.
SwingImageView: Implements a graphical user interface using Java Swing.
Methods in SwingImageView
Image Display:

setImage(Image image): Displays the loaded or processed image in the GUI.
updateHistogram(int[] red, int[] green, int[] blue): Displays the histogram of the current image.
User Interaction:

displayError(String message): Shows error messages in a pop-up dialog.
setButtonListener(ActionListener listener): Attaches event listeners to GUI buttons for operations like load, save, and flip.
Utility Methods:

createMenuBar(): Creates a menu bar with file and operation menus.
drawHistogram(Graphics2D g): Renders the histogram as a line graph.

4. Application

File: Application.java

Description:
The entry point for the application.
Allows users to select between CLI, GUI, or batch script modes.
Initializes the Model, Controller, and View.
Workflow
Prompts the user to choose CLI or GUI mode.
Initializes:
ImageProcessorImpl (Model).
ImageController (Controller).
TextView or SwingImageView (View).
Executes commands interactively or via batch scripts.

# Changes and Justifications

# Transition from Assignment 4 to Assignment 5

"All features specified in the assignment are fully implemented and tested”

New Features:

Compression: Added lossless/lossy compression using the Haar Wavelet Transform.
Histogram Generation: Added visualization of RGB histograms.
Color Correction: Improved color balance for images.
Levels Adjustment: Adjusted shadows, midtones, and highlights.
Split-View Preview: Compared original and processed images side-by-side.

Design Changes:

The main design remained consistent with previous assignments only the existing design were extended to handle new operations as expected in Assignment 5

Model: Extended ImageProcessor with new methods for compression, histogram, levels adjustment, color-correction , split-view preview
Controller: Updated processImage to handle new operations.
View: CLI commands for advanced features.

Justification:

Added advanced image processing capabilities, meeting requirements for image compression and visualization, histogram, levels-adjustment, color-correction, split-view preview as they were the new operations which were expected to be added from Assignment 4 to Assignment 5, so to implement all these new methods changes were done just to add new operations while the design remained consistent according to previous Assignment.

# Transition from Assignment 5 to Assignment 6

"All features specified in the assignment are fully implemented and tested”

New Features:

Graphical User Interface:
Built using Java Swing (SwingImageView).
Added real-time histogram updates and split-view preview.
Downscaling: Reduced image resolution to specified dimensions.
Partial Image Manipulation to mask image based on certain operations as specified.

Design Changes:

The main design remained consistent with previous assignments only the existing design were extended to handle new operations and GUI features as expected in Assignment 6.

Model: Enhanced operations to support GUI interactions such as Downscaling and Partial Image Manipulation methods were added as extra features which were expected in Assignment 6.
Controller: Added GUI-specific commands and event handling.
View: Introduced the Swing-based GUI for user-friendly operations.

Justification:

Improved usability by transitioning from text-based commands to an interactive GUI
and design changes were made just to accommodate new operations and GUI functionality.

Supported Batch Commands (From Assignment 4 to 6)
# Load and Save
load /path/to/image.png loadedImage
save /path/to/output.png processedImage

# Basic Operations
flip-horizontal loadedImage flippedImage
brighten 30 loadedImage brightenedImage

# Advanced Features
compress 50 loadedImage compressedImage
generate-histogram loadedImage histogramOutput
levels-adjust 10 100 200 loadedImage leveledImage
split-view 50 loadedImage splitViewImage
downscale 200 100 loadedImage downscaledImage

# Execution Methods for the JAR File

The JAR file (Assignment6.jar) enables the application to be executed in three different ways, offering flexibility based on the user's requirements:

1. Command-Line Interface (CLI)
The CLI mode allows users to interact with the application by entering commands directly into the terminal. This method is suitable for users who prefer a text-based approach or need to execute specific commands interactively.

How It Works:

1)Run the JAR file:
java -jar Assignment6.jar

2)Select CLI Mode by entering 1 when prompted.

3)Enter commands interactively. Examples:
Load an image:
load /path/to/image.png image1

Flip an image horizontally:
flip-horizontal image1 flippedImage

Save the processed image:
save /path/to/output.png flippedImage

Features Available:
All image operations are accessible through commands, such as flipping, brightening, compressing, and histogram generation.
Users can process images one operation at a time and save results after each modification.

2. Batch Script Execution
The Batch Script mode is designed for users who want to automate multiple image processing operations. This method uses a text file containing a sequence of commands, executed sequentially.

How It Works:

1)Create a text file (e.g., commands.txt) with the following format:

load /path/to/image.png image1
brighten 50 image1 brightenedImage
flip-horizontal brightenedImage flippedImage
save /path/to/output.png flippedImage

2)Run the JAR file and redirect the script as input:

java -jar Assignment6.jar < commands.txt

Features Available:
Automates repetitive tasks, reducing user effort.
Executes all operations in the script in sequence, with results stored as specified.

3. Graphical User Interface (GUI)
The GUI mode provides an interactive graphical interface using Java Swing. It allows users to perform operations visually and see results in real-time, making it ideal for users unfamiliar with command-line operations.

How It Works:
Run the JAR file:
java -jar Assignment6.jar

Select GUI Mode by entering 2 when prompted.
Use the graphical interface to:
Load an image: Click the Load button and select an image file.
Apply operations: Click buttons such as Flip Horizontal, Blur, or Brighten.
Adjust parameters: Use sliders or input dialogs for values (e.g., brightness, split percentage).
Save the processed image: Click the Save button and specify a file location.

Features Available:
Real-time image display and manipulation.
Interactive histogram visualization that updates with image modifications.
User-friendly error handling through pop-ups or on-screen messages.

# Supported Commands

load <image-path> <image-name>: Load an image from the specified path.
save <image-path> <image-name>: Save an image to the specified path.
red-component <image-name> <dest-image-name>: Create an image with the red component.
green-component <image-name> <dest-image-name>: Create an image with the green component.
blue-component <image-name> <dest-image-name>: Create an image with the blue component.
value-component <image-name> <dest-image-name>: Create an image based on the value component (maximum of RGB channels).
intensity-component <image-name> <dest-image-name>: Create an image based on the intensity component (average of RGB channels).
luma-component <image-name> <dest-image-name>: Create an image based on the luma component (weighted sum of RGB channels).
horizontal-flip <image-name> <dest-image-name>: Flip an image horizontally.
vertical-flip <image-name> <dest-image-name>: Flip an image vertically.
brighten <increment> <image-name> <dest-image-name>: Brighten or darken an image by the given increment.
blur <image-name> <dest-image-name>: Apply a blur filter to the image.
sharpen <image-name> <dest-image-name>: Apply a sharpen filter to the image.
sepia <image-name> <dest-image-name>: Convert an image to sepia tones.
greyscale <image-name> <dest-image-name>: Convert an image to greyscale.
rgb-split <image-name> <red-image-name> <green-image-name> <blue-image-name>: Split the RGB channels into separate images.
rgb-combine <dest-image-name> <red-image-name> <green-image-name> <blue-image-name>: Combine red, green, and blue images into one.
compress <compression-ratio> <image-name> <dest-image-name>: Compress an image with the given ratio.
histogram <image-name> <dest-image-name>: Generate a histogram for the image.
color-correct <image-name> <dest-image-name>: Correct the color balance of the image.
levels-adjust <black> <mid> <white> <image-name> <dest-image-name>: Adjust the levels of black, midtones, and white for the image.
split-view <image-name> <dest-image-name> <percentage>: Create a split-view image with the specified percentage.
blur <image-name> <dest-image-name> split <percentage>: Apply a blur filter with split-view.
sharpen <image-name> <dest-image-name> split <percentage>: Apply a sharpen filter with split-view.
sepia <image-name> <dest-image-name> split <percentage>: Convert to sepia tones with split-view.
greyscale <image-name> <dest-image-name> split <percentage>: Convert to greyscale with split-view.
color-correct <image-name> <dest-image-name> split <percentage>: Correct the color balance with split-view.
levels-adjust <black> <mid> <white> <image-name> <dest-image-name> split <percentage>: Adjust levels with split-view.
downscale <width> <height> <image-name> <dest-image-name>: Downscale the image to specified dimensions.
run <script-file>: Execute all commands from a script file.
exit: Save all the images to the ResultImages folder and exit.

# Changes Made to Implement New Features of Downscaling and Partial Image Manipulation

This section explains the modifications made to the program to implement the new features introduced in Assignment 6, including Graphical User Interface (GUI), Downscaling, and Partial Image Manipulation with Masking.

1. Graphical User Interface (SwingImageView.java)
To enable a user-friendly graphical interface, the following updates were made:

Image Display: Added a scrollable pane to handle images larger than the allocated display area.
Histogram Integration: Developed a dynamic histogram panel to show the red, green, and blue channel distributions, automatically updating with every image manipulation.
Interactive Operations: Included buttons, menus, and input dialogs for all image processing commands:
Sliders or input fields for setting parameters like brightness adjustments, compression percentage, and split percentage.
Buttons for basic and advanced operations (e.g., blur, sepia, flip, and downscale).

Key Updates:

setImage(Image image): Displays the current image in the GUI.
updateHistogram(int[] red, int[] green, int[] blue): Dynamically refreshes the histogram panel.
setButtonListener(ActionListener listener): Attaches listeners to handle user interactions.

2. Partial Image Manipulation with Masking (Controller and Model)
Partial image processing was introduced to allow applying transformations to specific regions of an image using a mask:

Mask Support: Users can load a mask image to define regions for partial processing.
Supported Operations: The following operations now work with masks:
Brightening and darkening.
Component visualizations (red, green, blue).
Blurring, sharpening, and sepia transformations.
Command Format: The command partial <operation> <source-image> <mask-image> <dest-image> allows the user to specify the mask.

Key Updates:

Controller Enhancements (ImageController.java):
Added processImageWithMask() to handle commands involving masks.
Validates the existence of the mask and ensures compatibility with the source image.
Model Enhancements (ImageProcessorImpl.java):
Added logic to restrict transformations to the mask-defined regions.
Optimized algorithms to minimize performance overhead when processing large images with masks.

3. Downscaling (Controller and Model)
This feature enables users to reduce the resolution of an image to specified dimensions, maintaining aspect ratio:

Functionality: The image is resized by mapping pixel values from the original to the target resolution using bilinear interpolation for smooth results.
Command Format: The command downscale <width> <height> <source-image> <dest-image> allows users to specify dimensions.

Key Updates:

Controller Enhancements (ImageController.java):
Extended processImage() to support the downscale operation with user-defined width and height.
Added error handling for invalid dimensions (e.g., zero or negative values).
Model Enhancements (ImageProcessorImpl.java):
Implemented downscale() to efficiently map pixel data using bilinear interpolation.
Ensured compatibility with both GUI and CLI commands.

4. Unified Controller for CLI, Batch, and GUI Operations
The controller was updated to handle commands consistently across all interaction modes:

CLI and Batch Compatibility: Extended execute() to handle both text-based commands and GUI-triggered events.
Error Handling: Integrated error messages for invalid operations, invalid parameters, and missing files, ensuring clarity for users in all modes.

Key Updates:

Controller Enhancements (ImageController.java):
Enhanced execute() to parse GUI-specific commands (e.g., split-view).
Ensured batch operations and GUI interactions do not conflict.

# Mistakes corrected from previous assignments

# From Assignment 4 to 5
Corrected applyFilter logic in loop rather than being hardcoded
Corrected brighten and darken operations which had hardcode increment instead of being customizable
The script was using absolute path rather than relative path . So made a proper script which will use relative path

# From Assignment 5 to 6
ReadMe didn't have justifications of changes from Assignment 4 to 5 corrected it and also added justification of changes from Assignment 5 to 6
Corrected Compress logic
Jar File was just accepting java -jar Program.jar and not working for java -jar Program.jar -file path-of-script-file
Corrected all Jar File versions so current project runs for all following versions:
java -jar Program.jar -file path-of-script-file
java -jar Program.jar -text
java -jar Program.jar

# Citation for image - TestImage1JPG is downloaded and cited from the following website: 

"HD Wallpaper" from Peakpx. Available at: [https://www.peakpx.com/en/hd-wallpaper-desktop-kvlyo]
(https://www.peakpx.com/en/hd-wallpaper-desktop-kvlyo). Accessed October 2024.

# Citation for image - TestImage1-mask is downloaded and cited from the following website:

"Chess Board in Black and White" from iStock. Available at: https://media.istockphoto.com/id/1394093629/vector/chess-board-in-black-and-white-gameboard-for-leisure-or-sport-game-of-chess-vector.jpg?s=612x612&w=0&k=20&c=XhYECWO27u79m9MYwKjR3bprHCo4EDtSOwdqMe6RDWM=. Accessed November 2024.
