<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {
		setLastFocusedElement();
		addFadeLayerEvents();
		$('#attachment1').focus();
		$('#attachment1').trigger("click");
	});
	
	function doValidationImgMiniatura() {
		var separator = $("#attachment1").val().indexOf("/") != -1 ? "/" : "\\";
		var imgProd = $("#attachment1").val().substring( $("#attachment1").val().lastIndexOf(separator) + 1);
		if (imgProd != '' && !hasOnlyNonAccentCharacters(imgProd)) {
			$.alerts.dialogClass = "style_1";
			jAlert(nomeFileInvalido.replace('{0}',imgProd), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				unBlockUI();
				$('#attachment1').focus();
			});
			return false; 
		} 
		return true;
	}
</script>