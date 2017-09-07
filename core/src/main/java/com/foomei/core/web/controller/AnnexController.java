package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.foomei.common.io.ImageUtil;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.UserService;

/**
 * 附件
 *
 * @author walker
 */
@Api(description = "附件管理")
@Controller
public class AnnexController {

  @Value("${upload.folder:/opt/upload/}")
  private String root;
  @Autowired
  private AnnexService annexService;
  @Autowired
  private UserService userService;

  @ApiOperation(value = "附件下载", httpMethod = "GET")
  @RequestMapping(value = "/annex/{path1}/{name:.+}", method = RequestMethod.GET)
  public void file(@PathVariable String path1, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String path = "/" + path1 + "/" + name;
    String realPath = root + path;
    File file = new File(realPath);
    String fileType = FilenameUtils.getExtension(file.getName());
    String fileName = FilenameUtils.getName(file.getName());

    download(file, fileType, fileName, request, response);
  }

  @ApiOperation(value = "附件下载", httpMethod = "GET")
  @RequestMapping(value = "/annex/{path1}/{path2}/{name:.+}", method = RequestMethod.GET)
  public void file(@PathVariable String path1, @PathVariable String path2, @PathVariable String name, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    String path = "/" + path1 + "/" + path2 + "/" + name;
    String realPath = root + path;
    Annex attachment = annexService.getByPath(path);

    File file = new File(realPath);
    String fileType = FilenameUtils.getExtension(file.getName());
    String fileName = FilenameUtils.getName(file.getName());

    if (attachment != null) {
      fileType = attachment.getType();
      fileName = attachment.getName();
    }

    download(file, fileType, fileName, request, response);
  }

  @ApiOperation(value = "用户头像下载", httpMethod = "GET")
  @RequestMapping(value = "/avatar/{id}", method = RequestMethod.GET)
  public void file(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    User user = userService.get(id);
    String path = user.getAvatar();
    String realPath = root + path;
    Annex attachment = annexService.getByPath(path);

    File file = new File(realPath);
    String fileType = FilenameUtils.getExtension(file.getName());
    String fileName = FilenameUtils.getName(file.getName());

    if (attachment != null) {
      fileType = attachment.getType();
      fileName = attachment.getName();
    }

    download(file, fileType, fileName, request, response);
  }

  private void download(File file, String fileType, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    if ((!file.exists() || file.length() == 0) && ImageUtil.isScale(file)) {
      String scaleRange = ImageUtil.getScaleRange(file);
      File srcFile = new File(ImageUtil.getScaleSource(file));
      ImageUtil.forceScale(srcFile, file, scaleRange);
    }

    response.setHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(fileName, "UTF-8")));
    response.addHeader("Content-Length", "" + file.length());

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
