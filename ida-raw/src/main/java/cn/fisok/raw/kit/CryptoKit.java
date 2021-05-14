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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:48
 * @desc :
 */
public abstract class CryptoKit {
    protected static Logger logger = LoggerFactory.getLogger(CryptoKit.class);
    public static final String KEY_MD5 = "MD5";


    public static final String getMD5(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        String md5 = getMD5(inputStream);
        IOKit.close(inputStream);
        return md5;
    }

    public static final String getMD5(File file) {
        String fileMD5 = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileMD5 = CryptoKit.getMD5(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            IOKit.close(fileInputStream);
        }
        return fileMD5;
    }

    public static final String getMD5(InputStream inputStream) {

        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance(KEY_MD5);
            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }

    public static String byteArrayToHex(byte[] byteArray) {
        //首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[byteArray.length * 2];
        //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        //字符数组组合成字符串返回
        return new String(resultCharArray);
    }
}