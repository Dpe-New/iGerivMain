/*
 * $Id: validation.js 692578 2008-09-05 23:30:16Z davenewton $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

function clearErrorMessages(form) {
	clearErrorMessagesXHTML(form);
}

function clearErrorMessagesXHTML(form) {

	var e = document.getElementsByTagName("span");
	for ( var i = 0; i < e.length; i++) {
		if (e[i].id.indexOf("err_") != -1) {
			document.getElementById(e[i].id).innerHTML = "";
		}
	}

}

function clearErrorLabels(form) {
	clearErrorLabelsXHTML(form);
}

function clearErrorLabelsXHTML(form) {

}

function addError(e, errorText) {
	addErrorXHTML(e, errorText);
}

function addErrorXHTML(e, errorText) {
	try {
		var spans = document.getElementsByTagName("span");
		for ( var i = 0; i < spans.length; i++) {
			if (spans[i].id.indexOf("err_" + e.id) != -1) {
				document.getElementById(spans[i].id).innerHTML = errorText;
				break;
			}
		}
	} catch (e) {
		alert(e);
	}
}
var cal_obj2 = null;
var formElement = null;
var format = '%d/%m/%Y';

var errorMessage;
var errorMessageDelete;
var okMessage;
var okMessageDelete;
var nessunaPubblicazione;
var chiudiLabel;
var submitButton;
var reportImage;
var reportImageHref;
var lastKeypressCode;
var lastFocusedFieldId;
var firstInvalidId = '';
var pingFailureCount = 0;
var lastFocusedRow = '';
var lastFocusColor = '';
var popupWiderThanViewport = false;

function closeLayerConfirm() {
	return true;
}

$.alt = function(key, callback, args, currKeyCode) {
    var isAlt = false;
    $(document).keydown(function(e) {
        if(!args) args=[];
        if(e.altKey) isAlt = true;
        currKeyCode = (typeof(currKeyCode) === 'undefined' || currKeyCode === '') ? key.charCodeAt(0) : currKeyCode;
        if(e.keyCode == currKeyCode && isAlt) {
        	if (e.preventDefault) {
                e.preventDefault();
        	} else {
                e.returnValue = false;
        	}
        	callback.apply(this, args);
            return false;
        }
    }).keyup(function(e) {
    	isAlt = false;
    });
};

$.altPlus = function(callback, args) {
	 var isAlt = false;
    $(document).keydown(function(e) {
        if (!args) args=[];
        if (e.altKey) isAlt = true;
        var code = null;
		if (!e) e = window.event;
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		var character = String.fromCharCode(code);
        if ((character == '+' || code == 171 || code == 187 || code == 107) && isAlt) {
        	if (e.preventDefault) {
                e.preventDefault();
        	} else {
                e.returnValue = false;
        	}
        	callback.apply(this, args);
            return false;
        }
    }).keyup(function(e) {
    	isAlt = false;
    });
};

$.altMinus = function(callback, args) {
	 var isAlt = false;
    $(document).keydown(function(e) {
        if (!args) args=[];
        if (e.altKey) isAlt = true;
        var code = null;
		if (!e) e = window.event;
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		var character = String.fromCharCode(code);
        if ((character == '-' || code == 173 || code == 189 || code == 109 ) && isAlt) {
        	if (e.preventDefault) {
                e.preventDefault();
        	} else {
                e.returnValue = false;
        	}
        	callback.apply(this, args);
            return false;
        }
    }).keyup(function(e) {
    	isAlt = false;
    });
};

$.ctrl = function(key, callback, args) {
    var isCtrl = false;
    $(document).keydown(function(e) {
        if (!args) args=[];
        if (e.ctrlKey) isCtrl = true;
        if (e.keyCode == key.charCodeAt(0) && isCtrl) {
        	if (e.preventDefault) {
                e.preventDefault();
        	} else {
                e.returnValue = false;
            }
        	callback.apply(this, args);
            return false;
        }
    }).keyup(function(e) {
        isCtrl = false;
    });
};

$.shift = function(key, callback, args, currKeyCode) {
    var isShift = false;
    $(document).keydown(function(e) {
        if (!args) args=[];
        if (e.shiftKey) isShift = true;
        currKeyCode = (typeof(currKeyCode) === 'undefined' || currKeyCode === '') ? key.charCodeAt(0) : currKeyCode;
        if (e.keyCode == currKeyCode && isShift) {
        	if (e.preventDefault) {
                e.preventDefault();
        	} else {
                e.returnValue = false;
        	}
        	callback.apply(this, args);
            return false;
        }
    }).keyup(function(e) {
    	isShift = false;
    });
};

$.ctrl('E', function() {
	var pdfImg = $("img[src*='pdf.gif']").first();
	if (pdfImg.length > 0)
		pdfImg.trigger("click");
});

$.ctrl('X', function() {
	var xlsImg = $("img[src*='xls.gif']").first();
	if (xlsImg.length > 0)
		xlsImg.trigger("click");
});

$.ctrl('M', function() {
	var forms = '';
	var saveBtn = '';
	if ($('#popup_name').is(':visible')) {
		forms = $('#popup_name').find("form");
	} else if ($('#popup_name_det').is(':visible')) {
		forms = $('#popup_name_det').find("form");
	}
	if (forms.length > 0) {
		forms.each(function() {
			saveBtn = $(this).find("input[value='" + memorizza + "']").first();	
			if (saveBtn.length > 0) {
				saveBtn.trigger("click");
				return false;
			}
		});
	} else {
		saveBtn = $("input[value='" + memorizza + "']").first();
	}
	saveBtn.trigger("click");
});

$.ctrl('I', function() {
	if ($("#memorizzaInvia").length > 0) {
		$("#memorizzaInvia").trigger("click");
	}
});

$(this).bind('keydown', function(event) {     
	lastKeypressCode = (event.keyCode ? event.keyCode : event.charCode);  
	var keycode = (event.keyCode ? event.keyCode : event.charCode);  
	switch(keycode) {
		//  esc: chiudi
    	case 27:	  
		if ($('#popup_name').is(':visible') || $('#popup_name_det').is(':visible') || $("#treeContiContainer").is(':visible')) {
			if (closeLayerConfirm()) {
				$("#close").trigger('click');
			}
		}
    	break;
	}	
}); 

/*
 * Disable submit buttons when clicked and enable them when action finished
 */
$("input[id*=memorizza],input[id*=cancella],input[id*=ricerca],input[id*=submit]").click(disableSubmitButton);

$("img[src*='pdf.gif'],img[src*='xls.gif']").click(function() {	
	blockUIForDownload($(this));
});

function setMenuListWidths() {
	if ($(window).width() <= 1024) {
		if (getBrowser().indexOf("MSIE") != -1) {
			$("#mydroplinemenu ul").css({'font-size':'10px'});
		} else {
			$("#mydroplinemenu ul").css({'font-size':'11px'});
		}
	} else if ($(window).width() > 1024 && $(window).width() <= 1280) {
		$("#mydroplinemenu ul li").css({'font-size':'12px'});
		$("#mydroplinemenu ul li a:not(ul li ul li a)").each(function() {
			$(this).css({'padding':'9px 0px 5px 20px'});
		});
	} else if ($(window).width() > 1280 && $(window).width() <= 1440) {
		$("#mydroplinemenu ul").css({'font-size':'13px'});
		$("#mydroplinemenu ul li a:not(ul li ul li a)").each(function() {
			$(this).css({'padding':'9px 0px 10px 20px'});
		});
	} else {
		$("#mydroplinemenu ul").css({'font-size':'13px'});
		$("#mydroplinemenu ul li a:not(ul li ul li a)").each(function() {
			$(this).css({'padding':'9px 0px 10px 25px'});
		});
	}
}

function playBarcodeBeep() {
	if (barcodeScannerBeepEnabled) {
		PlaySound('beep4');
	}
}

function PlaySound(soundObj) {
	
	if (allowBeep == "true") {
		/*Modifica effettuata il 27/06/2017 - mancato caricamento dei file wav */
		/* https://dopiaza.org/tools/datauri/index.php */
		if(soundObj == "beep"){
			var snd = new Audio("data:audio/wav;base64,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");
			snd.play();
		}else if(soundObj == "beep3"){
			var snd = new Audio("data:audio/wav;base64,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//8BAP//AAABAP//AQAAAP7/BAD8/wIAAAD//wEAAAAAAP//AQAAAP//AgD9/wMA/v8BAP//AAACAP3/AgAAAP7/AwD+/wAAAQD//wEAAAD//wEA//8BAAAA//8CAP3/AwD+/wEAAAD//wEA//8BAP//AQD//wEA/v8DAP7/AQD//wAAAQD//wEA//8AAAEA//8BAP//AAABAP//AQD//wEA//8BAP//AQAAAP//AQD//wEAAAD+/wMA/f8CAAAA/v8DAP7/AAABAP//AQD//wAAAQD//wEA/v8CAP//AQD//wEA/v8DAP3/AgAAAP7/AwD9/wIA//8BAP7/AwD8/wQA/v8AAAEA//8AAAEA//8BAAAA//8BAP//AQD//wEA//8BAP7/AgD+/wEAAAD//wEA//8BAAAA//8BAP//AQAAAAAA//8BAP//AQAAAP//AQD//wEAAAD//wEA//8AAAIA/f8DAP7/AAABAP//AgD+/wEA//8BAAAAAAD//wEAAAD//wIA/v8BAAAAAAD//wIA/v8BAAAAAAD//wIA/v8CAP7/AQABAP7/AgD+/wEAAAAAAP//AgD9/wIAAAD//wEA//8AAAEA//8BAP//AAABAP//AQD//wEA//8BAAAA//8BAAAA//8BAP//AQD//wEA//8AAAEA//8BAP7/BAD6/wgA+P8GAPz/AwD+/wIA/v8BAAAA//8CAP7/AQAAAP//AQD//wEA//8BAP//AAABAP7/AwD9/wIA//8AAAAAAAABAP7/AgD+/wIA//8AAAAAAAABAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD+/wEAAAAAAAEA/v8CAP7/AgD+/wIA/v8CAP//AAAAAAAA//8CAP3/AwD+/wEAAAD+/wIA//8AAAIA/P8FAPv/BQD8/wMA/v8BAP//AQD//wEA//8BAP7/AwD9/wEAAgD9/wMA/v///wQA/P8CAP//AAABAP//AQD+/wMA/f8CAP7/AgD//wAAAQD+/wMA/f8CAP//AQAAAP//AgD+/wEAAAAAAAAAAAD//wIA/v8BAAAA//8BAAAA//8BAAAA//8BAP//AAABAAAA//8AAAAAAQD//wEA//8AAAEAAAD//wEA//8AAAIA/f8DAP3/AgAAAP7/AwD+/wAAAgD9/wMA/////wIA/v8BAAEA/v8CAP//AAAAAAEA//8AAAEA/v8DAP3/AQABAP3/BQD7/wMA/////wIA//8AAAAAAAAAAAAAAQD+/wIA/////wMA/P8EAP7///8DAPv/BgD8/wIA/v8CAP7/AwD9/wEAAAAAAAEA//8AAP//AwD9/wIA/////wMA/f8CAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD/////BAD7/wMA//8AAAEA//8AAAAAAAABAP7/AgD//wAAAAAAAAAAAQD+/wIA/v8CAP//AAABAP7/AgD//wAAAQD+/wIA//8AAAEA//8AAAEA/v8CAP//AAABAP//AAABAP//AAABAP//AQD//wEA//8AAAIA/f8EAPv/BQD8/wMA/v8AAAIA/v8AAAIA/f8DAP7/AAABAAAA//8BAP//AAACAP3/AwD9/wIA//8BAP//AQD//wAAAAABAP//AQD//wAAAQD//wAAAAABAP7/AgD+/wEAAQD+/wIA/v8BAAEA/v8CAP////8CAP7/AgD+/wIA/f8DAP////8CAP3/AwD+/wEA//8CAP3/AwD+/wEAAQD9/wMA/v8BAAAA//8BAP//AgD9/wMA/f8CAAAA//8CAPz/BQD8/wMA/v8AAAEA//8CAP3/AwD9/wIAAAD//wEA//8CAP3/AwD9/wMA/v8CAP3/AwD+/wEA//8CAP3/AwD+/wEAAAAAAP//AgD+/wEAAQD9/wQA/f8BAAEA/f8EAP3/AgD//wAAAQD+/wMA/f8DAP7/AAAAAAEA//8BAAAA/v8CAP7/AgAAAP//AAABAP//AQAAAP7/BAD8/wMA/v8BAAAA//8CAP3/AwD+/wEA//8BAP//AgD9/wMA/v8BAAEA/f8DAP//AAABAP7/AgD+/wMA/P8EAPz/BAD8/wQA/P8DAP////8CAP7/AgD//wAAAAAAAAAAAQD//wAAAQD+/wIA//8BAP//AQD//wEA//8AAAEA//8CAP3/AgD//wEA//8AAAEAAAD//wEA/v8DAP7/AAABAP//AQD//wEA/v8DAP3/AwD9/wMA/v8AAAEA//8BAP//AAAAAAEA/v8CAP7/AQAAAAAAAQD+/wIA/v8CAP7/AwD8/wQA/P8EAPz/BAD7/wYA+v8GAPr/BAD+/wEAAAD//wAAAQD+/wMA/P8DAP////8DAPv/BQD9/wEAAAD//wEAAAD//wEA//8BAAAA//8BAAAA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AAAAAAEA/v8DAPz/BAD9/wIA/v8CAP7/AgD//wAAAQD//wAAAQD//wEAAAAAAP//AgD9/wMA/v8BAAAA//8BAP//AQAAAP//AQD//wEAAAD//wEA//8BAP//AAACAP3/AwD9/wIA//8AAAEA//8BAP7/AgD//wEA//8BAP7/AwD9/wIAAAD+/wMA/f8BAAEA/v8DAPz/BAD9/wIA//8BAP7/AwD9/wIA//8BAP//AQD+/wIA//8AAAEA/v8CAP//AAAAAAAAAAAAAAAAAAD//wIA/v8CAP3/AwD+/wEAAQD9/wMA/v8BAAAAAAAAAP//AgD9/wQA/f8AAAIA/f8EAP3/AQD//wEAAAAAAAAA//8BAAAA//8CAP3/BAD8/wMA/v8CAP////8BAAAAAAAAAP//AQD//wEAAAD//wEA//8AAAIA/f8DAPz/BAD+/wAAAQD//wEAAAD+/wMA/f8EAPv/BAD9/wIAAAD+/wIA//8BAP//AQD//wAAAwD7/wUA/P8DAP7/AgD9/wQA/P8DAP7/AQAAAP//AQAAAP//AgD9/wMA/v8CAP7/AQAAAP//AgD+/wAAAgD+/wEAAAD//wIA/v8BAAAAAAAAAAAA//8CAP////8DAPz/BAD9/wEAAAABAP7/AgD+/wEAAQD+/wIA/v8BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQD+/wIA/v8BAAAA//8CAP3/BAD7/wUA+/8FAP3/AQAAAP//AQABAP7/AgD+/wEAAAAAAAEA/v8CAP//AAABAP//AAACAP3/AwD9/wIAAAD//wEA/v8DAPz/BgD5/wcA+v8FAPz/AwD//wAAAAAAAP//AgD+/wEAAQD9/wMA/v8CAP7/AgD+/wIA//8AAAAAAAAAAAAAAQD9/wQA/P8DAP7/AgD9/wQA+/8FAPz/AwD9/wIA//8AAAIA/f8CAP//AAACAP3/AgAAAP7/BAD7/wQA/v8AAAEA//8BAP//AQD//wEAAAD+/wMA/v8AAAIA/P8FAPv/AwD//wAAAQD+/wIA/f8EAP3/AQAAAAAA//8DAPz/BAD9/wIA//8AAAEA/v8CAP//AAABAP7/AQABAP7/AgD+/wEAAQD+/wIA/v8CAP7/AgD//wAAAQD+/wIA//8AAAAAAAD//wIA/////wIA/f8DAP//AAD//wEA//8CAP7/AgD9/wQA/P8EAP3/AgD//wEA//8AAAAAAQD//wEA//8AAAEA//8BAAAA//8BAAAA//8BAAAA//8CAP7/AQD//wEAAAD//wIA/f8DAP7/AgD9/wQA/P8DAP////8DAP3/AgD+/wIA//8BAP//AQD+/wIA/v8CAAAA/v8CAP7/AgD//wAAAAAAAAEA/v8CAP7/AQAAAAAAAAD//wIA/v8BAAAA//8BAAEA/f8DAP7/AQAAAP//AQAAAAAAAAD//wEAAAAAAAAAAAD//wIA/v8CAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD+/wEAAQD9/wQA/P8DAP7/AQD//wEA//8BAP//AgD8/wQA/v8AAAIA/f8CAP//AQD//wEA//8AAAAAAgD8/wUA+/8EAP3/AgD+/wMA/f8CAP//AQD+/wMA/P8FAPz/AgD//wEA/v8EAPv/BQD8/wIA//8BAP//AgD9/wMA/f8DAP7/AQAAAP//AgD9/wMA/v8BAAEA/f8CAP//AAACAP7/AQD//wAAAQAAAP//AQD//wIA/f8DAP3/AwD/////AgD9/wMA/v8BAAEA/v8CAP3/BAD8/wQA/v///wMA/f8DAP7/AAABAP//AQD//wAAAAACAPz/BAD8/wMAAAD//wEA/v8BAAIA/f8EAPv/BAD+/wEAAAD//wIA/f8EAPz/BAD8/wMA/v8BAAAA//8BAAAA//8BAP//AQD//wIA/f8DAP3/AwD+/wEA//8AAAEA//8BAP//AQD+/wMA/f8DAP3/AwD9/wMA/f8DAP3/AwD9/wIA//8BAAAA//8AAAEA//8CAP7/AQD//wEA//8BAP//AQD//wEAAAD//wIA/v8BAAAAAAAAAAEA/f8DAP7/AgD+/wEA//8BAAAA//8BAP//AQD//wEA/v8DAP7/AAACAP3/AwD+/wEA//8CAP3/AwD+/wAAAQD//wEA//8BAP7/AwD9/wMA/f8DAP3/AwD9/wMA/v8BAAAAAAAAAAAA//8CAP7/AgD/////AQAAAP//AwD8/wMA/////wMA+/8GAPv/AwAAAP3/BAD8/wMA//8AAAAAAAD//wIA//8AAAAAAAAAAAEA/v8CAP7/AgD+/wEAAAAAAAAAAAD//wIA/v8CAP7/AQAAAAAAAAAAAAAA//8CAP3/AwD/////AQAAAP//AQAAAAAAAAAAAP//AgD+/wIA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");
			snd.play();
		}else if(soundObj == "beep4"){
			var snd = new Audio("data:audio/wav;base64,UklGRibqAABXQVZFZm10IBIAAAABAAIARKwAABCxAgAEABAAAABkYXRhAOoAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAP//AAAAAAAAAAAAAAAAAAABAAAAAQAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAEA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAP//AAAAAAAAAQAAAP//AAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAD//wAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAEAAAD//wAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAD//wEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAD//wAA//8AAAAAAAAAAAAAAQAAAAAAAAAAAAAAAQAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAEAAAAAAAAA//8AAAEAAAABAAAAAAAAAP//AAABAAAAAQAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////8AAAAAAAAAAAAAAAD/////AAAAAAAAAAAAAAAAAAD//wAA//8AAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////8BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAP//AAD//wAAAAAAAAAAAAABAAAAAQAAAAAAAAABAP//AAAAAAAAAQAAAAEAAAABAAAAAgAAAAAAAAABAAAAAAAAAAAAAAABAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAQAAAAAAAQAAAAAAAAAAAAAAAAAAAP//AAD//wAA//8AAP//AQAAAAAAAAAAAAAA//8AAP//AAD//wAA/////wAA//8AAAAAAAD//wAA//8AAP//AAD//wAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAP//AAD//wAAAAD+//////8BAP//AQAAAAAAAAD//wAA//8AAP7//////wAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAQAAAAAA//8AAAAAAAAAAP//AAAAAAAAAAD//wAAAAAAAAAAAAABAAAAAQAAAAAAAAAAAAEAAAAAAAAAAQAAAAEA//8AAAAA//8AAAAAAAAAAAAA//8AAAAAAAAAAAAA//8BAAAAAAAAAAAAAAAAAAAA//8AAP//AQAAAAAAAAD//wAA//8AAAAA//8AAAAAAAAAAAAA//8AAP//AAAAAAEAAAD//wAAAAD//wAAAAAAAAEA/////wAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAEAAQAAAAAA////////AAD//wEAAAAAAP//AQAAAP//AAAAAAAAAQAAAAAAAAD/////AAD//wAA//8AAAAA//8AAP//AAAAAAAAAAD//wAA//8AAAAA//8AAAAAAAABAAEAAAABAAAAAAAAAAAAAQAAAAAAAAAAAP//AAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAgAAAAIAAAAAAAAAAAABAP//AAD//wAA//8AAAAAAAD///////8AAP////8AAP///////wAAAAAAAP////8AAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAQD//wAA//8BAP////8AAP//AAD//wEA//8AAP//AAD//wAA///+/wAAAAABAP//AQAAAAAAAQAAAAAAAAD//wAAAAAAAAAAAAAAAP//AQD//////////////////////////////////wAAAAAAAAAA////////AAAAAAAAAAAAAAAAAAABAP//AAD//////////////////wAA//8AAP////8AAP//AAD+/wAA//8AAP//AAD/////AAD//wAA/////wAAAAAAAP//AQD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAP//AQAAAAAAAAD//wAAAAAAAP//AAAAAAAAAQD//wEA//8BAAAAAQAAAAAAAAD//wEA//8AAP7/AAAAAP//AAD//wAA//8AAAAAAAD+/wAAAAAAAAAAAAAAAAAAAAABAAAA////////AAAAAP////8AAP//AAAAAAAAAAAAAAAAAAABAAAAAQABAAAAAQABAAAAAQABAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAEAAQABAAEAAAAAAAAA/////wAA////////AAAAAP//AAD/////AAD//////v//////AQAAAAAAAAD//wAA/v8BAP//AAAAAAAAAAD//wAA//8AAP//AAD//wAAAAAAAAEAAQABAAAAAQAAAAEAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAQABAAEAAQABAAAAAQAAAAAAAAABAAAAAQABAAEAAQAAAAEAAAAAAAAA//8AAAAAAAABAAAAAAAAAAAA/////wEAAQACAAIAAQABAAAAAAAAAP//AAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAABAAAAAAAAAAAAAQAAAAAAAQABAAEAAQACAAEAAQABAAAAAAAAAAAAAQAAAAEAAQABAAEAAAABAAAAAQAAAAEAAQAAAAEAAAABAAAAAAAAAAEAAAAAAAAA//8AAAAAAAABAAAAAAAAAP//////////////////AAABAAAAAAAAAAEAAAABAAEA//8AAP//AAAAAAAAAQAAAAEAAAABAAAA//8AAAAA/////wAAAQABAAEAAQAAAAAAAAD/////////////////////AAAAAAAAAAAAAAAAAAABAAAAAAABAP//AAD//wAAAAAAAAAAAAD/////AAAAAAAAAAAAAP//AAD//wAAAQD//wEAAAABAAAAAQD//wAA/v8AAP7/AAAAAAAAAQD//wAAAAAAAAAAAAAAAAAAAAD//////v/9/////v8AAP//AQAAAP//AAD//wEAAAAAAAAAAAAAAP//AAD//wAA//8BAP//AAD//wAAAAAAAAEAAAABAAAA////////AAD//wAA///+////AAAAAAEAAAAAAAEAAgABAAEAAQD+/////v///////////wAAAAAAAP//AAAAAAAA//8AAP//AAAAAAEAAAAAAAAAAAD///////8AAP//AAABAAAAAQABAAAAAgAAAAAAAAAAAAEAAAAAAP///////wAAAAAAAP//AAAAAAAAAAD//wAAAAD//wAA//8AAP//AAAAAAEAAAAAAAAAAQAAAAAAAAD//wAA//8AAAAA//8AAP//AAAAAAEA//8AAP//AAD/////AAD+/wAAAAAAAAEA//8BAAAAAAAAAP//AAD+//////8AAAAAAQAAAAAAAQD//wAAAAD//wAA//8AAP//AAAAAAAA//8AAP//AAD//wEA//8BAAAAAAABAAAAAQAAAAEAAAAAAP//AAAAAAAAAAAAAAAA//8AAP//AAD//////////wAA//8AAP7/AAD//wAAAAAAAAAAAAD//wEA//8CAP//AAAAAP7/AQD//wEAAAABAAEAAQACAAAAAgAAAAAAAAAAAAAAAQAAAAAAAAD////////+//////8AAAAAAAABAAAAAAAAAAAAAAAAAP//AAD//wEAAAABAAAAAAAAAAAA//8BAP//AgD//wEAAAAAAAAA/////wAA//8BAAAA//8BAP//AAD/////AQD//wEAAAAAAAEAAAAAAAAAAQAAAAAA/////wAAAAAAAP//AAD+/////v///wAA//8AAAAAAQD//wAA//8AAP//AQAAAAEAAAABAAAAAAAAAAAAAQABAAEAAAAAAAEAAAAAAAAAAAD/////AAAAAAAA//8AAAAAAAD//wAA//8AAAAAAAAAAAAA//8AAP7///////7/AAD+/wAA//8AAAAA/////wAA/////wAAAAAAAAEAAAD//wAA///+/////v8BAP7/AQABAAEAAQABAAAA/////wAAAAAAAP//AAD//wAA/v8AAP//AAD+/////////wAAAAAAAP////8AAAAAAAABAAAAAAABAP7/AgD//wIAAQAAAAEA//8CAP//AQD//////////wAAAAABAAEAAQABAP//AQD//wEA/////wEA//8AAAAA//8AAP///////wAAAAABAP//AAD//wAAAAABAP//AQAAAAEAAAACAAAAAQABAP//AAABAP//AQAAAAAA//8AAP//AAAAAAAAAAD///////8AAAAAAAAAAAAAAAABAP//AAAAAP//AQD+/wAA//8BAP//AQABAP//AQD//wAA//8BAP//AQD//wAAAAAAAAAA//8AAAAA/////wAA//8BAP//AAD//wAA//8AAAAAAAABAAAAAgAAAAIA/v8AAP7///8AAP//AAD//wAA/v//////AAD//wAA//8AAAEA//8CAP7/AAD+/wAA//8BAAAAAAD//wEA//8BAAAAAQACAAIAAgABAAAAAQD//wIA/v8BAP//AAAAAAAA//8AAAAAAQABAAEAAAAAAP///////wAA/v///wAAAAACAAEAAQABAAEAAgABAAEAAAAAAAAAAAD//wEA//8BAAAAAAABAP7/AgD//wIA//8AAP////8AAP//AAD//wEAAAAAAAEA//8AAP7/AAAAAAEAAAAAAAAA/////wEA//8BAAAAAQABAAEAAQAAAAEAAAAAAAEA//8AAP7///8AAP//AAAAAP//AgD//wEA//8AAAAAAAAAAAEAAAABAAAAAQAAAAAAAAAAAAAAAAD///////8AAP//AAAAAP//AQD//wEA//////7////+/////////wAA//8AAP7//////wAA///+/wAA//8BAP////////7///////7//v8AAAAAAgACAAEAAgAAAAIA/v8BAP7/AQD+/wAAAAD//wEA//////////8AAAAAAgD//wIAAAACAP//AQD///////8AAP//AQD+/wAAAAAAAAEAAQACAAAAAQACAAAAAgAAAAAAAQAAAAEAAQAAAAIAAAADAAAAAgABAAEAAAD//wAAAAAAAAAAAAD//wAA/v8AAP7///8AAAAAAAAAAP//AAD//wAAAQAAAAEAAQAAAAEAAAABAP//AQAAAAEAAAABAP//AQD//wEAAQABAAEAAAAAAAAAAAABAAEAAQABAAEAAQABAAAAAAD//wEA//8BAAEAAQACAAEAAQACAAAAAAAAAP//AAAAAAEAAAABAAAAAAABAP//AQD//wEA//8BAAAAAQABAP//AAAAAAAAAQABAAIAAQABAAEAAAAAAAAAAAAAAAAAAAD//wEA//8BAAAAAAD//wAA//8AAP//AAD+/wAA/////wAA/v8AAP//AAD//////////wEA//8BAP//AQD/////AAAAAAAAAgD//wIAAAABAAAAAAAAAP//AQAAAAIAAAACAP//AQAAAAEA/////wAAAAACAAAAAQABAAAAAQD//wEA/v8BAP//AAD//wAAAAABAAAAAQD//wAAAAADAAEAAgACAAMAAgACAAEAAQAAAAIAAQAAAAAAAAD//wAA//8AAP//AQD//wEAAQD//wAA///+/wAA//8AAAAAAAAAAAAAAAAAAP//AAAAAAEA//8AAP////8AAP3/AAD+/wEA/v8BAAEAAQACAAEAAAABAP///////wAAAAACAAEAAwABAAIAAAAAAAAA//8AAAAAAAABAP//AQD//wAA//8BAAAAAAABAAAAAAABAP//AQAAAP//AAD+/wAA//8AAP//AAAAAAAA//8AAAAAAQAAAAEAAQAAAAEA//8AAP7////+/wEA//8BAAEAAAAAAAAAAAD//wEA//8BAAEAAAAAAAAAAAD//wEA/////wAA//8AAP//AAD//wEAAgACAAEAAQABAP//AQABAAEAAgAAAAIA/v8AAP7//////wEAAAACAAEAAAABAP7/AAAAAP//AgD+/wMA//8CAAAA/v////3/AAAAAAAAAAD//wAA//8BAP7/AQD+/wEAAAABAAAAAAAAAP//AAD///////8BAP//AgD//wEAAAABAP////////7/AAAAAAAAAAAAAP//AAAAAAIAAQABAAEA//8AAP3//v/9/wAA/v8BAAAAAQAAAAAAAAAAAAEAAgACAAEAAQD+//7//v/+/////v8AAP//AQD+/wEA/f8AAP//AAACAAAAAgAAAAAAAAD+/wAAAAABAAAAAAD//wEA//8AAAAA/v8AAP//AgAAAAAAAAD//////v///wEAAQACAAAAAgAAAAEAAAD//wAA/////wEA//8BAP//AAABAP3/AAD8///////+/wIA/v8CAP3/AQD+/wAA//////////8AAP7/AAD9/////v///wAA//8BAAAAAQABAAMAAQACAAEA//8AAP7/AAD//wAA///+/////f////7//f/+//7/////////AAAAAAAAAAAAAAEAAAABAAAAAQAAAAEAAAABAAAAAQAAAAEAAQAAAAEA//8AAP//AAAAAAAAAAAAAP/////+///////+/wAA/v8BAAAAAQD//wAA/////wAA/v8BAP//AQAAAAAAAAAAAP//AAD+/wEAAAAAAAIA/v8BAP//AAAAAP///v8BAP7/AgD+/wEA/v8AAP3/////////AQAAAAEAAQACAAEAAAAAAP7////+/wEA/v8DAP//AwD//wAAAAD//wAAAAAAAAIA//8BAAAA//8AAP7/AAD//wAAAAD/////AAAAAAEAAQABAAIAAAABAAEA/v8AAP3/AAD//wAAAAABAP//AQD+/wEAAQD+/wMA/v8CAP7/AAD/////AAD//wEA//8BAP3/AAD9//7////+/wEAAQACAAIAAQABAAEAAAACAAAAAgABAAIAAQAAAAAA////////AAD+/wEA//8AAAAA//////7//v////3////+//7/AAD+////AQD+/wIA//8AAAEA//8BAP//AAAAAP//AQAAAAEAAQACAAAAAgD+/wAA/v///wAAAAABAAEAAQABAAEA//8AAP////8BAP//AAAAAP7/AgD9/wEA/v///wAA/v8AAP7///////////8AAP//AQAAAP//AgD//wEA//8BAP//AwD8/wMA/f8CAAAA//8BAP7/AgABAAAAAQD//wAAAQD//wAAAQD+/wMA/f8CAP7/////////AAABAP//AgD//wAAAAD//wAAAAD+/wIA//8AAP////8BAP7/AwD//wMAAAACAAEAAgABAP//AQD+/wAA/f/+//7//v////7////+/wEA/f8BAP//AQABAAEAAQADAAEABQABAAUAAAADAAEAAQABAAAAAQD//wEA/v8BAP7/AAAAAP7/AQD//wEAAAAAAAAA//8BAAAAAAABAAIAAQACAP//AQD//wEA//8BAAEAAgABAAIAAQABAAEAAAABAAAAAAD//////v/+/wAA//8BAP//AAAAAP7////9/wAA/v8AAP//AAD+/wAA/P8DAPz/BQD+/wQAAQADAAIAAQACAAIAAQABAAEA//8CAP//AQAAAP7/AQD8/wEA/P////7//////wAAAAADAAAAAwABAAEAAQD+/wAA/v8AAAAAAQD//wMA/f8DAP3/AgD/////AQD9/wEA//8AAP///v////7//f////7/AQD+/wEA/v8AAP7//f////r/AQD7/////f/9/////f8AAP7/AQD+/wEA/f8AAP7/AAD//wAAAAD///7//f/9//z//f/9/////v8BAP//AAD+/wAA/v///////v////7/AAAAAAEAAgABAAIAAQAAAAAA///+/wAA/v8CAP7/AQD+/////v8BAP//AQAAAAEAAAAAAAEAAQABAAIAAQACAAEAAAAAAAAA//8CAP7/BAD//wIAAAD//wEAAAABAAEAAQAAAAEA/v8BAP3/AAD/////AgD//wIAAAACAAAAAgD+/wEA/f///////v8BAP7/AgD//wIAAgAAAAIAAAABAP//AQAAAAIAAAABAP//AAD+/wAA/v8BAP7/AQD//wAAAAD//wEAAAABAAIAAQADAAAAAwABAAMAAgAEAAMABAACAAQAAgADAAIAAwACAAMAAgADAAIAAgABAAMAAgADAAMAAQAEAAEAAgABAAIAAQABAAAAAgD//wIA//8DAAIAAgABAAEAAQABAAAAAAAAAP//AAD+////AAD+/wEA//8CAP//AgD+/wEA/v8BAAAAAQACAAIAAQACAP7/AQD+/wAA//8AAAAAAQAAAAEAAQAAAAAA/v///////////wAA/v8AAP7//v/9//3//f/7//z/+//6//r/+f/6//r/+f/5//v/+v/7//v/+v/6//j/9//4//T/+P/0//f/9f/0//b/8//0//X/8//3//T/9//3//T/+P/0//f/9v/1//f/9f/2//X/9v/4//b/+P/3//b/+f/1//r/9f/9//j//f/7//v//P/5//r/+//5////+////////P8BAPv///////3/BAD9/wQAAgABAAYAAQAFAAUABAAGAAMABgAFAAUACgAFAAwABQAKAAgACAAJAAgACwALAAsADwAIAA8ACAAMAAoACgAMAAsACwAOAAkADgAIAAoADAAIAA8ACgAOAAwACwANAAoACwAKAAoACgALAAgADQAGAAwABgALAAkACAALAAgADAAJAAoACwAKAAoACwAIAAsABgAKAAkACAANAAcADgAHAAoACAAGAAcACQAGAA0ABQAMAAMACQABAAQAAwACAAYAAwAEAAQA/v8DAPr/AAD8//z/AQD7/////f/4////9P/9//X/9//4//T/9//3//P/+//y//v/9P/1//X/7//z/+//8v/y//T/9P/2//P/9v/y//X/9P/3//n/+P/9//f//v/3//3/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AQAbAPj/CgD2//f/BADs/xsA7f8tAPv/MQAPACgAGgAUABcA+v8IAOL/8f/V/97/0//X/9n/4v/i//z/8f8XAAAAJAANAB4AEQALAA4A+f8NAPT/FgD9/yMADQAqABoAIwAaABAACwD4//T/5//h/+D/2f/k/+D/6v/x/+7/CQDw/xwA8/8jAPv/GAADAAMACADz/w8A8f8aAP7/IwANACMAFwAaABcACQAKAPn/9P/t/97/5P/U/97/2//c//D/4f8IAOn/FgDz/xcAAAALAA8A+v8bAO3/HQDw/xcAAgATABkAEwAnABQAIwANAA8AAAD0//H/4P/s/97/8f/w//z/DAAFACUABgAsAAEAHwD8/wkA/v/1/wgA7/8XAPn/HgAMABwAGgASABkABgAKAPv/9v/1/+T/8P/f/+3/5f/r//b/6v8JAO//FAD5/xEAAQABAAgA8f8LAO7/EgD3/xoABQAcABIAFwAXAAsAEgD//wIA9//u//b/4//5/+X////0/wMABgADABkAAAAjAAAAIQAFABMADgABABUA+P8bAP3/HAAJABsAFAAUABUACgAPAP//AwD1//b/7f/r/+n/6v/o//f/6/8JAPP/GQD7/xwAAQAUAAYABQALAPf/EAD0/xIA/v8TAAwAEgATAAwADQABAP//9f/y//D/6v/z/+n/9//v//b//P/1/wwA9/8XAP7/FQAGAAsACAABAAkA/v8OAAMAGQAKACQAEAAiABEAFQAJAAMA/v/0//P/7v/w//D/9P/3//z//P8HAP//EwABABkABAAUAAkABAAPAPX/EQD0/w8AAAALABIACQAdAAcAGQADAAoA+v/0//D/4v/q/97/6v/q//D//v/2/w4A/P8UAP//DQD//wAA/f/z//z/7f////P/CAABABUADgAcABEAFwAIAAgA+P/4/+z/7v/n/+z/7P/v//n/9/8KAAEAFgALABYADgAPAAwABAAJAAIADAAEABUACgAcAA4AHAARABIADwAGAAoA+v8DAPP/+//x//X/8f/y//T/9f/2//3/+f8JAPr/DgD4/wgA9//8//r/9P////X/CAD//w0ABgALAAgAAwABAPr/+P/0/+z/7//h/+z/3f/o/+H/5//t/+r//v/y/wwA/f8PAAMACQAEAP//AwD8/wYA//8PAAcAGAAPABgADwANAAgAAQD+//n/8//4/+z/+v/u//v/9//7/wQA/P8NAPz/EAD5/w0A+/8IAAQABAAQAAIAGgAFABsADAAXABIADwASAAUACAD5//v/7v/x/+j/6//o/+//7P/4//L/BAD4/wsA/v8JAAEAAAAAAPb/AgDz/wYA9/8NAP3/DwABAAcAAgD8////9P/4//D/7//u/+b/6v/l/+r/7P/t//n/9v8EAAAACAAFAAYAAgABAP3//P/7//v/AQD//woABwASAA0AEgAOAAsABgAAAPn/9v/s/+7/6P/p//D/6//9//D/CAD3/wsA/v8JAAQABQAHAP//CAD5/wcA+P8HAP7/CgAHAAsADAAHAAkA//8EAPr////+//3/CAD9/wwAAQAIAAsAAQAaAAAAJAAIACIAFAATABwAAwAcAPr/FgD8/w0ABQAGAAoABQAKAAgABAAJAP3/BQD3//z/9P/x//P/6f/3/+j/AQDv/w8A+/8YAAgAGAAPAA8AEQAHAA8ABgAOAAoADwASAA4AGQAOAB0AEAAcABIAFQARAAoACwABAAUA/P8DAP//BgAFAAoADgAMABAADAAMAAsABQAGAAAABAABAAQABgAHAAkACwAHAAwAAwAKAAEABgAAAAMA/P/8//b/9P/0/+7/9v/x//7/+P8EAAAABwAEAAcAAwAEAAAAAwD8/wQA/P8FAP//BQAFAAIACQD9/wUA+f/9//b/9P/z/+7/8P/q/+//6//y/+//9v/2//f//f/1/wEA8v////D/+f/v//P/8P/y//T/9//3////+f8EAPX/AwDu//r/7P/v/+7/6f/2/+3//f/3/wYA//8LAAYADgAKAAoADAAFAAsABAAGAAgAAgAOAAQAEwAKABIAEAANABMABgAPAAEABwD///7////5/wIA+f8FAAAACgAIAA4ADgAQAA8ADQAJAAgABQACAAQAAAAHAAUACAAMAAkADwAIAA4ABwAGAAMA+v/9//L/9//x//L/9v/w////9f8IAPz/CwAEAAYACAD8/wYA8/8CAPD//v/2//v//v/8/wQA/f8BAP//+//+//T/+v/v//b/7//x//H/7v/z/+z/+P/v////+P8DAAMAAQAGAPz/AgD1//n/9P/2//r/+v8DAAEACQAEAAcAAwACAAIA/f////n/+//4//X//P/y/wMA9f8JAP3/DAAFAAkACQADAAkA/v8HAPz/AQD8/////v8BAAEABAACAAcAAQAHAP//AgD9//3//f/5//7/9f////f/AgD+/wcACQAKABEACgASAAgADgAFAAkABAAGAAMABQADAAkAAwALAAMACgABAAYA/v/9//z/+P/8//j/AAD7/wUA/f8IAP//CQADAAgABgAGAAgAAgAHAPz/AwD7/wMA/v8EAAMABgAGAAUAAwADAP3////3//z/9//4//r/9v/9//T//v/1//7/9//+//z//f8BAPr/BAD4/wMA9/8AAPr////+////AgAAAAIAAAD8////9v/8//P/+P/z//T/9f/x//v/8/8AAPz/BQAGAAUADQABAA0A/P8JAP3/BQABAAEABwACAAkABAAIAAcABgAJAAQABwACAAIAAgD8/wEA+P8CAPb/AgD3/wAA+////wAAAAAEAAAACAD//wcA/v8FAAAABAADAAQABgAGAAUABwACAAcA//8EAP3//f/9//f//v/1/wAA9v8BAPr/AQAAAAIABQADAAkAAQAJAP3/BAD6////+//+//7/AQAAAAUA//8IAP3/BQD6////+v/5//v/9f/8//b//v/5/wMA/v8FAAMABAAFAP//BgD7/wUA+v8CAP3/AAAAAAEAAQADAAIABQD//wQA+v////j//P/6//j//f/1/wAA9f8BAPn///8AAP//BgADAAsABQALAAUACQACAAYAAQAFAAIABQAFAAUACAAFAAYAAwACAAAA///+/////v8BAPz/AwD9/wQA/v8DAAMABAAIAAQACQADAAUAAQAAAP7////9/wAA/v8EAAEABwACAAYAAgAEAP7/AAD8//7//P/+//////8BAP//AwD9/wMA/v8DAP//AgACAP//AQD9//7/+//9//r//f/8////AAABAAEAAgD+/wAA+v/9//j/+f/8//j////8/wIA//8BAP//AQD//wMAAAADAAIAAQACAP//AAD+//7////+/wEAAAD//wEA/v8CAP7/AwD9/wIA/P/+//3//P8AAP3/AgABAAIABgACAAgAAQAIAAEABQABAAEAAAD+/////v8AAAIAAQAFAAAABAD//wAAAAD8//7//P/+//z//v/+/wAA//8DAAEAAwADAAAABQD+/wQA/v8DAAAAAQABAAEAAAABAAEAAwABAAMA//8BAP3//P/9//n//f/4//7/+//+//7///8AAAAAAgABAAQAAAAFAPz/BAD7/wQA/P8EAP//BQABAAUAAgAEAAMAAgAEAP//AQD+//3//f/8//3//f/8/wAA/v8BAP//AQABAAAABAD//wYA/v8EAP7/BAD+/wIA/v8CAPz/AgD9/wIAAAACAAAA//8AAPr//v/3//7/+P8BAP3/AgAAAAAAAQD+/wIA//8CAAEAAwACAAMAAAACAP7/AgAAAAIAAQACAAIAAQACAP//AgD8/wUA+v8FAPj/AwD7/wEA/v///wIA//8CAAEAAQABAAEAAQABAAAA//////3//f/+//3/AQD+/wMA/v8DAAAAAQAEAAAABgD8/wQA+v8CAPj////7//////8BAAEAAgABAAEAAgD//wMA/f8EAPr/AQD3//7/+P/+//r/AQD+/wMAAgABAAMA//8DAP//AwD+/wIA/f////3/////////AAACAP//AwD+/wAA///9/wEA/P8AAP7/AAAAAAEAAQABAP//AgD9/wAA/P////7///8AAP//AgD+/wIA/P8CAP7/AQACAAEABQD//wQA/v8DAP//AgD//wUAAAAFAAAAAwAAAAAA//8AAP//AAABAAAAAgABAAIAAQACAAAAAwD+/wMA//8DAAIAAgACAAAAAQAAAAAAAAD+/wAA/f8AAP7/AAD/////AQD9/wIA/P8BAP3///////z/AAD8/wEA//8CAAIAAwACAAQAAgADAAEAAAD//////v////3/AAD8/wEA/f8AAP///v8BAP7/AgD//wEAAQAAAAEAAAABAAAA/v8AAP7/AAD/////AgD+/wIA/f8BAP3/AAD//wAAAAD//wEA/f8CAPv/AgD8/wIA/f8BAP////8AAAAAAAD//wEAAAABAAIAAgADAAIAAgACAAAAAQD+/////f/+/////P8AAP3/AAD+/wAA//8AAAAAAAADAAEAAgADAAAAAwD+/wMA//8CAAMAAgAEAAAABAAAAAMAAAACAAEAAQACAP//AQD9/wEA/f8AAPz//v/9//z//v/9/////////wEA//8CAP7/AwAAAAIAAgABAAIAAAABAAEAAAABAAEAAQABAAEAAgACAAAAAQAAAAAAAgD+/wMA/P8CAP3/AAAAAAAAAgD+/wIA/f8BAP3/AAD//wEAAgAAAAMAAAACAP//AgAAAAEAAAABAAEAAAABAAAAAgAAAAIA//8CAP//AQD////////+/////f////v////9/////f8AAP7/AAD//wAA/v/+//z////9/////////wAA//8AAP7/AAD//wAAAAAAAAAAAAACAP//BAD+/wMA//8AAAIA//8CAP//AQAAAP7/AgD9/wIA/f8BAAAAAAAAAAEAAAAAAP///v////7///8AAAAAAAAAAAAA//8AAP7/AAD/////AAD+/wEA/v8CAAAAAAACAAEAAwADAAMABQABAAYAAAAHAP//BgD+/wMA/v8BAAEAAAABAAEAAAABAAAA/v////v////7/wAA/P8AAP7/AQD9/wEA/v8BAAEAAQAEAAAABAAAAAMAAAABAP//AAD9/////f/+/wEA//8CAAEAAgABAAAA///+/////f/+//7/AQD//wMAAAACAAEAAgACAAAAAAD+//7//v/9/wEA/v8FAAAABAABAAMAAwAAAAMA//8CAP//AgD//wEA//8AAP7///////3//v////3/AgAAAAMAAwABAAMAAAABAP///////wAAAAADAAAABAAAAAIAAQAAAAIAAAABAAEA//8AAP7////8/////v/+/wAA/v8BAP//AQABAAEAAgAAAAEA/v8BAP//AAD+/wIA/f8BAP3/AQD9/wEA//8AAP///v8AAPz////8//7//v/9/wAA/f8AAP3////+/wAAAAACAAIAAwABAAEAAQD////////+/////v8BAP//AAABAAEAAQABAAAAAAD//wAA//8CAP//AgAAAAEAAAABAAEAAgACAAQAAgADAAEAAQAAAP/////+/////P8BAP3////9//7//f/+//3////8/wAA/v///wEAAAAEAAEABQADAAQABAACAAIAAQACAAIAAQABAAIA/v8BAP3/AgD8/wEA/P8BAP3/AAD+/wAA/v8AAP7///////7////+/wEA//8DAP7/AQD8/wEA+/8CAP3/BAAAAAMAAgABAAQAAAADAAAAAgAAAAMA/v8CAP3/AAD+/wAA/v//////AAABAP//AgD+/wMA/v8CAP7/AgD//wEAAAACAP//AwACAAMAAwABAAMAAAACAAAAAgD+/wAA/P////z////8/////f/+//7//f////7/AgD//wIAAAACAAAAAgAAAAEA//8CAAEAAQABAAEAAAAAAP7////9//7////9/wAA/P8AAPz/AQD+/wEA//8AAAEAAAACAAAAAwD//wMAAAACAAEABAAAAAMA/v8BAP7/AAD////////9/////v8AAP3/AQD9/wEA/v8DAP//BAD//wMA//8DAAAAAQADAP//BAD9/wMA/f8BAP3/AAD//wAA//8BAP//AAAAAAAA/////wEA//8CAP//BAAAAAMA//8BAP3/AAD+/wAA/v///wAA//8CAP//AgD//wIAAAABAP//AQAAAP//AQD+/wEA/v8BAAAAAQAAAAIA//8BAP//AQABAP//BAD//wIA//8BAAAAAAACAAAAAgAAAAAAAAD//wAA////////AAD//wEA//8AAAAAAQABAAEAAwABAAQAAAACAAAAAAAAAP///v////3////9/////P/+//z////9//7//v///////v8AAP7/AQD//wIAAQAEAAIABAACAAQAAQACAAEAAQABAAIAAAADAP//AgD+/wAA/////wAA/f8BAPz/AAD9//7//f/9//3//f////7/AgD//wIAAAACAAEAAQABAAEAAQACAAEAAwABAAQAAgACAAIAAQABAP3/AAD7//7/+//+//3//////wAAAAAAAP//AQAAAAIAAQACAAMAAgADAAEAAgABAAMAAQADAAIAAwAAAAIA/v////3//f/9//3//f/8/////P8AAPz////9//////8AAAIAAgAEAAMABAADAAMAAwAEAAIABAAAAAQAAgADAAEAAwAAAAIAAAD//////v/+//3/AAD9/wAA/v/+//7//v/9//7//f8AAP7/BAAAAAUAAwAFAAQAAwADAAAAAwAAAAAAAAAAAAAAAAAAAAAAAQAAAP////////7////+/////f8AAP3/AAD//wEA//8CAAAAAQAAAAEAAQABAAIAAgACAAQAAAADAP//AgD//wEAAAD//wAA//8AAP///v8AAP7//v/+//7//v/+//7//v//////AQABAAEAAwABAAIAAgD//wEAAAAAAAAAAAAAAP//AAD//wEAAAABAP/////+/////v/+/wEA//8CAAAAAgABAAEAAgABAAAAAAD///7////+/////////wEA//8AAAAAAQAAAAAA///+//7//v/+/////v8AAP//AgACAAEAAQABAAEAAQAAAAIAAAACAAAAAAABAP3/AQD9/wEA//8BAAEA//8BAP//AAD+/////f/+//3////+//////8BAAAAAwAAAAMAAQACAAMAAgADAAMAAgACAAIAAQAAAP7////8/////P////z////+/wAA//8AAAAA//8AAP//AgAAAAQAAAAEAAAABAAAAAQAAAADAAAAAgAAAAEAAQAAAAIAAAADAP//AgD//wAA/v////3////8/////P8AAAAA//8CAAAAAwD//wMA/v8BAP7/AQABAAAAAQABAAEAAQAAAAAAAQD//wAA//8AAAAA/////wAA//8AAP7/AQD+/wEA/////wAA/f8CAP7/AgD+/wIA//8DAP//AwAAAAMA//8CAP//AQAAAAEA///9//7//P/9//v//v/7/////P8BAP7/AQD//wEAAAABAAIAAAACAP//AQAAAAIA//8BAAEAAQAAAAEA//8AAP////8AAP7/AQD//wAA/////////v/9/////P////7/AAD//wAAAAD//wEA//8BAP//AQABAAAAAQABAAEAAAD//wEA/v8BAP7/AQD//wAAAQD+/wEA/v8BAP7/AAD+/wEA//8AAAAA//8AAP//AAAAAAEAAQACAAAAAgD//wMAAAACAAAAAQAAAAAAAQD//wEA//8AAP3/AAD9/////f////3////+/////v//////AAAAAAAAAgAAAAMAAQAEAAIABQACAAQAAgADAAEAAgAAAAAAAAD+/////v//////AAD//wAA/v8AAP7/AAD9/////f/9//3///8AAAAAAgD//wQAAAAFAAEAAwACAAEAAgAAAAEA////////AAD+/wAA/f8AAP7/AQD//wEA//8AAP//AQD//wAA//8BAAAAAAABAP//AQD//wMAAAABAP//AAAAAAAA//8AAP7////+//7//v/+//3//v/+//3////+/wAA/v8AAP/////+////AAD+/wEA//8BAAEAAwABAAMA//8DAP7/AQD+/wAA/v//////////////AAAAAAAAAQABAAEAAQAAAAEA//8BAAAAAQAAAAAAAQABAAAAAQABAAAAAQABAAEAAQACAAAAAAD//wEA//8AAAAA//8AAP7/AAD+/wAA/v8CAP7/AwD//wMAAAABAAAAAQAAAP//AAD+/wAAAQAAAAEA//8CAP//BAABAAQAAgABAAIAAAADAP7/AgD+/wAA//8AAAAA//8AAP//AQAAAAEAAAAAAP//AQD//wAA/v8AAP3/AQD9/wIA//8CAAEAAQACAP//AAAAAAEAAQACAAAAAgABAAIAAQACAAEAAAACAP//AQD9/wAA/P/+//z////9/wEAAAABAAAAAQD+/wEA/v8AAP//AAAAAAAAAAAAAAMAAAAEAP//AgD//wEA//8AAP////8AAAAAAAD//wAA//8AAP//AAD9/wEA/P8AAP3/AAD+/wAA//8BAAEAAAADAAAAAgAAAAIA//8BAAAAAQABAAEAAgABAAIAAAACAAAAAQD+/wEA/v8AAAAAAAABAAEAAAADAAAAAgAAAAIA//8AAAAA/v8AAP//AAD+/wEAAAACAAIAAAAEAAEAAwAAAAIAAAAAAP///v////7//////wEAAQABAAMAAAAEAP7/AgD//wIA//8AAAAA//////7///////7/AAAAAAEA//8CAP//AQAAAAAAAQD//wEA//8CAAAAAQABAP//AQAAAAEAAQACAAAAAQAAAAEA//8AAP7///////7/////////AAABAP//AgD//wIA//8EAP//AwD//wEAAAABAP//AQD//wAA/////////v////3////9/////f////7//v////7///////////8AAP//AAAAAAEAAAACAAAAAgAAAAEAAAABAAAAAQABAAEAAAAAAAAA//8AAP//AAD///7//v/////////+/wAA//8BAAAAAQABAAEAAgAAAAMAAAACAAAAAQD///////////////8AAAAA//8AAP////8AAP//AAD//////v8AAP//AAAAAP//AAABAAIAAQADAAEAAgAAAAMA//8CAAAAAgAAAAEAAAAAAP///v////7/AAD+/wAA/v8BAP//AAD+/wEA/f8AAP7/////////AQD+/wIA/v8DAAAAAwAAAAMA//8BAAAAAAAAAAAA/v8AAP//AQAAAAAAAQD+/wAA/v////7//v//////AgD//wIAAAACAAAAAQAAAAAAAQABAAAAAQD///////8AAAAAAAABAP//AAD////////9//3//v/+//7/////////AAABAP//AAAAAAAAAAAAAAAAAQD//wAAAAABAAAAAQAAAAAAAAAAAAAAAAAAAP//AAD////////+//3////8/////f8AAP//AQABAP//AgAAAAEAAAAAAAAAAAD//wEA/v8BAP7/AQD//wAA//8AAP//AAAAAP7/AQD//wAAAAAAAP////////////8AAP7/AQD//wEAAQABAAIAAQABAP//AQD9/wEA/v8BAP7/AgAAAAIAAAAAAP//AAD+//////////////8AAAAAAgAAAAMA//8BAP//AQAAAAEAAgABAAMA//8CAP//AgAAAAIAAQABAAAAAgAAAAIAAAABAAAAAAAAAP/////+/wAA/f8AAP7/AAD9/wAAAAAAAAAAAAABAAEAAAABAAIAAAACAAAAAwD+/wMA/v8BAP//AAD+/wAA/f//////////////AAD//wAAAAAAAAAAAAAAAAAAAAAAAAEAAQABAAEAAQABAAAAAQABAAEAAQAAAAAA////////////////AQAAAAEAAAD//wEAAQAAAAAAAAD//wAAAAD//wAA//8BAP//AAABAP//AgAAAAIA//8CAP//AAAAAAAAAAD//wAAAAACAAAAAAAAAP/////9/////////wAAAAACAAEAAQABAAEAAQAAAAEA/v///////////////////wAA/////wEA//8BAAAAAQAAAAEAAQAAAAEA//8BAP//AAABAAEAAQAAAAEAAQABAAEA//8BAP//AQAAAAAA///+/wAA/f////3/////////AQD//wAAAAAAAAAAAQABAAEAAgABAAIAAQABAAEAAgABAAEAAAD///////////7//v/9/////v8AAP7/AAD+//////8AAAEAAAABAAAAAgABAAIAAQABAAEAAQACAAIAAQABAAEAAgACAAIAAAAAAAAA//8AAP//AAAAAAEAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAQD//wAAAAD//wAA//8BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAP//AAAAAAAAAAD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAA//8BAAAAAAAAAAAAAQAAAAAAAAD//wAA/////wAAAAD//wAA//8AAAAAAAABAAAAAAAAAAAA//8AAAAAAAAAAAAAAQABAAAAAAD//wAAAAAAAAAAAAAAAAAA//8AAP//AAAAAAAAAAAAAAEAAAABAAEAAAAAAP//AAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAD/////AAAAAP///////wAA//8BAAEAAAABAAAAAAABAAAAAAAAAAAAAAAAAAAA//8AAAAAAAABAAAAAAAAAAAAAAAAAAAAAAD/////AAAAAAAAAAABAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQABAP//AAAAAAAAAQD//wAAAAAAAP//AAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAA//8AAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAA/////wAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAEAAQABAAAAAAAAAP//AAAAAP////8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAA//8AAAAAAAABAAEAAAAAAAAA//8AAAAA//8AAAAAAAD//wAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAP////8AAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////8AAAEAAAABAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAQAAAAAAAAAAAAAAAAD//wAAAAAAAAEAAAAAAAAAAAD//wAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAP//AAD//wAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAEAAAAAAP//AAAAAAAAAAAAAP//AAAAAP//AQAAAAAAAAAAAAAAAAAAAAAAAQAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAABAAEAAAAAAAAA//8AAP//AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAP//AAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAQAAAAEAAAD//wAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAP//AAAAAP//AAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAA//8BAAAAAQAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAP//AAAAAAAAAAAAAAAAAAAAAP//AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAA//8AAAAAAAD//wAAAAAAAAEAAAAAAAAAAAABAAAAAAAAAP//AAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAP//AAABAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAP//AAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAAAAD//wEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAD//wAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//wAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAA==");
			snd.play();
		}else{
			var snd = new Audio("data:audio/wav;base64,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//8BAP//AAABAP//AQAAAP7/BAD8/wIAAAD//wEAAAAAAP//AQAAAP//AgD9/wMA/v8BAP//AAACAP3/AgAAAP7/AwD+/wAAAQD//wEAAAD//wEA//8BAAAA//8CAP3/AwD+/wEAAAD//wEA//8BAP//AQD//wEA/v8DAP7/AQD//wAAAQD//wEA//8AAAEA//8BAP//AAABAP//AQD//wEA//8BAP//AQAAAP//AQD//wEAAAD+/wMA/f8CAAAA/v8DAP7/AAABAP//AQD//wAAAQD//wEA/v8CAP//AQD//wEA/v8DAP3/AgAAAP7/AwD9/wIA//8BAP7/AwD8/wQA/v8AAAEA//8AAAEA//8BAAAA//8BAP//AQD//wEA//8BAP7/AgD+/wEAAAD//wEA//8BAAAA//8BAP//AQAAAAAA//8BAP//AQAAAP//AQD//wEAAAD//wEA//8AAAIA/f8DAP7/AAABAP//AgD+/wEA//8BAAAAAAD//wEAAAD//wIA/v8BAAAAAAD//wIA/v8BAAAAAAD//wIA/v8CAP7/AQABAP7/AgD+/wEAAAAAAP//AgD9/wIAAAD//wEA//8AAAEA//8BAP//AAABAP//AQD//wEA//8BAAAA//8BAAAA//8BAP//AQD//wEA//8AAAEA//8BAP7/BAD6/wgA+P8GAPz/AwD+/wIA/v8BAAAA//8CAP7/AQAAAP//AQD//wEA//8BAP//AAABAP7/AwD9/wIA//8AAAAAAAABAP7/AgD+/wIA//8AAAAAAAABAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD+/wEAAAAAAAEA/v8CAP7/AgD+/wIA/v8CAP//AAAAAAAA//8CAP3/AwD+/wEAAAD+/wIA//8AAAIA/P8FAPv/BQD8/wMA/v8BAP//AQD//wEA//8BAP7/AwD9/wEAAgD9/wMA/v///wQA/P8CAP//AAABAP//AQD+/wMA/f8CAP7/AgD//wAAAQD+/wMA/f8CAP//AQAAAP//AgD+/wEAAAAAAAAAAAD//wIA/v8BAAAA//8BAAAA//8BAAAA//8BAP//AAABAAAA//8AAAAAAQD//wEA//8AAAEAAAD//wEA//8AAAIA/f8DAP3/AgAAAP7/AwD+/wAAAgD9/wMA/////wIA/v8BAAEA/v8CAP//AAAAAAEA//8AAAEA/v8DAP3/AQABAP3/BQD7/wMA/////wIA//8AAAAAAAAAAAAAAQD+/wIA/////wMA/P8EAP7///8DAPv/BgD8/wIA/v8CAP7/AwD9/wEAAAAAAAEA//8AAP//AwD9/wIA/////wMA/f8CAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD/////BAD7/wMA//8AAAEA//8AAAAAAAABAP7/AgD//wAAAAAAAAAAAQD+/wIA/v8CAP//AAABAP7/AgD//wAAAQD+/wIA//8AAAEA//8AAAEA/v8CAP//AAABAP//AAABAP//AAABAP//AQD//wEA//8AAAIA/f8EAPv/BQD8/wMA/v8AAAIA/v8AAAIA/f8DAP7/AAABAAAA//8BAP//AAACAP3/AwD9/wIA//8BAP//AQD//wAAAAABAP//AQD//wAAAQD//wAAAAABAP7/AgD+/wEAAQD+/wIA/v8BAAEA/v8CAP////8CAP7/AgD+/wIA/f8DAP////8CAP3/AwD+/wEA//8CAP3/AwD+/wEAAQD9/wMA/v8BAAAA//8BAP//AgD9/wMA/f8CAAAA//8CAPz/BQD8/wMA/v8AAAEA//8CAP3/AwD9/wIAAAD//wEA//8CAP3/AwD9/wMA/v8CAP3/AwD+/wEA//8CAP3/AwD+/wEAAAAAAP//AgD+/wEAAQD9/wQA/f8BAAEA/f8EAP3/AgD//wAAAQD+/wMA/f8DAP7/AAAAAAEA//8BAAAA/v8CAP7/AgAAAP//AAABAP//AQAAAP7/BAD8/wMA/v8BAAAA//8CAP3/AwD+/wEA//8BAP//AgD9/wMA/v8BAAEA/f8DAP//AAABAP7/AgD+/wMA/P8EAPz/BAD8/wQA/P8DAP////8CAP7/AgD//wAAAAAAAAAAAQD//wAAAQD+/wIA//8BAP//AQD//wEA//8AAAEA//8CAP3/AgD//wEA//8AAAEAAAD//wEA/v8DAP7/AAABAP//AQD//wEA/v8DAP3/AwD9/wMA/v8AAAEA//8BAP//AAAAAAEA/v8CAP7/AQAAAAAAAQD+/wIA/v8CAP7/AwD8/wQA/P8EAPz/BAD7/wYA+v8GAPr/BAD+/wEAAAD//wAAAQD+/wMA/P8DAP////8DAPv/BQD9/wEAAAD//wEAAAD//wEA//8BAAAA//8BAAAA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AAAAAAEA/v8DAPz/BAD9/wIA/v8CAP7/AgD//wAAAQD//wAAAQD//wEAAAAAAP//AgD9/wMA/v8BAAAA//8BAP//AQAAAP//AQD//wEAAAD//wEA//8BAP//AAACAP3/AwD9/wIA//8AAAEA//8BAP7/AgD//wEA//8BAP7/AwD9/wIAAAD+/wMA/f8BAAEA/v8DAPz/BAD9/wIA//8BAP7/AwD9/wIA//8BAP//AQD+/wIA//8AAAEA/v8CAP//AAAAAAAAAAAAAAAAAAD//wIA/v8CAP3/AwD+/wEAAQD9/wMA/v8BAAAAAAAAAP//AgD9/wQA/f8AAAIA/f8EAP3/AQD//wEAAAAAAAAA//8BAAAA//8CAP3/BAD8/wMA/v8CAP////8BAAAAAAAAAP//AQD//wEAAAD//wEA//8AAAIA/f8DAPz/BAD+/wAAAQD//wEAAAD+/wMA/f8EAPv/BAD9/wIAAAD+/wIA//8BAP//AQD//wAAAwD7/wUA/P8DAP7/AgD9/wQA/P8DAP7/AQAAAP//AQAAAP//AgD9/wMA/v8CAP7/AQAAAP//AgD+/wAAAgD+/wEAAAD//wIA/v8BAAAAAAAAAAAA//8CAP////8DAPz/BAD9/wEAAAABAP7/AgD+/wEAAQD+/wIA/v8BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQD+/wIA/v8BAAAA//8CAP3/BAD7/wUA+/8FAP3/AQAAAP//AQABAP7/AgD+/wEAAAAAAAEA/v8CAP//AAABAP//AAACAP3/AwD9/wIAAAD//wEA/v8DAPz/BgD5/wcA+v8FAPz/AwD//wAAAAAAAP//AgD+/wEAAQD9/wMA/v8CAP7/AgD+/wIA//8AAAAAAAAAAAAAAQD9/wQA/P8DAP7/AgD9/wQA+/8FAPz/AwD9/wIA//8AAAIA/f8CAP//AAACAP3/AgAAAP7/BAD7/wQA/v8AAAEA//8BAP//AQD//wEAAAD+/wMA/v8AAAIA/P8FAPv/AwD//wAAAQD+/wIA/f8EAP3/AQAAAAAA//8DAPz/BAD9/wIA//8AAAEA/v8CAP//AAABAP7/AQABAP7/AgD+/wEAAQD+/wIA/v8CAP7/AgD//wAAAQD+/wIA//8AAAAAAAD//wIA/////wIA/f8DAP//AAD//wEA//8CAP7/AgD9/wQA/P8EAP3/AgD//wEA//8AAAAAAQD//wEA//8AAAEA//8BAAAA//8BAAAA//8BAAAA//8CAP7/AQD//wEAAAD//wIA/f8DAP7/AgD9/wQA/P8DAP////8DAP3/AgD+/wIA//8BAP//AQD+/wIA/v8CAAAA/v8CAP7/AgD//wAAAAAAAAEA/v8CAP7/AQAAAAAAAAD//wIA/v8BAAAA//8BAAEA/f8DAP7/AQAAAP//AQAAAAAAAAD//wEAAAAAAAAAAAD//wIA/v8CAP7/AgD+/wIA/v8CAP7/AQABAP7/AgD+/wEAAQD9/wQA/P8DAP7/AQD//wEA//8BAP//AgD8/wQA/v8AAAIA/f8CAP//AQD//wEA//8AAAAAAgD8/wUA+/8EAP3/AgD+/wMA/f8CAP//AQD+/wMA/P8FAPz/AgD//wEA/v8EAPv/BQD8/wIA//8BAP//AgD9/wMA/f8DAP7/AQAAAP//AgD9/wMA/v8BAAEA/f8CAP//AAACAP7/AQD//wAAAQAAAP//AQD//wIA/f8DAP3/AwD/////AgD9/wMA/v8BAAEA/v8CAP3/BAD8/wQA/v///wMA/f8DAP7/AAABAP//AQD//wAAAAACAPz/BAD8/wMAAAD//wEA/v8BAAIA/f8EAPv/BAD+/wEAAAD//wIA/f8EAPz/BAD8/wMA/v8BAAAA//8BAAAA//8BAP//AQD//wIA/f8DAP3/AwD+/wEA//8AAAEA//8BAP//AQD+/wMA/f8DAP3/AwD9/wMA/f8DAP3/AwD9/wIA//8BAAAA//8AAAEA//8CAP7/AQD//wEA//8BAP//AQD//wEAAAD//wIA/v8BAAAAAAAAAAEA/f8DAP7/AgD+/wEA//8BAAAA//8BAP//AQD//wEA/v8DAP7/AAACAP3/AwD+/wEA//8CAP3/AwD+/wAAAQD//wEA//8BAP7/AwD9/wMA/f8DAP3/AwD9/wMA/v8BAAAAAAAAAAAA//8CAP7/AgD/////AQAAAP//AwD8/wMA/////wMA+/8GAPv/AwAAAP3/BAD8/wMA//8AAAAAAAD//wIA//8AAAAAAAAAAAEA/v8CAP7/AgD+/wEAAAAAAAAAAAD//wIA/v8CAP7/AQAAAAAAAAAAAAAA//8CAP3/AwD/////AQAAAP//AQAAAAAAAAAAAP//AgD+/wIA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");
			snd.play();
			
		}
	//		try {
	//		    var sound = document.getElementById(soundObj);
	//		    if (sound) {
	//		    	try {
	//		    		sound.Play();
	//		    	} catch(e) {
		
	//		    		sound.play();
	//		    	}
	//		    }
	//		} catch(e) {}
	}
 }

function disableSubmitButton(evt) {
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	if (node.type=="submitButton" || node.type=="button") {
		submitButton = $("#" + node.id);
		submitButton.attr("disabled",true);
		ray.ajax();
	}	
}

function clearPubblicazioniFilterFields() {
	$("#titolo").val("");
	$("#sottotitolo").val("");
	$("#argomento").val("");
	$("#periodicita").val("");
	$("#prezzo").val("");
	$("#codPubblicazione").val("");
	$("#codBarre").val("");
}

function clearPubblicazioniInsertBarcodeFilterFields() {
	$("#titoloInsertBarcode").val("");
	$("#sottotitoloInsertBarcode").val("");
	$("#prezzoInsertBarcode").val("");
	$("#codPubblicazioneInsertBarcode").val("");
	$("#codBarreInsertBarcode").val("");
}


/*
 * Detect the file download dialog in the browser. Block the UI when report is
 * processing until download finish.
 */
var fileDownloadCheckTimer;

function setLastFocusedElement(context) {
	if (typeof(context) === 'undefined' || context == '') {
		context = window.document;
	} 
	$("input[type='text'], input[type='file'], select, button", context).focus(function () {
		var type = $(this).attr("type");
		if (typeof(type) !== 'undefined' && (type == 'text' || type == 'file' || type == 'select-one')) {
			lastFocusedFieldId = this.id;
		}
	});
}

function blockUIForDownload(img) {	
	if (areCookiesEnabled()) {
	    var token = new Date().getTime();	    	    
	    if ( $('#popup_name_det').is(':visible') ) {
	    	if ($('#downloadToken2').length > 0) {
	    		$('#downloadToken2').val(token);
	    	} else {
	    		$('input[name="downloadToken2"]').val(token);
	    	}
	    	if (($('#downloadToken2').length > 0 && $('#downloadToken2').val().length > 0)
		    		|| ($('input[name="downloadToken2"]').length > 0 && $('input[name="downloadToken2"]').val().length > 0)) {
	    		ray1.ajax();
	    	}
	    } else if ( $('#popup_name').is(':visible') ) {
	    	if ($('#downloadToken1').length > 0) {
	    		$('#downloadToken1').val(token);
	    	} else {
	    		$('input[name="downloadToken1"]').val(token);
	    	}
	    	if (($('#downloadToken1').length > 0 && $('#downloadToken1').val().length > 0)
		    		|| ($('input[name="downloadToken1"]').length > 0 && $('input[name="downloadToken1"]').val().length > 0)) {
	    		ray1.ajax();
	    	}
	    } else {	    	
	    	if ($('#downloadToken').length > 0) {
	    		$('#downloadToken').val(token);
	    	} else {
	    		$('input[name="downloadToken"]').val(token);
	    	}
		    if (($('#downloadToken').length > 0 && $('#downloadToken').val().length > 0)
		    		|| ($('input[name="downloadToken"]').length > 0 && $('input[name="downloadToken"]').val().length > 0)) {
		    	ray.ajax();
	    	}
		}
	   
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = getCookie("fileDownloadToken");
			if (cookieValue == token) {
				finishDownload();
				if (typeof(finishDownloadCallback) === 'function') {
					finishDownloadCallback();
				}
			}
		}, 1000);
		
		/*
		 * window.setTimeout(function() { var cookieValue =
		 * getCookie("fileDownloadToken"); if (cookieValue != null) {
		 * alert(alertTimeout); finishDownload(); } }, 10000);
		 */

	}
}

function finishDownload() {	
	window.clearInterval(fileDownloadCheckTimer);
	setCookie("fileDownloadToken", null);
	unBlockUI();
}

function unBlockUI() {	
	if (document.getElementById('load')) {
		document.getElementById('load').style.visibility = "hidden";
	}
	if (document.getElementById('load1')) {		
		document.getElementById('load1').style.visibility = "hidden";
	}
	if (document.getElementById('load2')) {		
		document.getElementById('load2').style.visibility = "hidden";
	}
	if (submitButton) {
		submitButton.attr("disabled",false);		
	}
}


/*
 * Functions for ajax form submission
 */
function setFormAction(formId, action, metod, msgboxid, isSynchronous, context, callback, validationCallback, showOkMessage, errCallback, isAutomaticCall) {
	if (typeof(validationCallback) === 'function') {
		var valid = validationCallback();
		if (!valid) {
			return false;
		}
	}
	if (typeof(showOkMessage) === 'undefined' || showOkMessage.length == 0) {
		showOkMessage = true;
	}
	var $form = $("#" + formId);
	var synch = false;
	if (typeof context != 'undefined' && context != '') {
		$form = $("#" + formId, context);
	}
	if (typeof isSynchronous != "undefined" && isSynchronous != '') {
		synch = isSynchronous;
	}
	var $ecEti = $form.find("input[name='ec_eti']");
	if (typeof action != 'undefined' && action != '') {
		$form.attr("action", action);
	}
	if (typeof metod != 'undefined' && metod != '') {
		$form.attr("method", metod);
	}
	if ($ecEti.length > 0) {
		$ecEti.val('');
	}
	formSubmit(formId, msgboxid, action, synch, context, callback, showOkMessage, errCallback, isAutomaticCall);
}

function formSubmitMultipartAjax(formId, handleAsType, successCallback, validationCallback, showOkMessage, errCheckCallback) {
	if (typeof(validationCallback) === 'function') {
		var valid = validationCallback(false);
		if (!valid) {
			return false;
		}
	}
	if (typeof(handleAsType) == 'undefined' || handleAsType == '') {
		handleAsType = "html";
	}
	dojo.require("dojo.io.iframe"); 
	dojo.io.iframe.send({
		form : formId, 
		handleAs : handleAsType, 
		load : function(data, args) {
				unBlockUI();
				var bodyContent = data.body.innerHTML;
				var $doc = $(bodyContent);
				var errExists = $('igerivException', $doc).length > 0;
				var errMsgExists = ($('li.errormessage', $doc).length > 0) || ($('li.errorMessage', $doc).length > 0);
				if (errExists || errMsgExists) {
					var errMsg = errorMessage;				
					if (errExists) {
						errMsg = $('igerivException', $doc).html();
					} else if (errMsgExists) {
						errMsg = bodyContent;
					}
					$.alerts.dialogClass = "style_1";
    				jAlert(errMsg, attenzioneMsg.toUpperCase(), function() {
    					$.alerts.dialogClass = null;
    				});
				} else {	
					if (typeof(errCheckCallback) === 'function') {
						if (!errCheckCallback($doc)) {
							return false;
						}
					}
					if (showOkMessage) {
						$.alerts.dialogClass = "style_1";
	    				jAlert(okMessage, msgAvviso.toUpperCase(), function() {
	    					$.alerts.dialogClass = null;
	    				});
					}
					if (typeof(successCallback) === 'function') {
						successCallback(bodyContent);
					}
				}
				return data;
		}
	});
}

function formSubmit(formid, msgboxid, action, synch, context, callback, showOkMessage, errCallback, isAutomaticCall) {
	var hr = { "Content-Type": "application/x-www-form-urlencoded; charset=utf-8" };
	if (typeof(isAutomaticCall) !== 'undefined' && isAutomaticCall) {
		hr = { "Content-Type": "application/x-www-form-urlencoded; charset=utf-8", "Auto-Call" : "true" };
	}
	dojo.xhrPost({
		form: formid,
		handleAs: "text",	
		headers: hr,
		sync: synch,
		handle: function(data,args) {
			unBlockUI();
			var container = null;
			if (typeof(msgboxid) !== 'undefined' && msgboxid != '' && msgboxid && msgboxid.length > 0) {
				container = (typeof context != 'undefined') ? $("#" + msgboxid, context) : $("#" + msgboxid);
			}
			var $doc = $(data);
			var errExists = $('igerivException', $doc).length > 0;
			if (typeof data == "error" || errExists || args.xhr.status != 200) {
				var errMsg = ((action.indexOf('delete') != -1) ? errorMessageDelete : errorMessage);				
				if (errExists) {
					errMsg = $('igerivException', $doc).html();
				}
				if (getBrowser().indexOf("FIREFOX") != -1 && container && container.length > 0) {					
					container.html("<div style='color:red; width: 180px; border-style: solid; border: 1px solid red; font-size: 12px'>&nbsp;&nbsp;" + errMsg + "&nbsp;&nbsp;</div>");
				} else {
					var newdiv = document.createElement("div");
					newdiv.style.color = "red";
					newdiv.style.width = "180px";
					newdiv.style.borderStyle = "solid";
					newdiv.style.border = "1px solid red";
					newdiv.style.fontSize = "12px";
					newdiv.innerHTML = "&nbsp;&nbsp;" + ((action.indexOf('delete') != -1) ? errorMessageDelete : errorMessage) + "&nbsp;&nbsp;";					
					if (container && container.length > 0) {
						if (container.find("div")[0]) {														
							container.empty();						
						} 
						container.append(newdiv);				
					}
				}
				$.alerts.dialogClass = "style_1";
				jAlert(errMsg, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					if (typeof(errCallback) === 'function') {
						errCallback();
					}
				});
			}
			else {	
				var msg = ((action.indexOf('delete') != -1) ? okMessageDelete : (action.indexOf("send") != -1) ? bollaInTrasmissione : okMessage);
				if (getBrowser().indexOf("FIREFOX") != -1 && container && container.length > 0) {					
					container.html("<div style='color:green; width: 180px; border-style: solid; border: 1px solid green;'>&nbsp;&nbsp;" + msg + "&nbsp;&nbsp;</div>");
				} else {								
					var newdiv = document.createElement("div");
					newdiv.style.color = "green";
					newdiv.style.width = "180px";
					newdiv.style.borderStyle = "solid";
					newdiv.style.border = "1px solid green";
					newdiv.innerHTML = "&nbsp;&nbsp;" + msg + "&nbsp;&nbsp;";
					if (container && container.length > 0) {
						if (container.find("div")[0]) {														
							container.empty();						
						} 
						container.append(newdiv);		
					}
				}	
				if (showOkMessage) {
					$.alerts.dialogClass = "style_1";
					jAlert(msg, msgAvviso.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						return false;
					});
				}
				afterSuccessSave();
				if (typeof(callback) === 'function') {
					callback();
				}
			}			
			onLoadFunction();
		}
	});	
	
	function strip(html) {
	   var tmp = document.createElement("DIV");
	   tmp.innerHTML = html;
	   return tmp.textContent||tmp.innerText;
	}

};

/*
 * Uitlity functions
 */ 
 
function refreshimage(){
	document.getElementById('imgCaptcha').src = document.getElementById('imgCaptcha').src + '#';
}

// show calendar
function show_cal(el) {
	if (cal_obj2)
		return;
	formElement = el;
	cal_obj2 = new RichCalendar();
	cal_obj2.start_week_day = 1;
	cal_obj2.show_time = false;
	cal_obj2.language = 'it';
	cal_obj2.user_onchange_handler = cal2_on_change;
	cal_obj2.user_onclose_handler = cal2_on_close;
	cal_obj2.user_onautoclose_handler = cal2_on_autoclose;
	cal_obj2.parse_date(el.value, format);
	cal_obj2.show_at_element(el, "adj_right-top");
	cal_obj2.change_skin('alt');

}

// user defined onchange handler
function cal2_on_change(cal, object_code) {
	if (object_code == 'day') {
		formElement.value = cal.get_formatted_date(format);
		cal.hide();
		cal_obj2 = null;
	}
}

function cal2_on_close(cal) {
	cal.hide();
	cal_obj2 = null;
}

function cal2_on_autoclose(cal) {
	cal_obj2 = null;
}

function Trim(str) {
	return $.trim(str);
}

String.prototype.trim = function() {
	return Trim(this);
};

String.prototype.replaceAll = function(de, para) {
	var str = this;
	var pos = str.indexOf(de);
	while (pos > -1) {
		str = str.replace(de, para);
		pos = str.indexOf(de);
	}
	return (str);
};

String.prototype.replaceAt = function(index, c) {
    return this.substr(0, index) + c + this.substr(index + (c.length == 0 ? 1 : c.length));
};

function enableAllFormFields() {
	var e = document.getElementsByTagName("input");  
	for ( var i = 0; i < e.length; i++) {					
		e[i].disabled = false;
	}
	e = document.getElementsByTagName("select");  
	for ( var i = 0; i < e.length; i++) {					
		e[i].disabled = false;
	}
}

function disableAllFormFields(fieldsToExclude) {
	var argv = arguments;	
	var argc = argv.length;		
	var e = $("input:not(input[type=hidden])");	
	for ( var i = 0; i < e.length; i++) {			
		var found = false;
		for (var y = 0; y < argc; y++) {
			if (e[i].id != '' && e[i].id == argv[y]) {
				found = true;	
				break;
			}
		}
		e[i].disabled = !found;		
	}
	e = document.getElementsByTagName("select");  
	for ( var i = 0; i < e.length; i++) {						
		var found = false;
		for (var y = 0; y < argc; y++) {
			if (e[i].id != '' && e[i].id == argv[y]) {
				found = true;	
				break;
			}
		}	
		e[i].disabled = !found;
		
	}
}

// definita in commonHeader.jsp
function loadjscssfile(filename, filetype){
	if (filetype=="js") {
		var fileref=document.createElement('script');
		fileref.setAttribute("type","text/javascript");
		fileref.setAttribute("src", filename);
	}
	else if (filetype=="css") {
		var fileref=document.createElement("link");
		fileref.setAttribute("rel", "stylesheet");
		fileref.setAttribute("type", "text/css");
		fileref.setAttribute("href", filename);
	}
}

String.prototype.startsWith = function(str) {return (this.match("^"+str)==str);};

String.prototype.endsWith = function(str) {return (this.match(str+"$")==str);};

String.prototype.isInteger = function(str) {return !isNaN(parseInt(str));};
		
function onLoadFunction() {
}

// definita in commonHeader.jsp
function getBrowser() {
	return navigator.userAgent.toUpperCase();	
}

function getBrowserVersion() {
	var version = 0;
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) { 
		version = new Number(RegExp.$1);
	} else if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { 
		version = new Number(RegExp.$1);
	} 
	else if (getBrowser().indexOf("OPERA") != -1 || (getBrowser().indexOf("SAFARI") != -1 && getBrowser().indexOf("CHROME") == -1)) {
		if (/Version[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
			version = new Number(RegExp.$1);
		}
	} else if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { 
		version = new Number(RegExp.$1);
	}
	return version;
}

function getBrowserLanguage() {
	var userLang = (navigator.language) ? navigator.language : navigator.userLanguage;
	return userLang;
}

function areCookiesEnabled() {
    var r = false;
    setCookie("testing", "Hello", 1);    
    if (getCookie("testing") != null) {
        r = true;
        setCookie("testing", null, null);
    }
    return r;
}

function setCookie(name, value, duration) {
	try {
		$.cookie(name, value, duration);
	} catch (e) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + duration);
		document.cookie = name + "=" + escape(value) + ((duration == null) ? "" : ";expires="+exdate.toUTCString());
	}
}

function getCookie(name) {
	try {
		return $.cookie(name);
	} catch (e) {
		var c_start = document.cookie.indexOf(name + "=");
		if (c_start != -1) {
			c_start = c_start + name.length+1;
			var c_end = document.cookie.indexOf(";",c_start);
			if (c_end == -1) {
				c_end = document.cookie.length;
			}
			return unescape(document.cookie.substring(c_start,c_end));
		}
	}
	return null;
}

function makeBrowserChecks() {
	var msg = '';
	
	if (getBrowser().indexOf("MSIE") == -1 && getBrowser().indexOf("FIREFOX") == -1
		&& getBrowser().indexOf("OPERA") == -1 && getBrowser().indexOf("CHROME") == -1
		&& getBrowser().indexOf("SAFARI") == -1 && getBrowser().indexOf("TRIDENT") == -1) {
		msg = browserNotSupported;	
		
	}
	
	if (getBrowser().indexOf("TRIDENT") == -1 && (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 7) || 
			(getBrowser().indexOf("FIREFOX") != -1 && getBrowserVersion() < 3.5) || 
			(getBrowser().indexOf("OPERA") != -1 && getBrowserVersion() < 10.5) || 
			(getBrowser().indexOf("CHROME") != -1 && getBrowserVersion() < 8) ||
			(getBrowser().indexOf("SAFARI") != -1 && getBrowserVersion() < 4)) {
		msg = browserVersionNotSupported.replace("{0}", navigator.userAgent.appName);
		msg += (msg != '') ? "\n\n" + msg : msg;	
		
	}
	if (!areCookiesEnabled()) {
		msg += (msg != '') ? "\n\n" + cookiesDisabled : cookiesDisabled;
	}
	var browserWidth = window.screen.width;
	if (browserWidth < 1000) {
		msg = lowResolutionScreen;	
	}
	if (msg != '') {
		$.alerts.dialogClass = "style_1";
		jAlert(msg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			$("#j_username").focus();
		});
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
	}
}

function setChangedFieldAttribute($field) {
	var $fModificato = $field.parent().find("input:hidden[name^=modificato]").first();
	$fModificato.val("true");
}

function focusBarcodeField(evt) { 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	if ($('#barcode').length > 0) {
    	if ((evt.keyCode == 13 || evt.keyCode == 32) && (node.type == "text"))  {
    		evt.preventDefault();
    		var idTextField = node.id.replace(/([|])/g,'\\\$1');
    		var id = idTextField.replace('differenze','').replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','');
    		var $textField = $("#" + idTextField);
    		var $checkField = $("#" + "dpeChkbx_" + id);
    		if ($checkField.length > 0) {
	    		setTimeout(function() {
	    			var fieldVal = node.value.trim();
	    			if (!isNaN(fieldVal) && Number(fieldVal) > 101) {
						PlaySound('beep3');
						jConfirm(differenzaMoltoGrandeConfirm.replace('{0}',fieldVal), attenzioneMsg.toUpperCase(), function(r) {
						    if (r) { 
						    	$checkField.attr("checked", "true");	
						    	if (typeof(checkboxChanged) === 'function') {
					    			checkboxChanged($checkField);
					    		} else {
					    			setChangedFieldAttribute($textField);
					    		}
					    		$('#barcode').val('');			
					    		setTimeout(function() {$('#barcode').focus();}, 100);   
						    } else {
						    	$textField.val('');
						    	setTimeout(function() {$textField.select();}, 100);
						    }
						}, true, false);
	    			} else {
	    				$checkField.attr("checked", "true");		
			    		if (typeof(checkboxChanged) === 'function') {
			    			checkboxChanged($checkField);
			    		} else {
			    			setChangedFieldAttribute($textField);
			    		}
			    		$('#barcode').val('');			
			    		setTimeout(function() {$('#barcode').focus();}, 100);   
	    			}
				}, 100);
    		} else {
    			setTimeout(function() {
	    			var fieldVal = node.value.trim();
	    			if (!isNaN(fieldVal) && Number(fieldVal) > 101) {
						PlaySound('beep3');
						jConfirm(confirmResaInseritaMoltoGrande.replace('{0}',fieldVal), attenzioneMsg.toUpperCase(), function(r) {
						    if (r) { 
						    	$textField.trigger("blur");
				    			$('#barcode').val('');				
				    	    	setTimeout(function() {$('#barcode').focus();}, 100); 
						    } else {
						    	$textField.val('');
						    	setTimeout(function() {$textField.select();}, 100);
						    }
						}, true, false);
	    			} else {
	    				$textField.trigger("blur");
	        			$('#barcode').val('');				
	        	    	setTimeout(function() {$('#barcode').focus();}, 100);
	    			}
				}, 100);
    		}
    		return false;
    	} 
    }
} 

function stopRKey(event) {
	var keycode = (event.keyCode ? event.keyCode : event.charCode);
	if (keycode == '13') {									
		return false;
	} 
	return true;
} 			

function manageBarcodeKeyDown(callback, evt) {
	var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
	if (keycode == '13') {	
		var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
		evt.preventDefault();								
		var fieldVal = node.value;	
		var fieldName = node.name;	
		if (fieldVal.length == 0) {
			setTimeout(function() {$.alerts.dialogClass = "style_1"; jAlert(nessunaPubblicazione, attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; nessunaPubblicazioneAction();});}, 100);
			return false;	
		} else {			
			var pk = $("#" + fieldVal);
			var increment = false;						
			if ($("#autoincrement").length > 0) {
				if ($("#autoincrement").attr("checked") == true) {
					increment = true;
				}
			}			
			// Vittorio per ricerca parziale
			//if (pk.length == 0 && !increment) {
			//	pk = $("[id^=" + fieldVal + "]");
			//	if (pk.length > 1) {
			//		setTimeout(function() {$.alerts.dialogClass = "style_1"; jAlert("Ci sono più pubblicazioni col il codice a barre inserito", attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; nessunaPubblicazioneAction();});}, 100);
			//		return false;
			//	}
			//}
			
			if (pk.length > 0) {
				if (fieldName.length > 0) {
					fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
				}
				selectRowByBarcode(pk.val(), fieldName, increment);
				if (typeof(callback) === 'function') {
					callback(pk.val(), fieldName);
				}
			} else {
				noBarcodeFoundAction(fieldVal);
			}
		}
	}
}

function selectRowByBarcode(id, fieldName, increment) {
	if (id != '' && $('#' + id)) {
		playBarcodeBeep();
		if (lastFocusedRow != '') {
			lastFocusedRow.css({"backgroundColor":lastFocusColor});	
		}
		var currField = $("input:text[id='" + fieldName + id + "']");
		var val = currField.val();								
		if (increment) {
			if (typeof(val) === 'undefined') {
				currField.val(1);
			} else {
				var $fQtaReso = $("#qtaReso");
				var resoVal = isNaN($fQtaReso.val().trim()) ? 1 : Number($fQtaReso.val().trim());
				currField.val(!isNaN(val) ? (Number(val) + resoVal) : val);
				$fQtaReso.val(1);
			}
			setChangedFieldAttribute(currField);
			validateGiacenza(currField);
			if (showDialogResoSuperioreGiacenza) {
				setTimeout(function() {
					PlaySound('beep3');
					jConfirm(msgResoSuperioreGiacenza, attenzioneMsg.toUpperCase(), function(r) {
					    if (r) { 
							doActionsAfterSelectRow(currField, increment);
					    } else {
					    	var oldResoVal = !isNaN(val) ? Number(val) : val;
					    	objToFocus.val(oldResoVal);
					    	$("#copieDiv").text(oldResoVal);
					    }
					    showDialogResoSuperioreGiacenza = false;
						showDialogConfirmNumeroResaRespinto = false;
						objToFocus = null;
					});
					$("#barcode").val('');
				}, 100);
			} else {
				doActionsAfterSelectRow(currField, increment);
			}
		} else {		
			doActionsAfterSelectRow(currField, increment);
		}
	}	   
}

function showDialog(divId) {																
	var div = $("#" + divId);	
	var str = '<div id="close" style="z-index:999999; position:absolute; top:30px; left:630px"><a href="#"><img id="imgClose" src="/app_img/close.gif" style="border-style: none" border="0px" class="btn_close" title="' + chiudiMsg + '" alt="' + chiudiMsg + '"/></a></div>';		
	div.fadeIn('slow', function() {
		div.prepend(str);
	});										
	div.css({'visibility':'visible','top':'350px','width':'640px','height':'520px','border-color':'#66cccc'});											
	var popMargTop = (div.height() + 80) / 2;
    var popMargLeft = (div.width() + 80) / 2;
    div.css({
        'margin-top' : -popMargTop,
        'margin-left' : -popMargLeft
    });
    addFadeLayerEvents();	
}	

function doActionsAfterSelectRow(currField, increment) {
	var row = currField.parent().parent();	
	var oldBkColor = row.css("backgroundColor");	
	$("#barcode").val('');
	row.css({"backgroundColor":"#ffff66"});	
	currField.unbind("keydown", focusBarcodeField);
	currField.keydown( focusBarcodeField );
	if (!increment) {
		setTimeout(function() { currField.select(); },200);
		var bodyelem = '';
		if ($.browser.safari) { 
			bodyelem = $("body"); 
		} else { 
			bodyelem = $("html");
		}
		var topPos = row.offset().top - 40;
		//Vittorio 29/09/17 per problema Chrome
		//bodyelem.scrollTop(topPos);
		var scrollNode = document.scrollingElement || document.documentElement;
		scrollNode.scrollTop = topPos;
	} else {
		setTimeout(function() { setTotals(currField); doAfterAutoIncremetActions(currField); $("#barcode").focus(); },200);
	}
	lastFocusedRow = row;
	lastFocusColor = oldBkColor;
}

function setDpeCheckboxState(chkbx) {
	var e = document.getElementsByTagName("input");     
	if (chkbx.checked) {      
		chkbx.value="on";
		for ( var i = 0; i < e.length; i++) {					
			if (e[i].name == chkbx.name) {
				e[i].value = "on";
			}
		}				
	} else {   
		chkbx.value="off";
		for ( var i = 0; i < e.length; i++) {
			if (e[i].name == chkbx.name) {
				e[i].value = "off";
			}
		}
	}    
}   

function showMsgDisabledMenuStarterAlert() {
	jAlert(msgDisabledMenuStarter.replace('{0}', giorniProvaPerStarter), msgAvviso.toUpperCase());
}

function setDataTipoBolla(dataTipoBolla, selectId) {	
	var strSplit = dataTipoBolla.split("|");
	var val = strSplit[0] + "|" + strSplit[1];
	var optionVal = $("#" + selectId + " option[value^='" + val + "']").val();
	if (optionVal) {
		$("#dataTipoBolla").val(optionVal);
	} else {
		$("#dataTipoBolla option:first").attr('selected','selected');
	}
}

function spuntaAutomatica(o) {
	jConfirm(autoSpuntaMessage, attenzioneMsg.toUpperCase(), function(r) {
	    if (r) {
	    	var e = document.getElementsByTagName("input");
	    	for ( var i = 0; i < e.length; i++) {
	    		if (e[i].type == 'checkbox') {
	    			if (e[i].id.indexOf("dpeChkbx_") != -1) {
	    				e[i].checked = true;
	    				//MODIFICA EFFETTUATA SU RICHIESTA DI COMUZZI
	    				//12-05-2016 ticket:0000370
	    				var pkDiff = e[i].id.replace("dpeChkbx_", "differenze"); 
	    				var pkCopieLette = pkDiff.replace("differenze", "copieLette"); 
	    				if (document.getElementById(""+pkCopieLette+"") != null) {
	    					var valueDifferent = document.getElementById(""+pkDiff+"").value.replace("-", "");
		    				if(document.getElementById(""+pkCopieLette+"").value == ''){
		    					document.getElementById(""+pkCopieLette+"").value = valueDifferent;
		    					document.getElementById(""+pkDiff+"").value = "0";
		    				}
	    				
	    				}
	    				//FINE MODIFICA 
	    				
	    				
	    			}
	    		}
	    	}	
	    	if (typeof(setAllCheckboxChanged) === 'function') {
	    		setAllCheckboxChanged();
	    	}
	    }
	});
}

function spuntaAutomaticaOrdiniClienti(o) {
	var e = document.getElementsByTagName("input");
	for ( var i = 0; i < e.length; i++) {
		if (e[i].type == 'checkbox') {
			if (e[i].id.indexOf("dpeChkbx_") != -1) {
				e[i].checked = true;
			}
		}
	}	
	if (typeof(setAllCheckboxChanged) === 'function') {
		setAllCheckboxChanged();
	}
}

function validateFields(formId) {	
	var $fields = null;
	firstInvalidId = '';
	if (typeof formId != 'undefined') {
		$fields = $("#" + formId + " input:text[validateIsNumeric=true]");
	} else {
		$fields = $("input:text[validateIsNumeric=true]");
	}
	getFirstInvalidField($fields);
	if ((typeof firstInvalidId != 'undefined') && firstInvalidId != '') {
		selectRowByBarcode(firstInvalidId, '', false);
		unBlockUI();
		return false;
	}
	return true;
}

function validateFieldsClienteBase(showAlerts) {
	if ($('#inviaEmail').attr("checked") == true && $('#cliente\\.email').val().trim() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', emailLabel), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$('#inviaEmail').focus();
			});
		}
		unBlockUI();
		return false;
	}
	if ($("#cliente\\.nome").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', nomeLabel), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$('#cliente\\.nome').focus();
			});
		}
		unBlockUI();
		return false;
	}
	
	if ($("#autocomplete").val() != '' && $("#cliente\\.provincia").val()=='') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(error_localitaProviNuovoCliente, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$('#cliente\\.nome').focus();
			});
		}
		unBlockUI();
		return false;
	}
	
	
	$("#codCliente").attr("disabled", false);
	return true;
}

function getFirstInvalidField($fields) {
	$fields.each(function() {
		var $formField = $(this);
		var fVal = $formField.val().trim();
		if (fVal != '' && !bINT(fVal)) {
			$formField.css({"border":"2px solid red"});
			function animate() {
				$formField.border_color_animate('red', 500, function() {
					$formField.border_color_animate('#999', 500, function() {animate();});
				});
			}
			animate();
			firstInvalidId = $formField.attr("id");
			return false;			
		} else {
			$formField.stop();
			$formField.css({"borderColor":"#999","border-width":"1px"});
		}				
	});
}

function bINT(sText) {
	if ((parseFloat(sText) == parseInt(sText)) && !isNaN(sText)) {
		return true;
	} else {
		return false;
	} 
}

function bDecimal(sText){
	if (isNaN(parseFloat(sText)) || sText == '.')
		return false;
	else 
		return true;
}

function bBoolean(sText){
	if (sText.toString() == 'true' || sText.toString() == 'false')
		return true;
	else 
		return false;
}

function parseLocalNum(num) {
	var num1 = num;
	if (typeof(num) !== 'undefined') {
		num1 = num.trim();	
		var dotOccurrences = 0;
		var commaOccurrences = 0;
		if (num1.match(/\./g)) {
			dotOccurrences = num1.match(/\./g).length;
		}
		if (num1.match(/,/g)) {
			commaOccurrences = num1.match(/,/g).length;
		}
		if (dotOccurrences > 1 || commaOccurrences > 1) {
			$.alerts.dialogClass = "style_1";
			jAlert(numeroNonValido, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				throw numeroNonValido;
			});
		}	
		var numPrev = num1;
		if (num1.indexOf(",") != -1) {
			num1 = num1.replace(",", ".");
		}
		if (isNaN(num1)) {
			num1 = numPrev.replace(".", "");
			num1 = num1.replace(",", ".");
		}
	}
    return num1;
} 

function displayNum(num) { 	
	var num1 = num;	
	if (num.toString().length > 0) {		
		if (num.toString().indexOf(".") != -1) {
			num1 = num.toString().replace(".", ",");	
		}		
		if (num.toString().indexOf(",") != -1) {
			num1 = addThousandsSeparators(num1, ',', '.');
		}
	}
    return num1;
} 

function addThousandsSeparators(nStr, decimalSep, thousandsSep)
{
	nStr += '';
	x = nStr.split(decimalSep);
	x1 = x[0];
	x2 = x.length > 1 ? decimalSep + x[1] : '';
	var rgx = /(\d+)(\d{3})/;	
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + thousandsSep + '$2');
	}
	return x1 + x2;
}

function addWaitingLayerEventOnTableHeader(tableId) {
	$('#' + tableId + ' tbody tr').find('td[onclick]').each(function() {
		$(this).click(function() {	
			return (ray.ajax()); 
		});										
	});					
}

function addWaitingLayerEventOnTableHeaderThead(tableId) {
	$('#' + tableId + ' thead tr').find('td[onclick]').each(function() {
		$(this).click(function() {	
			return (ray.ajax()); 
		});										
	});					
}

function addWaitingLayerEventOnTableRows(tableId) {
	$('#' + tableId + ' tbody tr').each(function() {													
		$(this).click(function() {							
			return (ray.ajax()); 
		});										
	});					
}

function setScrollingTableBody(tableId, scrollDivId) {	 
	var html = $('#' + tableId + ' thead').html();
	if ($('#' + tableId + ' thead tr td').last().text().trim() != '') {
		var lastTdPos = html.toUpperCase().lastIndexOf("</TR>");
		html = html.substring(0, lastTdPos) + '<td class="tableHeader" width="2%" > </td>' + html.substring(lastTdPos + 5);	
	}
	var tableHead = '<table id="headerTable">' + html + '</table>';
	$('#' + tableId + ' thead').remove();
	$(tableHead).insertBefore($('#' + scrollDivId)).addClass('eXtremeTable').css({ 'border-collapse': 'collapse', 'table-layout': 'auto', 'width': '100%' });
	addWaitingLayerEventOnTableHeader('headerTable');
}

function addFadeLayerEvents(context) {
	var $closeDivs = $('#close, #fade');
	var $closeDetDivs = $('#closedet');
	var $fadeDiv = $('#fade, .popup_block');
	var $fadeDetDiv = $('#fadedet, #popup_name_det');
	var $fadeAndImgDet = $('#fadedet, #imgClosedet');
	if (typeof context != 'undefined') {
		$closeDivs = $('#close, #fade', context);
		$closeDetDivs = $('#closedet', context);
		$fadeDiv = $('#fade, .popup_block', context);
		$fadeDetDiv = $('#fadedet, #popup_name_det', context);
		$fadeAndImgDet = $('#fadedet, #imgClosedet', context);
	}
	$closeDivs.live('click', function() { 
		var $popup = $(this);
		$fadeDiv.fadeOut(function() {
			$fadeDiv.die();
	    });
		if (popupWiderThanViewport) {
			enableDraggingOnPopup($popup);
			popupWiderThanViewport = false;
		}
	    onCloseLayer();
	    return false;
	});	
	$closeDetDivs.live('click', function() { 
		$fadeDetDiv.fadeOut(function() {
			$fadeAndImgDet.die();
			$fadeAndImgDet.remove();	
	    });
	    return false;
	});	
}

function addCloseButtonToLayer(layerId, imgSrc, closeLabel) {
	var str = '<div id="close" style="z-index:999999"><a href="#" class="btn_close"><img id="imgClose" src="' + imgSrc + '" style="border-style: none" border="0px" class="btn_close" title="' + closeLabel + '" alt="' + closeLabel + '"/></a></div>';													
	$('#' + layerId).prepend(str);	
}

function addTableLastRow(tableId){ 		   	
	if ($("#" + tableId).length > 0) {
		var tabHeight = Number($("#" + tableId).css("height").replaceAll("px","").trim());	
		var totalRowHeight = 0;
		$("#" + tableId + " tbody tr").each(function() {
			var h = Number($(this).css("height").replaceAll("px","").trim());						
			totalRowHeight += (h == 0 || h > 35) ? 30 : h;
		});	
		var row;
		if ($("#" + tableId + " tbody tr").length > 1) {
			row = $("#" + tableId + " tbody>tr:last").prev().clone(true).find("td").text("");
		} else {
			row = $("#" + tableId + " tbody>tr:last").clone(true).find("td").text("");
		}
		row.insertAfter("#" + tableId + " tbody>tr:last").css({"height":(tabHeight - totalRowHeight) + "px"});
	}
}

function addHiddenInput(formId, hiddenFieldName) {	
	$("#" + formId + " input:hidden").last().append('<input type="hidden" name="' + hiddenFieldName + '" id="' + hiddenFieldName + '"/>"');				
}

function setFocusedFieldStyle(context) {
	if (typeof(context) === 'undefined' || context == '') {
		context = window.document;
	} 
	if (getBrowser().indexOf("SAFARI") == -1) {
		$("input[type='text'], select", context).focus(function() {
			$(this).css('background','#ffff99');
    	});
		$("input[type='text'], select", context).blur(function() { 
			$(this).css('background','#fff');
	    });
	}
}

function doCloseLayer(callback) {
	$("#close").trigger('click');
	if (callback && typeof(callback) === "function") {
		callback();
	}
}

function doValidationCategoria(showAlerts) {
	var desc = $("#descrizione",  window.parent.document).val();
	if (desc.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',descrizione), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#descrizione",  window.parent.document).focus();
		});
		return false; 
	} 
	$("#descrizione", window.parent.document).attr("disabled", false);
	return true; 
}

function doValidationSottoCategoria() {
	var desc = $("#descrizione",  window.parent.document).val();
	if (desc.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',descrizione), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#descrizione",  window.parent.document).focus();
		});
		return false; 
	} 
	$("#descrizione", window.parent.document).attr("disabled", false);
	return true;
}

function doValidationProdotti(isNew) {
	var pre = $('#prezzi', window.parent.document).length > 0 ? parseLocalNum($('#prezzi', window.parent.document).val()) : ''; 
	var val = $('#validita', window.parent.document).length > 0 ? $('#validita', window.parent.document).val() : ''; 
	var desc = $("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).val();
	var descB = $("#prodottoEdicola\\.descrizioneProdottoB",  window.parent.document).val();
	var bcode = $("#prodottoEdicola\\.barcode",  window.parent.document).val();
	var cat = $("#codCategoria",  window.parent.document).val();
	var scat = $("#sottoCategoria",  window.parent.document).val();
	var separator = $("#attachment1").val().indexOf("/") != -1 ? "/" : "\\";
	var imgProd = $("#attachment1", window.parent.document).val().substring($("#attachment1", window.parent.document).val().lastIndexOf(separator) + 1);
	if (desc.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',descrizione), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).focus();
		});
		return false; 
	} 
	if (cat == null || cat.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',msgCategoria), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#codCategoria",  window.parent.document).focus();
		});
		return false; 
	}
	if (scat == null || scat.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',msgSottoCategoria), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#sottoCategoria",  window.parent.document).focus();
		});
		return false; 
	}
	if (bcode.trim() == '') {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',barcode), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$("#prodottoEdicola\\.barcode",  window.parent.document).focus();
		});
		return false; 
	}
	if (val.trim() != '' && !checkDate(val)) {
		$.alerts.dialogClass = "style_1";
		jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$('#validita', window.parent.document).focus();
		});
		return false; 
	} 
	if (pre.trim() != '' && isNaN(parseLocalNum(pre.trim()))) {
		$.alerts.dialogClass = "style_1";
		jAlert(prezzoInvalido, attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$('#prezzi', window.parent.document).focus();
		});
		return false; 
	} 
	if (imgProd != '' && !hasOnlyNonAccentCharacters(imgProd)) {
		$.alerts.dialogClass = "style_1";
		jAlert(nomeFileInvalido.replace('{0}',imgProd), attenzioneMsg, function() {
			$.alerts.dialogClass = null;
			$('#attachment1', window.parent.document).focus();
		});
		return false; 
	} 
	$('#PrezziTab_table tbody tr td:nth-child(1)', window.parent.document).each(function() { 
		if ($(this).find('input:text').length == 0) { 
			pre += ',' + (isNaN(parseLocalNum($(this).text().trim())) ? 0 : parseLocalNum($(this).text().trim())); 
		} 
	}); 
	$('#PrezziTab_table tbody tr td:nth-child(2)', window.parent.document).each(function() { 
		if ($(this).find('input:text').length == 0) { 
			val += ',' + ($(this).text().trim() != 'undefined' ? $(this).text().trim() : ''); 
		} 
	}); 
	if ($("#PrezziTab_table").length > 0 && pre.length == 0) {
		$.alerts.dialogClass = "style_1";
		jAlert(campoObbligatorio.replace('{0}',prezzo), attenzioneMsg, function() {
			$('#prezzi', window.parent.document).focus();
			$.alerts.dialogClass = null;
		});
		return false; 
	}
	var retVal = true;
	dojo.xhrGet({
		url: appContext + '/pubblicazioniRpc_getPneBarcodeOrDescrizione.action?barcode=' + bcode,	
		handleAs: "json",				
		sync: true,
		headers: { "Content-Type": "application/json; charset=uft-8"}, 	
		preventCache: true,
		handle: function(data,args) {	
			if (data.length > 0) {
				var prodotti = '\n';
				for (var i = 0; i < data.length; i++) {
	            	if (i > 0 && i < data.length) {
	            		prodotti += '\n';
	            	}
	            	prodotti += '- ' + data[i].descrizione;
	            }
				if (data.length > ((isNew == 'true') ? 0 : 1)) {
					$.alerts.dialogClass = "style_1";
					jAlert(msgBarcodeAssociatoAltriProdotti.replace('{0}',bcode).replace('{1}',prodotti), attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
					retVal = false;
					window.parent.document.stopWaitingDiv = true;
					return false;
					/*if (!window.confirm(msgBarcodeAssociatoAltriProdotti.replace('{0}',bcode).replace('{1}',prodotti))) {
						retVal = false;
						window.parent.document.stopWaitingDiv = true;
						return false;
					}*/
				}
			}
		}
    });
	$("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).val(desc.replace(/\u20ac/g, '&#8364;'));
	$("#prodottoEdicola\\.descrizioneProdottoB",  window.parent.document).val(descB.replace(/\u20ac/g, '&#8364;'));
	$('#prezziSelected').val(pre); 
	$('#validitaSelected').val(val);
	$("#aliquotaIva", window.parent.document).attr("disabled", false);
	$("#causaleIva", window.parent.document).attr("disabled", false);
	$("#prodottoEdicola\\.descrizioneProdottoA", window.parent.document).attr("disabled", false);
	$("#prodottoEdicola\\.descrizioneProdottoB", window.parent.document).attr("disabled", false);
	$("#codCategoria", window.parent.document).attr("disabled", false);
	$("#sottoCategoria", window.parent.document).attr("disabled", false);
	return retVal;
}

function htmlEncode(value){
	var html = $('<div/>').text(value).html();
	return value;
}

function htmlDecode(value){
	var text = $('<div/>').html(value).text();
	return text;
}

	
function promptForEmail(url) {
	if (userType == 1) {
		if (hasProfiloStarter.toString() == 'true') {
			promptPrivacyEdicolaStarter(url);
		} else {
			promptPrivacyEdicola(url);
		}
	} else if (userType == 2) {
		promptPrivacyClienteEdicola(url);
	}
}

function promptPrivacyEdicolaStarter(url) {
	var params = '{';
	params += '"testoPrivacyEdicolaStarter2":"' + testoPrivacyEdicolaStarter2.replace('{0}', giorniProvaPerStarter) + '",';
	params += '"ragioneSociale":"' + ragioneSocialeLabel + '",';
	params += '"indirizzo":"' + indirizzoLabel + '",';
	params += '"numCivico":"' + numCivicoLabel + '",';
	params += '"localita":"' + localitaLabel + '",';
	params += '"provincia":"' + provinciaLabel + '",';
	params += '"cap":"' + capLabel + '",';
	params += '"telefono":"' + telefonoLabel + '",';
	params += '"cellulare":"' + cellulareLabel + '",';
	params += '"tipoEdicola":"' + tipoEdicolaLabel + '",';
	params += '"email":"' + emailLabel + '"';
	params += '}';
	jPromptPrivacyEdicolaStarter(testoPrivacyEdicolaStarter1,'','', msgAccettoPrivacy, params, function(result) {
		if (result != null && result != '') {
			var jsonResult = jQuery.parseJSON(result);
			var ragioneSociale = jsonResult.ragioneSociale;
			var tipoLocalita = jsonResult.tipoLocalita;
			var indirizzo = jsonResult.indirizzo;
			var numero = jsonResult.numero;
			var cap = jsonResult.cap;
			var localita = jsonResult.localita;
			var provincia = jsonResult.provincia;
			var telefono = jsonResult.telefono;
			var cellulare = jsonResult.cellulare;
			var tipoEdicola = jsonResult.tipoEdicola;
			var email = jsonResult.email;
			var qs = appContext + namespace + '/pubblicazioniRpc_sendValidationEmail.action?ragioneSociale=' + escape(ragioneSociale) + '&tipoLocalita=' + tipoLocalita + '&indirizzo=' + escape(indirizzo) + '&numero=' + escape(numero) + '&cap=' + cap + '&localita=' + escape(localita) + '&provincia=' + provincia + '&telefono=' + telefono + '&cellulare=' + cellulare + '&tipoEdicola=' + tipoEdicola + '&email=' + escape(email) + '&url=' + url;
			ray.ajax();
			dojo.xhrGet({
				url: qs,	
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				preventCache: true,
				handle: function(data,args) {	
					unBlockUI();
					if (args.xhr.status == 200) {
						var error = data[0].error;
						if (typeof(error) !== 'undefined' && error.length > 0) {
							$.alerts.dialogClass = "style_1";
							jAlert(error, attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								setTimeout(function() {
									promptPrivacyEdicolaStarter(url);
								}, 100);
							});
						} else {
							jAlert(msgInvioEmailVerifica, msgAvviso, function() {
								window.location = appContext + namespace + "/j_spring_security_logout";
							});
						}
					} else {
						jAlert(msgInvioEmailVerifica, msgAvviso, function() {
							window.location = appContext + namespace + "/j_spring_security_logout";
						});
					}
				}
		    });
		} 
	}, function() {
		if ($("#ragioneSociale").val() == '' || $("#ragioneSociale").val().trim() == ragioneSocialeLabel) {
			$("#ragioneSociale").css({"border":"1px solid red"});
			$("#ragioneSociale").focus().select();
			return false;
		}
		if ($("#indirizzo").val() == '' || $("#indirizzo").val().trim() == indirizzoLabel) {
			$("#indirizzo").css({"border":"1px solid red"});
			$("#indirizzo").focus().select();
			return false;
		}
		if ($("#numero").val().trim() == '' || $("#numero").val().trim() == numCivicoLabel) {
			$("#numero").css({"border":"1px solid red"});
			$("#numero").focus().select();
			return false;
		}
		if ($("#cap").val().trim() == '' || $("#cap").val().trim() == capLabel || $("#cap").val().trim().length < 5) {
			$("#cap").css({"border":"1px solid red"});
			$("#cap").focus().select();
			return false;
		}
		if ($("#localita").val().trim() == '' || $("#localita").val().trim() == $('<div/>').html(localitaLabel).text()) {
			$("#localita").css({"border":"1px solid red"});
			$("#localita").focus().select();
			return false;
		}
		if ($("#provincia").val() == -1) {
			$("#provincia").css({"background-color":"#ffff99"});
			$("#provincia").css({"border":"1px solid red"});
			$("#provincia").focus();
			return false;
		}
		if (($("#telefono").val().trim() == '' || $("#telefono").val().trim() == telefonoLabel) && ($("#cellulare").val().trim() == '' || $("#cellulare").val().trim() == cellulareLabel)) {
			$("#telefono").css({"border":"1px solid red"});
			$("#telefono").focus().select();
			return false;
		}
		if (($("#telefono").val().trim() == '' || $("#telefono").val().trim() == telefonoLabel) && ($("#cellulare").val().trim() == '' || $("#cellulare").val().trim() == cellulareLabel)) {
			$("#cellulare").css({"border":"1px solid red"});
			$("#cellulare").focus().select();
			return false;
		}
		if ($("#tipoEdicola").val() == -1) {
			$("#tipoEdicola").css({"background-color":"#ffff99"});
			$("#tipoEdicola").css({"border":"1px solid red"});
			$("#tipoEdicola").focus();
			return false;
		}
		if ($("#email").val().trim() == '' || $("#email").val().trim() == emailLabel) {
			$("#email").css({"border":"1px solid red"});
			$("#email").focus().select();
			return false;
		}
		if (!checkEmail($("#email").val().trim())) {
			$("#email").css({"border":"1px solid red"});
			$("#email").focus().select();
			return false;
		}
		if ($("#popup_prompt1").is(':checked') != true) {
			$("#popup_fieldtitle2").css({"border":"1px solid red"});
			$("#popup_prompt1").click(function() {
				$("#popup_fieldtitle2").css({"border":"none"});
			});
			return false;
		}
		return true;
	});
	
	setFocusedFieldStyle();
	
	$("#ragioneSociale").focus(function() {
		var $val = $(this).val();
		$(this).val($val == ragioneSocialeLabel ? "" : $val);
	});
	$("#ragioneSociale").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? ragioneSocialeLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#indirizzo").focus(function() {
		var $val = $(this).val();
		$(this).val($val == indirizzoLabel ? "" : $val);
	});
	$("#indirizzo").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? indirizzoLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#numero").focus(function() {
		var $val = $(this).val();
		$(this).val($val == numCivicoLabel ? "" : $val);
	});
	$("#numero").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? numCivicoLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#cap").focus(function() {
		var $val = $(this).val();
		$(this).val($val == capLabel ? "" : $val);
	});
	$("#cap").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? capLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#localita").focus(function() {
		var $val = $(this).val();
		$(this).val($val == $('<div/>').html(localitaLabel).text() ? "" : $val);
	});
	$("#localita").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? $('<div/>').html(localitaLabel).text() : $val);
		$(this).css({"border":"none"});
	});
	$("#provincia").change(function() {
		$(this).css({"background-color":"#fff"});
		$(this).css({"border":"none"});
	});
	$("#telefono").focus(function() {
		var $val = $(this).val();
		$(this).val($val == telefonoLabel ? "" : $val);
	});
	$("#telefono").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? telefonoLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#cellulare").focus(function() {
		var $val = $(this).val();
		$(this).val($val == cellulareLabel ? "" : $val);
	});
	$("#cellulare").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? cellulareLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#tipoEdicola").change(function() {
		$(this).css({"background-color":"#fff"});
		$(this).css({"border":"none"});
	});
	$("#email").focus(function() {
		var $val = $(this).val();
		$(this).val($val == emailLabel ? "" : $val);
	});
	$("#email").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? emailLabel : $val);
		$(this).css({"border":"none"});
	});
	$("#popup_prompt1").change(function() {
		$(this).css({"background-color":"#fff"});
		$(this).css({"border":"none"});
	});
	$("#ragioneSociale").focus();
}

function promptPrivacyClienteEdicola(url) {
	var params = '{';
	params += '"dtNasc":"' + dtNascLabel + '",';
	params += '"giorno":"' + giornoLabel + '",';
	params += '"mese":"' + meseLabel + '",';
	params += '"anno":"' + annoLabel + '",';
	params += '"email":"' + emailLabel + '"';
	params += '}';
	jPromptPrivacyCliente(privacyMessageLettore,'','', msgAccettoPrivacy, params, function(result) {
		if (result != null && result != '') {
			var jsonResult = jQuery.parseJSON(result);
			var dtG = jsonResult.dtG;
			var dtM = jsonResult.dtM;
			var dtA = jsonResult.dtA;
			var dtNasc = pad(dtG, 2) + "-" + pad(dtM, 2) + "-" + dtA;
			var sesso = jsonResult.sesso;
			var lavoro = jsonResult.lavoro;
			var scuola = jsonResult.scuola;
			var email = jsonResult.email;
			dojo.xhrGet({
				url: appContext + namespace + '/pubblicazioniRpc_sendValidationEmail.action?dtNasc=' + dtNasc + '&sesso=' + sesso + '&lavoro=' + lavoro + '&scuola=' + scuola + '&email=' + email + '&url=' + url,	
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				preventCache: true,
				handle: function(data,args) {	
					if (args.xhr.status == 200) {
						var error = data[0].error;
						if (typeof(error) !== 'undefined' && error.length > 0) {
							$.alerts.dialogClass = "style_1";
							jAlert(error, attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								setTimeout(function() {
									promptPrivacyClienteEdicola(url);
								}, 100);
							});
						} else {
							jAlert(msgInvioEmailVerifica, msgAvviso, function() {
								window.location = appContext + namespace + "/j_spring_security_logout";
							});
						}
					} else {
						jAlert(msgInvioEmailVerifica, msgAvviso, function() {
							window.location = appContext + namespace + "/j_spring_security_logout";
						});
					}
				}
		    });
		} 
	}, function() {
		var now = new Date(); 
		if ($("#dtNascG").val() == '' || $("#dtNascG").val().trim() == giornoLabel) {
			$("#dtNascG").focus();
			return false;
		}
		if ($("#dtNascM").val().trim() == '' || $("#dtNascM").val().trim() == meseLabel) {
			$("#dtNascM").focus();
			return false;
		}
		if ($("#dtNascA").val().trim() == '' || $("#dtNascA").val().trim() == annoLabel) {
			$("#dtNascA").focus();
			return false;
		}
		if ($("#sesso").val() == -1) {
			$("#sesso").css({"background-color":"#ffff99"});
			$("#sesso").focus();
			return false;
		}
		if ($("#lavoro").val() == -1) {
			$("#lavoro").css({"background-color":"#ffff99"});
			$("#lavoro").focus();
			return false;
		}
		if ($("#scuola").val() == -1) {
			$("#scuola").css({"background-color":"#ffff99"});
			$("#scuola").focus();
			return false;
		}
		if ($("#email").val().trim() == '' || $("#email").val().trim() == emailLabel) {
			$("#email").focus();
			return false;
		}
		if (Number($("#dtNascG").val()) < 1 || Number($("#dtNascG").val()) > 31) {
			$("#dtNascG").focus();
			return false;
		}
		if (Number($("#dtNascM").val()) < 1 || Number($("#dtNascM").val()) > 12) {
			$("#dtNascM").focus();
			return false;
		}
		if (Number($("#dtNascA").val()) <= (now.getFullYear() - 120) || Number($("#dtNascA").val()) >= now.getFullYear()) {
			$("#dtNascA").focus();
			return false;
		}
		if (!checkDate($("#dtNascG").val().trim() + "/" + $("#dtNascM").val().trim() + "/" + $("#dtNascA").val().trim())) {
			$("#dtNascG").focus();
			return false;
		}
		if (!checkEmail($("#email").val().trim())) {
			$("#email").focus();
			return false;
		}
		if ($("#popup_prompt1").is(':checked') != true) {
			$("#popup_fieldtitle2").css({"border":"1px solid red"});
			$("#popup_prompt1").click(function() {
				$("#popup_fieldtitle2").css({"border":"none"});
			});
			return false;
		}
		return true;
	});
	
	setFocusedFieldStyle();
	
	$("#dtNascG").focus(function() {
		var $val = $(this).val();
		$(this).val($val == giornoLabel ? "" : $val);
	});
	$("#dtNascG").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? giornoLabel : $val);
	});
	$("#dtNascM").focus(function() {
		var $val = $(this).val();
		$(this).val($val == meseLabel ? "" : $val);
	});
	$("#dtNascM").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? meseLabel : $val);
	});
	$("#dtNascA").focus(function() {
		var $val = $(this).val();
		$(this).val($val == annoLabel ? "" : $val);
	});
	$("#dtNascA").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? annoLabel : $val);
	});
	$("#email").focus(function() {
		var $val = $(this).val();
		$(this).val($val == emailLabel ? "" : $val);
	});
	$("#email").blur(function() {
		var $val = $(this).val().trim();
		$(this).val($val == '' ? emailLabel : $val);
	});
	$("#sesso").change(function() {
		$(this).css({"background-color":"#fff"});
	});
	$("#lavoro").change(function() {
		$(this).css({"background-color":"#fff"});
	});
	$("#scuola").change(function() {
		$(this).css({"background-color":"#fff"});
	});
	$("#popup_prompt1").change(function() {
		$(this).css({"background-color":"#fff"});
	});
	
	$("#dtNascG").focus();
}

function promptPrivacyEdicola(url) {
	jPromptPrivacy(privacyMessageEdicola,'','', email, msgAccettoPrivacy, function(result) {
		if (result != null) {
			ray.ajax();
			var email = result.split("|")[0];
			dojo.xhrGet({
				url: appContext + namespace + '/pubblicazioniRpc_sendValidationEmail.action?email=' + email + "&url=" + url,	
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				preventCache: true,
				handle: function(data,args) {	
					unBlockUI();
					if (args.xhr.status == 200) {
						var error = data[0].error;
						if (typeof(error) !== 'undefined' && error.length > 0) {
							$.alerts.dialogClass = "style_1";
							jAlert(error, attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								setTimeout(function() {
									promptPrivacyEdicola(url);
								}, 100);
							});
						} else {
							jAlert(msgInvioEmailVerifica);
						}
					} else {
						jAlert(msgErroreInvioRichiesta);
					}
				}
		    });
		} 
	}, function() {
		if ($("#email").val().trim() == '' || $("#email").val().trim() == emailLabel) {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', emailLabel), attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				promptForEmail(url);
			});
			$("#email").focus();
			return false;
		}
		if (!checkEmail($("#email").val().trim())) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgInvalidEmail, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				promptForEmail(url);
			});
			$("#email").focus();
			return false;
		}
		if ($("#popup_prompt1").is(':checked') != true) {
			$("#popup_fieldtitle2").css({"border":"1px solid red"});
			$("#popup_prompt1").click(function() {
				$("#popup_fieldtitle2").css({"border":"none"});
			});
			return false;
		}
		return true;
	});
	setFocusedFieldStyle();
}

function attivaPeriodoProvaStarter() {
	jConfirm(attivaPeriodoProvaConfirm.replace('{0}', giorniProvaPerStarter).replace('{1}', giorniProvaPerStarter), attenzioneMsg.toUpperCase(), function(r) {
	    if (r) { 
	    	dojo.xhrGet({
	    		url: appContext + namespace + '/pubblicazioniRpc_attivaPeriodoProva.action',	
	    		handleAs: "json",				
	    		headers: { "Content-Type": "application/json; charset=uft-8"}, 	
	    		preventCache: true,
	    		handle: function(data,args) {
	    			if (args.xhr.status == 200) {
	    				if (typeof(data.error) !== 'undefined' && data.error != '') {
		    				$.alerts.dialogClass = "style_1";
		    				jAlert(data.error, attenzioneMsg, function() {
		    					$.alerts.dialogClass = null;
		    					return false;
		    				});
	    				} else {
	    					var d = new Date();
	    				    var curr_date = pad(d.getDate(), 2);
	    				    var curr_month = pad(d.getMonth() + 1, 2);
	    				    var curr_year = d.getFullYear();
	    					var dataOggi = curr_date + '/' + curr_month + '/' + curr_year;
	    					var dS = new Date();
	    					dS.setDate(d.getDate() + Number(15));
	    					var curr_dateS = pad(dS.getDate(), 2);
	    				    var curr_monthS = pad(dS.getMonth() + 1, 2);
	    				    var curr_yearS = dS.getFullYear();
	    					var dataScadenza = curr_dateS + '/' + curr_monthS + '/' + curr_yearS;
		    				jAlert(msgAttivazionePeridoProvaEseguita.replace('{0}', giorniProvaPerStarter).replace('{1}', dataOggi).replace('{2}', dataScadenza), msgAvviso, function() {
		    					window.location = appContext + namespace + "/j_spring_security_logout";
		    				});
	    				}
	    			} else {
	    				jAlert(msgErroreInvioRichiesta);
	    			}
	    		}
	        });  
	    } else {
	    	$textField.val('');
	    	setTimeout(function() {$textField.focus();}, 100);
	    }
	}, true, false);
}

function pad(number, length) {
    var str = '' + number;
    while (str.length < length) {
        str = '0' + str;
    }
    return str;
}

function hasOnlyNonAccentCharacters(strValue) {
	var objRegExp  = /^(?=.*[A-Za-z0-9])[A-Za-z0-9 _,.-]*$/;
	return objRegExp.test(strValue);
}

function openDiv(popID, popWidth, popHeight, url, bgColor, type, context, callback) {
	var $div = $('#' + popID);
	if (typeof context != 'undefined' && context != '') {
		$div = $('#' + popID, context);
	}
	if (popWidth > $(window).width()) {
		$div.fadeIn().css({ 'width': '90%', 'height': '80%' });
	} else {
		$div.fadeIn().css({ 'width': Number( popWidth ), 'height': Number( popHeight ) });
	}
    var popMargTop = ($div.height() + 80) / 2;
    var popMargLeft = ($div.width() + 80) / 2;
    var backGroundColor = "#ccccff";
    var divType = (typeof type != 'undefined' && type.length > 0) ? type : '';
    if (typeof bgColor != 'undefined' && bgColor.length > 0) {
    	backGroundColor = bgColor;
    } 
    var closeBtnStyle = 'btn_close';
    if (popWidth > $(window).width()) {
    	popupWiderThanViewport = true;
    	$div.draggable("destroy");
    	closeBtnStyle = 'btn_closeScrollable';
    	if (getBrowser().indexOf("MSIE") != -1) {
    		$div.css({
	 	        'margin-top' : -popMargTop,
	 	        'margin-left' : -popMargLeft,
	 	        'background-color' : backGroundColor,
	 	        'overflow' : 'auto'
	 	    });
    	} else {
	    	 $div.css({
	 	        'margin-top' : -popMargTop,
	 	        'margin-left' : -popMargLeft,
	 	        'background-color' : backGroundColor,
	 	        'overflow-y' : 'scroll'
	 	    });
    	}
    } else {
	    $div.css({
	        'margin-top' : -popMargTop,
	        'margin-left' : -popMargLeft,
	        'background-color' : backGroundColor
	        
	    });
    }
    $div.html('<div style="position:absolute; width:100%; text-align:center; margin-left:auto; margin-right:auto; margin-top:10%;"><p><img src="/app_img/ajax-loader.gif" width="220px" height="19px" /></p></div>');
	var str = '<div id="close' + divType + '" style="z-index:999999;"><a href="#" class="btn_close"><img id="imgClose' + divType + '" src="/app_img/close.gif" style="border-style: none" border="0px" class="' + closeBtnStyle + '" title="' + chiudiLabel + '" alt="' + chiudiLabel + '"/></a></div>';
	if (url.length > 0) {
		url = url + ((url.indexOf("?") == -1) ? "?" : "&") + "ispopup=true";
		$div.load(url, function() {
			$div.prepend(str);
			if (typeof(callback) === 'function') {
				callback();
			}
		});
	} 
    if (getBrowser().indexOf("MSIE") != -1) {
    	$div.css({
	        'border-color' : '#3399cc'
	    });
    } else {
    	$('body').append('<div id="fade' + divType + '"></div>');
    }
}


function openDivRifornimenti(popID, popWidth, popHeight, url, bgColor, type, context, callback) {
	var $div = $('#' + popID);
	if (typeof context != 'undefined' && context != '') {
		$div = $('#' + popID, context);
	}
	if (popWidth > $(window).width()) {
		$div.fadeIn().css({ 'width': '90%', 'height': '80%' });
	} else {
		$div.fadeIn().css({ 'width': Number( popWidth ), 'height': Number( popHeight ) });
	}
    var popMargTop = ($div.height() + 80) / 2;
    var popMargLeft = ($div.width() + 80) / 2;
    var backGroundColor = "#ccccff";
    var divType = (typeof type != 'undefined' && type.length > 0) ? type : '';
    if (typeof bgColor != 'undefined' && bgColor.length > 0) {
    	backGroundColor = bgColor;
    } 
    var closeBtnStyle = 'btn_close';
    if (popWidth > $(window).width()) {
    	popupWiderThanViewport = true;
    	$div.draggable("destroy");
    	closeBtnStyle = 'btn_closeScrollable';
    	if (getBrowser().indexOf("MSIE") != -1) {
    		$div.css({
	 	        'margin-top' : -popMargTop,
	 	        'margin-left' : -popMargLeft,
	 	        'background-color' : backGroundColor,
	 	        'overflow' : 'auto'
	 	    });
    	} else {
	    	 $div.css({
	 	        'margin-top' : -popMargTop,
	 	        'margin-left' : -popMargLeft,
	 	        'background-color' : backGroundColor,
	 	        'overflow-y' : 'scroll'
	 	    });
    	}
    } else {
	    $div.css({
	        'margin-top' : -popMargTop,
	        'margin-left' : -popMargLeft,
	        'background-color' : backGroundColor,
	        'overflow' : 'auto'
	    });
    }
    $div.html('<div style="position:absolute; width:100%; text-align:center; margin-left:auto; margin-right:auto; margin-top:10%;"><p><img src="/app_img/ajax-loader.gif" width="220px" height="19px" /></p></div>');
	var str = '<div id="close' + divType + '" style="z-index:999999;margin: 40px 30px -10px 10px;"><a href="#" class="btn_close"><img id="imgClose' + divType + '" src="/app_img/close.gif" style="border-style: none" border="0px" class="' + closeBtnStyle + '" title="' + chiudiLabel + '" alt="' + chiudiLabel + '"/></a></div>';
	if (url.length > 0) {
		url = url + ((url.indexOf("?") == -1) ? "?" : "&") + "ispopup=true";
		$div.load(url, function() {
			$div.prepend(str);
			if (typeof(callback) === 'function') {
				callback();
			}
		});
	} 
    if (getBrowser().indexOf("MSIE") != -1) {
    	$div.css({
	        'border-color' : '#3399cc'
	    });
    } else {
    	$('body').append('<div id="fade' + divType + '"></div>');
    }
}



function findDuplicates(arr) {
  var len=arr.length,
      out=[],
      counts={};

  for (var i=0;i<len;i++) {
    var item = arr[i];
    counts[item] = counts[item] >= 1 ? counts[item] + 1 : 1;
  }

  for (var item in counts) {
    if(counts[item] > 1)
      out.push(item);
  }
  return out;
}

function sleep(ms) {
	var dt = new Date();
	dt.setTime(dt.getTime() + ms);
	while (new Date().getTime() < dt.getTime());
}

function checkDate(date) {
    var mo, day, yr;
    var entry = date;
    var re = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/;
    if (re.test(entry)) {
        var delimChar = (entry.indexOf("/") != -1) ? "/" : "-";
        var delim1 = entry.indexOf(delimChar);
        var delim2 = entry.lastIndexOf(delimChar);
        day = parseInt(entry.substring(0, delim1), 10);
        mo = parseInt(entry.substring(delim1+1, delim2), 10);
        yr = parseInt(entry.substring(delim2+1), 10);
        var testDate = new Date(yr, mo-1, day);
        if (testDate.getDate() == day) {
            if (testDate.getMonth() + 1 == mo) {
                if (testDate.getFullYear() == yr) {
                    return true;
                } else {
                	return false;
                }
            } else {
            	return false;
            }
        } else {
        	return false;
        }
    } else {
    	return false;
    }
    return false;
}

function checkEmail(email) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)){
		return (true);
	}
}

function autoSaveBolle(interval) {
	this.StartPolling = function(){
		this.StopPolling();
		this.timer = setInterval(poll, interval);
	};
	this.StopPolling = function(){
		clearInterval(this.timer);
	};
	this.setPollInterval= function(i) {
		interval = i;
	};
	function poll() {
		switch(tipoBolla) {
			case BOLLA_CONSEGNA: {
				if ($("#dataTipoBolla").val().split("|")[2] == "true" && $("#BollaControlloForm input:hidden[name^=modificato][value=true]").length > 0) {
					if (!validateFields('BollaControlloForm')) {
						$.alerts.dialogClass = "style_1";
	    				jAlert(campiBollaInvalidiError, attenzioneMsg, function() {
	    					$.alerts.dialogClass = null;
	    					if ((typeof firstInvalidId != 'undefined') && firstInvalidId != '') {
	    						$("#" + firstInvalidId.replace(/([|@])/g,'\\\$1')).focus().select();
	    					}
	    					return false;
	    				});
	    				return false;
					} 
					saveBollaAuto();
					
					//setFieldsToSave(true) && setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 'messageDiv', false, '', function() {setFieldsToSave(false);}, '', false, '', true);
					return false;
				}
				break;
			}
			case BOLLA_RESA: {	
				if ($("#dataTipoBolla").val().split("|")[2] == "true" && $("#BollaResaForm input:hidden[name^=modificato][value=true]").length > 0) {
					if (!validateFields('BollaResaForm')) {
						$.alerts.dialogClass = "style_1";
	    				jAlert(campiBollaInvalidiError, attenzioneMsg, function() {
	    					$.alerts.dialogClass = null;
	    					if ((typeof firstInvalidId != 'undefined') && firstInvalidId != '') {
	    						$("#" + firstInvalidId.replace(/([|@])/g,'\\\$1')).focus().select();
	    					}
	    					return false;
	    				});
	    				return false;
					} 
					
					ray.ajax();
					saveBollaResaAuto();
					//setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_save.action', '', 'messageDiv', false, '', function() {setFieldsToSave(false); deleteEmptyRowsFuoriVoce();}, '', false, '', true);
					return false;
				}
				break;
			}
		}
	};
}

function showPromemoria() {
	dojo.xhrGet({
		url: appContext + namespace + "/pubblicazioniRpc_hasPromemoria.action",	
		preventCache: true,
		handleAs: "json",				
		handle: function(data,args) {
			if (args.xhr.status == 200) {
				if (typeof(data.error) !== 'undefined' && data.error != '') {
    				$.alerts.dialogClass = "style_1";
    				jAlert(data.error, attenzioneMsg, function() {
    					$.alerts.dialogClass = null;
    					return false;
    				});
				} else {
					if (data != null && data.hasPromemoria != null && data.hasPromemoria.toString() == 'true') {
						$('#imgPromemoria').css({'visibility':'visible'});
						$('#imgPromemoria').tooltip({
							delay: 0,  
						    showURL: false
						}); 
						$('#imgPromemoria').click(function() {
							openDiv('popup_name', 650, 480, appContext + namespace + "/promemoria_showFirstPromemoria.action");
						});
						function pulsateImgPromemoria() { 
						    $("#imgPromemoria"). 
						      animate({opacity: 0.2}, 500, 'linear'). 
						      animate({opacity: 1}, 500, 'linear', pulsateImgPromemoria); 
						} 
						pulsateImgPromemoria(); 	
					} else {
						$('#imgPromemoria').css({'visibility':'hidden'});
					}
				}
			} else {
				jAlert(msgErroreInvioRichiesta);
			}
		}
	});
}

function highPriorityMessagesCheck(interval) {
	this.StartPolling = function(){
		this.StopPolling();
		this.timer = setInterval(poll, interval);
	};
	this.StopPolling = function(){
		clearInterval(this.timer);
	};
	this.setPollInterval= function(i) {
		interval = i;
	};
	function poll() {
		if (generalRpcService) {
			var def = generalRpcService.getPkHighPriorityMessage();
			def.addCallback(function(data) {
				var $div = $("#parentMessageDiv");
				if ($div.length == 0 || $div.is(":hidden")) {
					if (data != '' && data.priorita != '' && Number(data.priorita) === 2) {
						showAlertMessaggioAltaPriorita(data.pk, data.priorita);
					} else if (data != '' && data.priorita != '' && Number(data.priorita) === 3) {
						showAlertMessaggioMassimaPriorita(data.pk, data.priorita);
					}
				}
			});
		}
	};
}

function getNoteRivendita(noteRivendita, noteRivenditaCpu) {
	return noteRivendita.length > 0 ? noteRivendita : (typeof(noteRivenditaCpu) != 'undefined' && noteRivenditaCpu.length > 0) ? noteRivenditaCpu : msgNoteRivendita;
}

function showAlertMessaggioAltaPriorita(messaggioNonLetto, priorita) {
	var u = appContext + namespace + '/avviso_showAvvisoNuovoMessaggio.action?messagePk=' + messaggioNonLetto + '&priorita=' + priorita;
	var div = $("<div id='parentMessageDiv' class='new_message_popup_block'>");
	div.css({'width':'150px','height':'120px','text-align':'center','margin-left':'43%','margin-right':'auto','font-weight':'bold','font-size':'100%','position':'absolute','border':'1px solid black','z-index':'999999'});
	$('body').prepend(div);
	div.load(u, {limit: 25}, function(){
		div.slideDown('slow');
	});
}

function showAlertMessaggioMassimaPriorita(messaggioNonLetto, priorita) {
	var u = appContext + namespace + '/avviso_showAvvisoNuovoMessaggio.action?messagePk=' + messaggioNonLetto + '&priorita=' + priorita;
	var div = $("<div id='parentMessageDiv' class='new_message_popup_block' style='width:200px; height:180px; top:250px; text-align:center; font-size:150%; border:1px solid black; z-index:999999'>");
	$('body').prepend(div);
	div.load(u, {limit: 25}, function(){
		$('#overlay').fadeIn('fast');
		div.fadeIn('slow');
	});
}

function showPromptLivellamenti(listIdLivellamenti) {
	var u = appContext + namespace + '/avvisoLivellamenti_showAvvisoLivellamenti.action?listIdLivellamenti=' + listIdLivellamenti;
	var div = $("<div id='parentMessageDiv' class='new_message_livellamenti_popup_block'>");
	div.css({'width':'150px','height':'160px','text-align':'center','margin-left':'43%','margin-right':'auto','font-weight':'bold','font-size':'100%','position':'absolute','border':'1px solid black','z-index':'999999'});
	$('body').prepend(div);
	div.load(u, {limit: 25}, function(){
		div.fadeIn('slow');
	});
}
/*
function showPromptAccettazioneLivellamenti(listIdLivellamentiAccettati) {
	var u = appContext + namespace + '/avvisoLivellamenti_showAvvisoAccettazioneLivellamenti.action?listIdLivellamenti=' + listIdLivellamentiAccettati;
	var div = $("<div id='parentMessageDiv' class='new_message_accettazione_livellamenti_popup_block'>");
	div.css({'width':'150px','height':'160px','text-align':'center','margin-left':'43%','margin-right':'auto','font-weight':'bold','font-size':'100%','position':'absolute','border':'1px solid black','z-index':'999999'});
	$('body').prepend(div);
	div.load(u, {limit: 25}, function(){
		div.fadeIn('slow');
	});
}
*/
function showPromptAccettazioneLivellamenti(messaggioNonLetto, priorita) {
	var u = appContext + namespace + '/avviso_showAvvisoNuovoMessaggio.action?messagePk=' + messaggioNonLetto + '&priorita=' + priorita;
	var div = $("<div id='parentMessageDiv' class='new_message_accettazione_livellamenti_popup_block'>");
	div.css({'width':'150px','height':'120px','text-align':'center','margin-left':'43%','margin-right':'auto','font-weight':'bold','font-size':'100%','position':'absolute','border':'1px solid black','z-index':'999999'});
	$('body').prepend(div);
	div.load(u, {limit: 25}, function(){
		div.slideDown('slow');
	});
}


function getProgressivo(tab) {
	var prog = 0;
	var maxProg = 0;
	tab.store.fetch({ query: { },  
        onItem: function(item) {
        	prog = Number(item.progressivo);
        	if (prog > maxProg) {
        		maxProg = prog;
        	}
        }
	});
	var max = Number(maxProg) + 1;
	return max;
}

function populateTablePubblicazioni(bean, tableId) {
	var tab = dijit.byId(tableId);
	var data = {identifier: "idtn", items: bean};
	var dataStore = new dojo.data.ItemFileWriteStore({ data: data });
	tab.setStore(dataStore);
	tab.store = dataStore;
	$("#" + tableId + " tr").each(function() {
		$tr = $(this);
		$tr.css({"cursor":"pointer"});
		$tr.mouseover(function() {
			$(this).addClass("highlightVendite");
		});  
		$tr.mouseout(function() {
			$(this).removeClass("highlightVendite");
		});  					
	});				    	    
}	

onSelectPubblicazioniBarcodeTable = function(inputTextId, event) {		
	var tab = dijit.byId('pubblicazioniTableBarcode');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	var barcode = $('#' + inputTextId).val();
	jConfirm(msgConfirmAssociazioneBarcode.replace('{0}', barcode).replace('{1}', selectedBean.titolo).replace('{2}', selectedBean.numeroCopertina).replace('{3}', selectedBean.dataUscitaFormat), attenzioneMsg, function(r) {
	    if (r) {
	    	var deferred = service.associaBarcodePubblicazione(Number(selectedBean.idtn), Number(selectedBean.coddl), barcode.toString());		
			deferred.addCallback(function(bean) {
				if (bean.type == 'EXCEPTION') {
					$.alerts.dialogClass = "style_1";
					jAlert(bean.result, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#' + inputTextId).val("");
						if ($('#close').length > 0) {
							$('#close').trigger('click');
						}
				    	return false;
					});
				} else if (bean.type == 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE') {
					doAssociaBarcode(bean, dijit.byId("venditeTable"));
				} else {
					jAlert(bean.result, informazioneMsg, function() {
						$("#aggiornaBarcode").attr("checked", false);
						$("#aggiornaBarcode").trigger("change");
						if (typeof(doInputTextEnterKeyPress) === 'function') {
							doInputTextEnterKeyPress();
						}
						if ($('#close').length > 0) {
							$('#close').trigger('click');
						}
						$('#' + inputTextId).val("");
						$('#' + inputTextId).focus();
				    	return false;
					});
				}
			});	
	    } else {
	    	$('#' + inputTextId).val("");
	    	if ($('#close').length > 0) {
	    		$('#close').trigger('click');
	    	}
	    }
	    $("#aggiornaBarcode").attr("checked", false);
	    $("#aggiornaBarcode").trigger("change");
	    return false;
	}, true, false);
};

onSelectPubblicazioniRichiediBarcodeTable = function(inputTextId, event) {		
	var tab = dijit.byId('pubblicazioniTableRichiediBarcode');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	jConfirm(msgConfirmInviaRichiestaAssociazioneBarcode.replace('{0}', $('#' + inputTextId).val()).replace('{1}', selectedBean.titolo).replace('{2}', selectedBean.numeroCopertina).replace('{3}', selectedBean.dataUscitaFormat), attenzioneMsg, function(r) {
	    if (r) {
	    	var deferred = service.sendRequestAggiornaBarcode(Number(selectedBean.idtn), Number(selectedBean.coddl), $('#' + inputTextId).val().toString());		
			deferred.addCallback(function(bean) {
				if (bean.type == 'EXCEPTION') {
					$.alerts.dialogClass = "style_1";
					jAlert(bean.result, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				} else {
					jAlert(bean.result, informazioneMsg, function() {
				    	return false;
					});
				}
				$('#' + inputTextId).val("");
				if ($('#close').length > 0) {
					$('#close').trigger('click');
				}
				return false;
			});	
	    } else {
	    	$('#' + inputTextId).val("");
	    	if ($('#close').length > 0) {
	    		$('#close').trigger('click');
	    	}
	    }
	    $("#aggiornaBarcode").attr("checked", false);
	    $("#aggiornaBarcode").trigger("change");
	    return false;
	}, true, false);
};

function doAssociaBarcode(bean, venditeTableObject, callback, dataBolla, tipoBolla) {
	PlaySound('beep3');
	jConfirm(bean.exceptionMessage, attenzioneMsg, function(r) {
	    if (r) {
	    	setTimeout(function() {
		    	jPrompt(msgInserisciTitolo, null, msgAggiornamentoBarcode, function(res) {
					if (res != null && res.length > 0) {
						var prog = typeof(venditeTableObject) !== 'undefined' && venditeTableObject != null ? getProgressivo(venditeTableObject) : 0;
						var pars = (typeof(dataBolla) !== 'undefined' && typeof(tipoBolla) !== 'undefined') ? {inputText:res, soloPubblicazioniBarcodeNullo:true, progressivo:prog, mostraTutteUscite:false, dataBolla:dataBolla, tipoBolla:tipoBolla} : {inputText:res, soloPubblicazioniBarcodeNullo:true, progressivo:prog, mostraTutteUscite:false};
						var def = service.getRows(pars);		
						def.addCallback(function(b) {
							if (b.type == 'EXCEPTION') {							
								$.alerts.dialogClass = "style_1";
								jAlert(b.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
									unBlockUI();
									$.alerts.dialogClass = null;
									if (typeof(callback) === 'function') {
										callback();
									}
								});
							} else {
								showDialog(bean.type == 'CONFIRM_ASSOCIA_BARCODE' ? 'dialogContentBarcode' : (bean.type == 'VENDITE_TITOLO' ? 'dialogContent' : 'dialogContentRichiediBarcode'));
								var tabId = bean.type == 'CONFIRM_ASSOCIA_BARCODE' ? 'pubblicazioniTableBarcode' : (bean.type == 'VENDITE_TITOLO' ? 'pubblicazioniTable' : 'pubblicazioniTableRichiediBarcode');
								populateTablePubblicazioni(b.result, tabId);
							}
						});
					} else {
						if (typeof(callback) === 'function') {
							callback();
						}
					}
				});
	    	}, 100);
	    } else {
	    	if (typeof(callback) === 'function') {
				callback();
			}
	    	return false;
	    }
	}, true, false);
}

function addNote(titolo, idtn, cpu) {
	var noteCpu = $("#noteRivenditaCpu" + cpu).val();
	var noteIdtn = $("#noteRivendita" + idtn).val();
	var isFissa = typeof(noteCpu) != 'undefined' && noteCpu.trim().length > 0;
	var noteRivendita = typeof(noteIdtn) != 'undefined' && noteIdtn.trim().length > 0 ? noteIdtn : noteCpu;
	jPromptNote(msgNotePubblicazione.replace("{0}", titolo), noteRivendita, isFissa, msgNoteRivendita, msgNoteFisse, function(res) {
		var cpu1 = cpu;
		if (res != null) {
			ray.ajax();
			var note = res.split("|")[0].trim();
			var notaFissa = res.split("|")[1].trim();
			dojo.xhrGet({
				url: appContext + namespace + "/pubblicazioniRpc_saveNotaRivendita.action?idtn=" + idtn + "&notaRivendita=" + escape(note) + "&cpu=" + cpu1 + "&notaFissa=" + notaFissa,	
				preventCache: true,
				handleAs: "text",				
				handle: function(data,args) {
					
					//ticket 0000279
					unBlockUI();
					
					
					var noteImg = $("#noteRiv" + idtn);
					$("#noteRivenditaCpu" + cpu1).val((notaFissa == "true") ? note : '');
					$("#noteRivendita" + idtn).val((notaFissa == "false") ? note : '');
					noteImg.attr("title", (note.length > 0) ? note : msgNoteRivendita);
					noteImg.attr("src", "/app_img/" + ((note.trim().length == 0) ? "note_rivendita.gif" : "note_rivendita_red.gif"));
					noteImg.tooltip({
                        delay: 0,
                        showURL: false
                    });
					var field = noteImg.closest("td").prev("td").find("input:text").first();
					setTimeout(function() {
						if ($("#barcode").length > 0) {
							field.keydown(function(evt) {
								var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
								if (keycode == '13') { 
									$("#barcode").focus();
								}
							});
						}
						field.focus();
					}, 100);
				}
			});
		}
	});
}

function openOrdini(idtn) {
 	var url = appContext + "/bollaRivendita_showOrdini.action?idtn=" + idtn;
 	//openDiv('popup_name', 900, 350, url);
 	openDiv('popup_name_ordini', 900, 350, url);
	return false;
}

function disableImagesForPromo(context) {
	var $imgs;
	if (typeof context != 'undefined' && context != '') {
		//$imgs = $("img[src='/app_img/camera.gif'],img[src='/app_img/table/xls.gif']", context);
		$imgs = $("img[src='/app_img/camera-active.png'],img[src='/app_img/table/xls.gif']", context);
	} else {
		//$imgs = $("img[src='/app_img/camera.gif'],img[src='/app_img/table/xls.gif']");
		$imgs = $("img[src='/app_img/camera-active.png'],img[src='/app_img/table/xls.gif']");
	}
	$imgs.each(function() {
		var $this = $(this);
		if ($this.closest("a")) {
			$this.unwrap().css({"cursor":"pointer"}).unbind('click').click(function() {
				showActionNotAllowedMsg();
			});
		}
	});
}

function networkDetection(theUrl, interval) {
	online = false;
	this.StartPolling = function(){
		this.StopPolling();
		this.timer = setInterval(poll, interval);
	};
	this.StopPolling = function(){
		clearInterval(this.timer);
	};
	this.setPollInterval= function(i) {
		interval = i;
	};
	this.getOnlineStatus = function(){
		return online;
	};
	function poll() {
		var dataBolla = $("#dataTipoBolla").val().split("|")[0];
		var tipoBolla = $("#dataTipoBolla").val().split("|")[1].replace("Tipo","").trim();
		try {
			dojo.xhrGet({
				url: theUrl + "?codPagina=" + codPagina + "&time=" + $("#time").val() + "&dataBolla=" + dataBolla + "&tipoBolla=" + tipoBolla,
				handleAs: "json",	
				headers: { "Content-Type": "application/json; charset=uft-8"}, 
				preventCache: true,
				load: function(data,args) {	
					if (typeof(data) !== 'undefined') {
						if (typeof(data.pubb) !== 'undefined') {
							var form = codPagina == COD_PAGINA_BOLLA_CONSEGNA ? "BollaControlloForm" : "BollaResaForm";
				            for (var key in data.pubb) {
				            	$("#" + form + " input[type='hidden'][value='" + data.pubb[key] + "']").attr("id", key);
				            }
						} 
						if (typeof(data.time) !== 'undefined') {
							$("#time").val(data.time);
						}
					}
					pingFailureCount = 0;
					online = true;
				},
				error: function() {
					if (pingFailureCount >= 3) {
						$.alerts.dialogClass = "style_1";
						jAlert(userOfflineMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							pingFailureCount = 0;
						});
					}
					online = false;
					pingFailureCount++;
				}
		    });
		} catch (e) {
			dojo.io.bind ({ 
			   url: theUrl + "?codPagina=" + codPagina + "&time=" + $("#time").val() + "&dataBolla=" + dataBolla + "&tipoBolla=" + tipoBolla,
			   load: function (type, data, evt) { 
				    if (typeof(data) !== 'undefined') {
						if (typeof(data.pubb) !== 'undefined') {
							var form = codPagina == COD_PAGINA_BOLLA_CONSEGNA ? "BollaControlloForm" : "BollaResaForm";
				            for (var key in data.pubb) {
				            	$("#" + form + " input[type='hidden'][value='" + data.pubb[key] + "']").attr("id", key);
				            }
						} 
						if (typeof(data.time) !== 'undefined') {
							$("#time").val(data.time);
						}
					}
				    pingFailureCount = 0;
				    online = true; 
			   }, 
			   error: function(type, data, evt) {
				   if (pingFailureCount >= 3) {
						$.alerts.dialogClass = "style_1";
						jAlert(userOfflineMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							pingFailureCount = 0;
						});
				   }
				   online = false;
				   pingFailureCount++;
				},
			   mimetype: "application/json" 
			});
		}
	};
}

(function($) {
    $.fn.hasScrollBar = function() {
    	var scrollHeight = 0;
    	var scrollCorrection = 0;
    	if (getBrowser().indexOf("OPERA") != -1) {
    		scrollCorrection = 40; 
    	}
    	try {
    		scrollHeight = this.get(0).scrollHeight - scrollCorrection;
    	} catch (e) {
    		scrollHeight = this.scrollHeight - scrollCorrection;
    	}
        return scrollHeight > this.height();
    };
    
    $.fn.border_color_animate = function(color, time, callback) {
    	var obj = {};
    	$(["Left", "Right", "Top", "Bottom"]).each(function() {
    		obj["border"+this+"Color"] = color;
    	});
    	this.animate(obj, time, callback);
    };
    
    $.fn.sort_select_box = function(){
        var my_options = $("#" + this.attr('id') + ' option');
        my_options.sort(function(a,b) {
            if (a.text > b.text) return 1;
            else if (a.text < b.text) return -1;
            else return 0;
        });
       return my_options;
    };
    
    $.fn.reset = function (fn) {
    	return fn ? this.bind("reset", fn) : this.trigger("reset");
	};
	
    $.fn.center = function () {
        this.css("position","absolute");
        this.css("top", ( $(window).height() - this.height() ) / 2+$(window).scrollTop() + "px");
        this.css("left", ( $(window).width() - this.width() ) / 2+$(window).scrollLeft() + "px");
        return this;
    };
})(jQuery);

/*==============================================================================

                                 Rich Calendar 1.0
                                 =================
                    Copyright (c) 2007-2008 Vyacheslav Smolin


Author:
-------
Vyacheslav Smolin (http://www.richarea.com, http://html2xhtml.richarea.com,
re@richarea.com)

About the script:
-----------------
Rich Calendar is 100% JavaScript calendar script. No pop-up windows.
Skinnable and multilingual. Multiple calendar instances on one page.
Allows to embed calendar objects in html document or position them absolutely
using flexible horizontal and vertical alignment options.
Supports any date formats. Language dependant date formats. User-defined
behaviour possible.
Could be associated with an element (eg text field) to read/write date from/to.
Pop-up mode (calendar closes on mouse click outside it).

Requirements:
-------------
Rich Calendar works in IE, Mozilla-based browsers such as Firefox, Opera 9+,
and Safari 3.0.

Usage:
------
Please see example.html.

Demo:
-----
http://www.richarea.com/demo/rich_calendar/

License:
--------
Free. Copyright information must stay intact.
We'd appreciate if you place a direct link to us somewhere on your site.

==============================================================================*/

// Rich Calendar
RichCalendar = function(target_obj, show_time) {

	// value
	this.value = '';

	// format
	this.format = '%Y-%m-%d';

	// Week Day to start with (0 - Sunday, 1 - Monday, etc...)
	this.start_week_day = 1;

	// iframe object to show calendar object in
	this.iframe_obj = null;

	// path to calendar css and js files
	this.lib_path = 'rich_calendar/';

	// DOM object to take/set date from/to
	this.target_obj = target_obj;

	// show time
	this.show_time = show_time;

	// function called when calendar value changes
	this.user_onchange_handler = null;

	// function called when data choice is cancelled
	this.user_onclose_handler = null;

	// function called when mouse clicked outside calendar with auto_close set
	// to true after it is closed
	this.user_onautoclose_handler = null;

	// default language
	this.default_lang = 'it';

	// language
	this.language = 'it';

	// current date
	this.date = new Date();
/*
this.date.setFullYear(2008);
this.date.setMonth(1);
this.date.setDate(29);
*/
//this.date.setMonth(11);
//this.date.setDate(31);

	// calendar skin name
	this.skin = '';

	// calendar closes automatically on click outside it
	this.auto_close = true;

	// element which value is taken to initilize calendar and where calendar
	// returns date if user defined function to return date is not specified
	this.value_el = null;

	// specifies calendar positioning - absolute by default
	this.position = null;

}

RichCalendar.is_ie = /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent);

// Static functions
RichCalendar.get_iframe_styles = function() {
var i;
var j;

	var styles = document.styleSheets;
	var sheets_num = styles.length;

	var style_text = '';
    for (i=0; i<sheets_num; i++) {
		if (RichCalendar.is_ie) {
			// take all calendar styles in IE as cannot take text of each rule
			if (/rich_calendar(.*).css$/.test(styles[i].href)) {
				style_text += styles[i].cssText;
				break;
			}
		} else {
			var rules = null;
			try {
				if (RichCalendar.is_ie) {
					rules = styles[i].rules;
				} else {
					rules = styles[i].cssRules;
				}
			} catch(error) {
				continue;
			}

			if (rules != null) {
				var rules_num = rules.length;

				for (j=0; j<rules_num; j++) {
					var rule_value = rules[j].selectorText;
					if (/rc_iframe/.test(rule_value)) {
						style_text += rules[j].cssText;
					}
				}
			}
		}
	}
	return style_text;
}


RichCalendar.attach_event = function(obj, event, handler) {

	if (obj.addEventListener) {
		obj.addEventListener(event, handler, false);
	} else {
		if (obj.attachEvent) {
			obj.attachEvent('on'+event, handler);
		}
	}
}


RichCalendar.detach_event = function(obj, event, handler) {

	if (obj.removeEventListener) {
		obj.removeEventListener(event, handler, false);
	} else {
		if (obj.detachEvent) {
			obj.detachEvent('on'+event, handler);
		}
	}
}


// add event handlers to object obj
RichCalendar.attach_events = function(obj) {
	RichCalendar.attach_event(obj, 'click', RichCalendar.onclick);
	RichCalendar.attach_event(obj, 'mouseover', RichCalendar.onmouseover);
	RichCalendar.attach_event(obj, 'mouseout', RichCalendar.onmouseout);
}


// remove event handlers set to object obj
RichCalendar.detach_events = function(obj) {
	RichCalendar.detach_event(obj, 'click', RichCalendar.onclick);
	RichCalendar.detach_event(obj, 'mouseover', RichCalendar.onmouseover);
	RichCalendar.detach_event(obj, 'mouseout', RichCalendar.onmouseout);
}


// calendar onclick event handler
RichCalendar.onclick = function(e) {
//alert(e + ' => ' + e.srcElement + ' => ' + e.target + ' => ' + window.event);
//for (var i in e) alert(i + ' => ' + e[i]);

var event = RichCalendar.get_event(e);
var obj = RichCalendar.get_target_object(e);

	if (!obj) return;

var cal = obj.calendar;

var cur_year = cal.date.getFullYear();
var cur_month = cal.date.getMonth();
var cur_day = cal.date.getDate();

//alert(obj.rc_object_code);
	switch (obj.rc_object_code) {
		case 'day':
//			alert(obj.day_num);
			cal.date.setDate(obj.day_num);
			break;
		case 'prev_year':
			// determine number of days in prev year
			cal.date.setDate(1);
			cal.date.setFullYear(cur_year-1);
			var month_days = RichCalendar.get_month_days(cal.date);

			// prevent jumping to next month
			if (cur_day > month_days) {
				cal.date.setDate(month_days);
			} else {
				cal.date.setDate(cur_day);
			}

			cal.show_date();
			break;
		case 'prev_month':
			// determine number of days in prev month
			cal.date.setDate(1);
			cal.date.setMonth(cur_month-1);
			var month_days = RichCalendar.get_month_days(cal.date);

			// prevent jumping to next month
			if (cur_day > month_days) {
				cal.date.setDate(month_days);
			} else {
				cal.date.setDate(cur_day);
			}

			cal.show_date();
			break;
		case 'next_month':
			// determine number of days in prev month
			cal.date.setDate(1);
			cal.date.setMonth(cur_month+1);
			var month_days = RichCalendar.get_month_days(cal.date);

			// prevent jumping to next month
			if (cur_day > month_days) {
				cal.date.setDate(month_days);
			} else {
				cal.date.setDate(cur_day);
			}

			cal.show_date();
			break;
		case 'next_year':
			// determine number of days in next year
			cal.date.setDate(1);
			cal.date.setFullYear(cur_year+1);
			var month_days = RichCalendar.get_month_days(cal.date);

			// prevent jumping to next month
			if (cur_day > month_days) {
				cal.date.setDate(month_days);
			} else {
				cal.date.setDate(cur_day);
			}

			cal.show_date();
			break;
		case 'today':
			var today = new Date();
			today.setHours(cal.date.getHours());
			today.setMinutes(cal.date.getMinutes());
			today.setSeconds(cal.date.getSeconds());

			cal.date = today;
			cal.show_date();

			break;
		case 'clear':
			// handle clear request
			if (cal.value_el) {
				cal.value_el.value = '';
			}
			break;
		case 'close':
			// handle close request
			cal.onclose_handler();
			break;
		case 'week_day':
//alert(obj.innerHTML);
			cal.start_week_day = obj.week_day_num;
			cal.show_date();
			break;
		default:
			break;
	}


	// handle close request
	if (obj.rc_object_code != 'week_day') {
		cal.onchange_handler(obj.rc_object_code);
	}

	// handle date change


	// hide all other auto closing calendars
	RichCalendar.hide_auto_close(cal);

}


// calendar onmouseover event handler
RichCalendar.onmouseover = function(e) {

//alert(e + ' => ' + e.srcElement + ' => ' + e.target + ' => ' + window.event);
//for (var i in e) alert(i + ' => ' + e[i]);

var event = RichCalendar.get_event(e);
var obj = RichCalendar.get_target_object(e);

	if (!obj) return;

var cal = obj.calendar;

var cur_year = cal.date.getFullYear();
var cur_month = cal.date.getMonth();
var cur_day = cal.date.getDate();

	switch (obj.rc_object_code) {
		case 'day':
			var date = new Date(cal.date);
			date.setDate(obj.day_num);
			cal.set_footer_text(cal.get_formatted_date(cal.text('footerDateFormat'), date));

			// highlight day cell and its row
			RichCalendar.add_class(obj, "rc_highlight");
			RichCalendar.add_class(obj.parentNode, "rc_highlight");

			break;
		case 'clear':
		case 'today':
		case 'close':
		case 'prev_year':
		case 'prev_month':
		case 'next_month':
		case 'next_year':
			cal.set_footer_text(cal.text(obj.rc_object_code));
			break;
		case 'week_day':
			if (obj.week_day_num != cal.start_week_day) {
				var day_names = cal.text("dayNames");
				var name = day_names[obj.week_day_num];
				var text = cal.text("make_first");
				text = text.replace("%s", name);
			} else {
				var text = cal.text('footerDefaultText');
			}
			cal.set_footer_text(text);
			break;
		default:
			cal.set_footer_text(cal.text('footerDefaultText'));
			break;
	}

}


// calendar onmouseout event handler
RichCalendar.onmouseout = function(e) {

//alert(e + ' => ' + e.srcElement + ' => ' + e.target + ' => ' + window.event);
//for (var i in e) alert(i + ' => ' + e[i]);

var event = RichCalendar.get_event(e);
var obj = RichCalendar.get_target_object(e);

	if (!obj) return;

var cal = obj.calendar;

	cal.set_footer_text(cal.text('footerDefaultText'));

	// un-highlight day cell and its row
	RichCalendar.remove_class(obj, "rc_highlight");
	RichCalendar.remove_class(obj.parentNode, "rc_highlight");

}


// document onmousedown event handler
RichCalendar.document_onmousedown = function(e) {
var event = RichCalendar.get_event(e);
var obj = RichCalendar.get_target_object(e);

	if (!obj) return;

var el = obj;
var cal = null;

	while (el) {
		if (el.className && el.className.match(/^rc_iframe_body/) &&
			el.tagName.toUpperCase() == 'BODY') {

			cal = el.calendar;
			break;
		}
		el = el.parentNode;
	}

	// close all not active calendars
	RichCalendar.hide_auto_close(cal);

}


// hide all calendars that should autoclose except cal and remove
// them from RichCalendar.active_calendars
RichCalendar.hide_auto_close = function(cal) {
var active_cals = [];
var i;

	for (i=0; i<RichCalendar.active_calendars.length; i++) {
		var cur_cal = RichCalendar.active_calendars[i];
		if (cur_cal.auto_close && cur_cal != cal) {

			cur_cal.hide();

			if (cur_cal.user_onautoclose_handler) {
				cur_cal.user_onautoclose_handler(this);
			}

		} else {
			active_cals[active_cals.length] = cur_cal;
		}
	}

	RichCalendar.active_calendars = active_cals;
}


// remove calendar cal from list RichCalendar.active_calendars of active
// calendars
RichCalendar.make_inactive = function(cal) {
var active_cals = [];
var i;

	for (i=0; i<RichCalendar.active_calendars.length; i++) {
		var cur_cal = RichCalendar.active_calendars[i];
		if (cur_cal != cal) {
			active_cals[active_cals.length] = cur_cal;
		}
	}

	RichCalendar.active_calendars = active_cals;
}


// returns event object
RichCalendar.get_event = function(e) {

	return e||window.event;

}


// returns event target object
RichCalendar.get_target_object = function(e) {

	return e.target?e.target:(e.srcElement?e.srcElement:window.event.srcElement);

}


// returns skin suffics for skin class name
RichCalendar.skin_suffix = function(skin) {
	return (skin != '')?('_' + skin):'';
}


// return number of days in month
RichCalendar.get_month_days = function(date, month) {
var year = date.getFullYear();

	if (month) {
		month = parseInt(month);
		if (month <= 0 || month > 11) month = null;
	}

	if (!month) {
		month = date.getMonth();
	}

	if (month==1 && RichCalendar.is_leap_year(year)) {
		return 29;
	} else {
//alert(month + ' -> ' + RichCalendar.month_days[month]);
		return RichCalendar.month_days[month];
	}

}


// return true if year is a leap year
RichCalendar.is_leap_year = function(year) {
	return (year%4==0 && year%100!=0 || year%400==0) ? true : false;
}


// return day of the year
RichCalendar.get_day_of_year = function(date) {
var now = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0);
var year_start = new Date(date.getFullYear(), 0, 0, 0, 0, 0);

// milliseconds in day
var day_in_msecs = 24*60*60*1000;

	return Math.floor((now - year_start) / day_in_msecs);

}


// add class to element
RichCalendar.add_class = function(el, class_name) {

	RichCalendar.remove_class(el, class_name);
	el.className += " " + class_name;

}


// remove class from element
RichCalendar.remove_class = function(el, class_name) {

	if (!el || !el.className) return

	var new_class_parts = [];
	var class_parts = String(el.className).split(" ");
	var i;
	for (i=0; i<class_parts.length; i++) {
		if (class_parts[i] != "" && class_parts[i] != class_name) {
			new_class_parts[new_class_parts.length] = class_parts[i];
		}
	}

	el.className = new_class_parts.join(" ");
}


// return position of object obj; dont go above stop_obj in DOM structure
RichCalendar.get_obj_pos = function(obj, stop_obj){
	var pos = Array(0,0);

	if (!obj) return pos;

	var iniObj = obj;

	while (obj && stop_obj != obj) {

		pos[0] += obj.offsetLeft;
		pos[1] += obj.offsetTop;

		if (obj != iniObj) {
			pos[0] += parseInt(RichCalendar.get_style(obj, "borderTopWidth"), 10) || 0;
			pos[1] += parseInt(RichCalendar.get_style(obj, "borderLeftWidth"), 10) || 0;
		}

		obj = obj.offsetParent;

	}


	var obj = iniObj;

	while (obj && stop_obj != obj && obj.tagName.toLowerCase() != 'body') {
		pos[0] -= obj.scrollLeft;
		pos[1] -= obj.scrollTop;

		obj = obj.parentNode;
	}

	return pos;
}

// return current style value
RichCalendar.get_style = function (el, name) {
var view = document.defaultView;

	if (view && view.getComputedStyle) {
		var st = view.getComputedStyle(el, "");
		return st[name];
	}

//		if (document.defaultView.getComputedStyle(obj, '').getPropertyValue('position') == 'absolute') break;

	var v;
	if (v = el.currentStyle) {
		return v[name];
	}

	if (v = el.style[name]) {
		return v;
	}

}

// array of text data in various languages
RichCalendar.rc_lang_data = [];

// number of days in months
RichCalendar.month_days = [31,28,31,30,31,30,31,31,30,31,30,31];

// currently shown calendars
RichCalendar.active_calendars = [];

// true if all document handlers are set
RichCalendar.handlert_set = false;


// Calendar API


// show calendar inside/before/after (defined by argument position) element el
// if any specified or in the point specified (if any)
RichCalendar.prototype.show = function(x, y, el, position) {

	if (!this.value_el) {
		this.value_el = el;
	}

	this.position = position;

	this.iframe_obj = document.createElement('IFRAME');
	this.iframe_obj.className = 'rc_calendar'+RichCalendar.skin_suffix(this.skin);
	this.iframe_obj.setAttribute('scrolling', 'no');
	this.iframe_obj.setAttribute('src','javascript:false;');
	this.iframe_obj.calendar = this;


	// relative positioning
	if (this.is_relative_position(position)) {
		switch (position) {
			case "before":
				if (el.parentNode) {
					el.parentNode.insertBefore(this.iframe_obj, el);
				}
				break;
			case "after":
				if (el.parentNode) {
					el.parentNode.insertBefore(this.iframe_obj, el.nextSibling);
				}
				break;
			case "child":
			default:
				el.appendChild(this.iframe_obj);
				this.position = 'child';
				break;
		}

	} else { // absolute positioning

		this.iframe_obj.style.position = 'absolute';

		// move the iframe to the position specified
		var left = parseInt(x);
		var top = parseInt(y);
		if (typeof(x) == 'number' && typeof(y) == 'number') {
			this.iframe_obj.style.left = x + 'px';
			this.iframe_obj.style.top = y + 'px';
		}
		this.iframe_obj.style.border = '1px solid #000000';
		this.iframe_obj.value = this.value;

		document.body.appendChild(this.iframe_obj);

	}

	// styles to add to iframe
	var iframe_styles = RichCalendar.get_iframe_styles();

	// put calendar content into the iframe
	var iframe_content = '' +
'<html>' +
'<head>' +
'<style type="text/css">'+iframe_styles+'</style>' +
'</head>' +
'<body class="rc_iframe_body' + RichCalendar.skin_suffix(this.skin) + '" id="rc_body">' +
'</body></html>' +
	'';


	this.iframe_doc = this.iframe_obj.contentWindow.document;
	this.iframe_doc.open();
	this.iframe_doc.write(iframe_content);
	this.iframe_doc.close();

	RichCalendar.attach_event(this.iframe_doc, 'mousedown', RichCalendar.document_onmousedown);



	this.body_obj = this.iframe_doc.getElementById('rc_body');
	this.body_obj.calendar = this;
	// main table
	this.table_obj = this.iframe_doc.createElement('TABLE');
	this.table_obj.className = 'rc_table';
	this.table_obj.setAttribute('id', 'rc_iframe_table');
	this.table_obj.cellSpacing = 0;
	this.table_obj.cellPadding = 0;
	// store reference to the calendar
	this.table_obj.calendar = this;

	// header row
	this.head_tr = this.table_obj.insertRow(0);
	this.head_tr.className = 'rc_head_tr';

	this.clear_td = this.head_tr.insertCell(0);
	this.clear_td.innerHTML = 'c';
	this.clear_td.rc_object_code = 'clear';
	this.clear_td.calendar = this;
	RichCalendar.attach_events(this.clear_td);

//this.clear_td.className = 'rc_head_tr';
	this.head_td = this.head_tr.insertCell(1);
//this.head_td.className = 'rc_head_tr';
	this.head_td.colSpan = 5;
//this.head_td.innerHTML = 'asdf';
	this.close_td = this.head_tr.insertCell(2);
	this.close_td.innerHTML = 'x';
	this.close_td.rc_object_code = 'close';
	this.close_td.calendar = this;
	RichCalendar.attach_events(this.close_td);
//this.close_td.className = 'rc_head_tr';

	// navigation row
	this.nav_tr = this.table_obj.insertRow(1);
	this.nav_tr.className = 'rc_nav_tr';

	this.prev_year_td = this.nav_tr.insertCell(0);
	this.prev_year_td.innerHTML = '&#x00ab;';
	this.prev_year_td.rc_object_code = 'prev_year';
	this.prev_year_td.calendar = this;
	RichCalendar.attach_events(this.prev_year_td);

	this.prev_month_td = this.nav_tr.insertCell(1);
	this.prev_month_td.innerHTML = '&#x2039;';
	this.prev_month_td.rc_object_code = 'prev_month';
	this.prev_month_td.calendar = this;
	RichCalendar.attach_events(this.prev_month_td);

	this.today_td = this.nav_tr.insertCell(2);
	this.today_td.colSpan = 3;
	this.today_td.innerHTML = this.text('today');
	this.today_td.rc_object_code = 'today';
	this.today_td.calendar = this;
	RichCalendar.attach_events(this.today_td);

	this.next_month_td = this.nav_tr.insertCell(3);
	this.next_month_td.innerHTML = '&#x203a;';
	this.next_month_td.rc_object_code = 'next_month';
	this.next_month_td.calendar = this;
	RichCalendar.attach_events(this.next_month_td);

	this.next_year_td = this.nav_tr.insertCell(4);
	this.next_year_td.innerHTML = '&#x00bb;';
	this.next_year_td.rc_object_code = 'next_year';
	this.next_year_td.calendar = this;
	RichCalendar.attach_events(this.next_year_td);

	// weekdays row
	this.wd_tr = this.table_obj.insertRow(2);
	this.wd_tr.className = 'rc_wd_tr';

	var i;
//	var day_names = this.text('dayNamesShort');
	for (i=0; i<7; i++) {
//		var wd = (i+this.start_week_day)%7;

		var td = this.wd_tr.insertCell(i);
		td.rc_object_code = 'week_day';
		td.calendar = this;
		RichCalendar.attach_events(td);
//		td.innerHTML = day_names[wd];

//		if (typeof(weekend_days[wd]) != "undefined") {
//			td.className = "rc_weekend_head";
//		}
	}


	// calendar rows (initially create min number of rows necessary - 4)
	var rows_num = 4;
	var row_indx;
	var cell_indx;
	this.cal_tr = [];

	for (row_indx=0; row_indx<rows_num; row_indx++) {
		this.create_cal_row(row_indx);
/*
		this.cal_tr[row_indx] = this.table_obj.insertRow(3+row_indx);
		this.cal_tr[row_indx].className = 'rc_cal_tr';

		for (cell_indx=0; cell_indx<7; cell_indx++) {
			var td = this.cal_tr[row_indx].insertCell(cell_indx);
			td.innerHTML = row_indx + '-' + cell_indx;
		}
*/
	}


	if (this.show_time) {
		// create time row if necessary
		this.time_tr = this.table_obj.insertRow(rows_num+3);
		this.time_tr.className = 'rc_time_tr';
		var td = this.time_tr.insertCell(0);
		td.colSpan = 2;
		td.innerHTML = this.text('time') + ':';
	
		var td = this.time_tr.insertCell(1);
		td.colSpan = 3;

		this.hours_obj = this.createElement('INPUT', td);
		this.hours_obj.className = 'rc_hours';
		this.hours_obj.setAttribute('size', 1);
		this.hours_obj.setAttribute('maxlength', 2);

		this.colon_span = this.createElement('SPAN', td);
		this.colon_span.className = 'rc_colon_span';
		this.colon_span.innerHTML = '&nbsp;:&nbsp;';

		this.mins_obj = this.createElement('INPUT', td);
		this.mins_obj.className = 'rc_mins';
		this.mins_obj.setAttribute('size', 1);
		this.mins_obj.setAttribute('maxlength', 2);
	
		var td = this.time_tr.insertCell(2);
		td.colSpan = 2;
		td.innerHTML = '&nbsp;';
	}

	// footer row

	this.footer_tr = this.table_obj.insertRow(rows_num+3+(this.show_time?1:0));
	this.footer_tr.className = 'rc_footer_tr';
	this.footer_td = this.footer_tr.insertCell(0);
	this.footer_td.colSpan = 7;
	this.footer_td.innerHTML = this.text('footerDefaultText');


	this.body_obj.appendChild(this.table_obj);

	// create a DIV element to determine size of calendar
	this.size_div = document.createElement('DIV');
	this.size_div.className = this.body_obj.className;
	this.size_div.style.position = "absolute";
	this.size_div.style.left = "-1000px";
	this.size_div.style.top = "-1000px";
	document.body.appendChild(this.size_div);


	// show current date in calendar
	this.show_date();


	// set document handlers if not set yet
	if (!RichCalendar.handlers_set) {
		RichCalendar.attach_event(document, 'mousedown', RichCalendar.document_onmousedown);
		RichCalendar.handlers_set = true;
	}

	// store this calendar in array of active calendars
	RichCalendar.active_calendars[RichCalendar.active_calendars.length] = this;

//alert(this.body_obj.innerHTML);
}


// hide calendar (destroy iframe object)
RichCalendar.prototype.hide = function() {
	if (this.iframe_obj) {
		this.iframe_obj.parentNode.removeChild(this.iframe_obj);
		this.iframe_obj = null;
	}

	RichCalendar.make_inactive(this);
}


// show calendar inside/before/after (defined by argument position) element el
// ie relative to element el or 
RichCalendar.prototype.show_at_element = function(el, position) {

	if (typeof(el) != "object" || !el) return;

	// relative positioning
	if (this.is_relative_position(position)) {
		this.show(null, null, el, position);
		return;
	}
/*
	switch (position) {
		case "before":
		case "after":
		case "child":
			this.show(null, null, el, position);
			return;
		default:
			break;
	}
*/

	// absolute positioning
	var el_pos = RichCalendar.get_obj_pos(el);
	// negative coordinates to make calendar invisible for a while
	// as cannot determine right coordinates right now (calendar size is not
	// known yet right after this.show worked)
	var x = -1000;
	var y = -1000;

	this.show(x, y, el, position);


	// fix position (need to do this later then calendar is shown as
	// size of calendar could change in this.show(x, y)
//	var cal = this;
//	window.setTimeout(function(){cal.fix_position(el, position)}, 5);

}

// fix position of calendar
RichCalendar.prototype.fix_position = function(el) {
var position = this.position;

	if (this.is_relative_position(position)) {
		return;
	}

	if (!el) {
		el = this.value_el;
	}

//	alert(el.getAttribute("id") + " => " + position);

	var aligns = String(position).split("-");
	if (aligns.length == 2) {

		var el_pos = RichCalendar.get_obj_pos(el);
//alert(el_pos + ' => ' + el.offsetHeight);
		var x = el_pos[0];
		var y = el_pos[1] + el.offsetHeight;

		// iframe border thikness
		var border_width = parseInt(this.iframe_obj.style.borderWidth);

		var cal_width = parseInt(this.iframe_obj.width) + 2*border_width;
		var cal_height = parseInt(this.iframe_obj.height) + 2*border_width;

//alert('!: ' + cal_width + ' => ' + cal_height);
		// horizontal alignment
		switch (aligns[0]) {
			case "left":
				x -= cal_width;
				break;
			case "center":
				x += (el.offsetWidth - cal_width) / 2;
				break;
			case "right":
				x += el.offsetWidth;
				break;
			case "adj_right":
				x += el.offsetWidth - cal_width;
				break;
			default:
				break;
		}

		// vertical alignment
		switch (aligns[1]) {
			case "top":
				y -= el.offsetHeight + cal_height;
				break;
			case "center":
				y += (el.offsetHeight - cal_height) / 2 - el.offsetHeight;
				break;
			case "bottom":
				break;
			case "adj_bottom":
				y -= cal_height;
				break;
			default:
				break;
		}

		this.iframe_obj.style.left = x + 'px';
		this.iframe_obj.style.top = y + 'px';

		this.iframe_obj.style.visibility = 'visible';
	}

}


// return true if calendar is relatively positioned
RichCalendar.prototype.is_relative_position = function(position) {
	switch (position) {
		case "before":
		case "after":
		case "child":
			return true;
		default:
			return false;
	}
}


// creates an element in iframe
RichCalendar.prototype.createElement = function(tagName, parent) {

var el = this.iframe_doc.createElement(tagName);

	if (parent) {
		parent.appendChild(el);
	}

	return el;
}


// return text data desired
RichCalendar.prototype.text = function(name, language) {

	if (typeof(language) == "undefined") {
		language = this.language;
	}

	if (typeof(RichCalendar.rc_lang_data[language]) != "undefined") {
		return typeof(RichCalendar.rc_lang_data[language][name]) != "undefined"?RichCalendar.rc_lang_data[language][name]:'';
	}

	return (typeof(this.default_language) !== "undefined" && typeof(RichCalendar.rc_lang_data[this.default_language][name]) != "undefined")?RichCalendar.rc_lang_data[this.default_language][name]:'';

}

// show date in calendar
RichCalendar.prototype.show_date = function() {
	// update week days row

	// numbers of weekend days
	var weekend_days = this.get_weekend_days();

	var i;
	var day_names = this.text('dayNamesShort');
	for (i=0; i<7; i++) {
		var wd = (i+this.start_week_day)%7;

		var td = this.wd_tr.cells[i];
		td.innerHTML = day_names[wd];

		if (typeof(weekend_days[wd]) != "undefined") {
			td.className = "rc_weekend_head";
		} else {
			td.className = "";
		}

//		td.rc_object_code = 'week_day';
//		td.calendar = this;
		td.week_day_num = wd;
//		RichCalendar.attach_events(td);
	}


var month_days = RichCalendar.get_month_days(this.date);
//	alert(month_days);

// first day of the same month and year as this.date
var date = new Date(this.date);
	date.setDate(1);
var week_day = (date.getDay()+7-this.start_week_day)%7+1;
//	alert(week_day);

// current data
var cur_year = this.date.getFullYear();
var cur_month = this.date.getMonth();
var cur_day = this.date.getDate();
//alert(cur_year + ' => ' + cur_month + ' => ' + cur_day);

// today
var today = new Date();
var today_year = today.getFullYear();
var today_month = today.getMonth();
var today_day = today.getDate();

// 

	var month_names = this.text('monthNames');
	this.head_td.innerHTML = month_names[cur_month] + ', ' + cur_year;


	var row;
	var day;
	var days = 0;
	var last_row;
	for (row=0; row<6; row++) {

		// all days are shown => just check if need to remove unused rows
		if (days == month_days) {
			if (this.cal_tr[last_row+1]) {
				this.cal_tr[last_row+1].parentNode.removeChild(this.cal_tr[last_row+1]);
				this.cal_tr[row] = null;
			}
			continue;
		}

		for (day=0; day<7; day++) {

			if (!this.cal_tr[row]) {
				this.create_cal_row(row);
			}

			var cur_tr = this.cal_tr[row];
			var cell = cur_tr.cells[day];
			cell.className = "";
			// should remove or IE attach the same event several times
			RichCalendar.detach_events(cell);

			if (row==0 && day+1 < week_day || days == month_days) {
				var td_text = '&nbsp;';

//				RichCalendar.detach_events(cell);
			} else {
				var day_num = days+1;
				var td_text = day_num;
				days++;

				cell.rc_object_code = 'day';
				cell.day_num = day_num;
				cell.calendar = this;
				RichCalendar.attach_events(cell);

				// hilight current date
				if (cur_day == day_num) {
					RichCalendar.add_class(cell, "rc_current");
				}

				// hilight today date
				if (day_num == today_day &&
					cur_month == today_month &&
					cur_year == today_year) {
					RichCalendar.add_class(cell, "rc_today");
				}


				var wd = (day+this.start_week_day)%7;

				// hilight weekend days
				if (typeof(weekend_days[wd]) != "undefined") {
					RichCalendar.add_class(cell, "rc_weekend_day");
				} else {
					RichCalendar.remove_class(cell, "rc_weekend_day");
				}

			}
			cell.innerHTML = td_text;

			if (days == month_days) {
				last_row = row;
			}
		}
	}


	// set time
	if (this.show_time && this.hours_obj && this.mins_obj) {
		var hours = this.date.getHours();
		if (hours < 10) hours = '0' + hours;
		var mins = this.date.getMinutes();
		if (mins < 10) mins = '0' + mins;

		this.hours_obj.value = hours;
		this.mins_obj.value = mins;
	}

	// change size of the iframe to fit to its content
/*
	var table_obj = this.iframe_doc.getElementById('rc_iframe_table');
	this.iframe_obj.width = table_obj.offsetWidth;
	this.iframe_obj.height = table_obj.offsetHeight;
*/
	var cal = this;
	window.setTimeout(function(){cal.fit_to_content()}, 1);

	// fix position (need to do this later then calendar is shown as
	// size of calendar could change in this.show(x, y)
	window.setTimeout(function(){cal.fix_position()}, 5);

}

// change size of the iframe to fit to its content
RichCalendar.prototype.fit_to_content = function() {
try {
	var table_obj = this.iframe_doc.getElementById('rc_iframe_table');
	this.iframe_obj.width = table_obj.offsetWidth;
	this.iframe_obj.height = table_obj.offsetHeight;

//alert(this.iframe_obj.width + ' => ' + this.iframe_obj.height);
	// sometimes IE return 0 values, so need to use another approach to
	// determine size of the calendar
	if (!parseInt(this.iframe_obj.width) || !parseInt(this.iframe_obj.height)) {
		this.size_div.innerHTML = this.body_obj.innerHTML;
//alert(this.size_div.offsetWidth + ' => ' + this.size_div.offsetHeight);
	this.iframe_obj.width = this.size_div.offsetWidth;
	this.iframe_obj.height = this.size_div.offsetHeight;
	}

}catch(e){}
}


// create calendar row
RichCalendar.prototype.create_cal_row = function(index) {
var row = this.table_obj.insertRow(3+index);
	row.className = 'rc_cal_tr';

	var cell_indx;
	for (cell_indx=0; cell_indx<7; cell_indx++) {
		var td = row.insertCell(cell_indx);
//		td.innerHTML = index + '-' + cell_indx;
	}

	this.cal_tr[index] = row;

	return row;
}


// changes calendar layout
RichCalendar.prototype.change_skin = function(skin) {
	if (!this.iframe_obj) return;

	var skin_suffix = RichCalendar.skin_suffix(skin);

	this.iframe_obj.className = 'rc_calendar' + skin_suffix;

	this.body_obj.className = 'rc_iframe_body' + skin_suffix;

	this.skin = skin;
}


// returns formatted date (chars recognized are alike used by PHP function date)
RichCalendar.prototype.get_formatted_date = function(format, date) {

	if (!date) date = this.date;
	if (!format) format = this.get_date_format();

	// set time
	if (this.show_time && this.hours_obj && this.mins_obj) {
		this.date.setHours(this.hours_obj.value);
		var mins = this.date.setMinutes(this.mins_obj.value);
	}

var y = date.getFullYear();
var m = date.getMonth();
var d = date.getDate();
var wd = date.getDay();
var hr = date.getHours();
var mins = date.getMinutes();
var secs = date.getSeconds();

var month_names_short = this.text('monthNamesShort');
var month_names = this.text('monthNames');
var day_names_short = this.text('dayNamesShort');
var day_names = this.text('dayNames');

var am = hr < 12 ? true : false;
var hr12 = hr > 12 ? hr - 12 : (hr == 0 ? 12 : hr);

var f = [];

	f["%a"] = am?'am':'pm';
	f["%A"] = am?'AM':'PM';
	f["%d"] = d < 10 ? '0'+d : d; // day of the month, 2 digits with leading zeroes (01 to 31)
	f["%D"] = day_names_short[wd]; // day of the week, textual, short, eg "Fri"
	f["%F"] = month_names[m]; // month, textual, long; eg "January"
	f["%h"] = hr12 < 10 ? '0' + hr12 : hr12; // hour, 12-hour format (01 to 12)
	f["%H"] = hr < 10 ? '0' + hr : hr; // hour, 24-hour format (00 to 23)
	f["%g"] = hr12; // hour, 12-hour format without leading zeros (1 to 12)
	f["%G"] = hr; // hour, 24-hour format without leading zeros (0 to 23)
	f["%i"] = mins < 10 ? '0' + mins : mins; // minutes (00 to 59)
	f["%j"] = d; // day of the month without leading zeros (1 to 31)
	f["%l"] = day_names[wd]; // day of the week, textual, long, eg "Friday"
	f["%L"] = RichCalendar.is_leap_year(y)?1:0; // 1 if leap year, otherwise - 0
	f["%m"] = m < 9 ? '0' + (m+1) : (m+1); //month (01 to 12)
	f["%n"] = m + 1; //month without leading zeros (1 to 12)
	f["%M"] = month_names_short[m]; // month, textual, short, eg "Jan"
	f["%s"] = secs < 10 ? '0' + secs : secs; // seconds (00 to 59)
	f["%t"] = RichCalendar.get_month_days(date); // number of days in the month (28 to 31)
	f["%w"] = wd; // day of the week, numeric (0, Sunday to 6, Saturday)
	f["%Y"] = y; // year, 4 digits, eg 2007
	f["%y"] = String(y).substr(2, 2); // year, 2 digits, eg "07"
	f["%z"] = RichCalendar.get_day_of_year(date); // day of the year (1 to 366)

	var parts = String(format).match(/%./g);
	var i;
	var f_date = format;
	for (i=0; i<parts.length; i++) {
		var value = f[parts[i]];
		if (typeof(value) != "undefined") {
			var re = new RegExp(parts[i], 'g');
			f_date = f_date.replace(re, value);
		}
	}

	return f_date;

}


// set footer content
RichCalendar.prototype.set_footer_text = function(text) {
	if (this.footer_td) {
		this.footer_td.innerHTML = text;
	}
}


// return array with keys - weekend days
RichCalendar.prototype.get_weekend_days = function() {
var weekend_days = this.text('weekend');
var weekend_parts = weekend_days.split(",");
var i;
var result = [];

	for (i=0; i<weekend_parts.length; i++) {
		result[weekend_parts[i]] = true;
	}

	return result;
}


// calendar on close handler; returns true if operation successfull
RichCalendar.prototype.onclose_handler = function() {

	if (this.user_onclose_handler) {
		this.user_onclose_handler(this);
	} else {
		this.hide();
	}

}


// calendar on change handler
RichCalendar.prototype.onchange_handler = function(object_code) {

	if (this.user_onchange_handler) {
		this.user_onchange_handler(this, object_code);
	} else {
		if (object_code == 'day') {

			if (this.value_el) this.value_el.value = this.get_formatted_date();

			if (this.auto_close) this.hide();

		} else {

		}
	}

}


// returns current date format
RichCalendar.prototype.get_date_format = function() {
	var lang_date_format = this.text('dateFormat');
	var format = lang_date_format?lang_date_format:this.format;

	if (this.show_time) {
		format += ' %H:%i';
	}

	return format;
}


// parses date from string str
RichCalendar.prototype.parse_date = function(str, format) {
	if (typeof(str) == "undefined") {
		return;
	}

	if (!format) format = this.get_date_format();


//alert(format);
var today = new Date();
var year = 0;
var month = -1;
var day = 0;
var hours = 0;
var mins = 0;
var seconds = 0;

var month_names = this.text('monthNames');
var short_month_names = this.text('monthNamesShort');

var en_month_names = this.text('monthNames', 'en');
var en_short_month_names = this.text('monthNamesShort', 'en');

//alert(month_names);
	// national chars are not recognized as symbols in regular expressions =>
	// replace them with english month names
	for (j=0; j<month_names.length; j++) {
		var re = new RegExp(month_names[j], 'gi');
		str = str.replace(re, en_month_names[j]);
	}
	for (j=0; j<short_month_names.length; j++) {
		var re = new RegExp(short_month_names[j], 'gi');
		str = str.replace(re, en_short_month_names[j]);
	}

var p = String(str).split(/\W+/g);
var f_p = String(format).match(/%./g);
var i;
var j;
var k;

//alert(p + ' => ' + f_p);
	for (i=0; i<f_p.length; i++) {

		if (!p[i]) continue;

		switch (f_p[i]) {
			case '%a': // am pm
			case '%A':
				if (/am/i.test(p[i]) && hours >= 12) {
					hours -= 12;
				} else {
					if (/pm/i.test(p[i]) && hours < 12) {
						hours += 12;
					}
				}
				break;
			case '%d':
			case '%j':
				day = parseInt(Number(p[i]));
				break;
			case '%F':
				for (j=0; j<en_month_names.length; j++) {
					if (en_month_names[j].toLowerCase() == p[i].toLowerCase()) {
						month = j;
						break;
					}
				}
				break;
			case '%h':
			case '%H':
			case '%g':
			case '%G':
				hours = parseInt(Number(p[i]));
				// to recognize this: 10pm
				if (/am/i.test(p[i]) && hours >= 12) {
					hours -= 12;
				} else {
					if (/pm/i.test(p[i]) && hours < 12) {
						hours += 12;
					}
				}
				break;
			case '%i':
				mins = parseInt(Number(p[i]));
				break;
			case '%m':
			case '%n':
				month = parseInt(Number(p[i]))-1;
				break;
			case '%M':
				for (j=0; j<en_short_month_names.length; j++) {
					if (en_short_month_names[j].toLowerCase() == p[i].toLowerCase()) {
						month = j;
						break;
					}
				}
				break;
			case '%s':
				seconds = parseInt(Number(p[i]));
				break;
			case '%Y':
				year = parseInt(Number(p[i]));
				break;
			case '%y':
				year = parseInt(p[i]);
				if (year < 100) {
					year += year + (year > 29 ? 1900 : 2000);
				}
				break;
			default:
				break;
		}

	}

	if (isNaN(year) || year <= 0) year = today.getFullYear();
	if (isNaN(month) || month < 0 || month > 11) month = today.getMonth();
	if (isNaN(day) || day <= 0 || day > 31) day = today.getDate();
	if (isNaN(hours) || hours < 0 || hours > 23) hours = today.getHours();
	if (isNaN(mins) || mins < 0 || mins > 59) mins = today.getMinutes();
	if (isNaN(seconds) || seconds < 0 || seconds > 59) seconds = today.getSeconds();

//alert(year + ' => ' + month + ' => ' + day + ' => ' + hours + ' => ' + mins + ' => ' + seconds);
	this.date = new Date(year, month, day, hours, mins, seconds);

}
var text = new Array();

text['today'] = 'Oggi';
text['time'] = 'Time';

text['dayNamesShort'] = new Array(
'Dom',
'Lun',
'Mar',
'Mer',
'Gio',
'Ven',
'Sab'
);
text['dayNames'] = new Array(
'Domenica',
'Luned&igrave;',
'Marted&igrave;',
'Mercoled&igrave;',
'Gioved&igrave;',
'Venerd&igrave;',
'Sabato'
);

text['monthNamesShort'] = new Array(
'Gen',
'Feb',
'Mar',
'Apr',
'Mag',
'Giu',
'Lug',
'Ago',
'Set',
'Ott',
'Nov',
'Dic'
);

text['monthNames'] = new Array(
'Gennaio',
'Febbraio',
'Marzo',
'Aprile',
'Maggio',
'Giugno',
'Luglio',
'Agosto',
'Settembre',
'Ottobre',
'Novembre',
'Dicembre'
);


text['footerDateFormat'] = '%j %F %Y, %D',
text['dateFormat'] = '%n-%j-%Y',
text['footerDefaultText'] = 'Selezione data',

text['clear'] = 'Pulisci Data',
text['prev_year'] = 'Anno precedente',
text['prev_month'] = 'Mese precedente',
text['next_month'] = 'Mese successivo',
text['next_year'] = 'Anno successivo',
text['close'] = 'Chiudi',


// weekend days (0 - sunday, ... 6 - saturday)
text['weekend'] = "0,6";
text['make_first'] = "Inizia con %s";


RichCalendar.rc_lang_data['it'] = text;
function getParameterMap(form) {
    var p = document.forms[form].elements;
    var map = new Object();
    for(var x=0; x < p.length; x++) {
        var key = p[x].name;
        var val = p[x].value;
        
        //Check if this field name is unique.
        //If the field name is repeated more than once
        //add it to the current array.
        var curVal = map[key]; 
        if (curVal) { // more than one field so append value to array
        	curVal[curVal.length] = val;
        } else { // add field and value
        	map[key]= [val];
        }
    }
    return map;
}

/*********************
//* jQuery Drop Line Menu- By Dynamic Drive: http://www.dynamicdrive.com/
//* Last updated: June 27th, 09'
//* Menu avaiable at DD CSS Library: http://www.dynamicdrive.com/style/
*********************/

var subMenuSelected = false;

var droplinemenu = {

	arrowimage: {classname: 'downarrowclass', src: '/app_img/down-new' + style_suff + '.gif', leftpadding: 5}, //customize down arrow image
	animateduration: {clickin: 300, clickout: 100}, //duration of slide in/ out animation, in milliseconds
	
	buildmenu:function(menuid) {	
		$(document).ready(function($){
			var $mainmenu = $("#"+menuid+">ul");	
			var $headers = $mainmenu.children("li");	
			var count = 0;									
			$headers.each(function(i) {			
				var $curobj = $(this);
				$curobj.attr('count', count);
				var $subul=$(this).find('ul:eq(0)');
				var subWidth = $subul.width();			
				this._dimensions={h:$curobj.find('a:eq(0)').outerHeight()};
				this.istopheader=$curobj.parents("ul").length==1? true : false;
				if (!this.istopheader) {				
					$subul.css({left:0, top:this._dimensions.h});
				}
				var $innerheader=$curobj.find('a').eq(0);			
				$innerheader=($innerheader.children().eq(0).is('span'))? $innerheader.children().eq(0) : $innerheader;						
				$innerheader.append(
					'<img src="'+ droplinemenu.arrowimage.src
					+'" class="' + droplinemenu.arrowimage.classname
					+ '" style="border:0; padding-left: '+droplinemenu.arrowimage.leftpadding+'px" />'
				);
				$curobj.click(	
					function(e) {
						if (!subMenuSelected) {
							var isReturn = false;
							var $targetul = $(this).parent().children("li");
							var $targetul1 = $(this).children("ul:eq(0)");
							$targetul.each(function() {
								var $targetli = $(this).children("ul:eq(0)");
								if ($targetli.attr('down') == 'true') {	 
									$targetli.slideUp(droplinemenu.animateduration.clickout);
									$targetli.removeAttr('down');
									if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
										$("#breadcrumbDiv").show();	
									}
									isReturn = true;
									return false;
								}
							});
							if (!isReturn) {
								if ($targetul1.queue().length <= 1)	{					
									if (this.istopheader) {							
										$targetul1.css({left:0, top: this._dimensions.h});
										$subul.width(subWidth);																						
									}
									if (document.all && !window.XMLHttpRequest)
										$mainmenu.find('ul').css({overflow: (this.istopheader)? 'hidden' : 'visible'});
									$targetul1.slideDown(droplinemenu.animateduration.clickin);
									if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
										$("#breadcrumbDiv").hide();	
										$(this).show();							
									}
									$targetul1.attr('down','true');
								}
							}
						}
						subMenuSelected = false;
					}				
				);
				count++;
			});
		
		$headers = $mainmenu.find("ul[livello=1]").parent();	
			var count = 0;									
			$headers.each(function(i) {			
				var $curobj=$(this);
				$curobj.attr('count', count);
				var $subul=$(this).find('ul:eq(0)');
				var subWidth = $subul.width();			
				this._dimensions1={h:$curobj.find('a:eq(0)').outerHeight()};
				this.istopheader=$curobj.parents("ul").length==1? true : false;
				if (!this.istopheader) {				
					$subul.css({left:0, top:this._dimensions1.h});
				}
				var $innerheader=$curobj.find('a').eq(0);			
				$innerheader=($innerheader.children().eq(0).is('span'))? $innerheader.children().eq(0) : $innerheader;						
				$innerheader.append(
					'<img src="'+ droplinemenu.arrowimage.src
					+'" class="' + droplinemenu.arrowimage.classname
					+ '" style="border:0; padding-left: '+droplinemenu.arrowimage.leftpadding+'px" />'
				);
				$curobj.click(	
					function(e) {
						subMenuSelected = true;
						var isReturn = false;
						$targetul = $(this);
						var $targetul1 = $targetul.children("ul:eq(0)");
						// Chiudi tutti i menu gia' aperti allo stesso livello tranne quello corrente
						var $sameLevelLiList = $targetul.closest("ul").children("li");
						$sameLevelLiList.each(function() {
							var $ul1 = $(this).children("ul:eq(0)");
							if ($ul1.length > 0 && $ul1.html() != $targetul1.html()) {
								if ($ul1.attr('down') == 'true') {	
									$ul1.slideUp(droplinemenu.animateduration.clickout);
									$ul1.removeAttr('down');
								}
							}
						});
						// Apri o chiudi il menu corrente
						$targetul.each(function() {
							var $targetli = $(this).children("ul:eq(0)");
							if ($targetli.attr('down') == 'true') {	
								$targetli.slideUp(droplinemenu.animateduration.clickout);
								$targetli.removeAttr('down');
								if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
									$("#breadcrumbDiv").show();	
								}
								isReturn = true;
								return false;
							}
						});
						if (!isReturn) {
							if ($targetul1.queue().length <= 1)	{					
								if (this.istopheader) {							
									$targetul1.css({left:0, top: this._dimensions1.h});
									$subul.width(subWidth);																						
								}
								if (document.all && !window.XMLHttpRequest)
									$mainmenu.find('ul').css({overflow: (this.istopheader)? 'hidden' : 'visible'});
								$targetul1.slideDown(droplinemenu.animateduration.clickin);
								if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
									$("#breadcrumbDiv").hide();	
									$(this).show();							
								}
								$targetul1.attr('down','true');
							}
						}
					}				
				);
				count++;
			});
			$mainmenu.find("ul").css({display:'none', visibility:'visible', width:$mainmenu.width()});
		});
	}
};// Cross-Browser Rich Text Editor
// http://www.kevinroth.com/rte/demo.htm
// Written by Kevin Roth (kevin@NOSPAMkevinroth.com - remove NOSPAM)
// Visit the support forums at http://www.kevinroth.com/forums/index.php?c=2

//init variables
var isRichText = false;
var rng;
var currentRTE;
var allRTEs = "";

var isIE;
var isGecko;
var isSafari;
var isKonqueror;

var imagesPath;
var includesPath;
var cssFile;
var upadloadCallback;

function initRTE(imgPath, incPath, css, upcall) {
	//set browser vars
	var ua = navigator.userAgent.toLowerCase();
	isIE = ((ua.indexOf("msie") != -1) && (ua.indexOf("opera") == -1) && (ua.indexOf("webtv") == -1)); 
	isGecko = (ua.indexOf("gecko") != -1);
	isSafari = (ua.indexOf("safari") != -1);
	isKonqueror = (ua.indexOf("konqueror") != -1);
	
	//check to see if designMode mode is available
	if (document.getElementById && document.designMode) {
		isRichText = true;
	}
	
	if (isIE) {
		document.onmouseover = raiseButton;
		document.onmouseout  = normalButton;
		document.onmousedown = lowerButton;
		document.onmouseup   = raiseButton;
	}
	
	//set paths vars
	imagesPath = imgPath;
	includesPath = incPath;
	cssFile = css;
	upadloadCallback = upcall;
	
	//if (isRichText) strHtml += '<style type="text/css">@import "' + includesPath + 'rte.css";</style>');
		
	//for testing standard textarea, uncomment the following line
	//isRichText = false;
}

function writeRichText(rte, html, width, height, buttons, readOnly) {
	if (isRichText) {
		if (allRTEs.length > 0) allRTEs += ";";
		allRTEs += rte;
		
		if (readOnly) buttons = false;
		
		//adjust minimum table widths		
		if (isIE) {
			if (buttons && (width < 540)) width = 540;
			var tablewidth = width;
		} else {
			if (buttons && (width < 540)) width = 540;
			var tablewidth = width + 4;
		}		
		
		var strHtml;
		strHtml = '<div class="rteDiv">';
		if (buttons == true) {
			strHtml += '<table class="rteBack" cellpadding=2 cellspacing=0 id="Buttons1_' + rte + '" width="' + tablewidth + '">';
			strHtml += '	<tr>';
			strHtml += '		<td>';
			strHtml += '			<select id="formatblock_' + rte + '" onchange="selectFont(\'' + rte + '\', this.id);">';
			strHtml += '				<option value="">[' + stileLabel + ']</option>';
			strHtml += '				<option value="<p>">Paragraph &lt;p&gt;</option>';
			strHtml += '				<option value="<h1>">Heading 1 &lt;h1&gt;</option>';
			strHtml += '				<option value="<h2>">Heading 2 &lt;h2&gt;</option>';
			strHtml += '				<option value="<h3>">Heading 3 &lt;h3&gt;</option>';
			strHtml += '				<option value="<h4>">Heading 4 &lt;h4&gt;</option>';
			strHtml += '				<option value="<h5>">Heading 5 &lt;h5&gt;</option>';
			strHtml += '				<option value="<h6>">Heading 6 &lt;h6&gt;</option>';
			strHtml += '				<option value="<address>">Address &lt;ADDR&gt;</option>';
			strHtml += '				<option value="<pre>">Formatted &lt;pre&gt;</option>';
			strHtml += '			</select>';
			strHtml += '		</td>';
			strHtml += '		<td>';
			strHtml += '			<select id="fontname_' + rte + '" onchange="selectFont(\'' + rte + '\', this.id)">';
			strHtml += '				<option value="Font" selected>[' + carattereLabel + ']</option>';
			strHtml += '				<option value="Arial, Helvetica, sans-serif">Arial</option>';
			strHtml += '				<option value="Courier New, Courier, mono">Courier New</option>';
			strHtml += '				<option value="Times New Roman, Times, serif">Times New Roman</option>';
			strHtml += '				<option value="Verdana, Arial, Helvetica, sans-serif">Verdana</option>';
			strHtml += '			</select>';
			strHtml += '		</td>';
			strHtml += '		<td>';
			strHtml += '			<select unselectable="on" id="fontsize_' + rte + '" onchange="selectFont(\'' + rte + '\', this.id);">';
			strHtml += '				<option value="Size">[' + dimensioneLabel + ']</option>';
			strHtml += '				<option value="1">1</option>';
			strHtml += '				<option value="2">2</option>';
			strHtml += '				<option value="3">3</option>';
			strHtml += '				<option value="4">4</option>';
			strHtml += '				<option value="5">5</option>';
			strHtml += '				<option value="6">6</option>';
			strHtml += '				<option value="7">7</option>';
			strHtml += '			</select>';
			strHtml += '		</td>';
			strHtml += '		<td width="100%">';
			strHtml += '		</td>';
			strHtml += '	</tr>';
			strHtml += '</table>';
			strHtml += '<table class="rteBack" cellpadding="0" cellspacing="0" id="Buttons2_' + rte + '" width="' + tablewidth + '">';
			strHtml += '	<tr>';
			strHtml += '		<td><img id="bold" class="rteImage" src="' + imagesPath + 'bold.gif" width="25" height="24" alt="Bold" title="Bold" onClick="rteCommand(\'' + rte + '\', \'bold\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'italic.gif" width="25" height="24" alt="Italic" title="Italic" onClick="rteCommand(\'' + rte + '\', \'italic\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'underline.gif" width="25" height="24" alt="Underline" title="Underline" onClick="rteCommand(\'' + rte + '\', \'underline\', \'\')"></td>';
			strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'left_just.gif" width="25" height="24" alt="Align Left" title="Align Left" onClick="rteCommand(\'' + rte + '\', \'justifyleft\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'centre.gif" width="25" height="24" alt="Center" title="Center" onClick="rteCommand(\'' + rte + '\', \'justifycenter\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'right_just.gif" width="25" height="24" alt="Align Right" title="Align Right" onClick="rteCommand(\'' + rte + '\', \'justifyright\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'justifyfull.gif" width="25" height="24" alt="Justify Full" title="Justify Full" onclick="rteCommand(\'' + rte + '\', \'justifyfull\', \'\')"></td>';
			strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'hr.gif" width="25" height="24" alt="Horizontal Rule" title="Horizontal Rule" onClick="rteCommand(\'' + rte + '\', \'inserthorizontalrule\', \'\')"></td>';
			strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'numbered_list.gif" width="25" height="24" alt="Ordered List" title="Ordered List" onClick="rteCommand(\'' + rte + '\', \'insertorderedlist\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'list.gif" width="25" height="24" alt="Unordered List" title="Unordered List" onClick="rteCommand(\'' + rte + '\', \'insertunorderedlist\', \'\')"></td>';
			strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'outdent.gif" width="25" height="24" alt="Outdent" title="Outdent" onClick="rteCommand(\'' + rte + '\', \'outdent\', \'\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'indent.gif" width="25" height="24" alt="Indent" title="Indent" onClick="rteCommand(\'' + rte + '\', \'indent\', \'\')"></td>';
			strHtml += '		<td><div id="forecolor_' + rte + '"><img class="rteImage" src="' + imagesPath + 'textcolor.gif" width="25" height="24" alt="Text Color" title="Text Color" onClick="dlgColorPalette(\'' + rte + '\', \'forecolor\', \'\')"></div></td>';
			strHtml += '		<td><div id="hilitecolor_' + rte + '"><img class="rteImage" src="' + imagesPath + 'bgcolor.gif" width="25" height="24" alt="Background Color" title="Background Color" onClick="dlgColorPalette(\'' + rte + '\', \'hilitecolor\', \'\')"></div></td>';
			strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'hyperlink.gif" width="25" height="24" alt="Insert Link" title="Insert Link" onClick="insertLink(\'' + rte + '\')"></td>';
			strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'image.gif" width="25" height="24" alt="Add Image" title="Add Image" onClick="addImage(\'' + rte + '\')"></td>';
			strHtml += '		<td><div id="table_' + rte + '"><img class="rteImage" src="' + imagesPath + 'insert_table.gif" width="25" height="24" alt="Insert Table" title="Insert Table" onClick="dlgInsertTable(\'' + rte + '\', \'table\', \'\')"></div></td>';
			if (isIE) {
				strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'spellcheck.gif" width="25" height="24" alt="Spell Check" title="Spell Check" onClick="checkspell()"></td>';
			}
	//		strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
	//		strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'cut.gif" width="25" height="24" alt="Cut" title="Cut" onClick="rteCommand(\'' + rte + '\', \'cut\')"></td>';
	//		strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'copy.gif" width="25" height="24" alt="Copy" title="Copy" onClick="rteCommand(\'' + rte + '\', \'copy\')"></td>';
	//		strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'paste.gif" width="25" height="24" alt="Paste" title="Paste" onClick="rteCommand(\'' + rte + '\', \'paste\')"></td>';
	//		strHtml += '		<td><img class="rteVertSep" src="' + imagesPath + 'blackdot.gif" width="1" height="20" border="0" alt=""></td>';
	//		strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'undo.gif" width="25" height="24" alt="Undo" title="Undo" onClick="rteCommand(\'' + rte + '\', \'undo\')"></td>';
	//		strHtml += '		<td><img class="rteImage" src="' + imagesPath + 'redo.gif" width="25" height="24" alt="Redo" title="Redo" onClick="rteCommand(\'' + rte + '\', \'redo\')"></td>';
			strHtml += '		<td width="100%"></td>';
			strHtml += '	</tr>';
			strHtml += '</table>';
		}	
		// COMMENTATO LA RIGA SOTTO E AGGIUNTO LA RIGA SUCCESSIVA PER PERMETTERE LA VISUALIZZAZIONE NEL CONTENT DIV
		//strHtml += '<iframe id="' + rte + '" name="' + rte + '" width="' + width + 'px" height="' + height + 'px" src="' + includesPath + 'blank.htm"></iframe>';
		strHtml += '<iframe id="' + rte + '" name="' + rte + '" width="' + width + 'px" height="' + height + 'px" src="#"></iframe>';
		//if (!readOnly) strHtml += '<br /><input type="checkbox" id="chkSrc' + rte + '" onclick="toggleHTMLSrc(\'' + rte + '\');" />&nbsp;View Source';
		if (!readOnly) strHtml += '<br />';
		strHtml += '<iframe width="154" height="104" id="cp' + rte + '" src="' + includesPath + 'palette.htm" marginwidth="0" marginheight="0" scrolling="no" style="visibility:hidden; position: absolute;"></iframe>';
		strHtml += '<input type="hidden" id="hdn' + rte + '" name="' + rte + '" value="">';
		strHtml += '</div>';
		
		$('#innerDiv').append(strHtml);

		document.getElementById('hdn' + rte).value = html;
		enableDesignMode(rte, html, readOnly);				
	} else {
		if (!readOnly) {
			strHtml += '<textarea name="' + rte + '" id="' + rte + '" style="width: ' + width + 'px; height: ' + height + 'px;">' + html + '</textarea>';
		} else {
			strHtml += '<textarea name="' + rte + '" id="' + rte + '" style="width: ' + width + 'px; height: ' + height + 'px;" readonly>' + html + '</textarea>';
		}
	}
}

function enableDesignMode(rte, html, readOnly) {
	var frameHtml = "<html id=\"" + rte + "\">\n";
	frameHtml += "<head>\n";
	//to reference your stylesheet, set href property below to your stylesheet path and uncomment
	if (cssFile.length > 0) {
		frameHtml += "<link media=\"all\" type=\"text/css\" href=\"" + cssFile + "\" rel=\"stylesheet\">\n";
	} else {
		frameHtml += "<style>\n";
		frameHtml += "body {\n";
		frameHtml += "	background: #FFFFFF;\n";
		frameHtml += "	margin: 0px;\n";
		frameHtml += "	padding: 0px;\n";
		frameHtml += "}\n";
		frameHtml += "</style>\n";
	}
	frameHtml += "</head>\n";
	frameHtml += "<body>\n";
	frameHtml += html + "\n";
	frameHtml += "</body>\n";
	frameHtml += "</html>";
		
	if (document.all) {
		var oRTE = frames[rte].document;
		oRTE.open();
		oRTE.write(frameHtml);
		oRTE.close();
		if (!readOnly) oRTE.designMode = "On";
	} else {
		try {
			if (!readOnly) document.getElementById(rte).contentDocument.designMode = "on";
			try {
				var oRTE = document.getElementById(rte).contentWindow.document;				
				oRTE.open();
				oRTE.write(frameHtml);				
				oRTE.close();
				if (isGecko && !readOnly) {					
					//attach a keyboard handler for gecko browsers to make keyboard shortcuts work
					oRTE.addEventListener("keypress", kb_handler, true);
				}				
			} catch (e) {
				alert("Error preloading content.");
			}
		} catch (e) {
			//gecko may take some time to enable design mode.
			//Keep looping until able to set.			
			if (isGecko) {
				setTimeout("enableDesignMode('" + rte + "', '" + html + "', " + readOnly + ");", 10);
			} else {
				return false;
			}
		}
	}
}

function updateRTEs() {
	var vRTEs = allRTEs.split(";");
	for (var i = 0; i < vRTEs.length; i++) {
		updateRTE(vRTEs[i]);
	}
}

function updateRTE(rte) {
	if (!isRichText) return;
	
	//set message value
	var oHdnMessage = document.getElementById('hdn' + rte);
	var oRTE = document.getElementById(rte);
	var readOnly = false;
	
	//check for readOnly mode
	if (document.all) {
		if (frames[rte].document.designMode != "On") readOnly = true;
	} else {
		if (document.getElementById(rte).contentDocument.designMode != "on") readOnly = true;
	}
	
	if (isRichText && !readOnly) {
		//if viewing source, switch back to design view
		/*if (document.getElementById("chkSrc" + rte).checked) {
			document.getElementById("chkSrc" + rte).checked = false;
			toggleHTMLSrc(rte);
		}*/
		
		if (oHdnMessage.value == null) oHdnMessage.value = "";
		if (document.all) {
			oHdnMessage.value = frames[rte].document.body.innerHTML;
		} else {
			oHdnMessage.value = oRTE.contentWindow.document.body.innerHTML;
		}
		
		//if there is no content (other than formatting) set value to nothing
		if (stripHTML(oHdnMessage.value.replace("&nbsp;", " ")) == "" 
			&& oHdnMessage.value.toLowerCase().search("<hr") == -1
			&& oHdnMessage.value.toLowerCase().search("<img") == -1) oHdnMessage.value = "";
		//fix for gecko
		if (escape(oHdnMessage.value) == "%3Cbr%3E%0D%0A%0D%0A%0D%0A") oHdnMessage.value = "";
	}
}

function rteCommand(rte, command, option) {
	//function to perform command
	var oRTE;
	if (document.all) {
		oRTE = frames[rte];
	} else {
		oRTE = document.getElementById(rte).contentWindow;
	}
	
	try {
		oRTE.focus();
	  	oRTE.document.execCommand(command, false, option);
		oRTE.focus();
	} catch (e) {
//		alert(e);
//		setTimeout("rteCommand('" + rte + "', '" + command + "', '" + option + "');", 10);
	}
}

function toggleHTMLSrc(rte) {
	//contributed by Bob Hutzel (thanks Bob!)
	var oRTE;
	if (document.all) {
		oRTE = frames[rte].document;
	} else {
		oRTE = document.getElementById(rte).contentWindow.document;
	}
	
	/*if (document.getElementById("chkSrc" + rte).checked) {
		showHideElement("Buttons1_" + rte, "hide");
		showHideElement("Buttons2_" + rte, "hide");
		if (document.all) {
			oRTE.body.innerText = oRTE.body.innerHTML;
		} else {
			var htmlSrc = oRTE.createTextNode(oRTE.body.innerHTML);
			oRTE.body.innerHTML = "";
			oRTE.body.appendChild(htmlSrc);
		}
	} else*/ {
		showHideElement("Buttons1_" + rte, "show");
		showHideElement("Buttons2_" + rte, "show");
		if (document.all) {
			//fix for IE
			var output = escape(oRTE.body.innerText);
			output = output.replace("%3CP%3E%0D%0A%3CHR%3E", "%3CHR%3E");
			output = output.replace("%3CHR%3E%0D%0A%3C/P%3E", "%3CHR%3E");
			
			oRTE.body.innerHTML = unescape(output);
		} else {
			var htmlSrc = oRTE.body.ownerDocument.createRange();
			htmlSrc.selectNodeContents(oRTE.body);
			oRTE.body.innerHTML = htmlSrc.toString();
		}
	}
}

function dlgColorPalette(rte, command) {
	//function to display or hide color palettes
	setRange(rte);
	
	//get dialog position
	var oDialog = document.getElementById('cp' + rte);
	var buttonElement = document.getElementById(command + '_' + rte);
	var iLeftPos = getOffsetLeft(buttonElement);
	var iTopPos = getOffsetTop(buttonElement) + (buttonElement.offsetHeight + 4);
	oDialog.style.left = (iLeftPos) + "px";
	oDialog.style.top = (iTopPos) + "px";
	
	if ((command == parent.command) && (rte == currentRTE)) {
		//if current command dialog is currently open, close it
		if (oDialog.style.visibility == "hidden") {
			showHideElement(oDialog, 'show');
		} else {
			showHideElement(oDialog, 'hide');
		}
	} else {
		//if opening a new dialog, close all others
		var vRTEs = allRTEs.split(";");
		for (var i = 0; i < vRTEs.length; i++) {
			showHideElement('cp' + vRTEs[i], 'hide');
		}
		showHideElement(oDialog, 'show');
	}
	
	//save current values
	parent.command = command;
	currentRTE = rte;
}

function dlgInsertTable(rte, command) {
	//function to open/close insert table dialog
	//save current values
	setRange(rte);
	parent.command = command;
	currentRTE = rte;
	var windowOptions = 'history=no,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=no,resizable=no,width=360,height=200';
	window.open(includesPath + 'insert_table.htm', 'InsertTable', windowOptions);
}

function insertLink(rte) {
	//function to insert link
	var szURL = prompt("Enter a URL:", "");
	try {
		//ignore error for blank urls
		rteCommand(rte, "Unlink", null);
		rteCommand(rte, "CreateLink", szURL);
		rte.parentElement().target="_blank";
	} catch (e) {
		//do nothing
	}
}

function setColor(color) {
	//function to set color
	var rte = currentRTE;
	var parentCommand = parent.command;
	
	if (document.all) {
		//retrieve selected range
		var sel = frames[rte].document.selection; 
		if (parentCommand == "hilitecolor") parentCommand = "backcolor";
		if (sel != null) {
			var newRng = sel.createRange();
			newRng = rng;
			newRng.select();
		}
	}
	
	rteCommand(rte, parentCommand, color);
	showHideElement('cp' + rte, "hide");
}

function addImage(rte) {
	if (typeof(upadloadCallback) === 'function') {
		jPromptFile(uploadImmagineMsg, uploadImmagineTitle, null, function() {
			upadloadCallback();
		});
	} else {
		//function to add image
		imagePath = prompt('Enter Image URL:', 'http://');				
		if ((imagePath != null) && (imagePath != "")) {
			rteCommand(rte, 'InsertImage', imagePath);
		}
	}
}

// Ernst de Moor: Fix the amount of digging parents up, in case the RTE editor itself is displayed in a div.
// KJR 11/12/2004 Changed to position palette based on parent div, so palette will always appear in proper location regardless of nested divs
function getOffsetTop(elm) {
	var mOffsetTop = elm.offsetTop;
	var mOffsetParent = elm.offsetParent;
	var parents_up = 2; //the positioning div is 2 elements up the tree
	
	while(parents_up > 0) {
		mOffsetTop += mOffsetParent.offsetTop;
		mOffsetParent = mOffsetParent.offsetParent;
		parents_up--;
	}
	
	return mOffsetTop;
}

// Ernst de Moor: Fix the amount of digging parents up, in case the RTE editor itself is displayed in a div.
// KJR 11/12/2004 Changed to position palette based on parent div, so palette will always appear in proper location regardless of nested divs
function getOffsetLeft(elm) {
	var mOffsetLeft = elm.offsetLeft;
	var mOffsetParent = elm.offsetParent;
	var parents_up = 2;
	
	while(parents_up > 0) {
		mOffsetLeft += mOffsetParent.offsetLeft;
		mOffsetParent = mOffsetParent.offsetParent;
		parents_up--;
	}
	
	return mOffsetLeft;
}

function selectFont(rte, selectname) {
	//function to handle font changes
	var idx = document.getElementById(selectname).selectedIndex;
	// First one is always a label	
	if (idx != 0) {
		var selected = document.getElementById(selectname).options[idx].value;
		var cmd = selectname.replace('_' + rte, '');
		rteCommand(rte, cmd, selected);
		document.getElementById(selectname).selectedIndex = 0;
	}
}

function kb_handler(evt) {
	var rte = evt.target.id;
	
	//contributed by Anti Veeranna (thanks Anti!)
	if (evt.ctrlKey) {
		var key = String.fromCharCode(evt.charCode).toLowerCase();
		var cmd = '';
		switch (key) {
			case 'b': cmd = "bold"; break;
			case 'i': cmd = "italic"; break;
			case 'u': cmd = "underline"; break;
		};

		if (cmd) {
			rteCommand(rte, cmd, null);
			
			// stop the event bubble
			evt.preventDefault();
			evt.stopPropagation();
		}
 	}
}

function insertHTML(html) {
	//function to add HTML -- thanks dannyuk1982
	var rte = currentRTE;
	
	var oRTE;
	if (document.all) {
		oRTE = frames[rte];
	} else {
		oRTE = document.getElementById(rte).contentWindow;
	}
	
	oRTE.focus();
	if (document.all) {
		oRTE.document.selection.createRange().pasteHTML(html);
	} else {
		oRTE.document.execCommand('insertHTML', false, html);
	}
}

function showHideElement(element, showHide) {
	//function to show or hide elements
	//element variable can be string or object
	if (document.getElementById(element)) {
		element = document.getElementById(element);
	}
	
	if (showHide == "show") {
		element.style.visibility = "visible";
	} else if (showHide == "hide") {
		element.style.visibility = "hidden";
	}
}

function setRange(rte) {
	//function to store range of current selection
	var oRTE;
	if (document.all) {
		oRTE = frames[rte];
		var selection = oRTE.document.selection; 
		if (selection != null) rng = selection.createRange();
	} else {
		oRTE = document.getElementById(rte).contentWindow;
		var selection = oRTE.getSelection();
		rng = selection.getRangeAt(selection.rangeCount - 1).cloneRange();
	}
}

function stripHTML(oldString) {
	//function to strip all html
	var newString = oldString.replace(/(<([^>]+)>)/ig,"");
	
	//replace carriage returns and line feeds
   newString = newString.replace(/\r\n/g," ");
   newString = newString.replace(/\n/g," ");
   newString = newString.replace(/\r/g," ");
	
	//trim string
	newString = trim(newString);
	
	return newString;
}

function trim(inputString) {
   // Removes leading and trailing spaces from the passed string. Also removes
   // consecutive spaces and replaces it with one space. If something besides
   // a string is passed in (null, custom object, etc.) then return the input.
   if (typeof inputString != "string") return inputString;
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
	
   while (ch == " ") { // Check for spaces at the beginning of the string
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length - 1, retValue.length);
	
   while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length - 1);
      ch = retValue.substring(retValue.length - 1, retValue.length);
   }
	
	// Note that there are two spaces in the string - look for multiple spaces within the string
   while (retValue.indexOf("  ") != -1) {
		// Again, there are two spaces in each of the strings
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ") + 1, retValue.length);
   }
   return retValue; // Return the trimmed string back to the user
}

//*****************
//IE-Only Functions
//*****************
function checkspell() {
	//function to perform spell check
	try {
		var tmpis = new ActiveXObject("ieSpell.ieSpellExtension");
		tmpis.CheckAllLinkedDocuments(document);
	}
	catch(exception) {
		if(exception.number==-2146827859) {
			if (confirm("ieSpell not detected.  Click Ok to go to download page."))
				window.open("http://www.iespell.com/download.php","DownLoad");
		} else {
			alert("Error Loading ieSpell: Exception " + exception.number);
		}
	}
}

function raiseButton(e) {
	//IE-Only Function
	var el = window.event.srcElement;
	
	className = el.className;
	if (className == 'rteImage' || className == 'rteImageLowered') {
		el.className = 'rteImageRaised';
	}
}

function normalButton(e) {
	//IE-Only Function
	var el = window.event.srcElement;
	
	className = el.className;
	if (className == 'rteImageRaised' || className == 'rteImageLowered') {
		el.className = 'rteImage';
	}
}

function lowerButton(e) {
	//IE-Only Function
	var el = window.event.srcElement;
	
	className = el.className;
	if (className == 'rteImage' || className == 'rteImageRaised') {
		el.className = 'rteImageLowered';
	}
}
// -------------------------------------------------------------------
// Image Thumbnail Viewer Script- By Dynamic Drive, available at: http://www.dynamicdrive.com
// Last updated: July 7th, 2008- Fixed enlarged image not showing in IE sometimes
// -------------------------------------------------------------------

function resizeImage() {
	if ($("#imgShow").width() > $("#page").width() || $("#imgShow").height() > $("#page").height()) {
		var h = Math.round($("#page").height() * ($("#page").width() / $("#page").height()));
		var w = Math.round($("#page").width() * ($("#page").width() / $("#page").height()));
		$("#imgShow").width(Math.floor($("#page").height() * ($("#imgShow").width() / $("#imgShow").height())));
        $("#imgShow").height($("#page").height());
	}
}

function addRightClickMenu() { 		
    $("#thumbImage").contextMenu({ menu: 'printImgMenu', yTop: 0, xLeft: 20 }, 
    	function(action, el, pos) { 
    		if (action == "print") {
    			if (getBrowser().indexOf("FIREFOX") == -1) {
    				$('#thumbImage').printElement({printMode:'popup'});
    			} else {
    				$('#thumbImage').printElement();
    			}
    		}
    });	
}

var bEnableAnimation = (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() > 8) ? false : true;
var thumbnailviewer={
	enableTitle: bEnableAnimation, //Should "title" attribute of link be used as description?
	enableAnimation: false, //Enable fading animation?
	definefooter: '<div class="footerbar" style="cursor: pointer" onClick="thumbnailviewer.closeit()">' + chiudiMsg + '</div>', //Define HTML for footer interface
	defineLoading: '<img src="/app_img/loading.gif" /> ' + loadingMsg, //Define HTML for "loading" div
	
	/////////////No need to edit beyond here/////////////////////////
	
	scrollbarwidth: 16,
	opacitystring: 'filter:progid:DXImageTransform.Microsoft.alpha(opacity=10); -moz-opacity: 0.1; opacity: 0.1',
	targetlinks:[], //Array to hold links with rel="thumbnail"
	
	createthumbBox:function(){
		//write out HTML for Image Thumbnail Viewer plus loading div
		//document.write('<div id="thumbBox" class="ui-draggable" style="cursor: move"><div id="thumbImage"></div>'+this.definefooter+'</div>')
		// TODO: TESTARE LA LINEA SOTTO	 
		document.write('<div id="thumbBox" class="ui-draggable" style="cursor: move">'+this.definefooter+'<div id="thumbImage"></div>'+this.definefooter+'</div>')
		document.write('<div id="thumbLoading">'+this.defineLoading+'</div>');
		this.thumbBox=document.getElementById("thumbBox");
		this.thumbImage=document.getElementById("thumbImage"); //Reference div that holds the shown image
		this.thumbLoading=document.getElementById("thumbLoading"); //Reference "loading" div that will be shown while image is fetched
		this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body; //create reference to common "body" across doctypes
	},


	centerDiv:function(divobj){ //Centers a div element on the page
		var ie=document.all && !window.opera;
		var dom=document.getElementById;
		var scroll_top=(ie)? this.standardbody.scrollTop : window.pageYOffset;
		var scroll_left=(ie)? this.standardbody.scrollLeft : window.pageXOffset;
		var docwidth=(ie)? this.standardbody.clientWidth : window.innerWidth-this.scrollbarwidth;
		var docheight=(ie)? this.standardbody.clientHeight: window.innerHeight;
		var docheightcomplete=(this.standardbody.offsetHeight>this.standardbody.scrollHeight)? this.standardbody.offsetHeight : this.standardbody.scrollHeight; //Full scroll height of document
		var objwidth=divobj.offsetWidth; //width of div element
		var objheight=divobj.offsetHeight; //height of div element
		var topposition=(docheight>objheight)? scroll_top+docheight/2-objheight/2+"px" : scroll_top+10+"px"; //Vertical position of div element: Either centered, or if element height larger than viewpoint height, 10px from top of viewpoint
		divobj.style.left=docwidth/2-objwidth/2+"px"; //Center div element horizontally
		divobj.style.top=Math.floor(parseInt(topposition))+"px";
		divobj.style.zIndex="99999";
		divobj.style.visibility="visible";
	},
	
	showthumbBox:function(){ //Show ThumbBox div
		thumbnailviewer.thumbLoading.style.visibility="hidden"; //Hide "loading" div
		this.centerDiv(this.thumbBox);
		if (this.enableAnimation) { //If fading animation enabled
			this.currentopacity=0.1; //Starting opacity value
			this.opacitytimer=setInterval("thumbnailviewer.opacityanimation()", 20);
		}
	},
	
	loadimage:function(link){ //Load image function that gets attached to each link on the page with rel="thumbnail"
		if (this.thumbBox.style.visibility=="visible") //if thumbox is visible on the page already
			this.closeit(); //Hide it first (not doing so causes triggers some positioning bug in Firefox
		var imageHTML='<img id="imgShow" src="'+link.getAttribute("href")+'" style="'+this.opacitystring+'" />'; //Construct HTML for shown image
		if (this.enableTitle && link.getAttribute("title")) //Use title attr of the link as description?
			imageHTML+='<br />'+link.getAttribute("title");
		this.centerDiv(this.thumbLoading); //Center and display "loading" div while we set up the image to be shown
		this.thumbImage.innerHTML=imageHTML; //Populate thumbImage div with shown image's HTML (while still hidden)
		this.featureImage=this.thumbImage.getElementsByTagName("img")[0]; //Reference shown image itself
		if (this.featureImage.complete) {
			resizeImage();
			thumbnailviewer.showthumbBox();
		}
		else {
			this.featureImage.onload=function() { //When target image has completely loaded
				resizeImage();
				thumbnailviewer.showthumbBox(); //Display "thumbbox" div to the world!
			};
		}		
		if (document.all && !window.createPopup) //Target IE5.0 browsers only. Address IE image cache not firing onload bug: panoramio.com/blog/onload-event/
			this.featureImage.src=link.getAttribute("href");
		this.featureImage.onerror = function() { //If an error has occurred while loading the image to show
			thumbnailviewer.thumbLoading.style.visibility="hidden"; //Hide "loading" div, game over
		};
		addRightClickMenu();
	},
	
	loadimageInPopup:function(link){ //Load image function that gets attached to each link on the page with rel="thumbnail"
		var imageHTML='<center><img id="imgShow" src="'+link.getAttribute("href")+'" style="'+this.opacitystring+'" /></center>'; //Construct HTML for shown image
		if (this.enableTitle && link.getAttribute("title")) //Use title attr of the link as description?
			imageHTML+='<br />'+link.getAttribute("title");
		var win = window.open('','','width=900, height=900');
		win.document.write(imageHTML);
		win.focus();
	},
	
	setimgopacity:function(value){ //Sets the opacity of "thumbimage" div per the passed in value setting (0 to 1 and in between)
		var targetobject=this.featureImage;
		if (targetobject.filters && targetobject.filters[0]) { //IE syntax
			if (typeof targetobject.filters[0].opacity=="number") //IE6
				targetobject.filters[0].opacity=value*100;
			else //IE 5.5
				targetobject.style.filter="alpha(opacity="+value*100+")";
		}
		else if (typeof targetobject.style.MozOpacity!="undefined") //Old Mozilla syntax
			targetobject.style.MozOpacity=value;
		else if (typeof targetobject.style.opacity!="undefined") //Standard opacity syntax
			targetobject.style.opacity=value;
		else //Non of the above, stop opacity animation
			this.stopanimation();
	},
	
	opacityanimation:function(){ //Gradually increase opacity function
		this.setimgopacity(this.currentopacity);
		this.currentopacity+=0.1;
		if (this.currentopacity>1)
			this.stopanimation();
	},
	
	stopanimation:function(){
		if (typeof this.opacitytimer!="undefined")
			clearInterval(this.opacitytimer);
	},
	
	
	closeit:function(){ //Close "thumbbox" div function
		this.stopanimation();
		this.thumbBox.style.visibility="hidden";
		this.thumbImage.innerHTML="";
		this.thumbBox.style.left="-2000px";
		this.thumbBox.style.top="-2000px";
	},
	
	cleanup:function(){ //Clean up routine on page unload
		this.thumbLoading=null;
		if (this.featureImage) 
			this.featureImage.onload=null;
		this.featureImage=null;
		this.thumbImage=null;
		for (var i=0; i<this.targetlinks.length; i++)
			this.targetlinks[i].onclick=null;
		this.thumbBox=null;
	},
	
	dotask:function(target, functionref, tasktype){ //assign a function to execute to an event handler (ie: onunload)
		var tasktype=(window.addEventListener)? tasktype : "on"+tasktype;
		if (target.addEventListener)
			target.addEventListener(tasktype, functionref, false);
		else if (target.attachEvent)
			target.attachEvent(tasktype, functionref);
	},
	
	init:function(){ //Initialize thumbnail viewer script by scanning page and attaching appropriate function to links with rel="thumbnail"
		if (!this.enableAnimation)
			this.opacitystring="";
		var pagelinks=document.getElementsByTagName("a");
		for (var i=0; i<pagelinks.length; i++) { //BEGIN FOR LOOP
			if (pagelinks[i].getAttribute("rel") && pagelinks[i].getAttribute("rel")=="thumbnail"){ //Begin if statement
				pagelinks[i].onclick=function(){
					if (typeof(showImagesInWinPopup) !== 'undefined' && showImagesInWinPopup) {
						thumbnailviewer.loadimageInPopup(this);
					} else {
						thumbnailviewer.stopanimation(); //Stop any currently running fade animation on "thumbbox" div before proceeding
						thumbnailviewer.loadimage(this); //Load image
					}
					return false;
				};
				this.targetlinks[this.targetlinks.length]=pagelinks[i]; //store reference to target link
			} //end if statement
		} //END FOR LOOP
		//Reposition "thumbbox" div when page is resized
		this.dotask(window, function(){if (thumbnailviewer.thumbBox.style.visibility=="visible") thumbnailviewer.centerDiv(thumbnailviewer.thumbBox);}, "resize");
	} //END init() function
};

thumbnailviewer.createthumbBox(); //Output HTML for the image thumbnail viewer
thumbnailviewer.dotask(window, function(){thumbnailviewer.init();}, "load"); //Initialize script on page load
thumbnailviewer.dotask(window, function(){thumbnailviewer.cleanup();}, "unload");
/*
 * $Id: utils.js 590812 2007-10-31 20:32:54Z apetrelli $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var StrutsUtils = {};

// gets an object with validation errors from string returned by 
// the ajaxValidation interceptor
StrutsUtils.getValidationErrors = function(data) {
  if(data.indexOf("/* {") == 0) {
    return eval("( " + data.substring(2, data.length - 2) + " )");
  } else {
    return null;
  }  
};

StrutsUtils.clearValidationErrors = function(form) {
  var firstNode = StrutsUtils.firstElement(form);
  var xhtml = firstNode.tagName.toLowerCase() == "table";
  
  if(xhtml) {
    clearErrorMessagesXHTML(form);
    clearErrorLabelsXHTML(form);
  } else {
    clearErrorMessagesCSS(form);
    clearErrorLabelsCSS(form);
  }
};  

// shows validation errors using functions from xhtml/validation.js
// or css_xhtml/validation.js
StrutsUtils.showValidationErrors = function(form, errors) {
  StrutsUtils.clearValidationErrors(form, errors);

  var firstNode = StrutsUtils.firstElement(form);
  var xhtml = firstNode.tagName.toLowerCase() == "table";  
  if(errors.fieldErrors) {
    for(var fieldName in errors.fieldErrors) {
      for(var i = 0; i < errors.fieldErrors[fieldName].length; i++) {
        if(xhtml) {
          addErrorXHTML(form.elements[fieldName], errors.fieldErrors[fieldName][i]);
        } else {
          addErrorCSS(form.elements[fieldName], errors.fieldErrors[fieldName][i]);
        }  
      }
    }
  }
};

StrutsUtils.firstElement  = function(parentNode, tagName) {
  var node = parentNode.firstChild;
  while(node && node.nodeType != 1){
    node = node.nextSibling;
  }
  if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
    node = StrutsUtils.nextElement(node, tagName);
  }
  return node;  
};

StrutsUtils.nextElement = function(node, tagName) {
  if(!node) { return null; }
  do {
    node = node.nextSibling;
  } while(node && node.nodeType != 1);

  if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
    return StrutsUtils.nextElement(node, tagName);
  }
  return node;  
};

StrutsUtils.previousElement = function(node, tagName) {
  if(!node) { return null; }
  if(tagName) { tagName = tagName.toLowerCase(); }
  do {
    node = node.previousSibling;
  } while(node && node.nodeType != 1);
  
  if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
    return StrutsUtils.previousElement(node, tagName);
  }
  return node;  
};

StrutsUtils.addOnLoad = function(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      oldonload();
      func();
    }
  }
};

StrutsUtils.addEventListener = function(element, name, observer, capture) {
  if (element.addEventListener) {
    element.addEventListener(name, observer, false);
  } else if (element.attachEvent) {
    element.attachEvent('on' + name, observer);
  }
};

function dumpProps(obj, parent) {
	// Go through all the properties of the passed-in object 
	for ( var i in obj) {
		// if a parent (2nd parameter) was passed in, then use that to 
		// build the message. Message includes i (the object's property name) 
		// then the object's property value on a new line 
		if (parent) {
			var msg = parent + "." + i + "\n" + obj[i];
		} else {
			var msg = i + "\n" + obj[i];
		}
		// Display the message. If the user clicks "OK", then continue. If they 
		// click "CANCEL" then quit this level of recursion 
		if (!confirm(msg)) {
			return;
		}
		// If this property (i) is an object, then recursively process the object 
		if (typeof obj[i] == "object") {
			if (parent) {
				dumpProps(obj[i], parent + "." + i);
			} else {
				dumpProps(obj[i], i);
			}
		}
	}
}
