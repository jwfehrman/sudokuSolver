import React, { useState } from 'react';
import {
  View,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Text,
  Alert,
} from 'react-native';

const SudokuBoard = () => {
  const [board, setBoard] = useState(Array(9).fill().map(() => Array(9).fill('')));

  const handleCellChange = (rowIndex, colIndex, value) => {
    if (value === '' || (parseInt(value) >= 1 && parseInt(value) <= 9)) {
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
        body: JSON.stringify({
          board: board.map(row => row.map(cell => cell === '' ? 0 : parseInt(cell))),
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to solve Sudoku');
      }

      const solution = await response.json();
      setBoard(solution);
    } catch (error) {
      Alert.alert('Error', 'Failed to solve the Sudoku puzzle. Please try again.');
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
                  ((rowIndex + 1) % 3 === 0 && rowIndex !== 8) && styles.bottomBorder,
                  ((colIndex + 1) % 3 === 0 && colIndex !== 8) && styles.rightBorder,
                ]}
                value={cell.toString()}
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
        <TouchableOpacity style={[styles.button, styles.clearButton]} onPress={clearBoard}>
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
    padding: 20,
  },
  board: {
    borderWidth: 2,
    borderColor: '#000',
  },
  row: {
    flexDirection: 'row',
  },
  cell: {
    width: 40,
    height: 40,
    borderWidth: 0.5,
    borderColor: '#999',
    textAlign: 'center',
    fontSize: 20,
  },
  bottomBorder: {
    borderBottomWidth: 2,
  },
  rightBorder: {
    borderRightWidth: 2,
  },
  buttonContainer: {
    flexDirection: 'row',
    marginTop: 20,
    gap: 10,
  },
  button: {
    backgroundColor: '#007AFF',
    paddingHorizontal: 30,
    paddingVertical: 15,
    borderRadius: 8,
  },
  clearButton: {
    backgroundColor: '#FF3B30',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default SudokuBoard;
