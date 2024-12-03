# Sudoku Solver

A full-stack Sudoku solving application built with Spring Boot and React. This application provides a web interface where users can input Sudoku puzzles and solve them using a backtracking algorithm.

## Features

- Interactive 9x9 Sudoku grid
- Real-time validation of inputs
- Solve button to automatically solve the puzzle
- Clear button to reset the grid

## Technology Stack

- Backend: Spring Boot (Java)
- Frontend: React
- Build Tools: Maven (backend), npm (frontend)

## Prerequisites

- Java 17 or higher
- Node.js and npm
- Maven

## Running the Application

### Backend (Spring Boot)

1. Navigate to the root directory:
```bash
cd sudokuSolver
```

2. Build the backend:
```bash
mvn clean install
```

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend server will start on `http://localhost:8080`

### Frontend (React)

1. Navigate to the frontend directory:
```bash
cd sudoku-web
```

2. Install dependencies:
```bash
npm install
```

3. Start the React development server:
```bash
npm start
```

The frontend will be available at `http://localhost:3000`

## How to Use

1. Open your web browser and navigate to `http://localhost:3000`
2. Input numbers (1-9) into the Sudoku grid cells
3. Click "Solve" to solve the puzzle
4. Click "Clear" to reset the grid

## Development

- Backend API endpoint: `http://localhost:8080/api/sudoku/solve`
- Frontend source code is in the `sudoku-web` directory
- Main solving logic is in `src/main/java/com/example/service/SudokuService.java`
