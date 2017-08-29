package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.dto.UserDto;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Token;
import com.foomei.core.service.BaseUserService;
import com.foomei.core.service.TokenService;
import com.foomei.core.web.CoreThreadContext;

@Api(description = "用户接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserEndpoint {
    
    private static final String PAGE_SIZE = "10";
	
	@Autowired
	private BaseUserService baseUserService;
	@Autowired
	private TokenService tokenService;
	
	@ApiOperation(value = "用户智能搜索", notes="按名称和手机查询", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "search")
	public ResponseResult<Page<BaseUser>> search(@RequestParam int pageNo, @RequestParam int pageSize, @ApiParam(value="关键词", required=true) @RequestParam("q") String searchKey) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, BaseUser.PROP_NAME, "desc");
		SearchFilter searchFilter = new SearchFilter().or()
                .addLike(BaseUser.PROP_NAME, searchKey)
                .addLike(BaseUser.PROP_LOGIN_NAME, searchKey)
                .addLike(BaseUser.PROP_MOBILE, searchKey);
		Page<BaseUser> users = baseUserService.getPage(searchFilter, pageRequest);
		return ResponseResult.createSuccess(users);
	}
	
	@ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
    @RequestMapping(value = "page")
    public ResponseResult<Page<BaseUser>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
            @RequestParam(defaultValue = "false") Boolean advance, String searchKey, HttpServletRequest request) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<BaseUser> page = null;
        if(advance) {
            JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
            page = baseUserService.getPage(jqGridFilter, pageRequest);
        } else {
            SearchFilter searchFilter = new SearchFilter().or()
                    .addLike(BaseUser.PROP_NAME, searchKey)
                    .addLike(BaseUser.PROP_LOGIN_NAME, searchKey)
                    .addLike(BaseUser.PROP_MOBILE, searchKey);
            page = baseUserService.getPage(searchFilter, pageRequest);
        }
        return ResponseResult.createSuccess(page);
    }

	@ApiOperation(value = "用户获取", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "get/{id}")
	public ResponseResult<BaseUser> get(@PathVariable("id") Long id) {
		BaseUser user = baseUserService.get(id);
		return ResponseResult.createSuccess(user);
	}
	
	@ApiOperation(value = "修改用户信息", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "change")
    public ResponseResult<UserDto> change(@ApiParam(value="姓名", required=true)String name, @ApiParam(value="手机")String mobile, @ApiParam(value="电子邮件")String email) {
        BaseUser user = baseUserService.get(CoreThreadContext.getUserId());
        user.setName(name);
        user.setMobile(mobile);
        user.setEmail(email);
        user = baseUserService.save(user);
        return ResponseResult.createSuccess(user, UserDto.class);
    }

	@ApiOperation(value = "检查token是否有效", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "/validate/{token}")
    public ResponseResult validateToken(@ApiParam(required=true) @PathVariable("token") String id, HttpServletRequest request) {
        Token token = tokenService.get(id);
        if(token != null && token.isEnabled() && token.getUser().isEnabled()) {
            return ResponseResult.SUCCEED;
        }
        
        return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "由于您长时间未登录 请重新登录。");
    }
	
	@ApiOperation(value = "检查用户名是否存在", httpMethod = "GET")
	@RequestMapping(value = "checkLoginName")
    public boolean checkLoginName(Long id, @ApiParam(value="用户名", required=true) String loginName) {
        return !baseUserService.existLoginName(id, loginName);
    }
	
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}

}
