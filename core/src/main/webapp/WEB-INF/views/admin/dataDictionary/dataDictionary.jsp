<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>数据字典管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/toastr.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    input[type=checkbox].ace.ace-switch.ace-switch-5 + .lbl::before {
      content: "\a0\a0是\a0\a0\a0\a0\a0\a0\a0\a0\a0\a0\a0否";
    }

    .input-required {
      margin-left: 2px;
      color: #c00;
    }

    .gritter-width {
      width: 320px;
    }
  </style>
  <!--[if lte IE 8]>
  <style>
    .tree .tree-selected {
      background-color: #F0F7FC;
    }
  </style>
  <![endif]-->
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
          <a href="${ctx}/admin/dataType">数据字典管理</a>
        </li>
        <li class="active">字典管理</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          字典管理
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->

          <!-- #section:plugins/fuelux.treeview -->
          <div class="row">
            <div class="col-xs-offset-1 col-xs-10">
              <div class="widget-box">
                <div class="widget-header">
                  <h4 class="widget-title lighter smaller">${type.name}(${type.code})</h4>
                  <div class="widget-toolbar no-border">
                    <button class="btn btn-minier btn-success" onclick="reloadTree()"><i
                      class="ace-icon fa fa-refresh"></i>刷新
                    </button>
                    <c:if test="${type.editable}">
                      <button class="btn btn-minier btn-purple" onclick="addForm()"><i
                        class="ace-icon fa fa-plus-circle"></i>新增
                      </button>
                      <button class="btn btn-minier btn-primary" onclick="editForm()"><i
                        class="ace-icon fa fa-pencil"></i>修改
                      </button>
                      <button class="btn btn-minier btn-danger" onclick="deleteConfirm()"><i
                        class="ace-icon fa fa-trash-o"></i>删除
                      </button>
                    </c:if>
                    <button class="btn btn-minier" onclick="history.back()"><i
                      class="ace-icon fa fa-undo"></i>返回
                    </button>
                  </div>
                </div>

                <div class="widget-body">
                  <div class="widget-main padding-8">
                    <ul id="tree" class="ztree"></ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div id="dialog-container" class="hide">
            <div id="dialog-form">
              <form class="form-horizontal" id="validation-form" action="#" method="post" role="form">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="typeId" id="typeId" value="${type.id}"/>
                <input type="hidden" name="typeCode" id="typeCode" value="${type.code}"/>
                <input type="hidden" name="parentId" id="parentId"/>
                <input type="hidden" name="item"/>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-code"> 编码<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="code" id="form-code" placeholder="编码"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name"> 名称<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="name" id="form-name" placeholder="名称"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right"
                         for="form-priority"> 序号<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="priority" id="form-priority" placeholder="序号"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-item">
                    无子节点<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix" style="padding-top: 7px;">
                      <input type="hidden" name="item" placeholder="可修改" class="form-control"/>
                      <input type="checkbox" id="form-item" class="ace ace-switch ace-switch-5"/>
                      <span class="lbl"></span>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-remark">
                    备注 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                                            <textarea name="remark" id="form-remark" placeholder="备注 "
                                                      class="form-control"></textarea>
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
  <script src="${ctx}/static/js/jquery-ui.min.js"></script>
  <script src="${ctx}/static/js/jquery.validate.min.js"></script>
  <script src="${ctx}/static/js/additional-methods.min.js"></script>
  <script src="${ctx}/static/js/messages_zh.min.js"></script>
  <script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var setting = {
      async: {
        enable: true,
        url: '${ctx}/api/dataDictionary/tree',
        dataType: "json",
        autoParam: ["id"],
        otherParam: {
          "typeCode": "${type.code}"
        }
      },
      data: {
        simpleData: {
          enable: true
        }
      },
      view: {
        selectedMulti: false,
        autoCancelSelected: true,
        showIcon: false
      },
      callback: {
        onAsyncSuccess: function (event, treeId, treeNode, msg) {
          if (treeNode == null) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            zTree.expandAll(true);
          }
        }
      }
    };

    var reloadTree = function () {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      treeObj.refresh();
    }

    jQuery(function ($) {
      $.fn.zTree.init($('#tree'), setting, null);

      $('#validation-form').validate({
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
              url: '${ctx}/api/dataDictionary/checkCode',
              dataType: 'json',
              data: {
                id: function () {
                  return $('#id').val();
                }, typeCode: '${type.code}',
                code: function () {
                  return $('#form-code').val();
                }
              }
            }
          },
          name: {
            required: true,
            maxlength: 64
          },
          priority: {
            required: true,
            digits: true
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
          $(e).closest('.form-group').removeClass('has-error'); //.addClass('has-info');
          $(e).remove();
        },
        errorPlacement: function (error, element) {
          if (element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
            var controls = element.closest('div[class*="col-"]');
            if (controls.find(':checkbox,:radio').length > 1) controls.append(error);
            else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
          } else if (element.is('.select2')) {
            error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
          } else if (element.is('.chosen-select')) {
            error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
          } else error.insertAfter(element.parent());
        },
        submitHandler: function (form) {
        },
        invalidHandler: function (form) {
        }
      });
    });

    $('#form-item').on('click', function () {
      if (this.checked) {
        $("input[name='item']").val(true);
      } else {
        $("input[name='item']").val(false);
      }
    });

    function addForm() {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      var nodes = treeObj.getSelectedNodes();
      var treeNode = nodes[0];
      if (!treeNode) {
        $("#parentId").val("");
      } else if (!treeNode.isParent) {
        BootstrapDialog.alert('不能在叶子节点下创建子节点');
        return;
      } else {
        $("#parentId").val(treeNode.id);
      }

      var level = treeNode ? (treeNode.level + 2) : 1;
      if (level > ${type.level}) {
        BootstrapDialog.alert('已达到规定层级，不能再创建子节点');
        return;
      }

      $("#id").val("");
      $("#form-code").val("");
      $("#form-name").val("");
      $("#form-priority").val("");
      if (level == ${type.level}) {
        $("input[name='item']").val(true);
        $("#form-item").prop("checked", true);
      } else {
        $("input[name='item']").val(false);
        $("#form-item").prop("checked", false);
      }
      $("#form-remark").val("");

      BootstrapDialog.show({
        title: '<i class="ace-icon fa fa-plus-circle bigger-110"></i>&nbsp;新增数据',
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
                url: "${ctx}/api/dataDictionary/create",
                data: params,
                type: 'POST',
                cache: false,
                dataType: 'json',
                success: function (result) {
                  if (result.success) {
                    treeNode = treeObj.addNodes(treeNode, {
                      id: result.data.id,
                      pId: result.data.pId,
                      isParent: result.data.isParent,
                      name: result.data.name
                    });
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

    function editForm() {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      var nodes = treeObj.getSelectedNodes();
      var treeNode = nodes[0];
      if (!treeNode) {
        BootstrapDialog.alert('请先选择节点');
        return;
      }

      $.ajax({
        url: "${ctx}/api/dataDictionary/get/" + treeNode.id,
        type: 'POST',
        cache: false,
        dataType: 'json',
        success: function (result) {
          $("#id").val(result.data.id);
          $("#parentId").val(result.data.parentId);
          $("#form-code").val(result.data.code);
          $("#form-name").val(result.data.name);
          $("#form-priority").val(result.data.priority);
          if (result.data.item) {
            $("input[name='item']").val(true);
            $("#form-item").prop("checked", true);
          } else {
            $("input[name='item']").val(false);
            $("#form-item").prop("checked", false);
          }
          $("#form-remark").val(result.data.remark);

          BootstrapDialog.show({
            title: '<i class="ace-icon fa fa-plus-circle bigger-110"></i>&nbsp;修改数据',
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
                    url: "${ctx}/api/dataDictionary/update",
                    data: params,
                    type: 'POST',
                    cache: false,
                    dataType: 'json',
                    success: function (result) {
                      if (result.success) {
                        treeNode.name = result.data.name;
                        treeNode.isParent = result.data.isParent;
                        treeObj.updateNode(treeNode);
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

    function deleteConfirm() {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      var nodes = treeObj.getSelectedNodes();
      var treeNode = nodes[0];
      if (!treeNode) {
        BootstrapDialog.alert('请先选择节点');
        return;
      } else if (treeNode.isParent && treeNode.children) {
        BootstrapDialog.alert('请先删除子节点');
        return;
      }

      BootstrapDialog.confirm('你确定要删除吗？', function (result) {
        if (result) {
          $.ajax({
            url: "${ctx}/api/dataDictionary/delete/" + treeNode.id,
            type: 'POST',
            cache: false,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                treeObj.removeNode(treeNode);
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
  </script>
</pageJs>
</html>
