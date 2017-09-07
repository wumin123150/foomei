<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>角色授权</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/select2.min.css"/>
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .input-required {
      margin-left: 2px;
      color: #c00;
    }

    .ui-state-disabled {
      color: #BBB !important;
    }

    #btn-search {
      height: 28px;
      margin-bottom: 4px;
    }

    .no-data {
      margin-top: 10px;
    }

    .ui-jqdialog-content .searchFilter select {
      padding: 5px 1px;
      height: 30px;
      line-height: 30px;
    }

    .ui-jqdialog-content .searchFilter .input-elm {
      padding-bottom: 5px;
    }

    .chosen-container-multi .chosen-choices {
      border: 1px solid #d5d5d5;
    }

    .tag-input-style + .chosen-container-multi .chosen-choices li.search-choice {
      padding-top: 5px;
      padding-bottom: 6px;
    }

    .tag-input-style + .chosen-container-multi .chosen-choices li.search-field {
      padding-top: 3px;
      padding-bottom: 3px;
    }

    .form-group.has-error ul {
      border-color: #f2a696;
      color: #d68273;
      -webkit-box-shadow: none;
      box-shadow: none;
    }
  </style>
</pageCss>
<body>
<!-- /section:basics/sidebar -->
<div class="main-content">
  <div class="main-content-inner">
    <!-- #section:basics/content.breadcrumbs -->
    <div class="breadcrumbs" id="breadcrumbs">
      <script type="text/javascript">
        try {
          ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
      </script>

      <ul class="breadcrumb">
        <li>
          <i class="ace-icon fa fa-home home-icon"></i>
          <a href="${ctx}/admin/index">首页</a>
        </li>
        <li>
          <a href="${ctx}/admin/role">角色管理</a>
        </li>
        <li class="active">角色授权</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          角色授权
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <c:if test="${not empty message}">
            <div class="alert alert-block alert-success">
              <button type="button" class="close" data-dismiss="alert">
                <i class="ace-icon fa fa-times"></i>
              </button>

              <i class="ace-icon fa fa-check green"></i>
                ${message}
            </div>
          </c:if>
          <div class="row">
            <div class="col-xs-12 right-content">
              <form class="form-search" action="">
                <div class="dataTables_filter input-group">
                  <select name="roleId" id="roleId" class="chosen-select tag-input-style"
                          style="height: 28px; width: 100px;" data-placeholder="角色">
                    <c:forEach items="${roles}" var="role">
                      <c:set var="selectedRole" value="false"/>
                      <c:if test="${roleId eq role.id}">
                        <c:set var="selectedRole" value="true"/>
                      </c:if>
                      <option value="${role.id}" <c:if test="${selectedRole}">selected</c:if>>${role.name}</option>
                    </c:forEach>
                  </select>
                  <input type="text" id="searchKey" name="searchKey" value="${searchKey}" class="input-sm"
                         placeholder="账号、姓名、手机" aria-controls="datatables">
                  <input style="display:none"/>
                  <span class="input-group-btn">
											<button id="btn-search" class="btn btn-xs btn-purple" type="button"><i class="fa fa-search"></i>查询</button>
										</span>
                </div>
              </form>
            </div>
          </div>

          <table id="grid-table"></table>

          <div id="dialog-container" class="hide">
            <div id="dialog-form">
              <form class="form-horizontal" id="validation-form" action="#" method="post" role="form">
                <input type="hidden" name="roleId" id="form-roleId"/>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-userId"> 用户 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="userId" id="form-userId" class="select2"/>
                    </div>
                  </div>
                </div>
              </form>
            </div><!-- #dialog-form -->
          </div>
          <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.page-content -->
  </div>
</div><!-- /.main-content -->
</body>
<pluginJs>
  <!-- page specific plugin scripts -->
  <script src="${ctx}/static/js/jquery.validate.min.js"></script>
  <script src="${ctx}/static/js/additional-methods.min.js"></script>
  <script src="${ctx}/static/js/messages_zh.min.js"></script>
  <script src="${ctx}/static/js/jqGrid/jquery.jqGrid.min.js"></script>
  <script src="${ctx}/static/js/jqGrid/i18n/grid.locale-cn.js"></script>
  <script src="${ctx}/static/js/jquery.jqGrid.foomei.min.js"></script>
  <script src="${ctx}/static/js/select2.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var grid_selector = "#grid-table";
    var grid_page_url = "${ctx}/api/userRole/page";
    var grid_add_url = "${ctx}/api/userRole/create";
    var grid_edit_url = "";
    var grid_del_url = "${ctx}/api/userRole/delete";

    function addForm() {
      $("#validation-form")[0].reset();

      $("#form-roleId").val($("#roleId").val());

      BootstrapDialog.show({
        title: '<i class="ace-icon fa fa-plus-circle bigger-110"></i>&nbsp;新增用户',
        message: $('#dialog-form'),
        autodestroy: false,
        buttons: [{
          label: '<i class="ace-icon fa fa-times bigger-110"></i>取消',
          action: function (dialogRef) {
            dialogRef.close();
          }
        }, {
          label: '<i class="ace-icon fa fa-check bigger-110">确定</i>',
          cssClass: 'btn-primary',
          action: function (dialogRef) {
            if ($('#validation-form').valid()) {
              var params = $("#validation-form").serialize();
              $.ajax({
                url: grid_add_url,
                data: params,
                type: 'POST',
                cache: false,
                dataType: 'json',
                success: function (result) {
                  if (result.success) {
                    $(grid_selector).trigger('reloadGrid');
                    dialogRef.close();
                  } else {
                    var error = $('<div class="alert alert-danger"><button type="button" class="close" data-dismiss="alert"><i class="ace-icon fa fa-times"></i></button><i class="ace-icon fa fa-times"></i>' + result.message + '</div>');
                    $('#validation-form').prepend(error);
                  }
                },
                error: function () {
                  toastr.error('未知错误，请联系管理员');
                }
              });
            }
          }
        }]
      });
    }

    function delForm(id) {
      var roleId = $("#roleId").val();

      BootstrapDialog.confirm('你确定要删除吗？', function (result) {
        if (result) {
          $.ajax({
            url: grid_del_url,
            data: {
              userId: id,
              roleId: roleId
            },
            type: 'POST',
            cache: false,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                $(grid_selector).trigger('reloadGrid');
              } else {
                toastr.error(result.message);
              }
            },
            error: function () {
              toastr.error('未知错误，请联系管理员');
            }
          });
        }
      });
    }

    (function ($) {
      "use strict";
      $.fn.select2.locales['zh-CN'] = {
        formatNoMatches: function () {
          return "没有找到匹配项";
        },
        formatAjaxError: function (jqXHR, textStatus, errorThrown) {
          return "获取结果失败";
        },
        formatInputTooShort: function (input, min) {
          var n = min - input.length;
          return "请再输入" + n + "个字符";
        },
        formatInputTooLong: function (input, max) {
          var n = input.length - max;
          return "请删掉" + n + "个字符";
        },
        formatSelectionTooBig: function (limit) {
          return "你只能选择最多" + limit + "项";
        },
        formatLoadMore: function (pageNumber) {
          return "加载结果中…";
        },
        formatSearching: function () {
          return "搜索中…";
        }
      };

      $.extend($.fn.select2.defaults, $.fn.select2.locales['zh-CN']);
    })(jQuery);

    jQuery(function ($) {
      $('.select2').css('width', '100%').select2({
        placeholder: "用户",
        minimumInputLength: 1,
        ajax: {
          url: "${ctx}/api/user/search",
          dataType: 'json',
          delay: 250,
          data: function (term, pageNo) {
            return {
              q: $.trim(term),	//联动查询的字符
              pageSize: 15,    	//一次性加载的数据条数
              pageNo: pageNo,  	//页码
              time: new Date()  	//测试
            }
          },
          results: function (result, pageNo) {
            var more = (pageNo * 15) < result.data.totalElements; //用来判断是否还有更多数据可以加载
            return {
              results: result.data.content, more: more
            };
          },
          cache: false
        },
        escapeMarkup: function (markup) {
          return markup;
        }, // let our custom formatter work
        formatResult: function (repo) {
          if (repo.loading) return repo.text;

          return repo.name + "(" + repo.loginName + ")";
        },
        formatSelection: function (repo) {
          return repo.name || repo.text;
        }
      });

      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          userId: {
            required: true
          }
        },
        messages: {
          code: {
            remote: '编码已经被使用'
          }
        },
        highlight: function (e) {
          $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
        },
        success: function (e) {
          $(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
          $(e).remove();
        },
        errorPlacement: function (error, element) {
          if (element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
            var controls = element.closest('div[class*="col-"]');
            if (controls.find(':checkbox,:radio').length > 1) controls.append(error);
            else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
          }
          else if (element.is('.select2')) {
            error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
          }
          else if (element.is('.chosen-select')) {
            error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
          }
          else error.insertAfter(element.parent());
        },
        submitHandler: function (form) {
        },
        invalidHandler: function (form) {
        }
      });

      $(grid_selector).foomei_JqGrid({
        url: grid_page_url,
        datatype: 'local',
        colNames: ['操作', '账号', '姓名', '手机', '邮箱'],
        colModel: [
          //{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
          //formatter:'actions',
          //formatoptions:{
          //keys:true,
          //delbutton:true, delOptions:{recreateForm:true, beforeShowForm:beforeDeleteCallback},
          //editformbutton:true, editOptions:{recreateForm:true, beforeShowForm:beforeEditCallback}
          //}
          //},
          {
            name: 'myop', index: '', width: 80, fixed: true, sortable: false, resize: false, search: false,
            formatter: function (cellvalue, options, rowObject) {
              return '<div class="action-buttons">'
                + '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red" href="javascript:void(0);" onClick="delForm(\'' + rowObject.id + '\')"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'loginName', index: 'loginName', width: 100},
          {name: 'name', index: 'name', width: 100},
          {name: 'mobile', index: 'mobile', width: 100},
          {name: 'email', index: 'email', width: 100}
        ],
        nav: {
          view: false,
          search: false
        },
        navAdd: [
          {
            caption: '',
            title: '添加',
            buttonicon: 'ace-icon fa fa-plus-circle purple',
            onClickButton: function () {
              addForm();
            },
            position: 'first'
          }
        ]
      });

      $(grid_selector).foomei_JqGrid('resize', {container: '.right-content'});

      $("#searchKey").keydown(function (e) {
        if (e.keyCode == 13) {
          $(grid_selector).foomei_JqGrid('search', {
            "searchKey": $('#searchKey').val(),
            "roleId": $('#roleId').val()
          });
        }
      });

      $("#btn-search").click(function () {
        $(grid_selector).foomei_JqGrid('search', {
          "searchKey": $('#searchKey').val(),
          "roleId": $('#roleId').val()
        });
      }).trigger("click");
    })
  </script>
</pageJs>
</html>
