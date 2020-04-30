<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		<s:if test="hasActionErrors()">
			var msg = '';
			<s:iterator value="actionErrors">
				msg += '<s:property escape="false" />';
			</s:iterator>
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
	});
</script>