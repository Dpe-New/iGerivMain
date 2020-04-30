<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript">
	
	//var deleted = false;
	$(document).ready(function() {	
		addFadeLayerEvents();	
		
		$("#eliminaOrdine").click(function() {
			var idNumeroOrdine = $("#idNumeroOrdine").val();
			jConfirm('<s:text name="dpe.ordine.msg.cancella.ordine.intero"/>', attenzioneMsg.toUpperCase(), function(r) {
			    
				 if (r) { 
				    	dojo.xhrGet({
				    		//url: "${pageContext.request.contextPath}/pubblicazioniRpc_restoreRitiriCliente.action?pk=dededed",	
				    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_deleteOrdineIntero.action?idNumeroOrdine="+ idNumeroOrdine,	
							preventCache: true,
							handleAs: "json",				
							headers: { "Content-Type": "application/json; charset=utf-8"}, 	
							handle: function(data,args) {
								
								
								if ($('#popup_name').is(':visible')) {
									if (closeLayerConfirm()) {
										$("#close").trigger('click');
									}
								}
								<s:url action="libriScolasticiClienti_showOrdiniFilter.action" var="urlTag"/>
								window.location = '<s:property value="#urlTag" />';
								
								
								
							}
						});
				    }
				 unBlockUI();
			});
		});
		
		
		
		$("#confermaOrdine").click(function() {
			var idNumeroOrdine = $("#idNumeroOrdine").val();
			
			var countRow = $('#LibriScolasticiOrdineTab_table tr').length;
			if(countRow >1){
				jConfirm('<s:text name="dpe.ordine.msg.conferma.ordine.intero"/>', attenzioneMsg.toUpperCase(), function(r) {  
					 if (r) { 
					    	dojo.xhrGet({
					    		//url: "${pageContext.request.contextPath}/pubblicazioniRpc_restoreRitiriCliente.action?pk=dededed",	
					    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_confermaOrdine.action?idNumeroOrdine="+ idNumeroOrdine,	
								preventCache: true,
								handleAs: "json",				
								headers: { "Content-Type": "application/json; charset=utf-8"}, 	
								handle: function(data,args) {
									
									if ($('#popup_name').is(':visible')) {
										if (closeLayerConfirm()) {
											$("#close").trigger('click');
											
											var popID = 'popup_name';	   	     		    	  
										    var popWidth = 900;
										    var popHeight = 550;
										 	var url = "${pageContext.request.contextPath}/libriScolasticiClienti_confermaOrdineRiepilogo.action?idNumeroOrdine="+ idNumeroOrdine;
											openDiv(popID, popWidth, popHeight, url);
											
											
										}
									}
									//<s:url action="libriScolasticiClienti_showOrdiniFilter.action" var="urlTag"/>
									//window.location = '<s:property value="#urlTag" />';
								}
							});
					    }
					 unBlockUI();
				});
			}else{
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="dpe.ordine.msg.carrello.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		});
		
		
	});		

	
	function deleteLibroDalCarrello(sku) {
		var idNumeroOrdine = $("#idNumeroOrdine").val();
		jConfirm('<s:text name="dpe.ordine.msg.cancella.ordine.libro"/>', attenzioneMsg.toUpperCase(), function(r) {
		    
			 if (r) { 
			    	dojo.xhrGet({
			    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_deleteLibroDalCarrello.action?idNumeroOrdine="+idNumeroOrdine+"&sku="+sku,	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=utf-8"}, 	
						handle: function(data,args) {
							
							if ($('#popup_name').is(':visible')) {
								if (closeLayerConfirm()) {
									$('#testoRicerca').val("");
									$('#ricerca').trigger('click');	 
								}
							}	
						 
						}
					});
			    }
			 unBlockUI();
		});
	}
	
	
	
	
	function closeLayerConfirm() {
		return true;
	}
	
	
</script>