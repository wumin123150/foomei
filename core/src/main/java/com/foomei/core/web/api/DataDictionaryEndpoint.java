package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ResponseResult;
import com.foomei.core.dto.TreeNodeDto;
import com.foomei.core.entity.DataDictionary;
import com.foomei.core.service.DataDictionaryService;
import com.foomei.core.service.DataTypeService;
import com.google.common.collect.Lists;

@Api(description = "数据字典接口")
@RestController
@RequestMapping(value = "/api/dataDictionary")
public class DataDictionaryEndpoint {

	@Autowired
	private DataTypeService dataTypeService;
	@Autowired
	private DataDictionaryService dataDictionaryService;

	@ApiOperation(value = "根据数据类型和父节点获取数据字典列表", notes = "父节点为空时，获取数据类型下的所有内容", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "tree", method = RequestMethod.POST)
	public List<TreeNodeDto> tree(@ApiParam(value="数据类型编码", required=true)String typeCode, @ApiParam(value="父节点ID")Long id) {
		if(id != null) {
			List<DataDictionary> dataDictionarys = dataDictionaryService.findChildrenByTypeAndParent(typeCode, id);
			return toNodes(dataDictionarys);
		} else {
			List<DataDictionary> dataDictionarys = dataDictionaryService.findByTypeCode(typeCode);
			return toNodes(dataDictionarys);
		}
	}

	@ApiOperation(value = "数据字典新增", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseResult<TreeNodeDto> create(@Valid @ModelAttribute DataDictionary dataDictionary, BindingResult result, @ApiParam(value="数据类型编码", required=true)String typeCode) {
        if (dataDictionaryService.existCode(dataDictionary.getId(), typeCode, dataDictionary.getCode())) {
            result.addError(new FieldError("dataDictionary", "code", "编码已经被使用"));
        }
        
        if(dataDictionary.getParentId() != null) {
            DataDictionary parent = dataDictionaryService.get(dataDictionary.getParentId());
            if(parent != null) {
                dataDictionary.setLevel(parent.getLevel() + 1);
            } else {
                result.addError(new FieldError("dataDictionary", "parentId", "父节点不存在"));
            }
        } else {
            dataDictionary.setLevel(1);
        }

        if (result.hasErrors()) {
            return ResponseResult.createParamError(result);
        } else {
            dataDictionaryService.save(dataDictionary);
        }

		return new ResponseResult<TreeNodeDto>(new TreeNodeDto(dataDictionary));
	}

	@ApiOperation(value = "数据字典修改", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ResponseResult<TreeNodeDto> update(@Valid @ModelAttribute("preloadDataDictionary") DataDictionary dataDictionary, BindingResult result, @ApiParam(value="数据类型编码", required=true)String typeCode) {
		if (dataDictionaryService.existCode(dataDictionary.getId(), typeCode, dataDictionary.getCode())) {
			result.addError(new FieldError("dataDictionary", "code", "编码已经被使用"));
		}

		if (result.hasErrors()) {
			return ResponseResult.createParamError(result);
		} else {
			dataDictionaryService.save(dataDictionary);
		}

		return new ResponseResult<TreeNodeDto>(new TreeNodeDto(dataDictionary));
	}

	@ApiOperation(value = "数据字典删除", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "delete/{id}")
	public ResponseResult delete(@PathVariable("id") Long id) {
		dataDictionaryService.delete(id);
		return ResponseResult.SUCCEED;
	}
	
	@ApiOperation(value = "数据字典获取", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "get/{id}")
	public ResponseResult get(@PathVariable("id") Long id) {
		return new ResponseResult<DataDictionary>(dataDictionaryService.get(id));
	}
	
	/**
     * 判断编码的唯一性
     * 
     */
	@ApiOperation(value = "检查数据字典编码是否存在", httpMethod = "GET")
    @RequestMapping("checkCode")
    public boolean checkCode(Long id, @ApiParam(value="数据类型编码", required=true)String typeCode, @ApiParam(value="数据字典编码", required=true)String code) {
        return !dataDictionaryService.existCode(id, typeCode, code);
    }

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadDataDictionary")
	public DataDictionary getDataDictionary(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return dataDictionaryService.get(id);
		}
		return null;
	}
	
	private List<TreeNodeDto> toNodes(List<DataDictionary> dataDictionarys) {
		List<TreeNodeDto> treeNodes = Lists.newArrayListWithCapacity(dataDictionarys.size());
		for (DataDictionary dataDictionary : dataDictionarys) {
			treeNodes.add(new TreeNodeDto(dataDictionary));
		}
		return treeNodes;
	}
}
