<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>角色管理</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all"/>
</head>
<style type="text/css">
  .layui-form-item .layui-inline{ width:33.333%; float:left; margin-right:0; }
  @media(max-width:1240px){
    .layui-form-item .layui-inline{ width:100%; float:none; }
  }
</style>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
  <div class="layui-form-item">
    <label class="layui-form-label">代码</label>
    <div class="layui-input-block">
      <input type="text" name="code" lay-verify="code" placeholder="代码" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">名称</label>
    <div class="layui-input-block">
      <input type="text" name="name" lay-verify="required" placeholder="名称" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="save">保存</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>
<script src="${ctx}/static/js/layui/layui.js"></script>
</body>
<script>

  layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form,
      layer = layui.layer,
      layedit = layui.layedit,
      laydate = layui.laydate;

    //自定义验证规则
    form.verify({
      title: function (value) {
        if (value.length < 5) {
          return '标题至少得5个字符啊';
        }
      },
      pass: [/(.+){6,12}$/, '密码必须6到12位']
    });

    //监听提交
    form.on('submit(save)', function (data) {
      $.ajax({
        url: save_url,
        type: 'POST',
        cache: false,
        dataType: 'json',
        success: function (result) {
          if (result.success) {
            layer.msg('保存成功', {icon: 1});
            parent.layer.closeAll("iframe");
            parent.location.reload();
          } else {
            layer.msg(result.message, {icon: 5});
          }
        },
        error: function () {
          layer.msg('未知错误，请联系管理员', {icon: 5});
        }
      });
      return false;
    });
  });
</script>
</body>
</html>