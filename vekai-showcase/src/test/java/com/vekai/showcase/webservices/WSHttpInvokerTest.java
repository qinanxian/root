//package com.vekai.showcase.webservices;
//
//import com.vekai.runtime.kit.IOKit;
//import com.vekai.runtime.kit.StringKit;
//import org.dom4j.io.SAXReader;
//import org.dom4j.xpath.DefaultXPath;
//import org.junit.Test;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.XMLConstants;
//import javax.xml.namespace.NamespaceContext;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.soap.*;
//import javax.xml.xpath.*;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.Iterator;
//
//public class WSHttpInvokerTest {
//    @Test
//    public void test01() {
//        InputStream is = null;
//        OutputStream os = null;
//        HttpURLConnection conn = null;
//
////服务的地址
//        try {
//            URL wsUrl = new URL("http://home.cnic.com:82/services/UserCodeService");
//            conn = (HttpURLConnection) wsUrl.openConnection();
//
//            conn.setDoInput(true);//需要输出
//            conn.setDoOutput(true);//需要输入
//            conn.setUseCaches(false);   //不允许缓存
//            conn.setRequestMethod("POST");//设置POST方式连接
//            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
//            //设置请求属性
////            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
//            conn.setRequestProperty("Charset", "UTF-8");
//
//            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
////            conn.connect();
//            //getOutputStream()会自动connect
//            os = conn.getOutputStream();
//
//            //请求体
//            String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
//                    "                  xmlns:gs=\"webservices.services.weaver.com.cn\">\n" +
//                    "    <soapenv:Header/>\n" +
//                    "    <soapenv:Body>\n" +
//                    "        <gs:getWkCode>\n" +
//                    "            <gs:in0>03b778a84d1524f6ed8d566e0f1c8448</gs:in0>\n" +
//                    "        </gs:getWkCode>\n" +
//                    "    </soapenv:Body>\n" +
//                    "</soapenv:Envelope>";
//
//            os.write(soap.getBytes());
//            os.flush();
//
//            int resultCode=conn.getResponseCode();
//            is = conn.getInputStream();
//
//
//            //获得响应状态
//            if(HttpURLConnection.HTTP_OK==resultCode){
//                StringBuffer sb=new StringBuffer();
//                String readLine=new String();
//                BufferedReader responseReader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
//                while((readLine=responseReader.readLine())!=null){
//                    sb.append(readLine).append("\n");
//                }
//                responseReader.close();
//                System.out.println(sb.toString());
//                System.out.println(parseJsonText(sb.toString()));
//            }else{
//                System.err.println("出错了:"+resultCode);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//    }
//
//    private String parseJsonText(String text) throws ParserConfigurationException, IOException, SAXException {
//        InputStream inputStream = IOKit.toInputStream(text, Charset.defaultCharset());
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(inputStream);
//
//        System.out.println("处理该文档的DomImplementation对象  = "+ doc.getImplementation());
//
//        Element root = doc.getDocumentElement();    //soap:Envelope
//        Node body = root.getFirstChild();           //soap:Body
//        Node rep = body.getFirstChild();            //rep
//        Node out = rep.getFirstChild();             //out
//
//        return out.getTextContent();
//    }
//
//    @Test
//    public void test02() throws ParserConfigurationException, IOException, SAXException {
//        String text = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Body><ns1:getWkCodeResponse xmlns:ns1=\"webservices.services.weaver.com.cn\"><ns1:out>{&quot;code&quot;:&quot;F&quot;,&quot;msg&quot;:&quot;ticket:000000 &#24050;&#22833;&#25928;&quot;}</ns1:out></ns1:getWkCodeResponse></soap:Body></soap:Envelope>\n";
//        InputStream inputStream = IOKit.toInputStream(text, Charset.defaultCharset());
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(inputStream);
//
//        System.out.println("处理该文档的DomImplementation对象  = "+ doc.getImplementation());
//
//        Element root = doc.getDocumentElement();    //soap:Envelope
//        Node body = root.getFirstChild();           //soap:Body
//        Node rep = body.getFirstChild();            //rep
//        Node out = rep.getFirstChild();             //out
//        System.out.println(out);
//        System.out.println(out.getTextContent());
//        NodeList nList = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Body");
//        System.out.println("--------xpath-------");
//        String namespaceURI = doc.getDocumentURI();
//        System.out.println(doc.getDocumentURI());
//        System.out.println(doc.getNamespaceURI());
//        System.out.println(root.getNamespaceURI());
//
//    }
//    @Test
//    public void test03() throws SOAPException {
//        String text = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Body><ns1:getWkCodeResponse xmlns:ns1=\"webservices.services.weaver.com.cn\"><ns1:out>{&quot;code&quot;:&quot;F&quot;,&quot;msg&quot;:&quot;ticket:000000 &#24050;&#22833;&#25928;&quot;}</ns1:out></ns1:getWkCodeResponse></soap:Body></soap:Envelope>\n";
//        SOAPMessage soapMessage = parseSOAPText(text);
//        SOAPBody body = soapMessage.getSOAPBody();
//        Iterator<SOAPElement> iterator = body.getChildElements();
//        if(iterator.hasNext()){
//            SOAPElement soapElement = iterator.next();
//            System.out.println(soapElement.getTextContent());
//        }
//
//    }
//
//    @Test
//    public void test04() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
//        String text = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Body><ns1:getWkCodeResponse xmlns:ns1=\"webservices.services.weaver.com.cn\"><ns1:out>{&quot;code&quot;:&quot;F&quot;,&quot;msg&quot;:&quot;ticket:000000 &#24050;&#22833;&#25928;&quot;}</ns1:out></ns1:getWkCodeResponse></soap:Body></soap:Envelope>\n";
//        InputStream inputStream = IOKit.toInputStream(text, Charset.defaultCharset());
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(inputStream);
//
//
//        XPath xpath = XPathFactory.newInstance().newXPath();
////        xpath.setNamespaceContext(new UniversalNamespaceResolver(doc));
//        xpath.setNamespaceContext(new NamespaceContext() {
//            public String getNamespaceURI(String prefix) {
//                if (prefix == null) {
//                    throw new IllegalArgumentException("No prefix provided!");
//                } else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
//                    throw new IllegalArgumentException("No prefix provided!");
//                } else if (prefix.equals("soap")) {
//                    return "http://schemas.xmlsoap.org/soap/envelope/";
//                } else if (prefix.equals("ns1")) {
//                    return "webservices.services.weaver.com.cn";
//                } else {
//                    return XMLConstants.NULL_NS_URI;
//                }
//            }
//
//            public String getPrefix(String namespaceURI) {
//                return null;
//            }
//
//            public Iterator getPrefixes(String namespaceURI) {
//                return null;
//            }
//        });
//
////        NodeList result1 = (NodeList) xpath.evaluate("soap:Envelope/soap:Body", doc,XPathConstants.NODE);
////        System.out.println(result1.getLength());
////        System.out.println(xpath.evaluate("soap:Envelope/soap:Body", doc,XPathConstants.NODESET));
////        XPathExpression pathExpression = xpath.compile("//book[author='TEST']/title/text()");//使用XPath对象编译XPath表达式
//        XPathExpression pathExpression = xpath.compile("soap:Envelope/soap:Body");//使用XPath对象编译XPath表达式
//        Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);//计算 XPath 表达式得到结果
//        System.out.println(result);
//        if(result instanceof NodeList){
//            NodeList retNodeList = (NodeList)result;
//            System.out.println(retNodeList.getLength());
//        }
////        xpath.setNamespaceURIs(Collections.singletonMap("ns1","http://bank.services.applications.dykj.com"));
//    }
//
//    private static SOAPMessage parseSOAPText(String soapString) {
//        MessageFactory messageFactory;
//        try {
//            messageFactory = MessageFactory.newInstance();
//            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),new ByteArrayInputStream(soapString.getBytes()));
//            soapMessage.saveChanges();
//            return soapMessage;
//        } catch (SOAPException e) {
//            throw new RuntimeException("解析SOAP协议出错",e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("解析SOAP协议出错,字符集出错",e);
//        } catch (IOException e) {
//            throw new RuntimeException("解析SOAP协议出错,字符集出错",e);
//        }
//    }
//
//
//}
