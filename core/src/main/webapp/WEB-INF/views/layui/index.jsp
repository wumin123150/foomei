<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
<style>
  .logo {
    position: absolute;
    left: 40px;
    top: 16px;
  }
  .logo img {
    width: 82px;
    height: 31px;
  }
  .header {
    height: 60px;
    border-bottom: none;
  }
  .header .layui-nav {
    position: absolute;
    right: 0;
    top: 0;
    padding: 0;
    background: none;
  }

  .user-photo {
    width: 200px;
    height: 120px;
    padding-top: 15px;
    padding-bottom: 5px;
  }
  .user-photo a.img {
    display: block;
    width: 76px;
    height: 76px;
    margin: 0 auto;
    margin-bottom: 15px;
  }
  .user-photo a.img img {
    display: block;
    border: none;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    -webkit-border-radius: 50%;
    -moz-border-radius: 50%;
    border: 4px solid #44576b;
  }
  .user-photo p {
    text-align: center;
  }

  .layui-body .layui-tab {
    margin: 0px;
    width: 100%;
    height: 100%;
    overflow: hidden;
    border: 0;
  }

  ul.layui-tab-title {
    width: calc(100% - 180px);
    position: absolute;
    z-index: 2;
  }

  .layui-body .layui-tab-content {
    padding: 0;
    position: relative;
    top: 41px;
    overflow-y: auto;
  }

  .layui-tab .kit-tab-tool {
    position: absolute;
    width: 180px;
    height: 40px;
    top: 0;
    right: 0;
    background: #f2f2f2;
    border-bottom: 1px solid #e2e2e2;
  }

  .layui-tab .kit-tab-tool .refresh {
    width: 66px;
    position: absolute;
    top: 0px;
    left: 0px;
    background: #ffffff;
    line-height: 39px;
    border-left: 1px solid #e2e2e2;
    border-right: 1px solid #e2e2e2;
    cursor: pointer;
    text-align: center;
  }

  .layui-tab .kit-tab-tool .often {
    width: 120px;
    position: absolute;
    top: 0px;
    left: 68px;
    background: #ffffff;
    cursor: pointer;
    text-align: center;
  }

  .layui-tab .kit-tab-tool .often ul.layui-nav {
    width: 120px;
    padding: 0px;
    background: none;
  }

  .layui-tab .kit-tab-tool .often ul.layui-nav li.layui-nav-item a.top {
    padding: 0px;
    line-height: 40px;
    text-align: center;
    color: #000;
  }

  .often ul.layui-nav .layui-nav-bar {
    background: none;
  }

  .often ul.layui-nav li.layui-nav-item dl.layui-nav-child {
    width: 145px;
    text-align: center;
    position: absolute;
    top: 42px;
    left: -68px;
    background: #ffffff;
  }

  .layui-tab-content .layui-tab-item iframe {
    width: 100%;
    border: 0;
    height: 100%;
  }
</style>
<body class="kit-theme">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header header">
    <div class="layui-main">
      <div class="logo"><img src="//res.layui.com/images/layui/logo.png" alt="layui"></div>
      <ul class="layui-nav">
        <li class="layui-nav-item"><a href="javascript:;">控制台<span class="layui-badge-dot"></span></a></li>
        <li class="layui-nav-item"><a href="javascript:;">个人中心</a></li>
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
        <li class="layui-nav-item"><a href="login.html"><i class="fa fa-sign-out" aria-hidden="true"></i> 退出</a></li>
      </ul>
    </div>
  </div>

  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <div class="user-photo">
        <a class="img" title="我的头像" ><img src="http://m.zhengjinfan.cn/images/0.jpg"></a>
        <p>你好！<span class="userName" id="userNameSpan" title="${LOGIN_NAME.userName}">${LOGIN_NAME.userName}</span></p>
      </div>
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
    <div class="layui-tab layui-tab-card" id="larry-tab" lay-filter="bodyTab">
      <! -- 选项卡-->
      <ul class="layui-tab-title top_tab" id="top_tabs">
        <li class="layui-this" lay-id=""><i class="layui-icon"></i> 后台首页</li>
        <li lay-id="2"><i class="layui-icon"></i> 后台1</li>
      </ul>
      <div class="kit-tab-tool">

        <div class="refresh key-press" id="refresh_iframe"><i class="layui-icon">&#x1002;</i>刷新</div>

        <div class="often key-press">
          <ul class="layui-nav">
            <li class="layui-nav-item">
              <a class="top">常用操作</a>
              <dl class="layui-nav-child">
                <dd>
                  <a href="javascript:;" class="closeCurrent"><i class="larry-icon larry-guanbidangqianye"></i>关闭当前选项卡</a>
                </dd>
                <dd>
                  <a href="javascript:;" class="closeOther"><i class="larry-icon larry-guanbiqita"></i>关闭其他选项卡</a>
                </dd>
                <dd>
                  <a href="javascript:;" class="closeAll"><i class="larry-icon larry-guanbiquanbufenzu"></i>关闭全部选项卡</a>
                </dd>
              </dl>
            </li>
          </ul>
        </div>

      </div>
      <div class="layui-tab-content" style="height:793px;">
        <div class="layui-tab-item layui-show layui-anim layui-anim-upbit">
          <iframe src="${ctx}/index" data-id="0" name="ifr_0" id="ifr_0"></iframe>
        </div>
      </div>
    </div>
  </div>

  <div class="layui-footer footer">
    <div class="layui-main">
    2017 &copy; <a href="http://www.foomei.com/">www.foomei.com</a> MIT license
    </div>
  </div>
</div>
<script src="${ctx}/static/js/layui/layui.js"></script>
<script>
  layui.use(['form','element','layer','jquery'], function() {
    var $ = layui.jquery,
      layer = layui.layer,
      form = layui.form,
      element = layui.element;


  });
</script>
</body>

</html>