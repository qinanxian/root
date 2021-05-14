package com.vekai.appframe.support;

public class PersonIdCardTest {
    public static void main(String[] args) {
        String idcard15 = "370283790911703";
        String idcard18 = "37078119790127719x";
        PersonIdCard ie15 = new PersonIdCard(idcard15);
        PersonIdCard ie18 = new PersonIdCard(idcard18);
        System.out.println(ie15.toString());
        System.out.println(ie18.toString());
    }
}
