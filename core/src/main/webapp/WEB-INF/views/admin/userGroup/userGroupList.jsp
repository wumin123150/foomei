<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>机构管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
  <link rel="stylesheet" href="${ctx}/static/css/chosen.min.css"/>
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
        <li class="active">机构管理</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          机构管理
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-3">
          <div class="widget-box">
            <div class="widget-header widget-header-flat">
              <h4 class="widget-title smaller">
                组织机构
              </h4>
            </div>

            <div class="widget-body">
              <div class="widget-main">
                <div class="row">
                  <div class="col-xs-12">
                    <ul id="tree" class="ztree"></ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xs-9">
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
          <ul class="nav nav-tabs" id="myTab">
            <li class="active">
              <a data-toggle="tab" href="#home" aria-expanded="true">
                <i class="green ace-icon fa fa-group bigger-120"></i>
                下级机构
              </a>
            </li>
            <li>
              <a id="authTab" data-toggle="tab" href="#auth" aria-expanded="false">
                <i class="green ace-icon fa fa-user bigger-120"></i>
                机构人员
              </a>
            </li>
          </ul>
          <div class="tab-content">
            <div id="home" class="tab-pane fade active in">
              <div class="right-content">
                <form class="form-search" action="">
                  <div class="dataTables_filter input-group">
                    <input type="text" id="searchKey1" name="searchKey1" value="${searchKey}" class="input-sm"
                           placeholder="" aria-controls="datatables">
                    <input type="hidden" id="parentId" name="parentId"/>
                    <input style="display:none"/>
                    <span class="input-group-btn">
                        <button id="btn-search1" class="btn btn-xs btn-purple" type="button"><i
                          class="fa fa-search"></i>查询</button>
                      </span>
                  </div>
                </form>
              </div>

              <table id="grid-table1"></table>
            </div>
            <div id="auth" class="tab-pane fade">
              <form class="form-search" action="" id="form2">
                <div class="dataTables_filter input-group">
                  <input type="text" id="searchKey2" name="searchKey2" value="${searchKey}" class="input-sm"
                         placeholder="账号、姓名、手机" aria-controls="datatables">
                  <input type="hidden" id="groupId" name="groupId"/>
                  <input style="display:none"/>
                  <span class="input-group-btn">
                      <button id="btn-search2" class="btn btn-xs btn-purple" type="button"><i class="fa fa-search"></i>查询</button>
                    </span>
                </div>
              </form>

              <table id="grid-table2"></table>
            </div>
          </div>

          <div id="dialog-container" class="hide">
            <div id="dialog-form1">
              <form class="form-horizontal" id="validation-form1" action="#" method="post" role="form">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="parentId" id="form-parentId"/>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-code">
                    机构编码<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="code" id="form-code" placeholder="机构编码" class="form-control"/>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name">
                    机构名称<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="name" id="form-name" placeholder="机构名称" class="form-control"/>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-type">
                    机构类型<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="type" id="form-type" class="form-control" data-placeholder="机构类型">
                        <option value="0">公司</option>
                        <option value="1">部门</option>
                        <option value="2">小组</option>
                        <option value="3">其他</option>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-role"> 角色 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="roleIds" id="form-role" class="form-control chosen-select tag-input-style"
                              data-placeholder="角色" multiple="">
                        <c:forEach items="${roles}" var="role">
                          <option value="${role.id}">${role.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-remark"> 备注 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="remark" id="form-remark" placeholder="备注 " class="form-control"></textarea>
                    </div>
                  </div>
                </div>
              </form>
            </div><!-- #dialog-form -->
            <div id="dialog-form2">
              <form class="form-horizontal" id="validation-form2" action="#" method="post" role="form">
                <input type="hidden" name="groupId" id="form-groupId"/>
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
  <script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
  <script src="${ctx}/static/js/chosen.jquery.min.js"></script>
  <script src="${ctx}/static/js/select2.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var grid_selector = "#grid-table1";
    var grid_page_url = "${ctx}/api/userGroup/page";
    var grid_add_url = "${ctx}/api/userGroup/create";
    var grid_edit_url = "${ctx}/api/userGroup/update";
    var grid_del_url = "${ctx}/api/userGroup/delete/";
    var grid_get_url = "${ctx}/api/userGroup/get/";

    var auth_selector = "#grid-table2";
    var auth_page_url = "${ctx}/api/membership/page";
    var auth_add_url = "${ctx}/api/membership/create";
    var auth_edit_url = "";
    var auth_del_url = "${ctx}/api/membership/delete";

    var reloadTree = function () {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      var nodes = treeObj.getSelectedNodes();
      var treeNode = nodes[0];
      if (!treeNode) {
        treeObj.reAsyncChildNodes(null, "refresh");
      } else {
        treeObj.reAsyncChildNodes(treeNode, "refresh");
      }
    }

    function addForm() {
      $("#validation-form1")[0].reset();
      $("#form-parentId").val($("#parentId").val());

      BootstrapDialog.show({
        title: '<i class="ace-icon fa fa-plus-circle bigger-110"></i>&nbsp;新增机构',
        message: $('#dialog-form1'),
        autodestroy: false,
        onshown: function () {
          $("#form-role").trigger('chosen:updated');
          $("#form-role").trigger('resize.chosen');
        },
        buttons: [{
          label: '<i class="ace-icon fa fa-times bigger-110"></i>取消',
          action: function (dialogRef) {
            dialogRef.close();
          }
        }, {
          label: '<i class="ace-icon fa fa-check bigger-110">确定</i>',
          cssClass: 'btn-primary',
          action: function (dialogRef) {
            if ($('#validation-form1').valid()) {
              var params = $("#validation-form1").serialize();
              $.ajax({
                url: grid_add_url,
                data: params,
                type: 'POST',
                cache: false,
                dataType: 'json',
                success: function (result) {
                  if (result.success) {
                    $(grid_selector).trigger('reloadGrid');
                    reloadTree();
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

    function editForm(id) {
      $.ajax({
        url: grid_get_url + id,
        type: 'POST',
        cache: false,
        dataType: 'json',
        success: function (result) {
          $("#id").val(result.data.id);
          $("#form-parentId").val(result.data.parentId);
          $("#form-code").val(result.data.code);
          $("#form-name").val(result.data.name);
          $("#form-role").val(result.data.roleIds);
          $("#form-type").val(result.data.type);
          $("#form-priority").val(result.data.priority);
          $("#form-remark").val(result.data.remark);

          BootstrapDialog.show({
            title: '<i class="ace-icon fa fa-pencil bigger-110"></i>&nbsp;修改机构',
            message: $('#dialog-form1'),
            autodestroy: false,
            onshown: function () {
              $("#form-role").trigger('chosen:updated');
              $("#form-role").trigger('resize.chosen');
            },
            buttons: [{
              label: '<i class="ace-icon fa fa-times bigger-110"></i>取消',
              action: function (dialogRef) {
                dialogRef.close();
              }
            }, {
              label: '<i class="ace-icon fa fa-check bigger-110">确定</i>',
              cssClass: 'btn-primary',
              action: function (dialogRef) {
                if ($('#validation-form1').valid()) {
                  var params = $("#validation-form1").serialize();
                  $.ajax({
                    url: grid_edit_url,
                    data: params,
                    type: 'POST',
                    cache: false,
                    dataType: 'json',
                    success: function (result) {
                      if (result.success) {
                        $(grid_selector).trigger('reloadGrid');
                        reloadTree();
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
        },
        error: function () {
          toastr.error('未知错误，请联系管理员');
        }
      });
    }

    function delForm(id) {
      BootstrapDialog.confirm('你确定要删除吗？', function (result) {
        if (result) {
          $.ajax({
            url: grid_del_url + id,
            type: 'POST',
            cache: false,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                $(grid_selector).trigger('reloadGrid');
                reloadTree();
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

    function addForm2() {
      $("#validation-form2")[0].reset();
      $(".select2").select2("val", "");

      var groupId = $("#groupId").val();
      if (!groupId) {
        BootstrapDialog.alert('请先选择机构');
        return;
      } else {
        $("#form-groupId").val($("#groupId").val());
      }

      BootstrapDialog.show({
        title: '<i class="ace-icon fa fa-plus-circle bigger-110"></i>&nbsp;新增用户',
        message: $('#dialog-form2'),
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
            if ($('#validation-form2').valid()) {
              var params = $("#validation-form2").serialize();
              $.ajax({
                url: auth_add_url,
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

    function delForm2(id) {
      var groupId = $("#groupId").val();

      BootstrapDialog.confirm('你确定要删除吗？', function (result) {
        if (result) {
          $.ajax({
            url: auth_del_url,
            data: {
              userId: id,
              groupId: groupId
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

    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
      if (clickFlag === 0) {
        $("#parentId").val('');
        $("#groupId").val('');
      } else {
        $("#parentId").val(treeNode.id);
        $("#groupId").val(treeNode.id);
      }
      $("#btn-search1").trigger("click");
      $("#btn-search2").trigger("click");
    }
    ;

    var setting = {
      async: {
        enable: true,
        url: '${ctx}/api/userGroup/tree',
        dataType: "json",
        autoParam: ["id"]
      },
      data: {
        simpleData: {
          enable: true
        }
      },
      view: {
        selectedMulti: false,
        autoCancelSelected: true
      }, callback: {
        onClick: zTreeOnClick,
        onAsyncSuccess: function (event, treeId, treeNode, msg) {
          if (treeNode == null) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            zTree.expandAll(true);
          }
        }
      }
    };

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
      $.fn.zTree.init($('#tree'), setting, null);

      $('.chosen-select').chosen({
        allow_single_deselect: true
      });

      $(window).off('resize.chosen').on('resize.chosen', function () {
        $('.chosen-select').each(function () {
          var $this = $(this);
          $this.next().css({
            'width': $this.parent().width()
          });
        })
      }).trigger('resize.chosen');

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

      $('#validation-form1').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          code: {
            required: true,
            maxlength: 64,
            remote: {
              type: 'POST',
              url: '${ctx}/api/userGroup/checkCode',
              dataType: 'json',
              data: {
                id: function () {
                  return $('#id').val();
                }, code: function () {
                  return $('#form-code').val();
                }
              }
            }
          },
          name: {
            required: true,
            maxlength: 64
          },
          type: {
            required: true
          },
          remark: {
            maxlength: 128
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

      $('#validation-form2').validate({
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
        colNames: ['操作', '机构编码', '机构名称', '机构类型'],
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
                + '<div title="编辑" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="blue" href="javascript:void(0);" onClick="editForm(\'' + rowObject.id + '\')"><i class="ace-icon fa fa-pencil bigger-140"></i></a></div>'
                + '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red" href="javascript:void(0);" onClick="delForm(\'' + rowObject.id + '\')"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'code', index: 'code', width: 100},
          {name: 'name', index: 'name', width: 100},
          {name: 'type', index: 'type', width: 100, formatter: function (cellvalue, options, rowObject) {
            var content;
            switch (cellvalue) {
              case 0: content = '公司'; break;
              case 1: content = '部门'; break;
              case 2: content = '小组'; break;
              case 3: content = '其他'; break;
            }
            return content;
          }}
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

      $(grid_selector).foomei_JqGrid('resize', {container: '.form-search'});

      $("#searchKey1").keydown(function (e) {
        if (e.keyCode == 13) {
          $(grid_selector).foomei_JqGrid('search', {
            "searchKey": $('#searchKey1').val(),
            "parentId": $('#parentId').val()
          });
        }
      });

      $("#btn-search1").click(function () {
        $(grid_selector).foomei_JqGrid('search', {
          "searchKey": $('#searchKey1').val(),
          "parentId": $('#parentId').val()
        });
      });

      $(auth_selector).foomei_JqGrid({
        url: auth_page_url,
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
                + '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red" href="javascript:void(0);" onClick="delForm2(\'' + rowObject.id + '\')"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'loginName', index: 'loginName', width: 80},
          {name: 'name', index: 'name', width: 80},
          {name: 'mobile', index: 'mobile', width: 80},
          {name: 'email', index: 'email', width: 80}
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
              addForm2();
            },
            position: 'first'
          }
        ]
      });

      $(auth_selector).foomei_JqGrid('resize', {container: '.form-search'});

      $("#searchKey2").keydown(function (e) {
        if (e.keyCode == 13) {
          $(auth_selector).foomei_JqGrid('search', {
            "searchKey": $('#searchKey2').val(),
            "groupId": $('#groupId').val()
          });
        }
      });

      $("#btn-search2").click(function () {
        $(auth_selector).foomei_JqGrid('search', {
          "searchKey": $('#searchKey2').val(),
          "groupId": $('#groupId').val()
        });
      });
    })
  </script>
</pageJs>
</html>