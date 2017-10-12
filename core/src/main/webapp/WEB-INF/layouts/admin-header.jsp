<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://www.foomei.com/tags/shiro" %>
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
  <script type="text/javascript">
    try {
      ace.settings.check('navbar', 'fixed')
    } catch (e) {
    }
  </script>

  <div class="navbar-container" id="navbar-container">
    <!-- #section:basics/sidebar.mobile.toggle -->
    <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
      <span class="sr-only">Toggle sidebar</span>

      <span class="icon-bar"></span>

      <span class="icon-bar"></span>

      <span class="icon-bar"></span>
    </button>

    <!-- /section:basics/sidebar.mobile.toggle -->
    <div class="navbar-header pull-left">
      <!-- #section:basics/navbar.layout.brand -->
      <a href="javascript:void(0);" class="navbar-brand">
        <small>
          <i class="fa fa-leaf"></i>
          ${iApplication}
        </small>
      </a>

      <!-- /section:basics/navbar.layout.brand -->

      <!-- #section:basics/navbar.toggle -->

      <!-- /section:basics/navbar.toggle -->
    </div>

    <!-- #section:basics/navbar.dropdown -->
    <div class="navbar-buttons navbar-header pull-right" role="navigation">
      <ul class="nav ace-nav">
        <!-- #section:basics/navbar.user_menu -->
        <li class="light-blue">
          <a data-toggle="dropdown" href="javascript:void(0);" class="dropdown-toggle">
            <shiro:user>
              <img class="nav-user-photo" src="${ctx}/avatar/<shiro:principal property="id"/>" onerror="this.src='${ctx}/static/avatars/avatar6.jpg'"/>
              <span class="user-info">
                  <small>欢迎您,</small>
                  <shiro:principal property="name"/>
              </span>
            </shiro:user>

            <i class="ace-icon fa fa-caret-down"></i>
          </a>

          <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">

            <li>
              <a href="${ctx}/admin/changeAccount">
                <i class="ace-icon fa fa-user"></i>
                个人资料
              </a>
            </li>
            <li>
              <a href="${ctx}/admin/readNotice">
                <i class="ace-icon fa fa-cog"></i>
                通知
              </a>
            </li>

            <li class="divider"></li>

            <li>
              <a href="${ctx}/logout">
                <i class="ace-icon fa fa-power-off"></i>
                退出
              </a>
            </li>
          </ul>
        </li>

        <!-- /section:basics/navbar.user_menu -->
      </ul>
    </div>

    <!-- /section:basics/navbar.dropdown -->
  </div><!-- /.navbar-container -->
</div>