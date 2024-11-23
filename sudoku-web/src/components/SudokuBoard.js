import React, { useState } from 'react';
import './SudokuBoard.css';

const SudokuBoard = () => {
  const [board, setBoard] = useState(Array(9).fill().map(() => Array(9).fill('')));

  const handleCellChange = (row, col, value) => {
    // Only allow numbers 1-9 or empty string
    if (value === '' || (value >= '1' && value <= '9')) {
      const newBoard = board.map(row => [...row]);
      newBoard[row][col] = value;
      setBoard(newBoard);
    }
  };

  const handleSolve = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/sudoku/solve', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ board }),
      });
      
      if (!response.ok) {
        throw new Error('Failed to solve puzzle');
      }

      const solvedBoard = await response.json();
      setBoard(solvedBoard.board);
    } catch (error) {
      console.error('Error solving puzzle:', error);
      alert('Error solving puzzle. Please try again.');
    }
  };

  const handleClear = () => {
    setBoard(Array(9).fill().map(() => Array(9).fill('')));
  };

  return (
    <div className="sudoku-container">
      <div className="sudoku-board">
        {board.map((row, rowIndex) => (
          <div key={rowIndex} className="board-row">
            {row.map((cell, colIndex) => (
              <input
                key={`${rowIndex}-${colIndex}`}
                type="text"
                maxLength="1"
                value={cell}
                onChange={(e) => handleCellChange(rowIndex, colIndex, e.target.value)}
                className={`board-cell ${
                  (Math.floor(rowIndex / 3) + Math.floor(colIndex / 3)) % 2 === 0
                    ? 'alternate-section'
                    : ''
                }`}
              />
            ))}
          </div>
        ))}
      </div>
      <div className="button-container">
        <button onClick={handleSolve} className="action-button solve-button">
          Solve
        </button>
        <button onClick={handleClear} className="action-button clear-button">
          Clear
        </button>
      </div>
    </div>
  );
};

export default SudokuBoard;
