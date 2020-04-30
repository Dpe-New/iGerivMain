<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");
		writeRichText('rte1', $("#testo").val(), 730, 300, false, true);
	});
	
</script>