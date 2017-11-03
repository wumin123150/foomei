<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>消息管理</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/layui/page.css" media="all"/>
  <link rel="stylesheet" href="${ctx}/static/js/zTree/metroStyle/metroStyle.css">
  <style type="text/css">
    .input-required {
      margin-left: 2px;
      color: #c00;
    }

    ul.ztree {
      margin-top: 0;
      border: 1px solid #d5d5d5;
      background: #fff;
      width: 100% !important;
      height: 200px;
      overflow-y: scroll;
      overflow-x: auto;
    }
  </style>
</head>
<body class="kit-main">
<form class="layui-form layui-form-pane" action="${ctx}/api/messageText/create" method="post" style="width:80%;">
  <input type="hidden" name="id" id="id" value="${role.id}"/>
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">内容<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <textarea name="content" placeholder='内容' class="layui-textarea">${message.content}</textarea>
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">接收人<span class="input-required">*</span></label>
    <div class="layui-input-block">
      <input type="text" name="userNames" id="userNames" value="${userNames}" placeholder="接收人" class="layui-input" onclick="showMenu();" readonly/>
      <div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index: 1;">
        <ul id="tree" class="ztree"></ul>
      </div>
      <input type="text" name="users" id="users" class="hide"/>
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="save">保存</button>
      <button class="layui-btn layui-btn-primary btn-close">关闭</button>
    </div>
  </div>
</form>
<script src="${ctx}/static/js/layui/layui.js"></script>
<script src="${ctx}/webjars/jquery/jquery.min.js"></script>
<script src="${ctx}/static/js/zTree/jquery.ztree.core.min.js"></script>
<script src="${ctx}/static/js/zTree/jquery.ztree.excheck.min.js"></script>
</body>
<script>
  layui.use(['form'], function () {
    var form = layui.form,
      layer = layui.layer,
      $ = layui.jquery;

    //监听提交
    form.on('submit(save)', function (data) {
      var loadIndex = layer.load();
      $.ajax({
        url: data.form.action,
        type: 'POST',
        cache: false,
        data: data.field,
        dataType: 'json',
        success: function (result) {
          if (result.success) {
            loadIndex && layer.close(loadIndex);
            layer.msg('保存成功', {icon: 1});
            parent.layer.closeAll("iframe");
            parent.location.reload();
          } else {
            loadIndex && layer.close(loadIndex);
            if(result.data) {
              var message = '';
              for(var i=0;i<result.data.length;i++) {
                message += result.data[i].errorMsg + '<br>';
              }
              layer.msg(message, {icon: 2});
            } else
              layer.msg(result.message, {icon: 2});
          }
        },
        error: function () {
          loadIndex && layer.close(loadIndex);
          layer.msg('未知错误，请联系管理员', {icon: 5});
        }
      });
      return false;
    });

    $('.btn-close').on('click', function(){
      parent.layer.closeAll("iframe");
      return false;
    });
  });

  var setting = {
    check: {
      enable: true,
      chkboxType: {"Y": "ps", "N": "ps"}
    },
    data: {
      simpleData: {
        enable: true,
        idKey: "id",
        pIdKey: "parentId",
        rootPId: null
      }
    },
    view: {
      selectedMulti: true,
      autoCancelSelected: true
    }, callback: {
      onCheck: function (event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("tree");
        var nodes = zTree.getCheckedNodes(true);
        var names = new Array(), ids = new Array();

        $("#users").val('');
        for (var i = 0, l = nodes.length; i < l; i++) {
          if(!nodes[i].isParent) {
            var id = nodes[i].id.substr(nodes[i].id.indexOf('_') + 1);
            if(ids.indexOf(id) == -1) {
              names.push(nodes[i].name);
              ids.push(id);
            }
          }
        }
        $("#userNames").val(names.join(','));
        $("#users").val(ids.join(','));
      }
    }
  };

  function showMenu() {
    $("#menuContent").css({width: $("#userNames").outerWidth() - 12}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
  }

  function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
  }

  function onBodyDown(event) {
    if (!(event.target.id == "userNames" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
      hideMenu();
    }
  }

  jQuery(function ($) {
    $.get('${ctx}/api/userGroup/list', function(result) {
      if(result.success && result.data.length) {
        for(var i =0; i < result.data.length; i++) {
          result.data[i].open = true;
          result.data[i].isParent = true;
        }
        $.fn.zTree.init($('#tree'), setting, result.data);

        for(var i =0; i < result.data.length; i++) {
          var groupId = result.data[i].id;
          $.get('${ctx}/api/membership/list?groupId=' + groupId, function(result) {
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
  })
</script>
</body>
</html>