<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>系统配置管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
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
          <a href="${ctx}/admin/config">系统配置管理</a>
        </li>
        <li class="active"><c:choose><c:when test='${action == "create"}'>新增配置</c:when><c:when test='${action == "update"}'>修改配置</c:when></c:choose></li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          <c:choose><c:when test='${action == "create"}'>新增配置</c:when><c:when test='${action == "update"}'>修改配置</c:when></c:choose>
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
          <form class="form-horizontal" id="validation-form" action="${ctx}/admin/config/${action}" method="post" role="form">
            <input type="hidden" name="id" id="id" value="${config.id}"/>
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-code"> 键<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="code" value="${config.code}" id="form-code" placeholder="键" class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name"> 名称<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="name" value="${config.name}" id="form-name" placeholder="名称" class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-editable"> 值可修改<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix" style="padding-top: 7px;">
                      <input type="hidden" name="editable" value="${config.editable}" placeholder="值可修改" class="form-control"/>
                      <input type="checkbox" id="form-editable" class="ace ace-switch ace-switch-6"
                             <c:if test="${config.editable}">checked</c:if> />
                      <span class="lbl"></span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-type"> 类型<span class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="type" id="form-type" class="form-control" data-placeholder="类型">
                        <option value="0" <c:if test="${config.type eq 0}">selected</c:if>>Input输入框</option>
                        <option value="1" <c:if test="${config.type eq 1}">selected</c:if>>Textarea文本框</option>
                        <option value="2" <c:if test="${config.type eq 2}">selected</c:if>>Radio单选框</option>
                        <option value="3" <c:if test="${config.type eq 3}">selected</c:if>>Checkbox多选框</option>
                        <option value="4" <c:if test="${config.type eq 4}">selected</c:if>>Select单选框</option>
                        <option value="5" <c:if test="${config.type eq 5}">selected</c:if>>Select多选框</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-params"> 参数 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="params" id="form-params" placeholder='json格式（例如：{"1":"男","2":"女"}），选择时使用' class="form-control">${config.params}</textarea>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-remark"> 备注 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="remark" id="form-remark" placeholder="备注" class="form-control">${config.remark}</textarea>
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
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    jQuery(function ($) {
      $('#form-editable').on('click', function () {
        if (this.checked) {
          $("input[name='editable']").val(true);
        } else {
          $("input[name='editable']").val(false);
        }
      })

      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          code: {
            required: true,
            maxlength: 128,
            remote: {
              type: 'POST',
              url: '${ctx}/api/config/checkCode',
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
          params: {
            required: true,
            maxlength: 64
          },
          remark: {
            maxlength: 128
          }
        },
        messages: {
          code: {
            remote: '键已经被使用'
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
