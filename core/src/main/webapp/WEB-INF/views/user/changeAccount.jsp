<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>修改账户</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/chosen.min.css"/>
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .input-required {
      margin-left: 2px;
      color: #c00;
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
          <a href="${ctx}/${action}/index">首页</a>
        </li>
        <li class="active">修改账户</li>
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          修改账户
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
          <form class="form-horizontal" id="validation-form" action="${ctx}/${action}/changeAccount" method="post"
                role="form">
            <input type="hidden" name="id" id="id" value="${user.id}"/>
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-loginName"> 用户名<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="loginName" value="${user.loginName}" id="form-loginName"
                             placeholder="建议用手机/邮箱注册" class="form-control" disabled/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name"> 姓名<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="name" value="${user.name}" id="form-name" placeholder="姓名"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-mobile"> 手机<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="input-group">
													<span class="input-group-addon">
														<i class="ace-icon fa fa-phone"></i>
													</span>

                      <input type="text" name="mobile" value="${user.mobile}" id="form-mobile" placeholder="手机"
                             class="form-control input-mask-phone"/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-email"> 邮箱<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="input-group">
													<span class="input-group-addon">
														<i class="ace-icon fa fa-envelope"></i>
													</span>
                      <input type="text" name="email" value="${user.email}" id="form-email" placeholder="邮箱"
                             class="form-control"/>
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
  <script src="${ctx}/static/js/jquery.validate-methods.me.js"></script>
  <script src="${ctx}/static/js/messages_zh.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    jQuery(function ($) {
      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          name: {
            required: true,
            maxlength: 64
          },
          phone: {
            required: true,
            mobile: true
          },
          email: {
            required: true,
            email: true
          }
        },
        messages: {},
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
