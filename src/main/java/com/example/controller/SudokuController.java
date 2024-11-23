package com.example.controller;

import com.example.service.SudokuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:3000")
public class SudokuController {

    @Autowired
    private SudokuService sudokuService;

    @PostMapping("/solve")
    public ResponseEntity<SudokuResponse> solve(@RequestBody SudokuRequest request) {
        String[][] solvedBoard = sudokuService.solve(request.getBoard());
        if (solvedBoard != null) {
            SudokuResponse response = new SudokuResponse(solvedBoard);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(new SudokuResponse(null));
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
