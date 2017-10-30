<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>用户管理</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="stylesheet" href="${ctx}/static/js/layui/css/layui.css" media="all"/>
  <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all" />
</head>
<style>
  .kit-table, .kit-table .kit-table-header {
    position: relative;
    box-shadow: 0 1px 7px 0 #ccc;
  }

  .kit-table .kit-table-header {
    height: 50px;
  }

  .kit-table .kit-table-header .kit-search-btns {
    padding: 10px;
    position: absolute;
  }

  .kit-table .kit-table-header .kit-search-inputs {
    position: absolute;
    right: 70px;
    top: 0;
    padding: 10px 25px 10px 10px;
  }

  .kit-table .kit-table-header .kit-search-inputs .kit-search-keyword {
    margin-right: 10px;
    position: relative;
  }

  .kit-table .kit-table-header .kit-search-inputs .kit-search-keyword input {
    height: 30px;
    line-height: 30px;
    width: 200px;
    padding-right: 32px;
  }

  .kit-table .kit-table-header .kit-search-inputs .kit-search-keyword button {
    position: absolute;
    right: 0;
    top: 0;
    width: 30px;
    height: 30px;
    border: 0;
    cursor: pointer;
    background-color: #009688;
    color: #fff;
  }

  .kit-table .kit-table-header .kit-search-inputs .kit-search-more {
    cursor: pointer;
    color: #009688;
    position: absolute;
    top: 15px;
    right: -50px;
  }

  .kit-table .kit-search-mored {
    width: 100%;
    height: auto;
    top: 51px;
    background-color: #fff;
    z-index: 5;
    box-shadow: 0 4px 7px -3px #ccc;
    position: absolute;
    margin-bottom: 10px;
    display: none;
  }

  .kit-table .kit-search-mored .kit-search-body {
    padding: 10px 10px 45px;
  }

  .kit-table .kit-search-mored .kit-search-footer {
    height: 50px;
    bottom: 0;
    left: 0;
    position: absolute;
    width: 100%;
    border-top: 1px solid #e2e2e2;
    text-align: right;
  }

  .kit-table .kit-search-mored .kit-search-footer .kit-btn {
    margin: 10px 5px;
    padding: 0 15px;
  }

  .kit-table .kit-table-body .layui-table-view {
    margin: 0;
  }
</style>
<body class="childrenBody">
<div class="kit-table">
  <form class="layui-form" lay-filter="kit-search-form">
    <div class="kit-table-header">
      <div class="kit-search-btns">
        <a href="javascript:;" data-action="create" class="layui-btn layui-btn-small"><i class="layui-icon">&#xe608;</i>新增</a>
        <a href="javascript:;" data-action="delSelector" class="layui-btn layui-btn-small layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除所选</a>
      </div>
      <div class="kit-search-inputs">
        <div class="kit-search-keyword">
          <input type="text" class="layui-input" name="realName" placeholder="搜索关键字.." />
          <button lay-submit lay-filter="search"><i class="layui-icon">&#xe615;</i></button>
        </div>
        <div class="kit-search-more" id="kit-search-more">更多筛选<i class="layui-icon">&#xe61a;</i></div>
      </div>
    </div>
    <div class="kit-search-mored layui-anim layui-anim-upbit">
      <div class="kit-search-body">
        <div class="layui-form-item">
          <label class="layui-form-label">输入框</label>
          <div class="layui-input-block">
            <input type="text" name="title" placeholder="请输入标题" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <div class="layui-inline">
            <label class="layui-form-label">验证手机</label>
            <div class="layui-input-inline">
              <input type="tel" name="phone" autocomplete="off" class="layui-input">
            </div>
          </div>
          <div class="layui-inline">
            <label class="layui-form-label">验证邮箱</label>
            <div class="layui-input-inline">
              <input type="text" name="email" autocomplete="off" class="layui-input">
            </div>
          </div>
        </div>
        <div class="layui-form-item">
          <div class="layui-inline">
            <label class="layui-form-label">范围</label>
            <div class="layui-input-inline" style="width: 100px;">
              <input type="text" name="price_min" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid">-</div>
            <div class="layui-input-inline" style="width: 100px;">
              <input type="text" name="price_max" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">单行选择框</label>
          <div class="layui-input-block">
            <select name="interest" lay-filter="aihao">
              <option value=""></option>
              <option value="0">写作</option>
              <option value="1" selected="">阅读</option>
              <option value="2">游戏</option>
              <option value="3">音乐</option>
              <option value="4">旅行</option>
            </select>
          </div>
        </div>
      </div>
      <div class="kit-search-footer">
        <button type="reset" class="layui-btn layui-btn-small layui-btn-primary kit-btn">重置</button>
        <button lay-submit lay-filter="search" class="layui-btn layui-btn-small kit-btn">确定</button>
      </div>
    </div>
  </form>
  <div class="kit-table-body">
    <table id="kit-table" lay-filter="kit-table"></table>
    <script type="text/html" id="kit-table-bar">
      <a class="layui-btn layui-btn-mini" lay-event="detail">查看</a>
      <a class="layui-btn layui-btn-mini" lay-event="edit">编辑</a>
      <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del">删除</a>
    </script>
  </div>
</div>
<script src="${ctx}/static/js/layui/layui.js"></script>
</body>
<script>
  var tableId = 'kit-table';
  var tableFilter = 'kit-table';
  var table_page_url = "${ctx}/api/user/page";
  layui.use('table', function () {
    var table = layui.table,
      layer = layui.layer,
      $ = layui.jquery,
      form = layui.form;

    var kitTable = table.render({
      elem: '#' + tableId,
      id: tableId,
      url: table_page_url,
      height: 'full-50',
      cols: [
        [
          { checkbox: true, fixed: true },
          { field: 'id', title: 'ID', width: 80 },
          { field: 'loginName', title: '账号', width: 100, sort: true },
          { field: 'name', title: '姓名', width: 150 },
          { field: 'mobile', title: '手机号', width: 150 },
          { field: 'email', title: '邮箱', width: 150 },
          { field: 'status', title: '状态', width: 80 },
          { fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#kit-table-bar' }
        ]
      ],
      even: true,
      page: true,
      limits: [10, 20, 50, 100],
      limit: 10,
      request: {
        pageName: 'pageNo',
        limitName: 'pageSize'
      },
      response: {
        statusName: 'errorCode',
        statusCode: 0,
        msgName: 'message',
        countName: 'data.totalElements',
        dataName: 'data.content'
      }
    });
    //渲染表单
    form.render(null, 'kit-search-form');
    //监听搜索表单提交
    form.on('submit(search)', function (data) {
      console.log(data.field);
      //layer.msg(JSON.stringify(data.field));
      //带条件查询
      kitTable.reload({
        where: data.field
      });
      return false;
    });
    //监听排序
    table.on('sort(' + tableFilter + ')', function (obj) {
      table.reload(tableId, {
        initSort: obj,
        where: {
          sortBy: obj.field,
          sortDir: obj.type
        }
      });
    });
    //监听工具条
    table.on('tool(' + tableFilter + ')', function (obj) {
      var data = obj.data; //获得当前行数据
      var layEvent = obj.event; //获得 lay-event 对应的值
      var tr = obj.tr; //获得当前行 tr 的DOM对象
      //console.log(obj);

      if (layEvent === 'detail') { //查看
        //do somehing
      } else if (layEvent === 'del') { //删除
        // var htm = [
        //     '<p>确定要删除吗?</p>',
        //     '<button class="layui-btn layui-btn-mini layui-btn-primary">取消</button>',
        //     '<button class="layui-btn layui-btn-mini">确定</button>'
        // ];
        // layer.tips(htm.join(''), this, {
        //     time: 0,
        //     skin: 'kit-table'
        // });
        layer.confirm('真的删除行么', function (index) {
          obj.del(); //删除对应行（tr）的DOM结构
          layer.close(index);
          //向服务端发送删除指令
        });
      } else if (layEvent === 'edit') { //编辑
        //do something

        //同步更新缓存对应的值
        obj.update({
          username: '123',
          title: 'xxx'
        });
      }
    });
    $('#kit-search-more').on('click', function () {
      $('.kit-search-mored').toggle();
    });

    var tab = parent.tab;
    $('.kit-search-btns > a').off('click').on('click', function () {
      var $that = $(this),
        action = $that.data('action');
      switch (action) {
        case 'create':
          var index = layer.open({
            title : "添加会员",
            type : 2,
            content : "addUser.html",
            success : function(layero, index){
              setTimeout(function(){
                layui.layer.tips('点击此处列表', '.layui-layer-setwin .layui-layer-close', {
                  tips: 3
                });
              },50000)
            }
          })
          //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
          $(window).resize(function(){
            layer.full(index);
          })
          layer.full(index);
          break;
        case 'delSelector':
          break;
      }
    });
  });
</script>
</html>