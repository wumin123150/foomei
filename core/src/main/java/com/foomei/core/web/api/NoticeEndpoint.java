package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.core.dto.NoticeDto;
import com.foomei.core.dto.NoticeReceiveDto;
import com.foomei.core.entity.Notice;
import com.foomei.core.entity.NoticeReceive;
import com.foomei.core.service.NoticeReceiveService;
import com.foomei.core.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "通知接口")
@RestController
@RequestMapping(value = "/api/notice")
public class NoticeEndpoint {

  @Autowired
  private NoticeService noticeService;
  @Autowired
  private NoticeReceiveService noticeReceiveService;

  @ApiOperation(value = "通知分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "page", method = RequestMethod.GET)
  public ResponseResult<Page<NoticeDto>> page(PageQuery pageQuery, HttpServletRequest request) {
    Page<Notice> page = noticeService.getPage(new SearchRequest(pageQuery, Notice.PROP_TITLE));
    return ResponseResult.createSuccess(page, Notice.class, NoticeDto.class);
  }

  @ApiOperation(value = "通知删除", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "delete/{id}")
  public ResponseResult delete(@PathVariable("id") String id) {
    List<NoticeReceive> noticeReceives = noticeReceiveService.findByNotice(id);
    noticeReceiveService.deleteInBatch(noticeReceives);
    noticeService.delete(id);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "我的通知分页列表", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "myPage", method = RequestMethod.GET)
  public ResponseResult<Page<NoticeReceiveDto>> myPage(PageQuery pageQuery, HttpServletRequest request) {
    Page<NoticeReceive> page = noticeReceiveService.getMyPage(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
    return ResponseResult.createSuccess(page, NoticeReceive.class, NoticeReceiveDto.class);
  }

  @ApiOperation(value = "我的通知标记已读", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "read/{id}")
  public ResponseResult read(@PathVariable("id")String id, HttpServletRequest request) {
    noticeReceiveService.read(id);
    return ResponseResult.SUCCEED;
  }

}
