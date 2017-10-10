<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>通知管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css"/>
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .ui-state-disabled {
      color: #BBB !important;
    }

    #btn-search {
      height: 28px;
      margin-bottom: 4px;
    }

    .no-data {
      margin-top: 10px;
    }

    .ui-jqdialog-content .searchFilter select {
      padding: 5px 1px;
      height: 30px;
      line-height: 30px;
    }

    .ui-jqdialog-content .searchFilter .input-elm {
      padding-bottom: 5px;
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
        <li class="active">通知管理</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          通知管理
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <c:if test="${not empty message}">
            <div class="alert alert-block alert-success">
              <button type="button" class="close" data-dismiss="alert">
                <i class="ace-icon fa fa-times"></i>
              </button>

              <i class="ace-icon fa fa-check green"></i>
                ${message}
            </div>
          </c:if>
          <div class="row">
            <div class="col-xs-12">
              <form class="form-search" action="">
                <div class="dataTables_filter input-group">
                  <input type="text" id="searchKey" name="searchKey" value="${searchKey}" class="input-sm"
                         placeholder="标题" aria-controls="datatables">
                  <input style="display:none"/>
                  <span class="input-group-btn">
											<button id="btn-search" class="btn btn-xs btn-purple" type="button"><i class="fa fa-search"></i>查询</button>
										</span>
                </div>
              </form>
            </div>
          </div>

          <table id="grid-table"></table>
          <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.page-content -->
  </div>
</div><!-- /.main-content -->
</body>
<pluginJs>
  <!-- page specific plugin scripts -->
  <script src="${ctx}/static/js/jqGrid/jquery.jqGrid.min.js"></script>
  <script src="${ctx}/static/js/jqGrid/i18n/grid.locale-cn.js"></script>
  <script src="${ctx}/static/js/jquery.jqGrid.foomei.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    var grid_selector = "#grid-table";
    var grid_page_url = "${ctx}/api/notice/page";
    var grid_add_url = "${ctx}/admin/notice/create";
    var grid_edit_url = "${ctx}/admin/notice/update/";
    var grid_del_url = "${ctx}/admin/notice/delete/";

    jQuery(function ($) {
      $(grid_selector).foomei_JqGrid({
        url: grid_page_url,
        colNames: ['操作', '标题', '内容', '状态', '更新时间'],
        colModel: [
          //{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
          //formatter:'actions',
          //formatoptions:{
          //keys:true,
          //delbutton:true, delOptions:{recreateForm:true, beforeShowForm:beforeDeleteCallback},
          //editformbutton:true, editOptions:{recreateForm:true, beforeShowForm:beforeEditCallback}
          //}
          //},
          {
            name: 'myop', index: '', width: 80, fixed: true, sortable: false, resize: false, search: false,
            formatter: function (cellvalue, options, rowObject) {
              return '<div class="action-buttons">'
                + '<div title="编辑" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="blue" href="' + grid_edit_url + rowObject.id + '"><i class="ace-icon fa fa-pencil bigger-140"></i></a></div>'
                + '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red btn-del" href="javascript:void(0);" data-id="' + rowObject.id + '"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'title', index: 'title', width: 100},
          {name: 'content', index: 'content'},
          {name: 'status', index: 'status', width: 40, formatter: function (cellvalue, options, rowObject) {
            if (cellvalue == 1) {
              return '<span class="label label-sm label-success">发布</span>';
            } else {
              return '<span class="label label-sm">草稿</span>';
            }
          }},
          {
            name: 'updateTime',
            index: 'updateTime',
            formatter: 'date',
            formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'},
            width: 80
          }
        ],
        navAdd: [
          {
            caption: '',
            title: '添加',
            buttonicon: 'ace-icon fa fa-plus-circle purple',
            onClickButton: function () {
              window.location.href = grid_add_url;
            },
            position: 'first'
          }
        ]
      });

      $(grid_selector).foomei_JqGrid('resize');

      $("#searchKey").keydown(function (e) {
        if (e.keyCode == 13) {
          $(grid_selector).foomei_JqGrid('search', {
            "searchKey": $('#searchKey').val()
          });
        }
      });

      $("#btn-search").click(function () {
        $(grid_selector).foomei_JqGrid('search', {
          "searchKey": $('#searchKey').val()
        });
      });

      $(document).on('click', ".btn-del", function () {
        var id = $(this).attr("data-id");
        BootstrapDialog.confirm('你确定要删除吗？', function (result) {
          if (result) {
            window.location.href = grid_del_url + id;
          }
        });
      });
    })
  </script>
</pageJs>
</html>