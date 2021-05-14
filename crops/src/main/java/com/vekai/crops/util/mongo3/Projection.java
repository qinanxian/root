package com.vekai.crops.util.mongo3;

/**
 * mongodb projection
 * 
 *  参考：http://blog.csdn.net/u014265212/article/details/53404666
 */

import com.vekai.crops.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Projections.*;

public class Projection {

  private Bson projections;

  public static Projection Builder() {
    return new Projection();
  }

  public Bson builder() {
    return projections;
  }

  /**
   * include or exclude collection fields
   * <p>
   * 根据传入字段项获取基础的集合字段投射
   * </p>
   * <p>
   * 可对多个字段，包括_id进行投射处理
   * </p>
   * <p>
   * _id 默认是隐式展示，若要排除_id需显式的设置"-_id"
   * </p>
   * 
   * @param sFields
   *          需要进行投射的字段项
   *          <p>
   *          字段间以逗号分隔，要排除的字段加上"-"前缀，
   *          </p>
   * @return {Projection}
   */
  public Projection inOrExclude(String sFields) {
    if (CommonUtil.ifIsEmpty(sFields))
      return this;
    List<String> includeFields = new ArrayList<String>();
    List<String> excludeFields = new ArrayList<String>();
    boolean bExcludeid = false;
    List<Bson> bsons = new ArrayList<Bson>();
    String[] aFields = StringUtils.split(sFields, ",");
    for (String field : aFields) {
      if (CommonUtil.ifIsEmpty(field))
        continue;
      if (field.startsWith("-")) {
        field = field.substring(1);
        if ("_id".equals(field)) {
          bExcludeid = true;
          continue;
        }
        excludeFields.add(field);
      } else {
        includeFields.add(field);
      }
    }
    if (bExcludeid)
      bsons.add(excludeId());
    if (CommonUtil.ifIsNotEmpty(includeFields))
      bsons.add(include(includeFields));
    if (CommonUtil.ifIsNotEmpty(excludeFields))
      bsons.add(exclude(excludeFields));
    combine(bsons);
    return this;
  }

  /**
   * matches one element of arrays
   * <p>
   * 根据查询条件匹配展示第一个数组元素 field.$:1
   * </p>
   * 
   * @param sFields
   *          数组结构的字段，可以为多个，以逗号分隔
   * @return {Projection}
   */
  public Projection matchOneElementOfArray(String sFields) {
    return matchOneElementOfArray(sFields, "");
  }
  
  /**
   * matches one element of arrays
   * <p>
   * 根据查询条件匹配展示第一个数组元素
   * </p>
   * 
   * @param sFields
   *          数组结构的字段，可以为多个，以逗号分隔
   * @param sFilter
   *          匹配数组时的条件
   * @return {Projection}
   */
  public Projection matchOneElementOfArray(String sFields, String sFilter) {
    Bson bsFilter = null;
    if(CommonUtil.ifIsNotEmpty(sFilter)) {
      bsFilter = BsonDocument.parse(sFilter);
    }
    return matchOneElementOfArray(sFields, bsFilter);
  }
  
  /**
   * matches one element of arrays
   * <p>
   * 根据查询条件匹配展示第一个数组元素
   * </p>
   * 
   * @param sFields
   *          数组结构的字段，可以为多个，以逗号分隔
   * @param bsFilter
   *          匹配数组时的条件
   * @return {Projection}
   */
  public Projection matchOneElementOfArray(String sFields, Bson bsFilter) {
    if (CommonUtil.ifIsEmpty(sFields))
      return this;
//    if (CommonUtil.ifIsNull(bsFilter))
//      return this;
    List<Bson> bsons = new ArrayList<Bson>();
    String[] aFields = StringUtils.split(sFields, ",");
    for (String field : aFields) {
      Bson bsElemMatch = null;
      if (CommonUtil.ifIsNull(bsFilter)) {
        bsElemMatch = elemMatch(field);
      } else {
        bsElemMatch = elemMatch(field, bsFilter);
      }
      bsons.add(bsElemMatch);
    }
    combine(bsons);
    return this;
  }

  /**
   * slice element of arrays
   * <p>
   * 将数组元素进行切片，可指定切片的开始位置(默认0)和切片的长度
   * </p>
   * 
   * @param sField
   *          数组结构的字段 <var style="color:red">必输</var>
   * @param iSkip
   *          切片的起始位置 (默认0)
   * @param iLimit
   *          切片的长度 <var style="color:red">必输</var>
   * @return  Projection
   */
  public Projection sliceOfArray(String sField, int iSkip, int iLimit) {
    if (CommonUtil.ifIsEmpty(sField) || iLimit <= 0)
      return this;
    Bson bsSlice = null;
    // if (iSkip > 0) {
    // bsSlice = slice(sField, iSkip, iLimit);
    // } else {
    // bsSlice = slice(sField, 0, iLimit);
    // }
    bsSlice = slice(sField, iSkip, iLimit);
    combine(bsSlice);
    return this;
  }

  private void combine(Bson... bsProjection) {
    Bson[] bson = bsProjection;
    List<Bson> list = Arrays.asList(bson);
    combine(new ArrayList<Bson>(list));
  }

  private void combine(List<Bson> lProjection) {
    if (CommonUtil.ifIsNotEmpty(lProjection)) {
      if (CommonUtil.ifIsNotNull(projections)) {
        lProjection.add(projections);
      }
      projections = fields(lProjection);
    }
  }

}
