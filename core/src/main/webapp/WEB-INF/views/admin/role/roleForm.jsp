<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>角色管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/bootstrap-duallistbox.min.css"/>
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .input-required {
      margin-left: 2px;
      color: #c00;
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
        <li class="active"><c:choose><c:when test='${action == "create"}'>新增角色</c:when><c:when
          test='${action == "update"}'>修改角色</c:when></c:choose></li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          <c:choose><c:when test='${action == "create"}'>新增角色</c:when><c:when test='${action == "update"}'>修改角色</c:when></c:choose>
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <c:if test="${not empty errors}">
            <c:forEach items="${errors.fieldErrors}" var="error">
              <div class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert">
                  <i class="ace-icon fa fa-times"></i>
                </button>

                <i class="ace-icon fa fa-times"></i>
                  ${error.defaultMessage}
              </div>
            </c:forEach>
            <c:forEach items="${errors.globalErrors}" var="error">
              <div class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert">
                  <i class="ace-icon fa fa-times"></i>
                </button>

                <i class="ace-icon fa fa-times"></i>
                  ${error.defaultMessage}
              </div>
            </c:forEach>
          </c:if>
          <form class="form-horizontal" id="validation-form" action="${ctx}/admin/role/${action}" method="post"
                role="form">
            <input type="hidden" name="id" id="id" value="${role.id}"/>
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-code">
                    编码<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="code" value="${role.code}" id="form-code" placeholder="编码"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name">
                    名称<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="name" value="${role.name}" id="form-name" placeholder="名称"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-3 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-permission"> 拥有权限 </label>
                  <div class="col-sm-9">
                    <div class="hr hr-16 hr-dotted"></div>
                  </div>
                </div>
              </div>
              <div class="col-xs-9 col-sm-5">
                <div class="hr hr-16 hr-dotted"></div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <div class="col-sm-offset-1 col-xs-12 col-sm-10">
                    <!-- #section:plugins/input.duallist -->
                    <select name="permissions" id="form-permission" multiple="multiple" size="10">
                      <c:forEach items="${permissions}" var="permission">
                        <c:set var="selectedPermission" value="false"/>
                        <c:forEach items="${role.permissions}" var="ownPermission">
                          <c:if test="${ownPermission eq permission.code}">
                            <c:set var="selectedPermission" value="true"/>
                          </c:if>
                        </c:forEach>
                        <option value="${permission.id}"
                                <c:if test="${selectedPermission}">selected</c:if>>${permission.name}(${permission.code})
                        </option>
                      </c:forEach>
                    </select>
                    <!-- /section:plugins/input.duallist -->
                  </div>
                </div>
              </div><!-- /.col -->
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
  <script src="${ctx}/static/js/jquery.bootstrap-duallistbox.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    jQuery(function ($) {
      var listbox = $('#form-permission').bootstrapDualListbox({
        filterTextClear: '清除过滤',
        filterPlaceHolder: '过滤',
        infoText: '显示全部纪录，共 {0} 项',
        infoTextFiltered: '过滤出{0}项纪录，共 {1} 项',
        infoTextEmpty: '没有数据',
      });
      listbox.bootstrapDualListbox('getContainer').find('.btn').addClass('btn-white btn-info btn-bold');

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
              url: '${ctx}/api/role/checkCode',
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
          form.submit();
        },
        invalidHandler: function (form) {
        }
      });
    })
  </script>
</pageJs>
</html>
