<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		addTableLastRow('RichiestaRifornimentoTab_table');
		$('input:text[name=quantitaRifornimentoMap]').first().focus();
	});
	
	function afterSuccessSave() {						
		$("#close").trigger('click');								
	}
</script>