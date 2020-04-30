<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		
		showSuccessAlert = function() {
			jAlert('<s:text name="igeriv.msg.success.insert.nuova.edicola.infroiv.dl"/>', informazioneMsg.toUpperCase());
		};
		
		validateEdicolaForm = function() {
			if ($("#codDl").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.dl.1" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#codDl').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#codEdicolaDl").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.cod.edicola.dl.1" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#codEdicolaDl').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#ragioneSociale1").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.rag.sociale" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#ragioneSociale1').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#indirizzo").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.indirizzo" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#indirizzo').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#localita").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.localita" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#localita').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#provincia").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.provincia" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#provincia').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			if ($("#email").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.email" />'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#email').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			}
			return true;
		};
		
	});
	
	function doSubmitEdit() {
		setFormAction('EdicolaInforivDlForm','edicoleInforivDl_saveEdicolaInforivDl.action', '', '', false, '', function() { showSuccessAlert(); }, function() { return validateEdicolaForm(); }, false, '', false);
	}
	
</script>