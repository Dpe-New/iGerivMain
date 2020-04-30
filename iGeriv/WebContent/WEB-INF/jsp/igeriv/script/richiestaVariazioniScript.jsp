<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {			
		<s:if test='prenotazioneDisabled != null && prenotazioneDisabled == "true"'>
			disableAllFormFields();		
		</s:if>
		addFadeLayerEvents();
	});
	
	<s:if test="%{isQuotidiano != null && isQuotidiano}">
		$("#settimana").click(function() {
			var popID = 'popup_name_det';  
			var popWidth = 850;
		 	var popHeight = 400;
			var idtn = $("input[name='idtn']").val();
		    var url = "${pageContext.request.contextPath}/sonoInoltreUscite_showVariazioniSettimana.action?idtn=" + idtn;
		    openDiv(popID, popWidth, popHeight, url);
		});
	</s:if>
	
	function afterSuccessSave() {	
		$("#close").trigger('click');								
	}
</script>