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

import cn.fisok.raw.RawConsts;

import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:37
 * @desc : 字串比较匹配工具类
 */
public abstract class StringMatcherKit {

    /**
     * 比较两个字串，返回他们不同的字符个数
     *
     * @param str str
     * @param target target
     * @return int
     */
    public static int compare(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1

        if (n == 0)  return m;
        if (m == 0)   return n;
        d = new int[n + 1][m + 1];

        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }

        return d[n][m];
    }

    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 计算两个字串的相似度
     *
     * @param str str
     * @param target target
     * @return double
     */
    public static double getSimilarityRatio(String str, String target) {
        return 1 - (double) compare(str, target) / Math.max(str.length(), target.length());

    }

    /**
     * 相似度匹配排序，比较和源字串的相似度，相似度最高的放到最前
     *
     * @param srcStr 源字串
     * @param minRatio 最小匹配度，低于此值的，将不会出现到返回值中
     * @param strList 目标字串列表
     * @return list
     */
    public static List<String> sortSimilarity(String srcStr, double minRatio, String ...strList){
        return sortSimilarity(srcStr,minRatio, Arrays.asList(strList));
    }
    /**
     * 相似度匹配排序，比较和源字串的相似度，相似度最高的放到最前
     *
     * @param srcStr 源字串
     * @param minRatio 最小匹配度，低于此值的，将不会出现到返回值中
     * @param strList 目标字串列表
     * @return list
     */
    public static List<String> sortSimilarity(String srcStr,double minRatio,List<String> strList){
        List<String> simList = new ArrayList<String>();
        //筛选
        Map<String,String> simMap = new HashMap<String,String>();
        for(int i=0;i<strList.size();i++){
            String str = strList.get(i);
            double simRatio = getSimilarityRatio(srcStr,str);
            if(simRatio<minRatio)continue;
            String key = ""+simRatio;
            if(simMap.containsKey(key))key = key+"_1";
            simMap.put(key, str);
        }
        //排序
        while(simMap.size()>0){
            String s = getMaxKeyValue(simMap);
            if(s!=null)simList.add(s);
        }

        return simList;
    }

    private static String getMaxKeyValue(Map<String,String> simMap){
        if(simMap.size()==0)return null;
        String max = "";
        Iterator<String> iterator = simMap.keySet().iterator();
        while(iterator.hasNext()){
            String i = iterator.next();
            if(i.compareTo(max)>0){
                max = i;
            }
        }
        String r =   simMap.get(max);
        simMap.remove(max);
        return r;
    }

    /**
     * 比较两个字符串，取得两字符串开始不同的索引值。
     * <p/>
     * <pre>
     * indexOfDifference("i am a machine", "i am a robot")   = 7
     * indexOfDifference(null, null)                         = -1
     * indexOfDifference("", null)                           = -1
     * indexOfDifference("", "")                             = -1
     * indexOfDifference("", "abc")                          = 0
     * indexOfDifference("abc", "")                          = 0
     * indexOfDifference("abc", "abc")                       = -1
     * indexOfDifference("ab", "abxyz")                      = 2
     * indexOfDifference("abcde", "abxyz")                   = 2
     * indexOfDifference("abcde", "xyz")                     = 0
     * </pre>
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 两字符串开始产生差异的索引值，如果两字符串相同，则返回<code>-1</code>
     */
    public static int indexOfDifference(String str1, String str2) {
        if (str1 == str2 || str1 == null || str2 == null) {
            return -1;
        }

        int i;

        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }

        if (i < str2.length() || i < str1.length()) {
            return i;
        }

        return -1;
    }

    /**
     * 比较两个字符串，取得第二个字符串中，和第一个字符串不同的部分。
     * <p/>
     * <pre>
     * difference("i am a machine", "i am a robot")  = "robot"
     * difference(null, null)                        = null
     * difference("", "")                            = ""
     * difference("", null)                          = ""
     * difference("", "abc")                         = "abc"
     * difference("abc", "")                         = ""
     * difference("abc", "abc")                      = ""
     * difference("ab", "abxyz")                     = "xyz"
     * difference("abcde", "abxyz")                  = "xyz"
     * difference("abcde", "xyz")                    = "xyz"
     * </pre>
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 第二个字符串中，和第一个字符串不同的部分。如果两个字符串相同，则返回空字符串<code>""</code>
     */
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }

        if (str2 == null) {
            return str1;
        }

        int index = indexOfDifference(str1, str2);

        if (index == -1) {
            return RawConsts.EMPTY_STRING;
        }

        return str2.substring(index);
    }
}
