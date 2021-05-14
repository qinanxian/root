/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.lang;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:30
 * @desc :
 */
public class ValueObject {
    private Object value;
    private ValueType type;
    private static Pattern NUMBER_PATTERN = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
    private static DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.################################");
    private Map<String,Object> properties = new HashMap<String,Object>();

    public enum ValueType {
        String, Boolean, Number,Double,Integer, Long,Date, Object, Null,Array
    }

    public void setValueType(ValueType type) {
        this.type = type;
    }

    public ValueType getValueType() {
        return type;
    }

    public static ValueObject valueOf(Object value) {
        return new ValueObject(value);
    }

    public ValueObject() {
        setValue(null);
    }
    public ValueObject(Object value) {
        setValue(value);
    }

    public void setNull(){
        setValue(null);
    }

    public void setValue(Object value){
        if(value instanceof ValueObject){
            value = ((ValueObject)value).value();
        }
        if(type==null){
            if (value == null) {
                this.type = ValueType.Null;
            } else if (value instanceof String) {
                this.type = ValueType.String;
            } else if (value instanceof Number) {
                this.type = ValueType.Number;
            } else if (value instanceof Date) {
                this.type = ValueType.Date;
            } else if(value.getClass().isArray()) {
                this.type = ValueType.Array;
            } else {
                this.type = ValueType.Object;
            }
        }
        this.value = value;
    }

    public Object value() {
        return this.value;
    }

    public Object getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public <T> T objectVal(Class<T> classType) {
        return (T) value;
    }

    public Number numberValue() {
        if(value == null)return 0d;
        if (type == ValueType.Number) {
            return (Number) value;
        }else if (type == ValueType.Double) {
            return (Double) value;
        }else if (type == ValueType.Integer) {
            return (Integer) value;
        } else if (type == ValueType.Long) {
            return (Long) value;
        }  else if (type == ValueType.String && NUMBER_PATTERN.matcher((String) value).matches()) {
            return Double.parseDouble((String) value);
        } else {
            throw new RuntimeException("value [" + value + "] is not a digital format");
        }
    }

    public Long longValue() {
        return numberValue().longValue();
    }

    public Long longValue(Long defaultValue) {
        if(numberValue()==null)return defaultValue;
        return longValue();
    }

    public Integer intValue() {
        return numberValue().intValue();
    }
    public Integer intValue(Integer defaultValue) {
        if(numberValue()==null)return defaultValue;
        return intValue();
    }

    public Double doubleValue() {
        return numberValue().doubleValue();
    }
    public Double doubleValue(Double defaultValue) {
        if(numberValue()==null)return defaultValue;
        return doubleValue();
    }

    public String strValue() {
        return strValue(null);
    }
    public String strValue(String defaultValue) {
        if(value == null)return defaultValue;

        try{
            if (type == ValueType.Number) {
                return NUMBER_FORMAT.format(numberValue());
            } else if (type == ValueType.String) {
                return String.valueOf(value);
            } else if (type == ValueType.Date) {
                return (new SimpleDateFormat(DateKit.DATE_TIME_FORMAT)).format(dateValue());
            } else if (type == ValueType.Null) {
                return defaultValue;
            } else {
                return value.toString();
            }
        }catch (Exception e){
            return String.valueOf(value);
        }
    }

    @SuppressWarnings("deprecation")
    public Date dateValue() {
        if (type == ValueType.Number) {
            return DateKit.parse(longValue());
        } else if (type == ValueType.String) {
            String strValue = strValue();
            //如果是数字类型的，就按数字类型转
            if(strValue.matches("\\d+")){
                return DateKit.parse(longValue());
            }else{
                return DateKit.parse(strValue());
            }
        } else if (type == ValueType.Date) {
            return (Date) value;
        } else if (type == ValueType.Null) {
            return null;
        } else {
            try{
                return DateKit.parse(strValue());
            }catch (Exception e){
                throw new RuntimeException("value [" + value + "] is not a date format");
            }
        }
    }

    public Boolean boolValue() {
        if (type == ValueType.String) {
            return Boolean.parseBoolean(strValue());
        } else if (type == ValueType.Boolean) {
            return (Boolean) value;
        } else if (type == ValueType.Null) {
            return null;
        } else {
            try{
                return Boolean.parseBoolean(strValue());
            }catch (Exception e){
                throw new RuntimeException("value [" + value + "] is not a boolean format");
            }
        }
    }

    public Boolean boolValue(Boolean defaultValue){
        if(boolValue()==null)return defaultValue;
        return boolValue();
    }

    public Object[] objectArray(){
        Object[] values = null;
        if(value==null)return values;
        if(value.getClass().isArray()){
            return (Object[])value;
        }else if(value instanceof List){
            @SuppressWarnings("rawtypes")
            List<?> vList = (List)value;
            values = new Object[vList.size()];
            for(int i=0;i<vList.size();i++){
                values[i] = vList.get(i);
            }
        }
        return values;
    }

    public Number[] numberArray(){
        Object[] values = objectArray();
        if(values==null||values.length==0)return null;
        Number[] ret = new Number[values.length];
        for(int i=0;i<values.length;i++){
            ValueObject vo = new ValueObject(values[i]);
            ret[i] = vo.numberValue();
        }
        return ret;
    }

    public Long[] longArray() {
        Number[] numbers = numberArray();
        Long[] ret = new Long[numbers.length];
        for(int i=0;i<numbers.length;i++){
            ret[i] = numbers[i].longValue();
        }
        return ret;
    }

    public Integer[] intArray() {
        Number[] numbers = numberArray();
        Integer[] ret = new Integer[numbers.length];
        for(int i=0;i<numbers.length;i++){
            ret[i] = numbers[i].intValue();
        }
        return ret;
    }

    public Double[] doubleArray() {
        Number[] numbers = numberArray();
        Double[] ret = new Double[numbers.length];
        for(int i=0;i<numbers.length;i++){
            ret[i] = numbers[i].doubleValue();
        }
        return ret;
    }

    public String[] strArray() {
        Object[] values = objectArray();
        if(values==null||values.length==0)return null;
        String[] ret = new String[values.length];
        for(int i=0;i<values.length;i++){
            ValueObject vo = new ValueObject(values[i]);
            ret[i] = vo.strValue();
        }
        return ret;
    }

    public Date[] dateArray() {
        Object[] values = objectArray();
        if(values==null||values.length==0)return null;
        Date[] ret = new Date[values.length];
        for(int i=0;i<values.length;i++){
            ValueObject vo = new ValueObject(values[i]);
            ret[i] = vo.dateValue();
        }
        return ret;
    }

    public Boolean[] boolArray() {
        Object[] values = objectArray();
        if(values==null||values.length==0)return null;
        Boolean[] ret = new Boolean[values.length];
        for(int i=0;i<values.length;i++){
            ValueObject vo = new ValueObject(values[i]);
            ret[i] = vo.boolValue();
        }
        return ret;
    }

    /**
     * 值是否为NULL
     *
     * @return boolean
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * 值对象是否为没有内容,例如空字串,空集合等情况
     *
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    public boolean isEmpty() {
        if (isNull()) return true;
        if (value.getClass().isArray()) {
            return ((Object[]) value).length == 0;
        } else if (value instanceof Collection) {
            return ((Collection) value).size() == 0;
        }
        return false;
    }

    public boolean isBlank(){
        if (isNull()) return true;
        return StringKit.isBlank(strValue());
    }

    public boolean isArray(){
        if(value==null)return false;
        return value.getClass().isArray();
    }

    public String toString() {
        if(isArray()){
            StringBuffer stringBuffer = new StringBuffer("{");
            Object[] values = objectArray();
            for(int i=0;i<values.length;i++){
                stringBuffer.append(values[i]).append(",");
            }
            if(stringBuffer.charAt(stringBuffer.length()-1) == ','){
                stringBuffer.deleteCharAt(stringBuffer.length()-1);
            }
            stringBuffer.append("}");
            return stringBuffer.toString();
        }else{
            if (value != null) {
                return strValue();
            } else {
                return null;
            }
        }
    }

    public void setProperty(String name,Object value){
        properties.put(name,value);
    }
    public Object getProperty(String name){
        return properties.get(name);
    }
}
