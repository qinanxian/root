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

import cn.fisok.raw.lang.RawException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:23
 * @desc :
 */
public abstract class NetKit {

    public static InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String getHostAddress(){
        try{
            return getLocalHost().getHostAddress();
        }catch(UnknownHostException e){
            throw new RawException("",e);
        }
    }

    public static String getHostName(){
        try{
            return getLocalHost().getHostName();
        }catch(UnknownHostException e){
            throw new RawException("",e);
        }
    }


    /**
     * 获取机器编码
     * @return
     */
    public static long getMachineNum(){
        long machinePiece;
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RawException("",e);
        }
        while (enumeration.hasMoreElements()) {
            NetworkInterface ni = enumeration.nextElement();
            sb.append(ni.toString());
        }
        machinePiece = sb.toString().hashCode();
        return machinePiece;
    }

    /**
     * 获取所有的网卡信息
     *
     * @return list
     * @throws SocketException SocketException
     */
    public static List<InetAddress> getInetAddressList() throws SocketException {
        List<InetAddress> retList = new ArrayList<>();

        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()){
            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
            Enumeration address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress) address.nextElement();
                String netAdpterName = "未知网卡";
                String netIp = null;
                // 外网IP
                if (!inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
                    netAdpterName = "外网-地址";
                    netIp = inetAddress.getHostAddress();
                    // 内网IP
                } else if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
                    netAdpterName = "内网-地址";
                    netIp = inetAddress.getHostAddress();
                }
                if(netIp == null)continue;

                retList.add(inetAddress);
            }
        }

        return retList;

    }
}
