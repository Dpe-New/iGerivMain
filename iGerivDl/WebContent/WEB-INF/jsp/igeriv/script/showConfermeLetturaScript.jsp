<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {			
		addFadeLayerEvents();
		<s:if test="#request.conferme.size() == 1">
			addTableLastRow("ShowConfermeLetturaTab_table_table");
		</s:if>
		<s:else>
			$("#ShowConfermeLetturaTab_table_table").tablesorter();
		</s:else>
	});
</script>