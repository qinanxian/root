package com.vekai.lact.type;

import java.math.RoundingMode;

public abstract class LactConst {
    public static final RoundingMode RUNDING_MODE = RoundingMode.HALF_UP;
    public static final Integer CALC_SCALE = 12;
    /**
     * 计算时，利率的精度（保留2位小数，由于是百分比，因此需要再加上2）
     */
    public static final Integer INTEREST_RATE_SCALE = 4;
}
