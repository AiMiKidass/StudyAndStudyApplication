package alk.study.app.mineutil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class KidUtils {

	public static String getDateNow() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",Locale.getDefault());
		String strDate = dateformat.format(new Date());
		return strDate;
	}

	public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager.getConnectionInfo();
        if(null != wifiinfo) {
            return wifiinfo.getMacAddress().replace(":", "");
        }
        return "";
	}

	public static String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager.getConnectionInfo();
        if(null != wifiinfo) {
            return intToIp(wifiinfo.getIpAddress());
        }
        return "";
	}
	
	private static String intToIp(int i) {       
		return (i & 0xFF) + "." 
				+ ((i >> 8) & 0xFF) + "." 
				+ ((i >> 16) & 0xFF)+ "." 
				+ (i >> 24 & 0xFF); 
   }  
	
	/**
	 * 去掉字符串中的 空格、回车、换行符、制表符等空白符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");// 空格、回车、换行符、制表符
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}
	
	public static int type = 0;	// 表示当前连接类型

	/*********
	 * 获得Ip地址****
	 * 
	 * *********/
	public static String getLocalIpAddress() {
	    String wifiIpaddress = null;
	    String ethIpaddress = null;
	    String ipaddress = null;

        try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
			        .getNetworkInterfaces(); en.hasMoreElements();) {
			    NetworkInterface intf = en.nextElement();
			    for (Enumeration<InetAddress> enumIpAddr = intf
			            .getInetAddresses(); enumIpAddr.hasMoreElements();) {
			        InetAddress inetAddress = enumIpAddr.nextElement();
			        if (!inetAddress.isLoopbackAddress()) {
			            if(inetAddress instanceof Inet4Address) {
			                    if(intf.getName().equalsIgnoreCase("wlan0")){
			                    	wifiIpaddress = inetAddress.getHostAddress().toString();
			                    }
			                    if(intf.getName().equalsIgnoreCase("eth0")){
			                    	ethIpaddress = inetAddress.getHostAddress().toString();
			                    }
			                }
			                
			            }
			        }
			    }
		} catch (SocketException e) {
			e.printStackTrace();
		}
        
        
        if(!TextUtils.isEmpty(ethIpaddress)){
        	ipaddress =	ethIpaddress;
        	type = 0;
        }
        if(!TextUtils.isEmpty(wifiIpaddress)){
        	ipaddress =	wifiIpaddress;
        	type = 1;
        }
        return ipaddress;
    }

	/**
	 * 获得MAC地址
	 * @param type 0:wifi 1:ethernet
	 * @return
	 */
	public static String getMacAddressByCmdLine() {
	    String strMac = null;
	    String str = "";
	    try {
	        if(type < 2) {
    	        String strCmd = "cat /sys/class/net/wlan0/address ";
    	        if(type == 0)
    	            strCmd = "cat /sys/class/net/eth0/address ";
                Process pp = Runtime.getRuntime().exec(strCmd);
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
    
                for (; null != str;) {
                    str = input.readLine();
                    if (str != null) {
                        strMac = str.trim();// 去空格
                        break;
                    }
                }
	        }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
	    return strMac;
	}
	
	
}
