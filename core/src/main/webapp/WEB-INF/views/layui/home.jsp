<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>首页</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all" />
  <link rel="stylesheet" href="${ctx}/static/js/layui/main.css" media="all"/>
</head>
<body class="childrenBody">
<div class="panel_box row">
  <div class="panel col">
    <a href="javascript:;" data-url="page/message/message.html">
      <div class="panel_icon">
        <i class="layui-icon" data-icon="&#xe63a;">&#xe63a;</i>
      </div>
      <div class="panel_word newMessage">
        <span></span>
        <cite>新消息</cite>
      </div>
    </a>
  </div>
  <div class="panel col">
    <a href="javascript:;" data-url="page/user/allUsers.html">
      <div class="panel_icon" style="background-color:#009688;">
        <i class="layui-icon" data-icon="&#xe613;">&#xe613;</i>
      </div>
      <div class="panel_word userAll">
        <span></span>
        <cite>用户总数</cite>
      </div>
    </a>
  </div>
</div>
<div class="row">
  <div class="sysNotice col">
    <blockquote class="layui-elem-quote title">系统基本参数</blockquote>
    <table class="layui-table">
      <colgroup>
        <col width="150">
        <col>
      </colgroup>
      <tbody>
      <tr>
        <td>当前版本</td>
        <td class="version"></td>
      </tr>
      <tr>
        <td>开发作者</td>
        <td class="author"></td>
      </tr>
      <tr>
        <td>网站首页</td>
        <td class="homePage"></td>
      </tr>
      <tr>
        <td>服务器环境</td>
        <td class="server"></td>
      </tr>
      <tr>
        <td>数据库版本</td>
        <td class="dataBase"></td>
      </tr>
      <tr>
        <td>最大上传限制</td>
        <td class="maxUpload"></td>
      </tr>
      <tr>
        <td>当前用户权限</td>
        <td class="userRights"></td>
      </tr>
      </tbody>
    </table>
  </div>
</div>

<script src="${ctx}/static/js/layui/layui.js"></script>
</body>
<script>
  layui.use(['form','element','layer','jquery'],function(){
    var form = layui.form,
      layer = parent.layer === undefined ? layui.layer : parent.layer,
      element = layui.element,
      $ = layui.jquery;

    $(".panel a").on("click",function(){
      window.parent.addTab($(this));
    })
  })
</script>
</html>