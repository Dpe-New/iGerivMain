<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript">
	
	//var deleted = false;
	$(document).ready(function() {	
		addFadeLayerEvents();	
		
		
		
	});
		
		
	function viewTracking(numOrdineTxt) {

		jConfirm('<s:text name="dpe.ordine.msg.view.tracking.ordine"/>', attenzioneMsg.toUpperCase(), function(r) {
		    
			 if (r) { 
			    	dojo.xhrGet({
			    		url: "${pageContext.request.contextPath}/libriScolasticiTrackingClient_showFilterTracking.action",	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=utf-8"}, 	
						handle: function(data,args) {
							
							if ($('#popup_name').is(':visible')) {
								if (closeLayerConfirm()) {
									$("#close").trigger('click');
								}
							}
							<s:url action="libriScolasticiTrackingClient_showTracking.action" var="urlTag"/>
							window.location = '<s:property value="#urlTag" />?numOrdineTxtTracking=' + numOrdineTxt;
							
						 
						}
					});
			    }
			 unBlockUI();
		});
	}
	
	function viewPageConsegnaAlCliente(numOrdineTxt) {

		jConfirm('<s:text name="dpe.ordine.msg.view.consegna.libri"/>', attenzioneMsg.toUpperCase(), function(r) {
		    
			 if (r) { 
			    	dojo.xhrGet({
			    		url: "${pageContext.request.contextPath}/libriScolasticiVendita_showFilter.action",	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=utf-8"}, 	
						handle: function(data,args) {
							
							if ($('#popup_name').is(':visible')) {
								if (closeLayerConfirm()) {
									$("#close").trigger('click');
								}
							}
							<s:url action="libriScolasticiVendita_showVendite.action" var="urlTag"/>
							window.location = '<s:property value="#urlTag" />?ordineFornitore=' + numOrdineTxt;
							
						 
						}
					});
			    }
			 unBlockUI();
		});
	}
	

	
	
</script>