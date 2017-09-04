package com.foomei.common.web;

import com.foomei.common.concurrent.ThreadLocalContext;
import com.foomei.common.entity.CoreUser;

public class ThreadContext extends ThreadLocalContext {
	
	public static final String USER_KEY = "_user_key";
	public static final String IP_KEY = "_ip_key";
	public static final String URL_KEY = "_url_key";
	public static final String DATA_SOURCE_KEY = "_data_source_key";
	public static final String UPLOAD_PATH_KEY = "_upload_path_key";
	
	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public static CoreUser getUser() {
		return (CoreUser) get(USER_KEY);
	}

	/**
	 * 设置当前用户
	 * 
	 * @param user
	 */
	public static void setUser(CoreUser user) {
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
		CoreUser user = getUser();
		return user != null ? user.getId() : null;
	}
	
	public static String getUserName() {
		CoreUser user = getUser();
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

	public static String getDataSource() {
		return (String) get(DATA_SOURCE_KEY);
	}

	public static void setDataSource(String dataSource) {
		put(DATA_SOURCE_KEY, dataSource);
	}

	public static String getUploadPath() {
		return (String) get(UPLOAD_PATH_KEY);
	}

	public static void setUploadPath(String uploadPath) {
		put(UPLOAD_PATH_KEY, uploadPath);
	}

}
