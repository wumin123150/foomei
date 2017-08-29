<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
	<title>404错误</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="utf-8" />
	<meta name="description" content="Common form elements and layouts" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">

	<!-- bootstrap & fontawesome -->
	<link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${ctx}/static/css/font-awesome.min.css" />

	<!-- text fonts -->
	<link rel="stylesheet" href="${ctx}/static/css/ace-fonts.min.css" />

	<!-- ace styles -->
	<link rel="stylesheet" href="${ctx}/static/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />

	<!--[if lte IE 9]>
		<link rel="stylesheet" href="${ctx}/static/css/ace-part2.min.css" class="ace-main-stylesheet" />
	<![endif]-->

	<!--[if lte IE 9]>
	  <link rel="stylesheet" href="${ctx}/static/css/ace-ie.min.css" />
	<![endif]-->

	<!-- ace settings handler -->
	<script src="${ctx}/static/js/ace-extra.min.js"></script>

	<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

	<!--[if lte IE 8]>
	<script src="${ctx}/static/js/html5shiv.min.js"></script>
	<script src="${ctx}/static/js/respond.min.js"></script>
	<![endif]-->
</head>	
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="page-content">
			<div class="row">
				<div class="col-xs-12">
					<!-- PAGE CONTENT BEGINS -->
					<div class="error-container">
						<div class="well">
							<h1 class="grey lighter smaller"><span class="blue bigger-125"> <i class="ace-icon fa fa-sitemap"></i> 404 </span> 抱歉！页面无法访问…… </h1>
							<hr />
							<h3 class="lighter smaller">请检查地址是否完整或存在多余字符</h3>
							<div class="space"></div>
							<div class="center" style="padding-top: 200px;">
								<a href="javascript:history.back()" class="btn btn-grey"> <i class="ace-icon fa fa-arrow-left"></i> 返回 </a>
								<a href="${ctx}/index" class="btn btn-primary"> <i class="ace-icon fa fa-tachometer"></i> 首页 </a>
							</div>
						</div>
					</div>
					<!-- PAGE CONTENT ENDS -->
				</div><!-- /.col -->
			</div><!-- /.row -->
		</div><!-- /.page-content -->
	</div><!-- /.main-container -->

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

	<!-- ace scripts -->
	<script src="${ctx}/static/js/ace/elements.scroller.js"></script>
	<script src="${ctx}/static/js/ace/elements.colorpicker.js"></script>
	<script src="${ctx}/static/js/ace/elements.fileinput.js"></script>
	<script src="${ctx}/static/js/ace/elements.typeahead.js"></script>
	<script src="${ctx}/static/js/ace/elements.wysiwyg.js"></script>
	<script src="${ctx}/static/js/ace/elements.spinner.js"></script>
	<script src="${ctx}/static/js/ace/elements.treeview.js"></script>
	<script src="${ctx}/static/js/ace/elements.wizard.js"></script>
	<script src="${ctx}/static/js/ace/elements.aside.js"></script>
	<script src="${ctx}/static/js/ace/ace.js"></script>
	<script src="${ctx}/static/js/ace/ace.ajax-content.js"></script>
	<script src="${ctx}/static/js/ace/ace.touch-drag.js"></script>
	<script src="${ctx}/static/js/ace/ace.sidebar.js"></script>
	<script src="${ctx}/static/js/ace/ace.sidebar-scroll-1.js"></script>
	<script src="${ctx}/static/js/ace/ace.submenu-hover.js"></script>
	<script src="${ctx}/static/js/ace/ace.widget-box.js"></script>
	<script src="${ctx}/static/js/ace/ace.settings.js"></script>
	<script src="${ctx}/static/js/ace/ace.settings-rtl.js"></script>
	<script src="${ctx}/static/js/ace/ace.settings-skin.js"></script>
	<script src="${ctx}/static/js/ace/ace.widget-on-reload.js"></script>
	<script src="${ctx}/static/js/ace/ace.searchbox-autocomplete.js"></script>

</body>			
</html>
