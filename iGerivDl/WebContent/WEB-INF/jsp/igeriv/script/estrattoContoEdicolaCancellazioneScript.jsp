<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		$("input#edicolaLabel").focus();
	});
	
	$("input#edicolaLabel").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicole.action",
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
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {						
			document.getElementById('codEdicolaDl').value = ui.item.id;		
		}
	});	
	
	$.datepicker.setDefaults($.datepicker.regional['it']);
	$('#strDataEC').datepicker();
	
// 	$('#strDataEC').click(function() { 
//     	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 	});
	
	function checkFields() {
		if ($("#codEdicolaDl").val() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.specificare.rivendita"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			unBlockUI();
			return false;
		}
		if ($('#strDataEC').val() == '') {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="error.specificare.data"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			unBlockUI();
			return false;
		}
		
		return true;
	}
	
	function askForConfirm() {
		PlaySound('beep3');
		jConfirm("<s:text name='igeriv.cancellare.ec'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					submitForm('edicole_execDeleteEc.action');
				} else {
					unBlockUI();
					return false;
				}
			}
		);
	}
	
	function submitForm(actionName) {
		ray.ajax();
		document.getElementById("ECForm").action = actionName;
		document.getElementById("ECForm").submit();
	}
	
</script>