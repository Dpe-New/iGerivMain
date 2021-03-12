setDataTipoBolla(msgDataTipoBolla, "dataTipoBolla");
if (conditionItensBolla) {
	$(document).ready(function() {
		$("#BollaResaTab_table").thfloat();
		var container = $("#messageDiv");
		if (conditionDisableForm) {
			disableBollaResa();
		} else {
			container.css({"border":"none"});
			container.text("");
		}						
		$('#totalHeader_1').text($('#BollaResaTab_table tr.calcRow td:nth-child(6)').text().trim());
		$('#totalHeader_2').text($('#BollaResaTab_table tr.calcRow td:nth-child(11)').text().trim());
		$('#totalHeader_3').text($('#BollaResaTab_table tr.calcRow td:nth-child(12)').text().trim());	
		
		$('#totalHeader_1_viewBollaResa').text($('#BollaResaTab_table tr.calcRow td:nth-child(7)').text().trim());
		$('#totalHeader_2_viewBollaResa').text($('#BollaResaTab_table tr.calcRow td:nth-child(12)').text().trim());
		$('#totalHeader_3_viewBollaResa').text($('#BollaResaTab_table tr.calcRow td:nth-child(13)').text().trim());	
		
		addFadeLayerEvents();
		setLastFocusedElement();
		setOnChangeEventOnTextFields();
		setBorderToFuoriVoce();
		setNoteOnTitle();
		setTooltipOnRichiamoResa();
		setStatisticheOnNumero();
		setVariazioniOnDistribuito();
		setContentDivHeight(20);
		$("#memorizza, #memorizzaInvia").tooltip({
            delay: 0,
            showURL: false
        });
		if ($("#autoincrement").length > 0) {
			if ($("#autoincrement").attr("checked") == true) {
				$("#qtaReso").val(1);
				$("#quantita").show();
				$("#lastRowId").show();
			} else {
				$("#quantita").hide();
				$("#lastRowId").hide();
			}
			$("#autoincrement").change(function() {
				if ($(this).attr("checked") == true) {
					$("#quantita").show();
					$("#lastRowId").show();
					$("#qtaReso").val(1);
					$("#barcode").focus();
				} else {
					$("#quantita").hide();
					$("#lastRowId").hide();
					$("#barcode").focus();
				}
			});
			$("#soloResoDaInserire").change(function() {
				$("#barcode").focus();
			});
			 	 
		}
		
		$(this).bind('keydown', bollaResaKeyDown);
		
		$.altPlus(function() {
			var val = isNaN($("#qtaReso").val()) ? 0 : Number($("#qtaReso").val());
			$("#qtaReso").val(++val);
		}, null);
		
		$.altMinus(function() {
			var val = isNaN($("#qtaReso").val()) ? 0 : Number($("#qtaReso").val());
			$("#qtaReso").val(--val);
		}, null);
		
		if ($('#barcode').length > 0) { 
			$('#barcode').keydown({callback: null}, manageBarcodeKeyDownBollaResa);
			$('#barcode').focus();
		}
		
		$('#BollaResaForm input:text[name^=reso]').change(function() {
			setChangedFieldAttribute($(this));	
		}); 
		
		saveAndSendBollaResa = function() {
			
			
			confirmMemorizzaInvia = function() {
				setTimeout(
						jConfirm(memorizzaInviaConfirm, attenzioneMsg, function(r) { 
							if (r) {
								return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_send.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); reloadDataTipoBolleResa(); disableBollaResa();}, '', true));
							} 
							else {
								unBlockUI();
							};}, true, false),10);
				};
			
			
			confirmMemorizzaInviaMenta= function() {
				
				var quotidianiInBolla = 'false';
				var dataBolla = $("#dataTipoBolla").val().split("|")[0];
				var tipoBolla = $("#dataTipoBolla").val().split("|")[1].replace("Tipo","").trim();
				
				dojo.xhrGet({
					url: appContext + namespace + '/pubblicazioniRpc_verificaMentaSeQuotidianiInBollaResa.action?dataBollaResaMenta=' + dataBolla + '&tipoBollaResaMenta=' + tipoBolla ,	
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=uft-8"}, 	
					preventCache: true,
					handle: function(data,args) {
						
						unBlockUI();
						if (args.xhr.status == 200) {
							if(data.length>0){
								for (i = 0; i < data.length; i++) {
									  var quotidianiPresentiInBolla = data[i].quotidianiPresentiInBolla;
									  
									  if(quotidianiPresentiInBolla.toString() == 'true'){
										  setTimeout(
													jConfirm("NUOVO MESSAGGIO MENTA CONTROLLO QUOTIDIANI IN BOLLA", attenzioneMsg, function(r) { 
														if (r) {
															return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_send.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); reloadDataTipoBolleResa(); disableBollaResa();}, '', true));
														} 
														else {
															unBlockUI();
														};}, true, false),10);
									  }else{
										  setTimeout(
													jConfirm(memorizzaInviaConfirm, attenzioneMsg, function(r) { 
														if (r) {
															return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_send.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); reloadDataTipoBolleResa(); disableBollaResa();}, '', true));
														} 
														else {
															unBlockUI();
														};}, true, false),10);
									  }
									  
									
								}
							}
						}else{
							setTimeout(
									jConfirm(memorizzaInviaConfirm, attenzioneMsg, function(r) { 
										if (r) {
											return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_send.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); reloadDataTipoBolleResa(); disableBollaResa();}, '', true));
										} 
										else {
											unBlockUI();
										};}, true, false),10);
						}

					}
			    });
				
				
				
				
				
				
				
				
				
				
//				var res = verificaMentaSeQuotidianiInBollaResa();
//				setTimeout(
//					jConfirm("NUOVO MESSAGGIO MENTA CONTROLLO QUOTIDIANI IN BOLLA", attenzioneMsg, function(r) { 
//						if (r) {
//							return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_send.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); reloadDataTipoBolleResa(); disableBollaResa();}, '', true));
//						} 
//						else {
//							unBlockUI();
//						};}, true, false),10);
				};
			
			
			
			
			getCopieFuoriResa = function() {
				var totale = 0;
				$("#BollaResaForm input:text[name^='resoFuoriVoce']").each(function() {
					totale += !isNaN($(this).val()) ? Number($(this).val().trim()) : 0;
				});
				return totale;
			};
			
			if (hasPopupConfermaSuMemorizzaInviaBolle) {
				var totaleCopieFuoriResa = getCopieFuoriResa();
				var msg = $.validator.format(msgConfermaTotaliResa, [$("#totalHeader_1").text(), totaleCopieFuoriResa, $("#totalHeader_2").text(), $("#totalHeader_3").text()]);
				setTimeout(
					jConfirm(msg, attenzioneMsg, function(r) {
						if (r) {
							confirmMemorizzaInvia();
						} 
						unBlockUI();
					}, true, false)
				,10);
			} else {
				
				if(isMenta){
					confirmMemorizzaInvia();
					//confirmMemorizzaInviaMenta();
				}else{
					confirmMemorizzaInvia();
				}
			}
		};
				
		saveBollaResa = function() {
			setTimeout(function() {
				return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_save.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); }, '', true));},10);
		};
		
		saveBollaResaAuto = function() {
			setTimeout(function() {
				return (validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_save.action', '', 'messageDiv', false, '', function() { setFieldsToSave(false); deleteEmptyRowsFuoriVoce(); }, '', false));},10);
		};
		
		
		
	});
	
	function manageBarcodeKeyDownBollaResa(evt) {
		manageBarcodeKeyDown(evt.data.callback, evt);
	}
	
	function disableBollaResa() {
		disableAllFormFields('dlSelect','dataTipoBolla','submitFilter','popup_ok');
		if (conditionDlReadOnly) {
			$("input:button[name='plg.inserisci.nuovo.fuori.voce'], input:button[name='igeriv.memorizza'], input:button[name='igeriv.memorizza.invia']").hide();
		}
		var newdiv = document.createElement("div");
		newdiv.style.position = "absolute";
		newdiv.style.top = "0px";
		newdiv.style.color = "red";
		newdiv.style.width = "180px";
		newdiv.style.borderStyle = "solid";
		newdiv.style.border = "1px solid red";
		newdiv.style.textAlign = "center";
		newdiv.innerHTML = "<b>" + msgBollaGiaInviata;		
		var container = $("#messageDiv");
		container.css({"width":"180px", "left":"0px"});
		if (container.find("div")[0]) {
			y = container.find("div")[0];
			container.replace(newdiv, y);
		} else {
			container.append(newdiv);
		}
	}
	
	function bollaResaKeyDown(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);
		if ((!event.shiftKey && keycode == '9') || keycode == '40') {	// tab	|| arrow down
			setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var prevField = $("#" + id); if(prevField.val() != prevField.data('oldVal')) {prevField.trigger("blur");} prevField.css('background','#fff'); var field = prevField.closest("tr").next().find("input:text[name^='reso']").first(); if ($("#popup_container").is(":visible")) {} else {field.focus();}}, 100);
		} else if ((event.shiftKey && keycode == '9') || keycode == '38') {	// shift + tab	|| arrow up
			setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var prevField = $("#" + id); if(prevField.val() != prevField.data('oldVal')) {prevField.trigger("blur");} prevField.css('background','#fff'); var field = prevField.closest("tr").prev().find("input:text[name^='reso']").first(); if ($("#popup_container").is(":visible")) {} else {field.focus();}}, 100);
		} else if (keycode == 37) { // left arrow
			event.preventDefault();
			$("#qtaReso").focus();
			$("#barcode").css('background','#fff');
		} else if (keycode == 39) { // right arrow
			event.preventDefault();
			$("#barcode").focus();
			$("#qtaReso").css('background','#fff');
		}
	}
	
	window.onbeforeunload = function() {
		if (attivaMemorizzazioneAutomatica && $("#dtb").val().split("|")[2] == "true" && $("#BollaResaForm input:hidden[name^=modificato][value=true]").length > 0) {
			validateFields('BollaResaForm') && setFieldsToSave(true) && setFormAction('BollaResaForm','bollaResa_save.action', '', 'messageDiv', true, '', function() {setFieldsToSave(false); deleteEmptyRowsFuoriVoce();}, '', false);
			return msgConfirmSaveBolla;
		}
	};	
	
	function tableRowClick(obj) {		
		lastFieldFocusId = $(this).parent().find('input:text[name^=reso]').first().attr("id");
		var popID = 'popup_name';	  
	    var popURL = $(this).parent().attr('divParam');
	    var pk = $(this).parent().attr('pk'); 
	 	var popWidth = 900; 
	 	var popHeight = 300;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var cpu = dim[0].split('=')[1];
	    var coddl = pk.split("|")[0];
	    var crivw = pk.split("|")[1];
	 	var url = appContext + "/bollaResa_showBollaResaFuoriVoce.action?cpu=" + cpu + "&coddl=" + coddl + "&crivw=" + crivw + "&dataTipoBolla=" + escape($('#dataTipoBolla').val());	 
	 	openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	if (conditionDisableFormAndNotReadonly) {
		var $colTitolo = $("#BollaResaTab_table tbody").find("td:nth-child(3)");
		$colTitolo.css("cursor","pointer");
		$colTitolo.click(tableRowClick);
		$colTitolo.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b>" + msgInserisciModificaResaFuoriVoce + "</b>";
		    } 
		}); 
	}
	
	function noBarcodeFoundAction(barcode, confirmResult, confirmResultCD, confirmResultDPNA) {
		var query = '?barcode=' + barcode + '&dataTipoBolla=' + escape($("#dataTipoBolla").val()) 
		+ ((typeof(confirmResult) === 'undefined' || confirmResult == null)? '' : '&confirmResult=' + confirmResult) 
		+ ((typeof(confirmResultCD) === 'undefined' || confirmResultCD == null) ? '' : '&confirmResultCD=' + confirmResultCD) 
		+ ((typeof(confirmResultDPNA) === 'undefined' || confirmResultDPNA == null) ? '' : '&confirmResultDPNA=' + confirmResultDPNA);
		
		var hasTipoA = $("#dataTipoBolla option[value*='|Tipo A|']").first().val() != null;
		var hasTipoB = $("#dataTipoBolla option[value*='|Tipo B|']").first().val() != null;
		dojo.xhrGet({ 
			url: appContext + '/pubblicazioniRpc_getPubblicazioneByBarcode.action' + query + "&hasBolleQuotidianiPeriodiciDivise=" + (hasTipoA && hasTipoB),			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 			
			preventCache: true,
			handle: function(data,args) {
				if ((data != null) && (typeof data != 'undefined') && (data.length > 0)) {
					var found = false;
					var copertina = data[0];
					if (copertina.err && copertina.err.length > 0) {
						$.alerts.dialogClass = "style_1";
						jAlert(copertina.err, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							$('#barcode').val('');
							$('#barcode').focus();
						});
						return false;
					} else if (copertina.confirm && copertina.confirm.length > 0) {
						PlaySound('beep3');
						jConfirm(copertina.confirm, attenzioneMsg, function(r) {
							if (r) {
								noBarcodeFoundAction(barcode, r);
							} else {
								$('#barcode').val('');
								$('#barcode').focus();
							}
							return false;
						}, true, true);
						return false;
					} else if (copertina.confirmCD && copertina.confirmCD.length > 0) {
						PlaySound('beep3');
						jConfirm(copertina.confirmCD, attenzioneMsg, function(r) {
							if (r) {
								noBarcodeFoundAction(barcode, null, r);
							} else {
								$('#barcode').val('');
								$('#barcode').focus();
							}
							return false;
						}, true, true);
						return false;
					}else if (copertina.confirmDPNA && copertina.confirmDPNA.length > 0) {
						PlaySound('beep3');
						jConfirm(copertina.confirmDPNA, attenzioneMsg, function(r) {
							if (r) {
								noBarcodeFoundAction(barcode, null,null, r);
							} else {
								$('#barcode').val('');
								$('#barcode').focus();
							}
							return false;
						}, true, true);
						return false; 
					}else {
						$("#BollaResaTab_table tbody tr td:nth-child(2)").each(function() {
							var $cell = $(this);
							var field = $("#" + copertina.pk.replace(/([|@])/g,'\\\$1'));
							if (field.length > 0) {
								return false;
							} else if (Number($cell.text().trim()) == Number(copertina.cpu)) {
								found = true;	
								var $parentRow = $cell.closest("tr");
								// 15/12/2016 CDL - Gestione Ceste  	 
								var trTag = null;
								if(isCDL){
									trTag = getRowHtmlResaCDL(copertina, 0);
								}else{
									trTag = getRowHtmlResa(copertina, 0);
								}

								$(trTag).insertAfter($parentRow);
								$("#content1").css({"height": ($("#BollaResaForm").height() + 30) + "px"});
								var hiddenResoOld = "<input type='hidden' id='reso_old_" + copertina.pk + "' value='1'/>";
								var hiddenGiacenzaOld = "<input type='hidden' id='giacenza_old_" + copertina.pk + "' value='" + copertina.giacenza + "'/>";
								$('#extraHidden').append(hiddenResoOld + hiddenGiacenzaOld);
								var id = copertina.pk.replace(/([|@])/g,'\\\$1');
								field = $("#" + id);
								field.keydown( focusBarcodeField );
								setTotalValues(field);
								setOnChangeEventOnTextFields();
								setBorderToFuoriVoce();
								setFocusedFieldStyle();
								var $tds = $("#BollaResaTab_table tbody").find("td:nth-child(3)");
								$tds.unbind("click", tableRowClick);
								$tds.css("cursor","pointer").click(tableRowClick);
								return false;
							} 
						});
					}
					if (!found) {
						var pk = copertina.pk.replace('resoFuoriVoce','reso').replace(/([|@])/g,'\\\$1');
						var field = $("#" + pk);
						if (field.length <= 0) {
							// 15/12/2016 CDL - Gestione Ceste  	 
							var trTag = null;
							if(isCDL){
								trTag = getRowHtmlResaCDL(copertina, 0);
							}else{
								trTag = getRowHtmlResa(copertina, 0);
							}
							$(trTag).insertBefore($('#BollaResaTab_table tbody tr:last'));
							$("#content1").css({"height": ($("#BollaResaForm").height() + 30) + "px"});
							var hiddenResoOld = "<input type='hidden' id='reso_old_" + copertina.pk + "' value='1'/>";
							var hiddenGiacenzaOld = "<input type='hidden' id='giacenza_old_" + copertina.pk + "' value='" + copertina.giacenza + "'/>";
							$('#extraHidden').append(hiddenResoOld + hiddenGiacenzaOld);
							var id = copertina.pk.replace(/([|@])/g,'\\\$1');
							field = $("#" + id);
							field.keydown( focusBarcodeField );
							setTotalValues(field);
							setOnChangeEventOnTextFields();
							setBorderToFuoriVoce();
							setFocusedFieldStyle();
							var $tds = $("#BollaResaTab_table tbody").find("td:nth-child(3)");
							$tds.unbind("click", tableRowClick);
							$tds.css("cursor","pointer").click(tableRowClick);
						}
					}
					selectRowByBarcode(copertina.pk, "", $("#autoincrement").is(":checked"));
					return false;
				} else {
					setTimeout(function() {$.alerts.dialogClass = "style_1"; jAlert(nessunaPubblicazione, attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null;});}, 100);
					$('#barcode').val('');
					return false;
				}
			}		
		});
		return false;
	}
	
	function getRowHtmlResa(copertina, value) {
		var sottotitolo = !isNaN(copertina.sottotitolo) && copertina.sottotitolo.length > 0 ? "<br/><span class='sottotitoloBolla'>" + copertina.sottotitolo + "</span>" : "";
		var prezzoLordo = isNaN(parseLocalNum(copertina.prezzoLordo)) ? '' : Number(parseLocalNum(copertina.prezzoLordo)).toFixed(2);
		var prezzoNetto = isNaN(parseLocalNum(copertina.prezzoNetto)) ? '' : Number(parseLocalNum(copertina.prezzoNetto)).toFixed(4);
		var importoLordo = Number(prezzoLordo).toFixed(2);
		var importoNetto = Number(prezzoNetto).toFixed(4);
		var pkVal = copertina.pk.replace('resoFuoriVoce','');
		var trTag = "<tr class='odd' style='height: 30px;' onmouseover='this.className=\"highlight\"' onmouseout='this.className=\"odd\"' divparam='#?cpu=" + copertina.cpu + "' iv='' pv=''>";									
		trTag += "<td width='2%'>&nbsp;</td>";
		trTag += "<td style='text-align: center;' width='4%'>" + copertina.cpu + "</td>";
		trTag += "<td style='cursor: pointer;' width='23%'>" + copertina.titolo + sottotitolo + "</td>";
		trTag += "<td style='text-align: center;' width='4%'>" + copertina.numeroCopertina + "</td>";
		trTag += "<td style='text-align: center;' width='5%'>" + copertina.distribuito + "</td>";
		trTag += "<td style='font-size: 120%; font-weight: bold; text-align: right; border-width: 1px medium; border-style: solid none; border-color: red -moz-use-text-color;' width='5%'><input name='resoFuoriVoce[&#39;" + pkVal + "&#39;]' id='" + copertina.pk + "' value='" + (typeof(value) === 'undefined' ? 1 : value) + "' style='font-size:120%; font-weight:bold; text-align: right; border-color: rgb(153, 153, 153);' size='2' maxlength='3' validateisnumeric='true' isnewfield='true' type='text'><input type='hidden' name='modificatoFV[&#39;" + pkVal + "&#39;]' id='modificatoFV" + pkVal + "' value='false'/></td>";
		trTag += "<td style='text-align: center;' width='5%'>" + copertina.giacenza + "</td>";				
		trTag += "<td style='text-align: center;' style='text-align: right;' width='8%'>" + copertina.dataUscita + "</td>";
		trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoLordo) + "</td>";
		trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoNetto) + "</td>";	
		trTag += "<td style='text-align: right;' width='7%'>" + displayNum(importoLordo) + "</td>";
		trTag += "<td style='text-align: right;' width='7%'>" + displayNum(importoNetto) + "</td>";					
		trTag += "<td style='text-align: center;' width='12%'></td>"; 
		trTag += "<td style='text-align: center;' width='4%'><a href=\"/immagini/" + copertina.img + "\" rel=\"thumbnail\"><img src=\"/app_img/camera-active.png\" width=\"15px\" height=\"15px\" border=\"0\" style=\"border-style:none\"/></a></td>";
		trTag += "</tr>";	
		return trTag;
	}
	
	// 15/12/2016 CDL - Gestione Ceste  	 
	function getRowHtmlResaCDL(copertina, value) {
		var sottotitolo = !isNaN(copertina.sottotitolo) && copertina.sottotitolo.length > 0 ? "<br/><span class='sottotitoloBolla'>" + copertina.sottotitolo + "</span>" : "";
		var prezzoLordo = isNaN(parseLocalNum(copertina.prezzoLordo)) ? '' : Number(parseLocalNum(copertina.prezzoLordo)).toFixed(2);
		var prezzoNetto = isNaN(parseLocalNum(copertina.prezzoNetto)) ? '' : Number(parseLocalNum(copertina.prezzoNetto)).toFixed(4);
		var importoLordo = Number(prezzoLordo).toFixed(2);
		var importoNetto = Number(prezzoNetto).toFixed(4);
		
		var copertina_img = (typeof copertina.img === 'undefined' || copertina.img === null || copertina.img == '')? "" : "<a href=\"/immagini/" + copertina.img + "\" rel=\"thumbnail\"><img src=\"/app_img/camera-active.png\" width=\"15px\" height=\"15px\" border=\"0\" style=\"border-style:none\"/></a>";
		
		var pkVal = copertina.pk.replace('resoFuoriVoce','');
		var trTag = "<tr class='odd' style='height: 30px;' onmouseover='this.className=\"highlight\"' onmouseout='this.className=\"odd\"' divparam='#?cpu=" + copertina.cpu + "' iv='' pv=''>";									
		trTag += "<td width='2%'>&nbsp;</td>";
		trTag += "<td style='text-align: center;' width='4%'>" + copertina.cpu + "</td>";
		trTag += "<td style='cursor: pointer;' width='23%'>" + copertina.titolo + sottotitolo + "</td>";
		trTag += "<td style='text-align: center;' width='4%'>" + copertina.numeroCopertina + "</td>";
		trTag += "<td style='text-align: center;' width='5%'>" + copertina.distribuito + "</td>";
		trTag += "<td style='font-size: 120%; font-weight: bold; text-align: right; border-width: 1px medium; border-style: solid none; border-color: red -moz-use-text-color;' width='5%'><input name='resoFuoriVoce[&#39;" + pkVal + "&#39;]' id='" + copertina.pk + "' value='" + (typeof(value) === 'undefined' ? 1 : value) + "' style='font-size:120%; font-weight:bold; text-align: right; border-color: rgb(153, 153, 153);' size='2' maxlength='3' validateisnumeric='true' isnewfield='true' type='text'><input type='hidden' name='modificatoFV[&#39;" + pkVal + "&#39;]' id='modificatoFV" + pkVal + "' value='false'/></td>";
		trTag += "<td style='text-align: center;' width='5%'>" + copertina.giacenza + "</td>";				
		trTag += "<td style='text-align: center;' style='text-align: right;' width='8%'>" + copertina.dataUscita + "</td>";
		trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoLordo) + "</td>";
		trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoNetto) + "</td>";	
		trTag += "<td style='text-align: right;' width='7%'>" + displayNum(importoLordo) + "</td>";
		trTag += "<td style='text-align: right;' width='7%'>" + displayNum(importoNetto) + "</td>";					
		trTag += "<td style='text-align: center;' width='12%'></td>"; 
		trTag += "<td style='text-align: center;' width='5%'>" + copertina.cesta + "</td>"; 
		trTag += "<td style='text-align: center;' width='4%'>"+copertina_img+"</td>";
		trTag += "</tr>";	
		return trTag;
	}
	
	function onCloseLayer() {
		//$('#barcode').focus();	
	}
	
	$("input:button[name='plg.inserisci.nuovo.fuori.voce']").click(function() {
			var popID = 'popup_name';   	     		    	  
		    var popWidth = 900;
		    var popHeight = 500;
		 	var url = appContext + "/pubblicazioniResa_showFilter.action?dataTipoBolla=" + escape($('#dataTipoBolla').val()) + "&hasBolleQuotidianiPeriodiciDivise=" + $('#hasBolleQuotidianiPeriodiciDivise').val();
			openDiv(popID, popWidth, popHeight, url);
		}
	);
	
	function setTotalValues(obj) {
		try {
			var $obj = $(obj);
			var id = $obj.attr("id").replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','').replace(/([|@])/g,'\\\$1');
			var ov = parseLocalNum($('#reso_old_' + id).val());
			var numTd = $obj.parent().prev().prev();
			var titolo = numTd.prev().prev().text();
			var num = numTd.text();
			var href = numTd.find("a:first").attr("href");
			var query= href.split('?');
		    var dim= query[1].split('&');	   	   	    	  		  
		    var idtn = dim[0].split('=')[1];
			if ($("#contoDeposito_" + idtn).length > 0 && (ov == '' || ov == 0) && $obj.val().trim().length > 0) {
				var $f = obj;
				jConfirm(confirmResaContoDeposito1.replace('{0}',titolo).replace('{1}',num), attenzioneMsg.toUpperCase(), function(r) {
				    if (r) { 
				    	setTotalValues1(obj);
				    } else {
				    	setTimeout(function () { $f.focus(); }, 50);	
						fieldIsValidToIncrement = false;
						var oldValue = $f.data('oldVal');
						$f.val(oldValue);
						return false;
				    }
				}, true, true);
			} else {
				setTotalValues1(obj);
			}
		} catch (e) {
			setTotalValues1(obj);
		}
	}
	
	function setTotalValues1(obj) {
		var delta = getDelta(obj);
		if (delta < 0) {
			var $f = obj;
			jConfirm(msgResoSuperioreGiacenza, attenzioneMsg.toUpperCase(), function(r) {
			    if (r) { 
			    	setTotals($f);
			    } else {
			    	setTimeout(function () { $f.focus(); }, 50);	
					fieldIsValidToIncrement = false;
					var oldValue = $f.data('oldVal');
					$f.val(oldValue);
					return false;
			    }
			});
		} else {
			setTotals(obj);
		}
	}
	
	function setOnChangeEventOnTextFields() {
		var $inputs = $("#BollaResaForm input:text[name^='reso']");
		$inputs.numeric({negative : false, decimal : false}, null, function() {
			setTotalValues($(this));
		    return true;
		});
		$inputs.bind('keydown', function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);
			if (keycode == 13) { // enter
				event.preventDefault();
				setTotalValues($(this));
				if (invioPortaFocusBarcode) {
					$("#barcode").focus();
				}
			}
		});
		$inputs.each(function() {	
			var $field = $(this);
			var val = $field.val().trim();			
			$field.data('oldVal', val);
		});
		return false;
	}
	
	function setBorderToFuoriVoce() {
		$("#BollaResaForm input:text[name^='resoFuoriVoce']").closest("tr").each(function() {
			var $tr = $(this);
			$tr.contents('td').css({'border-top': '1px solid red','border-bottom': '1px solid red','border-left': 'none', 'border-right': 'none'});
			$tr.contents('td:first').css('border-left', '1px solid red');
			$tr.contents('td:last').css('border-right', '1px solid red');
		});
		return false;
	}
	
	function setTooltipOnRichiamoResa() {
		$("#BollaResaTab_table tbody tr:not(:last) td:nth-child(13)").each(function() {
			var $td = $(this);
			if ($td.text().length > 0) {
				var $tr = $td.parent("tr");
				var pk = $tr.attr("pk").replace(/([|@])/g,'\\\$1');
				var tipoRichiamo = $("#tipoRichiamoExt_" + pk).val();
				$td.find(".richiamoResaCls").attr("title", tipoRichiamo).tooltip({
					delay: 0,  
				    showURL: false
				});
				$td.find(".ordiniCls").tooltip({
					delay: 0,  
				    showURL: false
				});
				var $noBarcode = $td.find(".noBarcode");
				if ($noBarcode.length > 0) {
					$noBarcode.tooltip({
						delay: 0,  
					    showURL: false
					});
					function pulsateImgs() {
						$noBarcode.animate({opacity: 0.2}, 500, 'linear').animate({opacity: 1}, 500, 'linear', pulsateImgs); 
					} 
					pulsateImgs();
				}
			}
		});
		return false;
	}
	
	function setStatisticheOnNumero() {
		$("#BollaResaTab_table tbody tr td:nth-child(4)").each(function() {
			var $td = $(this); 
			$td.click(function() {
	            var link = $td.find("a");
	            if (link.length > 0 && link.attr("href").length > 0) {
	            	var cpu = $td.closest("tr").find("td:nth-child(2)").first().text();
	            	if(!viewImageByProfile_isDisabledPopupRifornimenti) {
		            	openStatistica(link.attr("href"), cpu);
	            	}
	            }
	        });
			$td.tooltip({
	            delay: 0,
	            showURL: false,
	            bodyHandler: function() {
			    	return "<b>" + msgVisualizzaStatistiche + "</b>";
			    }
	        });
		});
		return false;
	}
	
	function openStatistica(href, cpu) {
		var popID = 'popup_name'; 
	    var popURL = href;
	 	var popWidth = 900;
	 	var popHeight = 380;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var idtn = dim[0].split('=')[1];
	    var periodicita = dim[1].split('=')[1];
	    var url = appContext + "/statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action?codPubblicazione=" + cpu + "&periodicita=" + periodicita;
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	function setVariazioniOnDistribuito() {
		$("#BollaResaTab_table tbody tr td:nth-child(5)").each(function() {
			var $td = $(this); 
			$td.click(function() {
	            var link = $td.find("a");
	            if (link.length > 0 && link.attr("href").length > 0) {
	            	var titolo = escape($td.closest("tr").find("td:nth-child(3)").first().text());
	            	if(!viewImageByProfile_isDisabledPopupRifornimenti) {
		            	openVariazioni(link.attr("href"), titolo);
	            	}
	            }
	        });
			$td.tooltip({
	            delay: 0,
	            showURL: false,
	            bodyHandler: function() {
			    	return "<b>" + msgVariazioneServizio + "</b>";
			    }
	        });
		});
		return false;
	}
	
	function openVariazioni(href, titolo) {
		var popID = 'popup_name'; 
	    var popURL = href;
	 	var popWidth = 900;
	 	var popHeight = 380;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var idtn = dim[0].split('=')[1];
	    var coddl = dim[1].split('=')[1];
	    var periodicita = dim[2].split('=')[1];
	    var url = appContext + "/sonoInoltreUscite_showVariazioni.action?idtn=" + idtn + "&coddl=" + coddl + "&periodicita=" + periodicita + "&tableTitle=" + titolo;
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	function setNoteOnTitle() {
		$("#BollaResaTab_table tbody tr:not(:last) td:nth-child(1)").each(function() {
			var $tr = $(this).parent("tr");
			var pk = $tr.attr("pk").replace(/([|@])/g,'\\\$1');
			if (pk && pk.length > 0) {
				var noteByIdtn = $("#noteRivendita" + pk).val();
				var noteByCpu = $("#noteRivenditaCpu" + pk).val();
				var note = noteByIdtn.length > 0 ? noteByIdtn : noteByCpu;
				if (note.length > 0) {
					var titolo = $tr.find("td:nth-child(3)").text().trim();
					var numero = $tr.find("td:nth-child(4)").text().trim();
					$(this).css({"cursor":"pointer","background-image":"url('/app_img/corner.gif')","border":"0px","border-style":"none","background-repeat":"no-repeat"});
					$(this).click(function() {
						jAlert(note, msgNoteRivenditaPerPubblicazioneNumero.replace("{0}", titolo).replace("{1}", numero), function() {
							lastFocusedFieldId = null;
							selectRowByBarcode(pk, "reso");
							return false;
						});
					});
					var titleMsg = getNoteRivendita(noteByIdtn, noteByCpu);
					$(this).tooltip({
						delay: 0,  
					    showURL: false,
					    bodyHandler: function() { 
					    	return '<b>' + titleMsg + '</b>';
					    } 
					}); 
				}
			}
		});
		return false;
	}
	
	function verificaMentaSeQuotidianiInBollaResa(){
		
		var dataBolla = $("#dataTipoBolla").val().split("|")[0];
		var tipoBolla = $("#dataTipoBolla").val().split("|")[1].replace("Tipo","").trim();
		
		dojo.xhrGet({
			url: appContext + namespace + '/pubblicazioniRpc_verificaMentaSeQuotidianiInBollaResa.action?dataBollaResaMenta=' + dataBolla + '&tipoBollaResaMenta=' + tipoBolla ,	
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {
				
				unBlockUI();
				if (args.xhr.status == 200) {
					if(data.length>0){
						for (i = 0; i < data.length; i++) {
							  var quotidianiPresentiInBolla = data[i].quotidianiPresentiInBolla;
							  return quotidianiPresentiInBolla.toString();
						}
					}
				}else{
					return 'false';
				}

			}
	    });
	
	}
	
	
	
	
	function reloadDataTipoBolleResa() {
		dojo.xhrGet({
			url: appContext + '/pubblicazioniRpc_getDateTipoBollaResa.action',			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {
				data = JSON.parse(data);
				var list = '';		
	            for (var i = 0; i < data.length; i++) {
	            	var strChecked = '';
            		var dataTipoBollaStatus = data[i].dataBollaFormat + '|' + data[i].tipoBolla + '|' + data[i].readonly + '|' + data[i].gruppoSconto;
            		if (dataTipoBolla == dataTipoBollaStatus) {
            			strChecked = "selected";
            			if (data[i].readonly == true) {
            				disableBollaResa();
            			}
            		}
					if (Number(data[i].bollaTrasmessaDl) >= 2) {
						list += '<option ' + strChecked + ' style="color:red" value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaTrasmessa + ')</option>';
					} else if (Number(data[i].bollaTrasmessaDl) == 1) {
						list += '<option ' + strChecked + ' style="color:blue" value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaInTrasmissione + ')</option>';
					} else if (Number(data[i].bollaTrasmessaDl) == 0) {
						list += '<option ' + strChecked + ' value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaNonTrasmessa + ')</option>';
            		} else {
            			list += '<option ' + strChecked + ' value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '</option>';
            		}
	            }
	            $("#dataTipoBolla").empty();
	            $("#dataTipoBolla").html(list);
			}
	    });
	}
	
	function showConfirmDialogs() {
				
		if ((showAlertNonPermettiResaInContoDeposito.toString() == 'true' 
			 || (typeof showDialogPubblicazionePresenteNelleSuccessiveBolleResa !== 'undefined' && showDialogPubblicazionePresenteNelleSuccessiveBolleResa.toString() == 'true')
				|| showDialogResoSuperioreGiacenza.toString() == 'true' || showDialogConfirmNumeroResaRespinto.toString() == 'true' 
					|| showDialogConfirmResaInContoDeposito.toString() == 'true') 
				&& (objToFocus != null)) {
				
				var msg = '';
				// SOLO PER MENTA
				// 11/11/2016 VIENE CONTROLLATA LA PRESENZA ALL'INTERNO DELLE BOLLE 
				// SUCCESSIVE, DELLA PUBBLICAZIONE INSERITA IN RESA COME FUORI BOLLA.
				// TBL_9612
				if (showAlertNonPermettiResaInContoDeposito.toString() == 'true' 
						|| (typeof showDialogPubblicazionePresenteNelleSuccessiveBolleResa !== 'undefined' && showDialogPubblicazionePresenteNelleSuccessiveBolleResa.toString() == 'true') ) {
					jAlert(msgResaContoDepositoNonPermessa.replace('{0}', $tit).replace('{1}', $num).replace('{2}', $ncd.val()), attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						unBlockUI();
						showAlertNonPermettiResaInContoDeposito = false;
						showDialogResoSuperioreGiacenza = false;
						showDialogConfirmNumeroResaRespinto = false;
						showDialogConfirmResaInContoDeposito = false;
						showDialogPubblicazionePresenteNelleSuccessiveBolleResa = false;
						objToFocus = null;
				    	setTimeout(function() {
				    		$("#titolo").focus();
				    	}, 100);
					});
			} else if (showDialogResoSuperioreGiacenza.toString() == 'true') {
				PlaySound('beep3');
				jConfirm(msgResoSuperioreGiacenza, attenzioneMsg.toUpperCase(), function(r) {
				    if (r) { 
						setChangedFieldAttribute(objToFocus);
						objToFocus.data('oldVal', objToFocus.val());
						bypassValidateGiacenza = true;
						setTimeout(function() {return (setFormAction('BollaResaFuoriVoceForm','bollaResa_saveBollaResaFuoriVoce.action', '', 'messageDivFuoriVoce', false, '', function() {afterSaveResaFuoriVoce();}, function() { return validateFields('BollaResaFuoriVoceForm') && customValidateFields() && validatePubblicazioneRespinta() && validatePubblicazioneContoDeposito() && showConfirmDialogs();}, false));},100);
				    } else {
				    	var oldValue = objToFocus.data('oldVal');
				    	objToFocus.val(oldValue);
				    	unBlockUI();
				    }
				    showDialogResoSuperioreGiacenza = false;
					showDialogConfirmNumeroResaRespinto = false;
					objToFocus = null;
				});
			} else if (showDialogConfirmNumeroResaRespinto.toString() == 'true') {
				PlaySound('beep3');
				jConfirm(msgConfirmNumeroResaRespinto.replace('{0}', $tit).replace('{1}', $num).replace('{2}', $ncd.val()), attenzioneMsg.toUpperCase(), function(r) {
				    if (r) { 
						unBlockUI();
						setTimeout(function () { objToFocus.focus(); }, 200);	
						bypassValidatePubblicazioneRespinta = true;
						setTimeout(function() {return (setFormAction('BollaResaFuoriVoceForm','bollaResa_saveBollaResaFuoriVoce.action', '', 'messageDivFuoriVoce', false, '', function() {afterSaveResaFuoriVoce();}, function() { return validateFields('BollaResaFuoriVoceForm') && customValidateFields() && validatePubblicazioneRespinta() && validatePubblicazioneContoDeposito() && showConfirmDialogs();}, false));},100);
				    } else {
				    	$("#forzaNonRespingere").val(1);
				    }
				    showDialogResoSuperioreGiacenza = false;
					showDialogConfirmNumeroResaRespinto = false;
					objToFocus = null;
				});
			} else if (showDialogConfirmResaInContoDeposito.toString() == 'true') {
				PlaySound('beep3');
				jConfirm(confirmResaContoDeposito1.replace('{0}', $tit).replace('{1}', $num), attenzioneMsg.toUpperCase(), function(r) {
				    if (r) { 
						unBlockUI();
						setTimeout(function () { objToFocus.focus(); }, 200);	
						bypassValidatePubblicazioneContoDeposito = true;
						setTimeout(function() {return (setFormAction('BollaResaFuoriVoceForm','bollaResa_saveBollaResaFuoriVoce.action', '', 'messageDivFuoriVoce', false, '', function() {afterSaveResaFuoriVoce();}, function() { return validateFields('BollaResaFuoriVoceForm') && customValidateFields() && validatePubblicazioneRespinta() && validatePubblicazioneContoDeposito() && showConfirmDialogs();}, false));},100);
				    } else {
				    	unBlockUI();
				    	setTimeout(function() {
				    		$("#titolo").focus();
				    	}, 100);
				    }
				    showDialogResoSuperioreGiacenza = false;
					showDialogConfirmNumeroResaRespinto = false;
					objToFocus = null;
				}, true, true);
			}
			
			return false;
			
		}
		return true;
	}
	
	function customValidateFields() {
		if (!bypassValidateGiacenza) {
			$("#BollaResaFuoriVoceForm input:text[name^='resoFuoriVoce']").each(function() {
				if ($(this).val().trim() != '') {
					validateGiacenza($(this));
				}
			});
		} else {
			bypassValidateGiacenza = false;
		}
		return true;
	}
	
	function validatePubblicazioneRespinta() {
		if (bypassValidatePubblicazioneRespinta) {
			bypassValidatePubblicazioneRespinta = false;
			return true;
		}
		if ($("#tipoControlloPubblicazioniRespinte").val() == 2) {
			$("#BollaResaFuoriVoceForm input:hidden[id^='motivoResaRespinta_']").each(function() {
				$ncd = $(this);
				var $pk = $ncd.attr("id").replace('motivoResaRespinta_','').trim();
				var $txtField = $("#resoFuoriVoce" + $pk.replace(/([|@])/g,'\\\$1'));
				var qta = $txtField.val();
				var val = $ncd.val().trim();
				if (!val.startsWith('2|') && qta != '' && qta > 0) {
					$tit = $("#titolo_" + $pk.replace(/([|@])/g,'\\\$1')).val();
					$num = $("#numero_" + $pk.replace(/([|@])/g,'\\\$1')).val();
					showDialogConfirmNumeroResaRespinto = true;
					objToFocus = $txtField;
					return true;
				}
			});
		}
		return true;
	}
	
	function validatePubblicazioneContoDeposito() {
		if (bypassValidatePubblicazioneContoDeposito) {
			bypassValidatePubblicazioneContoDeposito = false;
			return true;
		}
		$("#BollaResaFuoriVoceForm input:hidden[id^='numeroInContoDeposito_']").each(function() {
			$ncd = $(this);
			var $pk = $ncd.attr("id").replace('numeroInContoDeposito_','').trim();
			var $pkReplace  = $pk.replace(/([|@])/g,'\\\$1');
			var $txtField = $("#resoFuoriVoce" + $pkReplace).length > 0 ? $("#resoFuoriVoce" + $pkReplace) : $("#reso" + $pkReplace);
			var qta = $txtField.val();		
			var val = $ncd.val().trim().split("|");
			var isNumeroInCD = val[0];
			var permetteCD = val[1];
			// SOLO PER MENTA
			// 11/11/2016 VIENE CONTROLLATA LA PRESENZA ALL'INTERNO DELLE BOLLE SUCCESSIVE, DELLA PUBBLICAZIONE INSERITA IN RESA COME FUORI BOLLA (TBL_9612)
			var pubSuccBolleResa = val[2];
				
			if (!isNaN(qta) && Number(qta) > 0) {
				if (isNumeroInCD.toString() == 'true' && permetteCD.toString() != 'false') {
					$tit = $("#titolo_" + $pk.replace(/([|@])/g,'\\\$1')).val();
					$num = $("#numero_" + $pk.replace(/([|@])/g,'\\\$1')).val();
					showDialogConfirmResaInContoDeposito = true;
					showDialogPubblicazionePresenteNelleSuccessiveBolleResa = false;
					objToFocus = $txtField;
					return true;
				} else if (isNumeroInCD.toString() == 'true' && permetteCD.toString() == 'false') {
					showDialogConfirmResaInContoDeposito = false;
					showDialogPubblicazionePresenteNelleSuccessiveBolleResa = false;
					showAlertNonPermettiResaInContoDeposito = true;
					objToFocus = $txtField;
					return true;
				}else if(pubSuccBolleResa.toString() == 'true' && permetteCD.toString() == 'false'){
					showDialogConfirmResaInContoDeposito = false;
					showAlertNonPermettiResaInContoDeposito = false;
					showDialogPubblicazionePresenteNelleSuccessiveBolleResa = true;
					return true;
				} 
			}
		});
		return true;
	}
	
	
	
	function deleteEmptyRowsFuoriVoce() {
		$("#BollaResaForm input:text[name^='resoFuoriVoce']").each(function() {
			if ($(this).val().trim() == '' || $(this).val().trim() == '0') {
				$(this).closest("tr").remove();
			}
		});
		return false;
	}
	
	function getDelta(objField) {
		var $obj = $(objField);
		var resoVal = parseLocalNum($obj.val());
		var id = $obj.attr("id").replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','').replace(/([|@])/g,'\\\$1');
		var ov = parseLocalNum($('#reso_old_' + id).val());
		var oldValue = isNaN(ov) ? 0 : ov;
		var ovg = parseLocalNum($('#giacenza_old_' + id).val());
		var oldValueGiacenza = isNaN(ovg) ? 0 : ovg;
		var delta = Number(oldValueGiacenza) + (oldValue - Number(resoVal));
		return delta;
	}
	
	function setTotals(objField, skipChange) {	
		var $obj = $(objField);
		var resoVal = parseLocalNum($obj.val());
		var textFieldName = $obj.attr("name");			
		var id = $obj.attr("id").replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','').replace(/([|@])/g,'\\\$1');
		var ov = parseLocalNum($('#reso_old_' + id).val());
		var oldValue = isNaN(ov) ? 0 : ov;
		var ovg = parseLocalNum($('#giacenza_old_' + id).val());
		var oldValueGiacenza = isNaN(ovg) ? 0 : ovg;
		var $td = $obj.parent().next();
		var $td1 = $td.next().next();
		var $td2 = $td1.next();
		var $il = $td2.next();
		var $in = $il.next();
		var oldImportoLordo = parseLocalNum($il.text());
		var oldImportoNetto = parseLocalNum($in.text());								
		
		if (isNaN(resoVal)) {
			return false;
		}
		
		if ($obj.attr("isnewfield") && $obj.attr("isnewfield") == "true") {			
			oldValue = 0; 
			oldImportoLordo = 0;
			oldImportoNetto = 0;	
			$obj.attr("isnewfield", "false");		
		}				
		
		var td = $td;						
		var delta = Number(oldValueGiacenza) + (oldValue - Number(resoVal));
		
		if (typeof(skipChange) === 'undefined' || skipChange == false) {
			setChangedFieldAttribute($obj);
		}
		$obj.data('oldVal', resoVal);
		var prezzoLordo = parseLocalNum($td1.text());
		var prezzoNetto = parseLocalNum($td2.text());		
		var importoLordo = (Number(resoVal) * Number(prezzoLordo)).toFixed(2);
		var importoNetto = (Number(resoVal) * Number(prezzoNetto)).toFixed(4);	
		var dd = displayNum(delta.toString());
		$('#giacenza_old_' + id).val(dd);
		$('#reso_old_' + id).val(displayNum(resoVal.toString()));							
		
		$td.text(isNaN(delta) ? '' : dd);				
		$il.text(isNaN(importoLordo) ? '' : displayNum(importoLordo.toString()));
		$in.text(isNaN(importoNetto) ? '' : displayNum(importoNetto.toString()));														
		
		var totalReso = Number(parseLocalNum($('#BollaResaTab_table tr.calcRow td:nth-child(6)').text()));			
		var totalLordo = Number(parseLocalNum($('#BollaResaTab_table tr.calcRow td:nth-child(11)').text()));
		var totalNetto = Number(parseLocalNum($('#BollaResaTab_table tr.calcRow td:nth-child(12)').text()));	
		
		totalReso = ((isNaN(totalReso) || isNaN(oldValue)) ? '' : (Number(totalReso)) + (Number(resoVal - oldValue))).toFixed(0);
		totalLordo = ((isNaN(totalLordo) || isNaN(oldImportoLordo)) ? '' : (Number(totalLordo)) + (Number(importoLordo) - Number(oldImportoLordo))).toFixed(2);
		totalNetto = ((isNaN(totalNetto) || isNaN(oldImportoNetto)) ? '' : (Number(totalNetto)) + (Number(importoNetto) - Number(oldImportoNetto))).toFixed(4);
		
		var totReso = displayNum(totalReso.toString());
		var totLordo = displayNum(totalLordo.toString());
		var totNetto = displayNum(totalNetto.toString());
		$('#BollaResaTab_table tr.calcRow td:nth-child(6)').text(totReso);
		$('#BollaResaTab_table tr.calcRow td:nth-child(11)').text(totLordo);
		$('#BollaResaTab_table tr.calcRow td:nth-child(12)').text(totNetto);				
		
		$('#totalHeader_1').text(totReso);
		$('#totalHeader_2').text(totLordo);
		$('#totalHeader_3').text(totNetto);								
	}
	
	function validateGiacenza(objField) {	
		var resoVal = parseLocalNum($(objField).val());
		var id = $(objField).attr("id").replace('reso','').replace('resoRichiamo','').replace('resoFuoriVoce','').replace(/([|@])/g,'\\\$1');
		if ($('#reso_old_' + id)) {
			var oldValue = isNaN(parseLocalNum($('#reso_old_' + id).val())) ? 0 : (typeof($('#reso_old_' + id).val()) === 'undefined' || $('#reso_old_' + id).val() == '') ? 0 : parseLocalNum($('#reso_old_' + id).val());
			var oldValueGiacenza = isNaN(parseLocalNum($('#giacenza_old_' + id).val())) ? 0 : parseLocalNum($('#giacenza_old_' + id).val());
			if ($(objField).attr("isnewfield") && $(objField).attr("isnewfield") == "true") {			
				oldValue = 0; 
				$(objField).attr("isnewfield", "false");		
			}				
			var delta = Number(oldValueGiacenza) + (Number(oldValue) - (Number(resoVal)));
			//var isBuono = titolo.toUpperCase().indexOf('BUONI') != -1;
			if (delta < 0) {
				showDialogResoSuperioreGiacenza = true;
				objToFocus = $(objField);
				return true;
			}
		}
		return true;
	}
	
	function setFieldsToSave(isSubmit) {
		ray.ajax();
		var $resi = $('#BollaResaForm input:text[name^=reso]');
		window.Remaining = $resi.length;
		$resi.attr("disabled",true);
		$resi.each(function() {
			var $field = $(this);
			if (isSubmit) {
				$field.data('oldValSubmit', $field.val());
			} else {
				var $fModificato = $field.next("input:hidden[name^=modificato]");
				if ($field.data('oldValSubmit') != $field.val()) {
					$fModificato.val("true");
				} else {
					$fModificato.val("false");
				}
			}
			--window.Remaining;
	        if (window.Remaining == 0) {
	        	$resi.attr("disabled",false);
	        }
		});
		unBlockUI();
		return true;
	}		
	
	function nessunaPubblicazioneAction() {
		$("#barcode").val('');
	}
	
	function doAfterAutoIncremetActions(currField) {
		
		var row = currField.parent().parent();
		var titolo = row.find("td:nth-child(3)").text();
		var num = row.find("td:nth-child(4)").text();
		var giac = row.find("td:nth-child(7)").text();
		$("#titoloDiv").text(titolo);
		$("#numeroDiv").text(num);
		$("#copieDiv").text(currField.val());
		$("#giacDiv").text(giac);
		if (!isNaN(giac) && Number(giac) > 0) {
			$("#giacDiv").css({"color":"red"});
		} else {
			$("#giacDiv").css({"color":"black"});
		}
		
		var cesta = row.find("td:nth-child(14)").text();
		$("#cestaDiv").css({"color":"red"});
		$("#cestaDiv").text(cesta);
		
//		if(cesta!='' && cesta == 'B1'){
//				$("#cestaDiv").css({"color":"#32cd32"});// green 32cd32
//				$("#cestaDiv").text(cesta);
//			}else if(cesta!='' && cesta == 'B2'){
//				$("#cestaDiv").css({"color":"#ff00ff"}); // magenta ff00ff
//				$("#cestaDiv").text(cesta);
//			}else{
//				$("#cestaDiv").text(cesta);
//		}

		
	}	
}