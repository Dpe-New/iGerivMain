var chars = [];
var keyCodes = [];
var pressed = false;
var regExpBarcode = /^[0-9]{10,18}$/;
var baseFieldName = 'copieLette';

function setBarcodeKeyDownEvent() {
	if ($('#barcode').length > 0) { 
		$('#barcode').keydown({callback: autoIncrementField}, manageBarcodeKeyDownBollaConsegna);
	}
}

function calcDiff($f) { 
	var copie = $f.closest("td").prev().prev().prev().text();
	if (isNaN(copie)) {
		if (copie.indexOf("(") != -1) {
			var copieExtra = copie.substring(copie.indexOf("(") + 1, copie.indexOf(")")).trim();
			copie = Number(copie.substring(0, copie.indexOf("(")).trim()) + Number(copieExtra);
		} else {
			copie = 0;
		}
	}
	var copieLette = Number($f.val());
	if (copieLette > 999) {
		$f.val($f.data('prevval'));
	} else {
		var $fieldDiff = $f.next("input[type='text']");
		$fieldDiff.val(copieLette - Number(copie));
		keyEnterPressed($fieldDiff);
		$f.css('background','#fff');
		$('#totalHeader_7').text(calculateMancanze());
		$('#totalHeader_8').text(calculateEccedenze());
	}
}

function setFieldEvents() {
	$fields = $('#BollaControlloForm input:text[name^=fieldMap]');
	$('#BollaControlloForm input:text[name^=' + baseFieldName + ']').each(function() {
		var $f = $(this); 
		$f.data('prevval', $f.val());
		$f.keydown(onFieldKeyDown);
		$f.blur(calcDiff($(this)));
		$f.numeric(false, null, function() {
			var $field = $(this);
			var $fSpunta = $field.next("input:hidden[name^=spunta]");
			var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
			if (Math.abs($field.val()) > 999) {
				$field.val($field.data('prevval'));
			}
			$fModificato.val("true");
		});
	});
}

function getCurrentTextField(node) {
	var idTextField = node.id.replace(/([|])/g,'\\\$1');
	return $("#" + idTextField);
}

function autoIncrementField(id, fieldName) {
	if (id != '' && $('#' + id)) {
		var $f = $("input:text[id='" + fieldName + id + "']");
		var val = isNaN($f.val()) ? 0 : (Number($f.val()) + 1);
		$f.val(val);
		$f.data('prevval', val);
		calcDiff($f);
	}
}

function executeBarcodeFound($textField, val) {
	var oldData = $textField.data('prevval');
	oldData = (typeof(oldData) !== 'undefined' && oldData != null && !isNaN(oldData)) ? oldData : 1;
	$textField.val(oldData);
	var pk = $("#" + val);	 
	var pkVal = pk.val();
	if (pk.length > 0) {
		selectRowByBarcodeInf(pkVal, baseFieldName, false);
		autoIncrementField(pkVal, baseFieldName);
	} else {
		noBarcodeFoundAction(val);
	}
}

/*
 * Evento keydown che esegue su ogni carattere digitato nel campo di testo differenze
 * Se nell'arco di 500 ms sono stati digitati almento 10 numeri, allora è un barcode inserito con il lettore di barcode:
 * 	=> setta il valore del campo di testo con il valore numerico precedente al barcode, esegue le azioni sul campo di testo, 
 *	   setta il contenuto del campo barcode con il barcode appena inserito ed esegue la ricerca
 * altrimenti verifica se il carattere digitato da tastiera è un invio, quindi esegue le azioni sul campo di testo e ritorna il focus al campo barcode 
 */
function onFieldKeyDown(evt) {
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	var $textField = getCurrentTextField(node);
	if (bollaRivKeyPress(evt, $textField)) {
		return true;
	} 
	if (evt.keyCode >= 48 && evt.keyCode <= 57) {
        chars.push(String.fromCharCode(evt.keyCode));
    } 
	if (evt.keyCode == 13 || evt.keyCode == 32) {
    	keyCodes.push(evt.keyCode);
    }
	if (pressed == false) {
		setTimeout(function() {
			var val = chars.join("");
            if (regExpBarcode.test(val)) {
            	executeBarcodeFound($textField, val);
            } else if (keyCodes.length === 1 && (keyCodes[0] === 13 || keyCodes[0] === 32))  {
        		evt.preventDefault();
        		calcDiff($textField);
        		setFieldsChanged(evt);
        		$('#barcode').val('');
        		$('#barcode').focus();
    		} else if (val.length < 10) {
    			if (val > 999) {
    				$textField.val($textField.data('prevval'));
    				calcDiff($textField);
    			} else {
            		$textField.data('prevval', $textField.val());
            		calcDiff($textField);
    			}
            }
            chars = [];
            keyCodes = [];
            pressed = false;
        }, 500);
	}
	pressed = true;
	if (evt.keyCode == 13 || evt.keyCode == 32) {
		return false;
	}
	return true;
}

function keyEnterPressed($textField) {
	$textField.data('prevval', $textField.val());
	$textField.trigger("change");
	var id = $textField.attr("id").replace(/([|])/g,'\\\$1').replace(baseFieldName,'').replace('differenze','');
	var $checkField = $("#" + "dpeChkbx_" + id);
	$checkField.attr("checked", "true");		
	if (typeof(checkboxChanged) === 'function') {
		checkboxChanged($checkField);
	} else {
		setChangedFieldAttribute($textField);
	}
	return false;
}
 
function setFieldsToSave(isSubmit) {
	var $resi = $('#BollaControlloForm input:text[name*=Map]');
	var retVal = true;
	$resi.each(function() {
		var $field = $(this);
		var $val = $field.val();
		if (!isNaN($val) && Number($val) > 999) {
			retVal = false;
			return;
		}
		$field.attr("disabled", !isSubmit);
		var $fSpunta = $field.next("input:hidden[name^=spunta]");
		if (isSubmit) {
			$field.data('oldVal', $val);
			$fSpunta.data('oldVal', $fSpunta.val());
		} else {
			var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
			if ($field.data('oldVal') != $val || $fSpunta.data('oldVal') != $fSpunta.val()) {
				$fModificato.val("true");
			} else {
				$fModificato.val("false");
			}
		}
	});
	return retVal;
}

function manageBarcodeKeyDownBollaConsegna(evt) {
	var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
	if (keycode === 116) {
		return true;
	} else {
		if ($("#aggiornaBarcode").length > 0 && $("#aggiornaBarcode").is(":checked") && $('#barcode').val().trim().length > 0 && !isNaN($('#barcode').val().trim()) && keycode == '13') {
			callBarcodeVenditeService();
		} else {
			manageBarcodeKeyDownInf(evt.data.callback, evt);
		}
	}
}

function manageBarcodeKeyDownInf(callback, evt) {
	var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
	if (keycode == '13') {	
		var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
		evt.preventDefault();								
		var fieldVal = node.value;	
		var fieldName = node.name;	
		var pk = $("#" + fieldVal);	
		if (pk.length > 0) {
			if (fieldName.length > 0) {
				fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
			}
			selectRowByBarcodeInf(pk.val(), fieldName);
			if (typeof(callback) === 'function') {
				callback(pk.val(), fieldName);
			}
		} else {
			noBarcodeFoundAction(fieldVal);
		}
	}
}

function selectRowByBarcodeInf(id, fieldName) {
	if (id != '' && $('#' + id)) {
		playBarcodeBeep();
		if (lastFocusedRow != '') {
			lastFocusedRow.css({"backgroundColor":lastFocusColor});	
		}
		var currField = $("input:text[id='" + fieldName + id + "']");
		doActionsAfterSelectRowInf(currField);
	}	   
}

function doActionsAfterSelectRowInf(currField) {
	var row = currField.parent().parent();	
	var oldBkColor = row.css("backgroundColor");	
	$("#barcode").val('');
	row.css({"backgroundColor":"#ffff66"});	
	setTimeout(function() { currField.select(); },50);
	var bodyelem = '';
	if ($.browser.safari) { 
		bodyelem = $("body"); 
	} else { 
		bodyelem = $("html");
	}
	var topPos = row.offset().top - 40;
	bodyelem.scrollTop(topPos);
	lastFocusedRow = row;
	lastFocusColor = oldBkColor;
}

function setFieldsChanged(evt) { 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	if ((evt.keyCode == 13 || evt.keyCode == 32) && (node.type == "text"))  {
		evt.preventDefault();
		var idTextField = node.id.replace(/([|])/g,'\\\$1');
		var id = idTextField.replace('differenze','').replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','');
		var $textField = $("#" + idTextField);
		var $checkField = $("#" + "dpeChkbx_" + id);
		$checkField.attr("checked", "true");		
		if (typeof(checkboxChanged) === 'function') {
			checkboxChanged($checkField);
		} else {
			setChangedFieldAttribute($textField);
		}
		return false;
	} 
}

function bollaRivKeyPress(event, $f) {
	var keycode = (event.keyCode ? event.keyCode : event.charCode);
	if ((!event.shiftKey && keycode == 9) || keycode == 40) {
		setTimeout(function() {var field = $f.closest("tr").next().find("input:text[name^='" + baseFieldName + "']").first(); $f.css('background','#fff'); $f.trigger("blur"); field.select();}, 200);
		return true;
	} else if ((event.shiftKey && keycode == 9) || keycode == 38) {
		setTimeout(function() {var field = $f.closest("tr").prev().find("input:text[name^='" + baseFieldName + "']").first(); $f.css('background','#fff'); $f.trigger("blur"); field.select();}, 200);
		return true;
	}
	return false;
}

function setBollaRivKeyPress() {
	$(this).bind('click', function() {
		if (!$("#popup_container").is(":visible") 
				&& !$("#popup_name").is(":visible") 
				&& $("#load").css('visibility') === 'hidden'
				&& $('input[type="text"]:focus, input[type="checkbox"]:focus, textarea:focus, select:focus').length === 0) {
			setTimeout(function() {
				$('#barcode').focus();					
			}, 50);
		}
	});
}