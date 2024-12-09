<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Motion Detection Using OpenCV and Java Swing</title>
</head>
<body>
    <h1>🚀 Motion Detection Using OpenCV and Java Swing</h1>
    <p>
        A Java-based real-time motion detection application that integrates <strong>OpenCV</strong> for video processing and <strong>Swing</strong> for the graphical user interface (GUI).
    </p>

    <h2>📋 Project Overview</h2>
    <p>
        This project captures video from a webcam, detects motion in real-time, and identifies the direction of motion (Left, Center, or Right). It uses OpenCV for image processing and Java Swing for the user interface.
    </p>

    <h2>🛠️ Features</h2>
    <ul>
        <li>📷 Real-time video capture using OpenCV.</li>
        <li>⚡ Efficient motion detection using frame difference.</li>
        <li>🧭 Motion direction detection: <em>Left</em>, <em>Center</em>, or <em>Right</em>.</li>
        <li>🖥️ GUI integration for video display and feedback.</li>
        <li>🎯 Visual feedback with bounding rectangles on moving objects.</li>
    </ul>

    <h2>🔧 How It Works</h2>
    <ol>
        <li><strong>GUI Setup:</strong> A user-friendly interface with video display, direction feedback, and control buttons.</li>
        <li><strong>Camera Initialization:</strong> Captures video frames using OpenCV's <code>VideoCapture</code>.</li>
        <li><strong>Motion Detection Algorithm:</strong>
            <ul>
                <li>Grayscale conversion and Gaussian blur to reduce noise.</li>
                <li>Frame difference to highlight motion areas.</li>
                <li>Thresholding and dilation to clean up the motion regions.</li>
                <li>Contour detection to identify motion regions.</li>
                <li>Calculation of motion direction based on region centroid.</li>
            </ul>
        </li>
        <li><strong>Visual Feedback:</strong> Bounding rectangles on motion areas and direction indication (Left, Center, Right).</li>
        <li><strong>Camera Control:</strong> Start and stop camera functionality.</li>
    </ol>

    <h2>🖼️ Screenshot</h2>
    <p>A snapshot of the application in action:</p>
    <!-- Add your image link here -->
    <img src="screenshot.png" alt="Motion Detection Application Screenshot" width="800">

    <h2>💻 Technologies Used</h2>
    <ul>
        <li>Java (Swing for GUI)</li>
        <li>OpenCV (for video processing)</li>
    </ul>

    <h2>🚀 How to Run</h2>
    <ol>
        <li>Install <a href="https://opencv.org/releases/">OpenCV</a> and configure it with your Java project.</li>
        <li>Ensure you have a working webcam connected to your system.</li>
        <li>Run the <code>Main.java</code> file.</li>
    </ol>

    <h2>📄 Code Overview</h2>
    <pre>
<strong>Main.java</strong>
- createGUI(): Sets up the GUI components.
- startCamera(): Initializes the camera and starts the motion detection thread.
- stopCamera(): Releases the camera resources.
- matToBufferedImage(): Converts OpenCV Mat objects to BufferedImage for display.
- Motion Detection Logic:
    - Frame difference, thresholding, contour detection, and centroid-based direction determination.
    </pre>

    <h2>🤝 Contributing</h2>
    <p>
        Contributions are welcome! Please fork this repository, make your changes, and submit a pull request.
    </p>

    <h2>📜 License</h2>
    <p>
        This project is licensed under the MIT License.
    </p>

</body>
</html>
