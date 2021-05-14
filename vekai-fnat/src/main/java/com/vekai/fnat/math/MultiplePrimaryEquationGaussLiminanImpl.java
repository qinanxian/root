package com.vekai.fnat.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.la4j.Matrix;
import org.la4j.Vector;


/**
 * 高斯消元法解多元一次方程(MultiplePrimaryEquation)
 * @author 杨松<syang@amarsoft.com>
 * @date 2016/05/11
 *
 */
public class MultiplePrimaryEquationGaussLiminanImpl implements MultiplePrimaryEquationSolver{

	private static final BigDecimal ZERO = new BigDecimal("0");
	private static final BigDecimal ONE = new BigDecimal("1");

	public BigDecimal[] solver(BigDecimal[][] matrix, int scale, RoundingMode roundingMode) {
		if (isNullOrEmptyMatrix(matrix)) {
			return new BigDecimal[0];
		}
		BigDecimal[][] triangular = elimination(matrix, scale, roundingMode);
		return substitutionUpMethod(triangular, scale, roundingMode);
	}
	
    public Vector solver(Matrix coefficientMatrix, Vector valueCol, int scale, RoundingMode roundingMode) {
        BigDecimal[][] matrix = new BigDecimal[coefficientMatrix.rows()][coefficientMatrix.columns()];
        BigDecimal[] vector = new BigDecimal[valueCol.length()];
        //矩阵转二维数组，主要为了复用以前的逻辑
        for(int i=0;i<coefficientMatrix.rows();i++){
            for(int j=0;j<coefficientMatrix.columns();j++){
                matrix[i][j] = BigDecimal.valueOf(coefficientMatrix.get(i, j));
            }
        }
        //向量转一级数组，主要为了复用以前的逻辑
        for(int i=0;i<valueCol.length();i++){
            vector[i]=BigDecimal.valueOf(valueCol.get(i));
        }
        
        //结果变形输出
        BigDecimal[] result = solver(matrix, vector, scale, roundingMode);
        double[] result1 = new double[result.length];
        for(int i=0;i<result.length;i++){
            result1[i] = result[i].doubleValue();
        }
        return Vector.fromArray(result1);
    }

	public BigDecimal[] solver(BigDecimal coefficientMatrix[][], BigDecimal valueCol[], int scale, RoundingMode roundingMode) {
		int variableCount = coefficientMatrix.length;
		//把列值列接到矩阵中来
		BigDecimal[][] matrix = new BigDecimal[coefficientMatrix.length][coefficientMatrix.length+1];
		for(int i=0;i<variableCount;i++){
			for(int j=0;j<coefficientMatrix[i].length;j++){
				matrix[i][j] = coefficientMatrix[i][j];
			}
			matrix[i][matrix[i].length-1]=valueCol[i];
		}
		
		return solver(matrix, scale, roundingMode);
		
	}
	/**
	 * 用高斯消元法将矩阵变为上三角形矩阵
	 *
	 * @param matrix
	 * @param scale
	 *            精确小数位数
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	private BigDecimal[][] elimination(BigDecimal[][] matrix, int scale, RoundingMode roundingMode) {
		if (isNullOrEmptyMatrix(matrix) || matrix.length != matrix[0].length - 1) {
			return new BigDecimal[0][0];
		}
		int matrixLine = matrix.length;
		for (int i = 0; i < matrixLine - 1; ++i) {
			// 第j行的数据 - (第i行的数据 / matrix[i][i])*matrix[j][i]
			for (int j = i + 1; j < matrixLine; ++j) {
				for (int k = i + 1; k <= matrixLine; ++k) {
					// matrix[j][k] = matrix[j][k] - (matrix[i][k]/matrix[i][i])*matrix[j][i];
					matrix[j][k] = matrix[j][k].subtract((matrix[i][k].divide(matrix[i][i], scale, roundingMode).multiply(matrix[j][i])));
				}
				matrix[j][i] = ZERO;
			}
		}
		return matrix;
	}

	/**
	 * 回代求解(针对上三角形矩阵)
	 *
	 * @param matrix
	 *            上三角阵
	 * @param scale
	 *            精确小数位数
	 * @param roundingMode
	 *            舍入模式
	 */
	private BigDecimal[] substitutionUpMethod(BigDecimal[][] matrix, int scale, RoundingMode roundingMode) {
		int row = matrix.length;
		for (int i = 0; i < row; ++i) {
			if (matrix[i][i].equals(ZERO.setScale(scale))) {// 方程无解或者解不惟一
				return new BigDecimal[0];
			}
		}
		BigDecimal[] result = new BigDecimal[row];
		for (int i = 0; i < result.length; ++i) {
			result[i] = ONE;
		}
		BigDecimal tmp;
		for (int i = row - 1; i >= 0; --i) {
			tmp = ZERO;
			int j = row - 1;
			while (j > i) {
				tmp = tmp.add(matrix[i][j].multiply(result[j]));
				--j;
			}
			result[i] = matrix[i][row].subtract(tmp).divide(matrix[i][i], scale, roundingMode);
		}
		return result;
	}

	/**
	 * 判断系数矩阵是否是null或空数组
	 * 
	 * @param matrix
	 *            系数矩阵
	 * @return null或空数组返回true,否则返回false
	 */
	private static boolean isNullOrEmptyMatrix(BigDecimal[][] matrix) {
		if (matrix == null || matrix.length == 0) {
			return true;
		}
		int row = matrix.length;
		int col = matrix[0].length;
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				if (matrix[i][j] == null) {
					return true;
				}
			}
		}
		return false;
	}

}
