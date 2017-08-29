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
	<link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.min.css" />
<link rel="stylesheet" href="${ctx}/static/css/daterangepicker.min.css" /> 
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
	</style>
</pageCss>
<body>
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<!-- #section:basics/content.breadcrumbs -->
				<div class="breadcrumbs" id="breadcrumbs">
					<script type="text/javascript">
						try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
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
									<div class="dataTables_filter row">
										<div class="col-xs-8">
										<div class="input-daterange input-group">
											<label>操作时间范围:</label>
												 <input type="text" name="plantRange" id="form-planRange" style="width:245px" class="input-sm" placeholder="按时间范围查询" aria-controls="datatables">
 									          <input type="hidden" name="startTime" id="form-startTime" />
 									          <input type="hidden" name="endTime" id="form-endTime" />
										</div>
										</div>
										<div class="col-xs-4">
										<div class="input-group">
											<input type="text" id="searchKey" style="width:200px" name="searchKey" value="${searchKey}" class="input-sm" placeholder="操作用户、操作描述、URL、IP" aria-controls="datatables">
											<input style="display:none" />
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
  <script src="${ctx}/static/js/date-time/daterangepicker.min.js"></script> </script>
</pluginJs>
<pageJs>
	<!-- inline scripts related to this page -->
	<script type="text/javascript">
		var locale = {
			format : "YYYY-MM-DD",
			separator: " ~ ",
			applyLabel : "确定",
			cancelLabel : "取消",
			fromLabel : "起始时间",
			toLabel: "结束时间'",
			customRangeLabel : "自定义",
			weekLabel : "W",
			daysOfWeek : [ "日", "一", "二", "三", "四", "五", "六" ],
			monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
			firstDay : 1
		};
		
		function getTimeOne(data) {
			return data < 10 ? ("0" + data) : data;
		}

		function getStartTimeStr(day) {
			var dd = new Date();
			dd.setDate(dd.getDate() + day);//获取AddDayCount天后的日期
			var y = getTimeOne(dd.getFullYear());
			var m = getTimeOne(dd.getMonth() + 1);//获取当前月份的日期
			var d = getTimeOne(dd.getDate()); 
			return y + "-" + m + "-" + d + "00:00";
		}
		
		function getEndTimeStr(day) {
			var dd = new Date();
			dd.setDate(dd.getDate() + day);//获取AddDayCount天后的日期
			var y = getTimeOne(dd.getFullYear());
			var m = getTimeOne(dd.getMonth() + 1);//获取当前月份的日期
			var d = getTimeOne(dd.getDate()); 
			return y + "-" + m + "-" + d + "23:59";
		}
		
		var ranges = { 
	    "清空" : [],
	 		"今天" : [ getStartTimeStr(0), getEndTimeStr(0) ],
			"昨天" : [ getStartTimeStr(-1), getEndTimeStr(-1) ],
			"近三天" : [ getStartTimeStr(-2), getEndTimeStr(0) ]
		};
		
		$("#form-planRange").daterangepicker({
			locale : locale,
			/* timePicker : true,
			timePicker24Hour : true, */
			ranges : ranges, 
			 
		});
		$("#form-planRange").attr("value", "");
		function setTimeRange() {
			var range = $("#form-planRange").val().split("~");
			var startTime = range[0].trim();
			var endTime = range[1].trim();
			$("#form-startTime").attr("value", startTime + " 00:00");
			$("#form-endTime").attr("value", endTime+" 23:59");
			if(startTime == endTime) {
				$("#form-planRange").val(startTime);
			}
		} 
		$(document).on("change", "#form-planRange", function() { 
		     setTimeRange();
	 	}); 
		 $(document).on("click", "li[data-range-key='清空']", function() {
			 $("#form-planRange").attr("value", "");
			 $("#form-planRange").val("");
			 $("#form-startTime").attr("value", "");
			 $("#form-endTime").attr("value", "");
		 });
		 $("#form-planRange").attr("value", "");
		 $("#form-planRange").val("");
		 
		 $("button.cancelBtn").bind("click", function(e) {
	  		if($("#form-planRange").val() == "") {
				$("#form-planRange").attr("value", "");
				$("#form-planRange").val("");
				$("#form-startTime").attr("value", "");
				$("#form-endTime").attr("value", "");
				$("div.daterangepicker.dropdown-menu").css("display", "none");
				e.stopPropagation();
			}     
		 }); 
	 $.fn.serializeObject = function() {
         var o = {};
         var a = this.serializeArray();
         $.each(a, function() {
             if (o[this.name] !== undefined) {
                 if (!o[this.name].push) {
                     o[this.name] = [o[this.name]];
                 }
                 o[this.name].push(this.value || '');
             } else {
                 o[this.name] = this.value || '';
             }
         });
         return o;
     };
		var grid_selector = "#grid-table";
		var grid_page_url = "${ctx}/api/log/page";
		var grid_del_url = "${ctx}/front/log/delete/";

		jQuery(function($) {
 			
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
	  		  {name:'myop',index:'', width:80, fixed:true, sortable:false, resize:false, search:false,
						formatter:function(cellvalue, options, rowObject) {
							return '<div class="action-buttons">'
								+ '<div title="删除" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\')"><a class="red btn-del" href="javascript:void(0);" data-id="' + rowObject.id + '"><i class="ace-icon fa fa-trash-o bigger-140"></i></a></div>'
								+ '</div>';
						}
					},

					{name:'username', index:'username', width:50, sortable:false},
					{name:'description', index:'description', width:100},
					{name:'url', index:'url', width:200},
					{name:'ip', index:'ip', width:100},
					{name:'logTime', index:'logTime', formatter:'date', formatoptions:{srcformat:'Y-m-d H:i:s', newformat:'Y-m-d H:i:s'}, width:100},
					{name:'spendTime', index:'spendTime', width:100}
			  ],
			  sortname:'logTime',
			  sortorder:'desc',
			  nav: {
			    view: false
			  },

			});

	    $(grid_selector).foomei_JqGrid('resize');
	    
	    $("#searchKey").keydown(function(e) {
          if (e.keyCode == 13) {
              $(grid_selector).jqGrid('setGridParam', {
                  search: false,
                  page: 1,
                  postData: $(".form-search").serializeObject()
              }).trigger("reloadGrid");
          }
      });

      $("#btn-search").click(function() {
          $(grid_selector).jqGrid('setGridParam', {
              search: false,
              page: 1,
              postData: $(".form-search").serializeObject()
          }).trigger("reloadGrid");
      });
			 
			$(document).on('click', ".btn-del", function() {
				var id = $(this).attr("data-id");
				BootstrapDialog.confirm('你确定要删除吗？', function(result) {
					if (result) {
								window.location.href=grid_del_url+id;
	 				}
				});
			});
		})
	</script>
</pageJs>
</html>