package com.foomei.core.web.api;

import com.baidu.unbiz.fluentvalidator.*;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.BeanMapper;
import com.foomei.core.dto.DataDictionaryDto;
import com.foomei.core.dto.TreeNodeDto;
import com.foomei.core.entity.DataDictionary;
import com.foomei.core.entity.DataType;
import com.foomei.core.service.DataDictionaryService;
import com.foomei.core.service.DataTypeService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validation;
import java.util.List;

@Api(description = "数据字典接口")
@RestController
@RequestMapping(value = "/api/dataDictionary")
public class DataDictionaryEndpoint {

	@Autowired
	private DataTypeService dataTypeService;
	@Autowired
	private DataDictionaryService dataDictionaryService;

	@ApiOperation(value = "根据数据类型和父节点获取数据字典列表", notes = "父节点为空时，获取数据类型下的所有内容", httpMethod = "POST", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeCode", value = "数据类型编码", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "id", value = "父节点ID", dataType = "long", paramType = "query")
	})
	@RequiresRoles("admin")
	@RequestMapping(value = "tree", method = RequestMethod.POST)
	public List<TreeNodeDto> tree(String typeCode, Long id) {
		if(id != null) {
			List<DataDictionary> dataDictionarys = dataDictionaryService.findChildrenByTypeAndParent(typeCode, id);
			return toNodes(dataDictionarys);
		} else {
			List<DataDictionary> dataDictionarys = dataDictionaryService.findByTypeCode(typeCode);
			return toNodes(dataDictionarys);
		}
	}

	@ApiOperation(value = "数据字典新增", httpMethod = "POST", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeCode", value = "数据类型编码", required = true, dataType = "string", paramType = "form")
	})
	@RequiresRoles("admin")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseResult<TreeNodeDto> create(DataDictionaryDto dictionary) {
		DataDictionary dataDictionary = BeanMapper.map(dictionary, DataDictionary.class);
		dataDictionary.setType(new DataType(dictionary.getTypeId()));

		if(dataDictionary.getParentId() != null) {
			DataDictionary parent = dataDictionaryService.get(dataDictionary.getParentId());
			if(parent != null) {
				dataDictionary.setLevel(parent.getLevel() + 1);
			}
		} else {
			dataDictionary.setLevel(1);
		}

		ComplexResult result = validate(dataDictionary, dictionary.getTypeCode());
		if (!result.isSuccess()) {
            return ResponseResult.createParamError(result);
        } else {
            dataDictionaryService.save(dataDictionary);
        }

		return new ResponseResult<TreeNodeDto>(new TreeNodeDto(dataDictionary));
	}

	@ApiOperation(value = "数据字典修改", httpMethod = "POST", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeCode", value = "数据类型编码", required = true, dataType = "string", paramType = "form")
	})
	@RequiresRoles("admin")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ResponseResult<TreeNodeDto> update(DataDictionaryDto dictionary) {
		DataDictionary dataDictionary = dataDictionaryService.get(dictionary.getId());
		dataDictionary = BeanMapper.map(dictionary, dataDictionary, DataDictionaryDto.class, DataDictionary.class);

		ComplexResult result = validate(dataDictionary, dictionary.getTypeCode());
        if (!result.isSuccess()) {
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
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeCode", value = "数据类型编码", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "code", value = "数据字典编码", required = true, dataType = "string", paramType = "query")
	})
    @RequestMapping("checkCode")
    public boolean checkCode(Long id, String typeCode, String code) {
        return !dataDictionaryService.existCode(id, typeCode, code);
    }

	private ComplexResult validate(DataDictionary dataDictionary, final String typeCode) {
		ComplexResult result = FluentValidator.checkAll()
				.on(dataDictionary, new HibernateSupportedValidator<DataDictionary>().setHiberanteValidator(Validation.buildDefaultValidatorFactory().getValidator()))
				.on(dataDictionary, new ValidatorHandler<DataDictionary>() {
					public boolean validate(ValidatorContext context, DataDictionary t) {
						if (dataDictionaryService.existCode(t.getId(), typeCode, t.getCode())) {
							context.addErrorMsg("编码已经被使用");
							return false;
						}
						return true;
					}
				})
				.doValidate()
				.result(ResultCollectors.toComplex());
		return result;
	}
	
	private List<TreeNodeDto> toNodes(List<DataDictionary> dataDictionarys) {
		List<TreeNodeDto> treeNodes = Lists.newArrayListWithCapacity(dataDictionarys.size());
		for (DataDictionary dataDictionary : dataDictionarys) {
			treeNodes.add(new TreeNodeDto(dataDictionary));
		}
		return treeNodes;
	}
}
