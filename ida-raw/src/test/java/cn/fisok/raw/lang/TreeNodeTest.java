//package cn.fisok.raw.lang;
//
//
//import com.vekai.runtime.kit.JSONKit;
//import com.vekai.runtime.kit.ListKit;
//import com.vekai.runtime.kit.TreeNodeKit;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TreeNodeTest {
//
//    @Test
//    public void buildTreeTest(){
//        List<NodeObject> nodeList = new ArrayList<NodeObject>();
//
//        nodeList.add(new NodeObject("11","11"));
//        nodeList.add(new NodeObject("111","111"));
//        nodeList.add(new NodeObject("1111","1111"));
//        nodeList.add(new NodeObject("1112","1112"));
//        nodeList.add(new NodeObject("112","112"));
//        nodeList.add(new NodeObject("1121","1121"));
//        nodeList.add(new NodeObject("1122","1122"));
//        nodeList.add(new NodeObject("1123","1123"));
//        nodeList.add(new NodeObject("11231","11231"));
//        nodeList.add(new NodeObject("11-1","11-1"));
//        nodeList.add(new NodeObject("11-11","11-11"));
//        nodeList.add(new NodeObject("11-12","11-12"));
//        nodeList.add(new NodeObject("/","/"));
//        nodeList.add(new NodeObject("/home","/home"));
//        nodeList.add(new NodeObject("/users"));
//        nodeList.add(new NodeObject("/Applications"));
//        nodeList.add(new NodeObject("/opt"));
//        nodeList.add(new NodeObject("/opt/java"));
//        nodeList.add(new NodeObject("/opt/java/jdk-1.7"));
//        nodeList.add(new NodeObject("/opt/java/jdk-1.8"));
//
//        List<TreeNodeWrapper<NodeObject>> nodeWrapperList = ListKit.newArrayList();
//        for(NodeObject nodeItem : nodeList){
//            nodeWrapperList.add(new TreeNodeWrapper<NodeObject>(null,nodeItem));
//        }
//
//        List<TreeNodeWrapper<NodeObject>> rootList = TreeNodeKit.buildTreeBranches(nodeWrapperList, nodeObject -> nodeObject.getSortCode());
//        System.out.println(JSONKit.toJsonString(rootList));
//        System.out.println(JSONKit.toJsonString(TreeNodeKit.buildWrapperTree(nodeList, nodeObject -> nodeObject.getSortCode())));
//    }
//
//    public static class NodeObject{
//        private String name;
//        private String value;
//        private String sortCode;
//
//        public NodeObject(String sortCode) {
//            this.sortCode = sortCode;
//        }
//
//        public NodeObject(String sortCode, String name) {
//            this.name = name;
//            this.sortCode = sortCode;
//        }
//
//        public NodeObject(String name, String value, String sortCode) {
//            this.name = name;
//            this.value = value;
//            this.sortCode = sortCode;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public String getSortCode() {
//            return sortCode;
//        }
//
//        public void setSortCode(String sortCode) {
//            this.sortCode = sortCode;
//        }
//    }
//}
