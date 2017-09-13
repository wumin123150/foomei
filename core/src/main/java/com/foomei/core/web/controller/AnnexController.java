package com.foomei.core.web.controller;

import com.foomei.common.io.ImageUtil;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.UserService;
import com.foomei.core.utils.DownloadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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
    download(path, request, response);
  }

  @ApiOperation(value = "附件下载", httpMethod = "GET")
  @RequestMapping(value = "/annex/{path1}/{path2}/{name:.+}", method = RequestMethod.GET)
  public void file(@PathVariable String path1, @PathVariable String path2, @PathVariable String name, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    String path = "/" + path1 + "/" + path2 + "/" + name;
    download(path, request, response);
  }

  @ApiOperation(value = "用户头像下载", httpMethod = "GET")
  @RequestMapping(value = "/avatar/{id}", method = RequestMethod.GET)
  public void file(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    User user = userService.get(id);
    String path = user.getAvatar();
    download(path, request, response);
  }

  private void download(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
    File file = new File(root + path);
    String fileName = FilenameUtils.getName(file.getName());

    Annex attachment = annexService.getByPath(path);
    if (attachment != null) {
      fileName = attachment.getName();
    }

    if ((!file.exists() || file.length() == 0) && ImageUtil.isScale(file)) {
      String scaleRange = ImageUtil.getScaleRange(file);
      File srcFile = new File(ImageUtil.getScaleSource(file));
      ImageUtil.forceScale(srcFile, file, scaleRange);
    }

    DownloadUtil.download(file, fileName, request, response);
  }

}
