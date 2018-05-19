package com.taotao.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Cookie 工具类
 * 
 */
public final class CookieUtils {

	/**
	 * 得到Cookie的值, 不编码
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return getCookieValue(request, cookieName, false);
	}

	/**
	 * 得到Cookie的值,
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					if (isDecoder) {
						retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
					} else {
						retValue = cookieList[i].getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	/**
	 * 得到Cookie的值,
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	/**
	 * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue) {
		setCookie(request, response, cookieName, cookieValue, -1);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效,但不编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage) {
		setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
	}

	/**
	 * 设置Cookie的值 不设置生效时间,但编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, boolean isEncode) {
		setCookie(request, response, cookieName, cookieValue, -1, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, String encodeString) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
	}

	/**
	 * 删除Cookie带cookie域名
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		doSetCookie(request, response, cookieName, "", -1, false);
	}

	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 * 
	 * @param cookieMaxage
	 *            cookie生效的最大秒数
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request) {// 设置域名的cookie
				String domainName = getDomainName(request);
				System.out.println(domainName);
				if (!"localhost".equals(domainName)) {
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 * 
	 * @param cookieMaxage
	 *            cookie生效的最大秒数
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, String encodeString) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else {
				cookieValue = URLEncoder.encode(cookieValue, encodeString);
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request) {// 设置域名的cookie
				String domainName = getDomainName(request);
				System.out.println(domainName);
				if (!"localhost".equals(domainName)) {
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到cookie的域名
	 */
	private static final String getDomainName(HttpServletRequest request) {
		String domainName = null;
		// 举个例子好理解，比如用户要登录，那么request.getRequestURL().toString()得到的值
		// 便是http://localhost:8088/user/login
		String serverName = request.getRequestURL().toString();
		if (serverName == null || serverName.equals("")) {
			domainName = "";
		} else {
			serverName = serverName.toLowerCase();// 将请求全部转为小写字母
			// 将http://去掉，只剩下了localhost:8088/user/login
			serverName = serverName.substring(7);
			// 获取第一个"/"的索引号
			final int end = serverName.indexOf("/");
			// substring包含开始0，不包括end，因此我们取到的是localhost:8088
			serverName = serverName.substring(0, end);
			// 以"."进行分割，由于"localhost:8088"中没有"."因此得到的字符串数组空的
			final String[] domains = serverName.split("\\.");
			// 获取数组的长度，现在是0
			int len = domains.length;
			if (len > 3) {
				// www.xxx.com.cn
				domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
			} else if (len <= 3 && len > 1) {
				// xxx.com or xxx.cn
				domainName = "." + domains[len - 2] + "." + domains[len - 1];
			} else {
				// 由于数组的长度是0，因此走这个分支，直接把localhost:8088赋值给domainName
				domainName = serverName;
			}
		}
		// domainName的值是localhost:8088，符合不为null并且包含":"
		if (domainName != null && domainName.indexOf(":") > 0) {
			// 按":"分组，得到的数组是["localhost","8088"]
			String[] ary = domainName.split("\\:");
			// 取第一个，就是"localhost"
			domainName = ary[0];
		}
		// 返回localhost
		return domainName;
	}

}