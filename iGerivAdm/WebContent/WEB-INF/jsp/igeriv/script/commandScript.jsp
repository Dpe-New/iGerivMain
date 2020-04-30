<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		$("#esegui").click(function() {
			if ($("#codDL").val() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.selezionare.dl"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
				return false;
			}
			ray.ajax();
			dojo.xhrPost({
				form: "CommandForm",			
				handleAs: "text",		
				handle: function(data,args) {
					unBlockUI();
					var $doc = $(data);
					var hasError = $("igerivException", $doc).length > 0;
					if (args.xhr.status != 200 || hasError) {
						$.alerts.dialogClass = "style_1";
						jAlert($("igerivException", $doc).text(), attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
						});
					} else {
						jAlert('<s:text name="igeriv.procedura.eseguito.con.suceesso"/>', avvisoMsg);
					}
				}		
			});
		});
	});
</script>		    