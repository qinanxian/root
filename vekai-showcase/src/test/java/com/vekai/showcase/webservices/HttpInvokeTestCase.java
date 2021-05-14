package com.vekai.showcase.webservices;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpInvokeTestCase {
    @Test
    public void test01() {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;

//服务的地址
        try {
            URL wsUrl = new URL("http://localhost:8080/amix/wsdemo");

            conn = (HttpURLConnection) wsUrl.openConnection();

            conn.setDoInput(true);//需要输出
            conn.setDoOutput(true);//需要输入
            conn.setUseCaches(false);   //不允许缓存
            conn.setRequestMethod("POST");//设置POST方式连接
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            //设置请求属性
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");

            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
//            conn.connect();
            //getOutputStream()会自动connect
            os = conn.getOutputStream();

            //请求体
            String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "                  xmlns:gs=\"http://wind.com/webservices/service\">\n" +
                    "    <soapenv:Header/>\n" +
                    "    <soapenv:Body>\n" +
                    "        <gs:getCountryRequest>\n" +
                    "            <gs:name>Spain</gs:name>\n" +
                    "        </gs:getCountryRequest>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            os.write(soap.getBytes());
            os.flush();

            int resultCode=conn.getResponseCode();
            is = conn.getInputStream();


            byte[] b = new byte[1024];
            int len = 0;
            String s = "";
            while ((len = is.read(b)) != -1) {
                String ss = new String(b, 0, len, "UTF-8");
                s += ss;
            }
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}
