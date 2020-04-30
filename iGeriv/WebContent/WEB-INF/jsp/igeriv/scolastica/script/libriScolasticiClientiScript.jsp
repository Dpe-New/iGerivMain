<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
// 	var deleted = false;
// 	$(document).ready(function() {	
// 		$("#cliente\\.cellulare").numeric({ negative : false });
		
// 		$.datepicker.setDefaults($.datepicker.regional['it']);
// 		$('#dataSospensioneDa').datepicker();
// 		$('#dataSospensioneA').datepicker();
		
		
// // 		$('#dataSospensioneDa').click(function() { 
// //         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// // 		});	
// // 		$('#dataSospensioneA').click(function() { 
// //         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// // 		});
// 		if ($('#inviaEmail').length > 0) {
// 			$('#inviaEmail').change(function() {
// 				if ($(this).attr("checked") == true) {
// 					$("#clientiTable tr:nth-child(11)").hide();
// 				} else {
// 					$("#clientiTable tr:nth-child(11)").show();
// 				}
// 			});
// 		}
// 		$("#tipoPagamento").change(function() {
// 			if ($(this).val() == 1) {
// 				$("#giorniScadenzaPagamento").val(0);
// 				$("#giorniScadenzaPagamento").attr("disabled", true);
// 			} else {
// 				$("#giorniScadenzaPagamento").attr("disabled", false);
// 			}
// 		});
// 		if ($("#tipoPagamento").val() == 1) {
// 			$("#giorniScadenzaPagamento").val(0);
// 			$("#giorniScadenzaPagamento").attr("disabled", true);
// 		} else {
// 			$("#giorniScadenzaPagamento").attr("disabled", false);
// 		}
// 		var nome = $('#cliente\\.nome').val();
// 		if (nome != '' && $('#inviaEmail').length > 0) {
// 			$('#inviaEmail').attr('checked', false);			
// 		} else if ($('#inviaEmail').length > 0) {
// 			$('#inviaEmail').attr('checked', true);
// 			$("#clientiTable tr:nth-child(11)").hide();
// 		}
// 		addFadeLayerEvents();	
// 		$("#igerivCardImg").tooltip({ 
// 			delay: 0,  
// 		    showURL: false
// 		});
// 		$('#cliente\\.nome').focus();
// 	});		
	
// 	function saveCliente(callback) {
// 		setTimeout(function() {
// 			setFormAction('EditClienteForm','gestioneClienti_saveCliente.action', '', '', true, '', function() {
// 				if (typeof(callback) === 'function') {
// 					callback();
// 				}
// 			}, 
// 			function() {
// 				return validateFieldsClienteEdicola(true);
// 			}, false, '');
// 		}, 100);
// 	}
	
// 	function clienteEdicolaSuccessCallback() {
// 		var cod = $("#codCliente").val();
// 		var nome = $("#cliente\\.nome").val();
// 		var cognome = $("#cliente\\.cognome").val();
		
// 		jConfirm("<s:text name='gp.dati.memorizzati'/><br><s:text name='gp.vuoi.inserire.prenotazioni.per.questo.cliente'/>", attenzioneMsg, 
// 			function(r) {
// 				if (r) {
// 					if (cod.length > 0 && cod != '' && nome.length > 0 && nome != '') {
// 						<s:url action="prenotazioneClienti_showFilter.action" var="urlTag"/>
// 						window.location = '<s:property value="#urlTag" />?idCliente=' + cod + '&nomeCliente=' + nome + ' ' + cognome;
// 					}
// 				} else {
// 					$("#ricerca").trigger("click");
// 				}
// 		}, true, false);
// 		setTimeout(function() {
// 			$("#popup_ok").focus();
// 		}, 100);
// 	}		

// 	function onLoadFunction() {
// 		$("#cliente\\.nome").focus();				
// 	}
	
// 	$("input#autocomplete").autocomplete({	
// 		minLength: 3,			
// 		source: function(request, response) {		
// 			dojo.xhrGet({
// 				url: "${pageContext.request.contextPath}/automcomplete_localita.action",	
// 				preventCache: true,
// 				handleAs: "json",				
// 				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
// 				content: { 
// 			    	term: request.term
// 			    }, 					
// 				handle: function(data,args) {
// 					response($.map(data, function(item) {		                            		                         	
// 		                                return {
// 		                                    id: item.id,	
// 		                                    label: item.label,
// 		                                    value: item.value,
// 		                                    idProvincia: item.idProvincia
// 		                                }
// 		                            }))
// 				}
// 			});
// 		},
// 		select: function (event, ui) {	
// 			setTimeout(function() {
// 				document.getElementById('cliente.provincia').value = ui.item.idProvincia;
// 				document.getElementById('codLocalita').value = ui.item.id;
// 			}, 100);
// 		}
// 	});				
	
// 	$("input#autocomplete").focusout(function() {
// 		alert($("input#autocomplete").value());		 
// 	});
	
// 	function doDelete() {
// 		PlaySound('beep3');
// 		jConfirm("<s:text name='gp.vuoi.cancellare.cliente'/>", attenzioneMsg, 
// 			function(r) {
// 				if (r) {
// 					$("#codCliente").attr("disabled", false);
// 					setFormAction('EditClienteForm','gestioneClienti_deleteCliente.action', '', '', true, '', function() {deleted = true; $("#ricerca").trigger("click");}, '', false, '');
// 				} else {
// 					setTimeout('unBlockUI();', 100);
// 				}
// 			}
// 		);
// 	}
	
	
	$("#butNuovoGestioneClienti").click(function() {
			var popID = 'popup_name';	   	     		    	  
		    var popWidth = 900;
		    var popHeight = 550;
		 	var url = "${pageContext.request.contextPath}/libriScolasticiClienti_showCliente.action";
			openDiv(popID, popWidth, popHeight, url);
		}
	);
	
	
	
	function openDetailAnagrafica(idC){
		openDiv('popup_name', 900, 550, 'libriScolasticiClienti_showCliente.action?idCliente=' + idC, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	
	function openDetailOrdini(idC){
		openDiv('popup_name', 900, 550, 'libriScolasticiClienti_showClienteDettaglioOrdini.action?idCliente=' + idC, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	function viewTracking(numOrdineTxt){
	 	$("#popupDettaglioOrdiniClientiForm").append('<input type="hidden" name="numOrdineTxt" id="numOrdineTxt" value="' + numOrdineTxt + '"/>');
	    $("#popupDettaglioOrdiniClientiForm").attr("action", "libriScolasticiClienti_showFilterTracking.action");
	    $("#popupDettaglioOrdiniClientiForm").submit();
	    return false;
	}
	
</script>