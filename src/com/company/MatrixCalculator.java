package com.company;

import java.util.ArrayList;

public class MatrixCalculator {
    static ArrayList<ArrayList<Double>> multiplyMatrix(ArrayList<ArrayList<Double>> firstMatrix,
                                                       ArrayList<ArrayList<Double>> secondMatrix) {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        for (int i = 0; i < firstMatrix.size(); ++i){
            result.add(new ArrayList<>(secondMatrix.get(0).size()));
            for (int j = 0; j < secondMatrix.get(0).size(); ++j)
                result.get(i).add(j,0.0);
        }

        for(int row = 0; row < result.size(); row++) {
            for(int col = 0; col < result.get(row).size(); col++) {
                result.get(row).set(col, multiplyMatrixCells(firstMatrix, secondMatrix, row, col));
            }
        }
        return result;

    }

    static double multiplyMatrixCells(ArrayList<ArrayList<Double>> firstMatrix,
                                      ArrayList<ArrayList<Double>> secondMatrix, int row, int col) {
        double cell = 0;

        //every cell of the matrix is the result of adding every row of the first matrix times every column of the
        // second matrix
        for(int i = 0; i < secondMatrix.size(); i++) {
            cell += firstMatrix.get(row).get(i) * secondMatrix.get(i).get(col);
        }
        return cell;
    }
}
