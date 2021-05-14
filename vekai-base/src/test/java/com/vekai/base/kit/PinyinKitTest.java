package com.vekai.base.kit;

import org.junit.Test;

public class PinyinKitTest {
    @Test
    public void test01(){
        System.out.println(PinyinKit.hanziToPinyin("中华人民共和国"));
        System.out.println(PinyinKit.hanziToPinyin("张3"));
        System.out.println(PinyinKit.hanziToPinyin("1李"));
        System.out.println(PinyinKit.hanziToPinyin("ABC"));
        System.out.println(PinyinKit.hanziToPinyin("A3"));
        System.out.println(PinyinKit.hanziToPinyin("赵C"));
    }
}
