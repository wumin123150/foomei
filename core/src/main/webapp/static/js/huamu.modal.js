;(function($, undefined) {
	"use strict";

	var pluginName = 'Modal';

	function Modal(options) {
		this.options = $.extend({}, $.fn[pluginName].defaults, options);
		this.$modal = $(this.options.target).attr('class', 'modal fade').hide();
		var self = this;

		function init() {
			if (self.options.title === '') {
				self.options.title = '&nbsp;';
			}
		};

		init();
	}

	$.extend(Modal.prototype, {
		show: function() {
			var self = this
			,$backdrop;

			if (!this.options.nobackdrop) {
				$backdrop = $('.modal-backdrop');
			}
			
			if (!this.$modal.length) {
				this.$modal = $('<div class="modal fade" id="' + this.options.target.substr(1) + '"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><a class="close" href="#" data-dismiss="modal">×</a><h4 class="modal-title">&nbsp;</h4></div><div class="modal-body"></div><div class="modal-footer"><button type="button" class="btn btn-white" data-dismiss="modal">关闭</button></div></div></div></div>').appendTo(this.options.appendTo).hide();
			}
			
			this.$modal.find('.modal-title').html(this.options.title);

			if (this.options.cssclass !== undefined) {
				this.$modal.find('.modal-dialog').addClass(this.options.cssclass);
			}

			if (this.options.width !== undefined) {
				this.$modal.width(this.options.width);
			}

			if (this.options.left !== undefined) {
				this.$modal.css({'left': this.options.left});
			}

			if (this.options.height !== undefined) {
				this.$modal.height(this.options.height);
			}

			if (this.options.top !== undefined) {
				this.$modal.css({'top': this.options.top});
			}

			if (this.options.keyboard) {
				this.escape();
			}
			
			if (!this.options.nobackdrop) {
				if (!$backdrop.length) {
					$backdrop = $('<div class="modal-backdrop fade in" />').insertBefore(this.$modal.find('.modal-dialog'));
				}
			}

			this.$modal.off('close.' + pluginName).on('close.' + pluginName, function() {
				self.close.call(self);
			});
			if (this.options.remote !== undefined && this.options.remote != '' && this.options.remote !== '#') {
				var spinner;
				if (typeof Spinner == 'function') {
					spinner = new Spinner({color: '#3d9bce'}).spin(this.$modal[0]);
				}
				this.$modal.find('.modal-body').load(this.options.remote, function() {
					if (spinner) {
						spinner.stop();
					}
					if (self.options.cache) {
						self.options.content = $(this).html();
						delete self.options.remote;
					}
				});
			} else {
				this.$modal.find('.modal-body').html(this.options.content);
			}

			$('body').addClass('modal-open');
			
			this.$modal.show().addClass('in');
			return this;
		}

		,close: function() {
			this.$modal.hide().off('.' + pluginName).find('.modal-body').html('');
			if (this.options.cssclass !== undefined) {
				this.$modal.find('.modal-dialog').removeClass(this.options.cssclass);
			}
			$('body').removeClass('modal-open');
			$(document).off('keyup.' + pluginName);
			if (typeof this.options.onClose === 'function') {
				this.options.onClose.call(this, this.options);
			}
			return this;
		}

		,destroy: function() {
			this.$modal.remove();
			$(document).off('keyup.' + pluginName);
			this.$modal = null;
			return this;
		}

		,escape: function() {
			var self = this;
			$(document).on('keyup.' + pluginName, function(e) {
				if (e.which == 27) {
					self.close();
				}
			});
		}
	});


	$.fn[pluginName] = function(options) {
		return this.each(function() {
			var obj;
			if (!(obj = $.data(this, pluginName))) {
				var  $this = $(this)
					,data = $this.data()
					,opts = $.extend({}, options, data)
					;
				if ($this.attr('href') !== '' && $this.attr('href') != '#') {
					opts.remote = $this.attr('href');
				}
				obj = new Modal(opts);
				$.data(this, pluginName, obj);
			}
			obj.show();
		});
	};


	$[pluginName] = function(options) {
		return new Modal(options);
	};


	$.fn[pluginName].defaults = {
		title: '&nbsp;'		// modal title
		,target: '#modal'	// the modal id. MUST be an id for now.
		,cssclass: 'modal-sm'
		,content: ''		// the static modal content (in case it's not loaded via ajax)
		,appendTo: 'body'	// where should the modal be appended to (default to document.body). Added for unit tests, not really needed in real life.
		,cache: false		// should we cache the output of the ajax calls so that next time they're shown from cache?
		,keyboard: false
	};

	 
	$(document).on('click.' + pluginName, '[data-trigger="modal"]', function() {
		$(this)[pluginName]();
		if ($(this).is('a')) {
			return false;
		}
	}).on('click.' + pluginName, '[data-dismiss="modal"]', function(e) {
		e.preventDefault();
		$(this).closest('.modal').trigger('close');
	});
})(jQuery);

;(function($, undefined) {
	"use strict";

	var pluginName = 'Alert';

	function Alert(options) {
		this.options = $.extend({}, $[pluginName].defaults, options);
		this.modal = $.Modal(this.options);
	}

	$.extend(Alert.prototype, {
		show: function() {
			this.modal.show();
			return this;
		}

		,close: function() {
			this.modal.close();
			return this;
		}

		,destroy: function() {
			this.modal.destroy();
			return this;
		}
	});

	$[pluginName] = function(options) {
		return new Alert(options);
	};
	
	$[pluginName].defaults = {
	};
})(jQuery);

;(function($, undefined) {
	"use strict";

	var pluginName = 'Confirm';

	function Confirm(options) {
		this.options = $.extend({}, $.fn[pluginName].defaults, options);

		var $modal = $(this.options.target);
		if (!$modal.length) {
			$modal = $('<div class="modal fade" id="' + this.options.target.substr(1) + '"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><a class="close" href="#" data-dismiss="modal">×</a><h4 class="modal-title">&nbsp;</h4></div><div class="modal-body"></div><div class="modal-footer"><a type="button" class="btn btn-white" href="#" data-dismiss="modal">关闭</a><a class="btn btn-primary" href="#" data-action="1">确定</a></div></div></div></div>').appendTo(this.options.appendTo).hide();
			if (typeof this.options.action == 'function') {
				var self = this;
				$modal.find('[data-action]').attr('href', '#').on('click.' + pluginName, function(e) {
					e.preventDefault();
					self.options.action.call(self);
					self.close();
				});
			} else if (typeof this.options.action == 'string') {
				$modal.find('[data-action]').attr('href', this.options.action);
			}
		}
		this.modal = $.Modal(this.options);
	}

	$.extend(Confirm.prototype, {
		show: function() {
			this.modal.show();
			return this;
		}

		,close: function() {
			this.modal.close();
			return this;
		}

		,destroy: function() {
			this.modal.destroy();
			return this;
		}
	});


	$.fn[pluginName] = function(options) {
		return this.each(function() {
			var obj;
			if (!(obj = $.data(this, pluginName))) {
				var $this = $(this)
					,data = $this.data()
					,title = $this.attr('title') || data.title
					,opts = $.extend({}, $.fn[pluginName].defaults, options, data)
					;
				if (!title) {
					title = 'this';
				}
				opts.content = opts.content.replace(':title', title);
				if (!opts.action) {
					opts.action = $this.attr('href');
				} else if (typeof window[opts.action] == 'function') {
					opts.action = window[opts.action];
				}
				obj = new Confirm(opts);
				$.data(this, pluginName, obj);
			}
			obj.show();
		});
	};

	$[pluginName] = function(options) {
		return new Confirm(options);
	};

	$.fn[pluginName].defaults = {
		content: 'Are you sure you want to delete :title?'
		,target: '#confirm_modal'	// this must be an id. This is a limitation for now, @todo should be fixed
		,appendTo: 'body'	// where should the modal be appended to (default to document.body). Added for unit tests, not really needed in real life.
	};

	$(document).on('click.' + pluginName, '[data-trigger="confirm"]', function() {
		$(this)[pluginName]();
		return false;
	});
})(jQuery);

;(function($, undefined) {
	"use strict";

	var pluginName = 'Message';

	$[pluginName] = function(message, type) {
		clearTimeout($[pluginName].timeout);
		var $selector = $('#' + $[pluginName].options.id);
		if (!$selector.length) {
			$selector = $('<div/>', {id: $[pluginName].options.id}).appendTo($[pluginName].options.appendTo);
		}
		if ($[pluginName].options.animate) {
			$selector.addClass('page_mess_animate');
		} else {
			$selector.removeClass('page_mess_animate');
		}
		$selector.html(message);
		if (type === undefined || type == $[pluginName].TYPE_ERROR) {
			$selector.removeClass($[pluginName].options.okClass).addClass($[pluginName].options.errClass);
		} else if (type == $[pluginName].TYPE_OK) {
			$selector.removeClass($[pluginName].options.errClass).addClass($[pluginName].options.okClass);
		}
		$selector.slideDown('fast', function() {
			$[pluginName].timeout = setTimeout(function() { $selector.slideUp('fast'); }, $[pluginName].options.delay);
		});
	};


	$.extend($[pluginName], {
		options: {
			 id: 'page_message'
			,okClass: 'page_mess_ok'
			,errClass: 'page_mess_error'
			,animate: false
			,delay: 4000
			,appendTo: 'body'	// where should the modal be appended to (default to document.body). Added for unit tests, not really needed in real life.
		},

		TYPE_ERROR: 1,
		TYPE_OK: 2
	});
})(jQuery);