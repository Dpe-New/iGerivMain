<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var deleted = false;
	$(document).ready(function() {	
		addFadeLayerEvents();	
	});		
	
	
	function addCart(callback) {
		
		setTimeout(function() {
			setFormAction('popupDettaglioLibroForm','libriScolasticiClienti_addCart.action', '', '', true, '', function() {
				if (typeof(callback) === 'function') {
					callback();
					$('#testoRicerca').val("");
					$('#ricerca').trigger('click');	 
					$('.errorMessage').hide();
				}
			}, 
			function() {
				return validateFieldsClienteEdicola(true);
			}, false, '');
		}, 100);
	}
	
	
	
	function clienteEdicolaSuccessCallback() {
	
		jConfirm("<s:text name='gp.dati.memorizzati'/><br><s:text name='gp.vuoi.inserire.prenotazioni.per.questo.cliente'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					if (cod.length > 0 && cod != '' && nome.length > 0 && nome != '') {
						<s:url action="prenotazioneClienti_showFilter.action" var="urlTag"/>
						window.location = '<s:property value="#urlTag" />?idCliente=' + cod + '&nomeCliente=' + nome + ' ' + cognome;
					}
				} else {
					$("#ricerca").trigger("click");
				}
		}, true, false);
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
	}		

	
	function validateFieldsClienteEdicola(showAlerts) {
		return true;
	}
	
	
	
	
</script>