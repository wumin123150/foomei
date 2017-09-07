package com.foomei.core.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foomei.common.net.HttpClientUtil;
import com.foomei.common.web.PropertyHolder;
import com.google.common.collect.Maps;

public class SmsUtil {

  private static boolean support;
  private static String url;
  private static String identityKey;

  static {
    support = PropertyHolder.getBoolProperty("sms.support");
    url = PropertyHolder.getProperty("sms.url");
    identityKey = PropertyHolder.getProperty("sms.key");
  }

  public static boolean send(String phones, String content) {
    Map<String, String> params = Maps.newHashMap();
    params.put("phones", phones);
    params.put("content", content);
    params.put("identityKey", identityKey);

    try {
      if (support) {
        String result = HttpClientUtil.postString(url, params, "UTF-8");
        return StringUtils.contains(result, "<code>0</code>");
      } else {
        return true;
      }
    } catch (Exception e) {

    }

    return false;
  }

}
