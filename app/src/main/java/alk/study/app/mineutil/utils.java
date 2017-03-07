package alk.study.app.mineutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author viennetta
 * 
 */
public final class Utils {
	private static final String TAG = "Utils";
	
	private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;
	
	private static long[] sCrcTable = new long[256];
	

	public static void disableGuidanceActivity(Context applicationContext) {
		SharedPreferences sp = applicationContext.getSharedPreferences(
				"lekandata", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("lekanGuidance", true);
		editor.commit();
	}


	public static void setUserPlayerTime(Context appContext, long uid, long time) {
	    SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(String.valueOf(uid), time);
        editor.commit();
	}
	
	public static long getUserPlayerTime(Context appContext, long uid) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        long time = sp.getLong(String.valueOf(uid), 0);
        return time;
    }
	
	public static void clearUserPlayerTime(Context appContext) {
	    SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sp.edit();
	    editor.clear();
	    editor.commit();
	}
	
	public static void savePlayerTimer(Context appContext, long timerLeft) {
	    SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timerleft", timerLeft);
        editor.commit();
	}
	
	public static long getPlayerTimer(Context appContext) {
	    SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        long timerLeft = sp.getLong("timerleft", 0);
        return timerLeft;
    }
	
	public static void savePlayerTimerConfig(Context appContext, long timerConfig) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timerconfig", timerConfig);
        editor.commit();
    }
	
	public static long getPlayerTimerConfig(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        long timerConfig = sp.getLong("timerconfig", 0);
        return timerConfig;
    }
	
	public static void savePlayerTimerDate(Context appContext, String strDate) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("timerDate", strDate);
        editor.commit();
    }
    
    public static String getPlayerTimerDate(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        String timerDate = sp.getString("timerDate", "2015-01-01");
        return timerDate;
    }
	
    public static long getPlayerTimerUserId(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        long userId = sp.getLong("userId", -1);
        return userId;
    }
    
    public static void savePlayerTimerUserId(Context appContext, long userId) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "playertimer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("userId", userId);
        editor.commit();
    }
    
    public static String getMessageReadTime(Context appContext, String strId) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "messageLog", Context.MODE_PRIVATE);
        String timerDate = sp.getString(strId, "1997-01-01 00:00:00");
        return timerDate;
    }
    
    public static void setMessageInfo(Context appContext, String strId, String strDate) {
        SharedPreferences sp = appContext.getSharedPreferences(
                "messageLog", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(strId, strDate);
        editor.commit();
    }
    
    public static String getCurrentDateStringFormat() {
        long current = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date(current);
        String dateFormat = format.format(date);
        return dateFormat;
    }
    
	public static String getFormatTime(long time) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd/HH:mm:ss");
	    return dateFormat.format(new Date(time));
	}

	/**
	 * 获取设备mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String strMac = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (null != wifiManager) {
			WifiInfo wifiinfo = wifiManager.getConnectionInfo();
			String macAddress = wifiinfo.getMacAddress();
			if (macAddress != null && !macAddress.equals("")) {
				strMac = macAddress.replace(":", "");
			}
		}
		if(strMac == null || 
		        (strMac != null && 
		            (strMac.equalsIgnoreCase("") || 
		                strMac.equalsIgnoreCase("000000000000")))) {
		    strMac = getMacAddressByCmdLine();
		}
		return strMac;
	}

	public static String getLocalIpAddress() {
		String ipaddress = "";

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						if (inetAddress instanceof Inet4Address) {
							if (intf.getName().equalsIgnoreCase("wlan0"))
								ipaddress = inetAddress.getHostAddress()
										.toString();
						}
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("UtilsInfo", ex.toString());
		}
		if (ipaddress == null)
			ipaddress = "";
		return ipaddress;
	}

	/**
	 * 判断网络连接是否正常
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean bNetworkAvailable = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected()) {
			bNetworkAvailable = true;
		}
		return bNetworkAvailable;
	}

	public static float getDensity(Context con) {
		return con.getResources().getDisplayMetrics().density;
	}

	public static int getWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 彻底保证handler停止,用了静态化
	 */
	static AnimationDrawable mAnimDrawable;
	static Runnable progressRunnable = new Runnable() {
		@Override
		public void run() {
			if (mAnimDrawable != null) {
				mAnimDrawable.start();
			}
		}
	};

	public static void startAnimation(Handler handler,
			final AnimationDrawable drawable) {
		if (handler == null || drawable == null) {
			return;
		}

		mAnimDrawable = drawable;

		try {
			handler.postDelayed(progressRunnable, 1500);
		} catch (Exception e) {
		}
	}

	public static void cancleAnimation(Handler handler) {
		if (mAnimDrawable != null) {
			mAnimDrawable.stop();
		}
		if (handler != null) {
			handler.removeCallbacks(mAnimDrawable);
		}
	}

	public static void cancleAnimation(AnimationDrawable hprogressBar) {
		if (hprogressBar != null) {
			hprogressBar.stop();
		}

	}

	public static void setAnimation(final AnimationDrawable hprogressBar) {

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (hprogressBar != null) {
					hprogressBar.start();
				}
			}
		}, 100);
	}

	public static void setQuickAnimation(final AnimationDrawable hprogressBar) {

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (hprogressBar != null) {
					hprogressBar.start();
				}
			}
		}, 1000);
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

	/**
	 * // * 开启线程池 // * @param str // * @return //
	 */
	//
	// 线程池中线程最大个数
	private static final int THREAD_MAX_NUMBER = 15;
	// 采用线程池来管理线程
	private static ExecutorService executorService = Executors
			.newFixedThreadPool(THREAD_MAX_NUMBER);

	public static ExecutorService getPool() {
		return executorService;
	}

	/**
	 * 获取未安装的APK信息
	 * 
	 * @param archiveFilePath
	 *            APK文件的路径�?如：/sdcard/download/XX.apk
	 * @return
	 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}

	/**
	 * 避免按键的连续点击
	 * 
	 * @param con
	 */
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) { // 原来是1000 改成2000
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 检测是否是wifi联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkWifiConnection(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isAvailable())
			return true;
		else
			return false;
	}


	/**
	 * 获得当前是星期几和小时的整数
	 * 
	 * @return
	 */
	public static String getDayAndHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周几
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			day = 7;

		}
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour > 9) {
			return day + "," + hour;
		} else {
			return day + ",0" + hour;
		}
	}


	public static int getTextHeight(TextView view) {
        int height = 0;
        if(view != null) {
            FontMetrics fm = view.getPaint().getFontMetrics();
            if(fm != null) {
                Log.d(TAG, "getTextHeight: top="+fm.top+", ascent="+
                        fm.ascent+", descent="+fm.descent+", bottom="+fm.bottom);
                height = (int)Math.ceil(fm.descent - fm.ascent);
                //height = (int)Math.ceil(fm.bottom - fm.top);
                fm = null;
            }
        }
        return height;
    }
	
	public static int getTextPaddingTop(TextView view) {
        int paddingTop = 0;
        if(view != null) {
            FontMetrics fm = view.getPaint().getFontMetrics();
            if(fm != null) {
                Log.d(TAG, "getTextHeight: top="+fm.top+", ascent="+
                        fm.ascent+", descent="+fm.descent+", bottom="+fm.bottom);
                paddingTop = (int)Math.ceil(fm.ascent - fm.top);
                //height = (int)Math.ceil(fm.bottom - fm.top);
                fm = null;
            }
        }
        return paddingTop;
    }
	

	public static CharSequence getMeasuredCharSequence(String strText, TextPaint tPaint, float width) {
	    CharSequence sequence = strText;
	    if(tPaint != null && !TextUtils.isEmpty(strText)) {
	        float textWidth = tPaint.measureText(strText);
	        if(textWidth != width)
	            sequence = TextUtils.ellipsize(strText, 
	                    tPaint, width, TextUtils.TruncateAt.END);
	    }
	    return sequence;
	}
	
	public static String getUrlAddUserId(String url) {
		String path = "";
		if (url != null && !url.equals("")) {
			path = getOpenWebUrl(url);
		}
		return path;
	}

	private static String getOpenWebUrl(String url) {
		int startPos = url.indexOf("=");
		int endPos = url.indexOf("&");
		String openUrl = url;
		if(startPos > -1 && endPos > startPos)
		    url.substring(startPos, endPos);
		return openUrl;
	}

	/**
	 * 当前星期 WEEKDAY
	 * @return
	 */
	public static int getCurrentDay() {
		String dayAndHour = getDayAndHour();
		int weekday = Integer.parseInt(dayAndHour.substring(0, 1));
		return weekday;
	}

	/**
	 * 当前小时 HOUR
	 * @return
	 */
	public static int getCurrentHour() {
		String dayAndHour = getDayAndHour();
		int hour = Integer.parseInt(dayAndHour.substring(2, 4));
		return hour;
	}
	
	/**
     * 获取bitmap的字节大小
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
	
    public static byte[] makeKey(String httpUrl) {
        return getBytes(httpUrl);
    }
    
    /**
     * A function thats returns a 64-bit crc for string
     *
     * @param in input string
     * @return a 64-bit crc value
     */
    public static final long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(in));
    }
    
    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }
    
    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];
        int output = 0;
        for (char ch : in.toCharArray()) {
            result[output++] = (byte) (ch & 0xFF);
            result[output++] = (byte) (ch >> 8);
        }
        return result;
    }
    
    public static boolean isSameKey(byte[] key, byte[] buffer) {
        int n = key.length;
        if (buffer.length < n) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (key[i] != buffer[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取可以使用的缓存目录
     * @param context
     * @param uniqueName 目录名称
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? 
                        getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
    
    /**
     * 获取程序外部的缓存目录
     * @param context
     * @return
     */
     public static File getExternalCacheDir(Context context) {
         final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
         return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
     }
    
	private static String getMacAddressByCmdLine() {
        String strMac = null;
        String str = "";
        try {
            String strCmd = "cat /sys/class/net/wlan0/address ";
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
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return strMac;
    }
	
}
