import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class Main {
    private JLabel cameraLabel;        // Displays camera feed
    private JLabel directionLabel;     // Displays direction (left, center, right)
    private VideoCapture camera;       // OpenCV VideoCapture
    private boolean isCameraRunning;   // Flag to control the camera
    private Mat previousFrame;         // Previous frame for motion detection
    private int frameWidth;            // Width of the video frame

    // Convert Mat to BufferedImage
    public BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    // Start the camera and motion detection
    public void startCamera() {
        if (isCameraRunning) return; // Prevent multiple starts
        isCameraRunning = true;

        camera = new VideoCapture(0); // Open camera
        if (!camera.isOpened()) {
            JOptionPane.showMessageDialog(null, "Error: Camera not detected!");
            isCameraRunning = false;
            return;
        }

        previousFrame = new Mat();
        camera.read(previousFrame);
        Imgproc.cvtColor(previousFrame, previousFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(previousFrame, previousFrame, new Size(21, 21), 0);

        frameWidth = (int) camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        // Height of the video frame
        int frameHeight = (int) camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        // Convert to grayscale and blur to reduce noise
        // Compute difference between frames
        // Dilate to fill in holes
        // Find contours of motion
        // Default centroid position
        // Filter out small motion areas
        // X position of motion centroid
        // Determine motion direction
        // Update previous frame
        // Scale the frame to fit the label while keeping aspect ratio
        // Thread for the camera
        Thread cameraThread = new Thread(() -> {
            Mat currentFrame = new Mat();
            Mat grayFrame = new Mat();
            Mat diffFrame = new Mat();
            Mat threshFrame = new Mat();

            while (isCameraRunning) {
                camera.read(currentFrame);
                if (!currentFrame.empty()) {
                    // Convert to grayscale and blur to reduce noise
                    Imgproc.cvtColor(currentFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur(grayFrame, grayFrame, new Size(21, 21), 0);

                    // Compute difference between frames
                    Core.absdiff(previousFrame, grayFrame, diffFrame);
                    Imgproc.threshold(diffFrame, threshFrame, 25, 255, Imgproc.THRESH_BINARY);

                    // Dilate to fill in holes
                    Imgproc.dilate(threshFrame, threshFrame, new Mat(), new Point(-1, -1), 2);

                    // Find contours of motion
                    java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
                    Imgproc.findContours(threshFrame, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                    int motionX = -1; // Default centroid position

                    for (MatOfPoint contour : contours) {
                        Rect rect = Imgproc.boundingRect(contour);
                        if (rect.area() > 1500) { // Filter out small motion areas
                            motionX = rect.x + rect.width / 2; // X position of motion centroid
                            Imgproc.rectangle(currentFrame, rect, new Scalar(0, 255, 0), 2);
                        }
                    }

                    // Determine motion direction
                    if (motionX != -1) {
                        if (motionX < frameWidth / 3) {
                            directionLabel.setText("You are on LEFT");
                        } else if (motionX > 2 * frameWidth / 3) {
                            directionLabel.setText("You are on RIGHT");
                        } else {
                            directionLabel.setText("You are in CENTER");
                        }
                    }

                    // Update previous frame
                    grayFrame.copyTo(previousFrame);

                    // Scale the frame to fit the label while keeping aspect ratio
                    BufferedImage image = matToBufferedImage(currentFrame);
                    ImageIcon icon = new ImageIcon(image);
                    Image scaledImage = icon.getImage().getScaledInstance(cameraLabel.getWidth(), cameraLabel.getHeight(), Image.SCALE_SMOOTH);
                    cameraLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            camera.release();
        });
        cameraThread.start();
    }

    // Stop the camera feed
    public void stopCamera() {
        isCameraRunning = false;
        if (camera != null) {
            camera.release();
        }
        directionLabel.setText("Camera stopped.");
    }

    // Create GUI
    public void createGUI() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV

        // Frame setup
        JFrame frame = new JFrame("Enhanced Motion Detection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Video display label
        cameraLabel = new JLabel();
        cameraLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cameraLabel.setPreferredSize(new Dimension(800, 600));
        frame.add(cameraLabel, BorderLayout.CENTER);

        // Direction label
        directionLabel = new JLabel("Waiting for motion...", SwingConstants.CENTER);
        directionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        directionLabel.setForeground(Color.RED);
        frame.add(directionLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Camera");
        JButton stopButton = new JButton("Stop Camera");

        // Start button
        startButton.addActionListener(e -> startCamera());
        // Stop button
        stopButton.addActionListener(e -> stopCamera());

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.createGUI();
        });
    }
}
