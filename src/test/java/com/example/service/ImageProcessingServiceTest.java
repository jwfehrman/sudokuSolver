// package com.example.service;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.web.multipart.MultipartFile;
// import org.bytedeco.opencv.opencv_core.*;
// import static org.bytedeco.opencv.global.opencv_core.*;
// import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
// import static org.junit.jupiter.api.Assertions.*;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @SpringBootTest
// public class ImageProcessingServiceTest {
//     @Autowired
//     private ImageProcessingService imageProcessingService;
//     private static final String SAMPLE_IMAGE_PATH = "src/main/resources/test-images/sudoku.png";

//     @Test
//     void testProcessImage() throws Exception {
//         // Load the sample image
//         Path path = Paths.get(SAMPLE_IMAGE_PATH);
//         String name = "sudoku.png";
//         String originalFileName = "sudoku.png";
//         String contentType = "image/png";
//         byte[] content = Files.readAllBytes(path);
//         MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);
//         System.out.println("Processing image: " + file.getOriginalFilename());
//         // Process the image
//         String[][] result = imageProcessingService.processImage(file);

//         // Verify results
//         assertNotNull(result);
//         assertTrue(isValidSudokuGrid(result), "Grid should contain valid Sudoku numbers");
        
//         // Print the grid for debugging
//         System.out.println("Recognized Sudoku grid:");
//         for (int i = 0; i < 9; i++) {
//             for (int j = 0; j < 9; j++) {
//                 System.out.print(result[i][j] + " ");
//             }
//             System.out.println();
//         }
//     }

//     private boolean isValidSudokuGrid(String[][] grid) {
//         if (grid == null) return false;
//         if (grid.length != 9) return false;
        
//         for (String[] row : grid) {
//             if (row.length != 9) return false;
//             for (String cell : row) {
//                 if (cell == null || !cell.matches("[0-9]")) {
//                     return false;
//                 }
//             }
//         }
//         return true;
//     }
// }
