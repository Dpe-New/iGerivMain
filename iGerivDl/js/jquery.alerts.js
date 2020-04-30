// jQuery Alert Dialogs Plugin
//
// Version 1.1
//
// Cory S.N. LaViska
// A Beautiful Site (http://abeautifulsite.net/)
// 14 May 2009
//
// Visit http://abeautifulsite.net/notebook/87 for more information
//
// Usage:
//		jAlert( message, [title, callback] )
//		jConfirm( message, [title, callback] )
//		jPrompt( message, [value, title, callback] )
// 
// History:
//
//		1.00 - Released (29 December 2008)
//
//		1.01 - Fixed bug where unbinding would destroy all resize events
//
// License:
// 
// This plugin is dual-licensed under the GNU General Public License and the MIT License and
// is copyright 2008 A Beautiful Site, LLC. 
//
var si;
var no;
var annulla;
(function($) {
	
	$.alerts = {
		
		// These properties can be read/written by accessing $.alerts.propertyName from your scripts at any time
		
		verticalOffset: -75,                // vertical offset of the dialog from center screen, in pixels
		horizontalOffset: 0,                // horizontal offset of the dialog from center screen, in pixels/
		repositionOnResize: true,           // re-centers the dialog on window resize
		overlayOpacity: .01,                // transparency level of overlay
		overlayColor: '#FFF',               // base color of overlay
		draggable: true,                    // make the dialogs draggable (requires UI Draggables plugin)
		okButton: '&nbsp;OK&nbsp;',         // text for the OK button
		siButton: '&nbsp;' + si + '&nbsp;',         // text for the Yes button
		noButton: '&nbsp;' + no + '&nbsp;',         // text for the No button
		cancelButton: '&nbsp;' + annulla + '&nbsp;', // text for the Cancel button
		esciButton: '&nbsp;' + esci + '&nbsp;', // text for the Logout button
		dialogClass: null,                  // if specified, this class will be applied to all dialogs
		
		// Public methods
		
		alert: function(message, title, callback) {
			if( title == null ) title = avvisoMsg;
			$.alerts._show(title, message, null, 'alert', function(result) {
				if( callback ) callback(result);
			});
		},
		
		alertWithCheckboxCallback: function(message, title, callback, checkboxLabel, checkboxCallback) {
			if( title == null ) title = avvisoMsg;
			$.alerts._showAlertWithCheckboxCallback(title, message, function(result) {
				if( callback ) callback(result);
			}, 'alert', checkboxLabel, function() {
				if( checkboxCallback ) checkboxCallback();
			});
		},
		
		confirmWithCheckboxCallback: function(message, title, callback, checkboxLabel, checkboxCallback) {
			if( title == null ) title = avvisoMsg;
			$.alerts._showConfirmWithCheckboxCallback(title, message, function(result) {
				if( callback ) callback(result);
			}, 'alert', checkboxLabel, function() {
				if( checkboxCallback ) checkboxCallback();
			});
		},
		
		confirm: function(message, title, callback, yesNoOptions, yesSelected) {
			if( title == null ) title = confermaMsg;
			$.alerts._show(title, message, null, 'confirm', function(result) {
				if( callback ) callback(result);
			}, null, null, null, yesNoOptions, yesSelected);
		},
		
		radio: function(message, title, radioName, labels, values, callback) {
			if( title == null ) title = confermaMsg;
			$.alerts._showRadio(title, message, radioName, labels, values, function(result) {
				if( callback ) callback(result);
			});
		},
		
		confirmEsportaResoPdf: function(message, title, callback) {
			if( title == null ) title = confermaMsg;
			$.alerts._showEsportaResoPdf(title, message, null, 'confirm', function(result) {
				if( callback ) callback(result);
			});
		},
			
		prompt: function(message, value, title, callback, multiline, rowsAllowed, colsAllowed) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._show(title, message, value, 'prompt', function(result) {
				if( callback ) callback(result);
			}, multiline, rowsAllowed, colsAllowed);
		},
		promptNote: function(message, value, value1, title, title1, callback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showPromptNote(title, title1, message, value, value1, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		prompt2Fields: function(message, value1, value2, title, titleField1, titleField2, callback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._show2Fields(title, message, value1, value2, titleField1, titleField2, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		promptPrivacy: function(message, value, title, titleField1, titleField2, callback, validationCallback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showPrivacy(title, message, value, titleField1, titleField2, 'prompt', function(result) {
				if( callback ) callback(result);
			}, validationCallback);
		},
		promptPrivacyCliente: function(message, value, title, msgAccettoPrivacy, params, callback, validationCallback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showPrivacyCliente(title, message, value, msgAccettoPrivacy, params, 'prompt', function(result) {
				if( callback ) callback(result);
			}, validationCallback);
		},
		promptPrivacyEdicolaStarter: function(message, value, title, msgAccettoPrivacy, params, callback, validationCallback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showPrivacyEdicolaStarter(title, message, value, msgAccettoPrivacy, params, 'prompt', function(result) {
				if( callback ) callback(result);
			}, validationCallback);
		},
		promptSmartEdicola: function(message, title, params, callback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showSmartEdicola(title, message, params, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		promptFile: function(message, title, params, callback) {
			if( title == null ) title = richiestaInfoMsg;
			$.alerts._showPromptFile(title, message, params, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		
		// Private methods
		
		_showPromptNote: function(title, title1, msg, value, value1, type, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").append('<br /><input type="text" size="30" id="popup_prompt" /><br />' + title1 + '&nbsp;&nbsp;<input type="checkbox" name="popup_prompt1" id="popup_prompt1" />').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div><br>');
			
			$("#popup_prompt").width( $("#popup_message").width() );
			$("#popup_ok").click( function() {
				var val = $("#popup_prompt").val() + '|' + $("#popup_prompt1").is(":checked");
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null, null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_prompt, #popup_ok, #popup_cancel").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
			});
			if( value ) $("#popup_prompt").val(value);
			$("#popup_prompt1").attr("checked", value1);
			$("#popup_prompt").focus().select();
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_show2Fields: function(title, msg, value1, value2, titleField1, titleField2, type, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").append('<br /><br /><div id="popup_fieldtitle1">&nbsp;<input type="text" size="25" id="popup_prompt" /></div><br /><div id="popup_fieldtitle2">&nbsp;<input type="text" size="25" id="popup_prompt1" /></div>').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
			$("#popup_fieldtitle1").prepend(titleField1).addClass($.alerts.dialogClass);
			$("#popup_fieldtitle2").prepend(titleField2).addClass($.alerts.dialogClass);
			
			$("#popup_prompt").width( $("#popup_message").width() );
			$("#popup_prompt1").width( $("#popup_message").width() );
			$("#popup_ok").click( function() {
				var val = $("#popup_prompt").val() + "|" + $("#popup_prompt1").val();
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_prompt1, #popup_ok, #popup_cancel").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
			});
			$("#popup_prompt").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_prompt1").focus();
			});
			if( value1 ) $("#popup_prompt").val(value1);
			if( value2 ) $("#popup_prompt1").val(value2);
			$("#popup_prompt").focus().select();
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showPrivacy: function(title, msg, value, titleField1, titleField2, type, callback, validationCallback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").append('<br /><br /><div id="popup_fieldtitle1">&nbsp;&nbsp;&nbsp;<input type="text" size="50" style="font-size:100%" id="email" /></div><br/><br/><div id="popup_fieldtitle2">&nbsp;&nbsp;&nbsp;<input type="checkbox" name="popup_prompt1" id="popup_prompt1" /></div>').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div><br>');
			$("#popup_fieldtitle1").prepend(titleField1).addClass($.alerts.dialogClass);
			$("#popup_fieldtitle2").prepend(titleField2).addClass($.alerts.dialogClass);
			
			$("#popup_ok").click( function() {
				var val = $("#email").val() + "|" + $("#popup_prompt1").is(':checked');
				if (typeof(validationCallback) === 'function') {
					if (!validationCallback()) {
						return false;
					}
				}
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_prompt1, #popup_ok, #popup_cancel").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
			});
			$("#email").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_prompt1").focus();
			});
			$("#email").focus().select();
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showPrivacyCliente: function(title, msg, value, msgAccettoPrivacy, params, type, callback, validationCallback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:100%; color:#808080;">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			var jsonParams = jQuery.parseJSON(params);
			var html = '';
			html += '<br /><br />';
			html += '<div style="width:480px;">';
			html += '<div style="height:30px;"><div id="divDtNasc" style="float:left;"><input type="text" size="2" maxlength="2" style="color:#808080; font-size:100%" id="dtNascG" class="numericOnly" value="' + jsonParams.giorno + '"/>&nbsp;&nbsp;<input type="text" size="2" maxlength="2" class="numericOnly" style="color:#808080; font-size:100%" id="dtNascM" value="' + jsonParams.mese + '"/>&nbsp;&nbsp;<input type="text" size="4" maxlength="4" style="color:#808080; font-size:100%" id="dtNascA" class="numericOnly" value="' + jsonParams.anno + '"/></div><div id="divSesso" style="float:left; margin-left:20px"><select name="sesso" id="sesso" style="color:#808080; width:100px"></select></div></div>';
			html += '<div style="height:30px;"><div id="divLavoro" style="float:left;"><select name="lavoro" id="lavoro" style="color:#808080; width:180px"></select></div><div id="divScuola" style="color:#80 margin-left:20px"><select name="scuola" id="scuola" style="color:#808080; width:180px"></select></div></div>';
			html += '<div style="height:30px;"><div id="divEmail"><input type="text" style="color:#808080;" value="' + jsonParams.email + '" size="35" id="email" /></div></div>';
			html += '<div style="height:30px;"><div id="popup_fieldtitle2" style="padding:3px"><input type="checkbox" name="popup_prompt1" id="popup_prompt1" /></div>';
			html += '</div>';
			
			$("#popup_message").append(html).after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /></div><br>');
			$("#divDtNasc").prepend(jsonParams.dtNasc + "&nbsp;&nbsp;").addClass($.alerts.dialogClass);
			$("#popup_fieldtitle2").prepend(msgAccettoPrivacy + "&nbsp;&nbsp;").addClass($.alerts.dialogClass);
			$("#popup_message").find("a").css({"text-decoration":"none"});
			$(".numericOnly").numeric(false);
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_sesso.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#sesso").html(items.join(''));
			});
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_lavoro.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#lavoro").html(items.join(''));
			});
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_scuola.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#scuola").html(items.join(''));
			});
			
			$("#popup_ok").click( function() {
				var val = '{';
				val += '"dtG":"' + $("#dtNascG").val().trim() + '",';
				val += '"dtM":"' + $("#dtNascM").val().trim() + '",';
				val += '"dtA":"' + $("#dtNascA").val().trim() + '",';
				val += '"sesso":"' + $("#sesso").val().trim() + '",';
				val += '"lavoro":"' + $("#lavoro").val().trim() + '",';
				val += '"scuola":"' + $("#scuola").val().trim() + '",';
				val += '"email":"' + $("#email").val().trim() + '"';
				val += '}';
				if (typeof(validationCallback) === 'function') {
					if (!validationCallback()) {
						return false;
					}
				}
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_prompt1, #popup_ok, #popup_cancel").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
			});
			$("#popup_prompt").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_prompt1").focus();
			});
			$("#popup_prompt").focus().select();
			
		},
		
		_showPrivacyEdicolaStarter: function(title, msg, value, msgAccettoPrivacy, params, type, callback, validationCallback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			var browserHeight = $(window).height();
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:' + (browserHeight < 700 ? '90' : '100') + '%; color:#808080; width:500px:">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition(-220);
			$.alerts._maintainPosition(true);
			
			var jsonParams = jQuery.parseJSON(params);
			var html = '';
			html += '<center>';
			html += '<fieldset style="width:450px; height:150px;">';
			html += '<div style="height:25px; white-space:nowrap"><div id="divRagioneSociale"><input type="text" style="color:#808080;" value="' + jsonParams.ragioneSociale + '" size="35" id="ragioneSociale" /></div></div>';
			html += '<div style="height:25px; white-space:nowrap"><div id="divTipoLocalita" style="float:left;"><select name="tipoLocalita" id="tipoLocalita" style="color:#808080; width:80px"></select>&nbsp;&nbsp;<input type="text" size="30" maxlength="100" style="color:#808080;" id="indirizzo" value="' + jsonParams.indirizzo + '"/>&nbsp;&nbsp;<input type="text" size="3" maxlength="5" style="color:#808080;" id="numero" value="' + jsonParams.numCivico + '"/></div></div>';
			html += '<div style="height:25px; white-space:nowrap"><div id="divCap" style="float:left;"><input type="text" size="5" maxlength="5" style="color:#808080;" id="cap" class="numericOnly" value="' + jsonParams.cap + '"/>&nbsp;&nbsp;<input type="text" size="30" maxlength="100" style="color:#808080;" id="localita" value="' + jsonParams.localita + '"/>&nbsp;&nbsp;<select name="provincia" id="provincia" style="color:#808080; width:60px"></select></div></div>';
			html += '<div style="height:25px; white-space:nowrap"><div id="divTelefono" style="float:left;"><input type="text" size="15" maxlength="24" style="color:#808080;" id="telefono" class="numericOnly" value="' + jsonParams.telefono + '"/>&nbsp;&nbsp;<input type="text" size="15" maxlength="24" style="color:#808080;" id="cellulare" class="numericOnly" value="' + jsonParams.cellulare + '"/>&nbsp;&nbsp;<select name="tipoEdicola" id="tipoEdicola" style="color:#808080; width:160px"></select></div></div>';
			html += '<div style="height:25px; white-space:nowrap"><div id="divEmail"><input type="text" style="color:#808080;" value="' + jsonParams.email + '" size="35" id="email" /></div></div>';
			html += '<div style="height:25px; white-space:nowrap"><div id="popup_fieldtitle2" style="padding:3px"><input type="checkbox" name="popup_prompt1" id="popup_prompt1" /></div></div>';
			html += '</fieldset></center>';
			html += '<div style="width:500px;">' + jsonParams.testoPrivacyEdicolaStarter2 + '</div>';
			
			$("#popup_message").append(html).after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" style="width:100px"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="' + $.alerts.esciButton + '" id="popup_esci" style="width:100px"/></div><br>');
			$("#popup_fieldtitle2").prepend(msgAccettoPrivacy + "&nbsp;&nbsp;").addClass($.alerts.dialogClass);
			$("#popup_message").find("a").css({"text-decoration":"none"});
			$(".numericOnly").numeric(false);
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_tipoLocalita.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#tipoLocalita").html(items.join(''));
			});
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_province.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#provincia").html(items.join(''));
			});
			
			$.getJSON(appContext + namespace + "/pubblicazioniRpc_tipiEdicola.action", function(data) {
				var items = [];
				$.each(data, function(key, val) {
				    items.push('<option value="' + key + '">' + val + '</option>');
				});
				$("#tipoEdicola").html(items.join(''));
			});
			
			$("#popup_ok").click( function() {
				var val = '{';
				val += '"ragioneSociale":"' + $("#ragioneSociale").val().trim() + '",';
				val += '"tipoLocalita":"' + $("#tipoLocalita").val().trim() + '",';
				val += '"indirizzo":"' + $("#indirizzo").val().trim() + '",';
				val += '"numero":"' + $("#numero").val().trim() + '",';
				val += '"cap":"' + $("#cap").val().trim() + '",';
				val += '"localita":"' + $("#localita").val().trim() + '",';
				val += '"provincia":"' + $("#provincia").val().trim() + '",';
				val += '"telefono":"' + $("#telefono").val().trim() + '",';
				val += '"cellulare":"' + $("#cellulare").val().trim() + '",';
				val += '"tipoEdicola":"' + $("#tipoEdicola").val().trim() + '",';
				val += '"email":"' + $("#email").val().trim() + '"';
				val += '}';
				if (typeof(validationCallback) === 'function') {
					if (!validationCallback()) {
						return false;
					}
				}
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_esci").click( function() {
				window.location = appContext + namespace + "/j_spring_security_logout";
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_prompt1, #popup_ok, #popup_cancel").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
			});
			$("#popup_prompt").keydown( function(e) {
				if( e.keyCode == 13 ) $("#popup_prompt1").focus();
			});
			$("#popup_prompt").focus().select();
		},
		
		_showSmartEdicola: function(title, msg, params, type, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:100%; color:#808080;">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			var data = jQuery.parseJSON(params);
			var html = '';
			html += '<br /><br />';
			html += '<div style="width:480px;">';
			html += '<div style="height:30px;"><div id="divSmartEdicola" style="float:left;"><select name="selectClienti" id="selectClienti" style="color:#808080; width:100%">';
			$.each(data, function(index) {
				html += '<option value="' + data[index].codCliente + '">' + data[index].nome + ' ' + data[index].cognome + '</option>';
			});
			html += '</div>';
			
			$("#popup_message").append(html).after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div><br>');
			$("#popup_message").find("a").css({"text-decoration":"none"});
			
			$("#popup_ok").click( function() {
				var val = $("#selectClienti").val();
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			doOkClick = function() { $("#popup_ok").trigger('click'); }
			$("#popup_ok, #popup_cancel").keydown( function(e) {
				if (!e) {
			        e = window.event;
			    }
				if( e.keyCode == 13 ) { doOkClick(); };
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
				if( e.keyCode == 37 ) { // left arrow
					$("#popup_ok").focus();
					doOkClick = function() { $("#popup_ok").trigger('click'); }
				} 
				if( e.keyCode == 39 ) { // right arrow
					$("#popup_cancel").focus();
					doOkClick = function() { $("#popup_cancel").trigger('click'); }
				}
			});
			$("#popup_ok").focus();
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showPromptFile: function(title, msg, params, type, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:100%; color:#808080;">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			var html = '';
			html += '<br /><br />';
			html += '<div style="width:480px;">';
			html += '<div style="height:30px;"><div id="fileDiv" style="float:left;"><input type="file" name="fileField" id="fileField" /></div>';
			
			$("#popup_message").append(html).after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div><br>');
			$("#popup_message").find("a").css({"text-decoration":"none"});
			
			$("#popup_ok").click( function() {
				var val = $("#fileField").val();
				$.alerts._hide();
				if( callback ) callback( val );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback( null );
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			doOkClick = function() { $("#popup_ok").trigger('click'); }
			$("#popup_ok, #popup_cancel").keydown( function(e) {
				if (!e) {
			        e = window.event;
			    }
				if( e.keyCode == 13 ) { doOkClick(); };
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
				if( e.keyCode == 37 ) { // left arrow
					$("#popup_ok").focus();
					doOkClick = function() { $("#popup_ok").trigger('click'); }
				} 
				if( e.keyCode == 39 ) { // right arrow
					$("#popup_cancel").focus();
					doOkClick = function() { $("#popup_cancel").trigger('click'); }
				}
			});
			$("#popup_ok").focus();
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showEsportaResoPdf: function(title, msg, params, type, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:100%; color:#808080;">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.siButton + '" id="popup_yes" /> <input type="button" value="' + $.alerts.noButton + '" id="popup_no" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
			$("#popup_yes").click( function() {
				$.alerts._hide();
				if( callback ) callback(1);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_no").click( function() {
				$.alerts._hide();
				if( callback ) callback(2);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback(3);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_yes").focus();
			$("#popup_yes, #popup_no, #popup_cancel").keydown( function(e) {
				if (!e) {
			        e = window.event;
			    }
				if( e.keyCode == 13 ) { $("#popup_yes").trigger('click'); };
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
				
			});
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showRadio: function(title, msg, radioName, labels, values, callback) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container" style="font-size:100%; color:#808080;">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass("confirm");
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			var html = '<fomr id="radioForm"><div style="width:250px; float:left;">';
			for (var i = 0; i < values.length; i++) {
				html += '<div style="float:left; width:250px; text-align:left"><input type="radio" ' + ((i == 0) ? "checked" : "") + ' name="' + radioName + '" value="' + values[i] + '" style="color:#808080;" />&nbsp;&nbsp;' + labels[i] + '</div>';
			}
			html += '</div>';
			
			$("#popup_message").append(html).after('<div id="popup_panel" style="margin-top:60px"><input type="button" value="' + $.alerts.okButton + '" id="popup_yes" />  <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div></form>');
			$("#popup_yes").click( function() {
				var val = $("#radioForm input[name='" + radioName + "']:checked").val();
				$.alerts._hide();
				if( callback ) callback(val);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_cancel").click( function() {
				$.alerts._hide();
				if( callback ) callback('');
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_yes").focus();
			doOkClick = function() { $("#popup_yes").trigger('click'); }
			$("#popup_yes, #popup_cancel").keydown( function(e) {
				if (!e) {
			        e = window.event;
			    }
				if( e.keyCode == 13 ) { doOkClick(); };
				if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
				if( e.keyCode == 37 ) { // left arrow
					$("#popup_yes").focus();
					doOkClick = function() { $("#popup_yes").trigger('click'); }
				} 
				if( e.keyCode == 39 ) { // right arrow
					$("#popup_cancel").focus();
					doOkClick = function() { $("#popup_cancel").trigger('click'); }
				}
				
			});
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_showAlertWithCheckboxCallback: function(title, msg, callback, type, checkboxLabel, checkboxCallback) {
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html('<div style="width:450px"><div style="width:100%;">' + $("#popup_message").text().replace(/\n/g, '<br />') + '</div>');
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").append('<br /><br /><div id="popup_panel1" style="width:100%; float:right;">' + checkboxLabel + '&nbsp;<input type="checkbox" name="chk" id="chk" /></div>').after('<br/><div id="popup_panel" style="width:100%"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /></div></div>');
			$("#popup_ok").click( function() {
				$.alerts._hide();
				callback(true);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_ok").focus().keydown( function(e) {
				if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
			});
			$("#chk").change(function() {
				if ($(this).attr("checked") == true) {
					checkboxCallback();
				}
				setTimeout(function() {
					$("#popup_ok").focus();
				}, 100);
			});
			if (title.toUpperCase() == attenzioneMsg.toUpperCase()) {
				PlaySound('beep3');
			}
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
			
		},
		
		_showConfirmWithCheckboxCallback: function(title, msg, callback, type, checkboxLabel, checkboxCallback) {
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html('<div style="width:450px"><div style="width:100%;">' + $("#popup_message").text().replace(/\n/g, '<br />') + '</div>');
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			$("#popup_message").append('<br /><br /><div id="popup_panel1" style="width:100%; float:right;">' + checkboxLabel + '&nbsp;<input type="checkbox" name="chk" id="chk" /></div>').after('<br/><div id="popup_panel"><input type="button" value="' + $.alerts.siButton + '" id="popup_yes" /> <input type="button" value="' + $.alerts.noButton + '" id="popup_no" /></div></div>');
			$("#popup_yes").click( function() {
				$.alerts._hide();
				callback(true);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_no").click( function() {
				$.alerts._hide();
				callback(false);
				if ($("#" + lastFocusedFieldId).length > 0) {
					$("#" + lastFocusedFieldId).focus();
				}
			});
			$("#popup_yes, #popup_no").keydown( function(e) {
				if (!e) {
			        e = window.event;
			    }
				if( e.keyCode == 13 ) { $("#popup_yes").trigger('click'); };
				if( e.keyCode == 27 ) $("#popup_no").trigger('click');
				
			});
			$("#chk").change(function() {
				if ($(this).attr("checked") == true) {
					checkboxCallback();
				}
				setTimeout(function() {
					$("#popup_ok").focus();
				}, 100);
			});
			$("#popup_yes").focus();
			
			if (title.toUpperCase() == attenzioneMsg.toUpperCase()) {
				PlaySound('beep3');
			}
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
			
		},
		
		_show: function(title, msg, value, type, callback, multiline, rowsAllowed, colsAllowed, yesNoOptions, yesSelected) {
			var hasYesNoOption = (typeof(yesNoOptions) === 'undefined') ? false : yesNoOptions;
			var hasYesSelected = (typeof(yesSelected) === 'undefined') ? true : yesSelected;
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  '<div id="popup_container">' +
			    '<h1 id="popup_title"></h1>' +
			    '<div id="popup_content">' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 999999,
				padding: 0,
				margin: 0
			});
			
			$("#popup_title").text(title);
			$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			switch( type ) {
				case 'alert':
					$("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /></div>');
					$("#popup_ok").click( function() {
						$.alerts._hide();
						callback(true);
						if ($("#" + lastFocusedFieldId).length > 0) {
							$("#" + lastFocusedFieldId).focus();
						}
					});
					$("#popup_ok").focus().keydown( function(e) {
						if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
					});
					if (title.toUpperCase() == attenzioneMsg.toUpperCase()) {
						PlaySound('beep3');
					}
				break;
				case 'confirm':
					$("#popup_message").after('<div id="popup_panel"><input type="button" value="' + (hasYesNoOption ? $.alerts.siButton : $.alerts.okButton) + '" id="popup_ok" /> <input type="button" value="' + (hasYesNoOption ? $.alerts.noButton : $.alerts.cancelButton) + '" id="popup_cancel" /></div>');
					$("#popup_ok").click( function() {
						$.alerts._hide();
						if( callback ) callback(true);
						if ($("#" + lastFocusedFieldId).length > 0) {
							$("#" + lastFocusedFieldId).focus();
						}
					});
					$("#popup_cancel").click( function() {
						$.alerts._hide();
						if( callback ) callback(false);
						if ($("#" + lastFocusedFieldId).length > 0) {
							$("#" + lastFocusedFieldId).focus();
						}
					});
					if (hasYesSelected) {
						$("#popup_ok").focus();
					} else {
						$("#popup_cancel").focus();
					}
					doOkClick = function() { if (hasYesSelected) {$("#popup_ok").trigger('click');} else {$("#popup_cancel").trigger('click');} }
					$("#popup_ok, #popup_cancel").keydown( function(e) {
						if (!e) {
					        e = window.event;
					    }
						if( e.keyCode == 13 ) { doOkClick(); };
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
						if( e.keyCode == 37 ) { // left arrow
							$("#popup_ok").focus();
							doOkClick = function() { $("#popup_ok").trigger('click'); }
						} 
						if( e.keyCode == 39 ) { // right arrow
							$("#popup_cancel").focus();
							doOkClick = function() { $("#popup_cancel").trigger('click'); }
						}
					});
				break;
				case 'prompt':
					if (multiline != 'undefined' && multiline) {
						$("#popup_message").append('<br /><textarea rows="' + ((rowsAllowed != 'undefined' && rowsAllowed > 0) ? rowsAllowed : 5) + '" cols="30" maxCols="' + ((colsAllowed != 'undefined' && colsAllowed > 0) ? colsAllowed : 30) + '" wrap="off" id="popup_prompt"></textarea>').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
					} else {
						$("#popup_message").append('<br /><input type="text" size="30" id="popup_prompt" />').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
					}
					$("#popup_prompt").width( $("#popup_message").width() );
					$("#popup_ok").click( function() {
						var val = $("#popup_prompt").val();
						$.alerts._hide();
						if( callback ) callback( val );
						if ($("#" + lastFocusedFieldId).length > 0) {
							$("#" + lastFocusedFieldId).focus();
						}
					});
					$("#popup_cancel").click( function() {
						$.alerts._hide();
						if( callback ) callback( null );
						if ($("#" + lastFocusedFieldId).length > 0) {
							$("#" + lastFocusedFieldId).focus();
						}
					});
					$("#popup_prompt, #popup_ok, #popup_cancel").keydown( function(e) {
						if (multiline == 'undefined' || !multiline) {
							if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
						}
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
					});
					if( value ) $("#popup_prompt").val(value);
					$("#popup_prompt").focus().select();
				break;
			}
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
			
			$("#popup_prompt").keydown(function(event) {
				if (event.keyCode != 13 && event.keyCode != 8 && event.keyCode != 46 && event.keyCode != 37 && event.keyCode != 39) {
					if ($(this).val().split("\n").length > $(this).attr('rows')) { 
				        return false;
				    } else {
				    	var rows = $(this).val().split("\n");
				    	if (rows[rows.length - 1].length > $(this).attr('maxCols')) {
				    		return false;
						}
				    }
				}
			});
		},
		
		_hide: function() {
			$("#popup_container").remove();
			$.alerts._overlay('hide');
			$.alerts._maintainPosition(false);
		},
		
		_overlay: function(status) {
			switch( status ) {
				case 'show':
					$.alerts._overlay('hide');
					$("BODY").append('<div id="popup_overlay"></div>');
					$("#popup_overlay").css({
						position: 'absolute',
						zIndex: 99998,
						top: '0px',
						left: '0px',
						width: '100%',
						height: $(document).height(),
						background: $.alerts.overlayColor,
						opacity: $.alerts.overlayOpacity
					});
				break;
				case 'hide':
					$("#popup_overlay").remove();
				break;
			}
		},
		
		_reposition: function(voffset) {
			var top = (($(window).height() / 2) - ($("#popup_container").outerHeight() / 2)) + ((typeof(voffset) !== 'undefined') ? voffset : $.alerts.verticalOffset);
			var left = (($(window).width() / 2) - ($("#popup_container").outerWidth() / 2)) + $.alerts.horizontalOffset;
			if( top < 0 ) top = 0;
			if( left < 0 ) left = 0;
			
			// IE6 fix
			if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
			
			$("#popup_container").css({
				top: top + 'px',
				left: left + 'px'
			});
			$("#popup_overlay").height( $(document).height() );
		},
		
		_maintainPosition: function(status) {
			if( $.alerts.repositionOnResize ) {
				switch(status) {
					case true:
						$(window).bind('resize', $.alerts._reposition);
					break;
					case false:
						$(window).unbind('resize', $.alerts._reposition);
					break;
				}
			}
		}
		
	};
	
	// Shortuct functions
	jAlert = function(message, title, callback) {
		$.alerts.alert(message, title, callback);
	};
	
	jAlertWithCheckboxCallback = function(message, title, callback, checkboxLabel, checkboxCallback) {
		$.alerts.alertWithCheckboxCallback(message, title, callback, checkboxLabel, checkboxCallback);
	};
	
	jConfirmWithCheckboxCallback = function(message, title, callback, checkboxLabel, checkboxCallback) {
		$.alerts.confirmWithCheckboxCallback(message, title, callback, checkboxLabel, checkboxCallback);
	};
	
	jConfirm = function(message, title, callback, yesNoOptions, yesSelected) {
		$.alerts.confirm(message, title, callback, yesNoOptions, yesSelected);
	};
	
	jRadio = function(message, title, radioName, labels, values, callback) {
		$.alerts.radio(message, title, radioName, labels, values, callback);
	};
	
	jConfirmEsportaResoPdf = function(message, title, callback) {
		$.alerts.confirmEsportaResoPdf(message, title, callback);
	};
		
	jPrompt = function(message, value, title, callback, multiline, rowsAllowed, colsAllowed) {
		$.alerts.prompt(message, value, title, callback, multiline, rowsAllowed, colsAllowed);
	};
	
	jPrompt2Fields = function(message, value1, value2, title, titleField1, titleField2, callback) {
		$.alerts.prompt2Fields(message, value1, value2, title, titleField1, titleField2, callback);
	};
	
	jPromptPrivacy = function(message, value, title, titleField1, titleField2, callback, validationCallback) {
		$.alerts.promptPrivacy(message, value, title, titleField1, titleField2, callback, validationCallback);
	};
	
	jPromptPrivacyCliente = function(message, value, title, msgAccettoPrivacy, params, callback, validationCallback) {
		$.alerts.promptPrivacyCliente(message, value, title, msgAccettoPrivacy, params, callback, validationCallback);
	};
	
	jPromptPrivacyEdicolaStarter = function(message, value, title, msgAccettoPrivacy, params, callback, validationCallback) {
		$.alerts.promptPrivacyEdicolaStarter(message, value, title, msgAccettoPrivacy, params, callback, validationCallback);
	};
	
	jPromptSmartEdicola = function(message, title, params, callback) {
		$.alerts.promptSmartEdicola(message, title, params, callback);
	};
	
	jPromptFile = function(message, title, params, callback) {
		$.alerts.promptFile(message, title, params, callback);
	};
	
	jPromptNote = function(message, value, value1, title, title1, callback) {
		$.alerts.promptNote(message, value, value1, title, title1, callback);
	};
	
})(jQuery);