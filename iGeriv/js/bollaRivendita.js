function manageBarcodeKeyDownBollaConsegna(evt) {
	var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
	if ($("#aggiornaBarcode").length > 0 && $("#aggiornaBarcode").is(":checked") && $('#barcode').val().trim().length > 0 && !isNaN($('#barcode').val().trim()) && keycode == '13') {
		callBarcodeVenditeService();
	} else {
		manageBarcodeKeyDown(evt.data.callback, evt);
	}
}

function setBarcodeKeyDownEvent() {
	if ($('#barcode').length > 0) { 
		$('#barcode').keydown({callback: null}, manageBarcodeKeyDownBollaConsegna);
	}
}

onBlurFields = function($field) {
	var $fSpunta = $field.next("input:hidden[name^=spunta]");
	var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
	if (Math.abs($field.val()) > 101) {
		PlaySound('beep3');
		jConfirm(differenzaMoltoGrandeConfirm.replace('{0}', $field.val()), attenzioneMsg.toUpperCase(), function(r) {
		    if (r) {
		    	$fModificato.val("true");
		    } else {
		    	setTimeout(function () { $field.focus(); }, 50);	
				$field.val('');
				return false;
		    };
		}, true, false);
		return false;
	}
	$fModificato.val("true");
};

function setFieldEvents() {
	$fields = $('#BollaControlloForm input:text[name^=field]:enabled');
	$fields.numeric({negative : true, decimal : false});
	$fields.attr('maxlength','4');
	$fields.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode); 
		if (keycode == 13) {
			event.preventDefault();
			onBlurFields($(this));
		}  
	});
	$fields.blur(function() {
		onBlurFields($(this));
	});
	
	$fields.change(function() {
		$('#totalHeader_7').text(calculateMancanze());
		$('#totalHeader_8').text(calculateEccedenze());
	});
}

function setFieldsToSave(isSubmit) {
	var $resi = $('#BollaControlloForm input:text[name^=fieldMap]:enabled');
	window.Remaining1 = $resi.length;
	$resi.each(function() {
		var $field = $(this);
		var $fSpunta = $field.next("input:hidden[name^=spunta]");
		if (isSubmit) {
			$field.data('oldVal', $field.val());
			$fSpunta.data('oldVal', $fSpunta.val());
		} else {
			var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
			if ($field.data('oldVal') != $field.val() || $fSpunta.data('oldVal') != $fSpunta.val()) {
				$fModificato.val("true");
			} else {
				$fModificato.val("false");
			}
		}
		--window.Remaining1;
        if (window.Remaining1 == 0) {
        	$resi.attr("disabled",false);
        }
	});
	return true;
}

function bollaRivKeyPress(event) {
	var keycode = (event.keyCode ? event.keyCode : event.charCode);
	if ((!event.shiftKey && keycode == '9') || keycode == '40') {	// tab	|| arrow down	
		setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field = $("#" + id).closest("tr").next().find("input:text[name^='fieldMap']:enabled").first(); $("#" + id).css('background','#fff'); field.focus(); $("#" + id).trigger("blur");}, 100);
	} else if ((event.shiftKey && keycode == '9') || keycode == '38') {	// shift + tab	|| arrow up
		setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field = $("#" + id).closest("tr").prev().find("input:text[name^='fieldMap']:enabled").first(); $("#" + id).css('background','#fff'); field.focus(); $("#" + id).trigger("blur");}, 100);
	}
}

function setBollaRivKeyPress() {
	$(this).bind('keydown', bollaRivKeyPress);
}
