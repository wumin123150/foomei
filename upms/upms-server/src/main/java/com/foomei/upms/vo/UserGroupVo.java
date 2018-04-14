package com.foomei.upms.vo;

import com.foomei.common.collection.ListUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "机构")
public class UserGroupVo {

  private Long id;
  @ApiModelProperty(value = "代码", required = true)
  @NotBlank(message = "代码不能为空")
  @Size(max = 64, message = "代码最大长度为64位")
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称最大长度为64位")
  private String name;
  @ApiModelProperty(value = "类型(0:公司,1:部门,2:小组,3:其他)", required = true)
  private Integer type;
  @ApiModelProperty(value = "负责人ID")
  private Long directorId;
  @ApiModelProperty(value = "父ID")
  private Long parentId;
  @ApiModelProperty(value = "备注")
  @Size(max = 128, message = "备注最大长度为128位")
  private String remark;
  @ApiModelProperty(value = "角色ID")
  private List<Long> roles = ListUtil.newArrayList();

}
