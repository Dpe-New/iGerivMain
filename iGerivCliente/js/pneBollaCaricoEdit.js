var stopWaitingDivBollaCarico = false;
var allowSearch = false;

$(document).ready(function() {	
	if (codCausale != null && codCausale > 0) {
		$("#causaliEdit").val(Number(codCausale));
	}
	$("#dataDocumentoEdit").click(function() { 
    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
	});
	$("#deleteRow, #addRow, #addFornitore").tooltip({
		delay: 0,  
	    showURL: false
	});
	$("#meBollaCarico").click(function() {
		if (!stopWaitingDivBollaCarico) {
			ray.ajax();
		}
	});
	addFadeLayerEvents();
	setLastFocusedElement();
	setTableFields();
	var fornVal = $("#fornitoriEdit").val();
	$("#fornitoriEdit").change(function() {
		if ($('#BollaCaricoEditTab_table > tbody tr').length > 1) {
			PlaySound('beep3');
			jConfirm(msgConfirmAzzerareVociBolla, attenzioneMsg, function(r) {
			    if (r) { 
			    	$('#BollaCaricoEditTab_table tbody tr:not(:last)').remove();
			    	fornVal = $("#fornitoriEdit").val();
			    	return false;
			    } else {
			    	$("#fornitoriEdit").val(fornVal);
		            return false;
			    }
			});
		}
	 });
	$("#fornitoriEdit").focus();
});	

function setTableFields() {
	var count = 0;
	$("#BollaCaricoEditTab_table tbody tr").each(function() {
		var $tr = $(this);
		if (!($tr.hasClass('calcRow'))) {
			var str = '<input name="dettagli[' + count + '].pk.idDocumento" id="idDoc' + count + '" value="" type="hidden">';
			str += '<input name="dettagli[' + count + '].prodotto.edicola.codEdicola" value="' + userId + '" type="hidden">';
			str += '<input name="dettagli[' + count + '].magazzinoDa" value="' + magazzinoEsterno + '" type="hidden">';
			str += '<input name="dettagli[' + count + '].magazzinoA" value="' + magazzinoInterno + '" type="hidden">';
			str += '<input name="dettagli[' + count + '].prodotto.descrizioneProdottoA" id="descrizioneProdottoA' + count + '" value="" type="hidden">';
			$tr.append(str);
			var $tds = $tr.find("td");
			var $tdCodForn = $tr.find("td:nth-child(1)");
			var $tdIva = $tr.find("td:nth-child(7)");
			var $firstTd = $tds.first();
			var index = $firstTd.find("input[id*='prog']").val();
			var $codFornField = $tdCodForn.find("input[name*='codiceProdottoFornitore']");
			var $ivaField = $tdIva.find("select[id*='aliquota']");
			$codFornField.attr('oldVal', $codFornField.val());
			$ivaField.attr('oldVal', $ivaField.val());
			$ivaField.attr('disabled',true);
			attachEventsToRow($(this), index);
			if ($tr.attr("prodottoDl") == 'true') {
				$codFornField.attr("disabled", true);
			}
			count++;
		}
	});
}


function removeTableRow() {
	if ($("#" + lastFocusedFieldId.replace(/([|@.\[\]])/g,'\\\$1')).length > 0) {
		var $tr = $("#" + lastFocusedFieldId.replace(/([|@.\[\]])/g,'\\\$1')).closest("tr");
		if ($tr.hasClass('odd') || $tr.hasClass('even')) {
			$tr.remove();
		}
	} else {
		$('#BollaCaricoEditTab_table > tbody tr:last').prev().remove();
	}
	updateTotal();
}

function getIvaOptions() {
	var options = '';
	var numberArr = numbersStr.split(",");
	for (var i = 0; i < numberArr.length; i++) {
		options += '<option value="' + numberArr[i] + '">' + numberArr[i] + '</option>';
	}
	return options;
}

function addTableRow() {
	$('#BollaCaricoEditTab_table > tbody tr td:nth-child(2)').find("img").each(function() {
		$(this).remove();
	});
	var index = $('#BollaCaricoEditTab_table > tbody tr').length;
	var tabRow = '<tr class="odd" style="height:22px"><td style="font-size: 100%; text-align: left;" width="8%"><input name="dettagli[' + index + '].codiceProdottoFornitore" id="codiceProdottoFornitore' + index + '" value="" class="extremeTableFieldsSmaller" style="font-size: 100%; text-align: left;" size="6" maxlength="10" type="text"></td>';
	tabRow += '<td style="text-align: left;" width="22%; white-space: nowrap;"><input name="dettagli[' + index + '].prodotto.barcode" id="barcode' + index + '" value="" class="extremeTableFieldsSmaller" style="font-size: 100%; text-align: left; vertical-align:middle" size="23" maxlength="20" type="text">&nbsp;<img src="/app_img/product.gif" width="18px" height="18px" id="product" alt=' + msgAggiungiProdotto + ' border="0" title="' + msgAggiungiProdotto + '" style="cursor:pointer; vertical-align:middle" onclick="javascript: addProduct();"/></td>';
	tabRow += '<td width="40%"></td>';
	tabRow += '<td width="8%" style="font-size: 100%; text-align: left;"><input name="dettagli[' + index + '].quantita" id="quant' + index + '" value="" class="extremeTableFieldsSmaller" style="font-size: 100%; text-align: left;" size="3" maxlength="10" type="text"></td>';
	tabRow += '<td style="font-size: 100%; text-align: left;" width="8%"><input name="dettagli[' + index + '].prezzo" id="prezzo' + index + '" value="" class="extremeTableFieldsSmaller" style="font-size: 100%; text-align: right;" size="8" maxlength="10" type="text"></td>';
	tabRow += '<td style="font-size: 100%; text-align: left;" width="8%"><input name="dettagli[' + index + '].importo" id="importo' + index + '" value="" class="extremeTableFieldsSmaller" style="font-size: 100%; text-align: right;" size="8" maxlength="10" type="text"></td>';
	tabRow += '<td style="font-size: 100%; text-align: left;" width="5%"><select name="dettagli[' + index + '].prodotto.aliquota" id="aliquota' + index + '" value="" class="extremeTableFieldsLarger" style="font-size: 12px; width: 60px;">' + getIvaOptions() + '</select></td>';
	tabRow += '<input name="dettagli[' + index + '].pk.progressivo" id="prog' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].pk.idDocumento" id="idDoc' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.codProdottoInterno" id="prodInterno' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.edicola.codEdicola" value="' + userId + '" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].magazzinoDa" value="' + magazzinoEsterno + '" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].magazzinoA" value="' + magazzinoInterno + '" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.descrizioneProdottoA" id="descrizioneProdottoA' + index + '" value="" type="hidden">';
	tabRow += '</tr>';
	if ($("#" + lastFocusedFieldId).length > 0 && $("#" + lastFocusedFieldId).attr("name").indexOf("dettagli") != -1) {
		var $tr = $("#" + lastFocusedFieldId).closest("tr");
		if ($tr.hasClass('odd') || $tr.hasClass('even')) {
			$tr.after(tabRow);
		}
	} else {
		$('#BollaCaricoEditTab_table tbody tr:last').before(tabRow);
	}
	$("#product").tooltip({
		delay: 0,  
	    showURL: false
	});
	$("#codProdottoInterno0").attr("disabled", true);
	setLastFocusedElement();
	$("#codiceProdottoFornitore" + index).focus();
	attachEventsToRow($("#codiceProdottoFornitore" + index).closest("tr"), index);
	setFocusedFieldStyle();
}

function attachEventsToRow($row, index) {
	var $codProdFornitoreField = $row.find(":input").first();
	var $prodField = $row.find("td:nth-child(2)").find(":input").first();
	var $quantField = $row.find("td:nth-child(4)").find(":input").first();
	var $prezzoField = $row.find("td:nth-child(5)").find(":input").first();
	var $importoField = $row.find("td:nth-child(6)").find(":input").first();
	var $iva = $row.find("td:nth-child(7)").find(":input").first();
	var $descProdotto = $("#descrizioneProdottoA" + index);
	$codProdFornitoreField.autocomplete({
		minLength: 2,			
		search: function(event, ui) {
			if (allowSearch) {
				ray.ajax();
			  	return true;				  	
			} else {
				return false;
			}
		},
		source: function(request, response) {		
			dojo.xhrGet({
				url: appContext + "/automcomplete_getProdottoNonEditorialeInBolla.action",
				delay: 100,
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					unBlockUI();
					if (data.length == 0) {
						focusNextField();
					} else if (data.length == 1) {
						var $td = $codProdFornitoreField.closest("td");
						var $prodTd = $td.next();
						var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
						var $prodField = $prodTd.find(":input");
						var $descTd = $td.next().next();
						var $quantField = $descTd.next();
						var $prezzoTd = $quantField.next();
						var $prezzoField = $prezzoTd.find(":input");
						var $ivaField = $prezzoTd.next().next().find("select");
						$prodField.val(data[0].barcode);
						$codProdoInternoField.val(data[0].codProdotto);
						$prezzoField.val(displayNum(Number(data[0].prezzo).toFixed(2)));
						$prodField.attr("disabled", true);
						$descTd.html(data[0].descrizione);
						$ivaField.val(data[0].iva);
						$ivaField.attr('oldVal', data[0].iva);
						if (data[0].iva != '') {
							$ivaField.attr("disabled", true);
						} else {
							$ivaField.attr("disabled", false);
						}
						$codProdFornitoreField.attr('oldVal', $codProdFornitoreField.val());
						if (data[0].prodottoDl == 'true') {
							$codProdFornitoreField.attr("disabled", true);
						} else {
							$codProdFornitoreField.attr("disabled", false);
						}
						setTimeout(function() {$("#" + lastFocusedFieldId).closest("td").next().next().next().find(":input").focus();},100);
					} else {
						response($.map(data, function(item) {		                            		                         	
		                                return {
		                                    id: item.codProdotto,	
		                                    label: item.codiceFornitore + " - " + item.descrizione,
		                                    descrizione: item.descrizione,
		                                    value: item.codiceFornitore,
		                                    prezzo: item.prezzo,
		                                    barcode: item.barcode,
		                                    iva: item.iva,
		                                    prodottoDl: item.prodottoDl
		                                };
		                            }));
					}
				}
			});
		},
		select: function (event, ui) {	
			var $td = $codProdFornitoreField.closest("td");
			var $prodTd = $td.next();
			var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
			var $prodField = $prodTd.find(":input");
			var $descTd = $td.next().next();
			var $quantField = $descTd.next();
			var $prezzoTd = $quantField.next();
			var $prezzoField = $prezzoTd.find(":input");
			var $ivaField = $prezzoTd.next().next().find("select");
			$prodField.val(ui.item.barcode);
			$codProdoInternoField.val(ui.item.id);
			$prezzoField.val(displayNum(Number(ui.item.prezzo).toFixed(2)));
			$prodField.attr("disabled", true);
			$descTd.html(ui.item.descrizione);
			$ivaField.val(ui.item.iva);
			$ivaField.attr('oldVal', ui.item.iva);
			if (ui.item.iva != '') {
				$ivaField.attr("disabled", true);
			} else {
				$ivaField.attr("disabled", false);
			}
			$codProdFornitoreField.attr('oldVal', $codProdFornitoreField.val());
			if (ui.item.prodottoDl == 'true') {
				$codProdFornitoreField.attr("disabled", true);
			} else {
				$codProdFornitoreField.attr("disabled", false);
			}
			setTimeout(function() {$("#" + lastFocusedFieldId).closest("td").next().next().next().find(":input").focus();},100);
		}
	});
	
	$codProdFornitoreField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			allowSearch = true;
		}   
	});
	
	$codProdFornitoreField.focus(function(event) {
		if ($(this).val() == '') {
			$prodField.attr("disabled", false);
		} 
	});
	
	$codProdFornitoreField.keyup(function(event) {
		if ($(this).val() == '') {
			$prodField.attr("disabled", false);
		} 
	});
	
	$quantField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			focusNextField();
			calcImporto($(this), $importoField, $prezzoField);
		}    
	});
	
	$prezzoField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			focusNextField();
			try {
				calcImporto($quantField, $importoField, $(this));
				$(this).val(displayNum($(this).val()));
			} catch (e) {
				setTimeout('$("#' + prodFieldId + '")[0].focus();', 100);
			}
		}    
	});
	
	$prezzoField.blur(function(event) {
		var prodFieldId = $(this).attr("id");
		try {
			calcImporto($quantField, $importoField, $(this));
			$(this).val(displayNum($(this).val()));
		} catch (e) {
			setTimeout('$("#' + prodFieldId + '")[0].focus();', 100);
		}
	});
	
	$importoField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			focusNextField();
		}    
	});
	
	$iva.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			addTableRow();
		}    
	});
	
	$prodField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode); 
		if (!isNaN($(this).val().trim()) && keycode == 13) {
			allowSearch = true;
		}
	});
	
	$prodField.autocomplete({	
		minLength: 3,	
		search: function(event, ui) {
			if (isNaN($(this).val().trim()) || allowSearch) {
				allowSearch = false;
				ray.ajax();
			  	return true;
			} else {
				return false;
			}
		},
		source: function(request, response) {	
			var $val = $prodField.val();
			dojo.xhrGet({
				url: appContext + '/pubblicazioniRpc_getPneBarcodeOrDescrizione.action?barcode=' + $val + '&codFornitore=' + $("#fornitoriEdit").val(),
				delay: 100,
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					unBlockUI();
					if ((data == '' || data.length == 0 || data[0].length == 0) && (isNaN($val))) {
						PlaySound('beep3');
						jConfirm(msgConfirmAggiungereNuovoProdotto.replace("{0}", $val), attenzioneMsg, function(r) {
						    if (r) {
						    	addProduct($val);
						    	return true;
						    } 
					        return false;
						});
					} else if (data.length == 1) {
						var $td = $codProdFornitoreField.closest("td");
						var $prodTd = $td.next();
						var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
						var $prodField = $prodTd.find(":input");
						var $descTd = $td.next().next();
						var $codProdFornField = $td.find(":input");
						var $prezzo = $descTd.next().next().find(":input");
						var $ivaField = $descTd.next().next().next().next().find("select");
						$prodField.val(data[0].barcode);
						$codProdoInternoField.val(data[0].codProdotto);
						$descTd.html(data[0].descrizione);
						$prezzo.val(displayNum(Number(data[0].ultimoPrezzoAcquisto).toFixed(2)));
						$ivaField.val(data[0].iva);
						$ivaField.attr('oldVal', data[0].iva);
						if (data[0].iva != '') {
							$ivaField.attr("disabled", true);
						} else {
							$ivaField.attr("disabled", false);
						}
						$descProdotto.val(data[0].descrizione);
						if (data[0].codiceProdottoFornitore.length > 0) {
							$codProdFornField.val(data[0].codiceProdottoFornitore);
							$codProdFornField.attr('oldVal', data[0].codiceProdottoFornitore);
							if (data[0].prodottoDl == 'true') {
								$codProdFornField.attr("disabled", true);
							} else {
								$codProdFornField.attr("disabled", false);
							}
						}
						$("#" + lastFocusedFieldId).closest("td").next().next().find(":input").focus();
					} else {
						response($.map(data, function(item) {		                            		                         	
                            return {
                                id: item.codProdotto,	
                                label: item.barcode + ' - ' + item.descrizione,
                                descrizione: item.descrizione,
                                value: item.barcode,
                                barcode: item.barcode,
                                iva: item.iva,
                                codiceProdottoFornitore: item.codiceProdottoFornitore,
                                ultimoPrezzoAcquisto: item.ultimoPrezzoAcquisto,
                                prodottoDl: item.prodottoDl
                            };
                        }));
					}
				}
			});
		},
		select: function (event, ui) {
			var $td = $codProdFornitoreField.closest("td");
			var $prodTd = $td.next();
			var $descTd = $prodTd.next();
			var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
			var $codProdFornField = $td.find(":input");
			var $ivaField = $descTd.next().next().next().next().find("select");
			$codProdoInternoField.val(ui.item.id);
			$descTd.html(ui.item.descrizione);
			var $prezzo = $descTd.next().next().find(":input");
			$prezzo.val(displayNum(Number(ui.item.ultimoPrezzoAcquisto).toFixed(2)));
			$ivaField.val(ui.item.iva);
			$ivaField.attr('oldVal', ui.item.iva);
			if (ui.item.iva != '') {
				$ivaField.attr("disabled", true);
			} else {
				$ivaField.attr("disabled", false);
			}
			$descProdotto.val(ui.item.descrizione);
			if (ui.item.codiceProdottoFornitore.length > 0) {
				$codProdFornField.val(ui.item.codiceProdottoFornitore);
				$codProdFornField.attr('oldVal', ui.item.codiceProdottoFornitore);
				if (ui.item.prodottoDl == 'true') {
					$codProdFornField.attr("disabled", true);
				} else {
					$codProdFornField.attr("disabled", false);
				}
			}
			setTimeout(function() {$("#" + lastFocusedFieldId).closest("td").next().next().find(":input").focus();}, 100);
		}
	});	
	
	$quantField.blur(function(event) { 
		var prodFieldId = $(this).attr("id");
		try {
			calcImporto($(this), $importoField, $prezzoField);
		} catch (e) {
			setTimeout('$("#' + prodFieldId + '")[0].focus();', 100);
		}
	});
}

function calcImporto(field, importoField, prezzo) {
	var quant = 0;
	try {
		quant = Number(parseLocalNum($(field).val())).toFixed(2);
	} catch (e) {
		throw e;
	}
	var pre = 0;
	try {
		pre = Number(parseLocalNum($(prezzo).val())).toFixed(2);
	} catch (e) {
		throw e;
	}
	if (!isNaN(quant) && !isNaN(pre)) {
		var imp = Number(quant * pre);
		$(importoField).val(displayNum(imp.toFixed(2)));
		updateTotal();
	}
}

function updateTotal() {
	var $totalImpField = $("#BollaCaricoEditTab_table tbody tr.calcRow td.calcResult");
	var total = 0;
	$("#BollaCaricoEditTab_table tbody tr td:nth-child(6)").each(function() {
		var $td = $(this);
		var $field = $td.find(":input:text");
		if ($field.length > 0) {
			var num = Number(parseLocalNum($field.val()));
			total += num;
		}
	});
	$totalImpField.text(displayNum(total.toFixed(2)));
}

function focusNextField() {
	$("#" + lastFocusedFieldId).closest("td").next().find(":input").focus().select();
}

function afterSuccessSave() {
	$("#ricerca").trigger('click');
}

function doDelete() {
	PlaySound('beep3');
	jConfirm(msgCancellareBolla, attenzioneMsg, function(r) {
	    if (r) {
	    	setFormAction('BollaCaricoEditForm','pneBollaCarico_deleteBolla.action', '', 'messageDiv', true);
			deleted = true;
			setTimeout('unBlockUI();', 100);
	    } else {
	    	setTimeout('unBlockUI();', 100);
	    	return false;
	    }
	});
}

function validateFieldsBollaCarico(showAlerts) {
	if ($("#fornitoriEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgFornitore), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		unBlockUI();
		return false;
	}
	if ($("#dataDocumentoEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgDataDocumento), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		unBlockUI();
		return false;
	} else if (!checkDate($("#dataDocumentoEdit").val())) {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgFormatoDataInvalido.replace('{0}', $("#dataDocumentoEdit").val()), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		unBlockUI();
		return false;
	}
	if ($("#numeroDocumentoEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgNumeroDocumento), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		unBlockUI();
		return false;
	}
	if ($("#causaliEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgCausale), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		unBlockUI();
		return false;
	}
	var count = 0;
	try{
		var arrCodProdottoInterno = [];
		$('#BollaCaricoEditTab_table tbody tr').each(function() {
			var hasProdotto = true;
			var descrizione = $(this).find("td:nth-child(3)").text().trim();
			$(this).find("input[name*='dettagli'], select[name*='dettagli']").each(function() {
				var name = $(this).attr("name");
				var fieldId = $(this).attr("id");
				if (name.indexOf("codProdottoInterno") != -1) {
					arrCodProdottoInterno.push($(this).val());	
				}
				if (fieldId.indexOf("quant") != -1 && $(this).val() == '') {
					if (showAlerts) {
						$.alerts.dialogClass = "style_1";
						jAlert(msgCampoObbligatorio.replace('{0}',msgQuantita), msgAttenzione, function() {
							$.alerts.dialogClass = null;
						});
					}
					unBlockUI();
					throw fieldId;
				}
				if (fieldId.indexOf("prezzo") != -1 && $(this).val() == '') {
					if (showAlerts) {
						$.alerts.dialogClass = "style_1";
						jAlert(msgCampoObbligatorio.replace('{0}',msgPrezzo), msgAttenzione, function() {
							$.alerts.dialogClass = null;
						});
					}
					unBlockUI();
					throw fieldId;
				}
				if (fieldId.indexOf("aliquota") != -1 && $(this).val().trim() != '') {
					$(this).attr("disabled", false);
					/*if (($(this).val().trim().indexOf(".") != -1 || $(this).val().trim().indexOf(",") != -1 || isNaN($(this).val().trim())) &&  showAlerts) {
						$.alerts.dialogClass = "style_1";
						jAlert(msgAliquotaInteger, msgAttenzione, function() {
							$.alerts.dialogClass = null;
						});
						unBlockUI();
						throw fieldId;
					}
					var aliquota = $(this).val().trim();
					var aliquotaPrec = $(this).attr('oldVal');
					if (!byPassAliquotaCheck && aliquota != '' && Number(aliquota) > 0 && aliquotaPrec != '' && Number(aliquotaPrec) > 0) {
						if (aliquotaPrec != aliquota) {
							setTimeout(function() {
				    			var id = fieldId;
								PlaySound('beep3');
								jConfirm(msgAliquotaIvaCambiata.replace('{0}',descrizione).replace('{1}',aliquotaPrec).replace('{2}',aliquota), attenzioneMsg.toUpperCase(), function(r) {
								    if (r) { 
								    	byPassAliquotaCheck = true;
								    	setTimeout(function() {return (validateFieldsBollaCarico(true) && setFormAction('BollaCaricoEditForm','pneBollaCarico_saveBolla.action', '', 'messageDiv'));},100);
								    } else {
								    	byPassAliquotaCheck = false;  
								    	unBlockUI();
								    	$("#" + id).focus();
								    	enableAllFormFields();
								    }
								});
							}, 100);
							throw fieldId;
						}
					}*/
				}
				if (name.indexOf("codiceProdottoFornitore") != -1 && $(this).val().trim() != '') {
					var codiceProdottoFornitore = $(this).val().trim();
					var codiceProdottoFornitorePrec = $(this).attr('oldVal');
					if (typeof(codiceProdottoFornitorePrec) != 'undefined' && codiceProdottoFornitore != '' && codiceProdottoFornitorePrec != '') {
						if (!byPassCodiceProdottoFornitoreCheck && codiceProdottoFornitorePrec != codiceProdottoFornitore) {
							var fid = $(this).attr("id");
							setTimeout(function() {
								PlaySound('beep3');
								jConfirm(msgCodiceProdottoFornitoreCambiato.replace('{0}',codiceProdottoFornitorePrec).replace('{1}',codiceProdottoFornitore).replace('{2}',descrizione), attenzioneMsg.toUpperCase(), function(r) {
								    var $f = $("#" + fid);
									if (r) { 
								    	byPassCodiceProdottoFornitoreCheck = true;
								    	setTimeout(function() {return (validateFieldsBollaCarico(true) && setFormAction('BollaCaricoEditForm','pneBollaCarico_saveBolla.action', '', 'messageDiv'));},100);
								    } else {
								    	byPassCodiceProdottoFornitoreCheck = false;  
								    	$f.val(codiceProdottoFornitorePrec);
										$f.attr('oldVal', codiceProdottoFornitorePrec);
										unBlockUI();
										$("#" + fieldId).focus();
								    	enableAllFormFields();
								    }
								});
							}, 100);
							throw fieldId;
						}
					}
				}
				var strToReplaceIndexFrom = name.indexOf("[");
				var strToReplaceIndexTo = name.indexOf("]") + 1;
				var strToReplace = name.substring(strToReplaceIndexFrom, strToReplaceIndexTo);
				$(this).attr("name", name.replace(strToReplace, "[" + count + "]"));
				if (fieldId.indexOf("prog") != -1) {
					$(this).val(count);
				} 
				if (fieldId.indexOf("idDoc") != -1) {
					$(this).val($("#idDocumento").val());
				}
				if (fieldId.indexOf("descrizioneProdottoA") != -1) {
					$(this).val(descrizione);
				}
			});
			if (!hasProdotto) {
				if (showAlerts) {
					$.alerts.dialogClass = "style_1";
					jAlert(msgCampoObbligatorio.replace('{0}',msgProdotto), msgAttenzione, function() {
						$.alerts.dialogClass = null;
					});
				}
				unBlockUI();
				throw fieldId;
			}
			count++;
		});
		var dups = findDuplicates(arrCodProdottoInterno);
		if (dups.length > 0) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert(msgProdottiDuplicatiBolla, msgAttenzione, function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
	} catch (e) {
		$("#" + e.toString()).focus();
		return false;
	}
	enableAllFormFields();
	byPassAliquotaCheck = false;
	byPassCodiceProdottoFornitoreCheck = false;
	return true;
}
 
function addForn() {
	var url = appContext + "/pneFornitori_editFornitore.action";
	openDiv("popup_name_det", 900, 350, url, '', 'det', '');
	return false;
}

function nuovoProdottoSuccessCallback() {
	var $prodField = $("#product").prev("input:text");
	var $tr = $prodField.closest("tr");
	var $codProdFornitoreField = $tr.find(":input").first();		
	var $descTd = $codProdFornitoreField.closest("td").next().next();
	var $prodInternoField = $tr.find("input[id^='prodInterno']").first();
	var $descProdotto = $tr.find("input[id^='descrizioneProdottoA']").first();
	$prodField.val($("#prodottoEdicola\\.barcode").val());
	$prodInternoField.val($("#prodottoEdicola\\.codProdottoInterno").val());
	$descProdotto.val($("#prodottoEdicola\\.descrizioneProdottoA").val());
	$descTd.html($("#prodottoEdicola\\.descrizioneProdottoA").val());
	$("#closedet").trigger("click");
	setTimeout(function() {$("#" + $prodField.attr("id")).closest("td").next().next().find(":input").focus();}, 100);
}

function afterSuccessSaveFromBollaCarico(val) {
	$("#closedet").trigger("click");
	dojo.xhrGet({
		url: appContext + '/pubblicazioniRpc_getFornitori.action',			
		handleAs: "json",				
		headers: { "Content-Type": "application/json; charset=uft-8"}, 	
		preventCache: true,
		handle: function(data,args) {	
			data = JSON.parse(data);
			var list = '';		
			var sel = '';
            for (var i = 0; i < data.length; i++) {
            	if (val == data[i].value) {
            		sel = 'selected';
            	} else {
            		sel = '';
            	}
				list += '<option ' + sel + ' value="' + data[i].key + '">' + data[i].value + '</option>';
            }
            $("#fornitoriEdit").empty();
            $("#fornitoriEdit").html(list);
            $("#fornitoriEdit").trigger("change");
		}
    });
}

function addProduct(descProdotto) {
	var query = '';
	if (typeof(descProdotto) != 'undefined' && descProdotto != '') {
		query = '?descrizioneProdotto=' + escape(descProdotto);
	}
	var url = appContext + "/pne_editProdottoEdicolaContent.action" + query;
	openDiv("popup_name_det", 800, 500, url, '', 'det', '');
	return false;
}