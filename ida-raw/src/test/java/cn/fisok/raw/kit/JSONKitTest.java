package cn.fisok.raw.kit;

public class JSONKitTest {
    public static void main(String[] args){
        System.out.println(JSONKit.toJsonString("123"));
        System.out.println(JSONKit.toJsonString(null));
        System.out.println(JSONKit.toJsonString(100));
        System.out.println(JSONKit.toJsonString(100.12));
        System.out.println(JSONKit.toJsonString(DateKit.now()));
    }
}
