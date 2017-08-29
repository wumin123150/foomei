package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.dto.ConfigDto;
import com.foomei.core.entity.Config;
import com.foomei.core.service.ConfigService;

@Api(description = "系统配置接口")
@RestController
@RequestMapping(value = "/api/config")
public class ConfigEndpoint {

	private static final String PAGE_SIZE = "10";

	@Autowired
	private ConfigService configService;
	
	static {
	    BeanMapper.registerClassMap(Config.class, ConfigDto.class, false, false);
	}

	@ApiOperation(value = "系统配置分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "page", method = RequestMethod.GET)
	public ResponseResult<Page<Config>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<Config> page = null;
		if(advance) {
			JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
			page = configService.getPage(jqGridFilter, pageRequest);
		} else {
		    SearchFilter searchFilter = new SearchFilter().or()
		        .addLike(Config.PROP_CODE, searchKey)
		        .addLike(Config.PROP_NAME, searchKey);
			page = configService.getPage(searchFilter, pageRequest);
		}

		return ResponseResult.createSuccess(page);
	}
	
	@ApiOperation(value = "单配置获取", httpMethod = "GET")
    @RequestMapping("get/{code}")
    public ResponseResult<ConfigDto> getByCode(@ApiParam(value="键", required=true) @PathVariable("code")String code) {
	    Config config = configService.getByCode(code);
	    return ResponseResult.createSuccess(config, ConfigDto.class);
    }
	
	@ApiOperation(value = "多配置获取", httpMethod = "GET")
    @RequestMapping("find/{code}")
    public ResponseResult<List<ConfigDto>> findByCode(@ApiParam(value="键前缀", required=true) @PathVariable("code")String code) {
	    SearchFilter searchFilter = new SearchFilter()
	        .addStartWith(Config.PROP_CODE, code + ".");
        List<Config> configs = configService.getList(searchFilter, null);
        return ResponseResult.createSuccess(configs, Config.class, ConfigDto.class);
    }
	
	@ApiOperation(value = "保存配置", httpMethod = "POST")
	@RequestMapping("save")
	private ResponseResult save(ConfigDto configDto) {
	    Config config = resolve(configDto);
	    configService.save(config);
	    return ResponseResult.SUCCEED;
	}
	
	/**
	 * 判断编码的唯一性
	 * 
	 */
	@ApiOperation(value = "检查配置键是否存在", httpMethod = "GET")
	@RequestMapping("checkCode")
	public boolean checkCode(Long id, @ApiParam(value="键", required=true) String code) {
		return !configService.existCode(id, code);
	}
	
    private Config resolve(ConfigDto configDto) {
        Config config = configService.get(configDto.getId());
        BeanMapper.map(configDto, config, ConfigDto.class, Config.class);
        return config;
    }
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}
	
}
