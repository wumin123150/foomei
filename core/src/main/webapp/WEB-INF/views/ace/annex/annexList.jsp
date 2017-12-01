<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>附件管理</title>
</head>
<pluginCss>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/daterangepicker.min.css"/>
  <link rel="stylesheet" href="${ctx}/static/css/colorbox.min.css" />
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
        <li class="active">附件管理</li>
      </ul><!-- /.breadcrumb -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          附件管理
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
                      <input type="text" name="plantRange" id="timeRange" style="width:245px" class="input-sm"
                             placeholder="按时间范围查询" aria-controls="datatables">
                      <input type="hidden" name="startTime" id="startTime"/>
                      <input type="hidden" name="endTime" id="endTime"/>
                    </div>
                  </div>
                  <div class="col-xs-4">
                    <div class="input-group">
                      <input type="text" id="searchKey" style="width:200px" name="searchKey" value="${searchKey}"
                             class="input-sm" placeholder="文件名称、存储路径、使用对象" aria-controls="datatables">
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
  <script src="${ctx}/static/js/jquery.colorbox.min.js"></script>
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

    var $overflow = '';
    var colorbox_params = {
      rel: 'colorbox',
      reposition:true,
      scalePhotos:true,
      scrolling:false,
      previous:'<i class="ace-icon fa fa-arrow-left"></i>',
      next:'<i class="ace-icon fa fa-arrow-right"></i>',
      close:'&times;',
      current:'{current} of {total}',
      maxWidth:'100%',
      maxHeight:'100%',
      onOpen:function(){
        $overflow = document.body.style.overflow;
        document.body.style.overflow = 'hidden';
      },
      onClosed:function(){
        document.body.style.overflow = $overflow;
      },
      onComplete:function(){
        $.colorbox.resize();
      }
    };

    var grid_selector = "#grid-table";
    var grid_page_url = "${ctx}/api/annex/page";
    var grid_del_url = "${ctx}/api/annex/delete/";
    var grid_batch_del_url = "${ctx}/api/annex/batch/delete";
    var grid_download_url = "${ctx}/admin/annex/download/";

    jQuery(function ($) {
      $(grid_selector).foomei_JqGrid({
        url: grid_page_url,
        colNames: ['操作', '文件名称', '存储路径', '使用对象', '创建日期'],
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
                + '<div title="下载" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="green btn-download" href="javascript:void(0);" data-id="' + rowObject.id + '"><i class="ace-icon fa fa-download bigger-140"></i></a></div>'
                + '</div>';
            }
          },
          {name: 'name', index: 'name', width: 100, formatter: function (cellvalue, options, rowObject) {
            var content = cellvalue;
            if(rowObject.type == 'jpg' || rowObject.type == 'jpeg' || rowObject.type == 'png' || rowObject.type == 'gif') {
              content += '<a href="${ctx}/annex/' + rowObject.path + '" title="' + rowObject.name + '" data-rel="colorbox"><i class="ace-icon fa fa-eye bigger-140"></i></a>';
            }
            return content;
          }},
          {name: 'path', index: 'path', width: 200},
          {name: 'objectType', index: 'objectType', width: 100},
          {
            name: 'createTime',
            index: 'createTime',
            formatter: 'date',
            formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'},
            width: 100
          }
        ],
        sortname: 'createTime',
        sortorder: 'desc',
        renderCallback: function(data) {
          $('td [data-rel="colorbox"]').colorbox(colorbox_params);
        },
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

      $(document).on('click', ".btn-download", function () {
        var id = $(this).attr("data-id");
        window.location.href = grid_download_url + id;
      });
    })
  </script>
</pageJs>
</html>