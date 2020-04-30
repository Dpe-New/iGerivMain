<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataCompetenza').datepicker();
		$('#strDataDocumento').datepicker();
		
// 		$('#strDataCompetenza').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#strDataDocumento').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		$("#emettiEC").val($("#tipoProdottiInEstrattoConto").val() == 5 ? '<s:text name="igeriv.emetti.fattura"/>' : '<s:text name="igeriv.emetti.estratto.conto"/>');
		$("#tipoProdottiInEstrattoConto").change(function() {
			if ($(this).val() == 1) {
				$("#tipiEstrattoConto").attr("disabled", false);
			} else {
				$("#tipiEstrattoConto").val("1|2");
				$("#tipiEstrattoConto").attr("disabled", true);
			}
		});
		if ($("#tipoProdottiInEstrattoConto").val() == 1) {
			$("#tipiEstrattoConto").attr("disabled", false);
		} else {
			$("#tipiEstrattoConto").val("1|2");
			$("#tipiEstrattoConto").attr("disabled", true);
		}
		
		changeNumerazioneFatture = function() {
			jConfirm('<s:text name="igeriv.confirm.cambia.numerazione.fatturazione"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function(r) {
				if (r) {
					ray.ajax();
					dojo.xhrGet({
						url: "paramsJ_setLastNumeroFatturazione.action?codParametro=<s:text name='constants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE'/>&valParametro=" + $("#numeroFattura").val(),	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=utf-8"}, 	
						handle: function(data,args) {
							unBlockUI();
							var hasError = (typeof(data.error) !== 'undefined' && data.error.length > 0);
							if (args.xhr.status != 200 || hasError) {
								$.alerts.dialogClass = "style_1";
								var msg = hasError ? data.error : '<s:text name="gp.errore.imprevisto"/>';
								jAlert(msg, attenzioneMsg.toUpperCase(), function() {
									$.alerts.dialogClass = null;
									setLastNumeroFatturazione();
								});
							} else {
								jAlert('<s:text name="gp.dati.memorizzati"/>', informazioneMsg);
							}
						}
					});
				} else {
					setLastNumeroFatturazione();
				}
			}, true, true);
		}
		
		setLastNumeroFatturazione = function() {
			$("#numeroFattura").val('<s:text name="%{#session.mapParamsEdicola.paramEdicola17.paramValue}"/>');
			dojo.xhrGet({
				url: "paramsJ_getLastNumeroFatturazione.action?codParametro=<s:text name='constants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE'/>",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				handle: function(data,args) {
					unBlockUI();
					var hasError = (typeof(data.error) !== 'undefined' && data.error.length > 0);
					if (args.xhr.status != 200 || hasError) {
						$.alerts.dialogClass = "style_1";
						var msg = hasError ? data.error : '<s:text name="gp.errore.imprevisto"/>';
						jAlert(msg, attenzioneMsg.toUpperCase());
					} else {
						$("#numeroFattura").val(data.lastNumeroFatturazione.toString());
					}
				}
			});
		}
		
		$("#numeroFattura").numeric({'decimal' : false, 'negative': false});
		$("#numeroFattura").change(changeNumerazioneFatture);
		
	});
	
	function checkFields() {
		var dataComp = $("#strDataCompetenza").val();
		if (dataComp.trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}','<s:text name="igeriv.data.competenza.estratto.conto"/>'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$("#strDataCompetenza").focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} else if (dataComp.trim() != '' && !checkDate(dataComp)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#strDataCompetenza').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		$("#tipiEstrattoConto").attr("disabled", false);
		return true;
	}
	
	function emettiEstrattoConto() {
		var dataComp = $("#strDataCompetenza").val();
		var dataDoc = $("#strDataDocumento").val();
		var hasClienti = hasClientiChecked();
		if (dataComp.trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}','<s:text name="igeriv.data.competenza.estratto.conto"/>'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$("#strDataCompetenza").focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} else if (dataComp.trim() != '' && !checkDate(dataComp)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#strDataCompetenza').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		if (dataDoc.trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}','<s:text name="igeriv.data.documento"/>'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$("#strDataDocumento").focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} else if (dataDoc.trim() != '' && !checkDate(dataDoc)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#strDataDocumento').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		if (!hasClienti) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.nessun.cliente.selezionato"/>', attenzioneMsg, function() {
				$.alerts.dialogClass = null;
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		}
		jConfirm($("#tipoProdottiInEstrattoConto").val() == 5 ? '<s:text name="igeriv.confirm.emissione.fattura"/>' : '<s:text name="igeriv.confirm.emissione.estratto.conto"/>'.replace('{0}',dataComp).replace('{1}',dataDoc), attenzioneMsg, function(r) {
		    if (r) { 
		    	blockUIForDownload($(this));
		    	var ccliArr = new Array();
	    		$("#EstrattoContoClientiForm input:checkbox[name='spunte']").each(function() {
	    			if ($(this).attr("checked") == true) {   	     	  		  
	    				ccliArr.push($(this).val());
	    			}
	    		});
		    	if ($("#tipoProdottiInEstrattoConto").val() == 1) {
			    	dojo.xhrGet({
			    		url: appContext + '/pubblicazioniRpc_validateEstrattoContoClientiEdicola.action?codClienti=' + ccliArr.toString() + '&dataAStr=' + $("#strDataCompetenza").val(),	
			    		handleAs: "json",				
			    		sync: true,
			    		headers: {"Content-Type": "application/json; charset=utf-8"}, 	
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
									submitForm();
			    				}
							} else {
								jAlert(msgErroreInvioRichiesta);
							}
			    			unBlockUI();
			    		}
			        });
		    	} else {
		    		dojo.xhrGet({
			    		url: appContext + '/pubblicazioniRpc_validateEmissioneFatturaClientiEdicola.action?codClienti=' + ccliArr.toString(),	
			    		handleAs: "json",				
			    		sync: true,
			    		headers: {"Content-Type": "application/json; charset=utf-8"}, 	
			    		preventCache: true,
			    		handle: function(data,args) {	
			    			if (args.xhr.status == 200) {
			    				if (typeof(data.error) !== 'undefined' && data.error != '') {
				    				$.alerts.dialogClass = "style_1";
				    				jAlert(data.error, attenzioneMsg, function() {
				    					$.alerts.dialogClass = null;
				    					return false;
				    				});
			    				} else if (typeof(data.clienteSenzaPivaCF) !== 'undefined' && data.clienteSenzaPivaCF != '') {
				    				jConfirm(data.clienteSenzaPivaCF, attenzioneMsg, function(r) {
				    					if (r) {
				    						openDiv('popup_name', 900, 550, "gestioneClienti_showCliente.action?idCliente=" + data.codcli, '', '', '', function() {clienteEdicolaSuccessCallback = function() {$("#ricerca").trigger("click");}});
				    					}
				    					return false;
				    				});
			    				} else {
									submitForm();
			    				}
							} else {
								jAlert(msgErroreInvioRichiesta);
							}
			    			unBlockUI();
			    		}
			        });
		    	}
		    	return false;
		    }
		    $("#inputText").focus();
		}, true, true);
	}
	
	function submitForm() {
		document.forms.EstrattoContoClientiForm.ec_eti.value='';
		$("#EstrattoContoClientiForm").attr("action", "report_emettiEstrattoContoClientiEdicola.action"); 	
		$("#strDataComp").val($("#strDataCompetenza").val());
		if ($("#tipoProdottiInEstrattoConto").val() == 5) {
			$("#tipoDocumento").val(2);
		} else {
			$("#tipoDocumento").val();
		}
		$("#EstrattoContoClientiForm").submit();
	}
	
	function hasClientiChecked() {
		var hasChecked = false; 
		$("#EstrattoContoClientiForm input[type='checkbox'][name='spunte']").each(function() {
			if ($(this).attr("checked")) {
				hasChecked = true;		
			}
		});
		return hasChecked;
	}
	
	function finishDownloadCallback() {
		$("#ricerca").trigger("click");
	}
</script>
