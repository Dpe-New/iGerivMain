<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		
		validateMenuProfilo = function()  {
			if ($("#titolo").val().trim().length == 0) {
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace("{0}", '<s:text name="label.print.Table.Title"/>'), attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#titolo").focus();
				});
				return false;
			}
			if ($("#descrizione").val().trim().length == 0) {
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace("{0}", '<s:text name="igeriv.descrizione"/>'), attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#descrizione").focus();
				});
				return false;
			}
			return true;
		}
		
		$('#titolo').focus();
	});		
</script>