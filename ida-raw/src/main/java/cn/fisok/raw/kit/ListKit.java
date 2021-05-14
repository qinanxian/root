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

import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:03
 * @desc :
 */
public abstract class ListKit {
    public static <E> ArrayList<E> newArrayList(int initialCapacity){
        return new ArrayList<E>(initialCapacity);
    };
    public static <E> ArrayList<E> newArrayList(){
//        return Lists.newArrayList();
        return new ArrayList<E>();
    };
    public static <E> ArrayList<E> listOf(E... elements){
        List<E> list = Arrays.asList(elements);
        return new ArrayList<E>(list);
    }

    /**
     * 合并去重
     *
     * @param lists  lists
     * @param <E> e
     * @return list
     */
    public static <E> List<E> mergeDistinct(Collection<E>... lists){
        Set<E> set = new LinkedHashSet<>();
        for(Collection<E> l : lists){
            if(l==null)continue;
            set.addAll(l);
        }
        ArrayList<E> list = new ArrayList<E>();
        list.addAll(set);
        return list;
    }
    /**
     * 合并
     *
     * @param lists lists
     * @param <E> e
     * @return list
     */
    public static <E> List<E> merge(Collection<E>... lists){
        ArrayList<E> list = new ArrayList<E>();
        for(Collection<E> l : lists){
            if(l==null)continue;
            list.addAll(l);
        }
        return list;
    }

    public static <E> boolean isBlank(List<E> list){
        return null == list || list.isEmpty();
    }

    public static <E> boolean isNotBlank(List<E> list){
        return null != list && !list.isEmpty();
    }
}
