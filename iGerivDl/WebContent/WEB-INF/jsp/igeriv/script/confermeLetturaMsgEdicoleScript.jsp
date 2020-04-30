<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		<s:if test="#request.message neq ''">
			jAlert("<s:text name='#request.message'/>", avvisoMsg);
		</s:if>
	});
</script>