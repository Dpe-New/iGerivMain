<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript">
	
	//var deleted = false;
	$(document).ready(function() {	
		
		addFadeLayerEvents();	
		
		$("#chiudiRiepiloOrdine").click(function() {
			var idNumeroOrdine = $("#idNumeroOrdine").val();	
		    	dojo.xhrGet({
		    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_chiudiConfermaOrdineRiepilogo.action?idNumeroOrdine="+ idNumeroOrdine,	
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					handle: function(data,args) {
						unBlockUI();
						if ($('#popup_name').is(':visible')) {
							if (closeLayerConfirm()) {
								$("#close").trigger('click');
							}
						}
						<s:url action="libriScolasticiClienti_showOrdiniFilter.action" var="urlTag"/>
						window.location = '<s:property value="#urlTag" />';
					}
				});
				    
			});
		
		$(document).ready(function() {
			$("#imgClose", window.parent.document).attr("style", "visibility: hidden");	
	    });
		
	});
		
		
		
		
		
		
		
		

	

	
	
	
</script>