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
  <link rel="stylesheet" href="${ctx}/static/js/layui/page.css" media="all"/>
  <style type="text/css">
    .input-required {
      margin-left: 2px;
      color: #c00;
    }
  </style>
</head>
<body class="kit-main">
<form class="layui-form layui-form-pane" action="${ctx}/api/user/save" method="post" style="width:80%;">
  <input type="hidden" name="id" id="id" value="${user.id}"/>
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
  <c:if test='${action == "create"}'>
  <div class="layui-form-item" id="pwdDiv">
    <label class="layui-form-label">密码<span class="input-required">*</span></label>
    <div class="layui-input-inline">
      <input type="password" name="password" lay-verify="required" placeholder="6~16个字符，区分大小写" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-input-inline">
      <input type="password" name="repassword" placeholder="请再次填写密码" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-inline">
      <span class="layui-btn layui-bg-gray pswState"> &nbsp;&nbsp;&nbsp; </span>
    </div>
  </div>
  </c:if>
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
    <label class="layui-form-label">状态<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <select name="status" id="status" data-placeholder="状态">
        <option value="I" <c:if test="${user.status eq 'I'}">selected</c:if>>未激活</option>
        <option value="A" <c:if test="${user.status eq 'A'}">selected</c:if>>正常</option>
        <option value="E" <c:if test="${user.status eq 'E'}">selected</c:if>>过期</option>
        <option value="L" <c:if test="${user.status eq 'L'}">selected</c:if>>锁定</option>
        <option value="T" <c:if test="${user.status eq 'T'}">selected</c:if>>停用</option>
      </select>
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="save">保存</button>
      <button class="layui-btn layui-btn-primary btn-close">关闭</button>
    </div>
  </div>
</form>
<script src="${ctx}/static/js/layui/layui.js"></script>
</body>
<script>
  layui.use(['form', 'laydate'], function () {
    var form = layui.form,
      layer = layui.layer,
      $ = layui.jquery,
      laydate = layui.laydate;

    laydate.render({
      elem: '#birthday'
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