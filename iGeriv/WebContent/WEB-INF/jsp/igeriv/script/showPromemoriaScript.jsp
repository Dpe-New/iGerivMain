<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>	
	$(document).ready(function() {
		addFadeLayerEvents();	
	});	
	
	$('#letto').click(function() {		
		if ($('#letto').attr('checked') == true) {
			dojo.xhrGet({
				url: appContext + namespace + "/pubblicazioniRpc_updatePromemoriaLetto.action?codPromemoria=" + $("#codProm").val(),	
				preventCache: true,
				handleAs: "json",				
				handle: function(data,args) {
					if (args.xhr.status == 200) {
	    				if (typeof(data.error) !== 'undefined' && data.error != '') {
		    				$.alerts.dialogClass = "style_1";
		    				jAlert(data.error, attenzioneMsg, function() {
		    					$.alerts.dialogClass = null;
		    					return false;
		    				});
	    				} else {
	    					unBlockUI();
	    					showPromemoria();
	    					$("#close").trigger('click');
	    				}
					} else {
						jAlert(msgErroreInvioRichiesta);
					}
				}
			});
		}
	});		
</script>

