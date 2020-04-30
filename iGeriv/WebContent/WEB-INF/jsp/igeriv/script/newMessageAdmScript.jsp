<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/encoder.js"></script>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		<s:if test='messaggio.abilitato eq true'>
			$("#abilitato").attr("checked", true);
		</s:if>
		<s:else>
			$("#abilitato").attr("checked", false);	
		</s:else>
		writeRichText('rte1', $("#message").val(), 600, 250, true, false);
		setContentDivHeight(30); 
		$("#rte1").focus();
	});		 
	
	function afterSuccessSave() {	
		$("#ricerca").trigger('click');								
	}		
 
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val(document.forms['MessageForm'].rte1.value);
		return true;
	}
</script>