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
	}else if ($('#popup_name_det').is(':visible')) {
		forms = $('#popup_name_dettaglio_profilo').find("form");
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
		if ($('#popup_name').is(':visible') || $('#popup_name_det').is(':visible') ||$('#popup_name_dettaglio_profilo').is(':visible') || $("#treeContiContainer").is(':visible')) {
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
	    
	    
	    if ( $('#popup_name_dettaglio_profilo').is(':visible') ) {
	    	if ($('#downloadToken3').length > 0) {
	    		$('#downloadToken3').val(token);
	    	} else {
	    		$('input[name="downloadToken3"]').val(token);
	    	}
	    	if (($('#downloadToken3').length > 0 && $('#downloadToken3').val().length > 0)
		    		|| ($('input[name="downloadToken3"]').length > 0 && $('input[name="downloadToken3"]').val().length > 0)) {
	    		ray1.ajax();
	    	}
	    } else if ( $('#popup_name_det').is(':visible') ) {
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
				var errMsgExists = ($('ul.errormessage', $doc).length > 0) || ($('ul.errorMessage', $doc).length > 0);
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

function openDivDettaglioProfilo(popID, popWidth, popHeight, url, bgColor, type, context, callback) {
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
})(jQuery);

