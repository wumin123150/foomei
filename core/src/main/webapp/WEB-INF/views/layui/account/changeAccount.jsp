<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>个人资料</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/layui/page.css" media="all"/>
  <style type="text/css">
  </style>
</head>
<body class="kit-main">
<div class="layui-tab layui-tab-brief">
  <ul class="layui-tab-title">
    <li class="layui-this">基本信息</li>
    <li>修改头像</li>
    <li>修改密码</li>
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item layui-show">
      <form class="layui-form layui-form-pane" action="${ctx}/api/account/change" method="post" style="width:80%;">
        <input type="hidden" name="id" value="${user.id}"/>
        <div class="layui-form-item">
          <label class="layui-form-label">账号<span class="input-required">*</span></label>
          <div class="layui-input-block">
            <input type="text" name="loginName" value="${user.loginName}" lay-verify="required" placeholder="建议用手机/邮箱注册" autocomplete="off" class="layui-input" disabled>
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
          <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="save">保存</button>
            <button class="layui-btn layui-btn-primary btn-close">关闭</button>
          </div>
        </div>
      </form>
    </div>
    <div class="layui-tab-item">
1
    </div>
    <div class="layui-tab-item">
      <form class="layui-form layui-form-pane" action="${ctx}/api/account/change" method="post" style="width:80%;">
        <input type="hidden" name="id" value="${user.id}"/>
        <div class="layui-form-item">
          <label class="layui-form-label">原密码<span class="input-required">*</span></label>
          <div class="layui-input-block">
            <input type="password" name="password" lay-verify="required" placeholder="原密码" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">新密码<span class="input-required">*</span></label>
          <div class="layui-input-inline">
            <input type="password" name="newPassword" id="password" lay-verify="pass" placeholder="6~16个字符，区分大小写" autocomplete="off" class="layui-input">
          </div>
          <div class="layui-input-inline">
            <input type="password" name="repassword" lay-verify="repass" placeholder="请再次填写密码" autocomplete="off" class="layui-input">
          </div>
          <div class="layui-form-inline">
            <span class="layui-btn layui-btn-primary pswState"> &nbsp;&nbsp;&nbsp; </span>
          </div>
        </div>
        <div class="layui-form-item">
          <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="save">保存</button>
            <button class="layui-btn layui-btn-primary btn-close">关闭</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="${ctx}/static/js/layui/layui.js"></script>
</body>
<script>
  layui.use(['form', 'element', 'laydate', 'upload'], function () {
    var form = layui.form,
      element = layui.element,
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

    //监听提交
    form.on('submit(save)', function (data) {
      var loadIndex = layer.load();
      $.ajax({
        url: data.form.action,
        type: 'POST',
        cache: false,
        data: data.field,
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
</script>
</body>
</html>