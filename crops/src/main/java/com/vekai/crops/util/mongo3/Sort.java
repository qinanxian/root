package com.vekai.crops.util.mongo3;

import com.vekai.crops.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Sorts.*;

public class Sort {

  private Bson sorts;

  public static Sort Builder() {
    return new Sort();
  }

  /**
   * ascending or descending collection fields
   * <p>
   * 对需要排序的集合字段信息加工处理
   * </p>
   * 
   * @param sFields
   *          需要进行排序的集合字段
   *          <p>
   *          字段间以逗号分隔，倒序排列的字段加上"-"前缀，
   *          </p>
   * @return {Sort}
   */
  public Sort ascOrDescending(String sFields) {
    if (CommonUtil.ifIsEmpty(sFields))
      return this;
    List<String> lAsc = new ArrayList<String>();
    List<String> lDesc = new ArrayList<String>();
    List<Bson> bsons = new ArrayList<Bson>();
    String[] aFields = StringUtils.split(sFields, ",");
    for (String field : aFields) {
      if (CommonUtil.ifIsEmpty(field))
        continue;
      if (field.startsWith("-")) {
        field = field.substring(1);
        lDesc.add(field);
      } else {
        lAsc.add(field);
      }
    }
    if (CommonUtil.ifIsNotEmpty(lAsc))
      bsons.add(ascending(lAsc));
    if (CommonUtil.ifIsNotEmpty(lDesc))
      bsons.add(descending(lDesc));
    combine(bsons);
    return this;
  }

  public Bson builder() {
    return sorts;
  }

  private void combine(Bson... bsSort) {
    Bson[] bson = bsSort;
    combine(Arrays.asList(bson));
  }

  private void combine(List<Bson> lSort) {
    if (CommonUtil.ifIsNotEmpty(lSort)) {
      if (CommonUtil.ifIsNotNull(sorts)) {
        lSort.add(sorts);
      }
      sorts = orderBy(lSort);
    }
  }

}
