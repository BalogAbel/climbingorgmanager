var siteFunctions = {
	//patch to fix a problem that the context menu disappears after update
	//delay the show to occure after the update
	patchContextMenuShow: function () {
		'use strict';
		var protShow = PrimeFaces.widget.ContextMenu.prototype.show;
		siteFunctions.patchContextMenuShow.lastEvent = null;
		PrimeFaces.widget.ContextMenu.prototype.show = function (e) {
			var ret;
			if (e) {

//                console.log('saving last event');

				siteFunctions.patchContextMenuShow.lastEvent = e;
				siteFunctions.patchContextMenuShow.lastEventArg = arguments;
				siteFunctions.patchContextMenuShow.lastEventContext = this;
			} else if (siteFunctions.patchContextMenuShow.lastEvent) {

//                console.log('executing last event');

				ret = protShow.apply(siteFunctions.patchContextMenuShow.lastEventContext, siteFunctions.patchContextMenuShow.lastEventArg);

//                console.log('clearing last event');
				siteFunctions.patchContextMenuShow.lastEvent = null;
			}
			return ret;
		};
	}
};

$(document).ready(function () {
	'use strict';
	try {
		siteFunctions.patchContextMenuShow();
	} catch (e) {
		console.error(e);
	}
});