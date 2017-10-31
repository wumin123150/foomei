<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>用户管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/chosen.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
  <link rel="stylesheet" href="${ctx}/static/css/datepicker.min.css" />
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

    ul.ztree {
      margin-top: 0;
      border: 1px solid #d5d5d5;
      background: #fff;
      width: 100% !important;
      height: 200px;
      overflow-y: scroll;
      overflow-x: auto;
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
          <a href="${ctx}/admin/user">用户管理</a>
        </li>
        <li class="active">
          <c:choose>
            <c:when test='${action == "create"}'>新增用户</c:when>
            <c:when test='${action == "update"}'>修改用户</c:when>
          </c:choose>
        </li>
      </ul>
      <!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          <c:choose>
            <c:when test='${action == "create"}'>新增用户</c:when>
            <c:when test='${action == "update"}'>修改用户</c:when>
          </c:choose>
        </h1>
      </div>
      <!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <c:if test="${not empty errors}">
            <c:forEach items="${errors.fieldErrors}" var="error">
              <div class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert"><i class="ace-icon fa fa-times"></i></button>

                <i class="ace-icon fa fa-times"></i>
                  ${error.defaultMessage}
              </div>
            </c:forEach>
            <c:forEach items="${errors.globalErrors}" var="error">
              <div class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert"><i class="ace-icon fa fa-times"></i></button>

                <i class="ace-icon fa fa-times"></i>
                  ${error.defaultMessage}
              </div>
            </c:forEach>
          </c:if>
          <form class="form-horizontal" id="validation-form" action="${ctx}/api/user/${action}" method="post" role="form">
            <input type="hidden" name="id" id="id" value="${user.id}"/>
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-loginName">
                    账号<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="loginName" value="${user.loginName}" id="form-loginName"
                             placeholder="建议用手机/邮箱注册" class="form-control"
                             <c:if test='${action == "update"}'>disabled</c:if> />
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
                      <input type="text" name="name" value="${user.name}" id="form-name" placeholder="姓名"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <c:if test='${action == "create"}'>
              <div class="row" id="pwdDiv">
                <div class="col-xs-12 col-sm-6">
                  <div class="form-group">
                    <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-password">
                      密码<span class="input-required">*</span>
                    </label>
                    <div class="col-xs-12 col-sm-8">
                      <div class="input-group">
                        <input type="password" name="password" id="form-password" placeholder="6~16个字符，区分大小写"
                               class="form-control"/>
                        <span class="input-group-addon pswState"> &nbsp;&nbsp;&nbsp; </span>
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
                        <input type="password" name="repassword" id="form-repassword" placeholder="请再次填写密码"
                               class="form-control"/>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </c:if>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right"> 性别 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <div class="radio">
                        <label>
                          <input type="radio" name="sex" class="ace" value="0" <c:if test="${user.sex eq 0}"> checked</c:if>>
                          <span class="lbl">保密</span>
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
                    <input type="text" name="birthday" value="<fmt:formatDate value='${user.birthday}'/>" id="form-birthday" placeholder="出生日期"
                           class="form-control date-picker"/>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-mobile"> 手机 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-addon"> <i class="ace-icon fa fa-phone"></i>
                        </span>
                      <input type="text" name="mobile" value="${user.mobile}" id="form-mobile" placeholder="手机"
                             class="form-control input-mask-phone"/>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-email"> 邮箱 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-addon"> <i class="ace-icon fa fa-envelope"></i>
                        </span>
                      <input type="text" name="email" value="${user.email}" id="form-email" placeholder="邮箱"
                             class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-group"> 归属部门 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="hidden" name="groups" id="groups"/>
                      <input type="text" name="groupNames" value="${user.groupNames}" id="form-group" readonly
                             data-placeholder="归属部门" class="form-control" style="background: #fff!important;"
                             onclick="showMenu();"/>
                      <div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index: 1;">
                        <ul id="tree" class="ztree"></ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-role"> 角色 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="roles" id="form-role" class="form-control chosen-select tag-input-style"
                              data-placeholder="角色" multiple="">
                        <c:forEach items="${roles}" var="role">
                          <c:set var="selectedRole" value="false"/>
                          <c:forEach items="${user.roleList}" var="ownRole">
                            <c:if test="${ownRole.code eq role.code}">
                              <c:set var="selectedRole" value="true"/>
                            </c:if>
                          </c:forEach>
                          <option value="${role.id}" <c:if test="${selectedRole}">selected</c:if>>${role.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-status">
                    状态<span class="input-required">*</span>
                  </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="status" id="form-status" class="form-control" data-placeholder="状态">
                        <option value="I" <c:if test="${user.status eq 'I'}">selected</c:if>>未激活</option>
                        <option value="A" <c:if test="${user.status eq 'A'}">selected</c:if>>正常</option>
                        <option value="E" <c:if test="${user.status eq 'E'}">selected</c:if>>过期</option>
                        <option value="L" <c:if test="${user.status eq 'L'}">selected</c:if>>锁定</option>
                        <option value="T" <c:if test="${user.status eq 'T'}">selected</c:if>>停用</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="clearfix form-actions">
              <div class="col-sm-offset-4 col-sm-8 col-xs-offset-3 col-xs-9">
                <button class="btn btn-info btn-sm" type="submit">
                  <i class="ace-icon fa fa-check bigger-110"></i> 保存
                </button>
                &nbsp; &nbsp; &nbsp;
                <button class="btn btn-sm" type="reset" onclick="history.back()">
                  <i class="ace-icon fa fa-undo bigger-110"></i> 返回
                </button>
              </div>
            </div>
          </form>
          <!-- PAGE CONTENT ENDS -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </div>
    <!-- /.page-content -->
  </div>
</div>
<!-- /.main-content -->
</body>
<pluginJs>
  <!-- page specific plugin scripts -->
  <script src="${ctx}/static/js/jquery.validate.min.js"></script>
  <script src="${ctx}/static/js/additional-methods.min.js"></script>
  <script src="${ctx}/static/js/jquery.validate.foomei.js"></script>
  <script src="${ctx}/static/js/messages_zh.min.js"></script>
  <script src="${ctx}/static/js/jquery.form.min.js"></script>
  <script src="${ctx}/static/js/chosen.jquery.min.js"></script>
  <script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
  <script src="${ctx}/static/js/zTree/jquery.ztree.excheck.min.js"></script>
  <script src="${ctx}/static/js/date-time/bootstrap-datepicker.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    $('.date-picker').datepicker({
      format: 'yyyy-mm-dd',
      language: 'cn',
      todayBtn: 'linked',
      autoclose: true
    })

    var setting = {
      check: {
        enable: true,
        chkboxType: {"Y": "", "N": ""}
      },
      data: {
        simpleData: {
          enable: true,
          idKey: "id",
          pIdKey: "parentId",
          rootPId: null
        }
      },
      view: {
        selectedMulti: true,
        autoCancelSelected: true
      }, callback: {
        onCheck: function (event, treeId, treeNode) {
          var zTree = $.fn.zTree.getZTreeObj("tree"),
            nodes = zTree.getCheckedNodes(true),
            names = "", ids = "";
          for (var i = 0, l = nodes.length; i < l; i++) {
            names += nodes[i].name + ", ";
            ids += nodes[i].id + ",";
          }
          if (names.length > 0) names = names.substring(0, names.length - 2);
          if (ids.length > 0) ids = ids.substring(0, ids.length - 1);
          $("#form-group").val(names);
          $("#groups").val(ids);
        }
      }
    };

    function showMenu() {
      $("#menuContent").css({width: $("#form-group").outerWidth()}).slideDown("fast");
      $("body").bind("mousedown", onBodyDown);
    }

    function hideMenu() {
      $("#menuContent").fadeOut("fast");
      $("body").unbind("mousedown", onBodyDown);
    }

    function onBodyDown(event) {
      if (!(event.target.id == "form-group" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
      }
    }

    jQuery(function ($) {
      $.post('${ctx}/api/userGroup/list', function(result) {
        if(result.success) {
          for(var i =0; i < result.data.length; i++) {
            result.data[i].open = true;
          }
          $.fn.zTree.init($('#tree'), setting, result.data);
        }
      });

      $('#form-password').bind('keyup', function (event) {
        var psw = $(this).val();
        var poor = /(^[0-9]+$)|(^[A-Z]+$)|(^[a-z]+$)|(^[`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
        var normal = /(^[0-9A-Z]+$)|(^[0-9a-z]+$)|(^[0-9`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[A-Za-z]+$)|(^[A-Z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[a-z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
        ;
        if (psw == '') {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").removeClass("pswState-strong").html("&nbsp;&nbsp;&nbsp;");
        } else if (poor.test(psw)) {
          $('.pswState').removeClass("pswState-normal").removeClass("pswState-strong").addClass("pswState-poor").html("弱");
        } else if (normal.test(psw)) {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-strong").addClass("pswState-normal").html("中");
        } else {
          $('.pswState').removeClass("pswState-poor").removeClass("pswState-normal").addClass("pswState-strong").html("强");
        }
      });

      if (!ace.vars['touch']) {
        $('.chosen-select').chosen({
          allow_single_deselect: true
        });

        $(window).off('resize.chosen').on('resize.chosen', function () {
          $('.chosen-select').each(function () {
            var $this = $(this);
            $this.next().css({
              'width': $this.parent().width()
            });
          })
        }).trigger('resize.chosen');
        //resize chosen on sidebar collapse/expand
        $(document).on('settings.ace.chosen', function (e, event_name, event_val) {
          if (event_name != 'sidebar_collapsed') return;
          $('.chosen-select').each(function () {
            var $this = $(this);
            $this.next().css({
              'width': $this.parent().width()
            });
          })
        });
      }

      $("#pwdDiv").hide();
      $("#form-password").prop("disabled", true);
      $("#form-repassword").prop("disabled", true);
      $("#form-status").change(function () {
        if ($("#form-status").val() == 'I') {
          $("#pwdDiv").hide();
          $("#form-password").prop("disabled", true);
          $("#form-repassword").prop("disabled", true);
        } else {
          $("#pwdDiv").show();
          $("#form-password").prop("disabled", false);
          $("#form-repassword").prop("disabled", false);
        }
      });

      $('#validation-form').validate({
        errorElement: 'div',
        errorClass: 'help-block',
        focusInvalid: false,
        ignore: "",
        rules: {
          loginName: {
            required: true,
            maxlength: 64,
            remote: {
              type: 'POST',
              url: '${ctx}/api/user/checkLoginName',
              dataType: 'json',
              data: {
                id: function () {
                  return $('#id').val();
                }, loginName: function () {
                  return $('#form-loginName').val();
                }
              }
            }
          },
          name: {
            required: true,
            maxlength: 64
          },
          password: {
            required: true,
            rangelength: [6, 16],
            pass: true
          },
          repassword: {
            equalTo: '#form-password'
          },
          mobile: {
            mobile: true
          },
          email: {
            email: true
          },
          weixin: {
            maxlength: 64
          }
        },
        messages: {
          loginName: {
            remote: '账号已经被使用'
          },
          plainPassword: {
            rangelength: '密码长度应为{0}~{1}个字符'
          },
          repassword: {
            equalTo: '必须与密码保持一致'
          },
          roles: {
            required: '必须选择一个角色'
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
          $('#validation-form').ajaxForm();
          return false;
        },
        invalidHandler: function (form) {
        }
      });
    })
  </script>
</pageJs>
</html>