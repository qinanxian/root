//package com.vekai.office.pageoffice;
//
//
//import com.zhuozhengsoft.base64.Base64;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//
//import org.w3c.dom.Attr;
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//class h {
//    PrintWriter printWriter;
//
//    static String encodeBase64(String text) {
//        try {
//            return Base64.encodeBase64String(text.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    static String decodeBase64(String var0) {
//        byte[] var1 = Base64.decodeBase64(var0);
//        try {
//            return new String(var1, 0, var1.length, "UTF-8");
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    static String c(String var0) {
//        byte[] var3;
//        byte[] var1 = new byte[(var3 = var0.getBytes()).length];
//
//        for(int i = 0; i < var3.length; ++i) {
//            if(i % 2 != 0) {
//                var1[i] = (byte)(var3[i] - 2);
//            } else {
//                var1[i] = (byte)(var3[i] - 3 ^ 73);
//            }
//        }
//
//        return Base64.encodeBase64String(var1);
//    }
//
//    static String d(String var0) {
//        int var1;
//        return (var1 = var0.lastIndexOf(".")) >= 0?var0.substring(var1):"";
//    }
//
//    public h(OutputStream var1) {
//        this.printWriter = new PrintWriter(var1);
//    }
//
//    public void a(Node node) {
//        this.a(node, null, 0);
//    }
//
//    public void a(Node node, int var2) {
//        this.a(node, null, var2);
//    }
//
//    public void a(Node node, String xmlText, int var3) {
//        if(node != null) {
//            xmlText = "";
//            short nodeType;
//            switch(nodeType = node.getNodeType()) {
//                case 1:
//                    this.printWriter.print(xmlText + "<");
//                    this.printWriter.print(node.getNodeName());
//                    Attr[] attrs = a(node.getAttributes());
//
//                    for(int i = 0; i < attrs.length; ++i) {
//                        Attr attr;
//                        if((attr = attrs[i]).getNodeValue().length() > 0) {
//                            this.printWriter.print(' ');
//                            this.printWriter.print(attr.getNodeName());
//                            this.printWriter.print("=\"");
//                            this.printWriter.print(e(attr.getNodeValue()));
//                            this.printWriter.print('\"');
//                        }
//                    }
//
//                    this.printWriter.print('>');
//                    NodeList nodeList;
//                    if((nodeList = node.getChildNodes()) != null) {
//                        int childSize = nodeList.getLength();
//
//                        for(int i = 0; i < childSize; ++i) {
//                            this.a(nodeList.item(i), var3 + 2);
//                        }
//                    }
//                case 2:
//                case 6:
//                case 8:
//                default:
//                    break;
//                case 3:
//                    if(node.getNodeValue().trim().length() > 0) {
//                        this.printWriter.print(xmlText + e(node.getNodeValue()));
//                    }
//                    break;
//                case 4:
//                    this.printWriter.print(xmlText + "<![CDATA[");
//                    this.printWriter.print(node.getNodeValue());
//                    this.printWriter.print("]]>");
//                    break;
//                case 5:
//                    this.printWriter.print('&');
//                    this.printWriter.print(node.getNodeName());
//                    this.printWriter.print(';');
//                    break;
//                case 7:
//                    this.printWriter.print(xmlText + "<?");
//                    this.printWriter.print(node.getNodeName());
//                    String var5;
//                    if((var5 = node.getNodeValue()) != null && var5.length() > 0) {
//                        this.printWriter.print(' ');
//                        this.printWriter.print(var5);
//                    }
//
//                    this.printWriter.print("?>");
//                    break;
//                case 9:
//                    this.printWriter.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//                    this.a(((Document)node).getDocumentElement(), 0);
//                    this.printWriter.flush();
//            }
//
//            if(nodeType == 1) {
//                this.printWriter.print(xmlText + "</");
//                this.printWriter.print(node.getNodeName());
//                this.printWriter.print('>');
//            }
//
//            this.printWriter.flush();
//        }
//    }
//
//    protected static Attr[] a(NamedNodeMap var0) {
//        int nodeSize = var0 != null?var0.getLength():0;
//        int size = nodeSize;
//        Attr[] attrs = new Attr[nodeSize];
//
//        for(int i = 0; i < size; ++i) {
//            attrs[i] = (Attr)var0.item(i);
//        }
//
//        for(int i = 0; i < size - 1; ++i) {
//            String nodeName = attrs[i].getNodeName();
//            int var4 = i;
//
//            for(int j = i + 1; j < size; ++j) {
//                String var6;
//                if((var6 = attrs[j].getNodeName()).compareTo(nodeName) < 0) {
//                    nodeName = var6;
//                    var4 = j;
//                }
//            }
//
//            if(var4 != i) {
//                Attr var8 = attrs[i];
//                attrs[i] = attrs[var4];
//                attrs[var4] = var8;
//            }
//        }
//
//        return attrs;
//    }
//
//    protected static String e(String var0) {
//        StringBuffer var1 = new StringBuffer();
//        int var2 = var0 != null?var0.length():0;
//
//        for(int var3 = 0; var3 < var2; ++var3) {
//            char var4;
//            switch(var4 = var0.charAt(var3)) {
//                case '\"':
//                    var1.append("&quot;");
//                    break;
//                case '&':
//                    var1.append("&amp;");
//                    break;
//                case '<':
//                    var1.append("&lt;");
//                    break;
//                case '>':
//                    var1.append("&gt;");
//                    break;
//                default:
//                    var1.append(var4);
//            }
//        }
//
//        return var1.toString();
//    }
//}
