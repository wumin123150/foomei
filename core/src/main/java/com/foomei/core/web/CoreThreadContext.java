package com.foomei.core.web;

import com.foomei.common.concurrent.ThreadLocalContext;
import com.foomei.core.entity.BaseUser;

public class CoreThreadContext extends ThreadLocalContext {
	
	public static final String USER_KEY = "_user_key";
	public static final String IP_KEY = "_ip_key";
	public static final String URL_KEY = "_url_key";
	
	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public static BaseUser getUser() {
		return (BaseUser) get(USER_KEY);
	}

	/**
	 * 设置当前用户
	 * 
	 * @param user
	 */
	public static void setUser(BaseUser user) {
		if (user != null)
			put(USER_KEY, user);
	}

	/**
	 * 移除当前用户
	 */
	public static void removeUser() {
		remove(USER_KEY);
	}
	
	public static Long getUserId() {
		BaseUser user = getUser();
		return user != null ? user.getId() : null;
	}
	
	public static String getUserName() {
		BaseUser user = getUser();
		return user != null ? user.getName() : null;
	}
	
	public static String getIp() {
		return (String) get(IP_KEY);
	}

	public static void setIp(String ip) {
		put(IP_KEY, ip);
	}
	
	public static String getUrl() {
		return (String) get(URL_KEY);
	}

	public static void setUrl(String url) {
		put(URL_KEY, url);
	}

}
