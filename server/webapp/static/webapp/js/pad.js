// ---------------------------------- Utils ----------------------------------
(function() {
	if (!HTMLDivElement.prototype.setSelectionRange) {
		HTMLDivElement.prototype.setSelectionRange = function(start, end) {
			if (this.createTextRange) {
				var range = this.createTextRange();
				this.collapse(true);
				this.moveEnd('character', end);
				this.moveStart('character', start);
				this.select();
			}
		};
	}
})();
function getKey(dict, value) {
	for (var key in dict) {
		if (dict[key] == value) {
			return key;
		}
	};
	return null;
};

// --------------------------------- Classes ---------------------------------
function PadMessage(data) {
	if (data)
		for (var i in data)
		this[i] = data[i];
}

PadMessage.__CODES__ = {
	purpose : "\u0001",
	login : "\u0002",
	password : "\u0003",
	token : "\u0004",
	message : "\u0005",
};
PadMessage.__UNIT_DELIMTR__ = "\u001F\u001F";
PadMessage.__INNER_DELIMTR__ = "\u001F";
PadMessage.__VERSION__ = "\u0001";

/**
 * Decodes single message record encoded with algorithm used by Academia
 * PadServer, Academia Android Client and Academia WebApp
 * @param {String} message Raw message from Pad server
 * @returns {PadMessage}
 */
PadMessage.prototype.decode = function(message) {
	var units = message.replace(/\n/g, '').split(PadMessage.__UNIT_DELIMTR__);
	for (var i in units) {
		var pair = units[i].split(PadMessage.__INNER_DELIMTR__);
		var key = getKey(PadMessage.__CODES__, pair[0]) || pair[0];
		//data[key] = (pair.length == 1) || pair[1];
		try {
			this[key] = (pair.length == 1) || B64.decode(pair[1]);
		} catch (err) {
			console.error(err);
		}
	}
	return this;
};
/**
 * Encodes single data dictionary with algorithm used by Academia PadServer,
 * Academia Android Client and Academia WebApp
 * @param {PadMessage} message Raw message from Pad server
 * @returns {String}
 */
PadMessage.prototype.encode = function() {
	return PadMessage.encode(this);
};
PadMessage.encode = function(data) {
	var units = [];
	for (var key in PadMessage.__CODES__) {
		if ( key in data) {
			units.push(PadMessage.__CODES__[key] + ((data[key] === true) ? "" : (PadMessage.__INNER_DELIMTR__ + B64.encode(data[key]))));
		}
	}
	return units.join(PadMessage.__UNIT_DELIMTR__);
};

// ----------------------------------- Defs ----------------------------------

/**
 * @todo Shall be further used in auth
 */
academia.pad.TOKEN
academia.pad.PERIOD = 500;

// ---------------------------------- Logic ----------------------------------
(function() {
	padContentElement = $('#pad-content')[0];
	var dmp = new diff_match_patch();
	var prev = padContentElement.textContent;
	// for now it's better to process textContent instead of innerHtml
	var curr = "";

	/**
	 * Patches fifo
	 */
	patches = [];
	var monitoring = false;
	/**
	 * Get changes monitor with on_local_patches callback set.
	 * This monitor watches for patches packages stored in patches fifo
	 * and calls applying each of them on local text.
	 *
	 * If no patches are available, it tests local text for local changes
	 * and calls preset callback (on_local_patches) witch should handle
	 * those. Should be invoked periodically (with quite hight frequency,
	 * 	eg 2Hz)
	 */
	monitor = function(on_local_patches) {
		return (function() {
			if (!monitoring) {
				monitoring = true;
				if (patches.length > 0) {
					count = patches.length;
					for (var i = 0; i < count; i++) {
						/* @todo newline handling in messages */
						apply(patches.pop(i));
					}
					highlight();
				} else {
					curr = padContentElement.textContent;
					// for now it's better to process text content
					var patches_text = dmp.patch_toText(dmp.patch_make(prev, curr, dmp.diff_main(prev, curr)));
					if (patches_text) {
						prev = curr;
						on_local_patches(patches_text);
					}
					// highlight();
				}
				monitoring = false;
			}
		});
	};

	/**
	 * Applies all changes stored in single patch package.
	 * @param {String} patch_text
	 */
	apply = function(patch_text) {
		if (patch_text.length > 2) {
			var patches = dmp.patch_fromText(patch_text);
			var s = window.getSelection();
			var r;
			var cursor_offset;
			console.debug(s.type);
			var move_cursor = s.type == "Caret";
			if (move_cursor) {
				r = s.getRangeAt(0);
				cursor_offset = r.startOffset;
				for (var i in patches) {
					if (patches[i].start1 < r.startOffset) {
						cursor_offset += (patches[i].length2 - patches[i].length1);
					}
				}
			}
			var result = dmp.patch_apply(patches, padContentElement.textContent.toString());
			prev = result[0];
			padContentElement.textContent = prev;
			if (move_cursor) {
				var position = cursor_offset;
				var range = document.createRange();
				range.setStart(padContentElement.childNodes[0], position);
				range.setEnd(padContentElement.childNodes[0], position);
				range.collapse(false);
				var sel = window.getSelection();
				sel.removeAllRanges();
				sel.addRange(range);
			}
		}
	};
	/**
	 * Pad websocket itself
	 */
	var socket = new WebSocket("ws://" + window.location.hostname + ":" + academia.pad.PORT);

	/**
	 * On websocket:
	 * Open send humble request to server to get involved in cooperation.
	 */
	socket.onopen = function() {
		socket.send(PadMessage.encode({
			purpose : "pad",
			message : "" + academia.pad.ID
		}));
	};

	/**
	 * On websocket message:
	 * Test what to do with the message and just do it :)
	 */
	socket.onmessage = function(message) {
		var data = (new PadMessage()).decode(message.data);
		if (data && data.purpose && data.message) {
			switch (data.purpose) {
				/**
				 * Patch current text
				 */
				case "patches":
					var patch_text = data.message;
					patches.push(patch_text);
					break;
				/**
				 * React on auth failure
				 * @todo do sth more with it
				 */
				case "auth":
					if (data.message == "failure") {
						alert();
					}
					break;
			}
		}
	};
	/**
	 * What should be done if diffs were detected within currently edited pad.
	 * @param {String} patches_text
	 */
	function on_local_patches(patches_text) {
		message = PadMessage.encode({
			purpose : "patches",
			message : patches_text,
			token : academia.pad.TOKEN
		});
		socket.send(message);
	}

	interval = setInterval(monitor(on_local_patches), academia.pad.PERIOD);

	function highlight() {
		$('#pad-content').each(function(i, e) {
			hljs.highlightBlock(e);
		});
	}

	hljs.initHighlightingOnLoad();
	hljs.configure({languages: ["markdown"]});
	highlight();
})();

