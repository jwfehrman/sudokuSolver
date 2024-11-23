import React from 'react';
import './App.css';
import SudokuBoard from './components/SudokuBoard';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Sudoku Solver</h1>
        <SudokuBoard />
      </header>
    </div>
  );
}

export default App;
