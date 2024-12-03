import React, { useState } from 'react';
import {
  View,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Text,
} from 'react-native';

const SudokuBoard = () => {
  const [board, setBoard] = useState(Array(9).fill().map(() => Array(9).fill('')));

  const handleCellChange = (rowIndex, colIndex, value) => {
    if (value === '' || (value >= '1' && value <= '9')) {
      const newBoard = [...board];
      newBoard[rowIndex][colIndex] = value;
      setBoard(newBoard);
    }
  };

  const solveSudoku = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/sudoku/solve', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ board }),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.board) {
          setBoard(data.board);
        }
      }
    } catch (error) {
      console.error('Error solving Sudoku:', error);
    }
  };

  const clearBoard = () => {
    setBoard(Array(9).fill().map(() => Array(9).fill('')));
  };

  return (
    <View style={styles.container}>
      <View style={styles.board}>
        {board.map((row, rowIndex) => (
          <View key={rowIndex} style={styles.row}>
            {row.map((cell, colIndex) => (
              <TextInput
                key={`${rowIndex}-${colIndex}`}
                style={[
                  styles.cell,
                  ((colIndex + 1) % 3 === 0 && colIndex !== 8) && styles.rightBorder,
                  ((rowIndex + 1) % 3 === 0 && rowIndex !== 8) && styles.bottomBorder,
                ]}
                value={cell}
                onChangeText={(value) => handleCellChange(rowIndex, colIndex, value)}
                keyboardType="numeric"
                maxLength={1}
              />
            ))}
          </View>
        ))}
      </View>
      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button} onPress={solveSudoku}>
          <Text style={styles.buttonText}>Solve</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={clearBoard}>
          <Text style={styles.buttonText}>Clear</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#282c34',
  },
  board: {
    borderWidth: 2,
    borderColor: '#8a8d94',
  },
  row: {
    flexDirection: 'row',
  },
  cell: {
    width: 40,
    height: 40,
    borderWidth: 1,
    borderColor: '#8a8d94',
    textAlign: 'center',
    color: '#FFFFFF',
    backgroundColor: '#353a45',
  },
  rightBorder: {
    borderRightWidth: 2,
  },
  bottomBorder: {
    borderBottomWidth: 2,
  },
  buttonContainer: {
    flexDirection: 'row',
    marginTop: 20,
    gap: 10,
  },
  button: {
    backgroundColor: '#4a90e2',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 5,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default SudokuBoard;
