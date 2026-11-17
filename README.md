# Numerical Methods Library (APR)

A Java library implementing common numerical methods, written as part of the **Analysis and Design by Computer (APR)** course at FER, University of Zagreb.

## Implemented methods

- **Matrices**: matrix arithmetic, LU and LUP decomposition, forward/backward substitution
- **Linear systems**: solving using decompositions, with dedicated exception types
- **Optimization**:
  - Golden-section search
  - Coordinate descent
  - Gradient descent
  - Newton–Raphson
  - Nelder–Mead simplex
  - Hooke–Jeeves
  - Box constrained optimization
  - Barrier/penalty transformation
- **Numerical integration of ODEs**:
  - Euler method
  - Backward Euler method
  - Trapezoidal method
  - Runge–Kutta method
  - Predictor–corrector methods

`IntegrationDemo` compares the integration methods on the example systems from the lab.

## Requirements

- Java JDK 11 or newer
- Eclipse is optional; the repository includes Eclipse project metadata

## Build and run

From the project root:

```bash
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac -d bin
java -cp bin IntegrationDemo
```

## Project structure

```text
src/
├── IntegrationDemo.java
└── util/
    ├── equations/
    ├── functions/
    ├── integration/
    ├── matrices/
    └── optimizations/
input_files/lab_1/   # sample matrices used by the first lab exercise
```

## Notes

`src/IntegrationDemo.java` contains absolute paths from the original author's machine (for example `/Users/.../APR-workspace/lab-5/...`). Before running the demo, change these paths to point to your own input files. The `input_files/lab_1` directory contains sample matrices from an earlier exercise.
