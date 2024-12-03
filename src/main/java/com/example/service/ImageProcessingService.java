// package com.example.service;

// import org.bytedeco.javacv.*;
// import org.bytedeco.opencv.opencv_core.*;
// import static org.bytedeco.opencv.global.opencv_core.*;
// import static org.bytedeco.opencv.global.opencv_imgproc.*;
// import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
// import org.bytedeco.javacpp.indexer.UByteIndexer;
// import org.bytedeco.javacpp.indexer.FloatIndexer;
// import org.bytedeco.javacpp.IntPointer;
// import org.bytedeco.javacpp.FloatPointer;
// import org.bytedeco.opencv.opencv_imgproc.Vec2fVector;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
// import javax.annotation.PostConstruct;
// import javax.annotation.PreDestroy;
// import java.awt.image.BufferedImage;
// import java.awt.image.DataBufferByte;
// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import net.sourceforge.tess4j.Tesseract;
// import net.sourceforge.tess4j.TesseractException;
// import java.util.Arrays;

// @Service
// public class ImageProcessingService {
//     private static final Logger logger = LoggerFactory.getLogger(ImageProcessingService.class);
//     private Mat[] digitTemplates;
//     private final Tesseract tesseract;

//     public ImageProcessingService() {
//         // Initialize Tesseract
//         tesseract = new Tesseract();
        
//         // Try multiple common Tesseract installation paths
//         String[] possiblePaths = {
//             "C:\\Program Files\\Tesseract-OCR\\tessdata",
//             "C:\\Program Files (x86)\\Tesseract-OCR\\tessdata",
//             System.getenv("TESSERACT_PATH"),
//             "./tessdata",  // Look for tessdata in current directory
//             "src/main/resources/tessdata", // Look in resources directory
//             "." // Look in current directory
//         };
        
//         boolean found = false;
//         for (String path : possiblePaths) {
//             if (path != null && new File(path).exists()) {
//                 tesseract.setDatapath(path);
//                 found = true;
//                 logger.info("Found Tesseract data path: {}", path);
//                 break;
//             }
//         }
        
//         if (!found) {
//             logger.warn("Tesseract data path not found in common locations. OCR functionality may be limited.");
//         }
        
//         // Configure Tesseract for digit recognition
//         tesseract.setPageSegMode(10); // Treat the image as a single character
//         tesseract.setTessVariable("tessedit_char_whitelist", "123456789"); // Only allow digits 1-9
//         tesseract.setTessVariable("tessedit_pageseg_mode", "10");
//         tesseract.setTessVariable("tessedit_ocr_engine_mode", "0"); // Use Legacy engine which works better for single characters

//         // Initialize digit templates
//         logger.info("Initializing digit templates");
//         try {
//             digitTemplates = new Mat[10];
//             for (int i = 0; i < 10; i++) {
//                 digitTemplates[i] = new Mat(28, 28, CV_8UC1);
//                 createDigitTemplate(digitTemplates[i], i);
//                 logger.debug("Created template for digit {}", i);
//             }
//             logger.info("Successfully initialized all digit templates");
//         } catch (Exception e) {
//             logger.error("Failed to initialize digit templates: {}", e.getMessage(), e);
//             throw new RuntimeException("Failed to initialize digit templates", e);
//         }
//     }
    
//     @PreDestroy
//     public void cleanup() {
//         // Release digit templates
//         if (digitTemplates != null) {
//             for (Mat template : digitTemplates) {
//                 if (template != null) {
//                     template.release();
//                 }
//             }
//         }
//     }
    
//     private void createDigitTemplate(Mat template, int digit) {
//         // Clear the template
//         template.put(new Mat(template.size(), template.type(), new Scalar(0)));
        
//         // Calculate center and size
//         int centerX = template.cols() / 2;
//         int centerY = template.rows() / 2;
//         int size = Math.min(template.cols(), template.rows()) * 3 / 4;  // Use 75% of the template size
        
//         // Thickness adjusted for better matching (fixed type conversion)
//         int thickness = Math.max(1, (int)(size / 12));
        
//         switch (digit) {
//             case 0:
//                 // Ellipse for 0
//                 ellipse(template,
//                        new Point(centerX, centerY),
//                        new Size(size/3, size/2),
//                        0, 0, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 1:
//                 // Vertical line with small serif
//                 line(template, 
//                      new Point(centerX, centerY - size/2), 
//                      new Point(centerX, centerY + size/2), 
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Top serif
//                 line(template,
//                      new Point(centerX - size/6, centerY - size/2),
//                      new Point(centerX + size/6, centerY - size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 2:
//                 // Top arc
//                 ellipse(template,
//                        new Point(centerX, centerY - size/4),
//                        new Size(size/3, size/4),
//                        0, 0, 180,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Diagonal line
//                 line(template,
//                      new Point(centerX + size/3, centerY - size/4),
//                      new Point(centerX - size/3, centerY + size/3),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom line
//                 line(template,
//                      new Point(centerX - size/3, centerY + size/3),
//                      new Point(centerX + size/3, centerY + size/3),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 3:
//                 // Top arc
//                 ellipse(template,
//                        new Point(centerX, centerY - size/4),
//                        new Size(size/3, size/4),
//                        0, 0, 180,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom arc
//                 ellipse(template,
//                        new Point(centerX, centerY + size/4),
//                        new Size(size/3, size/4),
//                        0, 180, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Middle line
//                 line(template,
//                      new Point(centerX - size/6, centerY),
//                      new Point(centerX + size/3, centerY),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 4:
//                 // Vertical line
//                 line(template,
//                      new Point(centerX + size/4, centerY - size/2),
//                      new Point(centerX + size/4, centerY + size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Horizontal line
//                 line(template,
//                      new Point(centerX - size/3, centerY),
//                      new Point(centerX + size/3, centerY),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Diagonal line
//                 line(template,
//                      new Point(centerX - size/3, centerY - size/2),
//                      new Point(centerX + size/4, centerY),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 5:
//                 // Top horizontal
//                 line(template,
//                      new Point(centerX - size/3, centerY - size/2),
//                      new Point(centerX + size/3, centerY - size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Middle horizontal
//                 line(template,
//                      new Point(centerX - size/3, centerY),
//                      new Point(centerX + size/3, centerY),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom horizontal
//                 line(template,
//                      new Point(centerX - size/3, centerY + size/2),
//                      new Point(centerX + size/3, centerY + size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Top vertical
//                 line(template,
//                      new Point(centerX - size/3, centerY - size/2),
//                      new Point(centerX - size/3, centerY),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom vertical
//                 line(template,
//                      new Point(centerX + size/3, centerY),
//                      new Point(centerX + size/3, centerY + size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 6:
//                 // Top arc
//                 ellipse(template,
//                        new Point(centerX, centerY - size/3),
//                        new Size(size/3, size/6),
//                        0, 90, 270,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom circle
//                 ellipse(template,
//                        new Point(centerX, centerY + size/6),
//                        new Size(size/3, size/3),
//                        0, 0, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 7:
//                 // Top horizontal
//                 line(template,
//                      new Point(centerX - size/3, centerY - size/2),
//                      new Point(centerX + size/3, centerY - size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 // Diagonal
//                 line(template,
//                      new Point(centerX + size/3, centerY - size/2),
//                      new Point(centerX - size/6, centerY + size/2),
//                      new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 8:
//                 // Top circle
//                 ellipse(template,
//                        new Point(centerX, centerY - size/4),
//                        new Size(size/3, size/4),
//                        0, 0, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom circle
//                 ellipse(template,
//                        new Point(centerX, centerY + size/4),
//                        new Size(size/3, size/4),
//                        0, 0, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 break;
                
//             case 9:
//                 // Top circle
//                 ellipse(template,
//                        new Point(centerX, centerY - size/6),
//                        new Size(size/3, size/3),
//                        0, 0, 360,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 // Bottom arc
//                 ellipse(template,
//                        new Point(centerX, centerY + size/3),
//                        new Size(size/3, size/6),
//                        0, -90, 90,
//                        new Scalar(255), thickness, LINE_AA, 0);
//                 break;
//         }
        
//         // Apply slight Gaussian blur to smooth the template
//         GaussianBlur(template, template, new Size(3, 3), 0);
//     }

//     private String recognizeDigitWithOpenCV(Mat cell, boolean isProblematicCell) {
//         Mat processed = null;
//         Mat normalized = null;
        
//         try {
//             processed = cell.clone();
            
//             // Convert to grayscale if needed
//             if (processed.channels() > 1) {
//                 cvtColor(processed, processed, COLOR_BGR2GRAY);
//             }
            
//             // Resize to match template size (slightly larger for better detail)
//             resize(processed, processed, new Size(32, 32));
            
//             // Apply Gaussian blur to reduce noise
//             GaussianBlur(processed, processed, new Size(3, 3), 0);
            
//             // Apply Otsu's thresholding for better binarization
//             threshold(processed, processed, 0, 255, THRESH_BINARY | THRESH_OTSU);
            
//             // Remove small noise
//             Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
//             try {
//                 morphologyEx(processed, processed, MORPH_OPEN, kernel);
//                 // Also close gaps
//                 morphologyEx(processed, processed, MORPH_CLOSE, kernel);
//             } finally {
//                 kernel.release();
//             }
            
//             // Check if cell is empty (too few white pixels)
//             int whitePixels = countNonZero(processed);
//             double whitePixelRatio = (double) whitePixels / (processed.rows() * processed.cols());
//             if (whitePixelRatio < 0.05 || whitePixelRatio > 0.95) {  // Adjusted thresholds
//                 return "0";  // Return 0 for empty or filled cells
//             }
            
//             // Normalize the image
//             normalized = new Mat();
//             processed.convertTo(normalized, CV_32F);
//             normalize(normalized, normalized, 0.0, 1.0, NORM_MINMAX, CV_32F, null);
            
//             // Try to match against each template
//             double bestMatch = -1;
//             int bestDigit = -1;
//             double[] matchScores = new double[10];
            
//             for (int i = 0; i < 10; i++) {
//                 Mat result = new Mat();
//                 Mat templateFloat = new Mat();
//                 try {
//                     // Convert template to float and normalize
//                     digitTemplates[i].convertTo(templateFloat, CV_32F);
//                     normalize(templateFloat, templateFloat, 0.0, 1.0, NORM_MINMAX, CV_32F, null);
                    
//                     // Try multiple scales for better matching
//                     double[] scales = {0.8, 0.9, 1.0, 1.1, 1.2};
//                     double bestScaleMatch = -1;
                    
//                     for (double scale : scales) {
//                         Mat scaledTemplate = new Mat();
//                         try {
//                             Size scaledSize = new Size((int)(templateFloat.cols() * scale), (int)(templateFloat.rows() * scale));
//                             resize(templateFloat, scaledTemplate, scaledSize);
                            
//                             // Ensure template fits within the image
//                             if (scaledTemplate.cols() <= normalized.cols() && scaledTemplate.rows() <= normalized.rows()) {
//                                 Mat scaleResult = new Mat();
//                                 try {
//                                     matchTemplate(normalized, scaledTemplate, scaleResult, TM_CCOEFF_NORMED);
//                                     double[] maxVal = new double[1];
//                                     minMaxLoc(scaleResult, null, maxVal, null, null, null);
//                                     bestScaleMatch = Math.max(bestScaleMatch, maxVal[0]);
//                                 } finally {
//                                     scaleResult.release();
//                                 }
//                             }
//                         } finally {
//                             scaledTemplate.release();
//                         }
//                     }
                    
//                     matchScores[i] = bestScaleMatch;
//                     if (bestScaleMatch > bestMatch) {
//                         bestMatch = bestScaleMatch;
//                         bestDigit = i;
//                     }
//                 } finally {
//                     result.release();
//                     templateFloat.release();
//                 }
//             }
            
//             // Log match scores for debugging
//             StringBuilder scores = new StringBuilder("Match scores: ");
//             for (int i = 0; i < 10; i++) {
//                 scores.append(String.format("%d=%.3f ", i, matchScores[i]));
//             }
//             logger.debug(scores.toString());
            
//             // Adjusted confidence threshold and added secondary check
//             if (bestMatch > 0.4) {  // Lowered primary threshold
//                 // Check if the best match is significantly better than the second best
//                 double secondBestMatch = -1;
//                 for (int i = 0; i < 10; i++) {
//                     if (i != bestDigit && matchScores[i] > secondBestMatch) {
//                         secondBestMatch = matchScores[i];
//                     }
//                 }
                
//                 if (bestMatch > secondBestMatch * 1.2) {  // Best match should be at least 20% better than second best
//                     return String.valueOf(bestDigit);
//                 }
//             }
            
//             return "0";  // Return 0 for unrecognized cells
            
//         } catch (Exception e) {
//             logger.error("Error in OpenCV recognition: {}", e.getMessage());
//             return "0";  // Return 0 for error cases
//         } finally {
//             if (processed != null) processed.release();
//             if (normalized != null) normalized.release();
//         }
//     }

//     private BufferedImage matToBufferedImage(Mat mat) {
//         int type = BufferedImage.TYPE_BYTE_GRAY;
//         if (mat.channels() > 1) {
//             type = BufferedImage.TYPE_3BYTE_BGR;
//         }
        
//         int bufferSize = mat.channels() * mat.cols() * mat.rows();
//         byte[] buffer = new byte[bufferSize];
//         mat.data().get(buffer);
        
//         BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
//         final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//         System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        
//         return image;
//     }

//     public String[][] processImage(MultipartFile file) throws Exception {
//         logger.info("Starting image processing for file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
        
//         if (file.isEmpty()) {
//             logger.error("Uploaded file is empty");
//             throw new IOException("Uploaded file is empty");
//         }

//         Path tempFile = Files.createTempFile("sudoku_", ".png");
//         String[][] grid = new String[9][9];
//         Mat original = null;
//         Mat preprocessed = null;
        
//         try {
//             // Save uploaded file to temp file
//             file.transferTo(tempFile.toFile());
            
//             // Read image
//             original = imread(tempFile.toString());
//             if (original.empty()) {
//                 throw new IOException("Failed to read image file");
//             }
            
//             // Preprocess image
//             preprocessed = preprocessImage(original);
            
//             // Find grid lines
//             List<Integer> horizontalLines = new ArrayList<>();
//             List<Integer> verticalLines = new ArrayList<>();
//             findGridLines(preprocessed, horizontalLines, verticalLines);
            
//             if (horizontalLines.size() < 10 || verticalLines.size() < 10) {
//                 throw new IOException("Failed to detect complete Sudoku grid in image. Expected 10 lines in each direction.");
//             }
            
//             // Process each cell
//             for (int row = 0; row < 9; row++) {
//                 for (int col = 0; col < 9; col++) {
//                     Mat cell = null;
//                     try {
//                         // Get cell boundaries from grid lines
//                         int x1 = verticalLines.get(col);
//                         int y1 = horizontalLines.get(row);
//                         int x2 = verticalLines.get(col + 1);
//                         int y2 = horizontalLines.get(row + 1);
                        
//                         // Extract and process cell
//                         cell = extractCell(preprocessed, x1, y1, x2, y2);
                        
//                         // Recognize number
//                         String result = recognizeDigit(cell);
                        
//                         // Store result
//                         grid[row][col] = result.isEmpty() ? "0" : result;
//                         logger.info("Cell [{},{}] recognized as: '{}'", row, col, grid[row][col]);
//                     } catch (Exception e) {
//                         logger.error("Error processing cell [{},{}]: {}", row, col, e.getMessage(), e);
//                         grid[row][col] = "0";
//                     } finally {
//                         if (cell != null) {
//                             cell.release();
//                         }
//                     }
//                 }
//             }
            
//             // Print recognized grid for debugging
//             logger.info("Recognized Sudoku grid:");
//             for (int i = 0; i < 9; i++) {
//                 StringBuilder row = new StringBuilder();
//                 for (int j = 0; j < 9; j++) {
//                     row.append(grid[i][j]).append(" ");
//                 }
//                 logger.info(row.toString());
//             }
            
//             return grid;
            
//         } finally {
//             // Clean up
//             try {
//                 Files.delete(tempFile);
//             } catch (Exception e) {
//                 logger.warn("Failed to delete temporary file: {}", e.getMessage());
//             }
            
//             // Release OpenCV resources
//             if (original != null) {
//                 original.release();
//             }
//             if (preprocessed != null) {
//                 preprocessed.release();
//             }
//         }
//     }

//     private Mat preprocessImage(Mat image) {
//         logger.debug("Starting image preprocessing");
//         Mat processed = null;
//         Mat gray = null;
//         Mat binary = null;
//         Mat result = null;
        
//         try {
//             // Create output matrix
//             processed = new Mat();
            
//             // Resize image if too large
//             if (image.cols() > 1000 || image.rows() > 1000) {
//                 double scale = Math.min(1000.0 / image.cols(), 1000.0 / image.rows());
//                 resize(image, processed, new Size(), scale, scale, INTER_AREA);
//             } else {
//                 image.copyTo(processed);
//             }

//             // Convert to grayscale
//             gray = new Mat();
//             if (processed.channels() > 1) {
//                 cvtColor(processed, gray, COLOR_BGR2GRAY);
//             } else {
//                 processed.copyTo(gray);
//             }

//             // Apply Gaussian blur to reduce noise
//             GaussianBlur(gray, gray, new Size(5, 5), 0);

//             // Apply adaptive thresholding
//             binary = new Mat();
//             adaptiveThreshold(gray, binary, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY_INV, 11, 2);

//             // Apply morphological operations to remove noise
//             Mat kernel = getStructuringElement(MORPH_RECT, new Size(3, 3));
//             try {
//                 result = new Mat();
//                 morphologyEx(binary, result, MORPH_OPEN, kernel);
//             } finally {
//                 kernel.release();
//             }

//             // Normalize the image
//             normalize(result, result, 0.0, 255.0, NORM_MINMAX, CV_8UC1, null);
            
//             return result.clone();
            
//         } catch (Exception e) {
//             logger.error("Error during preprocessing: {}", e.getMessage(), e);
//             throw e;
//         } finally {
//             // Clean up all resources
//             if (processed != null) processed.release();
//             if (gray != null) gray.release();
//             if (binary != null) binary.release();
//             if (result != null) result.release();
//         }
//     }

//     private Mat extractCell(Mat img, int x1, int y1, int x2, int y2) {
//         Mat cell = null;
//         Mat processed = null;
//         try {
//             // Add small padding to avoid grid lines
//             int padding = 3;
//             x1 += padding;
//             y1 += padding;
//             x2 -= padding;
//             y2 -= padding;

//             // Ensure coordinates are within bounds
//             x1 = Math.max(0, x1);
//             y1 = Math.max(0, y1);
//             x2 = Math.min(img.cols(), x2);
//             y2 = Math.min(img.rows(), y2);

//             // Check if the cell size is valid
//             if (x2 <= x1 || y2 <= y1) {
//                 throw new IllegalArgumentException("Invalid cell coordinates");
//             }

//             // Extract the cell
//             cell = new Mat(img, new Rect(x1, y1, x2 - x1, y2 - y1));
            
//             // Create a copy for processing
//             processed = new Mat();
//             cell.copyTo(processed);
            
//             // Only apply minimal morphological operations
//             Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
//             try {
//                 erode(processed, processed, kernel); // Remove thin grid lines
//                 dilate(processed, processed, kernel); // Restore number thickness
//             } finally {
//                 kernel.release();
//             }
            
//             // Return a clone of the processed cell
//             Mat result = processed.clone();
//             return result;
//         } catch (Exception e) {
//             logger.error("Error extracting cell: {}", e.getMessage());
//             // If we fail, return a copy of the original region
//             if (cell != null) {
//                 return cell.clone();
//             }
//             return new Mat(img, new Rect(x1, y1, x2 - x1, y2 - y1)).clone();
//         } finally {
//             if (cell != null) {
//                 cell.release();
//             }
//             if (processed != null) {
//                 processed.release();
//             }
//         }
//     }

//     private void findGridLines(Mat img, List<Integer> horizontalLines, List<Integer> verticalLines) {
//         int height = img.rows();
//         int width = img.cols();

//         // Create copy of preprocessed image
//         Mat gridImg = img.clone();
//         MatVector contours = new MatVector();
//         Mat hierarchy = new Mat();
//         Point2fVector points = new Point2fVector(4);
        
//         try {
//             // Find contours
//             findContours(gridImg, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

//             // Find the largest square contour (should be the Sudoku grid)
//             double maxArea = 0;
//             RotatedRect bestRect = null;
            
//             for (long i = 0; i < contours.size(); i++) {
//                 Mat contour = contours.get(i);
//                 double area = contourArea(contour);
//                 if (area > maxArea) {
//                     RotatedRect rect = minAreaRect(contour);
//                     // Check if it's approximately square
//                     Size2f size = rect.size();
//                     double aspectRatio = size.width() / size.height();
//                     if (aspectRatio >= 0.8 && aspectRatio <= 1.2) {
//                         maxArea = area;
//                         bestRect = rect;
//                     }
//                 }
//             }

//             if (bestRect != null) {
//                 // Get the corners of the grid
//                 bestRect.points(points);
                
//                 // Convert points to array for sorting
//                 float[][] cornerPoints = new float[4][2];
//                 for (int i = 0; i < 4; i++) {
//                     Point2f p = points.get(i);
//                     cornerPoints[i][0] = p.x();
//                     cornerPoints[i][1] = p.y();
//                 }
                
//                 // Sort points by x + y to get top-left (min) and bottom-right (max)
//                 Arrays.sort(cornerPoints, (a, b) -> Float.compare(a[0] + a[1], b[0] + b[1]));
//                 float[] topLeft = cornerPoints[0];
//                 float[] bottomRight = cornerPoints[3];
                
//                 // Sort points by x - y to get top-right (max) and bottom-left (min)
//                 Arrays.sort(cornerPoints, (a, b) -> Float.compare(a[0] - a[1], b[0] - b[1]));
//                 float[] topRight = cornerPoints[3];
//                 float[] bottomLeft = cornerPoints[0];

//                 // Add outer boundary lines first
//                 horizontalLines.add((int)topLeft[1]);
//                 verticalLines.add((int)topLeft[0]);
                
//                 // Add inner grid lines
//                 for (int i = 1; i < 9; i++) {
//                     // Calculate vertical line positions
//                     float x = topLeft[0] + (topRight[0] - topLeft[0]) * i / 9;
//                     verticalLines.add((int)x);
                    
//                     // Calculate horizontal line positions
//                     float y = topLeft[1] + (bottomLeft[1] - topLeft[1]) * i / 9;
//                     horizontalLines.add((int)y);
//                 }
                
//                 // Add final boundary lines
//                 horizontalLines.add((int)bottomRight[1]);
//                 verticalLines.add((int)bottomRight[0]);
                
//                 // Sort the lines
//                 Collections.sort(horizontalLines);
//                 Collections.sort(verticalLines);
                
//                 logger.debug("Found {} horizontal and {} vertical lines", 
//                            horizontalLines.size(), verticalLines.size());
//             } else {
//                 logger.error("No valid grid contour found");
//             }
//         } catch (Exception e) {
//             logger.error("Error finding grid lines: {}", e.getMessage(), e);
//         } finally {
//             // Clean up all native resources
//             gridImg.release();
//             contours.deallocate();  
//             hierarchy.release();
//             points.deallocate();
//         }
//     }

//     private String recognizeDigit(Mat cell) {
//         // Try template matching first, as it's more reliable for digit recognition
//         String templateResult = recognizeDigitWithOpenCV(cell, false);
//         if (!templateResult.isEmpty()) {
//             return templateResult;
//         }
        
//         // Only try OCR as a fallback
//         BufferedImage img = null;
//         try {
//             // Convert Mat to BufferedImage
//             img = matToBufferedImage(cell);
            
//             // Preprocess image for OCR
//             Mat processed = new Mat();
//             cell.copyTo(processed);
            
//             try {
//                 // Resize to a larger size for better OCR
//                 resize(processed, processed, new Size(100, 100));
//                 threshold(processed, processed, 0, 255, THRESH_BINARY | THRESH_OTSU);
                
//                 // Convert processed Mat to BufferedImage
//                 img = matToBufferedImage(processed);
                
//                 // Use Tesseract to recognize the digit
//                 String result = tesseract.doOCR(img).trim().replaceAll("\\s+", "");
                
//                 // Return the first recognized digit if any
//                 if (!result.isEmpty() && result.matches("[1-9]")) {
//                     return result;
//                 }
//             } finally {
//                 processed.release();
//             }
//         } catch (Exception e) {
//             logger.error("Error during OCR: {}", e.getMessage());
//         } finally {
//             if (img != null) {
//                 img.flush();
//             }
//         }
        
//         return "";
//     }
// }
