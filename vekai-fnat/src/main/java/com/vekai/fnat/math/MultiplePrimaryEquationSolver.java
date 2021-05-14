package com.vekai.fnat.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * 多元一次方程组(MultiplePrimaryEquation)的求解接口
 * @author 杨松<syang@amarsoft.com>
 * @date 2016/05/11
 *
 */
public interface MultiplePrimaryEquationSolver {
	
	/**
	 * 解多元一次方程组，解N个方程，N个未知数的情况
	 * @param coefficientMatrix 系数矩阵
	 * @param valueCol 值列
	 * @param scale 计算精度
	 * @param roundingMode 四舍五入模式
	 * @return
	 */
	public BigDecimal[] solver(BigDecimal coefficientMatrix[][], BigDecimal valueCol[], int scale, RoundingMode roundingMode);
	public Vector solver(Matrix coefficientMatrix, Vector valueCol, int scale, RoundingMode roundingMode);
}
