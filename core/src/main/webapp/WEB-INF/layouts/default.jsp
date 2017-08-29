<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:write property='title' /> - ${iApplication}</title>
	
	<!-- 
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	 -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="utf-8" />
	<meta name="description" content="${iApplication}" />
	<meta name="keywords" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<link type="image/x-icon" href="${ctx}/static/img/favicon.ico" rel="shortcut icon">

	<!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css" />
  <link rel="stylesheet" href="${ctx}/static/css/font-awesome.min.css" />

	<sitemesh:write property='pluginCss' />

	<sitemesh:write property='pageCss' />

	<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

	<!--[if lte IE 8]>
	<script src="${ctx}/static/js/html5shiv.min.js"></script>
	<script src="${ctx}/static/js/respond.min.js"></script>
	<![endif]-->
</head>	

<body>
	<%@ include file="/WEB-INF/layouts/header.jsp"%>

	<div class="main-container" id="main-container">
     <sitemesh:write property='body' />
     
     <%@ include file="/WEB-INF/layouts/footer.jsp"%>
  </div>

	<!-- basic scripts -->

	<!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery || document.write("<script src='${ctx}/static/js/jquery.min.js'>"+"<"+"/script>");
	</script>
	<!-- <![endif]-->

	<!--[if IE]>
	<script type="text/javascript">
		window.jQuery || document.write("<script src='${ctx}/static/js/jquery1x.min.js'>"+"<"+"/script>");
	</script>
	<![endif]-->
	<script type="text/javascript">
		if('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/static/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
	</script>
	<script src="${ctx}/static/js/bootstrap.min.js"></script>

	<sitemesh:write property='pluginJs' />

	<sitemesh:write property='pageJs' />
</body>			
</html>