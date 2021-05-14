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


import cn.fisok.raw.lang.TreeNode;

import java.util.*;
import java.util.function.Function;


/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/01/14
 * @desc : 树图节点构建器
 */
public abstract class TreeNodeKit {

    private static final String DEFAULT_SPLITTER = "";


    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodeList, Function<T, String> sortCodeFunc) {
        return buildTree(nodeList, sortCodeFunc, DEFAULT_SPLITTER);
    }

    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodeList, Function<T, String> sortCodeFunc, String splitter) {
        TreeBuilder<T> builder = new TreeBuilder<>(nodeList, sortCodeFunc, splitter);
        return builder.build();
    }


    private static class TreeBuilder<T extends TreeNode<T>>{
        private List<T> nodeList = null;
        private Function<T, String> sortCodeFunc;
        private String splitter;

        public TreeBuilder(List<T> nodeList, Function<T, String> sortCodeFunc, String splitter) {
            this.nodeList = nodeList;
            this.sortCodeFunc = sortCodeFunc;
            this.splitter = splitter;
            ensureSplitterAscii();
        }

        private void ensureSplitterAscii() {
            if (splitter.compareTo(Objects.toString(0L)) > 0)
                throw new IllegalArgumentException("splitter must be order before 0");
        }


        /**
         * 对树图节点排序
         */
        public void sort(){
            nodeList.sort(new Comparator<T>() {
                public int compare(T o1, T o2) {
                    String sv1 = sortCodeFunc.apply(o1);
                    String sv2 = sortCodeFunc.apply(o2);
                    return sv1.compareTo(sv2);
                }
            });
        }

        private boolean isChild(String parentSortCode, String childSortCode) {
            return parentSortCode.length() < childSortCode.length()
                    && (parentSortCode.endsWith(splitter) ? childSortCode.startsWith(parentSortCode) :
                    childSortCode.startsWith(parentSortCode + splitter));
        }

        private boolean isChild(T parent, T child) {
            return null != parent &&
                    isChild(sortCodeFunc.apply(parent), sortCodeFunc.apply(child));
        }

        /**
         * 搜索第一层节点（根节点）可能是一个，也有可能是多个(输入的数据，必需保证事先排序过）
         *
         * @return list
         */
        public List<T> getRootBranches(){
            List<T> rootList = new ArrayList<>();
            Set<T> used = new HashSet<T>();
            for(int i=0;i<nodeList.size();i++){
                T node1 = nodeList.get(i);
                if(used.contains(node1))continue;

                for(int j=i+1;j<nodeList.size();j++){
                    T node2 = nodeList.get(j);
                    if(used.contains(node2))continue;

                    String sort1 = sortCodeFunc.apply(node1);
                    String sort2 = sortCodeFunc.apply(node2);
                    if (isChild(sort1, sort2)){
                        if(!rootList.contains(node1))rootList.add(node1);
                        used.add(node2);
                        used.add(node1);
                    }
                }
                //可能的子节点都找过了，但还是找不到任何一个子节点，那么，他也是根
                if(!used.contains(node1)){
                    rootList.add(node1);
                }
            }
            return rootList;
        }

        public List<T> build() {
            sort();
            List<T> rootList = new ArrayList<>();
            Iterator<T> nodeIterator = nodeList.iterator();
            T lastNode = null;
            while (nodeIterator.hasNext()) {
                T node = nodeIterator.next();
                while (null != lastNode) {
                    if (isChild(lastNode, node)) {
                        lastNode.addChild(node);
                        lastNode = node;
                        break;
                    }
                    lastNode = lastNode.getParent();
                }
                if (null == lastNode) {
                    if (!rootList.contains(node)) rootList.add(node);
                    lastNode = node;
                }
            }
            return rootList;
        }

        public boolean hasChildren(T node, List<T> nodeList){
            int startIdx = nodeList.indexOf(node);
            if(startIdx<0)return false;
            String sort1 = sortCodeFunc.apply(node);
            for(int i=startIdx+1;i<nodeList.size();i++){
                T node2 = nodeList.get(i);
                String sort2 = sortCodeFunc.apply(node2);
                if (isChild(sort1, sort2)) return true;
            }
            return false;
        }

        /**
         * 构建树节点（(输入的数据，必需保证事先排序过）
         *
         * @param node node
         * @param nodeList nodeList
         * @param used used
         */
        public void buildChildren(T node, List<T> nodeList, Set<T> used){
            int startIdx = nodeList.indexOf(node);
            if(startIdx<0)return;
            String sort1 = sortCodeFunc.apply(node);

            for(int i=startIdx+1;i<nodeList.size();i++){
                T child = nodeList.get(i);
                String sort2 = sortCodeFunc.apply(child);
                if(hasChildren(child,nodeList)){
                    List<T> nodeLists=new ArrayList<>();
                    String sort1s = sortCodeFunc.apply(node);
                    for(int is=startIdx+1;is<nodeList.size();is++){
                        T nodeItem = nodeList.get(is);
                        String sort2s = sortCodeFunc.apply(nodeItem);
                        if (isChild(sort1s, sort2s)){
                            nodeLists.add(nodeList.get(is));
                        }
                    }
                    buildChildren(child,nodeLists,used);
                }
                if (isChild(sort1, sort2)){
                    if(!used.contains(child)){
                        node.addChild(child);
                    }
                    used.add(child);
                }
            }
        }

    }
}
