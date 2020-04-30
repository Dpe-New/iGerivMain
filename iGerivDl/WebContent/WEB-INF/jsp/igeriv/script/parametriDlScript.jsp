<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		$("#esegui").click(function() {
			jConfirm('<s:text name="igeriv.confirm.execute.esportazione.prodotti.vari"/>', attenzioneMsg.toUpperCase(), function(r) {
			    if (r) { 
			    	ray.ajax();
					dojo.xhrPost({
						form: "CommandForm",			
						handleAs: "text",		
						handle: function(data,args) {
							unBlockUI();
							var $doc = $(data);
							var hasError = $("igerivException", $doc).length > 0;
							if (args.xhr.status != 200 || hasError) {
								$.alerts.dialogClass = "style_1";
								jAlert($("igerivException", $doc).text(), attenzioneMsg.toUpperCase(), function() {
									$.alerts.dialogClass = null;
								});
							} else {
								jAlert('<s:text name="igeriv.procedura.eseguito.con.suceesso"/>', avvisoMsg);
							}
						}		
					});  
			    }
			}, true, false);
		});
	});
	
	function doSubmit() {
		jConfirm('<s:text name="igeriv.confirm.save.params.dl"/>', attenzioneMsg.toUpperCase(), function(r) {
		    if (r) { 
		    	setTimeout(function() {
					setFormAction('ParamAgenziaForm', 'params_saveParam.action', '', '', true, '', '', function() {return doValidationParams();}, true, '');
				}, 100);  
		    } else {
		    	unBlockUI();
		    }
		}, true, false);
	}
	
	function doValidationParams() {
		if ($('#paramAgenzia1').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.1" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia1").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia3').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.3" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia3").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia5').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.5" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia5").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia7').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.7" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia7").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia8').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.8" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia8").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia9').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.9" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia9").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia10').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.10" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia10").focus();
			});
			unBlockUI();
			return false; 
		} 
		if ($('#paramAgenzia11').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.11" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia11").focus();
			});
			unBlockUI();
			return false; 
		} 
		
		//LA NUOVOA MODALITA' FTP E' STATA CREATA PER LIGURIAPRESS - NON PRESENTA GESTIONALE
		var nuovaModalitaftp = <s:text name="agenzia.nuovaModalitaftp"/>;
		if(nuovaModalitaftp == 0){
			if ($('#paramAgenzia12').val().trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.12" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#paramAgenzia12").focus();
				});
				unBlockUI();
				return false; 
			} 
			
			if ($('#paramAgenzia13').val().trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.13" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#paramAgenzia13").focus();
				});
				unBlockUI();
				return false; 
			} 
			if ($('#paramAgenzia14').val().trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.14" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#paramAgenzia14").focus();
				});
				unBlockUI();
				return false; 
			} 
			if ($('#paramAgenzia15').val().trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.15" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#paramAgenzia15").focus();
				});
				unBlockUI();
				return false; 
			}						
		}
		if ($('#paramAgenzia16').val().trim() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.params.agenzia.16" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia16").focus();
			});
			unBlockUI();
			return false; 
		} else if ($('#paramAgenzia16').val().trim() != '' && isNaN($('#paramAgenzia16').val().trim())) {
			$.alerts.dialogClass = "style_1";
			jAlert(prezzoInvalido, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#paramAgenzia16").focus();
			});
			unBlockUI();
			return false; 
		}
		return true;
	} 
</script>