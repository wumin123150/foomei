package com.foomei.core.utils;

import com.foomei.common.io.FileUtil;
import com.foomei.common.web.Servlets;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class DownloadUtil {

  public static void download(File file, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String fileType = FileUtil.getFileExtension(fileName).toLowerCase(Locale.ENGLISH);
    // ---------------------------------------------------------------
    // 设置MIME
    // ---------------------------------------------------------------
    if (fileType.equalsIgnoreCase("xls") || fileType.equalsIgnoreCase("xlsx")) {
      response.setContentType("application/vnd.ms-excel");
    } else if (fileType.equalsIgnoreCase("doc")) {
      response.setContentType("application/msword");
    } else if (fileType.equalsIgnoreCase("ppt")) {
      response.setContentType("application/vnd.ms-powerpoint");
    } else if (fileType.equalsIgnoreCase("pdf")) {
      response.setContentType("application/pdf");
    } else if (fileType.equalsIgnoreCase("gif")) {
      response.setContentType("image/gif");
    } else if (fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")) {
      response.setContentType("image/jpeg");
    } else if (fileType.equalsIgnoreCase("png")) {
      response.setContentType("image/png");
    } else if (fileType.equalsIgnoreCase("bmp")) {
      response.setContentType("image/x-xbitmap");
    } else if (fileType.equalsIgnoreCase("svg")) {
      response.setContentType("image/svg+xml");
    } else if (fileType.equalsIgnoreCase("zip")) {
      response.setContentType("application/zip");
    } else {
      response.setContentType("multipart/form-data");
    }

    if (!file.exists() || !file.canRead()) {
      response.setContentType("text/html;charset=utf-8");
      response.getWriter().write("您下载的文件不存在！");
      return;
    }

    response.reset();
    Servlets.setNoCacheHeader(response);
    Servlets.setFileDownloadHeader(response, fileName, String.valueOf(file.length()));

    InputStream input = null;
    OutputStream output = response.getOutputStream();
    try {
      input = new FileInputStream(file);
      // 基于byte数组读取InputStream并直接写入OutputStream, 数组默认大小为4k.
      IOUtils.copy(input, output);
      output.flush();
    } finally {
      if (input != null) {
        // 保证InputStream的关闭.
        IOUtils.closeQuietly(input);
      }
    }
  }

}