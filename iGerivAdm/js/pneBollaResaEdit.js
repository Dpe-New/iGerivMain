var stopWaitingDivBollaResa = false;
var allowSearch = false;

$(document).ready(function() {
	
	$.datepicker.setDefaults($.datepicker.regional['it']);
	$('#dataDocumentoEdit').datepicker();
	
//	$("#dataDocumentoEdit").click(function() {
//    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
//	});
	
	if (isNew) {
		$("#dataDocumentoEdit").val(dataDocumento);
	}
	
	$("#deleteRow, #addRow, #imgPrint").tooltip({
		delay: 0,  
	    showURL: false
	});
	
	$("#meBollaResa").click(function() {
		if (!stopWaitingDivBollaResa) {
			ray.ajax();
		}
	});
	
	$("#meInvBollaResa").click(function() {
		memInvBollaResaPressed = true;
		if (!stopWaitingDivBollaResa) {
			ray.ajax();
		}
	});
	
	addFadeLayerEvents();
	setLastFocusedElement();
	setTableFields();
	$("#dataDocumentoEdit").blur(function() {
		setTimeout(function() {
			if ($("#dataDocumentoEdit").val().trim().length > 0 && $("#dataDocumentoEdit").trim().val() != $("#dataDocumentoOld").val()) {
				dojo.xhrGet({
					url: appContext + "/pneResaJ_isDataDocumentoBollaResaValida.action?codFornitore=" + $("#fornitoriEdit").val() + "&dataDocumento=" + $("#dataDocumentoEdit").val(),
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					handle: function(data,args) {
						unBlockUI();
						if (typeof(data.error) !== 'undefined') {
							$.alerts.dialogClass = "style_1";
							jAlert(data.error, msgAttenzione, function() {
								$.alerts.dialogClass = null;
							});
						} else if (typeof(data.result) !== 'undefined' && data.result.toString() === 'false') {
							$.alerts.dialogClass = "style_1";
							jAlert(msgDataDocumentoInvalida, msgAttenzione, function() {
								$.alerts.dialogClass = null;
							});
							setTimeout(function() {
								$(this).val('');
							}, 100);
							return false;
						}
					}
				});
			}
		}, 200);
	 });
	
	var fornVal = $("#fornitoriEdit").val();
	$("#fornitoriEdit").change(function() {
		if ($('#BollaResaEditTab_table > tbody tr').length > 1) {
			PlaySound('beep3');
			jConfirm(msgConfirmAzzerareVociBolla, attenzioneMsg, function(r) {
			    if (r) { 
			    	$('#BollaResaEditTab_table tbody tr:not(:last)').remove();
			    	fornVal = $("#fornitoriEdit").val();
			    	return false;
			    } else {
			    	$("#fornitoriEdit").val(fornVal);
		            return false;
			    }
			});
		}
	 });
	if (bollaInviata) {
		disableForm();
	} else {
		if (codFornitore != null) {
			$("#fornitoriEdit").val(codFornitore);
		}
		$("#fornitoriEdit").focus();
	}
	
	if (!bollaInviata) {
		$("#BollaResaEditForm img[src*='pdf.gif'], #BollaResaEditForm img[src*='xls.gif']").each(function() {
			var $link = $(this).closest("a");
			var href = $link.attr("href").replace('javascript:','');
			$link.attr("href", "javascript: memorizzaBolla(function() {" + href + "});");
		});
	}
	
});	

function onCloseLayer() {
	if (isNew || bollaMemorizzata) {
		$("#ricerca").trigger("click");
	}
}

function disableForm() {
	$("#BollaResaEditForm input:not(input[type=hidden])").each(function() {
		$(this).attr("disabled", true);
	});	
	$("#BollaResaEditForm select").each(function() {
		$(this).attr("disabled", true);
	});	
	$("#addRow, #deleteRow, #addFornitore").removeAttr("onclick");
}

function memorizzaBolla(callback) {
	if (validateFieldsBollaResa(true)) {
		ray.ajax();
    	$("#BollaResaEditForm").attr("action", "pneResaJ_saveBolla.action");
    	dojo.xhrPost({
			form: "BollaResaEditForm",			
			handleAs: "json",		
			handle: function(data,args) {
				$("#BollaResaEditForm").attr("action", "pneResa_editBolla.action");
				unBlockUI();
				if (typeof(data.error) !== 'undefined') {
					$.alerts.dialogClass = "style_1";
					jAlert(data.error, msgAttenzione, function() {
						$.alerts.dialogClass = null;
					});
				} else if (typeof(data) !== 'undefined' && typeof(data.result) !== 'undefined') {
					bollaMemorizzata = true;
					$("#codResa").val(data.result);
					$("#BollaResaEditForm input[name='idDocumento']").val(data.result);
					$("#isNew").val("false");
					if (typeof(callback) === 'function') {
						callback();
						$("#BollaResaEditTab_table tbody tr:first td:nth-child(4)").find("input:text").focus();
					} else {
						jAlert(okMessage, msgAvviso.toUpperCase(), function() {
							$("#BollaResaEditTab_table tbody tr:first td:nth-child(4)").find("input:text").focus();
						});
					}
				}
				disableFields();
			}		
		});
   	}
}

function memorizzaInviaBolla() {
	jConfirm(msgConfirmMemorizzaInviaBolla.replace("{0}", $("#fornitoriEdit :selected").text()), attenzioneMsg, function(r) {
	    if (r) {
	    	if (validateFieldsBollaResa(true)) {
	    		ray.ajax();
		    	$("#BollaResaEditForm").attr("action", "pneResaJ_sendBolla.action");
		    	dojo.xhrPost({
					form: "BollaResaEditForm",			
					handleAs: "json",		
					handle: function(data,args) {
						$("#BollaResaEditForm").attr("action", "pneResa_editBolla.action");
						unBlockUI();
						if (typeof(data.error) !== 'undefined') {
							$.alerts.dialogClass = "style_1";
							jAlert(data.error, msgAttenzione, function() {
								$.alerts.dialogClass = null;
							});
						} else if (typeof(data) !== 'undefined' && typeof(data.result) !== 'undefined') {
							bollaMemorizzata = true;
							$("#codResa").val(data.result);
							$("#isNew").val("false");
							memInviaSuccessCallback();
							disableForm();
						}
					}		
				});
	    	}
	    } else {
	    	setTimeout(function() {
				unBlockUI();						
			}, 200);
	    }
        return false;
	}, true, false);
}

function memInviaSuccessCallback() {
	blockUIForDownload();
	document.forms.BollaResaEditForm.ec_eti.value='';
	$("#BollaResaEditForm").attr("action", "reportPne_reportBollaResaProdottiVariPdf.action?idDocumento=" + $("#codResa").val()); 	
	$("#BollaResaEditForm").submit();
}

function finishDownloadCallback() {
	if (memInvBollaResaPressed) {
		dojo.xhrGet({
			url: appContext + "/pneResaJ_getReportBollaResaFileName.action?idDocumento=" + $("#codResa").val(),
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				setTimeout(function() {
					unBlockUI();						
				}, 200);
				if (typeof(data.result) !== 'undefined') {
					$("#printLink").attr("href", "/bolle_resa_prodotti_vari/" + codFiegDl + "/" + codEdicolaMaster + "/" + data.result);
					$('#butDiv').html($('#printDiv').css({"visibility":"visible"}));
				}
			}
		});
		memInvBollaResaPressed = false;
	}
	document.forms.BollaResaEditForm.ec_eti.value='';
}

function disableFields() {
	$("#BollaResaEditTab_table tbody tr").each(function() {
		var $tr = $(this);
		if (!($tr.hasClass('calcRow'))) {
			$tr.find("td:nth-child(1)").find("input:text").attr('disabled',true);
			$tr.find("td:nth-child(5)").find("input:text").attr('disabled',true);
			$tr.find("td:nth-child(6)").find("input:text").attr('disabled',true);
			$tr.find("td:nth-child(7)").find("select").attr('disabled',true);
			$tr.find("td:nth-child(8)").find("input:text").attr('disabled',true);
		}
	});
}

function setTableFields() {
	var count = 0;
	$("#BollaResaEditTab_table tbody tr").each(function() {
		var $tr = $(this);
		if (!($tr.hasClass('calcRow'))) {
			var str = '<input name="dettagli[' + count + '].prodotto.edicola.codEdicola" value="' + userId + '" type="hidden">';
			$tr.append(str);
			var index = $tr.find("td").first().find("input[id*='prog']").val();
			attachEventsToRow($tr, index);
			count++;
		}
	});
}


function removeTableRow() {
	if (bollaInviata) {
		return false;
	}
	if ($("#" + lastFocusedFieldId.replace(/([|@.\[\]])/g,'\\\$1')).length > 0) {
		var $tr = $("#" + lastFocusedFieldId.replace(/([|@.\[\]])/g,'\\\$1')).closest("tr");
		if ($tr.hasClass('odd') || $tr.hasClass('even')) {
			$tr.remove();
		}
	} else {
		$('#BollaResaEditTab_table > tbody tr:last').prev().remove();
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
	if (bollaInviata) {
		return false;
	}
	$('#BollaResaEditTab_table > tbody tr td:nth-child(2)').find("img").each(function() {
		$(this).remove();
	});
	var index = $('#BollaResaEditTab_table > tbody tr').length;
	var tabRow = '<tr class="odd" style="height:22px"><td style="text-align: left;" width="8%"><input name="dettagli[' + index + '].codiceProdottoFornitore" id="codiceProdottoFornitore' + index + '" value="" style="text-align: left;" size="6" maxlength="10" type="text"></td>';
	tabRow += '<td style="text-align: left;" width="14%; white-space: nowrap;"><input name="dettagli[' + index + '].prodotto.barcode" id="barcode' + index + '" value="" style="text-align: left; vertical-align:middle" size="23" maxlength="20" type="text"></td>';
	tabRow += '<td width="30%"></td>';
	tabRow += '<td width="8%" style="text-align: left;"><input name="dettagli[' + index + '].quantita" id="quant' + index + '" value="" style="text-align: left;" size="3" maxlength="10" type="text"></td>';
	tabRow += '<td style="text-align: left;" width="8%"><input name="dettagli[' + index + '].prezzo" id="prezzo' + index + '" value="" style="text-align: right;" size="8" maxlength="10" type="text" disabled="true"></td>';
	tabRow += '<td style="text-align: left;" width="8%"><input name="dettagli[' + index + '].importo" id="importo' + index + '" value="" style="text-align: right;" size="8" maxlength="10" type="text" disabled="true"></td>';
	tabRow += '<td style="text-align: left;" width="8%"><select name="dettagli[' + index + '].prodotto.aliquota" id="aliquota' + index + '" value="" style="text-align:center; font-size: 14px; width: 60px;" disabled="true">' + getIvaOptions() + '</select></td>';
	tabRow += '<td style="text-align: left;" width="8%"><input name="dettagli[' + index + '].giacenzaProdotto" id="giacenzaProdotto' + index + '" value="" style="text-align:center;" size="2" type="text" disabled="true"></td>';
	tabRow += '<input name="dettagli[' + index + '].pk.progressivo" id="prog' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].pk.idDocumento" id="idDoc' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.codProdottoInterno" id="prodInterno' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.edicola.codEdicola" value="' + userId + '" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.descrizioneProdottoA" id="descrizioneProdottoA' + index + '" value="" type="hidden">';
	tabRow += '<input name="dettagli[' + index + '].prodotto.percentualeResaSuDistribuito" id="percentualeResaSuDistribuito' + index + '" value="" type="hidden">';
	tabRow += '</tr>';
	if ($("#" + lastFocusedFieldId).length > 0 && $("#" + lastFocusedFieldId).attr("name").indexOf("dettagli") != -1) {
		var $tr = $("#" + lastFocusedFieldId).closest("tr");
		if ($tr.hasClass('odd') || $tr.hasClass('even')) {
			$tr.after(tabRow);
		}
	} else {
		$('#BollaResaEditTab_table tbody tr:last').before(tabRow);
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
	$prezzoField.numeric({ decimal : ",", negative : false });
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
				url: appContext + "/automcomplete_getProdottoNonEditorialeInBolla.action?codFornitore=" + $("#fornitoriEdit").val() + "&soloProdottiForniti=true",
				delay: 100,
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					setTimeout(function() {
						unBlockUI();						
					}, 200);
					if (data.length == 0) {
						focusNextField();
					} else if (data.length == 1) {
						var $td = $codProdFornitoreField.closest("td");
						var $prodTd = $td.next();
						var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
						var $percentualeResaSuDistribuito = $td.closest("tr").find(":input:hidden[name*='prodotto.percentualeResaSuDistribuito']");
						var $prodField = $prodTd.find(":input");
						var $descTd = $td.next().next();
						var $quantField = $descTd.next();
						var $prezzoTd = $quantField.next();
						var $prezzoField = $prezzoTd.find(":input");
						var $importoField = $prezzoTd.next().find(":input");
						var $ivaField = $prezzoTd.next().next().find("select");
						var $giacField = $prezzoTd.next().next().next().find(":input");
						$prodField.val(data[0].barcode);
						$codProdoInternoField.val(data[0].codProdotto);
						$percentualeResaSuDistribuito.val(data[0].percentualeResaSuDistribuito);
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
						$giacField.val(data[0].giacenza);
						$codProdFornitoreField.attr('oldVal', $codProdFornitoreField.val());
						$codProdFornitoreField.attr("disabled", true);
						$prezzoField.attr("disabled", true);
						$importoField.attr("disabled", true);
						$giacField.attr("disabled", true);
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
		                                    prodottoDl: item.prodottoDl,
		                                    percentualeResaSuDistribuito: item.percentualeResaSuDistribuito,
		                                    giacenza: item.giacenza
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
			var $percentualeResaSuDistribuito = $td.closest("tr").find(":input:hidden[name*='prodotto.percentualeResaSuDistribuito']");
			var $prodField = $prodTd.find(":input");
			var $descTd = $td.next().next();
			var $quantField = $descTd.next();
			var $prezzoTd = $quantField.next();
			var $prezzoField = $prezzoTd.find(":input");
			var $importoField = $prezzoTd.next().find(":input");
			var $ivaField = $prezzoTd.next().next().find("select");
			var $giacField = $prezzoTd.next().next().next().find(":input");
			$prodField.val(ui.item.barcode);
			$codProdoInternoField.val(ui.item.id);
			$percentualeResaSuDistribuito.val(ui.item.percentualeResaSuDistribuito);
			$prezzoField.val(displayNum(Number(ui.item.prezzo).toFixed(2)));
			$prezzoField.attr("disabled", true);
			$prodField.attr("disabled", true);
			$descTd.html(ui.item.descrizione);
			$ivaField.val(ui.item.iva);
			$ivaField.attr('oldVal', ui.item.iva);
			$giacField.val(ui.item.giacenza);
			if (ui.item.iva != '') {
				$ivaField.attr("disabled", true);
			} else {
				$ivaField.attr("disabled", false);
			}
			$codProdFornitoreField.attr('oldVal', $codProdFornitoreField.val());
			$codProdFornitoreField.attr("disabled", true);
			$prezzoField.attr("disabled", true);
			$importoField.attr("disabled", true);
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
	
	$quantField.numeric({ decimal : false, negative : false });
	
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
				url: appContext + '/pubblicazioniRpc_getPneBarcodeOrDescrizione.action?barcode=' + $val + '&codFornitore=' + $("#fornitoriEdit").val() + "&soloProdottiForniti=true",
				delay: 100,
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					setTimeout(function() {
						unBlockUI();						
					}, 200);
					if (data.length == 1) {
						var $td = $codProdFornitoreField.closest("td");
						var $prodTd = $td.next();
						var $codProdoInternoField = $td.closest("tr").find(":input:hidden[name*='prodotto.codProdottoInterno']");
						var $percentualeResaSuDistribuito = $td.closest("tr").find(":input:hidden[name*='prodotto.percentualeResaSuDistribuito']");
						var $prodField = $prodTd.find(":input");
						var $descTd = $td.next().next();
						var $codProdFornField = $td.find(":input");
						var $prezzo = $descTd.next().next().find(":input");
						var $importoField = $descTd.next().next().next().find(":input");
						var $ivaField = $descTd.next().next().next().next().find("select");
						var $giacField = $descTd.next().next().next().next().next().find(":input");
						$prodField.val(data[0].barcode);
						$codProdoInternoField.val(data[0].codProdotto);
						$percentualeResaSuDistribuito.val(data[0].percentualeResaSuDistribuito);
						$descTd.html(data[0].descrizione);
						$prezzo.val(displayNum(Number(data[0].ultimoPrezzoAcquisto).toFixed(2)));
						$prezzo.attr("disabled", true);
						$ivaField.val(data[0].iva);
						$ivaField.attr('oldVal', data[0].iva);
						if (data[0].iva != '') {
							$ivaField.attr("disabled", true);
						} else {
							$ivaField.attr("disabled", false);
						}
						$importoField.attr("disabled", true);
						$descProdotto.val(data[0].descrizione);
						if (data[0].codiceProdottoFornitore.length > 0) {
							$codProdFornField.val(data[0].codiceProdottoFornitore);
							$codProdFornField.attr('oldVal', data[0].codiceProdottoFornitore);
							$codProdFornField.attr("disabled", true);
						}
						$giacField.val(data[0].giacenza);
						$("#" + lastFocusedFieldId).closest("td").next().next().find(":input").focus();
					} else if (data.length > 1) {
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
                                prodottoDl: item.prodottoDl,
                                percentualeResaSuDistribuito: item.percentualeResaSuDistribuito,
                                giacenza: item.giacenza
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
			var $percentualeResaSuDistribuito = $td.closest("tr").find(":input:hidden[name*='prodotto.percentualeResaSuDistribuito']");
			var $codProdFornField = $td.find(":input");
			var $importoField = $descTd.next().next().next().find(":input");
			var $ivaField = $descTd.next().next().next().next().find("select");
			var $giac = $descTd.next().next().next().next().next().find(":input");
			$codProdoInternoField.val(ui.item.id);
			$percentualeResaSuDistribuito.val(ui.item.percentualeResaSuDistribuito);
			$descTd.html(ui.item.descrizione);
			var $prezzo = $descTd.next().next().find(":input");
			$prezzo.val(displayNum(Number(ui.item.ultimoPrezzoAcquisto).toFixed(2)));
			$prezzo.attr("disabled", true);
			$importoField.attr("disabled", true);
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
				$codProdFornField.attr("disabled", true);
			}
			$giac.val(ui.item.giacenza);
			setTimeout(function() {$("#" + lastFocusedFieldId).closest("td").next().next().find(":input").focus();}, 100);
		}
	});	
	
	validateQuantitaReso = function(resoField) {
		var $tr = resoField.closest("td").closest("tr");
		var codProdoInternoField = $tr.find(":input:hidden[name*='prodotto.codProdottoInterno']").val();
		var percentualeResaSuDistribuito = $tr.find(":input:hidden[name*='prodotto.percentualeResaSuDistribuito']").val();
		if (resoField.val().trim().length > 0) {
			if (Number(percentualeResaSuDistribuito) === 0) {
				$.alerts.dialogClass = "style_1";
				jAlert($.validator.format(msgResaNonConsentita, [$descProdotto.val()]), msgAttenzione, function() {
					$.alerts.dialogClass = null;
				});
				setTimeout(function() {
					resoField.val('');
				}, 100);
				return false;
			} else {
				dojo.xhrGet({
					url: appContext + "/pneResaJ_isQuantitaResoValida.action?reso=" + resoField.val() + "&idProdotto=" + codProdoInternoField,
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					handle: function(data,args) {
						setTimeout(function() {
							unBlockUI();						
						}, 200);
						if (typeof(data.error) !== 'undefined') {
							$.alerts.dialogClass = "style_1";
							jAlert(data.error, msgAttenzione, function() {
								$.alerts.dialogClass = null;
							});
						} else if (typeof(data.result) !== 'undefined' && data.result.toString() === 'false') {
							$.alerts.dialogClass = "style_1";
							jAlert($.validator.format(msgQtaResoInvalida, [$descProdotto.val()]), msgAttenzione, function() {
								$.alerts.dialogClass = null;
							});
							setTimeout(function() {
								resoField.val('');
							}, 100);
							return false;
						}
					}
				});
			}
		}
	};
	
	$quantField.blur(function(event) { 
		var prodFieldId = $(this).attr("id");
		try {
			if (hasMemorizzaInvia) {
				validateQuantitaReso($(this));
			}
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
	var $totalImpField = $("#BollaResaEditTab_table tbody tr.calcRow td.calcResult");
	var total = 0;
	$("#BollaResaEditTab_table tbody tr td:nth-child(6)").each(function() {
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

function doDelete() {
	PlaySound('beep3');
	jConfirm(msgCancellareBolla, attenzioneMsg, function(r) {
	    if (r) {
	    	$("#numeroDocumentoEdit").attr("disabled", false);
	    	setFormAction('BollaResaEditForm','pneResa_deleteBolla.action', '', 'messageDiv', true, null, function() {$("#ricerca").trigger('click');}, null, false);
			deleted = true;
			setTimeout('unBlockUI();', 100);
	    } else {
	    	setTimeout('unBlockUI();', 100);
	    	return false;
	    }
	});
}

function validateFieldsBollaResa(showAlerts) {
	if ($("#fornitoriEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgFornitore), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		setTimeout(function() {
			unBlockUI();						
		}, 200);
		return false;
	}
	if ($("#dataDocumentoEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgDataDocumento), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		setTimeout(function() {
			unBlockUI();						
		}, 200);
		return false;
	} else if (!checkDate($("#dataDocumentoEdit").val())) {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgFormatoDataInvalido.replace('{0}', $("#dataDocumentoEdit").val()), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		setTimeout(function() {
			unBlockUI();						
		}, 200);
		return false;
	}
	if ($("#numeroDocumentoEdit").val() == '') {
		if (showAlerts) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgCampoObbligatorio.replace('{0}',msgNumeroDocumento), msgAttenzione, function() {
				$.alerts.dialogClass = null;
			});
		}
		setTimeout(function() {
			unBlockUI();						
		}, 200);
		return false;
	}
	var count = 0;
	try{
		var arrCodProdottoInterno = [];
		$('#BollaResaEditTab_table tbody tr:not(:last)').each(function() {
			var hasProdotto = true;
			var descrizione = $(this).find("td:nth-child(3)").text().trim();
			if (descrizione.length == 0) {
				if (showAlerts) {
					$.alerts.dialogClass = "style_1";
					jAlert(msgErrorProdotto, msgAttenzione, function() {
						$.alerts.dialogClass = null;
					});
				}
				var fieldId = $(this).find("td:nth-child(2)").find(":input:text").attr("id");
				setTimeout(function() {
					unBlockUI();						
				}, 200);
				throw fieldId;
			}
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
					setTimeout(function() {
						unBlockUI();						
					}, 200);
					throw fieldId;
				} else if (fieldId.indexOf("quant") != -1 && Number($(this).val()) > 1000) {
					if (showAlerts) {
						$.alerts.dialogClass = "style_1";
						jAlert(numeroNonValido, msgAttenzione, function() {
							$.alerts.dialogClass = null;
						});
					}
					setTimeout(function() {
						unBlockUI();						
					}, 200);
					throw fieldId;
				}
				if (fieldId.indexOf("aliquota") != -1 && $(this).val().trim() != '') {
					$(this).attr("disabled", false);
				}
				if (fieldId.indexOf("prezzo") != -1 && $(this).val().trim() != '') {
					$(this).attr("disabled", false);
				}
				if (fieldId.indexOf("importo") != -1 && $(this).val().trim() != '') {
					$(this).attr("disabled", false);
				}
				var strToReplaceIndexFrom = name.indexOf("[");
				var strToReplaceIndexTo = name.indexOf("]") + 1;
				var strToReplace = name.substring(strToReplaceIndexFrom, strToReplaceIndexTo);
				$(this).attr("name", name.replace(strToReplace, "[" + count + "]"));
				if (fieldId.indexOf("prog") != -1) {
					$(this).val(count);
				} 
				if (fieldId.indexOf("idDoc") != -1) {
					$(this).val($("#codResa").val());
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
				setTimeout(function() {
					unBlockUI();						
				}, 200);
				throw null;
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
			setTimeout(function() {
				unBlockUI();				
			}, 200);
			return false;
		}
	} catch (e) {
		$("#" + e.toString()).focus();
		return false;
	}
	$("#dataDocumentoEdit").removeAttr("disabled");
	$("#numeroDocumentoEdit").removeAttr("disabled");
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

function afterSuccessSaveFromBollaResa(val) {
	$("#closedet").trigger("click");
	dojo.xhrGet({
		url: appContext + '/pubblicazioniRpc_getFornitori.action',			
		handleAs: "json",				
		headers: { "Content-Type": "application/json; charset=utf-8"}, 	
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

function closeLayerConfirm() {
	jConfirm(msgConfirmCloseDialog, attenzioneMsg, function(r) {
		if (r) {
			$("#close").trigger('click');
		}
	}, true, false);
}
