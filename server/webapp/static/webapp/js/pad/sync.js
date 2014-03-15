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
  for(var key in dict){
    if(dict[key] == value){
      return key;
    }
  };
  return null;
};
// --------------------------


academia.pad.CODES = {
	purpose: "\u0001",
	login: "\u0002",
	password: "\u0003",
	token: "\u0004",
	message: "\u0005",
};

academia.pad.UNIT_DELIMTR = "\u001F\u001F";
academia.pad.INNER_DELIMTR = "\u001F";

academia.pad.PERIOD = 500; // [ms]

academia.pad.VERSION = "\u0001";

academia.pad.TOKEN;

(function() {
	
	function decode(message) {
		var data = {};
		var units = message.replace(/\n/g, '').split(academia.pad.UNIT_DELIMTR);
		for (var i in units) {
			var pair = units[i].split(academia.pad.INNER_DELIMTR);
			var key = getKey(academia.pad.CODES, pair[0]) || pair[0];
			//data[key] = (pair.length == 1) || pair[1];
			try {
				data[key] = (pair.length == 1) || B64.decode(pair[1]);
			} catch (err) {
				console.error(err);
			}
		}
		return data;
	}
	function encode(data) {
		var units = [];
		for (var key in data) {
			units.push(academia.pad.CODES[key] + ((data[key] === true)?"":(academia.pad.INNER_DELIMTR + B64.encode(data[key]))));
		}
		return units.join(academia.pad.UNIT_DELIMTR);
	}
	padContentElement = document.getElementById('pad-content');
	var dmp = new diff_match_patch();
	var diffs = {
		prev: padContentElement.textContent, // for now it's better to process text content
		// prev: padContentElement.innerHTML,
		monitoring: false,
		monitor: function(on_local_patches) {return (function() {
			if (!diffs.monitoring) {
				diffs.monitoring = true;
				if (diffs.patches.length > 0) {
					patches = diffs.patches;
					count = patches.length;
					for (var i=0; i<count; i++) {
						diffs.apply(patches.pop(i));
					}
				} else {
					diffs.curr = padContentElement.textContent; // for now it's better to process text content
					diffs.list = dmp.diff_main(diffs.prev, diffs.curr);
					diffs.patches_text = dmp.patch_toText(dmp.patch_make(diffs.prev, diffs.curr, diffs.list));
					if (diffs.patches_text) {
						diffs.prev = diffs.curr;
						on_local_patches(diffs.patches_text);
					}
				}
				diffs.monitoring = false;
			}
		});},
		patches: [],
		apply: function(patch_text) {
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
				var result = dmp.patch_apply(patches, padContentElement.innerHTML.toString());
				padContentElement.innerHTML = result[0].replace(" ", "&nbsp;");
				diffs.prev = result[0];
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
		}
	};
	var socket = new WebSocket("ws://" + window.location.hostname + ":" + academia.pad.PORT);
	
	socket.onopen = function() {
		socket.send(encode({purpose: "pad", message: "" + academia.pad.ID}));
	};
	
	socket.onmessage = function(message) {
		var data = decode(message.data);
		if (data && data.purpose && data.message) {
			switch (data.purpose) {
				case "patches": 
					var patch_text = data.message;
					diffs.patches.push(patch_text);
					break;
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
		socket.send(encode({purpose: "patches", message: patches_text, token: academia.pad.TOKEN}));
	}
	diffs.interval = setInterval(diffs.monitor(on_local_patches), academia.pad.PERIOD);
})();

