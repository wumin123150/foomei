<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>个人资料</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/chosen.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/datepicker.min.css" />
  <link rel="stylesheet" href="${ctx}/static/js/webuploader/webuploader.css" />
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

    .img-responsive {
      min-width: 200px;
    }

    .pswState {
      margin-top: 1px;
    }

    .pswState-poor .s1 {
      background-color: #ea9292;
      border: 1px solid #ffffff;
      color: #ffffff;
    }

    .pswState-normal .s1 {
      line-height: 200px;
    }

    .pswState-normal .s1, .pswState-normal .s2 {
      background-color: #f1d93a;
      border: 1px solid #ffffff;
    }

    .pswState-normal .s2 {
      color: #ffffff;
    }

    .pswState-strong .s1, .pswState-strong .s2, .pswState-strong .s3 {
      background-color: #5aac47;
      border: 1px solid #ffffff;
      line-height: 200px;
    }

    .pswState-strong .s3 {
      color: #ffffff;
      line-height: 16px;
    }

    .pswState span {
      background-color: #e4e4e4;
      border: 1px solid #ffffff;
      color: #cecece;
      display: block;
      float: left;
      height: 16px;
      line-height: 16px;
      margin-right: -1px;
      overflow: hidden;
      text-align: center;
      width: 58px;
    }

    .pswState .t1 {
      background-color: #ffffff;
      border: 1px solid #ffffff;
      color: #000000;
      width: 68px;
    }

    .img-responsive {
      width: 200px;
      height: 200px;
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
        <li class="active">个人资料</li>
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          个人资料
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <ul class="nav nav-tabs padding-16">
            <li class="active">
              <a data-toggle="tab" href="#edit-basic">
                <i class="green ace-icon fa fa-pencil-square-o bigger-125"></i>
                基本信息
              </a>
            </li>
            <li>
              <a data-toggle="tab" href="#edit-avatar" id="avatar-tab">
                <i class="blue ace-icon fa fa-photo bigger-125"></i>
                修改头像
              </a>
            </li>
            <li>
              <a data-toggle="tab" href="#edit-password">
                <i class="purple ace-icon fa fa-key bigger-125"></i>
                修改密码
              </a>
            </li>
          </ul>
          <div class="tab-content profile-edit-tab-content">
            <div id="edit-basic" class="tab-pane in active">
              <div class="space-10"></div>
              <form class="form-horizontal" id="basic-form" action="" method="post" role="form">
              <input type="hidden" name="id" id="id" value="${user.id}"/>
              <!-- #section:elements.form -->
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-loginName"> 用户名
                      <span class="input-required">*</span>
                    </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="clearfix">
                        <input type="text" name="loginName" value="${user.loginName}" id="form-loginName" placeholder="建议用手机/邮箱注册" class="form-control" disabled/>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-name">
                      姓名<span class="input-required">*</span>
                    </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="clearfix">
                        <input type="text" name="name" value="${user.name}" id="form-name" placeholder="姓名" class="form-control"/>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right"> 性别 </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="clearfix">
                        <div class="radio">
                          <label>
                            <input type="radio" name="sex" class="ace" value="0" <c:if test="${user.sex eq 0}"> checked</c:if>>
                            <span class="lbl">未知</span>
                          </label>
                          <label>
                            <input type="radio" name="sex" class="ace" value="1" <c:if test="${user.sex eq 1}"> checked</c:if>>
                            <span class="lbl">男</span>
                          </label>
                          <label>
                            <input type="radio" name="sex" class="ace" value="2" <c:if test="${user.sex eq 2}"> checked</c:if>>
                            <span class="lbl">女</span>
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-birthday"> 出生日期 </label>
                    <div class="col-xs-12 col-sm-8">
                      <input type="text" name="birthday" value="<fmt:formatDate value='${user.birthday}'/>" id="form-birthday" placeholder="出生日期" class="form-control date-picker"/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-mobile">
                      手机<span class="input-required">*</span>
                    </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <i class="ace-icon fa fa-phone"></i>
                        </span>

                        <input type="text" name="mobile" value="${user.mobile}" id="form-mobile" placeholder="手机" class="form-control input-mask-phone"/>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-email">
                      邮箱<span class="input-required">*</span>
                    </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <i class="ace-icon fa fa-envelope"></i>
                        </span>
                        <input type="text" name="email" value="${user.email}" id="form-email" placeholder="邮箱" class="form-control"/>
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
            </div>
            <div id="edit-avatar" class="tab-pane">
              <div class="space-10"></div>
              <form class="form-horizontal" id="avatar-form" action="" method="post" role="form">
                <input type="hidden" name="id" value="${user.id}"/>
                <!-- #section:elements.form -->
                <div class="row">
                  <div class="col-xs-12 col-sm-12 center">
                    <span class="profile-picture">
                      <c:if test="${not empty user.avatar}">
                        <img class="img-responsive" src="${ctx}/avatar/${user.id}" onerror="this.src='${ctx}/static/avatars/avatar6.jpg'"/>
                      </c:if>
                      <c:if test="${empty user.avatar}">
                        <img class="img-responsive" src="${ctx}/static/avatars/avatar6.jpg"/>
                      </c:if>
                    </span>
                    <div id="picker">选择头像</div>
                    <input type="hidden" name="avatarId" id="avatarId"/>
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
            </div>
            <div id="edit-password" class="tab-pane">
              <div class="space-10"></div>
              <form class="form-horizontal" id="password-form" action="" method="post" role="form">
                <input type="hidden" name="id" value="${user.id}"/>
                <!-- #section:elements.form -->
                <div class="row">
                  <div class="col-xs-12 col-sm-6">
                    <div class="form-group">
                      <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-password">
                        原密码<span class="input-required">*</span>
                      </label>
                      <div class="col-xs-12 col-sm-8">
                        <div class="clearfix">
                          <input type="password" name="password" id="form-password" placeholder="原密码" class="form-control"/>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12 col-sm-6">
                    <div class="form-group">
                      <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-newPassword">
                        新密码<span class="input-required">*</span>
                      </label>
                      <div class="col-xs-12 col-sm-8">
                        <div class="clearfix">
                          <input type="password" name="newPassword" id="form-newPassword" placeholder="6~16个字符，区分大小写" class="form-control"/>
                        </div>
                        <div class="pswState">
                          <span class="t1">密码强度：</span>
                          <span class="s1">弱</span>
                          <span class="s2">中</span>
                          <span class="s3">强</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col-xs-12 col-sm-6">
                    <div class="form-group">
                      <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-repassword">
                        确认密码<span class="input-required">*</span>
                      </label>
                      <div class="col-xs-12 col-sm-8">
                        <div class="clearfix">
                          <input type="password" name="repassword" id="form-repassword" placeholder="请再次填写密码" class="form-control"/>
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
  <script src="${ctx}/static/js/jquery.validate.foomei.js"></script>
  <script src="${ctx}/static/js/messages_zh.min.js"></script>
  <script src="${ctx}/static/js/date-time/bootstrap-datepicker.min.js"></script>
  <script src="${ctx}/static/js/webuploader/webuploader.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var activeUploader = false;
    jQuery(function ($) {
      $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var activeTab = $(e.target).prop('id');
        if(activeTab == 'avatar-tab' && !activeUploader) {
          activeUploader = true;

          var uploader = WebUploader.create({
            swf : '${ctx}/static/js/webuploader/Uploader.swf',
            pick : "#picker",
            auto : true,
            disableGlobalDnd : true,
            chunked : true,
            server : '${ctx}/api/annex/save',
            accept : {
              title : 'Images',
              extensions : 'gif,jpg,jpeg,bmp,png',
              mimeTypes : 'image/*'
            },
            fileNumLimit : 1,
            fileSingleSizeLimit : 5 * 1024 * 1024
          });

          uploader.on("uploadSuccess", function(file, response) {
            var path = "${ctx}" + response.data.requestURI;
            $('.img-responsive').prop('src', path);
            $('#avatarId').val(response.data.id);
            uploader.reset();
          });
        }
      });

      $('.date-picker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'cn',
        todayBtn: 'linked',
        autoclose: true
      });

      $('#form-newPassword').bind('keyup', function (event) {
        var psw = $(this).val();
        var poor = /(^[0-9]+$)|(^[A-Z]+$)|(^[a-z]+$)|(^[`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
        var normal = /(^[0-9A-Z]+$)|(^[0-9a-z]+$)|(^[0-9`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[A-Za-z]+$)|(^[A-Z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[a-z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
        ;
        if (psw == '') {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").removeClass("pswState-strong");
        } else if (poor.test(psw)) {
          $('.pswState').removeClass("pswState-normal").removeClass("pswState-strong").addClass("pswState-poor");
        } else if (normal.test(psw)) {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-strong").addClass("pswState-normal");
        } else {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").addClass("pswState-strong");
        }
      });

      $('#basic-form').validate({
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
          //form.submit();
          var data = $('#basic-form').serialize();
          $.ajax({
            url: '${ctx}/api/account/change',
            type: 'POST',
            cache: false,
            data: data,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                toastr.success('保存成功');
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

      $('#avatar-form').submit(function(){
        var avatarId = $('#avatarId').val();
        if(avatarId == '') {
          toastr.error('请选择头像');
          return false;
        }

        $.ajax({
          url: '${ctx}/api/account/setAvatar',
          type: 'POST',
          cache: false,
          data: {avatarId: avatarId},
          dataType: 'json',
          success: function (result) {
            if (result.success) {
              toastr.success('保存成功');
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
      });

      $('#password-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          password: {
            required: true
          },
          newPassword: {
            required: true,
            rangelength: [6, 16]
          },
          repassword: {
            equalTo: '#form-newPassword'
          }
        },
        messages: {
          newPassword: {
            rangelength: '密码长度应为{0}~{1}个字符'
          },
          repassword: {
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
          var data = $('#password-form').serialize();
          $.ajax({
            url: '${ctx}/api/account/changePwd',
            type: 'POST',
            cache: false,
            data: data,
            dataType: 'json',
            success: function (result) {
              if (result.success) {
                toastr.success('保存成功');
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
