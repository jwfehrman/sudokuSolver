package com.example.controller;

import com.example.service.SudokuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:3000")
public class SudokuController {
    
    private final SudokuService sudokuService;

    public SudokuController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    @PostMapping("/solve")
    public ResponseEntity<SudokuResponse> solveSudoku(@RequestBody SudokuRequest request) {
        try {
            String[][] solvedBoard = sudokuService.solve(request.getBoard());
            return ResponseEntity.ok(new SudokuResponse(solvedBoard));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SudokuResponse(null));
        }
    }
}

class SudokuRequest {
    private String[][] board;

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}

class SudokuResponse {
    private String[][] board;

    public SudokuResponse(String[][] board) {
        this.board = board;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}
