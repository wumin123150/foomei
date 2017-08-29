var weixin = new Framework7({
	// Default title for modals
	modalTitle : '花木汇',
	modalButtonOk : '确定',
	modalButtonCancel : '取消',
	animateNavBackIcon : true,
	precompileTemplates : true,
	template7Pages : true,
	// pushState: true,
	router : true
});

var $$ = Dom7;

var mainView = weixin.addView('.view-main', {
	dynamicNavbar : true,
	animatePages : false,
	swipeBackPage : false
});

$$(document).on('ajaxComplete', function(e) {
	var xhr = e.detail.xhr;
	if (xhr.readyState == 4 && xhr.status == 200) {
		var result = JSON.parse(xhr.responseText);
		if (!result.success) {
			if (result.errno == 10002) {
				// weixin.loginScreen();
				window.location.href = "./login?returnUrl=/w/index";
			}
		}
	}
});

Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

Date.prototype.Week = function() {
	return "周" + "日一二三四五六".split("")[this.getDay()];
}

function parseDate(date) {
	return new Date(date.replace("-", "/").replace("-", "/"));
}

$$(document).on('click', '.login-button', function(e) {
	$$.ajax({
		url : $$('#login-form').data('url'),
		method : 'POST',
		data : {
			username : $$('#username').val(),
			password : $$('#password').val()
		},
		dataType : 'json',
		success : function(result) {
			if (result.success) {
				weixin.closeModal($$('.login-screen'));
			} else {
				weixin.alert('账号或密码错误', '系统消息');
			}
		}
	});
}, true);