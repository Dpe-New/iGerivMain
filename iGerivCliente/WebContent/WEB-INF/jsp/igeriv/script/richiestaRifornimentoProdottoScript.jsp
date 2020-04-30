<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		if ($("#richiestaRifornimentoProdotto\\.quantitaRichiesta").val() == 0) {
			$("#richiestaRifornimentoProdotto\\.quantitaRichiesta").val('');
		}
		$("#ultimaQuantitaRichiesta").val($("#richiestaRifornimentoProdotto\\.quatitaRichiesta").val());
		if ($("#richiestaRifornimentoProdotto\\.stato").val() != "N") {
			$("#richiestaRifornimentoProdotto\\.quatitaRichiesta").val("");
			$("#codRichiestaRifornimento").val("");
		}
		addFadeLayerEvents();
		setContentDivHeight(30);
		$("#richiestaRifornimentoProdotto\\.quatitaRichiesta").focus();
	});
	
	function afterSuccessSave() {						
		$("#close").trigger('click');								
	}
</script>