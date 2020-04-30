<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strData').datepicker();
		
// 		$('#strData').click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
		
		$("#password").focus();
	});
	
	function onSaved() {
		jAlert(okMessage, infoMsg.toUpperCase(), function() {
			$("#ricerca").trigger('click');
			$("#close").trigger('click');
		});
	}
</script>