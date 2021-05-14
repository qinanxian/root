package com.vekai.appframe.support;

public class PersonIdCardValidatorTest {
    public static void main(String[] args) throws Exception {

        String idcard15 = "370283790911703";//15位
        String idcard18 = "37078119790127719x";//18位
        PersonIdCardValidator iv = new PersonIdCardValidator();
        System.out.println(iv.isValidatedAllIdcard(idcard15));
        System.out.println(iv.isValidatedAllIdcard(idcard18));

    }
}
