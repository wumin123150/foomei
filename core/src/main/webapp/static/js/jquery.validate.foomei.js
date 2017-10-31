//中文验证 
jQuery.validator.addMethod("chinese", function(value, element) {
	var chinese = /^[\u4e00-\u9fa5]+$/;
	return this.optional(element) || (chinese.test(value));
}, "只能输入中文");
//密码验证 
jQuery.validator.addMethod("pass", function(value, element) {       
	return this.optional(element) || /^(?![0-9]+$)(?![A-Z]+$)(?![a-z]+$)(?![`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?]+$)[a-zA-Z0-9`~!@#$%^&*()+=|\\\][\]\{\}:;'\,.<>/?_]+$/.test(value);    
}, "密码过于简单，请尝试“字母+数字”组合");   
//手机验证 
jQuery.validator.addMethod("mobile", function(value, element) {       
	return this.optional(element) || /^1[3-8]\d{9}$/.test(value);    
}, "请输入有效的手机号码"); 
//日期比较验证
jQuery.validator.addMethod("compareDate", function(value, element, param) { 
	var target = $(param);
	if (this.settings.onfocusout) {
		target.unbind(".validate-equalTo").bind("blur.validate-equalTo", function() {
			$(element).valid();
		});
	}
	if(value != '' && target.val() != '') {
		var date1 = new Date(Date.parse(target.val().replace("-", "/"))); 
		var date2 = new Date(Date.parse(value.replace("-", "/"))); 
		return date1 < date2; 
	}
	return true;
}, "结束日期必须大于等于开始日期");  