<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();	
		<s:if test="menu.moduloPadre eq 1">
			$("#menu\\.moduloPadre").attr("checked", true);
			$("#menu\\.url").val("");
			$("#menu\\.url").attr("readonly", true);
		</s:if>
		<s:else>
			$("#menu\\.moduloPadre").attr("checked", false);
		</s:else>
		
		$("#menu\\.moduloPadre").change(function() {
			if ($(this).is(':checked')) {
				$("#menu\\.url").val("");
				$("#menu\\.url").attr("readonly", true);
			} else {
				$("#menu\\.url").val($("#urlHidden").val());
				$("#menu\\.url").attr("readonly", false);
			}
		});
		
		$('#menu\\.titolo').focus();
	});			
	
	function afterSuccessSave() {
		$("#ricerca").trigger("click");
	}		
</script>