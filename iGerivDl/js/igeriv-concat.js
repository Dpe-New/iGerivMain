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
			$("#close").trigger('click');
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

function PlaySound(soundObj) {
	if (allowBeep == "true") {
		try {
		    var sound = document.getElementById(soundObj);
		    if (sound) {
		    	try {
		    		sound.Play();
		    	} catch(e) {
		    		sound.play();
		    	}
		    }
		} catch(e) {}
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
function setFormAction(formId, action, metod, msgboxid, isSynchronous, context, callback, validationCallback, showOkMessage, errCallback) {
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
	formSubmit(formId, msgboxid, action, synch, context, callback, showOkMessage, errCallback);
}

function formSubmitMultipartAjax(formId, handleAsType, successCallback, validationCallback, showOkMessage) {
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

function formSubmit(formid, msgboxid, action, synch, context, callback, showOkMessage, errCallback) {
	dojo.xhrPost({
		form: formid,
		handleAs: "text",	
		headers: { "Content-Type": "application/x-www-form-urlencoded; charset=utf-8" },
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
				});
				if (typeof(errCallback) === 'function') {
					errCallback();
				}
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
	cal_obj2.default_language = "it";
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
	} else if (getBrowser().indexOf("OPERA") != -1 || (getBrowser().indexOf("SAFARI") != -1 && getBrowser().indexOf("CHROME") == -1)) {
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
		&& getBrowser().indexOf("SAFARI") == -1) {
		msg = browserNotSupported;	
	} 
	if ((getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 7) || 
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
	var browserWidth = $(window).width();
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
						    	setTimeout(function() {$textField.focus();}, 100);
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
						    	setTimeout(function() {$textField.focus();}, 100);
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

function manageBarcodeKeyDown(evt) {
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
			if (pk.length > 0) {
				var increment = false;						
				if ($("#autoincrement").length > 0) {
					if ($("#autoincrement").attr("checked") == true) {
						increment = true;
					}
				}			
				if (fieldName.length > 0) {
					fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
				}
				selectRowByBarcode(pk.val(), fieldName, increment);
			} else {
				noBarcodeFoundAction(fieldVal);
			}
		}
	}
} 

function selectRowByBarcode(id, fieldName, increment) {	
	if (id != '' && $('#' + id)) {
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
	currField.select();
	if (!increment) {
		currField.focus();
	} else {
		setTimeout(function() { setTotals(currField); doAfterAutoIncremetActions(currField); $("#barcode").focus(); },100);
	}
	var bodyelem = '';
	if ($.browser.safari) { 
		bodyelem = $("body"); 
	} else { 
		bodyelem = $("html");
	}
	var topPos = row.offset().top;
	bodyelem.scrollTop(topPos);
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
	$("#codCliente").attr("disabled", false);
	return true;
}

function getFirstInvalidField($fields) {
	$fields.each(function() {
		var $formField = $(this);
		var fVal = $formField.val().trim();
		if (fVal != '' && !bINT(fVal)) {
			$formField.css({"borderColor":"red"}); 
			firstInvalidId = $formField.attr("id");
			return false;			
		} else {
			$formField.css({"borderColor":"#999"});
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
	$("#prodottoEdicola\\.aliquota", window.parent.document).attr("disabled", false);
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
			dojo.xhrGet({
				url: qs,	
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				preventCache: true,
				handle: function(data,args) {	
					if (args.xhr.status == 200) {
						jAlert(msgInvioEmailVerifica, msgAvviso, function() {
							window.location = appContext + namespace + "/j_spring_security_logout";
						});
					} else {
						jAlert(msgErroreInvioRichiesta, msgAvviso, function() {
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
						jAlert(msgInvioEmailVerifica, msgAvviso, function() {
							window.location = appContext + namespace + "/j_spring_security_logout";
						});
					} else {
						jAlert(msgErroreInvioRichiesta);
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

function promptPrivacyEdicola() {
	jPromptPrivacy(privacyMessageEdicola,'','', email, msgAccettoPrivacy, function(result) {
		if (result != null) {
			var email = result.split("|")[0];
			dojo.xhrGet({
				url: appContext + namespace + '/pubblicazioniRpc_sendValidationEmail.action?email=' + email + "&url=" + url,	
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				preventCache: true,
				handle: function(data,args) {	
					if (args.xhr.status == 200) {
						jAlert(msgInvioEmailVerifica);
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
				if ($("#BollaControlloForm input:hidden[name^=modificato][value=true]").length > 0) {
					validateFields('BollaControlloForm'); 
					setFieldsToSave(true) && setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 'messageDiv', false, '', function() {setFieldsToSave(false);}, '', false);
					return false;
				}
				break;
			}
			case BOLLA_RESA: {
				if ($("#BollaResaForm input:hidden[name^=modificato][value=true]").length > 0) {
					validateFields('BollaResaForm'); 
					setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_save.action', '', 'messageDiv', false, '', function() {setFieldsToSave(false); deleteEmptyRowsFuoriVoce();}, '', false);
					return false;
				}
				break;
			}
		}
	};
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
				if (data != '' && ($div.length == 0 || $div.is(":hidden"))) {
					showAlertMessaggioAltaPriorita(data);
				}
			});
		}
	};
}

function getNoteRivendita(noteRivendita, noteRivenditaCpu) {
	return noteRivendita.length > 0 ? noteRivendita : (typeof(noteRivenditaCpu) != 'undefined' && noteRivenditaCpu.length > 0) ? noteRivenditaCpu : msgNoteRivendita;
}

function showAlertMessaggioAltaPriorita(messaggioNonLetto) {
	var u = appContext + namespace + "/avviso_showAvvisoNuovoMessaggio.action?messagePk=" + messaggioNonLetto;
	var div = $("<div id='parentMessageDiv' class='new_message_popup_block'>");
	div.css({'width':'150px','height':'120px','text-align':'center','margin-left':'43%','margin-right':'auto','font-weight':'bold','font-size':'100%','position':'absolute','border':'1px solid black','z-index':'999999'});
	$('body').prepend(div);
	div.hide();
	div.load(u, {limit: 25}, function(){
		div.slideDown('slow');
	});
}

function addNote(titolo, idtn, cpu) {
	var noteCpu = $("#noteRivenditaCpu" + cpu).val();
	var noteIdtn = $("#noteRivendita" + idtn).val();
	var isFissa = typeof(noteCpu) != 'undefined' && noteCpu.trim().length > 0;
	var noteRivendita = typeof(noteIdtn) != 'undefined' && noteIdtn.trim().length > 0 ? noteIdtn : noteCpu;
	jPromptNote(msgNotePubblicazione.replace("{0}", titolo), noteRivendita, isFissa, msgNoteRivendita, msgNoteFisse, function(res) {
		var cpu1 = cpu;
		if (res != null) {
			var note = res.split("|")[0].trim();
			var notaFissa = res.split("|")[1].trim();
			dojo.xhrGet({
				url: appContext + namespace + "/pubblicazioniRpc_saveNotaRivendita.action?idtn=" + idtn + "&notaRivendita=" + escape(note) + "&cpu=" + cpu1 + "&notaFissa=" + notaFissa,	
				preventCache: true,
				handleAs: "text",				
				handle: function(data,args) {
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
		try {
			dojo.xhrGet({
				url: theUrl,
				handleAs: "text",				
				preventCache: true,
				load: function(data,args) {	
					if (data != 'undefined' && data.indexOf("j_spring_security_check") != -1) {
					   $.alerts.dialogClass = "style_1";
						jAlert(sessionScadutaRedirectLoginMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							window.location.href = appContext + namespace + '/j_spring_security_logout';
						});
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
			   url: theUrl, 
			   load: function (type, data, evt) { 
				   if (data != 'undefined' && data.indexOf("j_spring_security_check") != -1) {
					   $.alerts.dialogClass = "style_1";
						jAlert(sessionScadutaRedirectLoginMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							window.location.href = appContext + namespace + '/j_spring_security_logout';
						});
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
			   mimetype: "text/plain" 
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
    
    $.fn.sort_select_box = function(){
       var my_options = $("#" + this.attr('id') + ' option');
       my_options.sort(function(a,b) {
            if (a.text > b.text) return 1;
            else if (a.text < b.text) return -1;
            else return 0;
       });
       $(this).empty().append( my_options );
       $("#"+this.attr('id')+" option").attr('selected', false);
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
'Lunedì',
'Martedì',
'Mercoledì',
'Giovedì',
'Venerdì',
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

	arrowimage: {classname: 'downarrowclass', src: '/app_img/down.gif', leftpadding: 5}, //customize down arrow image
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
var selectionText = "";
var currentRTE;
var allRTEs = "";

var isIE;
var isGecko;
var isSafari;
var isKonqueror;

var imagesPath;
var includesPath;
var cssFile;


function initRTE(imgPath, incPath, css) {
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
	
	var linkURL = prompt('Enter a URL:', 'http://');
	setRange(rte);
	selectionText = rng;
	var htmlInsertLink = '<a href="' + linkURL + '" target="_blank">' + selectionText + '</a>';
	
	var oRTE;
	if (document.all) {
		oRTE = frames[rte];
	} else {
		oRTE = document.getElementById(rte).contentWindow;
	}
	oRTE.document.execCommand('insertHTML', false, htmlInsertLink);
}

function rteCommandCustom(rte, command, option) {
	//function to perform command
	try {
		var oRTE = document.getElementById(rte);
		if (oRTE.contentWindow) {
			oRTE.contentWindow.focus();
			oRTE.contentWindow.document.execCommand(command, false, option);
		} else {
			oRTE.document.focus();
		  	oRTE.document.execCommand(command, false, option);
		}
		loopCnt = 0;
		
		return false;
	} catch (e) {
		if (debugMode) alert(e);
		//Keep looping until able to set.
		if (loopCnt < maxLoops) {
			setTimeout("rteCommand('" + rte + "', '" + command + "', '" + option + "');", 100);
			loopCnt += 1;
		} else {
			alert("Error executing command.");
		}
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
	//function to add image
	imagePath = prompt('Enter Image URL:', 'http://');				
	if ((imagePath != null) && (imagePath != "")) {
		rteCommand(rte, 'InsertImage', imagePath);
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
					thumbnailviewer.stopanimation(); //Stop any currently running fade animation on "thumbbox" div before proceeding
					thumbnailviewer.loadimage(this); //Load image
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
