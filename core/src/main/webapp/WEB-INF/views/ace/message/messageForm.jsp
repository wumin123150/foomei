<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>消息管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .input-required {
      margin-left: 2px;
      color: #c00;
    }

    ul.ztree {
      margin-top: 0;
      border: 1px solid #d5d5d5;
      background: #fff;
      width: 100% !important;
      height: 200px;
      overflow-y: scroll;
      overflow-x: auto;
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
          <a href="${ctx}/admin/message">消息管理</a>
        </li>
        <li class="active"><c:choose><c:when test='${action == "create"}'>新增消息</c:when><c:when
          test='${action == "update"}'>修改消息</c:when></c:choose></li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          <c:choose><c:when test='${action == "create"}'>新增消息</c:when><c:when test='${action == "update"}'>修改消息</c:when></c:choose>
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <form class="form-horizontal" id="validation-form" action="" method="post" role="form">
            <input type="hidden" name="id" id="id" value="${message.id}"/>
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12 col-sm-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-content">
                    内容<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="content" id="form-content" placeholder="内容" class="form-control" rows="5">${message.content}</textarea>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-receiver">
                    接收人<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="userNames" value="${userNames}" id="form-receiver" readonly
                             data-placeholder="接收人" class="form-control" style="background: #fff!important;"
                             onclick="showMenu();"/>
                      <div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index: 1;">
                        <ul id="tree" class="ztree"></ul>
                      </div>
                      <input type="text" name="users" id="users" class="hide"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="clearfix form-actions">
              <div class="col-sm-offset-4 col-sm-8 col-xs-offset-3 col-xs-9">
                <button class="btn btn-info btn-sm" type="submit">
                  <i class="ace-icon fa fa-check bigger-110"></i>
                  保存
                </button>

                &nbsp; &nbsp; &nbsp;
                <button class="btn btn-sm" type="reset" onclick="history.back()">
                  <i class="ace-icon fa fa-undo bigger-110"></i>
                  返回
                </button>
              </div>
            </div>
          </form>
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
  <script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
  <script src="${ctx}/static/js/zTree/jquery.ztree.excheck.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var setting = {
      check: {
        enable: true,
        chkboxType: {"Y": "ps", "N": "ps"}
      },
      data: {
        simpleData: {
          enable: true,
          idKey: "id",
          pIdKey: "parentId",
          rootPId: null
        }
      },
      view: {
        selectedMulti: true,
        autoCancelSelected: true
      }, callback: {
        onCheck: function (event, treeId, treeNode) {
          var zTree = $.fn.zTree.getZTreeObj("tree");
          var nodes = zTree.getCheckedNodes(true);
          var names = new Array(), ids = new Array();

          $("#users").val('');
          for (var i = 0, l = nodes.length; i < l; i++) {
            if(!nodes[i].isParent) {
              var id = nodes[i].id.substr(nodes[i].id.indexOf('_') + 1);
              if(ids.indexOf(id) == -1) {
                names.push(nodes[i].name);
                ids.push(id);
              }
            }
          }
          $("#form-receiver").val(names.join(','));
          $("#users").val(ids.join(','));
        }
      }
    };

    function showMenu() {
      $("#menuContent").css({width: $("#form-receiver").outerWidth()}).slideDown("fast");
      $("body").bind("mousedown", onBodyDown);
    }

    function hideMenu() {
      $("#menuContent").fadeOut("fast");
      $("body").unbind("mousedown", onBodyDown);
    }

    function onBodyDown(event) {
      if (!(event.target.id == "form-receiver" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
      }
    }

    jQuery(function ($) {
      $.get('${ctx}/api/userGroup/list', function(result) {
        if(result.success && result.data.length) {
          for(var i =0; i < result.data.length; i++) {
            result.data[i].open = true;
            result.data[i].isParent = true;
          }
          $.fn.zTree.init($('#tree'), setting, result.data);

          for(var i =0; i < result.data.length; i++) {
            let groupId = result.data[i].id;
            $.get('${ctx}/api/membership/list?groupId=' + groupId, function(result) {
              if(result.success && result.data.length > 0) {
                for (var i = 0; i < result.data.length; i++) {
                  result.data[i].id = groupId + '_' + result.data[i].id;
                }
                var zTree = $.fn.zTree.getZTreeObj("tree");
                var parentNode = zTree.getNodeByParam("id", groupId, null);
                zTree.addNodes(parentNode, result.data, true);
              }
            })
          }
        }
      });

      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          content: {
            required: true,
            maxlength: 200
          },
          users: {
            required: true
          }
        },
        messages: {

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
          //form.submit();
          var data = $('#validation-form').serialize();
          $.ajax({
            url: '${ctx}/api/messageText/${action}',
            type: 'POST',
            cache: false,
            data: data,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                toastr.success('保存成功');
                setTimeout(function(){
                  history.back();
                },500);
              } else {
                toastr.error(result.message);
                console.log(result.debug);
              }
            },
            error: function () {
              toastr.error('未知错误，请联系管理员');
            }
          });
          return false;
        },
        invalidHandler: function (form) {
        }
      });
    })
  </script>
</pageJs>
</html>
