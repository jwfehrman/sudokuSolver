package com.example;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class App {
    private JFrame frame;
    private JTextField[][] cells;
    private JButton solveButton;
    private JButton clearButton;
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 50;

    public App() {
        setupUI();
    }

    private void setupUI() {
        // Set up the modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Create the grid panel
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 1, 1));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(Color.BLACK);

        // Initialize the text fields
        cells = new JTextField[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                
                // Add borders to separate 3x3 boxes
                if ((row + 1) % 3 == 0 && row != GRID_SIZE - 1) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                } else if ((col + 1) % 3 == 0 && col != GRID_SIZE - 1) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                } else if ((row + 1) % 3 == 0 && (col + 1) % 3 == 0 && row != GRID_SIZE - 1 && col != GRID_SIZE - 1) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                } else {
                    cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                
                gridPanel.add(cells[row][col]);
            }
        }

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear");

        solveButton.addActionListener(this::solveButtonClicked);
        clearButton.addActionListener(e -> clearGrid());

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        // Add components to frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame size and make it visible
        frame.setSize(GRID_SIZE * CELL_SIZE + 100, GRID_SIZE * CELL_SIZE + 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void solveButtonClicked(ActionEvent e) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        
        // Read values from text fields
        try {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    String value = cells[i][j].getText().trim();
                    board[i][j] = value.isEmpty() ? 0 : Integer.parseInt(value);
                    if (board[i][j] < 0 || board[i][j] > 9) {
                        throw new NumberFormatException();
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, 
                "Please enter valid numbers (1-9) or leave cells empty.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Try to solve the puzzle
        if (solveSudoku(board)) {
            // Update UI with solution
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, 
                "No solution exists for this puzzle.", 
                "No Solution", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i][j].setText("");
            }
        }
    }

    private boolean solveSudoku(int[][] board) {
        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findEmptyCell(int[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check row
        for (int j = 0; j < GRID_SIZE; j++) {
            if (board[row][j] == num) return false;
        }

        // Check column
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == num) return false;
        }

        // Check 3x3 box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRow + i][boxCol + j] == num) return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}
