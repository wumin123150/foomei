<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <title>系统接口检测</title>
</head>
<myLink>
  <!-- page specific plugin styles -->
  <link rel="stylesheet" href="${ctx}/static/css/jquery.gritter.min.css"/>
</myLink>
<myCss>
  <!-- inline styles related to this page -->
  <style>
    .input-required {
      margin-left: 2px;
      color: #c00;
    }
  </style>
</myCss>
<body>
<div class="main-content">
  <div class="main-content-inner">
    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">
      <div class="page-header">
        <h1>
          系统接口检测
        </h1>
      </div><!-- /.page-header -->

      <div class="row">
        <div class="col-xs-12">
          <form class="form-horizontal" id="validation-form" role="form">
            <!-- #section:elements.form -->
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-url"> 地址<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="url" id="form-url" placeholder="地址" class="form-control"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-type"> 操作<span
                    class="input-required">*</span> </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <select name="type" id="form-type" class="form-control" data-placeholder="操作">
                        <option value="get">get</option>
                        <option value="post">post</option>
                        <option value="upload">upload</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-params"> 数据 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="params" id="form-params" placeholder="数据 " class="form-control"></textarea>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row" id="fileDiv">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-file"> 附件 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="file" id="form-file" name="file"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-time"> 用时(毫秒) </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <input type="text" name="time" id="form-time" placeholder="用时(毫秒)" class="form-control" readonly/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-xs-12">
                <div class="form-group">
                  <label class="col-xs-12 col-sm-3 control-label no-padding-right" for="form-result"> 返回结果 </label>
                  <div class="col-xs-12 col-sm-8">
                    <div class="clearfix">
                      <textarea name="result" id="form-result" placeholder="返回结果 " class="form-control" rows="5"
                                readonly></textarea>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="clearfix form-actions">
              <div class="col-sm-offset-5 col-sm-7">
                <button class="btn btn-info btn-sm" type="button" id="submit">
                  <i class="ace-icon fa fa-check bigger-110"></i>
                  提交
                </button>
              </div>
            </div>
          </form>
          <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.page-content -->
  </div>
</div><!-- /.main-content -->
</body>
<myScript>
  <!-- page specific plugin scripts -->
  <script src="${ctx}/static/js/jquery.gritter.min.js"></script>
  <script src="${ctx}/static/js/ajaxfileupload.js"></script>
</myScript>
<myJs>
  <!-- inline scripts related to this page -->
  <script type="text/javascript">
    jQuery(function ($) {
      $('#fileDiv').hide();

      $('#form-file').ace_file_input({
        no_file: '请选择 ...',
        btn_choose: '选择',
        btn_change: '修改',
        droppable: true,
        thumbnail: false //| true | large
        //whitelist:'gif|png|jpg|jpeg'
        //blacklist:'exe|php'
        //onchange:''
        //
      });

      $('#form-type').change(function () {
        var value = $(this).children('option:selected').val();
        if (value == "upload") {
          $('#fileDiv').show();
        } else {
          $('#fileDiv').hide();
        }
      });

      $("#submit").click(function () {
        var startTime = new Date();
        var url = $("input[name='url']").val();
        var type = $("select[name='type']").val();
        var data = $("textarea[name='params']").val();

        if (type == "upload") {
          var arr = data.split("&");
          data = {};
          for (var i = 0; i < arr.length; i++) {
            var value = arr[i].split("=");
            data[value[0]] = value[1];
          }
          $.ajaxFileUpload({
            url: url,
            secureuri: false,//安全协议
            fileElementId: 'form-file',
            type: 'POST',
            data: data,
            cache: false,
            dataType: 'json',
            success: function (result) {
              var endTime = new Date();
              var time = endTime.getTime() - startTime.getTime();

              $("input[name='time']").val(time);
              $("textarea[name='result']").val(JSON.stringify(result));
            },
            error: function () {
              $.gritter.add({
                title: '错误',
                text: '未知错误',
                class_name: 'gritter-error gritter-center gritter-light gritter-width'
              });
            }
          });
        } else {
          $.ajax({
            url: url,
            type: type,
            data: data,
            cache: false,
            dataType: 'json',
            success: function (result) {
              var endTime = new Date();
              var time = endTime.getTime() - startTime.getTime();

              $("input[name='time']").val(time);
              $("textarea[name='result']").val(JSON.stringify(result));
            },
            error: function () {
              $.gritter.add({
                title: '错误',
                text: '未知错误',
                class_name: 'gritter-error gritter-center gritter-light gritter-width'
              });
            }
          });
        }
      });
    })
  </script>
</myJs>
</html>