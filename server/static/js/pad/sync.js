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
    // --------------------------
	function getKey(dict, value) {
	  for(var key in dict){
	    if(dict[key] == value){
	      return key;
	    }
	  };
	  return null;
	};
	var codes = {
		purpose: "\u0001",
		pad: "\u0002",
		login: "\u0003",
		password: "\u0004",
		message: "\u0005",
	};
	
	var UNIT_DELIMTR = "\u001F\u001F";
	var INNER_DELIMTR = "\u001F";
	
	var VERSION = "\u0001";
	
	function decode(message) {
		version = message[0];
		if (version == VERSION) {
			data = {};
			units = message.substring(1, message.length).split(UNIT_DELIMTR);
			for (var i in units) {
				pair = units[i].split(INNER_DELIMTR);
				var key = getKey(codes, pair[0]) || pair[0];
				data[key] = (pair.length == 1) || pair[1];
				//data[key] = (pair.length == 1) || B64.decode(pair[1]);
			}
		}
		return data;
	}
	function encode(data) {
		units = [];
		for (var key in data) {
			units.push(codes[key] + ((data[key] == true)?"":(INNER_DELIMTR + data[key])));
		}
		return VERSION + units.join(UNIT_DELIMTR);
	}
	padContentElement = document.getElementById('pad-content');
	var dmp = new diff_match_patch();
	var diffs = {
		prev: padContentElement.innerHTML,
		schedule: function() {
			diffs.curr = padContentElement.innerHTML.toString();
			diffs.list = dmp.diff_main(diffs.prev, diffs.curr, true);
			diffs.patches_text = dmp.patch_toText(dmp.patch_make(diffs.prev, diffs.curr, diffs.list));
			if (diffs.patches_text) {
				diffs.prev = diffs.curr;
				socket.send(encode({purpose: "patches", message: diffs.patches_text, pad: academia.pad_id}));
			}
		},
		apply: function(patch_text) {
			if (patch_text.length > 2) {
				clearInterval(diffs.interval);
				var patches = dmp.patch_fromText(patch_text);
				console.log(patches);
				var s = window.getSelection();
				var r;
				var cursor_offset;
				console.debug(s.type);
				var move_cursor = s.type == "Caret";
				if (move_cursor) {
					r = s.getRangeAt(0);
					cursor_offset = r.startOffset;
					for (var i in patches) {
						console.log(patches[i].start2 + ", " + r.startOffset);
						if (patches[i].start1 < r.startOffset) {
							cursor_offset += (patches[i].length2 - patches[i].length1);
						}
					}
				}
				console.log("Offset: " + cursor_offset);
				console.log(patches);
				var result = dmp.patch_apply(patches, padContentElement.innerHTML.toString());
				padContentElement.innerHTML = result[0];
				diffs.prev = result[0];
				console.log (result[0]);
				if (move_cursor) {
					console.log("Carret...");
					var position = cursor_offset;
					
					var range = document.createRange();
					
					range.setStart(padContentElement.childNodes[0], position);
					range.setEnd(padContentElement.childNodes[0], position);
					range.collapse(false);
					
					var sel = window.getSelection(); 
					sel.removeAllRanges();
					sel.addRange(range);
				}
				
				diffs.interval = setInterval(diffs.schedule, 1000);
			}
			
		}
	};
	var socket = new WebSocket("ws://" + window.location.hostname + ":5002");
	socket.onmessage = function(response) {
		var patch_text = decode(response.data).message;
		diffs.apply(patch_text);
	};
	diffs.interval = setInterval(diffs.schedule, 1000);
