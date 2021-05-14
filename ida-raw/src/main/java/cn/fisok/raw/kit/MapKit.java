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
package cn.fisok.raw.kit;

import cn.fisok.raw.lang.MapObject;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:10
 * @desc : 把常用的对Map的操作方法放这里来，主要是放guava中对Map的操作,把常用的放这里,是为了简化操作,也方便升级调整
 */
public abstract class MapKit {

    public static Map<String,Object> newEmptyMap(){
        return new HashMap<String,Object>();
    }

    public static <K, V> Map<K, V> newHashMap(int initialCapacity) {
//        return Maps.newHashMap();
        return new HashMap<K,V>(3);
    }
    public static <K, V> Map<K, V> newHashMap() {
//        return Maps.newHashMap();
        return new HashMap<K,V>();
    }

    public static <K, V> Map<K, V> newLinkedHashMap() {
//        return Maps.newLinkedHashMap();
        return new LinkedHashMap<K,V>();
    }

    public static <K, V> Map<K, V> mapOf(K k1, V v1) {
//        return ImmutableMap.of(k1, v1);
        Map<K,V> map = newHashMap(1);
        map.put(k1,v1);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
//        return ImmutableMap.of(k1, v1, k2, v2);
        Map<K,V> map = newHashMap(1);
        map.put(k1,v1);
        map.put(k2,v2);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
//        return ImmutableMap.of(k1, v1, k2, v2, k3, v3);
        Map<K,V> map = newHashMap(1);
        map.put(k1,v1);
        map.put(k2,v2);
        map.put(k3,v3);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
//        return ImmutableMap.of(k1, v1, k2, v2, k3, v3,k4,v4);
        Map<K,V> map = newHashMap(1);
        map.put(k1,v1);
        map.put(k2,v2);
        map.put(k3,v3);
        map.put(k4,v4);
        return map;
    }
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
//        return ImmutableMap.of(k1, v1, k2, v2, k3, v3,k4,v4,k5,v5);
        Map<K,V> map = newHashMap(1);
        map.put(k1,v1);
        map.put(k2,v2);
        map.put(k3,v3);
        map.put(k4,v4);
        map.put(k5,v5);
        return map;
    }

    public static void merge(Map<String, ?> toMap,Map<String,Object> fromMap){
        Map<String,Object> param = (Map<String, Object>) toMap;
        param.putAll(fromMap);
    }

    public static Map<String,Object> flatMultiValueMap(MultiValueMap<String,Object> map){
        Map<String,Object> ret = new LinkedHashMap<String,Object>();
        Iterator<Map.Entry<String, List<Object>>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, List<Object>> entry = iterator.next();
            List<Object> value = entry.getValue();
            if(null != value && !value.isEmpty()){
                if(value.size()==1){
                    ret.put(entry.getKey(), value.get(0));
                }else{
                    ret.put(entry.getKey(), value);
                }
            }
        }
        return ret;
    }

    /**
     * 把Map列表转为MapObject列表
     * @param dataList
     * @return
     */
    public static List<MapObject> convertToMapObjects(List<Map<String,Object>> dataList){
        if(dataList==null) return null;
        List<MapObject> dataBoxList = ListKit.newArrayList();

        for(int i=0;i<dataList.size();i++){
            dataBoxList.add(convertToMapObject(dataList.get(i)));
        }

        return dataBoxList;
    }

    /**
     * 把Map转为MapObject
     * @param data
     * @return
     */
    public static MapObject convertToMapObject(Map<String,Object> data){
        if(data==null) return null;
        MapObject dataBox = new MapObject();
        dataBox.putAll(data);
        return dataBox;
    }
}
