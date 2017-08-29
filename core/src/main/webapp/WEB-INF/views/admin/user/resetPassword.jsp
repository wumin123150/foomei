<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>用户管理</title>
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
		.pswState {
			
		}
		.pswState-poor {
		    color: #d16e6c;
		}
		.pswState-normal {
		    color: #000;
		}
		.pswState-strong {
		    color: #009944;
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
						try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
					</script>

					<ul class="breadcrumb">
						<li>
							<i class="ace-icon fa fa-home home-icon"></i>
							<a href="${ctx}/admin/index">首页</a>
						</li>
						<li>
							<a href="${ctx}/admin/user">用户管理</a>
						</li>
						<li class="active">重置密码</li>
					</ul><!-- /.breadcrumb -->
				</div>

				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">
					<div class="page-header">
						<h1>
							重置密码
						</h1>
					</div><!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<c:if test="${error != null}">
					        <div class="alert alert-danger">
								<button type="button" class="close" data-dismiss="alert">
									<i class="ace-icon fa fa-times"></i>
								</button>

								<i class="ace-icon fa fa-times"></i>
								${error.defaultMessage}
							</div>
							</c:if>
							<form class="form-horizontal" id="validation-form" action="${ctx}/admin/user/reset" method="post" role="form">
								<input type="hidden" name="id" id="id" value="${user.id}"/>
								<!-- #section:elements.form -->
								<div class="row"> 
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-loginName"> 用户名<span class="input-required">*</span> </label>
											<div class="col-xs-12 col-sm-8">
												<div class="clearfix">
													<input type="text" name="loginName" value="${user.loginName}" id="form-loginName" placeholder="用户名" class="form-control" readonly/>
												</div>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name"> 姓名<span class="input-required">*</span> </label>
											<div class="col-xs-12 col-sm-8">
												<div class="clearfix">
													<input type="text" name="name" value="${user.name}" id="form-name" placeholder="姓名" class="form-control" disabled/>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row"> 
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-password"> 密码<span class="input-required">*</span> </label>
											<div class="col-xs-12 col-sm-8">
												<div class="input-group">
													<input type="password" name="plainPassword" id="form-password" placeholder="6~16个字符，区分大小写" class="form-control" />
                                                    <span class="input-group-addon pswState">
                                                      &nbsp;&nbsp;&nbsp;
                                                    </span>
												</div>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-repassword"> 确认密码<span class="input-required">*</span> </label>
											<div class="col-xs-12 col-sm-8">
												<div class="clearfix">
													<input type="password" name="repassword" id="form-repassword" placeholder="请再次填写密码" class="form-control" />
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
		jQuery(function($) {
			$('#form-password').bind('keyup',function(event){
				var psw = $(this).val();
      	var poor=/(^[0-9]+$)|(^[A-Z]+$)|(^[a-z]+$)|(^[`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
      	var normal=/(^[0-9A-Z]+$)|(^[0-9a-z]+$)|(^[0-9`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[A-Za-z]+$)|(^[A-Z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[a-z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;;
      	if(psw == '') {
      		$('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").removeClass("pswState-strong").html("&nbsp;&nbsp;&nbsp;");
      	} else if(poor.test(psw)) {
      		$('.pswState').removeClass("pswState-normal").removeClass("pswState-strong").addClass("pswState-poor").html("弱");
      	} else if(normal.test(psw)) {
      		$('.pswState').removeClass("pswState-poor").removeClass("pswState-strong").addClass("pswState-normal").html("中");
      	} else {
      		$('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").addClass("pswState-strong").html("强");
      	}
      });
			
			$('#validation-form').validate({
				errorElement: 'div',
				errorClass: 'help-block',
				focusInvalid: false,
				ignore: "",
				rules: {
					plainPassword:{
						required: true,
						rangelength: [6,16]
					},
					repassword:{
						equalTo: '#form-password'
					}
				},
				messages: {
					plainPassword:{
						rangelength: '密码长度应为{0}~{1}个字符'
					},
					repassword:{
						equalTo: '必须与密码保持一致'
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
					if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
						var controls = element.closest('div[class*="col-"]');
						if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
						else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
					}
					else if(element.is('.select2')) {
						error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
					}
					else if(element.is('.chosen-select')) {
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