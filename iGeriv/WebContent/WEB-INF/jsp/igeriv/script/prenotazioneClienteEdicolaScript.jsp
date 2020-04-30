<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		$('input:text[name=quantitaRifornimento]').first().focus();
	});

	function afterSuccessSave() {						
		$("#close").trigger('click');							
	}	
</script>