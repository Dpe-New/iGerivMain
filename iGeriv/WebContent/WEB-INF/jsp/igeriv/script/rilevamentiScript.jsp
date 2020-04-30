<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var memorizzaRilevamentiConfirm = '<s:text name="igeriv.conferm.rilevamenti"/>';
	$(document).ready(function() {
		$("input:text[class='extremeTableFieldsSmaller']").numeric({ decimal : false, negative : false });
	});
</script>
