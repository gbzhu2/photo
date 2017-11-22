/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2016-03-28 15:44:42
 * @version $Id$
 */

/**
 * @class Class for calculating pagination values
 */
$.PaginationCalculator = function (maxentries, opts) {
	this.maxentries = maxentries;
	this.opts = opts;
};

$.extend($.PaginationCalculator.prototype, {
	/**
	 * Calculate the maximum number of pages
	 * @method
	 * @returns {Number}
	 */
	numPages: function () {
		return Math.ceil(this.maxentries / this.opts.items_per_page);
	},
	/**
	 * Calculate start and end point of pagination links depending on 
	 * current_page and num_display_entries.
	 * @returns {Array}
	 */
	getInterval: function (current_page) {
		var ne_half = Math.floor(this.opts.num_display_entries / 2);
		var np = this.numPages();
		var upper_limit = np - this.opts.num_display_entries;
		var start = current_page > ne_half ? Math.max(Math.min(current_page - ne_half, upper_limit), 0) : 0;
		var end = current_page > ne_half ? Math.min(current_page + ne_half + (this.opts.num_display_entries % 2), np) : Math.min(this.opts.num_display_entries, np);
		return { start: start, end: end };
	}
});

// Initialize jQuery object container for pagination renderers
$.PaginationRenderers = {};

/**
 * @class Default renderer for rendering pagination links
 */
$.PaginationRenderers.defaultRenderer = function (maxentries, opts) {
	this.maxentries = maxentries;
	this.opts = opts;
	this.pc = new $.PaginationCalculator(maxentries, opts);
};
$.extend($.PaginationRenderers.defaultRenderer.prototype, {
	/**
	 * Helper function for generating a single link (or a span tag if it's the current page)
	 * @param {Number} page_id The page id for the new item
	 * @param {Number} current_page 
	 * @param {Object} appendopts Options for the new item: text and classes
	 * @returns {jQuery} jQuery object containing the link
	 */
	createLink: function (page_id, current_page, appendopts) {
		var lnk, np = this.pc.numPages();
		page_id = page_id < 0 ? 0 : (page_id < np ? page_id : np - 1); // Normalize page id to sane value
		appendopts = $.extend({ text: page_id + 1, classes: "" }, appendopts || {});
		if (page_id == current_page || page_id == -1) {
			lnk = $("<span class='current'>" + appendopts.text + "</span>");
		}
		else {
			lnk = $("<a>" + appendopts.text + "</a>")
				.attr('href', this.opts.link_to.replace(/__id__/, page_id));
		}
		if (appendopts.classes) { lnk.addClass(appendopts.classes); }
		lnk.data('page_id', page_id);
		return lnk;
	},
	// Generate a range of numeric links 
	appendRange: function (container, current_page, start, end, opts) {
		var i;
		for (i = start; i < end; i++) {
			this.createLink(i, current_page, opts).appendTo(container);
		}
	},
	getLinks: function (count, current_page, eventHandler) {
		var self = this, begin, end,
			interval = this.pc.getInterval(current_page),
			np = this.pc.numPages(),
			fragment = $("<div class='pagination'></div>");

		// Generate "Previous"-Link
		if (this.opts.prev_text && (current_page > 0 || this.opts.prev_show_always)) {
			fragment.append(this.createLink(current_page - 1, current_page, { text: this.opts.prev_text, classes: "prev" }));
		}
		// Generate starting points
		if (interval.start > 0 && this.opts.num_edge_entries > 0) {
			end = Math.min(this.opts.num_edge_entries, interval.start);
			this.appendRange(fragment, current_page, 0, end, { classes: 'sp' });
			if (this.opts.num_edge_entries < interval.start && this.opts.ellipse_text) {
				jQuery("<span>" + this.opts.ellipse_text + "</span>").appendTo(fragment);
			}
		}
		// Generate interval links
		this.appendRange(fragment, current_page, interval.start, interval.end);
		// Generate ending points
		if (interval.end < np && this.opts.num_edge_entries > 0) {
			if (np - this.opts.num_edge_entries > interval.end && this.opts.ellipse_text) {
				jQuery("<span>" + this.opts.ellipse_text + "</span>").appendTo(fragment);
			}
			begin = Math.max(np - this.opts.num_edge_entries, interval.end);
			this.appendRange(fragment, current_page, begin, np, { classes: 'ep' });
		}
		// Generate "Next"-Link
		if (this.opts.next_text && (current_page < np - 1 || this.opts.next_show_always)) {
			fragment.append(this.createLink(current_page + 1, current_page, { text: this.opts.next_text, classes: "next" }));
		}

		// 跳转
		if (this.opts.show_goto_btn) {
			fragment.append('跳转到<input type="text" class="page-num" name="page_num" value="' +
				((count > 0) ? (current_page + 1) : '') + '" />页</label><button class="page-btn">GO</button>');
			//fragment.append('<label>第<input type="text" name="page_num" value="' + 
			//((count > 0) ? (current_page + 1) : '') + '" />页</label><button>跳转</button>');
			//fragment.append('<span class="page-num-btn"><input class="page-num" type="text" name="page_num" value="' + 
			//((count > 0) ? (current_page + 1) : '') + '" /><button class="page-btn">跳转</button></span>');
		}
		$('a', fragment).click(eventHandler);
		$('button', fragment).click(function (e) {
			if ($('input.page-num', fragment).val() == (current_page + 1)) {
				return false;
			} else {
				eventHandler.call(this, e);
			}
		});
		$('input.page-num', fragment).keyup(function () {
			self.limitInput(this, np);
		});
		$('input.page-num', fragment)[0].onafterpaste = function () {
			self.limitInput(this, np);
		};
		// enter快捷键 支持
		$('input.page-num', fragment).bind("keydown keypress", function (event) {
			if (event.which === 13) {
				$('button', fragment).trigger('click');
				event.preventDefault();
			}
		});

		return fragment;
	},
	limitInput: function (ele, max) {
		var val = $(ele).val();
		val = val.replace(/\D/g, '');
		//            if (val) {
		//                val = parseInt(val);
		//                if (val < 1) val = 1;
		//                if (val > max) val =
		//                    max;
		//            } else {
		//                val = 1;
		//            }
		if (val || val === 0) {
			val = parseInt(val);
			if (val > max) val =
				max;
			if (val === 0) val =
				1;
		}
		$(ele).val(val);
	}

});

// Extend jQuery
$.fn.pagination = function (maxentries, opts) {

	// Initialize options with default values
	opts = jQuery.extend({
		items_per_page: 2,//每页显示的条目数
		num_display_entries: 3,//连续分页主体部分显示的分页条目
		current_page: 0, //当前选中的页码
		num_edge_entries: 1,
		link_to: "javascript:void(0)",//分页的链接
		show_goto_btn: true, //是否显示跳转按钮
		// prev_text: "<",
		// next_text: ">",
		prev_text: "上一页",
		next_text: "下一页",
		ellipse_text: "...",
		prev_show_always: true,
		next_show_always: true,
		renderer: "defaultRenderer",
		load_first_page: true,
		page_render: true,
		callback: function () { return false; }
	}, opts || {});

	var containers = this,
		renderer, links, current_page;

	/**
	 * This is the event handling function for the pagination links. 
	 * @param {int} page_id The new page number
	 */
	function paginationClickHandler(evt) {
		if (evt.target.tagName == "BUTTON") {
			//var goto_current_page = containers.find("input").val();
			var $goto_page = $(evt.target).parent().find("input");
			var goto_current_page = $goto_page.val();
			if (!isNaN(goto_current_page)) {
				goto_current_page = parseInt(goto_current_page);
				if (goto_current_page > 0 && goto_current_page <= np) {
					return selectPage(goto_current_page - 1);
				} else {
					// systemTip("请输入正确的数字",'warning');
					$goto_page.val("");
					return;
				}
			} else {
				// systemTip("请输入数字",'warning');
				$goto_page.val("");
				return;
			}
		} else {
			var new_current_page = $(evt.target).data('page_id'),
				continuePropagation = selectPage(new_current_page);
			if (!continuePropagation) {
				evt.stopPropagation();
			}
			//return continuePropagation;
			//引起IE7崩溃
			return false;
		}
	}
	function paginationGotoHandler(new_current_page) {
		selectPage(new_current_page);
	}
	/**
	 * 系统提示框
	 * @param  {[string]} content [提示内容]
	 * @param  {[number]} time    [提示时间]
	 * @return {[object]}         [提示框]
	 */
	function systemTip(content, stus, time) {
		return $.dialog({
			id: 'Tips',
			title: false,
			cancel: false,
			lock: true,
			drag: false,
			fixed: true,
			opacity: 0.7,
			esc: false,
			background: '#000000'
		})
			.content('<div class="dialog-content icons-' + stus + '">' + content + '</div>')
			.time(time || 1);
	}
	/**
	 * This is a utility function for the internal event handlers. 
	 * It sets the new current page on the pagination container objects, 
	 * generates a new HTMl fragment for the pagination links and calls
	 * the callback function.
	 */
	function selectPage(new_current_page) {
		// update the link display of a all containers
		containers.data('current_page', new_current_page);
		links = renderer.getLinks(maxentries, new_current_page, paginationClickHandler);
		containers.empty();
		links.appendTo(containers);
		// call the callback and propagate the event if it does not return false
		var continuePropagation = opts.callback(new_current_page, containers);
		return continuePropagation;
	}

	// -----------------------------------
	// Initialize containers
	// -----------------------------------
	current_page = opts.current_page;
	containers.data('current_page', current_page);
	// Create a sane value for maxentries and items_per_page
	maxentries = (!maxentries || maxentries < 0) ? 0 : maxentries;
	opts.items_per_page = (!opts.items_per_page || opts.items_per_page < 0) ? 1 : opts.items_per_page;

	if (!$.PaginationRenderers[opts.renderer]) {
		throw new ReferenceError("Pagination renderer '" + opts.renderer + "' was not found in jQuery.PaginationRenderers object.");
	}
	renderer = new $.PaginationRenderers[opts.renderer](maxentries, opts);

	// Attach control events to the DOM elements
	var pc = new $.PaginationCalculator(maxentries, opts);
	var np = pc.numPages();
	containers.bind('setPage', { numPages: np }, function (evt, page_id) {
		if (page_id >= 0 && page_id < evt.data.numPages) {
			selectPage(page_id); return false;
		}
	});
	containers.bind('prevPage', function (evt) {
		var current_page = $(this).data('current_page');
		if (current_page > 0) {
			selectPage(current_page - 1);
		}
		return false;
	});
	containers.bind('nextPage', { numPages: np }, function (evt) {
		var current_page = $(this).data('current_page');
		if (current_page < evt.data.numPages - 1) {
			selectPage(current_page + 1);
		}
		return false;
	});

	// When all initialisation is done, draw the links
	links = renderer.getLinks(maxentries, current_page, paginationClickHandler);
	containers.empty();
	links.appendTo(containers);
	// call callback function
	if (opts.load_first_page) {
		opts.callback(current_page, containers);
	}
}; // End of $.fn.pagination block