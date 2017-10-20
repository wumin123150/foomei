<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://www.foomei.com/tags/shiro" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <title><sitemesh:write property='title'/> - ${iApplication}</title>
  <!--
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  <meta http-equiv="Cache-Control" content="no-store" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />
   -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <meta name="description" content="Common form elements and layouts"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

  <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">

  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/bootstrap-dialog.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/toastr.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/font-awesome.min.css"/>

  <sitemesh:write property='pluginCss'/>

  <!-- text fonts -->
  <link rel="stylesheet" href="${ctx}/static/css/ace-fonts.min.css"/>

  <!-- ace styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style"/>

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="${ctx}/static/css/ace-part2.min.css" class="ace-main-stylesheet"/>
  <![endif]-->

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="${ctx}/static/css/ace-ie.min.css"/>
  <![endif]-->

  <style>
    .modal-content {
      border-radius: 6px;
      -webkit-box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
      box-shadow: 0 3px 9px rgba(0, 0, 0, .5)
    }

    .modal-header {
      padding: 12px;
    }

    .modal-footer {
      padding-top: 8px;
      padding-bottom: 10px;
    }

    .bootstrap-dialog-footer-buttons > .btn {
      border-width: 4px;
      font-size: 13px;
      padding: 4px 9px;
      line-height: 1.39;
    }
  </style>

  <sitemesh:write property='pageCss'/>

  <!-- ace settings handler -->
  <script src="${ctx}/static/js/ace-extra.min.js"></script>

  <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

  <!--[if lte IE 8]>
  <script src="${ctx}/static/js/html5shiv.min.js"></script>
  <script src="${ctx}/static/js/respond.min.js"></script>
  <![endif]-->
</head>
<body class="no-skin">
<%@ include file="/WEB-INF/layouts/admin-header.jsp" %>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
  <script type="text/javascript">
    try {
      ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
  </script>

  <%@ include file="/WEB-INF/layouts/admin-left.jsp" %>

  <sitemesh:write property='body'/>

  <%@ include file="/WEB-INF/layouts/admin-footer.jsp" %>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
  window.jQuery || document.write("<script src='${ctx}/static/js/jquery.min.js'>" + "<" + "/script>");
</script>
<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
  window.jQuery || document.write("<script src='${ctx}/static/js/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
  if ('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/static/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="${ctx}/static/js/bootstrap.min.js"></script>
<script src="${ctx}/static/js/bootstrap-dialog.min.js"></script>
<script src="${ctx}/static/js/toastr.min.js"></script>
<script>
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = '<i class="ace-icon fa fa-exclamation-triangle"></i>温馨提示';
  BootstrapDialog.DEFAULT_TEXTS['OK'] = '<i class="ace-icon fa fa-check bigger-110"></i>确定';
  BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = '<i class="ace-icon fa fa-times bigger-110"></i>取消';
  BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = '<i class="ace-icon fa fa-check bigger-110"></i>确认';
  toastr.options = {
    closeButton: false,
    debug: false,
    newestOnTop: true,
    progressBar: true,
    positionClass: "toast-top-center",
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "2000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
  };
</script>
<script src="${ctx}/static/js/sockjs.min.js"></script>
<script src="${ctx}/static/js/stomp.min.js"></script>
<script>
  $(document).ready(function(){
    var socket = new SockJS("${ctx}/socket");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('${ctx}/user/<shiro:principal property="id"/>/message',function(greeting){
        alert(greeting);
      });
    });
  })
</script>

<sitemesh:write property='pluginJs'/>

<!-- ace scripts -->
<script src="${ctx}/static/js/ace.min.js"></script>
<script src="${ctx}/static/js/ace-elements.min.js"></script>
<script src="${ctx}/static/js/ace-extra.min.js"></script>

<sitemesh:write property='pageJs'/>
</body>
</html>