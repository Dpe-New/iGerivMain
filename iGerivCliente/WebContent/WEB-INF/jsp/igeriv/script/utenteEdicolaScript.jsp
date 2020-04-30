<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {																
		addFadeLayerEvents();	
	});		
	
	$('#arrowLeft').click(function() {
		$('#moduliSelected option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduli').append(userClone);
			$(this).remove();
		});			
	});
	
	$('#arrowRight').click(function() {
		$('#moduli option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduliSelected').append(userClone);
			$(this).remove();
		});	
	});
	
	function afterSuccessSave() {						
		$("#ricerca").trigger('click');								
	}		

	function onLoadFunction() {
		document.getElementById('utente.nomeUtente').focus();					
	}
	
	function doDelete() {
		PlaySound('beep3');
		jConfirm("<s:text name='gp.cancellare.dati'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					setFormAction('EditUtenteForm','gestioneUtenti_deleteUtente.action', '', 'messageDiv');
				} else {
					setTimeout('unBlockUI();', 100);
				}
			}
		);
	}
	
	function validateFieldsUtenteEdicola(showAlerts) {
		if ($("#utente\\.nome").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="dpe.contact.form.name"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
		if ($("#utente\\.email").val() != '' && !checkEmail($("#utente\\.email").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="dpe.validation.msg.invalid.email"/>', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
		return true;
	}
</script>