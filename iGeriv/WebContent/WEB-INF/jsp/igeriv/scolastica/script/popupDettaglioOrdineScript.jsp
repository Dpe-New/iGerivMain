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
				$("#confermaOrdine").hide();
				jConfirm('<s:text name="dpe.ordine.msg.conferma.ordine.intero"/>', attenzioneMsg.toUpperCase(), function(r) {  
					 if (r) { 
						 	ray.ajax();
					    	dojo.xhrGet({
					    		//url: "${pageContext.request.contextPath}/pubblicazioniRpc_restoreRitiriCliente.action?pk=dededed",	
					    		//url: "${pageContext.request.contextPath}/libriScolasticiClienti_confermaOrdine.action?idNumeroOrdine="+ idNumeroOrdine,	
					    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_confermaOrdineClesp.action?idNumeroOrdine="+ idNumeroOrdine,	
					    		preventCache: true,
								handleAs: "json",				
								headers: { "Content-Type": "application/json; charset=utf-8"}, 	
								handle: function(data,args) {
									unBlockUI();
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
					    }else {
					    	$("#confermaOrdine").show();
					    	unBlockUI();
					    }
					 
					 //unBlockUI();
				});
			}else{
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="dpe.ordine.msg.carrello.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		});
		
		
	});		

	
	
	function infoCopertinaLibro(sqOrdine) {
			var idNumeroOrdine = $("#idNumeroOrdine").val();
			ray.ajax();
    		dojo.xhrGet({
    			url: "${pageContext.request.contextPath}/pubblicazioniRpc_infoCopertinaLibro.action?idNumeroOrdine="+idNumeroOrdine+"&seq="+sqOrdine,	
    			handleAs: "json",				
    			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
    			preventCache: true,
    			handle: function(data,args) {
    				unBlockUI();
						if (args.xhr.status == 200) {
							for (i = 0; i < data.length; i++) {
								
								var primaRiga = data[i].primaRiga;
								var secondaRiga = data[i].secondaRiga;
								var terzaRiga = data[i].terzaRiga;
								var idLogo = data[i].idLogo;
								
								var urlimg = "<img src=\"/app_img/scolasticaLoghi/"+idLogo+".jpg\" width=\"150px\" height=\"57px\" />";
								//var msginfo = "Nome Alunno: "+primaRiga+"\nCognome Alunno: "+secondaRiga+"\nClasse: "+terzaRiga+"\n\n"+urlimg;
								var msginfo = "Nome Alunno: "+primaRiga+"\nCognome Alunno: "+secondaRiga+"\nClasse: "+terzaRiga+"\n\n";

								$.alerts.dialogClass = "style_1";
								jAlert(msginfo, infoCopertina.toUpperCase(), function() {
									$.alerts.dialogClass = null;
									return false;
								});

							}
						}else {
							$.alerts.dialogClass = "style_1";
							jAlert("<s:text name='msg.errore.info.copertina.libro'/>", attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								return false;
							});
						} 
    				
    				
    				}
    		 	});
		}
	
	function modificaCopertinaLibro(sqOrdine) {
			var idNumeroOrdine = $("#idNumeroOrdine").val();
			
			ray.ajax();
    		dojo.xhrGet({
    			url: "${pageContext.request.contextPath}/pubblicazioniRpc_modificaCopertinaLibro.action?idNumeroOrdine="+idNumeroOrdine+"&seq="+sqOrdine,	
    			handleAs: "json",				
    			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
    			preventCache: true,
    			handle: function(data,args) {
    				unBlockUI();
						if (args.xhr.status == 200) {
							for (i = 0; i < data.length; i++) {
								
								var seqOrdine = data[i].seqOrdine;
								var isbn_barcode = data[i].isbn_barcode;
								var flgCopertina = data[i].flgCopertina;
								var primaRiga = data[i].primaRiga;
								var secondaRiga = data[i].secondaRiga;
								var terzaRiga = data[i].terzaRiga;
								var idLogo = data[i].idLogo;
								var sku = data[i].sku;
								
								if ($('#popup_name').is(':visible')) {
									if (closeLayerConfirm()) {

										//var url = "${pageContext.request.contextPath}/libriScolasticiClienti_showDettaglioLibro.action?sku="
										var url = "${pageContext.request.contextPath}/libriScolasticiClienti_showDettaglioLibroClesp.action?sku="+sku+"&idNumeroOrdine="+idNumeroOrdine+"&seqOrdine="+seqOrdine;
										openDiv("popup_name", 900, 620, url, '', '', '');
										
									}
								}	
								
							}
						}else {
							$.alerts.dialogClass = "style_1";
							jAlert("<s:text name='msg.errore.info.modifica.libro'/>", attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								return false;
							});
						} 
    				
    				
    				}
    		 	});
		}
	
	
	function deleteLibroDalCarrello(sqOrdine,sku) {
		var idNumeroOrdine = $("#idNumeroOrdine").val();
		
		jConfirm('<s:text name="dpe.ordine.msg.cancella.ordine.libro"/>', attenzioneMsg.toUpperCase(), function(r) {
			
			 if (r) { 
				 	ray.ajax();
			    	dojo.xhrGet({
			    		url: "${pageContext.request.contextPath}/libriScolasticiClienti_deleteLibroDalCarrello.action?idNumeroOrdine="+idNumeroOrdine+"&seq="+sqOrdine+"&sku="+sku,	
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