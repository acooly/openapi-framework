/**
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package com.yiji.framework.openapi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;


/**
 * @Filename: IPUtil.java
 * @Description:
 * @Version: 1.0
 * @Author: peigen
 * @Email: peigen@yiji.com
 * @History:<br> <li>Author: peigen</li>
 * <li>Date: 2011-7-28</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 * <p>
 * copy from yiji on 2016
 */
public class IPUtil {
    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);
    private static final String LOOP_BACK = "127.0.0.1";
    private static String firstNoLoopbackAddress = null;
    private static String firstNoLoopbackIPV4Address = null;
    private static String firstNoLoopbackIPV6Address = null;
    private static Collection<InetAddress> allHostIPV4Address = null;
    private static Collection<InetAddress> allHostIPV6Address = null;
    private static Collection<InetAddress> allHostAddress = null;

    private static String macAddress = null;

    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String UNKNOWN = "unknown";

    /**
     * 将IP地址(61.172.201.235)转变成Long，如果ip格式非法，那么返回0
     *
     * @param ip
     * @return
     */
    public static long ip2Long(String ip) {
        if (!isIP(ip)) {
            return 0;
        }

        long iplong = 0;
        String[] segs = Strings.split(ip, ".");

        for (int i = 0; i < segs.length; i++) {
            long seg = Long.parseLong(segs[i]);
            iplong += seg << ((3 - i) * 8);
        }

        return iplong;
    }

    /**
     * 将数据库中表示IP的Long型，转变成标准形式（61.172.201.235）
     *
     * @param ipLong
     * @return
     */
    public static String long2IP(long ipLong) {

        StringBuffer ip = new StringBuffer(String.valueOf(ipLong >> 24) + ".");

        ip.append(String.valueOf((ipLong & 16711680) >> 16) + ".");
        ip.append(String.valueOf((ipLong & 65280) >> 8) + ".");
        ip.append(String.valueOf(ipLong & 255));

        return ip.toString();
    }

    /**
     * 判断ip是否是公网ip
     */
    public static boolean isPublicIpv4(String ip) {
        if (Strings.isBlank(ip))
            return false;
        long ipL = ip2Long(ip);
        if (ipL == 0)
            return false;
        return isPublicIpv4(ipL);
    }

    public static boolean isPublicIpv4(long ip) {
        // 判断是否是ipv4，并且是否为0.0.0.0 或者 255.255.255.255
        if (ip <= 0 || ip >= 4294967295L)
            return false;
        // 判断是否本地回环地址 127.0.0.0 ~ 127.255.255.255
        if (ip >= 2130706432L && ip <= 2147483647L)
            return false;
        // 判断是否在10.0.0.0 ~ 10.255.255.255之间
        if (ip >= 167772160L && ip <= 184549375L)
            return false;
        // 判断是否在172.16.0.0 ~ 172.31.255.255之间
        if (ip >= 2886729728L && ip <= 2887778303L)
            return false;
        // 判断是否在192.168.0.0 ~ 192.168.255.255之间
        return !(ip >= 3232235520L && ip <= 3232301055L);
    }

    /**
     * 判断字符是否是一个表示IP的字符
     *
     * @param str
     * @return
     */
    public static boolean isIP(String str) {
        if (str == null) {
            return false;
        }
        String[] tokens = Strings.split(str, ".");
        if (tokens.length != 4) {
            return false;
        }

        for (int i = 0; i < tokens.length; i++) {
            if (Integer.parseInt(tokens[i]) > 255 || Integer.parseInt(tokens[i]) < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取所有本机ip地址
     *
     * @return
     */
    public static Collection<InetAddress> getAllHostAddress() {
        if (allHostAddress == null) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                Collection<InetAddress> addresses = new ArrayList<InetAddress>();

                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        addresses.add(inetAddress);
                    }
                }
                allHostAddress = addresses;
            } catch (SocketException e) {
                logger.error("获取ip地址失败", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return allHostAddress;
    }

    /**
     * 获取所有本机ipv4地址
     *
     * @return
     */
    public static Collection<InetAddress> getAllHostIPV4Address() {
        if (allHostIPV4Address == null) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                Collection<InetAddress> addresses = new ArrayList<InetAddress>();

                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet4Address) {
                            addresses.add(inetAddress);
                        }
                    }
                }
                allHostIPV4Address = addresses;
            } catch (SocketException e) {
                logger.error("获取ip地址失败", e);
                throw new RuntimeException(e.getMessage(), e);
            }

        }
        return allHostIPV4Address;
    }

    /**
     * 获取所有本机ipv6地址
     *
     * @return
     */
    public static Collection<InetAddress> getAllHostIPV6Address() {
        if (allHostIPV6Address == null) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                Collection<InetAddress> addresses = new ArrayList<InetAddress>();

                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet6Address) {
                            addresses.add(inetAddress);
                        }
                    }
                }
                allHostIPV6Address = addresses;
            } catch (SocketException e) {
                logger.error("获取ip地址失败", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return allHostIPV6Address;
    }

    /**
     * 获取所有本机非loopback地址
     *
     * @return
     */
    public static Collection<String> getAllNoLoopbackAddresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostAddress();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    /**
     * 获取所有本机非loopback IPV4地址
     *
     * @return
     */
    public static Collection<String> getAllNoLoopbackIPV4Addresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostIPV4Address();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    /**
     * 获取所有本机非loopback IPV6地址
     *
     * @return
     */
    public static Collection<String> getAllNoLoopbackIPV6Addresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostIPV6Address();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    /**
     * 获取ip地址，如果有多个网卡的情况，获取第一个非loopback ip地址
     *
     * @return
     */
    public static String getFirstNoLoopbackAddress() {
        if (firstNoLoopbackAddress != null) {
            return firstNoLoopbackAddress;
        }
        Collection<String> allNoLoopbackAddresses = null;
        try {
            allNoLoopbackAddresses = getAllNoLoopbackAddresses();
        } catch (Exception e) {
            logger.error("获取ip失败", e);
            return LOOP_BACK;
        }
        if (allNoLoopbackAddresses.isEmpty()) {
            return LOOP_BACK;
        }

        return firstNoLoopbackAddress = allNoLoopbackAddresses.iterator().next();
    }

    /**
     * 获取ipv4地址，如果有多个网卡的情况，获取第一个非loopback ip地址
     *
     * @return
     */
    public static String getFirstNoLoopbackIPV4Address() {
        if (firstNoLoopbackIPV4Address != null) {
            return firstNoLoopbackIPV4Address;
        }
        Collection<String> allNoLoopbackAddresses = null;
        try {
            allNoLoopbackAddresses = getAllNoLoopbackIPV4Addresses();
        } catch (Exception e) {
            logger.error("获取ip失败", e);
            return LOOP_BACK;
        }
        if (allNoLoopbackAddresses.isEmpty()) {
            return LOOP_BACK;
        }

        return firstNoLoopbackIPV4Address = allNoLoopbackAddresses.iterator().next();
    }

    /**
     * 获取ipv6地址，如果有多个网卡的情况，获取第一个非loopback ip地址
     *
     * @return
     */
    public static String getFirstNoLoopbackIPV6Address() {
        if (firstNoLoopbackIPV6Address != null) {
            return firstNoLoopbackIPV6Address;
        }
        Collection<String> allNoLoopbackAddresses = null;
        try {
            allNoLoopbackAddresses = getAllNoLoopbackIPV6Addresses();
        } catch (Exception e) {
            logger.error("获取ip失败", e);
            return LOOP_BACK;
        }
        if (allNoLoopbackAddresses.isEmpty()) {
            return LOOP_BACK;
        }

        return firstNoLoopbackIPV6Address = allNoLoopbackAddresses.iterator().next();
    }

    /**
     * 获取机器名
     *
     * @return
     */
    public static String getComputerName() {
        return System.getenv().get("COMPUTERNAME");
    }

    /**
     * 获取mac地址
     *
     * @return
     * @throws Exception
     */
    public static String getMACAddress() throws Exception {
        if (macAddress == null) {
            macAddress = getMacByNetworkInterface();
            if (macAddress == null) {
                macAddress = getMacByProcess();
            }
        }
        return macAddress;
    }

    /**
     * 获取hostname
     *
     * @return
     * @throws Exception
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 获取http请求真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取mac地址
     *
     * @param ia
     * @return
     */
    public static String getMACAddress(InetAddress ia) {
        byte[] mac = null;
        try {
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (SocketException e) {
            throw Exceptions.unchecked(e);
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().toUpperCase();
    }

    private static String formatMac(byte[] mac) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().toUpperCase();
    }

    private static String getMacByNetworkInterface() {
        String out = null;
        try {
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
            if (ifs != null) {
                while (ifs.hasMoreElements()) {
                    NetworkInterface iface = ifs.nextElement();
                    byte[] hardware = iface.getHardwareAddress();
                    if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
                        out = formatMac(hardware);
                        break;
                    }
                }
            }
        } catch (SocketException ex) {
            // Ignore it.
        }
        return out;
    }

    private static String getMacByProcess() {
        String macA = null;
        Process p = null;
        BufferedReader in = null;

        try {
            String osname = System.getProperty("os.name", ""), osver = System.getProperty("os.version", "");

            if (osname.startsWith("Windows")) {
                p = Runtime.getRuntime().exec(new String[]{"ipconfig", "/all"}, null);
            }

            // Solaris code must appear before the generic code
            else if (osname.startsWith("Solaris") || osname.startsWith("SunOS")) {
                if (osver.startsWith("5.11")) {
                    p = Runtime.getRuntime().exec(new String[]{"dladm", "show-phys", "-m"}, null);
                } else {
                    String hostName = getFirstLineOfCommand("uname", "-n");
                    if (hostName != null) {
                        p = Runtime.getRuntime().exec(new String[]{"/usr/sbin/arp", hostName}, null);
                    }
                }
            } else if (new File("/usr/sbin/lanscan").exists()) {
                p = Runtime.getRuntime().exec(new String[]{"/usr/sbin/lanscan"}, null);
            } else if (new File("/sbin/ifconfig").exists()) {
                p = Runtime.getRuntime().exec(new String[]{"/sbin/ifconfig", "-a"}, null);
            }

            if (p != null) {
                in = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
                String l = null;
                while ((l = in.readLine()) != null) {
                    macA = MACAddressParser.parse(l);
                    if (macA != null && Hex.parseShort(macA) != 0xff) {
                        break;
                    }
                }
            }

        } catch (SecurityException ex) {
            // Ignore it.
        } catch (IOException ex) {
            // Ignore it.
        } finally {
            if (p != null) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        // Ignore it.
                    }
                }
                try {
                    p.getErrorStream().close();
                } catch (IOException ex) {
                    // Ignore it.
                }
                try {
                    p.getOutputStream().close();
                } catch (IOException ex) {
                    // Ignore it.
                }
                p.destroy();
            }
        }
        if (macA != null) {
            macA = macA.toUpperCase();
        }
        return macA;
    }

    static String getFirstLineOfCommand(String... commands) throws IOException {

        Process p = null;
        BufferedReader reader = null;

        try {
            p = Runtime.getRuntime().exec(commands);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);

            return reader.readLine();
        } finally {
            if (p != null) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        // Ignore it.
                    }
                }
                try {
                    p.getErrorStream().close();
                } catch (IOException ex) {
                    // Ignore it.
                }
                try {
                    p.getOutputStream().close();
                } catch (IOException ex) {
                    // Ignore it.
                }
                p.destroy();
            }
        }

    }

}
