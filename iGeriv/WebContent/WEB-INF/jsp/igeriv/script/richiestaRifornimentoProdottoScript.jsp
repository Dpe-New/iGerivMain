<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		setContentDivHeight(30);
		$("#quantitaRichiesta").bind('keydown', handleKeyDown);
		$("#quantitaRichiesta").focus();
	});
	
	function handleKeyDown(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode); 
		if (keycode == 13) {
			event.preventDefault();
			$("#memorizzaProdotto").trigger("click");
		}
	}	
	
	function validateSave() {
		if ($("#quantitaRichiesta").val().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="error.qta.richiesta"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$("#quantitaRichiesta").focus();
			});
			setTimeout(function() {
				unBlockUI();				
			}, 100);
			return false;
		}
		return true;
	}
</script>