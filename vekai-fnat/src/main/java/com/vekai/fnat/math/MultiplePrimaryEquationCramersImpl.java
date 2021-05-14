package com.vekai.fnat.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * 使用克莱姆法则解多元一次方程(MultiplePrimaryEquation)<br/>
 * 此方法解方程效率较低，当未知数在7个时，需要用时6s多，以后，每增加一个未知数，效率低一倍<br/>
 * 此克莱姆法则目前证明不可靠，需要调整优化后方可使用<br/>
 * @author 杨松<syang@amarsoft.com>
 * @date 2016/05/11
 *
 */
public class MultiplePrimaryEquationCramersImpl implements MultiplePrimaryEquationSolver {
	private BigDecimal deterM[][];

	public BigDecimal[] solver(BigDecimal[][] matrix, int scale, RoundingMode roundingMode) {
		return null;
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
	/**
	 * 
	 * @param coefficientMatrix 左边的系数矩阵
	 * @param valueCol	右边的值列
	 * @param scale 精确小数位数
	 * @param roundingMode 舍入模式
	 * @return
	 */
	public BigDecimal[] solver(BigDecimal coefficientMatrix[][], BigDecimal valueCol[], int scale, RoundingMode roundingMode) {
		int variableCount = coefficientMatrix.length;
		if(coefficientMatrix.length != valueCol.length){
			throw new RuntimeException("argument coefficientMatrix.length != valueCol.length Error");
		}
		
		BigDecimal[] result = new BigDecimal[variableCount];
		
		BigDecimal temp[][] = new BigDecimal[variableCount][variableCount];
		for (int i = 0; i < variableCount; i++) {
			for (int j = 0; j < variableCount; j++) {
				for (int k = 0; k < variableCount; k++) {
					if (k == i){
						temp[j][k] = valueCol[j];
					}else{
						temp[j][k] = coefficientMatrix[j][k];
					}
				}
			}
			result[i] = determinant(temp, variableCount).divide(determinant(coefficientMatrix, variableCount),scale,roundingMode);

		}
		return result;
	}

	public BigDecimal determinant(BigDecimal A[][], int N) {
		BigDecimal res;

		if (N == 1){
			res = A[0][0];
		}else if (N == 2) {
//			res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
			res = (A[0][0].multiply(A[1][1])).subtract((A[1][0].multiply(A[0][1])));
		}else {
			res = new BigDecimal("0");
			for (int j1 = 0; j1 < N; j1++) {
				deterM = new BigDecimal[N - 1][];
				for (int k = 0; k < (N - 1); k++){
					deterM[k] = new BigDecimal[N - 1];
				}
				for (int i = 1; i < N; i++) {
					int j2 = 0;
					for (int j = 0; j < N; j++) {
						if (j == j1) continue;
						deterM[i - 1][j2] = A[i][j];
						j2++;
					}
				}
//				res += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1] * determinant(deterM, N - 1);
				res = res.add(BigDecimal.valueOf(Math.pow(-1.0, 1.0 + j1 + 1.0)).multiply(A[0][j1]).multiply(determinant(deterM, N-1)));

			}
		}

		return res;

	}

}
