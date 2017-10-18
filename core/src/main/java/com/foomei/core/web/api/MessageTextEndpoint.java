package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.core.dto.MessageTextDto;
import com.foomei.core.entity.Message;
import com.foomei.core.entity.MessageText;
import com.foomei.core.service.MessageService;
import com.foomei.core.service.MessageTextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "消息内容接口")
@RestController
@RequestMapping(value = "/api/messageText")
public class MessageTextEndpoint {

  @Autowired
  private MessageService messageService;
  @Autowired
  private MessageTextService messageTextService;

  @ApiOperation(value = "消息内容分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "page", method = RequestMethod.GET)
  public ResponseResult<Page<MessageTextDto>> page(PageQuery pageQuery, HttpServletRequest request) {
    Page<MessageText> page = messageTextService.getPage(new SearchRequest(pageQuery, new Sort(Sort.Direction.DESC, MessageText.PROP_CREATE_TIME), MessageText.PROP_CONTENT));
    return ResponseResult.createSuccess(page, MessageText.class, MessageTextDto.class);
  }

  @ApiOperation(value = "我的消息内容分页列表", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "myPage", method = RequestMethod.GET)
  public ResponseResult<Page<MessageTextDto>> myPage(PageQuery pageQuery, HttpServletRequest request) {
    Page<MessageText> page = messageTextService.getMyPage(pageQuery.getSearchKey(), pageQuery.buildPageRequest(new Sort(Sort.Direction.DESC, MessageText.PROP_CREATE_TIME)));
    return ResponseResult.createSuccess(page, MessageText.class, MessageTextDto.class);
  }

  @ApiOperation(value = "消息内容删除", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "delete/{id}")
  public ResponseResult delete(@PathVariable("id") String id) {
    List<Message> messages = messageService.findByText(id);
    messageService.deleteInBatch(messages);
    messageTextService.delete(id);
    return ResponseResult.SUCCEED;
  }

}
