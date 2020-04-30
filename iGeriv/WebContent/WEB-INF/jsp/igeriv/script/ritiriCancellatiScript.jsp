<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		addTableLastRow("RitiriCancTab_table");
		thumbnailviewer.init();
		restoreRitiri = function() {
			var pkArr = new Array();
			$("#RitiriCancForm input:checkbox[name='pk']").each(function() {
				if ($(this).attr("checked") == true) {   	     	  		  
					pkArr.push($(this).val());
				}
			});
			if (pkArr.length > 0) {
				jConfirm('<s:text name="gp.vuoi.ripristinare.ritiri.selezionati"/>', attenzioneMsg, function(r) {
				    if (r) { 
				    	dojo.xhrGet({
							url: "${pageContext.request.contextPath}/pubblicazioniRpc_restoreRitiriCliente.action?pk=" + pkArr.toString(),	
							preventCache: true,
							handleAs: "json",				
							headers: { "Content-Type": "application/json; charset=utf-8"}, 	
							handle: function(data,args) {
								unBlockUI();
								if (args.xhr.status == 200) {
									var result = data.result;
									if (typeof(result) != 'undefined' && result != '') {
										jAlert(result, informazioneMsg, function() {
											$("#ricerca").trigger("click");
								    		return false;
										});
									} else {
										$.alerts.dialogClass = "style_1";
										jAlert("<s:text name='msg.errore.invio.richiesta'/>", attenzioneMsg.toUpperCase(), function() {
											$.alerts.dialogClass = null;
											return false;
										});
									}
								} else {
									$.alerts.dialogClass = "style_1";
									jAlert("<s:text name='msg.errore.invio.richiesta'/>", attenzioneMsg.toUpperCase(), function() {
										$.alerts.dialogClass = null;
										return false;
									});
								} 
							}
						});
				    }
				    $("#inputText").focus();
				}, true, false);
			}
		}
	});
	
</script>
