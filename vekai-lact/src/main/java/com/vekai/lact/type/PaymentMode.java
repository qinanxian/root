package com.vekai.lact.type;

/**
 * 还款计算方式
 */
public enum PaymentMode {
    /** 等额本息 */
    EqualLoan,
    /** 等额本金 */
    EqualPrincipal,
    /** 先息后本 */
    EachInterest,
    /** 一次性还本付息(利随本清） */
    OneTime,
    /** 确定本金(明示利率),等期或者不等期,每期本金和还款日都已知 */
    DeterminatePrincipal,
    /** 隐含利率 */
    ImplicationCustomize
}
