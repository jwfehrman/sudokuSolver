// package com.example.service;

// import java.io.File;
// import java.nio.file.Files;
// import org.bytedeco.opencv.opencv_core.*;
// import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.web.multipart.MultipartFile;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;
// import static org.junit.jupiter.api.Assertions.*;

// public class TestImageProcessing {
//     private ImageProcessingService service;
    
//     @BeforeEach
//     void setUp() {
//         service = new ImageProcessingService();
//     }
    
//     @Test
//     void testProcessImage() throws Exception {
//         // Load the test image
//         String imagePath = "src/main/resources/test-images/sudoku.png";
//         byte[] imageData = Files.readAllBytes(new File(imagePath).toPath());
        
//         // Create MultipartFile from the image data
//         MultipartFile file = new MockMultipartFile(
//             "sudoku.png",
//             "sudoku.png",
//             "image/png",
//             imageData
//         );
        
//         // Process the image
//         String[][] result = service.processImage(file);
        
//         // Verify the result is not null and has correct dimensions
//         assertNotNull(result, "Result should not be null");
//         assertEquals(9, result.length, "Result should have 9 rows");
//         assertEquals(9, result[0].length, "Result should have 9 columns");
        
//         // Print the results for visual verification
//         System.out.println("Recognized Sudoku grid:");
//         for (int i = 0; i < 9; i++) {
//             for (int j = 0; j < 9; j++) {
//                 System.out.print(result[i][j] + " ");
//             }
//             System.out.println();
//         }
//     }
// }
