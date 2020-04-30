<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script> 
	function onLoadFunction() {
		document.getElementById('autocomplete').focus();
		$('#strData').click(function() {
	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});
	}
	
	$("input#autocomplete").keydown(setCodEdicola);
	
	function setCodEdicola(event) { 
		var keycode = (event.keyCode ? event.keyCode : event.charCode);
		if (keycode == '13' && $("input#autocomplete").val().length > 0 && !isNaN($("#autocomplete").val())) {
			$("#codEdicola").val($("input#autocomplete").val());
			return true;
		} 
	}
	
	function doSubmit() { 
		if ($("#autocomplete").val() == '') {
			document.getElementById('codEdicola').value = '';
		} else if (!isNaN($("#autocomplete").val())) {
			document.getElementById('codEdicola').value = $("#autocomplete").val();
		}
		return true;
	};
	
	$("input#autocomplete").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicoleByCrivDl.action",	
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
			document.getElementById('codEdicola').value = ui.item.id;
		}
	});	
</script>
<s:if test="%{#request.bolleRiassunto != null && !#request.bolleRiassunto.isEmpty()}">
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
	});
	
	$('#ManutenzioneBollaConsegnaForm select[name^=stato]').change(function() {	
		$(this).attr('isChanged', 'true');					
		$(this).closest('tr').find('input:hidden[name^=pk]').attr('isChanged', 'true');		
	});
	
	function setFieldsToSave(isSubmit) {
		$('#ManutenzioneBollaConsegnaForm select[name^=stato],input:hidden[name^=pk]').each(function() {		
			if (isSubmit) {
				if ($(this).attr('isChanged') != 'true') {		
					$(this).attr('disabled', true);
				}
			} else {
				$(this).attr('disabled', false);
				$(this).removeAttr('isChanged')
			}		
		});
		return true;
	}
</script>

</s:if>


