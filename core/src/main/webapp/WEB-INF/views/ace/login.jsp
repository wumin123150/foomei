<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <title>登录</title>
  <!--
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  <meta http-equiv="Cache-Control" content="no-store" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />
   -->
  <meta charset="utf-8"/>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta http-equiv="Access-Control-Allow-Origin" content="*">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

  <!--
  <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
  -->

  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="${ctx}/webjars/bootstrap/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/font-awesome.min.css"/>

  <link rel="stylesheet" href="${ctx}/static/css/toastr.min.css"/>

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

  <!-- inline styles related to this page -->
  <style>
    #message-info {
      margin-bottom: 15px;
    }

    #error-info {
      margin-bottom: 15px;
    }

    .no-skin .login-layout {
      background: none;
      background-color: #f9f9f9;
    }

    .login-box .toolbar {
      background: #76B774;
      border-top: 2px solid #759759;
    }

    html, body {
      background-color: #f9f9f9;
    }

    .main-container:before {
      background-color: #f9f9f9;
    }

    .main-container.container:before {
      -webkit-box-shadow: none;
      box-shadow: none;
    }

    .footer .footer-inner .footer-content {
      line-height: 18px;
      background-color: #f9f9f9;
    }

    @media only screen and (max-width: 1024px) {
      .hidden-1024 {
        display: none !important;
      }
    }

    .mobile-login {
      color: #666;
      cursor: pointer;
    }

    .user-login {
      color: #666;
      cursor: pointer;
      margin-bottom: 0px;
    }

    .mobile-title {
      color: #666;
      font-size: 12px;
      font-weight: bold;
    }

    .mobile-tip {
      color: #999;
      font-size: 12px;
    }
  </style>

  <!-- ace settings handler -->
  <script src="${ctx}/static/js/ace-extra.min.js"></script>

  <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

  <!--[if lte IE 8]>
  <script src="${ctx}/static/js/html5shiv.min.js"></script>
  <script src="${ctx}/static/js/respond.min.js"></script>
  <![endif]-->
</head>
<body class="no-skin">
<div class="login-layout light-login main-container container">
  <div class="container">
    <div class="row">
      <div class="col-sm-10 col-sm-offset-1">
        <div class="space-18"></div>
        <div class="login-container">
          <div class="center">
            <h1>
              <span class="grey" id="id-text2">${iApplication}</span>
            </h1>
            <h4 class="blue" id="id-company-text">© ${iCompany}</h4>
          </div>
          <div id="login-box" class="login-box visible widget-box no-border">
            <div class="widget-body">
              <div class="widget-main">
                <h4 class="header green lighter bigger">
                  <i class="ace-icon fa fa-coffee green"></i> 登录${iApplication}
                </h4>

                <div class="space-6"></div>
                <c:if test="${not empty shiroLoginFailure}">
                  <div id="message-info" class="text-danger"><i
                    class='ace-icon fa fa-exclamation-triangle'></i>${shiroLoginFailure}</div>
                </c:if>
                <c:if test="${not empty message}">
                  <div id="message-info" class="text-success"><i class='ace-icon fa fa-check'></i>${message}</div>
                </c:if>
                <form id="login-form" action="${ctx}/admin/login" method="post">
                  <fieldset>
                    <div class="form-group">
                      <label class="block clearfix">
												<span class="block input-icon input-icon-right"> 
													<input id="username" name="username" value="${username}" type="text" class="form-control"
                                 placeholder="手机/邮箱/用户名" style="ime-mode: disabled;" autocomplete="off"
                                 onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"/>
													<i class="ace-icon fa fa-user"></i>
												</span>
                      </label>
                    </div>
                    <div class="form-group">
                      <label class="block clearfix">
												<span class="block input-icon input-icon-right"> 
													<input id="password" name="password" type="password" class="form-control" placeholder="密码"
                                 onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"/>
													<i class="ace-icon fa fa-lock"></i>
												</span>
                      </label>
                    </div>
                    <!--
                    <div class="form-group">
                      <label class="block clearfix">
                        <span class="block input-icon input-icon-right">
                          <input id="captcha" name="captcha" type="text" class="form-control" placeholder="验证码" style="ime-mode:disabled; width:59%!important; display:inline!important;" autocomplete="off" />
                          <img id="loginCaptcha" src="/Kaptcha.jpg" title="点击换一张" onclick="this.src='/Kaptcha.jpg?d='+new Date()*1" width="110" height="34" />
                        </span>
                      </label>
                    </div>
                     -->
                    <div id="user-error-info" class="text-danger"><c:if test="${not empty error}"><i
                      class='fa fa-times-circle'></i>${error.defaultMessage}</c:if>&nbsp;</div>

                    <div class="clearfix">
                      <label class="inline">
                        <input type="checkbox" class="ace">
                        <span class="lbl"> 记住我</span>
                      </label>
                      <button type="submit" class="width-35 pull-right btn btn-sm btn-success">
                        <i class="ace-icon fa fa-key"></i>
                        <span class="bigger-110">登录</span>
                      </button>
                    </div>

                    <div class="space-4"></div>
                  </fieldset>
                </form>
              </div>
              <div class="toolbar clearfix">
                <div>
                  <a href="${ctx}/retrieve" data-target="#forgot-box" class="forgot-password-link">
                    <i class="ace-icon fa fa-arrow-left"></i>
                    忘记密码？
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="footer">
  <div class="footer-inner">
    <!-- #section:basics/footer -->
    <div class="footer-content">
				<span>
					<p class="center">${iCompany}版权所有© 2014-2017</p>
				</span>
    </div>

    <!-- /section:basics/footer -->
  </div>
</div>

<!-- basic scripts -->
<script src="${ctx}/webjars/jquery/jquery.min.js"></script>
<script src="${ctx}/webjars/bootstrap/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<script src="${ctx}/static/js/jquery.validate.min.js"></script>
<script src="${ctx}/static/js/additional-methods.min.js"></script>
<script src="${ctx}/static/js/jquery.validate.foomei.js"></script>
<script src="${ctx}/static/js/messages_zh.min.js"></script>
<script src="${ctx}/static/js/toastr.min.js"></script>
<script>
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

<!-- ace scripts -->
<script src="${ctx}/static/js/ace.min.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
  jQuery(function ($) {
    $('#login-form').validate({
      errorElement: 'div',
      focusInvalid: true,
      ignore: "",
      rules: {
        username: {
          required: true
        },
        password: {
          required: true
        }
      },
      messages: {
        username: {
          required: '请填写手机/邮箱/用户名'
        },
        password: {
          required: '请填写密码'
        }
      },
      highlight: function (e) {
      },
      success: function (e) {
        if ($("#user-error-info > div").html() == "") {
          $("#user-error-info").empty().append("&nbsp;");
        }
      },
      showErrors: function (errorMap, errorList) {
        $(errorList).each(function (i, item) {
          item.message = ("<i class='fa fa-times-circle'></i>" + item.message);
        })

        this.defaultShowErrors();
      },
      errorPlacement: function (error, element) {
        if ($("#message-info").size() > 0) {
          $("#message-info").remove();
        }

        if ($("#user-error-info > div").html() == "") {
          $("#user-error-info").empty().append("&nbsp;");
        }

        if ($("#user-error-info > .error").size() > 0) {
          return false;
        }

        $("#user-error-info").html(error);
      },
      submitHandler: function (form) {
        form.submit();
      },
      invalidHandler: function (form) {
      }
    });
  });
</script>
</body>
</html>