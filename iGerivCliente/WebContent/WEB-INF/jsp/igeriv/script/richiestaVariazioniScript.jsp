<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {			
		<s:if test='prenotazioneDisabled != null && prenotazioneDisabled == "true"'>
			disableAllFormFields();		
		</s:if>
		addFadeLayerEvents();
	});
	
	function afterSuccessSave() {	
		$("#close").trigger('click');								
	}
</script>