<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>用户选择</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/layui/page.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
  <style type="text/css">
    .ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
    .ztree li ul.level0 {padding:0; background:none;}
  </style>
</head>
<body class="kit-main">
<ul id="tree" class="ztree"></ul>
<input type="hidden" id="userId">
<input type="hidden" id="userName">
<script src="${ctx}/static/js/layui/layui.js"></script>
<script src="${ctx}/webjars/jquery/jquery.min.js"></script>
<script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
</body>
<script>
  layui.use('table', function () {
    var form = layui.form,
      layer = layui.layer,
      $ = layui.jquery;

    var index = parent.layer.getFrameIndex(window.name);

    $('.btn-close').on('click', function(){
      parent.layer.close(index);
      return false;
    });
  });

  var setting = {
    data: {
      simpleData: {
        enable: true,
        idKey: "id",
        pIdKey: "parentId",
        rootPId: null
      }
    },
    view: {
      selectedMulti: false,
      autoCancelSelected: true
    }, callback: {
      beforeClick: function(treeId, treeNode, clickFlag) {
        return !treeNode.isParent;
      },
      onClick: function(event, treeId, treeNode, clickFlag) {
        if (clickFlag === 0 || treeNode.id == 0) {
          $("#userId").val('');
          $("#userName").val('');
        } else {
          $("#userId").val(treeNode.id.substr(treeNode.id.indexOf('_') + 1));
          $("#userName").val(treeNode.name);
        }
      }
    }
  }

  jQuery(function ($) {
    $.getJSON('${ctx}/api/userGroup/list', function(result) {
      if(result.success) {
        for(var i=0;i<result.data.length;i++) {
          if(result.data[i].parentId == null) {
            result.data[i].parentId = 0;
          }
          result.data[i].open = true;
          result.data[i].isParent = true;
        }
        result.data[result.data.length] = { id:0, parentId:null, name:"组织机构树", open:true};
        $.fn.zTree.init($('#tree'), setting, result.data);

        for(var i =0; i < result.data.length; i++) {
          $.getJSON('${ctx}/api/membership/list?groupId=' + result.data[i].id, function(result, status, jqXHR) {
            var groupId = this.url.substr(this.url.indexOf('=') + 1);
            if(result.success && result.data.length > 0) {
              for (var i = 0; i < result.data.length; i++) {
                result.data[i].id = groupId + '_' + result.data[i].id;
              }
              var zTree = $.fn.zTree.getZTreeObj("tree");
              var parentNode = zTree.getNodeByParam("id", groupId, null);
              zTree.addNodes(parentNode, result.data, true);
            }
          })
        }
      }
    });
  });
</script>
</html>