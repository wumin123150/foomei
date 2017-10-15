<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <li class="active">参数设置</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->

          <div class="row">
            <div class="col-xs-offset-1 col-xs-10">
              <div class="widget-box">
                <div class="widget-header">
                  <h4 class="widget-title lighter smaller">参数设置</h4>
                  <div class="widget-toolbar no-border">
                    <button class="btn btn-minier" onclick="history.back()"><i class="ace-icon fa fa-undo"></i>返回
                    </button>
                  </div>
                </div>

                <div class="widget-body">
                  <div class="widget-main padding-8">
                    <form class="form-horizontal" id="validation-form" action="${ctx}/admin/config/updateAll"
                          method="post" role="form">
                      <!-- #section:elements.form -->
                      <c:forEach var="config" items="${configs}" varStatus="status">
                        <div class="row">
                          <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                              <label class="col-xs-12 col-sm-12" for="form-${config.code}">
                                  ${config.name}（代码：${config.code}）
                                <c:if test="${fn:length(config.remark)>1}">
                                  <span class="help-button" data-rel="popover" data-trigger="hover"
                                        data-placement="bottom" data-content="${config.remark}" title="参数说明">?</span>
                                </c:if>
                              </label>
                              <div class="col-xs-12 col-sm-12">
                                <input type="hidden" name="configs[${status.index}].id" value="${config.id}">
                                <c:choose>
                                  <c:when test="${config.type eq 0}">
                                    <input type="text" name="configs[${status.index}].value" id="form-${config.code}"
                                           class="form-control" value="${config.value}" <c:if
                                      test="${not config.editable}"> readonly="true"</c:if>
                                    />
                                  </c:when>
                                  <c:when test="${config.type eq 1}">
                                    <textarea name="configs[${status.index}].value" id="form-${config.code}"
                                              class="form-control" rows="5" <c:if
                                      test="${not config.editable}"> readonly="true"</c:if>>${config.value}</textarea>
                                  </c:when>
                                  <c:when test="${config.type eq 2}">
                                    <div class="radio">
                                      <c:forEach items="${config.options}" var="option">
                                        <label>
                                          <input type="radio" name="configs[${status.index}].value" class="ace"
                                                 value="${option.key}"
                                          <c:if test="${config.value eq option.key}"> checked</c:if>
                                          <c:if test="${not config.editable}"> readonly="true"</c:if>>
                                          <span class="lbl">${option.value}</span>
                                        </label>
                                      </c:forEach>
                                    </div>
                                  </c:when>
                                  <c:when test="${config.type eq 3}">
                                    <div class="checkbox">
                                      <c:set value="${fn:split(config.value, ',')}" var="values"/>
                                      <c:forEach items="${config.options}" var="option">
                                        <c:set value="false" var="selectedConfig"/>
                                        <c:forEach items="${values}" var="value">
                                          <c:if test="${value eq option.key}">
                                            <c:set value="true" var="selectedConfig"/>
                                          </c:if>
                                        </c:forEach>
                                        <label>
                                          <input type="checkbox" name="configs[${status.index}].value" class="ace"
                                                 value="${option.key}"
                                          <c:if test="${selectedConfig}"> checked</c:if>
                                          <c:if test="${not config.editable}"> readonly="true"</c:if>>
                                          <span class="lbl">${option.value}</span>
                                        </label>
                                      </c:forEach>
                                    </div>
                                  </c:when>
                                  <c:when test="${config.type eq 4}">
                                    <select name="configs[${status.index}].value" id="form-${config.code}"
                                            class="form-control"<c:if
                                      test="${not config.editable}"> readonly="true"</c:if>>
                                      <c:forEach items="${config.options}" var="option">
                                        <option value="${option.key}"<c:if
                                          test="${config.value eq option.key}"> selected</c:if>>${option.value}</option>
                                      </c:forEach>
                                    </select>
                                  </c:when>
                                  <c:when test="${config.type eq 5}">
                                    <c:set value="${fn:split(config.value, ',')}" var="values"/>
                                    <select name="configs[${status.index}].value" id="form-${config.code}"
                                            class="form-control" multiple<c:if
                                      test="${not config.editable}"> readonly="true"</c:if>>
                                      <c:forEach items="${config.options}" var="option">
                                        <c:set value="false" var="selectedConfig"/>
                                        <c:forEach items="${values}" var="value">
                                          <c:if test="${value eq option.key}">
                                            <c:set value="true" var="selectedConfig"/>
                                          </c:if>
                                        </c:forEach>
                                        <option value="${option.key}"<c:if
                                          test="${selectedConfig}"> selected</c:if>>${option.value}</option>
                                      </c:forEach>
                                    </select>
                                  </c:when>
                                </c:choose>
                              </div>
                            </div>
                          </div>
                        </div>
                      </c:forEach>
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
                  </div>
                </div>
              </div>
            </div>
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
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    jQuery(function ($) {
      $('[data-rel=popover]').popover({container: 'body'});

      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {},
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
