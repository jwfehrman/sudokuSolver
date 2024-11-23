package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class SudokuService {
    
    public String[][] solve(String[][] board) {
        // Convert string board to int board for solving
        int[][] intBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                intBoard[i][j] = board[i][j].isEmpty() ? 0 : Integer.parseInt(board[i][j]);
            }
        }

        if (solveSudoku(intBoard)) {
            // Convert back to string board
            String[][] solvedBoard = new String[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    solvedBoard[i][j] = String.valueOf(intBoard[i][j]);
                }
            }
            return solvedBoard;
        }
        return null;
    }

    private boolean solveSudoku(int[][] board) {
        int row = -1;
        int col = -1;
        boolean isEmpty = false;
        
        // Find empty cell
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = true;
                    break;
                }
            }
            if (isEmpty) {
                break;
            }
        }
        
        // No empty cell found
        if (!isEmpty) {
            return true;
        }
        
        // Try digits 1-9
        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }
    
    private boolean isSafe(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }
        
        // Check column
        for (int x = 0; x < 9; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }
        
        // Check 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
