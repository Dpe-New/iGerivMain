<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataIniziale').datepicker();
		$('#dataFinale').datepicker();
		
// 		$('#dataIniziale').click(function() { 
// 	        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataFinale').click(function() { 
// 	        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$("input#edicolaLabel").autocomplete({	
			minLength: 3,			
			source: function(request, response) {		
				dojo.xhrGet({
					url: "${pageContext.request.contextPath}${ap}/automcomplete_edicoleByCrivDl.action",
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					content: { 
				    	term: request.term
				    }, 					
					handle: function(data,args) {
						response($.map(data, function(item) {											
			                                return {
			                                    id: item.id,	
			                                    label: item.label,
			                                    value: item.label		                                   
			                                }
			                            }))
					}
				});
			},
			select: function (event, ui) {						
				document.getElementById('codRivendita').value = ui.item.id;		
			}
		});
	});
	
	function doSubmit() { 
		if ($("#edicolaLabel").val() == '') {
			document.getElementById('codRivendita').value = '';
		} else if (!isNaN($("#edicolaLabel").val())) {
			document.getElementById('codRivendita').value = $("#edicolaLabel").val();
		}
		return true;
	};
	
	function validateFieldsStatisticaUtilizzo(showAlerts) {
		if ($("#dataIniziale").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="igeriv.da"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			return false;
		} else if (!checkDate($("#dataIniziale").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="msg.formato.data.invalido"/>'.replace('{0}', $("#dataIniziale").val()), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			return false;
		}
		if ($("#dataFinale").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="igeriv.a"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			return false;
		} else if (!checkDate($("#dataFinale").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="msg.formato.data.invalido"/>'.replace('{0}', $("#dataFinale").val()), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			return false;
		}
		return true;
	}
</script>