<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>日志管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/daterangepicker.min.css"/>
</pluginCss>
<pageCss>
  <!-- inline styles related to this page -->
  <style>
    .ui-state-disabled {
      color: #BBB !important;
    }

    .dataTables_filter .input-group-addon {
      line-height: 1.2;
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

    .break-content {
      display: block;
      word-break: break-all;
      word-wrap: break-word;
      max-height: 200px;
      overflow-y: auto;
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
        <li class="active">日志管理</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          日志管理
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <!-- PAGE CONTENT BEGINS -->
          <div class="row">
            <div class="col-xs-12">
              <form class="form-search" action="">
                <div class="dataTables_filter row">
                  <div class="col-xs-8">
                    <div class="input-daterange input-group">
                      <label>操作时间范围:</label>
                      <input type="text" name="timeRange" id="timeRange" style="width:245px" class="input-sm"
                             placeholder="按时间范围查询" aria-controls="datatables">
                      <input type="hidden" name="startTime" id="startTime"/>
                      <input type="hidden" name="endTime" id="endTime"/>
                    </div>
                  </div>
                  <div class="col-xs-4">
                    <div class="input-group">
                      <input type="text" id="searchKey" style="width:200px" name="searchKey" value="${searchKey}"
                             class="input-sm" placeholder="操作用户、操作描述、URL、IP" aria-controls="datatables">
                      <input style="display:none"/>
                      <span class="input-group-btn">
                        <button id="btn-search" class="btn btn-xs btn-purple" type="button"><i class="fa fa-search"></i>查询</button>
                      </span>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>

          <table id="grid-table"></table>

          <div id="dialog-container" class="hide">
            <div id="dialog-form">
              <div class="profile-user-info profile-user-info-striped">
                <div class="profile-info-row">
                  <div class="profile-info-name">操作描述</div><div class="profile-info-value" id="form-description"></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">操作用户</div><div class="profile-info-value" id="form-username"></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">操作时间</div><div class="profile-info-value" id="form-logTime"></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">访问路径</div><div class="profile-info-value" id="form-url"></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">访问方式</div><div class="profile-info-value"><div class="break-content" id="form-userAgent"></div></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">请求参数</div><div class="profile-info-value"><div class="break-content" id="form-parameter"></div></div>
                </div>
                <div class="profile-info-row">
                  <div class="profile-info-name">响应结果</div><div class="profile-info-value"><div class="break-content" id="form-result"></div></div>
                </div>
              </div>
            </div>
          </div>
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
  <script src="${ctx}/static/js/date-time/moment.min.js"></script>
  <script src="${ctx}/static/js/date-time/daterangepicker.min.js"></script>
</pluginJs>
<pageJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    $("#timeRange").daterangepicker({
      locale: {
        format: "YYYY-MM-DD HH:mm",
        applyLabel: '确认',
        cancelLabel: '取消',
        fromLabel: '起始时间',
        toLabel: '结束时间',
        customRangeLabel: '自定义',
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        firstDay: 1
      },
      timePicker: true,
      timePickerIncrement: 10,
      timePicker24Hour: true,
      ranges: {
        '最近1小时': [moment().subtract(1, 'hours'), moment()],
        '今日': [moment().startOf('day'), moment()],
        '昨日': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
        '最近7日': [moment().subtract(6, 'days'), moment()],
        '最近30日': [moment().subtract(29, 'days'), moment()],
        '本月': [moment().startOf("month"), moment().endOf("month")],
        '上个月': [moment().subtract(1, "month").startOf("month"), moment().subtract(1, "month").endOf("month")]
      }
    });

    $('#timeRange').on('apply.daterangepicker', function (ev, picker) {
      $('#startTime').val(picker.startDate.format('YYYY-MM-DD HH:mm'));
      $('#endTime').val(picker.endDate.format('YYYY-MM-DD HH:mm'));
    });

    $('#timeRange').on('cancel.daterangepicker', function (ev, picker) {
      $('#timeRange').val('');
      $("#startTime").val('');
      $("#endTime").val('');
    });

    var grid_selector = "#grid-table";
    var grid_page_url = "${ctx}/api/log/page";
    var grid_del_url = "${ctx}/api/log/delete/";
    var grid_batch_del_url = "${ctx}/api/log/batch/delete";

    jQuery(function ($) {
      $(grid_selector).foomei_JqGrid({
        url: grid_page_url,
        colNames: ['操作', '操作用户', '操作描述', 'URL', 'IP', '操作日期', '消耗时间'],
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
                + '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red btn-del" href="javascript:void(0);" data-id="' + rowObject.id + '"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
                + '<div title="详情" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="green btn-view" href="javascript:void(0);" data-id="' + rowObject.id + '"><i class="ace-icon fa fa-eye bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'username', index: 'username', width: 50, sortable: false},
          {name: 'description', index: 'description', width: 100},
          {name: 'url', index: 'url', width: 200},
          {name: 'ip', index: 'ip', width: 100},
          {
            name: 'logTime',
            index: 'logTime',
            formatter: 'date',
            formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'},
            width: 100
          },
          {name: 'spendTime', index: 'spendTime', width: 100}
        ],
        sortname: 'logTime',
        sortorder: 'desc',
        navAdd: [{
          caption: '',
          title: '批量删除',
          buttonicon: 'ace-icon fa fa-trash-o red',
          onClickButton: function () {
            var ids = $(grid_selector).foomei_JqGrid("getIds");
            if(ids.length > 0) {
              BootstrapDialog.confirm('你确定要删除吗？', function (result) {
                if (result) {
                  $.ajax({
                    url: grid_batch_del_url,
                    data: {ids: ids.join(',')},
                    type: 'POST',
                    cache: false,
                    dataType: 'json',
                    success: function (result) {
                      if (result.success) {
                        toastr.success('批量删除成功');
                        $(grid_selector).foomei_JqGrid('reload');
                      } else {
                        toastr.error(result.message);
                      }
                    },
                    error: function () {
                      toastr.error('未知错误，请联系管理员');
                    }
                  });
                }
              });
            }
          },
          position: 'first'
        }]
      });

      $(grid_selector).foomei_JqGrid('resize');

      $("#searchKey").keydown(function (e) {
        if (e.keyCode == 13) {
          $(grid_selector).foomei_JqGrid('search', {
            "searchKey": $('#searchKey').val(),
            "startTime": $('#startTime').val(),
            "endTime": $('#endTime').val()
          });
        }
      });

      $("#btn-search").click(function () {
        $(grid_selector).foomei_JqGrid('search', {
          "searchKey": $('#searchKey').val(),
          "startTime": $('#startTime').val(),
          "endTime": $('#endTime').val()
        });
      });

      $(document).on('click', ".btn-del", function () {
        var id = $(this).attr("data-id");
        BootstrapDialog.confirm('你确定要删除吗？', function (result) {
          if (result) {
            $.ajax({
              url: grid_del_url + id,
              type: 'GET',
              cache: false,
              dataType: 'json',
              success: function (result) {
                if (result.success) {
                  toastr.success('删除成功');
                  $(grid_selector).foomei_JqGrid('reload');
                } else {
                  toastr.error(result.message);
                }
              },
              error: function () {
                toastr.error('未知错误，请联系管理员');
              }
            });
          }
        });
      });

      $(document).on('click', ".btn-view", function () {
        var id = $(this).attr("data-id");
        $.ajax({
          url: "${ctx}/api/log/get/" + id,
          type: 'GET',
          cache: false,
          dataType: 'json',
          success: function (result) {
            $('#form-description').text(result.data.description);
            $('#form-username').text(result.data.username + '(' + result.data.ip + ')');
            $('#form-logTime').text(result.data.logTime + '  耗时' + result.data.spendTime + '秒');
            $('#form-url').text('[' + result.data.method + ']' + result.data.url);
            $('#form-userAgent').text(result.data.userAgent);
            $('#form-parameter').text(result.data.parameter);
            $('#form-result').text(result.data.result);

            BootstrapDialog.show({
              title: '<i class="ace-icon fa fa-eye bigger-110"></i>&nbsp;查看日志',
              message: $('#dialog-form'),
              autodestroy: false,
              buttons: [{
                label: '<i class="ace-icon fa fa-times bigger-110"></i>取消',
                action: function (dialogRef) {
                  dialogRef.close();
                }
              }]
            });
          },
          error: function () {
            toastr.error('未知错误，请联系管理员');
          }
        });
      });
    })
  </script>
</pageJs>
</html>