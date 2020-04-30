<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var deleted = false;
	$(document).ready(function() {	
		$("#clienteGestioneScolastica\\.cellulare").numeric({ negative : false });
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataSospensioneDa').datepicker();
		$('#dataSospensioneA').datepicker();
		
		
// 		$('#dataSospensioneDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#dataSospensioneA').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
		if ($('#clienteGestioneScolastica_inviaEmail').length > 0) {
			$('#clienteGestioneScolastica_inviaEmail').change(function() {
				if ($(this).attr("checked") == true) {
					$("#clientiTable tr:nth-child(11)").hide();
				} else {
					$("#clientiTable tr:nth-child(11)").show();
				}
			});
		}
		$("#tipoPagamento").change(function() {
			if ($(this).val() == 1) {
				$("#giorniScadenzaPagamento").val(0);
				$("#giorniScadenzaPagamento").attr("disabled", true);
			} else {
				$("#giorniScadenzaPagamento").attr("disabled", false);
			}
		});
		if ($("#tipoPagamento").val() == 1) {
			$("#giorniScadenzaPagamento").val(0);
			$("#giorniScadenzaPagamento").attr("disabled", true);
		} else {
			$("#giorniScadenzaPagamento").attr("disabled", false);
		}
		var nome = $('#clienteGestioneScolastica\\.nome').val();
		if (nome != '' && $('#clienteGestioneScolastica_inviaEmail').length > 0) {
			$('#clienteGestioneScolastica_inviaEmail').attr('checked', false);			
		} else if ($('#clienteGestioneScolastica_inviaEmail').length > 0) {
			$('#clienteGestioneScolastica_inviaEmail').attr('checked', true);
			$("#clientiTable tr:nth-child(11)").hide();
		}
		addFadeLayerEvents();	
		$("#igerivCardImg").tooltip({ 
			delay: 0,  
		    showURL: false
		});
		$('#clienteGestioneScolastica\\.nome').focus();
	});		
	
	function saveGestioneCliente(callback) {
		setTimeout(function() {
			setFormAction('EditLibriScolasticiClienteForm','libriScolasticiClienti_saveClienteScolastica.action', '', '', true, '', function() {
				if (typeof(callback) === 'function') {
					callback();
				}
			}, 
			function() {
				return validateFieldsGestioneClienteEdicola(true);
			}, false, '');
		}, 100);
	}
	
	function gestioneClienteEdicolaSuccessCallback() {
		var cod = $("#codCliente").val();
		var nome = $("#clienteGestioneScolastica\\.nome").val();
		var cognome = $("#clienteGestioneScolastica\\.cognome").val();
		
		$("#ricerca").trigger("click");
		
// 		jConfirm("<s:text name='gp.dati.memorizzati'/><br><s:text name='gp.vuoi.inserire.prenotazioni.per.questo.cliente'/>", attenzioneMsg, 
// 			function(r) {
// 				if (r) {
// 					if (cod.length > 0 && cod != '' && nome.length > 0 && nome != '') {
// 						<s:url action="libriScolasticiClienti_showFilter.action" var="urlTag"/>
// 						window.location = '<s:property value="#urlTag" />?idCliente=' + cod + '&nomeCliente=' + nome + ' ' + cognome;
// 					}
// 				} else {
// 					$("#ricerca").trigger("click");
// 				}
// 		}, true, false);
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
	}		

	function onLoadFunction() {
		$("#clienteGestioneScolastica\\.nome").focus();				
	}
	
	$("input#autocomplete").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}/automcomplete_localita.action",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {		                            		                         	
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.value,
		                                    idProvincia: item.idProvincia
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {	
			setTimeout(function() {
				document.getElementById('clienteGestioneScolastica.provincia').value = ui.item.idProvincia;
				document.getElementById('codLocalita').value = ui.item.id;
			}, 100);
		}
	});				
	
	$("input#autocomplete").focusout(function() {
		alert($("input#autocomplete").value());		 
	});
	
	function doGestioneClienteDelete() {
		PlaySound('beep3');
		jConfirm("<s:text name='gp.vuoi.cancellare.cliente'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					$("#codCliente").attr("disabled", false);
					setFormAction('EditLibriScolasticiClienteForm','libriScolasticiClienti_deleteClienteScolastica.action', '', '', true, '', function() {deleted = true; $("#ricerca").trigger("click");}, '', false, '');
				} else {
					setTimeout('unBlockUI();', 100);
				}
			}
		);
	}
	
	function validateFieldsGestioneClienteEdicola(showAlerts) {
		var dtDa = $("#dataSospensioneDa").val();
		var dtA = $("#dataSospensioneA").val();
		if (dtDa == '' && dtA != '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert("<s:text name='error.date.sospensione.da.prenotazione'/>", attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					//$('#dataSospensioneDa').focus();
				});
			}
			unBlockUI();
			return false;
		}
		if (dtA == '' && dtDa != '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert("<s:text name='error.date.sospensione.a.prenotazione'/>", attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					//$('#dataSospensioneA').focus();
				});
			}
			unBlockUI();
			return false;
		}
		if (dtDa != '' && !checkDate(dtDa)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataSospensioneDa').focus();
			});
			unBlockUI();
			return false; 
		} 
		if (dtA != '' && !checkDate(dtA)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataSospensioneA').focus();
			});
			unBlockUI();
			return false; 
		} 
		
		return validateFieldsClienteBase(showAlerts);
	}
	
// 	function doAssociaIGerivCard() {
// 		if (validateFieldsClienteBase(true)) {
// 			var email = $("#clienteGestioneScolastica\\.email").val().trim();
// 			var cell = $("#clienteGestioneScolastica\\.cellulare").val().trim();
// 			if (email == '' && cell == '') {
// 				$.alerts.dialogClass = "style_1";
// 				jAlert('<s:text name="msg.error.email.o.cellluare"/>', attenzioneMsg.toUpperCase(), function() {
// 					$.alerts.dialogClass = null;
// 					return false;
// 				});
// 				return false;
// 			} else if (email != '' && !checkEmail(email)) {
// 				$.alerts.dialogClass = "style_1";
// 				jAlert(msgInvalidEmail, attenzioneMsg.toUpperCase());
// 				$("#email").focus();
// 				return false;
// 			} else if (cell != '' && !cell.match(/^((\+|00)?39)?3\d{2}\d{6,7}$/)) {
// 				$.alerts.dialogClass = "style_1";
// 				jAlert('<s:text name="dpe.validation.msg.invalid.cellulare"/>', attenzioneMsg.toUpperCase());
// 				$("#email").focus();
// 				return false;
// 			}
// 			if ($("#idCliente").val() == '') {
// 				saveGestioneCliente(function(){$("#idCliente").val($("#codCliente").val()); doAssociaIGerivCard();});
// 				return false;
// 			}
// 			jConfirm('<s:text name="msg.confirm.associare.card.igeriv.cliente"/>'.replace('{0}', $("#clienteGestioneScolastica\\.nome").val()), attenzioneMsg, function(r) {
// 			    if (r) { 
// 			    	setTimeout(function() {
// 						jPrompt(msgIGerivCard, null, msgInsertBarcodeIGerivCard, function(res) {
// 							if (res != null && res.length > 0) {
// 								associateIGerivCard(res, false);
// 							}
// 						});
// 			    	}, 100);
// 			    }
// 			});
// 		}
// 	}
	
// 	function associateIGerivCard(res, byPassClienteCheck) {
// 		var codCliente = $("#codCliente").val();
// 		dojo.xhrGet({
// 			url: "${pageContext.request.contextPath}/pubblicazioniRpc_associaIGerivCard.action?barcode=" + res + "&codCliente=" + codCliente + "&byPassClienteCheck=" + byPassClienteCheck,	
// 			preventCache: true,
// 			handleAs: "json",
// 			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
// 			load: function(data,args) {
// 				var result = data[0].result;
// 				if (typeof(result) != 'undefined' && result != '') {
// 					jAlert(result, informazioneMsg, function() {
// 			    		return false;
// 					});
// 				} else if (typeof(data[0].confirm) != 'undefined' && data[0].confirm != '') {
// 					jConfirm(data[0].confirm, attenzioneMsg, function(r) {
// 					    if (r) { 
// 					    	associateIGerivCard(res, true);
// 					    } else {
// 					    	return false;
// 					    }
// 					});
// 				} else {
// 					$.alerts.dialogClass = "style_1";
// 					jAlert(data[0].error, attenzioneMsg.toUpperCase(), function() {
// 						$.alerts.dialogClass = null;
// 						return false;
// 					});
// 				}
// 			}
// 		});
// 	}
	
	
	
	
</script>