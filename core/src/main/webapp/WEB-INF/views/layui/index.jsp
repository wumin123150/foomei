<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>

<head>
  <title>${iApplication}</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" />
</head>

<body class="kit-theme">
<div class="layui-layout layui-layout-admin kit-layout-admin">
  <div class="layui-header">
    <div class="layui-main">
      <div class="layui-logo">${iApplication}</div>
      <ul class="layui-nav kit-nav">
        <li class="layui-nav-item"><a href="javascript:;">控制台<span class="layui-badge-dot"></span></a></li>
        <li class="layui-nav-item">
          <a href="javascript:;">个人中心<span class="layui-badge-dot"></span></a>
        </li>
        <li class="layui-nav-item">
          <a href="javascript:;">其它系统</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;">邮件管理</a></dd>
            <dd><a href="javascript:;">消息管理</a></dd>
            <dd><a href="javascript:;">授权管理</a></dd>
          </dl>
        </li>
        <li class="layui-nav-item">
          <a href="javascript:;"><img src="http://t.cn/RCzsdCq" class="layui-nav-img">我</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" kit-target data-options="{url:'basic.html',icon:'&#xe658;',title:'基本资料',id:'966'}"><span>基本资料</span></a></dd>
            <dd><a href="javascript:;">安全设置</a></dd>
          </dl>
        </li>
        <li class="layui-nav-item"><a href="login.html"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a></li>
      </ul>
    </div>
  </div>

  <div class="layui-side layui-bg-black kit-side">
    <div class="layui-side-scroll">
      <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar>
        <li class="layui-nav-item layui-nav-itemed">
          <a class="" href="javascript:;"><i class="fa fa-plug" aria-hidden="true"></i><span> 基本元素</span></a>
          <dl class="layui-nav-child">
            <dd>
              <a href="javascript:;" kit-target data-options="{url:'test.html',icon:'&#xe6c6;',title:'表格',id:'1'}">
                <i class="layui-icon">&#xe6c6;</i><span> 表格</span></a>
            </dd>
            <dd>
              <a href="javascript:;" data-url="form.html" data-icon="fa-user" data-title="表单" kit-target data-id='2'><i class="fa fa-user" aria-hidden="true"></i><span> 表单</span></a>
            </dd>
            <dd>
              <a href="javascript:;" data-url="nav.html" data-icon="&#xe628;" data-title="导航栏" kit-target data-id='3'><i class="layui-icon">&#xe628;</i><span> 导航栏</span></a>
            </dd>
            <dd>
              <a href="javascript:;" data-url="list4.html" data-icon="&#xe614;" data-title="列表四" kit-target data-id='4'><i class="layui-icon">&#xe614;</i><span> 列表四</span></a>
            </dd>
            <dd>
              <a href="javascript:;" kit-target data-options="{url:'https://www.baidu.com',icon:'&#xe658;',title:'百度一下',id:'5'}"><i class="layui-icon">&#xe658;</i><span> 百度一下</span></a>
            </dd>
          </dl>
        </li>
        <li class="layui-nav-item layui-nav-itemed">
          <a href="javascript:;"><i class="fa fa-plug" aria-hidden="true"></i><span> 组件</span></a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" kit-target data-options="{url:'navbar.html',icon:'&#xe658;',title:'Navbar',id:'6'}"><i class="layui-icon">&#xe658;</i><span> Navbar</span></a></dd>
            <dd><a href="javascript:;" kit-target data-options="{url:'tab.html',icon:'&#xe658;',title:'TAB',id:'7'}"><i class="layui-icon">&#xe658;</i><span> Tab</span></a></dd>
            <dd><a href="javascript:;" kit-target data-options="{url:'onelevel.html',icon:'&#xe658;',title:'OneLevel',id:'50'}"><i class="layui-icon">&#xe658;</i><span> OneLevel</span></a></dd>
            <dd><a href="javascript:;" kit-target data-options="{url:'app.html',icon:'&#xe658;',title:'App',id:'8'}"><i class="layui-icon">&#xe658;</i><span> app.js主入口</span></a></dd>
          </dl>
        </li>
      </ul>
    </div>
  </div>
  <div class="layui-body" id="container">
    <!-- 内容主体区域 -->
    <div style="padding: 15px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63e;</i> 请稍等...</div>
  </div>

  <div class="layui-footer">
    <!-- 底部固定区域 -->
    2017 &copy;
    <a href="http://www.foomei.com/">www.foomei.com</a> MIT license
  </div>
</div>
<script src="${ctx}/static/js/layui/layui.js"></script>
<script>
  layui.use(['form','element','layer','jquery'], function() {
    var $ = layui.jquery,
      layer = layui.layer,
      form = layui.form(),
      element = layui.element();


  });
</script>
</body>

</html>