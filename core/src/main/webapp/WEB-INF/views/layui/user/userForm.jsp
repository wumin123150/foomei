<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>用户管理</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
  <link rel="stylesheet" href="${ctx}/static/js/layui/page.css" media="all"/>
  <style type="text/css">
    .center {
      text-align: center;
    }
    .layui-upload {
      margin-bottom: 10px;
    }
    .layui-upload-img {
      width: 152px;
      height: 152px;
      margin: 0 10px 10px 0;
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
</head>
<body class="kit-main">
<form id="form" class="layui-form layui-form-pane" action="${ctx}/api/user/${action}" method="post" style="width:80%;">
  <input type="hidden" name="id" id="id" value="${user.id}"/>
  <div class="layui-row">
  <div class="layui-col-xs12 layui-col-sm9">
  <div class="layui-form-item">
    <label class="layui-form-label">账号<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <input type="text" name="loginName" value="${user.loginName}" lay-verify="required" placeholder="建议用手机/邮箱注册" autocomplete="off" class="layui-input" <c:if test='${action == "update"}'>disabled</c:if>>
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">姓名<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <input type="text" name="name" value="${user.name}" lay-verify="required" placeholder="姓名" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item" pane="">
    <label class="layui-form-label">性别</label>
    <div class="layui-input-block">
      <input type="radio" name="sex" value="0" title="保密" <c:if test="${user.sex eq 0}"> checked</c:if>>
      <input type="radio" name="sex" value="1" title="男" <c:if test="${user.sex eq 1}"> checked</c:if>>
      <input type="radio" name="sex" value="2" title="女" <c:if test="${user.sex eq 2}"> checked</c:if>>
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">出生日期</label>
    <div class="layui-input-block">
      <input type="text" name="birthday" id="birthday" value="${user.birthday}" placeholder="出生日期" autocomplete="off" class="layui-input">
    </div>
  </div>
  </div>
    <div class="layui-col-xs12 layui-col-sm3 center">
      <div class="layui-upload">
        <c:if test="${not empty user.avatar}">
          <img id="avatar" class="layui-upload-img" src="${ctx}/avatar/${user.id}" onerror="this.src='${ctx}/static/avatars/avatar6.jpg'"/>
        </c:if>
        <c:if test="${empty user.avatar}">
          <img id="avatar" class="layui-upload-img" src="${ctx}/static/avatars/avatar6.jpg"/>
        </c:if>
        <p id="demoText"></p>
        <button type="button" class="layui-btn" id="btn-avatar">上传图片</button>
        <input type="hidden" name="avatarId" id="avatarId"/>
      </div>
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">状态<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <select name="status" lay-filter="status" data-placeholder="状态">
        <option value="I" <c:if test="${user.status eq 'I'}">selected</c:if>>未激活</option>
        <option value="A" <c:if test="${user.status eq 'A'}">selected</c:if>>正常</option>
        <option value="E" <c:if test="${user.status eq 'E'}">selected</c:if>>过期</option>
        <option value="L" <c:if test="${user.status eq 'L'}">selected</c:if>>锁定</option>
        <option value="T" <c:if test="${user.status eq 'T'}">selected</c:if>>停用</option>
      </select>
    </div>
  </div>
  <c:if test='${action == "create"}'>
    <div class="layui-form-item" id="pwdDiv">
      <label class="layui-form-label">密码<span class="input-required">*</span></label>
      <div class="layui-input-inline">
        <input type="password" name="password" id="password" lay-verify="pass" placeholder="6~16个字符，区分大小写" autocomplete="off" class="layui-input">
      </div>
      <div class="layui-input-inline">
        <input type="password" name="repassword" lay-verify="repass" placeholder="请再次填写密码" autocomplete="off" class="layui-input">
      </div>
      <div class="layui-form-inline">
        <span class="layui-btn layui-btn-primary pswState"> &nbsp;&nbsp;&nbsp; </span>
      </div>
    </div>
  </c:if>
  <div class="layui-form-item">
    <label class="layui-form-label">手机</label>
    <div class="layui-input-block">
      <input type="tel" name="mobile" value="${user.mobile}" placeholder="手机" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">邮箱</label>
    <div class="layui-input-block">
      <input type="text" name="email" value="${user.email}" placeholder="邮箱" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">归属部门</label>
    <div class="layui-input-block">
      <input type="text" name="groups" id="groups" class="hide"/>
      <input type="text" name="groupNames" id="groupNames" value="${user.groupNames}" placeholder="归属部门" class="layui-input" onclick="showMenu();" readonly/>
      <div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index: 1;">
        <ul id="tree" class="ztree"></ul>
      </div>
    </div>
  </div>
  <div class="layui-form-item" pane="">
    <label class="layui-form-label">角色</label>
    <div class="layui-input-block">
      <c:forEach items="${roles}" var="role">
        <c:set value="false" var="selectedRole"/>
        <c:forEach items="${user.roleList}" var="ownRole">
          <c:if test="${ownRole.code eq role.code}">
            <c:set var="selectedRole" value="true"/>
          </c:if>
        </c:forEach>
        <input type="checkbox" name="roles" value="${role.id}" lay-skin="primary" title="${role.name}" <c:if test="${selectedRole}"> checked</c:if>>
      </c:forEach>
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block kit-btns">
      <button class="layui-btn" lay-submit lay-filter="save">保存</button>
      <button class="layui-btn layui-btn-primary btn-close">关闭</button>
    </div>
  </div>
</form>
<script src="${ctx}/static/js/layui/layui.js"></script>
<script src="${ctx}/webjars/jquery/jquery.min.js"></script>
<script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
<script src="${ctx}/static/js/zTree/jquery.ztree.excheck.min.js"></script>
</body>
<script>
  layui.use(['form', 'laydate', 'upload'], function () {
    var form = layui.form,
      layer = layui.layer,
      $ = layui.jquery,
      laydate = layui.laydate,
      upload = layui.upload;

    laydate.render({
      elem: '#birthday'
    });

    $('#password').bind('keyup', function (event) {
      var psw = $(this).val();
      var poor = /(^[0-9]+$)|(^[A-Z]+$)|(^[a-z]+$)|(^[`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
      var normal = /(^[0-9A-Z]+$)|(^[0-9a-z]+$)|(^[0-9`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[A-Za-z]+$)|(^[A-Z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)|(^[a-z`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)/;
      ;
      if (psw == '') {
        $('.pswState').removeClass("layui-btn-danger").removeClass("layui-btn-warm").addClass("layui-btn-primary").html("&nbsp;&nbsp;&nbsp;");
      } else if (poor.test(psw)) {
        $('.pswState').removeClass("layui-btn-warm").removeClass("layui-btn-primary").addClass("layui-btn-danger").html("弱");
      } else if (normal.test(psw)) {
        $('.pswState').removeClass("layui-btn-danger").removeClass("layui-btn-primary").addClass("layui-btn-warm").html("中");
      } else {
        $('.pswState').removeClass("layui-btn-danger").removeClass("layui-btn-warm").html("强");
      }
    });

    var uploadInst = upload.render({
      elem: '#btn-avatar'
      ,url: '${ctx}/api/annex/save'
      ,done: function(response){
        if(response.success){
          var path = "${ctx}" + response.data.requestURI;
          $('#avatar').attr('src', path);
          $('#avatarId').val(response.data.id);
        } else {
          layer.msg(message, {icon: 2});
        }
      }
    });

    form.verify({
      pass: [/(.+){6,16}$/, '密码长度必须6到16位'],
      repass: function(value){
        if(value != $('#password').val()){
          return '必须与密码保持一致';
        }
      }
    });

    //监听提交
    form.on('submit(save)', function (data) {
      var loadIndex = layer.load();
      $.ajax({
        url: data.form.action,
        type: 'POST',
        cache: false,
        data: $('#form').serialize(),//data.field 不支持checkbox
        dataType: 'json',
        success: function (result) {
          if (result.success) {
            loadIndex && layer.close(loadIndex);
            layer.msg('保存成功', {icon: 1});
            parent.layer.closeAll("iframe");
            parent.location.reload();
          } else {
            loadIndex && layer.close(loadIndex);
            if(result.data) {
              var message = '';
              for(var i=0;i<result.data.length;i++) {
                message += result.data[i].errorMsg + '<br>';
              }
              layer.msg(message, {icon: 2});
            } else
              layer.msg(result.message, {icon: 2});
          }
        },
        error: function () {
          loadIndex && layer.close(loadIndex);
          layer.msg('未知错误，请联系管理员', {icon: 5});
        }
      });
      return false;
    });

    $('.btn-close').on('click', function(){
      parent.layer.closeAll("iframe");
      return false;
    });
  });

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

        $("#groups").val();
        for (var i = 0, l = nodes.length; i < l; i++) {
          names += nodes[i].name + ", ";
          ids += nodes[i].id + ",";
        }
        if (names.length > 0) names = names.substring(0, names.length - 2);
        if (ids.length > 0) ids = ids.substring(0, ids.length - 1);
        $("#groupNames").val(names);
        $("#groups").val(ids);
      }
    }
  };

  function showMenu() {
    $("#menuContent").css({width: $("#groupNames").outerWidth() - 12}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
  }

  function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
  }

  function onBodyDown(event) {
    if (!(event.target.id == "groupNames" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
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
  })
</script>
</body>
</html>