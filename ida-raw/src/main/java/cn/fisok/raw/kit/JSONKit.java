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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:03
 * @desc :
 */
public abstract class JSONKit {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static{
        SimpleDateFormat sdf = new SimpleDateFormat(DateKit.DATE_TIME_MS_FORMAT);
        OBJECT_MAPPER.setDateFormat(sdf);
    }

    static {
        // configure feature
        OBJECT_MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }


    public static synchronized String toJsonString(Object object,boolean prettify){
        if(prettify){
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("convert object to json string error", ex);
        } finally {
            if(prettify){
                OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
            }
        }

    }
    public static String toJsonString(Object object){
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("convert object to json string error", ex);
        }
    }


    public static <T>T jsonToBean(String text,Class<T> classType){
        try {
            return OBJECT_MAPPER.readValue(text, classType);
        }catch (IOException ex) {
            throw new RuntimeException("convert json string to object error", ex);
        }
    }

    public static Map<String,Object> jsonToMap(String text){
        try {
            return OBJECT_MAPPER.readValue(text, LinkedHashMap.class);
        }catch (IOException ex) {
            throw new RuntimeException("convert json string to object error", ex);
        }
    }

    public static <T> List<T> jsonToBeanList(String text, Class<T> classType){
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, classType));
        } catch (IOException ex) {
            throw new RuntimeException("convert json string to list object error", ex);
        }
    }
}
