package com.vekai.crops.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

public class ArrayNodeValueComparatorDesc implements Comparator {
  private Collator collator = Collator.getInstance();
  public boolean ASCE = true;
  public String Field = "";

  public ArrayNodeValueComparatorDesc() {
  }

  public int compare(Object o1, Object o2) {
    // 把字符串转换为一系列比特，它们可以以比特形式与 CollationKeys 相比较
    // 要想不区分大小写进行比较用o1.toString().toLowerCase()
    JsonNode node1 = (JsonNode) o1;
    JsonNode node2 = (JsonNode) o2;
    String s1 = "";
    String s2 = "";
    if (CommonUtil.ifIsEmpty(Field)) {
      s1 = node1.toString();
      s2 = node2.toString();
    } else {
      try {
        s1 = JsonUtil.getJsonStringValue(node1, Field);
        s2 = JsonUtil.getJsonStringValue(node2, Field);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    CollationKey key1 = collator.getCollationKey(s1);
    CollationKey key2 = collator.getCollationKey(s2);

    // 返回的分别为1,0,-1 分别代表大于，等于，小于。要想按照字母降序排序的话 加个“-”号
    if (ASCE)
      return -key2.compareTo(key1);
    else
      return key2.compareTo(key1);
  }
}
