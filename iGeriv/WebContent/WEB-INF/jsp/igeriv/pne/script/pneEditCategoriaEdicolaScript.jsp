<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var context = window.parent.document;
	$(document).ready(function() {
		addFadeLayerEvents(context);
		if ($("#codCategoria", context).val() < 10000) {
			$("#descrizione", context).attr("disabled", true);
		} else {
			$("#descrizione", context).focus();
			$("#descrizione", context).bind('keypress', function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.charCode);  
				if (keycode == 13) {
					event.preventDefault();
					$("#mem", context).trigger("click");
				}    
			});
		}
	});
</script>