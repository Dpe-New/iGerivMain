<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		if ($("input[name=destinatario]:checked").length == 0) {
			$("#destinatario1").attr("checked","true");
		}
		$("#destinatarioSelected").val('<s:text name="destinatario"/>');
		<s:if test="%{disableForm eq true}">
			$("#destinatario1, #destinatario1, #destinatario2, #edicolaLabel, #attch1, #delete1").attr("disabled", true);
		</s:if>
		<s:else>
			$("input[name=destinatario]").change(function() {switchDestinaratariDiv()});
			switchDestinaratariDiv();
		</s:else>
		//Usage: writeRichText(fieldname, html, width, height, buttons, readOnly) 
		writeRichText('rte1', $("#message").val(), 600, 250, true, <s:text name="disableForm"/>);	
	});		 
	
	function checkFields(noAlerts) {
		if ($("#message").val() == '') {
			if (!noAlerts) {
				jAlert('<s:text name="igeriv.email.vuoto"/>');
			}
			return false;
		}
		switch(Number($("input[name='destinatario']:checked").val())) {
				case 1:
					if ($("#edicolaLabel").val() == '') {
						if (!noAlerts) {
							jAlert('<s:text name="igeriv.specificare.rivendita"/>');
						}
						return false;
					}
					break;
		}
		return true;	
	}
	
	function switchDestinaratariDiv() {
		switch(Number($("input[@name=destinatario]:checked").val())) {
			case 0:	
				$("#edicolaLabel").val('');
				$("#edicolaLabel").attr("disabled","true");
				break;
			case 1:
				$("#edicolaLabel").removeAttr("disabled");
				$("#edicolaLabel").focus();
				break;
		}
	}
	
	function afterSuccessSave() {						
		$("#ricerca").trigger('click');								
	}		
 
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val(document.forms['MessageForm'].rte1.value);
		return true;
	}
	
	$("input#edicolaLabel").autocomplete({	
		minLength: 3,			
		source: function(request, response) {	
			var strUrl = "${pageContext.request.contextPath}${ap}/automcomplete_edicoleWeb.action";
			dojo.xhrGet({
				url: strUrl,	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {											
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.label,
		                                    coddl: item.coddl
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {		
			$('#strCodEdicolaDl').val(ui.item.id);	
			$('#coddl').val(ui.item.coddl);
		}
	});	
	
	function doDelete() {
		PlaySound('beep3');
		if (window.confirm("<s:text name='gp.cancellare.dati'/>")) {
			return (setFormAction('MessageForm','msgDpeRivendite_deleteMessage.action', '', 'messageDiv'));
		} 
	}
</script>